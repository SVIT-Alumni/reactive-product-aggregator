package com.technologyasamission.reactive_product_aggregator.service;

import org.springframework.stereotype.Service;
import com.technologyasamission.reactive_product_aggregator.model.PriceChange;
import com.technologyasamission.reactive_product_aggregator.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceChangeService {

    private final ProductStreamService productStreamService;
    private final KinesisProducer producer;

    // Local state tracks the last observed prices for change detection.
    // ConcurrentHashMap enables thread-safe updates in reactive pipelines.
    private final Map<String, Double> lastPrices = new ConcurrentHashMap<>();

    public PriceChangeService(ProductStreamService productStreamService, KinesisProducer producer) {
        this.productStreamService = productStreamService;
        this.producer = producer;
    }

    public Flux<PriceChange> streamPriceChanges() {
        return productStreamService.streamProducts()
                .flatMap(this::detectChange);
    }

    private Mono<PriceChange> detectChange(Product product) {
        String id = product.getId();
        double currentPrice = product.getPrice();

        Double previousPrice = lastPrices.put(id, currentPrice);

        // Early exit avoids unnecessary allocations and keeps the hot path fast.
        if (previousPrice == null || Double.compare(previousPrice, currentPrice) == 0) {
            return Mono.empty();
        }

        PriceChange change = new PriceChange(
                id,
                previousPrice,
                currentPrice,
                Instant.now().toString()
        );

        // Emit event to Kinesis without coupling upstream latency.
        // Non-blocking publish ensures the pipeline remains responsive under load.
        return producer.sendPriceChange(change)
                .thenReturn(change);
    }
}
