package edu.usu.sdl.openstorefront.core.model.internal;

import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class CoreAPIActivator
		implements BundleActivator
{

	private static final Logger log = Logger.getLogger(CoreAPIActivator.class.getName());

	@Override
	public void start(BundleContext context) throws Exception
	{
		log.log(Level.INFO, "Loading API Module");
		ServiceProxyFactory.setContext(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		log.log(Level.INFO, "Unloading Load API Module");
		ServiceProxyFactory.setContext(null);
	}

}
