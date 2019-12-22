/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.Prop;
import com.adamoutler.cats.screen_id.Hashes;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.cats.screen_id.ScreenDescription;
import com.adamoutler.imagerecognition.HashedPicCoords;
import com.babysharks.messaging.ErrorType;
import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.MessageData;
import com.sun.net.httpserver.HttpExchange;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;

/**
 * list the possible values of screenname for this calibration curl -X PATCH -d
 * '{"message":{"command":"listhashes"}}' list the possible values of screenname
 * along with descriptions curl -X PATCH -d
 * '{"message":{"command":"listhashesdecription"}}' calibrate the picture as a
 * new member of this screen name curl -X PATCH -d
 * '{"message":{"command":"hashcalibrate","data":{"png":"https://javashark.baby-sharks.com/excel/blind234234.png",
 * ScreenName:"CITYKINGSENLIST"}}}' remove all previous calibration values and
 * calibrate this picture curl -X PATCH -d
 * '{"message":{"command":"hashfreshcalibrate","data":{"png":"https://javashark.baby-sharks.com/excel/blind234234.png",
 * ScreenName:"CITYKINGSENLIST"}}}' calculate and delete the hash of this
 * picture curl -X PATCH -d
 * '{"message":{"command":"hashdelete","data":{"png":"https://javashark.baby-sharks.com/excel/blind234234.png",
 * ScreenName:"CITYKINGSENLIST"}}}'
 *
 *
 * https://baby-sharks.adamoutler.com/api/dev/command
 *
 * @author adamo
 */
public class HashHandler extends AbstractPapaHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        MessageData m = getRequest().getData();

        if (getRequest().getCommand().equals("listcalibrations")) {
            ArrayList<ScreenDescription> al = new ArrayList<>();
            for (ScreenData screenDataItem : ScreenData.values()) {
                al.add(new ScreenDescription(screenDataItem.name(), screenDataItem.getDescription(), screenDataItem.getPicCoords()));

            }
            this.getRequest().getData().setResultObject(al);
            this.sendSucessMessage(he, request);
            return;
        }
        String args = m.getArgs();
        boolean calibrate = args.contains("calibrate");
        boolean clear = args.contains("clear");
        boolean delete = args.contains("delete");
        boolean reload = args.contains("reload");

        if (delete) {
            String png2 = this.getRequest().getData().getPng();
            String filename2 = png2.replaceAll(".*/", "").replace(".jpg", ".png");
            File file2 = new File(Prop.getProp("screenshots") + filename2);
            ScreenData sd2 = getScreenEnum(m);
            try {
                BufferedImage bi2 = ImageIO.read(file2);
                m.setComment("Previous entry:" + sd2.getHashesPropertyEntry());
                boolean deleted = sd2.deleteHash(bi2);
                if (!deleted) {
                    m.setErrorType(ErrorType.DELETEREQUESTINVALID);
                    this.sendBadReq(he, request);
                    return;
                } else {
                    m.setResultObject(sd2.getNewHash(bi2));
                    m.setText(sd2.getHashesPropertyEntry());
                }
                this.sendSucessMessage(he, request);
                System.out.println("foo");
                return;
            } catch (IIOException ex) {
                m.setReason("Invalid file");
                m.setErrorType(ErrorType.COULDNOTFINDMATCHINGRECORDS);
                this.sendBadReq(he, request);

                return;
            }

        }

        if (reload) {
            if (HashedMessage.instance == null || HashedMessage.instance.isEmpty()) {
                // this is development
                Hashes.writeProperties();
                this.sendSucessMessage(he, request);
            } else {
                this.sendSucessMessage(he, request);
            }
            return;
        }

        try {

            if (clear || calibrate) {

                String png = this.getRequest().getData().getPng();
                String filename = png.replaceAll(".*/", "").replace(".jpg", ".png");
                File file = new File(Prop.getProp("screenshots") + filename);
                ScreenData sd = getScreenEnum(m);
                BufferedImage bi = ImageIO.read(file);
                HashedPicCoords hashedPicCords = sd.addNewHash(bi, clear);
                m.setResultObject(hashedPicCords);
                m.setText(sd.getHashesPropertyEntry());
                this.sendSucessMessage(he, request);
                return;
            }

        } catch (Exception ex) {
            m.setErrorType(ErrorType.FILEHASHERROR);
            this.sendBadReq(he, request);

        }

        m.setReason(
                "invalid combination of args");

        this.sendSucessMessage(he, request);
    }

    private ScreenData getScreenEnum(MessageData m) {
        return ScreenData.valueOf(m.getScreenName());
    }

}
