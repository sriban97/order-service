package com.green.orderservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl("https://www.green.com/order-service");
        devServer.setDescription("Order Service");

        Contact contact = new Contact();
        contact.setEmail("green@gmail.com");
        contact.setName("Green");
        contact.setUrl("https://www.green.com");

        License mitLicense = new License().name("MIT License").url("https://green.com/licenses/mit/");

        Info info = new Info()
                .title("Green Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage green")
                .termsOfService("https://www.green.com/terms")
                .license(mitLicense);
        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}