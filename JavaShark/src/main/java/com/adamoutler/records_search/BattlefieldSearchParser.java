/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.records_search;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class BattlefieldSearchParser {

    public List<File> getRecords(String folder, String fileExtension, String gang, String opponentName) throws SearchException {
        throwExceptionIfFolderNotSpecified(folder);
        BattleRecordSearch brs = createBattleSearch(gang, opponentName, folder, fileExtension);
        List<File> files = brs.search();
        return files;
    }

    public Map<Date, File> findOneRecordForEachDate(List<File> files, Date[] dates) throws SearchException {
        if (files == null || files.isEmpty()) {
            throw new SearchException("no records available");
        }
        SimpleDateFormat sobj = new SimpleDateFormat("dd-MM-yyyy");// format specified in double quotes
        Arrays.sort(dates);
        HashMap<Date, File> bestMatchFileMap = matchDatesToFiles(dates, files);
        return bestMatchFileMap;

    }

    private HashMap<Date, File> matchDatesToFiles(Date[] dates, List<File> files) {
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

    private File getBestFileMatchToDate(List<File> files, long preferredTime) {
        File currentBestFile = null;
        try {
            for (File testFile : files) {
                long currentFileCreated = setTimeOrZero(currentBestFile);
                long testFileCreated = setTimeOrZero(testFile);
                long currentFileDifference=Math.abs(currentFileCreated - preferredTime);
                long testFileDifference=Math.abs(testFileCreated - preferredTime);
                if (currentFileDifference > testFileDifference||currentFileCreated==0) {
                    currentBestFile = testFile;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(BattlefieldSearchParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return currentBestFile;
    }

    private long setTimeOrZero(File testFile) throws IOException {
        long retval = 0;
        if (testFile != null) {
            retval = Files.readAttributes(testFile.toPath(), BasicFileAttributes.class).creationTime().toMillis();
        }
        return retval;
    }

    private BattleRecordSearch createBattleSearch(String gang, String opponentName, String folder, String fileExtension) {
        String oGang = Optional.of(gang).orElse("");
        String oOpponentName = Optional.of(opponentName).orElse("");
        BattleRecordSearch brs = setSearchParameters(folder, oGang, oOpponentName, fileExtension);
        return brs;
    }

    private BattleRecordSearch setSearchParameters(String folder, String oGang, String oOpponentName, String fileExtension) {
        BattleRecordSearch brs = new BattleRecordSearch().setFolder(folder).setGang(oGang).setOpponentGangOCRName(oOpponentName);
        brs.setFolder(folder).setExtension(fileExtension);
        return brs;
    }

    private void throwExceptionIfFolderNotSpecified(String folder) throws SearchException {
        if (folder == null || folder.isEmpty()) {
            throw new SearchException("No folder specified");
        }
    }
}
