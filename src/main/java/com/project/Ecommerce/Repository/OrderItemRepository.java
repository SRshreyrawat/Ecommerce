package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Ecommerce.Entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    
}
