package com.technologyasamission.reactive_product_aggregator.controller;

import org.springframework.web.bind.annotation.*;

import com.technologyasamission.reactive_product_aggregator.model.Product;
import com.technologyasamission.reactive_product_aggregator.service.ProductService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable String id) {
        return service.getProduct(id);
    }
}
