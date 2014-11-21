/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasheet;

import static datasheet.XMLParser.getXML;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author jenhantao
 */
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
    protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        
//        if(request.getParameter("latex")!=null)
//        {
//            System.out.println("Found that thing!");
//        }
        
        
        
        String mode;
        mode = request.getParameter("mode");
        
        if(mode.equals(modes.makeLatex.toString()))
        {
            String latexJSON = request.getParameter("latex");
            
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
            
            String latexString = LatexCreator.makeLatex(map);
                
            String filePath = LatexCreator.writeLatex(latexString);
            
            Runtime.getRuntime().exec("/usr/texbin/pdflatex -output-directory=/Users/Zach/Documents/Owl/Test/PDF_Docs " + filePath);
                      
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
