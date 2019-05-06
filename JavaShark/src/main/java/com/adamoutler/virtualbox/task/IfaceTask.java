/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox.task;

import com.adamoutler.virtualbox.VirtualBoxMachine;

/**
 *
 * @author adamo
 */
public interface IfaceTask {
    public void init(VirtualBoxMachine vm);
    public void exec(VirtualBoxMachine vm);
    public boolean resume(VirtualBoxMachine vm);
}
