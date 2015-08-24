package edu.usu.sdl.example;

import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator
		implements BundleActivator
{

	private static final Logger log = Logger.getLogger(Activator.class.getName());

	@Override
	public void start(BundleContext context) throws Exception
	{
		log.info("Starting plugin example");
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		log.info("Stopping plugin example");
	}

}
