package com.technologyasaservice.reactive_product_aggregator.service;

import com.technologyasaservice.reactive_product_aggregator.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
public class ProductStreamService {

    private final ProductService productService;
    private final Random random = new Random();

    // List of products to be sent reactively
    private final List<String> productIds = List.of("1", "2", "3", "4", "5");

    public ProductStreamService(ProductService productService) {
        this.productService = productService;
    }

    public Flux<Product> streamProducts() {
        return Flux.interval(Duration.ofSeconds(2))
                .flatMap(tick -> {
                    String id = pickRandomProduct();
                    return productService.getProduct(id)
                            .map(product -> {
                                double variation = (random.nextDouble() - 0.5) * 10.0;
                                double newPrice = Math.max(1.0, product.getPrice() + variation);
                                product.setPrice(newPrice);
                                return product;
                            });
                });
    }

    private String pickRandomProduct() {
        return productIds.get(random.nextInt(productIds.size()));
    }
}
