package com.project.Ecommerce.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Ecommerce.Entity.Category;
import com.project.Ecommerce.Entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findBYCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String string, Pageable pageDetails);

   
}
