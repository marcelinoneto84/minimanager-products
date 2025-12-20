package br.com.minimanager.products.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade ProductComposite
 * Define produtos compostos (kits/combos)
 */
@Entity
@Table(name = "product_composites", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"composite_product_id", "component_product_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductComposite {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "composite_product_id", nullable = false)
    private UUID compositeProductId;

    @NotNull
    @Column(name = "component_product_id", nullable = false)
    private UUID componentProductId;

    @NotNull
    @Digits(integer = 12, fraction = 3)
    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity = BigDecimal.ONE;

    @Size(max = 10)
    @Column(name = "unit", length = 10)
    private String unit;

    @Column(name = "is_optional")
    private Boolean isOptional = false;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
