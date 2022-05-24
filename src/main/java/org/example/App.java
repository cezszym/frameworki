package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "EasyHotel", description = "Internet service that allows the rental of premises from private persons. API resources are accessed via JWT. For a better search for premises, the app has been equipped with a search engine."))
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
