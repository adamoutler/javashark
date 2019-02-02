/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.babyrequestresponder;

import com.sun.javafx.application.ParametersImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class Server extends ParametersImpl {

    Server(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        Server server = new Server(args);
        String port = server.getNamed().getOrDefault("port", "53329");
        try {
            server.startServer(Integer.parseInt(port));
        } catch (IOException ex) {
            System.err.print("Could not start server due to port error");
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startServer(int portNumber) throws IOException {
        System.out.println(this.getRaw());

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();

    }

    class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            StringBuilder sb = new StringBuilder();
            String n = "\n";
            sb.append("local address: ").append(t.getLocalAddress().toString()).append(n);
            sb.append("remote address: ").append(t.getRemoteAddress()).append(n);
            sb.append("Protocol: ").append(t.getProtocol()).append(n);
            sb.append("URI: ").append(t.getRequestURI().toString()).append(n);
            sb.append("request method: ").append(t.getRequestMethod()).append(n);

            sb.append("---request headers---").append(n).
                    append(t.getRequestHeaders().entrySet().toString()
                            .replaceAll(", ", "\n").replaceAll("\\[", "")
                            .replaceAll("\\]", "")).append(n);
            sb.append("---request body---").append(n)
                    .append(convertStreamToString(t.getRequestBody()))
                    .append(n).append("---end of request---").append(n);
            
            sb.append(n).append(response).append(n);
            t.sendResponseHeaders(200, sb.toString().length());
            OutputStream os = t.getResponseBody();
            os.write(sb.toString().getBytes());
            os.close();
        }

    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
