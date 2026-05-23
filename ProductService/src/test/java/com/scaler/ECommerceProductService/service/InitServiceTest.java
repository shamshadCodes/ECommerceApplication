package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.client.FakeStoreAPIClient;
import com.scaler.ECommerceProductService.dto.Request.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.Response.FakeStoreProductResponseDTO;
import com.scaler.ECommerceProductService.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitServiceTest {

    @Mock
    private FakeStoreAPIClient fakeStoreAPIClient;

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private InitService initService;

    @Test
    void copyProductsFromFakeStore_FetchesFromClientAndPersistsViaProductService() throws Exception {
        FakeStoreProductResponseDTO fake = new FakeStoreProductResponseDTO();
        fake.setTitle("FS Title");
        fake.setDescription("FS Desc");
        fake.setCategory("electronics");
        fake.setImage("fs.jpg");
        fake.setPrice(123.45);

        when(fakeStoreAPIClient.getAllProducts()).thenReturn(List.of(fake));
        when(productService.addProduct(any(ProductRequestDTO.class)))
                .thenReturn(new Product());

        List<Product> result = initService.copyProductsFromFakeStore();

        assertEquals(1, result.size());

        ArgumentCaptor<ProductRequestDTO> captor = ArgumentCaptor.forClass(ProductRequestDTO.class);
        verify(productService, times(1)).addProduct(captor.capture());
        ProductRequestDTO usedRequest = captor.getValue();
        assertEquals(fake.getTitle(), usedRequest.getTitle());
        assertEquals(fake.getCategory(), usedRequest.getCategory());
    }
}
