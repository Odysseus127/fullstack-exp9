package com.example.ci_pipeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ci_pipeline.product.Product;
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
