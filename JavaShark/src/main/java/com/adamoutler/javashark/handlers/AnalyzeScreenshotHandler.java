package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.cats.screen_id.ScreenData;
import static com.babysharks.messaging.ErrorType.INVALIDFILE;
import static com.babysharks.messaging.ErrorType.NOERROR;
import static com.babysharks.messaging.ErrorType.UNRECOGNIZEDSCREEN;
import com.sun.net.httpserver.HttpExchange;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class AnalyzeScreenshotHandler extends AbstractPapaHandler {

    public AnalyzeScreenshotHandler() {

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String screenshotURL = request.getData().getUrl();
        String filename = screenshotURL.substring(screenshotURL.lastIndexOf("/"));
        if (new File(ScreenshotHandler.OUTPUT + File.separator +  filename.replaceAll(".jpg", ".png")).exists()){
            filename=filename.replaceAll(".jpg", ".png");
        }
        File screenshotFile = new File(ScreenshotHandler.OUTPUT + File.separator + filename);
        if (!screenshotFile.exists()) {
            request.getData().setErrorType(INVALIDFILE);
            this.sendBadReq(t, request);
        }
        BufferedImage image = ImageIO.read(screenshotFile);
        ScreenData d = ScreenData.identifyScreen(image);
        if (!d.equals(ScreenData.CITYKINGS)) {
            request.getData().setErrorType(UNRECOGNIZEDSCREEN);
            this.sendBadReq(t, request);
        }
        CityKingsBattlefieldAnalysis ckb = new CityKingsBattlefieldAnalysis(image);
        request.getData().setResultObject(ckb);
        request.getData().setErrorType(NOERROR);
        this.sendSucessMessage(t, request);
    }

}
