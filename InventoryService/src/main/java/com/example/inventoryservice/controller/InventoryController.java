package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.*;
import com.example.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<InventoryItemDto>> createItem(
            @Valid @RequestBody CreateInventoryItemRequest request) {
        log.info("REST request to create inventory item: {}", request.getName());
        
        InventoryItemDto createdItem = inventoryService.createItem(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item created successfully", createdItem));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemDto>> getItemById(@PathVariable String id) {
        log.info("REST request to get inventory item by ID: {}", id);
        
        InventoryItemDto item = inventoryService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success(item));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> getAllItems(
            @RequestParam(required = false) String category) {
        log.info("REST request to get all inventory items. Category filter: {}", category);
        
        List<InventoryItemDto> items;
        if (category != null && !category.isEmpty()) {
            items = inventoryService.getItemsByCategory(category);
        } else {
            items = inventoryService.getAllItems();
        }
        
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> getLowStockItems() {
        log.info("REST request to get low stock items");
        
        List<InventoryItemDto> items = inventoryService.getLowStockItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> getOutOfStockItems() {
        log.info("REST request to get out of stock items");
        
        List<InventoryItemDto> items = inventoryService.getOutOfStockItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemDto>> updateItem(
            @PathVariable String id,
            @Valid @RequestBody UpdateInventoryItemRequest request) {
        log.info("REST request to update inventory item with ID: {}", id);
        
        InventoryItemDto updatedItem = inventoryService.updateItem(id, request);
        return ResponseEntity.ok(ApiResponse.success("Item updated successfully", updatedItem));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable String id) {
        log.info("REST request to delete inventory item with ID: {}", id);
        
        inventoryService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Item deleted successfully", null));
    }
    
    @PostMapping("/{id}/stock/add")
    public ResponseEntity<ApiResponse<InventoryItemDto>> addStock(
            @PathVariable String id,
            @Valid @RequestBody StockUpdateRequest request) {
        log.info("REST request to add stock for item ID: {}", id);
        
        InventoryItemDto updatedItem = inventoryService.addStock(id, request);
        return ResponseEntity.ok(ApiResponse.success("Stock added successfully", updatedItem));
    }
    
    @PostMapping("/{id}/stock/reduce")
    public ResponseEntity<ApiResponse<InventoryItemDto>> reduceStock(
            @PathVariable String id,
            @Valid @RequestBody StockUpdateRequest request) {
        log.info("REST request to reduce stock for item ID: {}", id);
        
        InventoryItemDto updatedItem = inventoryService.reduceStock(id, request);
        return ResponseEntity.ok(ApiResponse.success("Stock reduced successfully", updatedItem));
    }
    
    @GetMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<Boolean>> checkAvailability(
            @PathVariable String id,
            @RequestParam int quantity) {
        log.info("REST request to check availability for item ID: {}, quantity: {}", id, quantity);
        
        boolean available = inventoryService.checkAvailability(id, quantity);
        return ResponseEntity.ok(ApiResponse.success(available));
    }
    
    @GetMapping("/stats/count")
    public ResponseEntity<ApiResponse<Long>> getTotalItemCount() {
        log.info("REST request to get total item count");
        
        long count = inventoryService.getTotalItemCount();
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}

