/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.HashedPicCoords;
import com.adamoutler.imagerecognition.HashedPicCoordsPropertiesAccessor;
import com.adamoutler.imagerecognition.PicCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

;

/**
 *
 * @author adamo
 */
public enum ScreenData {
        UNKNOWN(HashedPicCoordsPropertiesAccessor.factory("UNKNOWN"), "Unidentified"),
        OFF(HashedPicCoordsPropertiesAccessor.factory("OFF"), "Turned off"),
        BLACKSCREEN(HashedPicCoordsPropertiesAccessor.factory("BLACKSCREEN"), "A black screen"),
        BOOTED(HashedPicCoordsPropertiesAccessor.factory("BOOTED"), "Android has booted"),
        //    BOXOPENNOW(new HashedPicCoords[]{new HashedPicCoords(370, 600, 263, 42, 1884123401)}, new ColoredXYCoords[]{}, "Box Screen, Click to open box"),
        //    BOXOPENNOWSKIP(new HashedPicCoords[]{new HashedPicCoords(566, 603, 197, 43, -410747955)}, new ColoredXYCoords[]{}, "Bos screen, open now or watch video"),
        //    BOXUNLOCK(new HashedPicCoords[]{new HashedPicCoords(370, 600, 263, 42, -1701044878)}, new ColoredXYCoords[]{}, "box screen, click to start 2-hour timer"),
        CHAMPIONSHIPGARAGE(HashedPicCoordsPropertiesAccessor.factory("CHAMPIONSHIPGARAGE"), "In Championship Garage working on car"),
        CATSLOADEDCHAMPIONSHIPSCREEN(HashedPicCoordsPropertiesAccessor.factory("CATSLOADEDCHAMPIONSHIPSCREEN"), "CATS loaded, on Championship Home screen"),
        CATSLOADEDULTIMATESCREEN(HashedPicCoordsPropertiesAccessor.factory("CATSLOADEDULTIMATESCREEN"), "CATS Loaded, on Ultimate Home screen"),
        CATSLOADINGSCREEN(HashedPicCoordsPropertiesAccessor.factory("CATSLOADINGSCREEN"), "In CATS Loading screen"),
        //    CHAT(new HashedPicCoords[]{new HashedPicCoords(574, 131, 90, 90, -1923340452)}, new ColoredXYCoords[]{}, "Gang Chat screen"),
        CITYKINGCAR1(HashedPicCoordsPropertiesAccessor.factory("CITYKINGCAR1"), "City Kings, Examining Sentry Owner Car 1 "),
        CITYKINGCAR2(HashedPicCoordsPropertiesAccessor.factory("CITYKINGCAR2"), "City Kings, Examining Sentry Owner Car 2 "),
        CITYKINGCAR3(HashedPicCoordsPropertiesAccessor.factory("CITYKINGCAR3"), "City Kings, Examining Sentry Owner Car 3 "),
        CITYKINGSBEGINTHEATTACK(HashedPicCoordsPropertiesAccessor.factory("CITYKINGSBEGINTHEATTACK"), "Begin the attack, waiting for opponent"),
        CITYKINGSENLIST(HashedPicCoordsPropertiesAccessor.factory("CITYKINGSENLIST"), "City Kings, Enlist screen"),
        CITYKINGS(HashedPicCoordsPropertiesAccessor.factory("CITYKINGS"), "City Kings, battlefield screen"),
        CITYKINGSBUILDINGNOSCOUT(HashedPicCoordsPropertiesAccessor.factory("CITYKINGSBUILDINGNOSCOUT"), "City Kings, building Attack Screen, no scout"),
        CITYKINGSBUILDINGATTACK(HashedPicCoordsPropertiesAccessor.factory("CITYKINGSBUILDINGATTACK"), "City Kings, Building Attack screen"),
        CITYKINGSBUILDINGDEFEND(HashedPicCoordsPropertiesAccessor.factory("CITYKINGSBUILDINGDEFEND"), "City Kings, building defend screen"),
        CITYKINGSBUILDINGSCREEN(HashedPicCoordsPropertiesAccessor.factory("CITYKINGSBUILDINGSCREEN"), "City Kings, building main screen"),
        //    CITYKINGSGANGINFO(new HashedPicCoords[]{new HashedPicCoords(412, 574, 181, 46, -988303182)}, new ColoredXYCoords[]{}, "City Kings, gang info screen"),
        //    CITYKINGSUNCLETONY(new HashedPicCoords[]{new HashedPicCoords(225, 85, 240, 54, 782978378), new HashedPicCoords(225, 85, 240, 54, 774163899)}, new ColoredXYCoords[]{}, "City Kings, Uncle Tony screen"),
        CITYKINGSVICTORY(HashedPicCoordsPropertiesAccessor.factory("CITYKINGSVICTORY"), "City Kings, Victory Screen"),
        CITYKINGSDEFEAT(HashedPicCoordsPropertiesAccessor.factory("CITYKINGSDEFEAT"), "Never happened."),
        EXITSCREEN(HashedPicCoordsPropertiesAccessor.factory("EXITSCREEN"), "Are you sure you want to quit?"),
        FRIENDS(HashedPicCoordsPropertiesAccessor.factory("FRIENDS"), "Gang Friends screen"),
        GANGFIGHTS(HashedPicCoordsPropertiesAccessor.factory("GANGFIGHTS"), "Gang Fights, ready for action screen"),
        GANGFIGHTSBATTLESCREEN(HashedPicCoordsPropertiesAccessor.factory("GANGFIGHTSBATTLESCREEN"), "Gang Fights, battle screen"),
        GANGFIGHTSCOMPLETE(HashedPicCoordsPropertiesAccessor.factory("GANGFIGHTSCOMPLETE"), "Gang Fights, complete and ready for reset screen"),
        GANGFIGHTSDEFEAT(HashedPicCoordsPropertiesAccessor.factory("GANGFIGHTSDEFEAT"), "Gang Fights, defeated screen"),
        GANGFIGHTSVICTORY(HashedPicCoordsPropertiesAccessor.factory("GANGFIGHTSVICTORY"), "Gang Fights, victory screen"),
        //    FACEBOOK(new HashedPicCoords[]{new HashedPicCoords(452, 49, 137, 29, -1556342175)}, new ColoredXYCoords[]{}, "Facebook login"),
        //    JOINALEAGUE(new HashedPicCoords[]{new HashedPicCoords(399, 168, 209, 41, 2014501043)}, new ColoredXYCoords[]{}, "CATS Loaded, on Ultimate Home screen, new league available"),
        //    LEAGUEFIGHTDEFEAT(new HashedPicCoords[]{new HashedPicCoords(430, 600, 120, 37, -1685251910)}, new ColoredXYCoords[]{}, "Ultimate League, defeat screen"),
        //    LEAGUEFIGHTRANKING(new HashedPicCoords[]{new HashedPicCoords(456, 162, 131, 43, 149289225)}, new ColoredXYCoords[]{}, "Ultimate League fight complete, ranking screen"),
        //    LEAGUEFIGHTWON(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Ultimate League, fight won"),
        //    MOVIESNOINTERESTINGMOVIES(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Go to the Movies, no movies available"),
        //    MYGANG(new HashedPicCoords[]{new HashedPicCoords(394, 20, 158, 36, -1955260861)}, new ColoredXYCoords[]{}, "My Gang screen"),
        //    POINTSSCREEN(new HashedPicCoords[]{new HashedPicCoords(82, 654, 68, 58, 364447078), new HashedPicCoords(82, 654, 68, 58, 210721006)}, new ColoredXYCoords[]{}, "Points distribution into skills screen"),
        //    QUICKFIGHTSLOST(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Quick Fights defeat screen"),
        //    QUICKFIGHTSWON(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Quick Fight won screen"),*/
        RECONNECT(HashedPicCoordsPropertiesAccessor.factory("RECONNECT"), "Logged in on another device, reconnect screen shown"),
        PARKEDCAR(HashedPicCoordsPropertiesAccessor.factory("PARKEDCAR"), "Car is a parked car"),
        //    TOPGANGS(new HashedPicCoords[]{new HashedPicCoords(701, 30, 178, 42, 1779073125)}, new ColoredXYCoords[]{}, "Top Gangs scoreboard screen"),
        //    TOPPLAYERS(new HashedPicCoords[]{new HashedPicCoords(440, 15, 176, 28, -1197974399), new HashedPicCoords(440, 15, 176, 28, 42390409)}, new ColoredXYCoords[]{}, "Top Players scoreboard creen"),
        //    TOPULTIMATELEAGUE(new HashedPicCoords[]{new HashedPicCoords(166, 15, 214, 29, -1958419302), new HashedPicCoords(166, 15, 214, 29, 1087084394)}, new ColoredXYCoords[]{}, "Top Ultimate League scoreboard screen"),
        //    VIP(new HashedPicCoords[]{new HashedPicCoords(100, 190, 40, 40, 166196393), new HashedPicCoords(100, 190, 40, 40, 643568160)}, new ColoredXYCoords[]{}, "VIP screen"),
        //    ZEPTOSCREEN(new HashedPicCoords[]{new HashedPicCoords(470, 480, 59, 10, -339401605), new HashedPicCoords(470, 480, 59, 10, 37205478)}, new ColoredXYCoords[]{}, "Zeptolab icon loading screen"),
        //    OKDEFEAT(new HashedPicCoords[]{new HashedPicCoords(430, 577, 173, 47, -1841029738)}, new ColoredXYCoords[]{}, "Defeat Screen with OK button"),

        UNRESPONSIVE(HashedPicCoordsPropertiesAccessor.factory("UNRESPONSIVE"), "The machine did not return a screenshot"),
        WHATCANYOUDOTODAY(HashedPicCoordsPropertiesAccessor.factory("WHATCANYOUDOTODAY"), "what do you want to do today"),
        STORE(HashedPicCoordsPropertiesAccessor.factory("STORE"), "Zepto Lab Store screen");

        /**
         * hash arrays
         */
        private HashedPicCoordsPropertiesAccessor hashes;

        /*
    *Description of screen
         */
        String description;

        /**
         * Initializer for selected enum value
         *
         * @param hashes associated hashes
         * @param colors associated colors
         * @param description description of screen
         */
        ScreenData(HashedPicCoordsPropertiesAccessor hashes, String description) {

            this.hashes = hashes;
            this.description = description;

        }

        /**
         *
         * @return hashes get associated hashes
         */
        public ArrayList<HashedPicCoords> getHashes() {
          
            return hashes.getHpcl();

        }

        public String getHashesPropertyEntry() {

            return hashes.toPropertiesString(description);

        }

        /**
         *
         * @return screen description
         */
        public String getDescription() {
            return description;
        }

        public static ScreenData identifyScreen(VirtualBoxMachine vm) {
            if (!vm.isPoweredOn()) {
                return ScreenData.OFF;
            }
            BufferedImage bi = vm.screenshot();
            ScreenData sd = identifyScreen(bi);
            return sd;
        }

        /**
         * Identifies the screen and returns a ScreenData based on known
         * attributes on the current screen
         *
         * @param bi Buffered Image of screenshot
         * @return identifier for screen
         */
        @SuppressWarnings("UnnecessaryContinue")
        public static ScreenData identifyScreen(BufferedImage bi) {

            for (ScreenData screen : ScreenData.values()) {
                //check color-point arrays to ensure any associated color-point matches the expectation in this image
                //check hash arrays to ensure any associated hash matches the expectation in this image
                if (screen.getPicCoords()==null||screen.getHashArray().length==0)continue;
                int testHash=screen.getNewHash(bi).getHash();
                for (int refrenceHash:screen.getHashArray()){
                    if (refrenceHash==testHash){
                        return screen;
                    }
                }
                

            }
            return ScreenData.UNKNOWN;
        }

        public boolean matchesHashes(BufferedImage screen) {
            boolean match = false;
            for (HashedPicCoords hpc : this.getHashes()) {
                if (screen == null || hpc == null) {
                    return false;
                } else if (hpc.hashMatches(screen)) {
                    match = true;
                }
            }
            return match;
        }

        public static void clearHashes(ScreenData item) {
            item.getHashes().clear();
        }

        public static HashedPicCoords addHash(ScreenData item, int hash) {
            try {
                HashedPicCoords hpc = cloneFirstItem(item, hash);
                item.getHashes().add(hpc);
                item.updateStoredValueOnDisk();
                return hpc;
            } catch (Exception ex) {
                Logger.getLogger(ScreenData.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public boolean deleteHash(BufferedImage bi) {
            HashedPicCoords hpc = getNewHash(bi);
            boolean result = getHashes().remove(hpc);
            if (result && getHashes().size() > 0) {
                Hashes.writeProperties();
            }
            updateStoredValueOnDisk();
            return result;

        }

        public void updateStoredValueOnDisk() {
            Hashes.addProperty(this.name(), this.getHashesPropertyEntry());
            Hashes.writeProperties();
        }

        public HashedPicCoords addNewHash(BufferedImage bi, boolean deleteAll) {
            HashedPicCoords hpc = getNewHash(bi);
            if (deleteAll) {
                getHashes().clear();
            }
            if (!getHashes().contains(hpc)) {
                this.getHashes().add(hpc);
                Hashes.writeProperties();
            }
            updateStoredValueOnDisk();
            return hpc;
        }

        public HashedPicCoords getNewHash(BufferedImage bi) {
            PicCoords pc = hashes.getPicCoords();
            int newHash = getNewHash(this, bi);
            HashedPicCoords newHpc = new HashedPicCoords(pc.getX(), pc.getY(), pc.getW(), pc.getH(), newHash);
            return newHpc;

        }
        
        public PicCoords getPicCoords(){
            return this.hashes.getPicCoords();
            
        }

        public static HashedPicCoords getHashedPicCoordsFromItem(ScreenData item, int hash) {
            PicCoords pc = item.getPicCoords();
            if (pc==null){
                return null;
            }
            HashedPicCoords hpc = new HashedPicCoords(pc.getX(), pc.getY(), pc.getW(), pc.getH(), hash);
            return hpc;
        }

        public static int getNewHash(ScreenData item, BufferedImage bi) {
            HashedPicCoords ref = getHashedPicCoordsFromItem(item, 0);
            int hash = ref.getImageHash(bi);
            return hash;
        }

        public static HashedPicCoords getNewHashedPicCoords(ScreenData item, BufferedImage bi) {
            HashedPicCoords hpc = getHashedPicCoordsFromItem(item, getNewHash(item, bi));

            return hpc;
        }

        public static HashedPicCoords cloneFirstItem(ScreenData item, int hash) throws Exception {
            if (item.getHashes().isEmpty()) {
                throw new Exception("cannot clone a null item. This prototype must be created before cloning");
            }
            HashedPicCoords hpc = item.getHashes().get(0).clone(hash);
            return hpc;

        }

        public static String toProperties() {
            StringBuilder sb = new StringBuilder();
            for (ScreenData item : ScreenData.values()) {
                sb.append(item.name()).append("=").append(item.getHashesPropertyEntry().replace("ERROR", "")).append("\n");
            }
            return sb.toString();
        }

    private int[] getHashArray() {
        return hashes.getHashes();
    }
    }

