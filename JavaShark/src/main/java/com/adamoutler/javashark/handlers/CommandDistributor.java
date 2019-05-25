/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import static com.adamoutler.javashark.handlers.AbstractPapaHandler.convertStreamToString;
import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessagingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class CommandDistributor implements HttpHandler {

    private HashMap<String, AbstractPapaHandler> commands = new HashMap<>();

    public void register(String command, AbstractPapaHandler clazz) {
        commands.put(command, clazz);

    }

    public void parseCommand(HttpExchange t) throws MessagingException {
        Message m = init(t);
        for (Entry<String, AbstractPapaHandler> entryPair : commands.entrySet()) {
            if (entryPair.getKey().toLowerCase().equals(m.getCommand())) {
                AbstractPapaHandler aph = entryPair.getValue();
                aph.setMessage(m);
                handleCommand(aph, t);
            }
        }
    }

    private void handleCommand(AbstractPapaHandler aph, HttpExchange t) {
        try {
            System.out.println("handling with " + aph.getClass().getTypeName());
            aph.handle(t);
        } catch (Exception ex) {
            Logger.getLogger(CommandDistributor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            t.close();
        }
    }

    private void sendErrorMessage(HttpExchange t) {
        try {
            System.out.println("System exited improperly.  maybe disconnect on other side?");
            t.getResponseBody().write("I just rolled all giveable fucks into a large ball, molded it into a person who looks just like you, and kicked it in the balls.".getBytes("UTF-8"));
        } catch (IOException ex) {
            System.out.println("What? I couldn't even send the snide message");
        } finally {
            t.close();
        }
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        parseCommand(he);
    }

    public Message init(HttpExchange t) throws MessagingException {
        if (!t.getRequestMethod().equals("PATCH")) {
            throw new MessagingException("IMPROPER COMMUNICATIONS!");
        }
        String json = convertStreamToString(t.getRequestBody());
        System.out.println(new Date() + " Received new Command\n" + json);
        return HashedMessage.decode(json).getMessage();
    }

}
