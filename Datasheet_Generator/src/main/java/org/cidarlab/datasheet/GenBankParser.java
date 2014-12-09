/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.datasheet;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Zach Chapasko
 */

public class GenBankParser {
/*Parses iGEM Parts Registry XML pages for a given part
 Returns JSON object of relevant information*/
    
    public static void main(String[] args) throws JSONException {
        
        //There will be an arraylist of actual part names
        ArrayList<String> partNames = new ArrayList<String>();
        partNames.add("NM_001138523");
        
        //Get part XML pages from Parts Registry
        ArrayList<String> partXMLs = getXML(partNames);
        
        //Parse through XML pages for relevant info
        String[] parsedString = parseXML(partXMLs);
       
        //Write relevant info to JSON Object for client
        JSONObject partInfo = writeJSONObject(parsedString);
        


        //Test print statements for writeJSONObject
        System.out.println(partInfo.toString());
    
    }

    public static ArrayList<String> getXML(ArrayList<String> partNames) {
        
        ArrayList<String> xmlStrings = new ArrayList<String>();
        
        //For each of the names provided, create a part Document
        //uses getPartDocument method 
        for (String name : partNames) { 
            List<String> partDocument = getPartDocument(name);
            xmlStrings.add(partDocument.toString());            
        }
        
        return xmlStrings;        
    }
    
    //given a part name, create a document object corresponding to the DOM

    public static List<String> getPartDocument(String partName) {
        
        List<String> partDoc;
        try {
            Path p1 = Paths.get(URI.create("file:///Users/Zach/Desktop/pAPCc-invF.txt"));
            Charset charset = Charset.forName("UTF-8");
            partDoc = Files.readAllLines(p1,charset);
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
            if (line.contains("VNTNAME|")) {               
                int start = line.indexOf("VNTNAME|") + 8;
                int end = line.indexOf("VNTAUTHORNAME|") - 15;
                jsonStringOUT.append(line);
                partNameString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            }
//            if (line.contains("DEFINITION")) {
//                int start = line.indexOf("DEFINITION") + 12;
//                int end = line.indexOf("\n");
//                partSummaryString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
//            } 
//            if (line.contains("<part_type>")) {
//                int start = line.indexOf("<part_type>") + 11;
//                int end = line.indexOf("</part_type>");
//                partTypeString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();            
//            } 
            if (line.contains("LOCUS")) {
                int start = line.indexOf("LOCUS") + 63;
                int end = line.indexOf(",");
                partDateString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            }
            if (line.contains("VNTAUTHORNAME|")) {
                int start = line.indexOf("VNTAUTHORNAME|") + 14;
                int end = line.indexOf("Vector_NTI_Display_Data") - 15;
                partAuthorString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            }
            if (line.contains("ORIGIN")) {
                int start = line.indexOf("ORIGIN") + 12;
                int end = line.indexOf(" //");
                seqDataString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
                seqDataString = seqDataString.replace(" ","");
                seqDataString = seqDataString.replace(",","");
                seqDataString = seqDataString.replace("1","");
                seqDataString = seqDataString.replace("2","");
                seqDataString = seqDataString.replace("3","");
                seqDataString = seqDataString.replace("4","");
                seqDataString = seqDataString.replace("5","");
                seqDataString = seqDataString.replace("6","");
                seqDataString = seqDataString.replace("7","");
                seqDataString = seqDataString.replace("8","");
                seqDataString = seqDataString.replace("9","");
                seqDataString = seqDataString.replace("0","");
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