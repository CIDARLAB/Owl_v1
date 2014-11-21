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
import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 * @author Zach
 */
public class LatexCreator {
    
    public static void main(){
        
    }
    
    public static String makeLatex(Map<String,String> map){
        
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
        
        int i = 0;
        
        for(Map.Entry<String, String> entry : map.entrySet()){
//            System.out.println("Printing an entry");
//            System.out.println(entry.getKey() + ":" +entry.getValue());
            
            if(i == 0)
                latexString += header;
                    
            if(entry.getKey().contains("title"))
                {
                if(i != 0)
                {
                    latexString = latexString.substring(0, latexString.length() - 3);
                    latexString += "\n" + tableEnd;
                }
                       
                latexString += tableSetup;
                latexString += entry.getValue() + "}\n";
                latexString += tableStart;
            }
            else
            {
                latexString += setup + entry.getKey() + "} & " + entry.getValue() + "\\\\" + "\n";
            }
            
            i++;
        }
                
        latexString = latexString.substring(0, latexString.length() - 3);
        latexString += "\n" + tableEnd;
        latexString += "\\end{document}";
            
        return latexString;
    }
    
    
    public static String writeLatex(String latexString){
        
        long num = System.currentTimeMillis();
                
        Path p = Paths.get("/Users/Zach/Documents/Owl/Test/" + num + ".tex");
        Charset charset = StandardCharsets.UTF_8;
        
        try{        
        Files.write(p, latexString.getBytes(charset));
        }catch (IOException e) {
            System.err.println(e);
        }
        
        return p.toString();
        
    }    
}
