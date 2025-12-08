package com.technologyasaservice.reactive_product_aggregator.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import com.technologyasaservice.reactive_product_aggregator.client.ProductClient;
import com.technologyasaservice.reactive_product_aggregator.model.Product;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductClient client;
    private final ReactiveRedisTemplate<String, String> redis;

    public ProductService(ProductClient client, ReactiveRedisTemplate<String, String> redis) {
        this.client = client;
        this.redis = redis;
    }

    public Mono<Product> getProduct(String id) {

        String redisKey = "product:" + id;

        // 1. Try to fetch from the cache
        Mono<Product> cached = redis.opsForValue()
                .get(redisKey)
                .map(json -> JsonUtils.fromJson(json, Product.class));

        // 2. If not in the cache, call external APIs
        Mono<Product> fetched = Mono.zip(
                client.getDetails(id),
                client.getPrice(id)
        ).map(tuple -> new Product(
                tuple.getT1().getId(),
                tuple.getT1().getTitle(),
                tuple.getT2().getPrice()
        ))
        .doOnSuccess(product ->
                redis.opsForValue()
                        .set(redisKey, JsonUtils.toJson(product))
                        .subscribe()
        );

        return cached.switchIfEmpty(fetched);
    }
}
