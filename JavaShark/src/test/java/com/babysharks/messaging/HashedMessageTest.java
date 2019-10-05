/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adamo
 */
public class HashedMessageTest {

    public HashedMessageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of toJson method, of class HashedMessage.
     */
    @Test
    public void testToJson() {
        System.out.println("toJson");
        HashedMessage instance = new HashedMessage(new Message().setCommand("foo"));
        String result = instance.toJson();
        System.out.println(result);
    }

}
