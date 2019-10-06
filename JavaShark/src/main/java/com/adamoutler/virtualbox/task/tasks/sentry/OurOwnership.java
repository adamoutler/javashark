/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks.sentry;

/**
 *
 * @author adamo
 */
public class OurOwnership {
    int sizeOfBar;
    int sizeOfOurCars;
    float percent;
    
    public OurOwnership(int sizeOfBar, int sizeOfOurCars){
        this.sizeOfBar=sizeOfBar;
        this.sizeOfOurCars=sizeOfOurCars;
        this.percent=sizeOfOurCars/sizeOfBar;
    }
}
