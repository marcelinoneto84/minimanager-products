package br.com.minimanager.products.repository;

import br.com.minimanager.products.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(UUID productId);

    List<ProductImage> findByProductIdAndImageTypeOrderByDisplayOrderAsc(
        UUID productId, 
        ProductImage.ImageType imageType
    );

    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(UUID productId);

    void deleteByProductId(UUID productId);
}
