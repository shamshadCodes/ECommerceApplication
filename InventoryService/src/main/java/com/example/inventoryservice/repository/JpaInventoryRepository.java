package com.example.inventoryservice.repository;

import com.example.inventoryservice.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaInventoryRepository extends JpaRepository<InventoryItem, String> {
    
    List<InventoryItem> findByCategory(String category);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.minimumStockLevel")
    List<InventoryItem> findLowStockItems();
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= 0")
    List<InventoryItem> findOutOfStockItems();
}

