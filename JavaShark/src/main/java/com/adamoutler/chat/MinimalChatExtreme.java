/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;

;

/**
 *
 * @author adamo
 */
public class MinimalChatExtreme {

    private final String room;
    final String user;
    final File filename;
    final String serverUrl;

    /**
     * constructor
     *
     * @param user username
     * @param room room to chat into
     */
    public MinimalChatExtreme(String user, String room) {
        this.user = user;
        this.room = room;
         serverUrl="https://chat.adamoutler.com/";
        filename = new File(System.getProperty("java.io.tmpdir") + "/" + room + ".cht");
        System.out.println("Initalizing chat as " + user + " to http://chat.adamoutler.com/?room=" + room);
    }
/**
     * constructor
     *
     * @param server base url to server "https://chat.adamoutler.com"
     * @param user username
     * @param room room to chat into
     */
    public MinimalChatExtreme(String server,String user, String room) {
        this.user = user;
        this.room = room;
        this.serverUrl=server;
        filename = new File(System.getProperty("java.io.tmpdir") + "/" + room + ".cht");
        System.out.println("Initalizing chat as " + user + " to "+serverUrl+"/?room=" + room);
    }
    public static void main(String[] args) {

        System.out.println("New data available: " + new MinimalChatExtreme("test", "badazzeshypersecure").isNewDataAvailable());
        Long currentLength = new MinimalChatExtreme("test", "badazzeshypersecure").getContentLength();
        System.out.println("Last update from site:" + currentLength);
        MinimalChatExtreme mc = new MinimalChatExtreme("test", "badazzeshypersecure");
        // mc.acknowledgeMessage();
        // mc.sendMessage("test");
        // String s = mc.getText();

        Object o = mc.getContent();

        // System.out.println(s);
        System.out.println();
    }

    public long getContentLength() {
        try {
            URL url = new URL(serverUrl+"/rooms/" + getRoom() + ".txt");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = con.getResponseCode();
            String s = con.getHeaderField("Content-Length").replace("[", "").replace("]", "");
            con.disconnect();
            return Long.parseLong(s.trim());
        } catch (ProtocolException | MalformedURLException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0L;
    }

    boolean created = false;

    final public boolean acknowledgeMessage() {
        long l = getContentLength();

        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(Long.toString(l).getBytes());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    private void createNewFile() {
        if (!created) {
            try {
                filename.createNewFile();
                created = true;

            } catch (IOException ex1) {
                Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public boolean sendMessage(String message) {
        try {

            URL url = new URL(serverUrl+"/submit.php/?room=" + getRoom() + "&message=" + URLEncoder.encode(message, "UTF-8") + "&user=" + user);
            URLConnection conn = (URLConnection) url.openConnection();
            conn.getInputStream();

        } catch (MalformedURLException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }

    public String getText() {
        try {
            URL url = new URL(serverUrl+"/rooms/" + getRoom() + ".txt");
            URLConnection conn = (URLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (br.ready()) {
                sb.append(br.readLine());
            }
            return sb.toString();

        } catch (MalformedURLException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public boolean clear() {
        try {

            URL url = new URL(serverUrl+"/clear.php/?room=" + getRoom());
            URLConnection conn = (URLConnection) url.openConnection();
            conn.getInputStream().close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;

    }

    public boolean isNewDataAvailable() {
        byte[] bytes = new byte[200];

        try {
            try (FileInputStream fis = new FileInputStream(filename)) {
                fis.read(bytes);
                String value = new String(bytes);
                Long stored = Long.parseLong(value.trim());
                Long current = getContentLength();
                if (!current.equals(stored)) {
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            acknowledgeMessage();
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            acknowledgeMessage();
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private Chat getContent() {
        Chat c = new Chat();
        try {
            List<Node> elements = Jsoup.connect(serverUrl+"/rooms/" + getRoom() + ".txt").get().body().childNodes();
            for (Node messageitem : elements.toArray(new Node[elements.size()])) {
                Message m = new Message();
                List<Node> nodes = messageitem.childNodes();
                try {
                    m.setContent((nodes.get(0).childNode(0).toString().trim()));
                    m.setUsername(nodes.get(1).childNode(0).childNode(0).toString().trim());
                    m.setTime(Long.parseLong(nodes.get(1).childNode(2).childNode(0).toString().trim()));
                } catch (IndexOutOfBoundsException ex) {
                    //invalid message we don't care about this.
                }
                c.getMessage().add(m);
            }
        } catch (IOException ex) {
            Logger.getLogger(MinimalChatExtreme.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    /**
     * @return the room
     */
    public String getRoom() {
        return room;
    }

}
