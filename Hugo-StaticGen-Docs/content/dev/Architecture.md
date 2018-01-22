
+++
title = "Architecture"
description = ""
weight = 2
+++

# 1.  Client Architecture

## 1.1 Client Architecture Diagram

![clientarch](/openstorefront/images/client-archtechture-new.png)

Figure 1. Client Architecture Diagram

## 1.2 Client Details

The client core structure is based on Ext.js which provides UI components and utilities. This reduces third-part dependencies significantly which in turn reduce maintenance, learning curve and improves quality and consistency.

Added to that is application specific overrides and high-level components created to facilitate re-use.
The application is composed by stripes layouts with a top-level page and fragment tool pages.

# 2.  Server Architecture

## 2.1 Server Architecture Diagram

![serverarch](/openstorefront/images/serverarch.png)

Figure 2. Server Architecture Diagram

## 2.2 Server Details

Component definitions are as shown below:

  -  **Security**        - Authentication and authorization is delegated to OpenAm. This is configured through a custom realm using the Apache Shiro library. All request are passed through this filter.
  -  **REST API**       - The REST API is the component that handles the data interaction between the clients and provides the interface with which the clients can communicate. The REST API is broken into two sections: resources and services. Resources handle the CRUD operations on the data. Service handle operation across data sets. This provides a clean and clear API for integrators.
  -  **API Docs**       - The API docs are generated live based on the currently running code. This keeps the documents always current and reduces maintenance. Other system related call backs (e.g., retrieving binary resources, login handling, etc.) are handled through the Stripes framework.
  -  **Business Layer**  - Handles all rules applied to the data as well transaction support.
  
>-  **Managers**   - The role of the manager class is to handle the interaction with a resource. This allow for clean initialization and shutdown of resources and provides centralized access.
  -   **Services**    - Each service is in charge of handling a specific group of Entity models. Services provide transaction support and business logic handling. All services are accessed through a service proxy class.  The service proxy class provides auto transaction and service interception support.
  -   **Models**  - The entity models represent the data in the system and provide the bridge from the application to the underlying storage.  
  -   **Import / Export** -Provides a mechanism for loading and transferring data from one system to another.


The server build environment relies on the following platforms/tools:

  -  **Java**     -            Core language and platform
  -  **Maven**   -            Used for the project structure, building and dependency management

## 2.3 Storage Details

![storage](/openstorefront/images/storage.png)

**File System**  - Holds configuration, resources, media, imports, reports and plugins.  By default under: /var/openstorefront it can be changed by setting the system property -Dapplication.datadir=<directory> on the command line. 

**Solr or Elasticsearch** - Use to do fuzzy index searches across the entries.  It's kept in sync by the application.

**Database** - Holds all entry data and application data. 


# 3. Runtime Environment

## 3.1 Runtime Environment Diagram

![deployarch](/openstorefront/images/deployarch.png)

Figure 3 . Runtime Environment diagram

## 3.2 Runtime Details

The runtime environment relies upon the following applications:

-  **Proxy Server**   -   This is an external system that proxies requests to the application server.
-  **Tomcat 7**  -    Tomcat is the web container used to host the storefront application.
-  **Java 8**  -            It the runtime platform which runs Tomcat
-  **OS/VM**  -             Is the host machines operating system
-  **Solr**          -    Enterprise search appliance that runs externally
-  **OpenAM**    -        OpenAM runs externally and a policy agent in Tomcat make sure the site is secure.

## 3.3 Runtime Component Integration Vectors

![component vectors](/openstorefront/images/civarch.png)

Figure 4. Runtime Component Integration Vectors

## 3.4 Component Integration Vectors Details

The component integration vectors (CIV) are show below.

 **Source Component**:  openstorefront           
 **Class**:  C  
 **Target Component**:      Solr or Elasticsearch                 
**Notes**

 **Source Component**:  openstorefront           
 **Class**:  C  
 **Target Component**:      OpenAM        
**Notes**: OpenAM with their policy agent; requires a hard tie to the application and the application server

 **Source Component**:  openstorefront           
 **Class**:  B  
 **Target Component**:      Solr or Elasticsearch                 
**Notes**: JEE Application Server   Currently configured to deploy on Tomcat

 **Source Component**:    Orient DB           
 **Class**:  B  
 **Target Component**:     openstorefront                
**Notes**: Embedded

 **Source Component**:   JEE Application Server           
 **Class**:  A  
 **Target Component**:      OS/VM                 
**Notes**: Currently targeted for CentOS

------

The CIVs represent an integration activity involving a source, Component
A, and a target, Component B.

The CIVs, as defined by the DI2E PMO, are as follows:

-   **Class A: A-deployed On-B**. Component B is the underlying
    environment (providing resources) for A; B does not actively manage
    A (e.g. OS, VM).

-   **Class B: A-contained In-B**. Component "lives in"  B; B manages
    the lifecycle of A, from cradle to grave. (e.g. Widget in OWF; EJB
    in JEE server; OSGi bundle in Karaf; SCA).

-   **Class C: A-interfaces With-B**. Component A initiates
    communication with B via API(s). (e.g., JDBC, JMS, REST/SOAP call,
    legacy communications)

-   **Class D: A-indirectly Consumes-B**. Component A has a dependency
    o.n data/functionality of B even though it doesn't interface with B.
    (e.g. subscriber/publisher relationship; A integrates with another
    component that offers data from B).

## 3.5 Ports

The applicable ports are shown below:

**Port (Defaults):**  8080
**Description:** Tomcat HTTP
**Type:** Inbound

**Port (Defaults):**  8009
**Description:** Tomcat AJP
**Type:** Inbound  (Open if not using 8080)

**Port (Defaults):**  2424
**Description:** OrientDB
**Type:** Internal  (Shouldn't be exposed externally)

**Port (Defaults):**  2480
**Description:** OrientDB
**Type:** Internal  (Shouldn't be exposed externally)

**Port (Defaults):**  8983
**Description:** Solr (If used)
**Type:** Outbound (Used internally doesn't need to be exposed outside the system)

**Port (Defaults):**  9300
**Description:** Elasticsearch (If used; Binary and the one used by the application)
**Type:** Outbound (Used internally does not need to be exposed outside the system)

**Port (Defaults):**  9200
**Description:** Elasticsearch (If used; JSON)
**Type:** Outbound (Used internally does not need to be exposed outside the system)

**Port (Defaults):**  8080
**Description:** OpenAM running on Tomcat; Setups on this vary so this just represents one case
**Type:** Outbound  (External application)

----

  All ports are configurable via configuration for the respected applications. Additional ports maybe be used depending on configuration.

