package com.technologyasaservice.reactive_product_aggregator.service;

import com.technologyasaservice.reactive_product_aggregator.model.PriceChange;
import com.technologyasaservice.reactive_product_aggregator.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceChangeService {

    private final ProductStreamService productStreamService;
    private final Map<String, Double> lastPrices = new ConcurrentHashMap<>();

    public PriceChangeService(ProductStreamService productStreamService) {
        this.productStreamService = productStreamService;
    }

    public Flux<PriceChange> streamPriceChanges() {
        return productStreamService.streamProducts()
                .flatMap(this::detectChange);
    }

    private Mono<PriceChange> detectChange(Product product) {
        String id = product.getId();
        double currentPrice = product.getPrice();

        Double previousPrice = lastPrices.put(id, currentPrice);

        if (previousPrice == null || Double.compare(previousPrice, currentPrice) == 0) {
            return Mono.empty();
        }

        PriceChange change = new PriceChange(
                id,
                previousPrice,
                currentPrice,
                Instant.now().toString()
        );

        return Mono.just(change);
    }
}
