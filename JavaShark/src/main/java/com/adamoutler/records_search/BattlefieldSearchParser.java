/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.records_search;

import com.babysharks.messaging.SearchQuery;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class BattlefieldSearchParser {

    private String gang;
    private String ocrName;
    private String startsWith;
    private String extension;
    private Long timestamp;

    public BattlefieldSearchParser() {

    }

    public BattlefieldSearchParser(SearchQuery sq) {
        this.gang = emptyIfNull(sq.getGang());
        this.ocrName = emptyIfNull(sq.getOpponentName());
        this.startsWith = emptyIfNull(sq.getType());
        this.extension = emptyIfNull(sq.getExtension());
        if (sq.getCenterTimestamp() != null) {
            this.timestamp = sq.getCenterTimestamp();
        }
    }

    private String emptyIfNull(String s) {
        if (s == null) {
            return "";
        }
        if (s.isEmpty()) {
            return "";
        }
        return s;
    }

    public List getRecords() throws SearchException {
        return getRecords("/var/www/baby-sharks/excel", extension, gang, ocrName, startsWith);
    }

    public static List<File> getRecords(String folder, String fileExtension, String gang, String opponentName, String startsWith) throws SearchException {
        throwExceptionIfFolderNotSpecified(folder);
        BattleRecordSearch brs = createBattleSearch(gang, opponentName, folder, fileExtension, startsWith);
        List<File> files = brs.search();
        return files;
    }

    public static Map<Date, File> findOneRecordForEachDate(List<File> files, Date[] dates) throws SearchException {
        if (files == null || files.isEmpty()) {
            throw new SearchException("no records available");
        }
        Arrays.sort(dates);
        HashMap<Date, File> bestMatchFileMap = matchDatesToFiles(dates, files);
        return bestMatchFileMap;

    }

    private static HashMap<Date, File> matchDatesToFiles(Date[] dates, List<File> files) {
        HashMap<Date, File> bestMatchFileMap = new HashMap<>();
        for (Date checkDate : dates) {
            long preferredTime = checkDate.toInstant().toEpochMilli();
            File currentBestFile = getBestFileMatchToDate(files, preferredTime);
            if (!bestMatchFileMap.containsValue(currentBestFile) || currentBestFile != null) {
                bestMatchFileMap.put(checkDate, currentBestFile);
            }
        }
        return bestMatchFileMap;
    }

    private static File getBestFileMatchToDate(List<File> files, long preferredTime) {
        File currentBestFile = null;
        try {
            for (File testFile : files) {
                long currentFileCreated = setTimeOrZero(currentBestFile);
                long testFileCreated = setTimeOrZero(testFile);
                long currentFileDifference = Math.abs(currentFileCreated - preferredTime);
                long testFileDifference = Math.abs(testFileCreated - preferredTime);
                if (currentFileDifference > testFileDifference || currentFileCreated == 0) {
                    currentBestFile = testFile;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(BattlefieldSearchParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return currentBestFile;
    }

    private static long setTimeOrZero(File testFile) throws IOException {
        long retval = 0;
        if (testFile != null) {
            retval = getCreatedEpochMillis(testFile);
        }
        return retval;
    }

    public static long getCreatedEpochMillis(File testFile) throws IOException {
        long retval;
        retval = Files.readAttributes(testFile.toPath(), BasicFileAttributes.class).creationTime().toMillis();
        return retval;
    }

    private static BattleRecordSearch createBattleSearch(String gang, String opponentName, String folder, String fileExtension, String startsWith) {
        String oGang = Optional.of(gang).orElse("");
        String oOpponentName = Optional.of(opponentName).orElse("");
        String oStartsWith = Optional.of(startsWith).orElse("");
        BattleRecordSearch brs = setSearchParameters(folder, oGang, oOpponentName, fileExtension, oStartsWith);
        return brs;
    }

    private static BattleRecordSearch setSearchParameters(String folder, String oGang, String oOpponentName, String fileExtension, String startsWith) {
        BattleRecordSearch brs = new BattleRecordSearch().setFolder(folder).setGang(oGang).setOpponentGangOCRName(oOpponentName).setStartsWith(startsWith);
        brs.setFolder(folder).setExtension(fileExtension);
        return brs;
    }

    private static void throwExceptionIfFolderNotSpecified(String folder) throws SearchException {
        if (folder == null || folder.isEmpty()) {
            throw new SearchException("No folder specified");
        }
    }
}
