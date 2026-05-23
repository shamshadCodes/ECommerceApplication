package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.CreateInventoryItemRequest;
import com.example.inventoryservice.dto.InventoryItemDto;
import com.example.inventoryservice.dto.StockUpdateRequest;
import com.example.inventoryservice.dto.UpdateInventoryItemRequest;
import com.example.inventoryservice.exception.InsufficientStockException;
import com.example.inventoryservice.exception.ResourceNotFoundException;
import com.example.inventoryservice.model.InventoryItem;
import com.example.inventoryservice.repository.JpaInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private JpaInventoryRepository jpaInventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private InventoryItem testItem;
    private CreateInventoryItemRequest createRequest;

    @BeforeEach
    void setUp() {
        testItem = InventoryItem.builder()
            .id("item-123")
            .name("Test Item")
            .description("Test Description")
            .category("Electronics")
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .minimumStockLevel(10)
            .build();

        createRequest = CreateInventoryItemRequest.builder()
            .name("Test Item")
            .description("Test Description")
            .category("Electronics")
            .quantity(100)
            .price(BigDecimal.valueOf(99.99))
            .minimumStockLevel(10)
            .build();
    }

    @Test
    void createItem_WithValidRequest_ShouldCreateItem() {
        // Arrange
        when(jpaInventoryRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // Act
        InventoryItemDto result = inventoryService.createItem(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testItem.getName(), result.getName());
        verify(jpaInventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void getItemById_WithValidId_ShouldReturnItem() {
        // Arrange
        String itemId = "item-123";
        when(jpaInventoryRepository.findById(itemId)).thenReturn(Optional.of(testItem));

        // Act
        InventoryItemDto result = inventoryService.getItemById(itemId);

        // Assert
        assertNotNull(result);
        assertEquals(itemId, result.getId());
        verify(jpaInventoryRepository, times(1)).findById(itemId);
    }

    @Test
    void getItemById_WithInvalidId_ShouldThrowException() {
        // Arrange
        String itemId = "invalid-item";
        when(jpaInventoryRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            inventoryService.getItemById(itemId)
        );
        verify(jpaInventoryRepository, times(1)).findById(itemId);
    }

    @Test
    void getAllItems_ShouldReturnAllItems() {
        // Arrange
        List<InventoryItem> items = Arrays.asList(testItem);
        when(jpaInventoryRepository.findAll()).thenReturn(items);

        // Act
        List<InventoryItemDto> result = inventoryService.getAllItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaInventoryRepository, times(1)).findAll();
    }

    @Test
    void getItemsByCategory_WithValidCategory_ShouldReturnItems() {
        // Arrange
        String category = "Electronics";
        List<InventoryItem> items = Arrays.asList(testItem);
        when(jpaInventoryRepository.findByCategory(category)).thenReturn(items);

        // Act
        List<InventoryItemDto> result = inventoryService.getItemsByCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaInventoryRepository, times(1)).findByCategory(category);
    }

    @Test
    void addStock_WithValidRequest_ShouldIncreaseQuantity() {
        // Arrange
        String itemId = "item-123";
        StockUpdateRequest stockRequest = StockUpdateRequest.builder()
            .quantity(50)
            .build();

        when(jpaInventoryRepository.findById(itemId)).thenReturn(Optional.of(testItem));
        when(jpaInventoryRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // Act
        InventoryItemDto result = inventoryService.addStock(itemId, stockRequest);

        // Assert
        assertNotNull(result);
        verify(jpaInventoryRepository, times(1)).findById(itemId);
        verify(jpaInventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void reduceStock_WithValidQuantity_ShouldDecreaseQuantity() {
        // Arrange
        String itemId = "item-123";
        StockUpdateRequest stockRequest = StockUpdateRequest.builder()
            .quantity(20)
            .build();

        when(jpaInventoryRepository.findById(itemId)).thenReturn(Optional.of(testItem));
        when(jpaInventoryRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // Act
        InventoryItemDto result = inventoryService.reduceStock(itemId, stockRequest);

        // Assert
        assertNotNull(result);
        verify(jpaInventoryRepository, times(1)).findById(itemId);
        verify(jpaInventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void reduceStock_WithInsufficientStock_ShouldThrowException() {
        // Arrange
        String itemId = "item-123";
        StockUpdateRequest stockRequest = StockUpdateRequest.builder()
            .quantity(200)
            .build();

        when(jpaInventoryRepository.findById(itemId)).thenReturn(Optional.of(testItem));

        // Act & Assert
        assertThrows(InsufficientStockException.class, () ->
            inventoryService.reduceStock(itemId, stockRequest)
        );
        verify(jpaInventoryRepository, times(1)).findById(itemId);
        verify(jpaInventoryRepository, never()).save(any(InventoryItem.class));
    }

    @Test
    void updateItem_WithValidRequest_ShouldUpdateItem() {
        // Arrange
        String itemId = "item-123";
        UpdateInventoryItemRequest updateRequest = UpdateInventoryItemRequest.builder()
            .name("Updated Item")
            .price(BigDecimal.valueOf(149.99))
            .build();

        when(jpaInventoryRepository.findById(itemId)).thenReturn(Optional.of(testItem));
        when(jpaInventoryRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // Act
        InventoryItemDto result = inventoryService.updateItem(itemId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(jpaInventoryRepository, times(1)).findById(itemId);
        verify(jpaInventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void deleteItem_WithValidId_ShouldDeleteItem() {
        // Arrange
        String itemId = "item-123";
        when(jpaInventoryRepository.existsById(itemId)).thenReturn(true);
        doNothing().when(jpaInventoryRepository).deleteById(itemId);

        // Act
        inventoryService.deleteItem(itemId);

        // Assert
        verify(jpaInventoryRepository, times(1)).existsById(itemId);
        verify(jpaInventoryRepository, times(1)).deleteById(itemId);
    }

    @Test
    void checkAvailability_WithSufficientStock_ShouldReturnTrue() {
        // Arrange
        String itemId = "item-123";
        int quantity = 50;
        when(jpaInventoryRepository.findById(itemId)).thenReturn(Optional.of(testItem));

        // Act
        boolean result = inventoryService.checkAvailability(itemId, quantity);

        // Assert
        assertTrue(result);
        verify(jpaInventoryRepository, times(1)).findById(itemId);
    }

    @Test
    void checkAvailability_WithInsufficientStock_ShouldReturnFalse() {
        // Arrange
        String itemId = "item-123";
        int quantity = 200;
        when(jpaInventoryRepository.findById(itemId)).thenReturn(Optional.of(testItem));

        // Act
        boolean result = inventoryService.checkAvailability(itemId, quantity);

        // Assert
        assertFalse(result);
        verify(jpaInventoryRepository, times(1)).findById(itemId);
    }

    @Test
    void getLowStockItems_ShouldReturnItemsBelowMinimum() {
        // Arrange
        List<InventoryItem> lowStockItems = Arrays.asList(testItem);
        when(jpaInventoryRepository.findLowStockItems()).thenReturn(lowStockItems);

        // Act
        List<InventoryItemDto> result = inventoryService.getLowStockItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaInventoryRepository, times(1)).findLowStockItems();
    }

    @Test
    void searchItems_WithValidKeyword_ShouldReturnMatchingItems() {
        // Arrange
        String keyword = "Test";
        List<InventoryItem> items = Arrays.asList(testItem);
        when(jpaInventoryRepository.searchByNameOrDescription(keyword)).thenReturn(items);

        // Act
        List<InventoryItemDto> result = inventoryService.searchItems(keyword);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaInventoryRepository, times(1)).searchByNameOrDescription(keyword);
    }
}
