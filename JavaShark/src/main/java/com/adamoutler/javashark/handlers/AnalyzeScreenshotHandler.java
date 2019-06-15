package com.adamoutler.javashark.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import com.adamoutler.cats.screen_id.ScreenData;
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
        String screenshotURL=request.getData().getUrl();
        String filename=screenshotURL.substring(screenshotURL.lastIndexOf("/"));
        File screenshotFile=new File(ScreenshotHandler.OUTPUT+File.separator+filename);
        if (! screenshotFile.exists()){
            this.sendBadReq(t,"invalid file");
        }
        BufferedImage image=ImageIO.read(screenshotFile);
        ScreenData d=ScreenData.identifyScreen(image);
        if (!d.equals(ScreenData.CITYKINGS)){
            this.sendBadReq(t, "unrecognized screen");
        }
        CityKingsBattlefieldAnalysis ckb=new CityKingsBattlefieldAnalysis(image);
        request.getData().setResultObject(ckb);
       
        this.sendSucessMessage(t, request);
    }

}
