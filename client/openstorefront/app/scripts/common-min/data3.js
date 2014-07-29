/* 
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


// Code here will be linted with JSHint.
/* jshint ignore:start */
// Code here will be linted with ignored by JSHint.
MOCKDATA2.svcv4 = [
{
  "TagValue_UID":'0359',
  "TagValue_Number":"3.5.1.1",
  "TagValue_Service Name":"Dissemination Authorization",
  "TagNotes_Service Definition":"Dissemination Authorization supports the process to submit, track, and authorize requests to release information.",
  "TagNotes_Service Description":"The Dissemination Authorization service receives an intelligence report and information the entity a report will be disseminated to, and uses classification markings, security metadata, information about the entity, and possibly even man-in-the-loop, to determine if the report is releasable to the specified entity.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"7.1.51 Broadcast Information\n7.1.95 Disseminate Sensor Products\n8.7.22 Disseminate Data\n14.1.11 Disseminate Intelligence Products",
  "TagValue_JARM/ESL Alignment":"6.02.03 Content Delivery Services",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0336',
  "TagValue_Number":"3.4.8.4",
  "TagValue_Service Name":"Weather Effect  Planning",
  "TagNotes_Service Definition":"Weather Effect Planning supports the planning and analysis of weather on operations and collections.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"Is this the actual weather service, or is it a planning tool that utilizes the weather services?"
},
{
  "TagValue_UID":'0350',
  "TagValue_Number":"3.4.6.2",
  "TagValue_Service Name":"Structured Analytic Techniques",
  "TagNotes_Service Definition":"Structured Analytic Techniques services provide the mechanism by which internal thought processes are externalized in a systematic and transparent manner.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0079',
  "TagValue_Number":"3.4.3.2",
  "TagValue_Service Name":"Identity Disambiguation",
  "TagNotes_Service Definition":"Identity Disambiguation determines if one entity (usually a person) is the same as another entity by analyzing descriptive information on the two entities.",
  "TagNotes_Service Description":"Identity Disambiguation typically takes people names (but sometimes place names or institution names) and determines if they refer to the same entity.  This is done in one (or both) of two ways: (1) names can be compared with each other via similarity metrics or association dictionaries or (2) by comparing associated metadata to determine degrees of similarity.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"Dependency on data analytics"
},
{
  "TagValue_UID":'0078',
  "TagValue_Number":"3.4.3.1",
  "TagValue_Service Name":"Entity Activity Patterns",
  "TagNotes_Service Definition":"The Entity Activity Patterns service determines intelligence relationships among specific objects such as people, places, items, and events.",
  "TagNotes_Service Description":"Entity Activity Patterns takes information about people, places, material items, organizations, events, etc. and organizes them into spatio-temporal and linkage contexts.  This enables highlighting of associations indicative of key patterns of activity such as geographic/geospatial location, memberships, clandestine organizational structures, financial activity patterns, attack indicator patterns, travel patterns, and so-called \"patterns of life\". \n\nService operations will include interrelated data organizing, filtering, display, and linkage analysis tools.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
  "TagNotes_JCSFL Alignment":"14.1.4  Produce Human Intelligence (HUMINT) Data",
  "TagValue_JARM/ESL Alignment":"7.04.08 Link Analysis Services",
  "TagNotes_Comments":"Dependency on data analytics"
},
{
  "TagValue_UID":'0076',
  "TagValue_Number":"3.4.3",
  "TagValue_Service Name":"HUMINT Analysis",
  "TagNotes_Service Definition":"The HUMINT Analysis family establishes intelligence derived from human collected information sources.",
  "TagNotes_Service Description":"The CI/HUMINT Analysis family provides intelligence derived from information collected by counter intelligence and/or human sources.  Typically HUMINT data is written reports containing raw text, but it may also be electronic telephone records, online sources, computer sources, audio transcripts, etc.\nThe objective of CI/HUMINT Analysis is to transform and filter the raw source material into an organized format that allows reports to be generated concerning specific people, activities, items (such as smuggled goods), institutional structures (such as insurgent cell networks and command chains), and events (both past and planned).\nSpecific CI/HUMINT services include:\n  • Entity Linkages - which associates CI/HUMINT related reports based on related metadata. \n  • Entity Activity Patterns - which determines intelligence relationships among specific objects such as people, places, items, and events.\n  • Identity Disambiguation - which determines if one entity (usually a person) is the same as another entity by analyzing descriptive information on the two entities.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0081',
  "TagValue_Number":"3.3.6",
  "TagValue_Service Name":"Support to Targeting",
  "TagNotes_Service Definition":"The Support to Targeting family provides the data necessary for the   target development process.",
  "TagNotes_Service Description":"Support to Targeting services implement target development process including managing and maintaining target lists, assisting in target development, functionality and vulnerability assessment, impact point  specification, status reporting, and targeting report dissemination.   \n\nSpecific services include:\n\n  • Target Management - which compiles and reports target information \n  • Target Data Matrix - which provides current target status  \n  • Target Validation - which portrays and locates target services, indicates vulnerabilities, and maintains relative target importance.\n  • Target Folder - which maintains hosts target intelligence artifacts \n  • Target List - which maintains summaries of candidate targets",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"GVS MIDB related services seem to apply here, but not sure exactly how at the lower levels"
},
{
  "TagValue_UID":'0038',
  "TagValue_Number":"3.2.1.4",
  "TagValue_Service Name":"Sensor Alerting",
  "TagNotes_Service Definition":"Sensor Alerting delivers sensor notifications to registered endpoints based on client-configurable subscriptions.",
  "TagNotes_Service Description":"Sensor Alerting  requests information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.    It receives metadata documents describing a server's abilities along with the sensor description, then retrieves a sensor alert message structure template along with the WSDL definition for the sensor interface.    A client can also subscribe to a sensor's alerts, renew an existing subscription, or cancel a subscription.  Optionally, servers can advertise the type of information published, renew an existing advertisement, or cancel an advertisement.                        \n\n[This service is supported by the OGC SWE SES discussion paper.]",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"3.3.5 Generate Alert\n3.3.13 Manage Alerts and Indications",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0037',
  "TagValue_Number":"3.2.1.3",
  "TagValue_Service Name":"Sensor Command Conversion",
  "TagNotes_Service Definition":"Sensor Command Conversion converts sensor requests into sensor specific commands.",
  "TagNotes_Service Description":"Sensor Command Conversion converts collection requests into tasking formats that a specific sensor type can understand.  The resulting commands are then sent to the sensor.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.32 Convert Geospatial Intelligence (GEOINT) Sensor Requests",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0342',
  "TagValue_Number":"3.2.1.2",
  "TagValue_Service Name":"Sensor Cross Queuing",
  "TagNotes_Service Definition":"Sensor Cross Queuing passes detection, Geo-location and targeting information to another sensor.",
  "TagNotes_Service Description":"The Sensor Cross Cueing service is broken into three subservices:  \n(1) Cue Instruction Definition - allows the client to develop a set of sensor cue instructions that accomplish a specific objective.  These instructions describe the parameters which trigger a sensor detection and rules by which the sensor determines what other sensors and/or detection adjudication servers to notify once a detection is made.   The Cue Instruction Definition subservice consists of a set of operations that can be performed by the client. \n(2) Cue Instruction Distribution  - Once a set of sensor cue instructions are developed for a specific objective,  the Cue Instruction Distribution subservice allows a client to distribute those instructions to the appropriate sensors and/or detection adjudication servers \n(3) Detection Distribution  - Once a sensor has made a detection, Detection Distribution distributes the detection, geo-location, and targeting information to other sensors and/or detection adjudication servers that were defined in the previously received cue instruction set.                 \n\nScoping note:  Correlation and fusion of sensor data is outside the scope of Sensor Cross Cueing.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.1.20 Perform Signal Parametric Analysis\n1.4.1 Fuse Sensor Data\n1.4.6 Coordinate Sensor Employment with Operational Plans and Employment\n1.4.27 Reconfigure Netted Sensor Grid Dynamically",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0340',
  "TagValue_Number":"3.2.1.1",
  "TagValue_Service Name":"Sensor Provisioning",
  "TagNotes_Service Definition":"Sensor Provisioning sends messages to various sensors asking what assignments they can perform, whether a task request is valid, and adding, modifying and canceling tasking requests.",
  "TagNotes_Service Description":"Sensor Provisioning allows clients to query individual sensors for information about appropriate sensor tasking, and to task the sensor.  For each sensor, clients can request and receive metadata documents that describe the actions the sensor can perform, request information needed to prepare assignment requests, and request feedback as to whether a specific assignment request is valid or needs improvement to meet business rules.   In addition, the client can send the sensor an assignment request, update a prior assignment request, cancel a prior assignment request, or request the return of the sensor status, .  Lastly, the client can inquire about where and how the results of the tasking can be accessed.\n\n[This service is supported by the OGC SWE SPS 2.0 service.]",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.17  Determine Time to Sensor Availability\n1.4.18  Identify Elapsing Platform and Sensor Tasking",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0033',
  "TagValue_Number":"3.2.1",
  "TagValue_Service Name":"Asset Management",
  "TagNotes_Service Definition":"The Asset Management family of services provide sensor collection tasking and status monitoring.  Included are efforts to allocate Enterprise resources, as well as gain insight into the ability to influence external collection assets.",
  "TagNotes_Service Description":"The Asset Management family of services provide sensor collection tasking and status monitoring.  Specific services include:\n• Sensor Provisioning - which sends messages to various sensors asking concerning tasking assignments \n• Sensor Registration - which allows clients to request, add, or delete information about sensors and servers     \n• Sensor Cross Queuing - which passes detection, Geo-location and targeting information to another sensor. \n• Sensor Command Conversion - which converts sensor requests into sensor specific commands.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0045',
  "TagValue_Number":"3.2",
  "TagValue_Service Name":"Collection",
  "TagNotes_Service Definition":"The Collection line includes services that provide the ability to gather data and obtain required information to satisfy information needs.",
  "TagNotes_Service Description":"The Tasking (Planning & Direction) line includes services that provide the ability to gather data and obtain required information to satisfy information needs.  This includes the following sub-areas: Signals Collection – The ability to gather information based on the interception of electromagnetic impulses.  Imagery Collection – The ability to obtain a visual presentation or likeness of any natural or man-made feature, object, or activity at rest or in motion.  Measurements and Signatures Collection – The ability to collect parameters and distinctive characteristics of natural or man-made phenomena, equipment, or objects.  Human Based Collection – The ability to acquire information from human resources, human-derived data, and human reconnaissance assets. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0031',
  "TagValue_Number":"3",
  "TagValue_Service Name":"Mission Services",
  "TagNotes_Service Definition":"The functions/services that represent a mission or business process or function.  When choreographed they represent the manifestation of high level mission capabilities.",
  "TagNotes_Service Description":"Layer",
  "TagValue_Example Specification":"Layer",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"Layer",
  "TagValue_DCGS Enterprise Status":"Layer",
  "TagValue_JCA Alignment":"Layer",
  "TagNotes_JCSFL Alignment":"Layer",
  "TagValue_JARM/ESL Alignment":"Layer",
  "TagNotes_Comments":"Layer"
},
{
  "TagValue_UID":'0361',
  "TagValue_Number":"3.5.1.4",
  "TagValue_Service Name":"Foreign Disclosure Management",
  "TagNotes_Service Definition":"Foreign Disclosure Management services support the  release of classified military information and law enforcement information to foreign entities or governments. Provides the ability to receive, process, release, and monitor requests for information release.\n",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"6.02.03 Content Delivery Services",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0360',
  "TagValue_Number":"3.5.1.3",
  "TagValue_Service Name":"Tear Line Reporting",
  "TagNotes_Service Definition":"Tear Line services support creation of Tear Lines within products to support dissemination across multiple security domains.",
  "TagNotes_Service Description":"The Tear Line Reporting service uses security markings and metadata to generate tear lines from existing intelligence reports, to support foreign disclosure and allow important intelligence to be disseminated to a greater audience without risking sensitive information.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"6.02.03 Content Delivery Services",
  "TagNotes_Comments":"Might want to combine with TDF"
},
{
  "TagValue_UID":'0089',
  "TagValue_Number":"3.5.1.2",
  "TagValue_Service Name":"Package Product",
  "TagNotes_Service Definition":"This service converts the intelligence product into a suitable form for dissemination.",
  "TagNotes_Service Description":"This service converts the intelligence product into a suitable form for dissemination.  After exploitation is complete and an intelligence product has been created, it must be prepared for dissemination.  This may include converting to a file format suitable for the customer, final security review of the intelligence product to be disseminated, and finding and assigning a globally unique number in preparation for dissemination.  This service would call to the Global Object ID service for assignment of the globally unique number.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"2.5.1 BA Data Transmission (IDD)",
  "TagNotes_JCSFL Alignment":"14.1.11 Disseminate Intelligence Products",
  "TagValue_JARM/ESL Alignment":"6.02.03 Content Delivery Services",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0088',
  "TagValue_Number":"3.5.1",
  "TagValue_Service Name":"Dissemination Management",
  "TagNotes_Service Definition":"Dissemination Management includes authorizing the release/dissemination of products, the assignment of addresses to receive those products, as well as assignment of the transmission path/medium for dissemination of the products.",
  "TagNotes_Service Description":"Dissemination Management includes authorizing the release/dissemination of products, the assignment of addresses to receive those products, as well as assignment of the transmission path/medium for dissemination of the products.  \n\nCurrently the one specific service included is Package Product which converts the intelligence product into a suitable form for dissemination.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0087',
  "TagValue_Number":"3.5",
  "TagValue_Service Name":"BA Data Dissemination and Relay",
  "TagNotes_Service Definition":"The BA Data Dissemination and Relay line includes services that provide the ability to present, distribute, or make available intelligence, information and environmental content and products that enable understanding of the operational/physical environment to military and national decision-makers.",
  "TagNotes_Service Description":"The BA Data Dissemination and Relay line includes services that provide the ability to present, distribute, or make available intelligence, information and environmental content and products that enable understanding of the operational/physical environment to military and national decision-makers.  This includes the following sub-areas: BA Data Transmission – The ability to send collected data directly to processing, exploitation analysis, production and visualization systems, leveraging both Net-Centric information transport and intelligence-controlled systems.  BA Data Access – The ability to provide authorized customer access to data and products, leveraging both Net-Centric computing infrastructure and intelligence-controlled systems. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)\nFamilies in this line: DISSEMINATION MANAGEMENT and MESSAGING SYSTEM INTERFACE.",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0358',
  "TagValue_Number":"3.4.8.3",
  "TagValue_Service Name":"Mission Planning and Force Execution support",
  "TagNotes_Service Definition":"This component supports mission planning and force execution analysis to find, fix, track, and target.",
  "TagNotes_Service Description":"The service supports mission planning and force execution analysis to find, fix, track, and target.  \nFind: Develop JIPOE, detect target, and determine target conditions\nFix: confirm target, refine target location, and plot movement\nTrack: maintain situational awareness and maintain track continuity\nTarget: validate desired effects, finalize target data, and predict consequences",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"2.2 Combat Identification (CID) (Group)\n3.4.5 Maintain Shared Situational Awareness\n3.4.8 Manage Prioritization of Defended Asset Information Sets\n\n3.4.11 Translate and Distribute Commander's Intent",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0357',
  "TagValue_Number":"3.4.8.2",
  "TagValue_Service Name":"Intelligence Preparation of the Battlefield",
  "TagNotes_Service Definition":"Intelligence Preparation of the Battlefield services continuously analyze the threat and environment in an area.",
  "TagNotes_Service Description":"The Intelligence Preparation of the Battlefield service supports intelligence analysts in satisfying Joint Intelligence Preparation of the Operational Environment (JIPOE) requirements.  This service assists users in:\n? Identifying the Operational Area\n? Analyzing the Mission and the Commander's Intent\n? Determining significant characteristics of the Operational Environment\n? Establishing the physical and nonphysical limitations of the force's Area of Interest\n? Establishing appropriate level of detail for intelligence analysis\n? Identify intelligence and information gaps, shortfalls and priorities\n? Submitting requests for information to support further analysis",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"3.2.1 Display Common Operational Picture (COP)\n3.2.2 Display Common Tactical Picture (CTP)\n3.5.6 Access Joint Asset Information\n3.5.8 Access Unit Readiness and Logistics Reports\n3.5.9 Enter, Display, Update, and Monitor Force Status\n3.5.10 Present Mission Resources Information\n3.5.14 Confirm Asset or Sensor Availability\n3.5.16 Identify and Catalog Joint Assets and Activities\n14.1.24 Provide Graphical Intelligence Preparation of the Operational Environment (IPOE) Products\n14.1.25 Provide Data Assets from Intelligence Preparation of the Operational Environment (IPOE) Products\n14.1.26 Conduct Joint Intelligence Preparation of the Operational Environment",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0356',
  "TagValue_Number":"3.4.8.1",
  "TagValue_Service Name":"Order of Battle Analysis",
  "TagNotes_Service Definition":"Order of Battle Analysis determines the identification, strength, command structure, and disposition of the personnel, units, and equipment of any military force.",
  "TagNotes_Service Description":"The Order of Battle Analysis service assists users in analyzing intelligence about friendly and opposing forces, and generating intelligence briefs about those forces.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"3.4 Situational Awareness (SA) Data Management (Group)\n3.4.10 Integrate Information on Potential Adversary Courses of Action (COAs)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0204',
  "TagValue_Number":"3.4.8",
  "TagValue_Service Name":"Analysis Support to C2",
  "TagNotes_Service Definition":"This line provides intelligence support for command and control",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0030',
  "TagValue_Number":"3.4.7.6",
  "TagValue_Service Name":"Orchestration Modeling",
  "TagNotes_Service Definition":"Orchestration Modeling presents an interface for designing the flow of service executions to achieve an overall functional need.",
  "TagNotes_Service Description":"Orchestration Modeling presents interfaces for designing actual or potential flows of service executions to achieve an overall functional need.   Functionality includes design definition and management.  Resultant models may be stored in orchestration languages such as Business Process Execution Language (BPEL) or Business Process Modeling Notation (BPMN). Business Process Management Language (BPML), or Web Service Choreography Interface (WSCI).     \n\nThe most popular of these, BPEL, is made of three main entities: partners that abstractly represent the services Involved, variables used to manipulate exchanged data and hold business logic states, and activities that describe the business logic operations such as invoking a web service or assigning a value to a variable.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.18.01 Modeling",
  "TagNotes_Comments":"Dependency on orchestration"
},
{
  "TagValue_UID":'0355',
  "TagValue_Number":"3.4.7.5",
  "TagValue_Service Name":"Target Solution Modeling",
  "TagNotes_Service Definition":"Target Solution Modeling models and/or simulates weaponeering and  weapon effects.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.18.01 Modeling",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0029',
  "TagValue_Number":"3.4.7.4",
  "TagValue_Service Name":"Sensor Modeling",
  "TagNotes_Service Definition":"Sensor Modeling models and/or simulates ISR platform and sensor access and collection parameters over time.",
  "TagNotes_Service Description":"Sensor Modeling models and/or simulates ISR platform and sensor access and collection parameters over time.  \n\nStructures of sensor modeling systems typically include Players (the specific sensor being modeled), Markers (those things implicitly represented; e.g., tactical positions and collection domains), Environment (establishes the physical battlespace), and Zones (abstract spaces where the location of sensors or related elements is not an important element of the model).\n\nEvents fall into these classes: physical (e.g., start of collection gathering), Information Exchange (e.g., orders, reports, and messages), and Element coordination (place in explicit modeling).\n\nSensor modeling operations typically might include: Simulation control (stop, pause, and resume simulation), Game state update; Command and Control (C2) decisions; and External events from outside the simulation.\n\nCriteria monitored might include items such as: Sensor maximum services, Sensor schedules, Sensor lifecycle estimates; Maintenance schedules; Intelligence needs assessments; Sensor identity, instance, location, and environment; Visual appearance, Acoustic appearance, Radar signatures, Identified intention, Mission role; Sensor health estimates; Relationship to ground receiving systems; and Probability tables and estimates.\n\nSensors modeled might include (but aren’t limited to): Class I and IV Unmanned Aerial Vehicles (UAVs), Electro Optical/Infrared Sensors (EO/IR), Synthetic Aperture Radar (SAR) sensors, Moving Target Indicators (MTIs), Aerial Common Sensor (ACS), U-2/TR-1, Global Hawk, Predator, Joint Surveillance Target Attack Radar System (JSTARS), Advanced Tactical Air Reconnaissance System (ATARS),  Prophet,  Airborne Reconnaissance Integrated Electronics Suite, and Unattended Ground Sensors (UGSs).",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"5.3.6  Model and Simulate Mission Scenarios",
  "TagValue_JARM/ESL Alignment":"8.18.01 Modeling",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0354',
  "TagValue_Number":"3.4.7.3",
  "TagValue_Service Name":"Model Building",
  "TagNotes_Service Definition":"The Model Building service supports the ability to build and visualize analytical models",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"5.3.9  Model and Simulate Risk Scenarios\n5.3.10  Identify Risk using Modeling and Simulation",
  "TagValue_JARM/ESL Alignment":"8.18.01 Modeling",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0353',
  "TagValue_Number":"3.4.7.2",
  "TagValue_Service Name":"Scenario Generation",
  "TagNotes_Service Definition":"The Scenario Generation services provides the ability to build and manage scenarios.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"5.3.8   Generate Wargaming Scenarios",
  "TagValue_JARM/ESL Alignment":"8.18.01 Modeling; 8.18.02 Simulation",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0028',
  "TagValue_Number":"3.4.7.1",
  "TagValue_Service Name":"War Gaming",
  "TagNotes_Service Definition":"War gaming brings together C4ISR, and Synthetic Natural Environments (SNE) and the Common Operating Picture (COP) to support course of action analysis, mission planning and rehearsal, and individual and collective training.",
  "TagNotes_Service Description":"War gaming models and/or simulate ISR services and collection parameters over time while allowing for analysis of the technical feasibility of the mission with respect to weather, terrain, battlefield conditions, order of battle, and threat characteristics through the use of expanded, robust Joint ISR resource management tools adapted to net-centric operations and transformational communications  (Ref. DCGS Integrated Roadmap (2005–2018) Version 1.2; 3 January 2005 (section 5))\nObjective war gaming operations include the ability to monitor, locate, identify, and represent military units and assets (Army, Navy, Marines, Air Force, and Special Operational Forces) as missions and sorties occur, build and maintain operational surface picture in an Area Of Interest (AOI);  develop and present threat conceptual models, services data, force structure and tactics; account for intelligence sensors, systems, processes, and organizations; re-use prior war game data and models; provide algorithms for the interaction of ‘Red’ (enemy force),  ‘Blue’ (friendly force), and non-combatant behaviors; maintain evolving threats catalog(s); maintain validated parameter and performance data; integrate decision making process including the impact of rules of engagement (ROE); provides realistic deterministic fusion methodology (versus simple stochastic replications); render “3D” views; present real time (or near real time) statistics; inject actual or potential weather effects; and accommodate Concepts of Operation (CONOPS) and other related threat policy and guidance.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.4.4 Prediction (AP)",
  "TagNotes_JCSFL Alignment":"4.6.3  Predict Situation Effects\n4.6.19  Predict Collateral Damage\n4.6.21  Calculate Probability of Engagement Effectiveness\n4.6.23  Analyze Force Vulnerability\n4.6.27  Determine Time to Complete the Mission\n5.3.6  Model and Simulate Mission Scenarios\n5.3.8   Generate Wargaming Scenarios\n5.3.9  Model and Simulate Risk Scenarios\n5.3.10  Identify Risk using Modeling and Simulation",
  "TagValue_JARM/ESL Alignment":"8.18.01 Modeling; 8.18.02 Simulation",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0027',
  "TagValue_Number":"3.4.7",
  "TagValue_Service Name":"Modeling and Simulation",
  "TagNotes_Service Definition":"The Modeling and Simulation family of services uses representative realities to assess current or possible future conditions in assess area of interest including DI2E ISR platforms and sensors.",
  "TagNotes_Service Description":"Modeling and Simulation uses representative realities to assess current or possible future conditions in assess area of interest including DI2E ISR platforms and sensors.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0365',
  "TagValue_Number":"3.4.6.5",
  "TagValue_Service Name":"Link Analysis",
  "TagNotes_Service Definition":"Link analysis is a data-analysis technique used to evaluate relationships (connections) between entities. Relationships may be identified among various types of objects, including organizations, people and transactions.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"5.2 Understand",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"7.04.08 Link Analysis Services",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0352',
  "TagValue_Number":"3.4.6.4",
  "TagValue_Service Name":"Alternative Future Analysis",
  "TagNotes_Service Definition":"Alternative Future Analysis supports postulating possible, probable, and preferable futures through a  systematic and pattern-based understanding of past and present, and to determine the likelihood of future events and trends.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.4.4 Prediction (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0351',
  "TagValue_Number":"3.4.6.3",
  "TagValue_Service Name":"Argument Mapping",
  "TagNotes_Service Definition":"Argument Mapping is the visual representation of the structure of an argument in informal logic. It includes the components of an argument such as a main contention, premises, co-premises, objections, rebuttals, and lemmas.",
  "TagNotes_Service Description":"Argument Mapping is the visual representation of the structure of an argument in informal logic. It includes the components of an argument such as a main contention, premises, co-premises, objections, rebuttals, and lemmas. Typically an argument map is a “box and arrow” diagram with boxes corresponding to propositions and arrows corresponding to relationships such as evidential support. Argument mapping is often designed to support deliberation over issues, ideas and arguments",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0349',
  "TagValue_Number":"3.4.6.1",
  "TagValue_Service Name":"Timelines Analysis",
  "TagNotes_Service Definition":"Timeline Analysis helps analysts determine relationships of events over time.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0024',
  "TagValue_Number":"3.4.6",
  "TagValue_Service Name":"Analytic Decision Support",
  "TagNotes_Service Definition":"The Analytic Decision Support Services family provides advanced analytic analysis and presentation to help analysts uncover, determine, or predict otherwise complex relationships among various DI2E entities.",
  "TagNotes_Service Description":"The Analytic Decision Support family provides advanced analytic analysis and presentation to help analysts uncover, determine, or predict otherwise complex relationships among various DI2E entities.    Services include the answering multi-dimensional analytical queries (online analytical processing).",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0370',
  "TagValue_Number":"3.4.5.3",
  "TagValue_Service Name":"Digital Production",
  "TagNotes_Service Definition":"Digital Production provides authoring, mark-up, dissemination, document control, geospatially visualized, geo-smart, GeoPDF functions.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0369',
  "TagValue_Number":"3.4.5.2",
  "TagValue_Service Name":"Production Workflow",
  "TagNotes_Service Definition":"Production Workflow provides the necessary management for the creation of new products, guiding the product status through various levels of approval.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0368',
  "TagValue_Number":"3.4.5.1",
  "TagValue_Service Name":"Reporting Services",
  "TagNotes_Service Definition":"Reporting services allow for the creation, editing, and approval of new reports.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0348',
  "TagValue_Number":"3.4.5",
  "TagValue_Service Name":"Production",
  "TagNotes_Service Definition":"The Production family of services to support production, to include Reporting Services, Production Workflow, and Digital Production.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0080',
  "TagValue_Number":"3.4.4",
  "TagValue_Service Name":"MASINT/AGI Analysis",
  "TagNotes_Service Definition":"Measurement and Signature Intelligence/Advanced Geospatial Intelligence (MASINT/AGI) Analysis services provide intelligence derived from many different sources and sensors to identify the specific characteristics of a target and enables it to be located and tracked.   \nNote: The family of MASINT services is very broad and many are sensitive or classified.   The single example provided below may be supplemented after engagement with the DI2E MASINT Functional Team.",
  "TagNotes_Service Description":"Measurement and Signature Intelligence/Advanced Geospatial Intelligence (MASINT/AGI) Analysis services provide intelligence derived from many different sources and sensors to identify the specific characteristics of a target and enables it to be located and tracked.   \nNote: The family of MASINT services is very broad and many are sensitive or classified.     Other (unclassified) MASINT examples include:\n  • SAR Coherent Change Detection (CCD)\n  • differential interferometric SAR (DInSAR)\n  • EO polarimetry\n  • SAR polarimetry\nThus the single COMINT Externals Analysis example provided below may be supplemented with additional services after discussions are held with the DI2E MASINT Functional Team.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0075',
  "TagValue_Number":"3.4.2.4",
  "TagValue_Service Name":"COMINT Externals Analysis",
  "TagNotes_Service Definition":"COMINT Externals Analysis analyzes patterns of communications metadata.",
  "TagNotes_Service Description":"COMINT Externals Analysis provides link analysis functionality for the specific application of highlighting patterns of communications activity.   The analysis is based solely on communications metadata (origination and destination node IDs, cell phone IDs, phone numbers, packet lengths, etc.) and not communication content (digitized voice, text messages, etc.).\n\nTo do this, COMINT Externals Analysis first ingests communication metadata and, if needed, strips off associated message content.  The resultant metadata entities are then linked to each other, as well as other entities.  Message transmission structures may also be analyzed for such things as statistics showing how often packets with one specific format are immediately followed by packets with a second specified format.  \n\nAnalyzing the structure of analog communications signals is possible, but digital analysis is increasingly more common.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
  "TagNotes_JCSFL Alignment":"8.2.26 Configure Communications and Security Devices\n8.12.4 Allocate and Manage Collaboration Communications",
  "TagValue_JARM/ESL Alignment":"8.16.05 Signals Analysis",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0074',
  "TagValue_Number":"3.4.2.3",
  "TagValue_Service Name":"Emitter Geolocation",
  "TagNotes_Service Definition":"Emitter Geolocation Analysis & Location Refinement determines the likely position of an emitter source from emitter intercept data.",
  "TagNotes_Service Description":"Emitter Geolocation takes correlated emitter intercepts collected by one or more platforms and computes the likely emitter geolocation, geolocation error ellipse, and (if the emitter is in motion) an estimate of the emitter's velocity.\n\nIntercepts may contain signal frequency and/or time of arrival information, intercept angle of arrival (AOA) , ID and \"collection instant geolocation\" of the platform(s) that generated the intercept reports, associated pointing/position uncertainties characterizing the collection systems involved, and/or previously computed emitter geolocation along with the geolocation error ellipse and target velocity vector.\n\nExample analytic methods include: 1) triangulation using AOA data, 2) time difference of arrival (TDOA),  3) frequency difference of arrival (FDOA), or 4) some combination of the first three methods.",
"TagValue_Example Specification":"N/A",
"TagValue_Example Solution":"N/A",
"TagValue_DI2E Framework Status":"4",
"TagValue_DCGS Enterprise Status":"2",
"TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
"TagNotes_JCSFL Alignment":"(gap)",
"TagValue_JARM/ESL Alignment":"8.16.05 Signals Analysis",
"TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0073',
  "TagValue_Number":"3.4.2.2",
  "TagValue_Service Name":"Emitter Correlation",
  "TagNotes_Service Definition":"Emitter Correlation correlates multiple intercepts to the same signal emitting entity.",
  "TagNotes_Service Description":"The service ingests emitter intercept signal characterizations and identifies which signal intercepts come from the same emitter by identifying signals whose extracted features sufficiently match.    The output is a list of which of the ingested signals (or signal pulse sequences) come from each separate emitter source, along with the associated times of the signal intercepts.\n\nThere are two ways in which this service can be used:  (1) Supporting multi-platform emitter geolocation analysis over short period using a pulse-by-pulse basis and (2) correlating signals collected over a more prolonged time interval using signal intercepts from several distinct emitters to analyze sequences of pulses for matching signal characteristics.\n\nEmitter Correlation is in some ways the converse of the ELINT de-duplication service (see 2.3.2 SIGINT Processing services).",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.3.2 Information Categorization (PE)",
  "TagNotes_JCSFL Alignment":"1.1.7  Register Electromagnetic Signals\n3.2.32 Mensurate Object Coordinates",
  "TagValue_JARM/ESL Alignment":"8.16.05 Signals Analysis",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0072',
  "TagValue_Number":"3.4.2.1",
  "TagValue_Service Name":"SIGINT Analysis and Reporting",
  "TagNotes_Service Definition":"SIGINT Analysis and Reporting generates various reports from raw SIGINT data.",
  "TagNotes_Service Description":"SIGINT Analysis and Reporting generates selected types of SIGINT reports from ingested SIGINT intercept data.   Three examples of the types of reports produced include: \n\n1)  A report on the full Electronic Order of Battle (EOB) of an adversary within a specified region.   The report might name the identified emitters along with a description of the function/services of the overall system they are associated with.  The report might also attempt to identify specific military units based on the identified emitters.\n\n2)  A report on the locations of all surface-to-air defense systems in a specified region.  The report would list the services, the mobility potential, and the known vulnerabilities or limitations of each air defense unit identified.\n\n3)  A report on the signal properties and service implications of unknown emitters detected in a specified region.   Such a report might note the presence of an unidentified chirped pulse emitter and, based on the chirp bandwidth, pulse repetition intervals, and scan rates suggest that the emitter is part of an upgraded surface-to-air defense system.",
"TagValue_Example Specification":"N/A",
"TagValue_Example Solution":"N/A",
"TagValue_DI2E Framework Status":"4",
"TagValue_DCGS Enterprise Status":"2",
"TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
"TagNotes_JCSFL Alignment":"1.1.20  Perform Signal Parametric Analysis\n1.1.13  Process Acoustic Sensor Data\n14.1.16 Disseminate High Priority Tactical Signals Intelligence (SIGINT) Report\n14.1.16  Disseminate High Priority Tactical Signals Intelligence (SIGINT) Report\n14.1.28  Disseminate Tactical Signals Intelligence (SIGINT) Report",
"TagValue_JARM/ESL Alignment":"8.16.05 Signals Analysis",
"TagNotes_Comments":"Terry (SIGINT FT, terence.a.wynne@lmco.com, 301-543-5441. ) proposed to drop the “SIGINT Analysis & Reporting” service, and to add 2 new ones: Specific Emitter Identifier (see Terry or Mark P. for more details), and Purge/Surge (or Enterprise Data Header) ? that should go under Records Mgmt…for more details, contact Melissa Bodman. - Julie Jamieson, August 8th, 2011"
},
{
  "TagValue_UID":'0071',
  "TagValue_Number":"3.4.2",
  "TagValue_Service Name":"SIGINT Analysis",
  "TagNotes_Service Definition":"SIGINT Analysis services provide various forms of signals intelligence (SIGINT) varying from recurring, serialized reports to instantaneous periodic reports.",
  "TagNotes_Service Description":"The SIGINT Analysis family of services support Electronic Intelligence (ELINT), Communications Intelligence (COMINT), and Foreign Instrumentation Signals Intelligence (FISINT).  \n • ELINT is the interception, geolocation, and analysis of electronic emissions produced by adversary equipment that intentionally transmits for non-communications purposes.  Most notably this includes radars but sometimes also includes other types of weaponry and equipment.   The purposes of ELINT are to: ascertain services and limitations of target emitters, geolocate target emitters, and determine the current state of readiness of target emitters.\n  • COMINT is the interception, geolocation, and decryption of either voice or electronic text transmissions. Included within COMINT analysis is communications traffic analysis.  \nSpecific SIGINT Analysis services include:\n  • SIGINT Analysis and Reporting - which generates various reports from raw SIGINT data. \n  • Emitter Correlation - which correlates multiple intercepts to the same signal emitting entity. \n  • Signal Pattern Recognition - which provides signal pattern recognition in order to detect signal characteristics that tell analysts information about the emitting source.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0366',
  "TagValue_Number":"3.4.1.11",
  "TagValue_Service Name":"GEO-Calculations",
  "TagNotes_Service Definition":"Geo-Calculations provide the ability to perform geospatially based calculations on data stored within the enterprise to further enrich the operator’s understanding of the data relationships.\n",
  "TagNotes_Service Description":"Users have the need to perform calculations and analytics on the geospatial within the enterprise.  Example analytics include geo-location lookups, distance calculations between two entities, coordinate conversions, and entity lookup.\n",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"3.4.3 Receive, Store and Maintain Geospatial Product Information\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
  "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0062',
  "TagValue_Number":"3.4.1.10",
  "TagValue_Service Name":"Sensor Model Instantiation",
  "TagNotes_Service Definition":"Sensor Model Instantiation generates sensor model state data from image metadata. ",
  "TagNotes_Service Description":"Sensor Model Instantiation determines a sensor model type and state data given raw image metadata.  \n\nAs an example, consider SAR and pushbroom EO sensor models ((pushbroom scanners deliver a perspective image with highly correlated \nexterior orientation parameters for each image line).   The SAR sensor model might require specification of collection squint and grazing angles.  However, the raw collection taken from the EO sensor may only provide metadata on the sensor collection trajectory and the specified image center.  The Sensor Model Instantiation service can use this raw EO sensor metadata to calculate collection squint and grazing angles as used by the SAR sensor model.  In other simpler cases, the service may simply transfer raw collection metadata values directly into matching sensor model parameter fields.\n\nResulting sensor model state data can be used with associated sensor functions such as image-to-ground and ground-to-image transformations and will support (but not provide) other GEOINT services such as Image Registration, DPPDB Mensuration, Triangulation, Source Selection, Resection, Rectification, and Geomensuration (see definitions in related 2.4.1 GEOINT Exploitation services).",
    "TagValue_Example Specification":"N/A",
    "TagValue_Example Solution":"N/A",
    "TagValue_DI2E Framework Status":"4",
    "TagValue_DCGS Enterprise Status":"2",
    "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
    "TagNotes_JCSFL Alignment":"3.2.36  Generate Coordinates\n3.2.32 Mensurate Object Coordinates\n3.4.3 Receive, Store and Maintain Geospatial Product Information",
    "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
    "TagNotes_Comments":"None"
  },
  {
    "TagValue_UID":'0070',
    "TagValue_Number":"3.4.1.9",
    "TagValue_Service Name":"Automatic Target Recognition",
    "TagNotes_Service Definition":" Automatic Target Recognition identifies specified entities on an image using image processing techniques. \n",
    "TagNotes_Service Description":"Automatic Target Recognition services process imagery to identify candidate specified 'targets' that match selected pixel 'signature patterns'.    \n\nExamples of 'targets' might be a tank even if partially covered or camouflaged an aircrafts located on runways.\n\nArtifact formats, targets, and analytic methods vary, but one example is an ATR service that ingests hyperspectral imagery (HSI) in the near IR (NIR), short wave IR (SWIR) bands, and target signature definitions and returns image coordinates where signatures are found along with nature of each candidate 'target'.   To do this it might perform anomaly detection through orthogonal subspace projection (considering each HSI pixel a vector in a multidimensional space, determining dominant spectral signatures by running a pixel clustering algorithm, creating a subspace projection operator matrix, and projecting the pixel's signature into a spectral subspace that does not contain the identified clutter signature components).",
    "TagValue_Example Specification":"N/A",
    "TagValue_Example Solution":"N/A",
    "TagValue_DI2E Framework Status":"4",
    "TagValue_DCGS Enterprise Status":"3",
    "TagValue_JCA Alignment":"2.4.2 Evaluation (AP)",
    "TagNotes_JCSFL Alignment":"3.4.3 Receive, Store and Maintain Geospatial Product Information\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data\n\n14.1.21  Extract Automated Features",
    "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
    "TagNotes_Comments":"None"
  },
  {
    "TagValue_UID":'0069',
    "TagValue_Number":"3.4.1.8",
    "TagValue_Service Name":"Topographical Survey",
    "TagNotes_Service Definition":"Topographical Survey derives and represents elevation and terrain characteristics from reports and data.\n",
    "TagNotes_Service Description":"Topographical Survey generates maps based on a variety of terrain, elevation, and feature data.  One example is creating a 2D contour line map with vector graphics indicating features such as rivers, communication lines, and known roads.\n",
    "TagValue_Example Specification":"N/A",
    "TagValue_Example Solution":"N/A",
    "TagValue_DI2E Framework Status":"4",
    "TagValue_DCGS Enterprise Status":"3",
    "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
    "TagNotes_JCSFL Alignment":"3.4.3 Receive, Store and Maintain Geospatial Product Information\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
    "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
    "TagNotes_Comments":"None"
  },
  {
    "TagValue_UID":'0068',
    "TagValue_Number":"3.4.1.7",
    "TagValue_Service Name":"MTI Tracking",
    "TagNotes_Service Definition":"Moving Target Indicator (MTI) Tracking links 'dots' of point locations into tracks that indicate historical & projected direction and speed.\n",
    "TagNotes_Service Description":"Moving Target Indicator (MTI) Tracking ingests and tags a sequence of point locations (\"dots\") of moving objects indicating their speed, direction, range, azimuth location (an angular measurement in a spherical coordinate system), and time stamp.    Dots from raw MTI sources are then linked into track segments which last until either a moving object stops, and thus disappears from the MTI sensor data stream or until an occlusion or sharp change of speed, or some other confounding event causes the service to \"break track\".     \n\nDots may also have additional attributes such as a one dimensional range profile, shape and/or intensity descriptor.\n",
    "TagValue_Example Specification":"N/A",
    "TagValue_Example Solution":"N/A",
    "TagValue_DI2E Framework Status":"4",
    "TagValue_DCGS Enterprise Status":"3",
    "TagValue_JCA Alignment":"2.4.4 Prediction (AP)",
    "TagNotes_JCSFL Alignment":"2.1.1  Form Tracks\n\n2.1.33  Provide Tracking Services\n\n3.4.20 Maintain Track Management Information\n\n3.4.21 Display Tracks",
    "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
    "TagNotes_Comments":"None"
  },
  {
    "TagValue_UID":'0067',
    "TagValue_Number":"3.4.1.6",
    "TagValue_Service Name":"Image Registration",
    "TagNotes_Service Definition":"Image Registration matches points in a selected image to one or more control images to increase the accuracy of geoposition coordinates in the selected image.\n",
    "TagNotes_Service Description":"Image Registration selects image coordinate matching points between a selected image and one or more control images.  It then adjusts the sensor model for the selected image through resection (converging cycles of bundle adjustments, accompanied by associated sensor model parameter adjustments and outlier point eliminations).   Positional uncertainties including circular and linear error are also propagated.\n\nThe end result is common geospatial coordinates between the selected image and the control image thus enabling accurate image overlays, mosaicking (appending images together to construct a single image covering a larger scene), or georeferencing.",
    "TagValue_Example Specification":"N/A",
    "TagValue_Example Solution":"N/A",
    "TagValue_DI2E Framework Status":"4",
    "TagValue_DCGS Enterprise Status":"2",
    "TagValue_JCA Alignment":"2.4.1 Integration (AP)",
    "TagNotes_JCSFL Alignment":"3.4.3 Receive, Store and Maintain Geospatial Product Information",
    "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
    "TagNotes_Comments":"None"
  },
  {
    "TagValue_UID":'0066',
    "TagValue_Number":"3.4.1.5",
    "TagValue_Service Name":"DPPDB Mensuration",
    "TagNotes_Service Definition":"Digital Point Positioning Database (DPPDB) Mensuration performs geopositioning by aligning directly to DPPDB metadata.\n",
    "TagNotes_Service Description":"Digital Point Positioning Database (DPPDB)  Mensuration (geometric measurement) identifies a point on one of the DPPDB images and returns the geolocation of that point, complete with circular and linear errors.  The service uses the DPPDB sensor model and the fact that DPPDB reference imagery provides stereo depth to get the elevation as well as the horizontal geolocation of the chosen point.  \n\nThe service relies on the point being geolocated actually appearing in the DPPDB imagery.  Trying to estimate where a mobile target might lie when it is not visible in the DPPDB imagery and then selecting that estimated position in DPPDB image coordinates for target geolocation invites very serious geolocation errors. This estimation technique is referred to as \"visual point transfer\" and should be used only under very special circumstances.\n",
    "TagValue_Example Specification":"N/A",
    "TagValue_Example Solution":"N/A",
    "TagValue_DI2E Framework Status":"4",
    "TagValue_DCGS Enterprise Status":"2",
    "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
    "TagNotes_JCSFL Alignment":"2.1.39  Acquire and Track Target\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
    "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
    "TagNotes_Comments":"None"
  },
  {
    "TagValue_UID":'0065',
    "TagValue_Number":"3.4.1.4",
    "TagValue_Service Name":"Geomensuration",
    "TagNotes_Service Definition":"Geomensuration computes image based mensuration calculations using image points measured from an image.\n",
    "TagNotes_Service Description":"Geomensuration (measurement of geometric quantities) measures absolute geolocation of a photo identifiable point (geocoordinates, circular error and  linear error), horizontal distance and vertical elevation separations between two photo identifiable points (relative or absolute, and includes error ranges).\n\nIn general, a geomensuration service ingests image coordinates for one or more points in a georegistered image combined with an accompanying georegistered Digital Elevation Map (DEM) or in two or more georegistered images without an accompanying DEM.  It then returns one or more precision measurements based on their image coordinates.  Resected sensor models also help geomensuration computations.\n\nExamples of specialized geomensuration services might include \"height of object determined from measured shadow length\", \"location of center of a circle\", or \"perpendicular separation between two parallel plane surfaces\".\n",
    "TagValue_Example Specification":"N/A",
    "TagValue_Example Solution":"N/A",
    "TagValue_DI2E Framework Status":"4",
    "TagValue_DCGS Enterprise Status":"3",
    "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
    "TagNotes_JCSFL Alignment":"3.2.44  Display Range and Bearing Between Objects\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
    "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
    "TagNotes_Comments":"None"
  },
  {
    "TagValue_UID":'0064',
    "TagValue_Number":"3.4.1.3",
    "TagValue_Service Name":"Resection",
    "TagNotes_Service Definition":"Resection adjusts image support data when the sensor model data is unknown, incomplete, or imprecise.\n",
    "TagNotes_Service Description":"Resection uses Digital Point Positioning Database (DPPDB) stereo reference image pairs or other reference imagery, possibly combined with a reference Digital Elevation Map (DEM), to adjust the support data of an image acquired by a less accurate source.  \n\nThe service ingests the image coordinates of tie points in all the images including reference images and the acquired image that is to be resected as well as instantiated sensor models.  The sensor model of the acquired image has adjustable parameters and enhancing the values of these adjustable sensor model parameters is the objective of the Resection service.\n\nThe Resection service uses the reference data to compute 3D geolocations for the selected tie points.  It then performs cycles of bundle adjustment (i.e. least squares sensor model parameter value and tie point coordinate value adjustment) until  it achieves convergence with the measured image coordinates for all the 3D reference points mapped onto the acquired image coordinate space. The resulting adjusted sensor model parameter values for the acquired image constitute the resection results.",
    "TagValue_Example Specification":"N/A",
    "TagValue_Example Solution":"N/A",
    "TagValue_DI2E Framework Status":"4",
    "TagValue_DCGS Enterprise Status":"2",
    "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
    "TagNotes_JCSFL Alignment":"3.4.3 Receive, Store and Maintain Geospatial Product Information\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
    "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
    "TagNotes_Comments":"None"
  },
  {
    "TagValue_UID":'0063',
    "TagValue_Number":"3.4.1.2",
    "TagValue_Service Name":"Triangulation",
    "TagNotes_Service Definition":"Triangulation calculates a geolocation estimate based on multiple image collections acquired from distinct collection angles. \n",
    "TagNotes_Service Description":"Triangulation performs a geolocation estimate of a photo identifiable point based on multiple image collections acquired from distinct collection angles.  In this process it combines four distinct algorithmic functions: 1) least squares adjustment of sensor support data along with selected image plane tie or control point measurements that are each common to two or more images, 2) rigorous uncertainty propagation calculations, 3) geolocation computations using a triangulation method, and 4) blunder detection to discard point measurement outliers.\n\nTo initiate a triangulation the service is presented with a set of images and associated image point coordinates.  Image sensor models and coordinates are also provided from each image for the point to be geolocated.    Adjustable parameters include image collection geometries and (sometimes) sensor internal parameters, such as lens focal length or lens distortion.\n",
"TagValue_Example Specification":"N/A",
"TagValue_Example Solution":"N/A",
"TagValue_DI2E Framework Status":"4",
"TagValue_DCGS Enterprise Status":"2",
"TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
"TagNotes_JCSFL Alignment":"3.2.36  Generate Coordinates\n\n3.2.32 Mensurate Object Coordinates\n\n3.4.3 Receive, Store and Maintain Geospatial Product Information",
"TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
"TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0060',
  "TagValue_Number":"3.4.1.1",
  "TagValue_Service Name":"Change Detection",
  "TagNotes_Service Definition":"Change Detection identifies feature changes via a pixel analysis between two images of the same area.",
  "TagNotes_Service Description":"Change detection ingests two precisely co-registered images and identifies differences in pixels collected before and after some interval of time at the same geographic location.   Typically the changes in pixel values must exceed a user defined thresholds including registration uncertainty \"guard band\", size, color, and/or shape in order to screen out noise/clutter changes.   Output is an image formatted mask that identifies which pixels have significant changes between the two images.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"2.4.1 Integration (AP)",
  "TagNotes_JCSFL Alignment":"3.4.3 Receive, Store and Maintain Geospatial Product Information",
  "TagValue_JARM/ESL Alignment":"7.04.06 Imagery Analysis Services",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0059',
  "TagValue_Number":"3.4.1",
  "TagValue_Service Name":"GEOINT Analysis",
  "TagNotes_Service Definition":"GEOINT Analysis services describe, assess, and visually depict physical features and geographically referenced activities on the Earth.",
  "TagNotes_Service Description":"GEOINT Analysis services provide intelligence information ( imagery, imagery intelligence, multi-spectral analysis, hyper-spectral analysis, etc.) for geographically referenced features and activities.   Specific services include:\n • Change Detection - which identifies pixel-by-pixel differences between two images of the same area.\n  • Source Selection - which presents a user with sets of images that can satisfy a geopositioning precision requirement.\n  • Sensor Model Instantiation - which generates sensor model state data from image metadata. \n  • Triangulation  - which calculates a geolocation estimate based on multiple image collections acquired from distinct collection angles. \n  • State Service - which converts image support data into a related sensor model state string.\n  • Resection  - which adjusts image support data when the sensor model data is unknown, incomplete, or imprecise.\n  • Geomensuration  - which computes image based mensuration calculations using image points measured from an image.\n  • DPPDB Mensuration - which performs geopositioning by aligning directly to DPPDB metadata.\n  • Image Registration - which matches points in a selected image to one or more control images to increase the accuracy of geoposition coordinates.\n  • Moving Target Indicator (MTI) - which links 'dots' of point locations into tracks that indicate historical & projected direction and speed.\n  • Topographical Survey - which derives and represents elevation and terrain characteristics from reports and data. \n  • Automatic Target Recognition - which identifies specified entities on an image using image processing techniques. \n  • Emitter Geolocation - which determines the likely position of an emitter source from emitter intercept data.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0058',
  "TagValue_Number":"3.4",
  "TagValue_Service Name":"Analysis, Prediction and Production",
  "TagNotes_Service Definition":"The Analysis, Prediction & Production line includes services that provide the ability to integrate, evaluate, interpret, and predict knowledge and information from available sources to develop intelligence and forecast the future state to enable situational awareness and provide actionable information.",
  "TagNotes_Service Description":"The Analysis, Prediction & Production line includes services that provide the ability to integrate, evaluate, interpret, and predict knowledge and information from available sources to develop intelligence and forecast the future state to enable situational awareness and provide actionable information.  This includes the following sub-areas: Integration – The ability to identify, assimilate and correlate relevant information from single or multiple sources.  Evaluation – The ability to provide focused examination of the information and assess its reliability and credibility to a stated degree of confidence.  Interpretation – The ability to derive knowledge and develop new insight from gathered information to postulate its significance.  Prediction – The ability to describe the anticipated future state of the operational/physical environment based on the depiction of past and current information.  Product Generation – The ability to develop and tailor intelligence, information, and environmental content and products per customer requirements.  (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)\nFamilies in this line: GEOINT ANALYSIS, SIGINT ANALYSIS, HUMINT ANALYSIS, MASINT/AGI ANALYSIS, and TARGETING SUPPORT.",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0347',
  "TagValue_Number":"3.3.6.7",
  "TagValue_Service Name":"BDA/CDA",
  "TagNotes_Service Definition":"This component performs Battle Damage Assessment (BDA) analysis. BDA is the estimate of damage resulting from the application of lethal or nonlethal military force. BDA is composed of physical damage assessment, functional damage assessment, and target system assessment.  \n\nGCCS Spec/USMTF message",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"7.2.8 Transmit Tasking and Target Data to Battle Damage Assessment (BDA) Assets\n7.2.51 Transmit Battle Damage Assessment (BDA) Report",
  "TagValue_JARM/ESL Alignment":"8.15.06 Target Planning",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0346',
  "TagValue_Number":"3.3.6.6",
  "TagValue_Service Name":"Target Mensuration",
  "TagNotes_Service Definition":"Target Mensuration services provide the measurement of a feature or location on the earth to determine an absolute latitude, longitude, and height.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"14.1.23 Perform Imagery Coordinate Mensuration",
  "TagValue_JARM/ESL Alignment":"8.15.06 Target Planning",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0086',
  "TagValue_Number":"3.3.6.5",
  "TagValue_Service Name":"Target List",
  "TagNotes_Service Definition":"The Target List service maintains the various target lists that are kept as part of the target strike management process.",
  "TagNotes_Service Description":"The Target List service provides methods to retrieve a prioritized list of target lists by type; query for an existing list by name; create or remove target lists;  and add, remove, or edit targets in existing target lists.  Targets can also be transferred from one target list to another list type in accordance with the target management process.\n\nExamples of target list types include: 1) the Joint Integrated Prioritized Target List (JIPTL) which are targets prioritized to be struck; 2) the Air Tasking Order (ATO); 3) on-call targets; 4) no-strike targets; 5) nominated targets; and 6) targets scheduled for battle damage assessment (BDA).",
"TagValue_Example Specification":"3.1.2.8",
"TagValue_Example Solution":"N/A",
"TagValue_DI2E Framework Status":"4",
"TagValue_DCGS Enterprise Status":"N/A",
"TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
"TagNotes_JCSFL Alignment":"4.7.2  Manage Target List",
"TagValue_JARM/ESL Alignment":"8.15.06 Target Planning",
"TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0085',
  "TagValue_Number":"3.3.6.4",
  "TagValue_Service Name":"Target Folder",
  "TagNotes_Service Definition":"The Target Folder service maintains a softcopy folder containing target intelligence and related materials prepared for planning and executing action against a specific target.",
  "TagNotes_Service Description":"The Target Folder service maintains a collection of information related to the planning and execution of a strike against a target.  The target folder service serves as a portal for displaying and retrieving data and information that has been placed in the folder.  It also enables analysts to add their inputs or new data to the folder. \n\nExamples of information maintained in a target folder are georegistered target images, lists of designated mean impact points (DMPI) that have been extracted from image data, collateral information about the target's activities and known functions, or no-strike information (such as the location of a place of worship or a civilian shelter within the target complex).",
  "TagValue_Example Specification":"3.1.2.8",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"14.1.34 Publish Target Model",
  "TagValue_JARM/ESL Alignment":"8.15.06 Target Planning",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0084',
  "TagValue_Number":"3.3.6.3",
  "TagValue_Service Name":"Target Validation",
  "TagNotes_Service Definition":"Target Validation portrays and locates target services, indicates target vulnerabilities, and maintains relative target importance.",
  "TagNotes_Service Description":"Target Validation maintains information about targets that is used to maintenance the target data matrix and provide input to other target support services by presenting and locates known functions and vulnerabilities of a target or target complex including current operational status of targets.  It also provides information that helps establish the strike prioritization of a target.\n\nFor example, a target may have been serving as a command and control communications hub in prior weeks but may have recently been restored to some more benign function, making it undesirable as a high priority target.  The service also may denote no-strike regions in a target complex if the target has multiple functions and if certain of those functions are best preserved.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.3 Interpretation (AP)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.15.06 Target Planning",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0083',
  "TagValue_Number":"3.3.6.2",
  "TagValue_Service Name":"Target Data Matrix",
  "TagNotes_Service Definition":"Target Data Matrix provides the current status of targets within a given area of responsibility (AOR).",
  "TagNotes_Service Description":"Target Data Matrix displays the status of targets as they progress from target nomination lists to target battle damage assessment lists.  It also generates reports on current target status (i.e. destroyed, recently nominated, current operational status, scheduled for strike, etc. ) within an Area of Responsibility (AOR).   Both these operations are important for managing targeting operations during joint strike operations.\n\nTo perform these operations Target Data Matrix uses target lists and target folders.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"2.1.39  Acquire and Track Target\n3.4.1  Display Automatic Target Recognition (ATR)",
  "TagValue_JARM/ESL Alignment":"8.15.06 Target Planning",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0082',
  "TagValue_Number":"3.3.6.1",
  "TagValue_Service Name":"Target Management",
  "TagNotes_Service Definition":"Target Management compiles and reports target information and provides a dissemination service for filtering, combining, and passing that information to higher, adjacent, and subordinate commanders.",
  "TagNotes_Service Description":"Target Management provides querying and reporting on target locations, battle damage assessments (BDA), weapon target pairing, and other related topics using target lists, the joint target matrix, and other related target files.  Reports can be customized, filtered and/or summarized as they are disseminated upward and downward through the chain of command.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"7.2.9 Transmit Tasking and Target Data\n14.1.31 Maintain Target Model Library\n14.1.32 Retrieve Target Model\n14.1.33 Distribute Target Model",
  "TagValue_JARM/ESL Alignment":"8.15.06 Target Planning",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0057',
  "TagValue_Number":"3.3.5",
  "TagValue_Service Name":"MASINT Processing",
  "TagNotes_Service Definition":"MASINT Processing services perform qualitative analysis of data (metric, angle, spatial, wavelength, time dependence, modulation, plasma, and hydro magnetic) derived from specific technical sensors for the purpose of identifying any distinctive features associated with the source, emitter, or sender and to facilitate subsequent identification and/or measurement of the same.",
  "TagNotes_Service Description":"MASINT processing services include specialized SAR image formation algorithms needed to assist in the creation of SAR MASINT products.  In particular, SAR MASINT often makes use of SAR complex image pairs to form interferometric products such as coherent change detection (CCD) or elevation change detection products.  In order for SAR complex images to be suitable for this sort of use they need to be formed in such a manner that they share near identical apertures in frequency space.  A MASINT processing service is often invoked to do SAR complex image pair formation in such a manner that a common frequency aperture is employed.\n\nSpecial condition image formation is only one example of a SAR MASINT processing service.  Other SAR MASINT processing of a different type is crucial to producing other types of SAR MASINT products.\n\nSimilarly, MASINT processing services are used to generate certain hyperspectral MASINT products from raw hyperspectral collections.  Examples include specialized methods of compensating for atmospherics as an initial step in material signature detection processing, etc.  Yet other MASINT processing services are involved in thermal infrared MASINT product formation.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"14.1.3 Produce Measurement and Signature Intelligence (MASINT) Data",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0056',
  "TagValue_Number":"3.3.4.1",
  "TagValue_Service Name":"Language Translation",
  "TagNotes_Service Definition":"Language Translation renders the meaning within a file from a source language and provides an equivalent version of that file in a user selected target second language.",
  "TagNotes_Service Description":"Language Translation takes text, a document, or a URL as input along with a specification of the original and destination language.  It then translates the material into the destination language.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"2.3.1 Data Transformation (PE)",
  "TagNotes_JCSFL Alignment":"7.1.50 Translate Foreign Languages",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0345',
  "TagValue_Number":"3.3.4",
  "TagValue_Service Name":"Data Exploitation",
  "TagNotes_Service Definition":"The family of services to exploit data sources, to include language translation.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0344',
  "TagValue_Number":"3.3.3.1",
  "TagValue_Service Name":"Source Management",
  "TagNotes_Service Definition":"The service that supports identification, coordination, and protection of HUMINT resources.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2.2 Develop Knowledge and Situational Awareness",
  "TagNotes_JCSFL Alignment":"14.1.14 Manage Human Intelligence (HUMINT) Dossier\n14.1.15 Manage Human Intelligence (HUMINT) Sources",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0055',
  "TagValue_Number":"3.3.3",
  "TagValue_Service Name":"CI/HUMINT Processing",
  "TagNotes_Service Definition":"CI/HUMINT Processing provides the intelligence derived from the collection discipline in which the human being is the primary collection instrument and can be both a source and collector.",
  "TagNotes_Service Description":"CI/HUMINT Processing involves primarily document language translation and automatic extraction of document metadata (e.g. place names, people, organizations, times, and key actions or relationships). \n\nIt also encompasses services that mine the internet or other repositories for documents pertaining to selected topics (though this service also falls under OSINT when the repository is in the public domain).  This latter service type is the equivalent of an automatic \"clipping service\".  Similarly, specialized RSS feeds could be considered CI/HUMINT processing services.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0054',
  "TagValue_Number":"3.3.2.1",
  "TagValue_Service Name":"Signal Pattern Recognition",
  "TagNotes_Service Definition":"Signal Pattern Recognition provides signal pattern recognition in order to detect signal characteristics that tell analysts information about the emitting source.",
  "TagNotes_Service Description":"Signal Pattern Recognition characterizes a signal based on key signal parameter values and then accesses a database of signal patterns to determine if they match known signal characteristics.   When matches are found, the signal is labeled as belonging to a specific type, piece, or mode of equipment or might indicate transmission protocols.\n\nSignal characteristics might include signal carrier radio frequency, bandwidth, pulse duration(s) and repetition interval(s), dwell or scanning time, polarization, or pulse envelope characteristics (pulse rise and fall times, shape characteristics (e.g. linear chirp or triangular shaped pulse), frequency or phase modulation, and time bandwidth products).",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.3.2 Information Categorization (PE)",
  "TagNotes_JCSFL Alignment":"1.1.1 Receive and Process Signals\n1.1.25 Pre-Process Sensor Data\n14.1.1 Produce Signals Intelligence (SIGINT) Data\n14.1.29 Validate Processed Sensor Data",
  "TagValue_JARM/ESL Alignment":"8.15.02 Signals Processing",
  "TagNotes_Comments":"The NeXTmidas tools are at http://nextmidas.techma.com/"
},
{
  "TagValue_UID":'0051',
  "TagValue_Number":"3.3.2",
  "TagValue_Service Name":"SIGINT Processing",
  "TagNotes_Service Definition":"SIGINT Processing services provide processing of raw signals data to form basic SIGINT products.",
  "TagNotes_Service Description":"SIGINT processing includes services used to filter, screen, resample, and automatically detect certain basic signal patterns for the purpose of characterizing raw input signals and assigning metadata, prior to exploitation.  This includes application of Fast Fourier Transform (FFT) processing and filters that are applied to the resulting frequency domain products.  SIGINT processing can also include specific convolution and noise reduction services used to clean up raw collected signal.\n\nAnother aspect of SIGINT processing involves very high volume data ingest services, data formatting, automatic dissemination of select snippets of processed SIGINT data, and certain types of near real time collection command and control.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0362',
  "TagValue_Number":"3.3.1.5",
  "TagValue_Service Name":"Image Chipping",
  "TagNotes_Service Definition":"Image Chipping Service creates user-defined full resolution chip from NITF (National Imagery Transmission Format) image.",
  "TagNotes_Service Description":"Image Chipping Service allows users to create an image chip from a larger image.  Imagery chips are full resolution \"What You See Is What You Get\" (WYSIWYG) from the current image view.  Users can select square/rectangle around an area of a detailed overview of an image, to create a chip, or smaller piece of the larger image.  \nThis service is needed to reduce the size of the image file and to reduce the image to just the area relevant to the work it will be used for.  Reducing the size of the file decreases the demand on the system and the network communications.  Reducing the image to eliminate unneeded portions of the image simplifies the task for the image analyst.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.3.1 Data Transformation (PE)",
  "TagNotes_JCSFL Alignment":"14.1.7 Manage Images",
  "TagValue_JARM/ESL Alignment":"8.15.01 GEOINT Processing",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0050',
  "TagValue_Number":"3.3.1.4",
  "TagValue_Service Name":"State Service",
  "TagNotes_Service Definition":"State Service converts image support data into a related sensor model state string.",
  "TagNotes_Service Description":"State Service extracts the image support data from a file and creates a sensor model state string.   Note: this sensor model state string is a necessary input for National Geospatial-Intelligence Agency (NGA) Common Geospatial Services (CGS).",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.3.1 Data Transformation (PE)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.15.01 GEOINT Processing",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0208',
  "TagValue_Number":"3.3.1.3",
  "TagValue_Service Name":"AOI Processing",
  "TagNotes_Service Definition":"AOI Processing enables utilities for adding, deleting and updating persisted user based geometrical areas of interest (AOI). This allows a user to create and manage an AOI (geometrical based).\n\nRef: GCCS-i3 (SOAP based )",
  "TagNotes_Service Description":"None",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.3.1 Data Transformation (PE)",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.15.01 GEOINT Processing",
  "TagNotes_Comments":"Need more info on this one.  Is it just something associated with NGA CGS?"
},
{
  "TagValue_UID":'0049',
  "TagValue_Number":"3.3.1.2",
  "TagValue_Service Name":"FMV Geoprocessing",
  "TagNotes_Service Definition":"FMV Processing georegisters a stream of video frames in real time..",
  "TagNotes_Service Description":"FMV georegistration is a real time processing service that takes a video stream and georegisters every frame.  The result is a continuous stream of video frames georegistered to an underlying digital elevation map and reference imagery or to Digital Point Positioning Data Base (DPPDB) reference products.  Any frame in the video stream can be used for precision targeting or other precision position extraction applications.  \n\nThe frames are also crudely mosaicked (i.e. they are all correctly geopositioned but may still have visible \"seams\" from small scene intensity changes during the collection).  These visible seams can be removed by a subsequent FMV mosaicking process that blends image intensities across frame boundaries.\n\nThe FMV georegistration service ingests a stream of FMV frames and either reference imagery with associated reference DEMs or reference DPPDB.  If the quality of the FMV collection metadata is poor (such as is common from small UAVs or mini UAVs), the FMV georegistration service can optionally ingest point correspondences between the 1st frame in the FMV stream and the reference imagery in order to initiate the georegistration process.\n\nOnce the process starts, the service updates the FMV collection metadata for every frame based on a Kalman filter estimation of sensor position and attitude that is augmented by periodic automatic extractions of matching point correspondences between selected frames and the reference imagery.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"2.3.1 Data Transformation (PE)",
  "TagNotes_JCSFL Alignment":"14.1.5 Collect and Process Video Sensor Data",
  "TagValue_JARM/ESL Alignment":"7.03.08 Video Processing Services",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0048',
  "TagValue_Number":"3.3.1.1",
  "TagValue_Service Name":"Image Rectification",
  "TagNotes_Service Definition":"Image Rectification provides transformations to project two or more images onto a common image or geocoordinate plane.",
  "TagNotes_Service Description":"Rectification is a process of geometrically correcting an image so that it can be represented on a planar surface with a standard coordinate system.  This is often done to make an image geometrically conform to other images acquired from other perspectives.  This supports applications such as image/video mosaicking (combining multiple photographic images with overlapping fields of view to produce a larger, apparently seamless image), change detection, and multisensor data fusion.\n\nIt is also done to make an image or multiple images conform to a map, often for geographic information system (GIS) purposes. A common method for this service is to associate geolocations with pixels on an image.  The image pixels are then resampled to place them in into a common map coordinate system. \n\nAnother important application in which a rectification service gets used is in performing an epipolar rectification to ensure that corresponding match points in a pair of stereo images are displaced only along a single axis.  This enables easier matching and analysis of corresponding points in stereo image pairs by reducing the pixel stereo offsets to lie entirely along a single preferred axis (the epipolar axis).\n\nThe Image rectification Service ingests at a minimum an image, and a description of the planar surface to be used in the rectification process.  In some instances, such as for epipolar rectification, the target planar surface is described entirely by a second image in a stereo pair.  Automatic point correspondence matching establishes the required rectification transformation.  In other instances, such as orthorectification, it is common to supply either a reference image and a corresponding digital elevation map (DEM) or a reference image pair such as is found in a Digital Point Positioning Data Base (DPPDB). The DEM is used in performing the rectification transformation to minimize unsightly image artifacts that can affect a rectification done on perspective images in cases where no information about underlying terrain is supplied.  The simplest input to a rectification service is an input image to be rectified with image support data and a specification of a horizontal plane surface.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.3.1 Data Transformation (PE)",
  "TagNotes_JCSFL Alignment":"1.1.22 Register Images\n8.4.21 Process Imagery\n14.1.8  Rectify Image\n14.1.22  Correlate Image",
  "TagValue_JARM/ESL Alignment":"7.03.04 Imagery Processing Services",
  "TagNotes_Comments":"Can be provided as an app, a war file, and a library"
},
{
  "TagValue_UID":'0047',
  "TagValue_Number":"3.3.1",
  "TagValue_Service Name":"GEOINT Processing",
  "TagNotes_Service Definition":"GEOINT Processing provides processing of raw imagery and geospatial data to form basic GEOINT products.",
  "TagNotes_Service Description":"The GeoINT processing family provides processing of imagery and  raw geospatial data to form basic GEOINT products.  Examples include forming images from raw source data, image format conversions, image compression,  basic video processing such as mosaicking, etc.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0046',
  "TagValue_Number":"3.3",
  "TagValue_Service Name":"Processing and Exploitation",
  "TagNotes_Service Definition":"The Processing/Exploitation line includes services that provide the ability to transform collected information into forms suitable for further analysis and/or action by man or machine.",
  "TagNotes_Service Description":"The Processing/Exploitation line includes services that provide the ability to transform collected information into forms suitable for further analysis and/or action by man or machine.  This includes the following sub-areas: Data Transformation – The ability to select, focus, simplify, tag and transform overtly or covertly collected data into human or machine interpretable form for further analysis or other action.  Information Categorization – The ability to identify, classify and verify information associated with time sensitive objectives enabling further analysis or action.  (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)\nFamilies in this line: GEOINT PROCESSING, SIGINT PROCESSING, CI/HUMINT PROCESSING, and MASINT PROCESSING.",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0364',
  "TagValue_Number":"3.1.6.2",
  "TagValue_Service Name":"Sensor Planning",
  "TagNotes_Service Definition":"Sensor Planning Service provides an interface for determining the capabilities and tasking of SPS-registered DI2E sensors.",
  "TagNotes_Service Description":"The Sensor Planning Service (SPS) provides a standardized interface for interoperable sensor tasking.  The SPS provides the client support for determining the capabilities of the sensor system, identifying the feasibility of a tasking request, accepting commands to task the sensor, tracking the status of sensor commands, and provide a reference to the observation data following the completion of a successful task.  All of which combine to obtain varying stages of planning, scheduling, tasking, collection, processing, archiving, and distribution of resulting observation data and information that is the result of a request.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"4",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.7   Generate National Intelligence, Surveillance, and Reconnaissance (ISR) Sensor Tasking\n1.4.10   Generate Sensor Coverage",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0039',
  "TagValue_Number":"3.1.6.1",
  "TagValue_Service Name":"Sensor Observation",
  "TagNotes_Service Definition":"Sensor Observation provides Observation data and Sensor descriptions for SOS-registered DI2E sensors.",
  "TagNotes_Service Description":"The Sensor Observation Service (SOS) provides a standardized interface for managing and retrieving metadata and observations from heterogeneous sensor systems.  It enables discovery and retrieval of real time or archived sensor descriptions, observations, and features of interest.  It allows the client to insert and delete sensor data.  The SOS also supports the functionality to list the capabilities of all registered sensors.  \n\n[This service is supported by the OGC SWE SOS 2.0 specification.]",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"4",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.17  Determine Time to Sensor Availability\n1.4.18  Identify Elapsing Platform and Sensor Tasking",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"OGC SOS Spec"
},
{
  "TagValue_UID":'0363',
  "TagValue_Number":"3.1.6",
  "TagValue_Service Name":"Sensor Web Enablement",
  "TagNotes_Service Definition":"The Sensor Web Enablement (SWE) family of services provides  observation data and sensor descriptions and well as an  interface for tasking of SPS-registered DI2E sensors.",
  "TagNotes_Service Description":"The Sensor Web Enablement (SWE) family of services provides  a standardized interface for managing and retrieving metadata and observations from heterogeneous sensor systems including discovery and retrieval of real time or archived sensor descriptions, observations, and features of interest.    They also provide a standardized interface for interoperable sensor tasking, sensor capabilities determination, tasking feasibility assessment, tasking request, sensor command tracking, and observation data following the completion of a successful task.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"Family",
  "TagValue_DCGS Enterprise Status":"Family",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.35 Identify Current Platform and Sensor Tasking\n1.4.33 Respond to Satellite Access Authorization\n1.4.34 Conduct Sensor Registration and Calibration\n1.4.20 Determine Platform and Sensor Status",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"OGC SWE spec"
},
{
  "TagValue_UID":'0339',
  "TagValue_Number":"3.1.5.2",
  "TagValue_Service Name":"Task Asset Request",
  "TagNotes_Service Definition":"Task Asset Request provides collection and sensor deployment plans.   Changes to these plans are sent to the sensor/platform through this interface. \n\nRef. DCGS-Army chicklit definition",
  "TagNotes_Service Description":"Task Asset Request serves as the interface that all services use to send sensor messages to the Sensor Provisioning service.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.15 Process Source Requirements\n1.4.16 Determine Availability of Joint Intelligence, Surveillance, and Reconnaissance (JISR) Capabilities\n1.4.17 Determine Time to Sensor Availability\n1.4.18 Identify Elapsing Platform and Sensor Tasking\n1.4.19 Direct and Monitor Sensor Employment\n3.5.17 Process Asset Specific Information Query",
  "TagValue_JARM/ESL Alignment":"8.15.04 Intelligence Asset Tasking",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0042',
  "TagValue_Number":"3.1.5.1",
  "TagValue_Service Name":"Tasking Message Preparation",
  "TagNotes_Service Definition":"Tasking Message Preparation formats the tasking information of the Collection Requirements Planning service for dissemination.",
  "TagNotes_Service Description":"Tasking Message Preparation allows client to request reformatting of the collection requirement and tasking assignments into a distributable format based on a specified day, sensor group, asset, or requirement set.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.9 Generate Theater and External Intelligence, Surveillance, and Reconnaissance (ISR) Sensor Tasking\n1.4.14 Generate Sensor Configuration Commands",
  "TagValue_JARM/ESL Alignment":"8.15.04 Intelligence Asset Tasking",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0338',
  "TagValue_Number":"3.1.5",
  "TagValue_Service Name":"Tasking Request",
  "TagNotes_Service Definition":"The family of services that support tasking requests",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0105',
  "TagValue_Number":"3.1.4.2",
  "TagValue_Service Name":"Asset Discovery",
  "TagNotes_Service Definition":"Asset Discovery publishes and finds information on DI2E connected devices such as  sensors, network hosted IT equipment, and weapons systems.",
  "TagNotes_Service Description":"Asset Discovery services maintain a repository of potentially available DI2E assets. Operations typically include Create, Read, Update, Delete (CRUD) type operations on the asset repository database(s) (with appropriate permissions) as well as operations for asset search and asset search results reporting.   Examples of typical asset information might include the asset name, location, operational status, various service descriptors, or anticipated shut down dates (many other fields of data are possible). One example of use is a  analyst might use this service within an application that provides intelligence on the assets that might be available to be tasked due to its services and current geographical location.",
  "TagValue_Example Specification":"ISO/IEC 19770 Software Asset Management (SAM) \n   - ISO/IEC 19770-1 - process framework \n   - ISO/IEC 19770-2 - data standard\n   - ISO/IEC 19770-3 - software licensing entitlement tags",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.3.1.1 Network Resource Visibility",
  "TagNotes_JCSFL Alignment":"1.3 Perform Detection (Group)\n1.4.22 Calculate Sensor Error or Uncertainty\n3.5.14  Confirm Asset or Sensor Availability\n3.5.16  Identify and Catalog Joint Assets and Activities\n3.5.17  Process Asset Specific Information Query\n5.4.5  Determine Operational and Tactical Assets\n8.1.78 - Query Global Directory Service \n8.7.53  Access Subject Matter Expert and Essential Information\n8.7.55  Establish Discovery Catalogs\n8.9.7  Manage Enterprise Information Assets",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0044',
  "TagValue_Number":"3.1.4.1",
  "TagValue_Service Name":"Asset Status Summary",
  "TagNotes_Service Definition":"Asset Status Summary provides aggregated asset status metadata (including sensors and sets of sensors) across the enterprise.",
  "TagNotes_Service Description":"Asset Status Summary allows the client to query the status of a specific sensor, a set of sensors, or all sensors across the enterprise.    Results of status summary queries enable reporting of actively monitored assets across the ISR spectrum, regardless of source.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.16Determine Availability of Joint Intelligence, Surveillance, and Reconnaissance (JISR) Capabilities",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0337',
  "TagValue_Number":"3.1.4",
  "TagValue_Service Name":"Asset Reporting",
  "TagNotes_Service Definition":"The Asset Reporting family provides services that support discovery, status, and the tasks assigned to intelligence assets.",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0335',
  "TagValue_Number":"3.1.3.5",
  "TagValue_Service Name":"Target Planning",
  "TagNotes_Service Definition":"Target Planning is the systematic examination of potential target systems-and their components, individual targets, and even elements of targets-to determine the necessary type and duration of the action that must be exerted on each target to create an effect that is consistent with the commander's specific objectives. (JP 1-02. SOURCE: JP 3-60)\n",
  "TagNotes_Service Description":"Target Planning services support the Target Development process by enabling target system analysis; entity-level target development; and target list management (TLM). \n \nTarget Development is defined as: The systematic examination of potential target systems-and their components, individual targets, and even elements of targets-to determine the necessary type and duration of the action that must be exerted on each target to create an effect that is consistent with the commander's specific objectives. (JP 1-02. SOURCE: JP 3-60)",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"N/A",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"4.7 Analyze Targets (Group)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0043',
  "TagValue_Number":"3.1.3.4",
  "TagValue_Service Name":"Exploitation Planning",
  "TagNotes_Service Definition":"Exploitation Planning tracks analyst skills and availabilities to identify who will do the sensor output analysis and assigns it to them.",
  "TagNotes_Service Description":"Exploitation Planning allows Analyst Managers to view, create, update, or delete information about a specific analyst.  Analyst Taskers can view, create, update, or delete tasks associated with specific analyst.     Completed, outstanding, or in-work tasks can be viewed by analyst, unit of time (day, week, etc.), or collection requirement.     Assignment Generators evaluate necessary skills, determine appropriate analysts with available time, and assign tasks to the best fitting analyst(s).",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.1.2 Develop Strategies (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.25 Correlate Joint Intelligence, Surveillance, and Reconnaissance (JISR) Resources\n1.4.26 Manage Joint Intelligence, Surveillance, and Reconnaissance (JISR) Resources\n1.4.31 Process Intelligence, Surveillance, and Reconnaissance (ISR) Support Requests",
  "TagValue_JARM/ESL Alignment":"Not Mapped",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0334',
  "TagValue_Number":"3.1.3.3",
  "TagValue_Service Name":"Intelligence Source Selection",
  "TagNotes_Service Definition":"Intelligence Source Selection services support the review of mission requirements for sensor and target range, system responsiveness, timelines, threat, weather, and reporting requirements.   to identify and determine asset and/or resource availability and capability.  Source selection applies to all types of collection sources (i.e. GEOINT, HUMINT, SIGINT, etc.).",
  "TagNotes_Service Description":"none",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.5 Manage Sensors\n1.4.13 Generate Sensor Tasking\n1.4.21 Assess Sensor Performance\n1.4.24 Recommend Unattended Ground Sensor (UGS) Emplacement",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0035',
  "TagValue_Number":"3.1.3.2",
  "TagValue_Service Name":"Sensor Cataloging",
  "TagNotes_Service Definition":"Sensor Cataloging allows clients to request information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.",
  "TagNotes_Service Description":"Sensor Cataloging allows clients to request information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.    The client can receive metadata documents describing a server's abilities, search for sensors instances on a registry server instance, and receive sensor instance descriptions from the server for either one or multiple sensors.       \n\nThe client can also insert sensor metadata into the registry server,  update sensor metadata on the registry server, or remove sensor metadata from the registry server.   In addition, the client can establish or remove a link between a sensor registry entry instance and an external OGC catalog.   Finally the client can get the sensor status, insert a sensor status, subscribe to a sensor status, or cancel a sensor status subscription.              \n\n[This service is supported by the OGC SWE SIR discussion paper.]",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.11  Maintain Sensor Configurations\n1.4.12   Characterize Sensors Capabilities and Limitations\n3.5.19  Display and Monitor Sensor Assets",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0041',
  "TagValue_Number":"3.1.3.1",
  "TagValue_Service Name":"Collection Requirements Planning",
  "TagNotes_Service Definition":"Collection Requirements Planning provides editing and visibility into the evolution of tasking and collection-related artifacts",
  "TagNotes_Service Description":"Collection Requirements Planning aggregates information such as asset tasking lists as they mature into ISR Synch Matrix and Proposed Collection Plans.    Using this service collection requirements managers can view, add, delete, or update requirements for a specific collection.  It also allows them to view the mapping of collection requirement to collection task.    It also allows tasking managers to view, add, delete, or update tasking for a specific sensor.\n\nBoth tasking requirements managers and task managers can view mapping of the collection tasks to collection requirements, collection results, and status of collection tasks.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"2.1.3 Task and Monitor Resources (P&D)",
  "TagNotes_JCSFL Alignment":"1.4.3 Identify Current Platform and Sensor Tasking\n1.4.7   Generate National Intelligence, Surveillance, and Reconnaissance (ISR) Sensor Tasking\n1.4.10   Generate Sensor Coverage",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0040',
  "TagValue_Number":"3.1.3",
  "TagValue_Service Name":"Planning",
  "TagNotes_Service Definition":"The family of services that support the Intelligence Planning Process.",
  "TagNotes_Service Description":"Operation planning occurs in a networked, collaborative environment, which requires iterative dialogue among senior leaders, concurrent and parallel plan development, and collaboration across multiple planning levels. The focus is on developing plans that contain a variety of viable, embedded options (branches and sequels).  This facilitates responsive plan development and modification, resulting in “living” plans (i.e., the systematic, on-demand, creation and revision of executable plans, with up-to-date options, as circumstances require). This type of adaptive planning also promotes greater involvement with other US agencies and multinational partners.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"There exist various services from NRO that give insight into current taskings.  This supports the planning process, but is not a direct planning tool - rather is input to the planning tool"
},
{
  "TagValue_UID":'0333',
  "TagValue_Number":"3.1.1.2",
  "TagValue_Service Name":"RFI Management",
  "TagNotes_Service Definition":"The service to create, deconflict, monitor, and disseminate Intelligence Requests for Information (RFI)",
  "TagNotes_Service Description":"An RFI is a specific time-sensitive ad hoc requirement for information or intelligence\nproducts, and is distinct from standing requirements or scheduled intelligence production. An RFI can\nbe initiated at any level of command, and will be validated in accordance with the combatant command’s\nprocedures. An RFI will lead to either a production requirement if the request can be answered with\ninformation on hand or a collection requirement if the request demands collection of new information.\nCollection planning and requirement management are major activities during planning and direction.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2 Enterprise Services (ES)",
  "TagNotes_JCSFL Alignment":"8.7.49 Submit Information Requirements\n8.7.87 Validate Request for Information (RFI)\n8.7.101 Track Requests for Information (RFIs)\n8.7.102 Respond or Reply to Request for Information (RFI)\n8.7.103 Receive Request for Information (RFI)\n8.7.113 Receive Requests for Information (RFI) Results",
  "TagValue_JARM/ESL Alignment":"7.07.01 Intelligence Requirements Management",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0332',
  "TagValue_Number":"3.1.1.1",
  "TagValue_Service Name":"PIR Management",
  "TagNotes_Service Definition":"PIR Management services support management of the Priority Intelligence Requirement (PIR) process .  Supports: \n1. Deriving PIRs from CCIRs\n2. Consolidating and recommending PIR nominations\n3. Deriving Information Requirements from Intelligence Requirements and PIRs\n4. Identifying EEIs which will answer PIRs",
  "TagNotes_Service Description":"PIR Management services assist a commander in deriving Priority Intelligence Requirements (PIR) to support a mission, tracking, updating, consolidating existing PIRs, and turning those PIRs into specific requirements that can be used for intelligence collection planning.  The PIR Management service works with the Collection Requirements Planning service to task and plan collection activities.",
  "TagValue_Example Specification":"N/A",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2 Enterprise Services (ES)",
  "TagNotes_JCSFL Alignment":"8.7.49 Submit Information Requirements\n8.7.87 Validate Request for Information (RFI)\n8.7.101 Track Requests for Information (RFIs)\n8.7.102 Respond or Reply to Request for Information (RFI)\n8.7.103 Receive Request for Information (RFI)\n8.7.113 Receive Requests for Information (RFI) Results\n14.1.17 Evaluate Collection Results\n14.1.18 Evaluate Intelligence Collection, Products, Reporting\n14.1.19 Identify, Prioritize and Validate Intelligence Requirements",
  "TagValue_JARM/ESL Alignment":"7.02.01 Collection Requirements Management",
  "TagNotes_Comments":"None"
},
{
  "TagValue_UID":'0331',
  "TagValue_Number":"3.1.1",
  "TagValue_Service Name":"Define and Prioritize Requirements",
  "TagNotes_Service Definition":"The family of services to define, document, and prioritize intelligence requirements",
  "TagNotes_Service Description":"N/A",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0032',
  "TagValue_Number":"3.1",
  "TagValue_Service Name":"Planning and Direction",
  "TagNotes_Service Definition":"The Planning & Direction line includes services that provide the ability to synchronize and integrate the activities of collection, processing, exploitation, analysis and dissemination resources to meet BA information requirements.",
  "TagNotes_Service Description":"The Planning & Direction line includes services that provide the ability to synchronize and integrate the activities of collection, processing, exploitation, analysis and dissemination resources to meet BA information requirements. This includes the following sub-areas: Define and Prioritize Requirements  – The ability to translate national through tactical objectives and needs into specific information and operational requirements.  Develop Strategies  – The ability to determine the best approach to collect, process, exploit, analyze, and disseminate data and information to address requirements and predict outcomes. Task and Monitor Resources  – The ability to task, track, direct, and adjust BA operations and their associated resources to fulfill requirements. Evaluation  – The ability to assess the results of BA operations and products to ensure that user requirements are being met. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)\nFamilies in this line: ASSET MANAGEMENT and PLANNING and REPORTING.",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"N/A",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0001',
  "TagValue_Number":"0",
  "TagValue_Service Name":"DI2E",
  "TagNotes_Service Definition":"The DI2E SvcV-4 documents services that are needed for the DI2E to achieve the desired level of service reuse and interoperability .",
  "TagNotes_Service Description":"The services that exist within the Defense Intelligence Enterprise that are governed by the DI2E.  The services shall be developed in a service oriented manner, be registered and accessible in the Enterprise Registry, and be tested and certified for reuse.",
  "TagValue_Example Specification":"All Layers",
  "TagValue_Example Solution":"All Layers",
  "TagValue_DI2E Framework Status":"All Layers",
  "TagValue_DCGS Enterprise Status":"All Layers",
  "TagValue_JCA Alignment":"All Layers",
  "TagNotes_JCSFL Alignment":"All Layers",
  "TagValue_JARM/ESL Alignment":"All Layers",
  "TagNotes_Comments":"All Layers"
},
{
  "TagValue_UID":'0161',
  "TagValue_Number":"1",
  "TagValue_Service Name":"Infrastructure Services",
  "TagNotes_Service Definition":"The functions/services that support the Enterprise and don't usually have a direct relationship to the mission or business processes.",
  "TagNotes_Service Description":"Infrastructure Services are fundamental to the DI2E.  This consistent set of services across the DI2E is foundational to future SOA-based enterprise services.     \n\nSpecific service lines include:\n\n   • Enterprise Management\n   • Web Service Security\n   • Web Service Discovery\n   • Messaging, and \n   • Orchestration",
  "TagValue_Example Specification":"Layer",
  "TagValue_Example Solution":"Layer",
  "TagValue_DI2E Framework Status":"Layer",
  "TagValue_DCGS Enterprise Status":"Layer",
  "TagValue_JCA Alignment":"Layer",
  "TagNotes_JCSFL Alignment":"Layer",
  "TagValue_JARM/ESL Alignment":"Layer",
  "TagNotes_Comments":"Layer"
},
{
  "TagValue_UID":'0202',
  "TagValue_Number":"1.4.2.2",
  "TagValue_Service Name":"Protocol Mediation",
  "TagNotes_Service Definition":"Protocol Mediation provides the ability to manage service communication format between sender and receiver.",
  "TagNotes_Service Description":"The Protocol Mediation service provides transformation and processing of service communication between sending and receiving services.  One example is mediating e-mail messages between POP to IMAP formats.    Another example is chat mediation between IRC or XMPP formats. Mediation between SOAP and REST is another example.",
  "TagValue_Example Specification":"Business Process Execution Language(BPEL)",
  "TagValue_Example Solution":"Oracle BPM suite; Bonitasoft\n\nDDF, Apache Camel, Apache ServiceMix, Apache Geronimo",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.9.5 Integrate Enterprise Applications",
  "TagValue_JARM/ESL Alignment":"6.05.04 Orchestration Services",
  "TagNotes_Comments":"Specifications include all of the protocols that need to  be mediated,  BPEL is not one of them (see column H)."
},
{
  "TagValue_UID":'0201',
  "TagValue_Number":"1.4.2.1",
  "TagValue_Service Name":"Execution Engine",
  "TagNotes_Service Definition":"An execution engine takes the definition of a service orchestration (e.g., BPEL) and executes the process",
  "TagNotes_Service Description":"Service orchestrations are only definitions of how services should be executed in sequence to complete a given business process. Execution of the orchestration requires some form of engine that will run the orchestrated process and return a result to the user. Most existing execution engines execute processes defined in Business Process Execution Language (BPEL). Other possibilities are available, including newer version of Business  Process Modeling Notation that are executable; mashup frameworks, and similar technologies",
  "TagValue_Example Specification":"• BPMN v2.0\n• Business Process Execution Language(BPEL)",
  "TagValue_Example Solution":"• Oracle BPM suite\n• Bonitasoft",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.9.5 Integrate Enterprise Applications",
  "TagValue_JARM/ESL Alignment":"6.05.04 Orchestration Services",
  "TagNotes_Comments":"Various open source tools exist.  Also, such engines are generally included in ESBs.  Google \"Open Source Orchestration  Engine\" to find lists of tools.  I would recommend a BPMN tool vice a BPEL tool, as BPEL is really limited to SOAP services, and it is the process model that is more important for interoperability (across engines) than the BPEL scripts."
},
{
  "TagValue_UID":'0200',
  "TagValue_Number":"1.4.2",
  "TagValue_Service Name":"Orchestration Execution",
  "TagNotes_Service Definition":"Orchestration execution performs the ordered calling of services as designed in service orchestration models (see orchestration modeling service).",
  "TagNotes_Service Description":"Orchestration execution makes in-sequence calls to services, holds intermediate inputs/outputs, handles exceptions, monitors execution status, and provides an interface for user presentation. \n\nCommon orchestration execution features include: W3C XHTML/CSS conformance, templating, class autoloading, object based design, nested groups with multiple assigned permissions, form design tools including help and error messages, labels, validation rules, inherited authorization, and macros support.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0199',
  "TagValue_Number":"1.4.1.2",
  "TagValue_Service Name":"Optimization",
  "TagNotes_Service Definition":"Optimization services provide analysis of the possible orchestrations and offer recommended alternatives to ensure the user's needs are best met from among the available services and orchestrations.",
  "TagNotes_Service Description":"For any given business process, there may be several possible orchestrations to choose from. These orchestrations may differ only in the instances of specific services that they use (e.g., the same service offered at different endpoints) or they may perform the same process using completely different service offerings. In a dynamic network where connectivity and QoS will vary widely over time, selecting the best orchestration from among the available options will be a complex task.\n\nAdditionally, in an environment where new services or new service instances may appear on the network frequently, it may be necessary to frequently update the orchestration to best meet the current situation.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"TBD",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.9.5 Integrate Enterprise Applications",
  "TagValue_JARM/ESL Alignment":"6.05.04 Orchestration Services",
  "TagNotes_Comments":"This looks like a combination of auto-discovery and real-time continuous modeling.  I don't believe anything exists for this yet, especially across a WAN.  This is more in the realm of research today."
},
{
  "TagValue_UID":'0198',
  "TagValue_Number":"1.4.1.1",
  "TagValue_Service Name":"Matchmaking",
  "TagNotes_Service Definition":"Matchmaking service provides a capability to verify that an individual service performs a desired task and that two or more services can be orchestrated by ensuring that the outputs of each service in the planned orchestration contain suitable inputs for the next service in the orchestration.",
  "TagNotes_Service Description":"Matchmaking services ensure the composability of services by comparing the function of a given service fulfills the task in the business process that the service is expected to complete. For example, if a process includes a step that converts an image file from JPG format to PNG format, any service that performs this task in the orchestration must indeed perform image format conversions.\n\nIn addition, the matchmaker service ensures that each individual service is suitable for orchestration with its predecessors and successors in the orchestration based on the inputs, outputs, preconditions, and effects (IOPEs) of each service. Put simply, in order to orchestration two services, A and B, it is necessary that Service A's outputs contain all the necessary inputs for Service B, those inputs are in the proper format, all security requirements are met, etc.",
  "TagValue_Example Specification":"• ebXML\n• OWL",
  "TagValue_Example Solution":"• Oracle Business Process Management suite\n• Bonitasoft",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.9.5 Integrate Enterprise Applications",
  "TagValue_JARM/ESL Alignment":"6.05.04 Orchestration Services",
  "TagNotes_Comments":"Combine BPMN engines with ESBs and/or message translators.. Orchestration engines should help with the mapping of output of one service to input of another.  In fact, many orchestration engines are designed to work with ESBs which perform the message translations necessary to map output of one to input of another."
},
{
  "TagValue_UID":'0197',
  "TagValue_Number":"1.4.1",
  "TagValue_Service Name":"Orchestration Planning",
  "TagNotes_Service Definition":"Service Orchestration Planning services provide support functions necessary to complete the process of converting a process model into an executable chain of services.",
  "TagNotes_Service Description":"Service Orchestration Planning services support coordinated arrangement, and management of individual services by ensuring a planning orchestration is feasible from among the available services. Orchestration differs in this sense from choreography in this sense by containing a central controller process that participating web services don't know about.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"Activiti, Intalio, ProcessMaker, BonitaSoft, Uengine",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"I think that this description best matches a BPMN based orchestration engine.  You model the orchestration in BPMN, and then the engine actually runs the process.  Recommend combining with the Services Orchestration above."
},
{
  "TagValue_UID":'0196',
  "TagValue_Number":"1.4",
  "TagValue_Service Name":"Orchestration Management",
  "TagNotes_Service Definition":"Orchestration Management provides automated SOA service as well as human operation modeling and execution.",
  "TagNotes_Service Description":"Orchestration Management services can provide automated SOA service as well as human operation modeling and execution. Currently the DI2E has mapped out services for service orchestration (modeling and execution) but is considering another family of services to cover human workflow (workflow modeling and workflow management).",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"Apache ODE, Orchestra, Intalio, os-workflow, jBPM",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Will need best practices guide.  Also don't want to limit this to BPEL and SOAP services."
},
{
  "TagValue_UID":'0326',
  "TagValue_Number":"1.3.2.3",
  "TagValue_Service Name":"Service Configuration Verification and Audit",
  "TagNotes_Service Definition":"Service Configuration Verification and Audit defines the services that perform regular checks, ensuring that the information contained in the CMS is an exact representation of the Configuration Items (CIs) actually installed in the live production environment.",
  "TagNotes_Service Description":"none",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"ER2, SDF",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2 Enterprise Services (ES)",
  "TagNotes_JCSFL Alignment":"8.1.41 Manage Services\n8.9.7 Manage Enterprise Information Assets",
  "TagValue_JARM/ESL Alignment":"8.06.03 Configuration Management",
  "TagNotes_Comments":"This belongs in enterprise management, and is a combination of continuous monitoring and VM image management (i.e., there exist mgmt tools that compare VMs with images to see if there are changes and sends alerts on the changes).  Not really a \"services mgmt\" tool as such."
},
{
  "TagValue_UID":'0325',
  "TagValue_Number":"1.3.2.2",
  "TagValue_Service Name":"Service Configuration Control",
  "TagNotes_Service Definition":"Service Configuration Control defines the services that ensure that no Service Configuration Items are added or modified without the required authorization, and that such modifications are adequately recorded in the CMS. ",
  "TagNotes_Service Description":"Service Configuration Control ensures that any changes to Configuration Items (CIs) are made by authorized parties, in accordance with the established configuration management policies, processes, and procedures.\n\nNote: Configuration Control enables the review of modifications to the Configuration Management System (CMS), to make sure the information stored in the CMS is complete and the modification was done by an authorized party. Other processes also support the objectives of Configuration Control: Configuration Identification defines who is authorized to make certain changes to the CMS. In a broader sense, Change Management and Release Management with their defined procedures also help to ensure that no unauthorized changes occur.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"DI2E-F Developer Collaboration Environment",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2 Enterprise Services (ES)",
  "TagNotes_JCSFL Alignment":"8.1.41 Manage Services\n\n8.1.72 Implement Configuration Alterations\n8.9.7 Manage Enterprise Information Assets",
  "TagValue_JARM/ESL Alignment":"8.06.03 Configuration Management",
  "TagNotes_Comments":"This is being provided by DI2E-F Developer  Collaboration Tools at the MACE.  Includes the following - for Code CM: Subversion, Git;\nBuild management/CM: Nexus, Hudson, Maven; Issue tracking/backlog mgmt: Jira\nKnowledge Mgmt: Confluence\n\nNote that developers and production environments will likely also have their own sets of tools to perform configuration management of components that are deployed to production systems."
},
{
  "TagValue_UID":'0324',
  "TagValue_Number":"1.3.2.1",
  "TagValue_Service Name":"Service Configuration Identification",
  "TagNotes_Service Definition":"Service Configuration Identification defines the services to define and maintain the underlying structure of the Configuration Management System (CMS) so that it is able to hold all information on Service Configuration Items (CIs). This includes specifying the attributes describing CI types and their sub-components, as well as determining their interrelationships.",
  "TagNotes_Service Description":"Service Configuration Identification stores information about configuration management (CM) controlled system components, called Configuration Items (CIs).  Configuration Items may be source code, compiled GOTS or COTS binaries, hardware lists, system settings or any combination thereof that make a cohesive unit and is under CM control.  Service Configuration Identification tracks specific information about each configuration item in the architecture, its sub-components, and its relationship to other CIs.  It also tracks information about who has authority to approve changes to each configuration item, such as the responsible Configuration Control Board.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"DI2E-F Developer Collaboration Environment",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2 Enterprise Services (ES)",
  "TagNotes_JCSFL Alignment":"8.1.41 Manage Services\n8.9.7 Manage Enterprise Information Assets",
  "TagValue_JARM/ESL Alignment":"8.06.03 Configuration Management",
  "TagNotes_Comments":"This is being provided by DI2E-F Developer  Collaboration Tools at the MACE.  Includes the following - for Code CM: Subversion, Git;\nBuild management/CM: Nexus, Hudson, Maven; Issue tracking/backlog mgmt: Jira\nKnowledge Mgmt: Confluence\n\nNote that developers and production environments will likely also have their own sets of tools to perform configuration management of components that are deployed to production systems."
},
{
  "TagValue_UID":'0323',
  "TagValue_Number":"1.3.2",
  "TagValue_Service Name":"Service Configuration Management",
  "TagNotes_Service Definition":"Service Configuration Management provides the set of services to provide configuration management and lifecycle support for registered services",
  "TagNotes_Service Description":"family",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0224',
  "TagValue_Number":"1.3.1.3",
  "TagValue_Service Name":"Service Publishing",
  "TagNotes_Service Definition":"Service Publishing allows service providers to publish information about available services in a metadata repository and categorize these services within related service taxonomies. Note that the repository is focused on design time lookup.",
  "TagNotes_Service Description":"The service publishing service allows service providers to publish information about themselves (service name, description, creating organization, file size, sponsoring agency, test accreditations, etc.), their service specifications (applicable standards, related conformance requirements, etc.), service offerings (versions of the service that are available), and service access points (internet URLs where the service can be reached) in a UDDI service registry.   It also allows service providers to categorize their services within related taxonomies.     Publishing limits are controlled through security protocols and related service registry publishing policies.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"jUDDI, ER2, DISA MDR\n\nER2, SDF, OMP, DI2E-F Developer Collaboration Environment",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"6",
  "TagValue_JCA Alignment":"6.2.3.7 Directory Services",
  "TagNotes_JCSFL Alignment":"8.1.59 Provide Discovery Services",
  "TagValue_JARM/ESL Alignment":"8.33.02 Service Publication",
  "TagNotes_Comments":"This should be part of the service registry."
},
{
  "TagValue_UID":'0223',
  "TagValue_Number":"1.3.1.2",
  "TagValue_Service Name":"Service Subscription",
  "TagNotes_Service Definition":"Service subscription service notifies potential service consumers of the availability of services as they become registered.",
  "TagNotes_Service Description":"The Service Subscription service allows service consumers to receive a notification when services of interest are registered in the Service Registry.   Consumers create a subscription containing service criteria that describes services they would be interested in.  When services are registered that fit the specified criteria the consumer is notified via email or a Simple Object Access Protocol (SOAP) message.   A registry must define the policy for supporting subscriptions including whether nodes may define their own policy.  In addition, policies that may be defined include restricting use of subscription, establishing additional authentication requirements, identifying the duration or life of a subscription, limiting subscriptions, and articulating exactly who can do what relative to subscriptions.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"jUDDI, ER2, DISA MDR\n\nER2, SDF, OMP, DI2E-F Developer Collaboration Environment",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"6",
  "TagValue_JCA Alignment":"6.2.3.7 Directory Services",
  "TagNotes_JCSFL Alignment":"8.1.55 Provide Subscription Services",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"This should be part of the service registry."
},
{
  "TagValue_UID":'0222',
  "TagValue_Number":"1.3.1.1",
  "TagValue_Service Name":"Service Inquiry",
  "TagNotes_Service Definition":"Service Inquiry allows consumers to query a service registry to find and retrieve service offerings.",
  "TagNotes_Service Description":"The Service Inquiry service allows service consumers to locate and obtain detail on service entries in the registry.  This includes individual users and machine-to-machine requests.   Using published service metadata (e.g., by name, category, provider, etc.), service consumers specify the criteria to be used to discover service offerings and then retrieve key service information (service name, description, URL, etc.).   The Inquiry Service also offers the service consumer a means to dynamically find service access points at runtime in order to build location transparency of services into their applications.  Related UDDI standards, which specify Hypertext Transfer Protocol Secure (HTTPS) and authorization support,  allow users and systems to publish, subscribe, and discover web services in a secure manner.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"jUDDI, ER2, DISA MDR\n\nER2, SDF, OMP, DI2E-F Developer Collaboration Environment",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"6",
  "TagValue_JCA Alignment":"6.2.3.7 Directory Services",
  "TagNotes_JCSFL Alignment":"8.1.28 Find Services\n8.1.73 Provide Directory Services",
  "TagValue_JARM/ESL Alignment":"8.33.03 Service Discovery",
  "TagNotes_Comments":"This should be the same as service registry.  See notes in service registry, as they apply here also."
},
{
  "TagValue_UID":'0221',
  "TagValue_Number":"1.3.1",
  "TagValue_Service Name":"Repository and Registry",
  "TagNotes_Service Definition":"The Repository and Registry family of services enable inquiry, subscription, and publishing, of DI2E services.",
  "TagNotes_Service Description":"The Repository and Registry family of  services enable publishing, inquiry, and subscription of existing system services using the Universal Description Discovery & Integration (UDDI) specification.    Through this set of services service providers are able to register (publish) the services they wish to make available to the DI2E community so that application/system/baseline providers can find & access them (service inquiry) and ask for notification when services that might meet their needs become available or updated (service subscription).",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0188',
  "TagValue_Number":"1.3",
  "TagValue_Service Name":"Service Management",
  "TagNotes_Service Definition":"Service Management capabilities provide publishing of, querying about, subscription and configuration management of services.",
  "TagNotes_Service Description":"Service Management capabilities provide publishing of, querying about, subscription and configuration management of services.    Information can be metadata about the service (registered in a Universal Description Discovery & Integration (UDDI) specification) or artifacts not strictly held in the UDDI registry but directly relevant to the registered service(s) (Installation manuals, test procedures, WSDL definitions, specifications, technical background, related architecture diagrams, etc.)",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"Line",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0187',
  "TagValue_Number":"1.2.6",
  "TagValue_Service Name":"Cross Domain",
  "TagNotes_Service Definition":"The Cross Domain services aid in the enforcement of security domain policies regarding the exchange of data and services across different security domain boundaries.",
  "TagNotes_Service Description":"Cross Domain enforces security domain policies (and de-conflicts policies) regarding the exchange of data and services across different security domain boundaries (including proxies).  Cross Domain ensures that users or services have the proper clearances and credentials to perform requested activities across two or more network fabrics or security domains.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"7.1.120 Communicate Information on the Secret Internet Protocol Router Network (SIPRnet)\n7.1.121 Communicate Information on the Non-Classified Internet Protocol Router Network (NIPRnet)\n7.1.122 Communicate Information on the Joint Worldwide Intelligence Communications System (JWICS) Network\n7.1.123 Communicate Information on the Coalition Mission Network\n8.2.22 Access Information at Multiple Levels of Security\n8.2.27 Manage Multiple Security Levels\n8.2.52 Manage Security Configuration",
  "TagValue_JARM/ESL Alignment":"5.05.04 Cross-Domain Services",
  "TagNotes_Comments":"Leverage existing cross domain solutions such as Centaur, etc."
},
{
  "TagValue_UID":'0186',
  "TagValue_Number":"1.2.5.2",
  "TagValue_Service Name":"Audit Log Reporting",
  "TagNotes_Service Definition":"The Audit Log Reporting service provides analysis reports on information contained in security audit logs.",
  "TagNotes_Service Description":"Audit Log Reporting works in sync with Audit Log Management to analyze and report audit log information including Quality of Service (QoS) metrics and operational status (availability, faults, etc.).   Ideally, managers are able to leverage audit log reporting to identify and respond to service problems before critical service failures.",
  "TagValue_Example Specification":"NIST SP 800-92,Guide to Computer Security Log Management",
  "TagValue_Example Solution":"Audit Logging\n\nSnare (intesectalliance.com), Apache ACE",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.4.3.2 Analyze Events",
  "TagNotes_JCSFL Alignment":"3.4.22  Create, Edit, and Display Log\n\n8.2.41  Provide Audit Services\n\n8.2.60 Passively Capture and Copy Network Traffic Data\n8.8.12 Log Operator Activity",
  "TagValue_JARM/ESL Alignment":"8.27.06 Audit Management; 5.05.02 Audit Logging Services",
  "TagNotes_Comments":"This service is not in the SDT document & SPFG may recommend removing - adjudication with SPFG and SMEs is needed.  - MP, July 29, 2011"
},
{
  "TagValue_UID":'0185',
  "TagValue_Number":"1.2.5.1",
  "TagValue_Service Name":"Audit Log Management",
  "TagNotes_Service Definition":"The Audit Log Management service supports security auditing by recording system accesses and operations and providing notification for certain events.",
  "TagNotes_Service Description":"Audit Log Management supports security auditing by recording a chronological record of system activities including system accesses and operations; and providing notification when previously identified events (e.g. a system goes down) occur.    Audit Log Management's purpose is to provide the data needed to enable reconstruction and examination of sequences of activities surrounding, or leading to, a specific event.",
  "TagValue_Example Specification":"NIST SP 800-92,Guide to Computer Security Log Management",
  "TagValue_Example Solution":"SolarWinds\ntripwire Log Center\nsplunk\n\nOpenAM, SSO Toolkit, OS ABAC/PicketLink",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.4.3.1 Detect Events",
  "TagNotes_JCSFL Alignment":"8.2.29  Detect and Record Host and Network Anomalies\n8.8.12  Log Operator Activity",
  "TagValue_JARM/ESL Alignment":"8.27.06 Audit Management; 5.05.02 Audit Logging Services",
  "TagNotes_Comments":"Access control audits should be done from the authentication and authorization systems that grant or deny access.  Note that it is as important to audit accesses as denials."
},
{
  "TagValue_UID":'0184',
  "TagValue_Number":"1.2.5",
  "TagValue_Service Name":"Audit Management",
  "TagNotes_Service Definition":"Audit Management defines the set of services that support the creation, persistence, and access of audit information relating to activities to access or\nutilize an application, system, network, or information resource.",
  "TagNotes_Service Description":"family",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"COE believes there is much IC activity going on in this area.  For example, there is a potential new mandate to require IC systems to transmit their audit log posts wrapped in the TDF XML format.  The ScvV-4 ought to defer to (and align with) whatever emerges in this area"
},
{
  "TagValue_UID":'0317',
  "TagValue_Number":"1.2.4.5",
  "TagValue_Service Name":"Incident Response",
  "TagNotes_Service Definition":"Incident Response services provide active response and remediation to a security incident that has allowed unauthorized access to an information system",
  "TagNotes_Service Description":" If a security incident occurs, Incident Response services assist administrators and authorities in stopping the incident to prevent additional damage, determining the scope and impact of the incident, restoring operational capability, performing forensics to determine how the incident occurred, and taking action to ensure that the incident does not occur again.  Incident Response services may also include tools to assist in legal action, if that is part of the organization's incident response plan.  Incident Response services are tied closely to organizational policies and procedures, and may include workflows or other business process tools to assist responders in following pre-established procedures.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"8.2.21 Respond to Security Events\n8.2.50 Drop Host Transactions",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0316',
  "TagValue_Number":"1.2.4.4",
  "TagValue_Service Name":"Virus Protection",
  "TagNotes_Service Definition":"Virus Protection and Malicious Code services identify and respond to specific security threats, including use of firewalls, anti-spam software, anti-virus software, and malware protection.",
  "TagNotes_Service Description":"Virus Protection is a specific kind of Intrusion Detection and Intrusion Prevention system aimed at targeting automated- or self-spreading malicious software (malware).  Virus Protection software is often combined with other intrusion detection and prevention software to form a complete host-based security system.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"8.2.40 Implement Firewall Protection",
  "TagValue_JARM/ESL Alignment":"4.03.05 Virus Protection and Malicious Code Service",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0315',
  "TagValue_Number":"1.2.4.3",
  "TagValue_Service Name":"Intrusion Prevention",
  "TagNotes_Service Definition":"Intrusion Prevention services include penetration testing and other measures to prevent unauthorized access to an information system.",
  "TagNotes_Service Description":"Intrusion Prevention services attempt to enforce limitations on process permissions and code execution capabilities to mitigate or stop entirely the execution of malicious software or activity on a system.  Network and host-based firewalls, technologies that enforce application whitelisting, and software that quarantines or removes suspicious software or processes are examples of Intrusion Prevention services.  Intrusion Prevention services often work closely with Intrusion Detection services; in many cases they are bundled together into the same software package.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"OISF Suricata, SNORT,  OSSEC",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"8.2.9 Maintain Secure Data Transmission\n8.2.10 Monitor Network Security\n8.2.16 Provide Transmission Security\n8.2.32 Mitigate Opportunity to Attack\n8.2.54 Perform Network Vulnerability Scanning",
  "TagValue_JARM/ESL Alignment":"8.27.10 Intrusion Prevention",
  "TagNotes_Comments":"Generally intrusion detection and prevention go hand-in-hand.  Prevention will also include policies and best practices.  Need some secure coding best practices along with this"
},
{
  "TagValue_UID":'0314',
  "TagValue_Number":"1.2.4.2",
  "TagValue_Service Name":"Intrusion Detection",
  "TagNotes_Service Definition":"Intrusion Detection services support the detection of unauthorized access to an information system.",
  "TagNotes_Service Description":"Intrusion Detection services monitor each computer in the enterprise and attempt to identify suspicious activity.  In the event that suspicious activity is detected, Intrusion Detection services notify other services so that immediate action can be taken.  Intrusion Detection services, on their own, do not attempt to prevent malicious activity.  They simply detect suspicious activity and notify other services or personnel, who are then responsible for responding appropriately.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"OISF Suricata, SNORT,  OSSEC",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"8.2.2 Detect Security Events\n8.2.10 Monitor Network Security",
  "TagValue_JARM/ESL Alignment":"8.27.11 Intrusion Detection; 3.02.04 Netwrok Intrusion Detection Services",
  "TagNotes_Comments":"Plenty of open source IDS engines"
},
{
  "TagValue_UID":'0313',
  "TagValue_Number":"1.2.4.1",
  "TagValue_Service Name":"Vulnerability Reporting",
  "TagNotes_Service Definition":"Vulnerability Reporting provides access to known reported vulnerabilities so appropriate action can be taken.",
  "TagNotes_Service Description":"none",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"3.3.8 Maintain Network Operations (NetOps) Related Threat Assessment\n8.2.31 Manage Threats to Network\n\n8.2.55 Disseminate Vulnerability Assessment",
  "TagValue_JARM/ESL Alignment":"8.27.24 Vulnerability Analysis",
  "TagNotes_Comments":"Each agency, COCOM, etc will have a set of rules and process for reporting vulnerabilities"
},
{
  "TagValue_UID":'0312',
  "TagValue_Number":"1.2.4",
  "TagValue_Service Name":"System and Communication Protection",
  "TagNotes_Service Definition":"System and Communication Protection is the family of services that monitor, control, and protect organizational communications (i.e., information transmitted or received by organizational information systems) at the external boundaries and key internal boundaries of the information systems; and employ architectural designs, software development techniques, and systems engineering principles that promote effective information security within organizational information systems.",
  "TagNotes_Service Description":"The System Communication and Protection family of systems focuses primarily on Computer and Network Defense.  Services within this family help to ensure that all systems are properly patched to mitigate or eliminate known vulnerabilities, configured to mitigate not-yet-known (i.e., 0day) vulnerabilities, defend against malicious software, and respond and recover from successful attacks or other security incidents.",
  "TagValue_Example Specification":"\nNIST SP 800-12",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0136',
  "TagValue_Number":"1.2.3.2",
  "TagValue_Service Name":"Security Label Format Validation",
  "TagNotes_Service Definition":"Security Label Format Validation validates the security marking of metadata.",
  "TagNotes_Service Description":"Security Label Format Validation validates the security marking of metadata.  Forwards metadata that passes validation.  Rejects metadata that fails validation.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"DDMS, NIEM marking validator.",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"8.2.64 Normalize Content and Semantics",
  "TagValue_JARM/ESL Alignment":"8.27.16 Security Classification",
  "TagNotes_Comments":"Could be library, war, and/or SOA service.  Not in place yet.  we ought to promote convergence toward the  security labeling standard that is embedded within the TDF format (and its associated XML schema components)... current plans are to discourage the proliferation of validators for this, and to instead adopt a single (probably NSA-maintained) validator capability...and since DoD is moving to NIEM we need to discourage use of DDMS.  Also want to consider same format for DoD and IC?"
},
{
  "TagValue_UID":'0310',
  "TagValue_Number":"1.2.3.1",
  "TagValue_Service Name":"Data Security Marking",
  "TagNotes_Service Definition":"Data Security Marking defines the services that create\nand annotate security classification to a specific information resource",
  "TagNotes_Service Description":"none",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"TDF library",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"8.2.1 Perform Network Information Assurance (IA)/Computer Network Defense (CND) Services",
  "TagValue_JARM/ESL Alignment":"8.27.16 Security Classification",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0309',
  "TagValue_Number":"1.2.3",
  "TagValue_Service Name":"Security Metadata Management",
  "TagNotes_Service Definition":"Security Metadata Management supports the annotation of data items (such as content, documents, records) with the proper security classifications, and applicable metadata for access control (which may include environment metadata).",
  "TagNotes_Service Description":"The Security Metadata Management family of services help ensure that products within the system have proper security markings and labels appropriate to their classification.  These markings may be used by other automated systems, such as the services within the Dissemination Management family, to make authorization or releasability decisions.  While releasability may still have a man-in-the-loop, it is nonetheless important to ensure that consistent, properly-formatted and well-understood security markings are present, to minimize unauthorized disclosure or other classified information spillage.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0178',
  "TagValue_Number":"1.2.1.11",
  "TagValue_Service Name":"Certificate Validation",
  "TagNotes_Service Definition":"Certificate Validation ensures a presented security assertion within a security token matches similar data in the Certification Authority's (CA) root certificate.",
  "TagNotes_Service Description":"Certificate Validation ensures a presented PKI certificate has been signed by a trusted CA, is within the valid timeframe, has not been tampered with, has not been revoked, etc.  Certificate Validation Service allows clients to delegate some or all certificate validation tasks and is especially useful when a client application has limited Public Key Infrastructure (PKI) services.   The service corresponds to a “Tier 2 Validation Service” and shields client applications from such PKI complexities as X.509v3 certificate syntax processing (e.g., expiration), revocation status checking, or certificate path validation.  \nCertificate Validation may include any OCSP responders.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"Picket Link, Picket Fence. METRO,\nApache CXF\n\nOpenAM, SSO Toolkit, ESF2",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":"6.2.3.5 Common Identity Assurance Services",
  "TagNotes_JCSFL Alignment":"8.2.19  Validate User Credentials\n8.2.6  Provide Entity Authentication Services",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"Additional examples not yet in garage: OCSP Responder"
},
{
  "TagValue_UID":'0307',
  "TagValue_Number":"1.2.2.1",
  "TagValue_Service Name":"Encryption/ Decryption",
  "TagNotes_Service Definition":"Encryption/Decryption defines the set of services that encrypt and decrypt interactions between consumers and providers to support minimal confidentiality requirements. Within a PKI-environment, encryption and decryption processes are done using the provider’s public and private keys.",
  "TagNotes_Service Description":"Encryption/Decryption can occur at multiple “levels” within an interaction.  For example, transport level encryption may be sufficient for many interactions. In this case, the provider’s public and private keys are used in the encryption process. For interactions requiring additional encryption, message-level encryption may be used where the subject’s public and private key may be utilized.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"8.2.7 Decrypt Data\n8.2.8 Encrypt Data\n8.2.12 Provide Embedded Programmable Cryptographic Capability\n8.2.13 Implement National Security Agency (NSA) and/or National Institute of Standards and Technology (NIST) Public Key Cryptography\n8.2.15 Provide Cryptographic Services",
  "TagValue_JARM/ESL Alignment":"8.27.08 Cryptography Management",
  "TagNotes_Comments":"Will need to be careful with this.  For US, will be ITAR restricted stuff.  Generally deployed as libraries or modules that can be incorporated into existing or new applications."
},
{
  "TagValue_UID":'0306',
  "TagValue_Number":"1.2.2",
  "TagValue_Service Name":"Cryptography Management",
  "TagNotes_Service Definition":"Cryptography Management defines the set of services that support the generation, exchange, use, escrow and management of ciphers (e.g., keys), including the use of encryption/decryption processes to ensure the confidentiality and integrity of data.",
  "TagNotes_Service Description":"The Cryptography Management family covers all manner of cryptography-related services.  This includes both symmetric and asymmetric crypto functions, one-way hashing functions, management of encryption keys, digital signature verification, and validation of PKI certificates.  The services within this family may not be discrete, stand-alone services as one would picture in the traditional SOA sense; rather, many Cryptography Management services are likely to be libraries or executables that are embedded within, or bundled with, other applications.  For example, encryption-in-transit is required for many types of data.  An encryption service, for these types of data, would create a chicken-and-egg problem: data must be encrypted before it can be sent to the encryption service, but the encryption service must do the encryption.  In this example, the encryption libraries will likely be resident on the system that generates the sensitive data.  Nonetheless, it is important to explicitly call out the cryptographic functions that are performed within the architecture, to ensure that they are properly accounted for and not overlooked.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0177',
  "TagValue_Number":"1.2.1.10",
  "TagValue_Service Name":"Attribute Access",
  "TagNotes_Service Definition":"Attribute Access provides values that describe human users and non-person entities (NPE) for the purpose of making authorization decisions.",
  "TagNotes_Service Description":"Attribute Access  accepts attribute requests from authorized consumers and responds with an attribute assertion containing the requested attribute values that are releasable to that consumer.   Attributes for either human users or non-person entities (NPE) can be passed and are typically used by Policy Decision Services, Policy Decision Points, Application Servers, or other SAML endpoints.   Secure attribute passing supports an Attribute-Based Access Control (ABAC) authorization model.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"NCES:\n- Robust Certificate Validation Service (provider:  DISA DoD PKI Program)\n\nOpenAM, SSO Toolkit",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":" 6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.2.11  Provide Identity Management Services\n8.2.18  Perform Registration Services\n8.2.19 Validate User Credentials\n8.2.20 Authorize Data Access\n8.8.3  Manage Accounts\n8.8.5  Assign Roles and Privileges\n8.8.6  Maintain and Create User Profile",
  "TagValue_JARM/ESL Alignment":"8.27.04 Attribute Management",
  "TagNotes_Comments":"This is done in association with or after authentication.  Additional examples not yet in garage: OpenLDAP, UAAS services deployed on high-side and their mirror's on SIPR, UAAS spec for identifying attributes.  Finally, see note for PDP.  Generally, the authorized attribute service pulls authorization-related attributes from disparate authoritative sources (e.g., HR systems, accreditation databases, training databases, etc.) and caches them locally for serving up to PDPs and to protected resources that have embedded their policy decision-making internally to the protected app."
},
{
  "TagValue_UID":'0374',
  "TagValue_Number":"1.2.1.9",
  "TagValue_Service Name":"Federation Service Management",
  "TagNotes_Service Definition":"Federation Service Management establishes trust relationships between different organizations or systems, allowing entities within an environment to access resources within another environment without the need to have an individual account on the remote environment.  This service defines and maintains trust relationships, which are then queried by the Security Token Validation service when making validation decisions.",
  "TagNotes_Service Description":"none",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"0",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0179',
  "TagValue_Number":"1.2.1.8",
  "TagValue_Service Name":"Security Token Service",
  "TagNotes_Service Definition":"Security Token Service creates enterprise security tokens to provide authentication across systems and services.",
  "TagNotes_Service Description":"Security Token Service forms the basis for identification and authentication activities by generating security tokens and inserting them in outgoing encrypted messages.   Passed security tokens contain authentication and attribute assertions that temporarily authenticate users.  Tokens also contain user attributes and a security assertion which can be validated by receiving policy services.  The Security Token Service may also perform token validation if required.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"IBM Data Power, Jericho System: Enterspace Decision Service \n\nOpenAM, SSO Toolkit",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":"6.2.3.5 Common Identity Assurance Services",
  "TagNotes_JCSFL Alignment":"8.2.13  Implement National Security Agency (NSA) and/or National Institute of Standards and Technology (NIST) Public Key Cryptography\n8.2.15  Provide Cryptographic Services\n8.2.34  Generate Network Keys\n8.8.8  Manage Cryptographic Accounts\n8.2.33  Distribute and Enable Network Keys\n8.2.35  Manage Network Keys\n8.2.36 Order Network Keys",
  "TagValue_JARM/ESL Alignment":"4.03.04 Token Services",
  "TagNotes_Comments":"Ping Identity is being used in some IC elements.  Also, this is associated with both authentication and authorization.  Authentication for Single Sign-on.  Authorization for chaining of identity and related attributes for authorization decisions."
},
{
  "TagValue_UID":'0183',
  "TagValue_Number":"1.2.1.7",
  "TagValue_Service Name":"Policy Access Point",
  "TagNotes_Service Definition":"Policy Access Point enables service providers and applications to request and retrieve access policies.",
  "TagNotes_Service Description":"The Policy Access Point exposes authorization policies by retrieving and managing policies as implemented by Policy Decision Point (PDP) logic (e.g., access control over portlets in a portal server, or whether a service consumer may search the registry for the key to retrieve all implementations). Implementation may optionally be published in the Enterprise Service Registry (ESR) as defined by the DoD/IC Service Registry and Governance Working Group.     [SSP note: a recommended format for PAS messages is extensible Access Control Markup Language (XACML)]",
  "TagValue_Example Specification":"OASIS extensible Access Control Markup Language (XACML)",
  "TagValue_Example Solution":"OS ABAC, OpenAM (maybe), SSO Toolkit (maybe)",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":"6.3.4 Cyber Management",
  "TagNotes_JCSFL Alignment":"8.2.68 Determine Network Security Requirements",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"OpenAM distributes authorization policies from the enterprise server to any distributed JEE or web agent.  Not sure if it distributes them to fedlets.  Note that this is usually associated with PDPs."
},
{
  "TagValue_UID":'0182',
  "TagValue_Number":"1.2.1.6",
  "TagValue_Service Name":"Policy Enforcement Point",
  "TagNotes_Service Definition":"Policy Enforcement Point (PEP)enforces security-related policies for access to protected resources.",
  "TagNotes_Service Description":"Policy Enforcement Points sit in front of the requested web services, intercepting incoming requests and outgoing responses to apply the appropriate access policies.  The PEP generates the access request to the Policy Decision Point (PDP) and interprets and enforces the PDP's decision.   Policies can be built to include, but are not limited to, authentication, authorization, data integrity, and confidentiality. When enforcing authorization policies, the PEP uses the PDP to evaluate an authorization policy for the resource.  When a security token is provided to the PEP, that token is validated prior to being trusted.  Token validation may occur in the PEP itself, or if the PEP is unable to validate the token, the PEP may leverage an STS to perform validation on its behalf.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"• IBM DataPower\n• Layer 7\n\nOpenAM Fedlet, OpenAM Web Agent, OpenAM JEE Agent, SSO  Toolkit, OS ABAC/PicketLink, ESF2",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.2.1  Perform Network Information Assurance (IA)/Computer Network Defense (CND) Services\n\n8.2.3 Regulate Information Dissemination\n8.8.17  Monitor Access Control\n\n8.9.3  Provide Environment Control Policy Enforcement Services",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"See notes related to PDP.  Note also that PEPs are often provided natively with the protected resources' container.  Recommend leveraging Spring Security for this to allow easy integration of different PEPs and PDPs."
},
{
  "TagValue_UID":'0181',
  "TagValue_Number":"1.2.1.5",
  "TagValue_Service Name":"Policy Decision Point",
  "TagNotes_Service Definition":"Policy Decision Points accepts access requests and returns whether the request is appropriate given access rules and conditions.",
  "TagNotes_Service Description":"A Policy Decision Point service hosts Quality of Protection (QoP) parameters and user attributes so that services can flexibly determine and execute protection measures.   For example, some services may require X.509 certificate based authentication whereas others may only need username / password authentication.   Or, clients that access a resource from different domains may require different “strengths” of authentication and access control.   PDPs do this by accepting authorization queries (typically XACML based), evaluating the request based on a variety of inputs (target resource, requested operation, requester's identity, etc.) and returning authorization decision assertions.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"IBM Data Power, Jericho System: Enterspace Decision Service\n\nOpenAM, SSO Toolkit, OS ABAC/PicketLink, ESF2",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.1.57 - Authorize Network Resource Request\n8.2.20  Authorize Data Access\n8.9.2  Provide Environment Control Policy Enforcement Decisions",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"In the IC, this is often co-located with the authorized attribute source, since the PDP needs the authorized attributes to make its decisions.  Also, consumers can decide whether to go to  the one place for policy decisions or for the attributes.  Likewise, the PEP may be provided by the same product that supports the PDP, or may be custom-built, or a generic part of the container."
},
{
  "TagValue_UID":'0373',
  "TagValue_Number":"1.2.1.4",
  "TagValue_Service Name":"Authentication Service",
  "TagNotes_Service Definition":"Authentication Service performs authentication of entities within the enterprise.",
  "TagNotes_Service Description":"none",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"0",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.2.6 Provide Entity Authentication Services",
  "TagValue_JARM/ESL Alignment":"8.27.02 Authentication Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0372',
  "TagValue_Number":"1.2.1.3",
  "TagValue_Service Name":"Resource Policy Management",
  "TagNotes_Service Definition":"Resource Policy Management creates and maintains access control policies for protected resources.",
  "TagNotes_Service Description":"none",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"0",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.27.19 IA Guidance Specification and Policy; 8.27.20 Digital Policy Management; 5.05.05 User Attribute Service",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0371',
  "TagValue_Number":"1.2.1.1",
  "TagValue_Service Name":"Local Identity Management",
  "TagNotes_Service Definition":"Local Identity Management provides the creation, maintenance, and deletion of user accounts, password maintenance, and the administration of user access rights. Also Creates local account from PKI certificate.  Maps DN to UserID in local account.",
  "TagNotes_Service Description":"Local Identity Management provides the creation, maintenance, and deletion of user accounts, group membership and other user attributes, password maintenance, and the administration of user access rights for local, role-based access control systems. This service also creates local accounts from PKI certificates, and maps the DN from a certificate to a local UserID.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"none",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"0",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.2.11 Provide Identity Management Services\n8.8.3 Manage Network Information Access for User Account\n8.8.5 Assign Roles and Privileges\n8.8.6 Create and Maintain Network User Account and Profile\n8.8.18 Delete User\n8.8.19 Request System Access\n8.8.22 Manage Local Information Access for User Account\n8.8.23 Create and Maintain Local User Account and Profile",
  "TagValue_JARM/ESL Alignment":"8.27.01 Identity Management; 8.27.05 Credential Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0176',
  "TagValue_Number":"1.2.1",
  "TagValue_Service Name":"Identity and Access Management",
  "TagNotes_Service Definition":"Identity and Access Management (IdAM) defines the set of services that manage permissions required to access each resource.",
  "TagNotes_Service Description":"IdAM includes services that provide criteria used in access decisions and the rules and requirements assessing each request against those criteria.  Resources may include applications, services, networks, and computing devices.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0175',
  "TagValue_Number":"1.2",
  "TagValue_Service Name":"Security Management",
  "TagNotes_Service Definition":"Security Management encompasses the processes and technologies by which people and systems are identified, vetted, credentialed, authenticated, authorized for access to resources, and held accountable for their actions.",
  "TagNotes_Service Description":"Line",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"Line",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0172',
  "TagValue_Number":"1.1.5.1",
  "TagValue_Service Name":"Global Unique Identifier (GUID)",
  "TagNotes_Service Definition":"The GUID service provides a Uniform Resource Identifier (URI) to an entity in the enterprise",
  "TagNotes_Service Description":"A GUID or Uniform Resource Identifier (URI) in the DI2E context provides a unique form of identification for any resource within the enterprise. A resource is anything that is or can be associated to the  Enterprise.  This can includes  nodes, data, information, role definitions and people.",
  "TagValue_Example Specification":"OASIS Extensible Resource Descriptor (XRD) 1.0 Committee Draft 02.",
  "TagValue_Example Solution":"XRI.net\n\nGUIDE",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.1.62  Provide Namespace Services\n8.2.18  Perform Registration Services",
  "TagValue_JARM/ESL Alignment":"8.24.02 Asset Cataloging / Identification; 6.02.01 Unique Identifier Services",
  "TagNotes_Comments":"GUID is not identity management.    Can be implemented as library, .war or as SOA service. GOTS and stds exist for this.  Recent conversation on DIB Hub related to GUIDE https://forum.macefusion.com/phpbb3/viewtopic.php?f=15&t=150 \n\nNote that this has been one of the key areas of focus of the IC ITE effort... after much painful negotiation, a standard was agreed to by the IC community... every discrete piece of intelligence product is supposed to be tagged with a GUIDE ID at its earliest point of consumability (if not sooner!)... the GUIDE ID is an extension/variation of CIA's original implementation of this.  In particular, an IC ITE GUIDE ID has two parts: a prefix and a suffix.  The prefix is an arbitrary number that will be assigned to an Intel producing organization (agency) by the TIC (an existing ODNI group)... an agency may end up having several assigned prefix numbers, perhaps dedicated to particular product types or producing systems.  The suffix can be assigned in one of two ways. The preferred approach is to use one of the available industry standard random generators to assign a 32 hex-character number (which is usually displayed as a 36-character string, after inserting 4 hyphens, at standard locations, to break up the hex number for readability reasons), the other (optional) approach is to assign some other unique string value, with the proviso that this string has to be unique (when combined with the associated prefix value), and with the proviso that the string cannot be of substantive intelligence significance (i.e., you can't use a BE Number or some other well-known intelligence-related code, because this value, by itself, must be meaningless).\n\nThe IC is going to force Intel producers to start assigning these things, so the ScvV-4 should align with this, rather than allowing some kind of variant of this approach."
},
{
  "TagValue_UID":'0302',
  "TagValue_Number":"1.1.5",
  "TagValue_Service Name":"Enterprise Resource Management",
  "TagNotes_Service Definition":"Enterprise Resource Management defines the set of services that support unambiguous, assured, and unique identities for both person and non-person entities (NPE).",
  "TagNotes_Service Description":"Enterprise Resource Management has a critical dependency on authoritative personnel and asset management systems for identity information, as well as Directory Services for the management of identity information.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"OpenDJ, OpenLDAP,  OpenIDM",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"This can leverage LDAP capabilities, as well as full-service identity management tools with workflow management, etc."
},
{
  "TagValue_UID":'0193',
  "TagValue_Number":"1.1.4.3",
  "TagValue_Service Name":"Notification Consumer",
  "TagNotes_Service Definition":"Notification Consumer service subscribes the producer services informing it that it wishes to start receiving notifications.",
  "TagNotes_Service Description":"Notification Consumer service sets up end points to receive, either through pushed or pulled delivery, event notifications sent to it by Notification Producer or Notification Broker services.  In doing so, it can set up subscriptions and also unsubscribe from the producer or broker informing it that it wishes to stop receiving event notifications.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"Apache CXF, SMOA NTF",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"6",
  "TagValue_JCA Alignment":"6.2.3.6 Enterprise Messaging",
  "TagNotes_JCSFL Alignment":"8.1.55 - Provide Subscription Services",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"This is probably a library that producers and consumers can import and that allow the producers and consumers to interact with the queuing system by supporting the actual events and the ability to push events onto and pull event details from the queues."
},
{
  "TagValue_UID":'0192',
  "TagValue_Number":"1.1.4.2",
  "TagValue_Service Name":"Notification Broker",
  "TagNotes_Service Definition":"Notification Broker service delivers notifications of events to end-users both within nodes and across node boundaries.",
  "TagNotes_Service Description":"Notification Broker service provides the ability to manage receipt and delivery of event notifications that may need to proxied, routed, and brokered until the receiver accepts the event notification. It provides a mechanism to manage event notifications within a group to provide unit of order delivery (i.e. you want the stand-down message to come in the correct order compared to the attack message).    Some event notifications may not be able to be delivered timely due to network issues, offline, errors etc.  This component queues them up and manages successful delivery.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"Apache CXF, SMOA NTF\n\n WSO2 Message Broker, JMS, Fuse, RabbitMQ",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"6",
  "TagValue_JCA Alignment":"6.2.3.6 Enterprise Messaging",
  "TagNotes_JCSFL Alignment":"3.3.13 Manage Alerts and Indications\n7.1.4  Send Messages on Timed Delivery Basis\n7.1.77 Coordinate Message Delivery\n8.1.11 Provide MessageServices\n8.5.32 Reroute Messages",
  "TagValue_JARM/ESL Alignment":"5.01.01 Data Transport Services",
  "TagNotes_Comments":"Another term for this is pub/sub.   Brokers are typically built on top of queuing systems.   Not clear how this is different from the event notification function above."
},
{
  "TagValue_UID":'0191',
  "TagValue_Number":"1.1.4.1",
  "TagValue_Service Name":"Notification Producer",
  "TagNotes_Service Definition":"Notification Producer service serves as the generic interface for publishing an event related to a topic.",
  "TagNotes_Service Description":"Notification Producer service sends notifications that an event occurred with data about that event to Notification Consumer services.    In doing so, the producer does not know about the consumers in advance and the set of consumers may change over time.    Related operations include (but may not be limited to) notification production (sending); pull pointing; creating a pullpoint; subscription management, providing a subscription/notify interface (for notification push); and sending event notifications to a Notification Broker service in order to publish notifications on given topics.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"Apache CXF, SMOA NTF",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"6",
  "TagValue_JCA Alignment":"6.2.3.6 Enterprise Messaging",
  "TagNotes_JCSFL Alignment":"3.3 Provide Indications, Warnings, and Alerts - Group\n7.1.55  Manage Messaging Services",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"This is probably a library that producers and consumers can import and that allow the producers and consumers to interact with the queuing system by supporting the actual events and the ability to push events onto and pull event details from the queues."
},
{
  "TagValue_UID":'0190',
  "TagValue_Number":"1.1.4",
  "TagValue_Service Name":"Event Notification",
  "TagNotes_Service Definition":"Eventing capabilities provide a federated, distributed, and fault-tolerant enterprise message bus delivering performance, scalable and interoperable asynchronous event notifications Quality of Service (QoS), guaranteed delivery to disconnected users or applications, and decoupling of information among producers and consumers.",
  "TagNotes_Service Description":"The Eventing line provides a federated, distributed, and fault-tolerant enterprise message bus. It delivers high performance, scalable and interoperable asynchronous event notifications to both applications and end-users. This service supports the configuration of Quality of Service (QoS) for a published message including the priority, precedence, and time-to-live (TTL); provides guaranteed delivery to disconnected users or applications; and utilizes multiple message brokers, potentially within different administrative domains to support the distributed, federated nature of the GIG. Messaging services promote decoupling of information among producers and consumers.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"RabbitMQ, JMS, ActiveMQ",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"This is more likely the message queuing capability.  Need to support fire and forget with reliability"
},
{
  "TagValue_UID":'0171',
  "TagValue_Number":"1.1.3.3",
  "TagValue_Service Name":"Site Monitoring",
  "TagNotes_Service Definition":"Site Monitoring provides the ability to measure and report the health and performance of websites and servers.",
  "TagNotes_Service Description":"Site Monitoring measures and reports the health and performance of web sites and servers through the collection of enterprise-defined metrics.  It provides a vendor, platform, network, and protocol neutral framework that shares a common messaging protocol to unify infrastructure status reporting.  In the event of a system fault or failure, Site Monitoring will use the Notification service to send event notifications to the appropriate subscribers.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"• Compuware Gomez\n• Host Tracker\n• Monitor.us\n• Nagios, \n\nSame tools as the metrics above.",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.3.1.1 Network Resource Visibility",
  "TagNotes_JCSFL Alignment":"3.5.24  Monitor Data Link Status\n3.5.26  Display System Status\n3.5.27 Monitor System Status\n8.1.13  Maintain Network and Communication Status \n8.1.16  Manage Network Performance\n8.1.24  Monitor and Assess Network \n8.1.25  Manage Status of Network Assets \n8.1.26  Perform System and Network Analysis \n8.1.32  Calculate Network Loads\n8.1.33  Calculate Processing Availability \n8.1.67  Manage Message Traffic \n8.1.77  Assess Network Equipment Performance \n8.1.81  Balance Processing Loads \n8.1.84  Track Network User Activity \n8.5.11  Monitor Network Usage\n8.7.62  Monitor System and Resource Allocation\n9.2.15  Report System Status\n9.2.16  Maintain System Status\n9.3.10 Receive and Register System Status",
  "TagValue_JARM/ESL Alignment":"8.33.01 Service Monitoring; 5.04.02 Infrastructure and Performance Monitoring Services",
  "TagNotes_Comments":"Generally, Fault detection/isolation is build on top of the metrics monitoring and reporting tools noted above.  The additional stuff includes rules processing that allows for histerisis and trend detection, as well as for analytics for tracing connections among faults (e.g., sometimes a single fault will result in may down-stream faults)."
},
{
  "TagValue_UID":'0301',
  "TagValue_Number":"1.1.3.2",
  "TagValue_Service Name":"Fault Isolation",
  "TagNotes_Service Definition":"Fault Isolation enables pinpointing the type of fault and its location.",
  "TagNotes_Service Description":"Fault Isolation works in cooperation with Fault Detection, to identify information about a fault when one has been detected.  This information may include which system/service has failed, the operations or jobs that were being processed at time of failure, and information about currently logged-on or authenticated users, as well as standard date/time and system location information.  This information is provided to the Site Monitoring service to provide appropriate notifications.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"Same tools as the metrics above.",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.1.1 Network Resource Visibility",
  "TagNotes_JCSFL Alignment":"9.1.19 Provide On-Line Fault Isolation\n9.2.4 Perform Network Fault Isolation and Reconfiguration\n9.2.5 Monitor Faults",
  "TagValue_JARM/ESL Alignment":"5.04.03 Enterprise IT Environment Services",
  "TagNotes_Comments":"Generally, Fault detection/isolation is build on top of the metrics monitoring and reporting tools noted above.  The additional stuff includes rules processing that allows for histerisis and trend detection, as well as for analytics for tracing connections among faults (e.g., sometimes a single fault will result in may down-stream faults)."
},
{
  "TagValue_UID":'0300',
  "TagValue_Number":"1.1.3.1",
  "TagValue_Service Name":"Fault Detection",
  "TagNotes_Service Definition":"Fault Detection enables system monitoring and identification of a fault occurrence.",
  "TagNotes_Service Description":"Fault Detection may be implemented as a centralized audit log monitoring and alerting system to which all computing assets push their logging data, as a heartbeat type service that periodically checks to make sure that other services are up and running, or as a local agent deployed on a system that monitors for faults and sends those notifications to a centralized detection location.  When a fault is detected, the Fault Detection service will interact with the Fault Isolation service to derive additional information.  This additional information is used by the Site Monitoring service to send appropriate notifications.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"Same tools as the metrics above.",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.3.1.1 Network Resource Visibility",
  "TagNotes_JCSFL Alignment":"9.1.18 Detect Equipment Faults\n9.3.8 Monitor and Report Software Faults\n9.3.9 Monitor and Report Hardware System Faults",
  "TagValue_JARM/ESL Alignment":"5.04.03 Enterprise IT Environment Services",
  "TagNotes_Comments":"Generally, Fault detection/isolation is build on top of the metrics monitoring and reporting tools noted above.  The additional stuff includes rules processing that allows for histerisis and trend detection, as well as for analytics for tracing connections among faults (e.g., sometimes a single fault will result in may down-stream faults)."
},
{
  "TagValue_UID":'0169',
  "TagValue_Number":"1.1.3",
  "TagValue_Service Name":"Enterprise Monitoring",
  "TagNotes_Service Definition":"The Enterprise Monitoring family monitors services and websites across the  enterprise.",
  "TagNotes_Service Description":"Enterprise Monitoring services measure and report the health and performance of web services as well as DI2E servers and websites.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0174',
  "TagValue_Number":"1.1.2.2",
  "TagValue_Service Name":"Time Synchronization",
  "TagNotes_Service Definition":"The Time Synchronization service establishes a consistent time to be used by all DI2E IT resources.",
  "TagNotes_Service Description":"Time Synchronization provides a consistent time reference for all DI2E devices, thus supporting secure log ins, service interoperability, trustworthy database transactions, and accurate monitoring activities.  The goal is to use well established and ubiquitous method of time synchronization that responds to time requests from any internet client and is consistent among all DI2E nodes & community related nodes.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"• Microsoft Server\n• Linux native NTP Daemon",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"6",
  "TagValue_JCA Alignment":"6.2.4.2 Utilize PNT Information",
  "TagNotes_JCSFL Alignment":"1.5.11   Translate Time Data\n8.1.23 - Synchronize Network Timing \n8.7.23  Disseminate Time Data",
  "TagValue_JARM/ESL Alignment":"3.01.04 Timing and Frequency Device Services",
  "TagNotes_Comments":"What is important here is that the time sync across the networks is from a common source (e.g., GPS or USNO).  This should be pretty much a done deal on all our networks, just need to confirm.  What we also should do is identify how a given app can access that time from the OS it's using, and we need to make sure that the OS in VMs can access the NTP as well."
},
{
  "TagValue_UID":'0225',
  "TagValue_Number":"1.1.2.1",
  "TagValue_Service Name":"Domain Name System (DNS)",
  "TagNotes_Service Definition":"This Domain Name System (DNS) locates and translates Internet domain names into Internet Protocol (IP) addresses using a hierarchy of authority.",
  "TagNotes_Service Description":"Domain names are meaningful identification labels for Internet addresses. The Domain Name System (DNS) translates the more easily used by humans domain names (example: www.army.mil)  into numerical Internet Protocol formats (example: 192.52.180.110) used by networking equipment for locating and addressing devices. \n\nDNS makes it possible to assign domain names to groups of Internet users in a meaningful way, independent of each user's physical location. Because of this, World Wide Web (WWW) hyperlinks and Internet contact information can remain consistent when internet routing arrangements change or a participant uses a mobile device.     Consistently applied DNS naming conventions across the enterprise further promote enterprise interoperability because users & systems can more easily and dependably find and understand the purpose of web based resources.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT\nNIST 800-81",
  "TagValue_Example Solution":"• Microsoft Server\n• BIND",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"6",
  "TagValue_JCA Alignment":"6.2.1 Information Sharing",
  "TagNotes_JCSFL Alignment":"8.1.56 Translate Name-to-Address\n8.1.60 Provide Distributed Domain Name Systems (DNSs)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"DNS is pretty much ubiquitous.  What is needed is not so much the tool as the guidance for best practices related to how to manage the DNS hierarchy within a COCOM and all the enclaves within the COCOM, as well as across COCOMs.  Additionally, need to make sure URL best practices are part of this guidance (e.g., \"Cool URLs Don't Change\" from Tim Berners Lee)"
},
{
  "TagValue_UID":'0173',
  "TagValue_Number":"1.1.2",
  "TagValue_Service Name":"Translation and Synchronization",
  "TagNotes_Service Definition":"Translation and Synchronization services promote standardized references of time and location across the  Enterprise.",
  "TagNotes_Service Description":"Translation & Synchronization enables the management of network operations by resolving service endpoint Universal Resource Locators (URLs) and maintaining synchronized time on all connected  nodes.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"family",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0168',
  "TagValue_Number":"1.1.1.2",
  "TagValue_Service Name":"Metrics Reporting",
  "TagNotes_Service Definition":"Metrics Reporting service provides an interface for the retrieval of managed resource quality of service metrics.",
  "TagNotes_Service Description":"Metrics Reporting is used by Knowledge Managers to retrieve metrics collected by the Metrics Measurements Collection service.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"DISA ESM tool",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":"6.3.1.1 Network Resource Visibility",
  "TagNotes_JCSFL Alignment":"3.4.18 Display Performance Data\n8.7.52 Report Performance Results\n9.1.29 Record and Store System Components Reliability Information",
  "TagValue_JARM/ESL Alignment":"8.16.11 Metrics Visualization",
  "TagNotes_Comments":"Probably an interface (both GUI and back-end interfaces) associated with the Metrics Measurement Collection tool"
},
{
  "TagValue_UID":'0164',
  "TagValue_Number":"1.1.1.1",
  "TagValue_Service Name":"Metrics Measurements Collection",
  "TagNotes_Service Definition":"Metrics Measurements Collection gathers and records service performance measurements.",
  "TagNotes_Service Description":"Metrics Measurements Collection service monitors invoked services and collects related performance measurements which are then used to calculate quality of service and other performance metrics.  Not directly accessed by users, this service gathers and records its information into the service performance database.",
  "TagValue_Example Specification":"Yes,see CDP hosted in SPMT",
  "TagValue_Example Solution":"DISA ESM tool",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":"6.3.1.1 Network Resource Visibility",
  "TagNotes_JCSFL Alignment":"3.4.12 Collect Performance Data\n3.4.18 Display Performance Data\n9.2.9 Record Real-Time System Performance and Condition Information",
  "TagValue_JARM/ESL Alignment":"8.16.10 Metrics Collection",
  "TagNotes_Comments":"Probably a combination of libraries such as log4j, best practice guidance, a tool that can forward metrics logs, and a tool that can collect metrics logs and store them in \"big storage\" and perform analytics on them."
},
{
  "TagValue_UID":'0163',
  "TagValue_Number":"1.1.1",
  "TagValue_Service Name":"Metrics Management",
  "TagNotes_Service Definition":"Metrics Management enables operational analysis and management for the quality of service provided by DI2E services.",
  "TagNotes_Service Description":"Metrics Management develops & collects metrics, monitors events, and evaluates performance.   This requires a standard set of metrics, reported events, and interface for querying and reporting collected metrics. \n\nService providers must analyze information to detect and report service degradation so corrective action can be taken when needed.   Real-time monitoring avoids Service Level Agreement (SLA) violations and minimizes service down time.   \n\nThe Metrics Management family supports two service interface models: Request/Response Interface – to support ad-hoc querying of metrics and event data; and the Publish/Subscribe Interface – to support a publish/subscribe interface.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"Nagios, Hyperic, Zenoss, and OpenNMS",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"Plenty of open source tools for doing this.  Recommend Nagios.  Need to manage not only networks, VMs, processes, but also the actual web services etc."
},
{
  "TagValue_UID":'0162',
  "TagValue_Number":"1.1",
  "TagValue_Service Name":"Enterprise Management",
  "TagNotes_Service Definition":"Enterprise Management services enable consistent service level agreement and quality of service reporting; service/site monitoring; consistent use of domain names; and time synchronization.",
  "TagNotes_Service Description":"Enterprise Management services enable consistent management of service performance, access, and time.  In particular:\n\n   • Service Level Agreement/Quality of Service (SLA/QoS) Reporting services enable operational analysis and management of DI2E services\n   • Enterprise Administration services monitor services, services, and websites \n   • Translation and Synchronization services promote standardized references of time and location",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"Line",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Focus on what should be the standard measures, by category of monitored item.  In addition, give guidance for how to calculate these measures (e.g., availability, MTTR, etc).   What is important is not so much consistency of SLAs as much as consistency of parameter definitions/measures that any given SLA will reference"
},
{
  "TagValue_UID":'0002',
  "TagValue_Number":"2",
  "TagValue_Service Name":"Common Services",
  "TagNotes_Service Definition":"The services that provide a  function that is common across many mission capabilities.",
  "TagNotes_Service Description":"Layer",
  "TagValue_Example Specification":"Layer",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"Layer",
  "TagValue_DCGS Enterprise Status":"Layer",
  "TagValue_JCA Alignment":"Layer",
  "TagNotes_JCSFL Alignment":"Layer",
  "TagValue_JARM/ESL Alignment":"Layer",
  "TagNotes_Comments":"Layer"
},
{
  "TagValue_UID":'0151',
  "TagValue_Number":"2.6.5.10",
  "TagValue_Service Name":"Change Agent",
  "TagNotes_Service Definition":"Change Agent provides for the management of information about the individuals that update and modify managed records.\n",
  "TagNotes_Service Description":"The Change Agent service is responsible for managing the information regarding organizational structure used for the purpose of establishing provenance within the records management service by identify the parties that are involved with the maintenance and manipulation of the Managed Records and the other entities within the records management service.  The Change Agent service has operations that provide access to a hierarchy of change agents and the roles that have been assigned to them.\n",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"How is this different than ABAC with PDP/PEP?"
},
{
  "TagValue_UID":'0150',
  "TagValue_Number":"2.6.5.9",
  "TagValue_Service Name":"Record Attribute Profiles",
  "TagNotes_Service Definition":"Record Attribute Profiles maintain the instance data for profiles that have been added to the records management services.\n",
  "TagNotes_Service Description":"The Record Attribute Profiles service maintains the instance data for profiles that have been added to the records management family. This service is not responsible for entering the profiles themselves.  The service’s responsibilities are limited to providing information contained in existing profiles and for storing instance data for attributable objects. Attribute profiles enable the dynamic addition of metadata to Managed Records based on profiles set up in the system. Objects are registered with the Record Attribute Profiles service so that attributes can be set for that particular object based on a profile.  The Record Attribute Profiles service contains operations setting and retrieving the attribute values.\n",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0149',
  "TagValue_Number":"2.6.5.8",
  "TagValue_Service Name":"Record Authentications",
  "TagNotes_Service Definition":"Record Authentications provides the management of authentication methods and the results of execution of those methods on managed records.\n",
  "TagNotes_Service Description":"The Record Authentications service provides the ability to manage authentication methods, execute those authentication methods on managed records and to maintain the results of those authentications to enable the assessment of authenticity of a particular record.  The Record Authentications service depends on the Managed Records service to get information relevant to the record to be authenticated. It also depends on the Record Documents service to get the actual contents of the record.\n\nThe Record Authentications service has operations that return the Authentication Result for the Managed Record, calculates the Authentication Result for the Managed Record and compares it to the Authentication Base, allows changing the Authentication Method of a Managed Record from an old Authentication Method to a new Authentication Method which causes the generation of a new Authentication Base using the new Authentication Method.  In addition, this service allows new Authentication Methods to be put into action as well as marking the retirement date of an Authentication Method.\n",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records ManagementN/A",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0148',
  "TagValue_Number":"2.6.5.7",
  "TagValue_Service Name":"Record Query",
  "TagNotes_Service Definition":"Record Query accepts a request and returns records identifiers that match the query.\n",
  "TagNotes_Service Description":"The Record Query service provides the ability query Managed Records based on the Record Management System data model elements and their relationships. The Record Queries service consists of a single query operation. The query operation takes a string as an input parameter and returns the results as a string. The input parameter qualifies the requested elements (likely provided in XQuery/Xpath string format) to the Records Management Environment.  The operation returns a string that contains the elements that match the request in the form of an XML string formatted according to the Records Management System XML Schema Definition.\n",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0147',
  "TagValue_Number":"2.6.5.6",
  "TagValue_Service Name":"Managed Records",
  "TagNotes_Service Definition":"Managed Records provides the management  of the information related to records that are being controlled by the records management system.",
  "TagNotes_Service Description":"The Managed Records service manages a wide variety of information related to Managed Records including the associations to other Managed Records and Case File Definitions.  Managed Records are the records generated during the course of business that an organization is interested in tracking and includes case files.  The Managed Records service depends on the Record Documents service to manage the actual documents that make up the contents of the record. The Managed Records service maintains the additional metadata about the document required for records management. The Managed Records service also depends on the Change Agent service to maintain information regarding organizations that play various roles with respect to the managed record such as record keeper or record creator.\n\nThe Managed Records service has operations to capture Managed Records, retrieve or destroy the Managed Records and any of the metadata associated with the Manages Record.  This service also allows the Managing Record to be associated with other Managed Records and Case Files.\n",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0146',
  "TagValue_Number":"2.6.5.5",
  "TagValue_Service Name":"Record Documents",
  "TagNotes_Service Definition":"Record Documents provides the management of the documents which comprise the content of the managed records.",
  "TagNotes_Service Description":"The Record Documents service manages the documents which comprise the content of Managed Records.  A Document managed by the records management family is interpreted simply as \"bit strings\" without presumption of form or purpose; in other words, the document contents per se are of no concern to the records management system, although a document type is maintained for use by the consumers of the record management system. Documents can be used in many Managed Records because a single Document can represent evidence of multiple business activities/purposes. When final disposition of a Managed Record in which a document participates occurs (transfer or destroy), the Managed Record is destroyed.   The Document itself is destroyed only when the Managed Record in final disposition is the only one that still refers to the document.\n\nThe Record Documents service has operations to save documents into the records management system, retrieve documents from the records management system and destroy document.",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0145',
  "TagValue_Number":"2.6.5.4",
  "TagValue_Service Name":"Record Dispositions",
  "TagNotes_Service Definition":"Record Dispositions provide for the management of information related to lifecycle of the managed records.\n",
  "TagNotes_Service Description":"The Record Dispositions service includes management of a wide variety of information related to disposition instructions, dispositions plans, and suspensions. When a record is assigned to a record category, a disposition plan is generated based on the disposition instruction. If record category is changed, the disposition plan must be deleted unless the managed record category points to the same disposition instruction.\n\nThe Record Dispositions service has operations to add and remove disposition instructions to disposition plans, associate disposition plans to sets of managed records, return the managed records associated with a disposition plan, return a disposition plan for a specific managed record, and manage suspense events.\n",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0144',
  "TagValue_Number":"2.6.5.3",
  "TagValue_Service Name":"Record Categories",
  "TagNotes_Service Definition":"Record Categories provide the assignment of records to categories defined in a category schema.\n",
  "TagNotes_Service Description":"The Record Categories service manages the association of the Managed Records with some business activity that requires that records of it be kept.  This is accomplished by associating Managed Records to business identified categories. The Record Categories service provides access to record schedules captured a Categorization Schema. The actual setup of the schemas is outside the scope of this service. The functionality provided herein allows for the use of the schema and the application of its categories to Managed Records.\n\nThe Record Categories service provides the ability to assign Managed Records to Record Categories either individually or as a set.  There are operations to add and remove record Categories, manage the association of Managed Records to Record Categories, return information on the Category Schemas and return the Disposition Instruction for specified Record Categories.",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0143',
  "TagValue_Number":"2.6.5.2",
  "TagValue_Service Name":"Record Authorities",
  "TagNotes_Service Definition":"Record Authorities provide the management of the Authorization instances on particular managed records.",
  "TagNotes_Service Description":"The Record Authorities service to manage information about the organizations that have authority for managed records within records management system by maintaining an association between an Authority for a particular records management element and the Change Agent that has that responsibility.  The Record Authorities service manages information regarding the parties with legal authority over the managed records and their annotations using the Change Agent service to manage the data about the Authority. The record Categories service maintains a reference to information in the Authorities service related to the authority for a categorization schema.\n",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"How is this different than ABAC with PDP/PEP?"
},
{
  "TagValue_UID":'0142',
  "TagValue_Number":"2.6.5.1",
  "TagValue_Service Name":"Record Annotations",
  "TagNotes_Service Definition":"This service provides the management of the markup and highlight instances that are associated with particular managed records",
  "TagNotes_Service Description":"The Record Annotations service provides markings on records that help to differentiate them from other records in the same category or across categories. These are typically used to support business needs for special handling or management of the record.  The Record Annotations service has operations that provide the ability to add an annotation to a managed record, retrieve a list of the annotations for a specified managed record and the ability to remove one or all annotations from a particular managed record.",
  "TagValue_Example Specification":"Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.13 Records Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0141',
  "TagValue_Number":"2.6.5",
  "TagValue_Service Name":"Records Management",
  "TagNotes_Service Definition":"The Records Management family of capabilities provides systematic control of the creation, receipt, maintenance, use and disposition of records.",
  "TagNotes_Service Description":"The Records Management family of capabilities provides systematic control of the creation, receipt, maintenance, use and disposition of records, including the processes for capturing and maintaining evidence of and information about business activities and transactions in the form of records. \n\n   • Record Annotations - which provides the management of the markup and highlight instances that are associated with particular managed records\n   • Record Authorities - which provides the management of the Authorization instances on particular managed records\n   • Record Categories - which provides assignment of records to categories defined in a category schema.\n   • Record Dispositions- which provides management of information related to lifecycle of the managed records.\n   • Record Documents- which provides management of the documents which comprise the content of the managed records\n   • Managed Records - which provides management  of the information related to records that are being controlled by the records management system\n   • Record Query - which accepts a request and returns records identifiers that match the query.\n   • Record Authentications- which provides the management of authentication methods and the results of execution of those methods on managed records\n   • Record Attribute Profiles- which maintains the instance data for profiles that have been added to the records management services.\n   • Change Agent- which provides management of information about the individuals that update and modify managed records",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0140',
  "TagValue_Number":"2.6.4.3",
  "TagValue_Service Name":"Data Quality Measurement",
  "TagNotes_Service Definition":"Data Quality Measurement performs data quality measurement and assessment computations that populate the operational quality data portion of the data quality metrics database",
  "TagNotes_Service Description":"The Data Quality Measurement service provides summary counts of the number of business rule violations and calculates the levels of quality of different data quality dimensions.   This is accomplished by comparing Data Quality measurements to established Data Quality standard requirement or desired threshold level for the metric such as: Simple Ratio, Weighted Average, Minimum and Maximum operations. The results of these measurement comparisons are then processed through one or more assessment schemes to identify the level of acceptance (or not) of the quality of the data.",
  "TagValue_Example Specification":"ISO 19100 International Standards",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.34 Validate Data",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0139',
  "TagValue_Number":"2.6.4.2",
  "TagValue_Service Name":"Data Quality Extraction",
  "TagNotes_Service Definition":"Data Quality Extraction processes commands to retrieve specified portions of the data quality metrics database",
  "TagNotes_Service Description":"This service provides access the data quality metrics database to be used by applications the will process the data quality information for presentation and analysis on the quality of the data within the system.  The output of this service will be an XML file that follows the data quality XML Schema.",
  "TagValue_Example Specification":"ISO 19100 International Standards",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.34 Validate Data",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0138',
  "TagValue_Number":"2.6.4.1",
  "TagValue_Service Name":"Data Quality Definition",
  "TagNotes_Service Definition":"Data Quality Definition populates the business tables in the definitional data portion of the data quality metrics database",
  "TagNotes_Service Description":"The Data Quality Definition service is the primary mechanism whereby various entries in the different definitional tables provided by the user. This includes Information Product Types, Data Item Types, Data Object Types, Business Rules, Data Quality metrics, and Data Quality Object Metrics. The Data Quality Definition service provides the storage of the Data Quality metadata in a repository.",
  "TagValue_Example Specification":"ISO 19100 International Standards",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.34 Validate Data",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0137',
  "TagValue_Number":"2.6.4",
  "TagValue_Service Name":"Data Quality",
  "TagNotes_Service Definition":"The Data Quality family of services provides methods and metrics by which the value of the data to the enterprise can be measured to ensure data is accurate, timely, relevant, complete, understood, trusted and satisfies intended use requirements.",
  "TagNotes_Service Description":"The Data Quality family of services provides methods and metrics by which the value of the data to the enterprise can be measured to ensure data is accurate, timely, relevant, complete, understood, trusted and satisfies intended use requirements.\n\n   • Data Quality Definition - which populates the business tables in the definitional data portion of the data quality metrics database\n   • Data Quality Extraction - which processes commands to retrieve specified portions of the data quality metrics database\n   • Data Quality Measurement  - which performs data quality measurement and assessment computations that populate the operational quality data portion of the data quality metrics database",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"The below 3 items are all part of the EDQMS.  Not sure whether it is worth pulling them out as separate items"
},
{
  "TagValue_UID":'0131',
  "TagValue_Number":"2.6.1.8",
  "TagValue_Service Name":"Content Policy",
  "TagNotes_Service Definition":"Content Policy is used to apply or remove a policy object to an object that is under the control of a policy",
  "TagNotes_Service Description":"The Content Policy service is used to apply or remove a policy object to a content object. A policy object represents an administrative policy that can be enforced by a repository, such as a retention management policy. Each policy object holds the text of an administrative policy as a repository-specific string, which may be used to support policies of various kinds. A repository may create subtypes of this base type to support different kinds of administrative policies more specifically.  Policy objects are created by the Object Processing service and manipulated by this Content Policy service.  The service has operations to apply a specified policy to an content object, get the list of policies currently applied to a specified content object and remove a specified policy from a specified content object.",
  "TagValue_Example Specification":"OASIS Content Management Interoperability Services (CMIS) Version 1.0",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7 Manage Data (Group)",
  "TagValue_JARM/ESL Alignment":"8.10 Content Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0130',
  "TagValue_Number":"2.6.1.7",
  "TagValue_Service Name":"Object Relationship",
  "TagNotes_Service Definition":"Object Relationship is used to retrieve the dependent relationship objects associated with an independent object",
  "TagNotes_Service Description":"The Object Relationship service is used to retrieve the dependent relationship objects associated with an independent object that were created with the Object Processing service.  The service gets all or a subset of relationships associated with an independent object.  In addition the service allows the consumer to specify whether the service returns relationships where the specified content object is the source of the relationship, the target of the relationship, or both.",
  "TagValue_Example Specification":"OASIS Content Management Interoperability Services (CMIS) Version 1.0",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7 Manage Data (Group)",
  "TagValue_JARM/ESL Alignment":"8.10 Content Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0129',
  "TagValue_Number":"2.6.1.6",
  "TagValue_Service Name":"Content Versioning",
  "TagNotes_Service Definition":"Content Versioning is used to navigate or update a document version series",
  "TagNotes_Service Description":"The Content Versioning service is used to navigate or update a document version series. The Content Versioning service provides a check-out operation that gets a the latest document object in the version series, allows a private working copy of the document to be created and provides a  checks-in operation that checks-in a private working copy document as the latest in the version series. In addition, the Content Versioning service provides an operation that reverses the effect of a check-out by removing the private working copy of the checked-out document, allowing other documents in the version series to be checked out again.  Finally there is a service to retrieve a the list of all Document Objects in the specified Version Series.",
  "TagValue_Example Specification":"OASIS Content Management Interoperability Services (CMIS) Version 1.0",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7 Manage Data (Group)",
  "TagValue_JARM/ESL Alignment":"8.10 Content Management",
  "TagNotes_Comments":"Why isn't this just Document CM?"
},
{
  "TagValue_UID":'0128',
  "TagValue_Number":"2.6.1.5",
  "TagValue_Service Name":"Managed Content Discovery",
  "TagNotes_Service Definition":"Managed Content Discovery executes a query statement against the contents of a content repository",
  "TagNotes_Service Description":"The Managed Content Discovery service (query) is used to search for query-able objects within the content repository.  The Managed Content Discovery service provides a type-based query service for discovering objects that match specified criteria,  The semantics of this query language is defined by applicable query language standards in conjunction with the model mapping defined by the relational view of the content management object repository.  This service also provides and operation to get a list of content changes to be used by search crawlers or other applications that need to efficiently understand what has changed in the repository.",
  "TagValue_Example Specification":"OASIS Content Management Interoperability Services (CMIS) Version 1.0",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7 Manage Data (Group)",
  "TagValue_JARM/ESL Alignment":"8.10 Content Management",
  "TagNotes_Comments":"If this is for queries against a content repository, then CDW fits.  If it is a query against a document  repository, then CDW might fit.  Better to call this Managed Document Discovery, perhaps.  In any case, it seems better to provide crawler support and then the index that is the results of the crawling would be searchable via CDW."
},
{
  "TagValue_UID":'0127',
  "TagValue_Number":"2.6.1.4",
  "TagValue_Service Name":"Object Folders",
  "TagNotes_Service Definition":"The Object Folders service is used to file and un-file objects into or from folders",
  "TagNotes_Service Description":"The Object Folders service supports the multi-filing and unfiling services. Multi-filing allows the same non folder content object to be to be filed in more than one folder.  The Object Folders service allows existing fileable non-folder objects to be added to a folder and allows existing fileable non-folder object to be removed from a folder.  This service is NOT used to create or delete content objects in the repository.  The content objects must have previously been created by the Object Processing service to be filed and a content object that is removed from a folder remains persistent in the content repository.",
  "TagValue_Example Specification":"OASIS Content Management Interoperability Services (CMIS) Version 1.0",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7 Manage Data (Group)",
  "TagValue_JARM/ESL Alignment":"8.10 Content Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0126',
  "TagValue_Number":"2.6.1.3",
  "TagValue_Service Name":"Object Processing",
  "TagNotes_Service Definition":"Object Processing provides ID-based Create, Retrieve, Update, Delete (CRUD), operations on objects in a repository.",
  "TagNotes_Service Description":"The Object Processing service provides Create, Retrieve, Update, Delete (CRUD) manipulation on the objects in a content repository. Specifically, the Object Processing service allows for the creation of a document object of the specified type in the specified location within the tree of folders and creates relationship objects of the specified types that are used to specify how content objects can be related. A relationship object is an explicit, binary, directional, non-invasive, and typed relationship between a specified source object and a specified target object.  Information contained in the properties of the content objects can be created, retrieved and updated.  The content objects, such as documents, themselves can be  created, retrieved deleted also content objects can be moved from one folder to another.",
  "TagValue_Example Specification":"OASIS Content Management Interoperability Services (CMIS) Version 1.0",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.14  Delete Data\n8.7.19  Categorize Data\n8.7.25  Register Data\n8.7.28  Provide Database Utilities\n8.7.36  Post or Publish Data\n8.7.39  Aggregate Data\n8.7.71  Store Semantic Information on Data\n8.7.66  Post Metadata to a Discovery Catalog\n8.7.67  Post Metadata to a Federated Directory\n8.7.68  Post Metadata to Metadata Registries\n8.7.89  Provide Discovery Metadata for Situational Awareness\n8.7.14  Delete Data",
  "TagValue_JARM/ESL Alignment":"8.10 Content Management",
  "TagNotes_Comments":"Note that RESTful does this via hyperlinks and content representation, and scales  better."
},
{
  "TagValue_UID":'0125',
  "TagValue_Number":"2.6.1.2",
  "TagValue_Service Name":"Content Navigation",
  "TagNotes_Service Definition":"Content Navigation is used to traverse the folder hierarchy in a repository, and to locate Documents that are checked out",
  "TagNotes_Service Description":"The Content Navigation service provides operations to retrieve objects from a tree of folder objects that have the following constraints:\n• Every folder object, except for one which is called the root folder, has one and only one parent folder. The Root Folder does not have a parent.\n• A folder object cannot have itself as one of its descendant objects.\n• A child object that is a folder object can itself be the parent object of other file-able objects such as other folders and documents.\n• The folder objects in a content repository form a strict hierarchy, with the Root Folder being the root of the hierarchy.\nThe Content Navigation service provides the retrieval of a list of child objects that are contained in a specified folder, a set of descendant objects contained in a specified folder or any of its child-folders.  In addition, the Content Navigation service provides the retrieval of the parent folder object for a specified folder object a list of documents that are checked out to which the user has access.",
  "TagValue_Example Specification":"OASIS Content Management Interoperability Services (CMIS) Version 1.0",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.1.78 Query Global Directory Service",
  "TagValue_JARM/ESL Alignment":"8.10 Content Management",
  "TagNotes_Comments":"Isn't this really more like Document CM?  Does Wikipedia satisfy this?  What if the documents are not in folders (e.g., hypermedia)?  Recommend re-naming to \"folder navigation\" because that is more consistent with the definition."
},
{
  "TagValue_UID":'0124',
  "TagValue_Number":"2.6.1.1",
  "TagValue_Service Name":"Content Repository",
  "TagNotes_Service Definition":"Content Repository is used to discover information about repositories and the object-types defined for the repository.",
  "TagNotes_Service Description":"The Content Repositories service has operations that return a list of existing content repositories available from the Content Management service endpoint; information about the content repository and its Access Control information.  The Content Repositories service also provides operations that return the definition of a specified content object type as well as list of content object types that are children or decedents of a specified content object type.",
  "TagValue_Example Specification":"OASIS Content Management Interoperability Services (CMIS) Version 1.0",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.1.31 Maintain Global Directory Service\n8.7.19 Categorize Data",
  "TagValue_JARM/ESL Alignment":"8.10 Content Management",
  "TagNotes_Comments":"Isn't this more like \"content repository discovery\" or \"content repository registry\"?  Actually, it may need to be \"document repository discovery/registry\""
},
{
  "TagValue_UID":'0123',
  "TagValue_Number":"2.6.1",
  "TagValue_Service Name":"Content Management",
  "TagNotes_Service Definition":"Content Management provides services that organize and facilitate the  collaboration and publication of documents and other enterprise content.",
  "TagNotes_Service Description":"Content Management services organize and facilitate the  collaboration and publication of documents and other enterprise content.\n\nSpecific services include: \n\n    • Content Repository - which is used to discover information about repositories and the object-types defined for the repository\n    • Content Navigation  - which traverses repository folder hierarchies and locates checked out documents\n    • Object Processing  - which provides ID-based Create, Retrieve, Update, Delete (CRUD), operations on repository objects \n    • Object Folders  - which files and un-files folder objects \n    • Managed Content Discovery  - which executes a query statement against the content repository\n    • Content Versioning   - which navigates or updates a document version series\n    • Object Relationship   - which retrieves the dependent relationship objects associated with an independent object\n    • Content Policy  - which applies or removes policy objects \n    • Content Access Control List This service processes the access control list (ACL) associated with repository objects",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"This should be \"document management\"  Note that the CMIS is for \"controlling diverse document management systems and repositories using web protocols\".  I.e., it is document focused vice data focused.  I would recommend combining all of the below into a document management service and list the sub-services as functionality that such a service should provide - but don't call them all out separately as their own 4th level item."
},
{
  "TagValue_UID":'0122',
  "TagValue_Number":"2.6",
  "TagValue_Service Name":"Data Handling",
  "TagNotes_Service Definition":"The Data Handling line of capabilities includes the data management and processing functions used to maintain and manage DI2E data stores.",
  "TagNotes_Service Description":"The Data Handling line of capabilities includes the data management and processing functions used to maintain and manage DI2E data stores.  Included families of capabilities provide content management, data quality control, and records management.",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"From definitions below, this is not \"data handling\" bot \"document handling\".  This is an important distinction, as data that is not in document format will need to be handled differently than data in document format"
},
{
  "TagValue_UID":'0243',
  "TagValue_Number":"2.5.1.4",
  "TagValue_Service Name":"Data Commenting",
  "TagNotes_Service Definition":"Data Commenting services allow the users to enrich the existing data by adding comments on the data.",
  "TagNotes_Service Description":"Data Commenting enables users to comment and collaborate on existing data within the system.  The comment service allows users to enrich the data set without altering the original product.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7.19 Categorize Data",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0156',
  "TagValue_Number":"2.5.1.3",
  "TagValue_Service Name":"Categorize Content",
  "TagNotes_Service Definition":"Categorize Content analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy.",
  "TagNotes_Service Description":"Categorize Content analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy. This is done to allow the user to browse for relevant content vs. key word search.\n\nFor example, a piece of content may be related to the USS Roosevelt. The if proper like categorized the user would be able to discover this content via traversing a taxonomy such as: Ships -> Military Ships -> US -> Air",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7.21  Correlate Data",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"Similar to 3.5.1.1, need COI taxonomies. {Nguyen, Phuc N. Phuc.Nguyen@l-3com.com, 10 Aug, 11}"
},
{
  "TagValue_UID":'0155',
  "TagValue_Number":"2.5.1.2",
  "TagValue_Service Name":"Entity Association",
  "TagNotes_Service Definition":"Entity Association establishes and records relationships between data objects for use in advanced analytical processing and reporting.",
  "TagNotes_Service Description":"Entity Association discovers and records in persistent data stores various analytic relationships that might exist among disparate database objects.   These stored relationships can then be used by Analytic Processing services to more easily import, analyze, and report various associations, trends, groupings, or status that might not be found using more traditional Data Content Discovery services .       \n\nExamples of relationships might include (but not necessarily limited to) spatial, temporal, event based, or data content relationships.   Complex combinations of various relationships are also possible.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7.4 Analyze Data\n8.7.19 Categorize Data\n8.7.21 Correlate Data\n8.7.43 Mine Data\n8.7.47 Synchronize Data\n8.7.55 Establish Discovery Catalogs\n8.7.59 Manage Encyclopedic Database\n8.7.99 Produce Information Mapping and Taxonomy\n8.7.111 Associate Data Characteristics",
  "TagValue_JARM/ESL Alignment":"6.03.04 Data Mining Services; 8.18.04 Data Mining",
  "TagNotes_Comments":"Need  COI ontology models to represent key entities, workflows and business rules. Automated (or machine-assisted) entity extraction and link discovery need lots of intelligence wired into the data layer. Current state-of-the-art still requires human in most analytic workflows. Take a look at NSA's Blackbook. {{Nguyen, Phuc N. Phuc.Nguyen@l-3com.com, 10 Aug, 11}"
},
{
  "TagValue_UID":'0154',
  "TagValue_Number":"2.5.1.1",
  "TagValue_Service Name":"Entity Extraction",
  "TagNotes_Service Definition":"Entity Extraction extracts specific data from unstructured text.",
  "TagNotes_Service Description":"Entity extraction scans text for semantic meaning and identifies key metadata and/or concepts in the document based on a semantic understanding of the language. It can identify things such as people, places, events, objects, etc.    It may also extract relationships such as “Muhammad Omani met has connections with suspected Taliban fighters”.    Information in the document may be tagged using standards such as Resource Description Framework (RDF) or Web Ontology Language (OWL).",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7.70 Re-Map Information",
  "TagValue_JARM/ESL Alignment":"6.03.04 Data Mining Services; 8.18.04 Data Mining",
  "TagNotes_Comments":"For automated asset ingest workflows, best when provided with COI ontology models or at least controlled vocabulary sets (dictionary, taxonomy, thesaurus). Take a look at USAF's AMPS (Auto Metadata Population Services). {Nguyen, Phuc N. Phuc.Nguyen@l-3com.com, 10 Aug, 11}.\n\nThis is, in general, associated with cloud based analytics"
},
{
  "TagValue_UID":'0153',
  "TagValue_Number":"2.5.1",
  "TagValue_Service Name":"Data Enrichment",
  "TagNotes_Service Definition":"The Data Enrichment family of capabilities provide analysis and enhancement of sets of data elements.",
  "TagNotes_Service Description":"The Data Enrichment family of capabilities provide analysis and enhancement of sets of data elements by providing semantics and extracting non-obvious information from datasets.  Specific capabilities include:\n\n• Entity Extraction - which extracts specific data from text documents.  \n• Entity Association - which establishes and records relationships between data objects for use in advanced analytical processing and reporting.\n• Categorize Content - which analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy. \n• Chat Monitor - which provides alerts and/or query of multiple chat rooms through detection of key words or other user specified events.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"This sounds a lot like data analytics.  I wonder if we should come up with a \"data analytics\" process flow, and then look for tools that meet those processes (e.g., data cleansing, entity extraction, entity association, validation, etc.)."
},
{
  "TagValue_UID":'0152',
  "TagValue_Number":"2.5",
  "TagValue_Service Name":"Data Analytics",
  "TagNotes_Service Definition":"Data Analytics provide advanced analytics by finding non-intuitive or non-trivial relationships within and among DI2E data holdings.",
  "TagNotes_Service Description":"Data Analytics provide advanced analytics by finding non-intuitive or non-trivial relationships within and among DI2E data holdings.   In particular several Data Enrichment services providing entity extraction and association, content categorization, and monitoring of chat room content.",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"This is interesting, and needs some more thought.  Should probably have categories based on the kinds of data being analyzed.  For example, the tools used for processing signet data/textual data/imagery metadata will include NLP tools - whereas tools for imagery, or geospatial data analytics will likely be different (e.g., blast coverage, radio coverage, target recognition).  There are also data analytics \"engines\" - i.e., tools that run data analytics algorithms (e.g., HDFS/MapReduce) and tools for creating algorithms (e.g., OpenNLP, NLTK, SharpNLP, etc.).  This is really too wide a category, and I would recommend that it be further decomposed into tools/rules engines and rules/algorithms.  We could further categorize into types of data being analyzed, and perhaps types of analysis (e.g., NLP, Imagery, Numbers, etc.)"
},
{
  "TagValue_UID":'0135',
  "TagValue_Number":"2.4.1.6",
  "TagValue_Service Name":"Data De-Duplication",
  "TagNotes_Service Definition":"Data De-Duplication provides a data compression technique for eliminating coarse-grained redundant data.",
  "TagNotes_Service Description":"Data De-Duplication provides a data compression technique for eliminating coarse-grained redundant data. In the de-duplication process, duplicate data is deleted, leaving only one copy of the data to be stored, along with references to the unique copy of data.  The focus of this service is on file or record level data and not on the data storage block level of data.  Although and data de-duplication method does save storage space, the emphasis here is providing analyses to ensure that a copy of an intelligence report, for instance, is the single authoritative copy in the system.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.17 Cleanse Data\n8.7.120 Deconflict Duplicate Data",
  "TagValue_JARM/ESL Alignment":"5.01.03 Mediation Services",
  "TagNotes_Comments":"Is this really for file or record compression, or for helping analysts on query results?  For the first case, there is plenty of open source software that exists, and they are generally used to improve storage efficiency (e.g., often used on backup).  If the latter, then it needs to be employed in \"data clouds\" and/or associated with processing federated query results.  Need clarity on the description of what you are looking for."
},
{
  "TagValue_UID":'0119',
  "TagValue_Number":"2.4.1.5",
  "TagValue_Service Name":"Image Transformation",
  "TagNotes_Service Definition":"Image Transformation converts one image format to another.",
  "TagNotes_Service Description":"The Image Format Conversion service converts one image format to another, typically in order to ensure that image data is in a format that can be ingested by specific applications or services.  There are many hundreds of different image formats used. Some of these are in broad commercial or government use. Others may be restricted to highly specialized purposes.  \n\nIncluded in the list of data products that may require image format conversion are many types of products that include 2-dimensional data array structures as part of their formats. Examples showing the diversity of image products are  raster formatted Geographic Information System (GIS) products, multiband images such as hyperspectral or multispectral imagery, digital handheld camera photos, scanned documents, and faxes.  In addition to the diversity of products and associated applications, image formats also often include image compression encodings that result in  specified degrees of reduction in image storage size.  Image encryption is also sometimes part of an image format.\n\nAn image format conversion service ingests an image in one format and outputs the image in a second format.  Besides the image input, additional inputs include the specification of the input format (with the default being that the input format is autodetected by the service) and the target destination image format.   \n\nThis service differs from Schema Transformation (in Data Mediation Line) in that Image Format conversion translate image file formats (example .jpeg formatted image to a .gif formatted image) whereas Schema Transformation converts data field layouts (schemas) from one data field arrangement to another (example: xsd schema to fields in a relational database).",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.3.1 Data Transformation (PE)",
  "TagNotes_JCSFL Alignment":"14.1.9  Convert Image Format",
  "TagValue_JARM/ESL Alignment":"5.01.03 Mediation Services",
  "TagNotes_Comments":"Image transformation has also to do with orthorectification, 2D to 3D, rotation, scale, etc.   I think the title you are looking for that matches the definition is \"image format conversion\".  Plenty of on-line tools as well as downloadable tools to leverage - both piecemeal or batch."
},
{
  "TagValue_UID":'0121',
  "TagValue_Number":"2.4.1.4",
  "TagValue_Service Name":"Schema Transformation",
  "TagNotes_Service Definition":"Schema Transformation transforms data from adhering to one schema to adhering to another schema.",
  "TagNotes_Service Description":"Schema Transformation converts data organized in an original schema to a target schema.  The term schema is used here to mean any way in which data might be organized, this may or may not be xsd or the schema of a relational database.  This service transforms data from adhering to one schema to adhering to another schema.  For example, converts from ADatP-3 to USMTF or convert XML data between different XML schemas.  Together, Format Validation and Format Transformation complete the process of ensuring that a service is provided data organized according to the needs of that service.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.1 Information Sharing",
  "TagNotes_JCSFL Alignment":"8.7.5 Translate Data\n8.7.16 Migrate Data\n8.7.40  Format Data",
  "TagValue_JARM/ESL Alignment":"5.01.03 Mediation Services",
  "TagNotes_Comments":"I'm thinking that schema translation should  really be combined with Data Transformation.  For example, XML schemas are used in XSLT to translate both the data and the elements to which the data is mapped.  Generally, included in ESB products"
},
{
  "TagValue_UID":'0120',
  "TagValue_Number":"2.4.1.3",
  "TagValue_Service Name":"Data Transformation",
  "TagNotes_Service Definition":"Data Transformation converts a data value form one format to another. For example convert Latitude from alphanumeric DDDMMSS[N/S] to numeric [+/-] seconds from equator, or feet to meters.",
  "TagNotes_Service Description":"Data Transformation converts a data value form one format to another. For example convert Latitude from alphanumeric DDDMMSS[N/S] to numeric [+/-] seconds from equator, or feet to meters.  While the Data Validation service checks for correctness against a defined set of rules, it is the Data Transformation service that will change data values based on a set of defined rules.  Together, Data Validation and Data Transformation complete the process of ensuring that a service operates on clean, correct and useful data.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.1 Information Sharing",
  "TagNotes_JCSFL Alignment":"8.7.15  Transform Data\n8.7.40 Format Data\n8.7.54 Convert Analog Data to Digital Data\n8.7.104  Convert Data File Format",
  "TagValue_JARM/ESL Alignment":"6.01.03 Data Transformation Services",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0117',
  "TagValue_Number":"2.4.1.2",
  "TagValue_Service Name":"Data Validation",
  "TagNotes_Service Definition":"Data Validation validates that data values are correct based on rules defined for the data. For example, may validate Latitude as conforming to a DDDMMSS[N/S] format.",
  "TagNotes_Service Description":"Data Validation uses routines, often called \"validation rules\" or \"check routines\", that check for correctness, meaningfulness, and security of data that are input to the system. The rules may be implemented through the automated facilities of a data dictionary, or by the inclusion of explicit application program validation logic. For business applications, data validation can be defined through declarative data integrity rules, or procedure-based business rules. Data that does not conform to these rules will negatively affect business process execution. Therefore, data validation should start with business process definition and set of business rules within this process. Rules can be collected through the requirements capture exercise. The simplest data validation verifies that the characters provided come from a valid set. For example, that data stored as YYYYMMDDHHMM have exactly 12 digits and  the MM is between 01 and 12 inclusive. A more sophisticated data validation routine would check to see the user had entered a valid country code, i.e., that the number of digits entered matched the convention for the country or area specified. Incorrect data validation can lead to data corruption or a security vulnerability. Data validation checks that data are valid, sensible, reasonable, and secure before they are processed.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.1 Information Sharing",
  "TagNotes_JCSFL Alignment":"8.7.7  Maintain Data Integrity\n8.7.34  Validate Data\n8.7.44 Mediate Data\n8.7.76 Provide Data Mediation\n8.7.98  Perform Data Check",
  "TagValue_JARM/ESL Alignment":"5.01.03 Mediation Services",
  "TagNotes_Comments":"This generally comes with the above.  The schemas, in general, define not only what the allowable elements and attributes are of a given document, but what the allowable values for those elements and attributes are.  Recommend combining with the above"
},
{
  "TagValue_UID":'0116',
  "TagValue_Number":"2.4.1.1",
  "TagValue_Service Name":"Schema Validation",
  "TagNotes_Service Definition":"Schema Validation validates whether or not data adheres to an identified schema.",
  "TagNotes_Service Description":"Schema Validation validates that a collection of data (e.g. USMFT message, XML document, etc.) conforms to specified schema. Schema is used here to mean any way in which data might be organized, which may or may not be xsd or the schema of a relational database.  This service validates whether or not data adheres to an identified schema.  This service will take a schema plus a collection of data or instance document and list any errors found in validating the document against the defined schema.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.1 Information Sharing",
  "TagNotes_JCSFL Alignment":"8.7.19 Categorize Data",
  "TagValue_JARM/ESL Alignment":"5.01.03 Mediation Services",
  "TagNotes_Comments":"Need to re-consider the definition.   For example, 1.2.7.2 (Security Label Format Validation) would fit this description.  I think that schema validation tools would be a good item for here, but not the actual schema validation itself.   For example, validating a .csv file would probably take a custom piece of code.  On the other hand, Schematron and XML Schema validation tools can work with different unique schemas, but the tools themselves would be re-usable.  Also worth listing the different types of schema languages for XML (e.g., .xsd, RelaxNG, Schematron), OWL, RDF, and JSON along with their associated validation tools.  Note that the tools will come in the form of on-line tools, downloadable tools, and software libraries that you leverage."
},
{
  "TagValue_UID":'0115',
  "TagValue_Number":"2.4.1",
  "TagValue_Service Name":"Data Preparation",
  "TagNotes_Service Definition":"Data Preparation includes services that confirm and applicably convert data.",
  "TagNotes_Service Description":"Data Preparation services verify that incoming data records follow specified formats and schema and, when needed, output these records using specified formats and schema for usable ingest by consumer services.  \n\nServices in this family are often used in combination since format/schema errors found during validation often necessitate subsequent transformation.    \n\n(note: schema is used here to mean however the data might be organized - whether through xsd specifications, relational database structure, or other means.)",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0114',
  "TagValue_Number":"2.4",
  "TagValue_Service Name":"Data Mediation",
  "TagNotes_Service Definition":"Data Mediation includes services that enable the dynamic resolution of representational differences among disparate data on the behalf of a service consumer.",
  "TagNotes_Service Description":"Data Mediation services enable the dynamic resolution of representational differences among disparate data on the behalf of a service consumer. There will be multiple data formats within the enterprise representing all types of data. In order to support multiple data consumers, the data must be mediated into the format and schema recognizable by the data consumer.  Schema is used here to mean any way in which data might be organized, which may or may not be xsd or the schema of a relational database.",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0160',
  "TagValue_Number":"2.5.2.3",
  "TagValue_Service Name":"Audio Monitor",
  "TagNotes_Service Definition":"Audio Monitoring enables tracking, recording, scanning, filtering, blocking, reporting, and logging of specific information in audio feeds.",
  "TagNotes_Service Description":"TBD",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"TBD",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0159',
  "TagValue_Number":"2.5.2.2",
  "TagValue_Service Name":"Video Monitor",
  "TagNotes_Service Definition":"Video Monitoring enables tracking, recording, scanning, filtering, blocking, reporting, and logging of specific information in video feeds",
  "TagNotes_Service Description":"TBD",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"TBD",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0158',
  "TagValue_Number":"2.5.2.1",
  "TagValue_Service Name":"Chat Monitor",
  "TagNotes_Service Definition":"Chat Monitor provides alerts and/or query of multiple chat rooms through detection of key words or other user specified events.",
  "TagNotes_Service Description":"Chat Monitor provides real-time chat monitoring along with chat log processing to enable additional awareness of current and historic chat content.\n\nCorresponding alerts and filtering may be based on key words, pattern matching, thesaurus, or gazetteer information.   A direct querying functionality is also provided.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"TBD",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0157',
  "TagValue_Number":"2.5.2",
  "TagValue_Service Name":"Media Monitoring",
  "TagNotes_Service Definition":"Media Monitoring services are services that can track or record data activity that may be of interest to a user or system.",
  "TagNotes_Service Description":"Media Monitoring services are services that can track or record data activity that may be of interest to a user or system. Monitoring services may look for levels of activity (e.g., data transfer rates), particular values (e.g., keywords), or other values of interest.\n\nAlerting services, automated maintenance services, and similar capabilities can be constructed on top of monitoring services to relieve end users of the task of manually tracking a multitude of data sources looking for items of interest. For example, an audio monitoring service might look at all audio feeds waiting for a audio signal that matches the audio signature of the name of a person of interest and pass that to an alerting service.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0103',
  "TagValue_Number":"2.3.1.7",
  "TagValue_Service Name":"Query Results Management",
  "TagNotes_Service Definition":"Query Results Management allows editing (adding, removing, modification) of query results as well as sharing and distributing of query results among  users",
  "TagNotes_Service Description":"Query Results Management enables further processing of returned results to customize for further analysis or sharing of search results with others.\n\nExamples of what Query Results Management might providing include enabling adding a new record manually from an artifact the search did not find, editing the description of the search hit to make it more clear for others, or removing less relevant hits in order to provide focus to those hits that remain. \n\nIn some advanced applications, query results management might also enable sending out final edited results, automatically or at the user’s request, to other identified users or applications.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7.96  Review Information Search Results",
  "TagValue_JARM/ESL Alignment":"8.29.05 Aggregated Search Results",
  "TagNotes_Comments":"Is Query Results Management duplicative with other Content Management services?  (to be considered after specification review). - TEM with DMO, 13 October, 2011\n\nThis sounds like workspaces combined with a results factoring/facets and pub/sub (or email), etc.  Also, recommend changing description to not be limited to DCGS users."
},
{
  "TagValue_UID":'0102',
  "TagValue_Number":"2.3.1.6",
  "TagValue_Service Name":"Query Management",
  "TagNotes_Service Definition":"Query Management allows consumers to build, store, retrieve, update, and delete pre-established search and query criteria.",
  "TagNotes_Service Description":"Many  analysts periodically or repetitively execute the same, or  highly similar, search queries.   In more advanced applications, the structure of the queries can be complex and require considerable time and/or skill to establish.    The query management service helps support this operational requirement by providing a tool that enables  analysts to establish, reuse, and share queries for later or other analyst’s use.\n\nQuery Management services accomplish this by supporting the maintenance of query repositories that offer queries for reuse, often with minimal or no additional editing or query parameter analysis.    The SvcV-4 is not prescriptive as the organization relationships of  query repositories (by user, by organization, by role, etc.) but does point to the specifications listed in the Service Portfolio Management Tool (SPMT) for guidance (and once CDP approved, conformance requirement) in the applications or services that present query repositories.    In doing so, it encourages search criteria management services to operate in a manner that promotes establishment and provisioning of stored queries for sharing and use by disparate systems and users across the  Enterprise.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"1.4.4 Control Search Parameters\n8.7.8  Query Data",
  "TagValue_JARM/ESL Alignment":"8.29.07 Query Management",
  "TagNotes_Comments":"On the one hand, this seems like it would work in conjunction with workspaces, shared workspaces, event notifications/rules engines, etc.  On the other hand,  this might just be a federated query specific function.  If the latter, then we still need to work out a CONOPS for how federated queries will work with pub/sub and event notifications/rules, etc.  \n\nThis will require some architecture as well"
},
{
  "TagValue_UID":'0379',
  "TagValue_Number":"2.3.1.5",
  "TagValue_Service Name":"Describe Content",
  "TagNotes_Service Definition":"The Describe Content service enables content repositories to publish information describing their content collections and content resources to the enterprise.  It also provides interested parties with a description of the resource and how it can be accessed or used. \n\n-- CDR Reference Architecture and Specification Framework\n\nhttps://metadata.ces.mil/dse/documents/DoDMWG/2010/04/2010-04-13_CDRIPT.ppt\n",
  "TagNotes_Service Description":"Describe Content serves as the primary mechanism for content providers to expose information to describe the context, access constraints, and current inventory status of the underlying content resources, and the exposed information will support static and dynamic discovery and accessibility of a content collection. Search and Brokered Search leverage the output of this component to determine whether the content collection may contain content resources that are relevant to the consumer’s query. To support a wide array of use cases, the Describe Component should reflect both the static9 and dynamic10 information about the underlying content collection.\n--CDR Reference Architecture",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"0",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0109',
  "TagValue_Number":"2.3.1.4",
  "TagValue_Service Name":"Deliver Content",
  "TagNotes_Service Definition":"Deliver Content enables content to be sent to a specified destination, which may or may not be the requesting component.",
  "TagNotes_Service Description":"Deliver Content enables content to be sent to a specified destination, which may or may not be the requesting component. In its simplest form, Deliver Content will take a consumer-supplied payload and send it to another consumer as specified in the delivery property set. For instance, if an analyst discovers a relevant data resource from a Data Discovery feed on her PDA, she might want to access and route that data content to her desktop computer so that she may review it later. The Retrieve Content service facilitates this use case through its use of WS-Addressing, but it requires a companion asynchronous callback interface to ultimately accept the routed data resource. This interface is captured by the Deliver Content  service. Also may include additional processing, such as compression, encryption, or conversion that makes delivery of the payload suitable for its destination and the delivery path to be used.  The terms Deliver and Receive are both used in the Content  Discovery and Retrieval Architecture to describe the service with this service.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"7.1.7  Transfer Data\n8.7.24 Manage Information Delivery\n8.7.100 Provide Information Delivery Vehicle",
  "TagValue_JARM/ESL Alignment":"8.29.08 Delivery Management",
  "TagNotes_Comments":"This is part of the CD&R specs.  Note the overlap in the description with Retrieve Content (sending to another destination)."
},
{
  "TagValue_UID":'0108',
  "TagValue_Number":"2.3.1.3",
  "TagValue_Service Name":"Retrieve Content",
  "TagNotes_Service Definition":"Retrieve Content serves as the primary content access mechanism. It encompasses the service to retrieve identified content and to initiate the delivery of the retrieved content to a designated location.",
  "TagNotes_Service Description":"The Retrieve Content service serves as the primary content access mechanism. It encompasses the service to retrieve identified content from the content collection in which it is stored and to initiate the delivery of the retrieved content to a designated location. The delivery of the content can be a return directly back to the requester or can use the Deliver Content service to redirect the response and comply with other handling instructions as supplied by the requester. It cannot redirect output to a component other than the requestor.  \nThe Retrieve Content service provides a common interface and behavioral model for Intelligence Community (IC) and Department of Defense (DoD) content collections, enabling content consumers to retrieve content from disparate collections across the IC/DOD enterprise. Specifically, it provides a means to accept a uniform syntax and semantics that can be transformed, as needed, and applied to newly-developed or existing content collections. Thus, it is unambiguously conveying a request for the content without knowing or setting requirements on the implementation of the underlying content collection.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"6.2.3.4 Content Delivery",
  "TagNotes_JCSFL Alignment":"8.7.27  Retrieve Data\n8.7.91 Acquire Data\n8.7.121 Receive Queried Information",
  "TagValue_JARM/ESL Alignment":"8.29.01 Retrieval",
  "TagNotes_Comments":"This is part of the CD&R specs."
},
{
  "TagValue_UID":'0376',
  "TagValue_Number":"2.3.1.2",
  "TagValue_Service Name":"Brokered Search",
  "TagNotes_Service Definition":"Brokered Search serves as the primary mechanism to 1) facilitate the distribution of queries to applicable/relevant content collections (exposed as Search Components) and 2) process the returned results.\n--CDR Reference Architecture\n",
"TagNotes_Service Description":"The Brokered Search service allows an entity to search multiple, independent resources or data repositories and retrieve a combined list of search results.  Brokered searches may be confined to the organization, or may be federated, allowing an entity to simultaneously search data stores in multiple organizations.  Rather than returning the identified products (which could number in the millions), a brokered search returns metadata about each matching product.  This metadata includes information to assist the entity in choosing products to retrieve  (such as the author, a description, abstract, or summary), as well as information about the resource that holds the product.  The entity then queries the repository directly to retrieve the actual product.\nhttp://www.dni.gov/index.php/about/organization/chief-information-officer/cdr-brokered-search.",
"TagValue_Example Specification":"none",
"TagValue_Example Solution":"<memo>",
"TagValue_DI2E Framework Status":"2",
"TagValue_DCGS Enterprise Status":"0",
"TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
"TagNotes_JCSFL Alignment":"",
"TagValue_JARM/ESL Alignment":"(gap)",
"TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0099',
  "TagValue_Number":"2.3.1.1",
  "TagValue_Service Name":"Content Search",
  "TagNotes_Service Definition":"Content Search provides a standard interface for discovering information, returning a 'hit list' of items which can then be retrieved.\n\nFederated Search transforms a search query into useful form(s), broadcasts is to multiple disparate databases, merges the results, and presents results is a succinct, organized format.",
  "TagNotes_Service Description":"Content Search provides a standard interface for discovering information in unstructured and semi-structured data stores.  This service searches Meta data tagged Imagery of various types and Free Text reports, articles, or documents.   Each item to be searched may have metadata associated with it, conforming to enterprise standards.  \n\nThe interface specifies criteria such as Author=”name” or “Published time later than some date” as ways of identifying items of interest.   Additionally the search of the body and/or meta data tags can be performed using text search strings, such as “(SOA OR Service) NEAR Component”.   The precise query language grammar will be specified in the service specification package and will include the following functionality: Boolean and, Boolean or, Boolean not, groupings, proximity, and wildcards.\n\nThe Federated Search service will simultaneously search multiple specific search resources through a single query request by distributing the search request to participating search engines.   Results are aggregated and results are processed for presentation to the user.\n\nOperations typically might include: (1) checking an incoming query for appropriate content and form, (2) analyzing which search resources to query for results, (3) transforming the query request to forms appropriate search requests for each search resource to be contacted, (4) executing a search request for each assigned search resource, (5) receiving and responding to the distributed search requests, (6) aggregate the results collected from the various search requests, and (7) preparing an organized representation of results, typically with reduced duplication and possibly some relevancy rankings.",
  "TagValue_Example Specification":"•  IC/DoD SOAP Encoding Interface Specification for CDR Retrieve v1.1\n•  IC/DoD REST Interface Encoding Specification for CDR Retrieve v1.1",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"4",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"1.2.8 Search\n8.7.8  Query Data\n8.7.11  Formulate Discovery Search\n8.7.63  Search within Context\n8.7.65  Perform Text Searches\n8.7.95  Search Structured and Unstructured Data\n8.7.96 Review Information Search Results\n8.7.97  Report Query Results",
  "TagValue_JARM/ESL Alignment":"8.29.06 Content Discovery",
  "TagNotes_Comments":"Very many tools will map to this.  This is probably where future plugfests should focus - to drive the implementations of the CD&R specs to true interoperability.  Also, need to consider how the  various \"data cloud\" implementations across both IC and DoD fit into this.  These data clouds will have to be considered data stores themselves, and therefore available for federated queries, but also they will have their own internal queries that will be related to content search.  Associated with this will need to be the ability to de-duplicate data (since federating to already federated data sources will result in duplication of data) and to track data provenance.  This last part may involve the service registries and artifacts as well, as they can disclose what data sources a particular federated search is connected to."
},
{
  "TagValue_UID":'0098',
  "TagValue_Number":"2.3.1",
  "TagValue_Service Name":"Content Discovery and Retrieval",
  "TagNotes_Service Definition":"The Content Discovery and Retrieval (CD&R) family processes a user's query to discover information from data assets.",
  "TagNotes_Service Description":"family",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0097',
  "TagValue_Number":"2.3",
  "TagValue_Service Name":"Data Discovery",
  "TagNotes_Service Definition":"The Data Discovery line processes a user's query to discover information.",
  "TagNotes_Service Description":"Data Discovery processes a user’s query to discover information by:  1) conducting searches that gather information from basic web content as well as structured data sources (general, federated, and semantic search); 2) enabling advanced manipulation and control of the search request and results (search management and enhancement); and 3) enabling enhanced understanding of the people and assets that are available to support  missions and analysis (resource discovery).",
"TagValue_Example Specification":"Line",
"TagValue_Example Solution":"<memo>",
"TagValue_DI2E Framework Status":"Line",
"TagValue_DCGS Enterprise Status":"Line",
"TagValue_JCA Alignment":"Line",
"TagNotes_JCSFL Alignment":"Line",
"TagValue_JARM/ESL Alignment":"Line",
"TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0009',
  "TagValue_Number":"2.2.3.2",
  "TagValue_Service Name":"Common Operational Picture (COP)",
  "TagNotes_Service Definition":"Common Operational Picture (COP) services allow for generation of a COP and User Defined Operating Picture (UDOP).\n",
  "TagNotes_Service Description":"The COP provides an overall 'picture' of a geographic domain of interest (typically a theatre engaged in military missions) and is maintained by the commander’s operations staff.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"5.2.3.2 Establish Collective Meaning (Collaboration)",
  "TagNotes_JCSFL Alignment":"14.1.24 Provide Graphical Intelligence Preparation of the Operational Environment (IPOE) Products\n14.1.25 Provide Data Assets from Intelligence Preparation of the Operational Environment (IPOE) Products\n14.1.26 Conduct Joint Intelligence Preparation of the Operational Environment",
  "TagValue_JARM/ESL Alignment":"8.16.08 Situational Analysis",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0330',
  "TagValue_Number":"2.2.3.1",
  "TagValue_Service Name":"Analytic Rendering",
  "TagNotes_Service Definition":"Analytic Rendering services render analytic products.  Includes Histograms, Semantic Network diagrams, Scatter Diagrams, Flow Charts, Relationship Charts.",
  "TagNotes_Service Description":"Analytic Rendering provides different ways to visually present data, to assist in the understanding and exploitation of that data and the identification of trends or other useful information.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.17.01 Graphics / Charting",
  "TagNotes_Comments":"There exist a bunch of these widgets that are pre-packaged with the Synapse stuff.   Also, should probably call out the framework for these - such as AgileClient and OWF"
},
{
  "TagValue_UID":'0329',
  "TagValue_Number":"2.2.3",
  "TagValue_Service Name":"Analytics Visualization",
  "TagNotes_Service Definition":"Family to visualize results of Analytical Processes.",
  "TagNotes_Service Description":"family",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0377',
  "TagValue_Number":"2.2.2.5",
  "TagValue_Service Name":"Weather Visualization",
  "TagNotes_Service Definition":"Weather Visualization services receive and display weather related conditions.",
  "TagNotes_Service Description":"Weather Visualization services receive and display weather related conditions from various weather reporting sources such as the National Weather Association (NWA), U.S. Air Force Weather Agency (AFWA), or Naval Meteorology & Oceanography Command (CNMOC) (list is for example, not comprehensive).   Weather visualization is typically for a specified geographic region; may include display of historic, current, or anticipated future weather conditions; and might be in a variety of formats including multiple spectral options of imagery, map based, audio, or text.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"2.5.2 BA Data Access (IDD)",
  "TagNotes_JCSFL Alignment":"",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0113',
  "TagValue_Number":"2.2.2.4",
  "TagValue_Service Name":"Web Map",
  "TagNotes_Service Definition":"Web Map provides a simple HTTP interface for requesting geo-registered map images from one or more distributed geospatial databases. A WMS request defines the geographic layer(s) and area of interest to be processed. The response to the request is one or more geo-registered map images (returned as JPEG, PNG, etc.) that can be displayed in a browser application.",
  "TagNotes_Service Description":"A Web Map Service (WMS) produces maps of spatially referenced data dynamically from geographic information. This International Standard defines a “map” to be a portrayal of geographic information as a digital image file suitable for display on a computer screen. A map is not the data itself. WMS-produced maps are generally rendered in a pictorial format such as PNG, GIF or JPEG, or occasionally as vector-based graphical elements in Scalable Vector Graphics (SVG) or Web Computer Graphics Metafile (WebCGM) formats.  \nThis International Standard defines three operations: one returns service-level metadata; another returns a map whose geographic and dimensional parameters are well-defined; and an optional third operation returns information about particular features shown on a map. Web Map Service operations can be invoked using a standard web browser by submitting requests in the form of Uniform Resource Locators (URLs). The content of such URLs depends on which operation is requested. In particular, when requesting a map the URL indicates what information is to be shown on the map, what portion of the Earth is to be mapped, the desired coordinate reference system, and the output image width and height. When two or more maps are produced with the same geographic parameters and output size, the results can be accurately overlaid to produce a composite map. The use of image formats that support transparent backgrounds (e.g. GIF or PNG) allows underlying maps to be visible. Furthermore, individual maps can be requested from different servers. The Web Map Service thus enables the creation of a network of distributed map servers from which clients can build customized maps.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.5.2 BA Data Access (IDD)",
  "TagNotes_JCSFL Alignment":"3.2.52   Display Map",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"OGC WMS specification. Note that this \"service\" is really an implementation of a spec, and there will be many of these implementations as the data behind it will be unique to the particular implementation"
},
{
  "TagValue_UID":'0112',
  "TagValue_Number":"2.2.2.3",
  "TagValue_Service Name":"Web Feature",
  "TagNotes_Service Definition":"Web Feature provides an interface allowing requests for geographical features across the web using platform-independent calls.  This service allows a client to retrieve and update geospatial data encoded in Geography Markup Language (GML).",
  "TagNotes_Service Description":"Web feature defines interfaces for data access and manipulation operations on geographic features using HTTP as the distributed computing platform. Via these interfaces, a web user or service can combine, use and manage geodata -- the feature information behind a map image -- from different sources by invoking the following WFS operations on geographic features and elements: create a new feature instance; delete a feature instance; update a feature instance; lock a feature instance; get or query features based on spatial and non-spatial constraints.\nWhen products are in compliance with open geospatial web service interface and data encoding specifications, end-users benefit from a larger pool of interoperable web based tools for geodata access and related geoprocessing services.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.5.2 BA Data Access (IDD)",
  "TagNotes_JCSFL Alignment":"8.10.10 Provide Graphical User Interface (GUI) Services\n8.10.22 Manage Display of Symbology\n8.10.23 Display Tabular Sortable Information\n8.10.25 Display Video",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"OGC WFS Specification.  Note that this \"service\" is really an implementation of a spec, and there will be many of these implementations as the data behind it will be unique to the particular implementation"
},
{
  "TagValue_UID":'0111',
  "TagValue_Number":"2.2.2.2",
  "TagValue_Service Name":"Web Coverage",
  "TagNotes_Service Definition":"Web Coverage provides an interface allowing requests for geographical coverages (digital geospatial information representing space-varying phenomena) across the web using platform-independent calls.",
  "TagNotes_Service Description":"Web Coverage supports electronic retrieval of geospatial data as \"coverages\" – that is, digital geospatial information representing space-varying phenomena. Provides access to potentially detailed and rich sets of geospatial information, in forms that are useful for client-side rendering, multi-valued coverages, and input into scientific models and other clients. Allows clients to choose portions of a server's information holdings based on spatial constraints and other criteria.\nProvides available data together with their detailed descriptions; defines a rich syntax for requests against these data; and returns data with its original semantics (instead of pictures) which may be interpreted, extrapolated, etc. – and not just portrayed. Returns coverages representing space-varying phenomena that relate a spatio-temporal domain to a (possibly multidimensional) range of properties.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.5.2 BA Data Access (IDD)",
  "TagNotes_JCSFL Alignment":"8.10.10 Provide Graphical User Interface (GUI) Services\n8.10.22 Manage Display of Symbology\n8.10.23 Display Tabular Sortable Information\n8.10.25 Display Video",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"OGC Web Coverage Service (WCS) specification.  Note that this \"service\" is really an implementation of a spec, and there will be many of these implementations as the data behind it will be unique to the particular implementation."
},
{
  "TagValue_UID":'0008',
  "TagValue_Number":"2.2.2.1",
  "TagValue_Service Name":"Geographic Information Display",
  "TagNotes_Service Definition":"Geographic Information Display provides end-users with visualization services for still images, satellite images, 2D, 3D, weather images, and video.",
  "TagNotes_Service Description":"Geographic Information Display captures, stores, manipulates, analyzes, manages and presents all types of geographically referenced data (2D imagery, 3D imagery, video, satellite, weather, maps, human terrain assessment, human intelligence reports, available signal data, etc.) by merging of cartography, statistical analysis and database technology.\n\nCommon input sources include ortho-rectified imagery taken from both from satellite and aerial sources, maps or graphical products of various forms, and databases containing geospatially related elements.   Within the GIS display, information is typically located spatially (recording longitude, latitude, and elevation), but may also be recorded temporally or with other quantified reference systems such as film frame number, sensor identifier, highway mile marker, surveyor benchmark, building address, or street intersection.\n\nGIS display data represents real objects with digital representations including raster data (digital image represented by reducible and enlargeable grids) or vectors (which represent features as geometric shapes such as lines, polylines, or polygons).    Real objects are also typically divided into two abstractions: discrete objects (e.g., a house) and continuous fields (such as rainfall amount, or elevations). \n\nCommon GIS display operations include:\n• contrast enhancement and color rendering \n• geographic data conversion\n• map integration and image rectification \n• complex spatial modeling \n• geometric network representation\n• hydrological modeling\n• Digital Elevation Model (DEM) assessment\n• data extraction\n• Geostatistics that analyze point-patterns and provide predictions\n• Digital cartography\n• Topological relationships (adjacency, containment, proximity, ..)\n• Surface interpolation using various mathematical models \n• Geocoding (interpolating spatial locations from street addresses or any other spatially referenced data using reference themes).\n• Spatial Extract, Transform, Load (ETL) tools",
  "TagValue_Example Specification":"2D Map API",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"2",
  "TagValue_JCA Alignment":"2.4.5 Product Generation (AP)",
  "TagNotes_JCSFL Alignment":"3.2.52 Display Map\n3.4.3 Receive, Store and Maintain Geospatial Product Information\n8.4.10 Generate Displays",
  "TagValue_JARM/ESL Alignment":"8.17.02 Imagery Visualization",
  "TagNotes_Comments":"GIS really covers a lot of different things. For example,  WMS, WFS, KML, GML services all are part of the definition.  Note that GVS will provide the ESRI tool suite via their own licensing costs which are far lower than most other  agencies can get on their own.  This should be included.  Overall, this should probably be further decomposed."
},
{
  "TagValue_UID":'0007',
  "TagValue_Number":"2.2.2",
  "TagValue_Service Name":"Geographic Visualization",
  "TagNotes_Service Definition":"The Geographic Visualization family gathers and displays inputs from multiple sources containing data linked to geo-coordinated data and displays it in a common geographical representation.",
  "TagNotes_Service Description":"The Geographic Visualization family gathers and displays inputs from multiple sources containing data linked to geo-coordinated data and displays it in a common geographical representation.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0006',
  "TagValue_Number":"2.2.1.2",
  "TagValue_Service Name":"Widget Framework",
  "TagNotes_Service Definition":"Widget Framework service provides a toolkit that assists in combining two or more widgets (portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page) to form a single application using a much lower level of technical skill and effort than is typically required for application development.",
  "TagNotes_Service Description":"Widget framework applications provide a layout management and messaging mechanism within a web browser for widgets (widgets are portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page, technically contained in an iFrame).   In doing so, they enable rapid assembly and configuration of rich Web applications composed of multiple, special-purpose widgets.\n\nTypical widget frameworks support both varied (desktop) and fixed (tabbed, portal, accordion, ...) layouts that can be set by the application’s user.   Components might typically include a server to run supplied applications, HTML and JavaScript files that provide the user interface, preference holding and retrieval mechanisms, and various security mechanisms.   \n\nKey features to look for in a widget application include (not all may be found in all widget frameworks):\n• standardized widget event model(s)\n• standardized Common Data Model (CDM) to promote data sharing between widgets\n• Scalability features to optimize for performance \n• data sharing mechanism \n• data handling and management features \n• ability to extend basic services through modification of existing widgets, or the development of entirely new ones. \n• support for bundling and deployment of specific widget sets  \n• framework extensions for standardized data and eventing models \n• workflow features that promote workflow analysis and linkage with widget framework architecture. \n• pre-existing, pre-tested widgets that can ‘jump start’ framework development with assured right-from-the-start interoperability\n• clearly documented APIs",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"5",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.9.5 Integrate Enterprise Applications",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"Additional examples not yet in garage: JackBe Presto"
},
{
  "TagValue_UID":'0005',
  "TagValue_Number":"2.2.1.1",
  "TagValue_Service Name":"Web Browser",
  "TagNotes_Service Definition":"Web Browsers retrieves, presents, and traverses information resources identified by a Uniform Resource Locators (URLs) including web pages, images, videos, or other artifacts and content.   Includes the ability to traverse hyperlinks and display commonly used web content formats including HTML 4.0, current versions of JavaScript, common commercial image formats (.bmp, .gif, ...), NITF plugins, and PDF.",
  "TagNotes_Service Description":"The web browser service is the primary UI mechanism for DI2E applications, supports OUSDI goals for a Web based SOA architecture, reduces the complexity and expenses of providing fat clients, and supports rapid deployment of services through widgets.\n\nWeb browsers do this accepting a Uniform Resource Locator (URL) using URI prefixes (http:, https:, ftp:, file:, etc.) to display web hosted content that includes images, audio, video, and XML files.   URI prefixes that the web browser cannot directly handle are processed by other applications (mailto: by default e-mail application, news: by user's default newsgroup reader, etc.).\n\nExpected features include supporting:  \n• popular web file and image formats \n• Widgets  \n• Operation on a variety of operating systems \n• Multiple open information resources \n• pop-up blockers \n• bookmarked web pages so a user can quickly return to them.\n• Back, forward, refresh, home, and stop buttons \n• Address and bars that report loading status or links under cursor hovering\n• page zooming\n• Search and find features within a web page.\n• Ability to delete the web cache, cookies, and browsing history. \n• Basic security features \n\nExamples of other popular features include (not a complete list): \n• e-mail support  \n• IRC chat client, Usenet news support, and Internet Relay Chat (IRC)\n• web feed aggregator\n• support for vertical text\n• support for image effects and page transitions not found in W3C CSS\n• Embedded OpenType (EOT) and OpenType fonts support\n• \"favorites icon\" and \"quick tabs\" features \n• ActiveX controls support \n• Performance Advisors\n• location-aware browsing and geolocation support  \n• thumbnail ‘speed dial’ to move to favorite pages. \n• plug-in support\n• Page zooming allows text, images and other content \n• keyboard control, voice control, and mouse gesture control\n• Mobile devices support \n• web feeds",
  "TagValue_Example Specification":"• HTML\n• XHTML\n• CSS\n• DOM\n• ECMAScript\n• Others…",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.1.80 - Populate and Render Data Entry Form via Web Browser\n8.4.6  Support Web Browsing\n8.4.10 Generate Displays",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"ChatSurfer (geotagging), GeoSextant (Geotagger)"
},
{
  "TagValue_UID":'0004',
  "TagValue_Number":"2.2.1",
  "TagValue_Service Name":"Web Visualization",
  "TagNotes_Service Definition":"The Web Visualization family provides tools to view and traverse World Wide Web accessible content and applications.",
  "TagNotes_Service Description":"Web Visualization  uses web based technologies to provide user interfaces (UIs)  that can display web based content and web hosted applications.  Specific services include:\n• Web Browser -  which retrieves, presents, and traverses information resources identified by a Uniform Resource Locators (URLs) including web pages, images, videos, or other artifacts and content.\n• Widget Framework - which provides a toolkit that assists in combining two or more widgets (portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page).     Widget Frameworks displays an information arrangement, changeable by the user, to form a single application using much less demanding technical efforts than is typically required for application development.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0003',
  "TagValue_Number":"2.2",
  "TagValue_Service Name":"Visualization",
  "TagNotes_Service Definition":"The Visualization line of services identifies services that enable users to view and analyze data.",
  "TagNotes_Service Description":"Visualization Tools enable users to view and analyze data by providing 1) web visualization tools that view and traverse World Wide Web accessible content and applications and 2) geographic visualization tools that gather/displays inputs from multiple sources of geo-coordinated data and displays it in common geographical representations.",
"TagValue_Example Specification":"Line",
"TagValue_Example Solution":"<memo>",
"TagValue_DI2E Framework Status":"Line",
"TagValue_DCGS Enterprise Status":"Line",
"TagValue_JCA Alignment":"Line",
"TagNotes_JCSFL Alignment":"Line",
"TagValue_JARM/ESL Alignment":"Line",
"TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'0328',
  "TagValue_Number":"2.1.4.2",
  "TagValue_Service Name":"Community of Interest Find",
  "TagNotes_Service Definition":"Community of Interest Find provides the capability to identify people with particular interests in various intelligence topics for collaboration. It finds experts throughout the DI2E and broader community to better disseminate Intel and analyze problems.",
  "TagNotes_Service Description":"The Community of Interest Find service allows users to search the enterprise for other users who share a specific interest or expertise.  This allows users to identify Subject Matter Experts (SMEs) who might be able to assist in solving an intelligence problem, or identify personnel who may be interested in a piece of intelligence that relates to a topic they are watching.  This search capability facilitates networking between analysts and promotes dynamic development of communities of interest within the enterprise.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1 Provide Ability to Communicate (Group)\n8.7.53 Access Subject Matter Expert and Essential Information\n8.7.56 Maintain Address Book\n8.7.58 Manage Communities of Interest (COI) Catalogs\n8.12.8 Determine Collaboration Resource Availability",
  "TagValue_JARM/ESL Alignment":"8.26.03 Workforce Directory / Locator",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0023',
  "TagValue_Number":"2.1.4.1",
  "TagValue_Service Name":"Shared Calendaring",
  "TagNotes_Service Definition":"Shared Calendaring allows users to view, schedule, organize, and share calendar events with other DI2E users.",
  "TagNotes_Service Description":"Shared Calendaring leverages standards that enable calendar information to be exchanged regardless of the application that is used to create or view the information. \n\nInteroperability features should include the ability to: send source calendar content via messages between differing calendar tracking applications (including the ability to customize the scope or type of calendar information passed); ability to move (drag) appointments from sent calendar system to users calendar; ability to review/find common free time among two or more calendars; see & (with permissions) edit group calendars; and host and subscribe to calendars (periodic synchronization of calendars).\n\nCommon client application features include the ability to: view the calendar in multiple forms and formats (daily/weekly/monthly, forward or backward through time, etc.); show appointments and events; create, edit, and update events; schedule recurring events; schedule meetings; schedule non-human resources; set work schedules & holidays; change presentation options such as font size, font styles, color themes, etc.; set and view importance markings (high importance vs. normal).",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"8.1.76 - Enable Calendar Scheduling",
  "TagValue_JARM/ESL Alignment":"8.28.04 Shared Calendaring",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0022',
  "TagValue_Number":"2.1.4",
  "TagValue_Service Name":"Social Networking",
  "TagNotes_Service Definition":"Social Networking services help coordinate personal/group schedules, status, and events.",
  "TagNotes_Service Description":"Social Networking services help coordinate personal/group schedules, status, and events.\n\n  • Group Calendar - which views, schedules, organizes, and shares calendar events with other DI2E users.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0020',
  "TagValue_Number":"2.1.3.2",
  "TagValue_Service Name":"Audio Messaging",
  "TagNotes_Service Definition":"Audio Messaging allows users to send and receive sound file recordings.",
  "TagNotes_Service Description":"Audio Messaging allows transmittal of recorded voice messages & other audio content between and among fixed or mobile devices.    Messages can be sent one-to-one or one-to-many.   \n\nFeatures to look for in audio messaging services include the ability to: record messages from multiple input systems or devices (phone, PC microphone, …); forwarding messages among different audio messaging systems; indicate who messages are from, when received; auto-attendant, announcement broadcasting including scheduled or event-based group broadcasts; and recall for handling feedback and verifying messages.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1.51   Broadcast Information\n7.1.56  Provide Shared Audio Visualization Capabilities\n7.1.59  Translate Text to Speech",
  "TagValue_JARM/ESL Alignment":"8.28.08 Audio Messaging",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0021',
  "TagValue_Number":"2.1.3.3",
  "TagValue_Service Name":"E-Mail",
  "TagNotes_Service Definition":"E-mail creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers.",
  "TagNotes_Service Description":"The electronic mail (e-mail) service exchanges digital messages from an author to one or more recipients.   Architecturally, e-mail clients help author, send, receive, display, and manage messages.   E-mail servers accept, store, and forward messages from and to e-mail clients.   Structurally, an email message consists of three components, the message envelope, the message header (control information, including an originator's email address and recipient addresses), and the message body.\n\nBasic steps in the passing of a typical e-mail message include: \n• user entering a message & sending the message to a local mail submission agent (typically provided by internet service providers), \n• e-mail servers use the Simple Mail Transfer Protocol (SMTP) to read to resolve the domain name and determine the fully qualified domain name of the mail exchange server in the Domain Name System (DNS),\n• mail exchange records listing the mail exchange servers for that domain are returned,  \n• the message is sent (using SMTP). \n• The Message Delivery Agent (MDA) delivers the message to the recipient’s mailbox\n• The e-mail client is users to pick up the message using standardized access protocols",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1.24  Manage E-Mail\n8.5.33 Scan Mailbox",
  "TagValue_JARM/ESL Alignment":"8.28.01 Email",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0019',
  "TagValue_Number":"2.1.3.1",
  "TagValue_Service Name":"Instant Messaging",
  "TagNotes_Service Definition":"Instant Messaging allows users to communicate using text-based chat and instant messaging.",
  "TagNotes_Service Description":"Instant messaging provides the ability to exchange short written text messages between and among fixed or mobile devices.   Messages can be sent one-to-one or one-to-many and might carry two-way conversational tones, short informational dissemination updates, or alerting notification.      \n\nCharacters that can be sent should include at least the upper and lower case 26 letters of the English alphabet and 10 numerals, but may include other special characters as well.   Security, confidentiality, reliability and speed are key criteria on concern in text messaging services.   Common mobile channel platforms include Short Message Services (SMS), Mobile Web, Mobile Client Applications, and SMS with Mobile Web and Secure SMS.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1 Provide Ability to Communicate (Group)\n8.4.1 Create and Edit Messages",
  "TagValue_JARM/ESL Alignment":"8.28.07 Instant Messaging",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0018',
  "TagValue_Number":"2.1.3",
  "TagValue_Service Name":"Collaborative Messaging",
  "TagNotes_Service Definition":"Collaborative messaging capabilities facilitate the rapid creation and sharing of messages and, where appropriate, associated attached artifacts.",
  "TagNotes_Service Description":"Collaborative messaging services facilitate the rapid creation and sharing of messages and, where appropriate, associated attached artifacts.\n\nSpecific services include:  \n\n  • Instant Messaging - which allows users to communicate using text-based chat and instant messaging.\n  • Audio Messaging - which allows users to send and receive sound file recordings. \n  • E-mail - which creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0017',
  "TagValue_Number":"2.1.2.3",
  "TagValue_Service Name":"Web Conferencing/VTC",
  "TagNotes_Service Definition":"The Web Conferencing/VTC service allows users in two or more locations to communicate using near real time full-duplex audio and video.",
  "TagNotes_Service Description":"The Web Conferencing/Video Telecommunications (VTC) service supports real time collaboration by offering full-duplex sharing of web, auto and video including internet telephone conferencing, videoconferencing, and web conferencing. \n\nTypical web conferencing features include sharing of documents, desktops, presentations, browsers, and other applications; remotely controlling presentation once given presenter rights; transferring files from within application; controlling layouts (including other users); and dual monitor support.  Advanced features might include ability to play and pause movie files, conference record and playback, integrated white boarding or text chat, and highlight or pointer tools.\n\nSome Video Teleconferencing features to look for include: higher definition support (preferably 720p or better @ 30 fps); support for various video peripherals at any end-point; on-the-fly video device switching; ability to play or pause each or every participant; customizable video size, resolution and frame rate for entire conference or any participant; show/hide participant's name; support for full-screen, tiled, floating, picture-in-picture (PIP), composite (one large, others tiled), and floating video bar display.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1.3  Employ Conference Communications Services\n7.1.44  Conduct Video Conferencing",
  "TagValue_JARM/ESL Alignment":"8.28.06 Web Conferencing",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0016',
  "TagValue_Number":"2.1.2.2",
  "TagValue_Service Name":"Whiteboard",
  "TagNotes_Service Definition":"The Whiteboard service provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.",
  "TagNotes_Service Description":"The Whiteboard service allows shared annotation of ‘open’ space or files (such as an image or map) among two users working at geographically separated workstations.   Each user can work see and mark the image at the same time, and each is able to see changes the others make in near-real time.\n\nIn this sense the white board is a virtual version of the physical whiteboards found in many professional office walls (some offer electronic features such as interactive display of a PC monitor or printing of the white board’s display, but these are not the type intended here).\n\nExamples of common features (not an exhaustive or mandatory list) include: multiple page support, rich text editing support, import of external images & graphics, export of whiteboard to multiple image formats, ability to save work for later session editing, drag/drop shape support, priority layering ('bring to front'), font adjustment, and automated fill in.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1.56 Provide Shared Audio Visualization Capabilities\n8.12.9 Provide Common Workspace",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0015',
  "TagValue_Number":"2.1.2.1",
  "TagValue_Service Name":"Desktop Sharing",
  "TagNotes_Service Definition":"Desktop Sharing provides the ability to share desktop applications with other users and groups.",
  "TagNotes_Service Description":"Desktop sharing allows remote access, and thus remote real-time collaboration, with a computer’s desktop through a graphical terminal emulator.    Besides screen sharing, other common embedded features include instant messaging, file passing, and the ability to share control.  Some desktop sharing systems also permit video conferencing\n\nFile transfer systems that support the X Window System (typically Unix-based) have basic desktop sharing abilities already built in.     Microsoft Windows (Windows 2000 and later) offer basic remote access in the form of Remote Desktop Protocol.",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1 Provide Ability to Communicate (Group)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0014',
  "TagValue_Number":"2.1.2",
  "TagValue_Service Name":"Environment Sharing",
  "TagNotes_Service Definition":"Environment Sharing capabilities provide environments to easily share work space environments in order to promote rapid, interactive collaboration.",
  "TagNotes_Service Description":"Environment Sharing capabilities provide environments that easily share work space environments in order to promote rapid, interactive collaboration.  Specific services include:\n\n  • Desktop Sharing - which provides the ability to share desktop applications with other users and groups.\n  • Whiteboard - which provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.\n  • Web Conferencing/VTC - which allows users in two or more locations to communicate using near real time full-duplex audio and video.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0013',
  "TagValue_Number":"2.1.1.2",
  "TagValue_Service Name":"Wiki",
  "TagNotes_Service Definition":"The Wiki service provides a page or collection of web pages that enable anyone with access to contribute to or modify content.",
  "TagNotes_Service Description":"Wiki applications allow web pages to be created and edited using a common web browser and typically run on a web server (or servers). The content is stored in a file system, and changes to the content are stored in a relational database management system.    The general maintenance paradigm is to allow alteration by general public without requiring registered user accounts and edits to appear almost instantly, but some wiki sites are private or password-protected.\n\nStyle of text presented typically includes plain text, HTML, Cascading Style Sheets (CSS) or other web based formats.",
  "TagValue_Example Specification":"• JAMWiki,MediaWiki\n• FlexWiki\n• Roadkill\n• Foswiki\n• TWiki\n• DokuWiki\n• MediaWiki\n• Gitit\n• Swiki",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1.22  Manage Message Boards",
  "TagValue_JARM/ESL Alignment":"8.28.11 Community Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0012',
  "TagValue_Number":"2.1.1.1",
  "TagValue_Service Name":"Bulletin Board",
  "TagNotes_Service Definition":"Bulletin Board provides the ability to post messages and notices that interested communities can see, share, and respond to.",
  "TagNotes_Service Description":"Bulletin Boards provide an internet based discussion forum where DI2E users can post messages that last for short to long periods of time.    Discussions are typically moderated and fully threaded (messages with replies and replies to replies) vs. non-threaded (messages with no replies) or semi-threaded (relies to a message, but not replies to a reply).\n\nGeneral users can see typically see threads, post new threads, respond to thread comments, and change certain preferences for their account such as their avatar (image representing the user), automatic signature, and ignore lists.   \n\nModerators can typically set, monitor, and change and privileges to groups or individual members; delete, merge, move, split, lock, or rename threads; remove unwanted content within a thread; provide access to unregistered guests; and control the size of posts.    \n\nAdministrators can typically promote and demote members, manage rules, create sections and sub-sections, and perform basic database operations.\nAdditional common features include the ability to subscribe for notification new content is added, view or vote in opinion polls, track post counts (how many posts a certain user has made), or attach symbols to convey emotional response.   More advanced bulletin boards might support Really Simple Syndication (RSS), XML and HTTP feeds (such as ATOM).",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"4",
  "TagValue_DCGS Enterprise Status":"3",
  "TagValue_JCA Alignment":"6.2.3.2 Collaboration",
  "TagNotes_JCSFL Alignment":"7.1.22  Manage Message Boards",
  "TagValue_JARM/ESL Alignment":"8.28.10 Event / News Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'0011',
  "TagValue_Number":"2.1.1",
  "TagValue_Service Name":"Information Boards",
  "TagNotes_Service Definition":"The Information Board family of services provide a unified electronic platform that supports synchronous and asynchronous communication and information sharing through a variety of tools & services.",
  "TagNotes_Service Description":"Information Board services help users readily share thoughts, files, diagrams, and other content that helps users work together with enhanced understanding of each other's knowledge, needs, issues, and perspectives.   Specific services include:\n• Bulletin Board - which provides the ability to post messages and notices that interested communities can see, share, and respond to\n• Wiki - which provides a page or collection of web pages that enable anyone with access to contribute to or modify content.\n• Desktop Sharing - which provides the ability to share desktop applications with other users and groups.\n• Whiteboard - which provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.\n• Web Conferencing/VTC - which allows users in two or more locations to communicate using near real time full-duplex audio and video.\n• Instant Messaging - which allows users to communicate using text-based chat and instant messaging.\n• Audio Messaging - which allows users to send and receive sound file recordings. \n• E-mail - which creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers. \n• Group Calendar - which allows users to view, schedule, organize, and share calendar events with other DI2E users.\n• People Find - which provides the service to identify people with particular interests or skills in various intelligence topics for consultation and collaboration.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"family",
  "TagValue_DCGS Enterprise Status":"family",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"family"
},
{
  "TagValue_UID":'0010',
  "TagValue_Number":"2.1",
  "TagValue_Service Name":"Collaboration",
  "TagNotes_Service Definition":"Collaboration provides tools and services so people can easily share knowledge, status, thoughts, and related information artifacts.",
  "TagNotes_Service Description":"The Collaboration is the aggregation of infrastructure, services, people, procedures, and information to create and share data, information and knowledge used to plan, execute, and assess joint forces operations. (Ref. DCGS CONOPS v1.0 - 15 May 2007).  This line focuses on the tools and services that enables this creation and sharing of data, information, and knowledge.",
  "TagValue_Example Specification":"Line",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"Line",
  "TagValue_DCGS Enterprise Status":"Line",
  "TagValue_JCA Alignment":"Line",
  "TagNotes_JCSFL Alignment":"Line",
  "TagValue_JARM/ESL Alignment":"Line",
  "TagNotes_Comments":"Line"
},
{
  "TagValue_UID":'380',
  "TagValue_Number":"1.4.3",
  "TagValue_Service Name":"Application and Website Hosting",
  "TagNotes_Service Definition":"Application and Website Hosting services provide a framework for hosting and deploying web-based applications by handling transactions, security, scalability, concurrency and management of the components they deploy. This enables developers to concentrate more on the business logic of the components rather than on infrastructure and integration tasks.",
  "TagNotes_Service Description":"Application and Website Hosting provides a framework for hosting and deploying web based applications or content. The framework typically includes the application server, which provides a generalized approach for creating an application-server implementation and handles application operations between users and an organization's backend business applications. It consists of web server connectors, runtime libraries and database connectors. \nThe application server runs behind a web server and usually in front of a database. Web applications run on top of application servers, are written in languages the application server supports, and call the runtime libraries and components the application server provides.",
  "TagValue_Example Specification":"Java - \nJava EE 6\nServlet\nJSP\n\n.NET - \n.NET Framework",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'381',
  "TagValue_Number":"1.4.3.1",
  "TagValue_Service Name":"Web Content Delivery",
  "TagNotes_Service Definition":"Web Content Delivery services serve up dynamic and static website content.",
  "TagNotes_Service Description":"The Web Content Delivery service serves content to clients. It also receives requests and supports server-side scripting. \n The commonly used specification is HTTP.",
  "TagValue_Example Specification":"HTTP",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.1.80 Populate and Render Data Entry Form via Web Browser",
  "TagValue_JARM/ESL Alignment":"5.03.01 Web Hosting Services",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'382',
  "TagValue_Number":"1.4.3.2",
  "TagValue_Service Name":"Security Access Proxy",
  "TagNotes_Service Definition":"Proxy Management services provide bi-directional access to security services.",
  "TagNotes_Service Description":"The Proxy Management service provides bi-directional access to security services for authentication and authorization purposes.. It may also utilize these security services in the establishment of trusted relationships, verification of certificates, and the enforcement of access policies to protected resources.",
  "TagValue_Example Specification":"UAAS",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.22.07 Service Brokering",
  "TagNotes_Comments":"Name needs to be improved"
},
{
  "TagValue_UID":'383',
  "TagValue_Number":"1.4.3.3",
  "TagValue_Service Name":"Transaction Processing",
  "TagNotes_Service Definition":"Transaction Processing services provide connectors to services that utilize data sources",
  "TagNotes_Service Description":"Transaction Processing services provide connectors to services that handle access, query and retrieval from data sources, such as a DBMS or file system. They also can provide interfaces with wrappers for federated searches within CD&R components.",
  "TagValue_Example Specification":"JDBC, ODBC",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.1.69 Provide Web Applications\n8.1.79 Provide Proxy Services",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'384',
  "TagValue_Number":"1.4.3.4",
  "TagValue_Service Name":"Application Management",
  "TagNotes_Service Definition":"Application Management services provide the capability for application and web servers to deploy, install, activate and deactivate applications.",
  "TagNotes_Service Description":"Application Management services provide the capability for application and web servers to deploy, install, activate and deactivate applications.  They also supply support for the scaling and concurrency of applications running on the server.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"8.1.9 Provide Network Applications Services",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'385',
  "TagValue_Number":"2.6.2",
  "TagValue_Service Name":"Database Management",
  "TagNotes_Service Definition":"The Database Management Family provides services to host database repositories and  interact with users, other applications, and the database itself to capture and analyze data.",
  "TagNotes_Service Description":"Database Management includes capabilities to perform Create, Read, Update, and Delete (CRUD) operations on content, discover database content, apply security labels and tags to data objects, define database structures, and perform database administration functions.\n\nNote: Data Object Security Labeling is included in the Security Metadata Management component.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'386',
  "TagValue_Number":"2.6.2.1",
  "TagValue_Service Name":"Database Describe",
  "TagNotes_Service Definition":"Database Describe enables the discovery of information about the database and the data object types stored in the database.",
  "TagNotes_Service Description":"Database Describe includes describe-like capabilities to list tables, list table fields, etc.  This service feeds information to the CD&R Describe Content Service.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.21 Data Management; 5.02.01 Database Management Services",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'387',
  "TagValue_Number":"2.6.2.2",
  "TagValue_Service Name":"Data Object Processing",
  "TagNotes_Service Definition":"Data Object Processing provides Create, Read, Update, and Delete (CRUD) operations on data objects stored in a database repository.",
  "TagNotes_Service Description":"Data Object Processing includes capabilities to ingest data, commit transactions, rollback transactions, etc.\n\nThis includes the Content Search service that exists in CD&R.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7.13 Compress Data\n8.7.14 Delete Data\n8.7.16 Migrate Data\n8.7.28 Provide Database Utilities\n8.7.81 Store Data\n8.7.82 Retain Data\n8.7.112 Modify Data\n8.7.115 Import Data\n8.7.116 Export Data \n8.7.117 Retrieve Recorded Data",
  "TagValue_JARM/ESL Alignment":"8.21 Data Management; 5.02.01 Database Management Services",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'388',
  "TagValue_Number":"2.6.2.3",
  "TagValue_Service Name":"Database Definition",
  "TagNotes_Service Definition":"Database Definition enables the creation of the database and its structure, i.e., tables, indexes, triggers, etc., and the ability to modify the database structure.",
  "TagNotes_Service Description":"Database Definition includes capabilities to create database, drop database, create tables, drop tables, alter tables, create indexes, drop indexes, etc.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.21 Data Management; 5.02.01 Database Management Services",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'389',
  "TagValue_Number":"2.6.2.4",
  "TagValue_Service Name":"Database Administration",
  "TagNotes_Service Definition":"Database Administration enables the overall management of existing database repositories in a organization.",
  "TagNotes_Service Description":"Database Administration includes capabilities to backup a database, recover a database, upgrade a database, restart a database, monitor database performance, tune database parameters, etc.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.21 Data Management; 5.02.01 Database Management Services",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'390',
  "TagValue_Number":"2.6.2.5",
  "TagValue_Service Name":"Data Object Tagging",
  "TagNotes_Service Definition":"Data Object Tagging tags data with various forms of content topic metadata for information discovery and automated dissemination purposes.",
  "TagNotes_Service Description":"Data tagging allows users to organize information more efficiently by associating pieces of information with tags, or keywords.  A tag is a non-hierarchical keyword or term assigned to a piece of information such as an image, file or record. This kind of metadata helps describe an item and allows it to be found again by browsing or searching.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.3 Content Discovery",
  "TagNotes_JCSFL Alignment":"8.7.66 Post Metadata to a Discovery Catalog\n8.7.67 Post Metadata to a Federated Directory\n8.7.68 Post Metadata to Metadata Registries",
  "TagValue_JARM/ESL Alignment":"8.21 Data Management; 5.02.01 Database Management Services",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'391',
  "TagValue_Number":"2.6.3",
  "TagValue_Service Name":"Workspace Management",
  "TagNotes_Service Definition":"Workspace Management services enable users to efficiently and locally establish, update, and share data in a persistent state between work sessions.",
  "TagNotes_Service Description":"Workspace Management services enable users to efficiently and locally establish, update, and share data in a persistent state between work sessions.     This lets users then ‘start where they left off’ between sessions, quickly view and analyze certain information conditions, or readily share their work session information with others.   Other benefits include:\n\n• Removing the need for developers to create their own persistence solutions for multiple applications\n• Common access to certain data layer content\n• Removes certain product specific dependences (i.e. Oracle Database)\n• Creates a ‘contract’ for sharing data between components\n• Enables customization of data structure based on user needs",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'393',
  "TagValue_Number":"2.6.3.1",
  "TagValue_Service Name":"Manage Workspace",
  "TagNotes_Service Definition":"Manage Workspace services enable a user to add documents a new workspace session (Create), get documents (Read) documents in their workspace, edit (Update) the relationships among documents in their workspace, or remove (Delete) documents in their workspace.",
  "TagNotes_Service Description":"Manage Workspace services enable a user to add documents a new workspace session (Create), get documents (Retrieve) documents in their workspace, edit (Update) the relationships among documents in their workspace, or remove (Delete) documents in their workspace.  The typical paradigm is to store documents in a hierarchical structure that contains ‘parents’ and ‘children’. Using the manage workspace service, users can then Create/Retrieve/Update/Delete (CRUD) documents using this structure in a persistent state for use between various work sessions.    \n\nTypical operations are broken down as follows:\nCreate - add documents to the workspace\nRetrieve – get a document, a document’s children, a documents parents, documents by type, or children documents by type\nUpdate – change the content of a document, or modify hierarchical relationships among documents\nDelete – remove individual documents, or entire ‘families’ of documents",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'394',
  "TagValue_Number":"2.6.3.2",
  "TagValue_Service Name":"Share Workspace",
  "TagNotes_Service Definition":"Share Workspace services enable a user to send or retrieve their workspace files to other workspace component users.",
  "TagNotes_Service Description":"Share Workspace services enable a user to send or retrieve their current or saved workspace files to other workspace component users.   Workspace content can be content currently in use, or a saved session of workspace content from an earlier session.   \n\nThe method of identifying candidate workspace users or methods of transmission are not prescribed as part of this service definition, but an illustrative example would be a workspace management component that enabled the storing e-mail addresses as one family within the workspace documents, then the ability to select e-mail addresses from this list and then optionally send entire workspace, or families within the overall workspace, to the selected set of e-mail addresses.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"6.2.3.8 Enterprise Application Software",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"(gap)",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'395',
  "TagValue_Number":"3.1.2",
  "TagValue_Service Name":"Workflow Management",
  "TagNotes_Service Definition":"Workflow Management enables coordination and cooperation among members of an organization, helping them to perform complex mission execution by optimizing, assigning, and tracking tasks across the enterprise.",
  "TagNotes_Service Description":"Workflow Management services are a sequence of connected steps that describe a repeatable chain of operations, can include multiple processes, and can stem across organizations or agencies. The workflows represent the mission by means of elementary activities and connections among them. The activities represent fully automated tasks executed by computer or tasks assigned to human actors executed with the support of a computer. Workflow Management provides a consistent, streamlined, and traceable process for users to follow, and monitors the progress of who has performed the work, the status of the work, and when the work has been completed at all times.",
  "TagValue_Example Specification":"family",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"family",
  "TagNotes_JCSFL Alignment":"family",
  "TagValue_JARM/ESL Alignment":"family",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'396',
  "TagValue_Number":"3.1.2.1",
  "TagValue_Service Name":"Define Workflows",
  "TagNotes_Service Definition":"The Define Workflows service enables a workflow designer to capture and maintain enterprise workflows in human-readable and machine-readable formats.",
  "TagNotes_Service Description":"Define Workflows models workflow using intuitive, visual tools and a model notation. Workflow definition helps to ensure control in execution of workflows to ensure both consistency in output and that no step is skipped. Each workflow represents a series of anticipated events by illustrating tasks, and connections among them. The tasks represent fully automated activities executed by computer or tasks assigned to human actors executed with the support of a computer. In DoD environments, workflows are most commonly defined in Business Process Modeling Notation (BPMN).",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2.2 Develop Knowledge and Situational Awareness",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.04 Tracking and Workflow; 7.07.03 Workflow Management",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'397',
  "TagValue_Number":"3.1.2.2",
  "TagValue_Service Name":"Identify Resources",
  "TagNotes_Service Definition":"Identify Resources captures and maintains a list of available human and technical assets, along with their skill/capability attributes, status, and schedule.",
  "TagNotes_Service Description":"The Identify Resources service identifies assets which may be distributed across multiple physical locations, and may include (but are not limited to) financial resources, inventory, human skills, production resources, information technology (IT), etc. The resources have definitions of their skills and capabilities based in terms of workflow tasks (Resource X can perform Task Y; Task Y is identified in the Define Workflow Service). The resources also have attributes for their current status (active, inactive, etc.) and their schedule (busy until a certain date, for example). The Identify Resources service integrates closely with the Execute Workflow service to identify the available resources to execute a workflow.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2.2 Develop Knowledge and Situational Awareness",
  "TagNotes_JCSFL Alignment":"4.8.20 Analyze Resource Tasking and Control",
  "TagValue_JARM/ESL Alignment":"8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'398',
  "TagValue_Number":"3.1.2.3",
  "TagValue_Service Name":"Execute Workflows",
  "TagNotes_Service Definition":"Execute Workflows utilizes the Define Workflow and Identify Resources services to execute an instance of the workflow across the enterprise.",
  "TagNotes_Service Description":"Execute Workflows enables workflow administrators to start, pause, and cancel workflows. It determines availability of resources and assigns resources to the workflow. It takes input from the Optimization service regarding the best resource to assign to a task.  After the appropriate resource is identified, it utilizes the event notification service to alert the first available resource of the task, and follows up with notifications as the workflow is executed. The service may also include information about the task, or pass along a link to reference information as a workflow is executed. Alerts can also be sent to key people within the organization when changes are made to workflow tasks.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2.2 Develop Knowledge and Situational Awareness",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'399',
  "TagValue_Number":"3.1.2.4",
  "TagValue_Service Name":"Task Summary",
  "TagNotes_Service Definition":"The Task Summary service pulls information from all of the currently running workflows in the system to allow the resource to see all of the tasks in their queue and provide information on the status of the task.",
  "TagNotes_Service Description":"The Task Summary service pulls information from all of the currently running workflows in the system to allow the resource to see all of the tasks in their queue and provide information on the status of the task. It offers a single view of all of the tasks assigned to a specific resource.  Resources can also use the task summary service to display past and current tasks.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2.2 Develop Knowledge and Situational Awareness",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'400',
  "TagValue_Number":"3.1.2.5",
  "TagValue_Service Name":"Task Manager",
  "TagNotes_Service Definition":"The Task Manager provides the current or past status of a specific task, including information for an activity in a workflow.",
  "TagNotes_Service Description":"Task Manager enables the resource to manually or systematically update the status of each task in their queue.  The system uses automatic notifications to send out alerts to users when the status of a task changes. Each user can configure rules for how the e-mails will be triggered (e.g. changes to certain products, certain updates to a specific workflow instance, or changes to a specific task.) Any change of status, such as changing the status of a task to complete, is captured and logged for historical reference. This gives users a complete chronological registry of workflow-related information such as interactions with a task, changes to the state of the task, reassignment of a step or task, and time-stamped data and comments.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2.2 Develop Knowledge and Situational Awareness",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'401',
  "TagValue_Number":"3.1.2.6",
  "TagValue_Service Name":"Enterprise Workflow Reporting",
  "TagNotes_Service Definition":"Enterprise Workflow Reporting enables an understanding of the overall enterprise status by generating a series of reports.",
  "TagNotes_Service Description":"Enterprise Workflow Reporting enables administrators to define and produce summary reports for workflows that are managed within the WFM Service Family. The reports offer a variety of views of the overall workflow status, and can include reports to track resources, time to complete tasks, or a summary of tasks assigned or completed. These reports may be sent to stakeholders in real time, or on a regularly scheduled basis.",
  "TagValue_Example Specification":"TBD",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"2",
  "TagValue_DCGS Enterprise Status":"1",
  "TagValue_JCA Alignment":"5.2.2 Develop Knowledge and Situational Awareness",
  "TagNotes_JCSFL Alignment":"(gap)",
  "TagValue_JARM/ESL Alignment":"8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
  "TagNotes_Comments":"none"
},
{
  "TagValue_UID":'402',
  "TagValue_Number":"1.2.1.2",
  "TagValue_Service Name":"Credential Management",
  "TagNotes_Service Definition":"Life-cycle management of credentials (e.g., PKI soft token) associated with digital identities. Credential management includes PKI life-cycle management of Identity and Non Person Entity (NPE) (e.g., server) keys and certificates.",
  "TagNotes_Service Description":"none",
  "TagValue_Example Specification":"none",
  "TagValue_Example Solution":"<memo>",
  "TagValue_DI2E Framework Status":"3",
  "TagValue_DCGS Enterprise Status":"0",
  "TagValue_JCA Alignment":"6.2.3.5 Common Identity Assurance Services; 6.4.1.1 Assure Access",
  "TagNotes_JCSFL Alignment":"8.2.19  Validate User Credentials\n8.2.6  Provide Entity Authentication Services",
  "TagValue_JARM/ESL Alignment":"8.27.02 Authentication Management",
  "TagNotes_Comments":""
}
];
























































































MOCKDATA2.parsedSvcv4 = {
  "children": [
    {
      "id": "0",
      "name": "0 - DI2E",
      "data": {
        "TagValue_UID": "0001",
        "TagValue_Number": "0",
        "TagValue_Service Name": "DI2E",
        "TagNotes_Service Definition": "The DI2E SvcV-4 documents services that are needed for the DI2E to achieve the desired level of service reuse and interoperability .",
        "TagNotes_Service Description": "The services that exist within the Defense Intelligence Enterprise that are governed by the DI2E.  The services shall be developed in a service oriented manner, be registered and accessible in the Enterprise Registry, and be tested and certified for reuse.",
        "TagValue_Example Specification": "All Layers",
        "TagValue_Example Solution": "All Layers",
        "TagValue_DI2E Framework Status": "All Layers",
        "TagValue_DCGS Enterprise Status": "All Layers",
        "TagValue_JCA Alignment": "All Layers",
        "TagNotes_JCSFL Alignment": "All Layers",
        "TagValue_JARM/ESL Alignment": "All Layers",
        "TagNotes_Comments": "All Layers"
      },
      "size": 100
    },
    {
      "id": "1",
      "name": "1 - Infrastructure Services",
      "data": {
        "TagValue_UID": "0161",
        "TagValue_Number": "1",
        "TagValue_Service Name": "Infrastructure Services",
        "TagNotes_Service Definition": "The functions/services that support the Enterprise and don't usually have a direct relationship to the mission or business processes.",
        "TagNotes_Service Description": "Infrastructure Services are fundamental to the DI2E.  This consistent set of services across the DI2E is foundational to future SOA-based enterprise services.     \n\nSpecific service lines include:\n\n   • Enterprise Management\n   • Web Service Security\n   • Web Service Discovery\n   • Messaging, and \n   • Orchestration",
        "TagValue_Example Specification": "Layer",
        "TagValue_Example Solution": "Layer",
        "TagValue_DI2E Framework Status": "Layer",
        "TagValue_DCGS Enterprise Status": "Layer",
        "TagValue_JCA Alignment": "Layer",
        "TagNotes_JCSFL Alignment": "Layer",
        "TagValue_JARM/ESL Alignment": "Layer",
        "TagNotes_Comments": "Layer"
      },
      "size": 100,
      "children": [
        {
          "id": "11",
          "name": "1.1 - Enterprise Management",
          "data": {
            "TagValue_UID": "0162",
            "TagValue_Number": "1.1",
            "TagValue_Service Name": "Enterprise Management",
            "TagNotes_Service Definition": "Enterprise Management services enable consistent service level agreement and quality of service reporting; service/site monitoring; consistent use of domain names; and time synchronization.",
            "TagNotes_Service Description": "Enterprise Management services enable consistent management of service performance, access, and time.  In particular:\n\n   • Service Level Agreement/Quality of Service (SLA/QoS) Reporting services enable operational analysis and management of DI2E services\n   • Enterprise Administration services monitor services, services, and websites \n   • Translation and Synchronization services promote standardized references of time and location",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "Line",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Focus on what should be the standard measures, by category of monitored item.  In addition, give guidance for how to calculate these measures (e.g., availability, MTTR, etc).   What is important is not so much consistency of SLAs as much as consistency of parameter definitions/measures that any given SLA will reference"
          },
          "size": 100,
          "children": [
            {
              "id": "111",
              "name": "1.1.1 - Metrics Management",
              "data": {
                "TagValue_UID": "0163",
                "TagValue_Number": "1.1.1",
                "TagValue_Service Name": "Metrics Management",
                "TagNotes_Service Definition": "Metrics Management enables operational analysis and management for the quality of service provided by DI2E services.",
                "TagNotes_Service Description": "Metrics Management develops & collects metrics, monitors events, and evaluates performance.   This requires a standard set of metrics, reported events, and interface for querying and reporting collected metrics. \n\nService providers must analyze information to detect and report service degradation so corrective action can be taken when needed.   Real-time monitoring avoids Service Level Agreement (SLA) violations and minimizes service down time.   \n\nThe Metrics Management family supports two service interface models: Request/Response Interface – to support ad-hoc querying of metrics and event data; and the Publish/Subscribe Interface – to support a publish/subscribe interface.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "Nagios, Hyperic, Zenoss, and OpenNMS",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "Plenty of open source tools for doing this.  Recommend Nagios.  Need to manage not only networks, VMs, processes, but also the actual web services etc."
              },
              "size": 100,
              "children": [
                {
                  "id": "1111",
                  "name": "1.1.1.1 - Metrics Measurements Collection",
                  "data": {
                    "TagValue_UID": "0164",
                    "TagValue_Number": "1.1.1.1",
                    "TagValue_Service Name": "Metrics Measurements Collection",
                    "TagNotes_Service Definition": "Metrics Measurements Collection gathers and records service performance measurements.",
                    "TagNotes_Service Description": "Metrics Measurements Collection service monitors invoked services and collects related performance measurements which are then used to calculate quality of service and other performance metrics.  Not directly accessed by users, this service gathers and records its information into the service performance database.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "DISA ESM tool",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": "6.3.1.1 Network Resource Visibility",
                    "TagNotes_JCSFL Alignment": "3.4.12 Collect Performance Data\n3.4.18 Display Performance Data\n9.2.9 Record Real-Time System Performance and Condition Information",
                    "TagValue_JARM/ESL Alignment": "8.16.10 Metrics Collection",
                    "TagNotes_Comments": "Probably a combination of libraries such as log4j, best practice guidance, a tool that can forward metrics logs, and a tool that can collect metrics logs and store them in \"big storage\" and perform analytics on them."
                  },
                  "size": 100
                },
                {
                  "id": "1112",
                  "name": "1.1.1.2 - Metrics Reporting",
                  "data": {
                    "TagValue_UID": "0168",
                    "TagValue_Number": "1.1.1.2",
                    "TagValue_Service Name": "Metrics Reporting",
                    "TagNotes_Service Definition": "Metrics Reporting service provides an interface for the retrieval of managed resource quality of service metrics.",
                    "TagNotes_Service Description": "Metrics Reporting is used by Knowledge Managers to retrieve metrics collected by the Metrics Measurements Collection service.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "DISA ESM tool",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": "6.3.1.1 Network Resource Visibility",
                    "TagNotes_JCSFL Alignment": "3.4.18 Display Performance Data\n8.7.52 Report Performance Results\n9.1.29 Record and Store System Components Reliability Information",
                    "TagValue_JARM/ESL Alignment": "8.16.11 Metrics Visualization",
                    "TagNotes_Comments": "Probably an interface (both GUI and back-end interfaces) associated with the Metrics Measurement Collection tool"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "112",
              "name": "1.1.2 - Translation and Synchronization",
              "data": {
                "TagValue_UID": "0173",
                "TagValue_Number": "1.1.2",
                "TagValue_Service Name": "Translation and Synchronization",
                "TagNotes_Service Definition": "Translation and Synchronization services promote standardized references of time and location across the  Enterprise.",
                "TagNotes_Service Description": "Translation & Synchronization enables the management of network operations by resolving service endpoint Universal Resource Locators (URLs) and maintaining synchronized time on all connected  nodes.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "1121",
                  "name": "1.1.2.1 - Domain Name System (DNS)",
                  "data": {
                    "TagValue_UID": "0225",
                    "TagValue_Number": "1.1.2.1",
                    "TagValue_Service Name": "Domain Name System (DNS)",
                    "TagNotes_Service Definition": "This Domain Name System (DNS) locates and translates Internet domain names into Internet Protocol (IP) addresses using a hierarchy of authority.",
                    "TagNotes_Service Description": "Domain names are meaningful identification labels for Internet addresses. The Domain Name System (DNS) translates the more easily used by humans domain names (example: www.army.mil)  into numerical Internet Protocol formats (example: 192.52.180.110) used by networking equipment for locating and addressing devices. \n\nDNS makes it possible to assign domain names to groups of Internet users in a meaningful way, independent of each user's physical location. Because of this, World Wide Web (WWW) hyperlinks and Internet contact information can remain consistent when internet routing arrangements change or a participant uses a mobile device.     Consistently applied DNS naming conventions across the enterprise further promote enterprise interoperability because users & systems can more easily and dependably find and understand the purpose of web based resources.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT\nNIST 800-81",
                    "TagValue_Example Solution": "• Microsoft Server\n• BIND",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "6",
                    "TagValue_JCA Alignment": "6.2.1 Information Sharing",
                    "TagNotes_JCSFL Alignment": "8.1.56 Translate Name-to-Address\n8.1.60 Provide Distributed Domain Name Systems (DNSs)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "DNS is pretty much ubiquitous.  What is needed is not so much the tool as the guidance for best practices related to how to manage the DNS hierarchy within a COCOM and all the enclaves within the COCOM, as well as across COCOMs.  Additionally, need to make sure URL best practices are part of this guidance (e.g., \"Cool URLs Don't Change\" from Tim Berners Lee)"
                  },
                  "size": 100
                },
                {
                  "id": "1122",
                  "name": "1.1.2.2 - Time Synchronization",
                  "data": {
                    "TagValue_UID": "0174",
                    "TagValue_Number": "1.1.2.2",
                    "TagValue_Service Name": "Time Synchronization",
                    "TagNotes_Service Definition": "The Time Synchronization service establishes a consistent time to be used by all DI2E IT resources.",
                    "TagNotes_Service Description": "Time Synchronization provides a consistent time reference for all DI2E devices, thus supporting secure log ins, service interoperability, trustworthy database transactions, and accurate monitoring activities.  The goal is to use well established and ubiquitous method of time synchronization that responds to time requests from any internet client and is consistent among all DI2E nodes & community related nodes.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "• Microsoft Server\n• Linux native NTP Daemon",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "6",
                    "TagValue_JCA Alignment": "6.2.4.2 Utilize PNT Information",
                    "TagNotes_JCSFL Alignment": "1.5.11   Translate Time Data\n8.1.23 - Synchronize Network Timing \n8.7.23  Disseminate Time Data",
                    "TagValue_JARM/ESL Alignment": "3.01.04 Timing and Frequency Device Services",
                    "TagNotes_Comments": "What is important here is that the time sync across the networks is from a common source (e.g., GPS or USNO).  This should be pretty much a done deal on all our networks, just need to confirm.  What we also should do is identify how a given app can access that time from the OS it's using, and we need to make sure that the OS in VMs can access the NTP as well."
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "113",
              "name": "1.1.3 - Enterprise Monitoring",
              "data": {
                "TagValue_UID": "0169",
                "TagValue_Number": "1.1.3",
                "TagValue_Service Name": "Enterprise Monitoring",
                "TagNotes_Service Definition": "The Enterprise Monitoring family monitors services and websites across the  enterprise.",
                "TagNotes_Service Description": "Enterprise Monitoring services measure and report the health and performance of web services as well as DI2E servers and websites.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "1131",
                  "name": "1.1.3.1 - Fault Detection",
                  "data": {
                    "TagValue_UID": "0300",
                    "TagValue_Number": "1.1.3.1",
                    "TagValue_Service Name": "Fault Detection",
                    "TagNotes_Service Definition": "Fault Detection enables system monitoring and identification of a fault occurrence.",
                    "TagNotes_Service Description": "Fault Detection may be implemented as a centralized audit log monitoring and alerting system to which all computing assets push their logging data, as a heartbeat type service that periodically checks to make sure that other services are up and running, or as a local agent deployed on a system that monitors for faults and sends those notifications to a centralized detection location.  When a fault is detected, the Fault Detection service will interact with the Fault Isolation service to derive additional information.  This additional information is used by the Site Monitoring service to send appropriate notifications.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "Same tools as the metrics above.",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.1.1 Network Resource Visibility",
                    "TagNotes_JCSFL Alignment": "9.1.18 Detect Equipment Faults\n9.3.8 Monitor and Report Software Faults\n9.3.9 Monitor and Report Hardware System Faults",
                    "TagValue_JARM/ESL Alignment": "5.04.03 Enterprise IT Environment Services",
                    "TagNotes_Comments": "Generally, Fault detection/isolation is build on top of the metrics monitoring and reporting tools noted above.  The additional stuff includes rules processing that allows for histerisis and trend detection, as well as for analytics for tracing connections among faults (e.g., sometimes a single fault will result in may down-stream faults)."
                  },
                  "size": 100
                },
                {
                  "id": "1132",
                  "name": "1.1.3.2 - Fault Isolation",
                  "data": {
                    "TagValue_UID": "0301",
                    "TagValue_Number": "1.1.3.2",
                    "TagValue_Service Name": "Fault Isolation",
                    "TagNotes_Service Definition": "Fault Isolation enables pinpointing the type of fault and its location.",
                    "TagNotes_Service Description": "Fault Isolation works in cooperation with Fault Detection, to identify information about a fault when one has been detected.  This information may include which system/service has failed, the operations or jobs that were being processed at time of failure, and information about currently logged-on or authenticated users, as well as standard date/time and system location information.  This information is provided to the Site Monitoring service to provide appropriate notifications.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "Same tools as the metrics above.",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.1.1 Network Resource Visibility",
                    "TagNotes_JCSFL Alignment": "9.1.19 Provide On-Line Fault Isolation\n9.2.4 Perform Network Fault Isolation and Reconfiguration\n9.2.5 Monitor Faults",
                    "TagValue_JARM/ESL Alignment": "5.04.03 Enterprise IT Environment Services",
                    "TagNotes_Comments": "Generally, Fault detection/isolation is build on top of the metrics monitoring and reporting tools noted above.  The additional stuff includes rules processing that allows for histerisis and trend detection, as well as for analytics for tracing connections among faults (e.g., sometimes a single fault will result in may down-stream faults)."
                  },
                  "size": 100
                },
                {
                  "id": "1133",
                  "name": "1.1.3.3 - Site Monitoring",
                  "data": {
                    "TagValue_UID": "0171",
                    "TagValue_Number": "1.1.3.3",
                    "TagValue_Service Name": "Site Monitoring",
                    "TagNotes_Service Definition": "Site Monitoring provides the ability to measure and report the health and performance of websites and servers.",
                    "TagNotes_Service Description": "Site Monitoring measures and reports the health and performance of web sites and servers through the collection of enterprise-defined metrics.  It provides a vendor, platform, network, and protocol neutral framework that shares a common messaging protocol to unify infrastructure status reporting.  In the event of a system fault or failure, Site Monitoring will use the Notification service to send event notifications to the appropriate subscribers.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "• Compuware Gomez\n• Host Tracker\n• Monitor.us\n• Nagios, \n\nSame tools as the metrics above.",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.3.1.1 Network Resource Visibility",
                    "TagNotes_JCSFL Alignment": "3.5.24  Monitor Data Link Status\n3.5.26  Display System Status\n3.5.27 Monitor System Status\n8.1.13  Maintain Network and Communication Status \n8.1.16  Manage Network Performance\n8.1.24  Monitor and Assess Network \n8.1.25  Manage Status of Network Assets \n8.1.26  Perform System and Network Analysis \n8.1.32  Calculate Network Loads\n8.1.33  Calculate Processing Availability \n8.1.67  Manage Message Traffic \n8.1.77  Assess Network Equipment Performance \n8.1.81  Balance Processing Loads \n8.1.84  Track Network User Activity \n8.5.11  Monitor Network Usage\n8.7.62  Monitor System and Resource Allocation\n9.2.15  Report System Status\n9.2.16  Maintain System Status\n9.3.10 Receive and Register System Status",
                    "TagValue_JARM/ESL Alignment": "8.33.01 Service Monitoring; 5.04.02 Infrastructure and Performance Monitoring Services",
                    "TagNotes_Comments": "Generally, Fault detection/isolation is build on top of the metrics monitoring and reporting tools noted above.  The additional stuff includes rules processing that allows for histerisis and trend detection, as well as for analytics for tracing connections among faults (e.g., sometimes a single fault will result in may down-stream faults)."
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "114",
              "name": "1.1.4 - Event Notification",
              "data": {
                "TagValue_UID": "0190",
                "TagValue_Number": "1.1.4",
                "TagValue_Service Name": "Event Notification",
                "TagNotes_Service Definition": "Eventing capabilities provide a federated, distributed, and fault-tolerant enterprise message bus delivering performance, scalable and interoperable asynchronous event notifications Quality of Service (QoS), guaranteed delivery to disconnected users or applications, and decoupling of information among producers and consumers.",
                "TagNotes_Service Description": "The Eventing line provides a federated, distributed, and fault-tolerant enterprise message bus. It delivers high performance, scalable and interoperable asynchronous event notifications to both applications and end-users. This service supports the configuration of Quality of Service (QoS) for a published message including the priority, precedence, and time-to-live (TTL); provides guaranteed delivery to disconnected users or applications; and utilizes multiple message brokers, potentially within different administrative domains to support the distributed, federated nature of the GIG. Messaging services promote decoupling of information among producers and consumers.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "RabbitMQ, JMS, ActiveMQ",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "This is more likely the message queuing capability.  Need to support fire and forget with reliability"
              },
              "size": 100,
              "children": [
                {
                  "id": "1141",
                  "name": "1.1.4.1 - Notification Producer",
                  "data": {
                    "TagValue_UID": "0191",
                    "TagValue_Number": "1.1.4.1",
                    "TagValue_Service Name": "Notification Producer",
                    "TagNotes_Service Definition": "Notification Producer service serves as the generic interface for publishing an event related to a topic.",
                    "TagNotes_Service Description": "Notification Producer service sends notifications that an event occurred with data about that event to Notification Consumer services.    In doing so, the producer does not know about the consumers in advance and the set of consumers may change over time.    Related operations include (but may not be limited to) notification production (sending); pull pointing; creating a pullpoint; subscription management, providing a subscription/notify interface (for notification push); and sending event notifications to a Notification Broker service in order to publish notifications on given topics.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "Apache CXF, SMOA NTF",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "6",
                    "TagValue_JCA Alignment": "6.2.3.6 Enterprise Messaging",
                    "TagNotes_JCSFL Alignment": "3.3 Provide Indications, Warnings, and Alerts - Group\n7.1.55  Manage Messaging Services",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "This is probably a library that producers and consumers can import and that allow the producers and consumers to interact with the queuing system by supporting the actual events and the ability to push events onto and pull event details from the queues."
                  },
                  "size": 100
                },
                {
                  "id": "1142",
                  "name": "1.1.4.2 - Notification Broker",
                  "data": {
                    "TagValue_UID": "0192",
                    "TagValue_Number": "1.1.4.2",
                    "TagValue_Service Name": "Notification Broker",
                    "TagNotes_Service Definition": "Notification Broker service delivers notifications of events to end-users both within nodes and across node boundaries.",
                    "TagNotes_Service Description": "Notification Broker service provides the ability to manage receipt and delivery of event notifications that may need to proxied, routed, and brokered until the receiver accepts the event notification. It provides a mechanism to manage event notifications within a group to provide unit of order delivery (i.e. you want the stand-down message to come in the correct order compared to the attack message).    Some event notifications may not be able to be delivered timely due to network issues, offline, errors etc.  This component queues them up and manages successful delivery.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "Apache CXF, SMOA NTF\n\n WSO2 Message Broker, JMS, Fuse, RabbitMQ",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "6",
                    "TagValue_JCA Alignment": "6.2.3.6 Enterprise Messaging",
                    "TagNotes_JCSFL Alignment": "3.3.13 Manage Alerts and Indications\n7.1.4  Send Messages on Timed Delivery Basis\n7.1.77 Coordinate Message Delivery\n8.1.11 Provide MessageServices\n8.5.32 Reroute Messages",
                    "TagValue_JARM/ESL Alignment": "5.01.01 Data Transport Services",
                    "TagNotes_Comments": "Another term for this is pub/sub.   Brokers are typically built on top of queuing systems.   Not clear how this is different from the event notification function above."
                  },
                  "size": 100
                },
                {
                  "id": "1143",
                  "name": "1.1.4.3 - Notification Consumer",
                  "data": {
                    "TagValue_UID": "0193",
                    "TagValue_Number": "1.1.4.3",
                    "TagValue_Service Name": "Notification Consumer",
                    "TagNotes_Service Definition": "Notification Consumer service subscribes the producer services informing it that it wishes to start receiving notifications.",
                    "TagNotes_Service Description": "Notification Consumer service sets up end points to receive, either through pushed or pulled delivery, event notifications sent to it by Notification Producer or Notification Broker services.  In doing so, it can set up subscriptions and also unsubscribe from the producer or broker informing it that it wishes to stop receiving event notifications.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "Apache CXF, SMOA NTF",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "6",
                    "TagValue_JCA Alignment": "6.2.3.6 Enterprise Messaging",
                    "TagNotes_JCSFL Alignment": "8.1.55 - Provide Subscription Services",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "This is probably a library that producers and consumers can import and that allow the producers and consumers to interact with the queuing system by supporting the actual events and the ability to push events onto and pull event details from the queues."
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "115",
              "name": "1.1.5 - Enterprise Resource Management",
              "data": {
                "TagValue_UID": "0302",
                "TagValue_Number": "1.1.5",
                "TagValue_Service Name": "Enterprise Resource Management",
                "TagNotes_Service Definition": "Enterprise Resource Management defines the set of services that support unambiguous, assured, and unique identities for both person and non-person entities (NPE).",
                "TagNotes_Service Description": "Enterprise Resource Management has a critical dependency on authoritative personnel and asset management systems for identity information, as well as Directory Services for the management of identity information.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "OpenDJ, OpenLDAP,  OpenIDM",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "This can leverage LDAP capabilities, as well as full-service identity management tools with workflow management, etc."
              },
              "size": 100,
              "children": [
                {
                  "id": "1151",
                  "name": "1.1.5.1 - Global Unique Identifier (GUID)",
                  "data": {
                    "TagValue_UID": "0172",
                    "TagValue_Number": "1.1.5.1",
                    "TagValue_Service Name": "Global Unique Identifier (GUID)",
                    "TagNotes_Service Definition": "The GUID service provides a Uniform Resource Identifier (URI) to an entity in the enterprise",
                    "TagNotes_Service Description": "A GUID or Uniform Resource Identifier (URI) in the DI2E context provides a unique form of identification for any resource within the enterprise. A resource is anything that is or can be associated to the  Enterprise.  This can includes  nodes, data, information, role definitions and people.",
                    "TagValue_Example Specification": "OASIS Extensible Resource Descriptor (XRD) 1.0 Committee Draft 02.",
                    "TagValue_Example Solution": "XRI.net\n\nGUIDE",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.1.62  Provide Namespace Services\n8.2.18  Perform Registration Services",
                    "TagValue_JARM/ESL Alignment": "8.24.02 Asset Cataloging / Identification; 6.02.01 Unique Identifier Services",
                    "TagNotes_Comments": "GUID is not identity management.    Can be implemented as library, .war or as SOA service. GOTS and stds exist for this.  Recent conversation on DIB Hub related to GUIDE https://forum.macefusion.com/phpbb3/viewtopic.php?f=15&t=150 \n\nNote that this has been one of the key areas of focus of the IC ITE effort... after much painful negotiation, a standard was agreed to by the IC community... every discrete piece of intelligence product is supposed to be tagged with a GUIDE ID at its earliest point of consumability (if not sooner!)... the GUIDE ID is an extension/variation of CIA's original implementation of this.  In particular, an IC ITE GUIDE ID has two parts: a prefix and a suffix.  The prefix is an arbitrary number that will be assigned to an Intel producing organization (agency) by the TIC (an existing ODNI group)... an agency may end up having several assigned prefix numbers, perhaps dedicated to particular product types or producing systems.  The suffix can be assigned in one of two ways. The preferred approach is to use one of the available industry standard random generators to assign a 32 hex-character number (which is usually displayed as a 36-character string, after inserting 4 hyphens, at standard locations, to break up the hex number for readability reasons), the other (optional) approach is to assign some other unique string value, with the proviso that this string has to be unique (when combined with the associated prefix value), and with the proviso that the string cannot be of substantive intelligence significance (i.e., you can't use a BE Number or some other well-known intelligence-related code, because this value, by itself, must be meaningless).\n\nThe IC is going to force Intel producers to start assigning these things, so the ScvV-4 should align with this, rather than allowing some kind of variant of this approach."
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "12",
          "name": "1.2 - Security Management",
          "data": {
            "TagValue_UID": "0175",
            "TagValue_Number": "1.2",
            "TagValue_Service Name": "Security Management",
            "TagNotes_Service Definition": "Security Management encompasses the processes and technologies by which people and systems are identified, vetted, credentialed, authenticated, authorized for access to resources, and held accountable for their actions.",
            "TagNotes_Service Description": "Line",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "Line",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "family",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "none"
          },
          "size": 100,
          "children": [
            {
              "id": "121",
              "name": "1.2.1 - Identity and Access Management",
              "data": {
                "TagValue_UID": "0176",
                "TagValue_Number": "1.2.1",
                "TagValue_Service Name": "Identity and Access Management",
                "TagNotes_Service Definition": "Identity and Access Management (IdAM) defines the set of services that manage permissions required to access each resource.",
                "TagNotes_Service Description": "IdAM includes services that provide criteria used in access decisions and the rules and requirements assessing each request against those criteria.  Resources may include applications, services, networks, and computing devices.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "1211",
                  "name": "1.2.1.1 - Local Identity Management",
                  "data": {
                    "TagValue_UID": "0371",
                    "TagValue_Number": "1.2.1.1",
                    "TagValue_Service Name": "Local Identity Management",
                    "TagNotes_Service Definition": "Local Identity Management provides the creation, maintenance, and deletion of user accounts, password maintenance, and the administration of user access rights. Also Creates local account from PKI certificate.  Maps DN to UserID in local account.",
                    "TagNotes_Service Description": "Local Identity Management provides the creation, maintenance, and deletion of user accounts, group membership and other user attributes, password maintenance, and the administration of user access rights for local, role-based access control systems. This service also creates local accounts from PKI certificates, and maps the DN from a certificate to a local UserID.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "none",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "0",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.2.11 Provide Identity Management Services\n8.8.3 Manage Network Information Access for User Account\n8.8.5 Assign Roles and Privileges\n8.8.6 Create and Maintain Network User Account and Profile\n8.8.18 Delete User\n8.8.19 Request System Access\n8.8.22 Manage Local Information Access for User Account\n8.8.23 Create and Maintain Local User Account and Profile",
                    "TagValue_JARM/ESL Alignment": "8.27.01 Identity Management; 8.27.05 Credential Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "12110",
                  "name": "1.2.1.10 - Attribute Access",
                  "data": {
                    "TagValue_UID": "0177",
                    "TagValue_Number": "1.2.1.10",
                    "TagValue_Service Name": "Attribute Access",
                    "TagNotes_Service Definition": "Attribute Access provides values that describe human users and non-person entities (NPE) for the purpose of making authorization decisions.",
                    "TagNotes_Service Description": "Attribute Access  accepts attribute requests from authorized consumers and responds with an attribute assertion containing the requested attribute values that are releasable to that consumer.   Attributes for either human users or non-person entities (NPE) can be passed and are typically used by Policy Decision Services, Policy Decision Points, Application Servers, or other SAML endpoints.   Secure attribute passing supports an Attribute-Based Access Control (ABAC) authorization model.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "NCES:\n- Robust Certificate Validation Service (provider:  DISA DoD PKI Program)\n\nOpenAM, SSO Toolkit",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": " 6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.2.11  Provide Identity Management Services\n8.2.18  Perform Registration Services\n8.2.19 Validate User Credentials\n8.2.20 Authorize Data Access\n8.8.3  Manage Accounts\n8.8.5  Assign Roles and Privileges\n8.8.6  Maintain and Create User Profile",
                    "TagValue_JARM/ESL Alignment": "8.27.04 Attribute Management",
                    "TagNotes_Comments": "This is done in association with or after authentication.  Additional examples not yet in garage: OpenLDAP, UAAS services deployed on high-side and their mirror's on SIPR, UAAS spec for identifying attributes.  Finally, see note for PDP.  Generally, the authorized attribute service pulls authorization-related attributes from disparate authoritative sources (e.g., HR systems, accreditation databases, training databases, etc.) and caches them locally for serving up to PDPs and to protected resources that have embedded their policy decision-making internally to the protected app."
                  },
                  "size": 100
                },
                {
                  "id": "12111",
                  "name": "1.2.1.11 - Certificate Validation",
                  "data": {
                    "TagValue_UID": "0178",
                    "TagValue_Number": "1.2.1.11",
                    "TagValue_Service Name": "Certificate Validation",
                    "TagNotes_Service Definition": "Certificate Validation ensures a presented security assertion within a security token matches similar data in the Certification Authority's (CA) root certificate.",
                    "TagNotes_Service Description": "Certificate Validation ensures a presented PKI certificate has been signed by a trusted CA, is within the valid timeframe, has not been tampered with, has not been revoked, etc.  Certificate Validation Service allows clients to delegate some or all certificate validation tasks and is especially useful when a client application has limited Public Key Infrastructure (PKI) services.   The service corresponds to a “Tier 2 Validation Service” and shields client applications from such PKI complexities as X.509v3 certificate syntax processing (e.g., expiration), revocation status checking, or certificate path validation.  \nCertificate Validation may include any OCSP responders.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "Picket Link, Picket Fence. METRO,\nApache CXF\n\nOpenAM, SSO Toolkit, ESF2",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": "6.2.3.5 Common Identity Assurance Services",
                    "TagNotes_JCSFL Alignment": "8.2.19  Validate User Credentials\n8.2.6  Provide Entity Authentication Services",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "Additional examples not yet in garage: OCSP Responder"
                  },
                  "size": 100
                },
                {
                  "id": "1212",
                  "name": "1.2.1.2 - Credential Management",
                  "data": {
                    "TagValue_UID": "402",
                    "TagValue_Number": "1.2.1.2",
                    "TagValue_Service Name": "Credential Management",
                    "TagNotes_Service Definition": "Life-cycle management of credentials (e.g., PKI soft token) associated with digital identities. Credential management includes PKI life-cycle management of Identity and Non Person Entity (NPE) (e.g., server) keys and certificates.",
                    "TagNotes_Service Description": "none",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "3",
                    "TagValue_DCGS Enterprise Status": "0",
                    "TagValue_JCA Alignment": "6.2.3.5 Common Identity Assurance Services; 6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.2.19  Validate User Credentials\n8.2.6  Provide Entity Authentication Services",
                    "TagValue_JARM/ESL Alignment": "8.27.02 Authentication Management",
                    "TagNotes_Comments": ""
                  },
                  "size": 100
                },
                {
                  "id": "1213",
                  "name": "1.2.1.3 - Resource Policy Management",
                  "data": {
                    "TagValue_UID": "0372",
                    "TagValue_Number": "1.2.1.3",
                    "TagValue_Service Name": "Resource Policy Management",
                    "TagNotes_Service Definition": "Resource Policy Management creates and maintains access control policies for protected resources.",
                    "TagNotes_Service Description": "none",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "none",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "0",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.27.19 IA Guidance Specification and Policy; 8.27.20 Digital Policy Management; 5.05.05 User Attribute Service",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "1214",
                  "name": "1.2.1.4 - Authentication Service",
                  "data": {
                    "TagValue_UID": "0373",
                    "TagValue_Number": "1.2.1.4",
                    "TagValue_Service Name": "Authentication Service",
                    "TagNotes_Service Definition": "Authentication Service performs authentication of entities within the enterprise.",
                    "TagNotes_Service Description": "none",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "none",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "0",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.2.6 Provide Entity Authentication Services",
                    "TagValue_JARM/ESL Alignment": "8.27.02 Authentication Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "1215",
                  "name": "1.2.1.5 - Policy Decision Point",
                  "data": {
                    "TagValue_UID": "0181",
                    "TagValue_Number": "1.2.1.5",
                    "TagValue_Service Name": "Policy Decision Point",
                    "TagNotes_Service Definition": "Policy Decision Points accepts access requests and returns whether the request is appropriate given access rules and conditions.",
                    "TagNotes_Service Description": "A Policy Decision Point service hosts Quality of Protection (QoP) parameters and user attributes so that services can flexibly determine and execute protection measures.   For example, some services may require X.509 certificate based authentication whereas others may only need username / password authentication.   Or, clients that access a resource from different domains may require different “strengths” of authentication and access control.   PDPs do this by accepting authorization queries (typically XACML based), evaluating the request based on a variety of inputs (target resource, requested operation, requester's identity, etc.) and returning authorization decision assertions.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "IBM Data Power, Jericho System: Enterspace Decision Service\n\nOpenAM, SSO Toolkit, OS ABAC/PicketLink, ESF2",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.1.57 - Authorize Network Resource Request\n8.2.20  Authorize Data Access\n8.9.2  Provide Environment Control Policy Enforcement Decisions",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "In the IC, this is often co-located with the authorized attribute source, since the PDP needs the authorized attributes to make its decisions.  Also, consumers can decide whether to go to  the one place for policy decisions or for the attributes.  Likewise, the PEP may be provided by the same product that supports the PDP, or may be custom-built, or a generic part of the container."
                  },
                  "size": 100
                },
                {
                  "id": "1216",
                  "name": "1.2.1.6 - Policy Enforcement Point",
                  "data": {
                    "TagValue_UID": "0182",
                    "TagValue_Number": "1.2.1.6",
                    "TagValue_Service Name": "Policy Enforcement Point",
                    "TagNotes_Service Definition": "Policy Enforcement Point (PEP)enforces security-related policies for access to protected resources.",
                    "TagNotes_Service Description": "Policy Enforcement Points sit in front of the requested web services, intercepting incoming requests and outgoing responses to apply the appropriate access policies.  The PEP generates the access request to the Policy Decision Point (PDP) and interprets and enforces the PDP's decision.   Policies can be built to include, but are not limited to, authentication, authorization, data integrity, and confidentiality. When enforcing authorization policies, the PEP uses the PDP to evaluate an authorization policy for the resource.  When a security token is provided to the PEP, that token is validated prior to being trusted.  Token validation may occur in the PEP itself, or if the PEP is unable to validate the token, the PEP may leverage an STS to perform validation on its behalf.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "• IBM DataPower\n• Layer 7\n\nOpenAM Fedlet, OpenAM Web Agent, OpenAM JEE Agent, SSO  Toolkit, OS ABAC/PicketLink, ESF2",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.2.1  Perform Network Information Assurance (IA)/Computer Network Defense (CND) Services\n\n8.2.3 Regulate Information Dissemination\n8.8.17  Monitor Access Control\n\n8.9.3  Provide Environment Control Policy Enforcement Services",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "See notes related to PDP.  Note also that PEPs are often provided natively with the protected resources' container.  Recommend leveraging Spring Security for this to allow easy integration of different PEPs and PDPs."
                  },
                  "size": 100
                },
                {
                  "id": "1217",
                  "name": "1.2.1.7 - Policy Access Point",
                  "data": {
                    "TagValue_UID": "0183",
                    "TagValue_Number": "1.2.1.7",
                    "TagValue_Service Name": "Policy Access Point",
                    "TagNotes_Service Definition": "Policy Access Point enables service providers and applications to request and retrieve access policies.",
                    "TagNotes_Service Description": "The Policy Access Point exposes authorization policies by retrieving and managing policies as implemented by Policy Decision Point (PDP) logic (e.g., access control over portlets in a portal server, or whether a service consumer may search the registry for the key to retrieve all implementations). Implementation may optionally be published in the Enterprise Service Registry (ESR) as defined by the DoD/IC Service Registry and Governance Working Group.     [SSP note: a recommended format for PAS messages is extensible Access Control Markup Language (XACML)]",
                    "TagValue_Example Specification": "OASIS extensible Access Control Markup Language (XACML)",
                    "TagValue_Example Solution": "OS ABAC, OpenAM (maybe), SSO Toolkit (maybe)",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "8.2.68 Determine Network Security Requirements",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "OpenAM distributes authorization policies from the enterprise server to any distributed JEE or web agent.  Not sure if it distributes them to fedlets.  Note that this is usually associated with PDPs."
                  },
                  "size": 100
                },
                {
                  "id": "1218",
                  "name": "1.2.1.8 - Security Token Service",
                  "data": {
                    "TagValue_UID": "0179",
                    "TagValue_Number": "1.2.1.8",
                    "TagValue_Service Name": "Security Token Service",
                    "TagNotes_Service Definition": "Security Token Service creates enterprise security tokens to provide authentication across systems and services.",
                    "TagNotes_Service Description": "Security Token Service forms the basis for identification and authentication activities by generating security tokens and inserting them in outgoing encrypted messages.   Passed security tokens contain authentication and attribute assertions that temporarily authenticate users.  Tokens also contain user attributes and a security assertion which can be validated by receiving policy services.  The Security Token Service may also perform token validation if required.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "IBM Data Power, Jericho System: Enterspace Decision Service \n\nOpenAM, SSO Toolkit",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": "6.2.3.5 Common Identity Assurance Services",
                    "TagNotes_JCSFL Alignment": "8.2.13  Implement National Security Agency (NSA) and/or National Institute of Standards and Technology (NIST) Public Key Cryptography\n8.2.15  Provide Cryptographic Services\n8.2.34  Generate Network Keys\n8.8.8  Manage Cryptographic Accounts\n8.2.33  Distribute and Enable Network Keys\n8.2.35  Manage Network Keys\n8.2.36 Order Network Keys",
                    "TagValue_JARM/ESL Alignment": "4.03.04 Token Services",
                    "TagNotes_Comments": "Ping Identity is being used in some IC elements.  Also, this is associated with both authentication and authorization.  Authentication for Single Sign-on.  Authorization for chaining of identity and related attributes for authorization decisions."
                  },
                  "size": 100
                },
                {
                  "id": "1219",
                  "name": "1.2.1.9 - Federation Service Management",
                  "data": {
                    "TagValue_UID": "0374",
                    "TagValue_Number": "1.2.1.9",
                    "TagValue_Service Name": "Federation Service Management",
                    "TagNotes_Service Definition": "Federation Service Management establishes trust relationships between different organizations or systems, allowing entities within an environment to access resources within another environment without the need to have an individual account on the remote environment.  This service defines and maintains trust relationships, which are then queried by the Security Token Validation service when making validation decisions.",
                    "TagNotes_Service Description": "none",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "none",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "0",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "122",
              "name": "1.2.2 - Cryptography Management",
              "data": {
                "TagValue_UID": "0306",
                "TagValue_Number": "1.2.2",
                "TagValue_Service Name": "Cryptography Management",
                "TagNotes_Service Definition": "Cryptography Management defines the set of services that support the generation, exchange, use, escrow and management of ciphers (e.g., keys), including the use of encryption/decryption processes to ensure the confidentiality and integrity of data.",
                "TagNotes_Service Description": "The Cryptography Management family covers all manner of cryptography-related services.  This includes both symmetric and asymmetric crypto functions, one-way hashing functions, management of encryption keys, digital signature verification, and validation of PKI certificates.  The services within this family may not be discrete, stand-alone services as one would picture in the traditional SOA sense; rather, many Cryptography Management services are likely to be libraries or executables that are embedded within, or bundled with, other applications.  For example, encryption-in-transit is required for many types of data.  An encryption service, for these types of data, would create a chicken-and-egg problem: data must be encrypted before it can be sent to the encryption service, but the encryption service must do the encryption.  In this example, the encryption libraries will likely be resident on the system that generates the sensitive data.  Nonetheless, it is important to explicitly call out the cryptographic functions that are performed within the architecture, to ensure that they are properly accounted for and not overlooked.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "1221",
                  "name": "1.2.2.1 - Encryption/ Decryption",
                  "data": {
                    "TagValue_UID": "0307",
                    "TagValue_Number": "1.2.2.1",
                    "TagValue_Service Name": "Encryption/ Decryption",
                    "TagNotes_Service Definition": "Encryption/Decryption defines the set of services that encrypt and decrypt interactions between consumers and providers to support minimal confidentiality requirements. Within a PKI-environment, encryption and decryption processes are done using the provider’s public and private keys.",
                    "TagNotes_Service Description": "Encryption/Decryption can occur at multiple “levels” within an interaction.  For example, transport level encryption may be sufficient for many interactions. In this case, the provider’s public and private keys are used in the encryption process. For interactions requiring additional encryption, message-level encryption may be used where the subject’s public and private key may be utilized.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "none",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "8.2.7 Decrypt Data\n8.2.8 Encrypt Data\n8.2.12 Provide Embedded Programmable Cryptographic Capability\n8.2.13 Implement National Security Agency (NSA) and/or National Institute of Standards and Technology (NIST) Public Key Cryptography\n8.2.15 Provide Cryptographic Services",
                    "TagValue_JARM/ESL Alignment": "8.27.08 Cryptography Management",
                    "TagNotes_Comments": "Will need to be careful with this.  For US, will be ITAR restricted stuff.  Generally deployed as libraries or modules that can be incorporated into existing or new applications."
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "123",
              "name": "1.2.3 - Security Metadata Management",
              "data": {
                "TagValue_UID": "0309",
                "TagValue_Number": "1.2.3",
                "TagValue_Service Name": "Security Metadata Management",
                "TagNotes_Service Definition": "Security Metadata Management supports the annotation of data items (such as content, documents, records) with the proper security classifications, and applicable metadata for access control (which may include environment metadata).",
                "TagNotes_Service Description": "The Security Metadata Management family of services help ensure that products within the system have proper security markings and labels appropriate to their classification.  These markings may be used by other automated systems, such as the services within the Dissemination Management family, to make authorization or releasability decisions.  While releasability may still have a man-in-the-loop, it is nonetheless important to ensure that consistent, properly-formatted and well-understood security markings are present, to minimize unauthorized disclosure or other classified information spillage.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "1231",
                  "name": "1.2.3.1 - Data Security Marking",
                  "data": {
                    "TagValue_UID": "0310",
                    "TagValue_Number": "1.2.3.1",
                    "TagValue_Service Name": "Data Security Marking",
                    "TagNotes_Service Definition": "Data Security Marking defines the services that create\nand annotate security classification to a specific information resource",
                    "TagNotes_Service Description": "none",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "TDF library",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "8.2.1 Perform Network Information Assurance (IA)/Computer Network Defense (CND) Services",
                    "TagValue_JARM/ESL Alignment": "8.27.16 Security Classification",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "1232",
                  "name": "1.2.3.2 - Security Label Format Validation",
                  "data": {
                    "TagValue_UID": "0136",
                    "TagValue_Number": "1.2.3.2",
                    "TagValue_Service Name": "Security Label Format Validation",
                    "TagNotes_Service Definition": "Security Label Format Validation validates the security marking of metadata.",
                    "TagNotes_Service Description": "Security Label Format Validation validates the security marking of metadata.  Forwards metadata that passes validation.  Rejects metadata that fails validation.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "DDMS, NIEM marking validator.",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "8.2.64 Normalize Content and Semantics",
                    "TagValue_JARM/ESL Alignment": "8.27.16 Security Classification",
                    "TagNotes_Comments": "Could be library, war, and/or SOA service.  Not in place yet.  we ought to promote convergence toward the  security labeling standard that is embedded within the TDF format (and its associated XML schema components)... current plans are to discourage the proliferation of validators for this, and to instead adopt a single (probably NSA-maintained) validator capability...and since DoD is moving to NIEM we need to discourage use of DDMS.  Also want to consider same format for DoD and IC?"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "124",
              "name": "1.2.4 - System and Communication Protection",
              "data": {
                "TagValue_UID": "0312",
                "TagValue_Number": "1.2.4",
                "TagValue_Service Name": "System and Communication Protection",
                "TagNotes_Service Definition": "System and Communication Protection is the family of services that monitor, control, and protect organizational communications (i.e., information transmitted or received by organizational information systems) at the external boundaries and key internal boundaries of the information systems; and employ architectural designs, software development techniques, and systems engineering principles that promote effective information security within organizational information systems.",
                "TagNotes_Service Description": "The System Communication and Protection family of systems focuses primarily on Computer and Network Defense.  Services within this family help to ensure that all systems are properly patched to mitigate or eliminate known vulnerabilities, configured to mitigate not-yet-known (i.e., 0day) vulnerabilities, defend against malicious software, and respond and recover from successful attacks or other security incidents.",
                "TagValue_Example Specification": "\nNIST SP 800-12",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "1241",
                  "name": "1.2.4.1 - Vulnerability Reporting",
                  "data": {
                    "TagValue_UID": "0313",
                    "TagValue_Number": "1.2.4.1",
                    "TagValue_Service Name": "Vulnerability Reporting",
                    "TagNotes_Service Definition": "Vulnerability Reporting provides access to known reported vulnerabilities so appropriate action can be taken.",
                    "TagNotes_Service Description": "none",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "none",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "3.3.8 Maintain Network Operations (NetOps) Related Threat Assessment\n8.2.31 Manage Threats to Network\n\n8.2.55 Disseminate Vulnerability Assessment",
                    "TagValue_JARM/ESL Alignment": "8.27.24 Vulnerability Analysis",
                    "TagNotes_Comments": "Each agency, COCOM, etc will have a set of rules and process for reporting vulnerabilities"
                  },
                  "size": 100
                },
                {
                  "id": "1242",
                  "name": "1.2.4.2 - Intrusion Detection",
                  "data": {
                    "TagValue_UID": "0314",
                    "TagValue_Number": "1.2.4.2",
                    "TagValue_Service Name": "Intrusion Detection",
                    "TagNotes_Service Definition": "Intrusion Detection services support the detection of unauthorized access to an information system.",
                    "TagNotes_Service Description": "Intrusion Detection services monitor each computer in the enterprise and attempt to identify suspicious activity.  In the event that suspicious activity is detected, Intrusion Detection services notify other services so that immediate action can be taken.  Intrusion Detection services, on their own, do not attempt to prevent malicious activity.  They simply detect suspicious activity and notify other services or personnel, who are then responsible for responding appropriately.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "OISF Suricata, SNORT,  OSSEC",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "8.2.2 Detect Security Events\n8.2.10 Monitor Network Security",
                    "TagValue_JARM/ESL Alignment": "8.27.11 Intrusion Detection; 3.02.04 Netwrok Intrusion Detection Services",
                    "TagNotes_Comments": "Plenty of open source IDS engines"
                  },
                  "size": 100
                },
                {
                  "id": "1243",
                  "name": "1.2.4.3 - Intrusion Prevention",
                  "data": {
                    "TagValue_UID": "0315",
                    "TagValue_Number": "1.2.4.3",
                    "TagValue_Service Name": "Intrusion Prevention",
                    "TagNotes_Service Definition": "Intrusion Prevention services include penetration testing and other measures to prevent unauthorized access to an information system.",
                    "TagNotes_Service Description": "Intrusion Prevention services attempt to enforce limitations on process permissions and code execution capabilities to mitigate or stop entirely the execution of malicious software or activity on a system.  Network and host-based firewalls, technologies that enforce application whitelisting, and software that quarantines or removes suspicious software or processes are examples of Intrusion Prevention services.  Intrusion Prevention services often work closely with Intrusion Detection services; in many cases they are bundled together into the same software package.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "OISF Suricata, SNORT,  OSSEC",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "8.2.9 Maintain Secure Data Transmission\n8.2.10 Monitor Network Security\n8.2.16 Provide Transmission Security\n8.2.32 Mitigate Opportunity to Attack\n8.2.54 Perform Network Vulnerability Scanning",
                    "TagValue_JARM/ESL Alignment": "8.27.10 Intrusion Prevention",
                    "TagNotes_Comments": "Generally intrusion detection and prevention go hand-in-hand.  Prevention will also include policies and best practices.  Need some secure coding best practices along with this"
                  },
                  "size": 100
                },
                {
                  "id": "1244",
                  "name": "1.2.4.4 - Virus Protection",
                  "data": {
                    "TagValue_UID": "0316",
                    "TagValue_Number": "1.2.4.4",
                    "TagValue_Service Name": "Virus Protection",
                    "TagNotes_Service Definition": "Virus Protection and Malicious Code services identify and respond to specific security threats, including use of firewalls, anti-spam software, anti-virus software, and malware protection.",
                    "TagNotes_Service Description": "Virus Protection is a specific kind of Intrusion Detection and Intrusion Prevention system aimed at targeting automated- or self-spreading malicious software (malware).  Virus Protection software is often combined with other intrusion detection and prevention software to form a complete host-based security system.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "none",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "8.2.40 Implement Firewall Protection",
                    "TagValue_JARM/ESL Alignment": "4.03.05 Virus Protection and Malicious Code Service",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "1245",
                  "name": "1.2.4.5 - Incident Response",
                  "data": {
                    "TagValue_UID": "0317",
                    "TagValue_Number": "1.2.4.5",
                    "TagValue_Service Name": "Incident Response",
                    "TagNotes_Service Definition": "Incident Response services provide active response and remediation to a security incident that has allowed unauthorized access to an information system",
                    "TagNotes_Service Description": " If a security incident occurs, Incident Response services assist administrators and authorities in stopping the incident to prevent additional damage, determining the scope and impact of the incident, restoring operational capability, performing forensics to determine how the incident occurred, and taking action to ensure that the incident does not occur again.  Incident Response services may also include tools to assist in legal action, if that is part of the organization's incident response plan.  Incident Response services are tied closely to organizational policies and procedures, and may include workflows or other business process tools to assist responders in following pre-established procedures.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "none",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                    "TagNotes_JCSFL Alignment": "8.2.21 Respond to Security Events\n8.2.50 Drop Host Transactions",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "125",
              "name": "1.2.5 - Audit Management",
              "data": {
                "TagValue_UID": "0184",
                "TagValue_Number": "1.2.5",
                "TagValue_Service Name": "Audit Management",
                "TagNotes_Service Definition": "Audit Management defines the set of services that support the creation, persistence, and access of audit information relating to activities to access or\nutilize an application, system, network, or information resource.",
                "TagNotes_Service Description": "family",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "COE believes there is much IC activity going on in this area.  For example, there is a potential new mandate to require IC systems to transmit their audit log posts wrapped in the TDF XML format.  The ScvV-4 ought to defer to (and align with) whatever emerges in this area"
              },
              "size": 100,
              "children": [
                {
                  "id": "1251",
                  "name": "1.2.5.1 - Audit Log Management",
                  "data": {
                    "TagValue_UID": "0185",
                    "TagValue_Number": "1.2.5.1",
                    "TagValue_Service Name": "Audit Log Management",
                    "TagNotes_Service Definition": "The Audit Log Management service supports security auditing by recording system accesses and operations and providing notification for certain events.",
                    "TagNotes_Service Description": "Audit Log Management supports security auditing by recording a chronological record of system activities including system accesses and operations; and providing notification when previously identified events (e.g. a system goes down) occur.    Audit Log Management's purpose is to provide the data needed to enable reconstruction and examination of sequences of activities surrounding, or leading to, a specific event.",
                    "TagValue_Example Specification": "NIST SP 800-92,Guide to Computer Security Log Management",
                    "TagValue_Example Solution": "SolarWinds\ntripwire Log Center\nsplunk\n\nOpenAM, SSO Toolkit, OS ABAC/PicketLink",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.4.3.1 Detect Events",
                    "TagNotes_JCSFL Alignment": "8.2.29  Detect and Record Host and Network Anomalies\n8.8.12  Log Operator Activity",
                    "TagValue_JARM/ESL Alignment": "8.27.06 Audit Management; 5.05.02 Audit Logging Services",
                    "TagNotes_Comments": "Access control audits should be done from the authentication and authorization systems that grant or deny access.  Note that it is as important to audit accesses as denials."
                  },
                  "size": 100
                },
                {
                  "id": "1252",
                  "name": "1.2.5.2 - Audit Log Reporting",
                  "data": {
                    "TagValue_UID": "0186",
                    "TagValue_Number": "1.2.5.2",
                    "TagValue_Service Name": "Audit Log Reporting",
                    "TagNotes_Service Definition": "The Audit Log Reporting service provides analysis reports on information contained in security audit logs.",
                    "TagNotes_Service Description": "Audit Log Reporting works in sync with Audit Log Management to analyze and report audit log information including Quality of Service (QoS) metrics and operational status (availability, faults, etc.).   Ideally, managers are able to leverage audit log reporting to identify and respond to service problems before critical service failures.",
                    "TagValue_Example Specification": "NIST SP 800-92,Guide to Computer Security Log Management",
                    "TagValue_Example Solution": "Audit Logging\n\nSnare (intesectalliance.com), Apache ACE",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.4.3.2 Analyze Events",
                    "TagNotes_JCSFL Alignment": "3.4.22  Create, Edit, and Display Log\n\n8.2.41  Provide Audit Services\n\n8.2.60 Passively Capture and Copy Network Traffic Data\n8.8.12 Log Operator Activity",
                    "TagValue_JARM/ESL Alignment": "8.27.06 Audit Management; 5.05.02 Audit Logging Services",
                    "TagNotes_Comments": "This service is not in the SDT document & SPFG may recommend removing - adjudication with SPFG and SMEs is needed.  - MP, July 29, 2011"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "126",
              "name": "1.2.6 - Cross Domain",
              "data": {
                "TagValue_UID": "0187",
                "TagValue_Number": "1.2.6",
                "TagValue_Service Name": "Cross Domain",
                "TagNotes_Service Definition": "The Cross Domain services aid in the enforcement of security domain policies regarding the exchange of data and services across different security domain boundaries.",
                "TagNotes_Service Description": "Cross Domain enforces security domain policies (and de-conflicts policies) regarding the exchange of data and services across different security domain boundaries (including proxies).  Cross Domain ensures that users or services have the proper clearances and credentials to perform requested activities across two or more network fabrics or security domains.",
                "TagValue_Example Specification": "none",
                "TagValue_Example Solution": "none",
                "TagValue_DI2E Framework Status": "2",
                "TagValue_DCGS Enterprise Status": "1",
                "TagValue_JCA Alignment": "6.3.4 Cyber Management",
                "TagNotes_JCSFL Alignment": "7.1.120 Communicate Information on the Secret Internet Protocol Router Network (SIPRnet)\n7.1.121 Communicate Information on the Non-Classified Internet Protocol Router Network (NIPRnet)\n7.1.122 Communicate Information on the Joint Worldwide Intelligence Communications System (JWICS) Network\n7.1.123 Communicate Information on the Coalition Mission Network\n8.2.22 Access Information at Multiple Levels of Security\n8.2.27 Manage Multiple Security Levels\n8.2.52 Manage Security Configuration",
                "TagValue_JARM/ESL Alignment": "5.05.04 Cross-Domain Services",
                "TagNotes_Comments": "Leverage existing cross domain solutions such as Centaur, etc."
              },
              "size": 100
            }
          ]
        },
        {
          "id": "13",
          "name": "1.3 - Service Management",
          "data": {
            "TagValue_UID": "0188",
            "TagValue_Number": "1.3",
            "TagValue_Service Name": "Service Management",
            "TagNotes_Service Definition": "Service Management capabilities provide publishing of, querying about, subscription and configuration management of services.",
            "TagNotes_Service Description": "Service Management capabilities provide publishing of, querying about, subscription and configuration management of services.    Information can be metadata about the service (registered in a Universal Description Discovery & Integration (UDDI) specification) or artifacts not strictly held in the UDDI registry but directly relevant to the registered service(s) (Installation manuals, test procedures, WSDL definitions, specifications, technical background, related architecture diagrams, etc.)",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "Line",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "131",
              "name": "1.3.1 - Repository and Registry",
              "data": {
                "TagValue_UID": "0221",
                "TagValue_Number": "1.3.1",
                "TagValue_Service Name": "Repository and Registry",
                "TagNotes_Service Definition": "The Repository and Registry family of services enable inquiry, subscription, and publishing, of DI2E services.",
                "TagNotes_Service Description": "The Repository and Registry family of  services enable publishing, inquiry, and subscription of existing system services using the Universal Description Discovery & Integration (UDDI) specification.    Through this set of services service providers are able to register (publish) the services they wish to make available to the DI2E community so that application/system/baseline providers can find & access them (service inquiry) and ask for notification when services that might meet their needs become available or updated (service subscription).",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "1311",
                  "name": "1.3.1.1 - Service Inquiry",
                  "data": {
                    "TagValue_UID": "0222",
                    "TagValue_Number": "1.3.1.1",
                    "TagValue_Service Name": "Service Inquiry",
                    "TagNotes_Service Definition": "Service Inquiry allows consumers to query a service registry to find and retrieve service offerings.",
                    "TagNotes_Service Description": "The Service Inquiry service allows service consumers to locate and obtain detail on service entries in the registry.  This includes individual users and machine-to-machine requests.   Using published service metadata (e.g., by name, category, provider, etc.), service consumers specify the criteria to be used to discover service offerings and then retrieve key service information (service name, description, URL, etc.).   The Inquiry Service also offers the service consumer a means to dynamically find service access points at runtime in order to build location transparency of services into their applications.  Related UDDI standards, which specify Hypertext Transfer Protocol Secure (HTTPS) and authorization support,  allow users and systems to publish, subscribe, and discover web services in a secure manner.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "jUDDI, ER2, DISA MDR\n\nER2, SDF, OMP, DI2E-F Developer Collaboration Environment",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "6",
                    "TagValue_JCA Alignment": "6.2.3.7 Directory Services",
                    "TagNotes_JCSFL Alignment": "8.1.28 Find Services\n8.1.73 Provide Directory Services",
                    "TagValue_JARM/ESL Alignment": "8.33.03 Service Discovery",
                    "TagNotes_Comments": "This should be the same as service registry.  See notes in service registry, as they apply here also."
                  },
                  "size": 100
                },
                {
                  "id": "1312",
                  "name": "1.3.1.2 - Service Subscription",
                  "data": {
                    "TagValue_UID": "0223",
                    "TagValue_Number": "1.3.1.2",
                    "TagValue_Service Name": "Service Subscription",
                    "TagNotes_Service Definition": "Service subscription service notifies potential service consumers of the availability of services as they become registered.",
                    "TagNotes_Service Description": "The Service Subscription service allows service consumers to receive a notification when services of interest are registered in the Service Registry.   Consumers create a subscription containing service criteria that describes services they would be interested in.  When services are registered that fit the specified criteria the consumer is notified via email or a Simple Object Access Protocol (SOAP) message.   A registry must define the policy for supporting subscriptions including whether nodes may define their own policy.  In addition, policies that may be defined include restricting use of subscription, establishing additional authentication requirements, identifying the duration or life of a subscription, limiting subscriptions, and articulating exactly who can do what relative to subscriptions.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "jUDDI, ER2, DISA MDR\n\nER2, SDF, OMP, DI2E-F Developer Collaboration Environment",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "6",
                    "TagValue_JCA Alignment": "6.2.3.7 Directory Services",
                    "TagNotes_JCSFL Alignment": "8.1.55 Provide Subscription Services",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "This should be part of the service registry."
                  },
                  "size": 100
                },
                {
                  "id": "1313",
                  "name": "1.3.1.3 - Service Publishing",
                  "data": {
                    "TagValue_UID": "0224",
                    "TagValue_Number": "1.3.1.3",
                    "TagValue_Service Name": "Service Publishing",
                    "TagNotes_Service Definition": "Service Publishing allows service providers to publish information about available services in a metadata repository and categorize these services within related service taxonomies. Note that the repository is focused on design time lookup.",
                    "TagNotes_Service Description": "The service publishing service allows service providers to publish information about themselves (service name, description, creating organization, file size, sponsoring agency, test accreditations, etc.), their service specifications (applicable standards, related conformance requirements, etc.), service offerings (versions of the service that are available), and service access points (internet URLs where the service can be reached) in a UDDI service registry.   It also allows service providers to categorize their services within related taxonomies.     Publishing limits are controlled through security protocols and related service registry publishing policies.",
                    "TagValue_Example Specification": "Yes,see CDP hosted in SPMT",
                    "TagValue_Example Solution": "jUDDI, ER2, DISA MDR\n\nER2, SDF, OMP, DI2E-F Developer Collaboration Environment",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "6",
                    "TagValue_JCA Alignment": "6.2.3.7 Directory Services",
                    "TagNotes_JCSFL Alignment": "8.1.59 Provide Discovery Services",
                    "TagValue_JARM/ESL Alignment": "8.33.02 Service Publication",
                    "TagNotes_Comments": "This should be part of the service registry."
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "132",
              "name": "1.3.2 - Service Configuration Management",
              "data": {
                "TagValue_UID": "0323",
                "TagValue_Number": "1.3.2",
                "TagValue_Service Name": "Service Configuration Management",
                "TagNotes_Service Definition": "Service Configuration Management provides the set of services to provide configuration management and lifecycle support for registered services",
                "TagNotes_Service Description": "family",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "1321",
                  "name": "1.3.2.1 - Service Configuration Identification",
                  "data": {
                    "TagValue_UID": "0324",
                    "TagValue_Number": "1.3.2.1",
                    "TagValue_Service Name": "Service Configuration Identification",
                    "TagNotes_Service Definition": "Service Configuration Identification defines the services to define and maintain the underlying structure of the Configuration Management System (CMS) so that it is able to hold all information on Service Configuration Items (CIs). This includes specifying the attributes describing CI types and their sub-components, as well as determining their interrelationships.",
                    "TagNotes_Service Description": "Service Configuration Identification stores information about configuration management (CM) controlled system components, called Configuration Items (CIs).  Configuration Items may be source code, compiled GOTS or COTS binaries, hardware lists, system settings or any combination thereof that make a cohesive unit and is under CM control.  Service Configuration Identification tracks specific information about each configuration item in the architecture, its sub-components, and its relationship to other CIs.  It also tracks information about who has authority to approve changes to each configuration item, such as the responsible Configuration Control Board.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "DI2E-F Developer Collaboration Environment",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2 Enterprise Services (ES)",
                    "TagNotes_JCSFL Alignment": "8.1.41 Manage Services\n8.9.7 Manage Enterprise Information Assets",
                    "TagValue_JARM/ESL Alignment": "8.06.03 Configuration Management",
                    "TagNotes_Comments": "This is being provided by DI2E-F Developer  Collaboration Tools at the MACE.  Includes the following - for Code CM: Subversion, Git;\nBuild management/CM: Nexus, Hudson, Maven; Issue tracking/backlog mgmt: Jira\nKnowledge Mgmt: Confluence\n\nNote that developers and production environments will likely also have their own sets of tools to perform configuration management of components that are deployed to production systems."
                  },
                  "size": 100
                },
                {
                  "id": "1322",
                  "name": "1.3.2.2 - Service Configuration Control",
                  "data": {
                    "TagValue_UID": "0325",
                    "TagValue_Number": "1.3.2.2",
                    "TagValue_Service Name": "Service Configuration Control",
                    "TagNotes_Service Definition": "Service Configuration Control defines the services that ensure that no Service Configuration Items are added or modified without the required authorization, and that such modifications are adequately recorded in the CMS. ",
                    "TagNotes_Service Description": "Service Configuration Control ensures that any changes to Configuration Items (CIs) are made by authorized parties, in accordance with the established configuration management policies, processes, and procedures.\n\nNote: Configuration Control enables the review of modifications to the Configuration Management System (CMS), to make sure the information stored in the CMS is complete and the modification was done by an authorized party. Other processes also support the objectives of Configuration Control: Configuration Identification defines who is authorized to make certain changes to the CMS. In a broader sense, Change Management and Release Management with their defined procedures also help to ensure that no unauthorized changes occur.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "DI2E-F Developer Collaboration Environment",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2 Enterprise Services (ES)",
                    "TagNotes_JCSFL Alignment": "8.1.41 Manage Services\n\n8.1.72 Implement Configuration Alterations\n8.9.7 Manage Enterprise Information Assets",
                    "TagValue_JARM/ESL Alignment": "8.06.03 Configuration Management",
                    "TagNotes_Comments": "This is being provided by DI2E-F Developer  Collaboration Tools at the MACE.  Includes the following - for Code CM: Subversion, Git;\nBuild management/CM: Nexus, Hudson, Maven; Issue tracking/backlog mgmt: Jira\nKnowledge Mgmt: Confluence\n\nNote that developers and production environments will likely also have their own sets of tools to perform configuration management of components that are deployed to production systems."
                  },
                  "size": 100
                },
                {
                  "id": "1323",
                  "name": "1.3.2.3 - Service Configuration Verification and Audit",
                  "data": {
                    "TagValue_UID": "0326",
                    "TagValue_Number": "1.3.2.3",
                    "TagValue_Service Name": "Service Configuration Verification and Audit",
                    "TagNotes_Service Definition": "Service Configuration Verification and Audit defines the services that perform regular checks, ensuring that the information contained in the CMS is an exact representation of the Configuration Items (CIs) actually installed in the live production environment.",
                    "TagNotes_Service Description": "none",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "ER2, SDF",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2 Enterprise Services (ES)",
                    "TagNotes_JCSFL Alignment": "8.1.41 Manage Services\n8.9.7 Manage Enterprise Information Assets",
                    "TagValue_JARM/ESL Alignment": "8.06.03 Configuration Management",
                    "TagNotes_Comments": "This belongs in enterprise management, and is a combination of continuous monitoring and VM image management (i.e., there exist mgmt tools that compare VMs with images to see if there are changes and sends alerts on the changes).  Not really a \"services mgmt\" tool as such."
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "14",
          "name": "1.4 - Orchestration Management",
          "data": {
            "TagValue_UID": "0196",
            "TagValue_Number": "1.4",
            "TagValue_Service Name": "Orchestration Management",
            "TagNotes_Service Definition": "Orchestration Management provides automated SOA service as well as human operation modeling and execution.",
            "TagNotes_Service Description": "Orchestration Management services can provide automated SOA service as well as human operation modeling and execution. Currently the DI2E has mapped out services for service orchestration (modeling and execution) but is considering another family of services to cover human workflow (workflow modeling and workflow management).",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "Apache ODE, Orchestra, Intalio, os-workflow, jBPM",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Will need best practices guide.  Also don't want to limit this to BPEL and SOAP services."
          },
          "size": 100,
          "children": [
            {
              "id": "141",
              "name": "1.4.1 - Orchestration Planning",
              "data": {
                "TagValue_UID": "0197",
                "TagValue_Number": "1.4.1",
                "TagValue_Service Name": "Orchestration Planning",
                "TagNotes_Service Definition": "Service Orchestration Planning services provide support functions necessary to complete the process of converting a process model into an executable chain of services.",
                "TagNotes_Service Description": "Service Orchestration Planning services support coordinated arrangement, and management of individual services by ensuring a planning orchestration is feasible from among the available services. Orchestration differs in this sense from choreography in this sense by containing a central controller process that participating web services don't know about.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "Activiti, Intalio, ProcessMaker, BonitaSoft, Uengine",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "I think that this description best matches a BPMN based orchestration engine.  You model the orchestration in BPMN, and then the engine actually runs the process.  Recommend combining with the Services Orchestration above."
              },
              "size": 100,
              "children": [
                {
                  "id": "1411",
                  "name": "1.4.1.1 - Matchmaking",
                  "data": {
                    "TagValue_UID": "0198",
                    "TagValue_Number": "1.4.1.1",
                    "TagValue_Service Name": "Matchmaking",
                    "TagNotes_Service Definition": "Matchmaking service provides a capability to verify that an individual service performs a desired task and that two or more services can be orchestrated by ensuring that the outputs of each service in the planned orchestration contain suitable inputs for the next service in the orchestration.",
                    "TagNotes_Service Description": "Matchmaking services ensure the composability of services by comparing the function of a given service fulfills the task in the business process that the service is expected to complete. For example, if a process includes a step that converts an image file from JPG format to PNG format, any service that performs this task in the orchestration must indeed perform image format conversions.\n\nIn addition, the matchmaker service ensures that each individual service is suitable for orchestration with its predecessors and successors in the orchestration based on the inputs, outputs, preconditions, and effects (IOPEs) of each service. Put simply, in order to orchestration two services, A and B, it is necessary that Service A's outputs contain all the necessary inputs for Service B, those inputs are in the proper format, all security requirements are met, etc.",
                    "TagValue_Example Specification": "• ebXML\n• OWL",
                    "TagValue_Example Solution": "• Oracle Business Process Management suite\n• Bonitasoft",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.9.5 Integrate Enterprise Applications",
                    "TagValue_JARM/ESL Alignment": "6.05.04 Orchestration Services",
                    "TagNotes_Comments": "Combine BPMN engines with ESBs and/or message translators.. Orchestration engines should help with the mapping of output of one service to input of another.  In fact, many orchestration engines are designed to work with ESBs which perform the message translations necessary to map output of one to input of another."
                  },
                  "size": 100
                },
                {
                  "id": "1412",
                  "name": "1.4.1.2 - Optimization",
                  "data": {
                    "TagValue_UID": "0199",
                    "TagValue_Number": "1.4.1.2",
                    "TagValue_Service Name": "Optimization",
                    "TagNotes_Service Definition": "Optimization services provide analysis of the possible orchestrations and offer recommended alternatives to ensure the user's needs are best met from among the available services and orchestrations.",
                    "TagNotes_Service Description": "For any given business process, there may be several possible orchestrations to choose from. These orchestrations may differ only in the instances of specific services that they use (e.g., the same service offered at different endpoints) or they may perform the same process using completely different service offerings. In a dynamic network where connectivity and QoS will vary widely over time, selecting the best orchestration from among the available options will be a complex task.\n\nAdditionally, in an environment where new services or new service instances may appear on the network frequently, it may be necessary to frequently update the orchestration to best meet the current situation.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "TBD",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.9.5 Integrate Enterprise Applications",
                    "TagValue_JARM/ESL Alignment": "6.05.04 Orchestration Services",
                    "TagNotes_Comments": "This looks like a combination of auto-discovery and real-time continuous modeling.  I don't believe anything exists for this yet, especially across a WAN.  This is more in the realm of research today."
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "142",
              "name": "1.4.2 - Orchestration Execution",
              "data": {
                "TagValue_UID": "0200",
                "TagValue_Number": "1.4.2",
                "TagValue_Service Name": "Orchestration Execution",
                "TagNotes_Service Definition": "Orchestration execution performs the ordered calling of services as designed in service orchestration models (see orchestration modeling service).",
                "TagNotes_Service Description": "Orchestration execution makes in-sequence calls to services, holds intermediate inputs/outputs, handles exceptions, monitors execution status, and provides an interface for user presentation. \n\nCommon orchestration execution features include: W3C XHTML/CSS conformance, templating, class autoloading, object based design, nested groups with multiple assigned permissions, form design tools including help and error messages, labels, validation rules, inherited authorization, and macros support.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "family",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "1421",
                  "name": "1.4.2.1 - Execution Engine",
                  "data": {
                    "TagValue_UID": "0201",
                    "TagValue_Number": "1.4.2.1",
                    "TagValue_Service Name": "Execution Engine",
                    "TagNotes_Service Definition": "An execution engine takes the definition of a service orchestration (e.g., BPEL) and executes the process",
                    "TagNotes_Service Description": "Service orchestrations are only definitions of how services should be executed in sequence to complete a given business process. Execution of the orchestration requires some form of engine that will run the orchestrated process and return a result to the user. Most existing execution engines execute processes defined in Business Process Execution Language (BPEL). Other possibilities are available, including newer version of Business  Process Modeling Notation that are executable; mashup frameworks, and similar technologies",
                    "TagValue_Example Specification": "• BPMN v2.0\n• Business Process Execution Language(BPEL)",
                    "TagValue_Example Solution": "• Oracle BPM suite\n• Bonitasoft",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.9.5 Integrate Enterprise Applications",
                    "TagValue_JARM/ESL Alignment": "6.05.04 Orchestration Services",
                    "TagNotes_Comments": "Various open source tools exist.  Also, such engines are generally included in ESBs.  Google \"Open Source Orchestration  Engine\" to find lists of tools.  I would recommend a BPMN tool vice a BPEL tool, as BPEL is really limited to SOAP services, and it is the process model that is more important for interoperability (across engines) than the BPEL scripts."
                  },
                  "size": 100
                },
                {
                  "id": "1422",
                  "name": "1.4.2.2 - Protocol Mediation",
                  "data": {
                    "TagValue_UID": "0202",
                    "TagValue_Number": "1.4.2.2",
                    "TagValue_Service Name": "Protocol Mediation",
                    "TagNotes_Service Definition": "Protocol Mediation provides the ability to manage service communication format between sender and receiver.",
                    "TagNotes_Service Description": "The Protocol Mediation service provides transformation and processing of service communication between sending and receiving services.  One example is mediating e-mail messages between POP to IMAP formats.    Another example is chat mediation between IRC or XMPP formats. Mediation between SOAP and REST is another example.",
                    "TagValue_Example Specification": "Business Process Execution Language(BPEL)",
                    "TagValue_Example Solution": "Oracle BPM suite; Bonitasoft\n\nDDF, Apache Camel, Apache ServiceMix, Apache Geronimo",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.9.5 Integrate Enterprise Applications",
                    "TagValue_JARM/ESL Alignment": "6.05.04 Orchestration Services",
                    "TagNotes_Comments": "Specifications include all of the protocols that need to  be mediated,  BPEL is not one of them (see column H)."
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "143",
              "name": "1.4.3 - Application and Website Hosting",
              "data": {
                "TagValue_UID": "380",
                "TagValue_Number": "1.4.3",
                "TagValue_Service Name": "Application and Website Hosting",
                "TagNotes_Service Definition": "Application and Website Hosting services provide a framework for hosting and deploying web-based applications by handling transactions, security, scalability, concurrency and management of the components they deploy. This enables developers to concentrate more on the business logic of the components rather than on infrastructure and integration tasks.",
                "TagNotes_Service Description": "Application and Website Hosting provides a framework for hosting and deploying web based applications or content. The framework typically includes the application server, which provides a generalized approach for creating an application-server implementation and handles application operations between users and an organization's backend business applications. It consists of web server connectors, runtime libraries and database connectors. \nThe application server runs behind a web server and usually in front of a database. Web applications run on top of application servers, are written in languages the application server supports, and call the runtime libraries and components the application server provides.",
                "TagValue_Example Specification": "Java - \nJava EE 6\nServlet\nJSP\n\n.NET - \n.NET Framework",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "2",
                "TagValue_DCGS Enterprise Status": "1",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "1431",
                  "name": "1.4.3.1 - Web Content Delivery",
                  "data": {
                    "TagValue_UID": "381",
                    "TagValue_Number": "1.4.3.1",
                    "TagValue_Service Name": "Web Content Delivery",
                    "TagNotes_Service Definition": "Web Content Delivery services serve up dynamic and static website content.",
                    "TagNotes_Service Description": "The Web Content Delivery service serves content to clients. It also receives requests and supports server-side scripting. \n The commonly used specification is HTTP.",
                    "TagValue_Example Specification": "HTTP",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.1.80 Populate and Render Data Entry Form via Web Browser",
                    "TagValue_JARM/ESL Alignment": "5.03.01 Web Hosting Services",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "1432",
                  "name": "1.4.3.2 - Security Access Proxy",
                  "data": {
                    "TagValue_UID": "382",
                    "TagValue_Number": "1.4.3.2",
                    "TagValue_Service Name": "Security Access Proxy",
                    "TagNotes_Service Definition": "Proxy Management services provide bi-directional access to security services.",
                    "TagNotes_Service Description": "The Proxy Management service provides bi-directional access to security services for authentication and authorization purposes.. It may also utilize these security services in the establishment of trusted relationships, verification of certificates, and the enforcement of access policies to protected resources.",
                    "TagValue_Example Specification": "UAAS",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.22.07 Service Brokering",
                    "TagNotes_Comments": "Name needs to be improved"
                  },
                  "size": 100
                },
                {
                  "id": "1433",
                  "name": "1.4.3.3 - Transaction Processing",
                  "data": {
                    "TagValue_UID": "383",
                    "TagValue_Number": "1.4.3.3",
                    "TagValue_Service Name": "Transaction Processing",
                    "TagNotes_Service Definition": "Transaction Processing services provide connectors to services that utilize data sources",
                    "TagNotes_Service Description": "Transaction Processing services provide connectors to services that handle access, query and retrieval from data sources, such as a DBMS or file system. They also can provide interfaces with wrappers for federated searches within CD&R components.",
                    "TagValue_Example Specification": "JDBC, ODBC",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.1.69 Provide Web Applications\n8.1.79 Provide Proxy Services",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "1434",
                  "name": "1.4.3.4 - Application Management",
                  "data": {
                    "TagValue_UID": "384",
                    "TagValue_Number": "1.4.3.4",
                    "TagValue_Service Name": "Application Management",
                    "TagNotes_Service Definition": "Application Management services provide the capability for application and web servers to deploy, install, activate and deactivate applications.",
                    "TagNotes_Service Description": "Application Management services provide the capability for application and web servers to deploy, install, activate and deactivate applications.  They also supply support for the scaling and concurrency of applications running on the server.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.1.9 Provide Network Applications Services",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "id": "2",
      "name": "2 - Common Services",
      "data": {
        "TagValue_UID": "0002",
        "TagValue_Number": "2",
        "TagValue_Service Name": "Common Services",
        "TagNotes_Service Definition": "The services that provide a  function that is common across many mission capabilities.",
        "TagNotes_Service Description": "Layer",
        "TagValue_Example Specification": "Layer",
        "TagValue_Example Solution": "<memo>",
        "TagValue_DI2E Framework Status": "Layer",
        "TagValue_DCGS Enterprise Status": "Layer",
        "TagValue_JCA Alignment": "Layer",
        "TagNotes_JCSFL Alignment": "Layer",
        "TagValue_JARM/ESL Alignment": "Layer",
        "TagNotes_Comments": "Layer"
      },
      "size": 100,
      "children": [
        {
          "id": "21",
          "name": "2.1 - Collaboration",
          "data": {
            "TagValue_UID": "0010",
            "TagValue_Number": "2.1",
            "TagValue_Service Name": "Collaboration",
            "TagNotes_Service Definition": "Collaboration provides tools and services so people can easily share knowledge, status, thoughts, and related information artifacts.",
            "TagNotes_Service Description": "The Collaboration is the aggregation of infrastructure, services, people, procedures, and information to create and share data, information and knowledge used to plan, execute, and assess joint forces operations. (Ref. DCGS CONOPS v1.0 - 15 May 2007).  This line focuses on the tools and services that enables this creation and sharing of data, information, and knowledge.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "<memo>",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "211",
              "name": "2.1.1 - Information Boards",
              "data": {
                "TagValue_UID": "0011",
                "TagValue_Number": "2.1.1",
                "TagValue_Service Name": "Information Boards",
                "TagNotes_Service Definition": "The Information Board family of services provide a unified electronic platform that supports synchronous and asynchronous communication and information sharing through a variety of tools & services.",
                "TagNotes_Service Description": "Information Board services help users readily share thoughts, files, diagrams, and other content that helps users work together with enhanced understanding of each other's knowledge, needs, issues, and perspectives.   Specific services include:\n• Bulletin Board - which provides the ability to post messages and notices that interested communities can see, share, and respond to\n• Wiki - which provides a page or collection of web pages that enable anyone with access to contribute to or modify content.\n• Desktop Sharing - which provides the ability to share desktop applications with other users and groups.\n• Whiteboard - which provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.\n• Web Conferencing/VTC - which allows users in two or more locations to communicate using near real time full-duplex audio and video.\n• Instant Messaging - which allows users to communicate using text-based chat and instant messaging.\n• Audio Messaging - which allows users to send and receive sound file recordings. \n• E-mail - which creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers. \n• Group Calendar - which allows users to view, schedule, organize, and share calendar events with other DI2E users.\n• People Find - which provides the service to identify people with particular interests or skills in various intelligence topics for consultation and collaboration.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2111",
                  "name": "2.1.1.1 - Bulletin Board",
                  "data": {
                    "TagValue_UID": "0012",
                    "TagValue_Number": "2.1.1.1",
                    "TagValue_Service Name": "Bulletin Board",
                    "TagNotes_Service Definition": "Bulletin Board provides the ability to post messages and notices that interested communities can see, share, and respond to.",
                    "TagNotes_Service Description": "Bulletin Boards provide an internet based discussion forum where DI2E users can post messages that last for short to long periods of time.    Discussions are typically moderated and fully threaded (messages with replies and replies to replies) vs. non-threaded (messages with no replies) or semi-threaded (relies to a message, but not replies to a reply).\n\nGeneral users can see typically see threads, post new threads, respond to thread comments, and change certain preferences for their account such as their avatar (image representing the user), automatic signature, and ignore lists.   \n\nModerators can typically set, monitor, and change and privileges to groups or individual members; delete, merge, move, split, lock, or rename threads; remove unwanted content within a thread; provide access to unregistered guests; and control the size of posts.    \n\nAdministrators can typically promote and demote members, manage rules, create sections and sub-sections, and perform basic database operations.\nAdditional common features include the ability to subscribe for notification new content is added, view or vote in opinion polls, track post counts (how many posts a certain user has made), or attach symbols to convey emotional response.   More advanced bulletin boards might support Really Simple Syndication (RSS), XML and HTTP feeds (such as ATOM).",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1.22  Manage Message Boards",
                    "TagValue_JARM/ESL Alignment": "8.28.10 Event / News Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2112",
                  "name": "2.1.1.2 - Wiki",
                  "data": {
                    "TagValue_UID": "0013",
                    "TagValue_Number": "2.1.1.2",
                    "TagValue_Service Name": "Wiki",
                    "TagNotes_Service Definition": "The Wiki service provides a page or collection of web pages that enable anyone with access to contribute to or modify content.",
                    "TagNotes_Service Description": "Wiki applications allow web pages to be created and edited using a common web browser and typically run on a web server (or servers). The content is stored in a file system, and changes to the content are stored in a relational database management system.    The general maintenance paradigm is to allow alteration by general public without requiring registered user accounts and edits to appear almost instantly, but some wiki sites are private or password-protected.\n\nStyle of text presented typically includes plain text, HTML, Cascading Style Sheets (CSS) or other web based formats.",
                    "TagValue_Example Specification": "• JAMWiki,MediaWiki\n• FlexWiki\n• Roadkill\n• Foswiki\n• TWiki\n• DokuWiki\n• MediaWiki\n• Gitit\n• Swiki",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1.22  Manage Message Boards",
                    "TagValue_JARM/ESL Alignment": "8.28.11 Community Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "212",
              "name": "2.1.2 - Environment Sharing",
              "data": {
                "TagValue_UID": "0014",
                "TagValue_Number": "2.1.2",
                "TagValue_Service Name": "Environment Sharing",
                "TagNotes_Service Definition": "Environment Sharing capabilities provide environments to easily share work space environments in order to promote rapid, interactive collaboration.",
                "TagNotes_Service Description": "Environment Sharing capabilities provide environments that easily share work space environments in order to promote rapid, interactive collaboration.  Specific services include:\n\n  • Desktop Sharing - which provides the ability to share desktop applications with other users and groups.\n  • Whiteboard - which provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.\n  • Web Conferencing/VTC - which allows users in two or more locations to communicate using near real time full-duplex audio and video.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2121",
                  "name": "2.1.2.1 - Desktop Sharing",
                  "data": {
                    "TagValue_UID": "0015",
                    "TagValue_Number": "2.1.2.1",
                    "TagValue_Service Name": "Desktop Sharing",
                    "TagNotes_Service Definition": "Desktop Sharing provides the ability to share desktop applications with other users and groups.",
                    "TagNotes_Service Description": "Desktop sharing allows remote access, and thus remote real-time collaboration, with a computer’s desktop through a graphical terminal emulator.    Besides screen sharing, other common embedded features include instant messaging, file passing, and the ability to share control.  Some desktop sharing systems also permit video conferencing\n\nFile transfer systems that support the X Window System (typically Unix-based) have basic desktop sharing abilities already built in.     Microsoft Windows (Windows 2000 and later) offer basic remote access in the form of Remote Desktop Protocol.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1 Provide Ability to Communicate (Group)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2122",
                  "name": "2.1.2.2 - Whiteboard",
                  "data": {
                    "TagValue_UID": "0016",
                    "TagValue_Number": "2.1.2.2",
                    "TagValue_Service Name": "Whiteboard",
                    "TagNotes_Service Definition": "The Whiteboard service provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.",
                    "TagNotes_Service Description": "The Whiteboard service allows shared annotation of ‘open’ space or files (such as an image or map) among two users working at geographically separated workstations.   Each user can work see and mark the image at the same time, and each is able to see changes the others make in near-real time.\n\nIn this sense the white board is a virtual version of the physical whiteboards found in many professional office walls (some offer electronic features such as interactive display of a PC monitor or printing of the white board’s display, but these are not the type intended here).\n\nExamples of common features (not an exhaustive or mandatory list) include: multiple page support, rich text editing support, import of external images & graphics, export of whiteboard to multiple image formats, ability to save work for later session editing, drag/drop shape support, priority layering ('bring to front'), font adjustment, and automated fill in.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1.56 Provide Shared Audio Visualization Capabilities\n8.12.9 Provide Common Workspace",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2123",
                  "name": "2.1.2.3 - Web Conferencing/VTC",
                  "data": {
                    "TagValue_UID": "0017",
                    "TagValue_Number": "2.1.2.3",
                    "TagValue_Service Name": "Web Conferencing/VTC",
                    "TagNotes_Service Definition": "The Web Conferencing/VTC service allows users in two or more locations to communicate using near real time full-duplex audio and video.",
                    "TagNotes_Service Description": "The Web Conferencing/Video Telecommunications (VTC) service supports real time collaboration by offering full-duplex sharing of web, auto and video including internet telephone conferencing, videoconferencing, and web conferencing. \n\nTypical web conferencing features include sharing of documents, desktops, presentations, browsers, and other applications; remotely controlling presentation once given presenter rights; transferring files from within application; controlling layouts (including other users); and dual monitor support.  Advanced features might include ability to play and pause movie files, conference record and playback, integrated white boarding or text chat, and highlight or pointer tools.\n\nSome Video Teleconferencing features to look for include: higher definition support (preferably 720p or better @ 30 fps); support for various video peripherals at any end-point; on-the-fly video device switching; ability to play or pause each or every participant; customizable video size, resolution and frame rate for entire conference or any participant; show/hide participant's name; support for full-screen, tiled, floating, picture-in-picture (PIP), composite (one large, others tiled), and floating video bar display.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1.3  Employ Conference Communications Services\n7.1.44  Conduct Video Conferencing",
                    "TagValue_JARM/ESL Alignment": "8.28.06 Web Conferencing",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "213",
              "name": "2.1.3 - Collaborative Messaging",
              "data": {
                "TagValue_UID": "0018",
                "TagValue_Number": "2.1.3",
                "TagValue_Service Name": "Collaborative Messaging",
                "TagNotes_Service Definition": "Collaborative messaging capabilities facilitate the rapid creation and sharing of messages and, where appropriate, associated attached artifacts.",
                "TagNotes_Service Description": "Collaborative messaging services facilitate the rapid creation and sharing of messages and, where appropriate, associated attached artifacts.\n\nSpecific services include:  \n\n  • Instant Messaging - which allows users to communicate using text-based chat and instant messaging.\n  • Audio Messaging - which allows users to send and receive sound file recordings. \n  • E-mail - which creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2131",
                  "name": "2.1.3.1 - Instant Messaging",
                  "data": {
                    "TagValue_UID": "0019",
                    "TagValue_Number": "2.1.3.1",
                    "TagValue_Service Name": "Instant Messaging",
                    "TagNotes_Service Definition": "Instant Messaging allows users to communicate using text-based chat and instant messaging.",
                    "TagNotes_Service Description": "Instant messaging provides the ability to exchange short written text messages between and among fixed or mobile devices.   Messages can be sent one-to-one or one-to-many and might carry two-way conversational tones, short informational dissemination updates, or alerting notification.      \n\nCharacters that can be sent should include at least the upper and lower case 26 letters of the English alphabet and 10 numerals, but may include other special characters as well.   Security, confidentiality, reliability and speed are key criteria on concern in text messaging services.   Common mobile channel platforms include Short Message Services (SMS), Mobile Web, Mobile Client Applications, and SMS with Mobile Web and Secure SMS.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1 Provide Ability to Communicate (Group)\n8.4.1 Create and Edit Messages",
                    "TagValue_JARM/ESL Alignment": "8.28.07 Instant Messaging",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2132",
                  "name": "2.1.3.2 - Audio Messaging",
                  "data": {
                    "TagValue_UID": "0020",
                    "TagValue_Number": "2.1.3.2",
                    "TagValue_Service Name": "Audio Messaging",
                    "TagNotes_Service Definition": "Audio Messaging allows users to send and receive sound file recordings.",
                    "TagNotes_Service Description": "Audio Messaging allows transmittal of recorded voice messages & other audio content between and among fixed or mobile devices.    Messages can be sent one-to-one or one-to-many.   \n\nFeatures to look for in audio messaging services include the ability to: record messages from multiple input systems or devices (phone, PC microphone, …); forwarding messages among different audio messaging systems; indicate who messages are from, when received; auto-attendant, announcement broadcasting including scheduled or event-based group broadcasts; and recall for handling feedback and verifying messages.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1.51   Broadcast Information\n7.1.56  Provide Shared Audio Visualization Capabilities\n7.1.59  Translate Text to Speech",
                    "TagValue_JARM/ESL Alignment": "8.28.08 Audio Messaging",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2133",
                  "name": "2.1.3.3 - E-Mail",
                  "data": {
                    "TagValue_UID": "0021",
                    "TagValue_Number": "2.1.3.3",
                    "TagValue_Service Name": "E-Mail",
                    "TagNotes_Service Definition": "E-mail creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers.",
                    "TagNotes_Service Description": "The electronic mail (e-mail) service exchanges digital messages from an author to one or more recipients.   Architecturally, e-mail clients help author, send, receive, display, and manage messages.   E-mail servers accept, store, and forward messages from and to e-mail clients.   Structurally, an email message consists of three components, the message envelope, the message header (control information, including an originator's email address and recipient addresses), and the message body.\n\nBasic steps in the passing of a typical e-mail message include: \n• user entering a message & sending the message to a local mail submission agent (typically provided by internet service providers), \n• e-mail servers use the Simple Mail Transfer Protocol (SMTP) to read to resolve the domain name and determine the fully qualified domain name of the mail exchange server in the Domain Name System (DNS),\n• mail exchange records listing the mail exchange servers for that domain are returned,  \n• the message is sent (using SMTP). \n• The Message Delivery Agent (MDA) delivers the message to the recipient’s mailbox\n• The e-mail client is users to pick up the message using standardized access protocols",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1.24  Manage E-Mail\n8.5.33 Scan Mailbox",
                    "TagValue_JARM/ESL Alignment": "8.28.01 Email",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "214",
              "name": "2.1.4 - Social Networking",
              "data": {
                "TagValue_UID": "0022",
                "TagValue_Number": "2.1.4",
                "TagValue_Service Name": "Social Networking",
                "TagNotes_Service Definition": "Social Networking services help coordinate personal/group schedules, status, and events.",
                "TagNotes_Service Description": "Social Networking services help coordinate personal/group schedules, status, and events.\n\n  • Group Calendar - which views, schedules, organizes, and shares calendar events with other DI2E users.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2141",
                  "name": "2.1.4.1 - Shared Calendaring",
                  "data": {
                    "TagValue_UID": "0023",
                    "TagValue_Number": "2.1.4.1",
                    "TagValue_Service Name": "Shared Calendaring",
                    "TagNotes_Service Definition": "Shared Calendaring allows users to view, schedule, organize, and share calendar events with other DI2E users.",
                    "TagNotes_Service Description": "Shared Calendaring leverages standards that enable calendar information to be exchanged regardless of the application that is used to create or view the information. \n\nInteroperability features should include the ability to: send source calendar content via messages between differing calendar tracking applications (including the ability to customize the scope or type of calendar information passed); ability to move (drag) appointments from sent calendar system to users calendar; ability to review/find common free time among two or more calendars; see & (with permissions) edit group calendars; and host and subscribe to calendars (periodic synchronization of calendars).\n\nCommon client application features include the ability to: view the calendar in multiple forms and formats (daily/weekly/monthly, forward or backward through time, etc.); show appointments and events; create, edit, and update events; schedule recurring events; schedule meetings; schedule non-human resources; set work schedules & holidays; change presentation options such as font size, font styles, color themes, etc.; set and view importance markings (high importance vs. normal).",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "8.1.76 - Enable Calendar Scheduling",
                    "TagValue_JARM/ESL Alignment": "8.28.04 Shared Calendaring",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2142",
                  "name": "2.1.4.2 - Community of Interest Find",
                  "data": {
                    "TagValue_UID": "0328",
                    "TagValue_Number": "2.1.4.2",
                    "TagValue_Service Name": "Community of Interest Find",
                    "TagNotes_Service Definition": "Community of Interest Find provides the capability to identify people with particular interests in various intelligence topics for collaboration. It finds experts throughout the DI2E and broader community to better disseminate Intel and analyze problems.",
                    "TagNotes_Service Description": "The Community of Interest Find service allows users to search the enterprise for other users who share a specific interest or expertise.  This allows users to identify Subject Matter Experts (SMEs) who might be able to assist in solving an intelligence problem, or identify personnel who may be interested in a piece of intelligence that relates to a topic they are watching.  This search capability facilitates networking between analysts and promotes dynamic development of communities of interest within the enterprise.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.2 Collaboration",
                    "TagNotes_JCSFL Alignment": "7.1 Provide Ability to Communicate (Group)\n8.7.53 Access Subject Matter Expert and Essential Information\n8.7.56 Maintain Address Book\n8.7.58 Manage Communities of Interest (COI) Catalogs\n8.12.8 Determine Collaboration Resource Availability",
                    "TagValue_JARM/ESL Alignment": "8.26.03 Workforce Directory / Locator",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "22",
          "name": "2.2 - Visualization",
          "data": {
            "TagValue_UID": "0003",
            "TagValue_Number": "2.2",
            "TagValue_Service Name": "Visualization",
            "TagNotes_Service Definition": "The Visualization line of services identifies services that enable users to view and analyze data.",
            "TagNotes_Service Description": "Visualization Tools enable users to view and analyze data by providing 1) web visualization tools that view and traverse World Wide Web accessible content and applications and 2) geographic visualization tools that gather/displays inputs from multiple sources of geo-coordinated data and displays it in common geographical representations.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "<memo>",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "221",
              "name": "2.2.1 - Web Visualization",
              "data": {
                "TagValue_UID": "0004",
                "TagValue_Number": "2.2.1",
                "TagValue_Service Name": "Web Visualization",
                "TagNotes_Service Definition": "The Web Visualization family provides tools to view and traverse World Wide Web accessible content and applications.",
                "TagNotes_Service Description": "Web Visualization  uses web based technologies to provide user interfaces (UIs)  that can display web based content and web hosted applications.  Specific services include:\n• Web Browser -  which retrieves, presents, and traverses information resources identified by a Uniform Resource Locators (URLs) including web pages, images, videos, or other artifacts and content.\n• Widget Framework - which provides a toolkit that assists in combining two or more widgets (portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page).     Widget Frameworks displays an information arrangement, changeable by the user, to form a single application using much less demanding technical efforts than is typically required for application development.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2211",
                  "name": "2.2.1.1 - Web Browser",
                  "data": {
                    "TagValue_UID": "0005",
                    "TagValue_Number": "2.2.1.1",
                    "TagValue_Service Name": "Web Browser",
                    "TagNotes_Service Definition": "Web Browsers retrieves, presents, and traverses information resources identified by a Uniform Resource Locators (URLs) including web pages, images, videos, or other artifacts and content.   Includes the ability to traverse hyperlinks and display commonly used web content formats including HTML 4.0, current versions of JavaScript, common commercial image formats (.bmp, .gif, ...), NITF plugins, and PDF.",
                    "TagNotes_Service Description": "The web browser service is the primary UI mechanism for DI2E applications, supports OUSDI goals for a Web based SOA architecture, reduces the complexity and expenses of providing fat clients, and supports rapid deployment of services through widgets.\n\nWeb browsers do this accepting a Uniform Resource Locator (URL) using URI prefixes (http:, https:, ftp:, file:, etc.) to display web hosted content that includes images, audio, video, and XML files.   URI prefixes that the web browser cannot directly handle are processed by other applications (mailto: by default e-mail application, news: by user's default newsgroup reader, etc.).\n\nExpected features include supporting:  \n• popular web file and image formats \n• Widgets  \n• Operation on a variety of operating systems \n• Multiple open information resources \n• pop-up blockers \n• bookmarked web pages so a user can quickly return to them.\n• Back, forward, refresh, home, and stop buttons \n• Address and bars that report loading status or links under cursor hovering\n• page zooming\n• Search and find features within a web page.\n• Ability to delete the web cache, cookies, and browsing history. \n• Basic security features \n\nExamples of other popular features include (not a complete list): \n• e-mail support  \n• IRC chat client, Usenet news support, and Internet Relay Chat (IRC)\n• web feed aggregator\n• support for vertical text\n• support for image effects and page transitions not found in W3C CSS\n• Embedded OpenType (EOT) and OpenType fonts support\n• \"favorites icon\" and \"quick tabs\" features \n• ActiveX controls support \n• Performance Advisors\n• location-aware browsing and geolocation support  \n• thumbnail ‘speed dial’ to move to favorite pages. \n• plug-in support\n• Page zooming allows text, images and other content \n• keyboard control, voice control, and mouse gesture control\n• Mobile devices support \n• web feeds",
                    "TagValue_Example Specification": "• HTML\n• XHTML\n• CSS\n• DOM\n• ECMAScript\n• Others…",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.1.80 - Populate and Render Data Entry Form via Web Browser\n8.4.6  Support Web Browsing\n8.4.10 Generate Displays",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "ChatSurfer (geotagging), GeoSextant (Geotagger)"
                  },
                  "size": 100
                },
                {
                  "id": "2212",
                  "name": "2.2.1.2 - Widget Framework",
                  "data": {
                    "TagValue_UID": "0006",
                    "TagValue_Number": "2.2.1.2",
                    "TagValue_Service Name": "Widget Framework",
                    "TagNotes_Service Definition": "Widget Framework service provides a toolkit that assists in combining two or more widgets (portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page) to form a single application using a much lower level of technical skill and effort than is typically required for application development.",
                    "TagNotes_Service Description": "Widget framework applications provide a layout management and messaging mechanism within a web browser for widgets (widgets are portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page, technically contained in an iFrame).   In doing so, they enable rapid assembly and configuration of rich Web applications composed of multiple, special-purpose widgets.\n\nTypical widget frameworks support both varied (desktop) and fixed (tabbed, portal, accordion, ...) layouts that can be set by the application’s user.   Components might typically include a server to run supplied applications, HTML and JavaScript files that provide the user interface, preference holding and retrieval mechanisms, and various security mechanisms.   \n\nKey features to look for in a widget application include (not all may be found in all widget frameworks):\n• standardized widget event model(s)\n• standardized Common Data Model (CDM) to promote data sharing between widgets\n• Scalability features to optimize for performance \n• data sharing mechanism \n• data handling and management features \n• ability to extend basic services through modification of existing widgets, or the development of entirely new ones. \n• support for bundling and deployment of specific widget sets  \n• framework extensions for standardized data and eventing models \n• workflow features that promote workflow analysis and linkage with widget framework architecture. \n• pre-existing, pre-tested widgets that can ‘jump start’ framework development with assured right-from-the-start interoperability\n• clearly documented APIs",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "5",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "8.9.5 Integrate Enterprise Applications",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "Additional examples not yet in garage: JackBe Presto"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "222",
              "name": "2.2.2 - Geographic Visualization",
              "data": {
                "TagValue_UID": "0007",
                "TagValue_Number": "2.2.2",
                "TagValue_Service Name": "Geographic Visualization",
                "TagNotes_Service Definition": "The Geographic Visualization family gathers and displays inputs from multiple sources containing data linked to geo-coordinated data and displays it in a common geographical representation.",
                "TagNotes_Service Description": "The Geographic Visualization family gathers and displays inputs from multiple sources containing data linked to geo-coordinated data and displays it in a common geographical representation.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2221",
                  "name": "2.2.2.1 - Geographic Information Display",
                  "data": {
                    "TagValue_UID": "0008",
                    "TagValue_Number": "2.2.2.1",
                    "TagValue_Service Name": "Geographic Information Display",
                    "TagNotes_Service Definition": "Geographic Information Display provides end-users with visualization services for still images, satellite images, 2D, 3D, weather images, and video.",
                    "TagNotes_Service Description": "Geographic Information Display captures, stores, manipulates, analyzes, manages and presents all types of geographically referenced data (2D imagery, 3D imagery, video, satellite, weather, maps, human terrain assessment, human intelligence reports, available signal data, etc.) by merging of cartography, statistical analysis and database technology.\n\nCommon input sources include ortho-rectified imagery taken from both from satellite and aerial sources, maps or graphical products of various forms, and databases containing geospatially related elements.   Within the GIS display, information is typically located spatially (recording longitude, latitude, and elevation), but may also be recorded temporally or with other quantified reference systems such as film frame number, sensor identifier, highway mile marker, surveyor benchmark, building address, or street intersection.\n\nGIS display data represents real objects with digital representations including raster data (digital image represented by reducible and enlargeable grids) or vectors (which represent features as geometric shapes such as lines, polylines, or polygons).    Real objects are also typically divided into two abstractions: discrete objects (e.g., a house) and continuous fields (such as rainfall amount, or elevations). \n\nCommon GIS display operations include:\n• contrast enhancement and color rendering \n• geographic data conversion\n• map integration and image rectification \n• complex spatial modeling \n• geometric network representation\n• hydrological modeling\n• Digital Elevation Model (DEM) assessment\n• data extraction\n• Geostatistics that analyze point-patterns and provide predictions\n• Digital cartography\n• Topological relationships (adjacency, containment, proximity, ..)\n• Surface interpolation using various mathematical models \n• Geocoding (interpolating spatial locations from street addresses or any other spatially referenced data using reference themes).\n• Spatial Extract, Transform, Load (ETL) tools",
                    "TagValue_Example Specification": "2D Map API",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "3.2.52 Display Map\n3.4.3 Receive, Store and Maintain Geospatial Product Information\n8.4.10 Generate Displays",
                    "TagValue_JARM/ESL Alignment": "8.17.02 Imagery Visualization",
                    "TagNotes_Comments": "GIS really covers a lot of different things. For example,  WMS, WFS, KML, GML services all are part of the definition.  Note that GVS will provide the ESRI tool suite via their own licensing costs which are far lower than most other  agencies can get on their own.  This should be included.  Overall, this should probably be further decomposed."
                  },
                  "size": 100
                },
                {
                  "id": "2222",
                  "name": "2.2.2.2 - Web Coverage",
                  "data": {
                    "TagValue_UID": "0111",
                    "TagValue_Number": "2.2.2.2",
                    "TagValue_Service Name": "Web Coverage",
                    "TagNotes_Service Definition": "Web Coverage provides an interface allowing requests for geographical coverages (digital geospatial information representing space-varying phenomena) across the web using platform-independent calls.",
                    "TagNotes_Service Description": "Web Coverage supports electronic retrieval of geospatial data as \"coverages\" – that is, digital geospatial information representing space-varying phenomena. Provides access to potentially detailed and rich sets of geospatial information, in forms that are useful for client-side rendering, multi-valued coverages, and input into scientific models and other clients. Allows clients to choose portions of a server's information holdings based on spatial constraints and other criteria.\nProvides available data together with their detailed descriptions; defines a rich syntax for requests against these data; and returns data with its original semantics (instead of pictures) which may be interpreted, extrapolated, etc. – and not just portrayed. Returns coverages representing space-varying phenomena that relate a spatio-temporal domain to a (possibly multidimensional) range of properties.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.5.2 BA Data Access (IDD)",
                    "TagNotes_JCSFL Alignment": "8.10.10 Provide Graphical User Interface (GUI) Services\n8.10.22 Manage Display of Symbology\n8.10.23 Display Tabular Sortable Information\n8.10.25 Display Video",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "OGC Web Coverage Service (WCS) specification.  Note that this \"service\" is really an implementation of a spec, and there will be many of these implementations as the data behind it will be unique to the particular implementation."
                  },
                  "size": 100
                },
                {
                  "id": "2223",
                  "name": "2.2.2.3 - Web Feature",
                  "data": {
                    "TagValue_UID": "0112",
                    "TagValue_Number": "2.2.2.3",
                    "TagValue_Service Name": "Web Feature",
                    "TagNotes_Service Definition": "Web Feature provides an interface allowing requests for geographical features across the web using platform-independent calls.  This service allows a client to retrieve and update geospatial data encoded in Geography Markup Language (GML).",
                    "TagNotes_Service Description": "Web feature defines interfaces for data access and manipulation operations on geographic features using HTTP as the distributed computing platform. Via these interfaces, a web user or service can combine, use and manage geodata -- the feature information behind a map image -- from different sources by invoking the following WFS operations on geographic features and elements: create a new feature instance; delete a feature instance; update a feature instance; lock a feature instance; get or query features based on spatial and non-spatial constraints.\nWhen products are in compliance with open geospatial web service interface and data encoding specifications, end-users benefit from a larger pool of interoperable web based tools for geodata access and related geoprocessing services.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.5.2 BA Data Access (IDD)",
                    "TagNotes_JCSFL Alignment": "8.10.10 Provide Graphical User Interface (GUI) Services\n8.10.22 Manage Display of Symbology\n8.10.23 Display Tabular Sortable Information\n8.10.25 Display Video",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "OGC WFS Specification.  Note that this \"service\" is really an implementation of a spec, and there will be many of these implementations as the data behind it will be unique to the particular implementation"
                  },
                  "size": 100
                },
                {
                  "id": "2224",
                  "name": "2.2.2.4 - Web Map",
                  "data": {
                    "TagValue_UID": "0113",
                    "TagValue_Number": "2.2.2.4",
                    "TagValue_Service Name": "Web Map",
                    "TagNotes_Service Definition": "Web Map provides a simple HTTP interface for requesting geo-registered map images from one or more distributed geospatial databases. A WMS request defines the geographic layer(s) and area of interest to be processed. The response to the request is one or more geo-registered map images (returned as JPEG, PNG, etc.) that can be displayed in a browser application.",
                    "TagNotes_Service Description": "A Web Map Service (WMS) produces maps of spatially referenced data dynamically from geographic information. This International Standard defines a “map” to be a portrayal of geographic information as a digital image file suitable for display on a computer screen. A map is not the data itself. WMS-produced maps are generally rendered in a pictorial format such as PNG, GIF or JPEG, or occasionally as vector-based graphical elements in Scalable Vector Graphics (SVG) or Web Computer Graphics Metafile (WebCGM) formats.  \nThis International Standard defines three operations: one returns service-level metadata; another returns a map whose geographic and dimensional parameters are well-defined; and an optional third operation returns information about particular features shown on a map. Web Map Service operations can be invoked using a standard web browser by submitting requests in the form of Uniform Resource Locators (URLs). The content of such URLs depends on which operation is requested. In particular, when requesting a map the URL indicates what information is to be shown on the map, what portion of the Earth is to be mapped, the desired coordinate reference system, and the output image width and height. When two or more maps are produced with the same geographic parameters and output size, the results can be accurately overlaid to produce a composite map. The use of image formats that support transparent backgrounds (e.g. GIF or PNG) allows underlying maps to be visible. Furthermore, individual maps can be requested from different servers. The Web Map Service thus enables the creation of a network of distributed map servers from which clients can build customized maps.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.5.2 BA Data Access (IDD)",
                    "TagNotes_JCSFL Alignment": "3.2.52   Display Map",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "OGC WMS specification. Note that this \"service\" is really an implementation of a spec, and there will be many of these implementations as the data behind it will be unique to the particular implementation"
                  },
                  "size": 100
                },
                {
                  "id": "2225",
                  "name": "2.2.2.5 - Weather Visualization",
                  "data": {
                    "TagValue_UID": "0377",
                    "TagValue_Number": "2.2.2.5",
                    "TagValue_Service Name": "Weather Visualization",
                    "TagNotes_Service Definition": "Weather Visualization services receive and display weather related conditions.",
                    "TagNotes_Service Description": "Weather Visualization services receive and display weather related conditions from various weather reporting sources such as the National Weather Association (NWA), U.S. Air Force Weather Agency (AFWA), or Naval Meteorology & Oceanography Command (CNMOC) (list is for example, not comprehensive).   Weather visualization is typically for a specified geographic region; may include display of historic, current, or anticipated future weather conditions; and might be in a variety of formats including multiple spectral options of imagery, map based, audio, or text.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.5.2 BA Data Access (IDD)",
                    "TagNotes_JCSFL Alignment": "",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "223",
              "name": "2.2.3 - Analytics Visualization",
              "data": {
                "TagValue_UID": "0329",
                "TagValue_Number": "2.2.3",
                "TagValue_Service Name": "Analytics Visualization",
                "TagNotes_Service Definition": "Family to visualize results of Analytical Processes.",
                "TagNotes_Service Description": "family",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2231",
                  "name": "2.2.3.1 - Analytic Rendering",
                  "data": {
                    "TagValue_UID": "0330",
                    "TagValue_Number": "2.2.3.1",
                    "TagValue_Service Name": "Analytic Rendering",
                    "TagNotes_Service Definition": "Analytic Rendering services render analytic products.  Includes Histograms, Semantic Network diagrams, Scatter Diagrams, Flow Charts, Relationship Charts.",
                    "TagNotes_Service Description": "Analytic Rendering provides different ways to visually present data, to assist in the understanding and exploitation of that data and the identification of trends or other useful information.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.17.01 Graphics / Charting",
                    "TagNotes_Comments": "There exist a bunch of these widgets that are pre-packaged with the Synapse stuff.   Also, should probably call out the framework for these - such as AgileClient and OWF"
                  },
                  "size": 100
                },
                {
                  "id": "2232",
                  "name": "2.2.3.2 - Common Operational Picture (COP)",
                  "data": {
                    "TagValue_UID": "0009",
                    "TagValue_Number": "2.2.3.2",
                    "TagValue_Service Name": "Common Operational Picture (COP)",
                    "TagNotes_Service Definition": "Common Operational Picture (COP) services allow for generation of a COP and User Defined Operating Picture (UDOP).\n",
                    "TagNotes_Service Description": "The COP provides an overall 'picture' of a geographic domain of interest (typically a theatre engaged in military missions) and is maintained by the commander’s operations staff.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "5.2.3.2 Establish Collective Meaning (Collaboration)",
                    "TagNotes_JCSFL Alignment": "14.1.24 Provide Graphical Intelligence Preparation of the Operational Environment (IPOE) Products\n14.1.25 Provide Data Assets from Intelligence Preparation of the Operational Environment (IPOE) Products\n14.1.26 Conduct Joint Intelligence Preparation of the Operational Environment",
                    "TagValue_JARM/ESL Alignment": "8.16.08 Situational Analysis",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "23",
          "name": "2.3 - Data Discovery",
          "data": {
            "TagValue_UID": "0097",
            "TagValue_Number": "2.3",
            "TagValue_Service Name": "Data Discovery",
            "TagNotes_Service Definition": "The Data Discovery line processes a user's query to discover information.",
            "TagNotes_Service Description": "Data Discovery processes a user’s query to discover information by:  1) conducting searches that gather information from basic web content as well as structured data sources (general, federated, and semantic search); 2) enabling advanced manipulation and control of the search request and results (search management and enhancement); and 3) enabling enhanced understanding of the people and assets that are available to support  missions and analysis (resource discovery).",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "<memo>",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "231",
              "name": "2.3.1 - Content Discovery and Retrieval",
              "data": {
                "TagValue_UID": "0098",
                "TagValue_Number": "2.3.1",
                "TagValue_Service Name": "Content Discovery and Retrieval",
                "TagNotes_Service Definition": "The Content Discovery and Retrieval (CD&R) family processes a user's query to discover information from data assets.",
                "TagNotes_Service Description": "family",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2311",
                  "name": "2.3.1.1 - Content Search",
                  "data": {
                    "TagValue_UID": "0099",
                    "TagValue_Number": "2.3.1.1",
                    "TagValue_Service Name": "Content Search",
                    "TagNotes_Service Definition": "Content Search provides a standard interface for discovering information, returning a 'hit list' of items which can then be retrieved.\n\nFederated Search transforms a search query into useful form(s), broadcasts is to multiple disparate databases, merges the results, and presents results is a succinct, organized format.",
                    "TagNotes_Service Description": "Content Search provides a standard interface for discovering information in unstructured and semi-structured data stores.  This service searches Meta data tagged Imagery of various types and Free Text reports, articles, or documents.   Each item to be searched may have metadata associated with it, conforming to enterprise standards.  \n\nThe interface specifies criteria such as Author=”name” or “Published time later than some date” as ways of identifying items of interest.   Additionally the search of the body and/or meta data tags can be performed using text search strings, such as “(SOA OR Service) NEAR Component”.   The precise query language grammar will be specified in the service specification package and will include the following functionality: Boolean and, Boolean or, Boolean not, groupings, proximity, and wildcards.\n\nThe Federated Search service will simultaneously search multiple specific search resources through a single query request by distributing the search request to participating search engines.   Results are aggregated and results are processed for presentation to the user.\n\nOperations typically might include: (1) checking an incoming query for appropriate content and form, (2) analyzing which search resources to query for results, (3) transforming the query request to forms appropriate search requests for each search resource to be contacted, (4) executing a search request for each assigned search resource, (5) receiving and responding to the distributed search requests, (6) aggregate the results collected from the various search requests, and (7) preparing an organized representation of results, typically with reduced duplication and possibly some relevancy rankings.",
                    "TagValue_Example Specification": "•  IC/DoD SOAP Encoding Interface Specification for CDR Retrieve v1.1\n•  IC/DoD REST Interface Encoding Specification for CDR Retrieve v1.1",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "4",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "1.2.8 Search\n8.7.8  Query Data\n8.7.11  Formulate Discovery Search\n8.7.63  Search within Context\n8.7.65  Perform Text Searches\n8.7.95  Search Structured and Unstructured Data\n8.7.96 Review Information Search Results\n8.7.97  Report Query Results",
                    "TagValue_JARM/ESL Alignment": "8.29.06 Content Discovery",
                    "TagNotes_Comments": "Very many tools will map to this.  This is probably where future plugfests should focus - to drive the implementations of the CD&R specs to true interoperability.  Also, need to consider how the  various \"data cloud\" implementations across both IC and DoD fit into this.  These data clouds will have to be considered data stores themselves, and therefore available for federated queries, but also they will have their own internal queries that will be related to content search.  Associated with this will need to be the ability to de-duplicate data (since federating to already federated data sources will result in duplication of data) and to track data provenance.  This last part may involve the service registries and artifacts as well, as they can disclose what data sources a particular federated search is connected to."
                  },
                  "size": 100
                },
                {
                  "id": "2312",
                  "name": "2.3.1.2 - Brokered Search",
                  "data": {
                    "TagValue_UID": "0376",
                    "TagValue_Number": "2.3.1.2",
                    "TagValue_Service Name": "Brokered Search",
                    "TagNotes_Service Definition": "Brokered Search serves as the primary mechanism to 1) facilitate the distribution of queries to applicable/relevant content collections (exposed as Search Components) and 2) process the returned results.\n--CDR Reference Architecture\n",
                    "TagNotes_Service Description": "The Brokered Search service allows an entity to search multiple, independent resources or data repositories and retrieve a combined list of search results.  Brokered searches may be confined to the organization, or may be federated, allowing an entity to simultaneously search data stores in multiple organizations.  Rather than returning the identified products (which could number in the millions), a brokered search returns metadata about each matching product.  This metadata includes information to assist the entity in choosing products to retrieve  (such as the author, a description, abstract, or summary), as well as information about the resource that holds the product.  The entity then queries the repository directly to retrieve the actual product.\nhttp://www.dni.gov/index.php/about/organization/chief-information-officer/cdr-brokered-search.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "0",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2313",
                  "name": "2.3.1.3 - Retrieve Content",
                  "data": {
                    "TagValue_UID": "0108",
                    "TagValue_Number": "2.3.1.3",
                    "TagValue_Service Name": "Retrieve Content",
                    "TagNotes_Service Definition": "Retrieve Content serves as the primary content access mechanism. It encompasses the service to retrieve identified content and to initiate the delivery of the retrieved content to a designated location.",
                    "TagNotes_Service Description": "The Retrieve Content service serves as the primary content access mechanism. It encompasses the service to retrieve identified content from the content collection in which it is stored and to initiate the delivery of the retrieved content to a designated location. The delivery of the content can be a return directly back to the requester or can use the Deliver Content service to redirect the response and comply with other handling instructions as supplied by the requester. It cannot redirect output to a component other than the requestor.  \nThe Retrieve Content service provides a common interface and behavioral model for Intelligence Community (IC) and Department of Defense (DoD) content collections, enabling content consumers to retrieve content from disparate collections across the IC/DOD enterprise. Specifically, it provides a means to accept a uniform syntax and semantics that can be transformed, as needed, and applied to newly-developed or existing content collections. Thus, it is unambiguously conveying a request for the content without knowing or setting requirements on the implementation of the underlying content collection.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.27  Retrieve Data\n8.7.91 Acquire Data\n8.7.121 Receive Queried Information",
                    "TagValue_JARM/ESL Alignment": "8.29.01 Retrieval",
                    "TagNotes_Comments": "This is part of the CD&R specs."
                  },
                  "size": 100
                },
                {
                  "id": "2314",
                  "name": "2.3.1.4 - Deliver Content",
                  "data": {
                    "TagValue_UID": "0109",
                    "TagValue_Number": "2.3.1.4",
                    "TagValue_Service Name": "Deliver Content",
                    "TagNotes_Service Definition": "Deliver Content enables content to be sent to a specified destination, which may or may not be the requesting component.",
                    "TagNotes_Service Description": "Deliver Content enables content to be sent to a specified destination, which may or may not be the requesting component. In its simplest form, Deliver Content will take a consumer-supplied payload and send it to another consumer as specified in the delivery property set. For instance, if an analyst discovers a relevant data resource from a Data Discovery feed on her PDA, she might want to access and route that data content to her desktop computer so that she may review it later. The Retrieve Content service facilitates this use case through its use of WS-Addressing, but it requires a companion asynchronous callback interface to ultimately accept the routed data resource. This interface is captured by the Deliver Content  service. Also may include additional processing, such as compression, encryption, or conversion that makes delivery of the payload suitable for its destination and the delivery path to be used.  The terms Deliver and Receive are both used in the Content  Discovery and Retrieval Architecture to describe the service with this service.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "7.1.7  Transfer Data\n8.7.24 Manage Information Delivery\n8.7.100 Provide Information Delivery Vehicle",
                    "TagValue_JARM/ESL Alignment": "8.29.08 Delivery Management",
                    "TagNotes_Comments": "This is part of the CD&R specs.  Note the overlap in the description with Retrieve Content (sending to another destination)."
                  },
                  "size": 100
                },
                {
                  "id": "2315",
                  "name": "2.3.1.5 - Describe Content",
                  "data": {
                    "TagValue_UID": "0379",
                    "TagValue_Number": "2.3.1.5",
                    "TagValue_Service Name": "Describe Content",
                    "TagNotes_Service Definition": "The Describe Content service enables content repositories to publish information describing their content collections and content resources to the enterprise.  It also provides interested parties with a description of the resource and how it can be accessed or used. \n\n-- CDR Reference Architecture and Specification Framework\n\nhttps://metadata.ces.mil/dse/documents/DoDMWG/2010/04/2010-04-13_CDRIPT.ppt\n",
                    "TagNotes_Service Description": "Describe Content serves as the primary mechanism for content providers to expose information to describe the context, access constraints, and current inventory status of the underlying content resources, and the exposed information will support static and dynamic discovery and accessibility of a content collection. Search and Brokered Search leverage the output of this component to determine whether the content collection may contain content resources that are relevant to the consumer’s query. To support a wide array of use cases, the Describe Component should reflect both the static9 and dynamic10 information about the underlying content collection.\n--CDR Reference Architecture",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "0",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2316",
                  "name": "2.3.1.6 - Query Management",
                  "data": {
                    "TagValue_UID": "0102",
                    "TagValue_Number": "2.3.1.6",
                    "TagValue_Service Name": "Query Management",
                    "TagNotes_Service Definition": "Query Management allows consumers to build, store, retrieve, update, and delete pre-established search and query criteria.",
                    "TagNotes_Service Description": "Many  analysts periodically or repetitively execute the same, or  highly similar, search queries.   In more advanced applications, the structure of the queries can be complex and require considerable time and/or skill to establish.    The query management service helps support this operational requirement by providing a tool that enables  analysts to establish, reuse, and share queries for later or other analyst’s use.\n\nQuery Management services accomplish this by supporting the maintenance of query repositories that offer queries for reuse, often with minimal or no additional editing or query parameter analysis.    The SvcV-4 is not prescriptive as the organization relationships of  query repositories (by user, by organization, by role, etc.) but does point to the specifications listed in the Service Portfolio Management Tool (SPMT) for guidance (and once CDP approved, conformance requirement) in the applications or services that present query repositories.    In doing so, it encourages search criteria management services to operate in a manner that promotes establishment and provisioning of stored queries for sharing and use by disparate systems and users across the  Enterprise.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "1.4.4 Control Search Parameters\n8.7.8  Query Data",
                    "TagValue_JARM/ESL Alignment": "8.29.07 Query Management",
                    "TagNotes_Comments": "On the one hand, this seems like it would work in conjunction with workspaces, shared workspaces, event notifications/rules engines, etc.  On the other hand,  this might just be a federated query specific function.  If the latter, then we still need to work out a CONOPS for how federated queries will work with pub/sub and event notifications/rules, etc.  \n\nThis will require some architecture as well"
                  },
                  "size": 100
                },
                {
                  "id": "2317",
                  "name": "2.3.1.7 - Query Results Management",
                  "data": {
                    "TagValue_UID": "0103",
                    "TagValue_Number": "2.3.1.7",
                    "TagValue_Service Name": "Query Results Management",
                    "TagNotes_Service Definition": "Query Results Management allows editing (adding, removing, modification) of query results as well as sharing and distributing of query results among  users",
                    "TagNotes_Service Description": "Query Results Management enables further processing of returned results to customize for further analysis or sharing of search results with others.\n\nExamples of what Query Results Management might providing include enabling adding a new record manually from an artifact the search did not find, editing the description of the search hit to make it more clear for others, or removing less relevant hits in order to provide focus to those hits that remain. \n\nIn some advanced applications, query results management might also enable sending out final edited results, automatically or at the user’s request, to other identified users or applications.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7.96  Review Information Search Results",
                    "TagValue_JARM/ESL Alignment": "8.29.05 Aggregated Search Results",
                    "TagNotes_Comments": "Is Query Results Management duplicative with other Content Management services?  (to be considered after specification review). - TEM with DMO, 13 October, 2011\n\nThis sounds like workspaces combined with a results factoring/facets and pub/sub (or email), etc.  Also, recommend changing description to not be limited to DCGS users."
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "24",
          "name": "2.4 - Data Mediation",
          "data": {
            "TagValue_UID": "0114",
            "TagValue_Number": "2.4",
            "TagValue_Service Name": "Data Mediation",
            "TagNotes_Service Definition": "Data Mediation includes services that enable the dynamic resolution of representational differences among disparate data on the behalf of a service consumer.",
            "TagNotes_Service Description": "Data Mediation services enable the dynamic resolution of representational differences among disparate data on the behalf of a service consumer. There will be multiple data formats within the enterprise representing all types of data. In order to support multiple data consumers, the data must be mediated into the format and schema recognizable by the data consumer.  Schema is used here to mean any way in which data might be organized, which may or may not be xsd or the schema of a relational database.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "<memo>",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "241",
              "name": "2.4.1 - Data Preparation",
              "data": {
                "TagValue_UID": "0115",
                "TagValue_Number": "2.4.1",
                "TagValue_Service Name": "Data Preparation",
                "TagNotes_Service Definition": "Data Preparation includes services that confirm and applicably convert data.",
                "TagNotes_Service Description": "Data Preparation services verify that incoming data records follow specified formats and schema and, when needed, output these records using specified formats and schema for usable ingest by consumer services.  \n\nServices in this family are often used in combination since format/schema errors found during validation often necessitate subsequent transformation.    \n\n(note: schema is used here to mean however the data might be organized - whether through xsd specifications, relational database structure, or other means.)",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2411",
                  "name": "2.4.1.1 - Schema Validation",
                  "data": {
                    "TagValue_UID": "0116",
                    "TagValue_Number": "2.4.1.1",
                    "TagValue_Service Name": "Schema Validation",
                    "TagNotes_Service Definition": "Schema Validation validates whether or not data adheres to an identified schema.",
                    "TagNotes_Service Description": "Schema Validation validates that a collection of data (e.g. USMFT message, XML document, etc.) conforms to specified schema. Schema is used here to mean any way in which data might be organized, which may or may not be xsd or the schema of a relational database.  This service validates whether or not data adheres to an identified schema.  This service will take a schema plus a collection of data or instance document and list any errors found in validating the document against the defined schema.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.1 Information Sharing",
                    "TagNotes_JCSFL Alignment": "8.7.19 Categorize Data",
                    "TagValue_JARM/ESL Alignment": "5.01.03 Mediation Services",
                    "TagNotes_Comments": "Need to re-consider the definition.   For example, 1.2.7.2 (Security Label Format Validation) would fit this description.  I think that schema validation tools would be a good item for here, but not the actual schema validation itself.   For example, validating a .csv file would probably take a custom piece of code.  On the other hand, Schematron and XML Schema validation tools can work with different unique schemas, but the tools themselves would be re-usable.  Also worth listing the different types of schema languages for XML (e.g., .xsd, RelaxNG, Schematron), OWL, RDF, and JSON along with their associated validation tools.  Note that the tools will come in the form of on-line tools, downloadable tools, and software libraries that you leverage."
                  },
                  "size": 100
                },
                {
                  "id": "2412",
                  "name": "2.4.1.2 - Data Validation",
                  "data": {
                    "TagValue_UID": "0117",
                    "TagValue_Number": "2.4.1.2",
                    "TagValue_Service Name": "Data Validation",
                    "TagNotes_Service Definition": "Data Validation validates that data values are correct based on rules defined for the data. For example, may validate Latitude as conforming to a DDDMMSS[N/S] format.",
                    "TagNotes_Service Description": "Data Validation uses routines, often called \"validation rules\" or \"check routines\", that check for correctness, meaningfulness, and security of data that are input to the system. The rules may be implemented through the automated facilities of a data dictionary, or by the inclusion of explicit application program validation logic. For business applications, data validation can be defined through declarative data integrity rules, or procedure-based business rules. Data that does not conform to these rules will negatively affect business process execution. Therefore, data validation should start with business process definition and set of business rules within this process. Rules can be collected through the requirements capture exercise. The simplest data validation verifies that the characters provided come from a valid set. For example, that data stored as YYYYMMDDHHMM have exactly 12 digits and  the MM is between 01 and 12 inclusive. A more sophisticated data validation routine would check to see the user had entered a valid country code, i.e., that the number of digits entered matched the convention for the country or area specified. Incorrect data validation can lead to data corruption or a security vulnerability. Data validation checks that data are valid, sensible, reasonable, and secure before they are processed.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.1 Information Sharing",
                    "TagNotes_JCSFL Alignment": "8.7.7  Maintain Data Integrity\n8.7.34  Validate Data\n8.7.44 Mediate Data\n8.7.76 Provide Data Mediation\n8.7.98  Perform Data Check",
                    "TagValue_JARM/ESL Alignment": "5.01.03 Mediation Services",
                    "TagNotes_Comments": "This generally comes with the above.  The schemas, in general, define not only what the allowable elements and attributes are of a given document, but what the allowable values for those elements and attributes are.  Recommend combining with the above"
                  },
                  "size": 100
                },
                {
                  "id": "2413",
                  "name": "2.4.1.3 - Data Transformation",
                  "data": {
                    "TagValue_UID": "0120",
                    "TagValue_Number": "2.4.1.3",
                    "TagValue_Service Name": "Data Transformation",
                    "TagNotes_Service Definition": "Data Transformation converts a data value form one format to another. For example convert Latitude from alphanumeric DDDMMSS[N/S] to numeric [+/-] seconds from equator, or feet to meters.",
                    "TagNotes_Service Description": "Data Transformation converts a data value form one format to another. For example convert Latitude from alphanumeric DDDMMSS[N/S] to numeric [+/-] seconds from equator, or feet to meters.  While the Data Validation service checks for correctness against a defined set of rules, it is the Data Transformation service that will change data values based on a set of defined rules.  Together, Data Validation and Data Transformation complete the process of ensuring that a service operates on clean, correct and useful data.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.1 Information Sharing",
                    "TagNotes_JCSFL Alignment": "8.7.15  Transform Data\n8.7.40 Format Data\n8.7.54 Convert Analog Data to Digital Data\n8.7.104  Convert Data File Format",
                    "TagValue_JARM/ESL Alignment": "6.01.03 Data Transformation Services",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2414",
                  "name": "2.4.1.4 - Schema Transformation",
                  "data": {
                    "TagValue_UID": "0121",
                    "TagValue_Number": "2.4.1.4",
                    "TagValue_Service Name": "Schema Transformation",
                    "TagNotes_Service Definition": "Schema Transformation transforms data from adhering to one schema to adhering to another schema.",
                    "TagNotes_Service Description": "Schema Transformation converts data organized in an original schema to a target schema.  The term schema is used here to mean any way in which data might be organized, this may or may not be xsd or the schema of a relational database.  This service transforms data from adhering to one schema to adhering to another schema.  For example, converts from ADatP-3 to USMTF or convert XML data between different XML schemas.  Together, Format Validation and Format Transformation complete the process of ensuring that a service is provided data organized according to the needs of that service.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.1 Information Sharing",
                    "TagNotes_JCSFL Alignment": "8.7.5 Translate Data\n8.7.16 Migrate Data\n8.7.40  Format Data",
                    "TagValue_JARM/ESL Alignment": "5.01.03 Mediation Services",
                    "TagNotes_Comments": "I'm thinking that schema translation should  really be combined with Data Transformation.  For example, XML schemas are used in XSLT to translate both the data and the elements to which the data is mapped.  Generally, included in ESB products"
                  },
                  "size": 100
                },
                {
                  "id": "2415",
                  "name": "2.4.1.5 - Image Transformation",
                  "data": {
                    "TagValue_UID": "0119",
                    "TagValue_Number": "2.4.1.5",
                    "TagValue_Service Name": "Image Transformation",
                    "TagNotes_Service Definition": "Image Transformation converts one image format to another.",
                    "TagNotes_Service Description": "The Image Format Conversion service converts one image format to another, typically in order to ensure that image data is in a format that can be ingested by specific applications or services.  There are many hundreds of different image formats used. Some of these are in broad commercial or government use. Others may be restricted to highly specialized purposes.  \n\nIncluded in the list of data products that may require image format conversion are many types of products that include 2-dimensional data array structures as part of their formats. Examples showing the diversity of image products are  raster formatted Geographic Information System (GIS) products, multiband images such as hyperspectral or multispectral imagery, digital handheld camera photos, scanned documents, and faxes.  In addition to the diversity of products and associated applications, image formats also often include image compression encodings that result in  specified degrees of reduction in image storage size.  Image encryption is also sometimes part of an image format.\n\nAn image format conversion service ingests an image in one format and outputs the image in a second format.  Besides the image input, additional inputs include the specification of the input format (with the default being that the input format is autodetected by the service) and the target destination image format.   \n\nThis service differs from Schema Transformation (in Data Mediation Line) in that Image Format conversion translate image file formats (example .jpeg formatted image to a .gif formatted image) whereas Schema Transformation converts data field layouts (schemas) from one data field arrangement to another (example: xsd schema to fields in a relational database).",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.3.1 Data Transformation (PE)",
                    "TagNotes_JCSFL Alignment": "14.1.9  Convert Image Format",
                    "TagValue_JARM/ESL Alignment": "5.01.03 Mediation Services",
                    "TagNotes_Comments": "Image transformation has also to do with orthorectification, 2D to 3D, rotation, scale, etc.   I think the title you are looking for that matches the definition is \"image format conversion\".  Plenty of on-line tools as well as downloadable tools to leverage - both piecemeal or batch."
                  },
                  "size": 100
                },
                {
                  "id": "2416",
                  "name": "2.4.1.6 - Data De-Duplication",
                  "data": {
                    "TagValue_UID": "0135",
                    "TagValue_Number": "2.4.1.6",
                    "TagValue_Service Name": "Data De-Duplication",
                    "TagNotes_Service Definition": "Data De-Duplication provides a data compression technique for eliminating coarse-grained redundant data.",
                    "TagNotes_Service Description": "Data De-Duplication provides a data compression technique for eliminating coarse-grained redundant data. In the de-duplication process, duplicate data is deleted, leaving only one copy of the data to be stored, along with references to the unique copy of data.  The focus of this service is on file or record level data and not on the data storage block level of data.  Although and data de-duplication method does save storage space, the emphasis here is providing analyses to ensure that a copy of an intelligence report, for instance, is the single authoritative copy in the system.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.17 Cleanse Data\n8.7.120 Deconflict Duplicate Data",
                    "TagValue_JARM/ESL Alignment": "5.01.03 Mediation Services",
                    "TagNotes_Comments": "Is this really for file or record compression, or for helping analysts on query results?  For the first case, there is plenty of open source software that exists, and they are generally used to improve storage efficiency (e.g., often used on backup).  If the latter, then it needs to be employed in \"data clouds\" and/or associated with processing federated query results.  Need clarity on the description of what you are looking for."
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "25",
          "name": "2.5 - Data Analytics",
          "data": {
            "TagValue_UID": "0152",
            "TagValue_Number": "2.5",
            "TagValue_Service Name": "Data Analytics",
            "TagNotes_Service Definition": "Data Analytics provide advanced analytics by finding non-intuitive or non-trivial relationships within and among DI2E data holdings.",
            "TagNotes_Service Description": "Data Analytics provide advanced analytics by finding non-intuitive or non-trivial relationships within and among DI2E data holdings.   In particular several Data Enrichment services providing entity extraction and association, content categorization, and monitoring of chat room content.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "<memo>",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "This is interesting, and needs some more thought.  Should probably have categories based on the kinds of data being analyzed.  For example, the tools used for processing signet data/textual data/imagery metadata will include NLP tools - whereas tools for imagery, or geospatial data analytics will likely be different (e.g., blast coverage, radio coverage, target recognition).  There are also data analytics \"engines\" - i.e., tools that run data analytics algorithms (e.g., HDFS/MapReduce) and tools for creating algorithms (e.g., OpenNLP, NLTK, SharpNLP, etc.).  This is really too wide a category, and I would recommend that it be further decomposed into tools/rules engines and rules/algorithms.  We could further categorize into types of data being analyzed, and perhaps types of analysis (e.g., NLP, Imagery, Numbers, etc.)"
          },
          "size": 100,
          "children": [
            {
              "id": "251",
              "name": "2.5.1 - Data Enrichment",
              "data": {
                "TagValue_UID": "0153",
                "TagValue_Number": "2.5.1",
                "TagValue_Service Name": "Data Enrichment",
                "TagNotes_Service Definition": "The Data Enrichment family of capabilities provide analysis and enhancement of sets of data elements.",
                "TagNotes_Service Description": "The Data Enrichment family of capabilities provide analysis and enhancement of sets of data elements by providing semantics and extracting non-obvious information from datasets.  Specific capabilities include:\n\n• Entity Extraction - which extracts specific data from text documents.  \n• Entity Association - which establishes and records relationships between data objects for use in advanced analytical processing and reporting.\n• Categorize Content - which analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy. \n• Chat Monitor - which provides alerts and/or query of multiple chat rooms through detection of key words or other user specified events.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "This sounds a lot like data analytics.  I wonder if we should come up with a \"data analytics\" process flow, and then look for tools that meet those processes (e.g., data cleansing, entity extraction, entity association, validation, etc.)."
              },
              "size": 100,
              "children": [
                {
                  "id": "2511",
                  "name": "2.5.1.1 - Entity Extraction",
                  "data": {
                    "TagValue_UID": "0154",
                    "TagValue_Number": "2.5.1.1",
                    "TagValue_Service Name": "Entity Extraction",
                    "TagNotes_Service Definition": "Entity Extraction extracts specific data from unstructured text.",
                    "TagNotes_Service Description": "Entity extraction scans text for semantic meaning and identifies key metadata and/or concepts in the document based on a semantic understanding of the language. It can identify things such as people, places, events, objects, etc.    It may also extract relationships such as “Muhammad Omani met has connections with suspected Taliban fighters”.    Information in the document may be tagged using standards such as Resource Description Framework (RDF) or Web Ontology Language (OWL).",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7.70 Re-Map Information",
                    "TagValue_JARM/ESL Alignment": "6.03.04 Data Mining Services; 8.18.04 Data Mining",
                    "TagNotes_Comments": "For automated asset ingest workflows, best when provided with COI ontology models or at least controlled vocabulary sets (dictionary, taxonomy, thesaurus). Take a look at USAF's AMPS (Auto Metadata Population Services). {Nguyen, Phuc N. Phuc.Nguyen@l-3com.com, 10 Aug, 11}.\n\nThis is, in general, associated with cloud based analytics"
                  },
                  "size": 100
                },
                {
                  "id": "2512",
                  "name": "2.5.1.2 - Entity Association",
                  "data": {
                    "TagValue_UID": "0155",
                    "TagValue_Number": "2.5.1.2",
                    "TagValue_Service Name": "Entity Association",
                    "TagNotes_Service Definition": "Entity Association establishes and records relationships between data objects for use in advanced analytical processing and reporting.",
                    "TagNotes_Service Description": "Entity Association discovers and records in persistent data stores various analytic relationships that might exist among disparate database objects.   These stored relationships can then be used by Analytic Processing services to more easily import, analyze, and report various associations, trends, groupings, or status that might not be found using more traditional Data Content Discovery services .       \n\nExamples of relationships might include (but not necessarily limited to) spatial, temporal, event based, or data content relationships.   Complex combinations of various relationships are also possible.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7.4 Analyze Data\n8.7.19 Categorize Data\n8.7.21 Correlate Data\n8.7.43 Mine Data\n8.7.47 Synchronize Data\n8.7.55 Establish Discovery Catalogs\n8.7.59 Manage Encyclopedic Database\n8.7.99 Produce Information Mapping and Taxonomy\n8.7.111 Associate Data Characteristics",
                    "TagValue_JARM/ESL Alignment": "6.03.04 Data Mining Services; 8.18.04 Data Mining",
                    "TagNotes_Comments": "Need  COI ontology models to represent key entities, workflows and business rules. Automated (or machine-assisted) entity extraction and link discovery need lots of intelligence wired into the data layer. Current state-of-the-art still requires human in most analytic workflows. Take a look at NSA's Blackbook. {{Nguyen, Phuc N. Phuc.Nguyen@l-3com.com, 10 Aug, 11}"
                  },
                  "size": 100
                },
                {
                  "id": "2513",
                  "name": "2.5.1.3 - Categorize Content",
                  "data": {
                    "TagValue_UID": "0156",
                    "TagValue_Number": "2.5.1.3",
                    "TagValue_Service Name": "Categorize Content",
                    "TagNotes_Service Definition": "Categorize Content analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy.",
                    "TagNotes_Service Description": "Categorize Content analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy. This is done to allow the user to browse for relevant content vs. key word search.\n\nFor example, a piece of content may be related to the USS Roosevelt. The if proper like categorized the user would be able to discover this content via traversing a taxonomy such as: Ships -> Military Ships -> US -> Air",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7.21  Correlate Data",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "Similar to 3.5.1.1, need COI taxonomies. {Nguyen, Phuc N. Phuc.Nguyen@l-3com.com, 10 Aug, 11}"
                  },
                  "size": 100
                },
                {
                  "id": "2514",
                  "name": "2.5.1.4 - Data Commenting",
                  "data": {
                    "TagValue_UID": "0243",
                    "TagValue_Number": "2.5.1.4",
                    "TagValue_Service Name": "Data Commenting",
                    "TagNotes_Service Definition": "Data Commenting services allow the users to enrich the existing data by adding comments on the data.",
                    "TagNotes_Service Description": "Data Commenting enables users to comment and collaborate on existing data within the system.  The comment service allows users to enrich the data set without altering the original product.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7.19 Categorize Data",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "252",
              "name": "2.5.2 - Media Monitoring",
              "data": {
                "TagValue_UID": "0157",
                "TagValue_Number": "2.5.2",
                "TagValue_Service Name": "Media Monitoring",
                "TagNotes_Service Definition": "Media Monitoring services are services that can track or record data activity that may be of interest to a user or system.",
                "TagNotes_Service Description": "Media Monitoring services are services that can track or record data activity that may be of interest to a user or system. Monitoring services may look for levels of activity (e.g., data transfer rates), particular values (e.g., keywords), or other values of interest.\n\nAlerting services, automated maintenance services, and similar capabilities can be constructed on top of monitoring services to relieve end users of the task of manually tracking a multitude of data sources looking for items of interest. For example, an audio monitoring service might look at all audio feeds waiting for a audio signal that matches the audio signature of the name of a person of interest and pass that to an alerting service.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2521",
                  "name": "2.5.2.1 - Chat Monitor",
                  "data": {
                    "TagValue_UID": "0158",
                    "TagValue_Number": "2.5.2.1",
                    "TagValue_Service Name": "Chat Monitor",
                    "TagNotes_Service Definition": "Chat Monitor provides alerts and/or query of multiple chat rooms through detection of key words or other user specified events.",
                    "TagNotes_Service Description": "Chat Monitor provides real-time chat monitoring along with chat log processing to enable additional awareness of current and historic chat content.\n\nCorresponding alerts and filtering may be based on key words, pattern matching, thesaurus, or gazetteer information.   A direct querying functionality is also provided.",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "TBD",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2522",
                  "name": "2.5.2.2 - Video Monitor",
                  "data": {
                    "TagValue_UID": "0159",
                    "TagValue_Number": "2.5.2.2",
                    "TagValue_Service Name": "Video Monitor",
                    "TagNotes_Service Definition": "Video Monitoring enables tracking, recording, scanning, filtering, blocking, reporting, and logging of specific information in video feeds",
                    "TagNotes_Service Description": "TBD",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "TBD",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2523",
                  "name": "2.5.2.3 - Audio Monitor",
                  "data": {
                    "TagValue_UID": "0160",
                    "TagValue_Number": "2.5.2.3",
                    "TagValue_Service Name": "Audio Monitor",
                    "TagNotes_Service Definition": "Audio Monitoring enables tracking, recording, scanning, filtering, blocking, reporting, and logging of specific information in audio feeds.",
                    "TagNotes_Service Description": "TBD",
                    "TagValue_Example Specification": "none",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "TBD",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "26",
          "name": "2.6 - Data Handling",
          "data": {
            "TagValue_UID": "0122",
            "TagValue_Number": "2.6",
            "TagValue_Service Name": "Data Handling",
            "TagNotes_Service Definition": "The Data Handling line of capabilities includes the data management and processing functions used to maintain and manage DI2E data stores.",
            "TagNotes_Service Description": "The Data Handling line of capabilities includes the data management and processing functions used to maintain and manage DI2E data stores.  Included families of capabilities provide content management, data quality control, and records management.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "<memo>",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "From definitions below, this is not \"data handling\" bot \"document handling\".  This is an important distinction, as data that is not in document format will need to be handled differently than data in document format"
          },
          "size": 100,
          "children": [
            {
              "id": "261",
              "name": "2.6.1 - Content Management",
              "data": {
                "TagValue_UID": "0123",
                "TagValue_Number": "2.6.1",
                "TagValue_Service Name": "Content Management",
                "TagNotes_Service Definition": "Content Management provides services that organize and facilitate the  collaboration and publication of documents and other enterprise content.",
                "TagNotes_Service Description": "Content Management services organize and facilitate the  collaboration and publication of documents and other enterprise content.\n\nSpecific services include: \n\n    • Content Repository - which is used to discover information about repositories and the object-types defined for the repository\n    • Content Navigation  - which traverses repository folder hierarchies and locates checked out documents\n    • Object Processing  - which provides ID-based Create, Retrieve, Update, Delete (CRUD), operations on repository objects \n    • Object Folders  - which files and un-files folder objects \n    • Managed Content Discovery  - which executes a query statement against the content repository\n    • Content Versioning   - which navigates or updates a document version series\n    • Object Relationship   - which retrieves the dependent relationship objects associated with an independent object\n    • Content Policy  - which applies or removes policy objects \n    • Content Access Control List This service processes the access control list (ACL) associated with repository objects",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "This should be \"document management\"  Note that the CMIS is for \"controlling diverse document management systems and repositories using web protocols\".  I.e., it is document focused vice data focused.  I would recommend combining all of the below into a document management service and list the sub-services as functionality that such a service should provide - but don't call them all out separately as their own 4th level item."
              },
              "size": 100,
              "children": [
                {
                  "id": "2611",
                  "name": "2.6.1.1 - Content Repository",
                  "data": {
                    "TagValue_UID": "0124",
                    "TagValue_Number": "2.6.1.1",
                    "TagValue_Service Name": "Content Repository",
                    "TagNotes_Service Definition": "Content Repository is used to discover information about repositories and the object-types defined for the repository.",
                    "TagNotes_Service Description": "The Content Repositories service has operations that return a list of existing content repositories available from the Content Management service endpoint; information about the content repository and its Access Control information.  The Content Repositories service also provides operations that return the definition of a specified content object type as well as list of content object types that are children or decedents of a specified content object type.",
                    "TagValue_Example Specification": "OASIS Content Management Interoperability Services (CMIS) Version 1.0",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.1.31 Maintain Global Directory Service\n8.7.19 Categorize Data",
                    "TagValue_JARM/ESL Alignment": "8.10 Content Management",
                    "TagNotes_Comments": "Isn't this more like \"content repository discovery\" or \"content repository registry\"?  Actually, it may need to be \"document repository discovery/registry\""
                  },
                  "size": 100
                },
                {
                  "id": "2612",
                  "name": "2.6.1.2 - Content Navigation",
                  "data": {
                    "TagValue_UID": "0125",
                    "TagValue_Number": "2.6.1.2",
                    "TagValue_Service Name": "Content Navigation",
                    "TagNotes_Service Definition": "Content Navigation is used to traverse the folder hierarchy in a repository, and to locate Documents that are checked out",
                    "TagNotes_Service Description": "The Content Navigation service provides operations to retrieve objects from a tree of folder objects that have the following constraints:\n• Every folder object, except for one which is called the root folder, has one and only one parent folder. The Root Folder does not have a parent.\n• A folder object cannot have itself as one of its descendant objects.\n• A child object that is a folder object can itself be the parent object of other file-able objects such as other folders and documents.\n• The folder objects in a content repository form a strict hierarchy, with the Root Folder being the root of the hierarchy.\nThe Content Navigation service provides the retrieval of a list of child objects that are contained in a specified folder, a set of descendant objects contained in a specified folder or any of its child-folders.  In addition, the Content Navigation service provides the retrieval of the parent folder object for a specified folder object a list of documents that are checked out to which the user has access.",
                    "TagValue_Example Specification": "OASIS Content Management Interoperability Services (CMIS) Version 1.0",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.1.78 Query Global Directory Service",
                    "TagValue_JARM/ESL Alignment": "8.10 Content Management",
                    "TagNotes_Comments": "Isn't this really more like Document CM?  Does Wikipedia satisfy this?  What if the documents are not in folders (e.g., hypermedia)?  Recommend re-naming to \"folder navigation\" because that is more consistent with the definition."
                  },
                  "size": 100
                },
                {
                  "id": "2613",
                  "name": "2.6.1.3 - Object Processing",
                  "data": {
                    "TagValue_UID": "0126",
                    "TagValue_Number": "2.6.1.3",
                    "TagValue_Service Name": "Object Processing",
                    "TagNotes_Service Definition": "Object Processing provides ID-based Create, Retrieve, Update, Delete (CRUD), operations on objects in a repository.",
                    "TagNotes_Service Description": "The Object Processing service provides Create, Retrieve, Update, Delete (CRUD) manipulation on the objects in a content repository. Specifically, the Object Processing service allows for the creation of a document object of the specified type in the specified location within the tree of folders and creates relationship objects of the specified types that are used to specify how content objects can be related. A relationship object is an explicit, binary, directional, non-invasive, and typed relationship between a specified source object and a specified target object.  Information contained in the properties of the content objects can be created, retrieved and updated.  The content objects, such as documents, themselves can be  created, retrieved deleted also content objects can be moved from one folder to another.",
                    "TagValue_Example Specification": "OASIS Content Management Interoperability Services (CMIS) Version 1.0",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.14  Delete Data\n8.7.19  Categorize Data\n8.7.25  Register Data\n8.7.28  Provide Database Utilities\n8.7.36  Post or Publish Data\n8.7.39  Aggregate Data\n8.7.71  Store Semantic Information on Data\n8.7.66  Post Metadata to a Discovery Catalog\n8.7.67  Post Metadata to a Federated Directory\n8.7.68  Post Metadata to Metadata Registries\n8.7.89  Provide Discovery Metadata for Situational Awareness\n8.7.14  Delete Data",
                    "TagValue_JARM/ESL Alignment": "8.10 Content Management",
                    "TagNotes_Comments": "Note that RESTful does this via hyperlinks and content representation, and scales  better."
                  },
                  "size": 100
                },
                {
                  "id": "2614",
                  "name": "2.6.1.4 - Object Folders",
                  "data": {
                    "TagValue_UID": "0127",
                    "TagValue_Number": "2.6.1.4",
                    "TagValue_Service Name": "Object Folders",
                    "TagNotes_Service Definition": "The Object Folders service is used to file and un-file objects into or from folders",
                    "TagNotes_Service Description": "The Object Folders service supports the multi-filing and unfiling services. Multi-filing allows the same non folder content object to be to be filed in more than one folder.  The Object Folders service allows existing fileable non-folder objects to be added to a folder and allows existing fileable non-folder object to be removed from a folder.  This service is NOT used to create or delete content objects in the repository.  The content objects must have previously been created by the Object Processing service to be filed and a content object that is removed from a folder remains persistent in the content repository.",
                    "TagValue_Example Specification": "OASIS Content Management Interoperability Services (CMIS) Version 1.0",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7 Manage Data (Group)",
                    "TagValue_JARM/ESL Alignment": "8.10 Content Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2615",
                  "name": "2.6.1.5 - Managed Content Discovery",
                  "data": {
                    "TagValue_UID": "0128",
                    "TagValue_Number": "2.6.1.5",
                    "TagValue_Service Name": "Managed Content Discovery",
                    "TagNotes_Service Definition": "Managed Content Discovery executes a query statement against the contents of a content repository",
                    "TagNotes_Service Description": "The Managed Content Discovery service (query) is used to search for query-able objects within the content repository.  The Managed Content Discovery service provides a type-based query service for discovering objects that match specified criteria,  The semantics of this query language is defined by applicable query language standards in conjunction with the model mapping defined by the relational view of the content management object repository.  This service also provides and operation to get a list of content changes to be used by search crawlers or other applications that need to efficiently understand what has changed in the repository.",
                    "TagValue_Example Specification": "OASIS Content Management Interoperability Services (CMIS) Version 1.0",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7 Manage Data (Group)",
                    "TagValue_JARM/ESL Alignment": "8.10 Content Management",
                    "TagNotes_Comments": "If this is for queries against a content repository, then CDW fits.  If it is a query against a document  repository, then CDW might fit.  Better to call this Managed Document Discovery, perhaps.  In any case, it seems better to provide crawler support and then the index that is the results of the crawling would be searchable via CDW."
                  },
                  "size": 100
                },
                {
                  "id": "2616",
                  "name": "2.6.1.6 - Content Versioning",
                  "data": {
                    "TagValue_UID": "0129",
                    "TagValue_Number": "2.6.1.6",
                    "TagValue_Service Name": "Content Versioning",
                    "TagNotes_Service Definition": "Content Versioning is used to navigate or update a document version series",
                    "TagNotes_Service Description": "The Content Versioning service is used to navigate or update a document version series. The Content Versioning service provides a check-out operation that gets a the latest document object in the version series, allows a private working copy of the document to be created and provides a  checks-in operation that checks-in a private working copy document as the latest in the version series. In addition, the Content Versioning service provides an operation that reverses the effect of a check-out by removing the private working copy of the checked-out document, allowing other documents in the version series to be checked out again.  Finally there is a service to retrieve a the list of all Document Objects in the specified Version Series.",
                    "TagValue_Example Specification": "OASIS Content Management Interoperability Services (CMIS) Version 1.0",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7 Manage Data (Group)",
                    "TagValue_JARM/ESL Alignment": "8.10 Content Management",
                    "TagNotes_Comments": "Why isn't this just Document CM?"
                  },
                  "size": 100
                },
                {
                  "id": "2617",
                  "name": "2.6.1.7 - Object Relationship",
                  "data": {
                    "TagValue_UID": "0130",
                    "TagValue_Number": "2.6.1.7",
                    "TagValue_Service Name": "Object Relationship",
                    "TagNotes_Service Definition": "Object Relationship is used to retrieve the dependent relationship objects associated with an independent object",
                    "TagNotes_Service Description": "The Object Relationship service is used to retrieve the dependent relationship objects associated with an independent object that were created with the Object Processing service.  The service gets all or a subset of relationships associated with an independent object.  In addition the service allows the consumer to specify whether the service returns relationships where the specified content object is the source of the relationship, the target of the relationship, or both.",
                    "TagValue_Example Specification": "OASIS Content Management Interoperability Services (CMIS) Version 1.0",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7 Manage Data (Group)",
                    "TagValue_JARM/ESL Alignment": "8.10 Content Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2618",
                  "name": "2.6.1.8 - Content Policy",
                  "data": {
                    "TagValue_UID": "0131",
                    "TagValue_Number": "2.6.1.8",
                    "TagValue_Service Name": "Content Policy",
                    "TagNotes_Service Definition": "Content Policy is used to apply or remove a policy object to an object that is under the control of a policy",
                    "TagNotes_Service Description": "The Content Policy service is used to apply or remove a policy object to a content object. A policy object represents an administrative policy that can be enforced by a repository, such as a retention management policy. Each policy object holds the text of an administrative policy as a repository-specific string, which may be used to support policies of various kinds. A repository may create subtypes of this base type to support different kinds of administrative policies more specifically.  Policy objects are created by the Object Processing service and manipulated by this Content Policy service.  The service has operations to apply a specified policy to an content object, get the list of policies currently applied to a specified content object and remove a specified policy from a specified content object.",
                    "TagValue_Example Specification": "OASIS Content Management Interoperability Services (CMIS) Version 1.0",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7 Manage Data (Group)",
                    "TagValue_JARM/ESL Alignment": "8.10 Content Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "262",
              "name": "2.6.2 - Database Management",
              "data": {
                "TagValue_UID": "385",
                "TagValue_Number": "2.6.2",
                "TagValue_Service Name": "Database Management",
                "TagNotes_Service Definition": "The Database Management Family provides services to host database repositories and  interact with users, other applications, and the database itself to capture and analyze data.",
                "TagNotes_Service Description": "Database Management includes capabilities to perform Create, Read, Update, and Delete (CRUD) operations on content, discover database content, apply security labels and tags to data objects, define database structures, and perform database administration functions.\n\nNote: Data Object Security Labeling is included in the Security Metadata Management component.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "2",
                "TagValue_DCGS Enterprise Status": "1",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "2621",
                  "name": "2.6.2.1 - Database Describe",
                  "data": {
                    "TagValue_UID": "386",
                    "TagValue_Number": "2.6.2.1",
                    "TagValue_Service Name": "Database Describe",
                    "TagNotes_Service Definition": "Database Describe enables the discovery of information about the database and the data object types stored in the database.",
                    "TagNotes_Service Description": "Database Describe includes describe-like capabilities to list tables, list table fields, etc.  This service feeds information to the CD&R Describe Content Service.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.21 Data Management; 5.02.01 Database Management Services",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2622",
                  "name": "2.6.2.2 - Data Object Processing",
                  "data": {
                    "TagValue_UID": "387",
                    "TagValue_Number": "2.6.2.2",
                    "TagValue_Service Name": "Data Object Processing",
                    "TagNotes_Service Definition": "Data Object Processing provides Create, Read, Update, and Delete (CRUD) operations on data objects stored in a database repository.",
                    "TagNotes_Service Description": "Data Object Processing includes capabilities to ingest data, commit transactions, rollback transactions, etc.\n\nThis includes the Content Search service that exists in CD&R.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7.13 Compress Data\n8.7.14 Delete Data\n8.7.16 Migrate Data\n8.7.28 Provide Database Utilities\n8.7.81 Store Data\n8.7.82 Retain Data\n8.7.112 Modify Data\n8.7.115 Import Data\n8.7.116 Export Data \n8.7.117 Retrieve Recorded Data",
                    "TagValue_JARM/ESL Alignment": "8.21 Data Management; 5.02.01 Database Management Services",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2623",
                  "name": "2.6.2.3 - Database Definition",
                  "data": {
                    "TagValue_UID": "388",
                    "TagValue_Number": "2.6.2.3",
                    "TagValue_Service Name": "Database Definition",
                    "TagNotes_Service Definition": "Database Definition enables the creation of the database and its structure, i.e., tables, indexes, triggers, etc., and the ability to modify the database structure.",
                    "TagNotes_Service Description": "Database Definition includes capabilities to create database, drop database, create tables, drop tables, alter tables, create indexes, drop indexes, etc.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.21 Data Management; 5.02.01 Database Management Services",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2624",
                  "name": "2.6.2.4 - Database Administration",
                  "data": {
                    "TagValue_UID": "389",
                    "TagValue_Number": "2.6.2.4",
                    "TagValue_Service Name": "Database Administration",
                    "TagNotes_Service Definition": "Database Administration enables the overall management of existing database repositories in a organization.",
                    "TagNotes_Service Description": "Database Administration includes capabilities to backup a database, recover a database, upgrade a database, restart a database, monitor database performance, tune database parameters, etc.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.21 Data Management; 5.02.01 Database Management Services",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2625",
                  "name": "2.6.2.5 - Data Object Tagging",
                  "data": {
                    "TagValue_UID": "390",
                    "TagValue_Number": "2.6.2.5",
                    "TagValue_Service Name": "Data Object Tagging",
                    "TagNotes_Service Definition": "Data Object Tagging tags data with various forms of content topic metadata for information discovery and automated dissemination purposes.",
                    "TagNotes_Service Description": "Data tagging allows users to organize information more efficiently by associating pieces of information with tags, or keywords.  A tag is a non-hierarchical keyword or term assigned to a piece of information such as an image, file or record. This kind of metadata helps describe an item and allows it to be found again by browsing or searching.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7.66 Post Metadata to a Discovery Catalog\n8.7.67 Post Metadata to a Federated Directory\n8.7.68 Post Metadata to Metadata Registries",
                    "TagValue_JARM/ESL Alignment": "8.21 Data Management; 5.02.01 Database Management Services",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "263",
              "name": "2.6.3 - Workspace Management",
              "data": {
                "TagValue_UID": "391",
                "TagValue_Number": "2.6.3",
                "TagValue_Service Name": "Workspace Management",
                "TagNotes_Service Definition": "Workspace Management services enable users to efficiently and locally establish, update, and share data in a persistent state between work sessions.",
                "TagNotes_Service Description": "Workspace Management services enable users to efficiently and locally establish, update, and share data in a persistent state between work sessions.     This lets users then ‘start where they left off’ between sessions, quickly view and analyze certain information conditions, or readily share their work session information with others.   Other benefits include:\n\n• Removing the need for developers to create their own persistence solutions for multiple applications\n• Common access to certain data layer content\n• Removes certain product specific dependences (i.e. Oracle Database)\n• Creates a ‘contract’ for sharing data between components\n• Enables customization of data structure based on user needs",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "2",
                "TagValue_DCGS Enterprise Status": "1",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "2631",
                  "name": "2.6.3.1 - Manage Workspace",
                  "data": {
                    "TagValue_UID": "393",
                    "TagValue_Number": "2.6.3.1",
                    "TagValue_Service Name": "Manage Workspace",
                    "TagNotes_Service Definition": "Manage Workspace services enable a user to add documents a new workspace session (Create), get documents (Read) documents in their workspace, edit (Update) the relationships among documents in their workspace, or remove (Delete) documents in their workspace.",
                    "TagNotes_Service Description": "Manage Workspace services enable a user to add documents a new workspace session (Create), get documents (Retrieve) documents in their workspace, edit (Update) the relationships among documents in their workspace, or remove (Delete) documents in their workspace.  The typical paradigm is to store documents in a hierarchical structure that contains ‘parents’ and ‘children’. Using the manage workspace service, users can then Create/Retrieve/Update/Delete (CRUD) documents using this structure in a persistent state for use between various work sessions.    \n\nTypical operations are broken down as follows:\nCreate - add documents to the workspace\nRetrieve – get a document, a document’s children, a documents parents, documents by type, or children documents by type\nUpdate – change the content of a document, or modify hierarchical relationships among documents\nDelete – remove individual documents, or entire ‘families’ of documents",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2632",
                  "name": "2.6.3.2 - Share Workspace",
                  "data": {
                    "TagValue_UID": "394",
                    "TagValue_Number": "2.6.3.2",
                    "TagValue_Service Name": "Share Workspace",
                    "TagNotes_Service Definition": "Share Workspace services enable a user to send or retrieve their workspace files to other workspace component users.",
                    "TagNotes_Service Description": "Share Workspace services enable a user to send or retrieve their current or saved workspace files to other workspace component users.   Workspace content can be content currently in use, or a saved session of workspace content from an earlier session.   \n\nThe method of identifying candidate workspace users or methods of transmission are not prescribed as part of this service definition, but an illustrative example would be a workspace management component that enabled the storing e-mail addresses as one family within the workspace documents, then the ability to select e-mail addresses from this list and then optionally send entire workspace, or families within the overall workspace, to the selected set of e-mail addresses.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "264",
              "name": "2.6.4 - Data Quality",
              "data": {
                "TagValue_UID": "0137",
                "TagValue_Number": "2.6.4",
                "TagValue_Service Name": "Data Quality",
                "TagNotes_Service Definition": "The Data Quality family of services provides methods and metrics by which the value of the data to the enterprise can be measured to ensure data is accurate, timely, relevant, complete, understood, trusted and satisfies intended use requirements.",
                "TagNotes_Service Description": "The Data Quality family of services provides methods and metrics by which the value of the data to the enterprise can be measured to ensure data is accurate, timely, relevant, complete, understood, trusted and satisfies intended use requirements.\n\n   • Data Quality Definition - which populates the business tables in the definitional data portion of the data quality metrics database\n   • Data Quality Extraction - which processes commands to retrieve specified portions of the data quality metrics database\n   • Data Quality Measurement  - which performs data quality measurement and assessment computations that populate the operational quality data portion of the data quality metrics database",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "The below 3 items are all part of the EDQMS.  Not sure whether it is worth pulling them out as separate items"
              },
              "size": 100,
              "children": [
                {
                  "id": "2641",
                  "name": "2.6.4.1 - Data Quality Definition",
                  "data": {
                    "TagValue_UID": "0138",
                    "TagValue_Number": "2.6.4.1",
                    "TagValue_Service Name": "Data Quality Definition",
                    "TagNotes_Service Definition": "Data Quality Definition populates the business tables in the definitional data portion of the data quality metrics database",
                    "TagNotes_Service Description": "The Data Quality Definition service is the primary mechanism whereby various entries in the different definitional tables provided by the user. This includes Information Product Types, Data Item Types, Data Object Types, Business Rules, Data Quality metrics, and Data Quality Object Metrics. The Data Quality Definition service provides the storage of the Data Quality metadata in a repository.",
                    "TagValue_Example Specification": "ISO 19100 International Standards",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.34 Validate Data",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2642",
                  "name": "2.6.4.2 - Data Quality Extraction",
                  "data": {
                    "TagValue_UID": "0139",
                    "TagValue_Number": "2.6.4.2",
                    "TagValue_Service Name": "Data Quality Extraction",
                    "TagNotes_Service Definition": "Data Quality Extraction processes commands to retrieve specified portions of the data quality metrics database",
                    "TagNotes_Service Description": "This service provides access the data quality metrics database to be used by applications the will process the data quality information for presentation and analysis on the quality of the data within the system.  The output of this service will be an XML file that follows the data quality XML Schema.",
                    "TagValue_Example Specification": "ISO 19100 International Standards",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.34 Validate Data",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2643",
                  "name": "2.6.4.3 - Data Quality Measurement",
                  "data": {
                    "TagValue_UID": "0140",
                    "TagValue_Number": "2.6.4.3",
                    "TagValue_Service Name": "Data Quality Measurement",
                    "TagNotes_Service Definition": "Data Quality Measurement performs data quality measurement and assessment computations that populate the operational quality data portion of the data quality metrics database",
                    "TagNotes_Service Description": "The Data Quality Measurement service provides summary counts of the number of business rule violations and calculates the levels of quality of different data quality dimensions.   This is accomplished by comparing Data Quality measurements to established Data Quality standard requirement or desired threshold level for the metric such as: Simple Ratio, Weighted Average, Minimum and Maximum operations. The results of these measurement comparisons are then processed through one or more assessment schemes to identify the level of acceptance (or not) of the quality of the data.",
                    "TagValue_Example Specification": "ISO 19100 International Standards",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.34 Validate Data",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "265",
              "name": "2.6.5 - Records Management",
              "data": {
                "TagValue_UID": "0141",
                "TagValue_Number": "2.6.5",
                "TagValue_Service Name": "Records Management",
                "TagNotes_Service Definition": "The Records Management family of capabilities provides systematic control of the creation, receipt, maintenance, use and disposition of records.",
                "TagNotes_Service Description": "The Records Management family of capabilities provides systematic control of the creation, receipt, maintenance, use and disposition of records, including the processes for capturing and maintaining evidence of and information about business activities and transactions in the form of records. \n\n   • Record Annotations - which provides the management of the markup and highlight instances that are associated with particular managed records\n   • Record Authorities - which provides the management of the Authorization instances on particular managed records\n   • Record Categories - which provides assignment of records to categories defined in a category schema.\n   • Record Dispositions- which provides management of information related to lifecycle of the managed records.\n   • Record Documents- which provides management of the documents which comprise the content of the managed records\n   • Managed Records - which provides management  of the information related to records that are being controlled by the records management system\n   • Record Query - which accepts a request and returns records identifiers that match the query.\n   • Record Authentications- which provides the management of authentication methods and the results of execution of those methods on managed records\n   • Record Attribute Profiles- which maintains the instance data for profiles that have been added to the records management services.\n   • Change Agent- which provides management of information about the individuals that update and modify managed records",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "2651",
                  "name": "2.6.5.1 - Record Annotations",
                  "data": {
                    "TagValue_UID": "0142",
                    "TagValue_Number": "2.6.5.1",
                    "TagValue_Service Name": "Record Annotations",
                    "TagNotes_Service Definition": "This service provides the management of the markup and highlight instances that are associated with particular managed records",
                    "TagNotes_Service Description": "The Record Annotations service provides markings on records that help to differentiate them from other records in the same category or across categories. These are typically used to support business needs for special handling or management of the record.  The Record Annotations service has operations that provide the ability to add an annotation to a managed record, retrieve a list of the annotations for a specified managed record and the ability to remove one or all annotations from a particular managed record.",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "26510",
                  "name": "2.6.5.10 - Change Agent",
                  "data": {
                    "TagValue_UID": "0151",
                    "TagValue_Number": "2.6.5.10",
                    "TagValue_Service Name": "Change Agent",
                    "TagNotes_Service Definition": "Change Agent provides for the management of information about the individuals that update and modify managed records.\n",
                    "TagNotes_Service Description": "The Change Agent service is responsible for managing the information regarding organizational structure used for the purpose of establishing provenance within the records management service by identify the parties that are involved with the maintenance and manipulation of the Managed Records and the other entities within the records management service.  The Change Agent service has operations that provide access to a hierarchy of change agents and the roles that have been assigned to them.\n",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "How is this different than ABAC with PDP/PEP?"
                  },
                  "size": 100
                },
                {
                  "id": "2652",
                  "name": "2.6.5.2 - Record Authorities",
                  "data": {
                    "TagValue_UID": "0143",
                    "TagValue_Number": "2.6.5.2",
                    "TagValue_Service Name": "Record Authorities",
                    "TagNotes_Service Definition": "Record Authorities provide the management of the Authorization instances on particular managed records.",
                    "TagNotes_Service Description": "The Record Authorities service to manage information about the organizations that have authority for managed records within records management system by maintaining an association between an Authority for a particular records management element and the Change Agent that has that responsibility.  The Record Authorities service manages information regarding the parties with legal authority over the managed records and their annotations using the Change Agent service to manage the data about the Authority. The record Categories service maintains a reference to information in the Authorities service related to the authority for a categorization schema.\n",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "How is this different than ABAC with PDP/PEP?"
                  },
                  "size": 100
                },
                {
                  "id": "2653",
                  "name": "2.6.5.3 - Record Categories",
                  "data": {
                    "TagValue_UID": "0144",
                    "TagValue_Number": "2.6.5.3",
                    "TagValue_Service Name": "Record Categories",
                    "TagNotes_Service Definition": "Record Categories provide the assignment of records to categories defined in a category schema.\n",
                    "TagNotes_Service Description": "The Record Categories service manages the association of the Managed Records with some business activity that requires that records of it be kept.  This is accomplished by associating Managed Records to business identified categories. The Record Categories service provides access to record schedules captured a Categorization Schema. The actual setup of the schemas is outside the scope of this service. The functionality provided herein allows for the use of the schema and the application of its categories to Managed Records.\n\nThe Record Categories service provides the ability to assign Managed Records to Record Categories either individually or as a set.  There are operations to add and remove record Categories, manage the association of Managed Records to Record Categories, return information on the Category Schemas and return the Disposition Instruction for specified Record Categories.",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2654",
                  "name": "2.6.5.4 - Record Dispositions",
                  "data": {
                    "TagValue_UID": "0145",
                    "TagValue_Number": "2.6.5.4",
                    "TagValue_Service Name": "Record Dispositions",
                    "TagNotes_Service Definition": "Record Dispositions provide for the management of information related to lifecycle of the managed records.\n",
                    "TagNotes_Service Description": "The Record Dispositions service includes management of a wide variety of information related to disposition instructions, dispositions plans, and suspensions. When a record is assigned to a record category, a disposition plan is generated based on the disposition instruction. If record category is changed, the disposition plan must be deleted unless the managed record category points to the same disposition instruction.\n\nThe Record Dispositions service has operations to add and remove disposition instructions to disposition plans, associate disposition plans to sets of managed records, return the managed records associated with a disposition plan, return a disposition plan for a specific managed record, and manage suspense events.\n",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2655",
                  "name": "2.6.5.5 - Record Documents",
                  "data": {
                    "TagValue_UID": "0146",
                    "TagValue_Number": "2.6.5.5",
                    "TagValue_Service Name": "Record Documents",
                    "TagNotes_Service Definition": "Record Documents provides the management of the documents which comprise the content of the managed records.",
                    "TagNotes_Service Description": "The Record Documents service manages the documents which comprise the content of Managed Records.  A Document managed by the records management family is interpreted simply as \"bit strings\" without presumption of form or purpose; in other words, the document contents per se are of no concern to the records management system, although a document type is maintained for use by the consumers of the record management system. Documents can be used in many Managed Records because a single Document can represent evidence of multiple business activities/purposes. When final disposition of a Managed Record in which a document participates occurs (transfer or destroy), the Managed Record is destroyed.   The Document itself is destroyed only when the Managed Record in final disposition is the only one that still refers to the document.\n\nThe Record Documents service has operations to save documents into the records management system, retrieve documents from the records management system and destroy document.",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2656",
                  "name": "2.6.5.6 - Managed Records",
                  "data": {
                    "TagValue_UID": "0147",
                    "TagValue_Number": "2.6.5.6",
                    "TagValue_Service Name": "Managed Records",
                    "TagNotes_Service Definition": "Managed Records provides the management  of the information related to records that are being controlled by the records management system.",
                    "TagNotes_Service Description": "The Managed Records service manages a wide variety of information related to Managed Records including the associations to other Managed Records and Case File Definitions.  Managed Records are the records generated during the course of business that an organization is interested in tracking and includes case files.  The Managed Records service depends on the Record Documents service to manage the actual documents that make up the contents of the record. The Managed Records service maintains the additional metadata about the document required for records management. The Managed Records service also depends on the Change Agent service to maintain information regarding organizations that play various roles with respect to the managed record such as record keeper or record creator.\n\nThe Managed Records service has operations to capture Managed Records, retrieve or destroy the Managed Records and any of the metadata associated with the Manages Record.  This service also allows the Managing Record to be associated with other Managed Records and Case Files.\n",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.3 Content Discovery",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2657",
                  "name": "2.6.5.7 - Record Query",
                  "data": {
                    "TagValue_UID": "0148",
                    "TagValue_Number": "2.6.5.7",
                    "TagValue_Service Name": "Record Query",
                    "TagNotes_Service Definition": "Record Query accepts a request and returns records identifiers that match the query.\n",
                    "TagNotes_Service Description": "The Record Query service provides the ability query Managed Records based on the Record Management System data model elements and their relationships. The Record Queries service consists of a single query operation. The query operation takes a string as an input parameter and returns the results as a string. The input parameter qualifies the requested elements (likely provided in XQuery/Xpath string format) to the Records Management Environment.  The operation returns a string that contains the elements that match the request in the form of an XML string formatted according to the Records Management System XML Schema Definition.\n",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2658",
                  "name": "2.6.5.8 - Record Authentications",
                  "data": {
                    "TagValue_UID": "0149",
                    "TagValue_Number": "2.6.5.8",
                    "TagValue_Service Name": "Record Authentications",
                    "TagNotes_Service Definition": "Record Authentications provides the management of authentication methods and the results of execution of those methods on managed records.\n",
                    "TagNotes_Service Description": "The Record Authentications service provides the ability to manage authentication methods, execute those authentication methods on managed records and to maintain the results of those authentications to enable the assessment of authenticity of a particular record.  The Record Authentications service depends on the Managed Records service to get information relevant to the record to be authenticated. It also depends on the Record Documents service to get the actual contents of the record.\n\nThe Record Authentications service has operations that return the Authentication Result for the Managed Record, calculates the Authentication Result for the Managed Record and compares it to the Authentication Base, allows changing the Authentication Method of a Managed Record from an old Authentication Method to a new Authentication Method which causes the generation of a new Authentication Base using the new Authentication Method.  In addition, this service allows new Authentication Methods to be put into action as well as marking the retirement date of an Authentication Method.\n",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.2.3.4 Content Delivery",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records ManagementN/A",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "2659",
                  "name": "2.6.5.9 - Record Attribute Profiles",
                  "data": {
                    "TagValue_UID": "0150",
                    "TagValue_Number": "2.6.5.9",
                    "TagValue_Service Name": "Record Attribute Profiles",
                    "TagNotes_Service Definition": "Record Attribute Profiles maintain the instance data for profiles that have been added to the records management services.\n",
                    "TagNotes_Service Description": "The Record Attribute Profiles service maintains the instance data for profiles that have been added to the records management family. This service is not responsible for entering the profiles themselves.  The service’s responsibilities are limited to providing information contained in existing profiles and for storing instance data for attributable objects. Attribute profiles enable the dynamic addition of metadata to Managed Records based on profiles set up in the system. Objects are registered with the Record Attribute Profiles service so that attributes can be set for that particular object based on a profile.  The Record Attribute Profiles service contains operations setting and retrieving the attribute values.\n",
                    "TagValue_Example Specification": "Electronic Records Management Software Applications Design Criteria Standard (DoD 5015.02-STD)",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "8.7.18  Archive Data\n8.7.108  Record Data\n8.7.82  Retain Data",
                    "TagValue_JARM/ESL Alignment": "8.13 Records Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "id": "3",
      "name": "3 - Mission Services",
      "data": {
        "TagValue_UID": "0031",
        "TagValue_Number": "3",
        "TagValue_Service Name": "Mission Services",
        "TagNotes_Service Definition": "The functions/services that represent a mission or business process or function.  When choreographed they represent the manifestation of high level mission capabilities.",
        "TagNotes_Service Description": "Layer",
        "TagValue_Example Specification": "Layer",
        "TagValue_Example Solution": "N/A",
        "TagValue_DI2E Framework Status": "Layer",
        "TagValue_DCGS Enterprise Status": "Layer",
        "TagValue_JCA Alignment": "Layer",
        "TagNotes_JCSFL Alignment": "Layer",
        "TagValue_JARM/ESL Alignment": "Layer",
        "TagNotes_Comments": "Layer"
      },
      "size": 100,
      "children": [
        {
          "id": "31",
          "name": "3.1 - Planning and Direction",
          "data": {
            "TagValue_UID": "0032",
            "TagValue_Number": "3.1",
            "TagValue_Service Name": "Planning and Direction",
            "TagNotes_Service Definition": "The Planning & Direction line includes services that provide the ability to synchronize and integrate the activities of collection, processing, exploitation, analysis and dissemination resources to meet BA information requirements.",
            "TagNotes_Service Description": "The Planning & Direction line includes services that provide the ability to synchronize and integrate the activities of collection, processing, exploitation, analysis and dissemination resources to meet BA information requirements. This includes the following sub-areas: Define and Prioritize Requirements  – The ability to translate national through tactical objectives and needs into specific information and operational requirements.  Develop Strategies  – The ability to determine the best approach to collect, process, exploit, analyze, and disseminate data and information to address requirements and predict outcomes. Task and Monitor Resources  – The ability to task, track, direct, and adjust BA operations and their associated resources to fulfill requirements. Evaluation  – The ability to assess the results of BA operations and products to ensure that user requirements are being met. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)\nFamilies in this line: ASSET MANAGEMENT and PLANNING and REPORTING.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "N/A",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "311",
              "name": "3.1.1 - Define and Prioritize Requirements",
              "data": {
                "TagValue_UID": "0331",
                "TagValue_Number": "3.1.1",
                "TagValue_Service Name": "Define and Prioritize Requirements",
                "TagNotes_Service Definition": "The family of services to define, document, and prioritize intelligence requirements",
                "TagNotes_Service Description": "N/A",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3111",
                  "name": "3.1.1.1 - PIR Management",
                  "data": {
                    "TagValue_UID": "0332",
                    "TagValue_Number": "3.1.1.1",
                    "TagValue_Service Name": "PIR Management",
                    "TagNotes_Service Definition": "PIR Management services support management of the Priority Intelligence Requirement (PIR) process .  Supports: \n1. Deriving PIRs from CCIRs\n2. Consolidating and recommending PIR nominations\n3. Deriving Information Requirements from Intelligence Requirements and PIRs\n4. Identifying EEIs which will answer PIRs",
                    "TagNotes_Service Description": "PIR Management services assist a commander in deriving Priority Intelligence Requirements (PIR) to support a mission, tracking, updating, consolidating existing PIRs, and turning those PIRs into specific requirements that can be used for intelligence collection planning.  The PIR Management service works with the Collection Requirements Planning service to task and plan collection activities.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2 Enterprise Services (ES)",
                    "TagNotes_JCSFL Alignment": "8.7.49 Submit Information Requirements\n8.7.87 Validate Request for Information (RFI)\n8.7.101 Track Requests for Information (RFIs)\n8.7.102 Respond or Reply to Request for Information (RFI)\n8.7.103 Receive Request for Information (RFI)\n8.7.113 Receive Requests for Information (RFI) Results\n14.1.17 Evaluate Collection Results\n14.1.18 Evaluate Intelligence Collection, Products, Reporting\n14.1.19 Identify, Prioritize and Validate Intelligence Requirements",
                    "TagValue_JARM/ESL Alignment": "7.02.01 Collection Requirements Management",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3112",
                  "name": "3.1.1.2 - RFI Management",
                  "data": {
                    "TagValue_UID": "0333",
                    "TagValue_Number": "3.1.1.2",
                    "TagValue_Service Name": "RFI Management",
                    "TagNotes_Service Definition": "The service to create, deconflict, monitor, and disseminate Intelligence Requests for Information (RFI)",
                    "TagNotes_Service Description": "An RFI is a specific time-sensitive ad hoc requirement for information or intelligence\nproducts, and is distinct from standing requirements or scheduled intelligence production. An RFI can\nbe initiated at any level of command, and will be validated in accordance with the combatant command’s\nprocedures. An RFI will lead to either a production requirement if the request can be answered with\ninformation on hand or a collection requirement if the request demands collection of new information.\nCollection planning and requirement management are major activities during planning and direction.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2 Enterprise Services (ES)",
                    "TagNotes_JCSFL Alignment": "8.7.49 Submit Information Requirements\n8.7.87 Validate Request for Information (RFI)\n8.7.101 Track Requests for Information (RFIs)\n8.7.102 Respond or Reply to Request for Information (RFI)\n8.7.103 Receive Request for Information (RFI)\n8.7.113 Receive Requests for Information (RFI) Results",
                    "TagValue_JARM/ESL Alignment": "7.07.01 Intelligence Requirements Management",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "312",
              "name": "3.1.2 - Workflow Management",
              "data": {
                "TagValue_UID": "395",
                "TagValue_Number": "3.1.2",
                "TagValue_Service Name": "Workflow Management",
                "TagNotes_Service Definition": "Workflow Management enables coordination and cooperation among members of an organization, helping them to perform complex mission execution by optimizing, assigning, and tracking tasks across the enterprise.",
                "TagNotes_Service Description": "Workflow Management services are a sequence of connected steps that describe a repeatable chain of operations, can include multiple processes, and can stem across organizations or agencies. The workflows represent the mission by means of elementary activities and connections among them. The activities represent fully automated tasks executed by computer or tasks assigned to human actors executed with the support of a computer. Workflow Management provides a consistent, streamlined, and traceable process for users to follow, and monitors the progress of who has performed the work, the status of the work, and when the work has been completed at all times.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "<memo>",
                "TagValue_DI2E Framework Status": "2",
                "TagValue_DCGS Enterprise Status": "1",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "none"
              },
              "size": 100,
              "children": [
                {
                  "id": "3121",
                  "name": "3.1.2.1 - Define Workflows",
                  "data": {
                    "TagValue_UID": "396",
                    "TagValue_Number": "3.1.2.1",
                    "TagValue_Service Name": "Define Workflows",
                    "TagNotes_Service Definition": "The Define Workflows service enables a workflow designer to capture and maintain enterprise workflows in human-readable and machine-readable formats.",
                    "TagNotes_Service Description": "Define Workflows models workflow using intuitive, visual tools and a model notation. Workflow definition helps to ensure control in execution of workflows to ensure both consistency in output and that no step is skipped. Each workflow represents a series of anticipated events by illustrating tasks, and connections among them. The tasks represent fully automated activities executed by computer or tasks assigned to human actors executed with the support of a computer. In DoD environments, workflows are most commonly defined in Business Process Modeling Notation (BPMN).",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2.2 Develop Knowledge and Situational Awareness",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.04 Tracking and Workflow; 7.07.03 Workflow Management",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "3122",
                  "name": "3.1.2.2 - Identify Resources",
                  "data": {
                    "TagValue_UID": "397",
                    "TagValue_Number": "3.1.2.2",
                    "TagValue_Service Name": "Identify Resources",
                    "TagNotes_Service Definition": "Identify Resources captures and maintains a list of available human and technical assets, along with their skill/capability attributes, status, and schedule.",
                    "TagNotes_Service Description": "The Identify Resources service identifies assets which may be distributed across multiple physical locations, and may include (but are not limited to) financial resources, inventory, human skills, production resources, information technology (IT), etc. The resources have definitions of their skills and capabilities based in terms of workflow tasks (Resource X can perform Task Y; Task Y is identified in the Define Workflow Service). The resources also have attributes for their current status (active, inactive, etc.) and their schedule (busy until a certain date, for example). The Identify Resources service integrates closely with the Execute Workflow service to identify the available resources to execute a workflow.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2.2 Develop Knowledge and Situational Awareness",
                    "TagNotes_JCSFL Alignment": "4.8.20 Analyze Resource Tasking and Control",
                    "TagValue_JARM/ESL Alignment": "8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "3123",
                  "name": "3.1.2.3 - Execute Workflows",
                  "data": {
                    "TagValue_UID": "398",
                    "TagValue_Number": "3.1.2.3",
                    "TagValue_Service Name": "Execute Workflows",
                    "TagNotes_Service Definition": "Execute Workflows utilizes the Define Workflow and Identify Resources services to execute an instance of the workflow across the enterprise.",
                    "TagNotes_Service Description": "Execute Workflows enables workflow administrators to start, pause, and cancel workflows. It determines availability of resources and assigns resources to the workflow. It takes input from the Optimization service regarding the best resource to assign to a task.  After the appropriate resource is identified, it utilizes the event notification service to alert the first available resource of the task, and follows up with notifications as the workflow is executed. The service may also include information about the task, or pass along a link to reference information as a workflow is executed. Alerts can also be sent to key people within the organization when changes are made to workflow tasks.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2.2 Develop Knowledge and Situational Awareness",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "3124",
                  "name": "3.1.2.4 - Task Summary",
                  "data": {
                    "TagValue_UID": "399",
                    "TagValue_Number": "3.1.2.4",
                    "TagValue_Service Name": "Task Summary",
                    "TagNotes_Service Definition": "The Task Summary service pulls information from all of the currently running workflows in the system to allow the resource to see all of the tasks in their queue and provide information on the status of the task.",
                    "TagNotes_Service Description": "The Task Summary service pulls information from all of the currently running workflows in the system to allow the resource to see all of the tasks in their queue and provide information on the status of the task. It offers a single view of all of the tasks assigned to a specific resource.  Resources can also use the task summary service to display past and current tasks.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2.2 Develop Knowledge and Situational Awareness",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "3125",
                  "name": "3.1.2.5 - Task Manager",
                  "data": {
                    "TagValue_UID": "400",
                    "TagValue_Number": "3.1.2.5",
                    "TagValue_Service Name": "Task Manager",
                    "TagNotes_Service Definition": "The Task Manager provides the current or past status of a specific task, including information for an activity in a workflow.",
                    "TagNotes_Service Description": "Task Manager enables the resource to manually or systematically update the status of each task in their queue.  The system uses automatic notifications to send out alerts to users when the status of a task changes. Each user can configure rules for how the e-mails will be triggered (e.g. changes to certain products, certain updates to a specific workflow instance, or changes to a specific task.) Any change of status, such as changing the status of a task to complete, is captured and logged for historical reference. This gives users a complete chronological registry of workflow-related information such as interactions with a task, changes to the state of the task, reassignment of a step or task, and time-stamped data and comments.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2.2 Develop Knowledge and Situational Awareness",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                },
                {
                  "id": "3126",
                  "name": "3.1.2.6 - Enterprise Workflow Reporting",
                  "data": {
                    "TagValue_UID": "401",
                    "TagValue_Number": "3.1.2.6",
                    "TagValue_Service Name": "Enterprise Workflow Reporting",
                    "TagNotes_Service Definition": "Enterprise Workflow Reporting enables an understanding of the overall enterprise status by generating a series of reports.",
                    "TagNotes_Service Description": "Enterprise Workflow Reporting enables administrators to define and produce summary reports for workflows that are managed within the WFM Service Family. The reports offer a variety of views of the overall workflow status, and can include reports to track resources, time to complete tasks, or a summary of tasks assigned or completed. These reports may be sent to stakeholders in real time, or on a regularly scheduled basis.",
                    "TagValue_Example Specification": "TBD",
                    "TagValue_Example Solution": "<memo>",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2.2 Develop Knowledge and Situational Awareness",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.04 Tracking and Workflow; 7.07.03 Workflow Managemen",
                    "TagNotes_Comments": "none"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "313",
              "name": "3.1.3 - Planning",
              "data": {
                "TagValue_UID": "0040",
                "TagValue_Number": "3.1.3",
                "TagValue_Service Name": "Planning",
                "TagNotes_Service Definition": "The family of services that support the Intelligence Planning Process.",
                "TagNotes_Service Description": "Operation planning occurs in a networked, collaborative environment, which requires iterative dialogue among senior leaders, concurrent and parallel plan development, and collaboration across multiple planning levels. The focus is on developing plans that contain a variety of viable, embedded options (branches and sequels).  This facilitates responsive plan development and modification, resulting in “living” plans (i.e., the systematic, on-demand, creation and revision of executable plans, with up-to-date options, as circumstances require). This type of adaptive planning also promotes greater involvement with other US agencies and multinational partners.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "There exist various services from NRO that give insight into current taskings.  This supports the planning process, but is not a direct planning tool - rather is input to the planning tool"
              },
              "size": 100,
              "children": [
                {
                  "id": "3131",
                  "name": "3.1.3.1 - Collection Requirements Planning",
                  "data": {
                    "TagValue_UID": "0041",
                    "TagValue_Number": "3.1.3.1",
                    "TagValue_Service Name": "Collection Requirements Planning",
                    "TagNotes_Service Definition": "Collection Requirements Planning provides editing and visibility into the evolution of tasking and collection-related artifacts",
                    "TagNotes_Service Description": "Collection Requirements Planning aggregates information such as asset tasking lists as they mature into ISR Synch Matrix and Proposed Collection Plans.    Using this service collection requirements managers can view, add, delete, or update requirements for a specific collection.  It also allows them to view the mapping of collection requirement to collection task.    It also allows tasking managers to view, add, delete, or update tasking for a specific sensor.\n\nBoth tasking requirements managers and task managers can view mapping of the collection tasks to collection requirements, collection results, and status of collection tasks.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.3 Identify Current Platform and Sensor Tasking\n1.4.7   Generate National Intelligence, Surveillance, and Reconnaissance (ISR) Sensor Tasking\n1.4.10   Generate Sensor Coverage",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3132",
                  "name": "3.1.3.2 - Sensor Cataloging",
                  "data": {
                    "TagValue_UID": "0035",
                    "TagValue_Number": "3.1.3.2",
                    "TagValue_Service Name": "Sensor Cataloging",
                    "TagNotes_Service Definition": "Sensor Cataloging allows clients to request information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.",
                    "TagNotes_Service Description": "Sensor Cataloging allows clients to request information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.    The client can receive metadata documents describing a server's abilities, search for sensors instances on a registry server instance, and receive sensor instance descriptions from the server for either one or multiple sensors.       \n\nThe client can also insert sensor metadata into the registry server,  update sensor metadata on the registry server, or remove sensor metadata from the registry server.   In addition, the client can establish or remove a link between a sensor registry entry instance and an external OGC catalog.   Finally the client can get the sensor status, insert a sensor status, subscribe to a sensor status, or cancel a sensor status subscription.              \n\n[This service is supported by the OGC SWE SIR discussion paper.]",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.11  Maintain Sensor Configurations\n1.4.12   Characterize Sensors Capabilities and Limitations\n3.5.19  Display and Monitor Sensor Assets",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3133",
                  "name": "3.1.3.3 - Intelligence Source Selection",
                  "data": {
                    "TagValue_UID": "0334",
                    "TagValue_Number": "3.1.3.3",
                    "TagValue_Service Name": "Intelligence Source Selection",
                    "TagNotes_Service Definition": "Intelligence Source Selection services support the review of mission requirements for sensor and target range, system responsiveness, timelines, threat, weather, and reporting requirements.   to identify and determine asset and/or resource availability and capability.  Source selection applies to all types of collection sources (i.e. GEOINT, HUMINT, SIGINT, etc.).",
                    "TagNotes_Service Description": "none",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.5 Manage Sensors\n1.4.13 Generate Sensor Tasking\n1.4.21 Assess Sensor Performance\n1.4.24 Recommend Unattended Ground Sensor (UGS) Emplacement",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3134",
                  "name": "3.1.3.4 - Exploitation Planning",
                  "data": {
                    "TagValue_UID": "0043",
                    "TagValue_Number": "3.1.3.4",
                    "TagValue_Service Name": "Exploitation Planning",
                    "TagNotes_Service Definition": "Exploitation Planning tracks analyst skills and availabilities to identify who will do the sensor output analysis and assigns it to them.",
                    "TagNotes_Service Description": "Exploitation Planning allows Analyst Managers to view, create, update, or delete information about a specific analyst.  Analyst Taskers can view, create, update, or delete tasks associated with specific analyst.     Completed, outstanding, or in-work tasks can be viewed by analyst, unit of time (day, week, etc.), or collection requirement.     Assignment Generators evaluate necessary skills, determine appropriate analysts with available time, and assign tasks to the best fitting analyst(s).",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.1.2 Develop Strategies (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.25 Correlate Joint Intelligence, Surveillance, and Reconnaissance (JISR) Resources\n1.4.26 Manage Joint Intelligence, Surveillance, and Reconnaissance (JISR) Resources\n1.4.31 Process Intelligence, Surveillance, and Reconnaissance (ISR) Support Requests",
                    "TagValue_JARM/ESL Alignment": "Not Mapped",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3135",
                  "name": "3.1.3.5 - Target Planning",
                  "data": {
                    "TagValue_UID": "0335",
                    "TagValue_Number": "3.1.3.5",
                    "TagValue_Service Name": "Target Planning",
                    "TagNotes_Service Definition": "Target Planning is the systematic examination of potential target systems-and their components, individual targets, and even elements of targets-to determine the necessary type and duration of the action that must be exerted on each target to create an effect that is consistent with the commander's specific objectives. (JP 1-02. SOURCE: JP 3-60)\n",
                    "TagNotes_Service Description": "Target Planning services support the Target Development process by enabling target system analysis; entity-level target development; and target list management (TLM). \n \nTarget Development is defined as: The systematic examination of potential target systems-and their components, individual targets, and even elements of targets-to determine the necessary type and duration of the action that must be exerted on each target to create an effect that is consistent with the commander's specific objectives. (JP 1-02. SOURCE: JP 3-60)",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "4.7 Analyze Targets (Group)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "314",
              "name": "3.1.4 - Asset Reporting",
              "data": {
                "TagValue_UID": "0337",
                "TagValue_Number": "3.1.4",
                "TagValue_Service Name": "Asset Reporting",
                "TagNotes_Service Definition": "The Asset Reporting family provides services that support discovery, status, and the tasks assigned to intelligence assets.",
                "TagNotes_Service Description": "N/A",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3141",
                  "name": "3.1.4.1 - Asset Status Summary",
                  "data": {
                    "TagValue_UID": "0044",
                    "TagValue_Number": "3.1.4.1",
                    "TagValue_Service Name": "Asset Status Summary",
                    "TagNotes_Service Definition": "Asset Status Summary provides aggregated asset status metadata (including sensors and sets of sensors) across the enterprise.",
                    "TagNotes_Service Description": "Asset Status Summary allows the client to query the status of a specific sensor, a set of sensors, or all sensors across the enterprise.    Results of status summary queries enable reporting of actively monitored assets across the ISR spectrum, regardless of source.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.16Determine Availability of Joint Intelligence, Surveillance, and Reconnaissance (JISR) Capabilities",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3142",
                  "name": "3.1.4.2 - Asset Discovery",
                  "data": {
                    "TagValue_UID": "0105",
                    "TagValue_Number": "3.1.4.2",
                    "TagValue_Service Name": "Asset Discovery",
                    "TagNotes_Service Definition": "Asset Discovery publishes and finds information on DI2E connected devices such as  sensors, network hosted IT equipment, and weapons systems.",
                    "TagNotes_Service Description": "Asset Discovery services maintain a repository of potentially available DI2E assets. Operations typically include Create, Read, Update, Delete (CRUD) type operations on the asset repository database(s) (with appropriate permissions) as well as operations for asset search and asset search results reporting.   Examples of typical asset information might include the asset name, location, operational status, various service descriptors, or anticipated shut down dates (many other fields of data are possible). One example of use is a  analyst might use this service within an application that provides intelligence on the assets that might be available to be tasked due to its services and current geographical location.",
                    "TagValue_Example Specification": "ISO/IEC 19770 Software Asset Management (SAM) \n   - ISO/IEC 19770-1 - process framework \n   - ISO/IEC 19770-2 - data standard\n   - ISO/IEC 19770-3 - software licensing entitlement tags",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "6.3.1.1 Network Resource Visibility",
                    "TagNotes_JCSFL Alignment": "1.3 Perform Detection (Group)\n1.4.22 Calculate Sensor Error or Uncertainty\n3.5.14  Confirm Asset or Sensor Availability\n3.5.16  Identify and Catalog Joint Assets and Activities\n3.5.17  Process Asset Specific Information Query\n5.4.5  Determine Operational and Tactical Assets\n8.1.78 - Query Global Directory Service \n8.7.53  Access Subject Matter Expert and Essential Information\n8.7.55  Establish Discovery Catalogs\n8.9.7  Manage Enterprise Information Assets",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "315",
              "name": "3.1.5 - Tasking Request",
              "data": {
                "TagValue_UID": "0338",
                "TagValue_Number": "3.1.5",
                "TagValue_Service Name": "Tasking Request",
                "TagNotes_Service Definition": "The family of services that support tasking requests",
                "TagNotes_Service Description": "N/A",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3151",
                  "name": "3.1.5.1 - Tasking Message Preparation",
                  "data": {
                    "TagValue_UID": "0042",
                    "TagValue_Number": "3.1.5.1",
                    "TagValue_Service Name": "Tasking Message Preparation",
                    "TagNotes_Service Definition": "Tasking Message Preparation formats the tasking information of the Collection Requirements Planning service for dissemination.",
                    "TagNotes_Service Description": "Tasking Message Preparation allows client to request reformatting of the collection requirement and tasking assignments into a distributable format based on a specified day, sensor group, asset, or requirement set.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.9 Generate Theater and External Intelligence, Surveillance, and Reconnaissance (ISR) Sensor Tasking\n1.4.14 Generate Sensor Configuration Commands",
                    "TagValue_JARM/ESL Alignment": "8.15.04 Intelligence Asset Tasking",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3152",
                  "name": "3.1.5.2 - Task Asset Request",
                  "data": {
                    "TagValue_UID": "0339",
                    "TagValue_Number": "3.1.5.2",
                    "TagValue_Service Name": "Task Asset Request",
                    "TagNotes_Service Definition": "Task Asset Request provides collection and sensor deployment plans.   Changes to these plans are sent to the sensor/platform through this interface. \n\nRef. DCGS-Army chicklit definition",
                    "TagNotes_Service Description": "Task Asset Request serves as the interface that all services use to send sensor messages to the Sensor Provisioning service.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.15 Process Source Requirements\n1.4.16 Determine Availability of Joint Intelligence, Surveillance, and Reconnaissance (JISR) Capabilities\n1.4.17 Determine Time to Sensor Availability\n1.4.18 Identify Elapsing Platform and Sensor Tasking\n1.4.19 Direct and Monitor Sensor Employment\n3.5.17 Process Asset Specific Information Query",
                    "TagValue_JARM/ESL Alignment": "8.15.04 Intelligence Asset Tasking",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "316",
              "name": "3.1.6 - Sensor Web Enablement",
              "data": {
                "TagValue_UID": "0363",
                "TagValue_Number": "3.1.6",
                "TagValue_Service Name": "Sensor Web Enablement",
                "TagNotes_Service Definition": "The Sensor Web Enablement (SWE) family of services provides  observation data and sensor descriptions and well as an  interface for tasking of SPS-registered DI2E sensors.",
                "TagNotes_Service Description": "The Sensor Web Enablement (SWE) family of services provides  a standardized interface for managing and retrieving metadata and observations from heterogeneous sensor systems including discovery and retrieval of real time or archived sensor descriptions, observations, and features of interest.    They also provide a standardized interface for interoperable sensor tasking, sensor capabilities determination, tasking feasibility assessment, tasking request, sensor command tracking, and observation data following the completion of a successful task.",
                "TagValue_Example Specification": "N/A",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "Family",
                "TagValue_DCGS Enterprise Status": "Family",
                "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                "TagNotes_JCSFL Alignment": "1.4.35 Identify Current Platform and Sensor Tasking\n1.4.33 Respond to Satellite Access Authorization\n1.4.34 Conduct Sensor Registration and Calibration\n1.4.20 Determine Platform and Sensor Status",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "OGC SWE spec"
              },
              "size": 100,
              "children": [
                {
                  "id": "3161",
                  "name": "3.1.6.1 - Sensor Observation",
                  "data": {
                    "TagValue_UID": "0039",
                    "TagValue_Number": "3.1.6.1",
                    "TagValue_Service Name": "Sensor Observation",
                    "TagNotes_Service Definition": "Sensor Observation provides Observation data and Sensor descriptions for SOS-registered DI2E sensors.",
                    "TagNotes_Service Description": "The Sensor Observation Service (SOS) provides a standardized interface for managing and retrieving metadata and observations from heterogeneous sensor systems.  It enables discovery and retrieval of real time or archived sensor descriptions, observations, and features of interest.  It allows the client to insert and delete sensor data.  The SOS also supports the functionality to list the capabilities of all registered sensors.  \n\n[This service is supported by the OGC SWE SOS 2.0 specification.]",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "4",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.17  Determine Time to Sensor Availability\n1.4.18  Identify Elapsing Platform and Sensor Tasking",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "OGC SOS Spec"
                  },
                  "size": 100
                },
                {
                  "id": "3162",
                  "name": "3.1.6.2 - Sensor Planning",
                  "data": {
                    "TagValue_UID": "0364",
                    "TagValue_Number": "3.1.6.2",
                    "TagValue_Service Name": "Sensor Planning",
                    "TagNotes_Service Definition": "Sensor Planning Service provides an interface for determining the capabilities and tasking of SPS-registered DI2E sensors.",
                    "TagNotes_Service Description": "The Sensor Planning Service (SPS) provides a standardized interface for interoperable sensor tasking.  The SPS provides the client support for determining the capabilities of the sensor system, identifying the feasibility of a tasking request, accepting commands to task the sensor, tracking the status of sensor commands, and provide a reference to the observation data following the completion of a successful task.  All of which combine to obtain varying stages of planning, scheduling, tasking, collection, processing, archiving, and distribution of resulting observation data and information that is the result of a request.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "4",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.7   Generate National Intelligence, Surveillance, and Reconnaissance (ISR) Sensor Tasking\n1.4.10   Generate Sensor Coverage",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "32",
          "name": "3.2 - Collection",
          "data": {
            "TagValue_UID": "0045",
            "TagValue_Number": "3.2",
            "TagValue_Service Name": "Collection",
            "TagNotes_Service Definition": "The Collection line includes services that provide the ability to gather data and obtain required information to satisfy information needs.",
            "TagNotes_Service Description": "The Tasking (Planning & Direction) line includes services that provide the ability to gather data and obtain required information to satisfy information needs.  This includes the following sub-areas: Signals Collection – The ability to gather information based on the interception of electromagnetic impulses.  Imagery Collection – The ability to obtain a visual presentation or likeness of any natural or man-made feature, object, or activity at rest or in motion.  Measurements and Signatures Collection – The ability to collect parameters and distinctive characteristics of natural or man-made phenomena, equipment, or objects.  Human Based Collection – The ability to acquire information from human resources, human-derived data, and human reconnaissance assets. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "N/A",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "321",
              "name": "3.2.1 - Asset Management",
              "data": {
                "TagValue_UID": "0033",
                "TagValue_Number": "3.2.1",
                "TagValue_Service Name": "Asset Management",
                "TagNotes_Service Definition": "The Asset Management family of services provide sensor collection tasking and status monitoring.  Included are efforts to allocate Enterprise resources, as well as gain insight into the ability to influence external collection assets.",
                "TagNotes_Service Description": "The Asset Management family of services provide sensor collection tasking and status monitoring.  Specific services include:\n• Sensor Provisioning - which sends messages to various sensors asking concerning tasking assignments \n• Sensor Registration - which allows clients to request, add, or delete information about sensors and servers     \n• Sensor Cross Queuing - which passes detection, Geo-location and targeting information to another sensor. \n• Sensor Command Conversion - which converts sensor requests into sensor specific commands.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3211",
                  "name": "3.2.1.1 - Sensor Provisioning",
                  "data": {
                    "TagValue_UID": "0340",
                    "TagValue_Number": "3.2.1.1",
                    "TagValue_Service Name": "Sensor Provisioning",
                    "TagNotes_Service Definition": "Sensor Provisioning sends messages to various sensors asking what assignments they can perform, whether a task request is valid, and adding, modifying and canceling tasking requests.",
                    "TagNotes_Service Description": "Sensor Provisioning allows clients to query individual sensors for information about appropriate sensor tasking, and to task the sensor.  For each sensor, clients can request and receive metadata documents that describe the actions the sensor can perform, request information needed to prepare assignment requests, and request feedback as to whether a specific assignment request is valid or needs improvement to meet business rules.   In addition, the client can send the sensor an assignment request, update a prior assignment request, cancel a prior assignment request, or request the return of the sensor status, .  Lastly, the client can inquire about where and how the results of the tasking can be accessed.\n\n[This service is supported by the OGC SWE SPS 2.0 service.]",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.17  Determine Time to Sensor Availability\n1.4.18  Identify Elapsing Platform and Sensor Tasking",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3212",
                  "name": "3.2.1.2 - Sensor Cross Queuing",
                  "data": {
                    "TagValue_UID": "0342",
                    "TagValue_Number": "3.2.1.2",
                    "TagValue_Service Name": "Sensor Cross Queuing",
                    "TagNotes_Service Definition": "Sensor Cross Queuing passes detection, Geo-location and targeting information to another sensor.",
                    "TagNotes_Service Description": "The Sensor Cross Cueing service is broken into three subservices:  \n(1) Cue Instruction Definition - allows the client to develop a set of sensor cue instructions that accomplish a specific objective.  These instructions describe the parameters which trigger a sensor detection and rules by which the sensor determines what other sensors and/or detection adjudication servers to notify once a detection is made.   The Cue Instruction Definition subservice consists of a set of operations that can be performed by the client. \n(2) Cue Instruction Distribution  - Once a set of sensor cue instructions are developed for a specific objective,  the Cue Instruction Distribution subservice allows a client to distribute those instructions to the appropriate sensors and/or detection adjudication servers \n(3) Detection Distribution  - Once a sensor has made a detection, Detection Distribution distributes the detection, geo-location, and targeting information to other sensors and/or detection adjudication servers that were defined in the previously received cue instruction set.                 \n\nScoping note:  Correlation and fusion of sensor data is outside the scope of Sensor Cross Cueing.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.1.20 Perform Signal Parametric Analysis\n1.4.1 Fuse Sensor Data\n1.4.6 Coordinate Sensor Employment with Operational Plans and Employment\n1.4.27 Reconfigure Netted Sensor Grid Dynamically",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3213",
                  "name": "3.2.1.3 - Sensor Command Conversion",
                  "data": {
                    "TagValue_UID": "0037",
                    "TagValue_Number": "3.2.1.3",
                    "TagValue_Service Name": "Sensor Command Conversion",
                    "TagNotes_Service Definition": "Sensor Command Conversion converts sensor requests into sensor specific commands.",
                    "TagNotes_Service Description": "Sensor Command Conversion converts collection requests into tasking formats that a specific sensor type can understand.  The resulting commands are then sent to the sensor.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "1.4.32 Convert Geospatial Intelligence (GEOINT) Sensor Requests",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3214",
                  "name": "3.2.1.4 - Sensor Alerting",
                  "data": {
                    "TagValue_UID": "0038",
                    "TagValue_Number": "3.2.1.4",
                    "TagValue_Service Name": "Sensor Alerting",
                    "TagNotes_Service Definition": "Sensor Alerting delivers sensor notifications to registered endpoints based on client-configurable subscriptions.",
                    "TagNotes_Service Description": "Sensor Alerting  requests information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.    It receives metadata documents describing a server's abilities along with the sensor description, then retrieves a sensor alert message structure template along with the WSDL definition for the sensor interface.    A client can also subscribe to a sensor's alerts, renew an existing subscription, or cancel a subscription.  Optionally, servers can advertise the type of information published, renew an existing advertisement, or cancel an advertisement.                        \n\n[This service is supported by the OGC SWE SES discussion paper.]",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.1.3 Task and Monitor Resources (P&D)",
                    "TagNotes_JCSFL Alignment": "3.3.5 Generate Alert\n3.3.13 Manage Alerts and Indications",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "33",
          "name": "3.3 - Processing and Exploitation",
          "data": {
            "TagValue_UID": "0046",
            "TagValue_Number": "3.3",
            "TagValue_Service Name": "Processing and Exploitation",
            "TagNotes_Service Definition": "The Processing/Exploitation line includes services that provide the ability to transform collected information into forms suitable for further analysis and/or action by man or machine.",
            "TagNotes_Service Description": "The Processing/Exploitation line includes services that provide the ability to transform collected information into forms suitable for further analysis and/or action by man or machine.  This includes the following sub-areas: Data Transformation – The ability to select, focus, simplify, tag and transform overtly or covertly collected data into human or machine interpretable form for further analysis or other action.  Information Categorization – The ability to identify, classify and verify information associated with time sensitive objectives enabling further analysis or action.  (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)\nFamilies in this line: GEOINT PROCESSING, SIGINT PROCESSING, CI/HUMINT PROCESSING, and MASINT PROCESSING.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "N/A",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "331",
              "name": "3.3.1 - GEOINT Processing",
              "data": {
                "TagValue_UID": "0047",
                "TagValue_Number": "3.3.1",
                "TagValue_Service Name": "GEOINT Processing",
                "TagNotes_Service Definition": "GEOINT Processing provides processing of raw imagery and geospatial data to form basic GEOINT products.",
                "TagNotes_Service Description": "The GeoINT processing family provides processing of imagery and  raw geospatial data to form basic GEOINT products.  Examples include forming images from raw source data, image format conversions, image compression,  basic video processing such as mosaicking, etc.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3311",
                  "name": "3.3.1.1 - Image Rectification",
                  "data": {
                    "TagValue_UID": "0048",
                    "TagValue_Number": "3.3.1.1",
                    "TagValue_Service Name": "Image Rectification",
                    "TagNotes_Service Definition": "Image Rectification provides transformations to project two or more images onto a common image or geocoordinate plane.",
                    "TagNotes_Service Description": "Rectification is a process of geometrically correcting an image so that it can be represented on a planar surface with a standard coordinate system.  This is often done to make an image geometrically conform to other images acquired from other perspectives.  This supports applications such as image/video mosaicking (combining multiple photographic images with overlapping fields of view to produce a larger, apparently seamless image), change detection, and multisensor data fusion.\n\nIt is also done to make an image or multiple images conform to a map, often for geographic information system (GIS) purposes. A common method for this service is to associate geolocations with pixels on an image.  The image pixels are then resampled to place them in into a common map coordinate system. \n\nAnother important application in which a rectification service gets used is in performing an epipolar rectification to ensure that corresponding match points in a pair of stereo images are displaced only along a single axis.  This enables easier matching and analysis of corresponding points in stereo image pairs by reducing the pixel stereo offsets to lie entirely along a single preferred axis (the epipolar axis).\n\nThe Image rectification Service ingests at a minimum an image, and a description of the planar surface to be used in the rectification process.  In some instances, such as for epipolar rectification, the target planar surface is described entirely by a second image in a stereo pair.  Automatic point correspondence matching establishes the required rectification transformation.  In other instances, such as orthorectification, it is common to supply either a reference image and a corresponding digital elevation map (DEM) or a reference image pair such as is found in a Digital Point Positioning Data Base (DPPDB). The DEM is used in performing the rectification transformation to minimize unsightly image artifacts that can affect a rectification done on perspective images in cases where no information about underlying terrain is supplied.  The simplest input to a rectification service is an input image to be rectified with image support data and a specification of a horizontal plane surface.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.3.1 Data Transformation (PE)",
                    "TagNotes_JCSFL Alignment": "1.1.22 Register Images\n8.4.21 Process Imagery\n14.1.8  Rectify Image\n14.1.22  Correlate Image",
                    "TagValue_JARM/ESL Alignment": "7.03.04 Imagery Processing Services",
                    "TagNotes_Comments": "Can be provided as an app, a war file, and a library"
                  },
                  "size": 100
                },
                {
                  "id": "3312",
                  "name": "3.3.1.2 - FMV Geoprocessing",
                  "data": {
                    "TagValue_UID": "0049",
                    "TagValue_Number": "3.3.1.2",
                    "TagValue_Service Name": "FMV Geoprocessing",
                    "TagNotes_Service Definition": "FMV Processing georegisters a stream of video frames in real time..",
                    "TagNotes_Service Description": "FMV georegistration is a real time processing service that takes a video stream and georegisters every frame.  The result is a continuous stream of video frames georegistered to an underlying digital elevation map and reference imagery or to Digital Point Positioning Data Base (DPPDB) reference products.  Any frame in the video stream can be used for precision targeting or other precision position extraction applications.  \n\nThe frames are also crudely mosaicked (i.e. they are all correctly geopositioned but may still have visible \"seams\" from small scene intensity changes during the collection).  These visible seams can be removed by a subsequent FMV mosaicking process that blends image intensities across frame boundaries.\n\nThe FMV georegistration service ingests a stream of FMV frames and either reference imagery with associated reference DEMs or reference DPPDB.  If the quality of the FMV collection metadata is poor (such as is common from small UAVs or mini UAVs), the FMV georegistration service can optionally ingest point correspondences between the 1st frame in the FMV stream and the reference imagery in order to initiate the georegistration process.\n\nOnce the process starts, the service updates the FMV collection metadata for every frame based on a Kalman filter estimation of sensor position and attitude that is augmented by periodic automatic extractions of matching point correspondences between selected frames and the reference imagery.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.3.1 Data Transformation (PE)",
                    "TagNotes_JCSFL Alignment": "14.1.5 Collect and Process Video Sensor Data",
                    "TagValue_JARM/ESL Alignment": "7.03.08 Video Processing Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3313",
                  "name": "3.3.1.3 - AOI Processing",
                  "data": {
                    "TagValue_UID": "0208",
                    "TagValue_Number": "3.3.1.3",
                    "TagValue_Service Name": "AOI Processing",
                    "TagNotes_Service Definition": "AOI Processing enables utilities for adding, deleting and updating persisted user based geometrical areas of interest (AOI). This allows a user to create and manage an AOI (geometrical based).\n\nRef: GCCS-i3 (SOAP based )",
                    "TagNotes_Service Description": "None",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.3.1 Data Transformation (PE)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.15.01 GEOINT Processing",
                    "TagNotes_Comments": "Need more info on this one.  Is it just something associated with NGA CGS?"
                  },
                  "size": 100
                },
                {
                  "id": "3314",
                  "name": "3.3.1.4 - State Service",
                  "data": {
                    "TagValue_UID": "0050",
                    "TagValue_Number": "3.3.1.4",
                    "TagValue_Service Name": "State Service",
                    "TagNotes_Service Definition": "State Service converts image support data into a related sensor model state string.",
                    "TagNotes_Service Description": "State Service extracts the image support data from a file and creates a sensor model state string.   Note: this sensor model state string is a necessary input for National Geospatial-Intelligence Agency (NGA) Common Geospatial Services (CGS).",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.3.1 Data Transformation (PE)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.15.01 GEOINT Processing",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3315",
                  "name": "3.3.1.5 - Image Chipping",
                  "data": {
                    "TagValue_UID": "0362",
                    "TagValue_Number": "3.3.1.5",
                    "TagValue_Service Name": "Image Chipping",
                    "TagNotes_Service Definition": "Image Chipping Service creates user-defined full resolution chip from NITF (National Imagery Transmission Format) image.",
                    "TagNotes_Service Description": "Image Chipping Service allows users to create an image chip from a larger image.  Imagery chips are full resolution \"What You See Is What You Get\" (WYSIWYG) from the current image view.  Users can select square/rectangle around an area of a detailed overview of an image, to create a chip, or smaller piece of the larger image.  \nThis service is needed to reduce the size of the image file and to reduce the image to just the area relevant to the work it will be used for.  Reducing the size of the file decreases the demand on the system and the network communications.  Reducing the image to eliminate unneeded portions of the image simplifies the task for the image analyst.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.3.1 Data Transformation (PE)",
                    "TagNotes_JCSFL Alignment": "14.1.7 Manage Images",
                    "TagValue_JARM/ESL Alignment": "8.15.01 GEOINT Processing",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "332",
              "name": "3.3.2 - SIGINT Processing",
              "data": {
                "TagValue_UID": "0051",
                "TagValue_Number": "3.3.2",
                "TagValue_Service Name": "SIGINT Processing",
                "TagNotes_Service Definition": "SIGINT Processing services provide processing of raw signals data to form basic SIGINT products.",
                "TagNotes_Service Description": "SIGINT processing includes services used to filter, screen, resample, and automatically detect certain basic signal patterns for the purpose of characterizing raw input signals and assigning metadata, prior to exploitation.  This includes application of Fast Fourier Transform (FFT) processing and filters that are applied to the resulting frequency domain products.  SIGINT processing can also include specific convolution and noise reduction services used to clean up raw collected signal.\n\nAnother aspect of SIGINT processing involves very high volume data ingest services, data formatting, automatic dissemination of select snippets of processed SIGINT data, and certain types of near real time collection command and control.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3321",
                  "name": "3.3.2.1 - Signal Pattern Recognition",
                  "data": {
                    "TagValue_UID": "0054",
                    "TagValue_Number": "3.3.2.1",
                    "TagValue_Service Name": "Signal Pattern Recognition",
                    "TagNotes_Service Definition": "Signal Pattern Recognition provides signal pattern recognition in order to detect signal characteristics that tell analysts information about the emitting source.",
                    "TagNotes_Service Description": "Signal Pattern Recognition characterizes a signal based on key signal parameter values and then accesses a database of signal patterns to determine if they match known signal characteristics.   When matches are found, the signal is labeled as belonging to a specific type, piece, or mode of equipment or might indicate transmission protocols.\n\nSignal characteristics might include signal carrier radio frequency, bandwidth, pulse duration(s) and repetition interval(s), dwell or scanning time, polarization, or pulse envelope characteristics (pulse rise and fall times, shape characteristics (e.g. linear chirp or triangular shaped pulse), frequency or phase modulation, and time bandwidth products).",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.3.2 Information Categorization (PE)",
                    "TagNotes_JCSFL Alignment": "1.1.1 Receive and Process Signals\n1.1.25 Pre-Process Sensor Data\n14.1.1 Produce Signals Intelligence (SIGINT) Data\n14.1.29 Validate Processed Sensor Data",
                    "TagValue_JARM/ESL Alignment": "8.15.02 Signals Processing",
                    "TagNotes_Comments": "The NeXTmidas tools are at http://nextmidas.techma.com/"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "333",
              "name": "3.3.3 - CI/HUMINT Processing",
              "data": {
                "TagValue_UID": "0055",
                "TagValue_Number": "3.3.3",
                "TagValue_Service Name": "CI/HUMINT Processing",
                "TagNotes_Service Definition": "CI/HUMINT Processing provides the intelligence derived from the collection discipline in which the human being is the primary collection instrument and can be both a source and collector.",
                "TagNotes_Service Description": "CI/HUMINT Processing involves primarily document language translation and automatic extraction of document metadata (e.g. place names, people, organizations, times, and key actions or relationships). \n\nIt also encompasses services that mine the internet or other repositories for documents pertaining to selected topics (though this service also falls under OSINT when the repository is in the public domain).  This latter service type is the equivalent of an automatic \"clipping service\".  Similarly, specialized RSS feeds could be considered CI/HUMINT processing services.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3331",
                  "name": "3.3.3.1 - Source Management",
                  "data": {
                    "TagValue_UID": "0344",
                    "TagValue_Number": "3.3.3.1",
                    "TagValue_Service Name": "Source Management",
                    "TagNotes_Service Definition": "The service that supports identification, coordination, and protection of HUMINT resources.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2.2 Develop Knowledge and Situational Awareness",
                    "TagNotes_JCSFL Alignment": "14.1.14 Manage Human Intelligence (HUMINT) Dossier\n14.1.15 Manage Human Intelligence (HUMINT) Sources",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "334",
              "name": "3.3.4 - Data Exploitation",
              "data": {
                "TagValue_UID": "0345",
                "TagValue_Number": "3.3.4",
                "TagValue_Service Name": "Data Exploitation",
                "TagNotes_Service Definition": "The family of services to exploit data sources, to include language translation.",
                "TagNotes_Service Description": "N/A",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3341",
                  "name": "3.3.4.1 - Language Translation",
                  "data": {
                    "TagValue_UID": "0056",
                    "TagValue_Number": "3.3.4.1",
                    "TagValue_Service Name": "Language Translation",
                    "TagNotes_Service Definition": "Language Translation renders the meaning within a file from a source language and provides an equivalent version of that file in a user selected target second language.",
                    "TagNotes_Service Description": "Language Translation takes text, a document, or a URL as input along with a specification of the original and destination language.  It then translates the material into the destination language.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.3.1 Data Transformation (PE)",
                    "TagNotes_JCSFL Alignment": "7.1.50 Translate Foreign Languages",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "335",
              "name": "3.3.5 - MASINT Processing",
              "data": {
                "TagValue_UID": "0057",
                "TagValue_Number": "3.3.5",
                "TagValue_Service Name": "MASINT Processing",
                "TagNotes_Service Definition": "MASINT Processing services perform qualitative analysis of data (metric, angle, spatial, wavelength, time dependence, modulation, plasma, and hydro magnetic) derived from specific technical sensors for the purpose of identifying any distinctive features associated with the source, emitter, or sender and to facilitate subsequent identification and/or measurement of the same.",
                "TagNotes_Service Description": "MASINT processing services include specialized SAR image formation algorithms needed to assist in the creation of SAR MASINT products.  In particular, SAR MASINT often makes use of SAR complex image pairs to form interferometric products such as coherent change detection (CCD) or elevation change detection products.  In order for SAR complex images to be suitable for this sort of use they need to be formed in such a manner that they share near identical apertures in frequency space.  A MASINT processing service is often invoked to do SAR complex image pair formation in such a manner that a common frequency aperture is employed.\n\nSpecial condition image formation is only one example of a SAR MASINT processing service.  Other SAR MASINT processing of a different type is crucial to producing other types of SAR MASINT products.\n\nSimilarly, MASINT processing services are used to generate certain hyperspectral MASINT products from raw hyperspectral collections.  Examples include specialized methods of compensating for atmospherics as an initial step in material signature detection processing, etc.  Yet other MASINT processing services are involved in thermal infrared MASINT product formation.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "14.1.3 Produce Measurement and Signature Intelligence (MASINT) Data",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100
            },
            {
              "id": "336",
              "name": "3.3.6 - Support to Targeting",
              "data": {
                "TagValue_UID": "0081",
                "TagValue_Number": "3.3.6",
                "TagValue_Service Name": "Support to Targeting",
                "TagNotes_Service Definition": "The Support to Targeting family provides the data necessary for the   target development process.",
                "TagNotes_Service Description": "Support to Targeting services implement target development process including managing and maintaining target lists, assisting in target development, functionality and vulnerability assessment, impact point  specification, status reporting, and targeting report dissemination.   \n\nSpecific services include:\n\n  • Target Management - which compiles and reports target information \n  • Target Data Matrix - which provides current target status  \n  • Target Validation - which portrays and locates target services, indicates vulnerabilities, and maintains relative target importance.\n  • Target Folder - which maintains hosts target intelligence artifacts \n  • Target List - which maintains summaries of candidate targets",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "GVS MIDB related services seem to apply here, but not sure exactly how at the lower levels"
              },
              "size": 100,
              "children": [
                {
                  "id": "3361",
                  "name": "3.3.6.1 - Target Management",
                  "data": {
                    "TagValue_UID": "0082",
                    "TagValue_Number": "3.3.6.1",
                    "TagValue_Service Name": "Target Management",
                    "TagNotes_Service Definition": "Target Management compiles and reports target information and provides a dissemination service for filtering, combining, and passing that information to higher, adjacent, and subordinate commanders.",
                    "TagNotes_Service Description": "Target Management provides querying and reporting on target locations, battle damage assessments (BDA), weapon target pairing, and other related topics using target lists, the joint target matrix, and other related target files.  Reports can be customized, filtered and/or summarized as they are disseminated upward and downward through the chain of command.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "7.2.9 Transmit Tasking and Target Data\n14.1.31 Maintain Target Model Library\n14.1.32 Retrieve Target Model\n14.1.33 Distribute Target Model",
                    "TagValue_JARM/ESL Alignment": "8.15.06 Target Planning",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3362",
                  "name": "3.3.6.2 - Target Data Matrix",
                  "data": {
                    "TagValue_UID": "0083",
                    "TagValue_Number": "3.3.6.2",
                    "TagValue_Service Name": "Target Data Matrix",
                    "TagNotes_Service Definition": "Target Data Matrix provides the current status of targets within a given area of responsibility (AOR).",
                    "TagNotes_Service Description": "Target Data Matrix displays the status of targets as they progress from target nomination lists to target battle damage assessment lists.  It also generates reports on current target status (i.e. destroyed, recently nominated, current operational status, scheduled for strike, etc. ) within an Area of Responsibility (AOR).   Both these operations are important for managing targeting operations during joint strike operations.\n\nTo perform these operations Target Data Matrix uses target lists and target folders.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "2.1.39  Acquire and Track Target\n3.4.1  Display Automatic Target Recognition (ATR)",
                    "TagValue_JARM/ESL Alignment": "8.15.06 Target Planning",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3363",
                  "name": "3.3.6.3 - Target Validation",
                  "data": {
                    "TagValue_UID": "0084",
                    "TagValue_Number": "3.3.6.3",
                    "TagValue_Service Name": "Target Validation",
                    "TagNotes_Service Definition": "Target Validation portrays and locates target services, indicates target vulnerabilities, and maintains relative target importance.",
                    "TagNotes_Service Description": "Target Validation maintains information about targets that is used to maintenance the target data matrix and provide input to other target support services by presenting and locates known functions and vulnerabilities of a target or target complex including current operational status of targets.  It also provides information that helps establish the strike prioritization of a target.\n\nFor example, a target may have been serving as a command and control communications hub in prior weeks but may have recently been restored to some more benign function, making it undesirable as a high priority target.  The service also may denote no-strike regions in a target complex if the target has multiple functions and if certain of those functions are best preserved.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.15.06 Target Planning",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3364",
                  "name": "3.3.6.4 - Target Folder",
                  "data": {
                    "TagValue_UID": "0085",
                    "TagValue_Number": "3.3.6.4",
                    "TagValue_Service Name": "Target Folder",
                    "TagNotes_Service Definition": "The Target Folder service maintains a softcopy folder containing target intelligence and related materials prepared for planning and executing action against a specific target.",
                    "TagNotes_Service Description": "The Target Folder service maintains a collection of information related to the planning and execution of a strike against a target.  The target folder service serves as a portal for displaying and retrieving data and information that has been placed in the folder.  It also enables analysts to add their inputs or new data to the folder. \n\nExamples of information maintained in a target folder are georegistered target images, lists of designated mean impact points (DMPI) that have been extracted from image data, collateral information about the target's activities and known functions, or no-strike information (such as the location of a place of worship or a civilian shelter within the target complex).",
                    "TagValue_Example Specification": "3.1.2.8",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "14.1.34 Publish Target Model",
                    "TagValue_JARM/ESL Alignment": "8.15.06 Target Planning",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3365",
                  "name": "3.3.6.5 - Target List",
                  "data": {
                    "TagValue_UID": "0086",
                    "TagValue_Number": "3.3.6.5",
                    "TagValue_Service Name": "Target List",
                    "TagNotes_Service Definition": "The Target List service maintains the various target lists that are kept as part of the target strike management process.",
                    "TagNotes_Service Description": "The Target List service provides methods to retrieve a prioritized list of target lists by type; query for an existing list by name; create or remove target lists;  and add, remove, or edit targets in existing target lists.  Targets can also be transferred from one target list to another list type in accordance with the target management process.\n\nExamples of target list types include: 1) the Joint Integrated Prioritized Target List (JIPTL) which are targets prioritized to be struck; 2) the Air Tasking Order (ATO); 3) on-call targets; 4) no-strike targets; 5) nominated targets; and 6) targets scheduled for battle damage assessment (BDA).",
                    "TagValue_Example Specification": "3.1.2.8",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "4.7.2  Manage Target List",
                    "TagValue_JARM/ESL Alignment": "8.15.06 Target Planning",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3366",
                  "name": "3.3.6.6 - Target Mensuration",
                  "data": {
                    "TagValue_UID": "0346",
                    "TagValue_Number": "3.3.6.6",
                    "TagValue_Service Name": "Target Mensuration",
                    "TagNotes_Service Definition": "Target Mensuration services provide the measurement of a feature or location on the earth to determine an absolute latitude, longitude, and height.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "14.1.23 Perform Imagery Coordinate Mensuration",
                    "TagValue_JARM/ESL Alignment": "8.15.06 Target Planning",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3367",
                  "name": "3.3.6.7 - BDA/CDA",
                  "data": {
                    "TagValue_UID": "0347",
                    "TagValue_Number": "3.3.6.7",
                    "TagValue_Service Name": "BDA/CDA",
                    "TagNotes_Service Definition": "This component performs Battle Damage Assessment (BDA) analysis. BDA is the estimate of damage resulting from the application of lethal or nonlethal military force. BDA is composed of physical damage assessment, functional damage assessment, and target system assessment.  \n\nGCCS Spec/USMTF message",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "7.2.8 Transmit Tasking and Target Data to Battle Damage Assessment (BDA) Assets\n7.2.51 Transmit Battle Damage Assessment (BDA) Report",
                    "TagValue_JARM/ESL Alignment": "8.15.06 Target Planning",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "34",
          "name": "3.4 - Analysis, Prediction and Production",
          "data": {
            "TagValue_UID": "0058",
            "TagValue_Number": "3.4",
            "TagValue_Service Name": "Analysis, Prediction and Production",
            "TagNotes_Service Definition": "The Analysis, Prediction & Production line includes services that provide the ability to integrate, evaluate, interpret, and predict knowledge and information from available sources to develop intelligence and forecast the future state to enable situational awareness and provide actionable information.",
            "TagNotes_Service Description": "The Analysis, Prediction & Production line includes services that provide the ability to integrate, evaluate, interpret, and predict knowledge and information from available sources to develop intelligence and forecast the future state to enable situational awareness and provide actionable information.  This includes the following sub-areas: Integration – The ability to identify, assimilate and correlate relevant information from single or multiple sources.  Evaluation – The ability to provide focused examination of the information and assess its reliability and credibility to a stated degree of confidence.  Interpretation – The ability to derive knowledge and develop new insight from gathered information to postulate its significance.  Prediction – The ability to describe the anticipated future state of the operational/physical environment based on the depiction of past and current information.  Product Generation – The ability to develop and tailor intelligence, information, and environmental content and products per customer requirements.  (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)\nFamilies in this line: GEOINT ANALYSIS, SIGINT ANALYSIS, HUMINT ANALYSIS, MASINT/AGI ANALYSIS, and TARGETING SUPPORT.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "N/A",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "341",
              "name": "3.4.1 - GEOINT Analysis",
              "data": {
                "TagValue_UID": "0059",
                "TagValue_Number": "3.4.1",
                "TagValue_Service Name": "GEOINT Analysis",
                "TagNotes_Service Definition": "GEOINT Analysis services describe, assess, and visually depict physical features and geographically referenced activities on the Earth.",
                "TagNotes_Service Description": "GEOINT Analysis services provide intelligence information ( imagery, imagery intelligence, multi-spectral analysis, hyper-spectral analysis, etc.) for geographically referenced features and activities.   Specific services include:\n • Change Detection - which identifies pixel-by-pixel differences between two images of the same area.\n  • Source Selection - which presents a user with sets of images that can satisfy a geopositioning precision requirement.\n  • Sensor Model Instantiation - which generates sensor model state data from image metadata. \n  • Triangulation  - which calculates a geolocation estimate based on multiple image collections acquired from distinct collection angles. \n  • State Service - which converts image support data into a related sensor model state string.\n  • Resection  - which adjusts image support data when the sensor model data is unknown, incomplete, or imprecise.\n  • Geomensuration  - which computes image based mensuration calculations using image points measured from an image.\n  • DPPDB Mensuration - which performs geopositioning by aligning directly to DPPDB metadata.\n  • Image Registration - which matches points in a selected image to one or more control images to increase the accuracy of geoposition coordinates.\n  • Moving Target Indicator (MTI) - which links 'dots' of point locations into tracks that indicate historical & projected direction and speed.\n  • Topographical Survey - which derives and represents elevation and terrain characteristics from reports and data. \n  • Automatic Target Recognition - which identifies specified entities on an image using image processing techniques. \n  • Emitter Geolocation - which determines the likely position of an emitter source from emitter intercept data.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3411",
                  "name": "3.4.1.1 - Change Detection",
                  "data": {
                    "TagValue_UID": "0060",
                    "TagValue_Number": "3.4.1.1",
                    "TagValue_Service Name": "Change Detection",
                    "TagNotes_Service Definition": "Change Detection identifies feature changes via a pixel analysis between two images of the same area.",
                    "TagNotes_Service Description": "Change detection ingests two precisely co-registered images and identifies differences in pixels collected before and after some interval of time at the same geographic location.   Typically the changes in pixel values must exceed a user defined thresholds including registration uncertainty \"guard band\", size, color, and/or shape in order to screen out noise/clutter changes.   Output is an image formatted mask that identifies which pixels have significant changes between the two images.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.4.1 Integration (AP)",
                    "TagNotes_JCSFL Alignment": "3.4.3 Receive, Store and Maintain Geospatial Product Information",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "34110",
                  "name": "3.4.1.10 - Sensor Model Instantiation",
                  "data": {
                    "TagValue_UID": "0062",
                    "TagValue_Number": "3.4.1.10",
                    "TagValue_Service Name": "Sensor Model Instantiation",
                    "TagNotes_Service Definition": "Sensor Model Instantiation generates sensor model state data from image metadata. ",
                    "TagNotes_Service Description": "Sensor Model Instantiation determines a sensor model type and state data given raw image metadata.  \n\nAs an example, consider SAR and pushbroom EO sensor models ((pushbroom scanners deliver a perspective image with highly correlated \nexterior orientation parameters for each image line).   The SAR sensor model might require specification of collection squint and grazing angles.  However, the raw collection taken from the EO sensor may only provide metadata on the sensor collection trajectory and the specified image center.  The Sensor Model Instantiation service can use this raw EO sensor metadata to calculate collection squint and grazing angles as used by the SAR sensor model.  In other simpler cases, the service may simply transfer raw collection metadata values directly into matching sensor model parameter fields.\n\nResulting sensor model state data can be used with associated sensor functions such as image-to-ground and ground-to-image transformations and will support (but not provide) other GEOINT services such as Image Registration, DPPDB Mensuration, Triangulation, Source Selection, Resection, Rectification, and Geomensuration (see definitions in related 2.4.1 GEOINT Exploitation services).",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "3.2.36  Generate Coordinates\n3.2.32 Mensurate Object Coordinates\n3.4.3 Receive, Store and Maintain Geospatial Product Information",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "34111",
                  "name": "3.4.1.11 - GEO-Calculations",
                  "data": {
                    "TagValue_UID": "0366",
                    "TagValue_Number": "3.4.1.11",
                    "TagValue_Service Name": "GEO-Calculations",
                    "TagNotes_Service Definition": "Geo-Calculations provide the ability to perform geospatially based calculations on data stored within the enterprise to further enrich the operator’s understanding of the data relationships.\n",
                    "TagNotes_Service Description": "Users have the need to perform calculations and analytics on the geospatial within the enterprise.  Example analytics include geo-location lookups, distance calculations between two entities, coordinate conversions, and entity lookup.\n",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "3.4.3 Receive, Store and Maintain Geospatial Product Information\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3412",
                  "name": "3.4.1.2 - Triangulation",
                  "data": {
                    "TagValue_UID": "0063",
                    "TagValue_Number": "3.4.1.2",
                    "TagValue_Service Name": "Triangulation",
                    "TagNotes_Service Definition": "Triangulation calculates a geolocation estimate based on multiple image collections acquired from distinct collection angles. \n",
                    "TagNotes_Service Description": "Triangulation performs a geolocation estimate of a photo identifiable point based on multiple image collections acquired from distinct collection angles.  In this process it combines four distinct algorithmic functions: 1) least squares adjustment of sensor support data along with selected image plane tie or control point measurements that are each common to two or more images, 2) rigorous uncertainty propagation calculations, 3) geolocation computations using a triangulation method, and 4) blunder detection to discard point measurement outliers.\n\nTo initiate a triangulation the service is presented with a set of images and associated image point coordinates.  Image sensor models and coordinates are also provided from each image for the point to be geolocated.    Adjustable parameters include image collection geometries and (sometimes) sensor internal parameters, such as lens focal length or lens distortion.\n",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "3.2.36  Generate Coordinates\n\n3.2.32 Mensurate Object Coordinates\n\n3.4.3 Receive, Store and Maintain Geospatial Product Information",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3413",
                  "name": "3.4.1.3 - Resection",
                  "data": {
                    "TagValue_UID": "0064",
                    "TagValue_Number": "3.4.1.3",
                    "TagValue_Service Name": "Resection",
                    "TagNotes_Service Definition": "Resection adjusts image support data when the sensor model data is unknown, incomplete, or imprecise.\n",
                    "TagNotes_Service Description": "Resection uses Digital Point Positioning Database (DPPDB) stereo reference image pairs or other reference imagery, possibly combined with a reference Digital Elevation Map (DEM), to adjust the support data of an image acquired by a less accurate source.  \n\nThe service ingests the image coordinates of tie points in all the images including reference images and the acquired image that is to be resected as well as instantiated sensor models.  The sensor model of the acquired image has adjustable parameters and enhancing the values of these adjustable sensor model parameters is the objective of the Resection service.\n\nThe Resection service uses the reference data to compute 3D geolocations for the selected tie points.  It then performs cycles of bundle adjustment (i.e. least squares sensor model parameter value and tie point coordinate value adjustment) until  it achieves convergence with the measured image coordinates for all the 3D reference points mapped onto the acquired image coordinate space. The resulting adjusted sensor model parameter values for the acquired image constitute the resection results.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "3.4.3 Receive, Store and Maintain Geospatial Product Information\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3414",
                  "name": "3.4.1.4 - Geomensuration",
                  "data": {
                    "TagValue_UID": "0065",
                    "TagValue_Number": "3.4.1.4",
                    "TagValue_Service Name": "Geomensuration",
                    "TagNotes_Service Definition": "Geomensuration computes image based mensuration calculations using image points measured from an image.\n",
                    "TagNotes_Service Description": "Geomensuration (measurement of geometric quantities) measures absolute geolocation of a photo identifiable point (geocoordinates, circular error and  linear error), horizontal distance and vertical elevation separations between two photo identifiable points (relative or absolute, and includes error ranges).\n\nIn general, a geomensuration service ingests image coordinates for one or more points in a georegistered image combined with an accompanying georegistered Digital Elevation Map (DEM) or in two or more georegistered images without an accompanying DEM.  It then returns one or more precision measurements based on their image coordinates.  Resected sensor models also help geomensuration computations.\n\nExamples of specialized geomensuration services might include \"height of object determined from measured shadow length\", \"location of center of a circle\", or \"perpendicular separation between two parallel plane surfaces\".\n",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "3.2.44  Display Range and Bearing Between Objects\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3415",
                  "name": "3.4.1.5 - DPPDB Mensuration",
                  "data": {
                    "TagValue_UID": "0066",
                    "TagValue_Number": "3.4.1.5",
                    "TagValue_Service Name": "DPPDB Mensuration",
                    "TagNotes_Service Definition": "Digital Point Positioning Database (DPPDB) Mensuration performs geopositioning by aligning directly to DPPDB metadata.\n",
                    "TagNotes_Service Description": "Digital Point Positioning Database (DPPDB)  Mensuration (geometric measurement) identifies a point on one of the DPPDB images and returns the geolocation of that point, complete with circular and linear errors.  The service uses the DPPDB sensor model and the fact that DPPDB reference imagery provides stereo depth to get the elevation as well as the horizontal geolocation of the chosen point.  \n\nThe service relies on the point being geolocated actually appearing in the DPPDB imagery.  Trying to estimate where a mobile target might lie when it is not visible in the DPPDB imagery and then selecting that estimated position in DPPDB image coordinates for target geolocation invites very serious geolocation errors. This estimation technique is referred to as \"visual point transfer\" and should be used only under very special circumstances.\n",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "2.1.39  Acquire and Track Target\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3416",
                  "name": "3.4.1.6 - Image Registration",
                  "data": {
                    "TagValue_UID": "0067",
                    "TagValue_Number": "3.4.1.6",
                    "TagValue_Service Name": "Image Registration",
                    "TagNotes_Service Definition": "Image Registration matches points in a selected image to one or more control images to increase the accuracy of geoposition coordinates in the selected image.\n",
                    "TagNotes_Service Description": "Image Registration selects image coordinate matching points between a selected image and one or more control images.  It then adjusts the sensor model for the selected image through resection (converging cycles of bundle adjustments, accompanied by associated sensor model parameter adjustments and outlier point eliminations).   Positional uncertainties including circular and linear error are also propagated.\n\nThe end result is common geospatial coordinates between the selected image and the control image thus enabling accurate image overlays, mosaicking (appending images together to construct a single image covering a larger scene), or georeferencing.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.1 Integration (AP)",
                    "TagNotes_JCSFL Alignment": "3.4.3 Receive, Store and Maintain Geospatial Product Information",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3417",
                  "name": "3.4.1.7 - MTI Tracking",
                  "data": {
                    "TagValue_UID": "0068",
                    "TagValue_Number": "3.4.1.7",
                    "TagValue_Service Name": "MTI Tracking",
                    "TagNotes_Service Definition": "Moving Target Indicator (MTI) Tracking links 'dots' of point locations into tracks that indicate historical & projected direction and speed.\n",
                    "TagNotes_Service Description": "Moving Target Indicator (MTI) Tracking ingests and tags a sequence of point locations (\"dots\") of moving objects indicating their speed, direction, range, azimuth location (an angular measurement in a spherical coordinate system), and time stamp.    Dots from raw MTI sources are then linked into track segments which last until either a moving object stops, and thus disappears from the MTI sensor data stream or until an occlusion or sharp change of speed, or some other confounding event causes the service to \"break track\".     \n\nDots may also have additional attributes such as a one dimensional range profile, shape and/or intensity descriptor.\n",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.4.4 Prediction (AP)",
                    "TagNotes_JCSFL Alignment": "2.1.1  Form Tracks\n\n2.1.33  Provide Tracking Services\n\n3.4.20 Maintain Track Management Information\n\n3.4.21 Display Tracks",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3418",
                  "name": "3.4.1.8 - Topographical Survey",
                  "data": {
                    "TagValue_UID": "0069",
                    "TagValue_Number": "3.4.1.8",
                    "TagValue_Service Name": "Topographical Survey",
                    "TagNotes_Service Definition": "Topographical Survey derives and represents elevation and terrain characteristics from reports and data.\n",
                    "TagNotes_Service Description": "Topographical Survey generates maps based on a variety of terrain, elevation, and feature data.  One example is creating a 2D contour line map with vector graphics indicating features such as rivers, communication lines, and known roads.\n",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "3.4.3 Receive, Store and Maintain Geospatial Product Information\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3419",
                  "name": "3.4.1.9 - Automatic Target Recognition",
                  "data": {
                    "TagValue_UID": "0070",
                    "TagValue_Number": "3.4.1.9",
                    "TagValue_Service Name": "Automatic Target Recognition",
                    "TagNotes_Service Definition": " Automatic Target Recognition identifies specified entities on an image using image processing techniques. \n",
                    "TagNotes_Service Description": "Automatic Target Recognition services process imagery to identify candidate specified 'targets' that match selected pixel 'signature patterns'.    \n\nExamples of 'targets' might be a tank even if partially covered or camouflaged an aircrafts located on runways.\n\nArtifact formats, targets, and analytic methods vary, but one example is an ATR service that ingests hyperspectral imagery (HSI) in the near IR (NIR), short wave IR (SWIR) bands, and target signature definitions and returns image coordinates where signatures are found along with nature of each candidate 'target'.   To do this it might perform anomaly detection through orthogonal subspace projection (considering each HSI pixel a vector in a multidimensional space, determining dominant spectral signatures by running a pixel clustering algorithm, creating a subspace projection operator matrix, and projecting the pixel's signature into a spectral subspace that does not contain the identified clutter signature components).",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.4.2 Evaluation (AP)",
                    "TagNotes_JCSFL Alignment": "3.4.3 Receive, Store and Maintain Geospatial Product Information\n\n14.1.2 Produce Imagery Intelligence (IMINT) Data\n\n14.1.21  Extract Automated Features",
                    "TagValue_JARM/ESL Alignment": "7.04.06 Imagery Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "342",
              "name": "3.4.2 - SIGINT Analysis",
              "data": {
                "TagValue_UID": "0071",
                "TagValue_Number": "3.4.2",
                "TagValue_Service Name": "SIGINT Analysis",
                "TagNotes_Service Definition": "SIGINT Analysis services provide various forms of signals intelligence (SIGINT) varying from recurring, serialized reports to instantaneous periodic reports.",
                "TagNotes_Service Description": "The SIGINT Analysis family of services support Electronic Intelligence (ELINT), Communications Intelligence (COMINT), and Foreign Instrumentation Signals Intelligence (FISINT).  \n • ELINT is the interception, geolocation, and analysis of electronic emissions produced by adversary equipment that intentionally transmits for non-communications purposes.  Most notably this includes radars but sometimes also includes other types of weaponry and equipment.   The purposes of ELINT are to: ascertain services and limitations of target emitters, geolocate target emitters, and determine the current state of readiness of target emitters.\n  • COMINT is the interception, geolocation, and decryption of either voice or electronic text transmissions. Included within COMINT analysis is communications traffic analysis.  \nSpecific SIGINT Analysis services include:\n  • SIGINT Analysis and Reporting - which generates various reports from raw SIGINT data. \n  • Emitter Correlation - which correlates multiple intercepts to the same signal emitting entity. \n  • Signal Pattern Recognition - which provides signal pattern recognition in order to detect signal characteristics that tell analysts information about the emitting source.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3421",
                  "name": "3.4.2.1 - SIGINT Analysis and Reporting",
                  "data": {
                    "TagValue_UID": "0072",
                    "TagValue_Number": "3.4.2.1",
                    "TagValue_Service Name": "SIGINT Analysis and Reporting",
                    "TagNotes_Service Definition": "SIGINT Analysis and Reporting generates various reports from raw SIGINT data.",
                    "TagNotes_Service Description": "SIGINT Analysis and Reporting generates selected types of SIGINT reports from ingested SIGINT intercept data.   Three examples of the types of reports produced include: \n\n1)  A report on the full Electronic Order of Battle (EOB) of an adversary within a specified region.   The report might name the identified emitters along with a description of the function/services of the overall system they are associated with.  The report might also attempt to identify specific military units based on the identified emitters.\n\n2)  A report on the locations of all surface-to-air defense systems in a specified region.  The report would list the services, the mobility potential, and the known vulnerabilities or limitations of each air defense unit identified.\n\n3)  A report on the signal properties and service implications of unknown emitters detected in a specified region.   Such a report might note the presence of an unidentified chirped pulse emitter and, based on the chirp bandwidth, pulse repetition intervals, and scan rates suggest that the emitter is part of an upgraded surface-to-air defense system.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "1.1.20  Perform Signal Parametric Analysis\n1.1.13  Process Acoustic Sensor Data\n14.1.16 Disseminate High Priority Tactical Signals Intelligence (SIGINT) Report\n14.1.16  Disseminate High Priority Tactical Signals Intelligence (SIGINT) Report\n14.1.28  Disseminate Tactical Signals Intelligence (SIGINT) Report",
                    "TagValue_JARM/ESL Alignment": "8.16.05 Signals Analysis",
                    "TagNotes_Comments": "Terry (SIGINT FT, terence.a.wynne@lmco.com, 301-543-5441. ) proposed to drop the “SIGINT Analysis & Reporting” service, and to add 2 new ones: Specific Emitter Identifier (see Terry or Mark P. for more details), and Purge/Surge (or Enterprise Data Header) ? that should go under Records Mgmt…for more details, contact Melissa Bodman. - Julie Jamieson, August 8th, 2011"
                  },
                  "size": 100
                },
                {
                  "id": "3422",
                  "name": "3.4.2.2 - Emitter Correlation",
                  "data": {
                    "TagValue_UID": "0073",
                    "TagValue_Number": "3.4.2.2",
                    "TagValue_Service Name": "Emitter Correlation",
                    "TagNotes_Service Definition": "Emitter Correlation correlates multiple intercepts to the same signal emitting entity.",
                    "TagNotes_Service Description": "The service ingests emitter intercept signal characterizations and identifies which signal intercepts come from the same emitter by identifying signals whose extracted features sufficiently match.    The output is a list of which of the ingested signals (or signal pulse sequences) come from each separate emitter source, along with the associated times of the signal intercepts.\n\nThere are two ways in which this service can be used:  (1) Supporting multi-platform emitter geolocation analysis over short period using a pulse-by-pulse basis and (2) correlating signals collected over a more prolonged time interval using signal intercepts from several distinct emitters to analyze sequences of pulses for matching signal characteristics.\n\nEmitter Correlation is in some ways the converse of the ELINT de-duplication service (see 2.3.2 SIGINT Processing services).",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.3.2 Information Categorization (PE)",
                    "TagNotes_JCSFL Alignment": "1.1.7  Register Electromagnetic Signals\n3.2.32 Mensurate Object Coordinates",
                    "TagValue_JARM/ESL Alignment": "8.16.05 Signals Analysis",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3423",
                  "name": "3.4.2.3 - Emitter Geolocation",
                  "data": {
                    "TagValue_UID": "0074",
                    "TagValue_Number": "3.4.2.3",
                    "TagValue_Service Name": "Emitter Geolocation",
                    "TagNotes_Service Definition": "Emitter Geolocation Analysis & Location Refinement determines the likely position of an emitter source from emitter intercept data.",
                    "TagNotes_Service Description": "Emitter Geolocation takes correlated emitter intercepts collected by one or more platforms and computes the likely emitter geolocation, geolocation error ellipse, and (if the emitter is in motion) an estimate of the emitter's velocity.\n\nIntercepts may contain signal frequency and/or time of arrival information, intercept angle of arrival (AOA) , ID and \"collection instant geolocation\" of the platform(s) that generated the intercept reports, associated pointing/position uncertainties characterizing the collection systems involved, and/or previously computed emitter geolocation along with the geolocation error ellipse and target velocity vector.\n\nExample analytic methods include: 1) triangulation using AOA data, 2) time difference of arrival (TDOA),  3) frequency difference of arrival (FDOA), or 4) some combination of the first three methods.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.16.05 Signals Analysis",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3424",
                  "name": "3.4.2.4 - COMINT Externals Analysis",
                  "data": {
                    "TagValue_UID": "0075",
                    "TagValue_Number": "3.4.2.4",
                    "TagValue_Service Name": "COMINT Externals Analysis",
                    "TagNotes_Service Definition": "COMINT Externals Analysis analyzes patterns of communications metadata.",
                    "TagNotes_Service Description": "COMINT Externals Analysis provides link analysis functionality for the specific application of highlighting patterns of communications activity.   The analysis is based solely on communications metadata (origination and destination node IDs, cell phone IDs, phone numbers, packet lengths, etc.) and not communication content (digitized voice, text messages, etc.).\n\nTo do this, COMINT Externals Analysis first ingests communication metadata and, if needed, strips off associated message content.  The resultant metadata entities are then linked to each other, as well as other entities.  Message transmission structures may also be analyzed for such things as statistics showing how often packets with one specific format are immediately followed by packets with a second specified format.  \n\nAnalyzing the structure of analog communications signals is possible, but digital analysis is increasingly more common.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "8.2.26 Configure Communications and Security Devices\n8.12.4 Allocate and Manage Collaboration Communications",
                    "TagValue_JARM/ESL Alignment": "8.16.05 Signals Analysis",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "343",
              "name": "3.4.3 - HUMINT Analysis",
              "data": {
                "TagValue_UID": "0076",
                "TagValue_Number": "3.4.3",
                "TagValue_Service Name": "HUMINT Analysis",
                "TagNotes_Service Definition": "The HUMINT Analysis family establishes intelligence derived from human collected information sources.",
                "TagNotes_Service Description": "The CI/HUMINT Analysis family provides intelligence derived from information collected by counter intelligence and/or human sources.  Typically HUMINT data is written reports containing raw text, but it may also be electronic telephone records, online sources, computer sources, audio transcripts, etc.\nThe objective of CI/HUMINT Analysis is to transform and filter the raw source material into an organized format that allows reports to be generated concerning specific people, activities, items (such as smuggled goods), institutional structures (such as insurgent cell networks and command chains), and events (both past and planned).\nSpecific CI/HUMINT services include:\n  • Entity Linkages - which associates CI/HUMINT related reports based on related metadata. \n  • Entity Activity Patterns - which determines intelligence relationships among specific objects such as people, places, items, and events.\n  • Identity Disambiguation - which determines if one entity (usually a person) is the same as another entity by analyzing descriptive information on the two entities.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3431",
                  "name": "3.4.3.1 - Entity Activity Patterns",
                  "data": {
                    "TagValue_UID": "0078",
                    "TagValue_Number": "3.4.3.1",
                    "TagValue_Service Name": "Entity Activity Patterns",
                    "TagNotes_Service Definition": "The Entity Activity Patterns service determines intelligence relationships among specific objects such as people, places, items, and events.",
                    "TagNotes_Service Description": "Entity Activity Patterns takes information about people, places, material items, organizations, events, etc. and organizes them into spatio-temporal and linkage contexts.  This enables highlighting of associations indicative of key patterns of activity such as geographic/geospatial location, memberships, clandestine organizational structures, financial activity patterns, attack indicator patterns, travel patterns, and so-called \"patterns of life\". \n\nService operations will include interrelated data organizing, filtering, display, and linkage analysis tools.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "14.1.4  Produce Human Intelligence (HUMINT) Data",
                    "TagValue_JARM/ESL Alignment": "7.04.08 Link Analysis Services",
                    "TagNotes_Comments": "Dependency on data analytics"
                  },
                  "size": 100
                },
                {
                  "id": "3432",
                  "name": "3.4.3.2 - Identity Disambiguation",
                  "data": {
                    "TagValue_UID": "0079",
                    "TagValue_Number": "3.4.3.2",
                    "TagValue_Service Name": "Identity Disambiguation",
                    "TagNotes_Service Definition": "Identity Disambiguation determines if one entity (usually a person) is the same as another entity by analyzing descriptive information on the two entities.",
                    "TagNotes_Service Description": "Identity Disambiguation typically takes people names (but sometimes place names or institution names) and determines if they refer to the same entity.  This is done in one (or both) of two ways: (1) names can be compared with each other via similarity metrics or association dictionaries or (2) by comparing associated metadata to determine degrees of similarity.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "Dependency on data analytics"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "344",
              "name": "3.4.4 - MASINT/AGI Analysis",
              "data": {
                "TagValue_UID": "0080",
                "TagValue_Number": "3.4.4",
                "TagValue_Service Name": "MASINT/AGI Analysis",
                "TagNotes_Service Definition": "Measurement and Signature Intelligence/Advanced Geospatial Intelligence (MASINT/AGI) Analysis services provide intelligence derived from many different sources and sensors to identify the specific characteristics of a target and enables it to be located and tracked.   \nNote: The family of MASINT services is very broad and many are sensitive or classified.   The single example provided below may be supplemented after engagement with the DI2E MASINT Functional Team.",
                "TagNotes_Service Description": "Measurement and Signature Intelligence/Advanced Geospatial Intelligence (MASINT/AGI) Analysis services provide intelligence derived from many different sources and sensors to identify the specific characteristics of a target and enables it to be located and tracked.   \nNote: The family of MASINT services is very broad and many are sensitive or classified.     Other (unclassified) MASINT examples include:\n  • SAR Coherent Change Detection (CCD)\n  • differential interferometric SAR (DInSAR)\n  • EO polarimetry\n  • SAR polarimetry\nThus the single COMINT Externals Analysis example provided below may be supplemented with additional services after discussions are held with the DI2E MASINT Functional Team.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100
            },
            {
              "id": "345",
              "name": "3.4.5 - Production",
              "data": {
                "TagValue_UID": "0348",
                "TagValue_Number": "3.4.5",
                "TagValue_Service Name": "Production",
                "TagNotes_Service Definition": "The Production family of services to support production, to include Reporting Services, Production Workflow, and Digital Production.",
                "TagNotes_Service Description": "N/A",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3451",
                  "name": "3.4.5.1 - Reporting Services",
                  "data": {
                    "TagValue_UID": "0368",
                    "TagValue_Number": "3.4.5.1",
                    "TagValue_Service Name": "Reporting Services",
                    "TagNotes_Service Definition": "Reporting services allow for the creation, editing, and approval of new reports.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3452",
                  "name": "3.4.5.2 - Production Workflow",
                  "data": {
                    "TagValue_UID": "0369",
                    "TagValue_Number": "3.4.5.2",
                    "TagValue_Service Name": "Production Workflow",
                    "TagNotes_Service Definition": "Production Workflow provides the necessary management for the creation of new products, guiding the product status through various levels of approval.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3453",
                  "name": "3.4.5.3 - Digital Production",
                  "data": {
                    "TagValue_UID": "0370",
                    "TagValue_Number": "3.4.5.3",
                    "TagValue_Service Name": "Digital Production",
                    "TagNotes_Service Definition": "Digital Production provides authoring, mark-up, dissemination, document control, geospatially visualized, geo-smart, GeoPDF functions.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "2.4.5 Product Generation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "346",
              "name": "3.4.6 - Analytic Decision Support",
              "data": {
                "TagValue_UID": "0024",
                "TagValue_Number": "3.4.6",
                "TagValue_Service Name": "Analytic Decision Support",
                "TagNotes_Service Definition": "The Analytic Decision Support Services family provides advanced analytic analysis and presentation to help analysts uncover, determine, or predict otherwise complex relationships among various DI2E entities.",
                "TagNotes_Service Description": "The Analytic Decision Support family provides advanced analytic analysis and presentation to help analysts uncover, determine, or predict otherwise complex relationships among various DI2E entities.    Services include the answering multi-dimensional analytical queries (online analytical processing).",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3461",
                  "name": "3.4.6.1 - Timelines Analysis",
                  "data": {
                    "TagValue_UID": "0349",
                    "TagValue_Number": "3.4.6.1",
                    "TagValue_Service Name": "Timelines Analysis",
                    "TagNotes_Service Definition": "Timeline Analysis helps analysts determine relationships of events over time.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3462",
                  "name": "3.4.6.2 - Structured Analytic Techniques",
                  "data": {
                    "TagValue_UID": "0350",
                    "TagValue_Number": "3.4.6.2",
                    "TagValue_Service Name": "Structured Analytic Techniques",
                    "TagNotes_Service Definition": "Structured Analytic Techniques services provide the mechanism by which internal thought processes are externalized in a systematic and transparent manner.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3463",
                  "name": "3.4.6.3 - Argument Mapping",
                  "data": {
                    "TagValue_UID": "0351",
                    "TagValue_Number": "3.4.6.3",
                    "TagValue_Service Name": "Argument Mapping",
                    "TagNotes_Service Definition": "Argument Mapping is the visual representation of the structure of an argument in informal logic. It includes the components of an argument such as a main contention, premises, co-premises, objections, rebuttals, and lemmas.",
                    "TagNotes_Service Description": "Argument Mapping is the visual representation of the structure of an argument in informal logic. It includes the components of an argument such as a main contention, premises, co-premises, objections, rebuttals, and lemmas. Typically an argument map is a “box and arrow” diagram with boxes corresponding to propositions and arrows corresponding to relationships such as evidential support. Argument mapping is often designed to support deliberation over issues, ideas and arguments",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.4.3 Interpretation (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3464",
                  "name": "3.4.6.4 - Alternative Future Analysis",
                  "data": {
                    "TagValue_UID": "0352",
                    "TagValue_Number": "3.4.6.4",
                    "TagValue_Service Name": "Alternative Future Analysis",
                    "TagNotes_Service Definition": "Alternative Future Analysis supports postulating possible, probable, and preferable futures through a  systematic and pattern-based understanding of past and present, and to determine the likelihood of future events and trends.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "2.4.4 Prediction (AP)",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3465",
                  "name": "3.4.6.5 - Link Analysis",
                  "data": {
                    "TagValue_UID": "0365",
                    "TagValue_Number": "3.4.6.5",
                    "TagValue_Service Name": "Link Analysis",
                    "TagNotes_Service Definition": "Link analysis is a data-analysis technique used to evaluate relationships (connections) between entities. Relationships may be identified among various types of objects, including organizations, people and transactions.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "7.04.08 Link Analysis Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "347",
              "name": "3.4.7 - Modeling and Simulation",
              "data": {
                "TagValue_UID": "0027",
                "TagValue_Number": "3.4.7",
                "TagValue_Service Name": "Modeling and Simulation",
                "TagNotes_Service Definition": "The Modeling and Simulation family of services uses representative realities to assess current or possible future conditions in assess area of interest including DI2E ISR platforms and sensors.",
                "TagNotes_Service Description": "Modeling and Simulation uses representative realities to assess current or possible future conditions in assess area of interest including DI2E ISR platforms and sensors.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3471",
                  "name": "3.4.7.1 - War Gaming",
                  "data": {
                    "TagValue_UID": "0028",
                    "TagValue_Number": "3.4.7.1",
                    "TagValue_Service Name": "War Gaming",
                    "TagNotes_Service Definition": "War gaming brings together C4ISR, and Synthetic Natural Environments (SNE) and the Common Operating Picture (COP) to support course of action analysis, mission planning and rehearsal, and individual and collective training.",
                    "TagNotes_Service Description": "War gaming models and/or simulate ISR services and collection parameters over time while allowing for analysis of the technical feasibility of the mission with respect to weather, terrain, battlefield conditions, order of battle, and threat characteristics through the use of expanded, robust Joint ISR resource management tools adapted to net-centric operations and transformational communications  (Ref. DCGS Integrated Roadmap (2005–2018) Version 1.2; 3 January 2005 (section 5))\nObjective war gaming operations include the ability to monitor, locate, identify, and represent military units and assets (Army, Navy, Marines, Air Force, and Special Operational Forces) as missions and sorties occur, build and maintain operational surface picture in an Area Of Interest (AOI);  develop and present threat conceptual models, services data, force structure and tactics; account for intelligence sensors, systems, processes, and organizations; re-use prior war game data and models; provide algorithms for the interaction of ‘Red’ (enemy force),  ‘Blue’ (friendly force), and non-combatant behaviors; maintain evolving threats catalog(s); maintain validated parameter and performance data; integrate decision making process including the impact of rules of engagement (ROE); provides realistic deterministic fusion methodology (versus simple stochastic replications); render “3D” views; present real time (or near real time) statistics; inject actual or potential weather effects; and accommodate Concepts of Operation (CONOPS) and other related threat policy and guidance.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "2",
                    "TagValue_JCA Alignment": "2.4.4 Prediction (AP)",
                    "TagNotes_JCSFL Alignment": "4.6.3  Predict Situation Effects\n4.6.19  Predict Collateral Damage\n4.6.21  Calculate Probability of Engagement Effectiveness\n4.6.23  Analyze Force Vulnerability\n4.6.27  Determine Time to Complete the Mission\n5.3.6  Model and Simulate Mission Scenarios\n5.3.8   Generate Wargaming Scenarios\n5.3.9  Model and Simulate Risk Scenarios\n5.3.10  Identify Risk using Modeling and Simulation",
                    "TagValue_JARM/ESL Alignment": "8.18.01 Modeling; 8.18.02 Simulation",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3472",
                  "name": "3.4.7.2 - Scenario Generation",
                  "data": {
                    "TagValue_UID": "0353",
                    "TagValue_Number": "3.4.7.2",
                    "TagValue_Service Name": "Scenario Generation",
                    "TagNotes_Service Definition": "The Scenario Generation services provides the ability to build and manage scenarios.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "5.3.8   Generate Wargaming Scenarios",
                    "TagValue_JARM/ESL Alignment": "8.18.01 Modeling; 8.18.02 Simulation",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3473",
                  "name": "3.4.7.3 - Model Building",
                  "data": {
                    "TagValue_UID": "0354",
                    "TagValue_Number": "3.4.7.3",
                    "TagValue_Service Name": "Model Building",
                    "TagNotes_Service Definition": "The Model Building service supports the ability to build and visualize analytical models",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "5.3.9  Model and Simulate Risk Scenarios\n5.3.10  Identify Risk using Modeling and Simulation",
                    "TagValue_JARM/ESL Alignment": "8.18.01 Modeling",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3474",
                  "name": "3.4.7.4 - Sensor Modeling",
                  "data": {
                    "TagValue_UID": "0029",
                    "TagValue_Number": "3.4.7.4",
                    "TagValue_Service Name": "Sensor Modeling",
                    "TagNotes_Service Definition": "Sensor Modeling models and/or simulates ISR platform and sensor access and collection parameters over time.",
                    "TagNotes_Service Description": "Sensor Modeling models and/or simulates ISR platform and sensor access and collection parameters over time.  \n\nStructures of sensor modeling systems typically include Players (the specific sensor being modeled), Markers (those things implicitly represented; e.g., tactical positions and collection domains), Environment (establishes the physical battlespace), and Zones (abstract spaces where the location of sensors or related elements is not an important element of the model).\n\nEvents fall into these classes: physical (e.g., start of collection gathering), Information Exchange (e.g., orders, reports, and messages), and Element coordination (place in explicit modeling).\n\nSensor modeling operations typically might include: Simulation control (stop, pause, and resume simulation), Game state update; Command and Control (C2) decisions; and External events from outside the simulation.\n\nCriteria monitored might include items such as: Sensor maximum services, Sensor schedules, Sensor lifecycle estimates; Maintenance schedules; Intelligence needs assessments; Sensor identity, instance, location, and environment; Visual appearance, Acoustic appearance, Radar signatures, Identified intention, Mission role; Sensor health estimates; Relationship to ground receiving systems; and Probability tables and estimates.\n\nSensors modeled might include (but aren’t limited to): Class I and IV Unmanned Aerial Vehicles (UAVs), Electro Optical/Infrared Sensors (EO/IR), Synthetic Aperture Radar (SAR) sensors, Moving Target Indicators (MTIs), Aerial Common Sensor (ACS), U-2/TR-1, Global Hawk, Predator, Joint Surveillance Target Attack Radar System (JSTARS), Advanced Tactical Air Reconnaissance System (ATARS),  Prophet,  Airborne Reconnaissance Integrated Electronics Suite, and Unattended Ground Sensors (UGSs).",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "5.3.6  Model and Simulate Mission Scenarios",
                    "TagValue_JARM/ESL Alignment": "8.18.01 Modeling",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3475",
                  "name": "3.4.7.5 - Target Solution Modeling",
                  "data": {
                    "TagValue_UID": "0355",
                    "TagValue_Number": "3.4.7.5",
                    "TagValue_Service Name": "Target Solution Modeling",
                    "TagNotes_Service Definition": "Target Solution Modeling models and/or simulates weaponeering and  weapon effects.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.18.01 Modeling",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3476",
                  "name": "3.4.7.6 - Orchestration Modeling",
                  "data": {
                    "TagValue_UID": "0030",
                    "TagValue_Number": "3.4.7.6",
                    "TagValue_Service Name": "Orchestration Modeling",
                    "TagNotes_Service Definition": "Orchestration Modeling presents an interface for designing the flow of service executions to achieve an overall functional need.",
                    "TagNotes_Service Description": "Orchestration Modeling presents interfaces for designing actual or potential flows of service executions to achieve an overall functional need.   Functionality includes design definition and management.  Resultant models may be stored in orchestration languages such as Business Process Execution Language (BPEL) or Business Process Modeling Notation (BPMN). Business Process Management Language (BPML), or Web Service Choreography Interface (WSCI).     \n\nThe most popular of these, BPEL, is made of three main entities: partners that abstractly represent the services Involved, variables used to manipulate exchanged data and hold business logic states, and activities that describe the business logic operations such as invoking a web service or assigning a value to a variable.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.2.3.8 Enterprise Application Software",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "8.18.01 Modeling",
                    "TagNotes_Comments": "Dependency on orchestration"
                  },
                  "size": 100
                }
              ]
            },
            {
              "id": "348",
              "name": "3.4.8 - Analysis Support to C2",
              "data": {
                "TagValue_UID": "0204",
                "TagValue_Number": "3.4.8",
                "TagValue_Service Name": "Analysis Support to C2",
                "TagNotes_Service Definition": "This line provides intelligence support for command and control",
                "TagNotes_Service Description": "N/A",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3481",
                  "name": "3.4.8.1 - Order of Battle Analysis",
                  "data": {
                    "TagValue_UID": "0356",
                    "TagValue_Number": "3.4.8.1",
                    "TagValue_Service Name": "Order of Battle Analysis",
                    "TagNotes_Service Definition": "Order of Battle Analysis determines the identification, strength, command structure, and disposition of the personnel, units, and equipment of any military force.",
                    "TagNotes_Service Description": "The Order of Battle Analysis service assists users in analyzing intelligence about friendly and opposing forces, and generating intelligence briefs about those forces.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "3.4 Situational Awareness (SA) Data Management (Group)\n3.4.10 Integrate Information on Potential Adversary Courses of Action (COAs)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3482",
                  "name": "3.4.8.2 - Intelligence Preparation of the Battlefield",
                  "data": {
                    "TagValue_UID": "0357",
                    "TagValue_Number": "3.4.8.2",
                    "TagValue_Service Name": "Intelligence Preparation of the Battlefield",
                    "TagNotes_Service Definition": "Intelligence Preparation of the Battlefield services continuously analyze the threat and environment in an area.",
                    "TagNotes_Service Description": "The Intelligence Preparation of the Battlefield service supports intelligence analysts in satisfying Joint Intelligence Preparation of the Operational Environment (JIPOE) requirements.  This service assists users in:\n? Identifying the Operational Area\n? Analyzing the Mission and the Commander's Intent\n? Determining significant characteristics of the Operational Environment\n? Establishing the physical and nonphysical limitations of the force's Area of Interest\n? Establishing appropriate level of detail for intelligence analysis\n? Identify intelligence and information gaps, shortfalls and priorities\n? Submitting requests for information to support further analysis",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "3.2.1 Display Common Operational Picture (COP)\n3.2.2 Display Common Tactical Picture (CTP)\n3.5.6 Access Joint Asset Information\n3.5.8 Access Unit Readiness and Logistics Reports\n3.5.9 Enter, Display, Update, and Monitor Force Status\n3.5.10 Present Mission Resources Information\n3.5.14 Confirm Asset or Sensor Availability\n3.5.16 Identify and Catalog Joint Assets and Activities\n14.1.24 Provide Graphical Intelligence Preparation of the Operational Environment (IPOE) Products\n14.1.25 Provide Data Assets from Intelligence Preparation of the Operational Environment (IPOE) Products\n14.1.26 Conduct Joint Intelligence Preparation of the Operational Environment",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3483",
                  "name": "3.4.8.3 - Mission Planning and Force Execution support",
                  "data": {
                    "TagValue_UID": "0358",
                    "TagValue_Number": "3.4.8.3",
                    "TagValue_Service Name": "Mission Planning and Force Execution support",
                    "TagNotes_Service Definition": "This component supports mission planning and force execution analysis to find, fix, track, and target.",
                    "TagNotes_Service Description": "The service supports mission planning and force execution analysis to find, fix, track, and target.  \nFind: Develop JIPOE, detect target, and determine target conditions\nFix: confirm target, refine target location, and plot movement\nTrack: maintain situational awareness and maintain track continuity\nTarget: validate desired effects, finalize target data, and predict consequences",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "N/A",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "2.2 Combat Identification (CID) (Group)\n3.4.5 Maintain Shared Situational Awareness\n3.4.8 Manage Prioritization of Defended Asset Information Sets\n\n3.4.11 Translate and Distribute Commander's Intent",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3484",
                  "name": "3.4.8.4 - Weather Effect  Planning",
                  "data": {
                    "TagValue_UID": "0336",
                    "TagValue_Number": "3.4.8.4",
                    "TagValue_Service Name": "Weather Effect  Planning",
                    "TagNotes_Service Definition": "Weather Effect Planning supports the planning and analysis of weather on operations and collections.",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "2",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "5.2 Understand",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "(gap)",
                    "TagNotes_Comments": "Is this the actual weather service, or is it a planning tool that utilizes the weather services?"
                  },
                  "size": 100
                }
              ]
            }
          ]
        },
        {
          "id": "35",
          "name": "3.5 - BA Data Dissemination and Relay",
          "data": {
            "TagValue_UID": "0087",
            "TagValue_Number": "3.5",
            "TagValue_Service Name": "BA Data Dissemination and Relay",
            "TagNotes_Service Definition": "The BA Data Dissemination and Relay line includes services that provide the ability to present, distribute, or make available intelligence, information and environmental content and products that enable understanding of the operational/physical environment to military and national decision-makers.",
            "TagNotes_Service Description": "The BA Data Dissemination and Relay line includes services that provide the ability to present, distribute, or make available intelligence, information and environmental content and products that enable understanding of the operational/physical environment to military and national decision-makers.  This includes the following sub-areas: BA Data Transmission – The ability to send collected data directly to processing, exploitation analysis, production and visualization systems, leveraging both Net-Centric information transport and intelligence-controlled systems.  BA Data Access – The ability to provide authorized customer access to data and products, leveraging both Net-Centric computing infrastructure and intelligence-controlled systems. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)\nFamilies in this line: DISSEMINATION MANAGEMENT and MESSAGING SYSTEM INTERFACE.",
            "TagValue_Example Specification": "Line",
            "TagValue_Example Solution": "N/A",
            "TagValue_DI2E Framework Status": "Line",
            "TagValue_DCGS Enterprise Status": "Line",
            "TagValue_JCA Alignment": "Line",
            "TagNotes_JCSFL Alignment": "Line",
            "TagValue_JARM/ESL Alignment": "Line",
            "TagNotes_Comments": "Line"
          },
          "size": 100,
          "children": [
            {
              "id": "351",
              "name": "3.5.1 - Dissemination Management",
              "data": {
                "TagValue_UID": "0088",
                "TagValue_Number": "3.5.1",
                "TagValue_Service Name": "Dissemination Management",
                "TagNotes_Service Definition": "Dissemination Management includes authorizing the release/dissemination of products, the assignment of addresses to receive those products, as well as assignment of the transmission path/medium for dissemination of the products.",
                "TagNotes_Service Description": "Dissemination Management includes authorizing the release/dissemination of products, the assignment of addresses to receive those products, as well as assignment of the transmission path/medium for dissemination of the products.  \n\nCurrently the one specific service included is Package Product which converts the intelligence product into a suitable form for dissemination.",
                "TagValue_Example Specification": "family",
                "TagValue_Example Solution": "N/A",
                "TagValue_DI2E Framework Status": "family",
                "TagValue_DCGS Enterprise Status": "family",
                "TagValue_JCA Alignment": "family",
                "TagNotes_JCSFL Alignment": "family",
                "TagValue_JARM/ESL Alignment": "family",
                "TagNotes_Comments": "family"
              },
              "size": 100,
              "children": [
                {
                  "id": "3511",
                  "name": "3.5.1.1 - Dissemination Authorization",
                  "data": {
                    "TagValue_UID": "0359",
                    "TagValue_Number": "3.5.1.1",
                    "TagValue_Service Name": "Dissemination Authorization",
                    "TagNotes_Service Definition": "Dissemination Authorization supports the process to submit, track, and authorize requests to release information.",
                    "TagNotes_Service Description": "The Dissemination Authorization service receives an intelligence report and information the entity a report will be disseminated to, and uses classification markings, security metadata, information about the entity, and possibly even man-in-the-loop, to determine if the report is releasable to the specified entity.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "7.1.51 Broadcast Information\n7.1.95 Disseminate Sensor Products\n8.7.22 Disseminate Data\n14.1.11 Disseminate Intelligence Products",
                    "TagValue_JARM/ESL Alignment": "6.02.03 Content Delivery Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3512",
                  "name": "3.5.1.2 - Package Product",
                  "data": {
                    "TagValue_UID": "0089",
                    "TagValue_Number": "3.5.1.2",
                    "TagValue_Service Name": "Package Product",
                    "TagNotes_Service Definition": "This service converts the intelligence product into a suitable form for dissemination.",
                    "TagNotes_Service Description": "This service converts the intelligence product into a suitable form for dissemination.  After exploitation is complete and an intelligence product has been created, it must be prepared for dissemination.  This may include converting to a file format suitable for the customer, final security review of the intelligence product to be disseminated, and finding and assigning a globally unique number in preparation for dissemination.  This service would call to the Global Object ID service for assignment of the globally unique number.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "3",
                    "TagValue_JCA Alignment": "2.5.1 BA Data Transmission (IDD)",
                    "TagNotes_JCSFL Alignment": "14.1.11 Disseminate Intelligence Products",
                    "TagValue_JARM/ESL Alignment": "6.02.03 Content Delivery Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                },
                {
                  "id": "3513",
                  "name": "3.5.1.3 - Tear Line Reporting",
                  "data": {
                    "TagValue_UID": "0360",
                    "TagValue_Number": "3.5.1.3",
                    "TagValue_Service Name": "Tear Line Reporting",
                    "TagNotes_Service Definition": "Tear Line services support creation of Tear Lines within products to support dissemination across multiple security domains.",
                    "TagNotes_Service Description": "The Tear Line Reporting service uses security markings and metadata to generate tear lines from existing intelligence reports, to support foreign disclosure and allow important intelligence to be disseminated to a greater audience without risking sensitive information.",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "6.02.03 Content Delivery Services",
                    "TagNotes_Comments": "Might want to combine with TDF"
                  },
                  "size": 100
                },
                {
                  "id": "3514",
                  "name": "3.5.1.4 - Foreign Disclosure Management",
                  "data": {
                    "TagValue_UID": "0361",
                    "TagValue_Number": "3.5.1.4",
                    "TagValue_Service Name": "Foreign Disclosure Management",
                    "TagNotes_Service Definition": "Foreign Disclosure Management services support the  release of classified military information and law enforcement information to foreign entities or governments. Provides the ability to receive, process, release, and monitor requests for information release.\n",
                    "TagNotes_Service Description": "N/A",
                    "TagValue_Example Specification": "N/A",
                    "TagValue_Example Solution": "N/A",
                    "TagValue_DI2E Framework Status": "4",
                    "TagValue_DCGS Enterprise Status": "1",
                    "TagValue_JCA Alignment": "6.4.1.1 Assure Access",
                    "TagNotes_JCSFL Alignment": "(gap)",
                    "TagValue_JARM/ESL Alignment": "6.02.03 Content Delivery Services",
                    "TagNotes_Comments": "None"
                  },
                  "size": 100
                }
              ]
            }
          ]
        }
      ]
    }
  ],
  "name": "SvcV-4",
  "id": 0
};





// function csvJSON(csv){
//   var lines=csv.split("\n");
//   var result = [];
//   var headers=lines[0].split(",");
//   for(var i=1;i<lines.length;i++){
//     var obj = {};
//     var currentline=lines[i].split(",");
//     for(var j=0;j<headers.length;j++){
//       obj[headers[j]] = currentline[j];
//     }
//     result.push(obj);
//   }
//   //return result; //JavaScript object
//   return JSON.stringify(result); //JSON
// }

// var insert = function(path, index, children, item) {
//   if (!path[index + 1]) {
//     var temp = {};
//     if (!index) {
//       temp.id = path[0];
//     } else {
//       temp.id = path.join('');
//     }
//     temp.name = item['TagValue_Number'] +" - "+ item['TagValue_Service Name'];
//     temp.data = item;
//     temp.size = 100;
//     temp.children = [];
//     children.push(temp);
//     // console.log('children', children);
//   } else {
//     var id;
//     id = path.slice(0, index + 1);
//     id = id.join('');
    
//     var newChild = _.find(children, { 'id': id});
//     // console.log('new', newChild);

//     if (newChild) {
//       insert(path, index + 1, newChild.children, item);
//     } else {
//       var temp = {};
//       temp.id = id;
//       temp.name = '';
//       temp.size = 100;
//       temp.children = [];
//       children.push(temp);
//       insert(path, index + 1, temp.children, item);
//     }
//   }
// }

// var makeTree = function() {
//   var unparsed = _.sortBy(MOCKDATA2.svcv4, function(s){ return s['TagValue_Number']});
//   var parsed = {};
//   var children = parsed.children = [];
//   parsed.name = "SvcV-4";
//   parsed.id = 0;
//   _.each(unparsed, function(item){
//     var path = item['TagValue_Number'].split('.');
//     insert(path, 0, children, item);
//   });
//   MOCKDATA2.parsed = parsed;
//   console.save(parsed);
// };

// (function(console){

// console.save = function(data, filename){

//     if(!data) {
//         console.error('Console.save: No data')
//         return;
//     }

//     if(!filename) filename = 'console.json'

//     if(typeof data === "object"){
//         data = JSON.stringify(data, undefined, 4)
//     }

//     var blob = new Blob([data], {type: 'text/json'}),
//         e    = document.createEvent('MouseEvents'),
//         a    = document.createElement('a')

//     a.download = filename
//     a.href = window.URL.createObjectURL(blob)
//     a.dataset.downloadurl =  ['text/json', a.download, a.href].join(':')
//     e.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
//     a.dispatchEvent(e)
//  }
// })(console)

// makeTree();

/* jshint ignore:end */
