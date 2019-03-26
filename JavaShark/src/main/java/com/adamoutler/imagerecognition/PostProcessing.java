/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

/**
 *
 * @author adamo
 */
public class PostProcessing {

    public static int getIntFromString(String input) {
        String[] stringAr = input.split(" ");
        if (stringAr.length>1&& isNumeric(stringAr[1])) {
            return Integer.parseInt(stringAr[1].trim());
        }
        for (String s : stringAr) {
            if (isNumeric(s)){
                return Integer.parseInt(s.trim());
            }

        }
        return 0;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
