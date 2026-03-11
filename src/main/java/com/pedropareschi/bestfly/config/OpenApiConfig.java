package com.pedropareschi.bestfly.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.web.filter.ForwardedHeaderFilter;
import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Best Fly API",
                version = "v1",
                description = "API for authentication, flight search, and management of users, favorite flights, and search history."
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
        @Value("${app.openapi.server-url:https://bestflyapi.duckdns.org}")
        private String openapiServerUrl;

        @Bean
        public OpenAPI customOpenAPI() {
                OpenAPI openAPI = new OpenAPI();
                Server server = new Server();
                server.setUrl(openapiServerUrl);
                openAPI.setServers(List.of(server));
                return openAPI;
        }

        @Bean
        public org.springframework.boot.web.servlet.FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
                ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
                org.springframework.boot.web.servlet.FilterRegistrationBean<ForwardedHeaderFilter> registration = new org.springframework.boot.web.servlet.FilterRegistrationBean<>(filter);
                registration.setOrder(org.springframework.core.Ordered.HIGHEST_PRECEDENCE);
                return registration;
        }
}
