package com.scaler.ECommerceProductService.Repository;

import com.scaler.ECommerceProductService.model.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Override
    @NonNull
    List<Product> findAll();

    Optional<Product> findByTitleIgnoreCase(String title);
}
