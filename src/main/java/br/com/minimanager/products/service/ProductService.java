package br.com.minimanager.products.service;

import br.com.minimanager.products.model.Product;
import br.com.minimanager.products.model.ProductStockMovement;
import br.com.minimanager.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockMovementService stockMovementService;

    @Transactional(readOnly = true)
    public List<Product> findAll(UUID merchantId) {
        return productRepository.findAllByMerchantIdAndDeletedAtIsNull(merchantId);
    }

    @Transactional(readOnly = true)
    public Product findById(UUID id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Product findByCode(String code) {
        return productRepository.findByCodeAndDeletedAtIsNull(code)
            .orElseThrow(() -> new RuntimeException("Product not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategory(UUID merchantId, String category) {
        return productRepository.findByMerchantIdAndCategoryAndDeletedAtIsNull(merchantId, category);
    }

    @Transactional(readOnly = true)
    public List<Product> findLowStock(UUID merchantId) {
        return productRepository.findLowStockProducts(merchantId);
    }

    @Transactional(readOnly = true)
    public List<Product> search(UUID merchantId, String query) {
        return productRepository.searchProducts(merchantId, query);
    }

    @Transactional(readOnly = true)
    public List<String> getCategories(UUID merchantId) {
        return productRepository.findDistinctCategoriesByMerchantId(merchantId);
    }

    @Transactional
    public Product create(Product product) {
        if (productRepository.existsByCodeAndMerchantIdAndDeletedAtIsNull(
                product.getCode(), product.getMerchantId())) {
            throw new RuntimeException("Product already exists with code: " + product.getCode());
        }
        
        Product saved = productRepository.save(product);
        
        // Register initial stock if greater than zero
        if (saved.getCurrentStock() != null && saved.getCurrentStock().compareTo(BigDecimal.ZERO) > 0) {
            stockMovementService.registerMovement(
                saved.getId(),
                ProductStockMovement.MovementType.ADJUSTMENT,
                saved.getCurrentStock(),
                saved.getCostPrice(),
                null,
                saved.getMerchantId(),
                "Initial stock",
                null
            );
        }
        
        return saved;
    }

    @Transactional
    public Product update(UUID id, Product updatedProduct) {
        Product existing = findById(id);
        
        existing.setCode(updatedProduct.getCode());
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setCategory(updatedProduct.getCategory());
        existing.setUnit(updatedProduct.getUnit());
        existing.setCostPrice(updatedProduct.getCostPrice());
        existing.setSalePrice(updatedProduct.getSalePrice());
        existing.setMinimumStock(updatedProduct.getMinimumStock());
        existing.setMaximumStock(updatedProduct.getMaximumStock());
        existing.setReorderPoint(updatedProduct.getReorderPoint());
        existing.setWeightKg(updatedProduct.getWeightKg());
        existing.setLengthCm(updatedProduct.getLengthCm());
        existing.setWidthCm(updatedProduct.getWidthCm());
        existing.setHeightCm(updatedProduct.getHeightCm());
        existing.setNcmCode(updatedProduct.getNcmCode());
        existing.setCestCode(updatedProduct.getCestCode());
        existing.setEanGtin(updatedProduct.getEanGtin());
        existing.setIsFractional(updatedProduct.getIsFractional());
        existing.setIsComposite(updatedProduct.getIsComposite());
        existing.setRequiresSerial(updatedProduct.getRequiresSerial());
        existing.setProductType(updatedProduct.getProductType());
        existing.setStatus(updatedProduct.getStatus());
        
        return productRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        Product product = findById(id);
        product.setDeletedAt(LocalDateTime.now());
        product.setStatus(Product.ProductStatus.DELETED);
        productRepository.save(product);
    }

    @Transactional
    public Product adjustStock(UUID id, BigDecimal quantity, String reason) {
        Product product = findById(id);
        BigDecimal previousStock = product.getCurrentStock();
        product.setCurrentStock(quantity);
        
        stockMovementService.registerMovement(
            id,
            ProductStockMovement.MovementType.ADJUSTMENT,
            quantity.subtract(previousStock),
            product.getCostPrice(),
            previousStock,
            product.getMerchantId(),
            reason,
            null
        );
        
        return productRepository.save(product);
    }

    @Transactional
    public Product addStock(UUID id, BigDecimal quantity, BigDecimal unitCost, String notes) {
        Product product = findById(id);
        BigDecimal previousStock = product.getCurrentStock();
        BigDecimal newStock = previousStock.add(quantity);
        product.setCurrentStock(newStock);
        
        stockMovementService.registerMovement(
            id,
            ProductStockMovement.MovementType.PURCHASE,
            quantity,
            unitCost,
            previousStock,
            product.getMerchantId(),
            notes,
            null
        );
        
        return productRepository.save(product);
    }

    @Transactional
    public Product removeStock(UUID id, BigDecimal quantity, String notes) {
        Product product = findById(id);
        BigDecimal previousStock = product.getCurrentStock();
        
        if (previousStock.compareTo(quantity) < 0) {
            throw new RuntimeException("Insufficient stock. Available: " + previousStock + ", Requested: " + quantity);
        }
        
        BigDecimal newStock = previousStock.subtract(quantity);
        product.setCurrentStock(newStock);
        
        stockMovementService.registerMovement(
            id,
            ProductStockMovement.MovementType.SALE,
            quantity.negate(),
            product.getCostPrice(),
            previousStock,
            product.getMerchantId(),
            notes,
            null
        );
        
        return productRepository.save(product);
    }
}
