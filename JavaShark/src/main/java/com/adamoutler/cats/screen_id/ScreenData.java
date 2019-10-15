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
 *
 * @author adamo
 */
public enum ScreenData {
    UNKNOWN(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Unidentified"),
    OFF(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Turned off"),
    BLACKSCREEN(new HashedPicCoords[]{new HashedPicCoords(107, 33, 45, 706, -230170519)}, new ColoredXYCoords[]{}, "A black screen"),
    BOOTED(new HashedPicCoords[]{new HashedPicCoords(200, 200, 29, 29, 321694902), new HashedPicCoords(135, 137, 29, 29, -238493308), new HashedPicCoords(129, 106, 29, 29, -238493308), new HashedPicCoords(129, 106, 29, 29, 900977023), new HashedPicCoords(129, 106, 29, 29, -1300936293)}, new ColoredXYCoords[]{}, "Android has booted"),
//    BOXOPENNOW(new HashedPicCoords[]{new HashedPicCoords(370, 600, 263, 42, 1884123401)}, new ColoredXYCoords[]{}, "Box Screen, Click to open box"),
//    BOXOPENNOWSKIP(new HashedPicCoords[]{new HashedPicCoords(566, 603, 197, 43, -410747955)}, new ColoredXYCoords[]{}, "Bos screen, open now or watch video"),
//    BOXUNLOCK(new HashedPicCoords[]{new HashedPicCoords(370, 600, 263, 42, -1701044878)}, new ColoredXYCoords[]{}, "box screen, click to start 2-hour timer"),
    CHAMPIONSHIPGARAGE(new HashedPicCoords[]{new HashedPicCoords(26, 594, 260, 11, 14874029)}, new ColoredXYCoords[]{}, "In Championship Garage working on car"),
    CATSLOADEDCHAMPIONSHIPSCREEN(new HashedPicCoords[]{new HashedPicCoords(960, 435, 25, 25, 457448725)}, new ColoredXYCoords[]{}, "CATS loaded, on Championship Home screen"),
    CATSLOADEDULTIMATESCREEN(new HashedPicCoords[]{new HashedPicCoords(960, 435, 25, 25, 168733178)}, new ColoredXYCoords[]{}, "CATS Loaded, on Ultimate Home screen"),
    CATSLOADINGSCREEN(new HashedPicCoords[]{new HashedPicCoords(372, 195, 276, 17, -220984222), new HashedPicCoords(372, 195, 276, 17, 593585040)}, new ColoredXYCoords[]{}, "In CATS Loading screen"),
//    CHAT(new HashedPicCoords[]{new HashedPicCoords(574, 131, 90, 90, -1923340452)}, new ColoredXYCoords[]{}, "Gang Chat screen"),
    CITYKINGCAR1(new HashedPicCoords[]{new HashedPicCoords(50, 500, 30, 50, -2124867224)}, new ColoredXYCoords[]{}, "City Kings, Examining Sentry Owner Car 1 "),
    CITYKINGCAR2(new HashedPicCoords[]{new HashedPicCoords(125, 500, 30, 50, 139952256)}, new ColoredXYCoords[]{}, "City Kings, Examining Sentry Owner Car 2 "),
    CITYKINGCAR3(new HashedPicCoords[]{new HashedPicCoords(200, 500, 30, 50, -116560625)}, new ColoredXYCoords[]{}, "City Kings, Examining Sentry Owner Car 3 "),
    CITYKINGSBEGINTHEATTACK(new HashedPicCoords[]{new HashedPicCoords(460,162,3,7,1762741752)}, new ColoredXYCoords[]{}, "Begin the attack, waiting for opponent"),
    CITYKINGSENLIST(new HashedPicCoords[]{new HashedPicCoords(392,133,55,25,1903642449)}, new ColoredXYCoords[]{}, "City Kings, Enlist screen"),
    CITYKINGS(new HashedPicCoords[]{new HashedPicCoords(10,250,30,300,-995957751), new HashedPicCoords(10, 250, 30, 300, 942357758)}, new ColoredXYCoords[]{}, "City Kings, battlefield screen"),

    CITYKINGSBUILDINGNOSCOUT(new HashedPicCoords[]{new HashedPicCoords(576, 311, 35, 47, -513431068), new HashedPicCoords(576, 311, 35, 47, 379408513), new HashedPicCoords(576, 311, 35, 47, 247867897)}, new ColoredXYCoords[]{}, "City Kings, building Attack Screen, no scout"),
    CITYKINGSBUILDINGATTACK(new HashedPicCoords[]{new HashedPicCoords(26, 84, 91, 154, -1036175644)}, new ColoredXYCoords[]{}, "City Kings, Building Attack screen"),
    CITYKINGSBUILDINGDEFEND(new HashedPicCoords[]{new HashedPicCoords(26, 84, 91, 154, 1435934289)}, new ColoredXYCoords[]{}, "City Kings, building defend screen"),
    CITYKINGSBUILDINGSCREEN(new HashedPicCoords[]{new HashedPicCoords(652, 336, 27, 27, -673171305), new HashedPicCoords(652, 336, 27, 27, -1473904319)}, new ColoredXYCoords[]{}, "City Kings, building main screen"),
//    CITYKINGSGANGINFO(new HashedPicCoords[]{new HashedPicCoords(412, 574, 181, 46, -988303182)}, new ColoredXYCoords[]{}, "City Kings, gang info screen"),
//    CITYKINGSUNCLETONY(new HashedPicCoords[]{new HashedPicCoords(225, 85, 240, 54, 782978378), new HashedPicCoords(225, 85, 240, 54, 774163899)}, new ColoredXYCoords[]{}, "City Kings, Uncle Tony screen"),
    CITYKINGSVICTORY(new HashedPicCoords[]{new HashedPicCoords(416, 130, 219, 81, -1607526986)}, new ColoredXYCoords[]{}, "City Kings, Victory Screen"),
    CITYKINGSDEFEAT(new HashedPicCoords[]{new HashedPicCoords(416, 114, 206, 82, -907069408)}, new ColoredXYCoords[]{}, ""),
    EXITSCREEN(new HashedPicCoords[]{new HashedPicCoords(387, 576, 238, 43, -1235775934), new HashedPicCoords(387, 576, 238, 43, -1310101643), new HashedPicCoords(387, 576, 238, 43, -1304481246)}, new ColoredXYCoords[]{}, "Are you sure you want to quit?"),
    FRIENDS(new HashedPicCoords[]{new HashedPicCoords(843, 44, 144, 15, -117038954), new HashedPicCoords(843, 44, 144, 15, 167789077)}, new ColoredXYCoords[]{}, "Gang Friends screen"),
    GANGFIGHTS(new HashedPicCoords[]{new HashedPicCoords(826, 698, 81, 37, -367153812)}, new ColoredXYCoords[]{}, "Gang Fights, ready for action screen"),
    GANGFIGHTSBATTLESCREEN(new HashedPicCoords[]{new HashedPicCoords(387, 576, 238, 43, 313145442)}, new ColoredXYCoords[]{}, "Gang Fights, battle screen"),
    GANGFIGHTSCOMPLETE(new HashedPicCoords[]{new HashedPicCoords(180, 694, 89, 39, 1241178161)}, new ColoredXYCoords[]{}, "Gang Fights, complete and ready for reset screen"),
    GANGFIGHTSDEFEAT(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Gang Fights, defeated screen"),
    GANGFIGHTSVICTORY(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "Gang Fights, victory screen"),
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
    RECONNECT(new HashedPicCoords[]{new HashedPicCoords(423, 597, 188, 62, -133649367), new HashedPicCoords(423, 597, 188, 62, -775156857)}, new ColoredXYCoords[]{}, "Logged in on another device, reconnect screen shown"),
    PARKEDCAR(new HashedPicCoords[]{new HashedPicCoords(209, 257, 13, 20, 1111413564)}, new ColoredXYCoords[]{}, "Car is a parked car"),
//    TOPGANGS(new HashedPicCoords[]{new HashedPicCoords(701, 30, 178, 42, 1779073125)}, new ColoredXYCoords[]{}, "Top Gangs scoreboard screen"),
//    TOPPLAYERS(new HashedPicCoords[]{new HashedPicCoords(440, 15, 176, 28, -1197974399), new HashedPicCoords(440, 15, 176, 28, 42390409)}, new ColoredXYCoords[]{}, "Top Players scoreboard creen"),
//    TOPULTIMATELEAGUE(new HashedPicCoords[]{new HashedPicCoords(166, 15, 214, 29, -1958419302), new HashedPicCoords(166, 15, 214, 29, 1087084394)}, new ColoredXYCoords[]{}, "Top Ultimate League scoreboard screen"),
//    VIP(new HashedPicCoords[]{new HashedPicCoords(100, 190, 40, 40, 166196393), new HashedPicCoords(100, 190, 40, 40, 643568160)}, new ColoredXYCoords[]{}, "VIP screen"),
//    ZEPTOSCREEN(new HashedPicCoords[]{new HashedPicCoords(470, 480, 59, 10, -339401605), new HashedPicCoords(470, 480, 59, 10, 37205478)}, new ColoredXYCoords[]{}, "Zeptolab icon loading screen"),
//    OKDEFEAT(new HashedPicCoords[]{new HashedPicCoords(430, 577, 173, 47, -1841029738)}, new ColoredXYCoords[]{}, "Defeat Screen with OK button"),
    
    UNRESPONSIVE(new HashedPicCoords[]{}, new ColoredXYCoords[]{}, "The machine did not return a screenshot"),
    WHATCANYOUDOTODAY(new HashedPicCoords[]{new HashedPicCoords(324, 149, 56, 52, 1559680746)}, new ColoredXYCoords[]{}, "what do you want to do today"),
    STORE(new HashedPicCoords[]{new HashedPicCoords(11, 695, 41, 48, 1553001173)}, new ColoredXYCoords[]{}, "Zepto Lab Store screen");

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
     *
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
     * @return associated point-colors
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

    public static ScreenData identifyScreen(VirtualBoxMachine vm) {
        if (!vm.isPoweredOn()) {
            return ScreenData.OFF;
        }
        BufferedImage bi = vm.screenshot();
        ScreenData sd = identifyScreen(bi);
        return sd;
    }

    /**
     * Identifies the screen and returns a ScreenData based on known attributes
     * on the current screen
     *
     * @param bi Buffered Image of screenshot
     * @return identifier for screen
     */
    public static ScreenData identifyScreen(BufferedImage bi) {

        ScreenData returnValue = ScreenData.UNKNOWN;
        for (ScreenData screen : ScreenData.values()) {

            //check color-point arrays to ensure any associated color-point matches the expectation in this image
            boolean hasColors = false, matchColors = false;
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
            boolean hasHashes = false, matchHash = false;
            for (HashedPicCoords hpc : screen.getHashes()) {
                hasHashes = true;
                if (bi == null || hpc == null) {
                    matchHash = false;
                } else if (hpc.hashMatches(bi)) {
                    matchHash = true;
                }
            }
            if ((hasHashes || hasColors) && (matchHash || matchColors)) {
                returnValue = screen;
                break;
            }
        }
        return returnValue;
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

}
