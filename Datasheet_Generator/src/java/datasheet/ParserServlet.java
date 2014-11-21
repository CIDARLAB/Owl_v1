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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
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
            System.out.println("Make Latex Button was clicked");
            String latexJSON = request.getParameter("latex");
            
            Map<String,String> map = new LinkedHashMap<String,String>();
            ObjectMapper mapper = new ObjectMapper();
            
            try {
                System.out.println("Latex String : "+ latexJSON);
		//convert JSON string to Map
		map = mapper.readValue(latexJSON, 
		    new TypeReference<LinkedHashMap<String,String>>(){});
                System.out.println("Map Size is : " + map.size());
                for(Map.Entry<String, String> entry : map.entrySet())
                {
                    //System.out.println("Printing an entry");
                    System.out.println(entry.getKey() + ":" +entry.getValue());
                }
		//System.out.println(map);
 
            } catch (IOException e) {
		e.printStackTrace();
            }
            
            
            /*
            System.out.println("If converted to map, you will see it ABOVE this message!");
            
            if(latexJSON!= null)
            {
                System.out.println("Latex code: ");
                System.out.println(latexJSON);
            }
            else
                System.out.println("Null value passed"); 
            
            JSONObject jsonObj = new JSONObject(latexJSON);
            //HashMap<String,Object> result = new ObjectMapper().readValue(jsonObj, HashMap.class);
            if (jsonObj instanceof JSONObject) {
                System.out.println("\n\nITS A JSONObject!!\n\n");
            }
            else
            {
                System.out.println("\n\nNot a JSONObject :(\n\n");
            }
            
            if(true)
            {
            
                String[] dataKeys = new String[100];
                String[] dataValues = new String[100];
                
                /*List<String> dataKeys = new ArrayList<String>();
                List<String> dataValues = new ArrayList<String>();
                dataKeys.add("Hello Zach");
                dataKeys.add("Nice day,eh?");
                
                System.out.println(dataKeys.get(0));
                
                for(int i=0;i<dataKeys.size();i++)
                {
                    //dataKeys.get(i);
                }
                for(String xString:dataKeys)
                {
                    //xString
                }
                
                
                int count = 0;
                
                Iterator keys = jsonObj.keys();
            
                while(keys.hasNext()) {
                    
                    String currentDynamicKey = keys.next().toString();
                    
                    Object xVal = jsonObj.get(currentDynamicKey);
                    
                    if(xVal == null)
                        System.out.println("JSON obj is null for key " + currentDynamicKey);
                    else
                    {
                        try
                        {
                            System.out.println(xVal.toString());
                        }
                        catch(Exception e)
                        {
                            System.out.println("Something wrong with that thing");
                        }
                    }
                    
 
                    
                    
                    /*
                    String currentDynamicValue = jsonObj.getJSONObject(currentDynamicKey).toString();
                    
                    dataKeys[count] = currentDynamicKey;
                    dataValues[count] = currentDynamicValue;
                    
                    System.out.println(dataValues[count] + "\n\n");
                    
                    count++;
                    
                }
                //for(jsonObj.)
                for(int i=0;i<jsonObj.length();i++)
                {
                }
                
                /*System.out.println("If it made it here, thats good!!\n\n");
                System.out.println(dataValues[0] + "\n\n");
                
                
                String latexString = "";
                
                String header = "%This is a test file to determine the layout of the Owl Datasheet\n"
                        + "\\documentclass{article}\n"
                        + "\\usepackage{ccaption} %Formatting table titles\n"
                        + "\\usepackage[margin=1in]{geometry} %Setting document margins\n"
                        + "\\usepackage{graphicx} %Using images\n"
                        + "\\usepackage{array} %Formatting table size and behavior\n"
                        + "\\begin{document}\n"
                        + "\\renewcommand{\\topfraction}{0.85} %Helps with keeping whitespace to a minimum\n"
                        + "\\renewcommand{\\textfraction}{0.1}\n"
                        + "\\renewcommand{\\floatpagefraction}{0.85}\n";
                
                String tableSetup = "\\begin{table}[h]\n"
                        + "\\setlength{\\belowcaptionskip}{4pt}\n"
                        + "\\setlength{\\extrarowheight}{8pt}\n"
                        + "\\legend{\\LARGE ";
                
                String tableStart = "\\begin{tabular}{m{1.2in}m{4.98in}}\n";
                
                String setup = "\\large \\textbf{";
                
                String tableEnd = "\\end{tabular}\n"
                        + "\\end{table}\n";
                
                for(int i = 0; i < count; i++)
                {
                    if(i == 0)
                        latexString += header;
                    
                    if(dataKeys[i].contains("title"))
                    {
                        if(i != 0)
                        {
                            latexString += "\b\b\b";
                            latexString += "\n" + tableEnd;
                        }
                        
                        latexString += tableSetup;
                        latexString += dataValues[i] + "}\n";
                        latexString += tableStart;
                    }
                    else
                    {
                        latexString += setup + dataKeys[i] + "} & " + dataValues[i] + "\\\\" + "\n";
                    }
                    
                }
                
                latexString += "\b\b\b";
                latexString += "\n" + tableEnd;
                latexString += "\\end{document}";
                
                System.out.println("STRING BELOW!!:\n\n");
                System.out.println(latexString);
            
            }*/
            
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
