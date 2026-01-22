package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Ecommerce.Entity.CartItem;

public interface CartItemRepository extends JpaRepository<Long,CartItem> {
    
}
