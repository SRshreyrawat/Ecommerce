package com.project.Ecommerce.Dto;

import java.time.LocalDate;
import java.util.List;

import com.project.Ecommerce.Entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private List<OrderItem> orderitems;
    private LocalDate orderDate;
    private PaymentDTO payment;
    private double totalAmount;
    private String orderStatus;
    private Long addressId;
}
