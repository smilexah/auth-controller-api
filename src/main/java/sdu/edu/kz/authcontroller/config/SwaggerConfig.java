package sdu.edu.kz.authcontroller.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User API",
                version = "version 1.0",
                contact = @Contact(
                        name = "SDU Edu",
                        email = "230103036@sdu.edu.kz",
                        url = "https://sdu.edu.kz"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                termsOfService = "https://sdu.edu.kz/language/en/terms-and-conditions/",
                description = "Spring Boot RESTFul API demo"
        )
)
public class SwaggerConfig {
}
