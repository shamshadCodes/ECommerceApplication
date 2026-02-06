package com.example.inventoryservice.repository;

import com.example.inventoryservice.model.InventoryItem;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryInventoryRepository implements InventoryRepository {
    
    private final Map<String, InventoryItem> storage = new ConcurrentHashMap<>();
    
    @Override
    public InventoryItem save(InventoryItem item) {
        storage.put(item.getId(), item);
        return item;
    }
    
    @Override
    public Optional<InventoryItem> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<InventoryItem> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public List<InventoryItem> findByCategory(String category) {
        return storage.values().stream()
                .filter(item -> category.equalsIgnoreCase(item.getCategory()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<InventoryItem> findLowStockItems() {
        return storage.values().stream()
                .filter(InventoryItem::isLowStock)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<InventoryItem> findOutOfStockItems() {
        return storage.values().stream()
                .filter(InventoryItem::isOutOfStock)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
    }
    
    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }
    
    @Override
    public long count() {
        return storage.size();
    }
}

