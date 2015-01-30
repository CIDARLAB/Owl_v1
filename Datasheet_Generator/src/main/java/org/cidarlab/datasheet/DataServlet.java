/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.datasheet;

import static datasheet.LatexGenerator.editLatex;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

/**
 *
 * @author Jenhan Tao <jenhantao@gmail.com>
 */
public class DataServlet extends HttpServlet {

    protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            if (holdingData) {
                out.write(heldData.toString());
                holdingData = false;
            } else {
                String data = request.getParameter("sending");
                System.out.print("\n\nThis is the data: " + data); /////////////////////////
                editLatex(data);
                heldData = new JSONObject(data);
                holdingData = true;
                response.sendRedirect("output.html");
            }

        } catch (Exception e) {
            e.printStackTrace();
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String exceptionAsString = stringWriter.toString().replaceAll("[\r\n\t]+", "<br/>");
            if (out != null) {
                out.write("{\"data\":\"" + exceptionAsString + "\",\"status\":\"bad\"}");
            }
        } finally {
            out.close();
        }
    }

    protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter writer = response.getWriter();
        try {
                ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
                response.setContentType("text/plain");
                String user = getUser(request);
                List<FileItem> items = uploadHandler.parseRequest(request);
                String uploadFilePath = this.getServletContext().getRealPath("/") + "/data/" + user + "/";

                new File(uploadFilePath).mkdirs();
                ArrayList<File> toLoad = new ArrayList();
                for (FileItem item : items) {
                    File file;
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        if (fileName.equals("")) {
                            System.out.println("You forgot to choose a file.");
                        }
                        if (fileName.lastIndexOf("\\") >= 0) {
                            file = new File(uploadFilePath + fileName.substring(fileName.lastIndexOf("\\")));
                        } else {
                            file = new File(uploadFilePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
                        }
                        item.write(file);
                        toLoad.add(file);
                        String path = file.getAbsolutePath();
                        writer.write("{\"result\":\"good\",\"status\":\"good\",\"path\":" + path + "}");

                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String exceptionAsString = stringWriter.toString().replaceAll("[\r\n\t]+", "<br/>");
            if (writer != null) {
                writer.write("{\"data\":\"" + exceptionAsString + "\",\"status\":\"bad\"}");
            }
        } finally {
            writer.close();
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
        processGetRequest(request, response);
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
        processPostRequest(request, response);
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
    private JSONObject heldData;

    private String getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String user = "default";
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("user")) {
                    user = cookies[i].getValue();
                }
            }
        }
        return user;
    }

    private void saveUrl(String urlString, String localFileName) {
        int size = 1024;
        OutputStream outStream = null;
        URLConnection uCon = null;
        InputStream inputStream = null;
        try {
            URL url;
            byte[] buf;
            int ByteRead, ByteWritten = 0;
            url = new URL(urlString);
            File newFile = new File(localFileName);
            newFile.createNewFile();
            FileOutputStream fileOutStream = new FileOutputStream(newFile);
            outStream = new BufferedOutputStream(fileOutStream);
            uCon = url.openConnection();
            inputStream = uCon.getInputStream();
            buf = new byte[size];
            while ((ByteRead = inputStream.read(buf)) != -1) {
                outStream.write(buf, 0, ByteRead);
                ByteWritten += ByteRead;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//   public static void main() throws IOException {
//       
//       appendLatex(heldData);
//       
//   }
    
//    public static void appendLatex(JSONObject heldData) throws IOException {
//        
//        Path p1 = Paths.get("/Users/Zach/Documents/Owl/Test/Test.tex");
//        Path p2 = Paths.get("/Users/Zach/Documents/Owl/Test/Blank.tex");
//        Charset charset = StandardCharsets.UTF_8;
//        System.out.print("\n\nThis is the data: " + heldData);
//        
//        try{
//        String content = new String(Files.readAllBytes(p1), charset);
//        
////        content = content.replace("BBa\\_",partInfoStrArr[0]);
////        content = content.replace("{Summary}\t\t&","{Summary}\t\t&" + " " + partInfoStrArr[1]);
////        content = content.replace("{Part Type}\t\t&","{Part Type}\t\t&" + " " + partInfoStrArr[2]);
////        content = content.replace("{Sequence}\t\t&","{Sequence}\t\t&" + " " + partInfoStrArr[5]);
////        content = content.replace("{Author(s)}\t\t\t\t&","{Author(s)}\t\t\t\t&" + " " + partInfoStrArr[4]);
////        content = content.replace("{Date}\t\t\t\t\t&","{Date}\t\t\t\t\t&" + " " + partInfoStrArr[3]);
////        content = content.replace("_","\\_");
//        
//        Files.write(p2, content.getBytes(charset));
//        }catch (IOException e) {
//            System.err.println(e);
//        }
//
//
//        
//        
//    }
}
