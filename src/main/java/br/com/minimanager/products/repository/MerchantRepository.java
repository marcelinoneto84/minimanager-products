package br.com.minimanager.products.repository;

import br.com.minimanager.products.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    Optional<Merchant> findByTaxIdAndDeletedAtIsNull(String taxId);

    Optional<Merchant> findByIdAndDeletedAtIsNull(UUID id);

    List<Merchant> findAllByDeletedAtIsNull();

    List<Merchant> findByStatusAndDeletedAtIsNull(Merchant.MerchantStatus status);

    @Query("SELECT m FROM Merchant m WHERE m.deletedAt IS NULL AND " +
           "(LOWER(m.legalName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.tradingName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "m.taxId LIKE CONCAT('%', :search, '%'))")
    List<Merchant> searchMerchants(String search);

    boolean existsByTaxIdAndDeletedAtIsNull(String taxId);
}
