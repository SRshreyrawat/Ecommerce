package com.project.Ecommerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.Ecommerce.Dto.CartDTO;
import com.project.Ecommerce.Service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {

        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);

        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }
}
