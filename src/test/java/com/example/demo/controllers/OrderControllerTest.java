package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private ModifyCartRequest request;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        request = new ModifyCartRequest();
        request.setItemId(1l);
        request.setQuantity(1);
        request.setUsername("test");

        Item item = new Item();
        item.setDescription("testItemDesc");
        item.setId(0l);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(12));

        Cart cart = new Cart();
        cart.setId(0l);
        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);

        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setId(0l);
        user.setCart(cart);
        UserOrder userOrder = UserOrder.createFromCart(cart);
        userOrder.setId(1l);
        List<UserOrder> orders = new ArrayList<>();
        orders.add(userOrder);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.save(userOrder)).thenReturn(userOrder);
        when(orderRepository.findByUser(user)).thenReturn(orders);
    }

    @Test
    public void testSubmitOrder() {
        final ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("testItem", response.getBody().getItems().get(0).getName());
    }

    @Test
    public void testOrderHistory() {
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("testItem", response.getBody().get(0).getItems().get(0).getName());
    }

}
