/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.spoon.aerospace.importer;

import edu.usu.sdl.spoon.aerospace.importor.model.Services;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author dshurtleff
 */
public class ParserUseCase
{

	@Test
	public void parseTest()
	{
		Serializer serializer = new Persister();
		try {
			Services services = serializer.read(Services.class, new File("c:/test/aerospace/products_091118.xml"));
			services.getProducts().forEach(p -> {
				System.out.println(p.getLongName());
			});

		} catch (Exception ex) {
			Logger.getLogger(ParserUseCase.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
