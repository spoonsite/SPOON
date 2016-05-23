/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.TrustedDataCollection;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

/**
 *
 * @author dshurtleff
 */
public class DescribeParser
{
	
	public static TrustedDataCollection  parse(File xmlfile) throws Exception
	{
		return DescribeParser.parse(new FileInputStream(xmlfile));
	}
	
	public static TrustedDataCollection  parse(InputStream in) throws Exception
	{
		Strategy strategy = new AnnotationStrategy();
                Serializer serializer = new Persister(strategy);
                TrustedDataCollection data = serializer.read(TrustedDataCollection.class, in);
		return data;
	}
	
}
