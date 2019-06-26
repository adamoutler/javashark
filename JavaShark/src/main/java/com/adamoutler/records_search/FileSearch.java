/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.records_search;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author adamo
 */
public abstract class FileSearch {

    abstract String getSearchPath();
    String searchPath = "";
    String extension = "";
    String startsWith = "";
    int maxResults = 1;

    abstract FilenameFilter getFilter();

    public FileSearch setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public FileSearch setMaxResults(int results) {
        this.maxResults = results;
        return this;
    }

    public List<File> search() {

        File[] listFiles = new File(getSearchPath()).listFiles(getFilter());
        return Arrays.asList(listFiles);

    }

}
