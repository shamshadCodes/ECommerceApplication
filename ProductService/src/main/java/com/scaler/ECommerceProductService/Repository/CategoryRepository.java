package com.scaler.ECommerceProductService.Repository;

import com.scaler.ECommerceProductService.model.Category;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Override
    @NonNull
    Optional<Category> findById(String id);

    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}