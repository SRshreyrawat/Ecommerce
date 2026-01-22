package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Ecommerce.Entity.Cart;

public interface CartRepository extends JpaRepository<Long , Cart> {
    
}
