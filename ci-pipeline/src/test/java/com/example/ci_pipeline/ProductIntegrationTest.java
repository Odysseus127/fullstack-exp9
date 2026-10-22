package com.example.ci_pipeline;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(properties = "spring.datasource.password=password")
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProductThenRetrieveIt() throws Exception {
        MvcResult creationResult = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Integration keyboard","price":49.99}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is("Integration keyboard")))
                .andExpect(jsonPath("$.price", is(49.99)))
                .andReturn();

        String id = com.jayway.jsonpath.JsonPath.read(
                creationResult.getResponse().getContentAsString(), "$.id").toString();
        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name", is("Integration keyboard")))
                .andExpect(jsonPath("$.price", is(49.99)));
    }

    @Test
    void rejectProductWithBlankName() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"  ","price":10.0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Invalid product")));
    }

    @Test
    void returnNotFoundForUnknownProduct() throws Exception {
        mockMvc.perform(get("/api/products/9223372036854775807"))
                .andExpect(status().isNotFound());
    }
}
