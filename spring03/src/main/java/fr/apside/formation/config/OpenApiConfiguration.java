package fr.apside.formation.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenApiCustomizer infoCustomiser() {
        return openApi -> {
            Info info = new Info()
                    .title("Spring Boot Employees application")
                    .description("Backend application")
                    .version("");
            openApi.setInfo(info);
        };
    }
}
