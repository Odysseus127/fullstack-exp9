package com.example.ci_pipeline;

import org.springframework.boot.SpringApplication;

public class TestCiPipelineApplication {

	public static void main(String[] args) {
		SpringApplication.from(CiPipelineApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
