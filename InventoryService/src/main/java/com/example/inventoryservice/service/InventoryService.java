package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.CreateInventoryItemRequest;
import com.example.inventoryservice.dto.InventoryItemDto;
import com.example.inventoryservice.dto.StockUpdateRequest;
import com.example.inventoryservice.dto.UpdateInventoryItemRequest;

import java.util.List;

public interface InventoryService {
    
    InventoryItemDto createItem(CreateInventoryItemRequest request);
    
    InventoryItemDto getItemById(String id);
    
    List<InventoryItemDto> getAllItems();
    
    List<InventoryItemDto> getItemsByCategory(String category);
    
    List<InventoryItemDto> getLowStockItems();
    
    List<InventoryItemDto> getOutOfStockItems();
    
    InventoryItemDto updateItem(String id, UpdateInventoryItemRequest request);
    
    void deleteItem(String id);
    
    InventoryItemDto addStock(String id, StockUpdateRequest request);
    
    InventoryItemDto reduceStock(String id, StockUpdateRequest request);
    
    boolean checkAvailability(String id, int quantity);
    
    long getTotalItemCount();
}

