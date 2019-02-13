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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class CommandDistributor implements HttpHandler {

    private  HashMap<String, AbstractPapaHandler> commands = new HashMap<>();

    public  void register(String command, AbstractPapaHandler clazz) {
        commands.put(command, clazz);

    }

    public  void parseCommand(HttpExchange t) throws MessagingException {
        Message m = init(t);

        commands.entrySet().stream().filter((entryPair) -> 
                (entryPair.getKey().equals(m.getCommand())))
                .forEachOrdered((entryPair) -> {
                AbstractPapaHandler aph = entryPair.getValue();
                aph.setMessage(m);
            try {
                aph.handle(t);
            } catch (IOException ex) {
                Logger.getLogger(CommandDistributor.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        });
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
       parseCommand(he);
    }

    public  Message init(HttpExchange t) throws MessagingException {
        if (!t.getRequestMethod().equals("PATCH")) {
            throw new MessagingException("IMPROPER COMMUNICATIONS!");
        }
        System.out.println("Received "+t.getProtocol()+" command.");
        String json = convertStreamToString(t.getRequestBody());
        return HashedMessage.decode(json).getMessage();
    }

}
