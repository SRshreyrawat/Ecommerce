package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Ecommerce.Entity.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {
    
}
