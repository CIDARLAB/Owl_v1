/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datasheet;

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
 * @author Zach
 */
public class LatexCreator {
    
    public static void main(){
        
    }
    
    public static String makeLatex(ArrayList<String> imageFilenames, Map<String,String> map){
        
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
//            System.out.println("Printing an entry");
//            System.out.println(entry.getKey() + ":" +entry.getValue());                              
                    
            if(entry.getKey().contains("title"))
            {
                if(!first)
                {
                    latexString = latexString.substring(0, latexString.length() - 3);
                    latexString += "\n" + tableEnd;
                }
                       
                latexString += tableSetup;
                latexString += entry.getValue() + "}\n";
                latexString += tableStart;
            }
            else if(entry.getKey().contains("<imglink>"))
            {
                latexString += setup + entry.getKey().substring(9) + "} & ";
                
                nameArray = entry.getValue().trim().split("/");
                name = nameArray[nameArray.length - 1];
                latexString += "\\includegraphics[width=2cm,height=2cm,keepaspectratio]{" + name + "} \\\\ \n";
            }
            else if(entry.getKey().contains("<imgupload>"))
            {
                latexString += setup + entry.getKey().substring(11) + "} & ";
                
                name = imageFilenames.get(uploadCount);
                latexString += "\\includegraphics[width=2cm,height=2cm,keepaspectratio]{/Users/Zach/Documents/Owl/Test/" + name + "} \\\\ \n";
                uploadCount++;
            }
            else
            {
                latexString += setup + entry.getKey() + "} & " + entry.getValue() + "\\\\" + "\n";
            }
            
            first = false;
        }
                
        latexString = latexString.substring(0, latexString.length() - 3);
        latexString += "\n" + tableEnd;
        latexString += "\\end{document}";
            
        return latexString;
    }
    
    
    public static List<String> writeLatex(String uniqueID, String latexString){
                       
        Path p = Paths.get("/Users/Zach/Documents/Owl/Test/" + uniqueID + ".tex");
        Charset charset = StandardCharsets.UTF_8;
        
        try{        
        Files.write(p, latexString.getBytes(charset));
        }catch (IOException e) {
            System.err.println(e);
        }
        
        List<String> result = new ArrayList<String>();
        result.add(p.toString());
        result.add(uniqueID + ".pdf");
        return result;
        
    }    
}
