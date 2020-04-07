+++
title = "Project Architecture"
description = ""
weight = 3
+++

This guide describes the high-level design of the server and client architecture.
<!--more-->

## Client Architecture

{{< figure src="/images/client-archtechture-new.png" alt="Client Architecture Diagram" title="Client Architecture Diagram" >}}

### Client Details

The client core structure is based on Ext.js which provides UI components and utilities. This significantly reduces third-party dependencies which in turn reduces maintenance, shortens the learning curve, and improves quality and consistency.

In addition, there are application-specific overrides and high-level components created to facilitate reuse.
The application is composed by stripes layouts with a top-level page and fragment tool pages.

## Server Architecture

{{< figure src="/images/serverarch.png" alt="Server Architecture Diagram" title="Server Architecture Diagram" >}}

### Server Details

Component definitions are as follows:

- **Security** - Authentication and authorization is delegated to OpenAM. This is configured through a custom realm using the Apache Shiro library. All requests are passed through this filter.
- **REST API** - The REST API is the component that handles the data interaction between the clients and provides the interface through which the clients can communicate. The REST API (application programming interface) is broken into two sections: resources and services. Resources handle the CRUD (create, reade, update, and delete) operations on the data. Services handle operations across data sets. This provides a clean and clear API for integrators.
- **API Docs** - The API docs are generated live based on the currently running code. This keeps the documents always current and reduces maintenance. Other system-related call-backs (e.g., retrieving binary resources, login handling, etc.) are handled through the Stripes framework.
- **Business Layer**  - Handles all rules applied to the data as well transaction support.
- **Managers** - The role of the manager class is to handle the interaction with a resource. This enables clean initialization and shutdown of resources and provides centralized access.
- **Services** - Each service is in charge of handling a specific group of Entity models. Services provide transaction support and business logic handling. All services are accessed through a service proxy class.  The service proxy class provides auto transaction and service interception support.
- **Models**  - The entity models represent the data in the system and provide the bridge from the application to the underlying storage.
- **Import / Export** - Provides a mechanism for loading and transferring data from one system to another.

The server build environment relies on the following platforms/tools:

- **Java** - Core language and platform
- **Maven** - Used for the project structure, building, and dependency management

## Storage Details

{{< figure src="/images/storage.png" alt="Storage Diagram" title="Storage Diagram" >}}

**File System**  - Holds configuration, resources, media, imports, reports and plugins. By default, under `/var/openstorefront` it can be changed by setting the system property `-Dapplication.datadir=<directory>` on the command line.

**Elasticsearch** - Used to perform fuzzy index searches across the entries. It is kept in sync by the application.

**Database** - Holds all entry data and application data.

## Runtime Environment

{{< figure src="/images/deployarch.png" title="Runtime Environment Diagram" alt="Runtime Environment Diagram">}}

### Runtime Details

The runtime environment relies upon the following applications:

- **Proxy Server** - This is an external system that proxies requests to the application server.
- **Tomcat 7** - Tomcat is the web container used to host the storefront application.
- **Java 8** - Is the runtime platform that runs Tomcat.
- **OS/VM** - Is the host machines operating system.
- **OpenAM** - OpenAM runs externally and a policy agent in Tomcat ensures the site is secure.

### Runtime Component Integration Vectors

{{< figure title="Runtime Component Integration Vectors" src="/images/civarch.png" alt="Runtime Component Integration Vectors" >}}

### Component Integration Vectors Details

The component integration vectors (CIV) are shown below.

| Source Component       | Class | Target Component | Notes                                                                            |
|------------------------|-------|------------------|----------------------------------------------------------------------------------|
| openstorefront         | C     | Elasticsearch    |                                                                                  |
| openstorefront         | C     | OpenAM           | OpenAM with their policy agent requires a hard tie to the application and server |
| openstorefront         | B     | Elasticsearch    | JEE Application Server currently configured to deploy on Tomcat                  |
| JEE Application Server | A     | OS/VM            | Currently targeted for CentOS                                                    |

------

The CIVs represent an integration activity involving a source, Component
A, and a target, Component B.

The CIVs, as defined by the DI2E PMO, are as follows:

- **Class A: A-deployed On-B**. Component B is the underlying
    environment (providing resources) for A; B does not actively manage
    A (e.g., OS, VM).

- **Class B: A-contained In-B**. Component "lives in"  B; B manages
    the lifecycle of A, from cradle to grave. (e.g., Widget in OWF; EJB
    in JEE server; OSGi bundle in Karaf; SCA).

- **Class C: A-interfaces With-B**. Component A initiates
    communication with B via API(s). (e.g., JDBC, JMS, REST/SOAP call,
    legacy communications).

- **Class D: A-indirectly Consumes-B**. Component A has a dependency
    o.n data/functionality of B even though it doesn't interface with B.
    (e.g., subscriber/publisher relationship; A integrates with another
    component that offers data from B).

### Ports

The applicable ports are shown below:

| Port (Default) | Description                   | Type                                                     |
|---------------|-------------------------------|----------------------------------------------------------|
| 8080          | Tomcat HTTP                   | Inbound                                                  |
| 8009          | Tomcat AJP                    | Inbound (Open if not using 8080)                          |
| 9200          | Elasticsearch (Accepts JSON)   | Outbound (Does not need to be exposed outside the system) |
| 9300          | Elasticsearch (Accepts Binary) | Outbound (Does not need to be exposed outside the system) |

------

All ports are configurable via configuration for the respected applications. Additional ports maybe be used depending on configuration.
