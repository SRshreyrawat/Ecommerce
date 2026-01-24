package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Ecommerce.Entity.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
    
}
