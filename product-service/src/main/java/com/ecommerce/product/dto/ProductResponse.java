package com.ecommerce.product.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
