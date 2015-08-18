/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cidarlab.api.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    protected String getFilepath() {
        String filepath;
        filepath = Owl.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("/target/"));
        //System.out.println("\nFILEPATH: " + filepath);
        return filepath;
    }
    
    protected void writePDFs(ArrayList<File> PDFs) throws IOException {
        String path = getFilepath() + "/src/main/resources/OwlTestFiles/PDFs/";
        String pathAndName;
        OutputStream out;
        InputStream fileContent;
        
        for(File temp : PDFs){
            pathAndName = path + temp.getName();
            System.out.println("\nPATH AND NAME: " + pathAndName + "\n");
            
            try{
                out = new FileOutputStream(new File(pathAndName));
                fileContent = new FileInputStream(temp);
                
                int read;
                final byte[] bytes = new byte[1024];
                
                while ((read = fileContent.read(bytes)) != 1) {
                    out.write(bytes, 0, read);
                }
                
                out.close();
                
            } catch (FileNotFoundException fne) {
                Logger.getLogger(CoreTest.class.getName()).log(Level.SEVERE, null, fne);
            }
        }
    }
    
    protected String reader(File latexMap) throws FileNotFoundException, IOException{
        String everything = "";
        BufferedReader br = new BufferedReader(new FileReader(latexMap));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }
        
        return everything;
    }
    
    @Test
    public void fileAPITest() throws IOException, InterruptedException {
        String path = getFilepath() + "/src/main/resources/OwlTestFiles/";
        
        // Text files
        ArrayList<File> textFilesJSON = new ArrayList<>();
        
        textFilesJSON.add(new File(path + "BBa_B0015.txt"));
        textFilesJSON.add(new File(path + "BBa_J23100.txt"));
        textFilesJSON.add(new File(path + "BBa_K1114107.txt"));
        textFilesJSON.add(new File(path + "BBa_K1114211.txt"));
        textFilesJSON.add(new File(path + "BBa_K1114400.txt"));
        textFilesJSON.add(new File(path + "BBa_K1179002.txt"));
        textFilesJSON.add(new File(path + "BBa_K678001.txt"));
        textFilesJSON.add(new File(path + "BBa_K783067.txt"));
        textFilesJSON.add(new File(path + "BBa_pSB1K3.txt"));
        textFilesJSON.add(new File(path + "CoxRG_AF.txt"));
        
        // Images        
        ArrayList<File> images = new ArrayList<>();
        
        images.add(new File(path + "BBa_B0015_pigeon.png"));
        images.add(new File(path + "BBa_B0015_plasmid_map.png"));
        
        images.add(new File(path + "BBa_J23100_pigeon.png"));
        images.add(new File(path + "BBa_J23100_plasmid_map.png"));
        
        images.add(new File(path + "BBa_K1114107_pigeon.png"));
        images.add(new File(path + "BBa_K1114107_plasmid_map.png"));
        
        images.add(new File(path + "BBa_K1114211_pigeon.png"));
        images.add(new File(path + "BBa_K1114211_plasmid_map.png"));
        
        images.add(new File(path + "BBa_K1114400_pigeon.png"));
        images.add(new File(path + "BBa_K1114400_plasmid_map.png"));
        
        images.add(new File(path + "BBa_K1179002_pigeon.png"));
        images.add(new File(path + "BBa_K1179002_plasmid_map.png"));
        images.add(new File(path + "BBa_K1179002_transfer_curve.png"));
        
        images.add(new File(path + "BBa_K678001_growth_curve.png"));
        images.add(new File(path + "BBa_K678001_pigeon.png"));
        images.add(new File(path + "BBa_K678001_plasmid_map.png"));
        images.add(new File(path + "BBa_K678001_transfer_curve.png"));
        
        images.add(new File(path + "BBa_K783067_pigeon.png"));
        images.add(new File(path + "BBa_K783067_transfer_curve.png"));
        
        images.add(new File(path + "BBa_pSB1K3_pigeon.png"));
        images.add(new File(path + "BBa_pSB1K3_plasmid_map.png"));
        
        images.add(new File(path + "CoxRG_AF_pigeon.png"));

        // API call
        ArrayList<File> PDFs = Owl.filesToPDF(textFilesJSON, images);
        
        // Write resulting files to local system
        writePDFs(PDFs);
    }
    
    //@Test
    public void stringAPITest() throws IOException, InterruptedException {
        String path = getFilepath() + "/src/main/resources/OwlTestFiles/";
        
        // Text files
        ArrayList<File> textFilesJSON = new ArrayList<>();
        
        textFilesJSON.add(new File(path + "BBa_B0015.txt"));
        textFilesJSON.add(new File(path + "BBa_J23100.txt"));
        textFilesJSON.add(new File(path + "BBa_K1114107.txt"));
        textFilesJSON.add(new File(path + "BBa_K1114211.txt"));
        textFilesJSON.add(new File(path + "BBa_K1114400.txt"));
        textFilesJSON.add(new File(path + "BBa_K1179002.txt"));
        textFilesJSON.add(new File(path + "BBa_K678001.txt"));
        textFilesJSON.add(new File(path + "BBa_K783067.txt"));
        textFilesJSON.add(new File(path + "BBa_pSB1K3.txt"));
        textFilesJSON.add(new File(path + "CoxRG_AF.txt"));
        
        // Images        
        ArrayList<File> images = new ArrayList<>();
        
        images.add(new File(path + "BBa_B0015_pigeon.png"));
        images.add(new File(path + "BBa_B0015_plasmid_map.png"));
        
        images.add(new File(path + "BBa_J23100_pigeon.png"));
        images.add(new File(path + "BBa_J23100_plasmid_map.png"));
        
        images.add(new File(path + "BBa_K1114107_pigeon.png"));
        images.add(new File(path + "BBa_K1114107_plasmid_map.png"));
        
        images.add(new File(path + "BBa_K1114211_pigeon.png"));
        images.add(new File(path + "BBa_K1114211_plasmid_map.png"));
        
        images.add(new File(path + "BBa_K1114400_pigeon.png"));
        images.add(new File(path + "BBa_K1114400_plasmid_map.png"));
        
        images.add(new File(path + "BBa_K1179002_pigeon.png"));
        images.add(new File(path + "BBa_K1179002_plasmid_map.png"));
        images.add(new File(path + "BBa_K1179002_transfer_curve.png"));
        
        images.add(new File(path + "BBa_K678001_growth_curve.png"));
        images.add(new File(path + "BBa_K678001_pigeon.png"));
        images.add(new File(path + "BBa_K678001_plasmid_map.png"));
        images.add(new File(path + "BBa_K678001_transfer_curve.png"));
        
        images.add(new File(path + "BBa_K783067_pigeon.png"));
        images.add(new File(path + "BBa_K783067_transfer_curve.png"));
        
        images.add(new File(path + "BBa_pSB1K3_pigeon.png"));
        images.add(new File(path + "BBa_pSB1K3_plasmid_map.png"));
        
        images.add(new File(path + "CoxRG_AF_pigeon.png"));
        
        // Convert text files to strings
        ArrayList<String> latexMapStrings = new ArrayList<>();
        for(File tempFile : textFilesJSON){
            latexMapStrings.add(reader(tempFile));
        }

        // API call
        ArrayList<File> PDFs = Owl.stringsToPDF(latexMapStrings, images);
        
        // Write resulting files to local system
        writePDFs(PDFs);
    }
}
