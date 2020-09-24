package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ModifyCartRequest request;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository", userRepository);
        TestUtils.injectObjects(cartController,"cartRepository", cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository", itemRepository);

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

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1l)).thenReturn(Optional.ofNullable(item));
        when(cartRepository.save(any())).thenReturn(cart);
    }

    @Test
    public void testAddCart(){
        final ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200,response.getStatusCode().value());
        assertEquals("testItem",response.getBody().getItems().get(0).getName());
    }

    @Test
    public void testRemoveFromCart(){
        final ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200,response.getStatusCode().value());
        assertEquals(0,response.getBody().getItems().size());
    }

    @Test
    public void testAddCartFailureUserMissing(){
        request.setUsername("badUser");
        final ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404,response.getStatusCode().value());
    }

    @Test
    public void testAddCartFailureItemUserMissing(){
        request.setItemId(-5l);
        final ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404,response.getStatusCode().value());
    }


    @Test
    public void testRemoveFromCartFailureUserMissing(){
        request.setUsername("badUser");
        final ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404,response.getStatusCode().value());
    }

    @Test
    public void testRemoveFromCartFailureItemUserMissing(){
        request.setItemId(-5l);
        final ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(404,response.getStatusCode().value());
    }
}
