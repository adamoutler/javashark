/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.records_search;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author adamo
 */
public class BattleRecordSearch extends FileSearch {

    String gang = "";
    String ocrName = "";
    private String startsWith = "";

    public BattleRecordSearch() {
        this.searchPath = "/var/www/baby-sharks/excel/";
    }

    public BattleRecordSearch setGang(String gang) {
        this.gang = gang;
        return this;
    }

    public BattleRecordSearch setFolder(String folder) {
        this.searchPath = folder;
        return this;
    }

    public BattleRecordSearch setOpponentGangOCRName(String ocrName) {
        this.ocrName = ocrName;
        return this;
    }

    @Override
    String getSearchPath() {
        return searchPath;
    }

    @Override
    FilenameFilter getFilter() {

        FilenameFilter ff = (File file, String string) -> {

            if (extension.isEmpty() && string.matches("(.*[.].*)")) {

                return false;
            }
            if (!string.endsWith(extension)) {
                return false;
            }
            if (!startsWith.isEmpty() && !string.startsWith(startsWith)) {
                return false;
            }
            String[] namesplit = string.split("-");
            if (!gang.isEmpty() && namesplit.length > 1 && !namesplit[2].equals(gang)) {
                return false;
            }
            if (!ocrName.isEmpty() && !string.contains("OCRName-" + ocrName)) {
                return false;
            }

            return true;
        };

        return ff;
    }

    /**
     * @param startsWith the startsWith to set
     */
    public BattleRecordSearch setStartsWith(String startsWith) {
        this.startsWith = startsWith;
        return this;
    }
}
