/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author adamo
 */
public class TestHandler implements HttpHandler {

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
        try (OutputStream os = t.getResponseBody()) {
            os.write(sb.toString().getBytes());
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
