/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.er2.parser;

/**
 *
 * @author rnethercott
 */
import edu.usu.sdl.er2.model.AcceptableValueList;
import org.simpleframework.xml.*;
import java.io.File;
import java.io.FileReader;
import org.simpleframework.xml.core.Persister;


public class App {

    public static void main(String[] args) {
        System.out.print("Starting XML Conversion\n");

        if (args.length == 0) {
            System.out.print("YOU MUST ENTER AN PATH:  java -jar XMLConverter <path to read xml file>\n");
            System.exit(1);
        } else {
            try {
                File xmlReadFile = new File(args[0]);
                FileReader fr = new FileReader(xmlReadFile);
                Serializer serializer = new Persister();
                AcceptableValueList avl= serializer.read(AcceptableValueList.class,xmlReadFile);
                System.out.print(avl.toString());
                
                
                
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("Argument was not a file.\n");
                System.exit(1);
            }
        }
    }
}
