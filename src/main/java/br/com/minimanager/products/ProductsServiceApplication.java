package br.com.minimanager.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MiniManager Products Service Application
 * 
 * Main entry point for the Products microservice.
 * Manages product catalog, inventory, pricing and related operations.
 * 
 * @author Marcelino Neto
 * @version 1.0.0
 * @since 2025-12-19
 */
@SpringBootApplication
public class ProductsServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductsServiceApplication.class, args);
    }
}
