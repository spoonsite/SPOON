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
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
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
    "allowMutlipleFlg" : false,
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
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
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
    "allowMutlipleFlg" : false,
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
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
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
    "allowMutlipleFlg" : false,
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
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
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
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
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
    "allowMutlipleFlg" : false,
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
    "requiredFlg" : false,
    "archtechtureFlg" : false,
    "importantFlg" : false,
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
    "archtechtureFlg" : false,
    "importantFlg" : false,
    "allowMutlipleFlg" : false,
    "codes" : [ {
      "code" : "1",
      "label" : "1 Infrastructure Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2",
      "label" : "2 Common Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3",
      "label" : "3 Mission Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1",
      "label" : "3.1 Planning and Direction",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1",
      "label" : "3.1.1 Define and Prioritize Requirements",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.1",
      "label" : "3.1.1.1 PIR Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.1.2",
      "label" : "3.1.1.2 RFI Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2",
      "label" : "3.1.2 Workflow Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.1",
      "label" : "3.1.2.1 Define Workflows",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.2",
      "label" : "3.1.2.2 Identify Resources",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.3",
      "label" : "3.1.2.3 Execute Workflows",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.4",
      "label" : "3.1.2.4 Task Summary",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.5",
      "label" : "3.1.2.5 Task Manager",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.2.6",
      "label" : "3.1.2.6 Enterprise Workflow Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3",
      "label" : "3.1.3 Planning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.1",
      "label" : "3.1.3.1 Collection Requirements Planning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.2",
      "label" : "3.1.3.2 Sensor Cataloging",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.3",
      "label" : "3.1.3.3 Intelligence Source Selection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.4",
      "label" : "3.1.3.4 Exploitation Planning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.3.5",
      "label" : "3.1.3.5 Target Planning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4",
      "label" : "3.1.4 Asset Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.1",
      "label" : "3.1.4.1 Asset Status Summary",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.4.2",
      "label" : "3.1.4.2 Asset Discovery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.5",
      "label" : "3.1.5 Tasking Request",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.5.1",
      "label" : "3.1.5.1 Tasking Message Preparation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.5.2",
      "label" : "3.1.5.2 Task Asset Request",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.6",
      "label" : "3.1.6 Sensor Web Enablement",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.6.1",
      "label" : "3.1.6.1 Sensor Observation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.1.6.2",
      "label" : "3.1.6.2 Sensor Planning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1",
      "label" : "2.1 Collaboration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1",
      "label" : "2.1.1 Information Boards",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1.1",
      "label" : "2.1.1.1 Bulletin Board",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.1.2",
      "label" : "2.1.1.2 Wiki",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2",
      "label" : "2.1.2 Environment Sharing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.1",
      "label" : "2.1.2.1 Desktop Sharing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.2",
      "label" : "2.1.2.2 Whiteboard",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.2.3",
      "label" : "2.1.2.3 Web Conferencing/VTC",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3",
      "label" : "2.1.3 Collaborative Messaging",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3.1",
      "label" : "2.1.3.1 Instant Messaging",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3.2",
      "label" : "2.1.3.2 Audio Messaging",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.3.3",
      "label" : "2.1.3.3 E-Mail",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4",
      "label" : "2.1.4 Social Networking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4.1",
      "label" : "2.1.4.1 Shared Calendaring",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.1.4.2",
      "label" : "2.1.4.2 Community of Interest Find",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1",
      "label" : "1.1 Enterprise Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1",
      "label" : "1.1.1 Metrics Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1.1",
      "label" : "1.1.1.1 Metrics Measurements Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.1.2",
      "label" : "1.1.1.2 Metrics Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.2",
      "label" : "1.1.2 Translation and Synchronization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.2.1",
      "label" : "1.1.2.1 Domain Name System (DNS)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.2.2",
      "label" : "1.1.2.2 Time Synchronization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3",
      "label" : "1.1.3 Enterprise Monitoring",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3.1",
      "label" : "1.1.3.1 Fault Detection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3.2",
      "label" : "1.1.3.2 Fault Isolation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.3.3",
      "label" : "1.1.3.3 Site Monitoring",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2",
      "label" : "3.2 Collection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1",
      "label" : "3.2.1 Asset Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.1",
      "label" : "3.2.1.1 Sensor Provisioning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.2",
      "label" : "3.2.1.2 Sensor Cross Queuing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.3",
      "label" : "3.2.1.3 Sensor Command Conversion",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.2.1.4",
      "label" : "3.2.1.4 Sensor Alerting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2",
      "label" : "2.2 Visualization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1",
      "label" : "2.2.1 Web Visualization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.1",
      "label" : "2.2.1.1 Web Browser",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.1.2",
      "label" : "2.2.1.2 Widget Framework",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2",
      "label" : "2.2.2 Geographic Visualization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.1",
      "label" : "2.2.2.1 Geographic Information Display",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.2",
      "label" : "2.2.2.2 Web Coverage",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.3",
      "label" : "2.2.2.3 Web Feature",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.4",
      "label" : "2.2.2.4 Web Map",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.2.5",
      "label" : "2.2.2.5 Weather Visualization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3",
      "label" : "2.2.3 Analytics Visualization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3.1",
      "label" : "2.2.3.1 Analytic Rendering",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.2.3.2",
      "label" : "2.2.3.2 Common Operational Picture (COP)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.4",
      "label" : "1.1.4 Event Notification",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.4.1",
      "label" : "1.1.4.1 Notification Producer",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.4.2",
      "label" : "1.1.4.2 Notification Broker",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.4.3",
      "label" : "1.1.4.3 Notification Consumer",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.5",
      "label" : "1.1.5 Enterprise Resource Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.1.5.1",
      "label" : "1.1.5.1 Global Unique Identifier (GUID)",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3",
      "label" : "3.3 Processing and Exploitation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1",
      "label" : "3.3.1 GEOINT Processing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.1",
      "label" : "3.3.1.1 Image Rectification",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.2",
      "label" : "3.3.1.2 FMV Geoprocessing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.3",
      "label" : "3.3.1.3 AOI Processing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.4",
      "label" : "3.3.1.4 State Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.1.5",
      "label" : "3.3.1.5 Image Chipping",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.2",
      "label" : "3.3.2 SIGINT Processing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.2.1",
      "label" : "3.3.2.1 Signal Pattern Recognition",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.3",
      "label" : "3.3.3 CI/HUMINT Processing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.3.1",
      "label" : "3.3.3.1 Source Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.4",
      "label" : "3.3.4 Data Exploitation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.4.1",
      "label" : "3.3.4.1 Language Translation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.5",
      "label" : "3.3.5 MASINT Processing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6",
      "label" : "3.3.6 Support to Targeting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.1",
      "label" : "3.3.6.1 Target Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.2",
      "label" : "3.3.6.2 Target Data Matrix",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.3",
      "label" : "3.3.6.3 Target Validation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.4",
      "label" : "3.3.6.4 Target Folder",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.5",
      "label" : "3.3.6.5 Target List",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.6",
      "label" : "3.3.6.6 Target Mensuration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.3.6.7",
      "label" : "3.3.6.7 BDA/CDA",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3",
      "label" : "2.3 Data Discovery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1",
      "label" : "2.3.1 Content Discovery and Retrieval",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.1",
      "label" : "2.3.1.1 Content Search",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.2",
      "label" : "2.3.1.2 Brokered Search",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.3",
      "label" : "2.3.1.3 Retrieve Content",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.4",
      "label" : "2.3.1.4 Deliver Content",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.5",
      "label" : "2.3.1.5 Describe Content",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.6",
      "label" : "2.3.1.6 Query Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.3.1.7",
      "label" : "2.3.1.7 Query Results Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.4",
      "label" : "2.4 Data Mediation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1",
      "label" : "2.4.1 Data Preparation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.1",
      "label" : "2.4.1.1 Schema Validation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.2",
      "label" : "2.4.1.2 Data Validation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.3",
      "label" : "2.4.1.3 Data Transformation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.4",
      "label" : "2.4.1.4 Schema Transformation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.5",
      "label" : "2.4.1.5 Image Transformation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.4.1.6",
      "label" : "2.4.1.6 Data De-Duplication",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2",
      "label" : "1.2 Security Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1",
      "label" : "1.2.1 Identity and Access Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.1",
      "label" : "1.2.1.1 Local Identity Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.10",
      "label" : "1.2.1.10 Attribute Access",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.11",
      "label" : "1.2.1.11 Certificate Validation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.2",
      "label" : "1.2.1.2 Credential Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.3",
      "label" : "1.2.1.3 Resource Policy Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.4",
      "label" : "1.2.1.4 Authentication Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.5",
      "label" : "1.2.1.5 Policy Decision Point",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.6",
      "label" : "1.2.1.6 Policy Enforcement Point",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.7",
      "label" : "1.2.1.7 Policy Access Point",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.8",
      "label" : "1.2.1.8 Security Token Service",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.1.9",
      "label" : "1.2.1.9 Federation Service Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4",
      "label" : "3.4 Analysis, Prediction and Production",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1",
      "label" : "3.4.1 GEOINT Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.1",
      "label" : "3.4.1.1 Change Detection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.10",
      "label" : "3.4.1.10 Sensor Model Instantiation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.11",
      "label" : "3.4.1.11 GEO-Calculations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.2",
      "label" : "3.4.1.2 Triangulation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.3",
      "label" : "3.4.1.3 Resection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.4",
      "label" : "3.4.1.4 Geomensuration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.5",
      "label" : "3.4.1.5 DPPDB Mensuration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.6",
      "label" : "3.4.1.6 Image Registration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.7",
      "label" : "3.4.1.7 MTI Tracking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.8",
      "label" : "3.4.1.8 Topographical Survey",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.1.9",
      "label" : "3.4.1.9 Automatic Target Recognition",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2",
      "label" : "3.4.2 SIGINT Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2.1",
      "label" : "3.4.2.1 SIGINT Analysis and Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2.2",
      "label" : "3.4.2.2 Emitter Correlation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2.3",
      "label" : "3.4.2.3 Emitter Geolocation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.2.4",
      "label" : "3.4.2.4 COMINT Externals Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.3",
      "label" : "3.4.3 HUMINT Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.3.1",
      "label" : "3.4.3.1 Entity Activity Patterns",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.3.2",
      "label" : "3.4.3.2 Identity Disambiguation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.4",
      "label" : "3.4.4 MASINT/AGI Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5",
      "label" : "2.5 Data Analytics",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1",
      "label" : "2.5.1 Data Enrichment",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1.1",
      "label" : "2.5.1.1 Entity Extraction",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1.2",
      "label" : "2.5.1.2 Entity Association",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1.3",
      "label" : "2.5.1.3 Categorize Content",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.1.4",
      "label" : "2.5.1.4 Data Commenting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.2",
      "label" : "2.5.2 Media Monitoring",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.2.1",
      "label" : "2.5.2.1 Chat Monitor",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.2.2",
      "label" : "2.5.2.2 Video Monitor",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.5.2.3",
      "label" : "2.5.2.3 Audio Monitor",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3",
      "label" : "1.2.3 Security Metadata Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3.1",
      "label" : "1.2.3.1 Data Security Marking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.3.2",
      "label" : "1.2.3.2 Security Label Format Validation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4",
      "label" : "1.2.4 System and Communication Protection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.1",
      "label" : "1.2.4.1 Vulnerability Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.2",
      "label" : "1.2.4.2 Intrusion Detection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.3",
      "label" : "1.2.4.3 Intrusion Prevention",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.4",
      "label" : "1.2.4.4 Virus Protection",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.4.5",
      "label" : "1.2.4.5 Incident Response",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.5",
      "label" : "1.2.5 Audit Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.5.1",
      "label" : "1.2.5.1 Audit Log Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.5.2",
      "label" : "1.2.5.2 Audit Log Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.2.6",
      "label" : "1.2.6 Cross Domain",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5",
      "label" : "3.4.5 Production",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5.1",
      "label" : "3.4.5.1 Reporting Services",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5.2",
      "label" : "3.4.5.2 Production Workflow",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.5.3",
      "label" : "3.4.5.3 Digital Production",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6",
      "label" : "3.4.6 Analytic Decision Support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.1",
      "label" : "3.4.6.1 Timelines Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.2",
      "label" : "3.4.6.2 Structured Analytic Techniques",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.3",
      "label" : "3.4.6.3 Argument Mapping",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.4",
      "label" : "3.4.6.4 Alternative Future Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.6.5",
      "label" : "3.4.6.5 Link Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7",
      "label" : "3.4.7 Modeling and Simulation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.1",
      "label" : "3.4.7.1 War Gaming",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.2",
      "label" : "3.4.7.2 Scenario Generation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.3",
      "label" : "3.4.7.3 Model Building",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.4",
      "label" : "3.4.7.4 Sensor Modeling",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.5",
      "label" : "3.4.7.5 Target Solution Modeling",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.7.6",
      "label" : "3.4.7.6 Orchestration Modeling",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8",
      "label" : "3.4.8 Analysis Support to C2",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8.1",
      "label" : "3.4.8.1 Order of Battle Analysis",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8.2",
      "label" : "3.4.8.2 Intelligence Preparation of the Battlefield",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8.3",
      "label" : "3.4.8.3 Mission Planning and Force Execution support",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.4.8.4",
      "label" : "3.4.8.4 Weather Effect  Planning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6",
      "label" : "2.6 Data Handling",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1",
      "label" : "2.6.1 Content Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.1",
      "label" : "2.6.1.1 Content Repository",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.2",
      "label" : "2.6.1.2 Content Navigation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.3",
      "label" : "2.6.1.3 Object Processing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.4",
      "label" : "2.6.1.4 Object Folders",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.5",
      "label" : "2.6.1.5 Managed Content Discovery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.6",
      "label" : "2.6.1.6 Content Versioning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.7",
      "label" : "2.6.1.7 Object Relationship",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.1.8",
      "label" : "2.6.1.8 Content Policy",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2",
      "label" : "2.6.2 Database Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.1",
      "label" : "2.6.2.1 Database Describe",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.2",
      "label" : "2.6.2.2 Data Object Processing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.3",
      "label" : "2.6.2.3 Database Definition",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.4",
      "label" : "2.6.2.4 Database Administration",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.2.5",
      "label" : "2.6.2.5 Data Object Tagging",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3",
      "label" : "1.3 Service Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1",
      "label" : "1.3.1 Repository and Registry",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.1",
      "label" : "1.3.1.1 Service Inquiry",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.2",
      "label" : "1.3.1.2 Service Subscription",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.1.3",
      "label" : "1.3.1.3 Service Publishing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2",
      "label" : "1.3.2 Service Configuration Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.1",
      "label" : "1.3.2.1 Service Configuration Identification",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.2",
      "label" : "1.3.2.2 Service Configuration Control",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.3.2.3",
      "label" : "1.3.2.3 Service Configuration Verification and Audit",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5",
      "label" : "3.5 BA Data Dissemination and Relay",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1",
      "label" : "3.5.1 Dissemination Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1.1",
      "label" : "3.5.1.1 Dissemination Authorization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1.2",
      "label" : "3.5.1.2 Package Product",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1.3",
      "label" : "3.5.1.3 Tear Line Reporting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "3.5.1.4",
      "label" : "3.5.1.4 Foreign Disclosure Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.3",
      "label" : "2.6.3 Workspace Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.3.1",
      "label" : "2.6.3.1 Manage Workspace",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.3.2",
      "label" : "2.6.3.2 Share Workspace",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.4",
      "label" : "2.6.4 Data Quality",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.4.1",
      "label" : "2.6.4.1 Data Quality Definition",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.4.2",
      "label" : "2.6.4.2 Data Quality Extraction",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.4.3",
      "label" : "2.6.4.3 Data Quality Measurement",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5",
      "label" : "2.6.5 Records Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.1",
      "label" : "2.6.5.1 Record Annotations",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.10",
      "label" : "2.6.5.10 Change Agent",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.2",
      "label" : "2.6.5.2 Record Authorities",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.3",
      "label" : "2.6.5.3 Record Categories",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.4",
      "label" : "2.6.5.4 Record Dispositions",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.5",
      "label" : "2.6.5.5 Record Documents",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.6",
      "label" : "2.6.5.6 Managed Records",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.7",
      "label" : "2.6.5.7 Record Query",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.8",
      "label" : "2.6.5.8 Record Authentications",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "2.6.5.9",
      "label" : "2.6.5.9 Record Attribute Profiles",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4",
      "label" : "1.4 Orchestration Management",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1",
      "label" : "1.4.1 Orchestration Planning",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.1",
      "label" : "1.4.1.1 Matchmaking",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.1.2",
      "label" : "1.4.1.2 Optimization",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2",
      "label" : "1.4.2 Orchestration Execution",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2.1",
      "label" : "1.4.2.1 Execution Engine",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.2.2",
      "label" : "1.4.2.2 Protocol Mediation",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3",
      "label" : "1.4.3 Application and Website Hosting",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3.1",
      "label" : "1.4.3.1 Web Content Delivery",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3.2",
      "label" : "1.4.3.2 Security Access Proxy",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3.3",
      "label" : "1.4.3.3 Transaction Processing",
      "description" : null,
      "fullTextAvailable" : false
    }, {
      "code" : "1.4.3.4",
      "label" : "1.4.3.4 Application Management",
      "description" : null,
      "fullTextAvailable" : false
    } ]
  } ];


/* jshint ignore:end */

