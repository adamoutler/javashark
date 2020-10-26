/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adamo
 */
public class VirtualBoxTest {

    public VirtualBoxTest() {
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
     * Test of processVMList method, of class VirtualBox.
     */
    @Test
    public void testProcessVMList() {
        System.out.println("processVMList");
        String value = "\"Debian Testing\" {c09ce3b3-bd2f-4909-ab8a-bb21fe52a386}\n"
                + "\"development_adamoutler_5558_10551\" {f75a2149-fbd0-4683-ad68-1099e1faa301}\n"
                + "\"Android 6.0 emulator\" {52067849-d622-4a09-9762-7940fc35c380}\n"
                + "\"android 32 bit emulator\" {2a1fcf82-39c3-4916-8253-ed894c90dde0}\n"
                + "\"development_javashark_5560_10553\" {92678bd8-e789-4733-8222-bf5c1b6b8555}\n"
                + "\"development_adamoutler_5562_10552\" {5b9f916e-f816-408b-9514-2b8a0a69ad37}";
        ArrayList<VirtualBoxMachine> al = new ArrayList<>();
        VirtualBox instance = new VirtualBox();
        instance.processVMList(value, al);
        VirtualBoxMachine m0 = al.get(0);
        VirtualBoxMachine m1 = al.get(1);
        VirtualBoxMachine m2 = al.get(2);
        assertEquals("127.0.0.1:5558", m0.getAdbName());
        assertEquals("development", m0.getGang());
        assertEquals("javashark", m1.getUsername());
        assertEquals("10553", m1.getRdpPort());
        assertEquals("5562", m2.getPortNumber());
        assertEquals(5562, m2.getPortNumberInt());
    }

}
