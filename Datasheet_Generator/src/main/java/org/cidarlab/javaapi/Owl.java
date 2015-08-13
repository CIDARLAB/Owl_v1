/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cidarlab.javaapi;

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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import java.io.FileReader;
import org.cidarlab.datasheet.LatexCreator;
//import static org.cidarlab.datasheet.XMLParser.getXML;

/**
 *
 * @author zach_chapasko
 */
public class Owl {
    
    protected static String getFilepath()
    {        
        String filepath;
        
        filepath = Owl.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("target/"));
        filepath += "src/main/webapp/tmp/";
        //System.out.println("\nFILEPATH: " + filepath);
        return filepath;
    }
    
    protected static String writeImage(File image, String oldName){
        BufferedImage bImage = null;
        String ext = oldName.substring(oldName.lastIndexOf(".")); //check for off-by-one errors //this should include the . in the extension
        
        System.out.println("\nEXTENSION: " + ext + "\n");
        
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
    
    protected static String reader(File latexMap) throws FileNotFoundException, IOException{
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
    
    public static ArrayList<File> stringsToPDF(ArrayList<String> latexMapStrings, ArrayList<File> images) throws IOException, InterruptedException {
        
        String path = getFilepath();
        int i = 0;
        String latexName;
        ArrayList<File> outputPDFs = new ArrayList<>();
        ArrayList<String> imageNames = new ArrayList<>();
        
        for(String latexJSON : latexMapStrings){
            imageNames.clear();
            Map<String,String> map = new LinkedHashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            
            try {
		map = mapper.readValue(latexJSON, 
		new TypeReference<LinkedHashMap<String,String>>(){});               
 
            } catch (IOException e) {
//		e.printStackTrace();
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
                        for(File tempImage : images){
                            if(entry.getValue().equals(tempImage.getName())){
                                imageNames.add(writeImage(tempImage, entry.getValue()));
                                imgMap.put(entry.getKey(), "");
                                images.remove(tempImage);
                            }
                        }
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
            
            String latexString = LatexCreator.makeLatex(imageNames, newMap, "Owl");
            //System.out.println(latexString);
            latexName = System.currentTimeMillis() + "_" + String.valueOf(i);
            i++;
            
            List<String> fileInfo = LatexCreator.writeLatex(latexName, latexString, "Owl");
            //System.out.println(fileInfo.get(0));
            //System.out.println("/usr/texbin/pdflatex --shell-escape -output-directory=/Users/Zach/Documents/Owl/igem-datasheet/Datasheet_Generator/tmp/ " + fileInfo.get(0));
            Process p1;
            try{
                p1 =  Runtime.getRuntime().exec("/usr/texbin/pdflatex --shell-escape -output-directory=" + path + "PDFs/ " + fileInfo.get(0)); //IMPORTANT, MUST CHANGE THIS LINE
                p1.waitFor();
            } catch (IOException | InterruptedException e) {
            }
            
            //String PDFpath = path + "PDFs/" + fileInfo.get(1);
            //System.out.println(PDFpath);
            
            outputPDFs.add(new File(path + "PDFs/" + fileInfo.get(1)));
        }
        return outputPDFs;
    }
    
    public static ArrayList<File> filesToPDF(ArrayList<File> inputFiles, ArrayList<File> images) throws IOException, InterruptedException {
        ArrayList<String> latexMapStrings = new ArrayList<>();
        for(File tempFile : inputFiles){
            latexMapStrings.add(reader(tempFile));
        }
        return stringsToPDF(latexMapStrings, images);
    }
}

//    public static void writeImages(ArrayList<File> images) throws IOException {
//        String pathAndName;
//        OutputStream out;
//        InputStream fileContent;
//        
//        for(File temp : images){
//            pathAndName = getFilepath() + temp.getName();
//            System.out.println("\nPATH AND NAME: " + pathAndName + "\n");
//            
//            try{
//                out = new FileOutputStream(new File(pathAndName));
//                fileContent = new FileInputStream(temp);
//                
//                int read;
//                final byte[] bytes = new byte[1024];
//                
//                while ((read = fileContent.read(bytes)) != 1) {
//                    out.write(bytes, 0, read);
//                }
//                
//                out.close();
//                
//            } catch (FileNotFoundException fne) {
//                Logger.getLogger(SheetTest.class.getName()).log(Level.SEVERE, null, fne);
//            }
//        }
//    }
