package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;


    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        LOG.info("SUBMIT_ORDER_REQUEST received for user={}.", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            LOG.error("SUBMIT_ORDER_FAILURE=\"User not found\" received for user={}.", username);
            return ResponseEntity.notFound().build();
        }
        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);
        LOG.info("SUBMIT_ORDER_REQUEST saved successfully for user={}.", username);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        LOG.info("USER_ORDER_HISTORY_REQUEST received for user={}.", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            LOG.error("USER_ORDER_HISTORY_FAILURE=\"User not found.\" for user={}.", username);
            return ResponseEntity.notFound().build();
        }
        LOG.info("USER_ORDER_HISTORY_REQUEST successful for user={}.", username);
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
