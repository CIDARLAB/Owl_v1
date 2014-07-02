/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasheet;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

/**
 *
 * @author Pooja Shah
 */

public class XMLParser {
/*Parses iGEM Parts Registry XML pages for a given part
 Returns JSON object of relevant information*/
    
    public static void main(String[] args) throws JSONException, IOException {
        
        //There will be an arraylist of actual part names
        ArrayList<String> partNames = new ArrayList<String>();
        partNames.add("K1114000");
        
        //Get part XML pages from Parts Registry
        ArrayList<String> partXMLs = getXML(partNames);
        
        //Parse through XML pages for relevant info
        String[] parsedString = parseXML(partXMLs);
       
        //Write relevant info to JSON Object for client
        JSONObject partInfo = writeJSONObject(parsedString);

        appendLatex(parsedString); ///////////////////////////////////
        
        //Test print statements for writeJSONObject
        System.out.println(partInfo.toString());
    
    }

    public static ArrayList<String> getXML(ArrayList<String> partNames) {
        
        ArrayList<String> xmlStrings = new ArrayList<String>();
        
        //For each of the names provided, create a part Document
        //uses getPartDocument method 
        for (String name : partNames) { 
            Document partDocument = getPartDocument(name);
            xmlStrings.add(partDocument.toString());            
        }
        
        return xmlStrings;        
    }
    
    //given a part name, create a document object corresponding to the DOM
    public static Document getPartDocument(String partName) {
        
        Document partDoc;
        try {
            partDoc = Jsoup.connect("http://parts.igem.org/xml/part." + partName)
                    .timeout(10000000)
                    .parser(Parser.xmlParser())
                    .get();
            return partDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }
   
    public static String[] parseXML(ArrayList<String> XMLstring) {
        
        //parse XML and convert to String Builder
        StringBuilder jsonStringOUT = new StringBuilder();
        
        //initialize variables for relevant data
        String partNameString = "";
        String partSummaryString = "";
        String partTypeString = "";
        String partDateString = "";
        String partAuthorString = "";
        String seqDataString = "";
        
        //Parse XML, find relevant info, and set to variable as String
        for (String line : XMLstring) {
            if (line.contains("<part_name>")) {               
                int start = line.indexOf("<part_name>") + 11;
                int end = line.indexOf("</part_name>");
                jsonStringOUT.append(line);
                partNameString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            }
            if (line.contains("<part_short_desc>")) {
                int start = line.indexOf("<part_short_desc>") + 17;
                int end = line.indexOf("</part_short_desc>");
                partSummaryString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            } 
            if (line.contains("<part_type>")) {
                int start = line.indexOf("<part_type>") + 11;
                int end = line.indexOf("</part_type>");
                partTypeString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();            
            } 
            if (line.contains("<part_entered>")) {
                int start = line.indexOf("<part_entered>") + 14;
                int end = line.indexOf("</part_entered>");
                partDateString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            }
            if (line.contains("<part_author>")) {
                int start = line.indexOf("<part_author>") + 13;
                int end = line.indexOf("</part_author");
                partAuthorString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            }
            if (line.contains("<seq_data>")) {
                int start = line.indexOf("<seq_data>") + 10;
                int end = line.indexOf("</seq_data");
                seqDataString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            }
        }

        //put all variables into a String array
        String[] partInfoStrArr = {partNameString, partSummaryString, 
            partTypeString, partDateString, partAuthorString, seqDataString};
        
        System.out.println(partInfoStrArr[0] + "\n" + partInfoStrArr[1] + "\n" + partInfoStrArr[2] + "\n" + partInfoStrArr[3] + "\n" + partInfoStrArr[4] + "\n" + partInfoStrArr[5] + "\n");
        
        
        return partInfoStrArr;    //return String array of relevant info
    }

    public static JSONObject writeJSONObject(String[] partInfoStrArr) throws JSONException {
        //take elements from array in String form and convert to JSON objects       
        
        //initialize JSONObjects
        JSONObject partsInfoJSON = new JSONObject();
        JSONObject designInformation = new JSONObject();      
        JSONObject contactInformation = new JSONObject();
                
        //make JSONObject partsInfoJSON
        partsInfoJSON.put("name", partInfoStrArr[0]);
        partsInfoJSON.put("summary", partInfoStrArr[1]);
        
        designInformation.put("deviceType", partInfoStrArr[2]);
        designInformation.put("date", partInfoStrArr[3]);
        partsInfoJSON.put("designInformation", designInformation);
        
        contactInformation.put("authors", partInfoStrArr[4]);
        partsInfoJSON.put("contactInformation", contactInformation);
        partsInfoJSON.put("sequence", partInfoStrArr[5]);
        
        return partsInfoJSON; //return partsInfoJSON JSON object
    }
    
    public static void appendLatex(String[] partInfoStrArr) throws IOException{
    
        Path p1 = Paths.get("/Users/Zach/Documents/Owl/Test/Test.tex");
        Path p2 = Paths.get("/Users/Zach/Documents/Owl/Test/Blank.tex");
        Charset charset = StandardCharsets.UTF_8;

        try{
        String content = new String(Files.readAllBytes(p1), charset);
        
        content = content.replace("BBa\\_",partInfoStrArr[0]);
        content = content.replace("{Summary}\t\t&","{Summary}\t\t&" + " " + partInfoStrArr[1]);
        content = content.replace("{Part Type}\t\t&","{Part Type}\t\t&" + " " + partInfoStrArr[2]);
        content = content.replace("{Sequence}\t\t&","{Sequence}\t\t&" + " " + partInfoStrArr[5]);
        content = content.replace("{Author(s)}\t\t\t\t&","{Author(s)}\t\t\t\t&" + " " + partInfoStrArr[4]);
        content = content.replace("{Date}\t\t\t\t\t&","{Date}\t\t\t\t\t&" + " " + partInfoStrArr[3]);
        content = content.replace("_","\\_");
        
        Files.write(p2, content.getBytes(charset));
        }catch (IOException e) {
            System.err.println(e);
        }
       
        
//        String docpath = "/Users/Zach/Documents/Owl/Test/Test.tex";
//        String line;
//        BufferedReader br;
//        BufferedWriter out = null;
//        try{
//            br = new BufferedReader(new FileReader(docpath));
//            out = new BufferedWriter(new FileWriter(docpath, true));
//            while((line = br.readLine()) != null){
//                if(line.contains("BBa\\_")){
//                    int start = line.indexOf("BBa\\_");
//                    int end = line.indexOf("BBa\\_") + 5;
//                    line = line.substring(0,start) + line.substring(start,end).replace("BBa\\_",partInfoStrArr[0]) + line.substring(end,line.indexOf("Name") + 4);
//                    line = line.replace("_","\\_");
//                    System.out.println("This is the line: " + line + "\n");
//                    out.write(line);
//                }    
//            }
//            out.flush();
//        }catch (IOException e) {
//            System.err.println(e);
//        }finally{
//            if(out != null){
//            out.close();
//            }
    }      
        
}