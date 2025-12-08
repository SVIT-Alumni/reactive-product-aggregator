package com.technologyasaservice.reactive_product_aggregator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice {
    private String id;
    private double price;
}