package br.com.minimanager.products.repository;

import br.com.minimanager.products.model.ProductStockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductStockMovementRepository extends JpaRepository<ProductStockMovement, UUID> {

    List<ProductStockMovement> findByProductIdOrderByCreatedAtDesc(UUID productId);

    List<ProductStockMovement> findByProductIdAndMovementTypeOrderByCreatedAtDesc(
        UUID productId, 
        ProductStockMovement.MovementType movementType
    );

    List<ProductStockMovement> findByMerchantIdOrderByCreatedAtDesc(UUID merchantId);

    @Query("SELECT psm FROM ProductStockMovement psm " +
           "WHERE psm.productId = :productId " +
           "AND psm.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY psm.createdAt DESC")
    List<ProductStockMovement> findByProductIdAndDateRange(
        UUID productId, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );

    @Query("SELECT psm FROM ProductStockMovement psm " +
           "WHERE psm.merchantId = :merchantId " +
           "AND psm.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY psm.createdAt DESC")
    List<ProductStockMovement> findByMerchantIdAndDateRange(
        UUID merchantId, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
}
