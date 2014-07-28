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

MOCKDATA.filters = [];

MOCKDATA.filters.push({
  'name': 'Types',
  'key': 'TYPE',
  'src': 'images/icon/pastel/application-double.png',
  'icon': 'fa fa-files-o',
  'showOnFront': true,
  'collection': [
  {
    'code': 'APPLICATION',
    'type': 'Applications',
    'desc': 'Redeployable, Middleware',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Applications. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'SE',
    'type': 'Service Endpoint',
    'desc': 'Soap, REST, ...',
    'longDesc': 'Soap, REST, .... This is the landing page for Enterprise Services. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'SSA',
    'type': 'Reference Documents',
    'desc': 'Standards, Specifications, and APIs',
    'longDesc': 'Standards, Specifications, and APIs. This is the landing page for Reference Documents. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'SOFTWARE',
    'type': 'Software Libraries',
    'desc': 'javascript, java, .net, python',
    'longDesc': 'javascript, java, .net, python. This is the landing page for Software Libraries. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'DOCUMENTATION',
    'type': 'Documentation',
    'desc': 'Other documentation not handled under standard reference documents',
    'longDesc': 'Test scripts, Development Tools. This is the landing page for Documents. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'WIDGET',
    'type': 'Widgets',
    'desc': 'Ozone widgets',
    'longDesc': 'Ozone widgets. This is the landing page for Widgets. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'Service Endpoints',
  'key': 'SERVICE_ENDPOINT',
  'src': 'images/icon/pastel/application-double.png',
  'icon': 'fa fa-files-o',
  'showOnFront': true,
  'collection': [
  {
    'code': 'SEE',
    'type': 'Service End Points',
    'desc': 'See Description for SIPR and JWICS endpoints',
    'longDesc': 'See Description for SIPR and JWICS endpoints. This is the landing page for Service Endpoints. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'OWF Compatible Widget (y/n)',
  'key': 'OWF',
  'src': 'images/icon/pastel/application-double.png',
  'icon': 'fa fa-files-o',
  'showOnFront': true,
  'collection': [
  {
    'code': 'YES',
    'type': 'Yes',
    'desc': 'This component is an OWF compatible widget',
    'longDesc': 'This component is an OWF compatible widget<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'NO',
    'type': 'No',
    'desc': 'No, this component is not an OWF compatible widget',
    'longDesc': 'No, this component is not an OWF compatible widget <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'Classification',
  'key': 'CLASSIFICATION',
  'src': 'images/icon/pastel/application-double.png',
  'icon': 'fa fa-files-o',
  'showOnFront': true,
  'collection': [
  {
    'code': 'U',
    'type': 'U',
    'desc': 'This component is an OWF compatible widget',
    'longDesc': 'This component is an OWF compatible widget<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'U/FOUO',
    'type': 'U/FOUO',
    'desc': 'No, this component is not an OWF compatible widget',
    'longDesc': 'No, this component is not an OWF compatible widget <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'ITAR Export Approved',
  'key': 'ITAR',
  'src': 'images/icon/pastel/application-double.png',
  'icon': 'fa fa-files-o',
  'showOnFront': true,
  'collection': [
  {
    'code': 'YES',
    'type': 'Yes',
    'desc': 'Yes, this component is ITAR Export Approved',
    'longDesc': 'Yes, this component is ITAR Export Approved<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'NO',
    'type': 'No',
    'desc': 'No, this component is not ITAR Export Approved',
    'longDesc': 'No, this component is not ITAR Export Approved<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'N/A',
    'type': 'Not Applicable',
    'desc': 'This component is not applicable under ITAR restrictions',
    'longDesc': 'This component is not applicable under ITAR restrictions<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'Life Cycle Stage',
  'key': 'LCS',
  'src': 'images/icon/pastel/application-double.png',
  'icon': 'fa fa-files-o',
  'showOnFront': true,
  'collection': [
  {
    'code': 'OPERATIONS',
    'type': 'Operational',
    'desc': 'This component is Operational',
    'longDesc': 'This component is operational<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'PILOT',
    'type': 'Pilot Stage',
    'desc': 'This Component is in the Pilot Stage',
    'longDesc': 'No, this component is not an OWF compatible widget <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'DEVELOPMENT',
    'type': 'Under Developement',
    'desc': 'This Component is under developement',
    'longDesc': 'No, this component is not an OWF compatible widget <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'Commercial Export Approved Via Ear',
  'key': 'CEAVE',
  'src': 'images/icon/pastel/application-double.png',
  'icon': 'fa fa-files-o',
  'showOnFront': true,
  'collection': [
  {
    'code': 'N/A',
    'type': 'N/A',
    'desc': 'This component is Operational',
    'longDesc': 'This component is operational<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'NO',
    'type': 'No',
    'desc': 'This Component is in the Pilot Stage',
    'longDesc': 'No, this component is not an OWF compatible widget <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'YES',
    'type': 'Yes',
    'desc': 'This Component is under developement',
    'longDesc': 'No, this component is not an OWF compatible widget <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'License Types',
  'key': 'LICENSETYPE',
  'src': 'images/icon/pastel/table-multiple.png',
  'icon': 'fa fa-list-alt',
  'showOnFront': true,
  'collection': [
  {
    'code': 'CASOS',
    'type': 'CAS Open Source',
    'desc': 'CAS Open Source',
    'longDesc': 'CAS Open source license type.. This is the landing page for Visualization. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'OS',
    'type': 'Open Source',
    'desc': 'General purpose Open Source',
    'longDesc': 'General Purpose Open Source<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false,
    'landing': 'views/temp/landingpage.html'
  },
  {
    'code': 'GOTS',
    'type': 'Government Off The Shelf',
    'desc': 'Government Off The Shelf license',
    'longDesc': 'Government Off the Shelf License. Database management system software could be considered a related category, though those will typically exist for the purpose of managing a database in a particular structure (i.e. relational, object-oriented). This is the landing page for Data Management. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'FOSS',
    'type': 'Free Open Source Software',
    'desc': 'Free Open Source Software License',
    'longDesc': 'Open Sourced, GOS, Etc... This is the landing page for Collaboration. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'GOSS',
    'type': 'Government Open Source Software',
    'desc': 'Government Open Source Software License',
    'longDesc': 'Government Open Source Software . One of the earliest definitions of collaborative software is intentional group processes plus software to support them. This is the landing page for Collection. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'COTS',
    'type': 'Commercial Off The Shelf',
    'desc': 'Commercial Off The Shelf license',
    'longDesc': 'Commercial Off The Shelf License. This is the landing page for Security Management. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'GUR',
    'type': 'Government Unlimited Rights',
    'desc': 'Government Unlimited Rights License',
    'longDesc': 'Government Unlimited Rights License. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'License Method',
  'key': 'LICENSEMETH',
  'src': 'images/icon/pastel/table-multiple.png',
  'icon': 'fa fa-list-alt',
  'showOnFront': true,
  'collection': [
  {
    'code': 'OS',
    'type': 'Open Source',
    'desc': 'Open Source Licensing Method',
    'longDesc': 'Open source license type.. This is the landing page for Visualization. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'APACHE2',
    'type': 'Apache 2.0 License',
    'desc': 'General purpose Open Source',
    'longDesc': 'General Purpose Open Source<br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false,
    'landing': 'views/temp/landingpage.html'
  },
  {
    'code': 'CCA',
    'type': 'Creative Commons',
    'desc': 'Licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License - http://creativecommons.org/licenses/by-nc-nd/3.0/',
    'longDesc': 'Government Off the Shelf License. Database management system software could be considered a related category, though those will typically exist for the purpose of managing a database in a particular structure (i.e. relational, object-oriented). This is the landing page for Data Management. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'GOTS',
    'type': 'Government Off The Shelf',
    'desc': 'Free Open Source Software License for Government Projects',
    'longDesc': 'Open Sourced, GOS, Etc... This is the landing page for Collaboration. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'ENTL',
    'type': 'Enterprise License',
    'desc': 'Enterprise License - http://www.exmeritus.com/license.html',
    'longDesc': 'Government Open Source Software . One of the earliest definitions of collaborative software is intentional group processes plus software to support them. This is the landing page for Collection. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'GR',
    'type': 'Government Rights',
    'desc': 'Government projects have rights to these license items',
    'longDesc': 'Commercial Off The Shelf License. This is the landing page for Security Management. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'GUR',
    'type': 'Government Unlimited Rights',
    'desc': 'Government Unlimited Rights License',
    'longDesc': 'Government Unlimited Rights License. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

MOCKDATA.filters.push({
  'name': 'DI2E SvcV-4 High Level Mapping',
  'key': 'SVCV4',
  'src': 'images/icon/pastel/table-multiple.png',
  'icon': 'fa fa-list-alt',
  'showOnFront': true,
  'collection': [
  {
    'code': 'ALL',
    'type': 'Tagged with All',
    'desc': 'All Categories',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'OTHER',
    'type': 'Tagged with Other',
    'desc': 'Other',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0162',
    'type': '1.1 - Enterprise Management',
    'desc': 'Enterprise Management',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0175',
    'type': '1.2 - Security Management',
    'desc': 'Security Management',
    'longDesc': 'Software, typically proprietary products or open-source projects, with a primary purpose of data management. Database management system software could be considered a related category, though those will typically exist for the purpose of managing a database in a particular structure (i.e. relational, object-oriented). This is the landing page for Data Management. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0188',
    'type': '1.3 - Service Management',
    'desc': 'Service Management',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0196',
    'type': '1.4 - ORCHESTRATION MANAGEMENT',
    'desc': 'ORCHESTRATION MANAGEMENT',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0003',
    'type': '2.2 - Visualization',
    'desc': 'Visualization',
    'longDesc': 'Data visualization is not only a way to present your data, but a way to explore and understand your data.. This is the landing page for Visualization. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0097',
    'type': '2.3 - Data Discovery',
    'desc': 'Data Discovery',
    'longDesc': 'Open Sourced, GOS, Etc... This is the landing page for Collaboration. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0114',
    'type': '2.4 - Data Mediation',
    'desc': 'Data Mediation',
    'longDesc': 'Collaborative software or groupware is an application software designed to help people involved in a common task to achieve goals. One of the earliest definitions of collaborative software is intentional group processes plus software to support them. This is the landing page for Collection. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0152',
    'type': '2.5 - Data Analytics',
    'desc': 'Data Analytics',
    'longDesc': 'Identity and Access Management (IdAM) Development and Sustainment Support <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false,
    'landing': 'views/temp/landingpage.html'
  },
  {
    'code': '0122',
    'type': '2.6 - Data Handling',
    'desc': 'Data Handling',
    'longDesc': 'Specific software and hardware solutions for incident management, guard tour, guard scheduling, computer-aided dispatch, electronic post orders and trend reporting/analysis. This is the landing page for Security Management. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0032',
    'type': '3.1 - Planning and Direction',
    'desc': 'Planning and Direction',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0046',
    'type': '3.3 - Processing and Exploitation',
    'desc': 'Processing and Exploitation',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': '0058',
    'type': '3.4 - Analysis,Prediction and Production',
    'desc': 'Analysis,Prediction and Production',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  },
  {
    'code': 'COLLAB',
    'type': 'Collaboration',
    'desc': 'Collaboration',
    'longDesc': 'Redeployable, Middleware. This is the landing page for Planning and Direction. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
    'checked': false
  }
  ]
});

// MOCKDATA.filters.push({
//   'name': 'Component State',
//   'key': 'conformanceState',
//   'src': 'images/icon/pastel/cog.png',
//   'icon': 'fa fa-gear',
//   'showOnFront': false,
//   'collection': [
//   {
//     'code': 'NE',
//     'type': 'Unevaluated',
//     'desc': 'Evaluation has not started',
//     'longDesc': 'No evaluations have been initialized for these results. This is the landing page for Not Evaluated. <br/><br/> We have extended this description in order to test this modal. Descriptions can be many different lengths. Here we are trying to test how well content is handled when it is over a few sentances. <br/><br/> Now we will provide you with some filler text: Lorem ipsum dolor sit amet, vis no officiis voluptatibus, nusquam appareat accusata cu has. Usu cu ceteros vivendo fabellas, nec cu oporteat salutatus constituam, at habeo vivendo efficiendi per. Ut cum suas dissentiunt. Omnis diceret nonumes ea eum.',
//     'checked': true
//   },
//   {
//     'code': 'CAN',
//     'type': 'DI2E Candidate',
//     'desc': 'Potential reuse candidate',
//     'longDesc': 'Component is considered as a potential reuse candidate but has not completed the DI2E Framework Evaluation Process.',
//     'checked': true
//   },
//   {
//     'code': 'COM',
//     'type': 'DI2E Component',
//     'desc': 'Reusable and Interoperable.',
//     'longDesc': 'Component has been through the DI2E Evaluation Process and determined to be reusable and interoperable.',
//     'checked': true
//   },
//   {
//     'code': 'NR',
//     'type': 'Not DI2E Reusable',
//     'desc': 'Not reusable or interoperable',
//     'longDesc': 'Component was evaluated and determined to not support reusability, is not interoperable, or does not conform to DI2E standards and specifications.',
//     'checked': false
//   },
//   {
//     'code': 'OBS',
//     'type': 'Withdrawn',
//     'desc': 'Retired, superseded, or not reusable by DI2E',
//     'longDesc': 'Component has been retired, superseded, or is generally considered not reusable by DI2E',
//     'checked': false,
//   }
//   ]
// });

MOCKDATA.filters.push({
  'name': 'Evaluation Levels',
  'key': 'LEVEL',
  'src': 'images/icon/pastel/cog.png',
  'icon': 'fa fa-edit',
  'showOnFront': true,
  'collection': [
  {
    'code': 'LEVEL0',
    'type': 'Level 0 – Available for Reuse/Not Evaluated',
    'desc': 'Available for Reuse/Not Evaluated',
    'longDesc': 'Asset is added to the Storefront for reuse or consumption. Asset has not been evaluated for Enterprise readiness or Conformance. Asset will enter DI2E Clearinghouse Process to be assessed for potential reuse. Asset has completed the Component Prep and Analysis phase',
    'checked': false
  },
  {
    'code': 'LEVEL1',
    'type': 'Level 1 – Initial Reuse Analysis',
    'desc': 'Initial Reuse Analysis',
    'longDesc': 'Inspection portion of DI2E Framework Evaluation Checklist complete. These questions focus mainly on the reuse potential (Visible, Accessible, and Understandable) by analysis of the information provided. This level does not represent the pass or fail, the Consumer must read the Evaluation Report.',
    'checked': false
  },
  {
    'code': 'LEVEL2',
    'type': 'Level 2 – Integration and Test',
    'desc': 'Integration and Test',
    'longDesc': 'Integration and Test portion of the DI2E Framework Evaluation Checklist complete. These questions focus on the interoperability and ease of reuse, and will normally include an I&T plan. This level does not indicate a pass or fail of the conformance test. Consumer will read the Test Report linked in the storefront entry for program functionality.',
    'checked': false
  },
  {
    'code': 'LEVEL3',
    'type': 'Level 3 – DI2E Framework Reference Implementation',
    'desc': 'DI2E Framework Reference Implementation',
    'longDesc': 'Asset has been determined to be reusable and interoperable, appropriately documented, and conformant to applicable DI2E Framework specifications and standards and is integrated into the DI2E Reference Ecosystem.',
    'checked': false,
    'landing': 'views/temp/landingpage2.html'
  },
  {
    'code': 'LEVELNA',
    'type': 'NA – No Evaluation Planned',
    'desc': 'Results not applicable to evaluation',
    'longDesc': 'Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.',
    'checked': false
  }
  ]
});


MOCKDATA.filters.push({
  'name': 'Government Point Of Contact',
  'key': 'GPOC',
  'src': 'images/icon/pastel/cog.png',
  'icon': 'fa fa-edit',
  'showOnFront': true,
  'collection': [
  {
    'code': 'DCGS-IC',
    'type': 'DCGS-IC',
    'desc': 'Available for Reuse/Not Evaluated',
    'longDesc': 'Asset is added to the Storefront for reuse or consumption. Asset has not been evaluated for Enterprise readiness or Conformance. Asset will enter DI2E Clearinghouse Process to be assessed for potential reuse. Asset has completed the Component Prep and Analysis phase',
    'checked': false
  },
  {
    'code': 'AFLCMC/HBBG',
    'type': 'AFLCMC/HBBG',
    'desc': 'Initial Reuse Analysis',
    'longDesc': 'Inspection portion of DI2E Framework Evaluation Checklist complete. These questions focus mainly on the reuse potential (Visible, Accessible, and Understandable) by analysis of the information provided. This level does not represent the pass or fail, the Consumer must read the Evaluation Report.',
    'checked': false
  },
  {
    'code': 'DCGSEFTC',
    'type': 'DCGSEFTC',
    'desc': 'Integration and Test',
    'longDesc': 'Integration and Test portion of the DI2E Framework Evaluation Checklist complete. These questions focus on the interoperability and ease of reuse, and will normally include an I&T plan. This level does not indicate a pass or fail of the conformance test. Consumer will read the Test Report linked in the storefront entry for program functionality.',
    'checked': false
  },
  {
    'code': 'NRO',
    'type': 'NRO',
    'desc': 'DI2E Framework Reference Implementation',
    'longDesc': 'Asset has been determined to be reusable and interoperable, appropriately documented, and conformant to applicable DI2E Framework specifications and standards and is integrated into the DI2E Reference Ecosystem.',
    'checked': false,
    'landing': 'views/temp/landingpage2.html'
  },
  {
    'code': 'AFLCMC',
    'type': 'AFLCMC',
    'desc': 'Results not applicable to evaluation',
    'longDesc': 'Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.',
    'checked': false
  },
  {
    'code': 'USATMC',
    'type': 'USATMC',
    'desc': 'Results not applicable to evaluation',
    'longDesc': 'Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.',
    'checked': false
  },
  {
    'code': 'STRATCOM',
    'type': 'STRATCOM',
    'desc': 'Results not applicable to evaluation',
    'longDesc': 'Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.',
    'checked': false
  },
  {
    'code': 'NGA',
    'type': 'NGA',
    'desc': 'Results not applicable to evaluation',
    'longDesc': 'Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.',
    'checked': false
  },
  {
    'code': 'NRO/GED',
    'type': 'NRO/GED',
    'desc': 'Results not applicable to evaluation',
    'longDesc': 'Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.',
    'checked': false
  },
  {
    'code': 'AFRL',
    'type': 'AFRL',
    'desc': 'Results not applicable to evaluation',
    'longDesc': 'Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.',
    'checked': false
  },
  {
    'code': 'NRL',
    'type': 'NRL',
    'desc': 'Results not applicable to evaluation',
    'longDesc': 'Planned Evaluation does not apply to DI2E Priorities, Focus Areas, Reference Architecture, Storefront (e.g. Guidebooks, reusable Contract Language, Lessons Learned, etc.). N/A indicates no evaluation is expected.',
    'checked': false
  }
  ]
});


/* jshint ignore:end */

