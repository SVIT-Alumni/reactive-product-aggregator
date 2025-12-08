package com.technologyasamission.reactive_product_aggregator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceChange {
    private String productId;
    private double oldPrice;
    private double newPrice;
    private String changedAt;
}
