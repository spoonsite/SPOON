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
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

public class App
{

	private static final Logger LOG = Logger.getLogger(App.class.getName());

	private App()
	{
	}

	public static void main(String[] args)
	{
		LOG.log(Level.INFO, "Starting XML Conversion\n");

		if (args.length == 0) {
			LOG.log(Level.INFO, "YOU MUST ENTER AN PATH:  java -jar XMLConverter <path to read xml file>\n");
			System.exit(1);
		} else {
			File xmlReadFile = new File(args[0]);

			try {

				Serializer serializer = new Persister();
				AcceptableValueList avl = serializer.read(AcceptableValueList.class, xmlReadFile);
				LOG.log(Level.INFO, avl.toString());

			} catch (Exception e) {
				LOG.log(Level.INFO, "Argument was not a file.\n", e);
				System.exit(1);
			}
		}
	}
}
