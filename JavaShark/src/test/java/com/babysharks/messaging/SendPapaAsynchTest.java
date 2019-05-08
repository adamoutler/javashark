/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public class SendPapaAsynchTest {

    String alert
            = "{\n"
            + "    \"command\": \"alert\",\n"
            + "    \"data\": {\n"
            + "      \"weOwn\": [2,3],\n"
            + "      \"theyOwn\": [0,1,4,5],\n"
            + "      \"nobodyOwns\": [],\n"
            + "      \"alertBuildings\": [\n"
            + "        4,\n"
            + "        5\n"
            + "      ],\n"
            + "      \"text\": \"Help\",\n"
            + "      \"reason\": \"alert\",\n"
            + "      \"vm\": \"Android6Marshmallow\",\n"
            + "      \"gang\": \"development\",\n"
            + "      \"username\": \"adamoutler\"\n"
            + "    },\n"
            + "    \"timestamp\": \"1550031134\"\n"
            + "  }";

    public SendPapaAsynchTest() {
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

    VirtualBoxMachine vm = new VirtualBox().getMachinesList().get(0);

    @Test
    public void testFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Message message = gson.fromJson(alert, Message.class);
        assertEquals(message.getCommand(), "alert");
        assertArrayEquals(message.getData().getWeOwn(), new int[]{2, 3});
        assertArrayEquals(message.getData().getTheyOwn(), new int[]{0, 1, 4, 5});
        assertArrayEquals(message.getData().getNobodyOwns(), new int[]{});
        assertArrayEquals(message.getData().getAlertBuilding(), new int[]{4, 5});

        assertEquals(message.getData().getText(), "Help");
        assertEquals(message.getData().getReason(), "alert");
        assertEquals(message.getData().getGang(), "development");
        assertEquals(message.getData().getUsername(), "adamoutler");

        System.out.println();

    }

    /**
     * Test of onlineWarning method, of class SendPapaAsynch.
     */
    @Test
    public void testOnlineWarning() {
        System.out.println("onlineWarning");
        SendPapaAsynch.onlineWarning(vm,true);
    }

    /**
     * Test of reasonWarning method, of class SendPapaAsynch.
     */
    @Test
    public void testReasonWarning() {
        System.out.println("reasonWarning");

        String reason = "PERFORMING UNIT TESTING";
        SendPapaAsynch.sentryReasonWarning(vm, reason);
    }

    /**
     * Test of sendToPapa method, of class SendPapaAsynch.
     */
    @Test
    public void testSendToPapa_Message() {
        System.out.println("sendToPapa");
        Message message = new Message().setCommand("UNIT TESTING");
        String expResult = "{\"error\":\"command failed processing\"}";
        String result = SendPapaAsynch.sendToPapa(message);
        assertEquals(expResult, result);

    }

    /**
     * Test of sendToPapa method, of class SendPapaAsynch.
     */
    @Test
    public void testSendToPapa_HashedMessage() {
        System.out.println("sendToPapa");
        Message mess = new Message().setCommand("alert").setData(new MessageData().setReason("UNIT TESTING"));
        HashedMessage hm = new HashedMessage(mess);
        String expResult = "{\"status\":\"success\"}";
       

        mess.getData().setText("help");
        mess.getData().setReason("alert");
        mess.getData().setGang("development");
        mess.getData().setUsername("AdamOutler");

        hm = new HashedMessage(mess);
        expResult = "{\"status\":\"success\"}";
        String result = SendPapaAsynch.sendToPapa(hm);
        assertEquals(expResult, result);

    }

}
