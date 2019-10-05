/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.googletools;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author adamo
 */
public class GoogleAuth {

    static boolean isAuthed = false;

    static void authExplicit(String jsonPath) throws IOException {
        if (!isAuthed) {
            // You can specify a credential file by providing a path to GoogleCredentials.
            // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath));
            // credentials.createScoped(Lists.newArrayList());
            /*
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            System.out.println("Buckets:");
            Page<Bucket> buckets = storage.list();
            for (Bucket bucket : buckets.iterateAll()) {
                System.out.println(bucket.toString());
            }*/
            isAuthed = true;
        }
    }
}
