/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.datasheet;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;


/**
 *
 * @author Zach Chapasko
 */

public class UploadParser {
/*Parses iGEM Parts Registry XML pages for a given part
 Returns JSON object of relevant information*/
    
    public static void main(String[] args) throws JSONException {
        
        //Get part XML pages from Parts Registry
        ArrayList<String> partXMLs = getXML();
        
        //Parse through XML pages for relevant info
        String[] parsedString = parseXML(partXMLs);
       
        //Write relevant info to JSON Object for client
        JSONObject partInfo = writeJSONObject(parsedString);
        


        //Test print statements for writeJSONObject
        System.out.println(partInfo.toString());
    
    }

    public static ArrayList<String> getXML() {
        
        ArrayList<String> xmlStrings = new ArrayList<String>();
        
        //For the file provided, create a part Document
        //uses getPartDocument method 
//        for (String name : partNames) { 
            List<String> partDocument = getPartDocument();
            xmlStrings.add(partDocument.toString());            
//        }
        return xmlStrings;        
    }
    
    //given a part name, create a document object corresponding to the DOM

    public static List<String> getPartDocument() {
        
        List<String> partDoc;
        try {
            Path p1 = Paths.get(URI.create("file:///Users/Zach/Desktop/B0035.rtf"));
            partDoc = Files.readAllLines(p1, StandardCharsets.US_ASCII);
            return partDoc;
        } catch (IOException e) {
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
            
            //Name Strings
            
            //GenBank
            if (line.contains("VNTNAME|")) {               
                int start = line.indexOf("VNTNAME|") + 8;
                int end = line.indexOf("VNTAUTHORNAME|") - 15;
                jsonStringOUT.append(line);
                partNameString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            //Registry
            if (line.contains("<part_name>")) {               
                int start = line.indexOf("<part_name>") + 11;
                int end = line.indexOf("</part_name>");
                jsonStringOUT.append(line);
                partNameString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            //SBOL
            if (line.contains("<s:name>")) {               
                int start = line.indexOf("<s:name>") + 8;
                int end = line.indexOf("</s:name");
                jsonStringOUT.append(line);                
                partNameString = "BBa_" + jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            
            //Summary Strings
            
            //Registry
            if (line.contains("<part_short_desc>")) {
                int start = line.indexOf("<part_short_desc>") + 17;
                int end = line.indexOf("</part_short_desc>");
                partSummaryString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            //SBOL
            if (line.contains("<s:description>")) {
                int start = line.indexOf("<s:description>") + 15;
                int end = line.indexOf("</s:description>");
                partSummaryString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            
            //Type Strings
            
            //Registry
            if (line.contains("<part_type>")) {
                int start = line.indexOf("<part_type>") + 11;
                int end = line.indexOf("</part_type>");
                partTypeString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();            
            }
            
            //Date Strings
            
            //GenBank
            if (line.contains("LOCUS")) {
                int start = line.indexOf("LOCUS") + 63;
                int end = line.indexOf(",");
                partDateString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            //Registry
            if (line.contains("<part_entered>")) {
                int start = line.indexOf("<part_entered>") + 14;
                int end = line.indexOf("</part_entered>");
                partDateString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            
            //Author Strings
            
            //GenBank            
            if (line.contains("VNTAUTHORNAME|")) {
                int start = line.indexOf("VNTAUTHORNAME|") + 14;
                int end = line.indexOf("Vector_NTI_Display_Data") - 15;
                partAuthorString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            //Registry
            if (line.contains("<part_author>")) {
                int start = line.indexOf("<part_author>") + 13;
                int end = line.indexOf("</part_author");
                partAuthorString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            
            //Sequence Strings
            
            //GenBank            
            if (line.contains("ORIGIN")) {
                int start = line.indexOf("ORIGIN") + 12;
                int end = line.indexOf(" //");
                seqDataString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("cf","").replaceAll("[0-9]","").trim();
                seqDataString = seqDataString.replaceAll(" ","").replaceAll(",","");
            }
            //Registry
            if (line.contains("<seq_data>")) {
                int start = line.indexOf("<seq_data>") + 10;
                int end = line.indexOf("</seq_data");
                seqDataString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }
            //SBOL
            if (line.contains("<s:nucleotides>")) {
                int start = line.indexOf("<s:nucleotides>") + 15;
                int end = line.indexOf("</s:nucleotides>");
                seqDataString = jsonStringOUT.substring(start, end).replaceAll("\n", "").replaceAll("\\\\","").replaceAll("cf[0-9]","").trim();
            }         
        }

        //put all variables into a String array
        String[] partInfoStrArr = {partNameString, partSummaryString, 
            partTypeString, partDateString, partAuthorString, seqDataString};
        
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

    
    
}