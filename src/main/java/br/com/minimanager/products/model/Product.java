package br.com.minimanager.products.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade Product
 * Representa produtos/serviços do catálogo
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Product code is required")
    @Size(max = 50)
    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @NotBlank(message = "Product name is required")
    @Size(max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 50)
    @Column(name = "category", length = 50)
    private String category;

    @Size(max = 50)
    @Column(name = "group_code", length = 50)
    private String groupCode;

    @Size(max = 10)
    @Column(name = "unit", length = 10)
    private String unit = "UN";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", length = 20)
    private ProductType productType = ProductType.PRODUCT;

    @NotNull
    @DecimalMin(value = "0.0", message = "Cost price must be positive")
    @Digits(integer = 12, fraction = 4)
    @Column(name = "cost_price", nullable = false, precision = 12, scale = 4)
    private BigDecimal costPrice = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.0", message = "Sale price must be positive")
    @Digits(integer = 12, fraction = 4)
    @Column(name = "sale_price", nullable = false, precision = 12, scale = 4)
    private BigDecimal salePrice = BigDecimal.ZERO;

    // profit_margin é calculado automaticamente pelo banco (GENERATED ALWAYS AS)
    @Column(name = "profit_margin", insertable = false, updatable = false)
    private BigDecimal profitMargin;

    @Min(value = 0, message = "Stock cannot be negative")
    @Digits(integer = 12, fraction = 3)
    @Column(name = "current_stock", precision = 12, scale = 3)
    private BigDecimal currentStock = BigDecimal.ZERO;

    @Min(value = 0)
    @Digits(integer = 12, fraction = 3)
    @Column(name = "minimum_stock", precision = 12, scale = 3)
    private BigDecimal minimumStock = BigDecimal.ZERO;

    @Digits(integer = 12, fraction = 3)
    @Column(name = "maximum_stock", precision = 12, scale = 3)
    private BigDecimal maximumStock;

    @Digits(integer = 12, fraction = 3)
    @Column(name = "reorder_point", precision = 12, scale = 3)
    private BigDecimal reorderPoint;

    @Digits(integer = 10, fraction = 3)
    @Column(name = "weight_kg", precision = 10, scale = 3)
    private BigDecimal weightKg;

    @Digits(integer = 10, fraction = 2)
    @Column(name = "length_cm", precision = 10, scale = 2)
    private BigDecimal lengthCm;

    @Digits(integer = 10, fraction = 2)
    @Column(name = "width_cm", precision = 10, scale = 2)
    private BigDecimal widthCm;

    @Digits(integer = 10, fraction = 2)
    @Column(name = "height_cm", precision = 10, scale = 2)
    private BigDecimal heightCm;

    @Size(max = 10)
    @Column(name = "ncm_code", length = 10)
    private String ncmCode;

    @Size(max = 10)
    @Column(name = "cest_code", length = 10)
    private String cestCode;

    @Size(max = 14)
    @Column(name = "ean_gtin", length = 14)
    private String eanGtin;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "is_fractional")
    private Boolean isFractional = false;

    @Column(name = "is_composite")
    private Boolean isComposite = false;

    @Column(name = "requires_serial")
    private Boolean requiresSerial = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @NotNull
    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica se o produto está disponível em estoque
     */
    public boolean isAvailable() {
        return currentStock != null && currentStock.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Verifica se o estoque está abaixo do mínimo
     */
    public boolean isLowStock() {
        if (currentStock == null || minimumStock == null) {
            return false;
        }
        return currentStock.compareTo(minimumStock) <= 0;
    }

    /**
     * Enum for product status
     */
    public enum ProductStatus {
        ACTIVE,
        INACTIVE,
        DELETED
    }

    /**
     * Enum for product type
     */
    public enum ProductType {
        PRODUCT,   // Produto físico
        SERVICE,   // Serviço
        COMPOSITE  // Kit/Combo
    }
}
