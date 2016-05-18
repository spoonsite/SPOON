/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.test;

import edu.usu.sdl.describe.model.TrustedDataCollection;
import edu.usu.sdl.describe.parser.DescribeParser;
import java.io.File;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ParseUseCase
{
	@Test
	public void testParser() throws Exception
	{
		TrustedDataCollection tdc  = DescribeParser.parse(new File("/temp/describe-example-simple.xml"));
		System.out.println("version = " + tdc.getVersion());
	}
	
}
