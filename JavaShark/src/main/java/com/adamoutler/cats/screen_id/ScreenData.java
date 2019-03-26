/*
 * Copyright (C) 2019 Adam Outler <adamoutler at adamoutler.com>
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.adamoutler.cats.screen_id;

import com.adamoutler.imagerecognition.ColoredXYCoords;
import com.adamoutler.imagerecognition.HashedPicCoords;
import com.adamoutler.virtualbox.VirtualBoxMachine;
import java.awt.image.BufferedImage;

/**
 * defines screens found in-game
 * @author adamo
 */
public enum ScreenData {
    UNKNOWN(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Unidentified"),
    BLACKSCREEN(new HashedPicCoords[]{new HashedPicCoords(107,33,45,706,-230170519)}, new ColoredXYCoords[]{}, "A black screen"),
    BOOTED(new HashedPicCoords[]{new HashedPicCoords(129,106,29,29,-238493308),new HashedPicCoords(129,106,29,29,-1300936293)}, new ColoredXYCoords[]{}, "Android has booted"),
    BOXOPENNOW(new HashedPicCoords[]{new HashedPicCoords(370,600,263,42,1806715308)}, new ColoredXYCoords[]{}, "Box Screen, Click to open box"),
    BOXOPENNOWSKIP(new HashedPicCoords[]{new HashedPicCoords(566,603,197,43,1892377544)}, new ColoredXYCoords[]{}, "Bos screen, open now or watch video"),
    BOXUNLOCK(new HashedPicCoords[]{new HashedPicCoords(370,600,263,42,2146314069)}, new ColoredXYCoords[]{}, "box screen, click to start 2-hour timer"),
    CATSLOADEDCHAMPIONSHIPSCREEN(new HashedPicCoords[]{new HashedPicCoords(917,261,95,98,-1641366908), new HashedPicCoords(212,598,107,23,-1906095139)}, new ColoredXYCoords[]{}, "CATS loaded, on Championship Home screen"),
    CATSLOADEDULTIMATESCREEN(new HashedPicCoords[]{new HashedPicCoords(388,602,230,40,660508215),new HashedPicCoords(946,283,53,48,-1139583725)}, new ColoredXYCoords[]{}, "CATS Loaded, on Ultimate Home screen"),
    CATSLOADINGSCREEN(new HashedPicCoords[]{new HashedPicCoords(372,195,276,17,1525539489)}, new ColoredXYCoords[]{}, "In CATS Loading screen"),
    CHAMPIONSHIPGARAGE(new HashedPicCoords[]{new HashedPicCoords(26,594,260,11,1259859504)}, new ColoredXYCoords[]{}, "In Championship Garage working on car"),
    CHAT(new HashedPicCoords[]{new HashedPicCoords(574,131,90,90,672990996)}, new ColoredXYCoords[]{}, "Gang Chat screen"),
    CITYKINGS(new HashedPicCoords[]{new HashedPicCoords(131,127,171,22,601329689),new HashedPicCoords(629,203,50,44,-1335721359),new HashedPicCoords(427,123,163,24,1441714547)}, new ColoredXYCoords[]{}, "City Kings, battlefield screen"),
    CITYKINGSBUILDINGATTACK(new HashedPicCoords[]{new HashedPicCoords(26,84,91,154,1788496405)}, new ColoredXYCoords[]{}, "City Kings, Building Attack screen"),
    CITYKINGSBUILDINGDEFEND(new HashedPicCoords[]{new HashedPicCoords(26,84,91,154,1587988222)}, new ColoredXYCoords[]{}, "City Kings, building defend screen"),
    CITYKINGSBUILDINGSCREEN(new HashedPicCoords[]{new HashedPicCoords(26,84,91,154,290854650)}, new ColoredXYCoords[]{}, "City Kings, building main screen"),
    CITYKINGSGANGINFO(new HashedPicCoords[]{new HashedPicCoords(412,574,181,46,-459207574)}, new ColoredXYCoords[]{}, "City Kings, gang info screen"),
    CITYKINGSUNCLETONY(new HashedPicCoords[]{new HashedPicCoords(225,85,240,54,774163899)}, new ColoredXYCoords[]{}, "City Kings, Uncle Tony screen"),
    EXITSCREEN(new HashedPicCoords[]{new HashedPicCoords(387,576,238,43,-1304481246)}, new ColoredXYCoords[]{}, "Are you sure you want to quit?"),
    FRIENDS(new HashedPicCoords[]{new HashedPicCoords(843,44,144,15,-91234188)}, new ColoredXYCoords[]{}, "Gang Friends screen"),
    GANGFIGHTS(new HashedPicCoords[]{new HashedPicCoords(826,698,81,37,905255899)}, new ColoredXYCoords[]{}, "Gang Fights, ready for action screen"),
    GANGFIGHTSBATTLESCREEN(new HashedPicCoords[]{new HashedPicCoords(387,576,238,43,313145442)}, new ColoredXYCoords[]{}, "Gang Fights, battle screen"),
    GANGFIGHTSCOMPLETE(new HashedPicCoords[]{new HashedPicCoords(180,694,89,39,1241178161)}, new ColoredXYCoords[]{}, "Gang Fights, complete and ready for reset screen"),
    GANGFIGHTSDEFEAT(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Gang Fights, defeated screen"),
    GANGFIGHTSVICTORY(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Gang Fights, victory screen"),
    JOINALEAGUE(new HashedPicCoords[]{new HashedPicCoords(399,168,209,41,2014501043)}, new ColoredXYCoords[]{}, "CATS Loaded, on Ultimate Home screen, new league available"),
    LEAGUEFIGHTDEFEAT(new HashedPicCoords[]{new HashedPicCoords(430,600,120,37,-1685251910)}, new ColoredXYCoords[]{}, "Ultimate League, defeat screen"),
    LEAGUEFIGHTRANKING(new HashedPicCoords[]{new HashedPicCoords(456,162,131,43,149289225)}, new ColoredXYCoords[]{}, "Ultimate League fight complete, ranking screen"),
    LEAGUEFIGHTWON(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Ultimate League, fight won"),
    MOVIESNOINTERESTINGMOVIES(new HashedPicCoords[]{new HashedPicCoords(430,607,152,38,-2104934140)}, new ColoredXYCoords[]{}, "Go to the Movies, no movies available"),
    MYGANG(new HashedPicCoords[]{new HashedPicCoords(394,20,158,36,399445131)}, new ColoredXYCoords[]{}, "My Gang screen"),
    POINTSSCREEN(new HashedPicCoords[]{new HashedPicCoords(82,654,68,58,-1033272872)}, new ColoredXYCoords[]{}, "Points distribution into skills screen"),
    QUICKFIGHTSLOST(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Quick Fights defeat screen"),
    QUICKFIGHTSWON(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Quick Fight won screen"),
    RECONNECT(new HashedPicCoords[]{new HashedPicCoords(423,597,188,62,-1987127223),new HashedPicCoords(416,601,169,38,-1813689011)}, new ColoredXYCoords[]{}, "Logged in on another device, reconnect screen shown"),
    STORE(new HashedPicCoords[]{new HashedPicCoords(11,695,41,48,25483029)}, new ColoredXYCoords[]{}, "Zepto Lab Store screen"),
    TOPGANGS(new HashedPicCoords[]{new HashedPicCoords(701,30,178,42,1779073125)}, new ColoredXYCoords[]{}, "Top Gangs scoreboard screen"),
    TOPPLAYERS(new HashedPicCoords[]{new HashedPicCoords(440,15,176,28,-1643148680)}, new ColoredXYCoords[]{}, "Top Players scoreboard creen"),
    TOPULTIMATELEAGE(new HashedPicCoords[]{new HashedPicCoords(166,15,214,29,-452886606)}, new ColoredXYCoords[]{}, "Top Ultimate League scoreboard screen"),
    ULTIMATEGARAGE(new HashedPicCoords[]{new HashedPicCoords(26,594,260,11,-1394165371)}, new ColoredXYCoords[]{}, "Ultimate League garage screen, working on car"),
    ZEPTOSCREEN(new HashedPicCoords[]{new HashedPicCoords(472,545,75,22,-1841029738)}, new ColoredXYCoords[]{}, "Zeptolab icon loading screen"),
    OKDEFEAT(new HashedPicCoords[]{new HashedPicCoords(430,577,173,47,-1841029738)}, new ColoredXYCoords[]{}, "Defeat Screen with OK button");

    /**
     * hash arrays
     */
    HashedPicCoords[] hashes;
    /*
    *point-color arrays
    */
    ColoredXYCoords[] colors;
    /*
    *Description of screen
    */
    String description;

    /**
     * Initializer for selected enum value
     * @param hashes associated hashes
     * @param colors associated colors
     * @param description description of screen
     */
    ScreenData(HashedPicCoords[] hashes, ColoredXYCoords[] colors, String description) {
        this.colors = colors;
        this.hashes = hashes;
        this.description = description;
    }

    /**
     * 
     * @return hashes get associated hashes
     */
    public HashedPicCoords[] getHashes() {
        return hashes;
    }

    /**
     * 
     * @return  associated point-colors
     */
    
    public ColoredXYCoords[] getColorCoords() {
        return colors;
    }

    /**
     * 
     * @return screen description
     */
    public String getDescription() {
        return description;
    }

    public static ScreenData identifyScreen(VirtualBoxMachine vm){
        BufferedImage bi=vm.screenshot();
        ScreenData sd=identifyScreen(bi);
        return sd;
    }
    
    /**
     * Identifies the screen and returns a ScreenData based on known attributes on the current screen
     * @param bi Buffered Image of screenshot
     * @return identifier for screen
     */
    public static ScreenData identifyScreen(BufferedImage bi) {
        ScreenData returnValue = ScreenData.UNKNOWN;
        for (ScreenData screen : ScreenData.values()) {
            
            //check color-point arrays to ensure any associated color-point matches the expectation in this image
            boolean hasColors = false,matchColors = false;
            for (ColoredXYCoords cxy : screen.getColorCoords()) {
                hasColors = true;
                if (cxy.approximateColorMatch(bi)) {
                    matchColors = true;
                }
            }
            if (hasColors && !matchColors) {
                continue;
            }
            
            //check hash arrays to ensure any associated hash matches the expectation in this image
            boolean hasHashes = false,  matchHash = false;
            for (HashedPicCoords hpc : screen.getHashes()) {
                hasHashes = true;
                if (hpc.hashMatches(bi)) {
                    matchHash = true;
                }
            }
            if ( (hasHashes || hasColors) && (matchHash || matchColors)) {
                returnValue = screen;
                break;
            }
        }
        return returnValue;
    }

}
