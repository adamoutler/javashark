/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 *
 * @author adamo
 */
public class TestHandler extends AbstractPapaHandler {

    
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
        sendSucessString(t, sb.toString());

    }

}
