package com.microshop.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public Inventory() {
    }

    public Inventory(Long productId, Integer quantity, Integer lowStockThreshold) {
        this.productId = productId;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
    }

    public Inventory(Long id, Long productId, Integer quantity, Integer lowStockThreshold, LocalDateTime lastUpdated) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
        this.lastUpdated = lastUpdated;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
