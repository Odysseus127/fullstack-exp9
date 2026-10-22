package com.example.ci_pipeline;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ci_pipeline.product.Product;
import com.example.ci_pipeline.repository.ProductRepository;
import com.example.ci_pipeline.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createSavesAndReturnsProduct() {
        Product product = new Product("Keyboard", 49.99);
        Product savedProduct = new Product("Keyboard", 49.99);
        savedProduct.setId(1L);
        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.create(product);

        assertSame(savedProduct, result);
        verify(productRepository).save(product);
    }

    @Test
    void findByIdReturnsProductWhenItExists() {
        Product product = new Product("Keyboard", 49.99);
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertSame(product, result.orElseThrow());
        assertEquals(1L, result.orElseThrow().getId());
        verify(productRepository).findById(1L);
    }

    @Test
    void findByIdReturnsEmptyWhenProductDoesNotExist() {
        when(productRepository.findById(404L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.findById(404L);

        assertTrue(result.isEmpty());
        verify(productRepository).findById(404L);
    }
}
