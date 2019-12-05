/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.babysharks.messaging;

/**
 *
 * @author adamo
 */
public enum ErrorType {
    NOERROR("NO error detected."),
    NOPLACEINFO("Place information was not available"),
    USERHASNOSENTRY("The user has no sentry"),
    VMERROR("There was a problem accessing the requested VM"),
    OTHERSENTRYACTIVE("Another sentry is currently active"),
    NOSENTRIESACTIVE("No sentries are currently active"),
    USERNOTONWATCH("The user is not on watch"),
    NOSUCHTASK("Invalid task requested"),
    COULDNOTFINDMATCHINGRECORDS("While searching for last cars, i could not find a matching record"),
    COULDNOTFINDREQUESTEDUSERINRECORDS("While searching for last cars, i could not find the user"),
    UNRECOGNIZEDSCREEN("I could not identify the screen"),
    INVALIDFILE("I tried to send a file, but no file exists"),
    FILEHASHERROR("There was a problem with hashing the file."),
    WRONGSCREEN("The requested action could not be completed because the sentry is on the wrong screen."),
    DELETEREQUESTINVALID("The request do delete could not be accomplished.  the record did not exist. "),
    WHATEVER("Because F you, that's why. Check the logs if you wanna know.");

    String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
