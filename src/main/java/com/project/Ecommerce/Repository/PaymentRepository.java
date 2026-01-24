package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Ecommerce.Entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    
}
