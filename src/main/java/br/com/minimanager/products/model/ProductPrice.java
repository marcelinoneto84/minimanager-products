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
 * Entidade ProductPrice
 * Histórico de alterações de preços
 */
@Entity
@Table(name = "product_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false, length = 20)
    private PriceType priceType;

    @Digits(integer = 12, fraction = 4)
    @Column(name = "old_value", precision = 12, scale = 4)
    private BigDecimal oldValue;

    @NotNull
    @Digits(integer = 12, fraction = 4)
    @Column(name = "new_value", nullable = false, precision = 12, scale = 4)
    private BigDecimal newValue;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "change_percentage", precision = 5, scale = 2)
    private BigDecimal changePercentage;

    @Size(max = 200)
    @Column(name = "reason", length = 200)
    private String reason;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "changed_by")
    private UUID changedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (validFrom == null) {
            validFrom = LocalDateTime.now();
        }
    }

    /**
     * Enum for price type
     */
    public enum PriceType {
        COST,         // Custo
        SALE,         // Venda
        PROMOTIONAL   // Promocional
    }
}
