package com.scaler.ECommerceProductService.Repository;

import com.scaler.ECommerceProductService.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Override
    List<Category> findAll();

    @Override
    @NonNull
    Optional<Category> findById(@NonNull String id);

    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}