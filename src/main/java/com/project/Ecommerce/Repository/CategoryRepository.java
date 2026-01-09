package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.Ecommerce.Entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{

    Category findByCategoryName(String categoryName);
    
}
