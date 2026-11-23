package com.isigntech.rightpath.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI rightPathOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("RightPath API")
						.description("API documentation for the RightPath recruitment platform.")
						.version("v1"))
				.schemaRequirement("bearerAuth", new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
						.description("Enter a JWT access token."));
	}
}
