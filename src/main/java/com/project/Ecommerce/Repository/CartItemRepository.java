package com.project.Ecommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.Ecommerce.Entity.CartItem;

public interface CartItemRepository extends JpaRepository<Long,CartItem> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id=?1 AND ci.product.id=?2")
    CartItem findCartItemByProductIdAndCartId(Long id, Long productId);

    void save(CartItem newCartItem);
    
}
