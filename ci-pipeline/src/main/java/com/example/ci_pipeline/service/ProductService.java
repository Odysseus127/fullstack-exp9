package com.example.ci_pipeline.service;
import org.springframework.stereotype.Service;

import com.example.ci_pipeline.product.Product;
import com.example.ci_pipeline.repository.ProductRepository;

import java.util.Optional;

@Service 
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}
