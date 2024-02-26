package com.scaler.ECommerceProductService.Repository;

import com.scaler.ECommerceProductService.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Override
    List<Product> findAll();
}
