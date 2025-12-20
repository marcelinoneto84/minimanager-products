package br.com.minimanager.products.repository;

import br.com.minimanager.products.model.ProductComposite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductCompositeRepository extends JpaRepository<ProductComposite, UUID> {

    List<ProductComposite> findByCompositeProductIdOrderByDisplayOrderAsc(UUID compositeProductId);

    List<ProductComposite> findByComponentProductId(UUID componentProductId);

    @Query("SELECT pc FROM ProductComposite pc " +
           "WHERE pc.compositeProductId = :compositeProductId " +
           "AND pc.isOptional = false " +
           "ORDER BY pc.displayOrder ASC")
    List<ProductComposite> findRequiredComponents(UUID compositeProductId);

    void deleteByCompositeProductId(UUID compositeProductId);

    boolean existsByCompositeProductIdAndComponentProductId(
        UUID compositeProductId, 
        UUID componentProductId
    );
}
