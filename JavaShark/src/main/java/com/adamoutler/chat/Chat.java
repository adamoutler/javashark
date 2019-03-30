/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.chat;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author adamo
 */
@XmlRootElement
public class Chat {
    private ArrayList<Message> message=new ArrayList<>();

    /**
     * @return the message
     */
    public ArrayList<Message> getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(ArrayList<Message> message) {
        this.message = message;
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        for (Message m:message){
            sb.append("\n").append(m);
        }
        return sb.toString();
    }
    
}
