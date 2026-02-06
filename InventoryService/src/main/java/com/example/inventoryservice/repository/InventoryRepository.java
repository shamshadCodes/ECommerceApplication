package com.example.inventoryservice.repository;

import com.example.inventoryservice.model.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    
    InventoryItem save(InventoryItem item);
    
    Optional<InventoryItem> findById(String id);
    
    List<InventoryItem> findAll();
    
    List<InventoryItem> findByCategory(String category);
    
    List<InventoryItem> findLowStockItems();
    
    List<InventoryItem> findOutOfStockItems();
    
    boolean existsById(String id);
    
    void deleteById(String id);
    
    long count();
}

