package com.github.alideweb.stuffshop.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Stuff-Shop API Documentation",
                description = "Please give a star to the project in the github.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Ali Moradi",
                        url = "https://github.com/alideweb"
                )
        )
)
public class OpenAPIConfigs {
}
