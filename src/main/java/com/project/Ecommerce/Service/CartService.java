package com.project.Ecommerce.Service;

import com.project.Ecommerce.Dto.CartDTO;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);
    
}
