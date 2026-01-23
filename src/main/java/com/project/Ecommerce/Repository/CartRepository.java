package com.project.Ecommerce.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.Ecommerce.Entity.Cart;
import com.project.Ecommerce.Entity.Product;
import com.project.Ecommerce.Entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email=?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email=?1AND c.id=?2")
    Cart findCartByEmailAndCartId(String email,Long cartId);

    Cart save(Cart cart);

    Optional<Product> findByUser(User user);
    
    
    
}
