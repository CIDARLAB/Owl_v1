/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cidarlab.datasheet;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import java.io.FileReader;
//import static org.cidarlab.datasheet.XMLParser.getXML;

/**
 *
 * @author zach_chapasko
 */
public class SheetTest {
    
    public static String getFilepath()
    {        
        String filepath;
        
        filepath = SheetTest.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("target/"));
        filepath += "src/main/webapp/tmp/";
        //System.out.println("\nFILEPATH: " + filepath);
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
            ImageIO.write(bImage, ext, new File(getFilepath() + newName));
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
        String path = getFilepath();
        
        ArrayList<String> fileNames = new ArrayList<String>();
//        fileNames.add(path + "test1.txt");
//        fileNames.add(path + "test2.txt");
        fileNames.add(path + "BBa_K678001.txt");
//        fileNames.add(path + "BBa_K783067.txt");
//        fileNames.add(path + "BBa_K1179002.txt");
//        fileNames.add(path + "CoxRG_AF.txt");
//        fileNames.add(path + "BBa_J23100.txt");
//        fileNames.add(path + "BBa_K1114107.txt");
//        fileNames.add(path + "BBa_K1114211.txt");
//        fileNames.add(path + "BBa_B0015.txt");
//        fileNames.add(path + "BBa_K1114400.txt");
//        fileNames.add(path + "BBa_pSB1K3.txt");
        
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
            
            if(!imgMap.isEmpty())
            {
                newMap.putAll(imgMap);
            }
            
            String latexString = LatexCreator.makeLatex(imageNames, newMap);
            System.out.println(latexString);
            //System.out.println(latexString);
            latexName = System.currentTimeMillis() + "_" + String.valueOf(i);
            
            List<String> fileInfo = LatexCreator.writeLatex(latexName, latexString);
            //System.out.println(fileInfo.get(0));
            //System.out.println("/usr/texbin/pdflatex --shell-escape -output-directory=/Users/Zach/Documents/Owl/igem-datasheet/Datasheet_Generator/tmp/ " + fileInfo.get(0));
            Process p1;
            try{
                p1 =  Runtime.getRuntime().exec("/usr/texbin/pdflatex --shell-escape -output-directory=" + path + "PDFs/ " + fileInfo.get(0)); //IMPORTANT, MUST CHANGE THIS LINE
                p1.waitFor();
            } catch (Exception e) {
            }
            
            //String PDFpath = path + "PDFs/" + fileInfo.get(1);
            //System.out.println(PDFpath);
            
        }
        
    }
    
}
