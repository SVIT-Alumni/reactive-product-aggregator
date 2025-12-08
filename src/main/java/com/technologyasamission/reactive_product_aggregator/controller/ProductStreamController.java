package com.technologyasamission.reactive_product_aggregator.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technologyasamission.reactive_product_aggregator.model.Product;
import com.technologyasamission.reactive_product_aggregator.service.ProductStreamService;

import reactor.core.publisher.Flux;

@RestController
public class ProductStreamController {

    private final ProductStreamService streamService;

    public ProductStreamController(ProductStreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping(value = "/stream/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> streamProducts() {
        return streamService.streamProducts();
    }
}
