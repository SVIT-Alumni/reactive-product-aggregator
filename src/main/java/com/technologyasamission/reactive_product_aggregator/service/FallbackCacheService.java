package com.technologyasamission.reactive_product_aggregator.service;

import com.technologyasamission.reactive_product_aggregator.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in memory fallback cache used only when Redis is down.
 * This keeps the application resilient and avoids 500 errors.
 */
@Component
public class FallbackCacheService {

    private final ConcurrentHashMap<String, Product> store = new ConcurrentHashMap<>();

    public Mono<Product> getFallbackProduct(String id) {
        return Mono.justOrEmpty(store.get(id));
    }

    public Mono<Void> saveFallbackProduct(String id, Product product) {
        store.put(id, product);
        return Mono.empty();
    }
}
