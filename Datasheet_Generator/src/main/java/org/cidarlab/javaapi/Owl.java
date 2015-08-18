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
    
    // Helper method for getting filepath independent of system
    protected static String getFilepath()
    {        
        String filepath;
        filepath = Owl.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("target/"));
        filepath += "src/main/webapp/tmp/";
        //System.out.println("\nFILEPATH: " + filepath);
        return filepath;
    }
    
    // Helper method for writing File object to local filesystem
    protected static String writeImage(File image, String oldName){
        BufferedImage bImage = null;
        
        // Get image extension
        String ext = oldName.substring(oldName.lastIndexOf(".")); //check for off-by-one errors //this should include the . in the extension
        
        System.out.println("\nEXTENSION: " + ext + "\n");
        
        // Read File object into BufferedImage object
        try {
            bImage = ImageIO.read(image);
        } catch (IOException e) {
        }
        
        // Rename the file to a unique name (incorporate timestamp)
        String newName = System.currentTimeMillis() + oldName;
        
        // Try to write the image to the local filesystem
        try{
            ImageIO.write(bImage, ext, new File(getFilepath() + newName));
        } catch (IOException e){
        }
        return newName;
    }
    
    // Helper method to obtain strings from a text file
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
    
    // API method for converting JSON strings and image files to PDF File objects
    public static ArrayList<File> stringsToPDF(ArrayList<String> latexMapStrings, ArrayList<File> images) throws IOException, InterruptedException {
        
        // Variable declaration/initialization in order of first appearance
        ArrayList<String> imageNames = new ArrayList<>();
        Map<String,String> map;
        ObjectMapper mapper;
        Map<String, String> newMap;
        Map<String, String> imgMap;
        String latexString;
        String latexName;
        int i = 0;
        List<String> fileInfo;
        String path = getFilepath();
        ArrayList<File> outputPDFs = new ArrayList<>();
        
        // For each JSON string in the passed ArrayList
        for(String latexJSON : latexMapStrings){
            // Reset reused variables
            imageNames.clear();
            map = new LinkedHashMap<>();
            mapper = new ObjectMapper();
            
            // Try to translate the JSON string into a linked hash map
            try {
		map = mapper.readValue(latexJSON, 
		new TypeReference<LinkedHashMap<String,String>>(){});               
 
            } catch (IOException e) {
		// e.printStackTrace();
            }
            
            // Prepare the image map
            newMap = new LinkedHashMap<>(); // A restructuring of the original map to force all images to appear at the bottom of wells
            imgMap = new LinkedHashMap<>(); // A temporary map to aid the construction of newMap
            
            for(Map.Entry<String, String> entry : map.entrySet()){
                // If an entry in the JSON map refers to an image
                if(entry.getKey().contains("<imglink>") || entry.getKey().contains("<imgupload>"))
                {
                    // If the image is from the web
                    if(entry.getKey().contains("<imglink>")){
                        // Add the image to the image map
                        imgMap.put(entry.getKey(), entry.getValue());
                    }
                    else{
                        // If the image is from a file
                        for(File tempImage : images){
                            // Locate the file based on name
                            if(entry.getValue().equals(tempImage.getName())){
                                // Write the file to the local filesystem using the entry's value
                                imageNames.add(writeImage(tempImage, entry.getValue()));
                                
                                // Add the image to the image map (value is not needed as it is now stored in imageNames above)
                                imgMap.put(entry.getKey(), "");
                                
                                // Remove the image from the passed image file ArrayList (for optimization)
                                images.remove(tempImage);
                            }
                        }
                    }
                }
                else 
                {
                    // If "title" is encountered in an entry then the previous well has finished
                    if(entry.getKey().contains("title"))
                    {
                        // Put all of the well's images at the end and clear imgMap for next well
                        newMap.putAll(imgMap);
                        imgMap = new LinkedHashMap<>();
                    }
                    // Add entries that are not images regularly and in order
                    newMap.put(entry.getKey(), entry.getValue());
                }
            }
            
            // If the last well has images, add them to newMap
            if(!imgMap.isEmpty())
            {
                newMap.putAll(imgMap);
            }
            
            // Make the content of the .tex latex file that will be used in typsetting
            latexString = LatexCreator.makeLatex(imageNames, newMap, "Owl");
            //System.out.println(latexString);
            
            // Unique filename for each .tex file
            latexName = System.currentTimeMillis() + "_" + String.valueOf(i);
            i++;
            
            // Write the latex file to the local filesystem
            fileInfo = LatexCreator.writeLatex(latexName, latexString, "Owl");
            //System.out.println(fileInfo.get(0));            
            
            // Typeset the PDF
            Process p1;
            try{
                p1 =  Runtime.getRuntime().exec("/usr/texbin/pdflatex --shell-escape -output-directory=" + path + "PDFs/ " + fileInfo.get(0)); //IMPORTANT, MAY NEED TO CHANGE THIS LINE
                p1.waitFor();
            } catch (IOException | InterruptedException e) {
            }
            
            // Add PDF to the output ArrayList
            outputPDFs.add(new File(path + "PDFs/" + fileInfo.get(1)));
        }
        return outputPDFs;
    }
    
    // API method for converting JSON text files and image files to PDF File objects
    public static ArrayList<File> filesToPDF(ArrayList<File> inputFiles, ArrayList<File> images) throws IOException, InterruptedException {
        
        ArrayList<String> latexMapStrings = new ArrayList<>();
        
        // Grab the content of each text file to populate an ArrayList of Strings
        for(File tempFile : inputFiles){
            latexMapStrings.add(reader(tempFile));
        }
        
        // Call stringsToPDF on the resulting reads of the text files
        return stringsToPDF(latexMapStrings, images);
    }
}

    // Old method that may be useful one day
    // Takes an ArrayList of images and writes them all to the local filesystem

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
