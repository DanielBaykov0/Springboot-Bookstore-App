package baykov.daniel.springbootbookstoreapp;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@OpenAPIDefinition(info = @Info(
		title = "Spring Boot Bookstore App REST APIs",
		description = "Spring Boot Bookstore App REST APIs Documentation",
		version = "v1.0",
		contact = @Contact(
				name = "Daniel",
				email = "daniel@gmail.com"
		)
),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Bookstore App Documentation",
				url = "https://github.com/DanielBaykov0/Springboot-Bookstore-App"
		)
)
@SpringBootApplication
@EnableAsync
public class SpringbootBookstoreAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBookstoreAppApplication.class, args);
	}

}
