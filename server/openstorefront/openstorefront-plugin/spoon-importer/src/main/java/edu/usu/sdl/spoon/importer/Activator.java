package edu.usu.sdl.spoon.importer;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileType;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator
		implements BundleActivator
{

	@Override
	public void start(BundleContext context) throws Exception
	{

		//Register new parser format
		Service service = ServiceProxyFactory.getServiceProxy();

		if (service.getSystemService().isSystemReady() ||
			service.getSystemService().isLoadingPluginsReady())
		{
			FileFormat spoonComponentFormat = new FileFormat();
			spoonComponentFormat.setCode(ComponentSpoonParser.FORMAT_CODE);
			spoonComponentFormat.setFileType(FileType.COMPONENT);
			spoonComponentFormat.setDescription("SPOON Entries (XML)");
			spoonComponentFormat.setSupportsDataMap(true);
			spoonComponentFormat.setParserClass(ComponentSpoonParser.class.getName());

			service.getImportService().registerFormat(spoonComponentFormat);

			FileFormat spoonAttributFormat = new FileFormat();
			spoonAttributFormat.setCode(AttributeSpoonParser.FORMAT_CODE);
			spoonAttributFormat.setFileType(FileType.ATTRIBUTE);
			spoonAttributFormat.setDescription("SPOON Attributes (XML)");
			spoonAttributFormat.setSupportsDataMap(true);
			spoonAttributFormat.setParserClass(AttributeSpoonParser.class.getName());

			service.getImportService().registerFormat(spoonAttributFormat);
		}

	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		Service service = ServiceProxyFactory.getServiceProxy();

		if (service.getSystemService().isSystemReady() ||
			service.getSystemService().isLoadingPluginsReady())
		{
			//unregister parsers
			service.getImportService().unregisterFormat(ComponentSpoonParser.class.getName());
			service.getImportService().unregisterFormat(AttributeSpoonParser.class.getName());
		}
	}

}
