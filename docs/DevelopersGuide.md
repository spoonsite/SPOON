
This guide is targeted at external developers who want to extend the application.

# Adding Custom Parser (Plugin)
----

A custom parser may be need for handling complex formats that can't be support via data mapping. 
In some cases, both a custom parser and data mapping may be required.

1 - Create an OSGi bundle (you use a maven project)

2 - Add a dependency to openstorefront-core-api

3 - In the Activator.java, register your new parser 
   (More than one parser can be added to a plugin)
>In start bundle:
>
    FileFormat customFormat = new FileFormat();
	customFormat.setCode("CUSTOMFORMATCODE");
	customFormat.setFileType(FileType.COMPONENT);
	customFormat.setDescription("Custom format");
	customFormat.setSupportsDataMap(true);  //Set to true to allow data mapping
	customFormat.setParserClass(CustomParser.class.getName());

>	service.getImportService().registerFormat(customFormat, CustomParser.class);

>   In stop bundle:

>   service.getImportService().unregisterFormat(customFormat.class.getName());

4 - Upload build JAR using the Admin->Application Management->System->Plugins
or copy jar to /var/openstorefront/perm/plugins and the application will auto-deploy

**Note:**  Only Libraries and API the application expose are available. (CORE-API, CORE-COMMON)
All other third-party libraries must be included with your JAR. 

## Parser Workflow

(Default flow but it can be overriden)
1 - Check Format (On Web Upload)

2 - Process Data 

> a) Get the parser Reader (CSV, Text, XML...etc)

> b) Read Record

> c) Validate Record

> d) Add record to storage batch (flushes batch if full)

> e) Loop through remaing records

> f) Flush any records not stored

> g) Finish Processing (Override for special handling; Eg Media and Relationships)


## Parser Class

1 - Extend Either the Component or Attribute Base Parser

2 - Implement Check format and parse record

Override other method such as getReader as needed.
The parse record method receive a model according to the reader.
Then it should return a record such as ComponentAll to be stored.
If the method return null it will skip the record.

**Note:** The developer has access to the filehistory record and the service proxy.

**See:** spoon importer plugin as a example.

