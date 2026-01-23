package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.Ecommerce.Entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email=?1")
    Cart findCartByEmail(String email);

    Cart save(Cart cart);
    
}
