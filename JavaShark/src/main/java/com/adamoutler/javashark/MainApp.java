/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark;

import com.adamoutler.libminimalchatextrem.MinimalChatExtreme;
import com.adamoutler.javashark.handlers.CommandDistributor;
import com.adamoutler.javashark.handlers.JsonTestHandler;
import com.adamoutler.javashark.handlers.ScreenshotHandler;
import com.adamoutler.javashark.handlers.SentryStatus;
import com.adamoutler.javashark.handlers.StartDeepBuildingScan;
import com.adamoutler.javashark.handlers.StartSentry;
import com.adamoutler.javashark.handlers.StopSentry;
import com.adamoutler.javashark.handlers.TaskHandler;
import com.adamoutler.javashark.handlers.TestHandler;
import com.adamoutler.javashark.handlers.TimeHandler;
import com.adamoutler.javashark.handlers.UpdateHandler;
import com.adamoutler.virtualbox.task.TaskManager;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.babysharks.messaging.SendPapaAsynch;
import com.sun.javafx.application.ParametersImpl;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class MainApp extends ParametersImpl {

    public static MinimalChatExtreme chat = new MinimalChatExtreme("JavaShark", generateRandom("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"));

    private static void notifyPapa() {
        Thread t = new Thread(() -> {
            Message message = new Message().setCommand("JavaSharkOnline")
                    .setData(new MessageData()
                            .setReason("Javashark Has Come Online. "
                                    + "Please silence notifications "
                                    + "for 10 seconds"));

            SendPapaAsynch.sendToPapa(message);
        });
        t.setName("Notify Papa Online");
        t.start();
    }

    MainApp(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        startADB();
        notifyPapa();
        MainApp server = new MainApp(args);
        TaskManager.start();
        String port = server.getNamed().getOrDefault("port", "8000");
        try {
            System.out.println("Starting server on " + port);
            server.startServer(Integer.parseInt(port));
        } catch (IOException ex) {
            System.err.print("Could not start server due to port error. please specify --port=8382 or whatever. ");
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void startADB() {
        Thread adb = new Thread(() -> {
            ProcessBuilder pb = new ProcessBuilder(new String[]{"adb", "devices"});
            try {
                pb.start();
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        adb.setName("Starting ADB");
        adb.start();
    }

    private void startServer(int portNumber) throws IOException {
        System.out.println(this.getRaw());
        CommandDistributor commandDistributor = new CommandDistributor();
        commandDistributor.register("time", new TimeHandler());
        commandDistributor.register("screenshot", new ScreenshotHandler());
        commandDistributor.register("json", new JsonTestHandler());
        commandDistributor.register("test", new TestHandler());
        commandDistributor.register("sentrystatus", new SentryStatus());
        commandDistributor.register("sentrystart", new StartSentry());
        commandDistributor.register("deepbuildingscan", new StartDeepBuildingScan());
        commandDistributor.register("sentrystop", new StopSentry());
        commandDistributor.register("addtask", new TaskHandler());
        commandDistributor.register("updatecats", new UpdateHandler());
        

        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext("/test", new TestHandler());
        server.createContext("/command", commandDistributor);
        server.setExecutor(new DefaultExecutor()); // creates a default executor
        server.start();

    }

    private static String generateRandom(String aToZ) {
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 17; i++) {
            int randIndex = rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }

    private static class DefaultExecutor implements Executor {

        DefaultExecutor() {
            Thread.currentThread().setName("Default Executor");

        }

        public void execute(Runnable task) {
            
            task.run();
        }
    }
}
