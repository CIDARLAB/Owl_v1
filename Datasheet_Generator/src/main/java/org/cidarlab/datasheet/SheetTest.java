/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cidarlab.datasheet;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;
import static org.cidarlab.datasheet.XMLParser.getXML;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import java.io.FileReader;

/**
 *
 * @author Zach
 */
public class SheetTest {
    
    public static String getFilepath()
    {        
        String filepath;
        
        filepath = SheetTest.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("/target/"));
        return filepath;
    }
    
    public static String writeImage(String path, String oldName){
        BufferedImage bImage = null;
        File image = new File(path + oldName);
        String ext = FilenameUtils.getExtension(path + oldName);
        
        try {
            bImage = ImageIO.read(image);
        } catch (IOException e) {
        }
        
        String newName = System.currentTimeMillis() + oldName;
        
        try{
        ImageIO.write(bImage, ext, new File("/Users/Zach/Documents/Owl/Test/" + newName));
        } catch (IOException e){
        }
        
        return newName;
    }
    
    
    public static String reader(String filename) throws FileNotFoundException, IOException{
        String everything = "";
        BufferedReader br = new BufferedReader(new FileReader(filename));
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
    
    public static void main(String[] args) throws IOException, InterruptedException {
        
        //getFilepath(): /Users/Zach/Documents/Owl/igem-datasheet/Datasheet_Generator
        String path = getFilepath() + "/tmp/";
        
        ArrayList<String> fileNames = new ArrayList<String>();
        fileNames.add(path + "test1.txt");
        fileNames.add(path + "test2.txt");
        
        String latexName;
        
        int i;
        String latexJSON = "";
        for(i = 0; i < fileNames.size(); i++){
            ArrayList<String> imageNames = new ArrayList<String>();
            
            latexJSON = reader(fileNames.get(i));
            Map<String,String> map = new LinkedHashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            
            try {
		map = mapper.readValue(latexJSON, 
		new TypeReference<LinkedHashMap<String,String>>(){});               
 
            } catch (IOException e) {
		e.printStackTrace();
            }
            
            Map<String, String> newMap = new LinkedHashMap<>();
            Map<String, String> imgMap = new LinkedHashMap<>();
            
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().contains("<imglink>") || entry.getKey().contains("<imgupload>"))
                {
                    if(entry.getKey().contains("<imglink>")){
                        imgMap.put(entry.getKey(), entry.getValue());
                    }
                    else{
                        imageNames.add(writeImage(path, entry.getValue()));
                        imgMap.put(entry.getKey(), "");
                    }
                }
                else 
                {
                    if(entry.getKey().contains("title"))
                    {
                        newMap.putAll(imgMap);
                        imgMap = new LinkedHashMap<String,String>();
                    }
                    newMap.put(entry.getKey(), entry.getValue());
                }
            }
            
            String latexString = LatexCreator.makeLatex(imageNames, newMap);
            //System.out.println(latexString);
            latexName = System.currentTimeMillis() + "_" + String.valueOf(i);
            
            List<String> fileInfo = LatexCreator.writeLatex(latexName, latexString);
            //System.out.println(fileInfo.get(0));
            //System.out.println("/usr/texbin/pdflatex --shell-escape -output-directory=/Users/Zach/Documents/Owl/igem-datasheet/Datasheet_Generator/tmp/ " + fileInfo.get(0));
            Process p1;
            try{
                p1 =  Runtime.getRuntime().exec("/usr/texbin/pdflatex --shell-escape -output-directory=" + path + " " + fileInfo.get(0));
                p1.waitFor();
            } catch (Exception e) {
            }
            
            //String PDFpath = path + "PDFs/" + fileInfo.get(1);
            //System.out.println(PDFpath);
            
        }
        
        Process p2;
        try{
            //p2 = Runtime.getRuntime().exec("mv " + getFilepath() + "/*.png /tmp/");
            p2 = Runtime.getRuntime().exec("rm /Users/Zach/Documents/Owl/igem-datasheet/Datasheet_Generator/*.png");
            p2.waitFor();
        } catch (Exception e) {
        }
        
    }
    
}
