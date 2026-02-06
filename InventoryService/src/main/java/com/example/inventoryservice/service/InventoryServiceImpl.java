package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.CreateInventoryItemRequest;
import com.example.inventoryservice.dto.InventoryItemDto;
import com.example.inventoryservice.dto.StockUpdateRequest;
import com.example.inventoryservice.dto.UpdateInventoryItemRequest;
import com.example.inventoryservice.exception.InsufficientStockException;
import com.example.inventoryservice.exception.ResourceNotFoundException;
import com.example.inventoryservice.model.InventoryItem;
import com.example.inventoryservice.repository.JpaInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final JpaInventoryRepository jpaInventoryRepository;
    
    @Override
    public InventoryItemDto createItem(CreateInventoryItemRequest request) {
        log.info("Creating new inventory item: {}", request.getName());

        InventoryItem item = InventoryItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .minimumStockLevel(request.getMinimumStockLevel() != null ? request.getMinimumStockLevel() : 10)
                .build();

        InventoryItem savedItem = jpaInventoryRepository.save(item);
        log.info("Created inventory item with ID: {}", savedItem.getId());

        return mapToDto(savedItem);
    }
    
    @Override
    @Transactional(readOnly = true)
    public InventoryItemDto getItemById(String id) {
        log.info("Fetching inventory item with ID: {}", id);

        InventoryItem item = jpaInventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", "id", id));

        return mapToDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDto> getAllItems() {
        log.info("Fetching all inventory items");

        return jpaInventoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDto> getItemsByCategory(String category) {
        log.info("Fetching inventory items by category: {}", category);

        return jpaInventoryRepository.findByCategory(category).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDto> getLowStockItems() {
        log.info("Fetching low stock items");

        return jpaInventoryRepository.findLowStockItems().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDto> getOutOfStockItems() {
        log.info("Fetching out of stock items");

        return jpaInventoryRepository.findOutOfStockItems().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public InventoryItemDto updateItem(String id, UpdateInventoryItemRequest request) {
        log.info("Updating inventory item with ID: {}", id);

        InventoryItem item = jpaInventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", "id", id));

        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            item.setCategory(request.getCategory());
        }
        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        if (request.getMinimumStockLevel() != null) {
            item.setMinimumStockLevel(request.getMinimumStockLevel());
        }

        InventoryItem updatedItem = jpaInventoryRepository.save(item);
        log.info("Updated inventory item with ID: {}", id);

        return mapToDto(updatedItem);
    }
    
    @Override
    public void deleteItem(String id) {
        log.info("Deleting inventory item with ID: {}", id);

        if (!jpaInventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("InventoryItem", "id", id);
        }

        jpaInventoryRepository.deleteById(id);
        log.info("Deleted inventory item with ID: {}", id);
    }

    @Override
    public InventoryItemDto addStock(String id, StockUpdateRequest request) {
        log.info("Adding stock for item ID: {}, quantity: {}", id, request.getQuantity());

        InventoryItem item = jpaInventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", "id", id));

        item.setQuantity(item.getQuantity() + request.getQuantity());

        InventoryItem updatedItem = jpaInventoryRepository.save(item);
        log.info("Added {} units to item ID: {}. New quantity: {}",
                request.getQuantity(), id, updatedItem.getQuantity());

        return mapToDto(updatedItem);
    }

    @Override
    public InventoryItemDto reduceStock(String id, StockUpdateRequest request) {
        log.info("Reducing stock for item ID: {}, quantity: {}", id, request.getQuantity());

        InventoryItem item = jpaInventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", "id", id));

        if (item.getQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(id, request.getQuantity(), item.getQuantity());
        }

        item.setQuantity(item.getQuantity() - request.getQuantity());

        InventoryItem updatedItem = jpaInventoryRepository.save(item);
        log.info("Reduced {} units from item ID: {}. New quantity: {}",
                request.getQuantity(), id, updatedItem.getQuantity());

        return mapToDto(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(String id, int quantity) {
        log.info("Checking availability for item ID: {}, quantity: {}", id, quantity);

        InventoryItem item = jpaInventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", "id", id));

        return item.getQuantity() >= quantity;
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalItemCount() {
        return jpaInventoryRepository.count();
    }
    
    private InventoryItemDto mapToDto(InventoryItem item) {
        return InventoryItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .category(item.getCategory())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .minimumStockLevel(item.getMinimumStockLevel())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .lowStock(item.isLowStock())
                .outOfStock(item.isOutOfStock())
                .build();
    }
}

