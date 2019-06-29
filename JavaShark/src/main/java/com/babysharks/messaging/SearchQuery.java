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
public class SearchQuery {

    private String type;
    private String gang;
    private String username;
    private String opponentName;
    private Long centerTimestamp;
    private Long range;
    private String extension;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     * @return reference to this object
     */
    public SearchQuery setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * @return the gang
     */
    public String getGang() {
        return gang;
    }

    /**
     * @param gang the gang to set
     * @return reference to this object
     */
    public SearchQuery setGang(String gang) {
        this.gang = gang;
        return this;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     * @return reference to this object
     */
    public SearchQuery setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * @return the opponentName
     */
    public String getOpponentName() {
        return opponentName;
    }

    /**
     * @param opponentName the opponentName to set
     * @return reference to this object
     */
    public SearchQuery setOpponentName(String opponentName) {
        this.opponentName = opponentName;
        return this;
    }

    /**
     * @return the centerTimestamp
     */
    public Long getCenterTimestamp() {
        return centerTimestamp;
    }

    /**
     * @param centerTimestamp the centerTimestamp to set
     * @return reference to this object
     *
     */
    public SearchQuery setCenterTimestamp(long centerTimestamp) {
        this.centerTimestamp = centerTimestamp;
        return this;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the range
     */
    public Long getRange() {
        return range;
    }

    /**
     * @param range the range to set
     * @return reference to this object
     */
    public SearchQuery setRange(Long range) {
        this.range = range;
        return this;
    }
}
