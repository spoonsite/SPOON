<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
       
        <script type="text/javascript">
            /* global Ext, CoreUtil */
            
            Ext.onReady(function () {
                var downloadFile = function(filename, csvContent ){
                    
                    var charset = "utf-8";
                    var blob = new Blob([csvContent], {
                         type: "text/csv;charset="+ charset + ";"
                    });
                    if (window.navigator.msSaveOrOpenBlob) {
                        console.log("Save Blob");
                         window.navigator.msSaveBlob(blob, filename);
                    } else {
                        console.log("Create Link");
                        var link = document.createElement("a");
                        if (link.download !== undefined) { // feature detection
                            // Browsers that support HTML5 download attribute
                            var url = URL.createObjectURL(blob);
                            link.setAttribute("href", url);
                            link.setAttribute("download", filename);
                            link.style.visibility = 'hidden';
                            document.body.appendChild(link);
                            link.click();
                            document.body.removeChild(link);
                        } 
                    }
                };
                
                //
                //  User Tracking Tab-------------->
                //
                
                //
                //  User Tracking Store
                //                
                var userTrackingGridStore = Ext.create('Ext.data.Store', {
                    id: 'userTrackingGridStore',
                    autoLoad: true,
                    pageSize: 100,
                    remoteSort: true,
                    sorters: [
                                new Ext.util.Sorter({
                                     id: 'userSorter',
                                     property: 'createUser',
                                     direction: 'DESC'
                                })
                            ],
                    fields: [
                                {name: 'deviceType', mapping: function (data) {
                                        return data.deviceType;
                                    }},
                                {name: 'storageVersion', mapping: function (data) {
                                        return data.storageVersion;
                                    }},
                                {name: 'securityMarkingType', mapping: function (data) {
                                        return data.securityMarkingType;
                                    }},
                                {name: 'activeStatus', mapping: function (data) {
                                        return data.activeStatus;
                                    }},
                                {name: 'adminModified', mapping: function (data) {
                                        return data.adminModified;
                                    }},
                                {name: 'screenWidth', mapping: function (data) {
                                        return data.screenWidth;
                                    }},
                                {name: 'screenHeight', mapping: function (data) {
                                        return data.screenHeight;
                                    }},
                                {name: 'eventDts', mapping: function (data) {
                                        return data.eventDts;
                                    }},
                                {name: 'trackingId', mapping: function (data) {
                                        return data.trackingId;
                                    }},
                                {name: 'trackEventTypeCode', mapping: function (data) {
                                        
                                        if(data.trackEventTypeCode === 'SYNC'){
                                            return 'Component Sync';
                                        }
                                        else if(data.trackEventTypeCode === 'ELC'){
                                            return 'External Link Click';
                                        }
                                        else if(data.trackEventTypeCode === 'L'){
                                            return 'Login';
                                        }
                                        else if(data.trackEventTypeCode === 'V'){
                                            return 'View';
                                        }
                                        else{
                                            return data.trackEventTypeCode;
                                        }
                                    }},
                                {name: 'updateDts', mapping: function (data) {
                                        return data.updateDts;
                                    }},
                                {name: 'browser', mapping: function (data) {
                                        return data.browser;
                                    }},
                                {name: 'userAgent', mapping: function (data) {
                                        return data.userAgent;
                                    }},
                                {name: 'browserVersion', mapping: function (data) {
                                        return data.browserVersion;
                                    }},
                                {name: 'organization', mapping: function (data) {
                                        return data.organization;
                                    }},
                                {name: 'updateUser', mapping: function (data) {
                                        return data.updateUser;
                                    }},
                                {name: 'createDts', mapping: function (data) {
                                        return data.createDts;
                                    }},
                                {name: 'createUser', mapping: function (data) {
                                        return data.createUser;
                                    }},
                                {name: 'userTypeCode', mapping: function (data) {
                                        return data.userTypeCode;
                                    }},
                                {name: 'osPlatform', mapping: function (data) {
                                        return data.osPlatform;
                                    }},
                                {name: 'clientIp', mapping: function (data) {
                                        return data.clientIp;
                                    }}                               
                            ],
                    proxy: CoreUtil.pagingProxy({
                                url: '../api/v1/resource/usertracking',
                                method: 'GET',
                                reader: {
                                    type: 'json',
                                    rootProperty: 'result',
                                    totalProperty: 'count'
                                }
                            })
                });

                var userTrackingGrid = Ext.create('Ext.grid.Panel', {
                    title: '',
                    id: 'userTrackingGrid',
                    store: userTrackingGridStore,
                    columnLines: true,
                    bodyCls: 'border_accent',
                    selModel: {
                        selType: 'checkboxmodel'
                    },
                    plugins: 'gridfilters',
                    enableLocking: true,
                    columns: [
                        {text: 'Name', dataIndex: 'createUser', width: 125, flex:1, lockable: true,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Organization', dataIndex: 'organization', width: 250,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'User Type', dataIndex: 'userTypeCode', width: 150,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Event Date', dataIndex: 'eventDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        {text: 'Event Type', dataIndex: 'trackEventTypeCode', width: 250},
                        {text: 'Client IP', dataIndex: 'clientIp', width: 150},
                        {text: 'Browser', dataIndex: 'browser', width: 150,
                            filter: {
                                type:'string'
                            }
                        },
                        {text: 'Browser Version', dataIndex: 'browserVersion', width: 150,
                            filter: {
                                type:'string'
                            }
                        },
                        {text: 'OS', dataIndex: 'osPlatform', width: 200,
                            filter: {
                                type:'string'
                            }
                        } 
                    ],
                    dockedItems: [
//                        {
//                            dock: 'top',
//                            xtype: 'toolbar',
//                            items: [
//                                  Ext.create('OSF.component.StandardComboBox', {
//                                            id: 'userTrackingFilter-ActiveStatus',
//                                            emptyText: 'All',
//                                            fieldLabel: 'Active Status',
//                                            name: 'activeStatus',	
//                                            listeners: {
//                                                change: function(filter, newValue, oldValue, opts){
//                                                    userRefreshGrid();
//                                                }
//                                            },
//                                            storeConfig: {
//										customStore: {
//											fields: [
//												'code',
//												'description'
//											],
//											data: [												
//												{
//													code: 'A',
//													description: 'Active'
//												},
//												{
//													code: 'I',
//													description: 'Inactive'
//												},
//												{
//													code: 'All',
//													description: 'All'
//												}
//											]
//										}
//									}
//                            })]
//                       },
                       {
                            dock: 'top',
                            xtype: 'toolbar',
                            items: [
                            {
                                text: 'Refresh',
                                scale: 'medium',
                                id: 'userRefreshButton',
                                iconCls: 'fa fa-2x fa-refresh',
                                tip: 'Refresh the list of records',
                                handler: function () {
                                   userRefreshGrid();
                                },
                                tooltip: 'Refresh the list of records'
                            },
                            {
                                text: 'View',
                                id: 'userViewButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-eye',
                                disabled: true,
                                handler: function () {
                                    userViewMessage();
                                },
                                tooltip: 'View the message data',
                            },
                            
                            {
                                text: 'Export',
                                id: 'userExportButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-download',
                                disabled: false,
                                handler: function () {
                                   userExport();         
                                },
                                tooltip:'Export data and download to .csv format'

                            },
                            {
                                xtype: 'tbfill'
                            }]
                        },
                        { 
                            xtype: 'pagingtoolbar',
                            dock: 'bottom',
                            store: 'userTrackingGridStore',
                            displayInfo: true   
                        }
                    ],
                    listeners: {
                        itemdblclick: function (grid, record, item, index, e, opts) {
                            console.log("Double Clicked Row:"+index);
                            userViewMessage();
                        },
                        selectionchange: function (grid, record, index, opts) {
                           userCheckNavButtons(); 
                        }
                    }
                });
                
                
                //
                //   USER Tracking FUNCTIONS
                //
                
                //
                // Check which buttons should be on and which should be off
                //
                var userCheckNavButtons = function() {
                    
                    if (userTrackingGrid.getSelectionModel().getCount() === 1) {
                        
                        Ext.getCmp('userViewButton').setDisabled(false);
                    } else if (userTrackingGrid.getSelectionModel().getCount() > 1) {
                        
                        Ext.getCmp('userViewButton').setDisabled(true); 
                    } else {
                        
                        Ext.getCmp('userViewButton').setDisabled(true);    
                    }
                };
                
                //
                //  Refresh and reload the grid
                //
                var userRefreshGrid = function(){ 
                     Ext.getCmp('userTrackingGrid').getStore().load({
//						params: {
//							status: Ext.getCmp('userTrackingFilter-ActiveStatus').getValue() ? Ext.getCmp('userTrackingFilter-ActiveStatus').getValue() : 'ALL',
//						    all: (Ext.getCmp('userTrackingFilter-ActiveStatus').getValue() === 'All')
//                             
//                        }
				    });
                };
                
                //
                //  Create View String
                //
                var createHTMLViewString= function(recobj){
                    var myHTMLStr = '';
                    
                    myHTMLStr+='<b>Platform Information</b><br/>';
                    myHTMLStr+='<ul><li><b>OS Platform: </b>'+recobj.osPlatform+'</li>';
                    myHTMLStr+='<li><b>User Agent: </b>'+recobj.userAgent+'</li></ul>';
                    
                    myHTMLStr+='<b>Browser Information</b><br/>';
                    myHTMLStr+='<ul><li><b>Device Type: </b>'+recobj.deviceType+'</li>';
                    myHTMLStr+='<li><b>Browser: </b>'+recobj.browser+'</li>';
                    myHTMLStr+='<li><b>Version: </b>'+recobj.browserVersion+'</li></ul>';
                    
                    myHTMLStr+='<b>Tracking Information</b><br/>';
                    myHTMLStr+='<ul><li><b>Tracking Id: </b>'+recobj.trackingId+'</li>';
                    myHTMLStr+='<li><b>Tracked Date: </b>'+recobj.eventDts+'</li></ul>';
                    return myHTMLStr;
                    
                };
                
                //
                // View Record
                //
                var  userViewMessage = function (whichone){
                    
                    var selectedObj = Ext.getCmp('userTrackingGrid').getSelection()[0].data;
                    console.log("Selected Obj",selectedObj);
                    
                    Ext.create('Ext.window.Window', {
                        title: 'View Record Information',
                        iconCls: 'fa fa-info-circle',
                        width: '30%',
                        bodyStyle: 'padding: 10px;',
                        y: 40,
                        modal: true,
                        maximizable: false,
                        layout: 'fit',
                        html: createHTMLViewString(selectedObj)
                        
                    }).show();                    
                };
                
                //
                // User Export
                //
                var  userExport = function (){
                    Ext.toast('Exporting User Tracking Data ...');
                    Ext.Ajax.request({
                        url: '../api/v1/resource/usertracking/export',
                        method: 'GET',
                        success: function (response, opts) {
                            
                            downloadFile('userTracking.csv', response.responseText );
                        }
                    });
                };
                
                
                
                
                
                //
                //Entry Tracking Tab-------------->
                //
                
                //
                // Entry Tracking Store
                //
                
                
                var entryTrackingGridStore = Ext.create('Ext.data.Store', {
                    id: 'entryTrackingGridStore',
                    autoLoad: false,
                    pageSize: 100,
                    remoteSort: true,
                    sorters: [
                                new Ext.util.Sorter({
                                     id: 'entrySorter',
                                     property: 'name',
                                     direction: 'DESC'
                                })
                            ],
                    fields: [
                                
                                {name: 'componentId', mapping: function (data) {
                                        return data.data.componentId;
                                        
                                    }},
                                {name: 'name', mapping: function (data) {
                                        console.log("Entry Data:", data);
                                        return data.name;
                                        
                                    }},
                                {name: 'description', mapping: function (data) {
                                        return data.description;
                                    }},
                                
                                {name: 'componentType', mapping: function (data) {
                                        return data.componentType;
                                    }},
                                {name: 'eventType', mapping: function (data) {
                                        return data.componentType;
                                    }},
                                
                                {name: 'eventDts', mapping: function (data) {
                                        return data.data.eventDts;
                                    }},
                                {name: 'trackingId', mapping: function (data) {
                                        return data.trackingId;
                                    }},
                                {name: 'trackEventTypeCode', mapping: function (data) {
                                        if(data.data.trackEventTypeCode === 'SYNC'){
                                            return 'Component Sync';
                                        }
                                        else if(data.data.trackEventTypeCode === 'ELC'){
                                            return 'External Link Click';
                                        }
                                        else if(data.data.trackEventTypeCode === 'L'){
                                            return 'Login';
                                        }
                                        else if(data.data.trackEventTypeCode === 'V'){
                                            return 'View';
                                        }
                                        else{
                                            return data.data.trackEventTypeCode;
                                        }
                                    }},
                                
                                {name: 'activeStatus', mapping: function (data) {
                                        return data.data.activeStatus;
                                    }},
                                {name: 'updateDts', mapping: function (data) {
                                        return data.data.updateDts;
                                    }},
                                {name: 'updateUser', mapping: function (data) {
                                        return data.data.updateUser;
                                    }},
                                {name: 'createDts', mapping: function (data) {
                                        return data.data.createDts;
                                    }},
                                {name: 'createUser', mapping: function (data) {
                                        return data.data.createUser;
                                    }},
                                {name: 'attributes', mapping: function (data) {
                                        return data.attributes;
                                    }},
                                {name: 'clientIp', mapping: function (data) {
                                        return data.data.clientIp;
                                    }} 
                            ],
                    proxy: CoreUtil.pagingProxy({
                                url: '../api/v1/resource/componenttracking',
                                reader: {
                                    type: 'json',
                                    rootProperty: 'result',
                                    totalProperty: 'count'
                                }
                            })
                });

                var entryTrackingGrid = Ext.create('Ext.grid.Panel', {
                    title: '',
                    id: 'entryTrackingGrid',
                    store: entryTrackingGridStore,
                    columnLines: true,
                    bodyCls: 'border_accent',
                    selModel: {
                        selType: 'checkboxmodel'
                    },
                    plugins: 'gridfilters',
                    enableLocking: true,
                    columns: [
                        {text: 'Name', dataIndex: 'name', width: 125, flex:1, lockable: true,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Entry ID', dataIndex: 'componentId', width: 300,
                            filter: {
                                type: 'string'
                            }
                        },
                        
                        {text: 'Event Date', dataIndex: 'eventDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        {text: 'Event Type', dataIndex: 'trackEventTypeCode', width: 150},
                        {text: 'Client IP', dataIndex: 'clientIp', width: 125},
                        {text: 'User', dataIndex: 'createUser', width: 150,
                            filter: {
                                type:'string'
                            }
                        }
                    ],
                    dockedItems: [
//                        {
//                            dock: 'top',
//                            xtype: 'toolbar',
//                            items: [
//                                  Ext.create('OSF.component.StandardComboBox', {
//                                            id: 'entryTrackingFilter-ActiveStatus',
//                                            emptyText: 'All',
//                                            fieldLabel: 'Active Status',
//                                            name: 'activeStatus',	
//                                            listeners: {
//                                                change: function(filter, newValue, oldValue, opts){
//                                                    entryRefreshGrid();
//                                                }
//                                            },
//                                            storeConfig: {
//										customStore: {
//											fields: [
//												'code',
//												'description'
//											],
//											data: [												
//												{
//													code: 'A',
//													description: 'Active'
//												},
//												{
//													code: 'I',
//													description: 'Inactive'
//												},
//												{
//													code: 'All',
//													description: 'All'
//												}
//											]
//										}
//									}
//                            })]
//                       },
                       {
                            dock: 'top',
                            xtype: 'toolbar',
                            items: [
                            {
                                text: 'Refresh',
                                scale: 'medium',
                                id: 'entryRefreshButton',
                                iconCls: 'fa fa-2x fa-refresh',
                                handler: function () {
                                   entryRefreshGrid();
                                },
                                tooltip: 'Refresh the list of records'
                            }, 
                            {
                                text: 'Export',
                                id: 'entryExportButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-download',
                                disabled: false,
                                handler: function () {
                                   entryExport();         
                                },
                                tooltip:'Export data and download to .csv format'

                            },
                            {
                                xtype: 'tbfill'
                            }]
                        },
                        { 
                            xtype: 'pagingtoolbar',
                            dock: 'bottom',
                            store: 'entryTrackingGridStore',
                            displayInfo: true   
                        }
                    ],
                    listeners: {
                        itemdblclick: function (grid, record, item, index, e, opts) {
                            console.log("Double Clicked Row:"+index);
                        },
                        selectionchange: function (grid, record, index, opts) {
                           //entryCheckNavButtons(); 
                        }
                    }
                });
                
                
                //
                //   Entry Tracking FUNCTIONS
                //
                

                //
                //  Refresh and reload the grid
                //
                var entryRefreshGrid = function(){ 
                     Ext.getCmp('entryTrackingGrid').getStore().load({
//						params: {
//							status: Ext.getCmp('entryTrackingFilter-ActiveStatus').getValue() ? Ext.getCmp('entryTrackingFilter-ActiveStatus').getValue() : '',
//						    all: (Ext.getCmp('entryTrackingFilter-ActiveStatus').getValue() === 'All')
//                             
//                        }
					});
                };
                

                //
                // Entry Export
                //
                var  entryExport = function (){
                    Ext.toast('Exporting Entry Tracking Data ...');
                    Ext.Ajax.request({
                        url: '../api/v1/resource/componenttracking/export',
                        method: 'GET',
                        success: function (response, opts) {
                            
                            downloadFile('entryTracking.csv', response.responseText );
                        }
                    });
                };
                
                
                 
                //
                //Article Tracking Tab-------------->
                //
                
                //
                // Article Tracking Store
                //
                
                
                var articleTrackingGridStore = Ext.create('Ext.data.Store', {
                    id: 'articleTrackingGridStore',
                    autoLoad: false,
                    pageSize: 100,
                    remoteSort: true,
                    sorters: [
                                new Ext.util.Sorter({
                                     id: 'entrySorter',
                                     property: 'name',
                                     direction: 'DESC'
                                })
                            ],
                    fields: [
                                
                                {name: 'componentId', mapping: function (data) {
                                        return data.data.componentId;
                                        
                                    }},
                                {name: 'name', mapping: function (data) {
                                        console.log("Entry Data:", data);
                                        return data.article.title;
                                        
                                    }},
                                {name: 'attributeType', mapping: function (data) {
                                        
                                        return data.data.attributeType;
                                        
                                    }},
                                {name: 'attributeCode', mapping: function (data) {
                                        
                                        return data.data.attributeCode;
                                        
                                    }},
                                {name: 'description', mapping: function (data) {
                                        return data.description;
                                    }},
                                
                                {name: 'componentType', mapping: function (data) {
                                        return data.componentType;
                                    }},
                                {name: 'eventType', mapping: function (data) {
                                        return data.componentType;
                                    }},
                                
                                {name: 'eventDts', mapping: function (data) {
                                        return data.data.eventDts;
                                    }},
                                {name: 'trackingId', mapping: function (data) {
                                        return data.trackingId;
                                    }},
                                {name: 'trackEventTypeCode', mapping: function (data) {
                                        if(data.data.trackEventTypeCode === 'SYNC'){
                                            return 'Component Sync';
                                        }
                                        else if(data.data.trackEventTypeCode === 'ELC'){
                                            return 'External Link Click';
                                        }
                                        else if(data.data.trackEventTypeCode === 'L'){
                                            return 'Login';
                                        }
                                        else if(data.data.trackEventTypeCode === 'V'){
                                            return 'View';
                                        }
                                        else{
                                            return data.data.trackEventTypeCode;
                                        }
                                    }},
                                
                                {name: 'activeStatus', mapping: function (data) {
                                        return data.data.activeStatus;
                                    }},
                                {name: 'updateDts', mapping: function (data) {
                                        return data.data.updateDts;
                                    }},
                                {name: 'updateUser', mapping: function (data) {
                                        return data.data.updateUser;
                                    }},
                                {name: 'createDts', mapping: function (data) {
                                        return data.data.createDts;
                                    }},
                                {name: 'createUser', mapping: function (data) {
                                        return data.data.createUser;
                                    }},
                                {name: 'attributes', mapping: function (data) {
                                        return data.attributes;
                                    }},
                                {name: 'clientIp', mapping: function (data) {
                                        return data.data.clientIp;
                                    }} 
                            ],
                    proxy: CoreUtil.pagingProxy({
                                url: '../api/v1/resource/articletracking',
                                reader: {
                                    type: 'json',
                                    rootProperty: 'result',
                                    totalProperty: 'count'
                                }
                            })
                });

                var articleTrackingGrid = Ext.create('Ext.grid.Panel', {
                    title: '',
                    id: 'articleTrackingGrid',
                    store: articleTrackingGridStore,
                    columnLines: true,
                    bodyCls: 'border_accent',
                    selModel: {
                        selType: 'checkboxmodel'
                    },
                    plugins: 'gridfilters',
                    enableLocking: true,
                    columns: [
                        {text: 'Name', dataIndex: 'name', width: 125, flex:1, lockable: true,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Attribute Type', dataIndex: 'attributeType', width: 150,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Attribute Code', dataIndex: 'attributeCode', width: 150,
                            filter: {
                                type: 'string'
                            }
                        },
                        
                        {text: 'Event Date', dataIndex: 'eventDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        {text: 'Event Type', dataIndex: 'trackEventTypeCode', width: 150},
                        {text: 'Client IP', dataIndex: 'clientIp', width: 150},
                        {text: 'User', dataIndex: 'createUser', width: 150,
                            filter: {
                                type:'string'
                            }
                        }
                    ],
                    dockedItems: [
//                        {
//                            dock: 'top',
//                            xtype: 'toolbar',
//                            items: [
//                                  Ext.create('OSF.component.StandardComboBox', {
//                                            id: 'articleTrackingFilter-ActiveStatus',
//                                            emptyText: 'All',
//                                            fieldLabel: 'Active Status',
//                                            name: 'activeStatus',	
//                                            listeners: {
//                                                change: function(filter, newValue, oldValue, opts){
//                                                    entryRefreshGrid();
//                                                }
//                                            },
//                                            storeConfig: {
//										customStore: {
//											fields: [
//												'code',
//												'description'
//											],
//											data: [												
//												{
//													code: 'A',
//													description: 'Active'
//												},
//												{
//													code: 'I',
//													description: 'Inactive'
//												},
//												{
//													code: 'All',
//													description: 'All'
//												}
//											]
//										}
//									}
//                            })]
//                       },
                       {
                            dock: 'top',
                            xtype: 'toolbar',
                            items: [
                            {
                                text: 'Refresh',
                                scale: 'medium',
                                id: 'articleRefreshButton',
                                iconCls: 'fa fa-2x fa-refresh',
                                handler: function () {
                                   articleRefreshGrid();
                                },
                                tooltip: 'Refresh the list of records'
                            }, 
                            {
                                text: 'Export',
                                id: 'artcileExportButton',
                                scale: 'medium',
                                iconCls: 'fa fa-2x fa-download',
                                disabled: false,
                                handler: function () {
                                   articleExport();         
                                },
                                tooltip:'Export data and download to .csv format'

                            },
                            {
                                xtype: 'tbfill'
                            }]
                        },
                        { 
                            xtype: 'pagingtoolbar',
                            dock: 'bottom',
                            store: 'articleTrackingGridStore',
                            displayInfo: true   
                        }
                    ],
                    listeners: {
                        itemdblclick: function (grid, record, item, index, e, opts) {
                            console.log("Double Clicked Row:"+index);
                        },
                        selectionchange: function (grid, record, index, opts) {
                           //articleCheckNavButtons(); 
                        }
                    }
                });
                
                
                //
                //   Article Tracking FUNCTIONS
                //
                
                
                //
                //  Refresh and reload the grid
                //
                var articleRefreshGrid = function(){ 
                     Ext.getCmp('articleTrackingGrid').getStore().load({
//						params: {
//							status: Ext.getCmp('articleTrackingFilter-ActiveStatus').getValue() ? Ext.getCmp('articleTrackingFilter-ActiveStatus').getValue() : '',
//						    all: (Ext.getCmp('articleTrackingFilter-ActiveStatus').getValue() === 'All')
//                             
//                        }
					});
                };

                //
                // Entry Export
                //
                var  articleExport = function (){
                    Ext.toast('Exporting Article Tracking Data ...');
                    Ext.Ajax.request({
                        url: '../api/v1/resource/articletracking/export',
                        method: 'GET',
                        success: function (response, opts) {
                            
                            downloadFile('articleTracking.csv', response.responseText );
                        }
                    });
                };
                
                
                
                var userPanel = Ext.create('Ext.panel.Panel', {
                    title: 'User',
                    iconCls: 'fa fa-user',
                    layout: 'fit',
                    items: [
                             userTrackingGrid
                           ]
                });
                
                var entryPanel = Ext.create('Ext.panel.Panel', {
                    title: 'Entry',
                    iconCls: 'fa fa-book',
                    layout: 'fit',
                    items: [
                             entryTrackingGrid
                           ]
                });
                
                var articlePanel = Ext.create('Ext.panel.Panel', {
                    title: 'Article',
                    iconCls: 'fa fa-file-text-o',
                    layout: 'fit',
                    items: [
                             articleTrackingGrid
                           ]
                });
                
                
                var msgTabPanel = Ext.create('Ext.tab.Panel', {
                    title:'Manage Tracking <i class="fa fa-question-circle"  data-qtip="Track user, entry, and article data." ></i>',
                    layout: 'fit',
                    items: [
                        userPanel,
                        entryPanel,
                        articlePanel,
                    ],
                    listeners: {
                        tabchange: function(tabPanel, newTab, oldTab, index){
                            
                            if(newTab.title === 'User'){
                               userRefreshGrid(); 
                            }
                            else if(newTab.title === 'Entry'){
                               entryRefreshGrid();
                            }
                            else if(newTab.title === 'Article'){
                               articleRefreshGrid();
                            }
                        }
                    }
                });
                
                Ext.create('Ext.container.Viewport', {
                    layout: 'fit',
                    items: [
                        msgTabPanel
                    ]
                });
        });

        </script>

    </stripes:layout-component>
</stripes:layout-render>
