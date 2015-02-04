/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.datasheet;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

/**
 *
 * @author Zach Chapasko
 */

public class SBOLParser {  //Rename class and file to "XMLParser" to test
/*Parses SBOL XML pages for a given part
 Returns JSON object of relevant information*/
    
    public static void main(String[] args) throws JSONException {
        
        //There will be an arraylist of actual part names
        ArrayList<String> partNames = new ArrayList<String>();
        partNames.add("B0034");
        
        //Get part XML pages from SBOL
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
            Document partDocument = getPartDocument(name);
            xmlStrings.add(partDocument.toString());            
        }
        
        return xmlStrings;        
    }
    
    //given a part name, create a document object corresponding to the DOM
    public static Document getPartDocument(String partName) {
        
        Document partDoc;
        try {
            partDoc = Jsoup.connect("http://convert.sbols.org/biobrick/" + partName)
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
            if (line.contains("<s:name>")) {               
                int start = line.indexOf("<s:name>") + 8;
                int end = line.indexOf("</s:name");
                jsonStringOUT.append(line);                
                partNameString = "BBa_" + jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            }
            if (line.contains("<s:description>")) {
                int start = line.indexOf("<s:description>") + 15;
                int end = line.indexOf("</s:description>");
                partSummaryString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
            } 
            
    //Note: the following three components are not available from SBOL files
            
//            if (line.contains("<rdf:type rdf:resource=\"http://partsregistry.org/type/")) {
//                int start = line.indexOf("<rdf:type rdf:resource=\"http://partsregistry.org/type/") + 55;
//                int end = line.indexOf(">") + start;
//                System.out.println("start is: " + start + "\n" + "end is: " + end);
//                System.out.println("\n" + jsonStringOUT.substring(start,end));
//                //partTypeString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
//                //System.out.println(partTypeString);
//            } 
//            if (line.contains("<part_entered>")) {
//                int start = line.indexOf("<part_entered>") + 14;
//                int end = line.indexOf("</part_entered>");
//                partDateString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
//            }
//            if (line.contains("<part_author>")) {
//                int start = line.indexOf("<part_author>") + 13;
//                int end = line.indexOf("</part_author");
//                partAuthorString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
//            }
            
            if (line.contains("<s:nucleotides>")) {
                int start = line.indexOf("<s:nucleotides>") + 15;
                int end = line.indexOf("</s:nucleotides>");
                seqDataString = jsonStringOUT.substring(start, end).replaceAll("\n", "").trim();
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