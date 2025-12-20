package br.com.minimanager.products.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI Configuration
 * 
 * Configures API documentation and Swagger UI.
 * 
 * @author Marcelino Neto
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("📦 MiniManager Products Service API")
                        .description("**MiniManager Intelligent - Products Microservice**\n\n" +
                                "This microservice manages the complete product catalog, offering:\n" +
                                "- ✅ Full CRUD operations for products\n" +
                                "- 💰 Price management with BigDecimal precision\n" +
                                "- 📦 Inventory control and stock tracking\n" +
                                "- 🔍 Advanced search capabilities\n" +
                                "- ⚠️ Low stock alerts\n" +
                                "- 🎯 Data validation\n\n" +
                                "**Key Features:**\n" +
                                "- Product registration with price and stock control\n" +
                                "- Availability management\n" +
                                "- Optimized endpoints for high performance\n" +
                                "- Pre-populated sample data\n\n" +
                                "**Technologies:**\n" +
                                "- Spring Boot 2.7.18\n" +
                                "- Spring Data JPA\n" +
                                "- H2 Database (development)\n" +
                                "- PostgreSQL (production ready)\n" +
                                "- Lombok\n" +
                                "- Jakarta Bean Validation\n" +
                                "- BigDecimal for monetary precision\n\n" +
                                "**Base URL:** `/api/v1/products`")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Marcelino Neto")
                                .email("marcelino.neto@estudante.xpe.edu.br")
                                .url("https://github.com/marcelinoneto84"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
