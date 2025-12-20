package br.com.minimanager.products.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade Merchant (Comerciante/Empresa)
 * Representa os comerciantes que utilizam o sistema
 */
@Entity
@Table(name = "merchants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Legal name is required")
    @Size(max = 200)
    @Column(name = "legal_name", nullable = false, length = 200)
    private String legalName;

    @Size(max = 200)
    @Column(name = "trading_name", length = 200)
    private String tradingName;

    @NotBlank(message = "Tax ID is required")
    @Size(max = 20)
    @Column(name = "tax_id", unique = true, nullable = false, length = 20)
    private String taxId;

    @Enumerated(EnumType.STRING)
    @Column(name = "person_type", length = 20)
    private PersonType personType = PersonType.BUSINESS;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 150)
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 20)
    @Column(name = "mobile", length = 20)
    private String mobile;

    @Size(max = 200)
    @Column(name = "website", length = 200)
    private String website;

    @Size(max = 200)
    @Column(name = "address_street", length = 200)
    private String addressStreet;

    @Size(max = 20)
    @Column(name = "address_number", length = 20)
    private String addressNumber;

    @Size(max = 100)
    @Column(name = "address_complement", length = 100)
    private String addressComplement;

    @Size(max = 100)
    @Column(name = "address_neighborhood", length = 100)
    private String addressNeighborhood;

    @Size(max = 100)
    @Column(name = "address_city", length = 100)
    private String addressCity;

    @Size(max = 2)
    @Column(name = "address_state", length = 2)
    private String addressState;

    @Size(max = 10)
    @Column(name = "address_postal_code", length = 10)
    private String addressPostalCode;

    @Size(max = 3)
    @Column(name = "address_country", length = 3)
    private String addressCountry = "BRA";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private MerchantStatus status = MerchantStatus.ACTIVE;

    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

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
     * Enum for person type
     */
    public enum PersonType {
        INDIVIDUAL,  // Pessoa Física/MEI
        BUSINESS     // Pessoa Jurídica
    }

    /**
     * Enum for merchant status
     */
    public enum MerchantStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }
}
