package com.technologyasamission.reactive_product_aggregator.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import com.technologyasamission.reactive_product_aggregator.client.ProductClient;
import com.technologyasamission.reactive_product_aggregator.model.Product;

import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductClient client;
    private final ReactiveRedisTemplate<String, String> redis;
    private final InMemoryReactiveCache memoryCache = new InMemoryReactiveCache();
    private final FallbackCacheService fallbackCache;

    public ProductService(ProductClient client,
            ReactiveRedisTemplate<String, String> redis,
            FallbackCacheService fallbackCache) {
        this.client = client;
        this.redis = redis;
        this.fallbackCache = fallbackCache;
    }

    private Mono<String> getCache(String key) {
        if (redis == null) {
            return memoryCache.get(key);
        }
        return redis.opsForValue().get(key);
    }

    private Mono<Void> setCache(String key, String value) {
        if (redis == null) {
            return memoryCache.set(key, value);
        }
        return redis.opsForValue().set(key, value).then();
    }

    public Mono<Product> getProduct(String id) {

        String redisKey = "product:" + id;

        // Read-through cache strategy
        Mono<Product> cached = getCache(redisKey)
                .map(json -> JsonUtils.fromJson(json, Product.class));

        // Load from external APIs and hydrate caches
        Mono<Product> fetched = Mono.zip(
                client.getDetails(id),
                client.getPrice(id))
                .map(tuple -> new Product(
                        tuple.getT1().getId(),
                        tuple.getT1().getTitle(),
                        tuple.getT2().getPrice()))
                .flatMap(product ->
                // Write-through semantics, but non-blocking
                setCache(redisKey, JsonUtils.toJson(product))
                        .then(fallbackCache.saveFallbackProduct(id, product))
                        .thenReturn(product))
                .onErrorResume(error -> {
                    // If external services fail, fall back to last known good state
                    return fallbackCache.getFallbackProduct(id);
                });

        return cached
                // Cache miss routes to source-of-truth fetch
                .switchIfEmpty(fetched)
                // Last-chance fallback: avoid propagating failures to user-facing flows
                .switchIfEmpty(fallbackCache.getFallbackProduct(id));
    }
}
