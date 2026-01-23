package com.project.Ecommerce.Service;

import java.util.List;

import com.project.Ecommerce.Dto.CartDTO;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();
    
}
