/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield;

import com.adamoutler.cats.battlefield.building.Building;
import com.adamoutler.cats.battlefield.building.BuildingFactory;
import com.adamoutler.cats.citykings.hash_gathering.TeamOwnershipPercentageBar;
import com.adamoutler.imagerecognition.ByteImage;
import com.adamoutler.imagerecognition.PicColors;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.virtualbox.VirtualBox;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author adamo
 */
public class BattlefieldFactory {

//    public static void main(String[] args) {
//        BattlefieldFactory bf = new BattlefieldFactory(true, new int[]{6,5,4,3,2,1});
//        VirtualBoxMachine vm;
//        ArrayList<VirtualBoxMachine> macs = new VirtualBox().getActiveMachines();
//        Battlefield battlefield=bf.scanBattleField(macs.get(2));
//        for(Building building:battlefield.getBuildings()){
//            System.out.println("problems:"+building.getSuspsectProblemReason());
//        }
//        
//    }
    boolean friendly;
    int[] whitelist;
    transient public final static PicCoords OURSCORECOORDS = new PicCoords(150, 135, 244, 45);
    transient public final static PicCoords THEIRSCORECOORDS = new PicCoords(632, 135, 244, 45);
    transient public final static PicCoords TARGETSCORECOORDS = new PicCoords(447, 149, 141, 38);
    final private Battlefield battlefield;

    public BattlefieldFactory(boolean friendly, int[] whitelist) {
        this.friendly = friendly;
        battlefield = new Battlefield(friendly);
        this.whitelist = whitelist;
    }

    public Battlefield scanBattleField(VirtualBoxMachine vm) {
        battlefield.setBuildings(prepareBuildings());
        for (Building building : battlefield.getBuildings()) {
            new BattlefieldNavigator().navigateToCK(vm);
            battlefield.setBoardScreenshot(vm.screenshot());
            boolean value = new BattlefieldNavigator().navigateToBuildingScreen(vm, building.getBuildingNumber());
            for (int retries = 0; retries < 3; retries++) {
                if (vm.isKillThread()) {
                    break;
                }
                if (new BuildingFactory(vm, building).scan()) {
                    break;
                }
            }
            if (building.isSuspectProblem() || vm.isKillThread()) {
                break;
            }
        }
        new BattlefieldNavigator().navigateToCK(vm);
        battlefield.setBoardScreenshot(vm.screenshot());

        return battlefield;
    }

    private LinkedList<Building> prepareBuildings() {
        LinkedList<Building> buildings = new LinkedList<>();
        for (int buildingNumber : whitelist) {
            Building b = new Building();
            b.setFriendlyScan(friendly);
            b.setBuildingNumber(buildingNumber);
            b.setIsLinked(isLinkedBuilding(b,this.battlefield.getBoardScreenshot()));
            buildings.add(b);
        }

        return buildings;
    }

    private static boolean isLinkedBuilding(Building ckb, BufferedImage newScreenshot) {
        PicCoords coords = Battlefield.buildingData.valueOf("BUILDING" + ckb.getBuildingNumber() + "NAME").getCoords();
        int startX = coords.getX();
        int y = coords.getY() + 7;
        int foundLoc = new PicColors(newScreenshot).scanRightXCoordinateForColorAtYCoordinate((int) 0xECAD21, startX, y, startX + 110, 10);
        return foundLoc > 0;
    }
}
