package com.microshop.inventory.service;

import com.microshop.inventory.model.Inventory;
import com.microshop.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public Optional<Inventory> getStockByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Transactional
    public Inventory updateStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(Inventory.builder()
                        .productId(productId)
                        .lowStockThreshold(10) // Default threshold
                        .build());
        
        inventory.setQuantity(quantity);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory reduceStock(Long productId, Integer amount) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario: " + productId));

        if (inventory.getQuantity() < amount) {
            throw new RuntimeException("Stock insuficiente para el producto: " + productId);
        }

        inventory.setQuantity(inventory.getQuantity() - amount);
        
        if (inventory.getQuantity() <= inventory.getLowStockThreshold()) {
            log.warn("ALERTA: Stock bajo para el producto {}. Cantidad actual: {}", productId, inventory.getQuantity());
            // Aquí se integraría con notification-service en el futuro
        }

        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory restoreStock(Long productId, Integer amount) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario: " + productId));

        inventory.setQuantity(inventory.getQuantity() + amount);
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getLowStockAlerts() {
        return inventoryRepository.findLowStockItems();
    }
}
