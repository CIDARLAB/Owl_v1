/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.api.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.javaapi.Owl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class PhoenixTest {
    
    @Test
    public void testOwlAPI(){
        String filepath = "/home/prash/cidar/testOWL/";
        String latexpath = "/usr/bin/pdflatex";
        JSONObject object1 = new JSONObject();
        object1.put("title1", "Test Part1");
        object1.put("Part Name", "Part ABC");
        object1.put("title2", "Module Info");
        object1.put("module type", "TU");
        
        JSONObject object2 = new JSONObject();
        object2.put("title1", "Test Part2");
        object2.put("Part Name", "Part BCD Inverter");
        object2.put("title2", "Module Info");
        object2.put("module type", "HF");
        
        ArrayList<String> jsonStrings = new ArrayList<String>();
        jsonStrings.add(object1.toString());
        jsonStrings.add(object2.toString());
        
        ArrayList<File> images = new ArrayList<File>();
        try {
            Owl.jsonStringsToPDF(jsonStrings, images, filepath,latexpath);
        } catch (IOException ex) {
            Logger.getLogger(PhoenixTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PhoenixTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
