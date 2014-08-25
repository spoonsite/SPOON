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


var MOCKDATA = {};


MOCKDATA.userProfile = {
  'username' : 'sample.user',
  'firstname' : 'Sample',
  'lastname' : 'User',
  'email' : 'sample.user@test.net',
  'organization' : 'Sample Organization',
  'userTypeCode' :  'USER',
  'createDts' : 1405096596000,
  'updateDts' : 1405096596000,
  'updateUser' : 'sample.user',
  'admin': false
};

MOCKDATA.userTypeCodes = {
  'totalResults': '3',
  'results': '3',
  'data':  [
  {
    'code': 'USER',
    'description': 'End User'
  },
  {
    'code': 'DEV',
    'description': 'Developer'
  },
  {
    'code': 'PM',
    'description': 'Project Manager'
  }
  ]
};


MOCKDATA.resources = {

};

MOCKDATA.feedback = {

};
MOCKDATA.componentState = [
  //
  {
    'id': 1,
    'state':'ssss',
    'desc':'DI2E Candidate'
  }
//  
];
MOCKDATA.externalDepend = [
  //
  {
    'name': 'WebLogic server',
    'desc':'Optional deployment container'
  },
  {
    'name': 'JBoss 7.1',
    'desc':'Optional deployment container'
  },
  {
    'name': 'Tomcat',
    'desc':'Optional deployment container'
  },
  {
    'name': 'JOGL',
    'desc':'Supplied within JView source code'
  }
  //  
  ];
  MOCKDATA.pointsContact = [
   //
   {
    'position': 'Program POC',
    'name': 'Jill Sample',
    'email': 'jill.sample@someemail.com'
  },
  {
    'position': 'Technical POC',
    'name': 'John Sample',
    'email': 'john.sample@someemail.com'
  },
  {
    'position': 'DI2E Asset Coordinator',
    'name': 'Jennifer Sample',
    'email': 'jennifer.sample@someemail.com'
  }
   //
   ];
   MOCKDATA.componentEvalProgressBar = [
  //
  {
    'class1': 'barborder',
    'type': 'success',
    'level': '0',
    'compmessage':'COMPLETED 2/15/2014',
    'compstatus':'0',
    'currvalue': 28.20512821,
    'desc':'Available for Reuse/Not Evaluated',
    'fulldesc':'<ul><li>Asset is added to the Storefront for reuse or consumption. Asset has not been evaluated for Enterprise readiness or Conformance.</li></ul>      <ul><li>Asset will enter DI2E Clearinghouse Process to be assessed for potential reuse.</li></ul>      <ul><li>Asset has completed the Component Prep and Analysis phase. </li></ul> '
  },
  {
    'class1': 'barborder',
    'type': 'info',
    'level': '1',
    'compmessage':'HALTED 3/15/2014',
    'compstatus':'2',
    'currvalue': 9.230769231,
    'desc':'Initial Reuse Analysis',
    'fulldesc':'<ul><li>Inspection portion of DI2E Framework Evaluation Checklist complete.</li></ul><ul><li>These questions focus mainly on the reuse potential (Visible, Accessible, and Understandable) by analysis of the information provided.</li></ul><ul><li>This level does not represent the pass or fail, the Consumer must read the Evaluation Report.</li></ul>'
  },
  {
    'class1': 'barborder',
    'type': 'danger',
    'level': '2',
    'compmessage':'IN PROGRESS (estimated complete 4/15/2014)',
    'compstatus':'1',
    'currvalue': 24.1025641,
    'desc':'Integration and Test',
    'fulldesc':'<ul><li>Integration and Test portion of the DI2E Framework Evaluation Checklist complete.</li></ul><ul><li>These questions focus on the interoperability and ease of reuse, and will normally include an I&T plan.</li></ul><ul><li>This level does not indicate a pass or fail of the conformance test.</li></ul><ul><li>Consumer will read the Test Report linked in the storefront entry for program functionality.</li></ul>'
  },
  {
    'class1': 'bar-transparent',
    'type': 'success',
    'level': '3',
    'compmessage':'NOT STARTED (estimated complete 5/15/2014)',
    'compstatus':'3',
    'currvalue': 38.46153846,
    'desc':'DI2E Framework Reference Implementation',
    'fulldesc':'<ul><li>Asset has been determined to be reusable and interoperable, appropriately documented, and conformant to applicable DI2E Framework specifications and standards and is integrated into the DI2E Reference Ecosystem.</li></ul>'
  }
  //
  ];
  MOCKDATA.componentEvalProgressBarDates = [
  //
  {
    'class1': 'barborder2',
    'type': 'success',
    'timedays': 28.20512821,
    'currdate': '2/25'
  },
  {
    'class1': 'barborder2',
    'type': 'success',
    'timedays': 9.230769231,
    'currdate': '3/15'
  },
  {
    'class1': 'barborder2',
    'type': 'info',
    'timedays': 24.1025641,
    'currdate': '5/1'
  },
  {
    'class1': 'bar-transparentdate',
    'type': 'success',
    'timedays': 38.46153846,
    'currdate': '7/15'
  }
//
];
MOCKDATA.resultsComments = [
  //
  {
    'question': 'Does this component integrate with Google Maps JavaScript API v3?',
    'by': 'Jimmy Tester',
    'date': '11:46 p.m. May 14, 2014',
    'answers': [
      //
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'Jill Sample',
        'date': '11:46 p.m. July 8, 2014'
      },
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'John Sample',
        'date': '7:46 a.m. July 9, 2014'
      },
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'Jackie Sample',
        'date': '8:07 a.m. July 10, 2014'
      }
    //
    ]
  },
  {
    'question': 'Has any one found a solution to ->this<- seg fault error???',
    'by': 'Sammy Tester',
    'date': '4:30 p.m. June 1, 2014',
    'answers': [
      //
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'Jill Sample',
        'date': '11:46 p.m. July 8, 2014'
      },
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'John Sample',
        'date': '7:46 a.m. July 9, 2014'
      },
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'Jackie Sample',
        'date': '8:07 a.m. July 10, 2014'
      }
    //
    ]
  },
  {
    'question': 'Just as a heads up, I\'ve figured out how to do this with this component in case it benefits someone in the future',
    'by': 'Johnny Tester',
    'date': '10:00 p.m. July 3, 2014',
    'answers': [
      //
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'Jill Sample',
        'date': '11:46 p.m. July 8, 2014'
      },
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'John Sample',
        'date': '7:46 a.m. July 9, 2014'
      },
      {
        'answer': 'Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
        'by': 'Jackie Sample',
        'date': '8:07 a.m. July 10, 2014'
      }
    //
    ]
  }
//
];
MOCKDATA.componentSummary = [
  //
  {
    'versions': '1.7.1',
    'evaluationdate': '3/14/2014',
    'componenttype': 'Redeployed Capability, API/Library',
    'agency': 'AFRL JView Team',
    'di2emarketplace': 'https://storefront.di2e.net/marketplace/serviceItem/show/24'
  }
//
];
MOCKDATA.componentVitals = [
  //
  {
    'name': 'SupportedOS',
    'desc': 'Same as Java 1.5+'
  },
  {
    'name': 'VM Support',
    'desc': 'yes'
  },
  {
    'name': 'License',
    'desc': 'JView license (CAC Card Required)'
  },
  {
    'name': 'SvcV-4 Alignment',
    'desc': '2.2.1.2 Widget Framework'
  },
  {
    'name': 'JCA Alignment',
    'desc': '6.2.3.8 Enterprise Application Software'
  },
  {
    'name': 'JCSFL Alignment',
    'desc': '8.9.5 Integrate Enterprise Applications'
  }
//
];
MOCKDATA.localAssetArtifacts = [
  //
  {
    'name': 'Home page',
    'desc':'https://extranet.rl.af.mil/jview/'
  },
  {
    'name': 'Wiki',
    'desc':'https://software.forge.mil/sf/wiki/do/viewPage/projects.jview/wiki/HomePage'
  },
  {
    'name': 'Binaries',
    'desc':'You must build this from the source code'
  },
  {
    'name': 'Source',
    'desc':'https://software.forge.mil/sf/go/proj1195'
  },
  {
    'name': 'Issue Tracking',
    'desc':'https://software.forge.mil/sf/go/proj1195'
  },
  {
    'name': 'Documentation',
    'desc':'See full evaluation report'
  },
  {
    'name': 'Training',
    'desc':'none'}
//
];
MOCKDATA.scoreTable = [
  //
  {
    'name': 'Discoverable',
    'ranking': 2,
    'questions':'Can the developer find the asset? Can a developer discover sufficient status information about the asset?',
    'criteria':'5 - A full description of the asset is hosted on a publicly available web page (e.g., owfgoss.org) and/or in a publicly available repository front page (e.g., GitHub), and the asset is registered in a well-known registry such as programmableweb.com.<br> <br>3 - A full description of the asset is hosted on a community available web page (IntellipediaU or forge.mil) and the asset is registered in a community provided storefront (DI2E Framework storefront or JC2CUI Enterprise Storefront).<br><br>1 - The only description of the asset is in a Word or PDF document saved in a limited access document repository (e.g., SharePoint or file system) that is not crawled by any search engine.',
    'icon': 'whatever'
  },
  {
    'name': 'Accessible',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Documentation',
    'ranking': 3,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Deployable',
    'ranking': 4,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Usable',
    'ranking': 5,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Error Handling',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Integrable',
    'ranking': 4,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'I/O Validation',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Testing',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Monitoring',
    'ranking': 1,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Performance',
    'ranking': 3,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Scalability',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Security',
    'ranking': 3,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Maintainability / Upgradability',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Community and Outreach',
    'ranking': 1,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Change Management',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'CA',
    'ranking': 1,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Licensing',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Roadmap',
    'ranking': 1,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Willingness',
    'ranking': 2,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  },
  {
    'name': 'Architecture Alignment',
    'ranking': 3,
    'questions':'',
    'criteria':'',
    'icon': 'whatever'
  }
//  
];
MOCKDATA.tagsList = [
  //
  'Application',
  'Data Exchange',
  'Data Transformation',
  'Data Validation',
  'Development Tool',
  'Enterprise Services',
  'IDE',
  'Image Search',
  'Image Mapping',
  'Java',
  'Mapping',
  'Planning and Direction',
  'Reference',
  'Reference Document',
  'Reference Documentation',
  'Software Libraries',
  'Software Library',
  'Visualization',
  'UDOP',
  'Widget',
  'Widgets',
  '#architecture',
  '#developement',
  '#maps',
  '#pluggable',
  '#trending',
  '#webdesign'
//
];

MOCKDATA.hotTopics = {
  //
  'list': []
//
};

MOCKDATA.prosConsList = {
  'pros':[
    //
    'Well tested',
    'Active Development',
    'Reliable',
    'Open Source',
    'Compact',
    'Well Tested'
  //
  ],
  'cons':[
    //
    'Poorly Tested',
    'Security Concerns',
    'Difficult Installation',
    'Poor Documentation',
    'Bulky',
    'Legacy system'
  //
  ]
};

MOCKDATA.watches = [
{
  'watchId' : 0,
  'lastUpdateDts' : 1358769330000,
  'lastViewDts' : 1358769330000,
  'createDts' : 1358769330000,
  'componentName' : 'Central Authentication Service (CAS)',
  'componentId' : 67,
  'notifyFlag' : false
},{
  'watchId' : 1,
  'lastUpdateDts' : 1406323711169,
  'lastViewDts' : 1358769330000,
  'createDts' : 1358769330000,
  'componentName' : 'Common Map Widget API',
  'componentId' : 11,
  'notifyFlag' : false
},{
  'watchId' : 2,
  'lastUpdateDts' : 1388769330000,
  'lastViewDts' : 1406323711169,
  'createDts' : 1358769330000,
  'componentName' : 'OpenSextant',
  'componentId' : 45,
  'notifyFlag' : false
},{
  'watchId' : 3,
  'lastUpdateDts' : 1388769330000,
  'lastViewDts' : 1358769330000,
  'createDts' : 1358769330000,
  'componentName' : 'Vega 3D Map Widget',
  'componentId' : 55,
  'notifyFlag' : false
},{
  'watchId' : 4,
  'lastUpdateDts' : 1388769330000,
  'lastViewDts' : 1358769330000,
  'createDts' : 1358769330000,
  'componentName' : 'DCGS-E Sensor Web Enablement (SWE) Technical Profile',
  'componentId' : 23,
  'notifyFlag' : false
}
];

MOCKDATA.filters = [ {
    "type" : "FUNDED",
    "description" : "Funded",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "Y",
      "label" : "Yes",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "Y",
      "label" : "No",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "JARALIGNMENT",
    "description" : "JARM ESL Alignment",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : true,
    "importantFlg" : true,
    "allowMutlipleFlg" : true,
    "codes" : [ {
      "code" : "1",
      "label" : "1 Customer Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1",
      "label" : "1.1 Customer Relationship Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2",
      "label" : "1.2 Customer Preeferences",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3",
      "label" : "1.3 Customer Initiated Assitance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2",
      "label" : "2 Process Automation Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1",
      "label" : "2.1 Tracting and Workflow",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2",
      "label" : "2.2 Routing and Scheduling",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3",
      "label" : "3 Business Management Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1",
      "label" : "3.1 Management of Process",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2",
      "label" : "3.2 Organizational Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3",
      "label" : "3.3 Investment Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4",
      "label" : "3.4 Supply Chain Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4",
      "label" : "4 Digital Asset Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1",
      "label" : "4.1 Analysis and Statistics",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.2",
      "label" : "4.2 Mission Performance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3",
      "label" : "4.3 Visualization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4",
      "label" : "4.4 Knowledge Discovery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.5",
      "label" : "4.5 Business Intelligence",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6",
      "label" : "4.6 Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5",
      "label" : "5 Back Office Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1",
      "label" : "5.1 Data Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2",
      "label" : "5.2 Human Resources",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3",
      "label" : "5.3 Financial Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4",
      "label" : "5.4 Asset / Materials Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5",
      "label" : "5.5 Development and Integration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.6",
      "label" : "5.6 Human Capital / Workforce Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6",
      "label" : "6 Support Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1",
      "label" : "6.1 Security Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2",
      "label" : "6.2 Collaboration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3",
      "label" : "6.3 Search",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4",
      "label" : "6.4 Communication",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.5",
      "label" : "6.5 System Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.6",
      "label" : "6.6 Forms Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.7",
      "label" : "6.7 Service Management",
      "description" : null,
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "ACCSTATUS",
    "description" : "Accreditation Status",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "ATO",
      "label" : "ATO",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "IATO",
      "label" : "IATO",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "ATOWPOA&M",
      "label" : "ATO with POA&M",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "IATT",
      "label" : "IATT",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "OTH",
      "label" : "Other",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "NETWORK",
    "description" : "Network(s) Fielded",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : true,
    "codes" : [ {
      "code" : "WWW",
      "label" : "Public Internet",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "NIPR",
      "label" : "NIPR",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "SIPR",
      "label" : "SIPR",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "JWICS",
      "label" : "JWICS",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "LICTYPE",
    "description" : "License Type",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "OPENSRC",
      "label" : "Open Source",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "ENT",
      "label" : "Enterprise",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "GOVUNL",
      "label" : "Government Unlimited Rights",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "PERSEAT",
      "label" : "Per Seat",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "PERCPU",
      "label" : "Per CPU",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "PERTRAN",
      "label" : "Per Transaction",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "FEE",
      "label" : "Fee for Service",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "UNKN",
      "label" : "Unknown",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "CEEAR",
    "description" : "Commercial Exportable via EAR",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "Y",
      "label" : "Yes",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "N",
      "label" : "No",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "DI2ELEVEL",
    "description" : "DI2E Evaluation Level",
    "visibleFlg" : true,
    "requiredFlg" : true,
    "archtechtureFlg" : false,
    "importantFlg" : true,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "NA",
      "label" : "NA - No Evaluation Planned",
      "description" : "Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.",
      "fullTextAvailable" : false
    }, {
      "code" : "LEVEL0",
      "label" : "Level 0 - Available for Reuse/Not Evaluated",
      "description" : "Asset is added to the Storefront for reuse or consumption. Asset has not been evaluated for Enterprise readiness or Conformance. Asset will enter DI2E Clearinghouse Process to be assessed for potential reuse. Asset has completed the Component Prep and Analysis phase.",
      "fullTextAvailable" : false
    }, {
      "code" : "LEVEL1",
      "label" : "Level 1 - Initial Reuse Analysis",
      "description" : "Inspection portion of DI2E Framework Evaluation Checklist complete. These questions focus mainly on the reuse potential (Visible, Accessible, and Understandable) by analysis of the information provided. This level does not represent the pass or fail, the Consumer must read the Evaluation Report.",
      "fullTextAvailable" : false
    }, {
      "code" : "LEVEL2",
      "label" : "Level 2 - Integration and Test",
      "description" : "Integration and Test portion of the DI2E Framework Evaluation Checklist complete. These questions focus on the interoperability and ease of reuse, and will normally include an I&T plan. This level does not indicate a pass or fail of the conformance test. Consumer will read the Test Report linked in the storefront entry for program functionality.",
      "fullTextAvailable" : false
    }, {
      "code" : "LEVEL3",
      "label" : "Level 3 - DI2E Framework Reference Implementation",
      "description" : "Asset has been determined to be reusable and interoperable, appropriately documented, and conformant to applicable DI2E Framework specifications and standards and is integrated into the DI2E Reference Ecosystem.",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "SERFORMAT",
    "description" : "Service Data Format",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : true,
    "codes" : [ {
      "code" : "XML",
      "label" : "XML",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "JSON",
      "label" : "JSON",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "KML",
      "label" : "KML",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "BINARY",
      "label" : "BINARY",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "OTHER",
      "label" : "Other",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "JCAALIGNMENT",
    "description" : "JCA Alignment",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : true,
    "importantFlg" : true,
    "allowMutlipleFlg" : true,
    "codes" : [ {
      "code" : "1",
      "label" : "1 Force Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1",
      "label" : "1.1 Force Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1",
      "label" : "1.1.1 Global Force Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1.1",
      "label" : "1.1.1.1 Apportionment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1.2",
      "label" : "1.1.1.2 Assignment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1.3",
      "label" : "1.1.1.3 Allocation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1.4",
      "label" : "1.1.1.4 Readiness Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.2",
      "label" : "1.1.2 Force Configuration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3",
      "label" : "1.1.3 Global Posture Execution",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2",
      "label" : "1.2 Force Preparation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1",
      "label" : "1.2.1 Training",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.2",
      "label" : "1.2.2 Exercising",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3",
      "label" : "1.2.3 Educating",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3.1",
      "label" : "1.2.3.1 Professional Military Education",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3.2",
      "label" : "1.2.3.2 Civilian Education",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4",
      "label" : "1.2.4 Doctrine",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.5",
      "label" : "1.2.5 Lessons Learned",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.6",
      "label" : "1.2.6 Concepts",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.7",
      "label" : "1.2.7 Experimentation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3",
      "label" : "1.3 Human Capital Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1",
      "label" : "1.3.1 Personnel and Family Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.1",
      "label" : "1.3.1.1 Community Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.2",
      "label" : "1.3.1.2 Casualty Assistance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.3",
      "label" : "1.3.1.3 Mortuary Affairs",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.4",
      "label" : "1.3.1.4 Wounded, ill and Injured Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2",
      "label" : "1.3.2 Personnel Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.1",
      "label" : "1.3.2.1 Manning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.2",
      "label" : "1.3.2.2 Compensation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.3",
      "label" : "1.3.2.3 Disability Evaluation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.4",
      "label" : "1.3.2.4 Personnel Accountability",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4",
      "label" : "1.4 Health Readiness",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1",
      "label" : "1.4.1 Force Health Protection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.1",
      "label" : "1.4.1.1 Human Performance Enhancement",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.2",
      "label" : "1.4.1.2 Medical Surveillance / Epidemiology",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.3",
      "label" : "1.4.1.3 Preventive Medicine",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.4",
      "label" : "1.4.1.4 In-transit Care",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.4.1",
      "label" : "1.4.1.4.1 In-Transit Care within a Joint Operational Area Intra-Theater",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.4.2",
      "label" : "1.4.1.4.2 In-Transit Care Outside a Joint Operational Area Inter-Theater",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.5",
      "label" : "1.4.1.5 Casualty Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.5.1",
      "label" : "1.4.1.5.1 Biomedical Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.5.2",
      "label" : "1.4.1.5.2 Ocular Health",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2",
      "label" : "1.4.2 Health Care Delivery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2.1",
      "label" : "1.4.2.1 Comprehensive Care Delivery in Military Facilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2.2",
      "label" : "1.4.2.2 Comprehensive Care Delivery via the Network of Civilian Providers",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3",
      "label" : "1.4.3 Health Service Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2",
      "label" : "2 Battlespace Awareness",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1",
      "label" : "2.1 Intelligence, Surveillance and Reconnaissance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1",
      "label" : "2.1.1 Intelligence, Surveillance and Reconnaissance Planning & Direction",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1.1",
      "label" : "2.1.1.1 Define and Prioritize Intelligence, Surveillance and Reconnaissance Requirements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1.2",
      "label" : "2.1.1.2 Develop a Collection Strategy",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1.3",
      "label" : "2.1.1.3 Task and Monitor Collection, Processing, Exploitation and Dissemination Resources",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1.4",
      "label" : "2.1.1.4 Intelligence, Surveillance and Reconnaissance Evaluation  Evaluation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2",
      "label" : "2.1.2 Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.1",
      "label" : "2.1.2.1 Signals Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.1.1",
      "label" : "2.1.2.1.1 Communications Signals Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.1.2",
      "label" : "2.1.2.1.2 Electronic Emissions Signals Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.1.3",
      "label" : "2.1.2.1.3 Foreign Instrumentation Signals Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.2",
      "label" : "2.1.2.2 Computer Network Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3",
      "label" : "2.1.2.3 Imagery Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3.1",
      "label" : "2.1.2.3.1 Electro-Optical Imagery Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3.1.1",
      "label" : "2.1.2.3.1.1 Panchronatic Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3.1.2",
      "label" : "2.1.2.3.1.2 Infrared Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3.1.3",
      "label" : "2.1.2.3.1.3 Ultraviolet Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3.1.4",
      "label" : "2.1.2.3.1.4 Specttral Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3.1.5",
      "label" : "2.1.2.3.1.5 Light Detection and Ranging Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3.2",
      "label" : "2.1.2.3.2 RADAR Imagery Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.4",
      "label" : "2.1.2.4 Measurements and Signatures Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.4.1",
      "label" : "2.1.2.4.1 Electro-Optical Signatures Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.4.2",
      "label" : "2.1.2.4.2 Radar Measurements and Signatures Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.4.3",
      "label" : "2.1.2.4.3 Geophysical Measurements & Signatures Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.4.4",
      "label" : "2.1.2.4.4 Radio-Frequency Signatures Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.4.5",
      "label" : "2.1.2.4.5 Chemical / Biological Materials Measurements and Signatures Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.4.6",
      "label" : "2.1.2.4.6 Nuclear Radiation Measurements and Signatures Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.5",
      "label" : "2.1.2.5 Human Intelligence, Surveillance and Reconnaissance Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.5.1",
      "label" : "2.1.2.5.1 Interrogation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.5.2",
      "label" : "2.1.2.5.2 Source Operations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.5.3",
      "label" : "2.1.2.5.3 Debriefing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.5.4",
      "label" : "2.1.2.5.4 Ground Reconnaissance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.5.5",
      "label" : "2.1.2.5.5 Biometrics Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.5.6",
      "label" : "2.1.2.5.6 Media Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3",
      "label" : "2.1.3 Processing / Exploitation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3.1",
      "label" : "2.1.3.1 Data Transformation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3.2",
      "label" : "2.1.3.2 Objective / Target Categorization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4",
      "label" : "2.1.4 Analysis and Production",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4.1",
      "label" : "2.1.4.1 Intelligence, Surveillance and Reconnaissance Analysis Integration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4.2",
      "label" : "2.1.4.2 Intelligence Evaluation and Interpretation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4.3",
      "label" : "2.1.4.3 Intelligence, Surveillance and Reconnaissance Product Generation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.5",
      "label" : "2.1.5 Intelligence, Surveillance and Reconnaissance Dissemination",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.5.1",
      "label" : "2.1.5.1 Real-Time Intelligence, Surveillance and Reconnaissance Data Transmission",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.5.2",
      "label" : "2.1.5.2 Intelligence, Surveillance and Reconnaissance Data Access",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2",
      "label" : "2.2 Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1",
      "label" : "2.2.1 Collect",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.1",
      "label" : "2.2.1.1 Collect Land Environmental Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.2",
      "label" : "2.2.1.2 Collect Ocean Environmental Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.3",
      "label" : "2.2.1.3 Collect Hydrographic Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.4",
      "label" : "2.2.1.4 Collect Bathymetric Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.5",
      "label" : "2.2.1.5 Collect Astrometry Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.6",
      "label" : "2.2.1.6 Collect Atmospheric Environmental Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.7",
      "label" : "2.2.1.7 Collect Space Environmental Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2",
      "label" : "2.2.2 Analyze",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.1",
      "label" : "2.2.2.1 Analyze Land Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.2",
      "label" : "2.2.2.2 Analyze Ocean Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.3",
      "label" : "2.2.2.3 Analyze Hydrographic Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.4",
      "label" : "2.2.2.4 Analyze Bathymetric Measurements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.5",
      "label" : "2.2.2.5 Analyze Atmospheric Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.6",
      "label" : "2.2.2.6 Analyze Space Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3",
      "label" : "2.2.3 Predict",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3.1",
      "label" : "2.2.3.1 Predict Land Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3.2",
      "label" : "2.2.3.2 Predict Ocean Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3.3",
      "label" : "2.2.3.3 Predict Atmospheric Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3.4",
      "label" : "2.2.3.4 Predict Space Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.4",
      "label" : "2.2.4 Exploit",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.4.1",
      "label" : "2.2.4.1 Determine Environmental Impacts",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.4.2",
      "label" : "2.2.4.2 Assess Environmental Effects",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.4.3",
      "label" : "2.2.4.3 Produce Environmental Decision Aids",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3",
      "label" : "3 Force Application",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1",
      "label" : "3.1 Maneuver",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1",
      "label" : "3.1.1 Maneuver to Engage (MTE)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.1",
      "label" : "3.1.1.1 Air (MTE)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.2",
      "label" : "3.1.1.2 Space (MTE)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.3",
      "label" : "3.1.1.3 Land (MTE)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.4",
      "label" : "3.1.1.4 Maritime (MTE)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.5",
      "label" : "3.1.1.5 Underground (MTE)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.6",
      "label" : "3.1.1.6 Underwater (MTE)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.7",
      "label" : "3.1.1.7 Cyberspace (MTE)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2",
      "label" : "3.1.2 Maneuver to Insert (MTI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.1",
      "label" : "3.1.2.1 Air (MTI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.2",
      "label" : "3.1.2.2 Space (MTI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.3",
      "label" : "3.1.2.3 Land (MTI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.4",
      "label" : "3.1.2.4 Maritime (MTI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.5",
      "label" : "3.1.2.5 Underground (MTI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.6",
      "label" : "3.1.2.6 Underwater (MTI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.7",
      "label" : "3.1.2.7 Cyberspace (MTI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3",
      "label" : "3.1.3 Maneuver to Influence (MTInfl)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.1",
      "label" : "3.1.3.1 Air (MTInfl)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.2",
      "label" : "3.1.3.2 Space (MTInfl)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.3",
      "label" : "3.1.3.3 Land (MTInfl)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.4",
      "label" : "3.1.3.4 Maritime (MTInfl)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.5",
      "label" : "3.1.3.5 Underground (MTInfl)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.6",
      "label" : "3.1.3.6 Underwater (MTInfl)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.7",
      "label" : "3.1.3.7 Cyberspace (MTInfl)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4",
      "label" : "3.1.4 Maneuver to Secure (MTS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.1",
      "label" : "3.1.4.1 Air (MTS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.2",
      "label" : "3.1.4.2 Space (MTS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.3",
      "label" : "3.1.4.3 Land (MTS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.3.1",
      "label" : "3.1.4.3.1 Populations (MTSL)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.3.2",
      "label" : "3.1.4.3.2 Infrastructure (MTSL)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.3.3",
      "label" : "3.1.4.3.3 Resources (MTSL)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.4",
      "label" : "3.1.4.4 Maritime (MTS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.5",
      "label" : "3.1.4.5 Underground (MTS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.6",
      "label" : "3.1.4.6 Underwater (MTS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.7",
      "label" : "3.1.4.7 Cyberspace (MTS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2",
      "label" : "3.2 Engagement",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1",
      "label" : "3.2.1 Kinetic Means",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1",
      "label" : "3.2.1.1 Fixed Target (EK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.1",
      "label" : "3.2.1.1.1 Surface (EKF)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.1.1",
      "label" : "3.2.1.1.1.1 Point (EKFS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.1.1.1",
      "label" : "3.2.1.1.1.1.1 Hardened (EKFSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.1.1.2",
      "label" : "3.2.1.1.1.1.2 Soft (EKFSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.1.1.3",
      "label" : "3.2.1.1.1.1.3 Chemical, Biological, Radiological and Nuclear (EKFSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.1.2",
      "label" : "3.2.1.1.1.2 Area (EKFS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.1.2.1",
      "label" : "3.2.1.1.1.2.1 Hardened (EKFSA)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.1.2.2",
      "label" : "3.2.1.1.1.2.2 Soft (EKFSA)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.2",
      "label" : "3.2.1.1.2 Underground (EKF)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.2.1",
      "label" : "3.2.1.1.2.1 Hardened (EKFU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.2.2",
      "label" : "3.2.1.1.2.2 Chemical, Biological, Radiological and Nuclear (EKFU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.3",
      "label" : "3.2.1.1.3 Underwater (EKF)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.3.1",
      "label" : "3.2.1.1.3.1 Surf Zone (EKFU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.3.2",
      "label" : "3.2.1.1.3.2 Very Shallow (EKFU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.3.3",
      "label" : "3.2.1.1.3.3 Shallow (EKFU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1.3.4",
      "label" : "3.2.1.1.3.4 Deep Water (EKFU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2",
      "label" : "3.2.1.2 Stationary Target (EK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.1",
      "label" : "3.2.1.2.1 Surface (EKS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.1.1",
      "label" : "3.2.1.2.1.1 Point (EKSS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.1.1.1",
      "label" : "3.2.1.2.1.1.1 Hardened (EKSSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.1.1.2",
      "label" : "3.2.1.2.1.1.2 Soft (EKSSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.1.1.3",
      "label" : "3.2.1.2.1.1.3 Chemical, Biological, Radiological and Nuclear (EKSSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.1.2",
      "label" : "3.2.1.2.1.2 Area (EKSS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.1.2.1",
      "label" : "3.2.1.2.1.2.1 Hardened (EKSSA)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.1.2.2",
      "label" : "3.2.1.2.1.2.2 Soft (EKSSA)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.2",
      "label" : "3.2.1.2.2 Underground (EKS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.2.1",
      "label" : "3.2.1.2.2.1 Sort (EKSU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.2.2",
      "label" : "3.2.1.2.2.2 Chemical, Biological, Radiological and Nuclear (EKSU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.3",
      "label" : "3.2.1.2.3 Underwater (EKS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.3.1",
      "label" : "3.2.1.2.3.1 Surf Zone (EKSU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.3.2",
      "label" : "3.2.1.2.3.2 Very Shallow (EKSU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.3.3",
      "label" : "3.2.1.2.3.3 Shallow (EKSU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2.3.4",
      "label" : "3.2.1.2.3.4 Deep Water (EKSU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3",
      "label" : "3.2.1.3 Moving Targets (EK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.1",
      "label" : "3.2.1.3.1 Air (EKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.2",
      "label" : "3.2.1.3.2 Space (EKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.3",
      "label" : "3.2.1.3.3 Surface (EKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.3.1",
      "label" : "3.2.1.3.3.1 Point (EKMS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.3.1.1",
      "label" : "3.2.1.3.3.1.1 Hardened (EKMSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.3.1.2",
      "label" : "3.2.1.3.3.1.2 Soft (EKMSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.3.1.3",
      "label" : "3.2.1.3.3.1.3 Chemical, Biological, Radiological and Nuclear (EKMSP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.3.2",
      "label" : "3.2.1.3.3.2 Area (EKMS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.3.2.1",
      "label" : "3.2.1.3.3.2.1 Hardened (EKMSA)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.3.2.2",
      "label" : "3.2.1.3.3.2.2 Soft (EKMSA)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.4",
      "label" : "3.2.1.3.4 Underground (EKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.4.1",
      "label" : "3.2.1.3.4.1 Soft (EKMU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.4.2",
      "label" : "3.2.1.3.4.2 Chemical, Biological, Radiological and Nuclear (EKMU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.5",
      "label" : "3.2.1.3.5 Underwater (EKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.5.1",
      "label" : "3.2.1.3.5.1 Surf Zone (EKMU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.5.2",
      "label" : "3.2.1.3.5.2 Very Shallow (EKMU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.5.3",
      "label" : "3.2.1.3.5.3 Shallow (EKMU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3.5.4",
      "label" : "3.2.1.3.5.4 Deep Water (EKMU)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2",
      "label" : "3.2.2 Non-Kinetic Means",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.1",
      "label" : "3.2.2.1 Fixed Target (ENK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.1.1",
      "label" : "3.2.2.1.1 Surface (ENKF)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.1.1.1",
      "label" : "3.2.2.1.1.1 Point (ENKFS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.1.1.2",
      "label" : "3.2.2.1.1.2 Area (ENKFS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.1.2",
      "label" : "3.2.2.1.2 Underground (ENKF)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.1.3",
      "label" : "3.2.2.1.3 Underwater (ENKF)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.2",
      "label" : "3.2.2.2 Stationary Target (ENK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.2.1",
      "label" : "3.2.2.2.1 Surface (ENKS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.2.1.1",
      "label" : "3.2.2.2.1.1 Point (ENKSS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.2.1.2",
      "label" : "3.2.2.2.1.2 Area (ENKSS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.2.2",
      "label" : "3.2.2.2.2 Underground (ENKS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.2.3",
      "label" : "3.2.2.2.3 Underwater (ENKS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.3",
      "label" : "3.2.2.3 Moving Targets (ENK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.3.1",
      "label" : "3.2.2.3.1 Air (ENKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.3.2",
      "label" : "3.2.2.3.2 Space (ENKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.3.3",
      "label" : "3.2.2.3.3 Surface (ENKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.3.3.1",
      "label" : "3.2.2.3.3.1 Point (ENKMS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.3.3.2",
      "label" : "3.2.2.3.3.2 Area (ENKMS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.3.4",
      "label" : "3.2.2.3.4 Underground (ENKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.3.5",
      "label" : "3.2.2.3.5 Underwater (ENKM)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.4",
      "label" : "3.2.2.4 Cyberspace (ENK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.4.1",
      "label" : "3.2.2.4.1 Computer Network Attack",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.4.2",
      "label" : "3.2.2.4.2 Computer Network Defense",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.5",
      "label" : "3.2.2.5 Electromagnetic Spectrum (ENK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.5.1",
      "label" : "3.2.2.5.1 Position, Navigation and Timing (ENKES)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.5.2",
      "label" : "3.2.2.5.2 Radar (ENKES)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.5.3",
      "label" : "3.2.2.5.3 Communication (ENKES)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.5.4",
      "label" : "3.2.2.5.4 ISR (ENKES)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2.6",
      "label" : "3.2.2.6 Chemical, Biological, Radiological and Nuclear (ENK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4",
      "label" : "4 Logistics",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1",
      "label" : "4.1 Deployment and Distribution",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1.1",
      "label" : "4.1.1 Move the Force",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1.1.1",
      "label" : "4.1.1.1 Strategically Move the Force",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1.1.2",
      "label" : "4.1.1.2 Operationally Move the Force",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1.2",
      "label" : "4.1.2 Sustain the Force",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1.2.1",
      "label" : "4.1.2.1 Deliver Non-Unit-Related Cargo",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1.2.2",
      "label" : "4.1.2.2 Deliver Non-Unit-Related Personnel",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.1.3",
      "label" : "4.1.3 Operate the JDDE",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.2",
      "label" : "4.2 Supply",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.2.1",
      "label" : "4.2.1 Manage Supplies and Equipment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.2.2",
      "label" : "4.2.2 Inventory Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.2.3",
      "label" : "4.2.3 Manage Supplier Networks",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3",
      "label" : "4.3 Maintain",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.1",
      "label" : "4.3.1 Inspect",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.2",
      "label" : "4.3.2 Test",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.3",
      "label" : "4.3.3 Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.3.1",
      "label" : "4.3.3.1 Activate / Inactivate",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.3.2",
      "label" : "4.3.3.2 Reclaim",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.4",
      "label" : "4.3.4 Repair",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.5",
      "label" : "4.3.5 Rebuild",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.5.1",
      "label" : "4.3.5.1 Modify",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.5.2",
      "label" : "4.3.5.2 Renovate",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.3.6",
      "label" : "4.3.6 Calibration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4",
      "label" : "4.4 Logistic Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.1",
      "label" : "4.4.1 Food Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.1.1",
      "label" : "4.4.1.1 Basecamp Feeding",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.1.2",
      "label" : "4.4.1.2 Forward Unit Feeding",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.1.3",
      "label" : "4.4.1.3 Remote Unit Feeding",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.1.4",
      "label" : "4.4.1.4 Installation Feeding",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.2",
      "label" : "4.4.2 Water and Ice Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.2.1",
      "label" : "4.4.2.1 Bulk Water (non-potable)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.2.2",
      "label" : "4.4.2.2 Bulk Water (potable)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.2.3",
      "label" : "4.4.2.3 Packaged Water (bottled/pouched)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.2.4",
      "label" : "4.4.2.4 Ice Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.2.5",
      "label" : "4.4.2.5 Water Reuse",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.3",
      "label" : "4.4.3 Basecamp Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.3.1",
      "label" : "4.4.3.1 Shelter",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.3.2",
      "label" : "4.4.3.2 Billeting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.3.3",
      "label" : "4.4.3.3 Utility Operations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.4",
      "label" : "4.4.4 Hygiene Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.4.1",
      "label" : "4.4.4.1 Personal Hygiene Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.4.4.2",
      "label" : "4.4.4.2 Textile Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.5",
      "label" : "4.5 Operational Contract Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.5.1",
      "label" : "4.5.1 Contract Support Integration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.5.2",
      "label" : "4.5.2 Contractor Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6",
      "label" : "4.6 Engineering",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.1",
      "label" : "4.6.1 General Engineering",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.1.1",
      "label" : "4.6.1.1 Gap Crossing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.1.2",
      "label" : "4.6.1.2 Develop and Maintain Facilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.1.3",
      "label" : "4.6.1.3 Establish Lines of Communications",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.1.4",
      "label" : "4.6.1.4 Global Access Engineering",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.1.5",
      "label" : "4.6.1.5 Repair and Restore Infrastructure",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.1.6",
      "label" : "4.6.1.6 Harden Key Infrastructure and Facilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.1.7",
      "label" : "4.6.1.7 Master Facility Design",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.2",
      "label" : "4.6.2 Combat Engineering",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.2.1",
      "label" : "4.6.2.1 Defeat Explosive Hazards",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.2.2",
      "label" : "4.6.2.2 Enhance Mobility",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.2.3",
      "label" : "4.6.2.3 Deny Enemy Freedom of Maneuver",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.2.4",
      "label" : "4.6.2.4 Enhance Survivability",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.3",
      "label" : "4.6.3 Geospatial Engineering",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.3.1",
      "label" : "4.6.3.1 Utilize Geospatial Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.3.2",
      "label" : "4.6.3.2 Provide Mobility Assessments",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7",
      "label" : "4.7 Installations Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.1",
      "label" : "4.7.1 Real Property Life Cycle Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.1.1",
      "label" : "4.7.1.1 Provide Installation Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.1.2",
      "label" : "4.7.1.2 Facilities Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.1.3",
      "label" : "4.7.1.3 Sustainment of Installation Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.1.4",
      "label" : "4.7.1.4 Recapitalization of Installation Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.1.5",
      "label" : "4.7.1.5 Disposal of Installation Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.1.6",
      "label" : "4.7.1.6 Economic Adjustment Activities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2",
      "label" : "4.7.2 Installation Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.1",
      "label" : "4.7.2.1 Security Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.1.1",
      "label" : "4.7.2.1.1 Law Enforcement",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.1.2",
      "label" : "4.7.2.1.2 Base Physical Security",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.2",
      "label" : "4.7.2.2 Emergency Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.3",
      "label" : "4.7.2.3 Installation Safety",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.4",
      "label" : "4.7.2.4 Base Support Vehicles and Equipment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.5",
      "label" : "4.7.2.5 Housing Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.6",
      "label" : "4.7.2.6 Airfield Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.7",
      "label" : "4.7.2.7 Port Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2.8",
      "label" : "4.7.2.8 Range Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5",
      "label" : "5 Command and Control",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1",
      "label" : "5.1 Organize",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.1",
      "label" : "5.1.1 Establish and Maintain Unity of Effort with Mission Partners",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.1.1",
      "label" : "5.1.1.1 Cultivate Relations with Mission Partners",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.1.2",
      "label" : "5.1.1.2 Cultivate Coordination with Partner Organizations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.2",
      "label" : "5.1.2 Structure Organization to Mission",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.2.1",
      "label" : "5.1.2.1 Define Structure",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.2.2",
      "label" : "5.1.2.2 Assess Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.2.3",
      "label" : "5.1.2.3 Assign Roles and Responsibilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.2.4",
      "label" : "5.1.2.4 Integrate Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.2.5",
      "label" : "5.1.2.5 Establish Commanders’ Expectations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.3",
      "label" : "5.1.3 Foster Organizational Collaboration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.3.1",
      "label" : "5.1.3.1 Establish Collaboration Policies",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.1.3.2",
      "label" : "5.1.3.2 Establish Collaborative Procedures",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2",
      "label" : "5.2 Understand",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.1",
      "label" : "5.2.1 Organize Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.1.1",
      "label" : "5.2.1.1 Compile Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.1.2",
      "label" : "5.2.1.2 Distill Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.1.3",
      "label" : "5.2.1.3 Disemminate Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.2",
      "label" : "5.2.2 Develop Knowledge and Situational Awareness",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.2.1",
      "label" : "5.2.2.1 Understand Implications",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.2.2",
      "label" : "5.2.2.2 Analyze Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.2.3",
      "label" : "5.2.2.3 Define Knowledge Structure",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.3",
      "label" : "5.2.3 Share Knowledge and Situational Awareness",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.3.1",
      "label" : "5.2.3.1 Define Associated Community",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.3.2",
      "label" : "5.2.3.2 Establish Collective Meaning (Collaboration)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.2.3.3",
      "label" : "5.2.3.3 Prepare Distributable Context",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3",
      "label" : "5.3 Planning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.1",
      "label" : "5.3.1 Analyze problem",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.1.1",
      "label" : "5.3.1.1 Analyze Situation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.1.2",
      "label" : "5.3.1.2 Document Problem Elements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.2",
      "label" : "5.3.2 Apply Situational Understanding",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.2.1",
      "label" : "5.3.2.1 Evaluate Operational Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.2.2",
      "label" : "5.3.2.2 Determine Vulnerabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.2.3",
      "label" : "5.3.2.3 Determine Opportunities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.3",
      "label" : "5.3.3 Develop Strategy",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.3.1",
      "label" : "5.3.3.1 Determine End State",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.3.2",
      "label" : "5.3.3.2 Develop Assumptions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.3.3",
      "label" : "5.3.3.3 Develop Objectives",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.4",
      "label" : "5.3.4 Develop Courses of Action",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.4.1",
      "label" : "5.3.4.1 Assess Available Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.4.2",
      "label" : "5.3.4.2 Understand Objectives",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.4.3",
      "label" : "5.3.4.3 Develop Options",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.5",
      "label" : "5.3.5 Analyze Course of Action",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.5.1",
      "label" : "5.3.5.1 Establish Selection Criteria",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.5.2",
      "label" : "5.3.5.2 Evaluate Courses of Actions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4",
      "label" : "5.4 Decide",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4.1",
      "label" : "5.4.1 Manage Risk",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4.2",
      "label" : "5.4.2 Select Actions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4.2.1",
      "label" : "5.4.2.1 Select Course of Action",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4.2.2",
      "label" : "5.4.2.2 Select Plan",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4.3",
      "label" : "5.4.3 Establish Rule Sets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4.4",
      "label" : "5.4.4 Establish Intent and Guidance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4.5",
      "label" : "5.4.5 Intuit",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5",
      "label" : "5.5 Direct",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.1",
      "label" : "5.5.1 Communicate Intent and Guidance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.1.1",
      "label" : "5.5.1.1 Issue Estimates",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.1.2",
      "label" : "5.5.1.2 Issue Priorities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.1.3",
      "label" : "5.5.1.3 Issue Rule Sets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.1.4",
      "label" : "5.5.1.4 Provide CONOPS",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.1.5",
      "label" : "5.5.1.5 Provide Warnings",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.1.6",
      "label" : "5.5.1.6 Issues Alerts",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.2",
      "label" : "5.5.2 Task",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.2.1",
      "label" : "5.5.2.1 Synchronize Operations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.2.2",
      "label" : "5.5.2.2 Issue Plans",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.2.3",
      "label" : "5.5.2.3 Issue Orders",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.3",
      "label" : "5.5.3 Establish Metrics",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.3.1",
      "label" : "5.5.3.1 Establish Measures of Performance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.5.3.2",
      "label" : "5.5.3.2 Establish Measures of Effectiveness",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.6",
      "label" : "5.6 Monitor",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.6.1",
      "label" : "5.6.1 Assess Compliance with Guidance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.6.1.1",
      "label" : "5.6.1.1 Assess Employment of Forces",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.6.1.2",
      "label" : "5.6.1.2 Assess Manner of Employment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.6.2",
      "label" : "5.6.2 Assess Effects",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.6.3",
      "label" : "5.6.3 Assess Achievement of Objectives",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.6.4",
      "label" : "5.6.4 Assess Guidance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6",
      "label" : "6 Net-Centric",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1",
      "label" : "6.1 Information Transport (IT)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.1",
      "label" : "6.1.1 Wired Transmission",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.1.1",
      "label" : "6.1.1.1 Localized Communications",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.1.2",
      "label" : "6.1.1.2 Long-Haul Telecommunications",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.2",
      "label" : "6.1.2 Wireless Transmission",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.2.1",
      "label" : "6.1.2.1 Line of Sight",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.2.2",
      "label" : "6.1.2.2 Beyond Line of Sight",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.3",
      "label" : "6.1.3 Switching and Routing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.3.1",
      "label" : "6.1.3.1 Communication Bridge",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.1.3.2",
      "label" : "6.1.3.2 Communication Gateway",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2",
      "label" : "6.2 Enterprise Services (ES)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.1",
      "label" : "6.2.1 Information Sharing/Computing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.1.1",
      "label" : "6.2.1.1 Information Sharing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.1.2",
      "label" : "6.2.1.2 Computing Infrastructure",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.1.2.1",
      "label" : "6.2.1.2.1 Shared Computing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.1.2.2",
      "label" : "6.2.1.2.2 Distributed Computing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.2",
      "label" : "6.2.2 Core Enterprise Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.2.1",
      "label" : "6.2.2.1 User Access (Portal)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.2.2",
      "label" : "6.2.2.2 Collaboration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.2.3",
      "label" : "6.2.2.3 Content Discovery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.2.4",
      "label" : "6.2.2.4 Content Delivery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.2.5",
      "label" : "6.2.2.5 Common Identity Assurance Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.2.6",
      "label" : "6.2.2.6 Enterprise Messaging",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.2.7",
      "label" : "6.2.2.7 Directory Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.3",
      "label" : "6.2.3 Position, Navigation, and Timing (PNT)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.3.1",
      "label" : "6.2.3.1 Provide PNT Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.2.3.2",
      "label" : "6.2.3.2 Utilize PNT Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3",
      "label" : "6.3 Net Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.1",
      "label" : "6.3.1 Optimized Network Functions and Resources",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.1.1",
      "label" : "6.3.1.1 Network Resource Visibility",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.1.2",
      "label" : "6.3.1.2 Rapid Configuration Change",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.2",
      "label" : "6.3.2 Deployable Scalable and Modular Networks",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.3",
      "label" : "6.3.3 Spectrum Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.3.1",
      "label" : "6.3.3.1 Spectrum Monitoring",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.3.2",
      "label" : "6.3.3.2 Spectrum Assignment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.3.3",
      "label" : "6.3.3.3 Spectrum Deconfliction",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.3.4",
      "label" : "6.3.4 Cyber Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4",
      "label" : "6.4 Information Assurance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.1",
      "label" : "6.4.1 Secure Information Exchange",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.1.1",
      "label" : "6.4.1.1 Assure Access",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.1.2",
      "label" : "6.4.1.2 Assure Transfer",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.2",
      "label" : "6.4.2 Protect Data and Networks",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.2.1",
      "label" : "6.4.2.1 Protect Against Network Infiltration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.2.2",
      "label" : "6.4.2.2 Protect Against Denial or Degradation of Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.2.3",
      "label" : "6.4.2.3 Protect Against Disclosure or Modification of Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.3",
      "label" : "6.4.3 Respond to Attack / Event",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.3.1",
      "label" : "6.4.3.1 Detect Events",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.3.2",
      "label" : "6.4.3.2 Analyze Events",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "6.4.3.3",
      "label" : "6.4.3.3 Respond to Incidents",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7",
      "label" : "7 Protection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1",
      "label" : "7.1 Prevent",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1",
      "label" : "7.1.1 Prevent Kinetic Attack",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.1",
      "label" : "7.1.1.1 Above (PK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.1.1",
      "label" : "7.1.1.1.1 Maneuvering (PKA)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.1.2",
      "label" : "7.1.1.1.2 Non-Maneuvering (PKA)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.2",
      "label" : "7.1.1.2 Surface (PK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.2.1",
      "label" : "7.1.1.2.1 Maneuvering (PKS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.2.2",
      "label" : "7.1.1.2.2 Non-Maneuvering (PKS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.3",
      "label" : "7.1.1.3 Sub-surface Kinetic (PK)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.3.1",
      "label" : "7.1.1.3.1 Maneuvering (PKSS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.1.3.2",
      "label" : "7.1.1.3.2 Non-Maneuvering (PKSS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.2",
      "label" : "7.1.2 Prevent Non-kinetic Attack",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.2.1",
      "label" : "7.1.2.1 Above Surface (PN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.2.2",
      "label" : "7.1.2.2 Surface (PN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.2.3",
      "label" : "7.1.2.3 Sub-Surface (PN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2",
      "label" : "7.2 Mitigate",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1",
      "label" : "7.2.1 Mitigate Lethal Effects",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.1",
      "label" : "7.2.1.1 Chemical (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.2",
      "label" : "7.2.1.2 Biological (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.2.1",
      "label" : "7.2.1.2.1 Contagious (MLB)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.2.2",
      "label" : "7.2.1.2.2 Non-contagious (MLB)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.3",
      "label" : "7.2.1.3 Radiological (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.4",
      "label" : "7.2.1.4 Nuclear (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.5",
      "label" : "7.2.1.5 Electro Magnetic Pulse (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.6",
      "label" : "7.2.1.6 Explosives (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.7",
      "label" : "7.2.1.7 Projectiles (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.8",
      "label" : "7.2.1.8 Directed Energy (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.1.9",
      "label" : "7.2.1.9 Natural Hazards (ML)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2",
      "label" : "7.2.2 Mitigate Non-lethal Effects",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.1",
      "label" : "7.2.2.1 Chemical (MN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.2",
      "label" : "7.2.2.2 Biological (MN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.2.1",
      "label" : "7.2.2.2.1 Contagious (MNB)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.2.2",
      "label" : "7.2.2.2.2 Non-contagious (MNB)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.3",
      "label" : "7.2.2.3 Electro Magnetic Pulse (MN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.4",
      "label" : "7.2.2.4 Explosives (MN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.5",
      "label" : "7.2.2.5 Projectiles (MN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.6",
      "label" : "7.2.2.6 Directed Energy (MN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.7",
      "label" : "7.2.2.7 Electro-Magnetic Spectrum (MN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.2.8",
      "label" : "7.2.2.8 Natural Hazards (MN)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.3",
      "label" : "7.3 Research and Development",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.3.1",
      "label" : "7.3.1 Basic Research",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.3.2",
      "label" : "7.3.2 Applied Research",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.3.3",
      "label" : "7.3.3 Advanced Technology Development",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8",
      "label" : "8 Building Partnerships",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1",
      "label" : "8.1 Communicate",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.1",
      "label" : "8.1.1 Inform Domestic and Foreign Audiences",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.1.1",
      "label" : "8.1.1.1 Develop Objective Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.1.2",
      "label" : "8.1.1.2 Identify Misinformation and Disinformation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.1.3",
      "label" : "8.1.1.3 Deliver and Adjust Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.2",
      "label" : "8.1.2 Persuade Partner Audiences",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.2.1",
      "label" : "8.1.2.1 Identify Foreign Audience Attitudes",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.2.2",
      "label" : "8.1.2.2 Develop Cognitive Programs and Products",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.2.3",
      "label" : "8.1.2.3 Deliver and Adjust Persuasive Content",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.3",
      "label" : "8.1.3 Influence Adversary and Competitor Audiences",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.3.1",
      "label" : "8.1.3.1 Identify Adversary and Competitor Attitudes",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.3.2",
      "label" : "8.1.3.2 Develop Influential Programs and Products",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.3.3",
      "label" : "8.1.3.3 Deliver and Adjust Influential Content",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2",
      "label" : "8.2 Shape",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.1",
      "label" : "8.2.1 Partner with Governments and Institutions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.1.1",
      "label" : "8.2.1.1 Engage Partners",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.1.2",
      "label" : "8.2.1.2 Develop Partnership Agreements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.2",
      "label" : "8.2.2 Provide Aid to Foreign Partners and Institutions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.2.1",
      "label" : "8.2.2.1 Identify Aid Requirements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.2.2",
      "label" : "8.2.2.2 Supply Partner Aid",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.3",
      "label" : "8.2.3 Build Capabilities and Capacities of Partners and Institutions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.3.1",
      "label" : "8.2.3.1 Determine Partner Requirements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.3.2",
      "label" : "8.2.3.2 Enhance Partner Capabilities and Capacities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.4",
      "label" : "8.2.4 Leverage Capacities and Capabilities of Security Estabs",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.4.1",
      "label" : "8.2.4.1 Identify Foreign Security-Related Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.4.2",
      "label" : "8.2.4.2 Determine Utility of Foreign Security-Related Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.4.3",
      "label" : "8.2.4.3 Stimulate the Use of Foreign Security-Related Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.5",
      "label" : "8.2.5 Strengthen Global Defense Posture",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9",
      "label" : "9 Corporate Management and Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1",
      "label" : "9.1 Advisory and Compliance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.1",
      "label" : "9.1.1 Advice and External Matters",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.1.1",
      "label" : "9.1.1.1 Legal Matters",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.1.2",
      "label" : "9.1.1.2 Legislative Matters",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.2",
      "label" : "9.1.2 Audit, Inspection and Investigation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.2.1",
      "label" : "9.1.2.1 Audits",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.2.2",
      "label" : "9.1.2.2 Inspections",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.2.3",
      "label" : "9.1.2.3 Investigations  ",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.3",
      "label" : "9.1.3 Operational Test and Evaluation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2",
      "label" : "9.2 Strategy and Assessment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.1",
      "label" : "9.2.1 Strategy Development",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.2",
      "label" : "9.2.2 Capabilities Development",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.3",
      "label" : "9.2.3 Enterprise-Wide Assessment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.4",
      "label" : "9.2.4 Studies and Analyses",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.3",
      "label" : "9.3 Information Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.3.1",
      "label" : "9.3.1 Enterprise Architecture",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.4",
      "label" : "9.4 Acquisition",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.4.1",
      "label" : "9.4.1 Acquisition Program Execution",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.4.2",
      "label" : "9.4.2 Contracting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.5",
      "label" : "9.5 Program, Budget and Finance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.5.1",
      "label" : "9.5.1 Program / Budget and Performance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.5.2",
      "label" : "9.5.2 Accounting and Finance",
      "description" : null,
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "SERPROTOCAL",
    "description" : "Service Transport Protocal",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : true,
    "codes" : [ {
      "code" : "SOAP",
      "label" : "SOAP",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "REST",
      "label" : "REST",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "XMLRPC",
      "label" : "XMLRPC",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "CUSTOM",
      "label" : "CUSTOM",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "OTH",
      "label" : "Other",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "DI2ESTATE",
    "description" : "DI2E State",
    "visibleFlg" : true,
    "requiredFlg" : true,
    "archtechtureFlg" : false,
    "importantFlg" : true,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "NOTEVAL",
      "label" : "Not evaluated",
      "description" : "Evaluation has not started",
      "fullTextAvailable" : false
    }, {
      "code" : "DI2ECAN",
      "label" : "DI2E Candidate",
      "description" : "Component is considered as a potential reuse candidate but has not completed the DI2E Framework Evaluation Process.",
      "fullTextAvailable" : false
    }, {
      "code" : "DI2ECOM",
      "label" : "DI2E Component",
      "description" : "Component has been through the DI2E Evaluation Process and determined to be reusable and interoperable",
      "fullTextAvailable" : false
    }, {
      "code" : "NOTREUSE",
      "label" : "Not DI2E Reusable",
      "description" : "Component was evaluated and determined to not support reusability, is not interoperable, or does not conform to DI2E standards and specifications",
      "fullTextAvailable" : false
    }, {
      "code" : "OBSOLETE",
      "label" : "Obsolete",
      "description" : "Component has been retired, superseded, or is generally considered not reusable by DI2E",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "DATAR",
    "description" : "Data Rights",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "UR",
      "label" : "Unlimited Rights (UR)",
      "description" : "All uses; no restrictions",
      "fullTextAvailable" : false
    }, {
      "code" : "GPR",
      "label" : "Government Purpose Rights (GPR)",
      "description" : "For “Government Purposes” only; no commercial use",
      "fullTextAvailable" : false
    }, {
      "code" : "LR",
      "label" : "Limited Rights (LR)",
      "description" : "Unlimited; except may not be used for manufacture",
      "fullTextAvailable" : false
    }, {
      "code" : "RR",
      "label" : "Restricted Rights (RR)",
      "description" : "Only one computer at a time; minimum backup copies; modification",
      "fullTextAvailable" : false
    }, {
      "code" : "NLR",
      "label" : "Negotiated License Rights",
      "description" : "As negotiated by the parties; however, must not be less than LR in TD and must not be less than RR in noncommercial CS (consult with legal counsel as other limits apply)",
      "fullTextAvailable" : false
    }, {
      "code" : "SBIR",
      "label" : "SBIR Data Rights",
      "description" : "All uses; no restrictions",
      "fullTextAvailable" : false
    }, {
      "code" : "COMTD",
      "label" : "Commercial TD License Rights",
      "description" : "Unlimited in FFF and OMIT; other rights as negotiated.",
      "fullTextAvailable" : false
    }, {
      "code" : "COMCS",
      "label" : "Commercial CS Licenses",
      "description" : "As specified in the commercial license customarily offered to the public4",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "OWFCOMP",
    "description" : "OWF Compatible",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "Y",
      "label" : "Yes",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "N",
      "label" : "No",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "JCFALIGNMENT",
    "description" : "JCFSL Alignment",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : true,
    "importantFlg" : true,
    "allowMutlipleFlg" : true,
    "codes" : [ {
      "code" : "1.2.8",
      "label" : "1.2.8 Search",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.8",
      "label" : "8.7.8  Query Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.11",
      "label" : "8.7.11  Formulate Discovery Search",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.63",
      "label" : "8.7.63  Search within Context",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.65",
      "label" : "8.7.65  Perform Text Searches",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.95",
      "label" : "8.7.95  Search Structured and Unstructured Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.96",
      "label" : "8.7.96 Review Information Search Results",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.97",
      "label" : "8.7.97  Report Query Results",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.27",
      "label" : "8.7.27  Retrieve Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.91",
      "label" : "8.7.91 Acquire Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.121",
      "label" : "8.7.121 Receive Queried Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.7",
      "label" : "7.1.7  Transfer Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.24",
      "label" : "8.7.24 Manage Information Delivery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.100",
      "label" : "8.7.100 Provide Information Delivery Vehicle",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.4",
      "label" : "1.4.4 Control Search Parameters",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.1",
      "label" : "8.2.1  Perform Network Information Assurance (IA)/Computer Network Defense (CND) Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.3",
      "label" : "8.2.3 Regulate Information Dissemination",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.17",
      "label" : "8.8.17  Monitor Access Control",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.9.3",
      "label" : "8.9.3  Provide Environment Control Policy Enforcement Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.11",
      "label" : "8.2.11 Provide Identity Management Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.3",
      "label" : "8.8.3 Manage Network Information Access for User Account",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.5",
      "label" : "8.8.5 Assign Roles and Privileges",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.6",
      "label" : "8.8.6 Create and Maintain Network User Account and Profile",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.18",
      "label" : "8.8.18 Delete User",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.19",
      "label" : "8.8.19 Request System Access",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.22",
      "label" : "8.8.22 Manage Local Information Access for User Account",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.23",
      "label" : "8.8.23 Create and Maintain Local User Account and Profile",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.6",
      "label" : "8.2.6 Provide Entity Authentication Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.57",
      "label" : "8.1.57 - Authorize Network Resource Request",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.20",
      "label" : "8.2.20  Authorize Data Access",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.9.2",
      "label" : "8.9.2  Provide Environment Control Policy Enforcement Decisions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.68",
      "label" : "8.2.68 Determine Network Security Requirements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.13",
      "label" : "8.2.13  Implement National Security Agency (NSA) and/or National Institute of Standards and Technology (NIST) Public Key Cryptography",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.15",
      "label" : "8.2.15  Provide Cryptographic Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.34",
      "label" : "8.2.34  Generate Network Keys",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.8",
      "label" : "8.8.8  Manage Cryptographic Accounts",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.33",
      "label" : "8.2.33  Distribute and Enable Network Keys",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.35",
      "label" : "8.2.35  Manage Network Keys",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.36",
      "label" : "8.2.36 Order Network Keys",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.18",
      "label" : "8.2.18  Perform Registration Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.19",
      "label" : "8.2.19 Validate User Credentials",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3",
      "label" : "3.3 Provide Indications, Warnings, and Alerts - Group",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.55",
      "label" : "7.1.55  Manage Messaging Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.13",
      "label" : "3.3.13 Manage Alerts and Indications",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.4",
      "label" : "7.1.4  Send Messages on Timed Delivery Basis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.77",
      "label" : "7.1.77 Coordinate Message Delivery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.11",
      "label" : "8.1.11 Provide MessageServices",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.5.32",
      "label" : "8.5.32 Reroute Messages",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.55",
      "label" : "8.1.55 - Provide Subscription Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.120",
      "label" : "7.1.120 Communicate Information on the Secret Internet Protocol Router Network (SIPRnet)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.121",
      "label" : "7.1.121 Communicate Information on the Non-Classified Internet Protocol Router Network (NIPRnet)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.122",
      "label" : "7.1.122 Communicate Information on the Joint Worldwide Intelligence Communications System (JWICS) Network",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.123",
      "label" : "7.1.123 Communicate Information on the Coalition Mission Network",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.22",
      "label" : "8.2.22 Access Information at Multiple Levels of Security",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.27",
      "label" : "8.2.27 Manage Multiple Security Levels",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.52",
      "label" : "8.2.52 Manage Security Configuration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.12",
      "label" : "3.4.12 Collect Performance Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.18",
      "label" : "3.4.18 Display Performance Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.9",
      "label" : "9.2.9 Record Real-Time System Performance and Condition Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.17",
      "label" : "1.4.17  Determine Time to Sensor Availability",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.18",
      "label" : "1.4.18  Identify Elapsing Platform and Sensor Tasking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.20",
      "label" : "1.1.20 Perform Signal Parametric Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1",
      "label" : "1.4.1 Fuse Sensor Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.6",
      "label" : "1.4.6 Coordinate Sensor Employment with Operational Plans and Employment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.27",
      "label" : "1.4.27 Reconfigure Netted Sensor Grid Dynamically",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.32",
      "label" : "1.4.32 Convert Geospatial Intelligence (GEOINT) Sensor Requests",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.5",
      "label" : "3.3.5 Generate Alert",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "LAYER",
      "label" : "Layer",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.3",
      "label" : "3.4.3 Receive, Store and Maintain Geospatial Product Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.2",
      "label" : "14.1.2 Produce Imagery Intelligence (IMINT) Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.5",
      "label" : "14.1.5 Collect and Process Video Sensor Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.9",
      "label" : "7.2.9 Transmit Tasking and Target Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.31",
      "label" : "14.1.31 Maintain Target Model Library",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.32",
      "label" : "14.1.32 Retrieve Target Model",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.33",
      "label" : "14.1.33 Distribute Target Model",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.39",
      "label" : "2.1.39  Acquire and Track Target",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1",
      "label" : "3.4.1  Display Automatic Target Recognition (ATR)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.34",
      "label" : "14.1.34 Publish Target Model",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7.2",
      "label" : "4.7.2  Manage Target List",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.23",
      "label" : "14.1.23 Perform Imagery Coordinate Mensuration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.8",
      "label" : "7.2.8 Transmit Tasking and Target Data to Battle Damage Assessment (BDA) Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.2.51",
      "label" : "7.2.51 Transmit Battle Damage Assessment (BDA) Report",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.51",
      "label" : "7.1.51 Broadcast Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.95",
      "label" : "7.1.95 Disseminate Sensor Products",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.22",
      "label" : "8.7.22 Disseminate Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.11",
      "label" : "14.1.11 Disseminate Intelligence Products",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.52",
      "label" : "3.2.52 Display Map",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.4.10",
      "label" : "8.4.10 Generate Displays",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.14",
      "label" : "8.7.14  Delete Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.19",
      "label" : "8.7.19  Categorize Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.25",
      "label" : "8.7.25  Register Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.28",
      "label" : "8.7.28  Provide Database Utilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.36",
      "label" : "8.7.36  Post or Publish Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.39",
      "label" : "8.7.39  Aggregate Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.71",
      "label" : "8.7.71  Store Semantic Information on Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.66",
      "label" : "8.7.66  Post Metadata to a Discovery Catalog",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.67",
      "label" : "8.7.67  Post Metadata to a Federated Directory",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.68",
      "label" : "8.7.68  Post Metadata to Metadata Registries",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.89",
      "label" : "8.7.89  Provide Discovery Metadata for Situational Awareness",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.10.10",
      "label" : "8.10.10 Provide Graphical User Interface (GUI) Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.10.22",
      "label" : "8.10.22 Manage Display of Symbology",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.10.23",
      "label" : "8.10.23 Display Tabular Sortable Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.10.25",
      "label" : "8.10.25 Display Video",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.7",
      "label" : "1.1.7  Register Electromagnetic Signals",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.32",
      "label" : "3.2.32 Mensurate Object Coordinates",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.26",
      "label" : "8.2.26 Configure Communications and Security Devices",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.12.4",
      "label" : "8.12.4 Allocate and Manage Collaboration Communications",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.3",
      "label" : "4.6.3  Predict Situation Effects",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.19",
      "label" : "4.6.19  Predict Collateral Damage",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.21",
      "label" : "4.6.21  Calculate Probability of Engagement Effectiveness",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.23",
      "label" : "4.6.23  Analyze Force Vulnerability",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.6.27",
      "label" : "4.6.27  Determine Time to Complete the Mission",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.6",
      "label" : "5.3.6  Model and Simulate Mission Scenarios",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.8",
      "label" : "5.3.8   Generate Wargaming Scenarios",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.9",
      "label" : "5.3.9  Model and Simulate Risk Scenarios",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.3.10",
      "label" : "5.3.10  Identify Risk using Modeling and Simulation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.13",
      "label" : "1.1.13  Process Acoustic Sensor Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.16",
      "label" : "14.1.16 Disseminate High Priority Tactical Signals Intelligence (SIGINT) Report",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.28",
      "label" : "14.1.28  Disseminate Tactical Signals Intelligence (SIGINT) Report",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1",
      "label" : "1.1.1 Receive and Process Signals",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.25",
      "label" : "1.1.25 Pre-Process Sensor Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.1",
      "label" : "14.1.1 Produce Signals Intelligence (SIGINT) Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.29",
      "label" : "14.1.29 Validate Processed Sensor Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.50",
      "label" : "7.1.50 Translate Foreign Languages",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.3",
      "label" : "14.1.3 Produce Measurement and Signature Intelligence (MASINT) Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.22",
      "label" : "1.1.22 Register Images",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.4.21",
      "label" : "8.4.21 Process Imagery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.8",
      "label" : "14.1.8  Rectify Image",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.22",
      "label" : "14.1.22  Correlate Image",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.7",
      "label" : "14.1.7 Manage Images",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.24",
      "label" : "14.1.24 Provide Graphical Intelligence Preparation of the Operational Environment (IPOE) Products",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.25",
      "label" : "14.1.25 Provide Data Assets from Intelligence Preparation of the Operational Environment (IPOE) Products",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.26",
      "label" : "14.1.26 Conduct Joint Intelligence Preparation of the Operational Environment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2",
      "label" : "2.2 Combat Identification (CID) (Group)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5",
      "label" : "3.4.5 Maintain Shared Situational Awareness",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8",
      "label" : "3.4.8 Manage Prioritization of Defended Asset Information Sets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.11",
      "label" : "3.4.11 Translate and Distribute Commander's Intent",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1",
      "label" : "2.1.1  Form Tracks",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.33",
      "label" : "2.1.33  Provide Tracking Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.20",
      "label" : "3.4.20 Maintain Track Management Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.21",
      "label" : "3.4.21 Display Tracks",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.4",
      "label" : "14.1.4  Produce Human Intelligence (HUMINT) Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.14",
      "label" : "14.1.14 Manage Human Intelligence (HUMINT) Dossier",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.15",
      "label" : "14.1.15 Manage Human Intelligence (HUMINT) Sources",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.16DETERMINE",
      "label" : "1.4.16Determine Availability of Joint Intelligence, Surveillance, and Reconnaissance (JISR) Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.13",
      "label" : "8.7.13 Compress Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.16",
      "label" : "8.7.16 Migrate Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.81",
      "label" : "8.7.81 Store Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.82",
      "label" : "8.7.82 Retain Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.112",
      "label" : "8.7.112 Modify Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.115",
      "label" : "8.7.115 Import Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.116",
      "label" : "8.7.116 Export Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.117",
      "label" : "8.7.117 Retrieve Recorded Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.15",
      "label" : "8.7.15  Transform Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.40",
      "label" : "8.7.40 Format Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.54",
      "label" : "8.7.54 Convert Analog Data to Digital Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.104",
      "label" : "8.7.104  Convert Data File Format",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.36",
      "label" : "3.2.36  Generate Coordinates",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.44",
      "label" : "3.2.44  Display Range and Bearing Between Objects",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.21",
      "label" : "14.1.21  Extract Automated Features",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.34",
      "label" : "8.7.34 Validate Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.7",
      "label" : "8.7.7  Maintain Data Integrity",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.44",
      "label" : "8.7.44 Mediate Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.76",
      "label" : "8.7.76 Provide Data Mediation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.98",
      "label" : "8.7.98  Perform Data Check",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.5",
      "label" : "8.7.5 Translate Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.9",
      "label" : "14.1.9  Convert Image Format",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.17",
      "label" : "8.7.17 Cleanse Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.120",
      "label" : "8.7.120 Deconflict Duplicate Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.31",
      "label" : "8.1.31 Maintain Global Directory Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.7",
      "label" : "8.2.7 Decrypt Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.8",
      "label" : "8.2.8 Encrypt Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.12",
      "label" : "8.2.12 Provide Embedded Programmable Cryptographic Capability",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.9",
      "label" : "8.2.9 Maintain Secure Data Transmission",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.10",
      "label" : "8.2.10 Monitor Network Security",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.16",
      "label" : "8.2.16 Provide Transmission Security",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.32",
      "label" : "8.2.32 Mitigate Opportunity to Attack",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.54",
      "label" : "8.2.54 Perform Network Vulnerability Scanning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.8",
      "label" : "3.3.8 Maintain Network Operations (NetOps) Related Threat Assessment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.31",
      "label" : "8.2.31 Manage Threats to Network",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.55",
      "label" : "8.2.55 Disseminate Vulnerability Assessment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.2",
      "label" : "8.2.2 Detect Security Events",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.40",
      "label" : "8.2.40 Implement Firewall Protection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.21",
      "label" : "8.2.21 Respond to Security Events",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.50",
      "label" : "8.2.50 Drop Host Transactions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1",
      "label" : "3.2.1 Display Common Operational Picture (COP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.2",
      "label" : "3.2.2 Display Common Tactical Picture (CTP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.6",
      "label" : "3.5.6 Access Joint Asset Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.8",
      "label" : "3.5.8 Access Unit Readiness and Logistics Reports",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.9",
      "label" : "3.5.9 Enter, Display, Update, and Monitor Force Status",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.10",
      "label" : "3.5.10 Present Mission Resources Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.14",
      "label" : "3.5.14 Confirm Asset or Sensor Availability",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.16",
      "label" : "3.5.16 Identify and Catalog Joint Assets and Activities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.24",
      "label" : "3.5.24  Monitor Data Link Status",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.26",
      "label" : "3.5.26  Display System Status",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.27",
      "label" : "3.5.27 Monitor System Status",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.13",
      "label" : "8.1.13  Maintain Network and Communication Status",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.16",
      "label" : "8.1.16  Manage Network Performance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.24",
      "label" : "8.1.24  Monitor and Assess Network",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.25",
      "label" : "8.1.25  Manage Status of Network Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.26",
      "label" : "8.1.26  Perform System and Network Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.32",
      "label" : "8.1.32  Calculate Network Loads",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.33",
      "label" : "8.1.33  Calculate Processing Availability",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.67",
      "label" : "8.1.67  Manage Message Traffic",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.77",
      "label" : "8.1.77  Assess Network Equipment Performance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.81",
      "label" : "8.1.81  Balance Processing Loads",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.84",
      "label" : "8.1.84  Track Network User Activity",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.5.11",
      "label" : "8.5.11  Monitor Network Usage",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.62",
      "label" : "8.7.62  Monitor System and Resource Allocation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.15",
      "label" : "9.2.15  Report System Status",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.16",
      "label" : "9.2.16  Maintain System Status",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.3.10",
      "label" : "9.3.10 Receive and Register System Status",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.18",
      "label" : "9.1.18 Detect Equipment Faults",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.3.8",
      "label" : "9.3.8 Monitor and Report Software Faults",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.3.9",
      "label" : "9.3.9 Monitor and Report Hardware System Faults",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.19",
      "label" : "9.1.19 Provide On- Fault Isolation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.4",
      "label" : "9.2.4 Perform Network Fault Isolation and Reconfiguration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.2.5",
      "label" : "9.2.5 Monitor Faults",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.29",
      "label" : "8.2.29  Detect and Record Host and Network Anomalies",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.8.12",
      "label" : "8.8.12  Log Operator Activity",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.22",
      "label" : "3.4.22  Create, Edit, and Display Log",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.41",
      "label" : "8.2.41  Provide Audit Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.2.60",
      "label" : "8.2.60 Passively Capture and Copy Network Traffic Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.52",
      "label" : "8.7.52 Report Performance Results",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "9.1.29",
      "label" : "9.1.29 Record and Store System Components Reliability Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.41",
      "label" : "8.1.41 Manage Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.9.7",
      "label" : "8.9.7 Manage Enterprise Information Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.56",
      "label" : "7.1.56  Provide Shared Audio Visualization Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.59",
      "label" : "7.1.59  Translate Text to Speech",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4",
      "label" : "3.4 Situational Awareness (SA) Data Management (Group)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.10",
      "label" : "3.4.10 Integrate Information on Potential Adversary Courses of Action (COAs)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.78",
      "label" : "8.1.78 Query Global Directory Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7",
      "label" : "8.7 Manage Data (Group)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.9",
      "label" : "8.1.9 Provide Network Applications Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3",
      "label" : "1.3 Perform Detection (Group)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.22",
      "label" : "1.4.22 Calculate Sensor Error or Uncertainty",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.17",
      "label" : "3.5.17  Process Asset Specific Information Query",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "5.4.5",
      "label" : "5.4.5  Determine Operational and Tactical Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.53",
      "label" : "8.7.53  Access Subject Matter Expert and Essential Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.55",
      "label" : "8.7.55  Establish Discovery Catalogs",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3",
      "label" : "1.4.3 Identify Current Platform and Sensor Tasking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.7",
      "label" : "1.4.7   Generate National Intelligence, Surveillance, and Reconnaissance (ISR) Sensor Tasking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.10",
      "label" : "1.4.10   Generate Sensor Coverage",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.11",
      "label" : "1.4.11  Maintain Sensor Configurations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.12",
      "label" : "1.4.12   Characterize Sensors Capabilities and Limitations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.19",
      "label" : "3.5.19  Display and Monitor Sensor Assets",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.5",
      "label" : "1.4.5 Manage Sensors",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.13",
      "label" : "1.4.13 Generate Sensor Tasking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.21",
      "label" : "1.4.21 Assess Sensor Performance",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.24",
      "label" : "1.4.24 Recommend Unattended Ground Sensor (UGS) Emplacement",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.7",
      "label" : "4.7 Analyze Targets (Group)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "4.8.20",
      "label" : "4.8.20 Analyze Resource Tasking and Control",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.9",
      "label" : "1.4.9 Generate Theater and External Intelligence, Surveillance, and Reconnaissance (ISR) Sensor Tasking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.14",
      "label" : "1.4.14 Generate Sensor Configuration Commands",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.70",
      "label" : "8.7.70 Re-Map Information",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.59",
      "label" : "8.1.59 Provide Discovery Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.4",
      "label" : "8.7.4 Analyze Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.21",
      "label" : "8.7.21 Correlate Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.43",
      "label" : "8.7.43 Mine Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.47",
      "label" : "8.7.47 Synchronize Data",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.59",
      "label" : "8.7.59 Manage Encyclopedic Database",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.99",
      "label" : "8.7.99 Produce Information Mapping and Taxonomy",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.111",
      "label" : "8.7.111 Associate Data Characteristics",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1",
      "label" : "7.1 Provide Ability to Communicate (Group)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.4.1",
      "label" : "8.4.1 Create and Edit Messages",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.3",
      "label" : "7.1.3  Employ Conference Communications Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.44",
      "label" : "7.1.44  Conduct Video Conferencing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.76",
      "label" : "8.1.76 - Enable Calendar Scheduling",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.1.80",
      "label" : "8.1.80 Populate and Render Data Entry Form via Web Browser",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.12.9",
      "label" : "8.12.9 Provide Common Workspace",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.49",
      "label" : "8.7.49 Submit Information Requirements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.87",
      "label" : "8.7.87 Validate Request for Information (RFI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.101",
      "label" : "8.7.101 Track Requests for Information (RFIs)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.102",
      "label" : "8.7.102 Respond or Reply to Request for Information (RFI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.103",
      "label" : "8.7.103 Receive Request for Information (RFI)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.113",
      "label" : "8.7.113 Receive Requests for Information (RFI) Results",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "7.1.24",
      "label" : "7.1.24  Manage E-Mail",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.5.33",
      "label" : "8.5.33 Scan Mailbox",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.4.6",
      "label" : "8.4.6  Support Web Browsing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.15",
      "label" : "1.4.15 Process Source Requirements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.16",
      "label" : "1.4.16 Determine Availability of Joint Intelligence, Surveillance, and Reconnaissance (JISR) Capabilities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.19",
      "label" : "1.4.19 Direct and Monitor Sensor Employment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.17",
      "label" : "14.1.17 Evaluate Collection Results",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.18",
      "label" : "14.1.18 Evaluate Intelligence Collection, Products, Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "14.1.19",
      "label" : "14.1.19 Identify, Prioritize and Validate Intelligence Requirements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.9.5",
      "label" : "8.9.5 Integrate Enterprise Applications",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.56",
      "label" : "8.7.56 Maintain Address Book",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.7.58",
      "label" : "8.7.58 Manage Communities of Interest (COI) Catalogs",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "8.12.8",
      "label" : "8.12.8 Determine Collaboration Resource Availability",
      "description" : null,
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "ACCPROCESS",
    "description" : "Accreditation Process",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "DOD8500",
      "label" : "DoD 8500",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "DCID6/3",
      "label" : "DCID 6/3",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "ICD 503",
      "label" : "DoD 8500",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "OTH",
      "label" : "Other",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "LIFECYCSTG",
    "description" : "Lifecycle Stage",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "CNPTDEF",
      "label" : "Concept Definition",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "DEV",
      "label" : "Development",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "PILOT",
      "label" : "Deployment Pilot",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "OPR",
      "label" : "Operations",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "DEPR",
      "label" : "Deprecated",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "RET",
      "label" : "Retired",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "LICCLASS",
    "description" : "License Classification",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "COTS",
      "label" : "COTS",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "FOSS",
      "label" : "FOSS",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "FOSSR",
      "label" : "FOSS Restricted",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "GOSS",
      "label" : "GOSS",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "GOTS",
      "label" : "GOTS",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "COTSUR",
      "label" : "Unrestricted COTS",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "NETWORKACC",
    "description" : "Network Accredited On",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : true,
    "codes" : [ {
      "code" : "WWW",
      "label" : "Public Internet",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "NIPR",
      "label" : "NIPR",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "SIPR",
      "label" : "SIPR",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "JWICS",
      "label" : "JWICS",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "TYPE",
    "description" : "Component Type",
    "visibleFlg" : true,
    "requiredFlg" : true,
    "archtechtureFlg" : false,
    "importantFlg" : true,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "APP",
      "label" : "Application",
      "description" : "An application is a human or machine accessible executable software implementation with one or more application endpoints. Applications perform multiple functions based on direct inputs from user interfaces or application and service instances",
      "fullTextAvailable" : false
    }, {
      "code" : "DEVTOOL",
      "label" : "Development Tools",
      "description" : "Tools that aid in software development",
      "fullTextAvailable" : false
    }, {
      "code" : "DOC",
      "label" : "Documentation",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "ENV",
      "label" : "Environment",
      "description" : "An Environment describes an IT infrastructure solution composed of both software and hardware used to host deployed applications and services.",
      "fullTextAvailable" : false
    }, {
      "code" : "WIDGET",
      "label" : "Widget",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "OTH",
      "label" : "Other",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "OZONE",
      "label" : "OZONE App",
      "description" : "OZONE app",
      "fullTextAvailable" : false
    }, {
      "code" : "SERVICE",
      "label" : "Service Endpoint",
      "description" : "A service Endpoint is a runtime access point for one instance of an implemented service, widget, application, or environment.",
      "fullTextAvailable" : false
    }, {
      "code" : "SOFTWARE",
      "label" : "Software",
      "description" : "A Software asset is an executable digital product or source code file A program would install and integrate a software asset into their Environment",
      "fullTextAvailable" : false
    }, {
      "code" : "SPEC",
      "label" : "Standards, Specifications, and APIs",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "WEBAPP",
      "label" : "Web App",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "PROIMPLEV",
    "description" : "Protection / Impact Level",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "DODMAC",
      "label" : "DoD MAC Level",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "DCID6/3",
      "label" : "DCID 6/3 Protection Level",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "ICD503",
      "label" : "ICD 503 Impact Level",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "PL-3",
      "label" : "PL-3",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "ITAR",
    "description" : "ITAR Exportable",
    "visibleFlg" : false,
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "Y",
      "label" : "Yes",
      "description" : "",
      "fullTextAvailable" : false
    }, {
      "code" : "N",
      "label" : "No",
      "description" : "",
      "fullTextAvailable" : false
    } ]
  }, {
    "type" : "DI2E-SVCV4-A",
    "description" : "DI2E SvcV-4 Alignment",
    "visibleFlg" : true,
    "requiredFlg" : false,
    "archtechtureFlg" : true,
    "importantFlg" : true,
    "allowMutlipleFlg" : true,
    "codes" : [ {
      "code" : "3.5.1.1",
      "label" : "3.5.1.1 Dissemination Authorization",
      "description" : "<b>Definition:</b>Dissemination Authorization supports the process to submit, track, and authorize requests to release information.<br><b>Description:</b>The Dissemination Authorization service receives an intelligence report and information the entity a report will be disseminated to, and uses classification markings, security metadata, information about the entity, and possibly even man-in-the-loop, to determine if the report is releasable to the specified entity.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8.4",
      "label" : "3.4.8.4 Weather Effect  Planning",
      "description" : "<b>Definition:</b>Weather Effect Planning supports the planning and analysis of weather on operations and collections.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.2",
      "label" : "3.4.6.2 Structured Analytic Techniques",
      "description" : "<b>Definition:</b>Structured Analytic Techniques services provide the mechanism by which internal thought processes are externalized in a systematic and transparent manner.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.3.2",
      "label" : "3.4.3.2 Identity Disambiguation",
      "description" : "<b>Definition:</b>Identity Disambiguation determines if one entity (usually a person) is the same as another entity by analyzing descriptive information on the two entities.<br><b>Description:</b>Identity Disambiguation typically takes people names (but sometimes place names or institution names) and determines if they refer to the same entity.  This is done in one (or both) of two ways: (1) names can be compared with each other via similarity metrics or association dictionaries or (2) by comparing associated metadata to determine degrees of similarity.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.3.1",
      "label" : "3.4.3.1 Entity Activity Patterns",
      "description" : "<b>Definition:</b>The Entity Activity Patterns service determines intelligence relationships among specific objects such as people, places, items, and events.<br><b>Description:</b>Entity Activity Patterns takes information about people, places, material items, organizations, events, etc. and organizes them into spatio-temporal and linkage contexts.  This enables highlighting of associations indicative of key patterns of activity such as geographic/geospatial location, memberships, clandestine organizational structures, financial activity patterns, attack indicator patterns, travel patterns, and so-called \"patterns of life\". <br><br>Service operations will include interrelated data organizing, filtering, display, and linkage analysis tools.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.3",
      "label" : "3.4.3 HUMINT Analysis",
      "description" : "<b>Definition:</b>The HUMINT Analysis family establishes intelligence derived from human collected information sources.<br><b>Description:</b>The CI/HUMINT Analysis family provides intelligence derived from information collected by counter intelligence and/or human sources.  Typically HUMINT data is written reports containing raw text, but it may also be electronic telephone records, online sources, computer sources, audio transcripts, etc.<br>The objective of CI/HUMINT Analysis is to transform and filter the raw source material into an organized format that allows reports to be generated concerning specific people, activities, items (such as smuggled goods), institutional structures (such as insurgent cell networks and command chains), and events (both past and planned).<br>Specific CI/HUMINT services include:<br>  • Entity Linkages - which associates CI/HUMINT related reports based on related metadata. <br>  • Entity Activity Patterns - which determines intelligence relationships among specific objects such as people, places, items, and events.<br>  • Identity Disambiguation - which determines if one entity (usually a person) is the same as another entity by analyzing descriptive information on the two entities.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6",
      "label" : "3.3.6 Support to Targeting",
      "description" : "<b>Definition:</b>The Support to Targeting family provides the data necessary for the   target development process.<br><b>Description:</b>Support to Targeting services implement target development process including managing and maintaining target lists, assisting in target development, functionality and vulnerability assessment, impact point  specification, status reporting, and targeting report dissemination.   <br><br>Specific services include:<br><br>  • Target Management - which compiles and reports target information <br>  • Target Data Matrix - which provides current target status  <br>  • Target Validation - which portrays and locates target services, indicates vulnerabilities, and maintains relative target importance.<br>  • Target Folder - which maintains hosts target intelligence artifacts <br>  • Target List - which maintains summaries of candidate targets",
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.4",
      "label" : "3.2.1.4 Sensor Alerting",
      "description" : "<b>Definition:</b>Sensor Alerting delivers sensor notifications to registered endpoints based on client-configurable subscriptions.<br><b>Description:</b>Sensor Alerting  requests information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.    It receives metadata documents describing a server's abilities along with the sensor description, then retrieves a sensor alert message structure template along with the WSDL definition for the sensor interface.    A client can also subscribe to a sensor's alerts, renew an existing subscription, or cancel a subscription.  Optionally, servers can advertise the type of information published, renew an existing advertisement, or cancel an advertisement.                        <br><br>[This service is supported by the OGC SWE SES discussion paper.]",
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3",
      "label" : "3.2.1.3 Sensor Command Conversion",
      "description" : "<b>Definition:</b>Sensor Command Conversion converts sensor requests into sensor specific commands.<br><b>Description:</b>Sensor Command Conversion converts collection requests into tasking formats that a specific sensor type can understand.  The resulting commands are then sent to the sensor.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2",
      "label" : "3.2.1.2 Sensor Cross Queuing",
      "description" : "<b>Definition:</b>Sensor Cross Queuing passes detection, Geo-location and targeting information to another sensor.<br><b>Description:</b>The Sensor Cross Cueing service is broken into three subservices:  <br>(1) Cue Instruction Definition - allows the client to develop a set of sensor cue instructions that accomplish a specific objective.  These instructions describe the parameters which trigger a sensor detection and rules by which the sensor determines what other sensors and/or detection adjudication servers to notify once a detection is made.   The Cue Instruction Definition subservice consists of a set of operations that can be performed by the client. <br>(2) Cue Instruction Distribution  - Once a set of sensor cue instructions are developed for a specific objective,  the Cue Instruction Distribution subservice allows a client to distribute those instructions to the appropriate sensors and/or detection adjudication servers <br>(3) Detection Distribution  - Once a sensor has made a detection, Detection Distribution distributes the detection, geo-location, and targeting information to other sensors and/or detection adjudication servers that were defined in the previously received cue instruction set.                 <br><br>Scoping note:  Correlation and fusion of sensor data is outside the scope of Sensor Cross Cueing.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1",
      "label" : "3.2.1.1 Sensor Provisioning",
      "description" : "<b>Definition:</b>Sensor Provisioning sends messages to various sensors asking what assignments they can perform, whether a task request is valid, and adding, modifying and canceling tasking requests.<br><b>Description:</b>Sensor Provisioning allows clients to query individual sensors for information about appropriate sensor tasking, and to task the sensor.  For each sensor, clients can request and receive metadata documents that describe the actions the sensor can perform, request information needed to prepare assignment requests, and request feedback as to whether a specific assignment request is valid or needs improvement to meet business rules.   In addition, the client can send the sensor an assignment request, update a prior assignment request, cancel a prior assignment request, or request the return of the sensor status, .  Lastly, the client can inquire about where and how the results of the tasking can be accessed.<br><br>[This service is supported by the OGC SWE SPS 2.0 service.]",
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1",
      "label" : "3.2.1 Asset Management",
      "description" : "<b>Definition:</b>The Asset Management family of services provide sensor collection tasking and status monitoring.  Included are efforts to allocate Enterprise resources, as well as gain insight into the ability to influence external collection assets.<br><b>Description:</b>The Asset Management family of services provide sensor collection tasking and status monitoring.  Specific services include:<br>• Sensor Provisioning - which sends messages to various sensors asking concerning tasking assignments <br>• Sensor Registration - which allows clients to request, add, or delete information about sensors and servers     <br>• Sensor Cross Queuing - which passes detection, Geo-location and targeting information to another sensor. <br>• Sensor Command Conversion - which converts sensor requests into sensor specific commands.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.2",
      "label" : "3.2 Collection",
      "description" : "<b>Definition:</b>The Collection line includes services that provide the ability to gather data and obtain required information to satisfy information needs.<br><b>Description:</b>The Tasking (Planning & Direction) line includes services that provide the ability to gather data and obtain required information to satisfy information needs.  This includes the following sub-areas: Signals Collection – The ability to gather information based on the interception of electromagnetic impulses.  Imagery Collection – The ability to obtain a visual presentation or likeness of any natural or man-made feature, object, or activity at rest or in motion.  Measurements and Signatures Collection – The ability to collect parameters and distinctive characteristics of natural or man-made phenomena, equipment, or objects.  Human Based Collection – The ability to acquire information from human resources, human-derived data, and human reconnaissance assets. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)",
      "fullTextAvailable" : false
    }, {
      "code" : "3",
      "label" : "3 Mission Services",
      "description" : "<b>Definition:</b>The functions/services that represent a mission or business process or function.  When choreographed they represent the manifestation of high level mission capabilities.<br><b>Description:</b>Layer",
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1.4",
      "label" : "3.5.1.4 Foreign Disclosure Management",
      "description" : "<b>Definition:</b>Foreign Disclosure Management services support the  release of classified military information and law enforcement information to foreign entities or governments. Provides the ability to receive, process, release, and monitor requests for information release.<br><br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1.3",
      "label" : "3.5.1.3 Tear Line Reporting",
      "description" : "<b>Definition:</b>Tear Line services support creation of Tear Lines within products to support dissemination across multiple security domains.<br><b>Description:</b>The Tear Line Reporting service uses security markings and metadata to generate tear lines from existing intelligence reports, to support foreign disclosure and allow important intelligence to be disseminated to a greater audience without risking sensitive information.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1.2",
      "label" : "3.5.1.2 Package Product",
      "description" : "<b>Definition:</b>This service converts the intelligence product into a suitable form for dissemination.<br><b>Description:</b>This service converts the intelligence product into a suitable form for dissemination.  After exploitation is complete and an intelligence product has been created, it must be prepared for dissemination.  This may include converting to a file format suitable for the customer, final security review of the intelligence product to be disseminated, and finding and assigning a globally unique number in preparation for dissemination.  This service would call to the Global Object ID service for assignment of the globally unique number.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1",
      "label" : "3.5.1 Dissemination Management",
      "description" : "<b>Definition:</b>Dissemination Management includes authorizing the release/dissemination of products, the assignment of addresses to receive those products, as well as assignment of the transmission path/medium for dissemination of the products.<br><b>Description:</b>Dissemination Management includes authorizing the release/dissemination of products, the assignment of addresses to receive those products, as well as assignment of the transmission path/medium for dissemination of the products.  <br><br>Currently the one specific service included is Package Product which converts the intelligence product into a suitable form for dissemination.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.5",
      "label" : "3.5 BA Data Dissemination and Relay",
      "description" : "<b>Definition:</b>The BA Data Dissemination and Relay line includes services that provide the ability to present, distribute, or make available intelligence, information and environmental content and products that enable understanding of the operational/physical environment to military and national decision-makers.<br><b>Description:</b>The BA Data Dissemination and Relay line includes services that provide the ability to present, distribute, or make available intelligence, information and environmental content and products that enable understanding of the operational/physical environment to military and national decision-makers.  This includes the following sub-areas: BA Data Transmission – The ability to send collected data directly to processing, exploitation analysis, production and visualization systems, leveraging both Net-Centric information transport and intelligence-controlled systems.  BA Data Access – The ability to provide authorized customer access to data and products, leveraging both Net-Centric computing infrastructure and intelligence-controlled systems. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)<br>Families in this line: DISSEMINATION MANAGEMENT and MESSAGING SYSTEM INTERFACE.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8.3",
      "label" : "3.4.8.3 Mission Planning and Force Execution support",
      "description" : "<b>Definition:</b>This component supports mission planning and force execution analysis to find, fix, track, and target.<br><b>Description:</b>The service supports mission planning and force execution analysis to find, fix, track, and target.  <br>Find: Develop JIPOE, detect target, and determine target conditions<br>Fix: confirm target, refine target location, and plot movement<br>Track: maintain situational awareness and maintain track continuity<br>Target: validate desired effects, finalize target data, and predict consequences",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8.2",
      "label" : "3.4.8.2 Intelligence Preparation of the Battlefield",
      "description" : "<b>Definition:</b>Intelligence Preparation of the Battlefield services continuously analyze the threat and environment in an area.<br><b>Description:</b>The Intelligence Preparation of the Battlefield service supports intelligence analysts in satisfying Joint Intelligence Preparation of the Operational Environment (JIPOE) requirements.  This service assists users in:<br>? Identifying the Operational Area<br>? Analyzing the Mission and the Commander's Intent<br>? Determining significant characteristics of the Operational Environment<br>? Establishing the physical and nonphysical limitations of the force's Area of Interest<br>? Establishing appropriate level of detail for intelligence analysis<br>? Identify intelligence and information gaps, shortfalls and priorities<br>? Submitting requests for information to support further analysis",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8.1",
      "label" : "3.4.8.1 Order of Battle Analysis",
      "description" : "<b>Definition:</b>Order of Battle Analysis determines the identification, strength, command structure, and disposition of the personnel, units, and equipment of any military force.<br><b>Description:</b>The Order of Battle Analysis service assists users in analyzing intelligence about friendly and opposing forces, and generating intelligence briefs about those forces.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8",
      "label" : "3.4.8 Analysis Support to C2",
      "description" : "<b>Definition:</b>This line provides intelligence support for command and control<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.6",
      "label" : "3.4.7.6 Orchestration Modeling",
      "description" : "<b>Definition:</b>Orchestration Modeling presents an interface for designing the flow of service executions to achieve an overall functional need.<br><b>Description:</b>Orchestration Modeling presents interfaces for designing actual or potential flows of service executions to achieve an overall functional need.   Functionality includes design definition and management.  Resultant models may be stored in orchestration languages such as Business Process Execution Language (BPEL) or Business Process Modeling Notation (BPMN). Business Process Management Language (BPML), or Web Service Choreography Interface (WSCI).     <br><br>The most popular of these, BPEL, is made of three main entities: partners that abstractly represent the services Involved, variables used to manipulate exchanged data and hold business logic states, and activities that describe the business logic operations such as invoking a web service or assigning a value to a variable.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.5",
      "label" : "3.4.7.5 Target Solution Modeling",
      "description" : "<b>Definition:</b>Target Solution Modeling models and/or simulates weaponeering and  weapon effects.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.4",
      "label" : "3.4.7.4 Sensor Modeling",
      "description" : "<b>Definition:</b>Sensor Modeling models and/or simulates ISR platform and sensor access and collection parameters over time.<br><b>Description:</b>Sensor Modeling models and/or simulates ISR platform and sensor access and collection parameters over time.  <br><br>Structures of sensor modeling systems typically include Players (the specific sensor being modeled), Markers (those things implicitly represented; e.g., tactical positions and collection domains), Environment (establishes the physical battlespace), and Zones (abstract spaces where the location of sensors or related elements is not an important element of the model).<br><br>Events fall into these classes: physical (e.g., start of collection gathering), Information Exchange (e.g., orders, reports, and messages), and Element coordination (place in explicit modeling).<br><br>Sensor modeling operations typically might include: Simulation control (stop, pause, and resume simulation), Game state update; Command and Control (C2) decisions; and External events from outside the simulation.<br><br>Criteria monitored might include items such as: Sensor maximum services, Sensor schedules, Sensor lifecycle estimates; Maintenance schedules; Intelligence needs assessments; Sensor identity, instance, location, and environment; Visual appearance, Acoustic appearance, Radar signatures, Identified intention, Mission role; Sensor health estimates; Relationship to ground receiving systems; and Probability tables and estimates.<br><br>Sensors modeled might include (but aren’t limited to): Class I and IV Unmanned Aerial Vehicles (UAVs), Electro Optical/Infrared Sensors (EO/IR), Synthetic Aperture Radar (SAR) sensors, Moving Target Indicators (MTIs), Aerial Common Sensor (ACS), U-2/TR-1, Global Hawk, Predator, Joint Surveillance Target Attack Radar System (JSTARS), Advanced Tactical Air Reconnaissance System (ATARS),  Prophet,  Airborne Reconnaissance Integrated Electronics Suite, and Unattended Ground Sensors (UGSs).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.3",
      "label" : "3.4.7.3 Model Building",
      "description" : "<b>Definition:</b>The Model Building service supports the ability to build and visualize analytical models<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.2",
      "label" : "3.4.7.2 Scenario Generation",
      "description" : "<b>Definition:</b>The Scenario Generation services provides the ability to build and manage scenarios.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.1",
      "label" : "3.4.7.1 War Gaming",
      "description" : "<b>Definition:</b>War gaming brings together C4ISR, and Synthetic Natural Environments (SNE) and the Common Operating Picture (COP) to support course of action analysis, mission planning and rehearsal, and individual and collective training.<br><b>Description:</b>War gaming models and/or simulate ISR services and collection parameters over time while allowing for analysis of the technical feasibility of the mission with respect to weather, terrain, battlefield conditions, order of battle, and threat characteristics through the use of expanded, robust Joint ISR resource management tools adapted to net-centric operations and transformational communications  (Ref. DCGS Integrated Roadmap (2005–2018) Version 1.2; 3 January 2005 (section 5))<br>Objective war gaming operations include the ability to monitor, locate, identify, and represent military units and assets (Army, Navy, Marines, Air Force, and Special Operational Forces) as missions and sorties occur, build and maintain operational surface picture in an Area Of Interest (AOI);  develop and present threat conceptual models, services data, force structure and tactics; account for intelligence sensors, systems, processes, and organizations; re-use prior war game data and models; provide algorithms for the interaction of ‘Red’ (enemy force),  ‘Blue’ (friendly force), and non-combatant behaviors; maintain evolving threats catalog(s); maintain validated parameter and performance data; integrate decision making process including the impact of rules of engagement (ROE); provides realistic deterministic fusion methodology (versus simple stochastic replications); render “3D” views; present real time (or near real time) statistics; inject actual or potential weather effects; and accommodate Concepts of Operation (CONOPS) and other related threat policy and guidance.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7",
      "label" : "3.4.7 Modeling and Simulation",
      "description" : "<b>Definition:</b>The Modeling and Simulation family of services uses representative realities to assess current or possible future conditions in assess area of interest including DI2E ISR platforms and sensors.<br><b>Description:</b>Modeling and Simulation uses representative realities to assess current or possible future conditions in assess area of interest including DI2E ISR platforms and sensors.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.5",
      "label" : "3.4.6.5 Link Analysis",
      "description" : "<b>Definition:</b>Link analysis is a data-analysis technique used to evaluate relationships (connections) between entities. Relationships may be identified among various types of objects, including organizations, people and transactions.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.4",
      "label" : "3.4.6.4 Alternative Future Analysis",
      "description" : "<b>Definition:</b>Alternative Future Analysis supports postulating possible, probable, and preferable futures through a  systematic and pattern-based understanding of past and present, and to determine the likelihood of future events and trends.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.3",
      "label" : "3.4.6.3 Argument Mapping",
      "description" : "<b>Definition:</b>Argument Mapping is the visual representation of the structure of an argument in informal logic. It includes the components of an argument such as a main contention, premises, co-premises, objections, rebuttals, and lemmas.<br><b>Description:</b>Argument Mapping is the visual representation of the structure of an argument in informal logic. It includes the components of an argument such as a main contention, premises, co-premises, objections, rebuttals, and lemmas. Typically an argument map is a “box and arrow” diagram with boxes corresponding to propositions and arrows corresponding to relationships such as evidential support. Argument mapping is often designed to support deliberation over issues, ideas and arguments",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.1",
      "label" : "3.4.6.1 Timelines Analysis",
      "description" : "<b>Definition:</b>Timeline Analysis helps analysts determine relationships of events over time.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6",
      "label" : "3.4.6 Analytic Decision Support",
      "description" : "<b>Definition:</b>The Analytic Decision Support Services family provides advanced analytic analysis and presentation to help analysts uncover, determine, or predict otherwise complex relationships among various DI2E entities.<br><b>Description:</b>The Analytic Decision Support family provides advanced analytic analysis and presentation to help analysts uncover, determine, or predict otherwise complex relationships among various DI2E entities.    Services include the answering multi-dimensional analytical queries (online analytical processing).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5.3",
      "label" : "3.4.5.3 Digital Production",
      "description" : "<b>Definition:</b>Digital Production provides authoring, mark-up, dissemination, document control, geospatially visualized, geo-smart, GeoPDF functions.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5.2",
      "label" : "3.4.5.2 Production Workflow",
      "description" : "<b>Definition:</b>Production Workflow provides the necessary management for the creation of new products, guiding the product status through various levels of approval.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5.1",
      "label" : "3.4.5.1 Reporting Services",
      "description" : "<b>Definition:</b>Reporting services allow for the creation, editing, and approval of new reports.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5",
      "label" : "3.4.5 Production",
      "description" : "<b>Definition:</b>The Production family of services to support production, to include Reporting Services, Production Workflow, and Digital Production.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.4",
      "label" : "3.4.4 MASINT/AGI Analysis",
      "description" : "<b>Definition:</b>Measurement and Signature Intelligence/Advanced Geospatial Intelligence (MASINT/AGI) Analysis services provide intelligence derived from many different sources and sensors to identify the specific characteristics of a target and enables it to be located and tracked.   <br>Note: The family of MASINT services is very broad and many are sensitive or classified.   The single example provided below may be supplemented after engagement with the DI2E MASINT Functional Team.<br><b>Description:</b>Measurement and Signature Intelligence/Advanced Geospatial Intelligence (MASINT/AGI) Analysis services provide intelligence derived from many different sources and sensors to identify the specific characteristics of a target and enables it to be located and tracked.   <br>Note: The family of MASINT services is very broad and many are sensitive or classified.     Other (unclassified) MASINT examples include:<br>  • SAR Coherent Change Detection (CCD)<br>  • differential interferometric SAR (DInSAR)<br>  • EO polarimetry<br>  • SAR polarimetry<br>Thus the single COMINT Externals Analysis example provided below may be supplemented with additional services after discussions are held with the DI2E MASINT Functional Team.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2.4",
      "label" : "3.4.2.4 COMINT Externals Analysis",
      "description" : "<b>Definition:</b>COMINT Externals Analysis analyzes patterns of communications metadata.<br><b>Description:</b>COMINT Externals Analysis provides link analysis functionality for the specific application of highlighting patterns of communications activity.   The analysis is based solely on communications metadata (origination and destination node IDs, cell phone IDs, phone numbers, packet lengths, etc.) and not communication content (digitized voice, text messages, etc.).<br><br>To do this, COMINT Externals Analysis first ingests communication metadata and, if needed, strips off associated message content.  The resultant metadata entities are then linked to each other, as well as other entities.  Message transmission structures may also be analyzed for such things as statistics showing how often packets with one specific format are immediately followed by packets with a second specified format.  <br><br>Analyzing the structure of analog communications signals is possible, but digital analysis is increasingly more common.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2.3",
      "label" : "3.4.2.3 Emitter Geolocation",
      "description" : "<b>Definition:</b>Emitter Geolocation Analysis & Location Refinement determines the likely position of an emitter source from emitter intercept data.<br><b>Description:</b>Emitter Geolocation takes correlated emitter intercepts collected by one or more platforms and computes the likely emitter geolocation, geolocation error ellipse, and (if the emitter is in motion) an estimate of the emitter's velocity.<br><br>Intercepts may contain signal frequency and/or time of arrival information, intercept angle of arrival (AOA) , ID and \"collection instant geolocation\" of the platform(s) that generated the intercept reports, associated pointing/position uncertainties characterizing the collection systems involved, and/or previously computed emitter geolocation along with the geolocation error ellipse and target velocity vector.<br><br>Example analytic methods include: 1) triangulation using AOA data, 2) time difference of arrival (TDOA),  3) frequency difference of arrival (FDOA), or 4) some combination of the first three methods.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2.2",
      "label" : "3.4.2.2 Emitter Correlation",
      "description" : "<b>Definition:</b>Emitter Correlation correlates multiple intercepts to the same signal emitting entity.<br><b>Description:</b>The service ingests emitter intercept signal characterizations and identifies which signal intercepts come from the same emitter by identifying signals whose extracted features sufficiently match.    The output is a list of which of the ingested signals (or signal pulse sequences) come from each separate emitter source, along with the associated times of the signal intercepts.<br><br>There are two ways in which this service can be used:  (1) Supporting multi-platform emitter geolocation analysis over short period using a pulse-by-pulse basis and (2) correlating signals collected over a more prolonged time interval using signal intercepts from several distinct emitters to analyze sequences of pulses for matching signal characteristics.<br><br>Emitter Correlation is in some ways the converse of the ELINT de-duplication service (see 2.3.2 SIGINT Processing services).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2.1",
      "label" : "3.4.2.1 SIGINT Analysis and Reporting",
      "description" : "<b>Definition:</b>SIGINT Analysis and Reporting generates various reports from raw SIGINT data.<br><b>Description:</b>SIGINT Analysis and Reporting generates selected types of SIGINT reports from ingested SIGINT intercept data.   Three examples of the types of reports produced include: <br><br>1)  A report on the full Electronic Order of Battle (EOB) of an adversary within a specified region.   The report might name the identified emitters along with a description of the function/services of the overall system they are associated with.  The report might also attempt to identify specific military units based on the identified emitters.<br><br>2)  A report on the locations of all surface-to-air defense systems in a specified region.  The report would list the services, the mobility potential, and the known vulnerabilities or limitations of each air defense unit identified.<br><br>3)  A report on the signal properties and service implications of unknown emitters detected in a specified region.   Such a report might note the presence of an unidentified chirped pulse emitter and, based on the chirp bandwidth, pulse repetition intervals, and scan rates suggest that the emitter is part of an upgraded surface-to-air defense system.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2",
      "label" : "3.4.2 SIGINT Analysis",
      "description" : "<b>Definition:</b>SIGINT Analysis services provide various forms of signals intelligence (SIGINT) varying from recurring, serialized reports to instantaneous periodic reports.<br><b>Description:</b>The SIGINT Analysis family of services support Electronic Intelligence (ELINT), Communications Intelligence (COMINT), and Foreign Instrumentation Signals Intelligence (FISINT).  <br> • ELINT is the interception, geolocation, and analysis of electronic emissions produced by adversary equipment that intentionally transmits for non-communications purposes.  Most notably this includes radars but sometimes also includes other types of weaponry and equipment.   The purposes of ELINT are to: ascertain services and limitations of target emitters, geolocate target emitters, and determine the current state of readiness of target emitters.<br>  • COMINT is the interception, geolocation, and decryption of either voice or electronic text transmissions. Included within COMINT analysis is communications traffic analysis.  <br>Specific SIGINT Analysis services include:<br>  • SIGINT Analysis and Reporting - which generates various reports from raw SIGINT data. <br>  • Emitter Correlation - which correlates multiple intercepts to the same signal emitting entity. <br>  • Signal Pattern Recognition - which provides signal pattern recognition in order to detect signal characteristics that tell analysts information about the emitting source.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.11",
      "label" : "3.4.1.11 GEO-Calculations",
      "description" : "<b>Definition:</b>Geo-Calculations provide the ability to perform geospatially based calculations on data stored within the enterprise to further enrich the operator’s understanding of the data relationships.<br><br><b>Description:</b>Users have the need to perform calculations and analytics on the geospatial within the enterprise.  Example analytics include geo-location lookups, distance calculations between two entities, coordinate conversions, and entity lookup.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.10",
      "label" : "3.4.1.10 Sensor Model Instantiation",
      "description" : "<b>Definition:</b>Sensor Model Instantiation generates sensor model state data from image metadata. <br><b>Description:</b>Sensor Model Instantiation determines a sensor model type and state data given raw image metadata.  <br><br>As an example, consider SAR and pushbroom EO sensor models ((pushbroom scanners deliver a perspective image with highly correlated <br>exterior orientation parameters for each image line).   The SAR sensor model might require specification of collection squint and grazing angles.  However, the raw collection taken from the EO sensor may only provide metadata on the sensor collection trajectory and the specified image center.  The Sensor Model Instantiation service can use this raw EO sensor metadata to calculate collection squint and grazing angles as used by the SAR sensor model.  In other simpler cases, the service may simply transfer raw collection metadata values directly into matching sensor model parameter fields.<br><br>Resulting sensor model state data can be used with associated sensor functions such as image-to-ground and ground-to-image transformations and will support (but not provide) other GEOINT services such as Image Registration, DPPDB Mensuration, Triangulation, Source Selection, Resection, Rectification, and Geomensuration (see definitions in related 2.4.1 GEOINT Exploitation services).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.9",
      "label" : "3.4.1.9 Automatic Target Recognition",
      "description" : "<b>Definition:</b> Automatic Target Recognition identifies specified entities on an image using image processing techniques. <br><br><b>Description:</b>Automatic Target Recognition services process imagery to identify candidate specified 'targets' that match selected pixel 'signature patterns'.    <br><br>Examples of 'targets' might be a tank even if partially covered or camouflaged an aircrafts located on runways.<br><br>Artifact formats, targets, and analytic methods vary, but one example is an ATR service that ingests hyperspectral imagery (HSI) in the near IR (NIR), short wave IR (SWIR) bands, and target signature definitions and returns image coordinates where signatures are found along with nature of each candidate 'target'.   To do this it might perform anomaly detection through orthogonal subspace projection (considering each HSI pixel a vector in a multidimensional space, determining dominant spectral signatures by running a pixel clustering algorithm, creating a subspace projection operator matrix, and projecting the pixel's signature into a spectral subspace that does not contain the identified clutter signature components).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.8",
      "label" : "3.4.1.8 Topographical Survey",
      "description" : "<b>Definition:</b>Topographical Survey derives and represents elevation and terrain characteristics from reports and data.<br><br><b>Description:</b>Topographical Survey generates maps based on a variety of terrain, elevation, and feature data.  One example is creating a 2D contour line map with vector graphics indicating features such as rivers, communication lines, and known roads.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.7",
      "label" : "3.4.1.7 MTI Tracking",
      "description" : "<b>Definition:</b>Moving Target Indicator (MTI) Tracking links 'dots' of point locations into tracks that indicate historical & projected direction and speed.<br><br><b>Description:</b>Moving Target Indicator (MTI) Tracking ingests and tags a sequence of point locations (\"dots\") of moving objects indicating their speed, direction, range, azimuth location (an angular measurement in a spherical coordinate system), and time stamp.    Dots from raw MTI sources are then linked into track segments which last until either a moving object stops, and thus disappears from the MTI sensor data stream or until an occlusion or sharp change of speed, or some other confounding event causes the service to \"break track\".     <br><br>Dots may also have additional attributes such as a one dimensional range profile, shape and/or intensity descriptor.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.6",
      "label" : "3.4.1.6 Image Registration",
      "description" : "<b>Definition:</b>Image Registration matches points in a selected image to one or more control images to increase the accuracy of geoposition coordinates in the selected image.<br><br><b>Description:</b>Image Registration selects image coordinate matching points between a selected image and one or more control images.  It then adjusts the sensor model for the selected image through resection (converging cycles of bundle adjustments, accompanied by associated sensor model parameter adjustments and outlier point eliminations).   Positional uncertainties including circular and linear error are also propagated.<br><br>The end result is common geospatial coordinates between the selected image and the control image thus enabling accurate image overlays, mosaicking (appending images together to construct a single image covering a larger scene), or georeferencing.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.5",
      "label" : "3.4.1.5 DPPDB Mensuration",
      "description" : "<b>Definition:</b>Digital Point Positioning Database (DPPDB) Mensuration performs geopositioning by aligning directly to DPPDB metadata.<br><br><b>Description:</b>Digital Point Positioning Database (DPPDB)  Mensuration (geometric measurement) identifies a point on one of the DPPDB images and returns the geolocation of that point, complete with circular and linear errors.  The service uses the DPPDB sensor model and the fact that DPPDB reference imagery provides stereo depth to get the elevation as well as the horizontal geolocation of the chosen point.  <br><br>The service relies on the point being geolocated actually appearing in the DPPDB imagery.  Trying to estimate where a mobile target might lie when it is not visible in the DPPDB imagery and then selecting that estimated position in DPPDB image coordinates for target geolocation invites very serious geolocation errors. This estimation technique is referred to as \"visual point transfer\" and should be used only under very special circumstances.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.4",
      "label" : "3.4.1.4 Geomensuration",
      "description" : "<b>Definition:</b>Geomensuration computes image based mensuration calculations using image points measured from an image.<br><br><b>Description:</b>Geomensuration (measurement of geometric quantities) measures absolute geolocation of a photo identifiable point (geocoordinates, circular error and  linear error), horizontal distance and vertical elevation separations between two photo identifiable points (relative or absolute, and includes error ranges).<br><br>In general, a geomensuration service ingests image coordinates for one or more points in a georegistered image combined with an accompanying georegistered Digital Elevation Map (DEM) or in two or more georegistered images without an accompanying DEM.  It then returns one or more precision measurements based on their image coordinates.  Resected sensor models also help geomensuration computations.<br><br>Examples of specialized geomensuration services might include \"height of object determined from measured shadow length\", \"location of center of a circle\", or \"perpendicular separation between two parallel plane surfaces\".<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.3",
      "label" : "3.4.1.3 Resection",
      "description" : "<b>Definition:</b>Resection adjusts image support data when the sensor model data is unknown, incomplete, or imprecise.<br><br><b>Description:</b>Resection uses Digital Point Positioning Database (DPPDB) stereo reference image pairs or other reference imagery, possibly combined with a reference Digital Elevation Map (DEM), to adjust the support data of an image acquired by a less accurate source.  <br><br>The service ingests the image coordinates of tie points in all the images including reference images and the acquired image that is to be resected as well as instantiated sensor models.  The sensor model of the acquired image has adjustable parameters and enhancing the values of these adjustable sensor model parameters is the objective of the Resection service.<br><br>The Resection service uses the reference data to compute 3D geolocations for the selected tie points.  It then performs cycles of bundle adjustment (i.e. least squares sensor model parameter value and tie point coordinate value adjustment) until  it achieves convergence with the measured image coordinates for all the 3D reference points mapped onto the acquired image coordinate space. The resulting adjusted sensor model parameter values for the acquired image constitute the resection results.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.2",
      "label" : "3.4.1.2 Triangulation",
      "description" : "<b>Definition:</b>Triangulation calculates a geolocation estimate based on multiple image collections acquired from distinct collection angles. <br><br><b>Description:</b>Triangulation performs a geolocation estimate of a photo identifiable point based on multiple image collections acquired from distinct collection angles.  In this process it combines four distinct algorithmic functions: 1) least squares adjustment of sensor support data along with selected image plane tie or control point measurements that are each common to two or more images, 2) rigorous uncertainty propagation calculations, 3) geolocation computations using a triangulation method, and 4) blunder detection to discard point measurement outliers.<br><br>To initiate a triangulation the service is presented with a set of images and associated image point coordinates.  Image sensor models and coordinates are also provided from each image for the point to be geolocated.    Adjustable parameters include image collection geometries and (sometimes) sensor internal parameters, such as lens focal length or lens distortion.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.1",
      "label" : "3.4.1.1 Change Detection",
      "description" : "<b>Definition:</b>Change Detection identifies feature changes via a pixel analysis between two images of the same area.<br><b>Description:</b>Change detection ingests two precisely co-registered images and identifies differences in pixels collected before and after some interval of time at the same geographic location.   Typically the changes in pixel values must exceed a user defined thresholds including registration uncertainty \"guard band\", size, color, and/or shape in order to screen out noise/clutter changes.   Output is an image formatted mask that identifies which pixels have significant changes between the two images.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1",
      "label" : "3.4.1 GEOINT Analysis",
      "description" : "<b>Definition:</b>GEOINT Analysis services describe, assess, and visually depict physical features and geographically referenced activities on the Earth.<br><b>Description:</b>GEOINT Analysis services provide intelligence information ( imagery, imagery intelligence, multi-spectral analysis, hyper-spectral analysis, etc.) for geographically referenced features and activities.   Specific services include:<br> • Change Detection - which identifies pixel-by-pixel differences between two images of the same area.<br>  • Source Selection - which presents a user with sets of images that can satisfy a geopositioning precision requirement.<br>  • Sensor Model Instantiation - which generates sensor model state data from image metadata. <br>  • Triangulation  - which calculates a geolocation estimate based on multiple image collections acquired from distinct collection angles. <br>  • State Service - which converts image support data into a related sensor model state string.<br>  • Resection  - which adjusts image support data when the sensor model data is unknown, incomplete, or imprecise.<br>  • Geomensuration  - which computes image based mensuration calculations using image points measured from an image.<br>  • DPPDB Mensuration - which performs geopositioning by aligning directly to DPPDB metadata.<br>  • Image Registration - which matches points in a selected image to one or more control images to increase the accuracy of geoposition coordinates.<br>  • Moving Target Indicator (MTI) - which links 'dots' of point locations into tracks that indicate historical & projected direction and speed.<br>  • Topographical Survey - which derives and represents elevation and terrain characteristics from reports and data. <br>  • Automatic Target Recognition - which identifies specified entities on an image using image processing techniques. <br>  • Emitter Geolocation - which determines the likely position of an emitter source from emitter intercept data.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.4",
      "label" : "3.4 Analysis, Prediction and Production",
      "description" : "<b>Definition:</b>The Analysis, Prediction & Production line includes services that provide the ability to integrate, evaluate, interpret, and predict knowledge and information from available sources to develop intelligence and forecast the future state to enable situational awareness and provide actionable information.<br><b>Description:</b>The Analysis, Prediction & Production line includes services that provide the ability to integrate, evaluate, interpret, and predict knowledge and information from available sources to develop intelligence and forecast the future state to enable situational awareness and provide actionable information.  This includes the following sub-areas: Integration – The ability to identify, assimilate and correlate relevant information from single or multiple sources.  Evaluation – The ability to provide focused examination of the information and assess its reliability and credibility to a stated degree of confidence.  Interpretation – The ability to derive knowledge and develop new insight from gathered information to postulate its significance.  Prediction – The ability to describe the anticipated future state of the operational/physical environment based on the depiction of past and current information.  Product Generation – The ability to develop and tailor intelligence, information, and environmental content and products per customer requirements.  (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)<br>Families in this line: GEOINT ANALYSIS, SIGINT ANALYSIS, HUMINT ANALYSIS, MASINT/AGI ANALYSIS, and TARGETING SUPPORT.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.7",
      "label" : "3.3.6.7 BDA/CDA",
      "description" : "<b>Definition:</b>This component performs Battle Damage Assessment (BDA) analysis. BDA is the estimate of damage resulting from the application of lethal or nonlethal military force. BDA is composed of physical damage assessment, functional damage assessment, and target system assessment.  <br><br>GCCS Spec/USMTF message<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.6",
      "label" : "3.3.6.6 Target Mensuration",
      "description" : "<b>Definition:</b>Target Mensuration services provide the measurement of a feature or location on the earth to determine an absolute latitude, longitude, and height.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.5",
      "label" : "3.3.6.5 Target List",
      "description" : "<b>Definition:</b>The Target List service maintains the various target lists that are kept as part of the target strike management process.<br><b>Description:</b>The Target List service provides methods to retrieve a prioritized list of target lists by type; query for an existing list by name; create or remove target lists;  and add, remove, or edit targets in existing target lists.  Targets can also be transferred from one target list to another list type in accordance with the target management process.<br><br>Examples of target list types include: 1) the Joint Integrated Prioritized Target List (JIPTL) which are targets prioritized to be struck; 2) the Air Tasking Order (ATO); 3) on-call targets; 4) no-strike targets; 5) nominated targets; and 6) targets scheduled for battle damage assessment (BDA).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.4",
      "label" : "3.3.6.4 Target Folder",
      "description" : "<b>Definition:</b>The Target Folder service maintains a softcopy folder containing target intelligence and related materials prepared for planning and executing action against a specific target.<br><b>Description:</b>The Target Folder service maintains a collection of information related to the planning and execution of a strike against a target.  The target folder service serves as a portal for displaying and retrieving data and information that has been placed in the folder.  It also enables analysts to add their inputs or new data to the folder. <br><br>Examples of information maintained in a target folder are georegistered target images, lists of designated mean impact points (DMPI) that have been extracted from image data, collateral information about the target's activities and known functions, or no-strike information (such as the location of a place of worship or a civilian shelter within the target complex).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.3",
      "label" : "3.3.6.3 Target Validation",
      "description" : "<b>Definition:</b>Target Validation portrays and locates target services, indicates target vulnerabilities, and maintains relative target importance.<br><b>Description:</b>Target Validation maintains information about targets that is used to maintenance the target data matrix and provide input to other target support services by presenting and locates known functions and vulnerabilities of a target or target complex including current operational status of targets.  It also provides information that helps establish the strike prioritization of a target.<br><br>For example, a target may have been serving as a command and control communications hub in prior weeks but may have recently been restored to some more benign function, making it undesirable as a high priority target.  The service also may denote no-strike regions in a target complex if the target has multiple functions and if certain of those functions are best preserved.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.2",
      "label" : "3.3.6.2 Target Data Matrix",
      "description" : "<b>Definition:</b>Target Data Matrix provides the current status of targets within a given area of responsibility (AOR).<br><b>Description:</b>Target Data Matrix displays the status of targets as they progress from target nomination lists to target battle damage assessment lists.  It also generates reports on current target status (i.e. destroyed, recently nominated, current operational status, scheduled for strike, etc. ) within an Area of Responsibility (AOR).   Both these operations are important for managing targeting operations during joint strike operations.<br><br>To perform these operations Target Data Matrix uses target lists and target folders.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.1",
      "label" : "3.3.6.1 Target Management",
      "description" : "<b>Definition:</b>Target Management compiles and reports target information and provides a dissemination service for filtering, combining, and passing that information to higher, adjacent, and subordinate commanders.<br><b>Description:</b>Target Management provides querying and reporting on target locations, battle damage assessments (BDA), weapon target pairing, and other related topics using target lists, the joint target matrix, and other related target files.  Reports can be customized, filtered and/or summarized as they are disseminated upward and downward through the chain of command.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.5",
      "label" : "3.3.5 MASINT Processing",
      "description" : "<b>Definition:</b>MASINT Processing services perform qualitative analysis of data (metric, angle, spatial, wavelength, time dependence, modulation, plasma, and hydro magnetic) derived from specific technical sensors for the purpose of identifying any distinctive features associated with the source, emitter, or sender and to facilitate subsequent identification and/or measurement of the same.<br><b>Description:</b>MASINT processing services include specialized SAR image formation algorithms needed to assist in the creation of SAR MASINT products.  In particular, SAR MASINT often makes use of SAR complex image pairs to form interferometric products such as coherent change detection (CCD) or elevation change detection products.  In order for SAR complex images to be suitable for this sort of use they need to be formed in such a manner that they share near identical apertures in frequency space.  A MASINT processing service is often invoked to do SAR complex image pair formation in such a manner that a common frequency aperture is employed.<br><br>Special condition image formation is only one example of a SAR MASINT processing service.  Other SAR MASINT processing of a different type is crucial to producing other types of SAR MASINT products.<br><br>Similarly, MASINT processing services are used to generate certain hyperspectral MASINT products from raw hyperspectral collections.  Examples include specialized methods of compensating for atmospherics as an initial step in material signature detection processing, etc.  Yet other MASINT processing services are involved in thermal infrared MASINT product formation.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.4.1",
      "label" : "3.3.4.1 Language Translation",
      "description" : "<b>Definition:</b>Language Translation renders the meaning within a file from a source language and provides an equivalent version of that file in a user selected target second language.<br><b>Description:</b>Language Translation takes text, a document, or a URL as input along with a specification of the original and destination language.  It then translates the material into the destination language.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.4",
      "label" : "3.3.4 Data Exploitation",
      "description" : "<b>Definition:</b>The family of services to exploit data sources, to include language translation.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.3.1",
      "label" : "3.3.3.1 Source Management",
      "description" : "<b>Definition:</b>The service that supports identification, coordination, and protection of HUMINT resources.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.3",
      "label" : "3.3.3 CI/HUMINT Processing",
      "description" : "<b>Definition:</b>CI/HUMINT Processing provides the intelligence derived from the collection discipline in which the human being is the primary collection instrument and can be both a source and collector.<br><b>Description:</b>CI/HUMINT Processing involves primarily document language translation and automatic extraction of document metadata (e.g. place names, people, organizations, times, and key actions or relationships). <br><br>It also encompasses services that mine the internet or other repositories for documents pertaining to selected topics (though this service also falls under OSINT when the repository is in the public domain).  This latter service type is the equivalent of an automatic \"clipping service\".  Similarly, specialized RSS feeds could be considered CI/HUMINT processing services.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.2.1",
      "label" : "3.3.2.1 Signal Pattern Recognition",
      "description" : "<b>Definition:</b>Signal Pattern Recognition provides signal pattern recognition in order to detect signal characteristics that tell analysts information about the emitting source.<br><b>Description:</b>Signal Pattern Recognition characterizes a signal based on key signal parameter values and then accesses a database of signal patterns to determine if they match known signal characteristics.   When matches are found, the signal is labeled as belonging to a specific type, piece, or mode of equipment or might indicate transmission protocols.<br><br>Signal characteristics might include signal carrier radio frequency, bandwidth, pulse duration(s) and repetition interval(s), dwell or scanning time, polarization, or pulse envelope characteristics (pulse rise and fall times, shape characteristics (e.g. linear chirp or triangular shaped pulse), frequency or phase modulation, and time bandwidth products).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.2",
      "label" : "3.3.2 SIGINT Processing",
      "description" : "<b>Definition:</b>SIGINT Processing services provide processing of raw signals data to form basic SIGINT products.<br><b>Description:</b>SIGINT processing includes services used to filter, screen, resample, and automatically detect certain basic signal patterns for the purpose of characterizing raw input signals and assigning metadata, prior to exploitation.  This includes application of Fast Fourier Transform (FFT) processing and filters that are applied to the resulting frequency domain products.  SIGINT processing can also include specific convolution and noise reduction services used to clean up raw collected signal.<br><br>Another aspect of SIGINT processing involves very high volume data ingest services, data formatting, automatic dissemination of select snippets of processed SIGINT data, and certain types of near real time collection command and control.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.5",
      "label" : "3.3.1.5 Image Chipping",
      "description" : "<b>Definition:</b>Image Chipping Service creates user-defined full resolution chip from NITF (National Imagery Transmission Format) image.<br><b>Description:</b>Image Chipping Service allows users to create an image chip from a larger image.  Imagery chips are full resolution \"What You See Is What You Get\" (WYSIWYG) from the current image view.  Users can select square/rectangle around an area of a detailed overview of an image, to create a chip, or smaller piece of the larger image.  <br>This service is needed to reduce the size of the image file and to reduce the image to just the area relevant to the work it will be used for.  Reducing the size of the file decreases the demand on the system and the network communications.  Reducing the image to eliminate unneeded portions of the image simplifies the task for the image analyst.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.4",
      "label" : "3.3.1.4 State Service",
      "description" : "<b>Definition:</b>State Service converts image support data into a related sensor model state string.<br><b>Description:</b>State Service extracts the image support data from a file and creates a sensor model state string.   Note: this sensor model state string is a necessary input for National Geospatial-Intelligence Agency (NGA) Common Geospatial Services (CGS).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.3",
      "label" : "3.3.1.3 AOI Processing",
      "description" : "<b>Definition:</b>AOI Processing enables utilities for adding, deleting and updating persisted user based geometrical areas of interest (AOI). This allows a user to create and manage an AOI (geometrical based).<br><br>Ref: GCCS-i3 (SOAP based )<br><b>Description:</b>None",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.2",
      "label" : "3.3.1.2 FMV Geoprocessing",
      "description" : "<b>Definition:</b>FMV Processing georegisters a stream of video frames in real time..<br><b>Description:</b>FMV georegistration is a real time processing service that takes a video stream and georegisters every frame.  The result is a continuous stream of video frames georegistered to an underlying digital elevation map and reference imagery or to Digital Point Positioning Data Base (DPPDB) reference products.  Any frame in the video stream can be used for precision targeting or other precision position extraction applications.  <br><br>The frames are also crudely mosaicked (i.e. they are all correctly geopositioned but may still have visible \"seams\" from small scene intensity changes during the collection).  These visible seams can be removed by a subsequent FMV mosaicking process that blends image intensities across frame boundaries.<br><br>The FMV georegistration service ingests a stream of FMV frames and either reference imagery with associated reference DEMs or reference DPPDB.  If the quality of the FMV collection metadata is poor (such as is common from small UAVs or mini UAVs), the FMV georegistration service can optionally ingest point correspondences between the 1st frame in the FMV stream and the reference imagery in order to initiate the georegistration process.<br><br>Once the process starts, the service updates the FMV collection metadata for every frame based on a Kalman filter estimation of sensor position and attitude that is augmented by periodic automatic extractions of matching point correspondences between selected frames and the reference imagery.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.1",
      "label" : "3.3.1.1 Image Rectification",
      "description" : "<b>Definition:</b>Image Rectification provides transformations to project two or more images onto a common image or geocoordinate plane.<br><b>Description:</b>Rectification is a process of geometrically correcting an image so that it can be represented on a planar surface with a standard coordinate system.  This is often done to make an image geometrically conform to other images acquired from other perspectives.  This supports applications such as image/video mosaicking (combining multiple photographic images with overlapping fields of view to produce a larger, apparently seamless image), change detection, and multisensor data fusion.<br><br>It is also done to make an image or multiple images conform to a map, often for geographic information system (GIS) purposes. A common method for this service is to associate geolocations with pixels on an image.  The image pixels are then resampled to place them in into a common map coordinate system. <br><br>Another important application in which a rectification service gets used is in performing an epipolar rectification to ensure that corresponding match points in a pair of stereo images are displaced only along a single axis.  This enables easier matching and analysis of corresponding points in stereo image pairs by reducing the pixel stereo offsets to lie entirely along a single preferred axis (the epipolar axis).<br><br>The Image rectification Service ingests at a minimum an image, and a description of the planar surface to be used in the rectification process.  In some instances, such as for epipolar rectification, the target planar surface is described entirely by a second image in a stereo pair.  Automatic point correspondence matching establishes the required rectification transformation.  In other instances, such as orthorectification, it is common to supply either a reference image and a corresponding digital elevation map (DEM) or a reference image pair such as is found in a Digital Point Positioning Data Base (DPPDB). The DEM is used in performing the rectification transformation to minimize unsightly image artifacts that can affect a rectification done on perspective images in cases where no information about underlying terrain is supplied.  The simplest input to a rectification service is an input image to be rectified with image support data and a specification of a horizontal plane surface.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1",
      "label" : "3.3.1 GEOINT Processing",
      "description" : "<b>Definition:</b>GEOINT Processing provides processing of raw imagery and geospatial data to form basic GEOINT products.<br><b>Description:</b>The GeoINT processing family provides processing of imagery and  raw geospatial data to form basic GEOINT products.  Examples include forming images from raw source data, image format conversions, image compression,  basic video processing such as mosaicking, etc.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.3",
      "label" : "3.3 Processing and Exploitation",
      "description" : "<b>Definition:</b>The Processing/Exploitation line includes services that provide the ability to transform collected information into forms suitable for further analysis and/or action by man or machine.<br><b>Description:</b>The Processing/Exploitation line includes services that provide the ability to transform collected information into forms suitable for further analysis and/or action by man or machine.  This includes the following sub-areas: Data Transformation – The ability to select, focus, simplify, tag and transform overtly or covertly collected data into human or machine interpretable form for further analysis or other action.  Information Categorization – The ability to identify, classify and verify information associated with time sensitive objectives enabling further analysis or action.  (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)<br>Families in this line: GEOINT PROCESSING, SIGINT PROCESSING, CI/HUMINT PROCESSING, and MASINT PROCESSING.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.6.2",
      "label" : "3.1.6.2 Sensor Planning",
      "description" : "<b>Definition:</b>Sensor Planning Service provides an interface for determining the capabilities and tasking of SPS-registered DI2E sensors.<br><b>Description:</b>The Sensor Planning Service (SPS) provides a standardized interface for interoperable sensor tasking.  The SPS provides the client support for determining the capabilities of the sensor system, identifying the feasibility of a tasking request, accepting commands to task the sensor, tracking the status of sensor commands, and provide a reference to the observation data following the completion of a successful task.  All of which combine to obtain varying stages of planning, scheduling, tasking, collection, processing, archiving, and distribution of resulting observation data and information that is the result of a request.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.6.1",
      "label" : "3.1.6.1 Sensor Observation",
      "description" : "<b>Definition:</b>Sensor Observation provides Observation data and Sensor descriptions for SOS-registered DI2E sensors.<br><b>Description:</b>The Sensor Observation Service (SOS) provides a standardized interface for managing and retrieving metadata and observations from heterogeneous sensor systems.  It enables discovery and retrieval of real time or archived sensor descriptions, observations, and features of interest.  It allows the client to insert and delete sensor data.  The SOS also supports the functionality to list the capabilities of all registered sensors.  <br><br>[This service is supported by the OGC SWE SOS 2.0 specification.]",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.6",
      "label" : "3.1.6 Sensor Web Enablement",
      "description" : "<b>Definition:</b>The Sensor Web Enablement (SWE) family of services provides  observation data and sensor descriptions and well as an  interface for tasking of SPS-registered DI2E sensors.<br><b>Description:</b>The Sensor Web Enablement (SWE) family of services provides  a standardized interface for managing and retrieving metadata and observations from heterogeneous sensor systems including discovery and retrieval of real time or archived sensor descriptions, observations, and features of interest.    They also provide a standardized interface for interoperable sensor tasking, sensor capabilities determination, tasking feasibility assessment, tasking request, sensor command tracking, and observation data following the completion of a successful task.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.5.2",
      "label" : "3.1.5.2 Task Asset Request",
      "description" : "<b>Definition:</b>Task Asset Request provides collection and sensor deployment plans.   Changes to these plans are sent to the sensor/platform through this interface. <br><br>Ref. DCGS-Army chicklit definition<br><b>Description:</b>Task Asset Request serves as the interface that all services use to send sensor messages to the Sensor Provisioning service.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.5.1",
      "label" : "3.1.5.1 Tasking Message Preparation",
      "description" : "<b>Definition:</b>Tasking Message Preparation formats the tasking information of the Collection Requirements Planning service for dissemination.<br><b>Description:</b>Tasking Message Preparation allows client to request reformatting of the collection requirement and tasking assignments into a distributable format based on a specified day, sensor group, asset, or requirement set.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.5",
      "label" : "3.1.5 Tasking Request",
      "description" : "<b>Definition:</b>The family of services that support tasking requests<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.2",
      "label" : "3.1.4.2 Asset Discovery",
      "description" : "<b>Definition:</b>Asset Discovery publishes and finds information on DI2E connected devices such as  sensors, network hosted IT equipment, and weapons systems.<br><b>Description:</b>Asset Discovery services maintain a repository of potentially available DI2E assets. Operations typically include Create, Read, Update, Delete (CRUD) type operations on the asset repository database(s) (with appropriate permissions) as well as operations for asset search and asset search results reporting.   Examples of typical asset information might include the asset name, location, operational status, various service descriptors, or anticipated shut down dates (many other fields of data are possible). One example of use is a  analyst might use this service within an application that provides intelligence on the assets that might be available to be tasked due to its services and current geographical location.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.1",
      "label" : "3.1.4.1 Asset Status Summary",
      "description" : "<b>Definition:</b>Asset Status Summary provides aggregated asset status metadata (including sensors and sets of sensors) across the enterprise.<br><b>Description:</b>Asset Status Summary allows the client to query the status of a specific sensor, a set of sensors, or all sensors across the enterprise.    Results of status summary queries enable reporting of actively monitored assets across the ISR spectrum, regardless of source.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4",
      "label" : "3.1.4 Asset Reporting",
      "description" : "<b>Definition:</b>The Asset Reporting family provides services that support discovery, status, and the tasks assigned to intelligence assets.<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.5",
      "label" : "3.1.3.5 Target Planning",
      "description" : "<b>Definition:</b>Target Planning is the systematic examination of potential target systems-and their components, individual targets, and even elements of targets-to determine the necessary type and duration of the action that must be exerted on each target to create an effect that is consistent with the commander's specific objectives. (JP 1-02. SOURCE: JP 3-60)<br><br><b>Description:</b>Target Planning services support the Target Development process by enabling target system analysis; entity-level target development; and target list management (TLM). <br> <br>Target Development is defined as: The systematic examination of potential target systems-and their components, individual targets, and even elements of targets-to determine the necessary type and duration of the action that must be exerted on each target to create an effect that is consistent with the commander's specific objectives. (JP 1-02. SOURCE: JP 3-60)",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.4",
      "label" : "3.1.3.4 Exploitation Planning",
      "description" : "<b>Definition:</b>Exploitation Planning tracks analyst skills and availabilities to identify who will do the sensor output analysis and assigns it to them.<br><b>Description:</b>Exploitation Planning allows Analyst Managers to view, create, update, or delete information about a specific analyst.  Analyst Taskers can view, create, update, or delete tasks associated with specific analyst.     Completed, outstanding, or in-work tasks can be viewed by analyst, unit of time (day, week, etc.), or collection requirement.     Assignment Generators evaluate necessary skills, determine appropriate analysts with available time, and assign tasks to the best fitting analyst(s).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.3",
      "label" : "3.1.3.3 Intelligence Source Selection",
      "description" : "<b>Definition:</b>Intelligence Source Selection services support the review of mission requirements for sensor and target range, system responsiveness, timelines, threat, weather, and reporting requirements.   to identify and determine asset and/or resource availability and capability.  Source selection applies to all types of collection sources (i.e. GEOINT, HUMINT, SIGINT, etc.).<br><b>Description:</b>none",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.2",
      "label" : "3.1.3.2 Sensor Cataloging",
      "description" : "<b>Definition:</b>Sensor Cataloging allows clients to request information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.<br><b>Description:</b>Sensor Cataloging allows clients to request information about sensors and servers, add or delete sensor information from an external catalog,  subscribe or unsubscribe to a sensor status, or add, modify, or delete sensor metadata from a sensor instance.    The client can receive metadata documents describing a server's abilities, search for sensors instances on a registry server instance, and receive sensor instance descriptions from the server for either one or multiple sensors.       <br><br>The client can also insert sensor metadata into the registry server,  update sensor metadata on the registry server, or remove sensor metadata from the registry server.   In addition, the client can establish or remove a link between a sensor registry entry instance and an external OGC catalog.   Finally the client can get the sensor status, insert a sensor status, subscribe to a sensor status, or cancel a sensor status subscription.              <br><br>[This service is supported by the OGC SWE SIR discussion paper.]",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.1",
      "label" : "3.1.3.1 Collection Requirements Planning",
      "description" : "<b>Definition:</b>Collection Requirements Planning provides editing and visibility into the evolution of tasking and collection-related artifacts<br><b>Description:</b>Collection Requirements Planning aggregates information such as asset tasking lists as they mature into ISR Synch Matrix and Proposed Collection Plans.    Using this service collection requirements managers can view, add, delete, or update requirements for a specific collection.  It also allows them to view the mapping of collection requirement to collection task.    It also allows tasking managers to view, add, delete, or update tasking for a specific sensor.<br><br>Both tasking requirements managers and task managers can view mapping of the collection tasks to collection requirements, collection results, and status of collection tasks.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3",
      "label" : "3.1.3 Planning",
      "description" : "<b>Definition:</b>The family of services that support the Intelligence Planning Process.<br><b>Description:</b>Operation planning occurs in a networked, collaborative environment, which requires iterative dialogue among senior leaders, concurrent and parallel plan development, and collaboration across multiple planning levels. The focus is on developing plans that contain a variety of viable, embedded options (branches and sequels).  This facilitates responsive plan development and modification, resulting in “living” plans (i.e., the systematic, on-demand, creation and revision of executable plans, with up-to-date options, as circumstances require). This type of adaptive planning also promotes greater involvement with other US agencies and multinational partners.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.2",
      "label" : "3.1.1.2 RFI Management",
      "description" : "<b>Definition:</b>The service to create, deconflict, monitor, and disseminate Intelligence Requests for Information (RFI)<br><b>Description:</b>An RFI is a specific time-sensitive ad hoc requirement for information or intelligence<br>products, and is distinct from standing requirements or scheduled intelligence production. An RFI can<br>be initiated at any level of command, and will be validated in accordance with the combatant command’s<br>procedures. An RFI will lead to either a production requirement if the request can be answered with<br>information on hand or a collection requirement if the request demands collection of new information.<br>Collection planning and requirement management are major activities during planning and direction.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.1",
      "label" : "3.1.1.1 PIR Management",
      "description" : "<b>Definition:</b>PIR Management services support management of the Priority Intelligence Requirement (PIR) process .  Supports: <br>1. Deriving PIRs from CCIRs<br>2. Consolidating and recommending PIR nominations<br>3. Deriving Information Requirements from Intelligence Requirements and PIRs<br>4. Identifying EEIs which will answer PIRs<br><b>Description:</b>PIR Management services assist a commander in deriving Priority Intelligence Requirements (PIR) to support a mission, tracking, updating, consolidating existing PIRs, and turning those PIRs into specific requirements that can be used for intelligence collection planning.  The PIR Management service works with the Collection Requirements Planning service to task and plan collection activities.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1",
      "label" : "3.1.1 Define and Prioritize Requirements",
      "description" : "<b>Definition:</b>The family of services to define, document, and prioritize intelligence requirements<br><b>Description:</b>N/A",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1",
      "label" : "3.1 Planning and Direction",
      "description" : "<b>Definition:</b>The Planning & Direction line includes services that provide the ability to synchronize and integrate the activities of collection, processing, exploitation, analysis and dissemination resources to meet BA information requirements.<br><b>Description:</b>The Planning & Direction line includes services that provide the ability to synchronize and integrate the activities of collection, processing, exploitation, analysis and dissemination resources to meet BA information requirements. This includes the following sub-areas: Define and Prioritize Requirements  – The ability to translate national through tactical objectives and needs into specific information and operational requirements.  Develop Strategies  – The ability to determine the best approach to collect, process, exploit, analyze, and disseminate data and information to address requirements and predict outcomes. Task and Monitor Resources  – The ability to task, track, direct, and adjust BA operations and their associated resources to fulfill requirements. Evaluation  – The ability to assess the results of BA operations and products to ensure that user requirements are being met. (Ref. Joint Service Areas, JCA 2010 Refinement, 8 April 2011)<br>Families in this line: ASSET MANAGEMENT and PLANNING and REPORTING.",
      "fullTextAvailable" : false
    }, {
      "code" : "0",
      "label" : "0 DI2E",
      "description" : "<b>Definition:</b>The DI2E SvcV-4 documents services that are needed for the DI2E to achieve the desired level of service reuse and interoperability .<br><b>Description:</b>The services that exist within the Defense Intelligence Enterprise that are governed by the DI2E.  The services shall be developed in a service oriented manner, be registered and accessible in the Enterprise Registry, and be tested and certified for reuse.",
      "fullTextAvailable" : false
    }, {
      "code" : "1",
      "label" : "1 Infrastructure Services",
      "description" : "<b>Definition:</b>The functions/services that support the Enterprise and don't usually have a direct relationship to the mission or business processes.<br><b>Description:</b>Infrastructure Services are fundamental to the DI2E.  This consistent set of services across the DI2E is foundational to future SOA-based enterprise services.     <br><br>Specific service lines include:<br><br>   • Enterprise Management<br>   • Web Service Security<br>   • Web Service Discovery<br>   • Messaging, and <br>   • Orchestration",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2.2",
      "label" : "1.4.2.2 Protocol Mediation",
      "description" : "<b>Definition:</b>Protocol Mediation provides the ability to manage service communication format between sender and receiver.<br><b>Description:</b>The Protocol Mediation service provides transformation and processing of service communication between sending and receiving services.  One example is mediating e-mail messages between POP to IMAP formats.    Another example is chat mediation between IRC or XMPP formats. Mediation between SOAP and REST is another example.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2.1",
      "label" : "1.4.2.1 Execution Engine",
      "description" : "<b>Definition:</b>An execution engine takes the definition of a service orchestration (e.g., BPEL) and executes the process<br><b>Description:</b>Service orchestrations are only definitions of how services should be executed in sequence to complete a given business process. Execution of the orchestration requires some form of engine that will run the orchestrated process and return a result to the user. Most existing execution engines execute processes defined in Business Process Execution Language (BPEL). Other possibilities are available, including newer version of Business  Process Modeling Notation that are executable; mashup frameworks, and similar technologies",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2",
      "label" : "1.4.2 Orchestration Execution",
      "description" : "<b>Definition:</b>Orchestration execution performs the ordered calling of services as designed in service orchestration models (see orchestration modeling service).<br><b>Description:</b>Orchestration execution makes in-sequence calls to services, holds intermediate inputs/outputs, handles exceptions, monitors execution status, and provides an interface for user presentation. <br><br>Common orchestration execution features include: W3C XHTML/CSS conformance, templating, class autoloading, object based design, nested groups with multiple assigned permissions, form design tools including help and error messages, labels, validation rules, inherited authorization, and macros support.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.2",
      "label" : "1.4.1.2 Optimization",
      "description" : "<b>Definition:</b>Optimization services provide analysis of the possible orchestrations and offer recommended alternatives to ensure the user's needs are best met from among the available services and orchestrations.<br><b>Description:</b>For any given business process, there may be several possible orchestrations to choose from. These orchestrations may differ only in the instances of specific services that they use (e.g., the same service offered at different endpoints) or they may perform the same process using completely different service offerings. In a dynamic network where connectivity and QoS will vary widely over time, selecting the best orchestration from among the available options will be a complex task.<br><br>Additionally, in an environment where new services or new service instances may appear on the network frequently, it may be necessary to frequently update the orchestration to best meet the current situation.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.1",
      "label" : "1.4.1.1 Matchmaking",
      "description" : "<b>Definition:</b>Matchmaking service provides a capability to verify that an individual service performs a desired task and that two or more services can be orchestrated by ensuring that the outputs of each service in the planned orchestration contain suitable inputs for the next service in the orchestration.<br><b>Description:</b>Matchmaking services ensure the composability of services by comparing the function of a given service fulfills the task in the business process that the service is expected to complete. For example, if a process includes a step that converts an image file from JPG format to PNG format, any service that performs this task in the orchestration must indeed perform image format conversions.<br><br>In addition, the matchmaker service ensures that each individual service is suitable for orchestration with its predecessors and successors in the orchestration based on the inputs, outputs, preconditions, and effects (IOPEs) of each service. Put simply, in order to orchestration two services, A and B, it is necessary that Service A's outputs contain all the necessary inputs for Service B, those inputs are in the proper format, all security requirements are met, etc.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1",
      "label" : "1.4.1 Orchestration Planning",
      "description" : "<b>Definition:</b>Service Orchestration Planning services provide support functions necessary to complete the process of converting a process model into an executable chain of services.<br><b>Description:</b>Service Orchestration Planning services support coordinated arrangement, and management of individual services by ensuring a planning orchestration is feasible from among the available services. Orchestration differs in this sense from choreography in this sense by containing a central controller process that participating web services don't know about.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4",
      "label" : "1.4 Orchestration Management",
      "description" : "<b>Definition:</b>Orchestration Management provides automated SOA service as well as human operation modeling and execution.<br><b>Description:</b>Orchestration Management services can provide automated SOA service as well as human operation modeling and execution. Currently the DI2E has mapped out services for service orchestration (modeling and execution) but is considering another family of services to cover human workflow (workflow modeling and workflow management).",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.3",
      "label" : "1.3.2.3 Service Configuration Verification and Audit",
      "description" : "<b>Definition:</b>Service Configuration Verification and Audit defines the services that perform regular checks, ensuring that the information contained in the CMS is an exact representation of the Configuration Items (CIs) actually installed in the live production environment.<br><b>Description:</b>none",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.2",
      "label" : "1.3.2.2 Service Configuration Control",
      "description" : "<b>Definition:</b>Service Configuration Control defines the services that ensure that no Service Configuration Items are added or modified without the required authorization, and that such modifications are adequately recorded in the CMS. <br><b>Description:</b>Service Configuration Control ensures that any changes to Configuration Items (CIs) are made by authorized parties, in accordance with the established configuration management policies, processes, and procedures.<br><br>Note: Configuration Control enables the review of modifications to the Configuration Management System (CMS), to make sure the information stored in the CMS is complete and the modification was done by an authorized party. Other processes also support the objectives of Configuration Control: Configuration Identification defines who is authorized to make certain changes to the CMS. In a broader sense, Change Management and Release Management with their defined procedures also help to ensure that no unauthorized changes occur.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.1",
      "label" : "1.3.2.1 Service Configuration Identification",
      "description" : "<b>Definition:</b>Service Configuration Identification defines the services to define and maintain the underlying structure of the Configuration Management System (CMS) so that it is able to hold all information on Service Configuration Items (CIs). This includes specifying the attributes describing CI types and their sub-components, as well as determining their interrelationships.<br><b>Description:</b>Service Configuration Identification stores information about configuration management (CM) controlled system components, called Configuration Items (CIs).  Configuration Items may be source code, compiled GOTS or COTS binaries, hardware lists, system settings or any combination thereof that make a cohesive unit and is under CM control.  Service Configuration Identification tracks specific information about each configuration item in the architecture, its sub-components, and its relationship to other CIs.  It also tracks information about who has authority to approve changes to each configuration item, such as the responsible Configuration Control Board.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2",
      "label" : "1.3.2 Service Configuration Management",
      "description" : "<b>Definition:</b>Service Configuration Management provides the set of services to provide configuration management and lifecycle support for registered services<br><b>Description:</b>family",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.3",
      "label" : "1.3.1.3 Service Publishing",
      "description" : "<b>Definition:</b>Service Publishing allows service providers to publish information about available services in a metadata repository and categorize these services within related service taxonomies. Note that the repository is focused on design time lookup.<br><b>Description:</b>The service publishing service allows service providers to publish information about themselves (service name, description, creating organization, file size, sponsoring agency, test accreditations, etc.), their service specifications (applicable standards, related conformance requirements, etc.), service offerings (versions of the service that are available), and service access points (internet URLs where the service can be reached) in a UDDI service registry.   It also allows service providers to categorize their services within related taxonomies.     Publishing limits are controlled through security protocols and related service registry publishing policies.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.2",
      "label" : "1.3.1.2 Service Subscription",
      "description" : "<b>Definition:</b>Service subscription service notifies potential service consumers of the availability of services as they become registered.<br><b>Description:</b>The Service Subscription service allows service consumers to receive a notification when services of interest are registered in the Service Registry.   Consumers create a subscription containing service criteria that describes services they would be interested in.  When services are registered that fit the specified criteria the consumer is notified via email or a Simple Object Access Protocol (SOAP) message.   A registry must define the policy for supporting subscriptions including whether nodes may define their own policy.  In addition, policies that may be defined include restricting use of subscription, establishing additional authentication requirements, identifying the duration or life of a subscription, limiting subscriptions, and articulating exactly who can do what relative to subscriptions.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.1",
      "label" : "1.3.1.1 Service Inquiry",
      "description" : "<b>Definition:</b>Service Inquiry allows consumers to query a service registry to find and retrieve service offerings.<br><b>Description:</b>The Service Inquiry service allows service consumers to locate and obtain detail on service entries in the registry.  This includes individual users and machine-to-machine requests.   Using published service metadata (e.g., by name, category, provider, etc.), service consumers specify the criteria to be used to discover service offerings and then retrieve key service information (service name, description, URL, etc.).   The Inquiry Service also offers the service consumer a means to dynamically find service access points at runtime in order to build location transparency of services into their applications.  Related UDDI standards, which specify Hypertext Transfer Protocol Secure (HTTPS) and authorization support,  allow users and systems to publish, subscribe, and discover web services in a secure manner.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1",
      "label" : "1.3.1 Repository and Registry",
      "description" : "<b>Definition:</b>The Repository and Registry family of services enable inquiry, subscription, and publishing, of DI2E services.<br><b>Description:</b>The Repository and Registry family of  services enable publishing, inquiry, and subscription of existing system services using the Universal Description Discovery & Integration (UDDI) specification.    Through this set of services service providers are able to register (publish) the services they wish to make available to the DI2E community so that application/system/baseline providers can find & access them (service inquiry) and ask for notification when services that might meet their needs become available or updated (service subscription).",
      "fullTextAvailable" : false
    }, {
      "code" : "1.3",
      "label" : "1.3 Service Management",
      "description" : "<b>Definition:</b>Service Management capabilities provide publishing of, querying about, subscription and configuration management of services.<br><b>Description:</b>Service Management capabilities provide publishing of, querying about, subscription and configuration management of services.    Information can be metadata about the service (registered in a Universal Description Discovery & Integration (UDDI) specification) or artifacts not strictly held in the UDDI registry but directly relevant to the registered service(s) (Installation manuals, test procedures, WSDL definitions, specifications, technical background, related architecture diagrams, etc.)",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.6",
      "label" : "1.2.6 Cross Domain",
      "description" : "<b>Definition:</b>The Cross Domain services aid in the enforcement of security domain policies regarding the exchange of data and services across different security domain boundaries.<br><b>Description:</b>Cross Domain enforces security domain policies (and de-conflicts policies) regarding the exchange of data and services across different security domain boundaries (including proxies).  Cross Domain ensures that users or services have the proper clearances and credentials to perform requested activities across two or more network fabrics or security domains.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.5.2",
      "label" : "1.2.5.2 Audit Log Reporting",
      "description" : "<b>Definition:</b>The Audit Log Reporting service provides analysis reports on information contained in security audit logs.<br><b>Description:</b>Audit Log Reporting works in sync with Audit Log Management to analyze and report audit log information including Quality of Service (QoS) metrics and operational status (availability, faults, etc.).   Ideally, managers are able to leverage audit log reporting to identify and respond to service problems before critical service failures.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.5.1",
      "label" : "1.2.5.1 Audit Log Management",
      "description" : "<b>Definition:</b>The Audit Log Management service supports security auditing by recording system accesses and operations and providing notification for certain events.<br><b>Description:</b>Audit Log Management supports security auditing by recording a chronological record of system activities including system accesses and operations; and providing notification when previously identified events (e.g. a system goes down) occur.    Audit Log Management's purpose is to provide the data needed to enable reconstruction and examination of sequences of activities surrounding, or leading to, a specific event.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.5",
      "label" : "1.2.5 Audit Management",
      "description" : "<b>Definition:</b>Audit Management defines the set of services that support the creation, persistence, and access of audit information relating to activities to access or<br>utilize an application, system, network, or information resource.<br><b>Description:</b>family",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.5",
      "label" : "1.2.4.5 Incident Response",
      "description" : "<b>Definition:</b>Incident Response services provide active response and remediation to a security incident that has allowed unauthorized access to an information system<br><b>Description:</b> If a security incident occurs, Incident Response services assist administrators and authorities in stopping the incident to prevent additional damage, determining the scope and impact of the incident, restoring operational capability, performing forensics to determine how the incident occurred, and taking action to ensure that the incident does not occur again.  Incident Response services may also include tools to assist in legal action, if that is part of the organization's incident response plan.  Incident Response services are tied closely to organizational policies and procedures, and may include workflows or other business process tools to assist responders in following pre-established procedures.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.4",
      "label" : "1.2.4.4 Virus Protection",
      "description" : "<b>Definition:</b>Virus Protection and Malicious Code services identify and respond to specific security threats, including use of firewalls, anti-spam software, anti-virus software, and malware protection.<br><b>Description:</b>Virus Protection is a specific kind of Intrusion Detection and Intrusion Prevention system aimed at targeting automated- or self-spreading malicious software (malware).  Virus Protection software is often combined with other intrusion detection and prevention software to form a complete host-based security system.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.3",
      "label" : "1.2.4.3 Intrusion Prevention",
      "description" : "<b>Definition:</b>Intrusion Prevention services include penetration testing and other measures to prevent unauthorized access to an information system.<br><b>Description:</b>Intrusion Prevention services attempt to enforce limitations on process permissions and code execution capabilities to mitigate or stop entirely the execution of malicious software or activity on a system.  Network and host-based firewalls, technologies that enforce application whitelisting, and software that quarantines or removes suspicious software or processes are examples of Intrusion Prevention services.  Intrusion Prevention services often work closely with Intrusion Detection services; in many cases they are bundled together into the same software package.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.2",
      "label" : "1.2.4.2 Intrusion Detection",
      "description" : "<b>Definition:</b>Intrusion Detection services support the detection of unauthorized access to an information system.<br><b>Description:</b>Intrusion Detection services monitor each computer in the enterprise and attempt to identify suspicious activity.  In the event that suspicious activity is detected, Intrusion Detection services notify other services so that immediate action can be taken.  Intrusion Detection services, on their own, do not attempt to prevent malicious activity.  They simply detect suspicious activity and notify other services or personnel, who are then responsible for responding appropriately.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.1",
      "label" : "1.2.4.1 Vulnerability Reporting",
      "description" : "<b>Definition:</b>Vulnerability Reporting provides access to known reported vulnerabilities so appropriate action can be taken.<br><b>Description:</b>none",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4",
      "label" : "1.2.4 System and Communication Protection",
      "description" : "<b>Definition:</b>System and Communication Protection is the family of services that monitor, control, and protect organizational communications (i.e., information transmitted or received by organizational information systems) at the external boundaries and key internal boundaries of the information systems; and employ architectural designs, software development techniques, and systems engineering principles that promote effective information security within organizational information systems.<br><b>Description:</b>The System Communication and Protection family of systems focuses primarily on Computer and Network Defense.  Services within this family help to ensure that all systems are properly patched to mitigate or eliminate known vulnerabilities, configured to mitigate not-yet-known (i.e., 0day) vulnerabilities, defend against malicious software, and respond and recover from successful attacks or other security incidents.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3.2",
      "label" : "1.2.3.2 Security Label Format Validation",
      "description" : "<b>Definition:</b>Security Label Format Validation validates the security marking of metadata.<br><b>Description:</b>Security Label Format Validation validates the security marking of metadata.  Forwards metadata that passes validation.  Rejects metadata that fails validation.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3.1",
      "label" : "1.2.3.1 Data Security Marking",
      "description" : "<b>Definition:</b>Data Security Marking defines the services that create<br>and annotate security classification to a specific information resource<br><b>Description:</b>none",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3",
      "label" : "1.2.3 Security Metadata Management",
      "description" : "<b>Definition:</b>Security Metadata Management supports the annotation of data items (such as content, documents, records) with the proper security classifications, and applicable metadata for access control (which may include environment metadata).<br><b>Description:</b>The Security Metadata Management family of services help ensure that products within the system have proper security markings and labels appropriate to their classification.  These markings may be used by other automated systems, such as the services within the Dissemination Management family, to make authorization or releasability decisions.  While releasability may still have a man-in-the-loop, it is nonetheless important to ensure that consistent, properly-formatted and well-understood security markings are present, to minimize unauthorized disclosure or other classified information spillage.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.11",
      "label" : "1.2.1.11 Certificate Validation",
      "description" : "<b>Definition:</b>Certificate Validation ensures a presented security assertion within a security token matches similar data in the Certification Authority's (CA) root certificate.<br><b>Description:</b>Certificate Validation ensures a presented PKI certificate has been signed by a trusted CA, is within the valid timeframe, has not been tampered with, has not been revoked, etc.  Certificate Validation Service allows clients to delegate some or all certificate validation tasks and is especially useful when a client application has limited Public Key Infrastructure (PKI) services.   The service corresponds to a “Tier 2 Validation Service” and shields client applications from such PKI complexities as X.509v3 certificate syntax processing (e.g., expiration), revocation status checking, or certificate path validation.  <br>Certificate Validation may include any OCSP responders.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.2.1",
      "label" : "1.2.2.1 Encryption/ Decryption",
      "description" : "<b>Definition:</b>Encryption/Decryption defines the set of services that encrypt and decrypt interactions between consumers and providers to support minimal confidentiality requirements. Within a PKI-environment, encryption and decryption processes are done using the provider’s public and private keys.<br><b>Description:</b>Encryption/Decryption can occur at multiple “levels” within an interaction.  For example, transport level encryption may be sufficient for many interactions. In this case, the provider’s public and private keys are used in the encryption process. For interactions requiring additional encryption, message-level encryption may be used where the subject’s public and private key may be utilized.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.2",
      "label" : "1.2.2 Cryptography Management",
      "description" : "<b>Definition:</b>Cryptography Management defines the set of services that support the generation, exchange, use, escrow and management of ciphers (e.g., keys), including the use of encryption/decryption processes to ensure the confidentiality and integrity of data.<br><b>Description:</b>The Cryptography Management family covers all manner of cryptography-related services.  This includes both symmetric and asymmetric crypto functions, one-way hashing functions, management of encryption keys, digital signature verification, and validation of PKI certificates.  The services within this family may not be discrete, stand-alone services as one would picture in the traditional SOA sense; rather, many Cryptography Management services are likely to be libraries or executables that are embedded within, or bundled with, other applications.  For example, encryption-in-transit is required for many types of data.  An encryption service, for these types of data, would create a chicken-and-egg problem: data must be encrypted before it can be sent to the encryption service, but the encryption service must do the encryption.  In this example, the encryption libraries will likely be resident on the system that generates the sensitive data.  Nonetheless, it is important to explicitly call out the cryptographic functions that are performed within the architecture, to ensure that they are properly accounted for and not overlooked.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.10",
      "label" : "1.2.1.10 Attribute Access",
      "description" : "<b>Definition:</b>Attribute Access provides values that describe human users and non-person entities (NPE) for the purpose of making authorization decisions.<br><b>Description:</b>Attribute Access  accepts attribute requests from authorized consumers and responds with an attribute assertion containing the requested attribute values that are releasable to that consumer.   Attributes for either human users or non-person entities (NPE) can be passed and are typically used by Policy Decision Services, Policy Decision Points, Application Servers, or other SAML endpoints.   Secure attribute passing supports an Attribute-Based Access Control (ABAC) authorization model.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.9",
      "label" : "1.2.1.9 Federation Service Management",
      "description" : "<b>Definition:</b>Federation Service Management establishes trust relationships between different organizations or systems, allowing entities within an environment to access resources within another environment without the need to have an individual account on the remote environment.  This service defines and maintains trust relationships, which are then queried by the Security Token Validation service when making validation decisions.<br><b>Description:</b>none",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.8",
      "label" : "1.2.1.8 Security Token Service",
      "description" : "<b>Definition:</b>Security Token Service creates enterprise security tokens to provide authentication across systems and services.<br><b>Description:</b>Security Token Service forms the basis for identification and authentication activities by generating security tokens and inserting them in outgoing encrypted messages.   Passed security tokens contain authentication and attribute assertions that temporarily authenticate users.  Tokens also contain user attributes and a security assertion which can be validated by receiving policy services.  The Security Token Service may also perform token validation if required.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.7",
      "label" : "1.2.1.7 Policy Access Point",
      "description" : "<b>Definition:</b>Policy Access Point enables service providers and applications to request and retrieve access policies.<br><b>Description:</b>The Policy Access Point exposes authorization policies by retrieving and managing policies as implemented by Policy Decision Point (PDP) logic (e.g., access control over portlets in a portal server, or whether a service consumer may search the registry for the key to retrieve all implementations). Implementation may optionally be published in the Enterprise Service Registry (ESR) as defined by the DoD/IC Service Registry and Governance Working Group.     [SSP note: a recommended format for PAS messages is extensible Access Control Markup Language (XACML)]",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.6",
      "label" : "1.2.1.6 Policy Enforcement Point",
      "description" : "<b>Definition:</b>Policy Enforcement Point (PEP)enforces security-related policies for access to protected resources.<br><b>Description:</b>Policy Enforcement Points sit in front of the requested web services, intercepting incoming requests and outgoing responses to apply the appropriate access policies.  The PEP generates the access request to the Policy Decision Point (PDP) and interprets and enforces the PDP's decision.   Policies can be built to include, but are not limited to, authentication, authorization, data integrity, and confidentiality. When enforcing authorization policies, the PEP uses the PDP to evaluate an authorization policy for the resource.  When a security token is provided to the PEP, that token is validated prior to being trusted.  Token validation may occur in the PEP itself, or if the PEP is unable to validate the token, the PEP may leverage an STS to perform validation on its behalf.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.5",
      "label" : "1.2.1.5 Policy Decision Point",
      "description" : "<b>Definition:</b>Policy Decision Points accepts access requests and returns whether the request is appropriate given access rules and conditions.<br><b>Description:</b>A Policy Decision Point service hosts Quality of Protection (QoP) parameters and user attributes so that services can flexibly determine and execute protection measures.   For example, some services may require X.509 certificate based authentication whereas others may only need username / password authentication.   Or, clients that access a resource from different domains may require different “strengths” of authentication and access control.   PDPs do this by accepting authorization queries (typically XACML based), evaluating the request based on a variety of inputs (target resource, requested operation, requester's identity, etc.) and returning authorization decision assertions.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.4",
      "label" : "1.2.1.4 Authentication Service",
      "description" : "<b>Definition:</b>Authentication Service performs authentication of entities within the enterprise.<br><b>Description:</b>none",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.3",
      "label" : "1.2.1.3 Resource Policy Management",
      "description" : "<b>Definition:</b>Resource Policy Management creates and maintains access control policies for protected resources.<br><b>Description:</b>none",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.1",
      "label" : "1.2.1.1 Local Identity Management",
      "description" : "<b>Definition:</b>Local Identity Management provides the creation, maintenance, and deletion of user accounts, password maintenance, and the administration of user access rights. Also Creates local account from PKI certificate.  Maps DN to UserID in local account.<br><b>Description:</b>Local Identity Management provides the creation, maintenance, and deletion of user accounts, group membership and other user attributes, password maintenance, and the administration of user access rights for local, role-based access control systems. This service also creates local accounts from PKI certificates, and maps the DN from a certificate to a local UserID.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1",
      "label" : "1.2.1 Identity and Access Management",
      "description" : "<b>Definition:</b>Identity and Access Management (IdAM) defines the set of services that manage permissions required to access each resource.<br><b>Description:</b>IdAM includes services that provide criteria used in access decisions and the rules and requirements assessing each request against those criteria.  Resources may include applications, services, networks, and computing devices.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2",
      "label" : "1.2 Security Management",
      "description" : "<b>Definition:</b>Security Management encompasses the processes and technologies by which people and systems are identified, vetted, credentialed, authenticated, authorized for access to resources, and held accountable for their actions.<br><b>Description:</b>Line",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.5.1",
      "label" : "1.1.5.1 Global Unique Identifier (GUID)",
      "description" : "<b>Definition:</b>The GUID service provides a Uniform Resource Identifier (URI) to an entity in the enterprise<br><b>Description:</b>A GUID or Uniform Resource Identifier (URI) in the DI2E context provides a unique form of identification for any resource within the enterprise. A resource is anything that is or can be associated to the  Enterprise.  This can includes  nodes, data, information, role definitions and people.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.5",
      "label" : "1.1.5 Enterprise Resource Management",
      "description" : "<b>Definition:</b>Enterprise Resource Management defines the set of services that support unambiguous, assured, and unique identities for both person and non-person entities (NPE).<br><b>Description:</b>Enterprise Resource Management has a critical dependency on authoritative personnel and asset management systems for identity information, as well as Directory Services for the management of identity information.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.4.3",
      "label" : "1.1.4.3 Notification Consumer",
      "description" : "<b>Definition:</b>Notification Consumer service subscribes the producer services informing it that it wishes to start receiving notifications.<br><b>Description:</b>Notification Consumer service sets up end points to receive, either through pushed or pulled delivery, event notifications sent to it by Notification Producer or Notification Broker services.  In doing so, it can set up subscriptions and also unsubscribe from the producer or broker informing it that it wishes to stop receiving event notifications.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.4.2",
      "label" : "1.1.4.2 Notification Broker",
      "description" : "<b>Definition:</b>Notification Broker service delivers notifications of events to end-users both within nodes and across node boundaries.<br><b>Description:</b>Notification Broker service provides the ability to manage receipt and delivery of event notifications that may need to proxied, routed, and brokered until the receiver accepts the event notification. It provides a mechanism to manage event notifications within a group to provide unit of order delivery (i.e. you want the stand-down message to come in the correct order compared to the attack message).    Some event notifications may not be able to be delivered timely due to network issues, offline, errors etc.  This component queues them up and manages successful delivery.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.4.1",
      "label" : "1.1.4.1 Notification Producer",
      "description" : "<b>Definition:</b>Notification Producer service serves as the generic interface for publishing an event related to a topic.<br><b>Description:</b>Notification Producer service sends notifications that an event occurred with data about that event to Notification Consumer services.    In doing so, the producer does not know about the consumers in advance and the set of consumers may change over time.    Related operations include (but may not be limited to) notification production (sending); pull pointing; creating a pullpoint; subscription management, providing a subscription/notify interface (for notification push); and sending event notifications to a Notification Broker service in order to publish notifications on given topics.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.4",
      "label" : "1.1.4 Event Notification",
      "description" : "<b>Definition:</b>Eventing capabilities provide a federated, distributed, and fault-tolerant enterprise message bus delivering performance, scalable and interoperable asynchronous event notifications Quality of Service (QoS), guaranteed delivery to disconnected users or applications, and decoupling of information among producers and consumers.<br><b>Description:</b>The Eventing line provides a federated, distributed, and fault-tolerant enterprise message bus. It delivers high performance, scalable and interoperable asynchronous event notifications to both applications and end-users. This service supports the configuration of Quality of Service (QoS) for a published message including the priority, precedence, and time-to-live (TTL); provides guaranteed delivery to disconnected users or applications; and utilizes multiple message brokers, potentially within different administrative domains to support the distributed, federated nature of the GIG. Messaging services promote decoupling of information among producers and consumers.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3.3",
      "label" : "1.1.3.3 Site Monitoring",
      "description" : "<b>Definition:</b>Site Monitoring provides the ability to measure and report the health and performance of websites and servers.<br><b>Description:</b>Site Monitoring measures and reports the health and performance of web sites and servers through the collection of enterprise-defined metrics.  It provides a vendor, platform, network, and protocol neutral framework that shares a common messaging protocol to unify infrastructure status reporting.  In the event of a system fault or failure, Site Monitoring will use the Notification service to send event notifications to the appropriate subscribers.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3.2",
      "label" : "1.1.3.2 Fault Isolation",
      "description" : "<b>Definition:</b>Fault Isolation enables pinpointing the type of fault and its location.<br><b>Description:</b>Fault Isolation works in cooperation with Fault Detection, to identify information about a fault when one has been detected.  This information may include which system/service has failed, the operations or jobs that were being processed at time of failure, and information about currently logged-on or authenticated users, as well as standard date/time and system location information.  This information is provided to the Site Monitoring service to provide appropriate notifications.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3.1",
      "label" : "1.1.3.1 Fault Detection",
      "description" : "<b>Definition:</b>Fault Detection enables system monitoring and identification of a fault occurrence.<br><b>Description:</b>Fault Detection may be implemented as a centralized audit log monitoring and alerting system to which all computing assets push their logging data, as a heartbeat type service that periodically checks to make sure that other services are up and running, or as a local agent deployed on a system that monitors for faults and sends those notifications to a centralized detection location.  When a fault is detected, the Fault Detection service will interact with the Fault Isolation service to derive additional information.  This additional information is used by the Site Monitoring service to send appropriate notifications.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3",
      "label" : "1.1.3 Enterprise Monitoring",
      "description" : "<b>Definition:</b>The Enterprise Monitoring family monitors services and websites across the  enterprise.<br><b>Description:</b>Enterprise Monitoring services measure and report the health and performance of web services as well as DI2E servers and websites.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.2.2",
      "label" : "1.1.2.2 Time Synchronization",
      "description" : "<b>Definition:</b>The Time Synchronization service establishes a consistent time to be used by all DI2E IT resources.<br><b>Description:</b>Time Synchronization provides a consistent time reference for all DI2E devices, thus supporting secure log ins, service interoperability, trustworthy database transactions, and accurate monitoring activities.  The goal is to use well established and ubiquitous method of time synchronization that responds to time requests from any internet client and is consistent among all DI2E nodes & community related nodes.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.2.1",
      "label" : "1.1.2.1 Domain Name System (DNS)",
      "description" : "<b>Definition:</b>This Domain Name System (DNS) locates and translates Internet domain names into Internet Protocol (IP) addresses using a hierarchy of authority.<br><b>Description:</b>Domain names are meaningful identification labels for Internet addresses. The Domain Name System (DNS) translates the more easily used by humans domain names (example: www.army.mil)  into numerical Internet Protocol formats (example: 192.52.180.110) used by networking equipment for locating and addressing devices. <br><br>DNS makes it possible to assign domain names to groups of Internet users in a meaningful way, independent of each user's physical location. Because of this, World Wide Web (WWW) hyperlinks and Internet contact information can remain consistent when internet routing arrangements change or a participant uses a mobile device.     Consistently applied DNS naming conventions across the enterprise further promote enterprise interoperability because users & systems can more easily and dependably find and understand the purpose of web based resources.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.2",
      "label" : "1.1.2 Translation and Synchronization",
      "description" : "<b>Definition:</b>Translation and Synchronization services promote standardized references of time and location across the  Enterprise.<br><b>Description:</b>Translation & Synchronization enables the management of network operations by resolving service endpoint Universal Resource Locators (URLs) and maintaining synchronized time on all connected  nodes.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1.2",
      "label" : "1.1.1.2 Metrics Reporting",
      "description" : "<b>Definition:</b>Metrics Reporting service provides an interface for the retrieval of managed resource quality of service metrics.<br><b>Description:</b>Metrics Reporting is used by Knowledge Managers to retrieve metrics collected by the Metrics Measurements Collection service.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1.1",
      "label" : "1.1.1.1 Metrics Measurements Collection",
      "description" : "<b>Definition:</b>Metrics Measurements Collection gathers and records service performance measurements.<br><b>Description:</b>Metrics Measurements Collection service monitors invoked services and collects related performance measurements which are then used to calculate quality of service and other performance metrics.  Not directly accessed by users, this service gathers and records its information into the service performance database.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1",
      "label" : "1.1.1 Metrics Management",
      "description" : "<b>Definition:</b>Metrics Management enables operational analysis and management for the quality of service provided by DI2E services.<br><b>Description:</b>Metrics Management develops & collects metrics, monitors events, and evaluates performance.   This requires a standard set of metrics, reported events, and interface for querying and reporting collected metrics. <br><br>Service providers must analyze information to detect and report service degradation so corrective action can be taken when needed.   Real-time monitoring avoids Service Level Agreement (SLA) violations and minimizes service down time.   <br><br>The Metrics Management family supports two service interface models: Request/Response Interface – to support ad-hoc querying of metrics and event data; and the Publish/Subscribe Interface – to support a publish/subscribe interface.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.1",
      "label" : "1.1 Enterprise Management",
      "description" : "<b>Definition:</b>Enterprise Management services enable consistent service level agreement and quality of service reporting; service/site monitoring; consistent use of domain names; and time synchronization.<br><b>Description:</b>Enterprise Management services enable consistent management of service performance, access, and time.  In particular:<br><br>   • Service Level Agreement/Quality of Service (SLA/QoS) Reporting services enable operational analysis and management of DI2E services<br>   • Enterprise Administration services monitor services, services, and websites <br>   • Translation and Synchronization services promote standardized references of time and location",
      "fullTextAvailable" : false
    }, {
      "code" : "2",
      "label" : "2 Common Services",
      "description" : "<b>Definition:</b>The services that provide a  function that is common across many mission capabilities.<br><b>Description:</b>Layer",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.10",
      "label" : "2.6.5.10 Change Agent",
      "description" : "<b>Definition:</b>Change Agent provides for the management of information about the individuals that update and modify managed records.<br><br><b>Description:</b>The Change Agent service is responsible for managing the information regarding organizational structure used for the purpose of establishing provenance within the records management service by identify the parties that are involved with the maintenance and manipulation of the Managed Records and the other entities within the records management service.  The Change Agent service has operations that provide access to a hierarchy of change agents and the roles that have been assigned to them.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.9",
      "label" : "2.6.5.9 Record Attribute Profiles",
      "description" : "<b>Definition:</b>Record Attribute Profiles maintain the instance data for profiles that have been added to the records management services.<br><br><b>Description:</b>The Record Attribute Profiles service maintains the instance data for profiles that have been added to the records management family. This service is not responsible for entering the profiles themselves.  The service’s responsibilities are limited to providing information contained in existing profiles and for storing instance data for attributable objects. Attribute profiles enable the dynamic addition of metadata to Managed Records based on profiles set up in the system. Objects are registered with the Record Attribute Profiles service so that attributes can be set for that particular object based on a profile.  The Record Attribute Profiles service contains operations setting and retrieving the attribute values.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.8",
      "label" : "2.6.5.8 Record Authentications",
      "description" : "<b>Definition:</b>Record Authentications provides the management of authentication methods and the results of execution of those methods on managed records.<br><br><b>Description:</b>The Record Authentications service provides the ability to manage authentication methods, execute those authentication methods on managed records and to maintain the results of those authentications to enable the assessment of authenticity of a particular record.  The Record Authentications service depends on the Managed Records service to get information relevant to the record to be authenticated. It also depends on the Record Documents service to get the actual contents of the record.<br><br>The Record Authentications service has operations that return the Authentication Result for the Managed Record, calculates the Authentication Result for the Managed Record and compares it to the Authentication Base, allows changing the Authentication Method of a Managed Record from an old Authentication Method to a new Authentication Method which causes the generation of a new Authentication Base using the new Authentication Method.  In addition, this service allows new Authentication Methods to be put into action as well as marking the retirement date of an Authentication Method.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.7",
      "label" : "2.6.5.7 Record Query",
      "description" : "<b>Definition:</b>Record Query accepts a request and returns records identifiers that match the query.<br><br><b>Description:</b>The Record Query service provides the ability query Managed Records based on the Record Management System data model elements and their relationships. The Record Queries service consists of a single query operation. The query operation takes a string as an input parameter and returns the results as a string. The input parameter qualifies the requested elements (likely provided in XQuery/Xpath string format) to the Records Management Environment.  The operation returns a string that contains the elements that match the request in the form of an XML string formatted according to the Records Management System XML Schema Definition.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.6",
      "label" : "2.6.5.6 Managed Records",
      "description" : "<b>Definition:</b>Managed Records provides the management  of the information related to records that are being controlled by the records management system.<br><b>Description:</b>The Managed Records service manages a wide variety of information related to Managed Records including the associations to other Managed Records and Case File Definitions.  Managed Records are the records generated during the course of business that an organization is interested in tracking and includes case files.  The Managed Records service depends on the Record Documents service to manage the actual documents that make up the contents of the record. The Managed Records service maintains the additional metadata about the document required for records management. The Managed Records service also depends on the Change Agent service to maintain information regarding organizations that play various roles with respect to the managed record such as record keeper or record creator.<br><br>The Managed Records service has operations to capture Managed Records, retrieve or destroy the Managed Records and any of the metadata associated with the Manages Record.  This service also allows the Managing Record to be associated with other Managed Records and Case Files.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.5",
      "label" : "2.6.5.5 Record Documents",
      "description" : "<b>Definition:</b>Record Documents provides the management of the documents which comprise the content of the managed records.<br><b>Description:</b>The Record Documents service manages the documents which comprise the content of Managed Records.  A Document managed by the records management family is interpreted simply as \"bit strings\" without presumption of form or purpose; in other words, the document contents per se are of no concern to the records management system, although a document type is maintained for use by the consumers of the record management system. Documents can be used in many Managed Records because a single Document can represent evidence of multiple business activities/purposes. When final disposition of a Managed Record in which a document participates occurs (transfer or destroy), the Managed Record is destroyed.   The Document itself is destroyed only when the Managed Record in final disposition is the only one that still refers to the document.<br><br>The Record Documents service has operations to save documents into the records management system, retrieve documents from the records management system and destroy document.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.4",
      "label" : "2.6.5.4 Record Dispositions",
      "description" : "<b>Definition:</b>Record Dispositions provide for the management of information related to lifecycle of the managed records.<br><br><b>Description:</b>The Record Dispositions service includes management of a wide variety of information related to disposition instructions, dispositions plans, and suspensions. When a record is assigned to a record category, a disposition plan is generated based on the disposition instruction. If record category is changed, the disposition plan must be deleted unless the managed record category points to the same disposition instruction.<br><br>The Record Dispositions service has operations to add and remove disposition instructions to disposition plans, associate disposition plans to sets of managed records, return the managed records associated with a disposition plan, return a disposition plan for a specific managed record, and manage suspense events.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.3",
      "label" : "2.6.5.3 Record Categories",
      "description" : "<b>Definition:</b>Record Categories provide the assignment of records to categories defined in a category schema.<br><br><b>Description:</b>The Record Categories service manages the association of the Managed Records with some business activity that requires that records of it be kept.  This is accomplished by associating Managed Records to business identified categories. The Record Categories service provides access to record schedules captured a Categorization Schema. The actual setup of the schemas is outside the scope of this service. The functionality provided herein allows for the use of the schema and the application of its categories to Managed Records.<br><br>The Record Categories service provides the ability to assign Managed Records to Record Categories either individually or as a set.  There are operations to add and remove record Categories, manage the association of Managed Records to Record Categories, return information on the Category Schemas and return the Disposition Instruction for specified Record Categories.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.2",
      "label" : "2.6.5.2 Record Authorities",
      "description" : "<b>Definition:</b>Record Authorities provide the management of the Authorization instances on particular managed records.<br><b>Description:</b>The Record Authorities service to manage information about the organizations that have authority for managed records within records management system by maintaining an association between an Authority for a particular records management element and the Change Agent that has that responsibility.  The Record Authorities service manages information regarding the parties with legal authority over the managed records and their annotations using the Change Agent service to manage the data about the Authority. The record Categories service maintains a reference to information in the Authorities service related to the authority for a categorization schema.<br>",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.1",
      "label" : "2.6.5.1 Record Annotations",
      "description" : "<b>Definition:</b>This service provides the management of the markup and highlight instances that are associated with particular managed records<br><b>Description:</b>The Record Annotations service provides markings on records that help to differentiate them from other records in the same category or across categories. These are typically used to support business needs for special handling or management of the record.  The Record Annotations service has operations that provide the ability to add an annotation to a managed record, retrieve a list of the annotations for a specified managed record and the ability to remove one or all annotations from a particular managed record.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5",
      "label" : "2.6.5 Records Management",
      "description" : "<b>Definition:</b>The Records Management family of capabilities provides systematic control of the creation, receipt, maintenance, use and disposition of records.<br><b>Description:</b>The Records Management family of capabilities provides systematic control of the creation, receipt, maintenance, use and disposition of records, including the processes for capturing and maintaining evidence of and information about business activities and transactions in the form of records. <br><br>   • Record Annotations - which provides the management of the markup and highlight instances that are associated with particular managed records<br>   • Record Authorities - which provides the management of the Authorization instances on particular managed records<br>   • Record Categories - which provides assignment of records to categories defined in a category schema.<br>   • Record Dispositions- which provides management of information related to lifecycle of the managed records.<br>   • Record Documents- which provides management of the documents which comprise the content of the managed records<br>   • Managed Records - which provides management  of the information related to records that are being controlled by the records management system<br>   • Record Query - which accepts a request and returns records identifiers that match the query.<br>   • Record Authentications- which provides the management of authentication methods and the results of execution of those methods on managed records<br>   • Record Attribute Profiles- which maintains the instance data for profiles that have been added to the records management services.<br>   • Change Agent- which provides management of information about the individuals that update and modify managed records",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.4.3",
      "label" : "2.6.4.3 Data Quality Measurement",
      "description" : "<b>Definition:</b>Data Quality Measurement performs data quality measurement and assessment computations that populate the operational quality data portion of the data quality metrics database<br><b>Description:</b>The Data Quality Measurement service provides summary counts of the number of business rule violations and calculates the levels of quality of different data quality dimensions.   This is accomplished by comparing Data Quality measurements to established Data Quality standard requirement or desired threshold level for the metric such as: Simple Ratio, Weighted Average, Minimum and Maximum operations. The results of these measurement comparisons are then processed through one or more assessment schemes to identify the level of acceptance (or not) of the quality of the data.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.4.2",
      "label" : "2.6.4.2 Data Quality Extraction",
      "description" : "<b>Definition:</b>Data Quality Extraction processes commands to retrieve specified portions of the data quality metrics database<br><b>Description:</b>This service provides access the data quality metrics database to be used by applications the will process the data quality information for presentation and analysis on the quality of the data within the system.  The output of this service will be an XML file that follows the data quality XML Schema.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.4.1",
      "label" : "2.6.4.1 Data Quality Definition",
      "description" : "<b>Definition:</b>Data Quality Definition populates the business tables in the definitional data portion of the data quality metrics database<br><b>Description:</b>The Data Quality Definition service is the primary mechanism whereby various entries in the different definitional tables provided by the user. This includes Information Product Types, Data Item Types, Data Object Types, Business Rules, Data Quality metrics, and Data Quality Object Metrics. The Data Quality Definition service provides the storage of the Data Quality metadata in a repository.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.4",
      "label" : "2.6.4 Data Quality",
      "description" : "<b>Definition:</b>The Data Quality family of services provides methods and metrics by which the value of the data to the enterprise can be measured to ensure data is accurate, timely, relevant, complete, understood, trusted and satisfies intended use requirements.<br><b>Description:</b>The Data Quality family of services provides methods and metrics by which the value of the data to the enterprise can be measured to ensure data is accurate, timely, relevant, complete, understood, trusted and satisfies intended use requirements.<br><br>   • Data Quality Definition - which populates the business tables in the definitional data portion of the data quality metrics database<br>   • Data Quality Extraction - which processes commands to retrieve specified portions of the data quality metrics database<br>   • Data Quality Measurement  - which performs data quality measurement and assessment computations that populate the operational quality data portion of the data quality metrics database",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.8",
      "label" : "2.6.1.8 Content Policy",
      "description" : "<b>Definition:</b>Content Policy is used to apply or remove a policy object to an object that is under the control of a policy<br><b>Description:</b>The Content Policy service is used to apply or remove a policy object to a content object. A policy object represents an administrative policy that can be enforced by a repository, such as a retention management policy. Each policy object holds the text of an administrative policy as a repository-specific string, which may be used to support policies of various kinds. A repository may create subtypes of this base type to support different kinds of administrative policies more specifically.  Policy objects are created by the Object Processing service and manipulated by this Content Policy service.  The service has operations to apply a specified policy to an content object, get the list of policies currently applied to a specified content object and remove a specified policy from a specified content object.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.7",
      "label" : "2.6.1.7 Object Relationship",
      "description" : "<b>Definition:</b>Object Relationship is used to retrieve the dependent relationship objects associated with an independent object<br><b>Description:</b>The Object Relationship service is used to retrieve the dependent relationship objects associated with an independent object that were created with the Object Processing service.  The service gets all or a subset of relationships associated with an independent object.  In addition the service allows the consumer to specify whether the service returns relationships where the specified content object is the source of the relationship, the target of the relationship, or both.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.6",
      "label" : "2.6.1.6 Content Versioning",
      "description" : "<b>Definition:</b>Content Versioning is used to navigate or update a document version series<br><b>Description:</b>The Content Versioning service is used to navigate or update a document version series. The Content Versioning service provides a check-out operation that gets a the latest document object in the version series, allows a private working copy of the document to be created and provides a  checks-in operation that checks-in a private working copy document as the latest in the version series. In addition, the Content Versioning service provides an operation that reverses the effect of a check-out by removing the private working copy of the checked-out document, allowing other documents in the version series to be checked out again.  Finally there is a service to retrieve a the list of all Document Objects in the specified Version Series.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.5",
      "label" : "2.6.1.5 Managed Content Discovery",
      "description" : "<b>Definition:</b>Managed Content Discovery executes a query statement against the contents of a content repository<br><b>Description:</b>The Managed Content Discovery service (query) is used to search for query-able objects within the content repository.  The Managed Content Discovery service provides a type-based query service for discovering objects that match specified criteria,  The semantics of this query language is defined by applicable query language standards in conjunction with the model mapping defined by the relational view of the content management object repository.  This service also provides and operation to get a list of content changes to be used by search crawlers or other applications that need to efficiently understand what has changed in the repository.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.4",
      "label" : "2.6.1.4 Object Folders",
      "description" : "<b>Definition:</b>The Object Folders service is used to file and un-file objects into or from folders<br><b>Description:</b>The Object Folders service supports the multi-filing and unfiling services. Multi-filing allows the same non folder content object to be to be filed in more than one folder.  The Object Folders service allows existing fileable non-folder objects to be added to a folder and allows existing fileable non-folder object to be removed from a folder.  This service is NOT used to create or delete content objects in the repository.  The content objects must have previously been created by the Object Processing service to be filed and a content object that is removed from a folder remains persistent in the content repository.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.3",
      "label" : "2.6.1.3 Object Processing",
      "description" : "<b>Definition:</b>Object Processing provides ID-based Create, Retrieve, Update, Delete (CRUD), operations on objects in a repository.<br><b>Description:</b>The Object Processing service provides Create, Retrieve, Update, Delete (CRUD) manipulation on the objects in a content repository. Specifically, the Object Processing service allows for the creation of a document object of the specified type in the specified location within the tree of folders and creates relationship objects of the specified types that are used to specify how content objects can be related. A relationship object is an explicit, binary, directional, non-invasive, and typed relationship between a specified source object and a specified target object.  Information contained in the properties of the content objects can be created, retrieved and updated.  The content objects, such as documents, themselves can be  created, retrieved deleted also content objects can be moved from one folder to another.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.2",
      "label" : "2.6.1.2 Content Navigation",
      "description" : "<b>Definition:</b>Content Navigation is used to traverse the folder hierarchy in a repository, and to locate Documents that are checked out<br><b>Description:</b>The Content Navigation service provides operations to retrieve objects from a tree of folder objects that have the following constraints:<br>• Every folder object, except for one which is called the root folder, has one and only one parent folder. The Root Folder does not have a parent.<br>• A folder object cannot have itself as one of its descendant objects.<br>• A child object that is a folder object can itself be the parent object of other file-able objects such as other folders and documents.<br>• The folder objects in a content repository form a strict hierarchy, with the Root Folder being the root of the hierarchy.<br>The Content Navigation service provides the retrieval of a list of child objects that are contained in a specified folder, a set of descendant objects contained in a specified folder or any of its child-folders.  In addition, the Content Navigation service provides the retrieval of the parent folder object for a specified folder object a list of documents that are checked out to which the user has access.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.1",
      "label" : "2.6.1.1 Content Repository",
      "description" : "<b>Definition:</b>Content Repository is used to discover information about repositories and the object-types defined for the repository.<br><b>Description:</b>The Content Repositories service has operations that return a list of existing content repositories available from the Content Management service endpoint; information about the content repository and its Access Control information.  The Content Repositories service also provides operations that return the definition of a specified content object type as well as list of content object types that are children or decedents of a specified content object type.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1",
      "label" : "2.6.1 Content Management",
      "description" : "<b>Definition:</b>Content Management provides services that organize and facilitate the  collaboration and publication of documents and other enterprise content.<br><b>Description:</b>Content Management services organize and facilitate the  collaboration and publication of documents and other enterprise content.<br><br>Specific services include: <br><br>    • Content Repository - which is used to discover information about repositories and the object-types defined for the repository<br>    • Content Navigation  - which traverses repository folder hierarchies and locates checked out documents<br>    • Object Processing  - which provides ID-based Create, Retrieve, Update, Delete (CRUD), operations on repository objects <br>    • Object Folders  - which files and un-files folder objects <br>    • Managed Content Discovery  - which executes a query statement against the content repository<br>    • Content Versioning   - which navigates or updates a document version series<br>    • Object Relationship   - which retrieves the dependent relationship objects associated with an independent object<br>    • Content Policy  - which applies or removes policy objects <br>    • Content Access Control List This service processes the access control list (ACL) associated with repository objects",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6",
      "label" : "2.6 Data Handling",
      "description" : "<b>Definition:</b>The Data Handling line of capabilities includes the data management and processing functions used to maintain and manage DI2E data stores.<br><b>Description:</b>The Data Handling line of capabilities includes the data management and processing functions used to maintain and manage DI2E data stores.  Included families of capabilities provide content management, data quality control, and records management.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1.4",
      "label" : "2.5.1.4 Data Commenting",
      "description" : "<b>Definition:</b>Data Commenting services allow the users to enrich the existing data by adding comments on the data.<br><b>Description:</b>Data Commenting enables users to comment and collaborate on existing data within the system.  The comment service allows users to enrich the data set without altering the original product.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1.3",
      "label" : "2.5.1.3 Categorize Content",
      "description" : "<b>Definition:</b>Categorize Content analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy.<br><b>Description:</b>Categorize Content analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy. This is done to allow the user to browse for relevant content vs. key word search.<br><br>For example, a piece of content may be related to the USS Roosevelt. The if proper like categorized the user would be able to discover this content via traversing a taxonomy such as: Ships -> Military Ships -> US -> Air",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1.2",
      "label" : "2.5.1.2 Entity Association",
      "description" : "<b>Definition:</b>Entity Association establishes and records relationships between data objects for use in advanced analytical processing and reporting.<br><b>Description:</b>Entity Association discovers and records in persistent data stores various analytic relationships that might exist among disparate database objects.   These stored relationships can then be used by Analytic Processing services to more easily import, analyze, and report various associations, trends, groupings, or status that might not be found using more traditional Data Content Discovery services .       <br><br>Examples of relationships might include (but not necessarily limited to) spatial, temporal, event based, or data content relationships.   Complex combinations of various relationships are also possible.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1.1",
      "label" : "2.5.1.1 Entity Extraction",
      "description" : "<b>Definition:</b>Entity Extraction extracts specific data from unstructured text.<br><b>Description:</b>Entity extraction scans text for semantic meaning and identifies key metadata and/or concepts in the document based on a semantic understanding of the language. It can identify things such as people, places, events, objects, etc.    It may also extract relationships such as “Muhammad Omani met has connections with suspected Taliban fighters”.    Information in the document may be tagged using standards such as Resource Description Framework (RDF) or Web Ontology Language (OWL).",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1",
      "label" : "2.5.1 Data Enrichment",
      "description" : "<b>Definition:</b>The Data Enrichment family of capabilities provide analysis and enhancement of sets of data elements.<br><b>Description:</b>The Data Enrichment family of capabilities provide analysis and enhancement of sets of data elements by providing semantics and extracting non-obvious information from datasets.  Specific capabilities include:<br><br>• Entity Extraction - which extracts specific data from text documents.  <br>• Entity Association - which establishes and records relationships between data objects for use in advanced analytical processing and reporting.<br>• Categorize Content - which analyzes the text and meta data for a piece of content in order to assign that document into the proper  place in a predefined taxonomy or hierarchy. <br>• Chat Monitor - which provides alerts and/or query of multiple chat rooms through detection of key words or other user specified events.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5",
      "label" : "2.5 Data Analytics",
      "description" : "<b>Definition:</b>Data Analytics provide advanced analytics by finding non-intuitive or non-trivial relationships within and among DI2E data holdings.<br><b>Description:</b>Data Analytics provide advanced analytics by finding non-intuitive or non-trivial relationships within and among DI2E data holdings.   In particular several Data Enrichment services providing entity extraction and association, content categorization, and monitoring of chat room content.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.6",
      "label" : "2.4.1.6 Data De-Duplication",
      "description" : "<b>Definition:</b>Data De-Duplication provides a data compression technique for eliminating coarse-grained redundant data.<br><b>Description:</b>Data De-Duplication provides a data compression technique for eliminating coarse-grained redundant data. In the de-duplication process, duplicate data is deleted, leaving only one copy of the data to be stored, along with references to the unique copy of data.  The focus of this service is on file or record level data and not on the data storage block level of data.  Although and data de-duplication method does save storage space, the emphasis here is providing analyses to ensure that a copy of an intelligence report, for instance, is the single authoritative copy in the system.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.5",
      "label" : "2.4.1.5 Image Transformation",
      "description" : "<b>Definition:</b>Image Transformation converts one image format to another.<br><b>Description:</b>The Image Format Conversion service converts one image format to another, typically in order to ensure that image data is in a format that can be ingested by specific applications or services.  There are many hundreds of different image formats used. Some of these are in broad commercial or government use. Others may be restricted to highly specialized purposes.  <br><br>Included in the list of data products that may require image format conversion are many types of products that include 2-dimensional data array structures as part of their formats. Examples showing the diversity of image products are  raster formatted Geographic Information System (GIS) products, multiband images such as hyperspectral or multispectral imagery, digital handheld camera photos, scanned documents, and faxes.  In addition to the diversity of products and associated applications, image formats also often include image compression encodings that result in  specified degrees of reduction in image storage size.  Image encryption is also sometimes part of an image format.<br><br>An image format conversion service ingests an image in one format and outputs the image in a second format.  Besides the image input, additional inputs include the specification of the input format (with the default being that the input format is autodetected by the service) and the target destination image format.   <br><br>This service differs from Schema Transformation (in Data Mediation Line) in that Image Format conversion translate image file formats (example .jpeg formatted image to a .gif formatted image) whereas Schema Transformation converts data field layouts (schemas) from one data field arrangement to another (example: xsd schema to fields in a relational database).",
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.4",
      "label" : "2.4.1.4 Schema Transformation",
      "description" : "<b>Definition:</b>Schema Transformation transforms data from adhering to one schema to adhering to another schema.<br><b>Description:</b>Schema Transformation converts data organized in an original schema to a target schema.  The term schema is used here to mean any way in which data might be organized, this may or may not be xsd or the schema of a relational database.  This service transforms data from adhering to one schema to adhering to another schema.  For example, converts from ADatP-3 to USMTF or convert XML data between different XML schemas.  Together, Format Validation and Format Transformation complete the process of ensuring that a service is provided data organized according to the needs of that service.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.3",
      "label" : "2.4.1.3 Data Transformation",
      "description" : "<b>Definition:</b>Data Transformation converts a data value form one format to another. For example convert Latitude from alphanumeric DDDMMSS[N/S] to numeric [+/-] seconds from equator, or feet to meters.<br><b>Description:</b>Data Transformation converts a data value form one format to another. For example convert Latitude from alphanumeric DDDMMSS[N/S] to numeric [+/-] seconds from equator, or feet to meters.  While the Data Validation service checks for correctness against a defined set of rules, it is the Data Transformation service that will change data values based on a set of defined rules.  Together, Data Validation and Data Transformation complete the process of ensuring that a service operates on clean, correct and useful data.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.2",
      "label" : "2.4.1.2 Data Validation",
      "description" : "<b>Definition:</b>Data Validation validates that data values are correct based on rules defined for the data. For example, may validate Latitude as conforming to a DDDMMSS[N/S] format.<br><b>Description:</b>Data Validation uses routines, often called \"validation rules\" or \"check routines\", that check for correctness, meaningfulness, and security of data that are input to the system. The rules may be implemented through the automated facilities of a data dictionary, or by the inclusion of explicit application program validation logic. For business applications, data validation can be defined through declarative data integrity rules, or procedure-based business rules. Data that does not conform to these rules will negatively affect business process execution. Therefore, data validation should start with business process definition and set of business rules within this process. Rules can be collected through the requirements capture exercise. The simplest data validation verifies that the characters provided come from a valid set. For example, that data stored as YYYYMMDDHHMM have exactly 12 digits and  the MM is between 01 and 12 inclusive. A more sophisticated data validation routine would check to see the user had entered a valid country code, i.e., that the number of digits entered matched the convention for the country or area specified. Incorrect data validation can lead to data corruption or a security vulnerability. Data validation checks that data are valid, sensible, reasonable, and secure before they are processed.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.1",
      "label" : "2.4.1.1 Schema Validation",
      "description" : "<b>Definition:</b>Schema Validation validates whether or not data adheres to an identified schema.<br><b>Description:</b>Schema Validation validates that a collection of data (e.g. USMFT message, XML document, etc.) conforms to specified schema. Schema is used here to mean any way in which data might be organized, which may or may not be xsd or the schema of a relational database.  This service validates whether or not data adheres to an identified schema.  This service will take a schema plus a collection of data or instance document and list any errors found in validating the document against the defined schema.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1",
      "label" : "2.4.1 Data Preparation",
      "description" : "<b>Definition:</b>Data Preparation includes services that confirm and applicably convert data.<br><b>Description:</b>Data Preparation services verify that incoming data records follow specified formats and schema and, when needed, output these records using specified formats and schema for usable ingest by consumer services.  <br><br>Services in this family are often used in combination since format/schema errors found during validation often necessitate subsequent transformation.    <br><br>(note: schema is used here to mean however the data might be organized - whether through xsd specifications, relational database structure, or other means.)",
      "fullTextAvailable" : false
    }, {
      "code" : "2.4",
      "label" : "2.4 Data Mediation",
      "description" : "<b>Definition:</b>Data Mediation includes services that enable the dynamic resolution of representational differences among disparate data on the behalf of a service consumer.<br><b>Description:</b>Data Mediation services enable the dynamic resolution of representational differences among disparate data on the behalf of a service consumer. There will be multiple data formats within the enterprise representing all types of data. In order to support multiple data consumers, the data must be mediated into the format and schema recognizable by the data consumer.  Schema is used here to mean any way in which data might be organized, which may or may not be xsd or the schema of a relational database.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.2.3",
      "label" : "2.5.2.3 Audio Monitor",
      "description" : "<b>Definition:</b>Audio Monitoring enables tracking, recording, scanning, filtering, blocking, reporting, and logging of specific information in audio feeds.<br><b>Description:</b>TBD",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.2.2",
      "label" : "2.5.2.2 Video Monitor",
      "description" : "<b>Definition:</b>Video Monitoring enables tracking, recording, scanning, filtering, blocking, reporting, and logging of specific information in video feeds<br><b>Description:</b>TBD",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.2.1",
      "label" : "2.5.2.1 Chat Monitor",
      "description" : "<b>Definition:</b>Chat Monitor provides alerts and/or query of multiple chat rooms through detection of key words or other user specified events.<br><b>Description:</b>Chat Monitor provides real-time chat monitoring along with chat log processing to enable additional awareness of current and historic chat content.<br><br>Corresponding alerts and filtering may be based on key words, pattern matching, thesaurus, or gazetteer information.   A direct querying functionality is also provided.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.2",
      "label" : "2.5.2 Media Monitoring",
      "description" : "<b>Definition:</b>Media Monitoring services are services that can track or record data activity that may be of interest to a user or system.<br><b>Description:</b>Media Monitoring services are services that can track or record data activity that may be of interest to a user or system. Monitoring services may look for levels of activity (e.g., data transfer rates), particular values (e.g., keywords), or other values of interest.<br><br>Alerting services, automated maintenance services, and similar capabilities can be constructed on top of monitoring services to relieve end users of the task of manually tracking a multitude of data sources looking for items of interest. For example, an audio monitoring service might look at all audio feeds waiting for a audio signal that matches the audio signature of the name of a person of interest and pass that to an alerting service.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.7",
      "label" : "2.3.1.7 Query Results Management",
      "description" : "<b>Definition:</b>Query Results Management allows editing (adding, removing, modification) of query results as well as sharing and distributing of query results among  users<br><b>Description:</b>Query Results Management enables further processing of returned results to customize for further analysis or sharing of search results with others.<br><br>Examples of what Query Results Management might providing include enabling adding a new record manually from an artifact the search did not find, editing the description of the search hit to make it more clear for others, or removing less relevant hits in order to provide focus to those hits that remain. <br><br>In some advanced applications, query results management might also enable sending out final edited results, automatically or at the user’s request, to other identified users or applications.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.6",
      "label" : "2.3.1.6 Query Management",
      "description" : "<b>Definition:</b>Query Management allows consumers to build, store, retrieve, update, and delete pre-established search and query criteria.<br><b>Description:</b>Many  analysts periodically or repetitively execute the same, or  highly similar, search queries.   In more advanced applications, the structure of the queries can be complex and require considerable time and/or skill to establish.    The query management service helps support this operational requirement by providing a tool that enables  analysts to establish, reuse, and share queries for later or other analyst’s use.<br><br>Query Management services accomplish this by supporting the maintenance of query repositories that offer queries for reuse, often with minimal or no additional editing or query parameter analysis.    The SvcV-4 is not prescriptive as the organization relationships of  query repositories (by user, by organization, by role, etc.) but does point to the specifications listed in the Service Portfolio Management Tool (SPMT) for guidance (and once CDP approved, conformance requirement) in the applications or services that present query repositories.    In doing so, it encourages search criteria management services to operate in a manner that promotes establishment and provisioning of stored queries for sharing and use by disparate systems and users across the  Enterprise.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.5",
      "label" : "2.3.1.5 Describe Content",
      "description" : "<b>Definition:</b>The Describe Content service enables content repositories to publish information describing their content collections and content resources to the enterprise.  It also provides interested parties with a description of the resource and how it can be accessed or used. <br><br>-- CDR Reference Architecture and Specification Framework<br><br>https://metadata.ces.mil/dse/documents/DoDMWG/2010/04/2010-04-13_CDRIPT.ppt<br><br><b>Description:</b>Describe Content serves as the primary mechanism for content providers to expose information to describe the context, access constraints, and current inventory status of the underlying content resources, and the exposed information will support static and dynamic discovery and accessibility of a content collection. Search and Brokered Search leverage the output of this component to determine whether the content collection may contain content resources that are relevant to the consumer’s query. To support a wide array of use cases, the Describe Component should reflect both the static9 and dynamic10 information about the underlying content collection.<br>--CDR Reference Architecture",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.4",
      "label" : "2.3.1.4 Deliver Content",
      "description" : "<b>Definition:</b>Deliver Content enables content to be sent to a specified destination, which may or may not be the requesting component.<br><b>Description:</b>Deliver Content enables content to be sent to a specified destination, which may or may not be the requesting component. In its simplest form, Deliver Content will take a consumer-supplied payload and send it to another consumer as specified in the delivery property set. For instance, if an analyst discovers a relevant data resource from a Data Discovery feed on her PDA, she might want to access and route that data content to her desktop computer so that she may review it later. The Retrieve Content service facilitates this use case through its use of WS-Addressing, but it requires a companion asynchronous callback interface to ultimately accept the routed data resource. This interface is captured by the Deliver Content  service. Also may include additional processing, such as compression, encryption, or conversion that makes delivery of the payload suitable for its destination and the delivery path to be used.  The terms Deliver and Receive are both used in the Content  Discovery and Retrieval Architecture to describe the service with this service.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.3",
      "label" : "2.3.1.3 Retrieve Content",
      "description" : "<b>Definition:</b>Retrieve Content serves as the primary content access mechanism. It encompasses the service to retrieve identified content and to initiate the delivery of the retrieved content to a designated location.<br><b>Description:</b>The Retrieve Content service serves as the primary content access mechanism. It encompasses the service to retrieve identified content from the content collection in which it is stored and to initiate the delivery of the retrieved content to a designated location. The delivery of the content can be a return directly back to the requester or can use the Deliver Content service to redirect the response and comply with other handling instructions as supplied by the requester. It cannot redirect output to a component other than the requestor.  <br>The Retrieve Content service provides a common interface and behavioral model for Intelligence Community (IC) and Department of Defense (DoD) content collections, enabling content consumers to retrieve content from disparate collections across the IC/DOD enterprise. Specifically, it provides a means to accept a uniform syntax and semantics that can be transformed, as needed, and applied to newly-developed or existing content collections. Thus, it is unambiguously conveying a request for the content without knowing or setting requirements on the implementation of the underlying content collection.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.2",
      "label" : "2.3.1.2 Brokered Search",
      "description" : "<b>Definition:</b>Brokered Search serves as the primary mechanism to 1) facilitate the distribution of queries to applicable/relevant content collections (exposed as Search Components) and 2) process the returned results.<br>--CDR Reference Architecture<br><br><b>Description:</b>The Brokered Search service allows an entity to search multiple, independent resources or data repositories and retrieve a combined list of search results.  Brokered searches may be confined to the organization, or may be federated, allowing an entity to simultaneously search data stores in multiple organizations.  Rather than returning the identified products (which could number in the millions), a brokered search returns metadata about each matching product.  This metadata includes information to assist the entity in choosing products to retrieve  (such as the author, a description, abstract, or summary), as well as information about the resource that holds the product.  The entity then queries the repository directly to retrieve the actual product.<br>http://www.dni.gov/index.php/about/organization/chief-information-officer/cdr-brokered-search.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.1",
      "label" : "2.3.1.1 Content Search",
      "description" : "<b>Definition:</b>Content Search provides a standard interface for discovering information, returning a 'hit list' of items which can then be retrieved.<br><br>Federated Search transforms a search query into useful form(s), broadcasts is to multiple disparate databases, merges the results, and presents results is a succinct, organized format.<br><b>Description:</b>Content Search provides a standard interface for discovering information in unstructured and semi-structured data stores.  This service searches Meta data tagged Imagery of various types and Free Text reports, articles, or documents.   Each item to be searched may have metadata associated with it, conforming to enterprise standards.  <br><br>The interface specifies criteria such as Author=”name” or “Published time later than some date” as ways of identifying items of interest.   Additionally the search of the body and/or meta data tags can be performed using text search strings, such as “(SOA OR Service) NEAR Component”.   The precise query language grammar will be specified in the service specification package and will include the following functionality: Boolean and, Boolean or, Boolean not, groupings, proximity, and wildcards.<br><br>The Federated Search service will simultaneously search multiple specific search resources through a single query request by distributing the search request to participating search engines.   Results are aggregated and results are processed for presentation to the user.<br><br>Operations typically might include: (1) checking an incoming query for appropriate content and form, (2) analyzing which search resources to query for results, (3) transforming the query request to forms appropriate search requests for each search resource to be contacted, (4) executing a search request for each assigned search resource, (5) receiving and responding to the distributed search requests, (6) aggregate the results collected from the various search requests, and (7) preparing an organized representation of results, typically with reduced duplication and possibly some relevancy rankings.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1",
      "label" : "2.3.1 Content Discovery and Retrieval",
      "description" : "<b>Definition:</b>The Content Discovery and Retrieval (CD&R) family processes a user's query to discover information from data assets.<br><b>Description:</b>family",
      "fullTextAvailable" : false
    }, {
      "code" : "2.3",
      "label" : "2.3 Data Discovery",
      "description" : "<b>Definition:</b>The Data Discovery line processes a user's query to discover information.<br><b>Description:</b>Data Discovery processes a user’s query to discover information by:  1) conducting searches that gather information from basic web content as well as structured data sources (general, federated, and semantic search); 2) enabling advanced manipulation and control of the search request and results (search management and enhancement); and 3) enabling enhanced understanding of the people and assets that are available to support  missions and analysis (resource discovery).",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3.2",
      "label" : "2.2.3.2 Common Operational Picture (COP)",
      "description" : "<b>Definition:</b>Common Operational Picture (COP) services allow for generation of a COP and User Defined Operating Picture (UDOP).<br><br><b>Description:</b>The COP provides an overall 'picture' of a geographic domain of interest (typically a theatre engaged in military missions) and is maintained by the commander’s operations staff.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3.1",
      "label" : "2.2.3.1 Analytic Rendering",
      "description" : "<b>Definition:</b>Analytic Rendering services render analytic products.  Includes Histograms, Semantic Network diagrams, Scatter Diagrams, Flow Charts, Relationship Charts.<br><b>Description:</b>Analytic Rendering provides different ways to visually present data, to assist in the understanding and exploitation of that data and the identification of trends or other useful information.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3",
      "label" : "2.2.3 Analytics Visualization",
      "description" : "<b>Definition:</b>Family to visualize results of Analytical Processes.<br><b>Description:</b>family",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.5",
      "label" : "2.2.2.5 Weather Visualization",
      "description" : "<b>Definition:</b>Weather Visualization services receive and display weather related conditions.<br><b>Description:</b>Weather Visualization services receive and display weather related conditions from various weather reporting sources such as the National Weather Association (NWA), U.S. Air Force Weather Agency (AFWA), or Naval Meteorology & Oceanography Command (CNMOC) (list is for example, not comprehensive).   Weather visualization is typically for a specified geographic region; may include display of historic, current, or anticipated future weather conditions; and might be in a variety of formats including multiple spectral options of imagery, map based, audio, or text.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.4",
      "label" : "2.2.2.4 Web Map",
      "description" : "<b>Definition:</b>Web Map provides a simple HTTP interface for requesting geo-registered map images from one or more distributed geospatial databases. A WMS request defines the geographic layer(s) and area of interest to be processed. The response to the request is one or more geo-registered map images (returned as JPEG, PNG, etc.) that can be displayed in a browser application.<br><b>Description:</b>A Web Map Service (WMS) produces maps of spatially referenced data dynamically from geographic information. This International Standard defines a “map” to be a portrayal of geographic information as a digital image file suitable for display on a computer screen. A map is not the data itself. WMS-produced maps are generally rendered in a pictorial format such as PNG, GIF or JPEG, or occasionally as vector-based graphical elements in Scalable Vector Graphics (SVG) or Web Computer Graphics Metafile (WebCGM) formats.  <br>This International Standard defines three operations: one returns service-level metadata; another returns a map whose geographic and dimensional parameters are well-defined; and an optional third operation returns information about particular features shown on a map. Web Map Service operations can be invoked using a standard web browser by submitting requests in the form of Uniform Resource Locators (URLs). The content of such URLs depends on which operation is requested. In particular, when requesting a map the URL indicates what information is to be shown on the map, what portion of the Earth is to be mapped, the desired coordinate reference system, and the output image width and height. When two or more maps are produced with the same geographic parameters and output size, the results can be accurately overlaid to produce a composite map. The use of image formats that support transparent backgrounds (e.g. GIF or PNG) allows underlying maps to be visible. Furthermore, individual maps can be requested from different servers. The Web Map Service thus enables the creation of a network of distributed map servers from which clients can build customized maps.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.3",
      "label" : "2.2.2.3 Web Feature",
      "description" : "<b>Definition:</b>Web Feature provides an interface allowing requests for geographical features across the web using platform-independent calls.  This service allows a client to retrieve and update geospatial data encoded in Geography Markup Language (GML).<br><b>Description:</b>Web feature defines interfaces for data access and manipulation operations on geographic features using HTTP as the distributed computing platform. Via these interfaces, a web user or service can combine, use and manage geodata -- the feature information behind a map image -- from different sources by invoking the following WFS operations on geographic features and elements: create a new feature instance; delete a feature instance; update a feature instance; lock a feature instance; get or query features based on spatial and non-spatial constraints.<br>When products are in compliance with open geospatial web service interface and data encoding specifications, end-users benefit from a larger pool of interoperable web based tools for geodata access and related geoprocessing services.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.2",
      "label" : "2.2.2.2 Web Coverage",
      "description" : "<b>Definition:</b>Web Coverage provides an interface allowing requests for geographical coverages (digital geospatial information representing space-varying phenomena) across the web using platform-independent calls.<br><b>Description:</b>Web Coverage supports electronic retrieval of geospatial data as \"coverages\" – that is, digital geospatial information representing space-varying phenomena. Provides access to potentially detailed and rich sets of geospatial information, in forms that are useful for client-side rendering, multi-valued coverages, and input into scientific models and other clients. Allows clients to choose portions of a server's information holdings based on spatial constraints and other criteria.<br>Provides available data together with their detailed descriptions; defines a rich syntax for requests against these data; and returns data with its original semantics (instead of pictures) which may be interpreted, extrapolated, etc. – and not just portrayed. Returns coverages representing space-varying phenomena that relate a spatio-temporal domain to a (possibly multidimensional) range of properties.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.1",
      "label" : "2.2.2.1 Geographic Information Display",
      "description" : "<b>Definition:</b>Geographic Information Display provides end-users with visualization services for still images, satellite images, 2D, 3D, weather images, and video.<br><b>Description:</b>Geographic Information Display captures, stores, manipulates, analyzes, manages and presents all types of geographically referenced data (2D imagery, 3D imagery, video, satellite, weather, maps, human terrain assessment, human intelligence reports, available signal data, etc.) by merging of cartography, statistical analysis and database technology.<br><br>Common input sources include ortho-rectified imagery taken from both from satellite and aerial sources, maps or graphical products of various forms, and databases containing geospatially related elements.   Within the GIS display, information is typically located spatially (recording longitude, latitude, and elevation), but may also be recorded temporally or with other quantified reference systems such as film frame number, sensor identifier, highway mile marker, surveyor benchmark, building address, or street intersection.<br><br>GIS display data represents real objects with digital representations including raster data (digital image represented by reducible and enlargeable grids) or vectors (which represent features as geometric shapes such as lines, polylines, or polygons).    Real objects are also typically divided into two abstractions: discrete objects (e.g., a house) and continuous fields (such as rainfall amount, or elevations). <br><br>Common GIS display operations include:<br>• contrast enhancement and color rendering <br>• geographic data conversion<br>• map integration and image rectification <br>• complex spatial modeling <br>• geometric network representation<br>• hydrological modeling<br>• Digital Elevation Model (DEM) assessment<br>• data extraction<br>• Geostatistics that analyze point-patterns and provide predictions<br>• Digital cartography<br>• Topological relationships (adjacency, containment, proximity, ..)<br>• Surface interpolation using various mathematical models <br>• Geocoding (interpolating spatial locations from street addresses or any other spatially referenced data using reference themes).<br>• Spatial Extract, Transform, Load (ETL) tools",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2",
      "label" : "2.2.2 Geographic Visualization",
      "description" : "<b>Definition:</b>The Geographic Visualization family gathers and displays inputs from multiple sources containing data linked to geo-coordinated data and displays it in a common geographical representation.<br><b>Description:</b>The Geographic Visualization family gathers and displays inputs from multiple sources containing data linked to geo-coordinated data and displays it in a common geographical representation.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.2",
      "label" : "2.2.1.2 Widget Framework",
      "description" : "<b>Definition:</b>Widget Framework service provides a toolkit that assists in combining two or more widgets (portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page) to form a single application using a much lower level of technical skill and effort than is typically required for application development.<br><b>Description:</b>Widget framework applications provide a layout management and messaging mechanism within a web browser for widgets (widgets are portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page, technically contained in an iFrame).   In doing so, they enable rapid assembly and configuration of rich Web applications composed of multiple, special-purpose widgets.<br><br>Typical widget frameworks support both varied (desktop) and fixed (tabbed, portal, accordion, ...) layouts that can be set by the application’s user.   Components might typically include a server to run supplied applications, HTML and JavaScript files that provide the user interface, preference holding and retrieval mechanisms, and various security mechanisms.   <br><br>Key features to look for in a widget application include (not all may be found in all widget frameworks):<br>• standardized widget event model(s)<br>• standardized Common Data Model (CDM) to promote data sharing between widgets<br>• Scalability features to optimize for performance <br>• data sharing mechanism <br>• data handling and management features <br>• ability to extend basic services through modification of existing widgets, or the development of entirely new ones. <br>• support for bundling and deployment of specific widget sets  <br>• framework extensions for standardized data and eventing models <br>• workflow features that promote workflow analysis and linkage with widget framework architecture. <br>• pre-existing, pre-tested widgets that can ‘jump start’ framework development with assured right-from-the-start interoperability<br>• clearly documented APIs",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.1",
      "label" : "2.2.1.1 Web Browser",
      "description" : "<b>Definition:</b>Web Browsers retrieves, presents, and traverses information resources identified by a Uniform Resource Locators (URLs) including web pages, images, videos, or other artifacts and content.   Includes the ability to traverse hyperlinks and display commonly used web content formats including HTML 4.0, current versions of JavaScript, common commercial image formats (.bmp, .gif, ...), NITF plugins, and PDF.<br><b>Description:</b>The web browser service is the primary UI mechanism for DI2E applications, supports OUSDI goals for a Web based SOA architecture, reduces the complexity and expenses of providing fat clients, and supports rapid deployment of services through widgets.<br><br>Web browsers do this accepting a Uniform Resource Locator (URL) using URI prefixes (http:, https:, ftp:, file:, etc.) to display web hosted content that includes images, audio, video, and XML files.   URI prefixes that the web browser cannot directly handle are processed by other applications (mailto: by default e-mail application, news: by user's default newsgroup reader, etc.).<br><br>Expected features include supporting:  <br>• popular web file and image formats <br>• Widgets  <br>• Operation on a variety of operating systems <br>• Multiple open information resources <br>• pop-up blockers <br>• bookmarked web pages so a user can quickly return to them.<br>• Back, forward, refresh, home, and stop buttons <br>• Address and bars that report loading status or links under cursor hovering<br>• page zooming<br>• Search and find features within a web page.<br>• Ability to delete the web cache, cookies, and browsing history. <br>• Basic security features <br><br>Examples of other popular features include (not a complete list): <br>• e-mail support  <br>• IRC chat client, Usenet news support, and Internet Relay Chat (IRC)<br>• web feed aggregator<br>• support for vertical text<br>• support for image effects and page transitions not found in W3C CSS<br>• Embedded OpenType (EOT) and OpenType fonts support<br>• \"favorites icon\" and \"quick tabs\" features <br>• ActiveX controls support <br>• Performance Advisors<br>• location-aware browsing and geolocation support  <br>• thumbnail ‘speed dial’ to move to favorite pages. <br>• plug-in support<br>• Page zooming allows text, images and other content <br>• keyboard control, voice control, and mouse gesture control<br>• Mobile devices support <br>• web feeds",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1",
      "label" : "2.2.1 Web Visualization",
      "description" : "<b>Definition:</b>The Web Visualization family provides tools to view and traverse World Wide Web accessible content and applications.<br><b>Description:</b>Web Visualization  uses web based technologies to provide user interfaces (UIs)  that can display web based content and web hosted applications.  Specific services include:<br>• Web Browser -  which retrieves, presents, and traverses information resources identified by a Uniform Resource Locators (URLs) including web pages, images, videos, or other artifacts and content.<br>• Widget Framework - which provides a toolkit that assists in combining two or more widgets (portable, lightweight, single-purpose applications that can be installed and executed within an HTML-based Web page).     Widget Frameworks displays an information arrangement, changeable by the user, to form a single application using much less demanding technical efforts than is typically required for application development.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.2",
      "label" : "2.2 Visualization",
      "description" : "<b>Definition:</b>The Visualization line of services identifies services that enable users to view and analyze data.<br><b>Description:</b>Visualization Tools enable users to view and analyze data by providing 1) web visualization tools that view and traverse World Wide Web accessible content and applications and 2) geographic visualization tools that gather/displays inputs from multiple sources of geo-coordinated data and displays it in common geographical representations.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4.2",
      "label" : "2.1.4.2 Community of Interest Find",
      "description" : "<b>Definition:</b>Community of Interest Find provides the capability to identify people with particular interests in various intelligence topics for collaboration. It finds experts throughout the DI2E and broader community to better disseminate Intel and analyze problems.<br><b>Description:</b>The Community of Interest Find service allows users to search the enterprise for other users who share a specific interest or expertise.  This allows users to identify Subject Matter Experts (SMEs) who might be able to assist in solving an intelligence problem, or identify personnel who may be interested in a piece of intelligence that relates to a topic they are watching.  This search capability facilitates networking between analysts and promotes dynamic development of communities of interest within the enterprise.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4.1",
      "label" : "2.1.4.1 Shared Calendaring",
      "description" : "<b>Definition:</b>Shared Calendaring allows users to view, schedule, organize, and share calendar events with other DI2E users.<br><b>Description:</b>Shared Calendaring leverages standards that enable calendar information to be exchanged regardless of the application that is used to create or view the information. <br><br>Interoperability features should include the ability to: send source calendar content via messages between differing calendar tracking applications (including the ability to customize the scope or type of calendar information passed); ability to move (drag) appointments from sent calendar system to users calendar; ability to review/find common free time among two or more calendars; see & (with permissions) edit group calendars; and host and subscribe to calendars (periodic synchronization of calendars).<br><br>Common client application features include the ability to: view the calendar in multiple forms and formats (daily/weekly/monthly, forward or backward through time, etc.); show appointments and events; create, edit, and update events; schedule recurring events; schedule meetings; schedule non-human resources; set work schedules & holidays; change presentation options such as font size, font styles, color themes, etc.; set and view importance markings (high importance vs. normal).",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4",
      "label" : "2.1.4 Social Networking",
      "description" : "<b>Definition:</b>Social Networking services help coordinate personal/group schedules, status, and events.<br><b>Description:</b>Social Networking services help coordinate personal/group schedules, status, and events.<br><br>  • Group Calendar - which views, schedules, organizes, and shares calendar events with other DI2E users.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3.2",
      "label" : "2.1.3.2 Audio Messaging",
      "description" : "<b>Definition:</b>Audio Messaging allows users to send and receive sound file recordings.<br><b>Description:</b>Audio Messaging allows transmittal of recorded voice messages & other audio content between and among fixed or mobile devices.    Messages can be sent one-to-one or one-to-many.   <br><br>Features to look for in audio messaging services include the ability to: record messages from multiple input systems or devices (phone, PC microphone, …); forwarding messages among different audio messaging systems; indicate who messages are from, when received; auto-attendant, announcement broadcasting including scheduled or event-based group broadcasts; and recall for handling feedback and verifying messages.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3.3",
      "label" : "2.1.3.3 E-Mail",
      "description" : "<b>Definition:</b>E-mail creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers.<br><b>Description:</b>The electronic mail (e-mail) service exchanges digital messages from an author to one or more recipients.   Architecturally, e-mail clients help author, send, receive, display, and manage messages.   E-mail servers accept, store, and forward messages from and to e-mail clients.   Structurally, an email message consists of three components, the message envelope, the message header (control information, including an originator's email address and recipient addresses), and the message body.<br><br>Basic steps in the passing of a typical e-mail message include: <br>• user entering a message & sending the message to a local mail submission agent (typically provided by internet service providers), <br>• e-mail servers use the Simple Mail Transfer Protocol (SMTP) to read to resolve the domain name and determine the fully qualified domain name of the mail exchange server in the Domain Name System (DNS),<br>• mail exchange records listing the mail exchange servers for that domain are returned,  <br>• the message is sent (using SMTP). <br>• The Message Delivery Agent (MDA) delivers the message to the recipient’s mailbox<br>• The e-mail client is users to pick up the message using standardized access protocols",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3.1",
      "label" : "2.1.3.1 Instant Messaging",
      "description" : "<b>Definition:</b>Instant Messaging allows users to communicate using text-based chat and instant messaging.<br><b>Description:</b>Instant messaging provides the ability to exchange short written text messages between and among fixed or mobile devices.   Messages can be sent one-to-one or one-to-many and might carry two-way conversational tones, short informational dissemination updates, or alerting notification.      <br><br>Characters that can be sent should include at least the upper and lower case 26 letters of the English alphabet and 10 numerals, but may include other special characters as well.   Security, confidentiality, reliability and speed are key criteria on concern in text messaging services.   Common mobile channel platforms include Short Message Services (SMS), Mobile Web, Mobile Client Applications, and SMS with Mobile Web and Secure SMS.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3",
      "label" : "2.1.3 Collaborative Messaging",
      "description" : "<b>Definition:</b>Collaborative messaging capabilities facilitate the rapid creation and sharing of messages and, where appropriate, associated attached artifacts.<br><b>Description:</b>Collaborative messaging services facilitate the rapid creation and sharing of messages and, where appropriate, associated attached artifacts.<br><br>Specific services include:  <br><br>  • Instant Messaging - which allows users to communicate using text-based chat and instant messaging.<br>  • Audio Messaging - which allows users to send and receive sound file recordings. <br>  • E-mail - which creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3",
      "label" : "2.1.2.3 Web Conferencing/VTC",
      "description" : "<b>Definition:</b>The Web Conferencing/VTC service allows users in two or more locations to communicate using near real time full-duplex audio and video.<br><b>Description:</b>The Web Conferencing/Video Telecommunications (VTC) service supports real time collaboration by offering full-duplex sharing of web, auto and video including internet telephone conferencing, videoconferencing, and web conferencing. <br><br>Typical web conferencing features include sharing of documents, desktops, presentations, browsers, and other applications; remotely controlling presentation once given presenter rights; transferring files from within application; controlling layouts (including other users); and dual monitor support.  Advanced features might include ability to play and pause movie files, conference record and playback, integrated white boarding or text chat, and highlight or pointer tools.<br><br>Some Video Teleconferencing features to look for include: higher definition support (preferably 720p or better @ 30 fps); support for various video peripherals at any end-point; on-the-fly video device switching; ability to play or pause each or every participant; customizable video size, resolution and frame rate for entire conference or any participant; show/hide participant's name; support for full-screen, tiled, floating, picture-in-picture (PIP), composite (one large, others tiled), and floating video bar display.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.2",
      "label" : "2.1.2.2 Whiteboard",
      "description" : "<b>Definition:</b>The Whiteboard service provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.<br><b>Description:</b>The Whiteboard service allows shared annotation of ‘open’ space or files (such as an image or map) among two users working at geographically separated workstations.   Each user can work see and mark the image at the same time, and each is able to see changes the others make in near-real time.<br><br>In this sense the white board is a virtual version of the physical whiteboards found in many professional office walls (some offer electronic features such as interactive display of a PC monitor or printing of the white board’s display, but these are not the type intended here).<br><br>Examples of common features (not an exhaustive or mandatory list) include: multiple page support, rich text editing support, import of external images & graphics, export of whiteboard to multiple image formats, ability to save work for later session editing, drag/drop shape support, priority layering ('bring to front'), font adjustment, and automated fill in.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.1",
      "label" : "2.1.2.1 Desktop Sharing",
      "description" : "<b>Definition:</b>Desktop Sharing provides the ability to share desktop applications with other users and groups.<br><b>Description:</b>Desktop sharing allows remote access, and thus remote real-time collaboration, with a computer’s desktop through a graphical terminal emulator.    Besides screen sharing, other common embedded features include instant messaging, file passing, and the ability to share control.  Some desktop sharing systems also permit video conferencing<br><br>File transfer systems that support the X Window System (typically Unix-based) have basic desktop sharing abilities already built in.     Microsoft Windows (Windows 2000 and later) offer basic remote access in the form of Remote Desktop Protocol.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2",
      "label" : "2.1.2 Environment Sharing",
      "description" : "<b>Definition:</b>Environment Sharing capabilities provide environments to easily share work space environments in order to promote rapid, interactive collaboration.<br><b>Description:</b>Environment Sharing capabilities provide environments that easily share work space environments in order to promote rapid, interactive collaboration.  Specific services include:<br><br>  • Desktop Sharing - which provides the ability to share desktop applications with other users and groups.<br>  • Whiteboard - which provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.<br>  • Web Conferencing/VTC - which allows users in two or more locations to communicate using near real time full-duplex audio and video.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1.2",
      "label" : "2.1.1.2 Wiki",
      "description" : "<b>Definition:</b>The Wiki service provides a page or collection of web pages that enable anyone with access to contribute to or modify content.<br><b>Description:</b>Wiki applications allow web pages to be created and edited using a common web browser and typically run on a web server (or servers). The content is stored in a file system, and changes to the content are stored in a relational database management system.    The general maintenance paradigm is to allow alteration by general public without requiring registered user accounts and edits to appear almost instantly, but some wiki sites are private or password-protected.<br><br>Style of text presented typically includes plain text, HTML, Cascading Style Sheets (CSS) or other web based formats.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1.1",
      "label" : "2.1.1.1 Bulletin Board",
      "description" : "<b>Definition:</b>Bulletin Board provides the ability to post messages and notices that interested communities can see, share, and respond to.<br><b>Description:</b>Bulletin Boards provide an internet based discussion forum where DI2E users can post messages that last for short to long periods of time.    Discussions are typically moderated and fully threaded (messages with replies and replies to replies) vs. non-threaded (messages with no replies) or semi-threaded (relies to a message, but not replies to a reply).<br><br>General users can see typically see threads, post new threads, respond to thread comments, and change certain preferences for their account such as their avatar (image representing the user), automatic signature, and ignore lists.   <br><br>Moderators can typically set, monitor, and change and privileges to groups or individual members; delete, merge, move, split, lock, or rename threads; remove unwanted content within a thread; provide access to unregistered guests; and control the size of posts.    <br><br>Administrators can typically promote and demote members, manage rules, create sections and sub-sections, and perform basic database operations.<br>Additional common features include the ability to subscribe for notification new content is added, view or vote in opinion polls, track post counts (how many posts a certain user has made), or attach symbols to convey emotional response.   More advanced bulletin boards might support Really Simple Syndication (RSS), XML and HTTP feeds (such as ATOM).",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1",
      "label" : "2.1.1 Information Boards",
      "description" : "<b>Definition:</b>The Information Board family of services provide a unified electronic platform that supports synchronous and asynchronous communication and information sharing through a variety of tools & services.<br><b>Description:</b>Information Board services help users readily share thoughts, files, diagrams, and other content that helps users work together with enhanced understanding of each other's knowledge, needs, issues, and perspectives.   Specific services include:<br>• Bulletin Board - which provides the ability to post messages and notices that interested communities can see, share, and respond to<br>• Wiki - which provides a page or collection of web pages that enable anyone with access to contribute to or modify content.<br>• Desktop Sharing - which provides the ability to share desktop applications with other users and groups.<br>• Whiteboard - which provides a virtual whiteboard service allowing one or more users to write and draw images on a simulated canvas.<br>• Web Conferencing/VTC - which allows users in two or more locations to communicate using near real time full-duplex audio and video.<br>• Instant Messaging - which allows users to communicate using text-based chat and instant messaging.<br>• Audio Messaging - which allows users to send and receive sound file recordings. <br>• E-mail - which creates and sends e-mails to, and retrieves and displays e-mail messages from, enterprise accessible e-mail servers. <br>• Group Calendar - which allows users to view, schedule, organize, and share calendar events with other DI2E users.<br>• People Find - which provides the service to identify people with particular interests or skills in various intelligence topics for consultation and collaboration.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.1",
      "label" : "2.1 Collaboration",
      "description" : "<b>Definition:</b>Collaboration provides tools and services so people can easily share knowledge, status, thoughts, and related information artifacts.<br><b>Description:</b>The Collaboration is the aggregation of infrastructure, services, people, procedures, and information to create and share data, information and knowledge used to plan, execute, and assess joint forces operations. (Ref. DCGS CONOPS v1.0 - 15 May 2007).  This line focuses on the tools and services that enables this creation and sharing of data, information, and knowledge.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3",
      "label" : "1.4.3 Application and Website Hosting",
      "description" : "<b>Definition:</b>Application and Website Hosting services provide a framework for hosting and deploying web-based applications by handling transactions, security, scalability, concurrency and management of the components they deploy. This enables developers to concentrate more on the business logic of the components rather than on infrastructure and integration tasks.<br><b>Description:</b>Application and Website Hosting provides a framework for hosting and deploying web based applications or content. The framework typically includes the application server, which provides a generalized approach for creating an application-server implementation and handles application operations between users and an organization's backend business applications. It consists of web server connectors, runtime libraries and database connectors. <br>The application server runs behind a web server and usually in front of a database. Web applications run on top of application servers, are written in languages the application server supports, and call the runtime libraries and components the application server provides.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3.1",
      "label" : "1.4.3.1 Web Content Delivery",
      "description" : "<b>Definition:</b>Web Content Delivery services serve up dynamic and static website content.<br><b>Description:</b>The Web Content Delivery service serves content to clients. It also receives requests and supports server-side scripting. <br> The commonly used specification is HTTP.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3.2",
      "label" : "1.4.3.2 Security Access Proxy",
      "description" : "<b>Definition:</b>Proxy Management services provide bi-directional access to security services.<br><b>Description:</b>The Proxy Management service provides bi-directional access to security services for authentication and authorization purposes.. It may also utilize these security services in the establishment of trusted relationships, verification of certificates, and the enforcement of access policies to protected resources.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3.3",
      "label" : "1.4.3.3 Transaction Processing",
      "description" : "<b>Definition:</b>Transaction Processing services provide connectors to services that utilize data sources<br><b>Description:</b>Transaction Processing services provide connectors to services that handle access, query and retrieval from data sources, such as a DBMS or file system. They also can provide interfaces with wrappers for federated searches within CD&R components.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3.4",
      "label" : "1.4.3.4 Application Management",
      "description" : "<b>Definition:</b>Application Management services provide the capability for application and web servers to deploy, install, activate and deactivate applications.<br><b>Description:</b>Application Management services provide the capability for application and web servers to deploy, install, activate and deactivate applications.  They also supply support for the scaling and concurrency of applications running on the server.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2",
      "label" : "2.6.2 Database Management",
      "description" : "<b>Definition:</b>The Database Management Family provides services to host database repositories and  interact with users, other applications, and the database itself to capture and analyze data.<br><b>Description:</b>Database Management includes capabilities to perform Create, Read, Update, and Delete (CRUD) operations on content, discover database content, apply security labels and tags to data objects, define database structures, and perform database administration functions.<br><br>Note: Data Object Security Labeling is included in the Security Metadata Management component.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.1",
      "label" : "2.6.2.1 Database Describe",
      "description" : "<b>Definition:</b>Database Describe enables the discovery of information about the database and the data object types stored in the database.<br><b>Description:</b>Database Describe includes describe-like capabilities to list tables, list table fields, etc.  This service feeds information to the CD&R Describe Content Service.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.2",
      "label" : "2.6.2.2 Data Object Processing",
      "description" : "<b>Definition:</b>Data Object Processing provides Create, Read, Update, and Delete (CRUD) operations on data objects stored in a database repository.<br><b>Description:</b>Data Object Processing includes capabilities to ingest data, commit transactions, rollback transactions, etc.<br><br>This includes the Content Search service that exists in CD&R.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.3",
      "label" : "2.6.2.3 Database Definition",
      "description" : "<b>Definition:</b>Database Definition enables the creation of the database and its structure, i.e., tables, indexes, triggers, etc., and the ability to modify the database structure.<br><b>Description:</b>Database Definition includes capabilities to create database, drop database, create tables, drop tables, alter tables, create indexes, drop indexes, etc.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.4",
      "label" : "2.6.2.4 Database Administration",
      "description" : "<b>Definition:</b>Database Administration enables the overall management of existing database repositories in a organization.<br><b>Description:</b>Database Administration includes capabilities to backup a database, recover a database, upgrade a database, restart a database, monitor database performance, tune database parameters, etc.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.5",
      "label" : "2.6.2.5 Data Object Tagging",
      "description" : "<b>Definition:</b>Data Object Tagging tags data with various forms of content topic metadata for information discovery and automated dissemination purposes.<br><b>Description:</b>Data tagging allows users to organize information more efficiently by associating pieces of information with tags, or keywords.  A tag is a non-hierarchical keyword or term assigned to a piece of information such as an image, file or record. This kind of metadata helps describe an item and allows it to be found again by browsing or searching.",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.3",
      "label" : "2.6.3 Workspace Management",
      "description" : "<b>Definition:</b>Workspace Management services enable users to efficiently and locally establish, update, and share data in a persistent state between work sessions.<br><b>Description:</b>Workspace Management services enable users to efficiently and locally establish, update, and share data in a persistent state between work sessions.     This lets users then ‘start where they left off’ between sessions, quickly view and analyze certain information conditions, or readily share their work session information with others.   Other benefits include:<br><br>• Removing the need for developers to create their own persistence solutions for multiple applications<br>• Common access to certain data layer content<br>• Removes certain product specific dependences (i.e. Oracle Database)<br>• Creates a ‘contract’ for sharing data between components<br>• Enables customization of data structure based on user needs",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.3.1",
      "label" : "2.6.3.1 Manage Workspace",
      "description" : "<b>Definition:</b>Manage Workspace services enable a user to add documents a new workspace session (Create), get documents (Read) documents in their workspace, edit (Update) the relationships among documents in their workspace, or remove (Delete) documents in their workspace.<br><b>Description:</b>Manage Workspace services enable a user to add documents a new workspace session (Create), get documents (Retrieve) documents in their workspace, edit (Update) the relationships among documents in their workspace, or remove (Delete) documents in their workspace.  The typical paradigm is to store documents in a hierarchical structure that contains ‘parents’ and ‘children’. Using the manage workspace service, users can then Create/Retrieve/Update/Delete (CRUD) documents using this structure in a persistent state for use between various work sessions.    <br><br>Typical operations are broken down as follows:<br>Create - add documents to the workspace<br>Retrieve – get a document, a document’s children, a documents parents, documents by type, or children documents by type<br>Update – change the content of a document, or modify hierarchical relationships among documents<br>Delete – remove individual documents, or entire ‘families’ of documents",
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.3.2",
      "label" : "2.6.3.2 Share Workspace",
      "description" : "<b>Definition:</b>Share Workspace services enable a user to send or retrieve their workspace files to other workspace component users.<br><b>Description:</b>Share Workspace services enable a user to send or retrieve their current or saved workspace files to other workspace component users.   Workspace content can be content currently in use, or a saved session of workspace content from an earlier session.   <br><br>The method of identifying candidate workspace users or methods of transmission are not prescribed as part of this service definition, but an illustrative example would be a workspace management component that enabled the storing e-mail addresses as one family within the workspace documents, then the ability to select e-mail addresses from this list and then optionally send entire workspace, or families within the overall workspace, to the selected set of e-mail addresses.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2",
      "label" : "3.1.2 Workflow Management",
      "description" : "<b>Definition:</b>Workflow Management enables coordination and cooperation among members of an organization, helping them to perform complex mission execution by optimizing, assigning, and tracking tasks across the enterprise.<br><b>Description:</b>Workflow Management services are a sequence of connected steps that describe a repeatable chain of operations, can include multiple processes, and can stem across organizations or agencies. The workflows represent the mission by means of elementary activities and connections among them. The activities represent fully automated tasks executed by computer or tasks assigned to human actors executed with the support of a computer. Workflow Management provides a consistent, streamlined, and traceable process for users to follow, and monitors the progress of who has performed the work, the status of the work, and when the work has been completed at all times.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.1",
      "label" : "3.1.2.1 Define Workflows",
      "description" : "<b>Definition:</b>The Define Workflows service enables a workflow designer to capture and maintain enterprise workflows in human-readable and machine-readable formats.<br><b>Description:</b>Define Workflows models workflow using intuitive, visual tools and a model notation. Workflow definition helps to ensure control in execution of workflows to ensure both consistency in output and that no step is skipped. Each workflow represents a series of anticipated events by illustrating tasks, and connections among them. The tasks represent fully automated activities executed by computer or tasks assigned to human actors executed with the support of a computer. In DoD environments, workflows are most commonly defined in Business Process Modeling Notation (BPMN).",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.2",
      "label" : "3.1.2.2 Identify Resources",
      "description" : "<b>Definition:</b>Identify Resources captures and maintains a list of available human and technical assets, along with their skill/capability attributes, status, and schedule.<br><b>Description:</b>The Identify Resources service identifies assets which may be distributed across multiple physical locations, and may include (but are not limited to) financial resources, inventory, human skills, production resources, information technology (IT), etc. The resources have definitions of their skills and capabilities based in terms of workflow tasks (Resource X can perform Task Y; Task Y is identified in the Define Workflow Service). The resources also have attributes for their current status (active, inactive, etc.) and their schedule (busy until a certain date, for example). The Identify Resources service integrates closely with the Execute Workflow service to identify the available resources to execute a workflow.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.3",
      "label" : "3.1.2.3 Execute Workflows",
      "description" : "<b>Definition:</b>Execute Workflows utilizes the Define Workflow and Identify Resources services to execute an instance of the workflow across the enterprise.<br><b>Description:</b>Execute Workflows enables workflow administrators to start, pause, and cancel workflows. It determines availability of resources and assigns resources to the workflow. It takes input from the Optimization service regarding the best resource to assign to a task.  After the appropriate resource is identified, it utilizes the event notification service to alert the first available resource of the task, and follows up with notifications as the workflow is executed. The service may also include information about the task, or pass along a link to reference information as a workflow is executed. Alerts can also be sent to key people within the organization when changes are made to workflow tasks.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.4",
      "label" : "3.1.2.4 Task Summary",
      "description" : "<b>Definition:</b>The Task Summary service pulls information from all of the currently running workflows in the system to allow the resource to see all of the tasks in their queue and provide information on the status of the task.<br><b>Description:</b>The Task Summary service pulls information from all of the currently running workflows in the system to allow the resource to see all of the tasks in their queue and provide information on the status of the task. It offers a single view of all of the tasks assigned to a specific resource.  Resources can also use the task summary service to display past and current tasks.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.5",
      "label" : "3.1.2.5 Task Manager",
      "description" : "<b>Definition:</b>The Task Manager provides the current or past status of a specific task, including information for an activity in a workflow.<br><b>Description:</b>Task Manager enables the resource to manually or systematically update the status of each task in their queue.  The system uses automatic notifications to send out alerts to users when the status of a task changes. Each user can configure rules for how the e-mails will be triggered (e.g. changes to certain products, certain updates to a specific workflow instance, or changes to a specific task.) Any change of status, such as changing the status of a task to complete, is captured and logged for historical reference. This gives users a complete chronological registry of workflow-related information such as interactions with a task, changes to the state of the task, reassignment of a step or task, and time-stamped data and comments.",
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.6",
      "label" : "3.1.2.6 Enterprise Workflow Reporting",
      "description" : "<b>Definition:</b>Enterprise Workflow Reporting enables an understanding of the overall enterprise status by generating a series of reports.<br><b>Description:</b>Enterprise Workflow Reporting enables administrators to define and produce summary reports for workflows that are managed within the WFM Service Family. The reports offer a variety of views of the overall workflow status, and can include reports to track resources, time to complete tasks, or a summary of tasks assigned or completed. These reports may be sent to stakeholders in real time, or on a regularly scheduled basis.",
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.2",
      "label" : "1.2.1.2 Credential Management",
      "description" : "<b>Definition:</b>Life-cycle management of credentials (e.g., PKI soft token) associated with digital identities. Credential management includes PKI life-cycle management of Identity and Non Person Entity (NPE) (e.g., server) keys and certificates.<br><b>Description:</b>none",
      "fullTextAvailable" : false
    } ]
  } ];


/* jshint ignore:end */

