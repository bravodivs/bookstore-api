package api.bookstore.catalog_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${bookstore.api-gateway-url:http://localhost:8080}")
    private String apiGatewayUrl;

    @Bean
    public OpenAPI catalogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catalog Service API")
                        .description("Book catalog management: books, authors, and publishers")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bookstore API Team")
                                .email("api@bookstore.local"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url(apiGatewayUrl)
                                .description("API Gateway (use for Try it out)"),
                        new Server()
                                .url("http://localhost:8081")
                                .description("Catalog service direct")));
    }
}
