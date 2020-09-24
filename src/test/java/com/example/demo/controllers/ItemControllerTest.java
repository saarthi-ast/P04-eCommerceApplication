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

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private ModifyCartRequest request;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository", itemRepository);

        Item item = new Item();
        item.setDescription("testItemDesc");
        item.setId(0l);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(12));

        List<Item> items = new ArrayList<Item>();
        items.add(item);

        when(itemRepository.findById(1l)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.findByName("testItem")).thenReturn(items);
        Item item2 = new Item();
        item2.setDescription("testItemDesc2");
        item2.setId(1l);
        item2.setName("testItem2");
        item2.setPrice(BigDecimal.valueOf(15));
        items.add(item2);
        when(itemRepository.findAll()).thenReturn(items);
    }

    @Test
    public void testfindItemById(){
        ResponseEntity<Item> response = itemController.getItemById(1l);
        assertNotNull(response);
        assertEquals(200,response.getStatusCode().value());
        assertEquals("testItem",response.getBody().getName());
    }

    @Test
    public void testfindItemByName(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");
        assertNotNull(response);
        assertEquals(200,response.getStatusCode().value());
        assertEquals("testItem",response.getBody().get(0).getName());
    }

    @Test
    public void testfindAllItems(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200,response.getStatusCode().value());
        assertEquals(2,response.getBody().size());
        assertEquals("testItem",response.getBody().get(0).getName());
        assertEquals("testItem2",response.getBody().get(1).getName());
    }
}
