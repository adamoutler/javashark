/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark;

import com.adamoutler.javashark.handlers.AnalyzeScreenshotHandler;
import com.adamoutler.javashark.handlers.AuthorizedHandler;
import com.adamoutler.javashark.handlers.BattlefieldScan;
import com.adamoutler.javashark.handlers.CommandDistributor;
import com.adamoutler.javashark.handlers.HashHandler;
import com.adamoutler.javashark.handlers.HistoricalSearchHandler;
import com.adamoutler.javashark.handlers.JsonTestHandler;
import com.adamoutler.javashark.handlers.LastCarSearchHandler;
import com.adamoutler.javashark.handlers.MyCarsHandler;
import com.adamoutler.javashark.handlers.ScreenshotHandler;
import com.adamoutler.javashark.handlers.SentryStatusHandler;
import com.adamoutler.javashark.handlers.StartSentry;
import com.adamoutler.javashark.handlers.StopSentry;
import com.adamoutler.javashark.handlers.TaskHandler;
import com.adamoutler.javashark.handlers.TemporaryTaskHandler;
import com.adamoutler.javashark.handlers.TestHandler;
import com.adamoutler.javashark.handlers.TimeHandler;
import com.adamoutler.javashark.handlers.UpdateHandler;
import com.adamoutler.virtualbox.task.TaskManager;
import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.babysharks.messaging.PapaAsync;
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

    private static void notifyPapa() {
        Thread t = new Thread(() -> {
            Message message = new Message().setCommand("JavaSharkOnline")
                    .setData(new MessageData()
                            .setReason("Javashark Has Come Online. "
                                    + "Please silence notifications "
                                    + "for 10 seconds"));

            PapaAsync.send(message);
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
        HashedMessage.setInstance(server.getNamed().getOrDefault("instance", "development"));
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
        commandDistributor.register("sentrystatus", new SentryStatusHandler());
        commandDistributor.register("sentrystart", new StartSentry());
        commandDistributor.register("deepbuildingscan", new BattlefieldScan());
        commandDistributor.register("deepbuildingrecon", new BattlefieldScan());
        commandDistributor.register("sentrystop", new StopSentry());
        commandDistributor.register("addtask", new TaskHandler());
        commandDistributor.register("addsidetask", new TemporaryTaskHandler());
        commandDistributor.register("updatecats", new UpdateHandler());
        commandDistributor.register("whoauthorizedthis", new AuthorizedHandler());
        commandDistributor.register("analyzescreen", new AnalyzeScreenshotHandler());
        commandDistributor.register("calibrate", new HashHandler());
        commandDistributor.register("hashdelete", new HashHandler());
        commandDistributor.register("hashReload", new HashHandler());
        commandDistributor.register("listcalibrations", new HashHandler());
        commandDistributor.register("search",new HistoricalSearchHandler());
        
        commandDistributor.register("mycars", new MyCarsHandler());
        commandDistributor.register("lastcars", new LastCarSearchHandler());

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
