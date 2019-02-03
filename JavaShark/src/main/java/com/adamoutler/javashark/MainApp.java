/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark;

import com.sun.javafx.application.ParametersImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class MainApp extends ParametersImpl {

    MainApp(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        MainApp server = new MainApp(args);
        String port = server.getNamed().getOrDefault("port", "8000");
        try {
            server.startServer(Integer.parseInt(port));
        } catch (IOException ex) {
            System.err.print("Could not start server due to port error. please specify --port=8382 or whatever. ");
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startServer(int portNumber) throws IOException {
        System.out.println(this.getRaw());

        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext("/test", new TestHandler());
        server.createContext("/json", new JsonTestHandler());

        server.setExecutor(null); // creates a default executor
        server.start();

    }

   
}
