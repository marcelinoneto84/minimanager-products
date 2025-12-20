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
 * Entidade ProductStockMovement
 * Histórico de movimentações de estoque
 */
@Entity
@Table(name = "product_stock_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockMovement {

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
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    @NotNull
    @Digits(integer = 12, fraction = 3)
    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Digits(integer = 12, fraction = 4)
    @Column(name = "unit_cost", precision = 12, scale = 4)
    private BigDecimal unitCost;

    @Digits(integer = 12, fraction = 3)
    @Column(name = "previous_stock", precision = 12, scale = 3)
    private BigDecimal previousStock;

    @Digits(integer = 12, fraction = 3)
    @Column(name = "new_stock", precision = 12, scale = 3)
    private BigDecimal newStock;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Size(max = 50)
    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @NotNull
    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Enum for movement type
     */
    public enum MovementType {
        PURCHASE,      // Compra/Entrada
        SALE,          // Venda/Saída
        ADJUSTMENT,    // Ajuste manual
        PRODUCTION,    // Produção
        LOSS,          // Perda/Quebra
        RETURN,        // Devolução
        TRANSFER,      // Transferência
        INVENTORY      // Inventário
    }
}
