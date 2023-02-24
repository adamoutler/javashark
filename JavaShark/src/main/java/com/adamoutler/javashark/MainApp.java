/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark;

import com.adamoutler.Prop;
import com.adamoutler.javashark.handlers.AnalyzeScreenshotHandler;
import com.adamoutler.javashark.handlers.AuthorizedHandler;
import com.adamoutler.javashark.handlers.BattlefieldScan;
import com.adamoutler.javashark.handlers.CommandDistributor;
import com.adamoutler.javashark.handlers.HashHandler;
import com.adamoutler.javashark.handlers.HistoricalSearchHandler;
import com.adamoutler.javashark.handlers.JsonTestHandler;
import com.adamoutler.javashark.handlers.LastCarSearchHandler;
import com.adamoutler.javashark.handlers.MyCarsHandler;
import com.adamoutler.javashark.handlers.OverallStatusHandler;
import com.adamoutler.javashark.handlers.ScreenshotHandler;
import com.adamoutler.javashark.handlers.SentryStatusHandler;
import com.adamoutler.javashark.handlers.StartSentry;
import com.adamoutler.javashark.handlers.StopSentry;
import com.adamoutler.javashark.handlers.TaskHandler;
import com.adamoutler.javashark.handlers.TemporaryTaskHandler;
import com.adamoutler.javashark.handlers.TestHandler;
import com.adamoutler.javashark.handlers.TimeHandler;
import com.adamoutler.javashark.handlers.TrophyRecordHandler;
import com.adamoutler.javashark.handlers.UpdateHandler;
import com.adamoutler.virtualbox.task.TaskManager;
import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.babysharks.messaging.PapaAsync;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author adamo
 */
public class MainApp {

    private static String[] args;
    static Parameters p;
    public static Date onlineTime = new Date();
    public static String serverUrl;

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
        MainApp.args = args;
        Logger.getLogger("MainApp").log(Level.FINE, "args: {0}", Arrays.asList(args).toString());
        p = new Parameters(args);
    }

    public static void main(String[] args) {
        startADB();
        notifyPapa();
        MainApp server = new MainApp(args);
        TaskManager.start();
        String port = null;
        HashedMessage.setInstance(p.getNamed().getOrDefault("instance", "unnamed"));
        port = (p.getNamed().getOrDefault("port", "8000"));
        serverUrl=p.getNamed().getOrDefault("serverURL", "baby-sharks.hackedyour.info");

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
                Logger.getLogger(MainApp.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        });
        adb.setName("Starting ADB");
        adb.start();
    }

    private void startServer(int portNumber) throws IOException {
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
        commandDistributor.register("search", new HistoricalSearchHandler());
        commandDistributor.register("trophyrecord", new TrophyRecordHandler());

        commandDistributor.register("mycars", new MyCarsHandler());
        commandDistributor.register("lastcars", new LastCarSearchHandler());

        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext("/test", new TestHandler());
        server.createContext("/status", new OverallStatusHandler());

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

    public static Date getJenkinsBuildTime() {
        try {
            String url = "https://jenkins.adamoutler.com/view/The%20Baby%20Sharks/job/Baby%20Sharks%20Tools/lastBuild/api/xml?xpath=/*/timestamp";
            URL obj = new URL(url);
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode((Prop.getProp("jenkinsusername") + ":" + Prop.getProp("jenkinspass")).getBytes()));
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestMethod("GET");
            String response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine().replaceAll("/", "").replaceAll("<timestamp>", "");
            return new Date(Long.parseLong(response));

        } catch (MalformedURLException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date();
    }

}
