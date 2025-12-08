package com.technologyasamission.reactive_product_aggregator.service;

import reactor.core.publisher.Mono;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryReactiveCache {

    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    public Mono<String> get(String key) {
        return Mono.justOrEmpty(store.get(key));
    }

    public Mono<Void> set(String key, String value) {
        store.put(key, value);
        return Mono.empty();
    }
}
