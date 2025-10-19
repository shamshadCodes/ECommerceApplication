package com.scaler.ECommerceProductService.Repository;

import com.scaler.ECommerceProductService.model.Category;
import com.scaler.ECommerceProductService.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Override
    @NonNull
    List<Product> findAll();

    @NonNull
    @Override
    Optional<Product> findById(@NonNull String id);

    @NonNull
    @Override
    <S extends Product> S save(@NonNull S product);

    Optional<Product> findByTitleIgnoreCase(String title);

    List<Product> findAllByCategory(Category category);

    @Query("SELECT p FROM products p WHERE " +
            "(:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:category IS NULL OR LOWER(p.category.categoryName) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(:minPrice IS NULL OR p.price.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price.price <= :maxPrice)")
    Page<Product> searchProducts(@Param("title") String title,
                                 @Param("category") String category,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice,
                                 Pageable pageable);
}
