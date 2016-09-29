/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.describe.parser;

import edu.usu.sdl.describe.model.TrustedDataCollection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
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

	private static final Logger LOG = Logger.getLogger(DescribeParser.class.getName());

	private DescribeParser()
	{
	}

	public static TrustedDataCollection parse(File xmlfile) throws FileNotFoundException
	{
		return DescribeParser.parse(new FileInputStream(xmlfile));
	}

	public static TrustedDataCollection parse(InputStream in)
	{
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);
		TrustedDataCollection data = null;
		try {
			data = serializer.read(TrustedDataCollection.class, in);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
					LOG.log(Level.WARNING, "Unable to close describe file.", ioe);
				}
			}
		}
		return data;
	}

	public static void write(OutputStream out, TrustedDataCollection dataCollection) throws Exception
	{
		Strategy strategy = new AnnotationStrategy();
		Serializer serializer = new Persister(strategy);

		try {
			serializer.write(dataCollection, out);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
					LOG.log(Level.WARNING, "Unable to close describe file.", ioe);
				}
			}
		}
	}

}
