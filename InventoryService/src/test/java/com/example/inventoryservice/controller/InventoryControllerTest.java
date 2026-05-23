package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.*;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private InventoryItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = InventoryItemDto.builder()
                .id("item-123")
                .name("Test Item")
                .description("Test Description")
                .category("Electronics")
                .quantity(100)
                .price(BigDecimal.valueOf(99.99))
                .minimumStockLevel(10)
                .lowStock(false)
                .outOfStock(false)
                .build();
    }

    @Test
    void createItem_ReturnsCreatedResponseWithApiWrapper() {
        CreateInventoryItemRequest request = CreateInventoryItemRequest.builder()
                .name("Test Item")
                .description("Test Description")
                .category("Electronics")
                .quantity(100)
                .price(BigDecimal.valueOf(99.99))
                .minimumStockLevel(10)
                .build();

        when(inventoryService.createItem(request)).thenReturn(itemDto);

        ResponseEntity<ApiResponse<InventoryItemDto>> response =
                inventoryController.createItem(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Item created successfully", response.getBody().getMessage());
        assertEquals(itemDto.getId(), response.getBody().getData().getId());
        verify(inventoryService, times(1)).createItem(request);
    }

    @Test
    void getItemById_ReturnsItemWrappedInApiResponse() {
        when(inventoryService.getItemById("item-123")).thenReturn(itemDto);

        ResponseEntity<ApiResponse<InventoryItemDto>> response =
                inventoryController.getItemById("item-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(itemDto.getId(), response.getBody().getData().getId());
        verify(inventoryService, times(1)).getItemById("item-123");
    }

    @Test
    void getAllItems_WithoutCategory_UsesGetAllItems() {
        when(inventoryService.getAllItems()).thenReturn(Collections.singletonList(itemDto));

        ResponseEntity<ApiResponse<List<InventoryItemDto>>> response =
                inventoryController.getAllItems(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
        verify(inventoryService, times(1)).getAllItems();
        verify(inventoryService, never()).getItemsByCategory(any());
    }

    @Test
    void getAllItems_WithCategory_UsesGetItemsByCategory() {
        when(inventoryService.getItemsByCategory("Electronics"))
                .thenReturn(Collections.singletonList(itemDto));

        ResponseEntity<ApiResponse<List<InventoryItemDto>>> response =
                inventoryController.getAllItems("Electronics");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(inventoryService, times(1)).getItemsByCategory("Electronics");
        verify(inventoryService, never()).getAllItems();
    }

    @Test
    void getLowStockItems_ReturnsList() {
        when(inventoryService.getLowStockItems()).thenReturn(List.of(itemDto));

        ResponseEntity<ApiResponse<List<InventoryItemDto>>> response =
                inventoryController.getLowStockItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(inventoryService, times(1)).getLowStockItems();
    }

    @Test
    void getOutOfStockItems_ReturnsList() {
        when(inventoryService.getOutOfStockItems()).thenReturn(List.of(itemDto));

        ResponseEntity<ApiResponse<List<InventoryItemDto>>> response =
                inventoryController.getOutOfStockItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(inventoryService, times(1)).getOutOfStockItems();
    }

    @Test
    void updateItem_ReturnsUpdatedItem() {
        UpdateInventoryItemRequest request = UpdateInventoryItemRequest.builder()
                .name("Updated Name")
                .build();

        when(inventoryService.updateItem(eq("item-123"), any(UpdateInventoryItemRequest.class)))
                .thenReturn(itemDto);

        ResponseEntity<ApiResponse<InventoryItemDto>> response =
                inventoryController.updateItem("item-123", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item updated successfully", response.getBody().getMessage());
        verify(inventoryService, times(1)).updateItem("item-123", request);
    }

    @Test
    void deleteItem_ReturnsSuccessMessage() {
        doNothing().when(inventoryService).deleteItem("item-123");

        ResponseEntity<ApiResponse<Void>> response =
                inventoryController.deleteItem("item-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item deleted successfully", response.getBody().getMessage());
        verify(inventoryService, times(1)).deleteItem("item-123");
    }

    @Test
    void addStock_ReturnsUpdatedItem() {
        StockUpdateRequest request = StockUpdateRequest.builder()
                .quantity(10)
                .reason("Restock")
                .build();

        when(inventoryService.addStock("item-123", request)).thenReturn(itemDto);

        ResponseEntity<ApiResponse<InventoryItemDto>> response =
                inventoryController.addStock("item-123", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Stock added successfully", response.getBody().getMessage());
        verify(inventoryService, times(1)).addStock("item-123", request);
    }

    @Test
    void reduceStock_ReturnsUpdatedItem() {
        StockUpdateRequest request = StockUpdateRequest.builder()
                .quantity(5)
                .reason("Sale")
                .build();

        when(inventoryService.reduceStock("item-123", request)).thenReturn(itemDto);

        ResponseEntity<ApiResponse<InventoryItemDto>> response =
                inventoryController.reduceStock("item-123", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Stock reduced successfully", response.getBody().getMessage());
        verify(inventoryService, times(1)).reduceStock("item-123", request);
    }

    @Test
    void checkAvailability_ReturnsBooleanFlag() {
        when(inventoryService.checkAvailability("item-123", 10)).thenReturn(true);

        ResponseEntity<ApiResponse<Boolean>> response =
                inventoryController.checkAvailability("item-123", 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData());
        verify(inventoryService, times(1)).checkAvailability("item-123", 10);
    }

    @Test
    void getTotalItemCount_ReturnsCount() {
        when(inventoryService.getTotalItemCount()).thenReturn(42L);

        ResponseEntity<ApiResponse<Long>> response =
                inventoryController.getTotalItemCount();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(42L, response.getBody().getData());
        verify(inventoryService, times(1)).getTotalItemCount();
    }
}
