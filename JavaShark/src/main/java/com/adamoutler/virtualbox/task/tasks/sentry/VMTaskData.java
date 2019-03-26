/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task.tasks.sentry;

import com.adamoutler.cats.citykings.hash_gathering.building.OwnedBy;



/**
 *
 * @author adamo
 */
public class VMTaskData {

    int nullCount = 0;
    public OwnedBy[] previous;
    public OwnedBy[] current;
    long machineBootUpTime=0;
    long gameBootUpTime=0;
    
   
}
