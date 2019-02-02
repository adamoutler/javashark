/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author adamo
 */
public class UserTimeCalculator {

    public static String calculateUTCTimeWithOffset(long offset) {
        long msOffset = offset * 60 * 1000;
        long utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        long time = utc + msOffset;
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm.ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String curDate = sdf.format(new Date(time));
        return curDate;
    }

    public double calculateUserTime(List<User> users) {
        ArrayList<Integer> times = new ArrayList<>();
        users.forEach((user) -> {
            times.add(user.getTz_offset());
        });
        return calculateAverage(times);
    }

    private double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Iterator<Integer> it = marks.iterator(); it.hasNext();) {
                Integer mark = it.next();
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }

}
