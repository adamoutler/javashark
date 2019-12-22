/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import com.adamoutler.cats.screen_id.HashProperties;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author adamo
 */
public class HashedPicCoordsPropertiesAccessor {

    final private ArrayList<HashedPicCoords> hpcl = new ArrayList<>();
    private String name;
    private boolean initialized = true;
    PicCoords pc;

    public HashedPicCoordsPropertiesAccessor(String input) {
        doPropertiesInitialization(input);
    }
    
    public HashedPicCoordsPropertiesAccessor() {
        initialized = false;
    }

    public HashedPicCoordsPropertiesAccessor(String name, String input) {
        this.name = name;
        doPropertiesInitialization(input);
    }

    public HashedPicCoordsPropertiesAccessor(HashedPicCoords hpc, String name) {
        this.name = name;
        pc = (PicCoords) hpc;
        hpcl.add(hpc);
    }

    public HashedPicCoordsPropertiesAccessor(PicCoords pc, String name) {
        this.name = name;
        this.pc = pc;

    }

    public PicCoords getPicCoords() throws NumberFormatException {
        return pc;
    }

    private HashedPicCoords getHashedPicCoords(String hash) throws NumberFormatException {
        int hashValue = Integer.parseInt(hash.trim());
        int x = pc.getX();
        int y = pc.getY();
        int w = pc.getW();
        int h = pc.getH();
        HashedPicCoords hpc = new HashedPicCoords(x, y, w, h, hashValue);
        return hpc;
    }

    public void init(String name, Properties input) {
        if (initialized) {
            return;
        }
        this.name = name;
        doPropertiesInitialization(input.getProperty(name));
        this.initialized = true;
    }
    public static HashedPicCoordsPropertiesAccessor factory(String ScreenData){
        String initString=HashProperties.getProps().getProperty(ScreenData);
        HashedPicCoordsPropertiesAccessor hpcpa=new HashedPicCoordsPropertiesAccessor(initString);
        return hpcpa;
    }

    private void doPropertiesInitialization(String input) throws NumberFormatException {
        
        if (input == null || input.isEmpty()) {
            return;
        }
        String[] inputSplit = input.split(",");
        if (inputSplit.length > 3) {
            pc = getPicCoordsFromArray(inputSplit);
            if (inputSplit.length == 4) {
                return;
            }
            for (int i = 4; i < inputSplit.length; i++) {
                String hash = inputSplit[i].trim();
                if (isNumeric(hash)) {
                    HashedPicCoords hpc = this.getHashedPicCoords(hash);
                    hpcl.add(hpc);
                }
            }

        } else {
            pc = null;
        }
    }

    private PicCoords getPicCoordsFromArray(String[] inputSplit) throws NumberFormatException {
        PicCoords p;
        int x = Integer.parseInt(inputSplit[0].trim());
        int y = Integer.parseInt(inputSplit[1].trim());
        int w = Integer.parseInt(inputSplit[2].trim());
        int h = Integer.parseInt(inputSplit[3].trim());
        p = new PicCoords(x, y, w, h);
        return p;
    }

    public String toPropertiesString(String name) {
        StringBuilder sb = new StringBuilder();
        if (pc != null) {
            sb.append(pc.getX()).append(", ")
                    .append(pc.getY()).append(", ")
                    .append(pc.getW()).append(", ")
                    .append(pc.getH());
        }
        for (HashedPicCoords hpc : getHpcl()) {
            sb.append(", ").append(hpc.getHash());
        }
        return sb.toString().trim();

    }

    /**
     * @return the hpcl
     */
    public ArrayList<HashedPicCoords> getHpcl() {
        return hpcl;
    }

    public int[] getHashes() {
        int[] items = new int[hpcl.size()];
        hpcl.stream().forEach((hpc) -> {
            items[hpcl.indexOf(hpc)] = hpc.hash;
        });
        return items;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }


    public void add(HashedPicCoords newHash) {
        hpcl.add(newHash);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
