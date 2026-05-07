package com.ecommerce.product.dto;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private double price;
    private String category;
}
