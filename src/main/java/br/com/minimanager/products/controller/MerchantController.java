package br.com.minimanager.products.controller;

import br.com.minimanager.products.model.Merchant;
import br.com.minimanager.products.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
@Tag(name = "Merchants", description = "Merchant management endpoints")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping
    @Operation(summary = "Get all merchants", description = "Retrieve all active merchants")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        return ResponseEntity.ok(merchantService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get merchant by ID", description = "Retrieve a specific merchant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Merchant found"),
        @ApiResponse(responseCode = "404", description = "Merchant not found")
    })
    public ResponseEntity<Merchant> getMerchantById(@PathVariable UUID id) {
        return ResponseEntity.ok(merchantService.findById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search merchants", description = "Search merchants by name or tax ID")
    @ApiResponse(responseCode = "200", description = "Search completed")
    public ResponseEntity<List<Merchant>> searchMerchants(@RequestParam String query) {
        return ResponseEntity.ok(merchantService.search(query));
    }

    @PostMapping
    @Operation(summary = "Create merchant", description = "Create a new merchant")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Merchant created"),
        @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    public ResponseEntity<Merchant> createMerchant(@Valid @RequestBody Merchant merchant) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(merchantService.create(merchant));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update merchant", description = "Update an existing merchant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Merchant updated"),
        @ApiResponse(responseCode = "404", description = "Merchant not found")
    })
    public ResponseEntity<Merchant> updateMerchant(
            @PathVariable UUID id,
            @Valid @RequestBody Merchant merchant) {
        return ResponseEntity.ok(merchantService.update(id, merchant));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete merchant", description = "Soft delete a merchant")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Merchant deleted"),
        @ApiResponse(responseCode = "404", description = "Merchant not found")
    })
    public ResponseEntity<Void> deleteMerchant(@PathVariable UUID id) {
        merchantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change merchant status", description = "Change merchant status")
    @ApiResponse(responseCode = "200", description = "Status changed")
    public ResponseEntity<Merchant> changeStatus(
            @PathVariable UUID id,
            @RequestParam Merchant.MerchantStatus status) {
        return ResponseEntity.ok(merchantService.changeStatus(id, status));
    }
}
