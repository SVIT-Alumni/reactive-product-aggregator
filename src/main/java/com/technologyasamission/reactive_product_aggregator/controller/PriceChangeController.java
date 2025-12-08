package com.technologyasaservice.reactive_product_aggregator.controller;

import com.technologyasaservice.reactive_product_aggregator.model.PriceChange;
import com.technologyasaservice.reactive_product_aggregator.service.PriceChangeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class PriceChangeController {

    private final PriceChangeService priceChangeService;

    public PriceChangeController(PriceChangeService priceChangeService) {
        this.priceChangeService = priceChangeService;
    }

    @GetMapping(value = "/stream/price-changes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PriceChange> streamPriceChanges() {
        return priceChangeService.streamPriceChanges();
    }
}
