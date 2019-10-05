/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

/**
 *
 * @author adamo
 */
public class TaskTime {

    private long timeStarted;
    private long timeComplete;

    public TaskTime setTimeStarted() {
        this.timeStarted = System.currentTimeMillis();
        return this;
    }

    public TaskTime setTimeComplete() {
        this.timeComplete = System.currentTimeMillis();
        return this;
    }

}
