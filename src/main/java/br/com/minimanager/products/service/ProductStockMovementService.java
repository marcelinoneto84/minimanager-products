package br.com.minimanager.products.service;

import br.com.minimanager.products.model.ProductStockMovement;
import br.com.minimanager.products.repository.ProductStockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductStockMovementService {

    private final ProductStockMovementRepository repository;

    @Transactional(readOnly = true)
    public List<ProductStockMovement> findByProductId(UUID productId) {
        return repository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Transactional(readOnly = true)
    public List<ProductStockMovement> findByMerchantId(UUID merchantId) {
        return repository.findByMerchantIdOrderByCreatedAtDesc(merchantId);
    }

    @Transactional(readOnly = true)
    public List<ProductStockMovement> findByDateRange(UUID productId, LocalDateTime start, LocalDateTime end) {
        return repository.findByProductIdAndDateRange(productId, start, end);
    }

    @Transactional
    public ProductStockMovement registerMovement(
            UUID productId,
            ProductStockMovement.MovementType movementType,
            BigDecimal quantity,
            BigDecimal unitCost,
            BigDecimal previousStock,
            UUID merchantId,
            String notes,
            UUID createdBy) {
        
        ProductStockMovement movement = new ProductStockMovement();
        movement.setProductId(productId);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setUnitCost(unitCost);
        movement.setPreviousStock(previousStock);
        
        if (previousStock != null) {
            movement.setNewStock(previousStock.add(quantity));
        }
        
        movement.setMerchantId(merchantId);
        movement.setNotes(notes);
        movement.setCreatedBy(createdBy);
        
        return repository.save(movement);
    }
}
