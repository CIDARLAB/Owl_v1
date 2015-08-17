/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cidarlab.api.test;

import java.io.File;
import java.util.ArrayList;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.cidarlab.javaapi.Owl;

/**
 *
 * @author zach_chapasko
 */
public class CoreTest {
    
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
    
    public String getFilepath() {
        String filepath;
        filepath = Owl.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("/target/"));
        //System.out.println("\nFILEPATH: " + filepath);
        return filepath;
    }
    
    
    
}
