package br.com.minimanager.products.service;

import br.com.minimanager.products.model.Merchant;
import br.com.minimanager.products.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    @Transactional(readOnly = true)
    public List<Merchant> findAll() {
        return merchantRepository.findAllByDeletedAtIsNull();
    }

    @Transactional(readOnly = true)
    public Merchant findById(UUID id) {
        return merchantRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new RuntimeException("Merchant not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Merchant findByTaxId(String taxId) {
        return merchantRepository.findByTaxIdAndDeletedAtIsNull(taxId)
            .orElseThrow(() -> new RuntimeException("Merchant not found with tax ID: " + taxId));
    }

    @Transactional(readOnly = true)
    public List<Merchant> search(String query) {
        return merchantRepository.searchMerchants(query);
    }

    @Transactional
    public Merchant create(Merchant merchant) {
        if (merchantRepository.existsByTaxIdAndDeletedAtIsNull(merchant.getTaxId())) {
            throw new RuntimeException("Merchant already exists with tax ID: " + merchant.getTaxId());
        }
        return merchantRepository.save(merchant);
    }

    @Transactional
    public Merchant update(UUID id, Merchant updatedMerchant) {
        Merchant existing = findById(id);
        
        existing.setLegalName(updatedMerchant.getLegalName());
        existing.setTradingName(updatedMerchant.getTradingName());
        existing.setPersonType(updatedMerchant.getPersonType());
        existing.setEmail(updatedMerchant.getEmail());
        existing.setPhone(updatedMerchant.getPhone());
        existing.setMobile(updatedMerchant.getMobile());
        existing.setWebsite(updatedMerchant.getWebsite());
        existing.setAddressStreet(updatedMerchant.getAddressStreet());
        existing.setAddressNumber(updatedMerchant.getAddressNumber());
        existing.setAddressComplement(updatedMerchant.getAddressComplement());
        existing.setAddressNeighborhood(updatedMerchant.getAddressNeighborhood());
        existing.setAddressCity(updatedMerchant.getAddressCity());
        existing.setAddressState(updatedMerchant.getAddressState());
        existing.setAddressCountry(updatedMerchant.getAddressCountry());
        existing.setAddressPostalCode(updatedMerchant.getAddressPostalCode());
        existing.setStatus(updatedMerchant.getStatus());
        
        return merchantRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        Merchant merchant = findById(id);
        merchant.setDeletedAt(LocalDateTime.now());
        merchant.setStatus(Merchant.MerchantStatus.INACTIVE);
        merchantRepository.save(merchant);
    }

    @Transactional
    public Merchant changeStatus(UUID id, Merchant.MerchantStatus newStatus) {
        Merchant merchant = findById(id);
        merchant.setStatus(newStatus);
        return merchantRepository.save(merchant);
    }
}
