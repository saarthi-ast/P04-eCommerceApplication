package com.example.demo.model.persistence;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ItemTest {
    @Test
    public void testHashCode(){
        Item item = new Item();
        assertNotNull(item.hashCode());
    }

    @Test
    public void testEquals(){
        Item item = new Item();
        item.setDescription("testItemDesc");
        item.setId(0l);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(12));

        Item item2 = new Item();
        item2.setDescription("testItemDesc");
        item2.setId(0l);
        item2.setName("testItem");
        item2.setPrice(BigDecimal.valueOf(12));

        Item item3 = new Item();
        item3.setDescription("testItemDesc12");
        item3.setId(null);
        item3.setName("testItem12");
        item3.setPrice(BigDecimal.valueOf(18));

        User user = new User();

        assertEquals(item,item);
        assertEquals(item,item2);
        assertNotEquals(item,item3);
        assertNotEquals(item3,item);
        assertNotEquals(null,item3);
        assertNotEquals(user,item3);
        assertNotEquals(item,null);
    }
}
