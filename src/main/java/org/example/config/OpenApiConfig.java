package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${app.version}")
    private String version;

    @Value("${spring.application.name:nome}")
    private String appName;

    //http://localhost:8081/swagger-ui/index.html
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info().title(appName)
                        .description("API service in Swagger framework")
                        .version(version)
                );
    }
}