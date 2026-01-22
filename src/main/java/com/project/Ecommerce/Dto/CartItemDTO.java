package com.project.Ecommerce.Dto;

import com.project.Ecommerce.Entity.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Cart cart;
    private ProductDTO productDTO;
    private Integer quantity;
    private double discount;
    private double productPrice;

    
}
