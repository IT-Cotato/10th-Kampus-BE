package com.cotato.kampus.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(
	info = @Info(title = "Kampus 프로젝트 API 명세서",
		description = "Kampus 서버 API 명세서",
		version = "v1")
)
@Configuration
public class SwaggerConfig {

	private static final String ACCESS_HEADER_NAME = "Authorization";
	private static final String REFRESH_HEADER_NAME = "Refresh-Token";

	@Bean
	public OpenAPI customOpenAPI() {


		// Define the SecurityRequirement to be included in the request
		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList(ACCESS_HEADER_NAME)
			.addList(REFRESH_HEADER_NAME);

		Components components = new Components()
			.addSecuritySchemes(ACCESS_HEADER_NAME, new SecurityScheme()
				.name(ACCESS_HEADER_NAME)
				.type(SecurityScheme.Type.APIKEY)
				.in(SecurityScheme.In.HEADER)
				.name(ACCESS_HEADER_NAME))
			.addSecuritySchemes(REFRESH_HEADER_NAME, new SecurityScheme()
				.name(REFRESH_HEADER_NAME)
				.type(SecurityScheme.Type.APIKEY)
				.in(SecurityScheme.In.HEADER)
				.name(REFRESH_HEADER_NAME));

		return new OpenAPI()
			.addServersItem(new Server().url("https://kampus.kro.kr").description("배포 서버"))
			.addServersItem(new Server().url("http://localhost:8080").description("Local Server"))
			.addServersItem(new Server().url("http://54.180.123.60:8080").description("Ec2 http 직접 접근(배포 서버 안될 때 사용)"))
			.addSecurityItem(securityRequirement)
			.components(components);
	}
}
