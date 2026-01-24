package com.project.Ecommerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Ecommerce.Dto.OrderDTO;
import com.project.Ecommerce.Dto.OrderRequestDTO;
import com.project.Ecommerce.Service.OrderService;
import com.project.Ecommerce.Util.AuthUtil;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired 
    private AuthUtil authUtil;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(
            @PathVariable String paymentMethod,
            @RequestBody OrderRequestDTO orderRequestDTO) {

        String emailId = authUtil.loggedInEmail();

        OrderDTO order = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
