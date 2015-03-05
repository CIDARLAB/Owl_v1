/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.datasheet;

import static org.cidarlab.datasheet.XMLParser.getXML;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author jenhantao
 */
@MultipartConfig(location="/Users/Zach/Documents/Owl/Test/")
public class ParserServlet extends HttpServlet {
//Server side communication code

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    public static String getFilepath()
    {        
        String filepath;
        
        filepath = ParserServlet.class.getClassLoader().getResource(".").getPath();
        System.out.println(filepath);
        
        //filepath = filepath.substring(0,filepath.indexOf("/target/"));
        return filepath;
    }
    
    private static String getValue(Part part) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            value.append(buffer, 0, length);
        }
        return value.toString();
    }
    
       
    protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
                
        String ipAddress = request.getHeader("X-FORWARDED-FOR");  
        if (ipAddress == null)
        {  
	   ipAddress = request.getRemoteAddr();  
        }
        ipAddress = ipAddress.replaceAll("[^0-9]","");
        
        String ipAndTime = ipAddress + "_" + System.currentTimeMillis();
                
        String filename = ipAndTime + "_image";
        String extension;
        ArrayList<String> imageNames = new ArrayList<String>();
        
        int i = 0;
        Part image = null;
        image = request.getPart("image" + i);
        
        while(image != null)
        {
            extension = image.getContentType();
            extension = "." + extension.substring(extension.indexOf("/") + 1);
                        
            image.write(filename + i + extension);
            imageNames.add(filename + i + extension);
            
            i++;
            image = null;
            image = request.getPart("image" + i);
        }        
               
        String mode;
        mode = getValue(request.getPart("mode"));
        
        if(mode.equals(modes.makeLatex.toString()))
        {
            String latexJSON = getValue(request.getPart("latex"));
            System.out.println(latexJSON);
            
            //File image = request.getParameter("file");
            
            Map<String,String> map = new LinkedHashMap<String,String>();
            ObjectMapper mapper = new ObjectMapper();
            
            
            
            
            try {
                //System.out.println("Latex String : "+ latexJSON);
		//convert JSON string to Map
		map = mapper.readValue(latexJSON, 
		    new TypeReference<LinkedHashMap<String,String>>(){});               
 
            } catch (IOException e) {
		e.printStackTrace();
            }
            
            int wellKeys = 0;
            int title = 1;
            
            Map<String, String> newMap = new LinkedHashMap<String,String>();
            Map<String, String> imgMap = new LinkedHashMap<String,String>();
            
            boolean notImageBlock = false;
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().contains("<imglink>") || entry.getKey().contains("<imgupload>"))
                {
                    imgMap.put(entry.getKey(), entry.getValue());
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
            List<String> fileInfo = LatexCreator.writeLatex(ipAndTime, latexString);
            //System.out.println("/usr/texbin/pdflatex --shell-escape -output-directory=/Users/Zach/Documents/Owl/igem-datasheet/Datasheet_Generator/tmp/ " + fileInfo.get(0));
            Process p;
            try{
                p = Runtime.getRuntime().exec("/usr/texbin/pdflatex --shell-escape -output-directory=/Users/Zach/Documents/Owl/igem-datasheet/Datasheet_Generator/tmp/ " + fileInfo.get(0));
                p.waitFor();
            } catch (Exception e) {
            }
            
            String PDFpath = "/Users/Zach/Documents/Owl/igem-datasheet/Datasheet_Generator/tmp/" + fileInfo.get(1);
            
            //System.out.println("PDFpath is: " + PDFpath);
            
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("filename",PDFpath);      
                    
            data = dataToSend;
            
            holdingData = true;
            PrintWriter out = response.getWriter();
            out.write(data.toString());
                      
        }
        else if(mode.equals(modes.search.toString()))
        {
            
        }
        else
        {
            
        }
        
        String part = request.getParameter("file");

        String name = request.getParameter("name");
        
        if(null != name){
        //There will be an arraylist of actual part names
        ArrayList<String> partNames = new ArrayList<String>();
//        partNames.add("K1114000");
        partNames.add(name);

        //Get part XML pages from Parts Registry
        ArrayList<String> partXMLs = getXML(partNames);

        //Parse through XML pages for relevant info
        String[] parsedString = XMLParser.parseXML(partXMLs);

        //Write relevant info to JSON Object for client
        JSONObject partInfo = XMLParser.writeJSONObject(parsedString);
        //save the data for use after redirect
        
        //appendLatex(parsedString); /////////////////////////////////////////
        
        data = partInfo;
        
        System.out.println("partInfo" + partInfo);
        
        holdingData = true;
        PrintWriter out = response.getWriter();
            out.write(data.toString());
        response.sendRedirect("dynamicForm.html");
        
        }
        
        if(null != part){
            
        ArrayList<String> partXMLs = new ArrayList<String>();
      
        partXMLs.add(part);
        
        //Parse through XML pages for relevant info
        String[] parsedString = UploadParser.parseXML(partXMLs);

        //Write relevant info to JSON Object for client
        JSONObject partInfo = UploadParser.writeJSONObject(parsedString);
        //save the data for use after redirect
        
        //appendLatex(parsedString); /////////////////////////////////////////
        
        data = partInfo;
        
        System.out.println("partInfo" + partInfo);
        
        holdingData = true;
        PrintWriter out = response.getWriter();
            out.write(data.toString());
        response.sendRedirect("dynamicForm.html");  
            
        }

    }

    protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        if (holdingData) {
            holdingData = false;
            //return the held data and vacate the stored data
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(data.toString());
        }

    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processGetRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(ParserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processPostRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(ParserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    private Boolean holdingData = false;
    private JSONObject data;
}
