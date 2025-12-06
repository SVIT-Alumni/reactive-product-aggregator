package com.technologyasaservice.reactive_product_aggregator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private String id;
    private String title;
    private double price;
}
