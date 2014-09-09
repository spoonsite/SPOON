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


var MOCKDATA2 = {};
MOCKDATA2.componentList = [ {
  "componentId" : 67,
  "guid" : "3a57b4c2-a90b-4098-8c56-916618afd8ee",
  "name" : "Central Authentication Service (CAS)",
  "description" : "The Central Authentication Service (CAS) is a single sign-on protocol for the web. Its purpose is to permit a user to access multiple applications while providing their credentials (such as userid and password) only once. It also allows web applications to authenticate users without gaining access to a user's security credentials, such as a password. The name CAS also refers to a software package that implements this protocol. <br> <br>CAS provides enterprise single sign-on service: <br>-An open and well-documented protocol <br>-An open-source Java server component <br>-A library of clients for Java, .Net, PHP, Perl, Apache, uPortal, and others <br>-Integrates with uPortal, Sakai, BlueSocket, TikiWiki, Mule, Liferay, Moodle and others <br>-Community documentation and implementation support <br>-An extensive community of adopters",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NRO",
  "releaseDate" : null,
  "version" : null,
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "david.treat",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1405698045000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "Project Manager",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "End User",
      "answeredDate" : 1399961730000
    } ]
  }, {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "End User",
      "answeredDate" : 1398845730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1399961730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "1.2.1",
    "codeDescription" : "Identity and Access Management",
    "important" : true,
    "codeLongDescription": "<strong>Description:</strong> IdAM includes services that provide criteria used in access decisions and the rules and requirements assessing each request against those criteria.  Resources may include applications, services, networks, and computing devices.<br/> <strong>Definition:</strong> Identity and Access Management (IdAM) defines the set of services that manage permissions required to access each resource.",
  }, {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "LICTYPE",
    "typeDescription" : "License Type",
    "code" : "OPENSRC",
    "codeDescription" : "Open Source",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/CAS.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='http://www.jasig.org/cas' title='http://www.jasig.org/cas' target='_blank'> http://www.jasig.org/cas</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='http://www.jasig.org/cas/download' title='http://www.jasig.org/cas/download' target='_blank'> http://www.jasig.org/cas/download</a>"
  } ],
  "reviews" : [ {
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "comment" : "This is a great product however, it's missing what I think are critical features.  Hopefully, they are working on it for future updates.",
    "rating" : 4,
    "title" : "Great but missing features (SAMPLE)",
    "usedTimeCode" : "< 1 year",
    "lastUsed" : 1381644930000,
    "updateDate" : 1399961730000,
    "organization" : "NSA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Difficult Installation"
    } ]
  }, {
    "username" : "Cathy TEST",
    "userType" : "Project Manager",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 5,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1381644930000,
    "updateDate" : 1391537730000,
    "organization" : "NSA",
    "recommend" : false,
    "pros" : [  {
      "text" : "Well Tested"
    } ],
    "cons" : [ {
      "text" : "Poorly Tested"
    }, {
      "text" : "Bulky"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 9,
  "guid" : "75718e9b-f650-4600-b949-cdebc5c179ec",
  "name" : "CLAVIN",
  "description" : "CLAVIN (Cartographic Location And Vicinity Indexer) is an open source software package for document geotagging and geoparsing that employs context-based geographic entity resolution. It extracts location names from unstructured text and resolves them against a gazetteer to produce data-rich geographic entities.  CLAVIN uses heuristics to identify exactly which \"Portland\" (for example) was intended by the author, based on the context of the document. CLAVIN also employs fuzzy search to handle incorrectly-spelled location names, and it recognizes alternative names (e.g., \"Ivory Coast\" and \"Cete d'Ivoire\") as referring to the same geographic entity.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1398578400000,
  "version" : "see site for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1400005248000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : 1388769330000,
    "endDate" : 1393694130000,
    "currentLevelCode" : "LEVEL1",
    "reviewedVersion" : "1.0",
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "P"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ {
      "name" : "Discoverable",
      "score" : 3
    }, {
      "name" : "Accessible",
      "score" : 4
    }, {
      "name" : "Documentation",
      "score" : 3
    }, {
      "name" : "Deployable",
      "score" : 4
    }, {
      "name" : "Usable",
      "score" : 5
    }, {
      "name" : "Error Handling",
      "score" : 2
    }, {
      "name" : "Integrable",
      "score" : 1
    }, {
      "name" : "I/O Validation",
      "score" : 2
    }, {
      "name" : "Testing",
      "score" : 0
    }, {
      "name" : "Monitoring",
      "score" : 2
    }, {
      "name" : "Performance",
      "score" : 1
    }, {
      "name" : "Scalability",
      "score" : 1
    }, {
      "name" : "Security",
      "score" : 4
    }, {
      "name" : "Maintainability",
      "score" : 3
    }, {
      "name" : "Community",
      "score" : 3
    }, {
      "name" : "Change Management",
      "score" : 2
    }, {
      "name" : "CA",
      "score" : 3
    }, {
      "name" : "Licensing",
      "score" : 4
    }, {
      "name" : "Roadmap",
      "score" : 0
    }, {
      "name" : "Willingness",
      "score" : 5
    }, {
      "name" : "Architecture Alignment",
      "score" : 5
    } ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL1",
    "codeDescription" : "Level 1 - Initial Reuse Analysis",
    "important" : true
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "FOSS",
    "codeDescription" : "FOSS",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Mapping"
  }, {
    "text" : "Reference"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/clavin_logo.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://clavin.bericotechnologies.com' title='http://clavin.bericotechnologies.com' target='_blank'> http://clavin.bericotechnologies.com</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://github.com/Berico-Technologies/CLAVIN' title='https://github.com/Berico-Technologies/CLAVIN' target='_blank'> https://github.com/Berico-Technologies/CLAVIN</a>"
  }, {
    "name" : "DI2E Framework Evaluation Report URL",
    "type" : "DI2E Framework Evaluation Report URL",
    "description" : null,
    "link" : "<a href='https://storefront.di2e.net/marketplace/public/Clavin_ChecklistReport_v1.0.docx' title='https://storefront.di2e.net/marketplace/public/Clavin_ChecklistReport_v1.0.docx' target='_blank'> https://storefront.di2e.net/marketplace/public/Clavin_ChecklistReport_v1.0.docx</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='http://clavin.bericotechnologies.com/' title='http://clavin.bericotechnologies.com/' target='_blank'> http://clavin.bericotechnologies.com/</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://github.com/Berico-Technologies/CLAVIN' title='https://github.com/Berico-Technologies/CLAVIN' target='_blank'> https://github.com/Berico-Technologies/CLAVIN</a>"
  } ],
  "reviews" : [ {
    "username" : "Abby TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 4,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "> 3 years",
    "lastUsed" : 1388769330000,
    "updateDate" : 1391537730000,
    "organization" : "DCGS Navy",
    "recommend" : false,
    "pros" : [ {
      "text" : "Well Tested"
    }, {
      "text" : "Compact"
    } ],
    "cons" : [ {
      "text" : "Poor Documentation"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 10,
  "guid" : "23aa2f6b-1842-43d8-910f-f547f4696c0d",
  "name" : "Common Map API Javascript Library",
  "description" : "Core Map API is a library of JavaScript (JS) functions that hide the underlying Common Map Widget API so that developers only have to include the JS library and call the appropriate JS functions that way they are kept away from managing or interacting directly with the channels.  <br> <br>Purpose/Goal of the Software: Core Map API hides the details on directly interacting with the Ozone Widget Framework (WOF) publish/subscribe channels.  It is intended to insulate applications from changes in the underlying CMWAPI as they happen. <br> <br>Additional documentation can also be found here - <a href='https://confluence.di2e.net/download/attachments/14518881/cpce-map-api-jsDocs.zip?api=v2' title='https://confluence.di2e.net/download/attachments/14518881/cpce-map-api-jsDocs.zip?api=v2' target='_blank'> cpce-map-api-jsDocs.zip?api=v2</a> <br> <br>Target Audience: Developers",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DCGS-A",
  "releaseDate" : 1362812400000,
  "version" : "1.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485055000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : 1388769330000,
    "endDate" : 1393694130000,
    "currentLevelCode" : "LEVEL1",
    "reviewedVersion" : "1.0",
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1392138930000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ {
      "name" : "Discoverable",
      "score" : 3
    }, {
      "name" : "Accessible",
      "score" : 4
    }, {
      "name" : "Documentation",
      "score" : 3
    }, {
      "name" : "Deployable",
      "score" : 0
    }, {
      "name" : "Usable",
      "score" : 1
    }, {
      "name" : "Error Handling",
      "score" : 0
    }, {
      "name" : "Integrable",
      "score" : 3
    }, {
      "name" : "I/O Validation",
      "score" : 2
    }, {
      "name" : "Testing",
      "score" : 1
    }, {
      "name" : "Monitoring",
      "score" : 5
    }, {
      "name" : "Performance",
      "score" : 0
    }, {
      "name" : "Scalability",
      "score" : 5
    }, {
      "name" : "Security",
      "score" : 3
    }, {
      "name" : "Maintainability",
      "score" : 5
    }, {
      "name" : "Community",
      "score" : 3
    }, {
      "name" : "Change Management",
      "score" : 0
    }, {
      "name" : "CA",
      "score" : 3
    }, {
      "name" : "Licensing",
      "score" : 5
    }, {
      "name" : "Roadmap",
      "score" : 3
    }, {
      "name" : "Willingness",
      "score" : 4
    }, {
      "name" : "Architecture Alignment",
      "score" : 3
    } ]
  },
  "questions" : [ {
    "question" : "Are there alternate licenses? (SAMPLE)",
    "username" : "Colby TEST",
    "userType" : "End User",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "End User",
      "answeredDate" : 1393834530000
    }, {
      "response" : "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "End User",
      "answeredDate" : 1398845730000
    } ]
  }, {
    "question" : "Does this support the 2.0 specs? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "End User",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "No,  they planned to add support next Version(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1393834530000
    }, {
      "response" : "Update: they backport support to version 1.6(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Developer",
      "answeredDate" : 1398845730000
    } ]
  }, {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "End User",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1398845730000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "End User",
      "answeredDate" : 1399961730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL1",
    "codeDescription" : "Level 1 - Initial Reuse Analysis",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "PILOT",
    "codeDescription" : "Deployment Pilot",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/download/attachments/14518881/CP-CE%20COE%20v2%20Map%20API%20Developer%20Guide.pdf?api=v2' title='https://confluence.di2e.net/download/attachments/14518881/CP-CE%20COE%20v2%20Map%20API%20Developer%20Guide.pdf?api=v2' target='_blank'> https://confluence.di2e.net/download/attachments/14518881/CP-CE%20COE%20v2%20Map%20API%20Developer%20Guide.pdf?api=v2</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/download/attachments/14518881/cpce-map-api.war?api=v2' title='https://confluence.di2e.net/download/attachments/14518881/cpce-map-api.war?api=v2' target='_blank'> https://confluence.di2e.net/download/attachments/14518881/cpce-map-api.war?api=v2</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/EMP/repos/map-api/browse/js' title='https://stash.di2e.net/projects/EMP/repos/map-api/browse/js' target='_blank'> https://stash.di2e.net/projects/EMP/repos/map-api/browse/js</a>"
  }, {
    "name" : "DI2E Framework Evaluation Report URL",
    "type" : "DI2E Framework Evaluation Report URL",
    "description" : null,
    "link" : "<a href='https://storefront.di2e.net/marketplace/public/CMAPI-JavascriptLibrary_ChecklistReport_v1.0.docx' title='https://storefront.di2e.net/marketplace/public/CMAPI-JavascriptLibrary_ChecklistReport_v1.0.docx' target='_blank'> https://storefront.di2e.net/marketplace/public/CMAPI-JavascriptLibrary_ChecklistReport_v1.0.docx</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 11,
  "guid" : "4a1c06c7-c854-43db-a41b-5318a7012412",
  "name" : "Common Map Widget API",
  "description" : "Background <br>Many programs and projects create widgets that search for or manipulate data then present the results on a map. The desire is to be able to combine data search/manipulation widgets from any provider with map widgets from other providers. In order to accomplish this, a standard way for the data search/manipulation widgets to be able to communicate with the map widget is necessary. This Application Program Interface (API) is the codification of that standard.  <br> <br>Overview <br>Using this API allows developers to focus on the problem domain rather than implementing a map widget themselves. It also allows the actual map implementation used to be chosen dynamically by the user at runtime rather than being chosen by the developer. Any map implementation that applies this API can be used. Currently, implementations using Google Earth, Google Maps V2, Google Maps V3, and OpenLayers APIs are available, and others can be written as needed. <br>Another benefit of this API is that it allows multiple widgets to collaboratively display data on a single map widget rather than forcing the user to have a separate map for each widget so the user does not have to learn a different map user interface for each widget. <br>The API uses the OZONE Widget Framework (OWF) inter-widget communication mechanism to allow client widgets to interact with the map. Messages are sent to the appropriate channels (defined below), and the map updates its state accordingly. Other widgets interested in knowing the current map state can subscribe to these messages as well. <br> <br>also available on <a href='https://software.forge.mil/sf/frs/do/viewRelease/projects.jc2cui/frs.common_map_widget.common_map_widget_api' title='https://software.forge.mil/sf/frs/do/viewRelease/projects.jc2cui/frs.common_map_widget.common_map_widget_api' target='_blank'> frs.common_map_widget.common_map...</a>",
  "parentComponent" : {
    "componentId" : 9,
    "name" : "CLAVIN"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DI2E Framework",
  "releaseDate" : 1364364000000,
  "version" : "1.1",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "System",
  "updateDts" : 1404172800000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Charting"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://www.cmapi.org/spec.html' title='http://www.cmapi.org/spec.html' target='_blank'> http://www.cmapi.org/spec.html</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='http://www.cmapi.org/spec.html' title='http://www.cmapi.org/spec.html' target='_blank'> http://www.cmapi.org/spec.html</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 12,
  "guid" : "bbf65a32-157c-4770-9cee-948bd48b3837",
  "name" : "Content Discovery and Retrieval Engine - Brokered Search Component",
  "description" : "Content Discovery and Retrieval functionality is based on the architecture, standards, and specifications being developed and maintained by the joint IC/DoD Content Discovery and Retrieval (CDR) Integrated Product Team (IPT). The components contained in this release are the 1.1 version of the reference implementation of the CDR components currently integrated within a system architecture that supports the Intelligence Community. <br> <br>The Brokered Search Component is an implementation of the capabilities and interfaces defined in the following CDR Brokered Search Service Specifications: <br><ul><li>IC/DoD CDR Brokered Search Service Specification for OpenSearch Implementations Version 1.0, 25 October 2010.</li> <br><li>IC/DoD CDR Brokered Search Service Specification for SOAP Implementations, Version 1.0, 26 October 2010.</li></ul> <br> <br>Generally speaking, the main thread of the Brokered Search Component is: <br>1.\tAccept a CDR 1.0 formatted brokered search request from a consumer <br>2.\tExtract routing criteria from the brokered search request <br>3.\tIdentify the correct search components to route the search request to based on the extracted routing criteria <br>4.\tRoute the search request to the identified search components <br>5.\tAggregate the metadata results from the separate search components into a single result stream and return the results to the consumer <br>It is important to note that a compatible service registry must be configured with Brokered Search, in order for the service to work as-implemented. Brokered Search queries the registry to get an accurate list of available endpoints.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DI2E-F",
  "releaseDate" : 1329634800000,
  "version" : "n/a",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485057000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Does this support the 2.0 specs? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "Project Manager",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "No,  they planned to add support next Version(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391447730000
    }, {
      "response" : "Update: they backport support to version 1.6(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "Developer",
      "answeredDate" : 1391537730000
    } ]
  }, {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "Developer",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Developer",
      "answeredDate" : 1393834530000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1393834530000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Charting"
  }, {
    "text" : "Data Exchange"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/satellite-icon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' title='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' target='_blank'> https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' title='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' target='_blank'> https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/Content_Discovery_and_Retrieval_Brokered_Search_Service' title='https://intellipedia.intelink.gov/wiki/Content_Discovery_and_Retrieval_Brokered_Search_Service' target='_blank'> https://intellipedia.intelink.gov/wiki/Content_Discovery_and_Retrieval_Brokered_Search_Service</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 13,
  "guid" : "ccec2c4d-fa44-487f-9b34-ac7d10ec62ba",
  "name" : "Content Discovery and Retrieval Engine - Describe Component",
  "description" : "Content Discovery and Retrieval functionality is based on the architecture, standards, and specifications being developed and maintained by the joint IC/DoD Content Discovery and Retrieval (CDR) Integrated Product Team (IPT). The components contained in this release are the 1.1 version of the reference implementation of the CDR components currently integrated within a system architecture that supports the Intelligence Community. <br> <br>The Describe Service component supports machine-to-machine interactions to provide a service consumer with a list of available search components for which a Brokered Search Service may search. <br> <br>As a result of invoking the Describe Service component, the service consumer receives a list of search components that a broker may search. In any case, the results are returned in the format of an Atom feed.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DI2E Framework",
  "releaseDate" : 1329894000000,
  "version" : "n/a",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485058000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "End User",
    "createDts" : 1367309730000,
    "updateDts" : 1367309730000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1398845730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1398845730000
    } ]
  }, {
    "question" : "Does this support the 2.0 specs? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "End User",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "No,  they planned to add support next Version(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1393834530000
    }, {
      "response" : "Update: they backport support to version 1.6(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1398845730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Data Exchange"
  }, {
    "text" : "UDOP"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/satellite-icon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' title='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' target='_blank'> https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' title='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' target='_blank'> https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 14,
  "guid" : "2a5401d9-3e8b-4c7a-a75d-273953be24e6",
  "name" : "Content Discovery and Retrieval Engine - Retrieve Component",
  "description" : "The Retrieve Components are implementations of the capabilities and interfaces defined in IC/DoD CDR Retrieve Service Specification for SOAP Implementations DRAFT Version 1.0-20100331, March 31, 2010. Each Retrieve Component is a separately deployable component that provides access to a defined content provider/content collection. The main thread of a Retrieve Component is: <br> 1. Accept a CDR 1.0 formatted retrieve request from a consumer <br> 2. Transform the CDR 1.0 retrieve request to a provider-specific retrieve request and execute the request against the provider/content collection to obtain the provider content <br> 3. Package the provider content into a CDR 1.0 formatted retrieve result and return it to the consumer <br>  <br>The ADL CDR Components include a programmer Software Development Kit (SDK). The SDK includes the framework, sample code, and test driver for developing Retrieve Components with a SOAP interface conforming to the Agency Data Layer CDR Components implementation of the CDR 1.0 Specifications. <br>  <br>Note that this is an abstract service definition and it is expected that it will be instantiated multiple times on multiple networks and in multiple systems. Each of those instances will have their own concrete service description which will include endpoint, additional security information, etc. <br>  <br>Also see the Agency Data Layer Overview in <a href='https://www.intelink.gov/inteldocs/browse.php?fFolderId=431781' title='https://www.intelink.gov/inteldocs/browse.php?fFolderId=431781' target='_blank'> browse.php?fFolderId=431781</a> for more general information about CDR.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DI2E-F",
  "releaseDate" : 1329807600000,
  "version" : "n/a",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485059000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Project Manager",
    "createDts" : 1328379330000,
    "updateDts" : 1328379330000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Developer",
      "answeredDate" : 1399961730000
    } ]
  }, {
    "question" : "Does this support the 2.0 specs? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "Project Manager",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "No,  they planned to add support next Version(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1393834530000
    }, {
      "response" : "Update: they backport support to version 1.6(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Developer",
      "answeredDate" : 1391537730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/satellite-icon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' title='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' target='_blank'> https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' title='https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov' target='_blank'> https://software.forge.mil/sf/frs/do/listReleases/projects.di2e-f/frs.agency_data_layer_content_discov</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/wiki/Content_Discovery_and_Retrieval_Retrieve_Service' title='https://www.intelink.gov/wiki/Content_Discovery_and_Retrieval_Retrieve_Service' target='_blank'> https://www.intelink.gov/wiki/Content_Discovery_and_Retrieval_Retrieve_Service</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 15,
  "guid" : "f8a744b0-39b1-4ee9-8dd4-daf5a6f931a2",
  "name" : "Cursor on Target Toolkit",
  "description" : "(U) Cursor on Target is a set of components focused on driving interoperability in message exchanges. Components include: <br>*An XML message schema <br> &nbsp;&nbsp;Basic (mandatory): what, when, where <br>&nbsp;&nbsp; Extensible (optional): add subschema to add details  <br>\u0001*A standard <br>&nbsp;&nbsp; Established as USAF standard by SECAF memo April 2007 <br>&nbsp;&nbsp; Incorporated in USAF (SAF/XC) Enterprise Architecture <br>&nbsp;&nbsp; Registered by SAF/XC in DISROnline as a USAF Organizationally Unique Standard (OUS) <br>&nbsp;&nbsp; Foundation for UCore data model <br>&nbsp;&nbsp; On the way to becoming a MIL-STD <br>\u0001*A set of software plug-ins to enable systems, including VMF and Link 16, to input and output CoT messages <br>*A CoT message router (software) to facilitate publish/subscribe message routing <br>*A simple developer's tool kit",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "Air Force",
  "releaseDate" : 1230534000000,
  "version" : "1.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485060000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/CoTSS.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/CoTIcon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/projects/cot' title='https://software.forge.mil/sf/projects/cot' target='_blank'> https://software.forge.mil/sf/projects/cot</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/projects/cot' title='https://software.forge.mil/sf/projects/cot' target='_blank'> https://software.forge.mil/sf/projects/cot</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 16,
  "guid" : "8bfc523a-591c-4fb9-a3ff-6bf96ed02c99",
  "name" : "DCGS Discovery Metadata Guide",
  "description" : "This document provides information on DCGS metadata artifacts and guidance for populating DDMS and the DIB Metacard, using DDMS Schema Extensions, and creating new DDMS extension schemas. These guidelines should be used by developers and System Integrators building resource adapters and schemas to work with DIB v2.0 or later. <br> <br>DISTRIBUTION STATEMENT C - Distribution authorized to U.S. Government Agencies and their contractors (Critical Technology)   Not ITAR restricted",
  "parentComponent" : null,
  "subComponents" : [ {
    "componentId" : 15,
    "name" : "Cursor on Target Toolkit"
  } ],
  "relatedComponents" : [ ],
  "organization" : "AFLCMC/HBBG",
  "releaseDate" : 1393830000000,
  "version" : "See site for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485060000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Are there alternate licenses? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "End User",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "End User",
      "answeredDate" : 1391537730000
    }, {
      "response" : "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "End User",
      "answeredDate" : 1393834530000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "DOC",
    "codeDescription" : "Documentation",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : null,
    "codeDescription" : null,
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://wiki.macefusion.com/display/MMT/DCGS+Discovery+Metadata+Guide' title='https://wiki.macefusion.com/display/MMT/DCGS+Discovery+Metadata+Guide' target='_blank'> https://wiki.macefusion.com/display/MMT/DCGS+Discovery+Metadata+Guide</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 17,
  "guid" : "1687ab9a-f6de-4253-9887-f9dfcece10d5",
  "name" : "DCGS Enterprise Messaging Technical Profile",
  "description" : "DCGS Enterprise Services rely on asynchronous communications for sending messages so they can notify users when certain data is published or a particular event has occurred. Users may subscriber to a data source so they can be notified when a piece of intelligence has been published on a topic of interest. Enterprise Messaging is a key capability that supports the processing of messages between Web Services that are needed for an enterprise to function efficiently. As the number of Web Services deployed across an enterprise increases, the ability to effectively send and receive messages across an enterprise becomes critical for its success. This Technical Design Document (TDD) was created by the DCGS Enterprise Focus Team (EFT) to provide guidance for Enterprise Messaging for use by the DCGS Enterprise Community at large. DCGS Service Providers will use this guidance to support the Enterprise Standards for the DCGS Enterprise.  <br> <br>Content of Enterprise Messaging (EM) CDP: <br> - Technical Design Document (TDD) <br> - Service Specification Package (SSP)  <br> - Conformance Test Kit (CTK)  <br>    - Conformance Traceability Matrix (CTM) <br>    - Test Procedure Document <br>    - Test Request Messages <br>    - Gold Data Set <br>    - Conformance Checks Scripts",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DCGS EFT",
  "releaseDate" : 1278309600000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485061000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/q30MNpu' title='https://www.intelink.gov/go/q30MNpu' target='_blank'> https://www.intelink.gov/go/q30MNpu</a>"
  } ],
  "reviews" : [ {
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 4,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1362298530000,
    "updateDate" : 1399961730000,
    "organization" : "Private Company",
    "recommend" : false,
    "pros" : [ {
      "text" : "Compact"
    }, {
      "text" : "Well Tested"
    } ],
    "cons" : [ ]
  }, {
    "username" : "Cathy TEST",
    "userType" : "End User",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 2,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1381644930000,
    "updateDate" : 1391447730000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Security Concerns"
    }, {
      "text" : "Difficult Installation"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 18,
  "guid" : "fe190601-3d24-48fd-b7ca-09ea9b5ddd5f",
  "name" : "DCGS Enterprise OWF IdAM Technical Profile",
  "description" : "The Ozone Widget Framework is a web-application engine built around the concept of discrete, reusable web application interface components called widgets. Widgets may be composed into full applications by Ozone users or administrators. <br> <br> <br>The architecture of the Ozone framework allows widgets to be implemented and deployed in remote web application servers. This leads to the possibility of an enterprise Ozone architecture, where Ozone users may access widgets from multiple providers in the enterprise. <br> <br>An enterprise Ozone architecture will require a capability that can provide identity and access management services to Ozone and widget web applications and provide a single-sign-on experience to Ozone users. <br> <br>Content of Ozone Widget Framework Identity and Access Management  CDP: <br>- Technical Design Document (TDD) <br>- Web Service Specification, Web Browser Single Sign On <br>- Conformance Test Kit (CTK)  <br>- Conformance Traceability Matrix (CTM) <br>- Test Procedure Document <br>- Test Request Messages <br>- Gold Data Set <br>- Conformance Checks Scripts",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1382940000000,
  "version" : "draft",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399487595000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/jeYvHyO' title='https://www.intelink.gov/go/jeYvHyO' target='_blank'> https://www.intelink.gov/go/jeYvHyO</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ {
    "dependency" : "Erlang",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 3.0"
  } ]
}, {
  "componentId" : 19,
  "guid" : "346042fd-1e3c-4486-93a6-f3dc9acb71c1",
  "name" : "DCGS Integration Backbone (DIB)",
  "description" : "The Distributed Common Ground/Surface System (DCGS) Integration Backbone (DIB) is the enabler for DCGS enterprise interoperability. The DIB is a technical infrastructure, the foundation for sharing data across the ISR enterprise. More specifically the DIB is: <br>1) Standards-based set of software, services, documentation and metadata schema designed to enhance interoperability of ISR <br>2) Data sharing enabler for the ISR enterprise <br>3) Reduces development costs through component sharing and reuse. <br>DIB Provides timely information, with access to all enterprise intelligence dissemination nodes, containing terabytes of data, the ability to filter data to relevant results, and supports real-time Cross Domain ISR data query and retrieval across Coalition and/or Security domains",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DMO",
  "releaseDate" : 1383548400000,
  "version" : "4.0.2",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485063000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/DIBProjectLogo.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/go/proj1145' title='https://software.forge.mil/sf/go/proj1145' target='_blank'> https://software.forge.mil/sf/go/proj1145</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/go/proj1145' title='https://software.forge.mil/sf/go/proj1145' target='_blank'> https://software.forge.mil/sf/go/proj1145</a>"
  } ],
  "reviews" : [ {
    "username" : "Dawson TEST",
    "userType" : "Project Manager",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 3,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "> 3 years",
    "lastUsed" : 1381644930000,
    "updateDate" : 1393834530000,
    "organization" : "NSA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Poorly Tested"
    } ]
  }, {
    "username" : "Abby TEST",
    "userType" : "Project Manager",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 1,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 6 Months",
    "lastUsed" : 1367309730000,
    "updateDate" : 1391537730000,
    "organization" : "DCGS Navy",
    "recommend" : false,
    "pros" : [ {
      "text" : "Well Tested"
    } ],
    "cons" : [ ]
  } ],
  "dependencies" : [ {
    "dependency" : "Ruby",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 2.0+"
  } ]
}, {
  "componentId" : 20,
  "guid" : "4cd37f95-c98e-4a83-800b-887cd972c714",
  "name" : "DCGS-E Audit Log Management and Reporting Technical Profile",
  "description" : "This CDP provides the technical design description for the Audit and Accountability capability of the Distributed Common Ground/Surface System (DCGS) Enterprise Service Dial Tone (SDT) layer. It includes the capability architecture design details, conformance requirements, and implementation guidance. <br> <br>Certain user actions, performed within a limited time period and in certain patterns, can be signs of preparation or attempts to exploit system vulnerabilities that involve privileged access.  Certain actions taken by the application server, in response to a perceived threat, are also potential signs of an attack.  Taken individually, these events are not absolute indicators and any response to them could be premature.  However, if the execution of the actions is not recorded, it becomes impossible to recognize later the pattern that confirms the occurrence of an attack.  In a Service Oriented Architecture (SOA), Web services dynamically bind to one another, making it even more difficult to recognize these patterns across Web services and determine accountability in a service chain.  Audit and Accountability is the capability that logs these events at each Web service so that these patterns can be identified, after the fact, and accountability enforced. <br> <br>In support of enterprise behavior within the DCGS Family of Systems (FoS), this technical design document was created by the DCGS Enterprise Focus Team (EFT) to provide guidance for audit and accountability of the DCGS Enterprise Service Dial Tone (SDT) layer for use by the DCGS Enterprise Community at large.  It includes the capability architecture design details, conformance requirements, and implementation guidance.  DCGS service providers will use this guidance to generate security audit logs to enforce accountability in a service chain. <br> <br>Content of Audit Logging (SOAP) CDP: <br> - Technical Design Document (TDD) <br> - Service Specification Package (SSP) for each service - Not applicable <br> - Conformance Test Kit (CTK) - Currently under development",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1330412400000,
  "version" : "Draft",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485064000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "UDOP"
  }, {
    "text" : "Charting"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/zsxM4al' title='https://www.intelink.gov/go/zsxM4al' target='_blank'> https://www.intelink.gov/go/zsxM4al</a>"
  } ],
  "reviews" : [ {
    "username" : "Dawson TEST",
    "userType" : "End User",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 2,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1362298530000,
    "updateDate" : 1398845730000,
    "organization" : "NSA",
    "recommend" : true,
    "pros" : [ {
      "text" : "Reliable"
    } ],
    "cons" : [ {
      "text" : "Poor Documentation"
    } ]
  }, {
    "username" : "Cathy TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 3,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "> 3 years",
    "lastUsed" : 1362298530000,
    "updateDate" : 1393834530000,
    "organization" : "NSA",
    "recommend" : false,
    "pros" : [ {
      "text" : "Well Tested"
    }, {
      "text" : "Open Source"
    } ],
    "cons" : [ {
      "text" : "Security Concerns"
    }, {
      "text" : "Bulky"
    } ]
  } ],
  "dependencies" : [ {
    "dependency" : "Tomcat",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 7 or 8"
  } ]
}, {
  "componentId" : 21,
  "guid" : "74f8c854-d70d-45f7-ab74-f63c27abdf8f",
  "name" : "DCGS-E Domain Name System (DNS) Technical Profile",
  "description" : "(U) This CDP provides guidance for DNS for use by the DCGS Enterprise Community at large. DCGS Service Providers will use this guidance to make their services visibility and accessibility in the DCGS Enterprise SOA (DES). Several Intelligence Community/Department of Defense (IC/DoD) documents were incorporated to support the guidance provided. DNS is important to the DCGS Community because it supports visibility and accessibility of services hosted on their servers. <br> <br>(U//FOUO) This CDP provides a technical design description for the Domain Name System of the Distributed Common Ground/Surface System (DCGS) Enterprise Service Dial Tone (SDT).  It includes the service architecture design details, conformance requirements, and implementation guidance. <br> <br>(U//FOUO) Domain names are meaningful identification labels for Internet addresses. The Domain Name System capability translates domain names into the numerical identifiers associated with networking equipment for the purpose of locating and addressing these devices across the globe. The Domain Name System capability makes it possible to assign domain names to groups of Internet users in a meaningful way, independent of each user's physical location. Because of this, World Wide Web (WWW) hyperlinks and Internet contact information can remain consistent and constant even if the current Internet routing arrangements change or the participant uses a mobile device.  The Domain Name System (DNS) is the industry standard for domain name translation and will be utilized across the DCGS Enterprise as the DCGS Enterprise Domain Name System solution.   <br> <br>(U//FOUO) In support of enterprise behavior within the DCGS Family of Systems (FoS), this technical design document was created by the DCGS Enterprise Focus Team (EFT) to provide guidance for the use of the Domain Name System capability by the DCGS Enterprise Community at large. DCGS service providers will use this guidance to make their services visible and accessible in the DCGS Enterprise SOA (DES).  <br> <br>Content of Domain Name System (DNS) CDP: <br> - Technical Design Document (TDD) <br> - Service Specification Package (SSP)  <br> - Conformance Test Kit (CTK)  <br>    - Conformance Traceability Matrix (CTM) <br>    - Test Procedure Document <br>    - Test Request Messages - N/A (DNS is a network infrastructure service) <br>    - Gold Data Set - N/A (DNS is a network infrastructure service)  <br>    - Conformance Checks Scripts - N/A (DNS is a network infrastructure service)",
  "parentComponent" : {
    "componentId" : 19,
    "name" : "DCGS Integration Backbone (DIB)"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DCGS EFT",
  "releaseDate" : 1287640800000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485064000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "Project Manager",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "End User",
      "answeredDate" : 1391537730000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "End User",
      "answeredDate" : 1391537730000
    } ]
  }, {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "End User",
      "answeredDate" : 1398845730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/J9qBGN9' title='https://www.intelink.gov/go/J9qBGN9' target='_blank'> https://www.intelink.gov/go/J9qBGN9</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 22,
  "guid" : "50ababb2-08e3-4f2a-98a5-068b261d9424",
  "name" : "DCGS-E Metrics Management Technical Profile",
  "description" : "**Previously titled Service Level Agreement/Quality of Service CDP** <br> <br>(U) This CDP provides guidance for Metrics Management for use by the DCGS Enterprise Community at large. DCGS Service Providers will use this guidance to make their services visibility and accessibility in the DCGS Enterprise SOA. Several Intelligence Community/Department of Defense (IC/DoD) documents were incorporated to support the guidance provided. <br> <br>(U//FOUO) Enterprise Management refers to the management techniques, metrics and related tools that DCGS programs of record can use to support their nodes and make strategic decisions to maintain the overall health of their services. The Metrics Management service family is a key capability that supports Enterprise Management. It is used to monitor the performance and operational status of services.  <br> <br>(U//FOUO) Metrics Management measures what is actually delivered to the service consumer via a set of metrics (e.g. service performance and availability). As the number of service offerings deployed across an enterprise increases, the ability to effectively manage and monitor them becomes critical to ensure a successful implementation. Clearly defined metrics need to be collected and reported to determine if the service offerings are meeting their users? needs.   <br> <br>(U//FOUO) In support of enterprise behavior within the DCGS Family of Systems (FoS), this technical design document was created by the DCGS Enterprise Focus Team (EFT) to provide guidance on metrics, service events, and service interfaces needed by the DCGS Enterprise Community at large to support Metrics Management.  DCGS service providers will use this guidance to collect metrics measurements, calculate associated metrics, and report those metrics to interested parties. <br> <br>Content of Metrics Management (formerly known as QoS Management) CDP: <br> - Technical Design Document (TDD) <br> - Service Specification Package (SSP)  <br> - Conformance Test Kit (CTK)  <br>    - Conformance Traceability Matrix (CTM) <br>    - Test Procedure Document <br>    - Test Request Messages <br>    - Gold Data Set <br>    - Conformance Checks Scripts",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DCGS EFT",
  "releaseDate" : 1278396000000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485065000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/CMpInl8' title='https://www.intelink.gov/go/CMpInl8' target='_blank'> https://www.intelink.gov/go/CMpInl8</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 23,
  "guid" : "a84fe52a-c964-4be1-b0b1-e00212417817",
  "name" : "DCGS-E Sensor Web Enablement (SWE) Technical Profile",
  "description" : "(U//FOUO)  This CDP provides the technical design description for the SensorWeb capability of the Distributed Common Ground/Surface System (DCGS) Enterprise Intelligence, Surveillance and Reconnaissance (ISR) Common layer.   <br> <br>(U//FOUO)  SensorWeb is a development effort led by the Defense Intelligence Agency (DIA) National Measurement and Signature Intelligence (MASINT) Office (NMO) that aligns native sensor data output to open standard data formats in order to improve sensor data discoverability and to increase interoperability between sensor technologies, analytical tools, and Common Operating Environments (COE).  <br> <br>(U//FOUO)  SensorWeb architecture is based on the Open Geospatial Consortium (OGC) suite of Sensor Web Enablement (SWE) standards and practices: specifically, the Sensor Observation Service (SOS) Interface Standard 2.0 and the Sensor Planning Service (SPS) Interface Standard 2.0.  This TDD outlines the SOS and SPS and describes how they are used in the SensorWeb reference implementation. <br>(U//FOUO)  The SensorWeb reference implementation can be combined with other SOS servers to cover a wider range of sensor systems, or it can be used as a stand-alone to observe a limited area or slate of sensors. <br> <br>(U//FOUO)  The objective of the SensorWeb is to leverage existing data standards and apply them to MASINT sensors and sensor systems.  MASINT sensors cross a broad spectrum of collection types and techniques, including radar, sonar, directed energy weapons, and chemical, biological, radiological, and nuclear incident reporting.  SensorWeb ingests sensor data from its earliest point of transmittal and aligns that raw sensor output to established data standards.  SensorWeb outputs sensor data in Keyhole Markup Language (KML) format, making sensor data readily available in near real-time to systems that support KML, such as Google Earth. <br> <br>(U//FOUO)  SensorWeb provides unified access and control of disparate sensor data and standardized services across the ISR Enterprise, as well as delivers Command and Control (C2)/ISR Battlespace Awareness through a visualization client(s). <br>(U//FOUO)  The SensorWeb Service Oriented Architecture (SOA) ingests raw data from multiple sensor systems and then converts the data to OGC standardized schemas which allows for sensor discovery, observation, and dissemination using common, open source data exchange standards. <br> <br>(U//FOUO)  SensorWeb was established to determine a method and means for collecting and translating MASINT sensor data output and aligning that output with open standards supported by the OGC, the Worldwide Web Consortium (W3C), the International Organization for Standardization (ISO), and the Institute of Electrical and Electronics Engineers (IEEE) directives. <br> <br>(U//FOUO)  The models, encodings, and services of the SWE architecture enable implementation of interoperable and scalable service-oriented networks of heterogeneous sensor systems and client applications.  In much the same way that Hypertext Markup Language (HTML) and Hypertext Transfer Protocol (HTTP) standards enable the exchange of any type of information on the Web, the OGC's SWE initiative is focused on developing standards to enable the discovery, exchange, and processing of sensor observations, as well as the tasking of sensor systems. <br> <br>Content of Sensor Web Enablement (SWE) CDP: <br> - Technical Design Document (TDD) <br> - Service Specification Package (SSP)  <br> - Conformance Test Kit (CTK)  <br>    - Conformance Traceability Matrix (CTM) <br>    - Test Procedure Document - Under development <br>    - Test Request Messages - Under development <br>    - Gold Data Set - Under development <br>    - Conformance Checks Scripts - Under development",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1393225200000,
  "version" : "draft",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485066000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "End User",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "End User",
      "answeredDate" : 1398845730000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "UDOP"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/N4Gma4M' title='https://www.intelink.gov/go/N4Gma4M' target='_blank'> https://www.intelink.gov/go/N4Gma4M</a>"
  } ],
  "reviews" : [ {
    "username" : "Cathy TEST",
    "userType" : "Project Manager",
    "comment" : "This is a great product however, it's missing what I think are critical features.  Hopefully, they are working on it for future updates.",
    "rating" : 5,
    "title" : "Great but missing features (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1328379330000,
    "updateDate" : 1391537730000,
    "organization" : "NSA",
    "recommend" : true,
    "pros" : [ {
      "text" : "Open Source"
    }, {
      "text" : "Reliable"
    } ],
    "cons" : [ {
      "text" : "Poorly Tested"
    } ]
  }, {
    "username" : "Abby TEST",
    "userType" : "Project Manager",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 4,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1381644930000,
    "updateDate" : 1398845730000,
    "organization" : "DCGS Navy",
    "recommend" : false,
    "pros" : [ {
      "text" : "Compact"
    }, {
      "text" : "Well Tested"
    } ],
    "cons" : [ {
      "text" : "Security Concerns"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 24,
  "guid" : "8f915029-78bb-4df2-944f-f6fc4a84eeec",
  "name" : "DCGS-E Service Registration and Discovery (SRD) Technical Profile",
  "description" : "(U//FOUO) Service discovery provides DCGS Enterprise consumers the ability to locate and invoke services to support a given task or requirement in a trusted and secure operational environment. By leveraging service discovery, existing DCGS Enterprise services will be published to a root service registry, which is searchable via keyword or taxonomy. This capability provides users and machines the ability to search and discover services or business offerings. The Service Discovery capability is a foundational building block allowing the DCGS programs of record to publish and discover service offerings allowing the DCGS Enterprise Community to share and reuse information in a common, proven, and standards-based manner. <br> <br>(U//FOUO) In support of enterprise behavior within the DCGS Family of Systems (FoS), this technical design document was created by the DCGS Enterprise Focus Team (EFT) to provide guidance for service discovery for use by the DCGS Enterprise Community at large. DCGS service providers will use this guidance to make their services discoverable within the DCGS Enterprise. <br> <br>Content of Service Registration and Discovery (SRD) CDP: <br> - Technical Design Document (TDD) <br> - Service Specification Package (SSP)  <br> - Conformance Test Kit (CTK)  <br>    - Conformance Traceability Matrix (CTM) <br>    - Test Procedure Document <br>    - Test Request Messages <br>    - Gold Data Set <br>    - Conformance Checks Scripts",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DCGS EFT",
  "releaseDate" : 1278309600000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485067000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1398845730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Developer",
      "answeredDate" : 1391537730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ {
    "label" : "Provides Web Hooks via RPC service(SAMPLE)",
    "value" : "Yes"
  }, {
    "label" : "Available to public (SAMPLE)",
    "value" : "YES"
  } ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/bujBaGB' title='https://www.intelink.gov/go/bujBaGB' target='_blank'> https://www.intelink.gov/go/bujBaGB</a>"
  } ],
  "reviews" : [ {
    "username" : "Dawson TEST",
    "userType" : "End User",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 2,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 6 Months",
    "lastUsed" : 1381644930000,
    "updateDate" : 1393834530000,
    "organization" : "NGA",
    "recommend" : false,
    "pros" : [ ],
    "cons" : [ ]
  }, {
    "username" : "Cathy TEST",
    "userType" : "End User",
    "comment" : "This is a great product however, it's missing what I think are critical features.  Hopefully, they are working on it for future updates.",
    "rating" : 2,
    "title" : "Great but missing features (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1388769330000,
    "updateDate" : 1391537730000,
    "organization" : "DCGS Army",
    "recommend" : false,
    "pros" : [ {
      "text" : "Reliable"
    } ],
    "cons" : [ {
      "text" : "Poor Documentation"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 25,
  "guid" : "01724885-d2c1-4788-9aa0-ea0128fff805",
  "name" : "DCGS-E Time Synchronization (NTP) Technical Profile",
  "description" : "(U) This provides guidance for Network Time Protocol (NTP) for use by the DCGS Enterprise Community at large. DCGS Service Providers will use this guidance to make their services secure and interoperable in the DCGS Enterprise SOA <br> <br>(U//FOUO) Time synchronization is a critical service in any distributed system because it provides the frame of reference for time between all devices on the network. Time synchronization ensures that the system time on a machine located in, for example, San Francisco is the same as the time on a machine located in London before time zones are taken into account. As such, synchronized time is extremely important when performing any operations across a network. When it comes to security, it would be very hard to develop a reliable picture of an incident if logs between routers and other network devices cannot be compared successfully and accurately. Put simply, time synchronization enables security in a Net-centric environment. . <br> <br>Content of Time Synchronization (NTP) CDP: <br>- Technical Design Document (TDD) <br>- Service Specification Package (SSP)  <br>- Conformance Test Kit (CTK)  <br>- Conformance Traceability Matrix (CTM) <br>- Test Procedure Document <br>- Test Request Messages - N/A (NTP is a network infrastructure service) <br>- Gold Data Set - N/A (NTP is a network infrastructure service)  <br>- Conformance Checks Scripts - N/A (NTP is a network infrastructure service",
  "parentComponent" : {
    "componentId" : 21,
    "name" : "DCGS-E Domain Name System (DNS) Technical Profile"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DCGS EFT",
  "releaseDate" : 1341208800000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485068000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Access"
  }, {
    "text" : "Visualization"
  } ],
  "metadata" : [ {
    "label" : "Provides Web Hooks via RPC service(SAMPLE)",
    "value" : "Yes"
  }, {
    "label" : "Common Uses (SAMPLE)",
    "value" : "UDOP, Information Sharing, Research"
  }, {
    "label" : "Support Common Interface 2.1 (SAMPLE)",
    "value" : "No"
  } ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/x5UIJnv' title='https://www.intelink.gov/go/x5UIJnv' target='_blank'> https://www.intelink.gov/go/x5UIJnv</a>"
  } ],
  "reviews" : [ {
    "username" : "Cathy TEST",
    "userType" : "Project Manager",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 4,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1328379330000,
    "updateDate" : 1391447730000,
    "organization" : "DCGS Army",
    "recommend" : true,
    "pros" : [ {
      "text" : "Compact"
    } ],
    "cons" : [ ]
  } ],
  "dependencies" : [ {
    "dependency" : "Tomcat",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 7 or 8"
  } ]
}, {
  "componentId" : 26,
  "guid" : "b2866715-764f-4266-868f-64cac88556fe",
  "name" : "DCGS-E Web Service Access Control Technical Profile",
  "description" : "Access Control incorporates an open framework and industry best practices/standards that leverage Web Services Security (WSS) [17] standards, also referred to as WS-Security, to create a common framework using Attribute Based Access Control (ABAC). The DCGS Enterprise advocates an ABAC model as the desired vision to provide policy compliance and accountability throughout the entire enterprise. To enforce ABAC, identification and authentication is executed on the service consumer and access control is enforced by the service provider.  <br> <br>Identification is the process in which the identity of either a user or system is established. Authentication is the process by which an entity proves a claim regarding its identity to one or more other entities. Together, identification and authentication allows a system to securely communicate a service consumer's identity and related security attributes to a service provider. With increased sharing across programs of record and external partners, identification and authentication is crucial to ensure that services and data are secured. The successful authentication of a subject and the attributes assigned to that subject assist in the determination of whether or not a user will be allowed to access a particular service.  <br> <br>Identification and authentication joined with access control enforcement provide for the Access Control capability, which is critical for meeting Information Assurance (IA) requirements for those services targeted for the Global Information Grid (GIG). Access control is the process that allows a system to control access to resources in an information system including services and data. Services, supporting the DCGS Enterprise, will be made available to users within and between nodes. It is important that these services and their resources are adequately protected in a consistent manner across the DCGS Enterprise. Services are intended to be accessible only to authorized requesters, thus requiring mechanisms to determine the rights of an authenticated user based on their attributes and enforce access based on its security policy.",
  "parentComponent" : {
    "componentId" : 14,
    "name" : "Content Discovery and Retrieval Engine - Retrieve Component"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : null,
  "releaseDate" : 1393311600000,
  "version" : "draft",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485068000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "1.2.1",
    "codeDescription" : "Identity and Access Management",
    "important" : true
  }, {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Mapping"
  }, {
    "text" : "Communication"
  } ],
  "metadata" : [ {
    "label" : "Available to public (SAMPLE)",
    "value" : "YES"
  }, {
    "label" : "Support Common Interface 2.1 (SAMPLE)",
    "value" : "No"
  }, {
    "label" : "Provides Web Hooks via RPC service(SAMPLE)",
    "value" : "Yes"
  } ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/kcN4VyS' title='https://www.intelink.gov/go/kcN4VyS' target='_blank'> https://www.intelink.gov/go/kcN4VyS</a>"
  } ],
  "reviews" : [ {
    "username" : "Jack TEST",
    "userType" : "End User",
    "comment" : "I had issues trying to obtain the component and once I got it is very to difficult to install.",
    "rating" : 4,
    "title" : "Hassle (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1367309730000,
    "updateDate" : 1398845730000,
    "organization" : "DCGS Army",
    "recommend" : true,
    "pros" : [ {
      "text" : "Well Tested"
    } ],
    "cons" : [ ]
  } ],
  "dependencies" : [ {
    "dependency" : "Windows",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 8.1"
  } ]
}, {
  "componentId" : 27,
  "guid" : "35d44bb5-adc5-42ca-9671-c58e257570d9",
  "name" : "DI2E Framework DIAS Simulator",
  "description" : "This package provides a basic simulator of the DoDIIS Identity and Authorization Service (DIAS) in a deployable web application using Apache CFX architecture.  The DI2E Framework development team have used this when testing DIAS specific attribute access internally with Identity and Access Management functionality.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DI2E Framework",
  "releaseDate" : 1397455200000,
  "version" : "1.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200707000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "DEV",
    "codeDescription" : "Development",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "GOTS",
    "codeDescription" : "GOTS",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/IdAMLogoMed-Size.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DIAS+Simulation' title='https://confluence.di2e.net/display/DI2E/DIAS+Simulation' target='_blank'> https://confluence.di2e.net/display/DI2E/DIAS+Simulation</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DIAS+Simulation' title='https://confluence.di2e.net/display/DI2E/DIAS+Simulation' target='_blank'> https://confluence.di2e.net/display/DI2E/DIAS+Simulation</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DIAS+Simulation' title='https://confluence.di2e.net/display/DI2E/DIAS+Simulation' target='_blank'> https://confluence.di2e.net/display/DI2E/DIAS+Simulation</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ {
    "dependency" : "Linux",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Tested on CentOS 5 and Ubuntu Server 11.0"
  } ]
}, {
  "componentId" : 28,
  "guid" : "bcd54d72-3f0d-4a72-b473-2390b42515d5",
  "name" : "DI2E Framework OpenAM",
  "description" : "The DI2E Framework IdAM solution provides a core OpenAM Web Single Sign-On implementation wrapped in a Master IdAM Administration Console for ease of configuration and deployment.  Additionally, there are several enhancements to the core authentication functionality as well as external and local attribute access with support for Ozone Widget Framework implementations.  Please review the Release Notes and associated documentation for further information.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DI2E Framework",
  "releaseDate" : 1397455200000,
  "version" : "2.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "dan.stroud",
  "updateDts" : 1405308845000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "DEV",
    "codeDescription" : "Development",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "FOSS",
    "codeDescription" : "FOSS",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/IdAMLogoMed-Size.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download' title='https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download' target='_blank'> https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download' title='https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download' target='_blank'> https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download' title='https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download' target='_blank'> https://confluence.di2e.net/display/DI2E/DI2E+Framework+IdAM+Software+Download</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ {
    "dependency" : "Linux",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Tested on CentOS 5 and Ubuntu Server 11.0"
  } ]
}, {
  "componentId" : 30,
  "guid" : "3a7e6bf2-bcc4-4c1e-bc33-296cc945e733",
  "name" : "DI2E Framework Reference Architecture",
  "description" : "The DI2E Framework reference Architecture provides a structural foundation for the DI2E requirements assessment.  The DI2E Framework is specified using a level of abstraction that is not dependent on a specific technical solution, but can leverage the benefits of the latest technology advancements and make them readily accessible across the enterprise. <br> <br>The DI2E Framework architecture is modeled using DoD Architecture Framework (DoDAF) version 2.02.  Below is a list of artifacts that are currently available for the DI2E Framework Reference Architecture. <br> <br>Artifact\tName / Purpose <br>OV-5a\tOperational Activity Model Node Tree.   A breakdown of the fundamental activities performed by various actors within the DI2E Framework community.   Built from both DCGS and JIOC operational models and includes related text descriptions for each activity. <br> <br>SV-1\tComponent (System) Interfaces.   Diagrams that show the fundamental interface relationships among DI2E Framework components. <br> <br>SV-2\tComponent Resource Flows.   Extends the SV-1 diagrams by highlighting the 'data-in-motion' that passes between components along their interfaces. <br> <br>SV-4\tComponent Functionality.  A breakdown of DI2E Framework components, including a short description of their expected functionality. <br> <br>SV-5a\tActivity : Component Matrix.   A mapping showing the alignment between DI2E Framework components and activities (and vice versa). <br> <br>SV-10c\tComponent Event Tract Diagrams.   Example threads through the component architecture showcasing the interaction of various components relative to example operational scenarios. <br> <br>SvcV-3a\tComponents-Services Matrix.   A mapping showing the alignment between DI2E Framework components and services <br> <br>SvcV-4\tServices.   A breakdown of the architectures services, including service definitions, descriptions, and other relevant service metadata. <br> <br>StdV-1\tStandards Profile.   Lists the various standards and specifications that will be applicable to DI2E Framework. <br> <br>SWDS\tSoftware Description Specification.   Documents the basic structure for DI2E Framework component and service specification, then points to an EXCEL worksheet documenting the specifications & related metadata for DI2E Framework components and services. <br> <br>DIV-3\tData Model.   A list of 'data-in-motion' Data Object Types (DOTs) that are applicable to the component architecture. <br> <br>DBDS\tDatabase Design Specification.  A description of the approach and various data repository lists used to define the data model (DIV-3), along with a link to the defined data model. <br> <br>RVTM\tRequirements Verification Traceability Matrix.   A list of DI2E Framework requirements including requirement ID #s, names, descriptions, alignment with the component and service architecture, and other related metadata. <br> <br>PDS\tProject Development Specification.   A high level overview of the e DI2E Framework Reference Implementation (RI), and how the components of this RI relate with the overall component architecture and related DI2E Framework requirements. <br> <br>ICD\tInterface Control Document.  A further breakdown of the PDS (see row above), showing how RI components relate with overall component specifications as documented in the SWDS.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NRO // DI2E Framework PMO",
  "releaseDate" : 1390028400000,
  "version" : "1.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200710000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "DOC",
    "codeDescription" : "Documentation",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Reference"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/DI2E Framework RA.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DI2E+Framework+Reference+Architecture+files' title='https://confluence.di2e.net/display/DI2E/DI2E+Framework+Reference+Architecture+files' target='_blank'> https://confluence.di2e.net/display/DI2E/DI2E+Framework+Reference+Architecture+files</a>"
  }, {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://inteldocs.intelink.gov/inteldocs/page/repository#filter=path|/Group%20Folders/D/DI2E%20Framework/DI2E%20Framework%20Architecture&page=1' title='https://inteldocs.intelink.gov/inteldocs/page/repository#filter=path|/Group%20Folders/D/DI2E%20Framework/DI2E%20Framework%20Architecture&page=1' target='_blank'> https://inteldocs.intelink.gov/inteldocs/page/repository#filter=path|/Group%20Folders/D/DI2E%20Framework/DI2E%20Framework%20Architecture&page=1</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DI2E+Framework+Reference+Architecture+files' title='https://confluence.di2e.net/display/DI2E/DI2E+Framework+Reference+Architecture+files' target='_blank'> https://confluence.di2e.net/display/DI2E/DI2E+Framework+Reference+Architecture+files</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/DI2E+Reference+Architecture' title='https://confluence.di2e.net/display/DI2E/DI2E+Reference+Architecture' target='_blank'> https://confluence.di2e.net/display/DI2E/DI2E+Reference+Architecture</a>"
  } ],
  "reviews" : [ {
    "username" : "Jack TEST",
    "userType" : "End User",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 2,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 6 Months",
    "lastUsed" : 1328379330000,
    "updateDate" : 1391537730000,
    "organization" : "Private Company",
    "recommend" : false,
    "pros" : [ {
      "text" : "Well Tested"
    }, {
      "text" : "Compact"
    } ],
    "cons" : [ ]
  }, {
    "username" : "Colby TEST",
    "userType" : "Developer",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 4,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1388769330000,
    "updateDate" : 1391447730000,
    "organization" : "NSA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ ]
  }, {
    "username" : "Cathy TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 2,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1388769330000,
    "updateDate" : 1393834530000,
    "organization" : "NSA",
    "recommend" : false,
    "pros" : [ ],
    "cons" : [ ]
  } ],
  "dependencies" : [ {
    "dependency" : "Erlang",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 3.0"
  } ]
}, {
  "componentId" : 31,
  "guid" : "2f8ca98a-a935-4ebf-8504-82756b8ef81b",
  "name" : "DI2E RESTful CDR Search Technical Profile",
  "description" : "This profile provides the technical design description for the RESTful Search web service of Defense Intelligence Information Enterprise (DI2E). The profile includes capability architecture design details, implementation requirements and additional implementation guidance.  DI2E Enterprise service providers and clients will use this design to support enterprise standards while creating search services within their nodes.  <br> <br>This document extends the IC/DoD REST interface encoding specification for Content Discovery and Retrieval (CDR) Search [CDR-RS] . It defines an interface to which a search service implementation and a subsequent deployment must conform. <br> <br>The search service provides the ability to search for contents within the DI2E Enterprise with two primary functions: search and results paging. Analysts require the flexibility to search data stores in a myriad of combinations. For example, a user may perform a search for records with the keyword 'Paris' occurring between January 16, 2009 and January 21, 2009; a user may perform a search for records with the keyword 'airport' and a geo location. The previous examples highlight the three different types of queries that MUST be supported by a search service implementation:  <br>- Keyword query with results containing the specified keyword or keyword combination <br>- Temporal query with results within the specified temporal range <br>- Geographic query with results within the specified geographic area <br> <br>The search service provides a standard interface for discovering information and returns a 'hit list' of items. A search service's results are generally resource discovery metadata rather than actual content resources. In the context of search, resource discovery metadata generally refers to a subset of a resource's available metadata, not the entire underlying record. Some of the information contained within each search result may provide the information necessary for a client to retrieve or otherwise use the referenced resource. Retrieval of the product resources associated with each entry in the 'hit list' is discussed in the DI2E RESTful Retrieve profile. <br> <br>Content of DI2E RESTful CDR Search Profile: <br> - Technical Design Document (TDD) <br> - Service Specification Package (SSP) for each service - Not applicable.  Since this CDP only contains one service, contents of SSP are rolled into the TDD. <br> - Conformance Test Kit (CTK) <br>    - Conformance Traceability Matrix  <br>    - Test Procedure Document <br>    - Test Request Messages  <br>    - Gold Data Set  <br>    - Conformance Checks Scripts",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1392966000000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485073000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SPEC",
    "codeDescription" : "Standards, Specifications, and APIs",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Visualization"
  }, {
    "text" : "Access"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/go/zankYmq' title='https://www.intelink.gov/go/zankYmq' target='_blank'> https://www.intelink.gov/go/zankYmq</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ {
    "dependency" : "Windows",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 8.1"
  } ]
}, {
  "componentId" : 32,
  "guid" : "e299a3aa-585f-4bad-a4f1-009397b97b93",
  "name" : "Distributed Data Framework (DDF)",
  "description" : "DDF is a free and open source common data layer that abstracts services and business logic from the underlying data structures to enable rapid integration of new data sources.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1394002800000,
  "version" : "See Site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1405694884000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "LICTYPE",
    "typeDescription" : "License Type",
    "code" : "OPENSRC",
    "codeDescription" : "Open Source",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "FOSS",
    "codeDescription" : "FOSS",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "UDOP"
  } ],
  "metadata" : [ {
    "label" : "Extra Metadata (SAMPLE)",
    "value" : "Unfiltered"
  }, {
    "label" : "Provides Web Hooks via RPC service(SAMPLE)",
    "value" : "Yes"
  } ],
  "componentMedia" : [ {
    "link" : "images/oldsite/ddf-screenshot-lg.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/ddf-logo.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://tools.codice.org/wiki/display/DDF/DDF+Catalog+Application+Users+Guide' title='https://tools.codice.org/wiki/display/DDF/DDF+Catalog+Application+Users+Guide' target='_blank'> https://tools.codice.org/wiki/display/DDF/DDF+Catalog+Application+Users+Guide</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://ddf.codice.org' title='http://ddf.codice.org' target='_blank'> http://ddf.codice.org</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='http://ddf.codice.org' title='http://ddf.codice.org' target='_blank'> http://ddf.codice.org</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://github.com/codice/ddf' title='https://github.com/codice/ddf' target='_blank'> https://github.com/codice/ddf</a>"
  } ],
  "reviews" : [ {
    "username" : "Jack TEST",
    "userType" : "Developer",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 2,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1362298530000,
    "updateDate" : 1399961730000,
    "organization" : "NGA",
    "recommend" : false,
    "pros" : [ ],
    "cons" : [ ]
  }, {
    "username" : "Colby TEST",
    "userType" : "Developer",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 5,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1367309730000,
    "updateDate" : 1393834530000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ {
      "text" : "Compact"
    } ],
    "cons" : [ {
      "text" : "Poor Documentation"
    }, {
      "text" : "Bulky"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 57,
  "guid" : "79e51469-81b6-4cfb-b6b2-7b0be8065912",
  "name" : "Domain Name System (DNS) Guidebook for Linux/BIND",
  "description" : "This Guidebook focuses on those involved with the Domain Name System (DNS) in DI2E systems. Those building systems based on DI2E-offered components, running in a DI2E Framework. It provides guidance for two different roles - those who configure DNS, and those who rely on DNS in the development of distributed systems. <br> <br>The DNS service in both DI2E and the Distributed Common Ground/Surface System Enterprise (DCGS Enterprise) relies on the Berkeley Internet Name Domain software, commonly referred to as BIND. Requirements and policy guidance for DNS are provided in the \"Distributed Common Ground/Surface System Enterprise (DCGS Enterprise), Service Dial Tone Technical Design Document Domain Name System, Version 1.1 (Final) Jun 2012\" .   This guidebook supplements the technical profile and describes how DNS capabilities must be acquired, built and deployed by DI2E programs.",
  "parentComponent" : null,
  "subComponents" : [ {
    "componentId" : 67,
    "name" : "Central Authentication Service (CAS)"
  } ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1396677600000,
  "version" : "2.1.1",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200713000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "DOC",
    "codeDescription" : "Documentation",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://storefront.di2e.net/marketplace/public/DNS_Guidebook_for' title='https://storefront.di2e.net/marketplace/public/DNS_Guidebook_for' target='_blank'> https://storefront.di2e.net/marketplace/public/DNS_Guidebook_for</a> Linux-BIND V2.1.1.doc"
  } ],
  "reviews" : [ {
    "username" : "Colby TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 5,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "> 3 years",
    "lastUsed" : 1367309730000,
    "updateDate" : 1391447730000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ ]
  }, {
    "username" : "Abby TEST",
    "userType" : "End User",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 5,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1362298530000,
    "updateDate" : 1391537730000,
    "organization" : "NSA",
    "recommend" : false,
    "pros" : [ {
      "text" : "Active Development"
    }, {
      "text" : "Well Tested"
    } ],
    "cons" : [ {
      "text" : "Legacy system"
    } ]
  }, {
    "username" : "Jack TEST",
    "userType" : "End User",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 1,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1388769330000,
    "updateDate" : 1393834530000,
    "organization" : "NSA",
    "recommend" : false,
    "pros" : [ {
      "text" : "Open Source"
    }, {
      "text" : "Well Tested"
    } ],
    "cons" : [ {
      "text" : "Poor Documentation"
    } ]
  } ],
  "dependencies" : [ {
    "dependency" : "Linux",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Tested on CentOS 5 and Ubuntu Server 11.0"
  } ]
}, {
  "componentId" : 33,
  "guid" : "ef0eaf61-05e2-405e-934a-7c377cc61269",
  "name" : "eDIB",
  "description" : "(U) eDIB contains the eDIB 4.0 services (management VM and worker VM). eDIB 4.0 is a scalable, virtualized webservice architecture based on the DMO's DIB 4.0. eDIB provides GUIs for an administrator to manage/configure eDIB. An end-user interface is not provided. Different data stores are supported including Oracle and SOLR. <br> <br>(U) The eDIB provides an easily managed and scalable DIB deployment. The administration console allows for additional worker VMs to scale up to support additional ingest activities or additional query activities. This provides the ability to manage a dynamic workload with a minimum of resources. <br> <br>(U) The software and documentation are currently available on JWICS at GForge, or you can email the DI2E Framework team for access on unclass.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DI2E Framework",
  "releaseDate" : 1355036400000,
  "version" : "4.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1405102845000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Reference"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/eDibIcon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 34,
  "guid" : "b52b36b8-b556-4d24-a9fe-2597402c32f7",
  "name" : "Extensible Mapping Platform (EMP)",
  "description" : "The Extensible Mapping Platform (EMP) is a US Government Open Source project providing a framework for building robust OWF map widgets and map centric web applications. The EMP project is currently managed by US Army Tactical Mission Command (TMC) in partnership with DI2E, and developed by CECOM Software Engineering Center Tactical Solutions Directorate (SEC) . EMP is intended to be used and contributed to by any US Government organization that falls within the ITAR restrictions placed on the software hosted at this location. Current contributors include National Geospatial-Intelligence Agency (NGA), and ICITE-Cloud INSCOM/DCGS-A with growing interest across the defense and Intelligence communities. EMP evolved from the US Army Command Post Computing Environment (CPCE) initiative. The term CPCE is still used in many areas and there is a CPCE targeted build that is produced from EMP to meet the CPCE map widget and API requirements. The great news about EMP is that it is not limited to the CPCE specific build and feature set. EMP is designed to be customized and extended to meet the needs and schedule of any organization. <br> <br>Map Core: <br>The map core is pure HTML, CSS, and JavaScript that can be embedded in any web application. The primary target for this currently is an Ozone Widget Framework (OWF) map widget, however the core code will function outside of OWF and can be used in custom web applications. When running in OWF the map core can handle all CMWA 1.0 and 1.1 messages as well as the capabilities included in the EMP JavaScript API library. <br> <br>Additional Links <br> <a href='http://cmapi.org/' title='http://cmapi.org/' target='_blank'> </a>  <br> <a href='https://project.forge.mil/sf/frs/do/viewRelease/projects.dcgsaozone/frs.cpce_mapping_components.sprint_18_cpce_infrastructure_er' title='https://project.forge.mil/sf/frs/do/viewRelease/projects.dcgsaozone/frs.cpce_mapping_components.sprint_18_cpce_infrastructure_er' target='_blank'> frs.cpce_mapping_components.spri...</a> ",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1393830000000,
  "version" : "See site for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200714000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : 1388769330000,
    "endDate" : 1393694130000,
    "currentLevelCode" : "LEVEL1",
    "reviewedVersion" : "1.0",
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1392138930000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ {
      "name" : "Discoverable",
      "score" : 3
    }, {
      "name" : "Accessible",
      "score" : 4
    }, {
      "name" : "Documentation",
      "score" : 3
    }, {
      "name" : "Deployable",
      "score" : 4
    }, {
      "name" : "Usable",
      "score" : 5
    }, {
      "name" : "Error Handling",
      "score" : 2
    }, {
      "name" : "Integrable",
      "score" : 1
    }, {
      "name" : "I/O Validation",
      "score" : 2
    }, {
      "name" : "Testing",
      "score" : 3
    }, {
      "name" : "Monitoring",
      "score" : 2
    }, {
      "name" : "Performance",
      "score" : 0
    }, {
      "name" : "Scalability",
      "score" : 1
    }, {
      "name" : "Security",
      "score" : 4
    }, {
      "name" : "Maintainability",
      "score" : 3
    }, {
      "name" : "Community",
      "score" : 3
    }, {
      "name" : "Change Management",
      "score" : 2
    }, {
      "name" : "CA",
      "score" : 0
    }, {
      "name" : "Licensing",
      "score" : 4
    }, {
      "name" : "Roadmap",
      "score" : 0
    }, {
      "name" : "Willingness",
      "score" : 5
    }, {
      "name" : "Architecture Alignment",
      "score" : 5
    } ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "WIDGET",
    "codeDescription" : "Widget",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL1",
    "codeDescription" : "Level 1 - Initial Reuse Analysis",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "DEV",
    "codeDescription" : "Development",
    "important" : false
  }, {
    "type" : "OWFCOMP",
    "typeDescription" : "OWF Compatible",
    "code" : "Y",
    "codeDescription" : "Yes",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "GOSS",
    "codeDescription" : "GOSS",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/core-map-api.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/EMP/repos/map-docs/browse' title='https://stash.di2e.net/projects/EMP/repos/map-docs/browse' target='_blank'> https://stash.di2e.net/projects/EMP/repos/map-docs/browse</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/EMP' title='https://stash.di2e.net/projects/EMP' target='_blank'> https://stash.di2e.net/projects/EMP</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/EMP' title='https://stash.di2e.net/projects/EMP' target='_blank'> https://stash.di2e.net/projects/EMP</a>"
  }, {
    "name" : "DI2E Framework Evaluation Report URL",
    "type" : "DI2E Framework Evaluation Report URL",
    "description" : null,
    "link" : "<a href='https://storefront.di2e.net/marketplace/public/Extensible_Mapping_Platform_Evaluation_Report.docx' title='https://storefront.di2e.net/marketplace/public/Extensible_Mapping_Platform_Evaluation_Report.docx' target='_blank'> https://storefront.di2e.net/marketplace/public/Extensible_Mapping_Platform_Evaluation_Report.docx</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/EMP/Extensible+Mapping+Platform+Home' title='https://confluence.di2e.net/display/EMP/Extensible+Mapping+Platform+Home' target='_blank'> https://confluence.di2e.net/display/EMP/Extensible+Mapping+Platform+Home</a>"
  } ],
  "reviews" : [ {
    "username" : "Abby TEST",
    "userType" : "End User",
    "comment" : "It's a good product.  The features are nice and performed well.  Its quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 4,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "> 3 years",
    "lastUsed" : 1328379330000,
    "updateDate" : 1391537730000,
    "organization" : "DCGS Navy",
    "recommend" : true,
    "pros" : [ {
      "text": "Reliable"
    }, {
      "text": "Compact"
    }, {
      "text": "Well Tested"
    } ],
    "cons" : [ ]
  }, {
    "username" : "Colby TEST",
    "userType" : "Developer",
    "comment" : "This is a great product however, it's missing what I think are critical features.  Hopefully, they are working on it for future updates.",
    "rating" : 2.5,
    "title" : "Great but missing features (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1328379330000,
    "updateDate" : 1391447730000,
    "organization" : "DCGS Navy",
    "recommend" : false,
    "pros" : [  {
      "text" : "Well Tested"
    } ],
    "cons" : [ {
      "text" : "Difficult Installation"
    }, {
      "text" : "Security Concerns"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 73,
  "guid" : "9d61a682-3b5f-4756-96cd-41df323fb371",
  "name" : "GAPS - Data Catalog Service",
  "description" : "The GAPS Data Catalog Service provides a set of RESTful services to manage data sources accessible to GAPS applications.  The catalog service offers RESTful interfaces for querying sources as XML or JSON structures. The service is a combination of two web services; the root service, and the metric service.  This root service allows users to add, remove and update metadata about information sources.  The metrics service offers the ability to capture and view metrics information associated with information sources.    <br> <br> <br> <br>The Data Catalog Service provides integration with the GAPS KML Feeds to expose all of the KML sources that are registered with GAPS. Documentation states it provides the ability to integrate with OGC CSW catalog services to provide search capabilities that federate across one or more CSW catalogs.  The services are also available via a number of OWF widgets that implement the Data Catalog Service API. <br> <br>Root Service <br>SIPR: <a href='https://gapsprod1.stratcom.smil.mil/datacatalogservice/datacatalogservice.ashx' title='https://gapsprod1.stratcom.smil.mil/datacatalogservice/datacatalogservice.ashx' target='_blank'> datacatalogservice.ashx</a>  <br>JWICS:  <a href='http://jwicsgaps.usstratcom.ic.gov/datacatalogservice/datacatalogservice.ashx' title='http://jwicsgaps.usstratcom.ic.gov/datacatalogservice/datacatalogservice.ashx' target='_blank'> datacatalogservice.ashx</a>  <br> <br>Metrics Service <br>SIPR: <a href='https://gapsprod1.stratcom.smil.mil/datacatalogservice/datasourcemetrics.ashx' title='https://gapsprod1.stratcom.smil.mil/datacatalogservice/datasourcemetrics.ashx' target='_blank'> datasourcemetrics.ashx</a>  <br>JWICS:  <a href='http://jwicsgaps.usstratcom.ic.gov/datacatalogservice/datasourcemetrics.ashx' title='http://jwicsgaps.usstratcom.ic.gov/datacatalogservice/datasourcemetrics.ashx' target='_blank'> datasourcemetrics.ashx</a>  <br> <br>Overview <br>Global Awareness Presentation Services (GAPS) is a web-based command-wide service created to provide analysts and other decision makers the capability to generate Net-Centric Situational Awareness (SA) visualization products from disparate and dispersed data services.  GAPS is interoperable with ongoing Department of Defense (DoD) wide C4ISR infrastructure efforts such as FSR and SKIWEB.  GAPS attempts to share this data/information of an event in a real-time basis on SIPRNET and JWICS.  GAPS allows users at all organizational levels to define, customize, and refine their specific User Defined Operational Picture (UDOP) based on mission and task. GAPS middleware capabilities serve as the \"facilitator\" of authoritative data source information by hosting over 3,000 dynamic and static data feeds and provides metrics and status on those feeds.  GAPS provides several Ozone Widget Framework (OWF) based components that offer UDOP service functionality inside of an OWF dashboard.   <br> <br>GAPS exposes a number of core UDOP Services through SOAP/RESTful services.   <br>-GAPS Gazetteer Services <br>-GAPS Data Catalog Services <br>-GAPS Scenario Services",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "STRATCOM J8",
  "releaseDate" : null,
  "version" : "2.5",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "david.treat",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1405691893000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Cathy TEST",
    "userType" : "Developer",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "Developer",
      "answeredDate" : 1399961730000
    } ]
  }, {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "Developer",
    "createDts" : 1328379330000,
    "updateDts" : 1328379330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "End User",
      "answeredDate" : 1399961730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "End User",
      "answeredDate" : 1391537730000
    } ]
  }, {
    "question" : "Does this support the 2.0 specs? (SAMPLE)",
    "username" : "Colby TEST",
    "userType" : "Project Manager",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "No,  they planned to add support next Version(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "End User",
      "answeredDate" : 1391447730000
    }, {
      "response" : "Update: they backport support to version 1.6(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ {
    "label" : "Available to public (SAMPLE)",
    "value" : "YES"
  }, {
    "label" : "Extra Metadata (SAMPLE)",
    "value" : "Unfiltered"
  } ],
  "componentMedia" : [ {
    "link" : "images/oldsite/GAPS.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ ],
  "reviews" : [ {
    "username" : "Dawson TEST",
    "userType" : "Project Manager",
    "comment" : "I had issues trying to obtain the component and once I got it is very to difficult to install.",
    "rating" : 2,
    "title" : "Hassle (SAMPLE)",
    "usedTimeCode" : "< 6 Months",
    "lastUsed" : 1381644930000,
    "updateDate" : 1393834530000,
    "organization" : "DCGS Navy",
    "recommend" : true,
    "pros" : [ {
      "text" : "Well Tested"
    } ],
    "cons" : [ ]
  }, {
    "username" : "Jack TEST",
    "userType" : "End User",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 4,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 6 Months",
    "lastUsed" : 1381644930000,
    "updateDate" : 1391447730000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ {
      "text" : "Well Tested"
    }, {
      "text" : "Reliable"
    } ],
    "cons" : [ ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 74,
  "guid" : "2e7cb2fb-8672-4d8f-9f68-b2243b523a2f",
  "name" : "GAPS - Gazetteer Service",
  "description" : "The GAPS Gazetteer Service offers a set of geographical dictionary services that allows users to search for city and military bases, retrieving accurate latitude and longitude values for those locations.  The user may search based on name or location, with the gazetteer returning all entries that match the provided name or spatial area.  The GAPS Gazetteer Service imports gazetteer services from other data sources including NGA, USGS, DAFIF, and DISDI. <br> <br>Current Service End-Points <br>SIPR:   <br>      <a href='https://gapsprod1.stratcom.smil.mil/gazetteers/NgaGnsGazetteerService.asmx' title='https://gapsprod1.stratcom.smil.mil/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a> (NGA Gazetteer) <br>      <a href='https://gapsprod1.stratcom.smil.mil/gazetteers/UsgsGnisGazetteerService.asmx' title='https://gapsprod1.stratcom.smil.mil/gazetteers/UsgsGnisGazetteerService.asmx' target='_blank'> UsgsGnisGazetteerService.asmx</a> (USGS Gazetteer) <br> <br>JWICS:             <br>      <a href='<a href='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' title='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a>' title='<a href='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' title='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a>' target='_blank'> NgaGnsGazetteerService.asmx</a>  (USGS Gazetteer)                        <br>      <a href='<a href='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' title='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a>' title='<a href='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' title='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a>' target='_blank'> NgaGnsGazetteerService.asmx</a> (USGS Gazetteer)           <br> <br>Overview <br>Global Awareness Presentation Services (GAPS) is a web-based command-wide service created to provide analysts and other decision makers the capability to generate Net-Centric Situational Awareness (SA) visualization products from disparate and dispersed data services.  GAPS is interoperable with ongoing Department of Defense (DoD) wide C4ISR infrastructure efforts such as FSR and SKIWEB.  GAPS attempts to share this data/information of an event in a real-time basis on SIPRNET and JWICS.  GAPS allows users at all organizational levels to define, customize, and refine their specific User Defined Operational Picture (UDOP) based on mission and task. GAPS middleware capabilities serve as the \"facilitator\" of authoritative data source information by hosting over 3,000 dynamic and static data feeds and provides metrics and status on those feeds.  GAPS provides several Ozone Widget Framework (OWF) based components that offer UDOP service functionality inside of an OWF dashboard.   <br> <br>GAPS exposes a number of core UDOP Services through SOAP/RESTful services.   <br>-GAPS Gazetteer Services <br>-GAPS Data Catalog Services <br>-GAPS Scenario Services",
  "parentComponent" : {
    "componentId" : 9,
    "name" : "CLAVIN"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "STRATCOM J8",
  "releaseDate" : null,
  "version" : "2.5",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "david.treat",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1405691776000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Reference"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/GAPS.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 72,
  "guid" : "6fcf0dc4-091d-4431-a066-d074b4fd63ea",
  "name" : "GAPS - Scenario Service",
  "description" : "The GAPS Scenario Service is a SOAP based interface into the GAPS UDOP creation, execution and publishing mechanism.  The Scenario Service allows an external entity to perform Machine to Machine (M2M) calls in order to create and execute UDOPs.  This interface can be used to integrate with other systems in order to provide geospatial visualization to give contextual SA to end users. <br> <br>GAPS Scenarios are defined as data packages which are stored within the GAPS Repository. The GAPS Repository Service is a SOAP based interface into the GAPS Repository which stores and maintains UDOP scenarios and geospatial products from GAPS data sources.  The Repository Service additionally creates a series of DDMS metacards to describe all of the UDOPs and artifacts stored within the GAPS Repository.  These metacards can be further published to other metadata catalogs to facilitate discovery of GAPS UDOPs and aggregated data products. <br> <br>A Scenario consists of a UDOP template document, products generated from a UDOP template and metadata gathered from data sources during the execution of a UDOP template.   For example, a SKIWeb scenario would consist of a UDOP template that captures information about an event (location, time, description) and other data overlays to give it a spatio-temporal context, JPEG screenshots taken automatically at different view scales for the event on a globe, a movie file with animation for temporal data, a VDF file, a KML file to execute the UDOP in Google Earth, a timeline view and all of the metadata captured during the UDOP execution. <br> <br>The following are the Service Endpoints on SIPR and JWICS.  GAPS does not have an UNCLASS instance: <br>SIPR: <br>       scenario service: <a href='https://gapsprod1.stratcom.smil.mil/ScenarioService/ScenarioService.asmx' title='https://gapsprod1.stratcom.smil.mil/ScenarioService/ScenarioService.asmx' target='_blank'> ScenarioService.asmx</a> <br>       repository service: <a href='https://gapsprod1.stratcom.smil.mil/VsaPortal/RespositoryService.asmx' title='https://gapsprod1.stratcom.smil.mil/VsaPortal/RespositoryService.asmx' target='_blank'> RespositoryService.asmx</a> <br>JWICS: <br>      scenario service: <a href='http://jwicsgaps.usstratcom.ic.gov:8000/ScenarioService/ScenarioService.asmx' title='http://jwicsgaps.usstratcom.ic.gov:8000/ScenarioService/ScenarioService.asmx' target='_blank'> ScenarioService.asmx</a> <br>      repository service: <a href='http://jwicsgaps.usstratcom.ic.gov:8000/VsaPortal/RepositoryService.asmx' title='http://jwicsgaps.usstratcom.ic.gov:8000/VsaPortal/RepositoryService.asmx' target='_blank'> RepositoryService.asmx</a> <br> <br>GAPS Overview <br>Global Awareness Presentation Services (GAPS) is a web-based command-wide service created to provide analysts and other decision makers the capability to generate Net-Centric Situational Awareness (SA) visualization products from disparate and dispersed data services.  GAPS is interoperable with ongoing Department of Defense (DoD) wide C4ISR infrastructure efforts such as FSR and SKIWEB.  GAPS attempts to share this data/information of an event in a real-time basis on SIPRNET and JWICS.  GAPS allows users at all organizational levels to define, customize, and refine their specific User Defined Operational Picture (UDOP) based on mission and task. GAPS middleware capabilities serve as the \"facilitator\" of authoritative data source information by hosting over 3,000 dynamic and static data feeds and provides metrics and status on those feeds.  GAPS provides several Ozone Widget Framework (OWF) based components that offer UDOP service functionality inside of an OWF dashboard.   <br> <br>GAPS exposes a number of core UDOP Services through SOAP/RESTful services.   <br>-GAPS Gazetteer Services <br>-GAPS Data Catalog Services <br>-GAPS Scenario Services",
  "parentComponent" : {
    "componentId" : 17,
    "name" : "DCGS Enterprise Messaging Technical Profile"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "STRATCOM J8",
  "releaseDate" : null,
  "version" : "2.5",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "david.treat",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1405691317000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Charting"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/GAPS.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 35,
  "guid" : "871e8252-1a22-4677-9a3d-fdd647d5d500",
  "name" : "GVS",
  "description" : "The National Geospatial-Intelligence Agency's (NGA) GEOINT Visualization Services is a suite of web-based capabilities that delivers geospatial visualization services to the Department of Defense (DoD) and Intelligence Community (IC) via classified and unclassified computer networks to provide visualization and data access services to the war fighter, intelligence officer, and the policy-maker. More information can be found on  <br>Intelink-U: Wiki: <a href='https://www.intelink.gov/wiki/GVS' title='https://www.intelink.gov/wiki/GVS' target='_blank'> GVS</a>  <br>Blog: <a href='https://www.intelink.gov/blogs/geoweb/' title='https://www.intelink.gov/blogs/geoweb/' target='_blank'> </a> Forge.mil  <br>Dashboard GVS: <a href='https://community.forge.mil/content/geoint-visualization-services-gvs-program' title='https://community.forge.mil/content/geoint-visualization-services-gvs-program' target='_blank'> geoint-visualization-services-gv...</a>  <br>Forge.mil Dashboard GVS/PalX3: <a href='https://community.forge.mil/content/geoint-visualization-services-gvs-palanterrax3' title='https://community.forge.mil/content/geoint-visualization-services-gvs-palanterrax3' target='_blank'> geoint-visualization-services-gv...</a>",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1361084400000,
  "version" : "1",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "chris.bashioum",
  "updateDts" : 1405612927000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/800px-GVS-4ShotMerge.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/gvs.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/docman/do/listDocuments/projects.gvs' title='https://software.forge.mil/sf/docman/do/listDocuments/projects.gvs' target='_blank'> https://software.forge.mil/sf/docman/do/listDocuments/projects.gvs</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/Gvs' title='https://intellipedia.intelink.gov/wiki/Gvs' target='_blank'> https://intellipedia.intelink.gov/wiki/Gvs</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/Gvs' title='https://intellipedia.intelink.gov/wiki/Gvs' target='_blank'> https://intellipedia.intelink.gov/wiki/Gvs</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ {
    "dependency" : "Tomcat",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 7 or 8"
  } ]
}, {
  "componentId" : 58,
  "guid" : "cd86387e-08ae-4efe-b783-e8fddeb87021",
  "name" : "GVS - Base Maps - ESRi",
  "description" : "World Imagery Basemap is a global service that presents imagery from NGA holdings for Cache Scales from 1:147M - 1:282. This imagery includes various sources and resolutions spanning 15m to 75mm.  The imagery sources are Buckeye, US/CAN/MX Border Imagery, Commercial Imagery, USGS High Resolution Orthos, Rampant Lion II, NAIP, CIB, Natural Vue data.  Bathymetry data includes NOAA Coastal Relief Model and NGA DBED.  This product undergoes bimonthly updates.  <br> <br>GVS Overview:  GVS has several core categories for developers of Geospatial products.  These categories are reflected in the evaluation structure and content itself. <br>* Base Maps - Google Globe Google Maps, ArcGIS <br>* Features - MIDB, Intelink, CIDNE SIGACTS, GE shared products, WARP, NES <br>* Imagery - Commercial, WARP, NES <br>* Overlays - Streets, transportation, boundaries <br>* Discovery/Search - GeoQuery, Proximity Query, Google Earth, Globe Catalog, MapProxy, OMAR WCS <br>* Converters - GeoRSS to XML, XLS to KML, Shapefile to KML, KML to Shapefile, Coordinates <br>* Analytic Tools - Viewshed, Best Road Route <br>* Drawing Tools - Ellipse Generator, KML Styles, Custom Icons, Custom Placemarks, Mil Std 2525 Symbols <br>* Other - RSS Viewer, Network Link Generator, KML Report Creator",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NGA",
  "releaseDate" : 1401861600000,
  "version" : "See site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200715000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/gvs.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/documentation' title='https://home.gvs.nga.mil/home/documentation' target='_blank'> https://home.gvs.nga.mil/home/documentation</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/GVS' title='https://intellipedia.intelink.gov/wiki/GVS' target='_blank'> https://intellipedia.intelink.gov/wiki/GVS</a>"
  } ],
  "reviews" : [ {
    "username" : "Jack TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 5,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1367309730000,
    "updateDate" : 1399961730000,
    "organization" : "NSA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Bulky"
    }, {
      "text" : "Difficult Installation"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 59,
  "guid" : "d10e748d-a555-4661-aec9-f192e2f131cc",
  "name" : "GVS - Base Maps - Google Globe - Summary Information",
  "description" : "GVS Google Earth Globe Services are constructed from DTED, CIB, Natural View, and Commercial Imagery. Vector data includes NGA standards such as VMAP, GeoNames, and GNIS. GVS recently added NavTeq Streets and Homeland Security Infrastructure data to the Google Globe over the United States. The Globes are continuously improved. <br> <br>The GVS Google Globe Imagery can also be accessed using a WMS/WMTS client at the following URLs: <br>*WMS - <a href='https://earth.gvs.nga.mil/wms/fusion_maps_wms.cgi' title='https://earth.gvs.nga.mil/wms/fusion_maps_wms.cgi' target='_blank'> fusion_maps_wms.cgi</a> <br>*WMS - AF/PK - <a href='https://earth.gvs.nga.mil/wms/afpk_map/fusion_maps_wms.cgi' title='https://earth.gvs.nga.mil/wms/afpk_map/fusion_maps_wms.cgi' target='_blank'> fusion_maps_wms.cgi</a>    <br>*WMS - Natural View - <a href='https://earth.gvs.nga.mil/wms/nvue_map/fusion_maps_wms.cgi' title='https://earth.gvs.nga.mil/wms/nvue_map/fusion_maps_wms.cgi' target='_blank'> fusion_maps_wms.cgi</a> <br>*WMTS - <a href='https://earth.gvs.nga.mil/cgi-bin/ogc/service.py' title='https://earth.gvs.nga.mil/cgi-bin/ogc/service.py' target='_blank'> service.py</a>  <br>*WMTS OpenLayers Example <br> <br>GVS Overview:  GVS has several core categories for developers of Geospatial products.  These categories are reflected in the evaluation structure and content itself. <br>* Base Maps - Google Globe Google Maps, ArcGIS <br>* Features - MIDB, Intelink, CIDNE SIGACTS, GE shared products, WARP, NES <br>* Imagery - Commercial, WARP, NES <br>* Overlays - Streets, transportation, boundaries <br>* Discovery/Search - GeoQuery, Proximity Query, Google Earth, Globe Catalog, MapProxy, OMAR WCS <br>* Converters - GeoRSS to XML, XLS to KML, Shapefile to KML, KML to Shapefile, Coordinates <br>* Analytic Tools - Viewshed, Best Road Route <br>* Drawing Tools - Ellipse Generator, KML Styles, Custom Icons, Custom Placemarks, Mil Std 2525 Symbols <br>* Other - RSS Viewer, Network Link Generator, KML Report Creator",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NGA",
  "releaseDate" : 1401948000000,
  "version" : "See site for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200717000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/gvs.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/documentation' title='https://home.gvs.nga.mil/home/documentation' target='_blank'> https://home.gvs.nga.mil/home/documentation</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/google-earth' title='https://home.gvs.nga.mil/home/google-earth' target='_blank'> https://home.gvs.nga.mil/home/google-earth</a>"
  } ],
  "reviews" : [ {
    "username" : "Colby TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 4,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1328379330000,
    "updateDate" : 1398845730000,
    "organization" : "DCGS Army",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 60,
  "guid" : "134b6770-35f8-40fa-b11f-25e5c782d920",
  "name" : "GVS - Features - CIDNE SIGACTS",
  "description" : "The GVS CIDNE SIGACTS Data Layer provides a visualization-ready (KML 2.2 compliant formatted) result set for a query of SIGACTS, as obtained from the Combined Information Data Network Exchange (CIDNE) within a defined AOI (\"bounding box\", \"point/radius\", \"path buffer\"). <br> <br>To access the CIDNE SIGACTS Dynamic Data Layer use the base address: <br>SIPRNet: <a href='http://home.gvs.nga.smil.mil/CIDNE/SigactsServlet' title='http://home.gvs.nga.smil.mil/CIDNE/SigactsServlet' target='_blank'> SigactsServlet</a> <br>JWICs: <a href='http://home.gvs.nga.ic.gov/CIDNE/SigactsServlet' title='http://home.gvs.nga.ic.gov/CIDNE/SigactsServlet' target='_blank'> SigactsServlet</a> <br> <br>GVS Overview:  GVS has several core categories for developers of Geospatial products.  These categories are reflected in the evaluation structure and content itself. <br>* Base Maps - Google Globe Google Maps, ArcGIS <br>* Features - MIDB, Intelink, CIDNE SIGACTS, GE shared products, WARP, NES <br>* Imagery - Commercial, WARP, NES <br>* Overlays - Streets, transportation, boundaries <br>* Discovery/Search - GeoQuery, Proximity Query, Google Earth, Globe Catalog, MapProxy, OMAR WCS <br>* Converters - GeoRSS to XML, XLS to KML, Shapefile to KML, KML to Shapefile, Coordinates <br>* Analytic Tools - Viewshed, Best Road Route <br>* Drawing Tools - Ellipse Generator, KML Styles, Custom Icons, Custom Placemarks, Mil Std 2525 Symbols <br>* Other - RSS Viewer, Network Link Generator, KML Report Creator",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NGA",
  "releaseDate" : 1401948000000,
  "version" : "See site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200718000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/gvs.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/documentation' title='https://home.gvs.nga.mil/home/documentation' target='_blank'> https://home.gvs.nga.mil/home/documentation</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/capabilities/queries/cidne_sigacts' title='https://home.gvs.nga.mil/home/capabilities/queries/cidne_sigacts' target='_blank'> https://home.gvs.nga.mil/home/capabilities/queries/cidne_sigacts</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/GVS' title='https://intellipedia.intelink.gov/wiki/GVS' target='_blank'> https://intellipedia.intelink.gov/wiki/GVS</a>"
  } ],
  "reviews" : [ {
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 4,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1381644930000,
    "updateDate" : 1391537730000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 61,
  "guid" : "f1fa43e1-b2bd-4636-bc86-0b050549c26f",
  "name" : "GVS - Features - GE Shared Products",
  "description" : "GVS Shared Product Query provides you with the ability to display geospatial information layers in a Google Earth client depicting data previously created and stored in the GVS Shared Product Buffer by other users. <br> <br>Home page: <a href='https://home.gvs.nga.mil/UPS/RSS' title='https://home.gvs.nga.mil/UPS/RSS' target='_blank'> RSS</a> for the manual query <a href='https://home.gvs.nga.mil/home/capabilities/queries/shared_products' title='https://home.gvs.nga.mil/home/capabilities/queries/shared_products' target='_blank'> shared_products</a> for the query tool <br>Wiki: A general GVS Wiki can be found here <a href='https://intellipedia.intelink.gov/wiki/GVS' title='https://intellipedia.intelink.gov/wiki/GVS' target='_blank'> GVS</a> no specialized shared product wiki pages exist <br> <br>GVS Overview:  GVS has several core categories for developers of Geospatial products.  These categories are reflected in the evaluation structure and content itself. <br>* Base Maps - Google Globe Google Maps, ArcGIS <br>* Features - MIDB, Intelink, CIDNE SIGACTS, GE shared products, WARP, NES <br>* Imagery - Commercial, WARP, NES <br>* Overlays - Streets, transportation, boundaries <br>* Discovery/Search - GeoQuery, Proximity Query, Google Earth, Globe Catalog, MapProxy, OMAR WCS <br>* Converters - GeoRSS to XML, XLS to KML, Shapefile to KML, KML to Shapefile, Coordinates <br>* Analytic Tools - Viewshed, Best Road Route <br>* Drawing Tools - Ellipse Generator, KML Styles, Custom Icons, Custom Placemarks, Mil Std 2525 Symbols <br>* Other - RSS Viewer, Network Link Generator, KML Report Creator",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NGA",
  "releaseDate" : 1401861600000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200719000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/gvs.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/documentation' title='https://home.gvs.nga.mil/home/documentation' target='_blank'> https://home.gvs.nga.mil/home/documentation</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/capabilities/queries/shared_products' title='https://home.gvs.nga.mil/home/capabilities/queries/shared_products' target='_blank'> https://home.gvs.nga.mil/home/capabilities/queries/shared_products</a> or  <a href='https://intellipedia.intelink.gov/wiki/GVS' title='https://intellipedia.intelink.gov/wiki/GVS' target='_blank'> https://intellipedia.intelink.gov/wiki/GVS</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ {
    "dependency" : "Linux",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Tested on CentOS 5 and Ubuntu Server 11.0"
  } ]
}, {
  "componentId" : 62,
  "guid" : "aa40dec7-efc9-4b4c-b29a-8eb51876e135",
  "name" : "GVS - Features - Intelink",
  "description" : "GVS - Features - Intelink: Provides the capability to perform a temporal keyword search for news items, Intellipedia data, and intelligence reporting and finished products found on Intelink. A list of geo-referenced locations in response to a query based on a filter. <br> <br>GVS INTELINK GEO SEARCH WFS INTERFACE (https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf) <br> <br>Interface Details: <br>* Access Protocol: HTTP WFS calls with URL-encoded parameters <br>* Base Address: <br>JWICS: <a href='http://home.gvs.nga.ic.gov/metacarta/wfs' title='http://home.gvs.nga.ic.gov/metacarta/wfs' target='_blank'> wfs</a> <br>SIPRNet: <a href='http://home.gvs.nga.smil.mil/metacarta/wfs' title='http://home.gvs.nga.smil.mil/metacarta/wfs' target='_blank'> wfs</a> <br> <br>GVS Overview:  GVS has several core categories for developers of Geospatial products.  These categories are reflected in the evaluation structure and content itself. <br>* Base Maps - Google Globe Google Maps, ArcGIS <br>* Features - MIDB, Intelink, CIDNE SIGACTS, GE shared products, WARP, NES <br>* Imagery - Commercial, WARP, NES <br>* Overlays - Streets, transportation, boundaries <br>* Discovery/Search - GeoQuery, Proximity Query, Google Earth, Globe Catalog, MapProxy, OMAR WCS <br>* Converters - GeoRSS to XML, XLS to KML, Shapefile to KML, KML to Shapefile, Coordinates <br>* Analytic Tools - Viewshed, Best Road Route <br>* Drawing Tools - Ellipse Generator, KML Styles, Custom Icons, Custom Placemarks, Mil Std 2525 Symbols <br>* Other - RSS Viewer, Network Link Generator, KML Report Creator",
  "parentComponent" : {
    "componentId" : 28,
    "name" : "DI2E Framework OpenAM"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NGA",
  "releaseDate" : 1401861600000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200721000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/gvs.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/documentation' title='https://home.gvs.nga.mil/home/documentation' target='_blank'> https://home.gvs.nga.mil/home/documentation</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' title='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' target='_blank'> https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/GVS' title='https://intellipedia.intelink.gov/wiki/GVS' target='_blank'> https://intellipedia.intelink.gov/wiki/GVS</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 63,
  "guid" : "25dc2698-a99c-4f4e-b8c1-4e4734f0fbf5",
  "name" : "GVS - Features - MIDB",
  "description" : "GVS - Features - MIDB allows the user to query MIDB (Equipment) data within a defined AOI.  User takes HTTP-Get parameters (Listed below the summary table and in the documentation) and builds a query. <br> <br>In the exposed Service Interfaces document MIDB services are covered in section 6.1 which is approximately page 73 in the current guide. <br> <br>Service URI <br>JWICS: <a href='http://home.gvs.nga.ic.gov/MIDB/wfs' title='http://home.gvs.nga.ic.gov/MIDB/wfs' target='_blank'> wfs</a>  <br>SIPRNet: <a href='http://home.gvs.nga.smil.mil/MIDB/wfs' title='http://home.gvs.nga.smil.mil/MIDB/wfs' target='_blank'> wfs</a> <br> <br>GVS Documentation page <br>NIPRNet: <a href='<a href='https://home.gvs.nga.smil.mil/home/documentation' title='https://home.gvs.nga.smil.mil/home/documentation' target='_blank'> documentation</a>' title='<a href='https://home.gvs.nga.smil.mil/home/documentation' title='https://home.gvs.nga.smil.mil/home/documentation' target='_blank'> documentation</a>' target='_blank'> documentation</a>  <br>SIPRNet: <a href='<a href='https://home.gvs.nga.smil.mil/home/documentation' title='https://home.gvs.nga.smil.mil/home/documentation' target='_blank'> documentation</a>' title='<a href='https://home.gvs.nga.smil.mil/home/documentation' title='https://home.gvs.nga.smil.mil/home/documentation' target='_blank'> documentation</a>' target='_blank'> documentation</a>  <br>JWICs: <a href='https://home.gvs.nga.ic.gov/home/documentation' title='https://home.gvs.nga.ic.gov/home/documentation' target='_blank'> documentation</a>  <br> <br>Exposed Service Interface Guide  <br>NIPRNet: <a href='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' title='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' target='_blank'> GVS_Exposed_Service_Interfaces.pdf</a>  <br>SIPRNet: <a href='https://home.gvs.nga.smil.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' title='https://home.gvs.nga.smil.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' target='_blank'> GVS_Exposed_Service_Interfaces.pdf</a>  <br>JWICs: <a href='https://home.gvs.ic.gov/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' title='https://home.gvs.ic.gov/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' target='_blank'> GVS_Exposed_Service_Interfaces.pdf</a>  <br> <br>GVS Quick Reference Guide  <br>NIPRNet: <a href='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' title='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' target='_blank'> GVS_Consolidated_QRG_Book_Reduce...</a>  <br>SIPRNet: <a href='https://home.gvs.nga.smil.mil/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' title='https://home.gvs.nga.smil.mil/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' target='_blank'> GVS_Consolidated_QRG_Book_Reduce...</a>  <br>JWICs: <a href='https://home.gvs.nga.ic.gov/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' title='https://home.gvs.nga.ic.gov/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' target='_blank'> GVS_Consolidated_QRG_Book_Reduce...</a>  <br> <br> <br>GVS Overview:  GVS has several core categories for developers of Geospatial products.  These categories are reflected in the evaluation structure and content itself. <br>* Base Maps - Google Globe Google Maps, ArcGIS <br>* Features - MIDB, Intelink, CIDNE SIGACTS, GE shared products, WARP, NES <br>* Imagery - Commercial, WARP, NES <br>* Overlays - Streets, transportation, boundaries <br>* Discovery/Search - GeoQuery, Proximity Query, Google Earth, Globe Catalog, MapProxy, OMAR WCS <br>* Converters - GeoRSS to XML, XLS to KML, Shapefile to KML, KML to Shapefile, Coordinates <br>* Analytic Tools - Viewshed, Best Road Route <br>* Drawing Tools - Ellipse Generator, KML Styles, Custom Icons, Custom Placemarks, Mil Std 2525 Symbols <br>* Other - RSS Viewer, Network Link Generator, KML Report Creator",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NGA",
  "releaseDate" : 1402120800000,
  "version" : "see site for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200722000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Colby TEST",
    "userType" : "End User",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "End User",
      "answeredDate" : 1391447730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391447730000
    } ]
  }, {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "Project Manager",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1393834530000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Access"
  }, {
    "text" : "Data Exchange"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/gvs.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/documentation' title='https://home.gvs.nga.mil/home/documentation' target='_blank'> https://home.gvs.nga.mil/home/documentation</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/go/proj1769' title='https://software.forge.mil/sf/go/proj1769' target='_blank'> https://software.forge.mil/sf/go/proj1769</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/GVS' title='https://intellipedia.intelink.gov/wiki/GVS' target='_blank'> https://intellipedia.intelink.gov/wiki/GVS</a>"
  } ],
  "reviews" : [ {
    "username" : "Jack TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 3,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1381644930000,
    "updateDate" : 1391447730000,
    "organization" : "Private Company",
    "recommend" : false,
    "pros" : [ {
      "text" : "Well Tested"
    } ],
    "cons" : [ ]
  }, {
    "username" : "Dawson TEST",
    "userType" : "End User",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 1,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1362298530000,
    "updateDate" : 1391537730000,
    "organization" : "DCGS Army",
    "recommend" : true,
    "pros" : [ {
      "text" : "Compact"
    } ],
    "cons" : [ {
      "text" : "Security Concerns"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 64,
  "guid" : "cfb2449d-86fe-4792-ada8-e5d965046026",
  "name" : "GVS - Features - NES",
  "description" : "GVS - Features - NES provides a visualization-ready (KML 2.2 compliant formatted) result set for a query of the targets obtained from the NGA National Exploitation System (NES), within a defined AOI (\"bounding box\", \"point/radius\", \"path buffer\").. Note, NES is only available on JWICS. <br> <br>Interface Details:  <br>* Access Protocol: HTTP GET or POST with URL-encoded parameters <br>* Base Address: <br>JWICS: <a href='http://home.gvs.nga.ic.gov/NESQuery/CoverageQuery' title='http://home.gvs.nga.ic.gov/NESQuery/CoverageQuery' target='_blank'> CoverageQuery</a> <br> <br> <br>GVS Overview:  GVS has several core categories for developers of Geospatial products.  These categories are reflected in the evaluation structure and content itself. <br>* Base Maps - Google Globe Google Maps, ArcGIS <br>* Features - MIDB, Intelink, CIDNE SIGACTS, GE shared products, WARP, NES <br>* Imagery - Commercial, WARP, NES <br>* Overlays - Streets, transportation, boundaries <br>* Discovery/Search - GeoQuery, Proximity Query, Google Earth, Globe Catalog, MapProxy, OMAR WCS <br>* Converters - GeoRSS to XML, XLS to KML, Shapefile to KML, KML to Shapefile, Coordinates <br>* Analytic Tools - Viewshed, Best Road Route <br>* Drawing Tools - Ellipse Generator, KML Styles, Custom Icons, Custom Placemarks, Mil Std 2525 Symbols <br>* Other - RSS Viewer, Network Link Generator, KML Report Creator",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NGA",
  "releaseDate" : 1402207200000,
  "version" : "see site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200723000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Cathy TEST",
    "userType" : "Developer",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "End User",
      "answeredDate" : 1398845730000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "End User",
      "answeredDate" : 1399961730000
    } ]
  }, {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Colby TEST",
    "userType" : "Project Manager",
    "createDts" : 1367309730000,
    "updateDts" : 1367309730000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Developer",
      "answeredDate" : 1399961730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SERVICE",
    "codeDescription" : "Service Endpoint",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Access"
  }, {
    "text" : "Charting"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/gvs.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/home/documentation' title='https://home.gvs.nga.mil/home/documentation' target='_blank'> https://home.gvs.nga.mil/home/documentation</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' title='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' target='_blank'> https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/GVS' title='https://intellipedia.intelink.gov/wiki/GVS' target='_blank'> https://intellipedia.intelink.gov/wiki/GVS</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 65,
  "guid" : "fe3041eb-1e40-4203-987b-e6622695f55b",
  "name" : "HardwareWall",
  "description" : "The Boeing eXMeritus HardwareWall* is a highly configurable, commercial-off-the-shelf cross domain solution that enables rapid, automated, and secure data transfer between security domains: <br> <br>eXMeritus has designed HardwareWall* as a secure data transfer system and an off-the-shelf Controlled Interface that meets and exceeds all mission and information assurance requirements for the world's highest-level security directives.  Our systems have been deployed and certified in PL-3, PL-4 and PL-5 environments and continue to operate and evolve to meet ever changing requirements and threats. <br> <br>Links: <br> <a href='http://www.exmeritus.com/hardware_wall.html' title='http://www.exmeritus.com/hardware_wall.html' target='_blank'> hardware_wall.html</a>  <br> <a href='http://www.boeing.com/advertising/c4isr/isr/exmeritus_harware_wall.html' title='http://www.boeing.com/advertising/c4isr/isr/exmeritus_harware_wall.html' target='_blank'> exmeritus_harware_wall.html</a>  <br> <br>HardwareWall* Applications <br>*Files <br>     **High to low or low to high <br>     **Automated format and content review <br>     **Digital signatures <br>     **External utilities (e.g. virus scanning) <br> <br>*Streaming data <br>     **High to low or low to high <br>     **Automated format and content review <br>     **Signed records or messages <br> <br>HardwareWall* Capacity <br>     **Current installation moving large files at over 85MB/s           <br>     **Multiple methods to achieve multi-Gigabyte per second throughput: <br>     **Scales by replication <br>     **Scales by CPUs and interfaces <br>     **Demonstrated ability to stripe across multiple Gigabit Ethernet fibers",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "Boeing",
  "releaseDate" : 1402812000000,
  "version" : "See Site",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "patrick.cross",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200725000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "Y",
    "codeDescription" : "Yes",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "COTS",
    "codeDescription" : "COTS",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/eXMeritus.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://www.exmeritus.com/hardware_wall.html' title='http://www.exmeritus.com/hardware_wall.html' target='_blank'> http://www.exmeritus.com/hardware_wall.html</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://www.exmeritus.com/hardware_wall.html' title='http://www.exmeritus.com/hardware_wall.html' target='_blank'> http://www.exmeritus.com/hardware_wall.html</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='http://www.exmeritus.com/hardware_wall.html' title='http://www.exmeritus.com/hardware_wall.html' target='_blank'> http://www.exmeritus.com/hardware_wall.html</a>"
  } ],
  "reviews" : [ {
    "username" : "Colby TEST",
    "userType" : "Project Manager",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 1,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1388769330000,
    "updateDate" : 1391537730000,
    "organization" : "DCGS Army",
    "recommend" : true,
    "pros" : [ {
      "text" : "Open Source"
    }, {
      "text" : "Reliable"
    } ],
    "cons" : [ {
      "text" : "Poor Documentation"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 36,
  "guid" : "7268ddbc-ad59-4c8c-b205-96d81d4615e4",
  "name" : "IC AppsMall Cookbook: Applications Development",
  "description" : "The Applications Development Cookbook is designed as an overview of principles and best practices for Web Application Development, with a focus on new web and related technologies & concepts that take full advantage of modern browser capabilities. The ideas introduced in this document are not intended to be all inclusive, instead, they are met to expose the reader to concepts and examples that can be adapted and applied to a broad array of development projects and tangential tasks. The content is structured so that a reader with basic technical skills can garner a high-level understanding of the concepts. Where applicable, more detailed information has been included, or identified, to allow further examination of individual areas.  <br> <br>This initial document is structured around four subject areas* JavaScript, User Experience (UX), HTML5, and Elastic Scalability. Each section provides a description, and then introduces key concepts[1].  <br> <br>JavaScript - this section provides a concise review of the main impediments developers with a few years of experience struggle with when using JavaScript. The resources section directs less experienced users to references that will teach them basic JavaScript.  <br> <br>User Experience (UX) : this section provides a general overview of what UX is, as well as why and how it should, and can, be applied to application development projects. The content in this section has broad applicability, as it can inform decisions across a large spectrum* from aiding developers in designing more appealing applications to assisting managers in assuring they have marketable products.  <br> <br>HTML5 : the HTML5 portion of the cookbook provides developers unfamiliar with the proposed standards of the newest iteration of Hypertext Markup Language an introduction to key concepts. The section includes simple exercises to demonstrate utility.  <br> <br>Elastic Scalability : the Elastic Scalability section provides an introduction to architecture principles that can help ensure an infrastructure is capable of meeting a modern web browser's demands in accomplishing tasks traditionally held in the realm of the thick client.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "IC SOG",
  "releaseDate" : 1360047600000,
  "version" : "NA",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485077000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "Project Manager",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391447730000
    } ]
  }, {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "Developer",
    "createDts" : 1328379330000,
    "updateDts" : 1328379330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1398845730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "End User",
      "answeredDate" : 1393834530000
    } ]
  }, {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Project Manager",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "End User",
      "answeredDate" : 1393834530000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1398845730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "DOC",
    "codeDescription" : "Documentation",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/AppsMall_Cookbook:_Applications_Development' title='https://intellipedia.intelink.gov/wiki/AppsMall_Cookbook:_Applications_Development' target='_blank'> https://intellipedia.intelink.gov/wiki/AppsMall_Cookbook:_Applications_Development</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://intellipedia.intelink.gov/wiki/AppsMall_Cookbook:_Applications_Development' title='https://intellipedia.intelink.gov/wiki/AppsMall_Cookbook:_Applications_Development' target='_blank'> https://intellipedia.intelink.gov/wiki/AppsMall_Cookbook:_Applications_Development</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ {
    "dependency" : "Tomcat",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 7 or 8"
  } ]
}, {
  "componentId" : 37,
  "guid" : "dcb2db8f-fc5d-4db2-8e1b-cd1459cb34be",
  "name" : "ISF Enterprise Data Viewer Widget",
  "description" : "A widget designed to display search results in a tabular format.  It can page, sort, filter, and group results and organize items into working folders called \"Workspaces\" as well as perform full-record retrieval for supported result types.  It depends on the Persistence Service to store and retrieve results and optionally uses the Map widget to visualize results geospatially. <br> <br>This was previously entered in the storefront as the Enterprise Search Results Widget.",
  "parentComponent" : {
    "componentId" : 28,
    "name" : "DI2E Framework OpenAM"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NRO/GED",
  "releaseDate" : 1348380000000,
  "version" : "See links for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485078000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Are there alternate licenses? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1393834530000
    }, {
      "response" : "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1399961730000
    } ]
  }, {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "Project Manager",
    "createDts" : 1367309730000,
    "updateDts" : 1367309730000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "End User",
      "answeredDate" : 1393834530000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "End User",
      "answeredDate" : 1391447730000
    } ]
  }, {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "End User",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Developer",
      "answeredDate" : 1398845730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "WIDGET",
    "codeDescription" : "Widget",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  }, {
    "type" : "OWFCOMP",
    "typeDescription" : "OWF Compatible",
    "code" : "Y",
    "codeDescription" : "Yes",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "GOTS",
    "codeDescription" : "GOTS",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ {
    "label" : "Extra Metadata (SAMPLE)",
    "value" : "Unfiltered"
  }, {
    "label" : "Support Common Interface 2.1 (SAMPLE)",
    "value" : "No"
  }, {
    "label" : "Common Uses (SAMPLE)",
    "value" : "UDOP, Information Sharing, Research"
  } ],
  "componentMedia" : [ {
    "link" : "images/oldsite/grid.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/ISF/repos/data-viewer/browse' title='https://stash.di2e.net/projects/ISF/repos/data-viewer/browse' target='_blank'> https://stash.di2e.net/projects/ISF/repos/data-viewer/browse</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/ISF/repos/data-viewer/browse' title='https://stash.di2e.net/projects/ISF/repos/data-viewer/browse' target='_blank'> https://stash.di2e.net/projects/ISF/repos/data-viewer/browse</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/ISF/repos/data-viewer/browse' title='https://stash.di2e.net/projects/ISF/repos/data-viewer/browse' target='_blank'> https://stash.di2e.net/projects/ISF/repos/data-viewer/browse</a>"
  } ],
  "reviews" : [ {
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 3,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 6 Months",
    "lastUsed" : 1367309730000,
    "updateDate" : 1391447730000,
    "organization" : "DCGS Army",
    "recommend" : true,
    "pros" : [ {
      "text" : "Active Development"
    } ],
    "cons" : [ {
      "text" : "Bulky"
    }, {
      "text" : "Legacy system"
    } ]
  }, {
    "username" : "Colby TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 5,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "> 3 years",
    "lastUsed" : 1328379330000,
    "updateDate" : 1393834530000,
    "organization" : "DCGS Army",
    "recommend" : false,
    "pros" : [  {
      "text" : "Well Tested"
    } ],
    "cons" : [ ]
  }, {
    "username" : "Jack TEST",
    "userType" : "Developer",
    "comment" : "I had issues trying to obtain the component and once I got it is very to difficult to install.",
    "rating" : 1,
    "title" : "Hassle (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1388769330000,
    "updateDate" : 1399961730000,
    "organization" : "Private Company",
    "recommend" : false,
    "pros" : [ {
      "text" : "Active Development"
    }, {
      "text" : "Open Source"
    } ],
    "cons" : [ ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 38,
  "guid" : "247533a9-3109-4edd-a1a8-52fdc5bd516e",
  "name" : "ISF Persistence Service",
  "description" : "A RESTful service that persists JSON documents.  It is designed to have a swappable backend and currently supports MongoDB and in-memory implementations.",
  "parentComponent" : null,
  "subComponents" : [ {
    "componentId" : 19,
    "name" : "DCGS Integration Backbone (DIB)"
  } ],
  "relatedComponents" : [ ],
  "organization" : "NRO/GED",
  "releaseDate" : 1329462000000,
  "version" : "See links for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485079000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "GOTS",
    "codeDescription" : "GOTS",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/Database-Icon.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/ISF/repos/persistence-service/browse' title='https://stash.di2e.net/projects/ISF/repos/persistence-service/browse' target='_blank'> https://stash.di2e.net/projects/ISF/repos/persistence-service/browse</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/ISF/repos/persistence-service/browse' title='https://stash.di2e.net/projects/ISF/repos/persistence-service/browse' target='_blank'> https://stash.di2e.net/projects/ISF/repos/persistence-service/browse</a>"
  } ],
  "reviews" : [ {
    "username" : "Abby TEST",
    "userType" : "End User",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 1,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1367309730000,
    "updateDate" : 1393834530000,
    "organization" : "NSA",
    "recommend" : true,
    "pros" : [ {
      "text" : "Active Development"
    } ],
    "cons" : [ {
      "text" : "Poor Documentation"
    }, {
      "text" : "Difficult Installation"
    } ]
  }, {
    "username" : "Dawson TEST",
    "userType" : "End User",
    "comment" : "I had issues trying to obtain the component and once I got it is very to difficult to install.",
    "rating" : 5,
    "title" : "Hassle (SAMPLE)",
    "usedTimeCode" : "< 1 year",
    "lastUsed" : 1328379330000,
    "updateDate" : 1391537730000,
    "organization" : "DCGS Navy",
    "recommend" : false,
    "pros" : [ {
      "text" : "Open Source"
    } ],
    "cons" : [ {
      "text" : "Bulky"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 39,
  "guid" : "e9433496-106f-48bc-9510-3e9a792f949a",
  "name" : "ISF Search Criteria Widget",
  "description" : "A widget dedicated to providing search criteria to a compatible CDR backend.  It depends on the Persistence Service as a place to put retrieved metadata results.  It also optionally depends on the Map widget to interactively define geospatial queries (a text-based option is also available) and to render results.  A table of results is provided by the Data Viewer widget.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NRO/GED",
  "releaseDate" : 1348380000000,
  "version" : "See links for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485080000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "WIDGET",
    "codeDescription" : "Widget",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  }, {
    "type" : "OWFCOMP",
    "typeDescription" : "OWF Compatible",
    "code" : "Y",
    "codeDescription" : "Yes",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "GOTS",
    "codeDescription" : "GOTS",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/EntSearchWidget.PNG",
    "contentType" : "image/PNG",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/EntSearchWidgetStatus.PNG",
    "contentType" : "image/PNG",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/Search.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/ISF/repos/search-criteria/browse' title='https://stash.di2e.net/projects/ISF/repos/search-criteria/browse' target='_blank'> https://stash.di2e.net/projects/ISF/repos/search-criteria/browse</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/ISF/repos/search-criteria/browse' title='https://stash.di2e.net/projects/ISF/repos/search-criteria/browse' target='_blank'> https://stash.di2e.net/projects/ISF/repos/search-criteria/browse</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/ISF/repos/search-criteria/browse' title='https://stash.di2e.net/projects/ISF/repos/search-criteria/browse' target='_blank'> https://stash.di2e.net/projects/ISF/repos/search-criteria/browse</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 40,
  "guid" : "c1289f76-8704-41f4-8fa3-4b88c9cd0c3b",
  "name" : "iSpatial",
  "description" : "iSpatial is a commercially available geospatial framework designed as a set of ready-to-customize, baseline tools that can be rapidly adapted to meet use cases calling for geo-visualization.  iSpatial consists of four major areas of core functionality:  Authorizing, Searching, Managing and Collaborating.  iSpatial's APIs and software libraries can be accessed by developers to customize off the existing framework. It is a front-end browser-based interface developed in JavaScript and ExtJS, a collection of REST services that connect the user interface to the back end, and a PostgreSQL/PostGIS and MongoDB back end. <br> <br>  <br> <br>The iSpatial Core Services stack has four main components:  the Message Queue (MQ), an Authentication Proxy Service (TAPS), Common Data Access Service (CDAS) , and Agent Manager (AM). <br> <br>iSpatial White Paper: <a href='http://www.t-sciences.com/wp-content/uploads/2013/04/iSpatial_v3_Technical_White_Paper.pdf' title='http://www.t-sciences.com/wp-content/uploads/2013/04/iSpatial_v3_Technical_White_Paper.pdf' target='_blank'> iSpatial_v3_Technical_White_Pape...</a> <br>iSpatial <a href='http://www.t-sciences.com/wp-content/uploads/2014/01/iSpatial_Fed.pptx' title='http://www.t-sciences.com/wp-content/uploads/2014/01/iSpatial_Fed.pptx' target='_blank'> iSpatial_Fed.pptx</a>",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "Thermopylae Sciences and Technology",
  "releaseDate" : 1395813600000,
  "version" : "3.2.4",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200726000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.1",
    "codeDescription" : "2.1 Collaboration",
    "important" : false
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "COTS",
    "codeDescription" : "COTS",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/iSpatialLogosquare.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://www.t-sciences.com/product/ispatial' title='http://www.t-sciences.com/product/ispatial' target='_blank'> http://www.t-sciences.com/product/ispatial</a>"
  }, {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://therml-pf-app02.pf.cee-w.net/static/docs/iSpatial_v3_User_Guide.pdf' title='http://therml-pf-app02.pf.cee-w.net/static/docs/iSpatial_v3_User_Guide.pdf' target='_blank'> http://therml-pf-app02.pf.cee-w.net/static/docs/iSpatial_v3_User_Guide.pdf</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://www.t-sciences.com/product/ispatial' title='http://www.t-sciences.com/product/ispatial' target='_blank'> http://www.t-sciences.com/product/ispatial</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='http://www.t-sciences.com/product/ispatial' title='http://www.t-sciences.com/product/ispatial' target='_blank'> http://www.t-sciences.com/product/ispatial</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 41,
  "guid" : "96c32c04-852d-4795-b659-235cfd0fdb05",
  "name" : "JC2CUI Common 2D Map API Widget",
  "description" : "This is a map widget developed by JC2CUI that conforms to the Common Map API - see below for more information on the API. <br> <br>Using this API allows developers to focus on the problem domain rather than implementing a map widget themselves. It also allows the actual map implementation used to be chosen dynamically by the user at runtime rather than being chosen by the developer. Any map implementation that applies this API can be used. Currently, implementations using Google Earth, Google Maps V2, Google Maps V3, and OpenLayers APIs are available, and others can be written as needed. <br> Another benefit of this API is that it allows multiple widgets to collaboratively display data on a single map widget rather than forcing the user to have a separate map for each widget, so the user does not have to struggle with a different map user interface for each widget. The API uses the OZONE Widget Framework (OWF) inter-widget communication mechanism to allow client widgets to interact with the map. Messages are sent to the appropriate channels (defined below), and the map updates its state accordingly. Other widgets interested in knowing the current map state can subscribe to these messages as well. It is worth noting that the map itself may publish data to these channels on occasion. For example, a map.feature.selected message may originate from a widget asking that a particular feature be selected or because a user has selected the feature on the map. While in most instances the map will not echo back another message to confirm that it has performed an operation, the map will send a view status message whenever the map view (zoom/pan) has been changed, either directly by the user or due to a view change message sent by another widget. This allows non-map widgets that wish to be aware of the current viewport to have that information without having to process all the various messages that can change its state.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "JC2CUI",
  "releaseDate" : 1342850400000,
  "version" : "1.3",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485082000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Developer",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391537730000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "End User",
      "answeredDate" : 1391537730000
    } ]
  }, {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Cathy TEST",
    "userType" : "Developer",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "End User",
      "answeredDate" : 1391447730000
    } ]
  }, {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Colby TEST",
    "userType" : "Project Manager",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Developer",
      "answeredDate" : 1391537730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391537730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "WIDGET",
    "codeDescription" : "Widget",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  }, {
    "type" : "OWFCOMP",
    "typeDescription" : "OWF Compatible",
    "code" : "Y",
    "codeDescription" : "Yes",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/EntMapWidget.PNG",
    "contentType" : "image/PNG",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/EntMapWidgetNoDraw.PNG",
    "contentType" : "image/PNG",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/maps-icon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.intelink.gov/inteldocs/browse.php?fFolderId=431871' title='https://www.intelink.gov/inteldocs/browse.php?fFolderId=431871' target='_blank'> https://www.intelink.gov/inteldocs/browse.php?fFolderId=431871</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/frs/do/listReleases/projects.jc2cui/frs.common_map_widget' title='https://software.forge.mil/sf/frs/do/listReleases/projects.jc2cui/frs.common_map_widget' target='_blank'> https://software.forge.mil/sf/frs/do/listReleases/projects.jc2cui/frs.common_map_widget</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/frs/do/listReleases/projects.jc2cui/frs.common_map_widget' title='https://software.forge.mil/sf/frs/do/listReleases/projects.jc2cui/frs.common_map_widget' target='_blank'> https://software.forge.mil/sf/frs/do/listReleases/projects.jc2cui/frs.common_map_widget</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 42,
  "guid" : "a2a4488f-a139-41a2-b455-a1b9ea87f7c8",
  "name" : "JView",
  "description" : "JView is a Java-based API (application programmer's interface) that was developed to reduce the time, cost, & effort associated with the creation of computer visualization applications. <br> <br> <br> <br>JView provides the programmer with the ability to quickly develop 2-dimensional and 3-dimensional visualization applications that are tailored to address a specific problem. <br> <br>The JView API is government-owned and available free of charge to government agencies and their contractors. <br> <br>JView License information:  Full release of JView is currently being made available to government agencies and their contractors. This release includes, source code, sample JView-based applications, sample models and terrain, and documentation. A limited release that does not include source code is available to universities and foreign governments. All releases are subject to the approval of the JView program office. One can request a copy of JView by visiting <a href='https://extranet.rl.af.mil/jview/.' title='https://extranet.rl.af.mil/jview/.' target='_blank'> .</a> Formal configuration management and distribution of JView is performed through the Information Management Services program.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "AFRL",
  "releaseDate" : 1310796000000,
  "version" : "1.7.1",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1405370213000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : 1388769330000,
    "endDate" : 1393694130000,
    "currentLevelCode" : "LEVEL1",
    "reviewedVersion" : "1.0",
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1392138930000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ {
      "name" : "Discoverable",
      "score" : 2
    }, {
      "name" : "Accessible",
      "score" : 2
    }, {
      "name" : "Documentation",
      "score" : 3
    }, {
      "name" : "Deployable",
      "score" : 4
    }, {
      "name" : "Usable",
      "score" : 5
    }, {
      "name" : "Error Handling",
      "score" : 2
    }, {
      "name" : "Integrable",
      "score" : 4
    }, {
      "name" : "I/O Validation",
      "score" : 2
    }, {
      "name" : "Testing",
      "score" : 2
    }, {
      "name" : "Monitoring",
      "score" : 1
    }, {
      "name" : "Performance",
      "score" : 3
    }, {
      "name" : "Scalability",
      "score" : 2
    }, {
      "name" : "Security",
      "score" : 3
    }, {
      "name" : "Maintainability",
      "score" : 2
    }, {
      "name" : "Community",
      "score" : 1
    }, {
      "name" : "Change Management",
      "score" : 2
    }, {
      "name" : "CA",
      "score" : 0
    }, {
      "name" : "Licensing",
      "score" : 2
    }, {
      "name" : "Roadmap",
      "score" : 1
    }, {
      "name" : "Willingness",
      "score" : 2
    }, {
      "name" : "Architecture Alignment",
      "score" : 5
    } ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL1",
    "codeDescription" : "Level 1 - Initial Reuse Analysis",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "GOTS",
    "codeDescription" : "GOTS",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/JView+Screen+Shot+Shader+Demo.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/JView+Screen+Shot+screencap_large_f22.jpg",
    "contentType" : "image/jpg",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/JView_Logo_133_81[1].png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/scm/do/listRepositories/projects.jview/scm' title='https://software.forge.mil/sf/scm/do/listRepositories/projects.jview/scm' target='_blank'> https://software.forge.mil/sf/scm/do/listRepositories/projects.jview/scm</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/scm/do/listRepositories/projects.jview/scm' title='https://software.forge.mil/sf/scm/do/listRepositories/projects.jview/scm' target='_blank'> https://software.forge.mil/sf/scm/do/listRepositories/projects.jview/scm</a>"
  }, {
    "name" : "DI2E Framework Evaluation Report URL",
    "type" : "DI2E Framework Evaluation Report URL",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/JView+API+Evaluation' title='https://confluence.di2e.net/display/DI2E/JView+API+Evaluation' target='_blank'> https://confluence.di2e.net/display/DI2E/JView+API+Evaluation</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://extranet.rl.af.mil/jview/' title='https://extranet.rl.af.mil/jview/' target='_blank'> https://extranet.rl.af.mil/jview/</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://software.forge.mil/sf/go/proj1195' title='https://software.forge.mil/sf/go/proj1195' target='_blank'> https://software.forge.mil/sf/go/proj1195</a>"
  } ],
  "reviews" : [ {
    "username" : "Cathy TEST",
    "userType" : "End User",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 1,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "> 3 years",
    "lastUsed" : 1328379330000,
    "updateDate" : 1391447730000,
    "organization" : "DCGS Navy",
    "recommend" : false,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Poorly Tested"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 43,
  "guid" : "20dd2ed3-491f-4b8c-99a9-33029c308182",
  "name" : "Military Symbology Renderer",
  "description" : "The Mil Symbology Renderer is both a developer's toolkit as well as a ready to use deployable web application. <br>The goal of this project is to provide a single distributable solution that can support as many use cases as possible <br>for military symbology rendering. The current features available are: <br>* Use in web applications using the JavaScript library and SECRenderer applet <br>* Use with thin Ozone Widget Framework with the Rendering widget and API <br>* Use as a REST web service (Currently supports icon based symbols only) <br>* Use of underlying rendering jar files in an application or service",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1358924400000,
  "version" : "1.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485084000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "WIDGET",
    "codeDescription" : "Widget",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  }, {
    "type" : "OWFCOMP",
    "typeDescription" : "OWF Compatible",
    "code" : "Y",
    "codeDescription" : "Yes",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/maps-icon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://github.com/missioncommand/mil-sym-java/blob/master/documentation/Mil%20Symbology%20Web%20Service%20Developer's%20Guide.docx' title='https://github.com/missioncommand/mil-sym-java/blob/master/documentation/Mil%20Symbology%20Web%20Service%20Developer's%20Guide.docx' target='_blank'> https://github.com/missioncommand/mil-sym-java/blob/master/documentation/Mil%20Symbology%20Web%20Service%20Developer's%20Guide.docx</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://github.com/missioncommand' title='https://github.com/missioncommand' target='_blank'> https://github.com/missioncommand</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://github.com/missioncommand' title='https://github.com/missioncommand' target='_blank'> https://github.com/missioncommand</a>"
  } ],
  "reviews" : [ {
    "username" : "Jack TEST",
    "userType" : "Project Manager",
    "comment" : "I had issues trying to obtain the component and once I got it is very to difficult to install.",
    "rating" : 1,
    "title" : "Hassle (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1367309730000,
    "updateDate" : 1398845730000,
    "organization" : "NSA",
    "recommend" : false,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Difficult Installation"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 44,
  "guid" : "f303606d-5b76-4ebe-beca-cec20c3791e1",
  "name" : "OpenAM",
  "description" : "OpenAM is an all-in-one access management platform with the adaptive intelligence to protect against risk-based threats across any environment.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "DI2E-F",
  "releaseDate" : 1345701600000,
  "version" : "See site for latest versions",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200729000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "1.2.1",
    "codeDescription" : "Identity and Access Management",
    "important" : true
  }, {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : null,
    "codeDescription" : null,
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/security.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://forgerock.com/products/open-identity-stack/openam/' title='http://forgerock.com/products/open-identity-stack/openam/' target='_blank'> http://forgerock.com/products/open-identity-stack/openam/</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://forgerock.com/products/open-identity-stack/openam/' title='http://forgerock.com/products/open-identity-stack/openam/' target='_blank'> http://forgerock.com/products/open-identity-stack/openam/</a>"
  } ],
  "reviews" : [ {
    "username" : "Cathy TEST",
    "userType" : "End User",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 1,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "< 6 Months",
    "lastUsed" : 1362298530000,
    "updateDate" : 1393834530000,
    "organization" : "NGA",
    "recommend" : false,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Poorly Tested"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 45,
  "guid" : "7c4aa579-64c7-4b35-859d-7b2922ab0efb",
  "name" : "OpenSextant",
  "description" : "(U) OpenSextant is an open source software package for geotagging unstructured text. OpenSextant is implemented in Java and based on the open source text analytic software GATE ( <a href='http://gate.ac.uk/' title='http://gate.ac.uk/' target='_blank'> </a> ). <br>(U) OpenSextant can geotag documents in any file format supported by GATE, which includes plain text, Microsoft Word, PDF, and HTML. Multiple files can submitted in a compressed archive. OpenSextant can unpack both .zip and .tar archives, as well as tarball archives with .gz or .tgz extensions. The newer .zipx format is not currently supported.  <br>(U) Geospatial information is written out in commonly used geospatial data formats, such as KML, ESRI Shapefile, CSV, JSON or WKT. Details on how OpenSextant works and output formats it supports can be found in the document Introduction to OpenSextant. <br>OpenSextant can be run either as a standalone application or as a web service <br>(U) OpenSextant identifies and disambiguates geospatial information in unstructured text. Geospatial information includes named places as well as explicit spatial coordinates such as latitude-longitude pairs and Military Grid References.  Named places can be any geospatial feature, such as countries, cities, rivers, and so on.  Disambiguation refers to matching a named place in a document with one or more entries in a gazetteer and providing a relative confidence level for each match.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NGA",
  "releaseDate" : 1367042400000,
  "version" : "1.3.1",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485085000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ {
    "label" : "Common Uses (SAMPLE)",
    "value" : "UDOP, Information Sharing, Research"
  }, {
    "label" : "Support Common Interface 2.1 (SAMPLE)",
    "value" : "No"
  } ],
  "componentMedia" : [ {
    "link" : "images/oldsite/OpenSextantSS.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/OpenSextanticon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://github.com/OpenSextant/opensextant/tree/master/OpenSextantToolbox/doc' title='https://github.com/OpenSextant/opensextant/tree/master/OpenSextantToolbox/doc' target='_blank'> https://github.com/OpenSextant/opensextant/tree/master/OpenSextantToolbox/doc</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://github.com/OpenSextant/opensextant' title='https://github.com/OpenSextant/opensextant' target='_blank'> https://github.com/OpenSextant/opensextant</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://github.com/OpenSextant/opensextant' title='https://github.com/OpenSextant/opensextant' target='_blank'> https://github.com/OpenSextant/opensextant</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 46,
  "guid" : "66bca318-5cca-45e4-a207-688ad923ede8",
  "name" : "OpenStack",
  "description" : "OpenStack is open source software for building public and private clouds ( <a href='http://www.openstack.org' title='http://www.openstack.org' target='_blank'> www.openstack.org</a> ). The release here has been developed by USC/ISI and has two distinctions from the mainstream open source release.  First, we've focused on support for high-performance, heterogeneous hardware.  Second, we've packaged a release for deployment in secure environments.  The release has been developed for RHEL and related host operating systems, includes an SELinux policy, and includes all of the dependent packages, so the release can be deployed stand-alone in environments not connected to the internet.  The release uses a modified version of the open source Rackspace Private Cloud software for easy installation.",
  "parentComponent" : {
    "componentId" : 45,
    "name" : "OpenSextant"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "USC/ISI",
  "releaseDate" : 1386831600000,
  "version" : "NA, see listing for latest",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485086000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "Project Manager",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1398845730000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "End User",
      "answeredDate" : 1398845730000
    } ]
  }, {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Project Manager",
    "createDts" : 1367309730000,
    "updateDts" : 1367309730000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "End User",
      "answeredDate" : 1391447730000
    } ]
  }, {
    "question" : "Are there alternate licenses? (SAMPLE)",
    "username" : "Cathy TEST",
    "userType" : "Project Manager",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    }, {
      "response" : "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1399961730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "PILOT",
    "codeDescription" : "Deployment Pilot",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/OpenStack.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/Heterogeneous+OpenStack+Grizzly+Release' title='https://confluence.di2e.net/display/DI2E/Heterogeneous+OpenStack+Grizzly+Release' target='_blank'> https://confluence.di2e.net/display/DI2E/Heterogeneous+OpenStack+Grizzly+Release</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/Heterogeneous+OpenStack+Grizzly+Release' title='https://confluence.di2e.net/display/DI2E/Heterogeneous+OpenStack+Grizzly+Release' target='_blank'> https://confluence.di2e.net/display/DI2E/Heterogeneous+OpenStack+Grizzly+Release</a>"
  } ],
  "reviews" : [ {
    "username" : "Abby TEST",
    "userType" : "End User",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 4,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 1 year",
    "lastUsed" : 1381644930000,
    "updateDate" : 1393834530000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ {
      "text" : "Well Tested"
    }, {
      "text" : "Active Development"
    } ],
    "cons" : [ {
      "text" : "Difficult Installation"
    } ]
  }, {
    "username" : "Colby TEST",
    "userType" : "End User",
    "comment" : "I had issues trying to obtain the component and once I got it is very to difficult to install.",
    "rating" : 2,
    "title" : "Hassle (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1367309730000,
    "updateDate" : 1398845730000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ ]
  }, {
    "username" : "Cathy TEST",
    "userType" : "End User",
    "comment" : "This is a great product however, it's missing what I think are critical features.  Hopefully, they are working on it for future updates.",
    "rating" : 2,
    "title" : "Great but missing features (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1388769330000,
    "updateDate" : 1393834530000,
    "organization" : "NSA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Security Concerns"
    }, {
      "text" : "Poor Documentation"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 47,
  "guid" : "638e11e6-39c5-44b3-8168-af7cc1469f15",
  "name" : "OpenStack Lessons Learned Document",
  "description" : "Lessons learned in the Openstack Folsom deployment by USC/ISI in the DI2E Framework environment.  This document is meant to be very specific to one deployment experience with the intention that it will be useful to others deploying in a similar environment. <br> <br>Note this document is stored on the DI2E Framework Wiki which requires a DI2E Framework Devtools account ( <a href='https://devtools.di2e.net/' title='https://devtools.di2e.net/' target='_blank'> </a> )",
  "parentComponent" : {
    "componentId" : 31,
    "name" : "DI2E RESTful CDR Search Technical Profile"
  },
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1386226800000,
  "version" : "1.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485087000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Jack TEST",
    "userType" : "Developer",
    "createDts" : 1328379330000,
    "updateDts" : 1328379330000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Developer",
      "answeredDate" : 1399961730000
    } ]
  }, {
    "question" : "Are there alternate licenses? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "End User",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Developer",
      "answeredDate" : 1393834530000
    }, {
      "response" : "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Developer",
      "answeredDate" : 1398845730000
    } ]
  }, {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "Developer",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1399961730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "DOC",
    "codeDescription" : "Documentation",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/OpenStack.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/download/attachments/8847504/OpenStack-LessonsLearned.pdf?api=v2' title='https://confluence.di2e.net/download/attachments/8847504/OpenStack-LessonsLearned.pdf?api=v2' target='_blank'> https://confluence.di2e.net/download/attachments/8847504/OpenStack-LessonsLearned.pdf?api=v2</a>"
  } ],
  "reviews" : [ {
    "username" : "Cathy TEST",
    "userType" : "Project Manager",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 4,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 3 years",
    "lastUsed" : 1381644930000,
    "updateDate" : 1398845730000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ {
      "text" : "Reliable"
    }, {
      "text" : "Open Source"
    } ],
    "cons" : [ {
      "text" : "Poorly Tested"
    } ]
  }, {
    "username" : "Jack TEST",
    "userType" : "Developer",
    "comment" : "This is a great product however, it's missing what I think are critical features.  Hopefully, they are working on it for future updates.",
    "rating" : 2,
    "title" : "Great but missing features (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1388769330000,
    "updateDate" : 1398845730000,
    "organization" : "NGA",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ ]
  }, {
    "username" : "Colby TEST",
    "userType" : "End User",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 2,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1381644930000,
    "updateDate" : 1398845730000,
    "organization" : "DCGS Navy",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Security Concerns"
    }, {
      "text" : "Poorly Tested"
    } ]
  } ],
  "dependencies" : [ ]
}, {
  "componentId" : 48,
  "guid" : "5cb546ca-40de-4744-962d-56cfa9e1cbab",
  "name" : "OWASP Enterprise Security API",
  "description" : "ESAPI (The OWASP Enterprise Security API) is a free, open source, web application security control library that makes it easier for programmers to write lower-risk applications. The ESAPI libraries are designed to make it easier for programmers to retrofit security into existing applications. The ESAPI libraries also serve as a solid foundation for new development. <br> <br>Allowing for language-specific differences, all OWASP ESAPI versions have the same basic design: <br>* There is a set of security control interfaces. They define for example types of parameters that are passed to types of security controls. <br>* There is a reference implementation for each security control. The logic is not organization-specific and the logic is not application-specific. An example: string-based input validation. <br>* There are optionally your own implementations for each security control. There may be application logic contained in these classes which may be developed by or for your organization. An example: enterprise authentication. <br> <br>This project source code is licensed under the BSD license, which is very permissive and about as close to public domain as is possible. The project documentation is licensed under the Creative Commons license. You can use or modify ESAPI however you want, even include it in commercial products. <br> <br>The following organizations are a few of the many organizations that are starting to adopt ESAPI to secure their web applications: American Express, Apache Foundation, Booz Allen Hamilton, Aspect Security, Coraid, The Hartford, Infinite Campus, Lockheed Martin, MITRE, U.S. Navy - SPAWAR, The World Bank, SANS Institute. <br> <br>The guide is produced by the Open Web Application Security Project (OWASP) - <a href='https://www.owasp.org.' title='https://www.owasp.org.' target='_blank'> www.owasp.org.</a>",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "OWASP",
  "releaseDate" : 1121839200000,
  "version" : "2.10",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485088000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "SOFTWARE",
    "codeDescription" : "Software",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Access"
  }, {
    "text" : "Testing"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/OWASPAPIScreenShot.PNG",
    "contentType" : "image/PNG",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/ologo.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.owasp.org/index.php/Esapi' title='https://www.owasp.org/index.php/Esapi' target='_blank'> https://www.owasp.org/index.php/Esapi</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.owasp.org/index.php/Esapi#tab=Downloads' title='https://www.owasp.org/index.php/Esapi#tab=Downloads' target='_blank'> https://www.owasp.org/index.php/Esapi#tab=Downloads</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='https://www.owasp.org/index.php/Esapi' title='https://www.owasp.org/index.php/Esapi' target='_blank'> https://www.owasp.org/index.php/Esapi</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 49,
  "guid" : "f2a509c0-419f-489e-844d-e783be5f5fe2",
  "name" : "OWASP Web Application Security Guide",
  "description" : "The Development Guide is aimed at architects, developers, consultants and auditors and is a comprehensive manual for designing, developing and deploying secure Web Applications and Web Services. The original OWASP Development Guide has become a staple diet for many web security professionals. Since 2002, the initial version was downloaded over 2 million times. Today, the Development Guide is referenced by many leading government, financial, and corporate standards and is the Gold standard for Web Application and Web Service security. <br>The guide is produced by the Open Web Application Security Project (OWASP) - <a href='https://www.owasp.org.' title='https://www.owasp.org.' target='_blank'> www.owasp.org.</a>",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "OWASP",
  "releaseDate" : 1122098400000,
  "version" : "2.01",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485088000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "DOC",
    "codeDescription" : "Documentation",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : null,
    "codeDescription" : null,
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Charting"
  }, {
    "text" : "Visualization"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/OWASPGuideScreenShot.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/ologo.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.owasp.org/index.php/Category:OWASP_Guide_Project' title='https://www.owasp.org/index.php/Category:OWASP_Guide_Project' target='_blank'> https://www.owasp.org/index.php/Category:OWASP_Guide_Project</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.owasp.org/index.php/Category:OWASP_Guide_Project' title='https://www.owasp.org/index.php/Category:OWASP_Guide_Project' target='_blank'> https://www.owasp.org/index.php/Category:OWASP_Guide_Project</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 50,
  "guid" : "0a1931ac-611d-4d82-a83e-4a48ae51ec7a",
  "name" : "Ozone Marketplace",
  "description" : "The Ozone marketplace is a storefront to store widgets, services, and web applications. It can be linked to the Ozone Widget Framework.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "OWF Goss",
  "releaseDate" : 1349935200000,
  "version" : "5.0.1",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "CROSS.PATRICK.I.1126309879",
  "updateDts" : 1399485089000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "UDOP"
  }, {
    "text" : "Communication"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/OMP.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/OMPIcon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.owfgoss.org/downloads/Marketplace/5.0/Marketplace-5.0.1-GA/' title='https://www.owfgoss.org/downloads/Marketplace/5.0/Marketplace-5.0.1-GA/' target='_blank'> https://www.owfgoss.org/downloads/Marketplace/5.0/Marketplace-5.0.1-GA/</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.owfgoss.org/downloads/Marketplace/5.0/Marketplace-5.0.1-GA/' title='https://www.owfgoss.org/downloads/Marketplace/5.0/Marketplace-5.0.1-GA/' target='_blank'> https://www.owfgoss.org/downloads/Marketplace/5.0/Marketplace-5.0.1-GA/</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 51,
  "guid" : "3dd46fe2-8926-45ee-a4dc-63057c030a9e",
  "name" : "Ozone Widget Framework",
  "description" : "OWF is a web application that allows users to easily access all their online tools from one location. Not only can users access websites and applications with widgets, they can group them and configure some applications to interact with each other via widget intents. <br> <br> <br> <br> <br> <br>Some Links: <br>http://www.ozoneplatform.org/ (mirror site to below) <br>https://www.owfgoss.org/ (mirror site to above)",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "OWF GOSS",
  "releaseDate" : 1356246000000,
  "version" : "7.0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1402947991000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "End User",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391447730000
    } ]
  }, {
    "question" : "Are there alternate licenses? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "End User",
    "createDts" : 1328379330000,
    "updateDts" : 1328379330000,
    "responses" : [ {
      "response" : "You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1398845730000
    }, {
      "response" : "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)",
      "username" : "Colby TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391447730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  }, {
    "type" : "LICTYPE",
    "typeDescription" : "License Type",
    "code" : "GOVUNL",
    "codeDescription" : "Government Unlimited Rights",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Communication"
  } ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/launch-menu.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/graph.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  }, {
    "link" : "images/oldsite/Ozone-Banner_30x110.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Cathy TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.owfgoss.org/' title='https://www.owfgoss.org/' target='_blank'> https://www.owfgoss.org/</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://www.owfgoss.org/' title='https://www.owfgoss.org/' target='_blank'> https://www.owfgoss.org/</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://www.owfgoss.org/' title='https://www.owfgoss.org/' target='_blank'> https://www.owfgoss.org/</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
}, {
  "componentId" : 53,
  "guid" : "68e0403b-41d0-48fc-bd46-8f5a6fc8d83d",
  "name" : "VANTAGE Software Suite",
  "description" : "VANTAGE is a tactical imagery exploitation software suite. This field proven application screens digital imagery from various tactical reconnaissance sensors and includes simultaneous support for multiple sensor types. VANTAGE provides for real and near real time screening of digital image data from a live data link, solid state recorder, DVD/CD, or local disk. In addition, VANTAGE provides a full featured database that can be used to quickly find imagery based on an area (or point) of interest, a time query, or sensor type. <br> <br> <br>This software suite allows the image analyst to receive, decompress/compress, process, display, evaluate, exploit, and store imagery data; as well as create/disseminate processed image products. VANTAGE was developed by SDL in support of various sensors such as SPIRITT, ATARS/APG-73, LSRS, ASARS-2A, SYERS-2, and Global Hawk and provides simultaneous support for multiple sensor types.  <br> <br>The VANTAGE software suite includes the following applications: <br> <br>VANTAGE ViewPoint <br>     *Provides rapid display and exploitation of tactical imagery <br>     *Displays geo-referenced digital tactical imagery in a robust, near real-time waterfall of decimated imagery <br>     *Provides on-demand display of full-resolution imagery with exploitation tools <br> <br>VANTAGE Ascent  <br>     *Configures ground stations for device interface management (solid-state recorders, CDL interface, STANAG 4575, Ethernet feeds, etc.) <br>     *Provides sensor interfacing/processing <br>     *Performs NITF image formation <br>     *Provides for database management <br> <br>VANTAGE Soar  <br>     *Interfaces with data received through SDL's CDL Interface Box (CIB) or Sky Lynx for data link interfacing and simulation <br>     *Allows operator to load and play simulation CDL files, select CDL data rate, select bit error rates, perform Built-In Test (BIT), and view advanced CDL statistics <br> <br>VANTAGE Web Enabled Sensor Service (WESS) <br>     *Provides map-based database access via a standard web browser <br> <br> <br>The Vantage Web Enabled Sensor Service (WESS) is an optional service that provides access to Vantage databases from other applications. WESS The Web Enabled Sensor Service (WESS) is a server that runs on the Vantage server and exposes the Vantage database of imagery and metadata to the outside via SOAP. WESS uses an open protocol to communicate data and is not bound to a browser or programming language. The WESS client is an application that allows users to display imagery from the Vantage database in a web browser, over a network. retrieves imagery, video files, MTI files, and related products from a Vantage database over a network connection, and displays it in a web browser. If you are using Internet Explorer (IE) 9 or Firefox 3, you can also use WESS to manipulate and enhance imagery. <br> <br>",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NRL",
  "releaseDate" : 1377669600000,
  "version" : "n/a",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1405452097000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Colby TEST",
    "userType" : "Developer",
    "createDts" : 1367309730000,
    "updateDts" : 1367309730000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1393834530000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    } ]
  }, {
    "question" : "Are there alternate licenses? (SAMPLE)",
    "username" : "Cathy TEST",
    "userType" : "End User",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "Developer",
      "answeredDate" : 1399961730000
    }, {
      "response" : "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1399961730000
    } ]
  }, {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "End User",
    "createDts" : 1328379330000,
    "updateDts" : 1328379330000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "End User",
      "answeredDate" : 1393834530000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "APP",
    "codeDescription" : "Application",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "DEV",
    "codeDescription" : "Development",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Mapping"
  } ],
  "metadata" : [ {
    "label" : "Support Common Interface 2.1 (SAMPLE)",
    "value" : "No"
  }, {
    "label" : "Common Uses (SAMPLE)",
    "value" : "UDOP, Information Sharing, Research"
  }, {
    "label" : "Available to public (SAMPLE)",
    "value" : "YES"
  } ],
  "componentMedia" : [ {
    "link" : "images/oldsite/wess_logo.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Abby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://www.spacedynamics.org/products/vantage' title='http://www.spacedynamics.org/products/vantage' target='_blank'> http://www.spacedynamics.org/products/vantage</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='http://www.spacedynamics.org/products/vantage' title='http://www.spacedynamics.org/products/vantage' target='_blank'> http://www.spacedynamics.org/products/vantage</a>"
  }, {
    "name" : "Product Homepage",
    "type" : "Homepage",
    "description" : null,
    "link" : "<a href='http://www.spacedynamics.org/products/vantage' title='http://www.spacedynamics.org/products/vantage' target='_blank'> http://www.spacedynamics.org/products/vantage</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "email David.Marchant@sdl.usu.edu"
  } ],
  "reviews" : [ {
    "username" : "Abby TEST",
    "userType" : "End User",
    "comment" : "This wasn't quite what I thought it was.",
    "rating" : 4,
    "title" : "Confused (SAMPLE)",
    "usedTimeCode" : "< 1 year",
    "lastUsed" : 1362298530000,
    "updateDate" : 1398845730000,
    "organization" : "NSA",
    "recommend" : false,
    "pros" : [ {
      "text" : "Reliable"
    } ],
    "cons" : [ ]
  }, {
    "username" : "Colby TEST",
    "userType" : "Developer",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 4,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 1 year",
    "lastUsed" : 1367309730000,
    "updateDate" : 1399961730000,
    "organization" : "DCGS Navy",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Legacy system"
    }, {
      "text" : "Poor Documentation"
    } ]
  } ],
  "dependencies" : [ {
    "dependency" : "Erlang",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 3.0"
  } ]
}, {
  "componentId" : 54,
  "guid" : "9a540308-86e4-406a-8923-97496fb9a215",
  "name" : "VANTAGE WESS OZONE Widget",
  "description" : "The Vantage Web Enabled Sensor Service (WESS) is an optional service that provides access to Vantage databases from other applications. WESS The Web Enabled Sensor Service (WESS) is a server that runs on the Vantage server and exposes the Vantage database of imagery and metadata to the outside via SOAP. WESS uses an open protocol to communicate data and is not bound to a browser or programming language. The WESS client is an application that allows users to display imagery from the Vantage database in a web browser, over a network. retrieves imagery, video files, MTI files, and related products from a Vantage database over a network connection, and displays it in a web browser. If you are using Internet Explorer (IE) 9 or Firefox 3, you can also use WESS to manipulate and enhance imagery. <br> <br> <br>There are two widgets provided by the WESS application, called the VANTAGE WESS Search widget and the VANTAGE WESS Viewer widget.  Together the WESS OZONE Widgets are a lightweight web application that, after installation into the Ozone Widget Framework and configuration, allows the user to view the contents of a VANTAGE database or a CDR compliant data source; it also interfaces with the JC2CUI map widget.  Google Maps is one of the view capabilities provided by the Widget.  The Widget provides a search capability.   <br> <br>Links: <br>SDL Vantage Link:  <a href='http://www.spacedynamics.org/products/vantage' title='http://www.spacedynamics.org/products/vantage' target='_blank'> vantage</a>",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "NRL",
  "releaseDate" : 1378101600000,
  "version" : "0",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "david.treat",
  "updateDts" : 1405452122000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Which version supports y-docs 1.5+? (SAMPLE)",
    "username" : "Cathy TEST",
    "userType" : "Developer",
    "createDts" : 1328379330000,
    "updateDts" : 1328379330000,
    "responses" : [ {
      "response" : "Version 3.1 added support.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Developer",
      "answeredDate" : 1399961730000
    }, {
      "response" : "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "End User",
      "answeredDate" : 1393834530000
    } ]
  }, {
    "question" : "Are samples included? (SAMPLE)",
    "username" : "Abby TEST",
    "userType" : "End User",
    "createDts" : 1381644930000,
    "updateDts" : 1381644930000,
    "responses" : [ {
      "response" : "They are included in a separate download.(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "End User",
      "answeredDate" : 1391537730000
    } ]
  }, {
    "question" : "Are there alternate licenses? (SAMPLE)",
    "username" : "Colby TEST",
    "userType" : "End User",
    "createDts" : 1362298530000,
    "updateDts" : 1362298530000,
    "responses" : [ {
      "response" : "You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "End User",
      "answeredDate" : 1399961730000
    }, {
      "response" : "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "End User",
      "answeredDate" : 1399961730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "WIDGET",
    "codeDescription" : "Widget",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LICCLASS",
    "typeDescription" : "License Classification",
    "code" : "GOTS",
    "codeDescription" : "GOTS",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "DEV",
    "codeDescription" : "Development",
    "important" : false
  }, {
    "type" : "OWFCOMP",
    "typeDescription" : "OWF Compatible",
    "code" : "Y",
    "codeDescription" : "Yes",
    "important" : false
  } ],
  "tags" : [ {
    "text" : "Mapping"
  } ],
  "metadata" : [ {
    "label" : "Support Common Interface 2.1 (SAMPLE)",
    "value" : "No"
  }, {
    "label" : "Extra Metadata (SAMPLE)",
    "value" : "Unfiltered"
  }, {
    "label" : "Available to public (SAMPLE)",
    "value" : "YES"
  } ],
  "componentMedia" : [ {
    "link" : "images/oldsite/wess_logo.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Jack TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/WESS+Documentation' title='https://confluence.di2e.net/display/DI2E/WESS+Documentation' target='_blank'> https://confluence.di2e.net/display/DI2E/WESS+Documentation</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://confluence.di2e.net/display/DI2E/Installation' title='https://confluence.di2e.net/display/DI2E/Installation' target='_blank'> https://confluence.di2e.net/display/DI2E/Installation</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://stash.di2e.net/projects/USU/repos/wess-widget/browse' title='https://stash.di2e.net/projects/USU/repos/wess-widget/browse' target='_blank'> https://stash.di2e.net/projects/USU/repos/wess-widget/browse</a>"
  } ],
  "reviews" : [ {
    "username" : "Colby TEST",
    "userType" : "Developer",
    "comment" : "This was perfect it solved our issues and provided tools for things we didn't even anticipate.",
    "rating" : 4,
    "title" : "Just what I was looking for (SAMPLE)",
    "usedTimeCode" : "< 1 month'",
    "lastUsed" : 1388769330000,
    "updateDate" : 1393834530000,
    "organization" : "DCGS Army",
    "recommend" : false,
    "pros" : [ {
      "text" : "Reliable"
    } ],
    "cons" : [ ]
  }, {
    "username" : "Colby TEST",
    "userType" : "End User",
    "comment" : "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.",
    "rating" : 4,
    "title" : "Good Product (SAMPLE)",
    "usedTimeCode" : "< 3 Months",
    "lastUsed" : 1362298530000,
    "updateDate" : 1399961730000,
    "organization" : "DCGS Army",
    "recommend" : true,
    "pros" : [ ],
    "cons" : [ {
      "text" : "Poorly Tested"
    }, {
      "text" : "Difficult Installation"
    } ]
  } ],
  "dependencies" : [ {
    "dependency" : "Tomcat",
    "version" : null,
    "dependencyReferenceLink" : null,
    "comment" : "Version 7 or 8"
  } ]
}, {
  "componentId" : 55,
  "guid" : "3e3b21d6-2374-4417-ba9c-ec56a9e0a314",
  "name" : "Vega 3D Map Widget",
  "description" : "Vega is a 3D map widget with support for high-resolution imagery in various formats including WMS, WFS, and ArcGIS.  Vega also supports 3-dimensional terrain, and time-based data and has tools for drawing shapes and points and importing/exporting data.",
  "parentComponent" : null,
  "subComponents" : [ ],
  "relatedComponents" : [ ],
  "organization" : "unknown",
  "releaseDate" : 1377669600000,
  "version" : "0.1-ALPHA2",
  "activeStatus" : "A",
  "lastActivityDate" : null,
  "createUser" : "CROSS.PATRICK.I.1126309879",
  "createDts" : null,
  "updateUser" : "patrick.cross",
  "updateDts" : 1403200733000,
  "approvedDate" : null,
  "approvalState" : "A",
  "approvedUser" : null,
  "evaluation" : {
    "startDate" : null,
    "endDate" : null,
    "currentLevelCode" : "LEVEL0",
    "reviewedVersion" : null,
    "evaluationSchedule" : [ {
      "evaluationLevelCode" : "LEVEL0",
      "estimatedCompletionDate" : null,
      "actualCompletionDate" : 1389460530000,
      "levelStatus" : "C"
    }, {
      "evaluationLevelCode" : "LEVEL1",
      "estimatedCompletionDate" : 1392138930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL2",
      "estimatedCompletionDate" : 1393002930000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    }, {
      "evaluationLevelCode" : "LEVEL3",
      "estimatedCompletionDate" : 1393694130000,
      "actualCompletionDate" : null,
      "levelStatus" : "N"
    } ],
    "evaluationSections" : [ ]
  },
  "questions" : [ {
    "question" : "Does this support the 2.0 specs? (SAMPLE)",
    "username" : "Cathy TEST",
    "userType" : "Developer",
    "createDts" : 1367309730000,
    "updateDts" : 1367309730000,
    "responses" : [ {
      "response" : "No,  they planned to add support next Version(SAMPLE)",
      "username" : "Abby TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1393834530000
    }, {
      "response" : "Update: they backport support to version 1.6(SAMPLE)",
      "username" : "Dawson TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391447730000
    } ]
  }, {
    "question" : "Which os platforms does this support? (SAMPLE)",
    "username" : "Dawson TEST",
    "userType" : "Project Manager",
    "createDts" : 1388769330000,
    "updateDts" : 1388769330000,
    "responses" : [ {
      "response" : "CentOS 6.5 and MorphOS(SAMPLE)",
      "username" : "Jack TEST",
      "userType" : "Project Manager",
      "answeredDate" : 1391447730000
    }, {
      "response" : "'I think they also have Windows and ReactOS support. (SAMPLE)",
      "username" : "Cathy TEST",
      "userType" : "Developer",
      "answeredDate" : 1391447730000
    } ]
  } ],
  "attributes" : [ {
    "type" : "TYPE",
    "typeDescription" : "Component Type",
    "code" : "WIDGET",
    "codeDescription" : "Widget",
    "important" : true
  }, {
    "type" : "DI2ELEVEL",
    "typeDescription" : "DI2E Evaluation Level",
    "code" : "LEVEL0",
    "codeDescription" : "Level 0 - Available for Reuse/Not Evaluated",
    "important" : true
  }, {
    "type" : "DI2E-SVCV4-A",
    "typeDescription" : "DI2E SvcV-4 Alignment",
    "code" : "2.2",
    "codeDescription" : "2.2 Visualization",
    "important" : false
  }, {
    "type" : "CEEAR",
    "typeDescription" : "Commercial Exportable via EAR",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "ITAR",
    "typeDescription" : "ITAR Exportable",
    "code" : "N",
    "codeDescription" : "No",
    "important" : false
  }, {
    "type" : "LIFECYCSTG",
    "typeDescription" : "Lifecycle Stage",
    "code" : "OPR",
    "codeDescription" : "Operations",
    "important" : false
  }, {
    "type" : "OWFCOMP",
    "typeDescription" : "OWF Compatible",
    "code" : "Y",
    "codeDescription" : "Yes",
    "important" : false
  } ],
  "tags" : [ ],
  "metadata" : [ ],
  "componentMedia" : [ {
    "link" : "images/oldsite/maps-icon.png",
    "contentType" : "image/png",
    "caption" : null,
    "description" : null
  } ],
  "contacts" : [ {
    "postionDescription" : "Technical POC",
    "name" : "Colby TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  }, {
    "postionDescription" : "Government POC",
    "name" : "Dawson TEST",
    "email" : "sample_email@test.com",
    "phone" : "555-555-5555",
    "organization" : "sample organization"
  } ],
  "resources" : [ {
    "name" : "Documentation",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://wiki.macefusion.com/display/Widgets/Installing+Vega+%283D+Map+Widget%29+into+OWF' title='https://wiki.macefusion.com/display/Widgets/Installing+Vega+%283D+Map+Widget%29+into+OWF' target='_blank'> https://wiki.macefusion.com/display/Widgets/Installing+Vega+%283D+Map+Widget%29+into+OWF</a>"
  }, {
    "name" : "Install Url",
    "type" : "Document",
    "description" : null,
    "link" : "<a href='https://git.macefusion.com/git/dcgs-widgets' title='https://git.macefusion.com/git/dcgs-widgets' target='_blank'> https://git.macefusion.com/git/dcgs-widgets</a>"
  }, {
    "name" : "Code Location URL",
    "type" : "Code",
    "description" : null,
    "link" : "<a href='https://git.macefusion.com/git/dcgs-widgets' title='https://git.macefusion.com/git/dcgs-widgets' target='_blank'> https://git.macefusion.com/git/dcgs-widgets</a>"
  } ],
  "reviews" : [ ],
  "dependencies" : [ ]
} ];









  /* jshint ignore:end */




















































// Code here will be linted with JSHint.
/* jshint ignore:start */
// Code here will be linted with ignored by JSHint.


MOCKDATA2.resultsList = [ {
  "listingType" : "Component",
  "componentId" : 67,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Central Authentication Service (CAS)",
  "description" : "The Central Authentication Service (CAS) is a single sign-on protocol for the web. Its purpose is to permit a user to access multiple applications while providing their credentials (such as userid and password) only once. It also allows web applications to authenticate users without gaining access to ...",
  "organization" : "NRO",
  "lastActivityDate" : null,
  "updateDts" : 1405698045000,
  "averageRating" : 1,
  "views" : 126,
  "totalNumberOfReviews" : 71,
  "resourceLocation" : "api/v1/resource/components/67/detail",
  "attributes" : [ {
    "type" : "DI2E-SVCV4-A",
    "code" : "1.2.1"
  }, {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "LICTYPE",
    "code" : "OPENSRC"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 9,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "CLAVIN",
  "description" : "CLAVIN (Cartographic Location And Vicinity Indexer) is an open source software package for document geotagging and geoparsing that employs context-based geographic entity resolution. It extracts location names from unstructured text and resolves them against a gazetteer to produce data-rich geographic ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1400005248000,
  "averageRating" : 3,
  "views" : 167,
  "totalNumberOfReviews" : 34,
  "resourceLocation" : "api/v1/resource/components/9/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL1"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "LICCLASS",
    "code" : "FOSS"
  } ],
  "tags" : [ {
    "text" : "Mapping"
  }, {
    "text" : "Reference"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 10,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Common Map API Javascript Library",
  "description" : "Core Map API is a library of JavaScript (JS) functions that hide the underlying Common Map Widget API so that developers only have to include the JS library and call the appropriate JS functions that way they are kept away from managing or interacting directly with the channels.   Purpose/Goal of the <a href='https://confluence.di2e.net/download/attachments/14518881/cpce-map-api-jsDocs.zip?api=v2' title='https://confluence.di2e.net/download/attachments/14518881/cpce-map-api-jsDocs.zip?api=v2' target='_blank'> cpce-map-api-jsDocs.zip?api=v2</a> ...",
  "organization" : "DCGS-A",
  "lastActivityDate" : null,
  "updateDts" : 1399485055000,
  "averageRating" : 1,
  "views" : 70,
  "totalNumberOfReviews" : 57,
  "resourceLocation" : "api/v1/resource/components/10/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL1"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "PILOT"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 11,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Common Map Widget API",
  "description" : "Background Many programs and projects create widgets that search for or manipulate data then present the results on a map. The desire is to be able to combine data search/manipulation widgets from any provider with map widgets from other providers. In order to accomplish this, a standard way for the <a href='https://software.forge.mil/sf/frs/do/viewRelease/projects.jc2cui/frs.common_map_widget.common_map_widget_api' title='https://software.forge.mil/sf/frs/do/viewRelease/projects.jc2cui/frs.common_map_widget.common_map_widget_api' target='_blank'> frs.common_map_widget.common_map...</a> ...",
  "organization" : "DI2E Framework",
  "lastActivityDate" : null,
  "updateDts" : 1404172800000,
  "averageRating" : 0,
  "views" : 122,
  "totalNumberOfReviews" : 0,
  "resourceLocation" : "api/v1/resource/components/11/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "Charting"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 12,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Content Discovery and Retrieval Engine - Brokered Search Component",
  "description" : "Content Discovery and Retrieval functionality is based on the architecture, standards, and specifications being developed and maintained by the joint IC/DoD Content Discovery and Retrieval (CDR) Integrated Product Team (IPT). The components contained in this release are the 1.1 version of the reference ...",
  "organization" : "DI2E-F",
  "lastActivityDate" : null,
  "updateDts" : 1399485057000,
  "averageRating" : 0,
  "views" : 30,
  "totalNumberOfReviews" : 70,
  "resourceLocation" : "api/v1/resource/components/12/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ {
    "text" : "Charting"
  }, {
    "text" : "Data Exchange"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 13,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Content Discovery and Retrieval Engine - Describe Component",
  "description" : "Content Discovery and Retrieval functionality is based on the architecture, standards, and specifications being developed and maintained by the joint IC/DoD Content Discovery and Retrieval (CDR) Integrated Product Team (IPT). The components contained in this release are the 1.1 version of the reference ...",
  "organization" : "DI2E Framework",
  "lastActivityDate" : null,
  "updateDts" : 1399485058000,
  "averageRating" : 1,
  "views" : 147,
  "totalNumberOfReviews" : 37,
  "resourceLocation" : "api/v1/resource/components/13/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ {
    "text" : "Data Exchange"
  }, {
    "text" : "UDOP"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 14,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Content Discovery and Retrieval Engine - Retrieve Component",
  "description" : "The Retrieve Components are implementations of the capabilities and interfaces defined in IC/DoD CDR Retrieve Service Specification for SOAP Implementations DRAFT Version 1.0-20100331, March 31, 2010. Each Retrieve Component is a separately deployable component that provides access to a defined content <a href='https://www.intelink.gov/inteldocs/browse.php?fFolderId=431781' title='https://www.intelink.gov/inteldocs/browse.php?fFolderId=431781' target='_blank'> browse.php?fFolderId=431781</a> for more general information about CDR. ...",
  "organization" : "DI2E-F",
  "lastActivityDate" : null,
  "updateDts" : 1399485059000,
  "averageRating" : 1,
  "views" : 126,
  "totalNumberOfReviews" : 27,
  "resourceLocation" : "api/v1/resource/components/14/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 15,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Cursor on Target Toolkit",
  "description" : "(U) Cursor on Target is a set of components focused on driving interoperability in message exchanges. Components include: *An XML message schema  &nbsp;&nbsp;Basic (mandatory): what, when, where &nbsp;&nbsp; Extensible (optional): add subschema to add details  \u0001*A standard &nbsp;&nbsp; Established as ...",
  "organization" : "Air Force",
  "lastActivityDate" : null,
  "updateDts" : 1399485060000,
  "averageRating" : 3,
  "views" : 22,
  "totalNumberOfReviews" : 90,
  "resourceLocation" : "api/v1/resource/components/15/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 16,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS Discovery Metadata Guide",
  "description" : "This document provides information on DCGS metadata artifacts and guidance for populating DDMS and the DIB Metacard, using DDMS Schema Extensions, and creating new DDMS extension schemas. These guidelines should be used by developers and System Integrators building resource adapters and schemas to work ...",
  "organization" : "AFLCMC/HBBG",
  "lastActivityDate" : null,
  "updateDts" : 1399485060000,
  "averageRating" : 3,
  "views" : 43,
  "totalNumberOfReviews" : 22,
  "resourceLocation" : "api/v1/resource/components/16/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "DOC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 17,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS Enterprise Messaging Technical Profile",
  "description" : "DCGS Enterprise Services rely on asynchronous communications for sending messages so they can notify users when certain data is published or a particular event has occurred. Users may subscriber to a data source so they can be notified when a piece of intelligence has been published on a topic of interest. ...",
  "organization" : "DCGS EFT",
  "lastActivityDate" : null,
  "updateDts" : 1399485061000,
  "averageRating" : 0,
  "views" : 52,
  "totalNumberOfReviews" : 76,
  "resourceLocation" : "api/v1/resource/components/17/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 18,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS Enterprise OWF IdAM Technical Profile",
  "description" : "The Ozone Widget Framework is a web-application engine built around the concept of discrete, reusable web application interface components called widgets. Widgets may be composed into full applications by Ozone users or administrators.   The architecture of the Ozone framework allows widgets to be implemented ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1399487595000,
  "averageRating" : 0,
  "views" : 139,
  "totalNumberOfReviews" : 79,
  "resourceLocation" : "api/v1/resource/components/18/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 19,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS Integration Backbone (DIB)",
  "description" : "The Distributed Common Ground/Surface System (DCGS) Integration Backbone (DIB) is the enabler for DCGS enterprise interoperability. The DIB is a technical infrastructure, the foundation for sharing data across the ISR enterprise. More specifically the DIB is: 1) Standards-based set of software, services, ...",
  "organization" : "DMO",
  "lastActivityDate" : null,
  "updateDts" : 1399485063000,
  "averageRating" : 2,
  "views" : 98,
  "totalNumberOfReviews" : 8,
  "resourceLocation" : "api/v1/resource/components/19/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 20,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS-E Audit Log Management and Reporting Technical Profile",
  "description" : "This CDP provides the technical design description for the Audit and Accountability capability of the Distributed Common Ground/Surface System (DCGS) Enterprise Service Dial Tone (SDT) layer. It includes the capability architecture design details, conformance requirements, and implementation guidance. ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1399485064000,
  "averageRating" : 1,
  "views" : 148,
  "totalNumberOfReviews" : 32,
  "resourceLocation" : "api/v1/resource/components/20/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "UDOP"
  }, {
    "text" : "Charting"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 21,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS-E Domain Name System (DNS) Technical Profile",
  "description" : "(U) This CDP provides guidance for DNS for use by the DCGS Enterprise Community at large. DCGS Service Providers will use this guidance to make their services visibility and accessibility in the DCGS Enterprise SOA (DES). Several Intelligence Community/Department of Defense (IC/DoD) documents were incorporated ...",
  "organization" : "DCGS EFT",
  "lastActivityDate" : null,
  "updateDts" : 1399485064000,
  "averageRating" : 1,
  "views" : 182,
  "totalNumberOfReviews" : 35,
  "resourceLocation" : "api/v1/resource/components/21/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 22,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS-E Metrics Management Technical Profile",
  "description" : "**Previously titled Service Level Agreement/Quality of Service CDP**  (U) This CDP provides guidance for Metrics Management for use by the DCGS Enterprise Community at large. DCGS Service Providers will use this guidance to make their services visibility and accessibility in the DCGS Enterprise SOA. ...",
  "organization" : "DCGS EFT",
  "lastActivityDate" : null,
  "updateDts" : 1399485065000,
  "averageRating" : 5,
  "views" : 51,
  "totalNumberOfReviews" : 42,
  "resourceLocation" : "api/v1/resource/components/22/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 23,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS-E Sensor Web Enablement (SWE) Technical Profile",
  "description" : "(U//FOUO)  This CDP provides the technical design description for the SensorWeb capability of the Distributed Common Ground/Surface System (DCGS) Enterprise Intelligence, Surveillance and Reconnaissance (ISR) Common layer.    (U//FOUO)  SensorWeb is a development effort led by the Defense Intelligence ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1399485066000,
  "averageRating" : 2,
  "views" : 179,
  "totalNumberOfReviews" : 39,
  "resourceLocation" : "api/v1/resource/components/23/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "UDOP"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 24,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS-E Service Registration and Discovery (SRD) Technical Profile",
  "description" : "(U//FOUO) Service discovery provides DCGS Enterprise consumers the ability to locate and invoke services to support a given task or requirement in a trusted and secure operational environment. By leveraging service discovery, existing DCGS Enterprise services will be published to a root service registry, ...",
  "organization" : "DCGS EFT",
  "lastActivityDate" : null,
  "updateDts" : 1399485067000,
  "averageRating" : 3,
  "views" : 171,
  "totalNumberOfReviews" : 67,
  "resourceLocation" : "api/v1/resource/components/24/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 25,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS-E Time Synchronization (NTP) Technical Profile",
  "description" : "(U) This provides guidance for Network Time Protocol (NTP) for use by the DCGS Enterprise Community at large. DCGS Service Providers will use this guidance to make their services secure and interoperable in the DCGS Enterprise SOA  (U//FOUO) Time synchronization is a critical service in any distributed ...",
  "organization" : "DCGS EFT",
  "lastActivityDate" : null,
  "updateDts" : 1399485068000,
  "averageRating" : 1,
  "views" : 86,
  "totalNumberOfReviews" : 41,
  "resourceLocation" : "api/v1/resource/components/25/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "Access"
  }, {
    "text" : "Visualization"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 26,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DCGS-E Web Service Access Control Technical Profile",
  "description" : "Access Control incorporates an open framework and industry best practices/standards that leverage Web Services Security (WSS) [17] standards, also referred to as WS-Security, to create a common framework using Attribute Based Access Control (ABAC). The DCGS Enterprise advocates an ABAC model as the desired ...",
  "organization" : null,
  "lastActivityDate" : null,
  "updateDts" : 1399485068000,
  "averageRating" : 5,
  "views" : 180,
  "totalNumberOfReviews" : 91,
  "resourceLocation" : "api/v1/resource/components/26/detail",
  "attributes" : [ {
    "type" : "DI2E-SVCV4-A",
    "code" : "1.2.1"
  }, {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "Mapping"
  }, {
    "text" : "Communication"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 27,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DI2E Framework DIAS Simulator",
  "description" : "This package provides a basic simulator of the DoDIIS Identity and Authorization Service (DIAS) in a deployable web application using Apache CFX architecture.  The DI2E Framework development team have used this when testing DIAS specific attribute access internally with Identity and Access Management ...",
  "organization" : "DI2E Framework",
  "lastActivityDate" : null,
  "updateDts" : 1403200707000,
  "averageRating" : 1,
  "views" : 60,
  "totalNumberOfReviews" : 43,
  "resourceLocation" : "api/v1/resource/components/27/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "DEV"
  }, {
    "type" : "LICCLASS",
    "code" : "GOTS"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 28,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DI2E Framework OpenAM",
  "description" : "The DI2E Framework IdAM solution provides a core OpenAM Web Single Sign-On implementation wrapped in a Master IdAM Administration Console for ease of configuration and deployment.  Additionally, there are several enhancements to the core authentication functionality as well as external and local attribute ...",
  "organization" : "DI2E Framework",
  "lastActivityDate" : null,
  "updateDts" : 1405308845000,
  "averageRating" : 3,
  "views" : 29,
  "totalNumberOfReviews" : 64,
  "resourceLocation" : "api/v1/resource/components/28/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "LIFECYCSTG",
    "code" : "DEV"
  }, {
    "type" : "LICCLASS",
    "code" : "FOSS"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 30,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DI2E Framework Reference Architecture",
  "description" : "The DI2E Framework reference Architecture provides a structural foundation for the DI2E requirements assessment.  The DI2E Framework is specified using a level of abstraction that is not dependent on a specific technical solution, but can leverage the benefits of the latest technology advancements and ...",
  "organization" : "NRO // DI2E Framework PMO",
  "lastActivityDate" : null,
  "updateDts" : 1403200710000,
  "averageRating" : 1,
  "views" : 154,
  "totalNumberOfReviews" : 15,
  "resourceLocation" : "api/v1/resource/components/30/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "DOC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "Reference"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 31,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "DI2E RESTful CDR Search Technical Profile",
  "description" : "This profile provides the technical design description for the RESTful Search web service of Defense Intelligence Information Enterprise (DI2E). The profile includes capability architecture design details, implementation requirements and additional implementation guidance.  DI2E Enterprise service providers ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1399485073000,
  "averageRating" : 3,
  "views" : 70,
  "totalNumberOfReviews" : 6,
  "resourceLocation" : "api/v1/resource/components/31/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SPEC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "Visualization"
  }, {
    "text" : "Access"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 32,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Distributed Data Framework (DDF)",
  "description" : "DDF is a free and open source common data layer that abstracts services and business logic from the underlying data structures to enable rapid integration of new data sources. ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1405694884000,
  "averageRating" : 0,
  "views" : 186,
  "totalNumberOfReviews" : 6,
  "resourceLocation" : "api/v1/resource/components/32/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "LICTYPE",
    "code" : "OPENSRC"
  }, {
    "type" : "LICCLASS",
    "code" : "FOSS"
  } ],
  "tags" : [ {
    "text" : "UDOP"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 57,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Domain Name System (DNS) Guidebook for Linux/BIND",
  "description" : "This Guidebook focuses on those involved with the Domain Name System (DNS) in DI2E systems. Those building systems based on DI2E-offered components, running in a DI2E Framework. It provides guidance for two different roles - those who configure DNS, and those who rely on DNS in the development of distributed ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1403200713000,
  "averageRating" : 1,
  "views" : 110,
  "totalNumberOfReviews" : 38,
  "resourceLocation" : "api/v1/resource/components/57/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "DOC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 33,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "eDIB",
  "description" : "(U) eDIB contains the eDIB 4.0 services (management VM and worker VM). eDIB 4.0 is a scalable, virtualized webservice architecture based on the DMO's DIB 4.0. eDIB provides GUIs for an administrator to manage/configure eDIB. An end-user interface is not provided. Different data stores are supported including ...",
  "organization" : "DI2E Framework",
  "lastActivityDate" : null,
  "updateDts" : 1405102845000,
  "averageRating" : 0,
  "views" : 22,
  "totalNumberOfReviews" : 34,
  "resourceLocation" : "api/v1/resource/components/33/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "Reference"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 34,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Extensible Mapping Platform (EMP)",
  "description" : "The Extensible Mapping Platform (EMP) is a US Government Open Source project providing a framework for building robust OWF map widgets and map centric web applications. The EMP project is currently managed by US Army Tactical Mission Command (TMC) in partnership with DI2E, and developed by CECOM Software <a href='http://cmapi.org/' title='http://cmapi.org/' target='_blank'> <a href='https://project.forge.mil/sf/frs/do/viewRelease/projects.dcgsaozone/frs.cpce_mapping_components.sprint_18_cpce_infrastructure_er' title='https://project.forge.mil/sf/frs/do/viewRelease/projects.dcgsaozone/frs.cpce_mapping_components.sprint_18_cpce_infrastructure_er' target='_blank'> frs.cpce_mapping_components.spri...</a> ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1403200714000,
  "averageRating" : 4,
  "views" : 176,
  "totalNumberOfReviews" : 6,
  "resourceLocation" : "api/v1/resource/components/34/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "WIDGET"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL1"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "DEV"
  }, {
    "type" : "OWFCOMP",
    "code" : "Y"
  }, {
    "type" : "LICCLASS",
    "code" : "GOSS"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 73,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GAPS - Data Catalog Service",
  "description" : "The GAPS Data Catalog Service provides a set of RESTful services to manage data sources accessible to GAPS applications.  The catalog service offers RESTful interfaces for querying sources as XML or JSON structures. The service is a combination of two web services; the root service, and the metric service. <a href='https://gapsprod1.stratcom.smil.mil/datacatalogservice/datacatalogservice.ashx' title='https://gapsprod1.stratcom.smil.mil/datacatalogservice/datacatalogservice.ashx' target='_blank'> datacatalogservice.ashx</a> <a href='http://jwicsgaps.usstratcom.ic.gov/datacatalogservice/datacatalogservice.ashx' title='http://jwicsgaps.usstratcom.ic.gov/datacatalogservice/datacatalogservice.ashx' target='_blank'> datacatalogservice.ashx</a> <a href='https://gapsprod1.stratcom.smil.mil/datacatalogservice/datasourcemetrics.ashx' title='https://gapsprod1.stratcom.smil.mil/datacatalogservice/datasourcemetrics.ashx' target='_blank'> datasourcemetrics.ashx</a> <a href='http://jwicsgaps.usstratcom.ic.gov/datacatalogservice/datasourcemetrics.ashx' title='http://jwicsgaps.usstratcom.ic.gov/datacatalogservice/datasourcemetrics.ashx' target='_blank'> datasourcemetrics.ashx</a> ...",
  "organization" : "STRATCOM J8",
  "lastActivityDate" : null,
  "updateDts" : 1405691893000,
  "averageRating" : 2,
  "views" : 107,
  "totalNumberOfReviews" : 23,
  "resourceLocation" : "api/v1/resource/components/73/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 74,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GAPS - Gazetteer Service",
  "description" : "The GAPS Gazetteer Service offers a set of geographical dictionary services that allows users to search for city and military bases, retrieving accurate latitude and longitude values for those locations.  The user may search based on name or location, with the gazetteer returning all entries that match <a href='https://gapsprod1.stratcom.smil.mil/gazetteers/NgaGnsGazetteerService.asmx' title='https://gapsprod1.stratcom.smil.mil/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a> (NGA Gazetteer) <a href='https://gapsprod1.stratcom.smil.mil/gazetteers/UsgsGnisGazetteerService.asmx' title='https://gapsprod1.stratcom.smil.mil/gazetteers/UsgsGnisGazetteerService.asmx' target='_blank'> UsgsGnisGazetteerService.asmx</a> (USGS Gazetteer) <a href='<a href='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' title='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a>' title='<a href='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' title='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a>' target='_blank'> NgaGnsGazetteerService.asmx</a> <a href='<a href='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' title='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a>' title='<a href='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' title='http://jwicsgaps.usstratcom.ic.gov/gazetteers/NgaGnsGazetteerService.asmx' target='_blank'> NgaGnsGazetteerService.asmx</a>' target='_blank'> NgaGnsGazetteerService.asmx</a> (USGS Gazetteer) ...",
  "organization" : "STRATCOM J8",
  "lastActivityDate" : null,
  "updateDts" : 1405691776000,
  "averageRating" : 2,
  "views" : 130,
  "totalNumberOfReviews" : 37,
  "resourceLocation" : "api/v1/resource/components/74/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ {
    "text" : "Reference"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 72,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GAPS - Scenario Service",
  "description" : "The GAPS Scenario Service is a SOAP based interface into the GAPS UDOP creation, execution and publishing mechanism.  The Scenario Service allows an external entity to perform Machine to Machine (M2M) calls in order to create and execute UDOPs.  This interface can be used to integrate with other systems <a href='https://gapsprod1.stratcom.smil.mil/ScenarioService/ScenarioService.asmx' title='https://gapsprod1.stratcom.smil.mil/ScenarioService/ScenarioService.asmx' target='_blank'> ScenarioService.asmx</a> <a href='https://gapsprod1.stratcom.smil.mil/VsaPortal/RespositoryService.asmx' title='https://gapsprod1.stratcom.smil.mil/VsaPortal/RespositoryService.asmx' target='_blank'> RespositoryService.asmx</a> JWICS: <a href='http://jwicsgaps.usstratcom.ic.gov:8000/ScenarioService/ScenarioService.asmx' title='http://jwicsgaps.usstratcom.ic.gov:8000/ScenarioService/ScenarioService.asmx' target='_blank'> ScenarioService.asmx</a> <a href='http://jwicsgaps.usstratcom.ic.gov:8000/VsaPortal/RepositoryService.asmx' title='http://jwicsgaps.usstratcom.ic.gov:8000/VsaPortal/RepositoryService.asmx' target='_blank'> RepositoryService.asmx</a> ...",
  "organization" : "STRATCOM J8",
  "lastActivityDate" : null,
  "updateDts" : 1405691317000,
  "averageRating" : 1,
  "views" : 85,
  "totalNumberOfReviews" : 89,
  "resourceLocation" : "api/v1/resource/components/72/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ {
    "text" : "Charting"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 35,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GVS",
  "description" : "The National Geospatial-Intelligence Agency's (NGA) GEOINT Visualization Services is a suite of web-based capabilities that delivers geospatial visualization services to the Department of Defense (DoD) and Intelligence Community (IC) via classified and unclassified computer networks to provide visualization <a href='https://www.intelink.gov/wiki/GVS' title='https://www.intelink.gov/wiki/GVS' target='_blank'> GVS</a> <a href='https://www.intelink.gov/blogs/geoweb/' title='https://www.intelink.gov/blogs/geoweb/' target='_blank'> <a href='https://community.forge.mil/content/geoint-visualization-services-gvs-program' title='https://community.forge.mil/content/geoint-visualization-services-gvs-program' target='_blank'> geoint-visualization-services-gv...</a> <a href='https://community.forge.mil/content/geoint-visualization-services-gvs-palanterrax3' title='https://community.forge.mil/content/geoint-visualization-services-gvs-palanterrax3' target='_blank'> geoint-visualization-services-gv...</a> ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1405612927000,
  "averageRating" : 1,
  "views" : 87,
  "totalNumberOfReviews" : 81,
  "resourceLocation" : "api/v1/resource/components/35/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 58,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GVS - Base Maps - ESRi",
  "description" : "World Imagery Basemap is a global service that presents imagery from NGA holdings for Cache Scales from 1:147M - 1:282. This imagery includes various sources and resolutions spanning 15m to 75mm.  The imagery sources are Buckeye, US/CAN/MX Border Imagery, Commercial Imagery, USGS High Resolution Orthos, ...",
  "organization" : "NGA",
  "lastActivityDate" : null,
  "updateDts" : 1403200715000,
  "averageRating" : 2,
  "views" : 120,
  "totalNumberOfReviews" : 73,
  "resourceLocation" : "api/v1/resource/components/58/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 59,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GVS - Base Maps - Google Globe - Summary Information",
  "description" : "GVS Google Earth Globe Services are constructed from DTED, CIB, Natural View, and Commercial Imagery. Vector data includes NGA standards such as VMAP, GeoNames, and GNIS. GVS recently added NavTeq Streets and Homeland Security Infrastructure data to the Google Globe over the United States. The Globes <a href='https://earth.gvs.nga.mil/wms/fusion_maps_wms.cgi' title='https://earth.gvs.nga.mil/wms/fusion_maps_wms.cgi' target='_blank'> fusion_maps_wms.cgi</a> *WMS - AF/PK - <a href='https://earth.gvs.nga.mil/wms/afpk_map/fusion_maps_wms.cgi' title='https://earth.gvs.nga.mil/wms/afpk_map/fusion_maps_wms.cgi' target='_blank'> fusion_maps_wms.cgi</a> <a href='https://earth.gvs.nga.mil/wms/nvue_map/fusion_maps_wms.cgi' title='https://earth.gvs.nga.mil/wms/nvue_map/fusion_maps_wms.cgi' target='_blank'> fusion_maps_wms.cgi</a> *WMTS - <a href='https://earth.gvs.nga.mil/cgi-bin/ogc/service.py' title='https://earth.gvs.nga.mil/cgi-bin/ogc/service.py' target='_blank'> service.py</a> ...",
  "organization" : "NGA",
  "lastActivityDate" : null,
  "updateDts" : 1403200717000,
  "averageRating" : 0,
  "views" : 39,
  "totalNumberOfReviews" : 30,
  "resourceLocation" : "api/v1/resource/components/59/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 60,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GVS - Features - CIDNE SIGACTS",
  "description" : "The GVS CIDNE SIGACTS Data Layer provides a visualization-ready (KML 2.2 compliant formatted) result set for a query of SIGACTS, as obtained from the Combined Information Data Network Exchange (CIDNE) within a defined AOI (\"bounding box\", \"point/radius\", \"path buffer\").  To access the CIDNE SIGACTS Dynamic <a href='http://home.gvs.nga.smil.mil/CIDNE/SigactsServlet' title='http://home.gvs.nga.smil.mil/CIDNE/SigactsServlet' target='_blank'> SigactsServlet</a> JWICs: <a href='http://home.gvs.nga.ic.gov/CIDNE/SigactsServlet' title='http://home.gvs.nga.ic.gov/CIDNE/SigactsServlet' target='_blank'> SigactsServlet</a> ...",
  "organization" : "NGA",
  "lastActivityDate" : null,
  "updateDts" : 1403200718000,
  "averageRating" : 4,
  "views" : 48,
  "totalNumberOfReviews" : 35,
  "resourceLocation" : "api/v1/resource/components/60/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 61,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GVS - Features - GE Shared Products",
  "description" : "GVS Shared Product Query provides you with the ability to display geospatial information layers in a Google Earth client depicting data previously created and stored in the GVS Shared Product Buffer by other users.  Home page: <a href='https://home.gvs.nga.mil/UPS/RSS' title='https://home.gvs.nga.mil/UPS/RSS' target='_blank'> RSS</a> for the manual query <a href='https://home.gvs.nga.mil/home/capabilities/queries/shared_products' title='https://home.gvs.nga.mil/home/capabilities/queries/shared_products' target='_blank'> shared_products</a> for the query tool Wiki: A general GVS Wiki can be found here <a href='https://intellipedia.intelink.gov/wiki/GVS' title='https://intellipedia.intelink.gov/wiki/GVS' target='_blank'> GVS</a> no specialized shared product wiki pages exist ...",
  "organization" : "NGA",
  "lastActivityDate" : null,
  "updateDts" : 1403200719000,
  "averageRating" : 1,
  "views" : 0,
  "totalNumberOfReviews" : 3,
  "resourceLocation" : "api/v1/resource/components/61/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 62,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GVS - Features - Intelink",
  "description" : "GVS - Features - Intelink: Provides the capability to perform a temporal keyword search for news items, Intellipedia data, and intelligence reporting and finished products found on Intelink. A list of geo-referenced locations in response to a query based on a filter.  GVS INTELINK GEO SEARCH WFS INTERFACE <a href='http://home.gvs.nga.ic.gov/metacarta/wfs' title='http://home.gvs.nga.ic.gov/metacarta/wfs' target='_blank'> wfs</a> SIPRNet: <a href='http://home.gvs.nga.smil.mil/metacarta/wfs' title='http://home.gvs.nga.smil.mil/metacarta/wfs' target='_blank'> wfs</a> ...",
  "organization" : "NGA",
  "lastActivityDate" : null,
  "updateDts" : 1403200721000,
  "averageRating" : 5,
  "views" : 132,
  "totalNumberOfReviews" : 80,
  "resourceLocation" : "api/v1/resource/components/62/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 63,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GVS - Features - MIDB",
  "description" : "GVS - Features - MIDB allows the user to query MIDB (Equipment) data within a defined AOI.  User takes HTTP-Get parameters (Listed below the summary table and in the documentation) and builds a query.  In the exposed Service Interfaces document MIDB services are covered in section 6.1 which is approximately <a href='http://home.gvs.nga.ic.gov/MIDB/wfs' title='http://home.gvs.nga.ic.gov/MIDB/wfs' target='_blank'> wfs</a> <a href='http://home.gvs.nga.smil.mil/MIDB/wfs' title='http://home.gvs.nga.smil.mil/MIDB/wfs' target='_blank'> wfs</a> <a href='<a href='https://home.gvs.nga.smil.mil/home/documentation' title='https://home.gvs.nga.smil.mil/home/documentation' target='_blank'> documentation</a>' title='<a href='https://home.gvs.nga.smil.mil/home/documentation' title='https://home.gvs.nga.smil.mil/home/documentation' target='_blank'> documentation</a>' target='_blank'> documentation</a> <a href='<a href='https://home.gvs.nga.smil.mil/home/documentation' title='https://home.gvs.nga.smil.mil/home/documentation' target='_blank'> documentation</a>' title='<a href='https://home.gvs.nga.smil.mil/home/documentation' title='https://home.gvs.nga.smil.mil/home/documentation' target='_blank'> documentation</a>' target='_blank'> documentation</a> <a href='https://home.gvs.nga.ic.gov/home/documentation' title='https://home.gvs.nga.ic.gov/home/documentation' target='_blank'> documentation</a> <a href='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' title='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' target='_blank'> GVS_Exposed_Service_Interfaces.pdf</a> <a href='https://home.gvs.nga.smil.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' title='https://home.gvs.nga.smil.mil/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' target='_blank'> GVS_Exposed_Service_Interfaces.pdf</a> <a href='https://home.gvs.ic.gov/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' title='https://home.gvs.ic.gov/GVSData/UE_Docs/GVS_Exposed_Service_Interfaces.pdf' target='_blank'> GVS_Exposed_Service_Interfaces.pdf</a> <a href='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' title='https://home.gvs.nga.mil/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' target='_blank'> GVS_Consolidated_QRG_Book_Reduce...</a> <a href='https://home.gvs.nga.smil.mil/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' title='https://home.gvs.nga.smil.mil/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' target='_blank'> GVS_Consolidated_QRG_Book_Reduce...</a> <a href='https://home.gvs.nga.ic.gov/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' title='https://home.gvs.nga.ic.gov/GVSData/UE_Docs/GVS_Consolidated_QRG_Book_Reduced_Size.pdf' target='_blank'> GVS_Consolidated_QRG_Book_Reduce...</a> ...",
  "organization" : "NGA",
  "lastActivityDate" : null,
  "updateDts" : 1403200722000,
  "averageRating" : 2,
  "views" : 95,
  "totalNumberOfReviews" : 41,
  "resourceLocation" : "api/v1/resource/components/63/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ {
    "text" : "Access"
  }, {
    "text" : "Data Exchange"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 64,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "GVS - Features - NES",
  "description" : "GVS - Features - NES provides a visualization-ready (KML 2.2 compliant formatted) result set for a query of the targets obtained from the NGA National Exploitation System (NES), within a defined AOI (\"bounding box\", \"point/radius\", \"path buffer\").. Note, NES is only available on JWICS.  Interface Details: <a href='http://home.gvs.nga.ic.gov/NESQuery/CoverageQuery' title='http://home.gvs.nga.ic.gov/NESQuery/CoverageQuery' target='_blank'> CoverageQuery</a> ...",
  "organization" : "NGA",
  "lastActivityDate" : null,
  "updateDts" : 1403200723000,
  "averageRating" : 4,
  "views" : 176,
  "totalNumberOfReviews" : 10,
  "resourceLocation" : "api/v1/resource/components/64/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SERVICE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ {
    "text" : "Access"
  }, {
    "text" : "Charting"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 65,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "HardwareWall",
  "description" : "The Boeing eXMeritus HardwareWall* is a highly configurable, commercial-off-the-shelf cross domain solution that enables rapid, automated, and secure data transfer between security domains:  eXMeritus has designed HardwareWall* as a secure data transfer system and an off-the-shelf Controlled Interface <a href='http://www.exmeritus.com/hardware_wall.html' title='http://www.exmeritus.com/hardware_wall.html' target='_blank'> hardware_wall.html</a> <a href='http://www.boeing.com/advertising/c4isr/isr/exmeritus_harware_wall.html' title='http://www.boeing.com/advertising/c4isr/isr/exmeritus_harware_wall.html' target='_blank'> exmeritus_harware_wall.html</a> ...",
  "organization" : "Boeing",
  "lastActivityDate" : null,
  "updateDts" : 1403200725000,
  "averageRating" : 0,
  "views" : 183,
  "totalNumberOfReviews" : 72,
  "resourceLocation" : "api/v1/resource/components/65/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "Y"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  }, {
    "type" : "LICCLASS",
    "code" : "COTS"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 36,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "IC AppsMall Cookbook: Applications Development",
  "description" : "The Applications Development Cookbook is designed as an overview of principles and best practices for Web Application Development, with a focus on new web and related technologies & concepts that take full advantage of modern browser capabilities. The ideas introduced in this document are not intended ...",
  "organization" : "IC SOG",
  "lastActivityDate" : null,
  "updateDts" : 1399485077000,
  "averageRating" : 5,
  "views" : 64,
  "totalNumberOfReviews" : 66,
  "resourceLocation" : "api/v1/resource/components/36/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "DOC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 37,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "ISF Enterprise Data Viewer Widget",
  "description" : "A widget designed to display search results in a tabular format.  It can page, sort, filter, and group results and organize items into working folders called \"Workspaces\" as well as perform full-record retrieval for supported result types.  It depends on the Persistence Service to store and retrieve ...",
  "organization" : "NRO/GED",
  "lastActivityDate" : null,
  "updateDts" : 1399485078000,
  "averageRating" : 3,
  "views" : 57,
  "totalNumberOfReviews" : 20,
  "resourceLocation" : "api/v1/resource/components/37/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "WIDGET"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  }, {
    "type" : "OWFCOMP",
    "code" : "Y"
  }, {
    "type" : "LICCLASS",
    "code" : "GOTS"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 38,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "ISF Persistence Service",
  "description" : "A RESTful service that persists JSON documents.  It is designed to have a swappable backend and currently supports MongoDB and in-memory implementations. ...",
  "organization" : "NRO/GED",
  "lastActivityDate" : null,
  "updateDts" : 1399485079000,
  "averageRating" : 5,
  "views" : 188,
  "totalNumberOfReviews" : 80,
  "resourceLocation" : "api/v1/resource/components/38/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  }, {
    "type" : "LICCLASS",
    "code" : "GOTS"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 39,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "ISF Search Criteria Widget",
  "description" : "A widget dedicated to providing search criteria to a compatible CDR backend.  It depends on the Persistence Service as a place to put retrieved metadata results.  It also optionally depends on the Map widget to interactively define geospatial queries (a text-based option is also available) and to render ...",
  "organization" : "NRO/GED",
  "lastActivityDate" : null,
  "updateDts" : 1399485080000,
  "averageRating" : 3,
  "views" : 144,
  "totalNumberOfReviews" : 15,
  "resourceLocation" : "api/v1/resource/components/39/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "WIDGET"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  }, {
    "type" : "OWFCOMP",
    "code" : "Y"
  }, {
    "type" : "LICCLASS",
    "code" : "GOTS"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 40,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "iSpatial",
  "description" : "iSpatial is a commercially available geospatial framework designed as a set of ready-to-customize, baseline tools that can be rapidly adapted to meet use cases calling for geo-visualization.  iSpatial consists of four major areas of core functionality:  Authorizing, Searching, Managing and Collaborating. <a href='http://www.t-sciences.com/wp-content/uploads/2013/04/iSpatial_v3_Technical_White_Paper.pdf' title='http://www.t-sciences.com/wp-content/uploads/2013/04/iSpatial_v3_Technical_White_Paper.pdf' target='_blank'> iSpatial_v3_Technical_White_Pape...</a> iSpatial <a href='http://www.t-sciences.com/wp-content/uploads/2014/01/iSpatial_Fed.pptx' title='http://www.t-sciences.com/wp-content/uploads/2014/01/iSpatial_Fed.pptx' target='_blank'> iSpatial_Fed.pptx</a> ...",
  "organization" : "Thermopylae Sciences and Technology",
  "lastActivityDate" : null,
  "updateDts" : 1403200726000,
  "averageRating" : 2,
  "views" : 45,
  "totalNumberOfReviews" : 99,
  "resourceLocation" : "api/v1/resource/components/40/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.1"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "LICCLASS",
    "code" : "COTS"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 41,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "JC2CUI Common 2D Map API Widget",
  "description" : "This is a map widget developed by JC2CUI that conforms to the Common Map API - see below for more information on the API.  Using this API allows developers to focus on the problem domain rather than implementing a map widget themselves. It also allows the actual map implementation used to be chosen dynamically ...",
  "organization" : "JC2CUI",
  "lastActivityDate" : null,
  "updateDts" : 1399485082000,
  "averageRating" : 1,
  "views" : 96,
  "totalNumberOfReviews" : 40,
  "resourceLocation" : "api/v1/resource/components/41/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "WIDGET"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  }, {
    "type" : "OWFCOMP",
    "code" : "Y"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 42,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "JView",
  "description" : "JView is a Java-based API (application programmer's interface) that was developed to reduce the time, cost, & effort associated with the creation of computer visualization applications.    JView provides the programmer with the ability to quickly develop 2-dimensional and 3-dimensional visualization <a href='https://extranet.rl.af.mil/jview/.' title='https://extranet.rl.af.mil/jview/.' target='_blank'> .</a> Formal configuration management and distribution of JView is performed through the Information Management Services program. ...",
  "organization" : "AFRL",
  "lastActivityDate" : null,
  "updateDts" : 1405370213000,
  "averageRating" : 1,
  "views" : 174,
  "totalNumberOfReviews" : 4,
  "resourceLocation" : "api/v1/resource/components/42/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL1"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LICCLASS",
    "code" : "GOTS"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 43,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Military Symbology Renderer",
  "description" : "The Mil Symbology Renderer is both a developer's toolkit as well as a ready to use deployable web application. The goal of this project is to provide a single distributable solution that can support as many use cases as possible for military symbology rendering. The current features available are: * ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1399485084000,
  "averageRating" : 2,
  "views" : 178,
  "totalNumberOfReviews" : 60,
  "resourceLocation" : "api/v1/resource/components/43/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "WIDGET"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  }, {
    "type" : "OWFCOMP",
    "code" : "Y"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 44,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "OpenAM",
  "description" : "OpenAM is an all-in-one access management platform with the adaptive intelligence to protect against risk-based threats across any environment. ...",
  "organization" : "DI2E-F",
  "lastActivityDate" : null,
  "updateDts" : 1403200729000,
  "averageRating" : 5,
  "views" : 29,
  "totalNumberOfReviews" : 2,
  "resourceLocation" : "api/v1/resource/components/44/detail",
  "attributes" : [ {
    "type" : "DI2E-SVCV4-A",
    "code" : "1.2.1"
  }, {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 45,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "OpenSextant",
  "description" : "(U) OpenSextant is an open source software package for geotagging unstructured text. OpenSextant is implemented in Java and based on the open source text analytic software GATE ( <a href='http://gate.ac.uk/' title='http://gate.ac.uk/' target='_blank'> </a> ). (U) OpenSextant can geotag documents in any ...",
  "organization" : "NGA",
  "lastActivityDate" : null,
  "updateDts" : 1399485085000,
  "averageRating" : 3,
  "views" : 172,
  "totalNumberOfReviews" : 29,
  "resourceLocation" : "api/v1/resource/components/45/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 46,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "OpenStack",
  "description" : "OpenStack is open source software for building public and private clouds ( <a href='http://www.openstack.org' title='http://www.openstack.org' target='_blank'> www.openstack.org</a> ). The release here has been developed by USC/ISI and has two distinctions from the mainstream open source release.  First, ...",
  "organization" : "USC/ISI",
  "lastActivityDate" : null,
  "updateDts" : 1399485086000,
  "averageRating" : 4,
  "views" : 64,
  "totalNumberOfReviews" : 17,
  "resourceLocation" : "api/v1/resource/components/46/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "PILOT"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 47,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "OpenStack Lessons Learned Document",
  "description" : "Lessons learned in the Openstack Folsom deployment by USC/ISI in the DI2E Framework environment.  This document is meant to be very specific to one deployment experience with the intention that it will be useful to others deploying in a similar environment.  Note this document is stored on the DI2E Framework <a href='https://devtools.di2e.net/' title='https://devtools.di2e.net/' target='_blank'> ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1399485087000,
  "averageRating" : 0,
  "views" : 178,
  "totalNumberOfReviews" : 65,
  "resourceLocation" : "api/v1/resource/components/47/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "DOC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Component",
  "componentId" : 48,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "OWASP Enterprise Security API",
  "description" : "ESAPI (The OWASP Enterprise Security API) is a free, open source, web application security control library that makes it easier for programmers to write lower-risk applications. The ESAPI libraries are designed to make it easier for programmers to retrofit security into existing applications. The ESAPI <a href='https://www.owasp.org.' title='https://www.owasp.org.' target='_blank'> www.owasp.org.</a> ...",
  "organization" : "OWASP",
  "lastActivityDate" : null,
  "updateDts" : 1399485088000,
  "averageRating" : 5,
  "views" : 158,
  "totalNumberOfReviews" : 92,
  "resourceLocation" : "api/v1/resource/components/48/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "SOFTWARE"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "Access"
  }, {
    "text" : "Testing"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 49,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "OWASP Web Application Security Guide",
  "description" : "The Development Guide is aimed at architects, developers, consultants and auditors and is a comprehensive manual for designing, developing and deploying secure Web Applications and Web Services. The original OWASP Development Guide has become a staple diet for many web security professionals. Since 2002, <a href='https://www.owasp.org.' title='https://www.owasp.org.' target='_blank'> www.owasp.org.</a> ...",
  "organization" : "OWASP",
  "lastActivityDate" : null,
  "updateDts" : 1399485088000,
  "averageRating" : 1,
  "views" : 46,
  "totalNumberOfReviews" : 89,
  "resourceLocation" : "api/v1/resource/components/49/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "DOC"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  } ],
  "tags" : [ {
    "text" : "Charting"
  }, {
    "text" : "Visualization"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 50,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Ozone Marketplace",
  "description" : "The Ozone marketplace is a storefront to store widgets, services, and web applications. It can be linked to the Ozone Widget Framework. ...",
  "organization" : "OWF Goss",
  "lastActivityDate" : null,
  "updateDts" : 1399485089000,
  "averageRating" : 1,
  "views" : 53,
  "totalNumberOfReviews" : 60,
  "resourceLocation" : "api/v1/resource/components/50/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : null
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  } ],
  "tags" : [ {
    "text" : "UDOP"
  }, {
    "text" : "Communication"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 51,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Ozone Widget Framework",
  "description" : "OWF is a web application that allows users to easily access all their online tools from one location. Not only can users access websites and applications with widgets, they can group them and configure some applications to interact with each other via widget intents.      Some Links: http://www.ozoneplatform.org/ ...",
  "organization" : "OWF GOSS",
  "lastActivityDate" : null,
  "updateDts" : 1402947991000,
  "averageRating" : 1,
  "views" : 173,
  "totalNumberOfReviews" : 37,
  "resourceLocation" : "api/v1/resource/components/51/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  }, {
    "type" : "LICTYPE",
    "code" : "GOVUNL"
  } ],
  "tags" : [ {
    "text" : "Communication"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 53,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "VANTAGE Software Suite",
  "description" : "VANTAGE is a tactical imagery exploitation software suite. This field proven application screens digital imagery from various tactical reconnaissance sensors and includes simultaneous support for multiple sensor types. VANTAGE provides for real and near real time screening of digital image data from ...",
  "organization" : "NRL",
  "lastActivityDate" : null,
  "updateDts" : 1405452097000,
  "averageRating" : 2,
  "views" : 77,
  "totalNumberOfReviews" : 95,
  "resourceLocation" : "api/v1/resource/components/53/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "APP"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "DEV"
  } ],
  "tags" : [ {
    "text" : "Mapping"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 54,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "VANTAGE WESS OZONE Widget",
  "description" : "The Vantage Web Enabled Sensor Service (WESS) is an optional service that provides access to Vantage databases from other applications. WESS The Web Enabled Sensor Service (WESS) is a server that runs on the Vantage server and exposes the Vantage database of imagery and metadata to the outside via SOAP. <a href='http://www.spacedynamics.org/products/vantage' title='http://www.spacedynamics.org/products/vantage' target='_blank'> vantage</a> ...",
  "organization" : "NRL",
  "lastActivityDate" : null,
  "updateDts" : 1405452122000,
  "averageRating" : 4,
  "views" : 152,
  "totalNumberOfReviews" : 78,
  "resourceLocation" : "api/v1/resource/components/54/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "WIDGET"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LICCLASS",
    "code" : "GOTS"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "DEV"
  }, {
    "type" : "OWFCOMP",
    "code" : "Y"
  } ],
  "tags" : [ {
    "text" : "Mapping"
  } ]
}, {
  "listingType" : "Component",
  "componentId" : 55,
  "articleAttributeType" : null,
  "articleAttributeCode" : null,
  "name" : "Vega 3D Map Widget",
  "description" : "Vega is a 3D map widget with support for high-resolution imagery in various formats including WMS, WFS, and ArcGIS.  Vega also supports 3-dimensional terrain, and time-based data and has tools for drawing shapes and points and importing/exporting data. ...",
  "organization" : "unknown",
  "lastActivityDate" : null,
  "updateDts" : 1403200733000,
  "averageRating" : 4,
  "views" : 198,
  "totalNumberOfReviews" : 75,
  "resourceLocation" : "api/v1/resource/components/55/detail",
  "attributes" : [ {
    "type" : "TYPE",
    "code" : "WIDGET"
  }, {
    "type" : "DI2ELEVEL",
    "code" : "LEVEL0"
  }, {
    "type" : "DI2E-SVCV4-A",
    "code" : "2.2"
  }, {
    "type" : "CEEAR",
    "code" : "N"
  }, {
    "type" : "ITAR",
    "code" : "N"
  }, {
    "type" : "LIFECYCSTG",
    "code" : "OPR"
  }, {
    "type" : "OWFCOMP",
    "code" : "Y"
  } ],
  "tags" : [ ]
}, {
  "listingType" : "Article",
  "componentId" : null,
  "articleAttributeType" : "1.2.1",
  "articleAttributeCode" : "DI2E-SVCV4-A",
  "name" : "IdAM",
  "description" : "Identity and Access Management Article.....",
  "organization" : "PMO",
  "lastActivityDate" : 1407175956075,
  "updateDts" : 1407175956075,
  "averageRating" : 0,
  "views" : 0,
  "totalNumberOfReviews" : 0,
  "resourceLocation" : "api/v1/resource/attributes/DI2E-SVCV4-A/attributeCode/1.2.1/article",
  "attributes" : [ {
    "type" : "DI2E-SVCV4-A",
    "code" : "1.2.1"
  } ],
  "tags" : [ ]
} ];


// (function(){
//   var types = [];
//   _.each(MOCKDATA2.resultsList, function(item){
//     _.each(item.attributes, function(attribute){
//       if (!types[attribute.typeDescription]) {
//         types[attribute.typeDescription] = {};
//         types[attribute.typeDescription].codes = [];
//       }
//       types[attribute.typeDescription].codes.push(attribute.codeDescription);
//       types[attribute.typeDescription].codes = jQuery.unique(types[attribute.typeDescription].codes);
//     })
//   })
//   console.log('results', types);
// }());



/* jshint ignore:end */

