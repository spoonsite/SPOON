package edu.usu.sdl.spoon.aerospace.importor;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileType;
import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    private static final Logger log = Logger.getLogger(Activator.class.getName());

   public void start(BundleContext context) throws Exception {
       log.info("Starting Aerospace Import plugin");
       //Register new parser format
       Service service = ServiceProxyFactory.getServiceProxy();

       if (service.getSystemService().isSystemReady()
               || service.getSystemService().isLoadingPluginsReady()) {
           FileFormat aerospaceFormat = new FileFormat();
           aerospaceFormat.setCode(AerospaceParser.FORMAT_CODE);
           aerospaceFormat.setFileType(FileType.COMPONENT);
           aerospaceFormat.setDescription("Aerospace Import (ZIP)");
           aerospaceFormat.setSupportsDataMap(true);
           aerospaceFormat.setParserClass(AerospaceParser.class.getName());

           service.getImportService().registerFormat(aerospaceFormat, AerospaceParser.class);
       }
   }

   public void stop(BundleContext context) throws Exception {
       log.info("Stopping Aerospace Import plugin");
       Service service = ServiceProxyFactory.getServiceProxy();

       if (service.getSystemService().isSystemReady()
               || service.getSystemService().isLoadingPluginsReady()) {
           //unregister parsers
           service.getImportService().unregisterFormat(AerospaceParser.class.getName());
       }
   }

}
