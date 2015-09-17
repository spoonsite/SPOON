package edu.usu.sdl.openstorefront.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OpenstorefrontCommonActivator
		implements BundleActivator
{

	private static final Logger log = Logger.getLogger(OpenstorefrontCommonActivator.class.getName());

	@Override
	public void start(BundleContext context) throws Exception
	{
		log.log(Level.INFO, "Common bundle started.");
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		log.log(Level.INFO, "Common bundle stopped.");
	}

}
