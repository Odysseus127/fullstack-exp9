package com.example.ci_pipeline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.example.ci_pipeline.product.Product;
import com.example.ci_pipeline.repository.ProductRepository;

@Provider("ProductService")
@PactFolder("src/test/resources/pacts")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.password=password")
@Import(TestcontainersConfiguration.class)
@ExtendWith(PactVerificationSpringProvider.class)
class ProductContractVerificationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void configureTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
        product1Exists();
    }

    @TestTemplate
    void verifyPactInteraction(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("product 1 exists")
    public void product1Exists() {
        jdbcTemplate.execute("TRUNCATE TABLE product");
        productRepository.saveAndFlush(new Product("Keyboard", 49.99));
    }
}
