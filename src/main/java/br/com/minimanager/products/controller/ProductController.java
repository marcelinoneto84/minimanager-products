package br.com.minimanager.products.controller;

import br.com.minimanager.products.model.Product;
import br.com.minimanager.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/merchants/{merchantId}/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products for a merchant")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<Product>> getAllProducts(
            @PathVariable UUID merchantId) {
        return ResponseEntity.ok(productService.findAll(merchantId));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getProductById(
            @PathVariable UUID merchantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name, code or category")
    @ApiResponse(responseCode = "200", description = "Search completed")
    public ResponseEntity<List<Product>> searchProducts(
            @PathVariable UUID merchantId,
            @RequestParam String query) {
        return ResponseEntity.ok(productService.search(merchantId, query));
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get categories", description = "Get all distinct categories")
    @ApiResponse(responseCode = "200", description = "Categories retrieved")
    public ResponseEntity<List<String>> getCategories(
            @PathVariable UUID merchantId) {
        return ResponseEntity.ok(productService.getCategories(merchantId));
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Filter products by category")
    @ApiResponse(responseCode = "200", description = "Products retrieved")
    public ResponseEntity<List<Product>> getByCategory(
            @PathVariable UUID merchantId,
            @PathVariable String category) {
        return ResponseEntity.ok(productService.findByCategory(merchantId, category));
    }
    
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Get products below minimum stock")
    @ApiResponse(responseCode = "200", description = "Low stock products retrieved")
    public ResponseEntity<List<Product>> getLowStockProducts(
            @PathVariable UUID merchantId) {
        return ResponseEntity.ok(productService.findLowStock(merchantId));
    }
    
    @PostMapping
    @Operation(summary = "Create product", description = "Create a new product")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Product> createProduct(
            @PathVariable UUID merchantId,
            @Valid @RequestBody Product product) {
        product.setMerchantId(merchantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(product));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Product> updateProduct(
            @PathVariable UUID merchantId,
            @PathVariable UUID id,
            @Valid @RequestBody Product product) {
        product.setMerchantId(merchantId);
        return ResponseEntity.ok(productService.update(id, product));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Soft delete a product")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID merchantId,
            @PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/stock/adjust")
    @Operation(summary = "Adjust stock", description = "Adjust product stock to exact quantity")
    @ApiResponse(responseCode = "200", description = "Stock adjusted successfully")
    public ResponseEntity<Product> adjustStock(
            @PathVariable UUID merchantId,
            @PathVariable UUID id,
            @RequestParam BigDecimal quantity,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(productService.adjustStock(id, quantity, reason));
    }
    
    @PatchMapping("/{id}/stock/add")
    @Operation(summary = "Add stock", description = "Add quantity to product stock")
    @ApiResponse(responseCode = "200", description = "Stock added successfully")
    public ResponseEntity<Product> addStock(
            @PathVariable UUID merchantId,
            @PathVariable UUID id,
            @RequestParam BigDecimal quantity,
            @RequestParam(required = false) BigDecimal unitCost,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(productService.addStock(id, quantity, unitCost, notes));
    }
    
    @PatchMapping("/{id}/stock/remove")
    @Operation(summary = "Remove stock", description = "Remove quantity from product stock")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock removed successfully"),
        @ApiResponse(responseCode = "400", description = "Insufficient stock")
    })
    public ResponseEntity<Product> removeStock(
            @PathVariable UUID merchantId,
            @PathVariable UUID id,
            @RequestParam BigDecimal quantity,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(productService.removeStock(id, quantity, notes));
    }
}
