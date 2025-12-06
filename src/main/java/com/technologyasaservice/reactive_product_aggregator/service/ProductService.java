package com.technologyasaservice.reactive_product_aggregator.service;

import com.technologyasaservice.reactive_product_aggregator.client.ProductClient;
import com.technologyasaservice.reactive_product_aggregator.model.Product;
import com.technologyasaservice.reactive_product_aggregator.model.ProductDetails;
import com.technologyasaservice.reactive_product_aggregator.model.ProductPrice;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductClient client;

    public ProductService(ProductClient client) {
        this.client = client;
    }

    public Mono<Product> getProduct(String id) {
        Mono<ProductDetails> detailsMono = client.getDetails(id);
        Mono<ProductPrice> priceMono = client.getPrice(id);

        return Mono.zip(detailsMono, priceMono)
                .map(tuple -> {
                    ProductDetails details = tuple.getT1();
                    ProductPrice price = tuple.getT2();

                    return new Product(
                            details.getId(),
                            details.getTitle(),
                            price.getPrice()
                    );
                });
    }
}
