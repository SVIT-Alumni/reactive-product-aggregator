package com.technologyasaservice.reactive_product_aggregator.client;

import com.technologyasaservice.reactive_product_aggregator.model.ProductDetails;
import com.technologyasaservice.reactive_product_aggregator.model.ProductPrice;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class ProductClient {

    private final WebClient webClient;

    public ProductClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://fakestoreapi.com")
                .build();
    }

    public Mono<ProductDetails> getDetails(String id) {
        return webClient.get()
                .uri("/products/" + id)
                .retrieve()
                .bodyToMono(ProductDetails.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(200))
                                .maxBackoff(Duration.ofSeconds(2))
                )
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<ProductPrice> getPrice(String id) {
        return WebClient.create("https://dummyjson.com")
                .get()
                .uri("/products/" + id)
                .retrieve()
                .bodyToMono(ProductPrice.class)
                .onErrorResume(e -> Mono.just(new ProductPrice(id, 0.0)));
    }
}