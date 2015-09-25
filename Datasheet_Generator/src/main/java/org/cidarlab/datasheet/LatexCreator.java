/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cidarlab.datasheet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author zach_chapasko
 */
public class LatexCreator {
    
    public static void main(){
    }
    
    public static String getFilepath(String classCaller)
    {        
        String filepath;
        filepath = LatexCreator.class.getClassLoader().getResource(".").getPath();
        //System.out.println("\nFILEPATH: " + filepath + "\n");
        //System.out.println("\nOPERATING SYSTEM: " + System.getProperty("os.name").substring(0, 7) + "\n");

        if(classCaller.equals("Owl")){
            filepath = filepath.substring(0,filepath.indexOf("target/"));
            filepath += "src/main/webapp/tmp/";
//            if(System.getProperty("os.name").substring(0, 7).equals("Windows")){
//                filepath = "/c" + filepath.substring(3);
//            }
        } else if (classCaller.equals("ParserServlet")){
            filepath = filepath.substring(0,filepath.indexOf("WEB-INF/"));
            filepath += "tmp/";   
        }
        
        //System.out.println("\nFILEPATH: " + filepath + "\n");

        return filepath;
    }
    
    public static String makeLatex(ArrayList<String> imageFilenames, Map<String,String> map, String classCaller){
        
        String latexString = "";
                
        String header = "%This is a test file to determine the layout of the Owl Datasheet\n"
            + "\\documentclass{article}\n"
            + "\\pagestyle{myheadings}\n"
            + "\\markright{" + map.get("title1").trim().replaceAll("_", "\\\\_") + "}\n"
            + "\\usepackage[xcolor]{mdframed} %Top header has banner!\n"
            + "\\usepackage{hyphenat} %Column titles are not to have hyphenation\n"
            + "\\usepackage{seqsplit} %Manages long DNA sequence line breaks\n"
            + "\\usepackage{ccaption} %Formatting table titles\n"
            + "\\usepackage[margin=1in]{geometry} %Setting document margins\n"
            + "\\usepackage{graphicx} %Using images\n"
            + "\\usepackage{array} %Formatting table size and behavior\n"
            + "\\begin{document}\n"
            + "\\renewcommand{\\topfraction}{0.99} %Helps with keeping whitespace to a minimum\n"
            + "\\renewcommand{\\textfraction}{0.99}\n"
            + "\\renewcommand{\\floatpagefraction}{0.99}\n";
                
        String tableSetup = "\\begin{table}[htbp]\n"
            + "\\setlength{\\belowcaptionskip}{4pt}\n"
            + "\\setlength{\\extrarowheight}{8pt}\n";
            //+ "\\legend{\\LARGE ";
             
        String tableStart = "\\begin{tabular}{m{1.2in}m{4.98in}}\n";
                
        String setup = "\\large \\textbf{\\nohyphens{";
                
        String tableEnd = "\\end{tabular}\n"
            + "\\end{table}\n";
        
        latexString += header;
        
        String[] nameArray;
        String name;
        
        for(Map.Entry<String, String> entry : map.entrySet())
        {
            if(entry.getKey().contains("<imglink>"))
            {
                latexString += "\\immediate\\write18{curl -O " + entry.getValue().trim() + "}\n";
            }
        }
        
        boolean first = true;
        int uploadCount = 0;
        
        for(Map.Entry<String, String> entry : map.entrySet()){
            //System.out.println("\nPrinting an entry");
            //System.out.println(entry.getKey() + ":" + entry.getValue() + "\n");                              
                    
            if(entry.getKey().contains("title"))
            {
                if(!first)
                {
                    latexString = latexString.substring(0, latexString.length() - 3);
                    latexString += "\n" + tableEnd;
                    latexString += tableSetup
                                + "\\begin{mdframed}[backgroundcolor=gray!32,topline=false,rightline=false,leftline=false,bottomline=false] \\legend{\\LARGE ";
                    latexString += entry.getValue().replaceAll("_", "\\\\_") + "}\\end{mdframed}\n";
                    latexString += tableStart;
                }
                else{
                    latexString += tableSetup;
                    //latexString = latexString.replace("LARGE", "Huge");
                    //latexString += "\\underline{" + entry.getValue().replaceAll("_", "\\\\_") + "}} \\hfill \\break \n";
                    latexString += "\\begin{mdframed}[backgroundcolor=gray!32,topline=false,rightline=false,leftline=false,bottomline=false] \\legend{\\Huge \\underline{"
                            + entry.getValue().replaceAll("_", "\\\\_")
                            + "}} \\end{mdframed} \\hfill \\break\n";
                    latexString += tableStart;
                }
                       
                
            }
            else if(entry.getKey().contains("<imglink>"))
            {
                latexString += setup + entry.getKey().substring(9).replaceAll("_", "\\\\_") + "}} & ";
                
                nameArray = entry.getValue().trim().split("/");
                name = nameArray[nameArray.length - 1];
                latexString += "\\hfill \\break \\includegraphics[width=10cm,height=10cm,keepaspectratio]{" + name + "} \\\\ \n";
            }
            else if(entry.getKey().contains("<imgupload>"))
            {
                latexString += setup + entry.getKey().substring(11).replaceAll("_", "\\\\_") + "}} & ";
                
                name = imageFilenames.get(uploadCount);
                latexString += "\\hfill \\break \\includegraphics[width=10cm,height=10cm,keepaspectratio]{" + getFilepath(classCaller) + name + "} \\\\ \n";
                uploadCount++;
            }
            else
            {
                if(!"".equals(entry.getValue())){
                    latexString += setup + entry.getKey().replaceAll("_", "\\\\_") + "}} & ";
                    if(entry.getValue().trim().contains(" ")){
                        latexString += entry.getValue().trim().replaceAll("_", "\\\\_") + "\\\\" + "\n";
                    }
                    else {
                        latexString += "\\seqsplit{" + entry.getValue().trim().replaceAll("_","\\\\_") + "}\\\\" + "\n";
                    }
                    //+ entry.getValue().replaceAll("_", "\\\\_") + "\\\\" + "\n";
                }
            }
            
            first = false;
        }
                
        latexString = latexString.substring(0, latexString.length() - 3);
        latexString += "\n" + tableEnd;
        latexString += "\\end{document}";
        latexString = latexString.replaceAll("\\{4.98in\\n", "{4.98in}}\n");
        
        String[] line = latexString.split("\\n");
        
        List<Integer> linesToRemove = new ArrayList<>();
        
        for(int i = 0; i < line.length; i++)
        {
            if(line[i].contains("\\begin{table}[htbp]"))
            {
                if(line[i + 5].contains("\\end{tabular}"))
                {
                    linesToRemove.add(i);
                    linesToRemove.add(i+1);
                    linesToRemove.add(i+2);
                    linesToRemove.add(i+3);
                    linesToRemove.add(i+4);
                    linesToRemove.add(i+5);
                    linesToRemove.add(i+6);
                }
            }
        }
        
        StringBuilder newLatexString = new StringBuilder(8192);
        
        for(int i = 0; i < line.length; i++)
        {
            if(!linesToRemove.contains(i))
            {
                newLatexString.append(line[i]).append("\n");
            }
        }
        
        latexString = newLatexString.toString();
            
        return latexString;
    }
    
    
    public static List<String> writeLatex(String uniqueID, String latexString, String classCaller){
                       
        Path p = Paths.get(getFilepath(classCaller) + uniqueID + ".tex");
//        int pathCount = p.getNameCount();
//        p = p.subpath(1, pathCount - 1);
        Charset charset = StandardCharsets.UTF_8;
        
        try{        
        Files.write(p, latexString.getBytes(charset));
        }catch (IOException e) {
            System.err.println(e);
        }
        
        List<String> result = new ArrayList<>();
        result.add(p.toString());
        result.add(uniqueID + ".pdf");
        return result;
    }    
}
