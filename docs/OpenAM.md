# OpenAM
<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [Versions Used](#versions-used)
- [Installation of OpenAM Java EE Policy Agent into Tomcat 7.0.55](#installation-of-openam-java-ee-policy-agent-into-tomcat-7055)
  - [Reference](#reference)
- [Configuration of OpenAM](#configuration-of-openam)
  - [Creating the Agent Profile](#creating-the-agent-profile)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Versions Used

* [Tomcat 7.0.55 64-bit Windows zip]( http://mirrors.sonic.net/apache/tomcat/tomcat-7/v7.0.55/bin/apache-tomcat-7.0.55-windows-x64.zip)
* [OpenAM 11.0.0.0](https://backstage.forgerock.com/downloads/enterprise/openam/openam11/11.0.0/OpenAM-11.0.0.zip)
  * http://docs.forgerock.org/en/openam/11.0.0/release-notes/index/index.html
* [J2EE Policy Agent 3.3.0 Apache Tomcat 6 and 7](https://backstage.forgerock.com/downloads/enterprise/openam/j2eeagents/stable/3.3.0/Tomcat-v6-7-Agent-3.3.0.zip)
  * http://docs.forgerock.org/en/openam-pa/3.3.0/jee-release-notes/index/index.html
* 64 bit JRE 1.7.0_67-b01

## Installation of OpenAM Java EE Policy Agent into Tomcat 7.0.55



1. Make sure the Agent Profile has already been created in OpenAM
2. Create a `pwd.txt` file at `C:\Temp\pwd.txt` and add your Agent Profile password to it
3. Shutdown the Tomcat server that is going to run your web application
4. Make sure the Tomcat server that is running OpenAM is still running
5. Extract Tomcat-v6-7-Agent-3.3.0.zip to a known directory
6. `CD` into the `j2ee_agents/tomcat_v6_agent/bin` directory
7. Execute `agentadmin --install` to install the agent


### Reference
- http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/jee-install-guide/index/chap-apache-tomcat.html
- http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/jee-install-guide/#chap-apache-tomcat

## Configuration of OpenAM

### Creating the Agent Profile

1. Open up OpenAM in a web browser `http://c00788.usurf.usu.edu:8080/openam`
2. Log into OpenAM using `amadmin`
3. Click on `Access Control > Top Level Realm > Agents > J2EE`
4. Create a new J2EE agent by click on the `New...` button under Agent
5. Craete the agent with the following parameters
  - Name: `myagent`
  - Password: `password`
  - Configuration: `Centralized`
  - Server URL: `http://c00788.usurf.usu.edu:8080/openam`
  - Agent URL: `http://c00788.usurf.usu.edu:8081/agentsample`
