package br.com.minimanager.products.repository;

import br.com.minimanager.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByCodeAndDeletedAtIsNull(String code);

    Optional<Product> findByIdAndDeletedAtIsNull(UUID id);

    List<Product> findAllByMerchantIdAndDeletedAtIsNull(UUID merchantId);

    List<Product> findByMerchantIdAndStatusAndDeletedAtIsNull(
        UUID merchantId, 
        Product.ProductStatus status
    );

    List<Product> findByMerchantIdAndCategoryAndDeletedAtIsNull(
        UUID merchantId, 
        String category
    );

    @Query("SELECT p FROM Product p WHERE p.merchantId = :merchantId " +
           "AND p.deletedAt IS NULL " +
           "AND p.currentStock <= p.minimumStock")
    List<Product> findLowStockProducts(UUID merchantId);

    @Query("SELECT p FROM Product p WHERE p.merchantId = :merchantId " +
           "AND p.deletedAt IS NULL " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Product> searchProducts(UUID merchantId, String search);

    boolean existsByCodeAndMerchantIdAndDeletedAtIsNull(String code, UUID merchantId);

    @Query("SELECT DISTINCT p.category FROM Product p " +
           "WHERE p.merchantId = :merchantId AND p.deletedAt IS NULL " +
           "ORDER BY p.category")
    List<String> findDistinctCategoriesByMerchantId(UUID merchantId);
}
