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
