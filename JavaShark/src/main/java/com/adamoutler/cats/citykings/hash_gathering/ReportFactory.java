/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.citykings.hash_gathering;

import com.adamoutler.Prop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 * @author adamo
 */
public class ReportFactory {

    private String repoFolder = Prop.getProp("reports");

    public CityKingsMatchRaw getReport(String filename) throws FileNotFoundException {
        String name = filename.substring(0, filename.lastIndexOf(".")).replaceAll(".*/", "");
        Gson gson = new GsonBuilder().create();
        File f = new File(getRepoFolder() + name);
        System.out.println("loading " + f.getAbsolutePath());
        FileReader fr = new FileReader(new File(getRepoFolder() + "/" + name));
        CityKingsMatchRaw match = gson.fromJson(fr, CityKingsMatchRaw.class);
        return match;
    }

    /**
     * @return the repoFolder
     */
    public String getRepoFolder() {
        return repoFolder;
    }

    /**
     * @param repoFolder the repoFolder to set
     */
    public void setRepoFolder(String repoFolder) {
        this.repoFolder = repoFolder;
    }

}
