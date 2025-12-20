package br.com.minimanager.products.repository;

import br.com.minimanager.products.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {

    List<ProductPrice> findByProductIdOrderByCreatedAtDesc(UUID productId);

    @Query("SELECT pp FROM ProductPrice pp WHERE pp.productId = :productId " +
           "AND pp.priceType = :priceType ORDER BY pp.createdAt DESC")
    List<ProductPrice> findByProductIdAndPriceType(
        UUID productId, 
        ProductPrice.PriceType priceType
    );

    @Query("SELECT pp FROM ProductPrice pp WHERE pp.productId = :productId " +
           "AND :date BETWEEN pp.validFrom AND COALESCE(pp.validTo, :date) " +
           "ORDER BY pp.createdAt DESC")
    List<ProductPrice> findValidPricesAtDate(UUID productId, LocalDateTime date);

    Optional<ProductPrice> findFirstByProductIdAndPriceTypeOrderByCreatedAtDesc(
        UUID productId, 
        ProductPrice.PriceType priceType
    );
}
