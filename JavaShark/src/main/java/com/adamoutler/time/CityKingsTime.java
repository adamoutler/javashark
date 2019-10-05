/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.time;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 *
 * @author adamo
 */
public class CityKingsTime {

    final static int CKSTOPTIME = 3;   //UTC hour 2 = 0300 UK, 2400 EST, 2100 PAC, 1200 BEJ
    final static int CKSTARTTIME = 10;  //UTC hour 7 = 0800 UK, 0400 EST, 2300 PAC, 1800 BEJ
    final static int CKGOODTIME = 13;
//    public static void main(String[] args) {
//        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
//        int uHour = getHour(now);
//        int uMinute = getMinute(now);
//        int uSecond = getSecond(now);
//        for (int i = 0; i <= 23; i++) {
//            System.out.println(calculateCKTimes(i, uMinute, uSecond));
//        }
//        System.out.println(calculateCKTimes(uHour, uMinute, uSecond));
//    }

    public static String getCityKingsTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        return calculateCKTimes(getHour(now), getMinute(now), getSecond(now));
    }

    static int getHour(ZonedDateTime now) {
        return now.getHour();
    }

    static int getMinute(ZonedDateTime now) {
        return now.getMinute();
    }

    static int getSecond(ZonedDateTime now) {
        return now.getSecond();
    }

    static int maybeAdd24(int value) {
        if (value < 0) {
            return value + 24;
        }
        return value;
    }

    static String calculateCKTimes(int uHour, int utcMinutes, int utcSeconds) {

        //calculate
        int stopHours = maybeAdd24(CKSTOPTIME - uHour);
        int startHours = maybeAdd24(CKSTARTTIME - uHour);
        int goodHours = maybeAdd24(CKGOODTIME - uHour);
        int minutesleft = 59 - utcMinutes;
        int secondsleft = 59 - utcSeconds;
        String minutes;
        String seconds;

        //format 0 prefixes
        if (minutesleft < 10) {
            minutes = "0" + Integer.toString(minutesleft);
        } else {
            minutes = Integer.toString(minutesleft);
        }
        if (secondsleft < 10) {
            seconds = Integer.toString(secondsleft);
        } else {
            seconds = Integer.toString(secondsleft);
        }

        //display
        if (uHour > CKSTOPTIME && uHour <= CKSTARTTIME) {
            return " DO NOT START CK! Odds of Instant Victory are greatly reduced for a coordinated strike, it is better to wait for " + goodHours + " hours, " + minutes + " minutes, " + seconds + " seconds. Want to see the flowchart? Just ask!";
        } else {
            if (uHour <= CKGOODTIME) {
                return " DO NOT START CK! Odds of Instant Victory are greatly reduced for a coordinated strike, it is better to wait for " + goodHours + " hours, " + minutes + " minutes, " + seconds + " seconds. Want to see the flowchart? Just ask!";
            } else {
                return " Go for it! It's prime hours to start a new City Kings round for the next " + stopHours + " hours, " + minutes + " minutes, " + seconds + " seconds. Want to see the flowchart? Just ask!";
            }
        }
    }
}
