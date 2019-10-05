/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.records_search.SearchException;
import com.adamoutler.records_search.search_assemblers.HistoricalImageSearch;
import static com.babysharks.messaging.ErrorType.COULDNOTFINDMATCHINGRECORDS;
import static com.babysharks.messaging.ErrorType.COULDNOTFINDREQUESTEDUSERINRECORDS;
import com.sun.net.httpserver.HttpExchange;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class LastCarSearchHandler extends AbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {

        String hash = request.getData().getHashes().entrySet().iterator().next().getKey();
        try {
            BufferedImage bi = HistoricalImageSearch.getLastCars(hash, "/var/www/baby-sharks/excel", request.getData().getGang());
            if (bi.getWidth() == 1) {
                request.getData().setErrorType(COULDNOTFINDREQUESTEDUSERINRECORDS);

                this.sendArbitrary(404, he, "Could not find user");
            }

            String outputFolder = "/var/www/baby-sharks/screenshots/";
            String safeName = "LastCars-" + request.getData().getGang() + "-"
                    + request.getData().getUsername()
                            .replaceAll("[^a-zA-Z0-9]", "")
                    + UUID.randomUUID().toString() + ".png";
            File output = new File(outputFolder + safeName);
            ImageIO.write(bi, "PNG", output);
            request.getData().setReason("Acquired images")
                    .setUrl("https://javashark.baby-sharks.com/excel/"
                            + output.getName());
            this.sendSucessMessage(he, request);
        } catch (SearchException ex) {
            request.getData().setErrorType(COULDNOTFINDMATCHINGRECORDS);
            this.sendArbitrary(404, he, "Could not find matching records");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

}
