/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.virtualbox;

import com.adamoutler.cats.screen_id.ScreenData;
import com.adamoutler.imagerecognition.XYCoords;
import com.adamoutler.virtualbox.task.tasks.sentry.FastBuildingOwnershipDetector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import se.vidstige.jadb.JadbDevice;

/**
 *
 * @author adamo
 */
public class VirtualBoxMachineTest {
    
    public VirtualBoxMachineTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

   

    /**
     * Test of DEBUGaddImages method, of class VirtualBoxMachine.
     */
    @Test
    public void testDEBUGaddImages() {
        System.out.println("DEBUGaddImages");
        BufferedImage[] image = getImages("CITYKINGS.2.png","CITYKINGS.1.png","CITYKINGS.2.png","CITYKINGS.1.png");
        ArrayList<VirtualBoxMachine> instanceList = new VirtualBox().getGangMachines("development");
        if (!instanceList.isEmpty()){
        VirtualBoxMachine instance=instanceList.get(0);
        instance.DEBUGaddImages(image);
        FastBuildingOwnershipDetector.run(instance);
        FastBuildingOwnershipDetector.run(instance);
        FastBuildingOwnershipDetector.run(instance);
                FastBuildingOwnershipDetector.run(instance);

        }
        
    }

    public final static String LOCATION="src/test/java/com/adamoutler/cats/catsobjects/resources/allthescreens/";
    BufferedImage[] getImages(String... names){
        LinkedList<BufferedImage> ll=new LinkedList<>();
        for (String name:names){
            try {
                ll.add(ImageIO.read(new File(LOCATION+name)));
            } catch (IOException ex) {
                Logger.getLogger(VirtualBoxMachineTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ll.toArray(new BufferedImage[ll.size()]);
    }
    
    @Test
    public void testRecording() throws InterruptedException{
        try {
            System.out.println("Test recording");
            ArrayList<VirtualBoxMachine> allvms=new VirtualBox().getActiveMachines();
            ArrayList<VirtualBoxMachine> vms=new ArrayList<>();
            for (VirtualBoxMachine vm:allvms){
                if (vm.isPoweredOn())vms.add(vm);
            }
            assumeTrue(!vms.isEmpty());
            VirtualBoxMachine vm=vms.get(0);
            File f=File.createTempFile("fasdfasdf",".WebM");
            f.delete();
            vm.startRecording(f);
            
            Thread.sleep(5000);
            vm.stopRecording();
            
        } catch (IOException ex) {
            Logger.getLogger(VirtualBoxMachineTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
    
}
