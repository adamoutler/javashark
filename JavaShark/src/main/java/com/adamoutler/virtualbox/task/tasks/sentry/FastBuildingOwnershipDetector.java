/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks.sentry;

import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;
import static com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy.NOBODY;
import static com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy.THEM;
import static com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy.US;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.Screenshottable;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import com.adamoutler.virtualbox.task.TaskType;
import com.babysharks.messaging.HashedMessage;
import com.babysharks.messaging.Message;
import com.babysharks.messaging.MessageData;
import com.babysharks.messaging.SendPapaAsynch;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author adamo
 */
public class FastBuildingOwnershipDetector {

    final static XYCoords BUILDING1 = new XYCoords(165, 353);
    final static XYCoords BUILDING2 = new XYCoords(110, 505);
    final static XYCoords BUILDING3 = new XYCoords(355, 595);
    final static XYCoords BUILDING4 = new XYCoords(630, 607);
    final static XYCoords BUILDING5 = new XYCoords(834, 512);
    final static XYCoords BUILDING6 = new XYCoords(818, 335);
    String gang;
    String username;
    VirtualBoxMachine vm;

    public FastBuildingOwnershipDetector(VirtualBoxMachine vm) {
        this.gang = vm.getGang();
        this.username = vm.getUsername();
        this.vm=vm;
    }

    /**
     * takes a screenshot of city kings and scans buildings for changes
     *
     * @param screenshot ck screenshot
     * @return an OwnedBy array
     * @throws Exception
     */
    public OwnedBy[] scan(BufferedImage screenshot) throws Exception {
        PicColors pc = new PicColors(screenshot);
        OwnedBy[] buildings = new OwnedBy[6];
        buildings[0] = (this.checkBuilding(BUILDING1, pc));
        buildings[1] = (this.checkBuilding(BUILDING2, pc));
        buildings[2] = (this.checkBuilding(BUILDING3, pc));
        buildings[3] = (this.checkBuilding(BUILDING4, pc));
        buildings[4] = (this.checkBuilding(BUILDING5, pc));
        buildings[5] = (this.checkBuilding(BUILDING6, pc));
        return buildings;
    }

    /**
     * runs the FastBuildingOwnershipDetector. Sends notifications to Papa.
     *
     */
    public void run() {
        try {
           
                vm.getData().current = scan(vm.screenshot());
                if (anyAreNull(vm.getData().current, vm.getData().previous)) {
                    System.out.println("Null detected!!!!!!");
                    for (int i = 0; i < 6; i++) {
                        System.out.println("C: " + vm.getData().current[i] + " P: " + vm.getData().previous[i]);
                    }
                    if (vm.getData().nullCount++ > 9) {
                        vm.setTaskComplete(true);
                        SendPapaAsynch.reasonWarning(vm, "offline");

                    }
                    return;
                }
                vm.getData().current = vm.getData().previous;
                vm.getData().nullCount = 0;

                reactToChangesInBuildingOwnership();
                vm.getData().previous = vm.getData().current;


        } catch (Exception ex) {
            Logger.getLogger(FastBuildingOwnershipDetector.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private void reactToChangesInBuildingOwnership() throws RestClientException {
        System.out.println(new Date() + " " + Arrays.asList(vm.getData().current));
        ArrayList<Integer> alertBuildings = getAlertBuildings(vm.getData().current, vm.getData().previous);
        maybeFreakOut(alertBuildings);
    }



    private void maybeFreakOut(ArrayList<Integer> alertBuildings) throws RestClientException {
        if (alertBuildings.size() > 0) {
            int[] alertBuildingsArray = alertBuildings.stream().mapToInt(i -> i).toArray();
            freakOut(alertBuildingsArray);
        }
    }

    ArrayList<Integer> getAlertBuildings(OwnedBy[] current, OwnedBy[] previous) {
        ArrayList<Integer> alertBuildings = new ArrayList<>();
        for (int i = 0; i < current.length; i++) {
            if (current[i] != previous[i] && previous[i].equals(OwnedBy.US)) {
                alertBuildings.add(i);

            }
        }
        return alertBuildings;
    }

    private static boolean anyAreNull(OwnedBy[] current, OwnedBy[] previous) {
        return current[0] == null || current[1] == null
                || current[2] == null || current[3] == null
                || current[4] == null || current[5] == null
                || previous[0] == null || previous[1] == null
                || previous[2] == null || previous[3] == null
                || previous[4] == null || previous[5] == null;
    }

   

   

    void freakOut(int[] buildings) throws RestClientException {
        MessageData data = configureData(buildings, vm);
        System.out.println("TIME TO FREAK OUT!!!!! OMFG");
        //https://catsbot-201602.appspot.com/adam_api/secret_shark_path_shhhh/async
        Message message = new Message();
        HashedMessage hm = new HashedMessage(message.setCommand("alert").setData(data));
        String s=SendPapaAsynch.sendToPapa(hm);
        System.out.println(s);

    }

    MessageData configureData(int[] alertBuildings, VirtualBoxMachine vm) {
        MessageData data = new MessageData().setAlertBuilding(alertBuildings)
                .setText("Help").setReason("alert").setBuildings(new String[]{
            vm.getData().current[0].getFriendlyname(),
            vm.getData().current[1].getFriendlyname(),
            vm.getData().current[2].getFriendlyname(), 
            vm.getData().current[3].getFriendlyname(),
            vm.getData().current[4].getFriendlyname(), 
            vm.getData().current[5].getFriendlyname()}
        ).setGang(gang).setActive(new ArrayList<>(Arrays.asList(vm)))
                .setUsername(username);

        ArrayList<Integer> weOwn = new ArrayList<>();
        ArrayList<Integer> theyOwn = new ArrayList<>();
        ArrayList<Integer> nobodyOwns = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            switch (vm.getData().current[i]) {
                case THEM:
                    theyOwn.add(i);
                    break;
                case US:
                    weOwn.add(i);
                    break;
                case NOBODY:
                    nobodyOwns.add(i);
                    break;
            }
        }
        data.setWeOwn(weOwn.stream().mapToInt(i->i).toArray());
        data.setTheyOwn(theyOwn.stream().mapToInt(i->i).toArray());
        data.setNobodyOwns(nobodyOwns.stream().mapToInt(i->i).toArray());
        return data;
    }

    private OwnedBy checkBuilding(XYCoords coords, PicColors screenshot) throws Exception {
        return getOwnership(screenshot, coords);
    }

    private OwnedBy getOwnership(PicColors screenshot1, XYCoords coords) throws Exception {
        if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.US.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {
            return OwnedBy.US;
        } else if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.THEM.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {
            return OwnedBy.THEM;
        } else if (screenshot1.scanRightXCoordinateForColorAtYCoordinate(OwnedBy.NOBODY.getColor(), coords.getX(), coords.getY(), coords.getX() + 100) > -1) {
            return OwnedBy.NOBODY;
        }
        return null;
    }

}
