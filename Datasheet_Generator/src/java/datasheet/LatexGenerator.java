/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datasheet;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Zach
 */
public class LatexGenerator {
    
    public static void main() throws IOException{
        
        String[] parsedString = new String[0];
        appendLatex(parsedString);
        
        String editedString = null;
        editLatex(editedString);
        
        
        
    }
    
    public static void appendLatex(String[] partInfoStrArr) throws IOException{
    
        Path p1 = Paths.get("/Users/Zach/Documents/Owl/Test/Test.tex");
        
        long num = System.currentTimeMillis();
                
        Path p2 = Paths.get("/Users/Zach/Documents/Owl/Test/" + num + ".tex");
        Charset charset = StandardCharsets.UTF_8;

        try{
        String content = new String(Files.readAllBytes(p1), charset);
        
        content = content.replace("BBa\\_",partInfoStrArr[0]);
        content = content.replace("{Summary}\t\t&","{Summary}\t\t&" + " " + partInfoStrArr[1]);
        content = content.replace("{Part Type}\t\t&","{Part Type}\t\t&" + " " + partInfoStrArr[2]);
        content = content.replace("{Sequence}\t\t&","{Sequence}\t\t&" + " " + partInfoStrArr[5]);
        content = content.replace("{Author(s)}\t\t\t\t&","{Author(s)}\t\t\t\t&" + " " + partInfoStrArr[4]);
        content = content.replace("{Date}\t\t\t\t\t&","{Date}\t\t\t\t\t&" + " " + partInfoStrArr[3]);
        content = content.replace("_","\\_");
        
        Files.write(p2, content.getBytes(charset));
        }catch (IOException e) {
            System.err.println(e);
        }

    }
    
    public static void editLatex(String editedString) throws IOException{
        String doc = "\\documentclass{article}\n\\usepackage{ccaption}\n\\usepackage[margin=1in]{geometry}\n" +
                "\\usepackage{graphicx}\n\\usepackage{array}\n\\begin{document}\n" +
                "\\renewcommand{\\topfraction}{0.85}\n\\renewcommand{\\textfraction}{0.1}\n" +
                "\\renewcommand{\\floatpagefraction}{0.85}\n\n";
        
        doc = doc + "blah";
        
        Path p1 = Paths.get("/Users/Zach/Documents/Owl/Test/Test.tex");
        
        long num = System.currentTimeMillis();
                
        Path p2 = Paths.get("/Users/Zach/Documents/Owl/Test/" + num + ".tex");
        Charset charset = StandardCharsets.UTF_8;

        try{
        String content = new String(Files.readAllBytes(p1), charset);
        
        content = content.replace("BBa\\_",editedString.substring(9,14));
//        content = content.replace("{Summary}\t\t&","{Summary}\t\t&" + " " + partInfoStrArr[1]);
//        content = content.replace("{Part Type}\t\t&","{Part Type}\t\t&" + " " + partInfoStrArr[2]);
//        content = content.replace("{Sequence}\t\t&","{Sequence}\t\t&" + " " + partInfoStrArr[5]);
//        content = content.replace("{Author(s)}\t\t\t\t&","{Author(s)}\t\t\t\t&" + " " + partInfoStrArr[4]);
//        content = content.replace("{Date}\t\t\t\t\t&","{Date}\t\t\t\t\t&" + " " + partInfoStrArr[3]);
        content = content.replace("_","\\_");
        
        Files.write(p2, content.getBytes(charset));
        }catch (IOException e) {
            System.err.println(e);
        }

    }
    
}
