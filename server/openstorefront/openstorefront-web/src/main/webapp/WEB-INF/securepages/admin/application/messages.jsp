<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">

        <script type="text/javascript">
            /* global Ext, CoreUtil */
            
            Ext.onReady(function () {

                
                //MAIN GRID -------------->
                
                var userMessageGridStore = Ext.create('Ext.data.Store', {
                    autoLoad: true,
                    pageSize: 100,
                    remoteSort: true,
                    sorters: [
                                new Ext.util.Sorter({
                                     property: 'username',
                                     direction: 'DESC'
                                })
                            ],
                    fields: [
                                {name: 'username', mapping: function (data) {
                                        console.log("Data email",data);
                                        return data.username;
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
                                {name: 'userMessageId', mapping: function (data) {
                                        return data.userMessageId;
                                    }},
                                {name: 'userMessageType', mapping: function (data) {
                                        return data.userMessageType;
                                    }},
                                {name: 'emailAddress', mapping: function (data) {
                                        return data.emailAddress;
                                    }},
                                {name: 'componentId', mapping: function (data) {
                                        return data.componentId;
                                    }},
                                {name: 'alertId', mapping: function (data) {
                                        return data.alertId;
                                    }},
                                {name: 'updateDts', mapping: function (data) {
                                        return data.updateDts;
                                    }},
                                {name: 'bodyOfMessage', mapping: function (data) {
                                        return data.bodyOfMessage;
                                    }},
                                {name: 'subject', mapping: function (data) {
                                        return data.subject;
                                    }},
                                {name: 'sentEmailAddress', mapping: function (data) {
                                        return data.sentEmailAddress;
                                    }},
                                {name: 'retryCount', mapping: function (data) {
                                        return data.retryCount;
                                    }},
                                {name: 'updateUser', mapping: function (data) {
                                        return data.updateUser;
                                    }},
                                {name: 'createDts', mapping: function (data) {
                                        return data.createDts;
                                    }},
                                {name: 'createUser', mapping: function (data) {
                                        return data.createUser;
                                    }}
                            ],
                    proxy: CoreUtil.pagingProxy({
                                url: '../api/v1/resource/usermessages',
                                reader: {
                                    type: 'json',
                                    rootProperty: 'data',
                                    totalProperty: 'totalNumber'
                                }
                            })
                });

                var userMessageGrid = Ext.create('Ext.grid.Panel', {
                    title: '',
                    id: 'userMessageGrid',
                    store: userMessageGridStore,
                    columnLines: true,
                    bodyCls: 'border_accent',
                    selModel: {
                        selType: 'checkboxmodel'
                    },
                    plugins: 'gridfilters',
                    enableLocking: true,
                    columns: [
                        {text: 'Username / Email', dataIndex: 'username', width: 200, flex:1, lockable: true,
                            filter: {
                                type: 'string'
                            },
                            renderer: function(val, meta, record, rowIndex) {
                                if(typeof val === "undefined"){
                                    if(record.get('emailAddress')){
                                        return record.get('emailAddress');
                                    }
                                    else{
                                        return null;
                                    }
                                }
                                return val;
                            }
                        },
                        {text: 'Message Type', align: 'center', dataIndex: 'userMessageType', width: 125,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Component Id / Alert Id', dataIndex: 'alertId', width: 200,
                            filter: {
                                type: 'string'
                            },
                            renderer: function(val, meta, record, rowIndex) {
                                console.log("Val",val);
                                if(typeof val === "undefined"){
                                    if(record.get('componentId')){
                                        return record.get('componentId');
                                    }
                                    else {
                                        return null;
                                    }
                                }
                                return val;
                            }
                        },
                        {text: 'Status', dataIndex: 'activeStatus', width: 75,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Update Date', dataIndex: 'updateDts', width: 125, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        {text: 'Subject', dataIndex: 'subject', width: 150},
                        {text: 'Sent Email Address', dataIndex: 'sentEmailAddress', width: 200, flex:1}
                    ],
                    dockedItems: [
                        {
                            dock: 'top',
                            xtype: 'toolbar',
                            items: [
                                {
                                    text: 'Refresh',
                                    scale: 'medium',
                                    id: 'refreshButton',
                                    iconCls: 'fa fa-2x fa-refresh',
                                    handler: function () {
                                       refreshGrid();
                                    }
                                },
                                {
                                    text: 'View',
                                    id: 'mViewButton',
                                    scale: 'medium',
                                    iconCls: 'fa fa-2x fa-eye',
                                    disabled: true,
                                    handler: function () {
                                        viewEntry();
                                    }
                                },
                                {
                                    text: 'Process Now',
                                    scale: 'medium',
                                    id: 'mProcessButton',
                                    iconCls: 'fa fa-2x fa-bolt',
                                    handler: function () {
                                      processAll();
                                    }
                                },
                                {
                                    text: 'Cleanup Now',
                                    id: 'mCleanUpButton',
                                    scale: 'medium',
                                    iconCls: 'fa fa-2x fa-eraser',
                                    disabled: true,
                                    handler: function () {
                                       cleanupEntry();         
                                    }
                                },
                                {
                                    text: 'Delete',
                                    id: 'mDeleteButton',
                                    cls: 'alert-danger',
                                    scale: 'medium',
                                    iconCls: 'fa fa-2x fa-trash',
                                    disabled: true,
                                    handler: function () {
                                        deleteEntry();  
                                    }
                                },
                                {
                                    text: 'Filter Options',
                                    scale: 'medium',
                                    id: 'mFilterButton',
                                    iconCls: 'fa fa-2x fa-filter',
                                    handler: function () {
                                       filterAll(0);
                                    }
                                },
                                
                                {
                                    xtype: 'tbfill'
                                }
                            ]
                        }
                    ],
                    listeners: {
                        itemdblclick: function (grid, record, item, index, e, opts) {
                            console.log("Double Clicked Row:"+index);
                        },
                        selectionchange: function (grid, record, index, opts) {
                           console.log("Check Buttons");
                        }
                    }
                });
                
                
                //
                //  Notifications
                //
                var notificationsGridStore = Ext.create('Ext.data.Store', {
                    autoLoad: false,
                    pageSize: 300,
                    remoteSort: true,
                    sorters: [
                                new Ext.util.Sorter({
                                     property: 'name',
                                     direction: 'DESC'
                                })
                            ],
                    fields: [
                                {name: 'name', mapping: function (data) {
                                        return data.name;
                                    }},
                                {name: 'applicationName', mapping: function (data) {
                                        return data.applicationName;
                                    }},
                                {name: 'description', mapping: function (data) {
                                        return data.description;
                                    }},
                                {name: 'activeStatus', mapping: function (data) {
                                        return data.activeStatus;
                                    }},
                                {name: 'landingPageTitle', mapping: function (data) {
                                        return data.landingPageTitle;
                                    }},
                                {name: 'Update Date', mapping: function (data) {
                                        return data.updateDts;
                                    }},
                                {name: 'Update User', mapping: function (data) {
                                        return data.updateUser;
                                    }},
                                {name: 'CreateDate', mapping: function (data) {
                                        return data.createDts;
                                    }},
                                {name: 'Create User', mapping: function (data) {
                                        return data.createUser;
                                    }}
                            ],
                    proxy: CoreUtil.pagingProxy({
                                url: '../api/v1/resource/branding/',
                                reader: {
                                    type: 'json',
                                    rootProperty: 'components',
                                    totalProperty: 'totalNumber'
                                }
                            })
                        });

                var notificationsGrid = Ext.create('Ext.grid.Panel', {
                    title: '',
                    id: 'notificationGrid',
                    store: notificationsGridStore,
                    columnLines: true,
                    bodyCls: 'border_accent',
                    selModel: {
                        selType: 'checkboxmodel'
                    },
                    viewConfig:{
                      getRowClass: function (record, index){
                          if(record.get('activeStatus')==='A'){
                              return 'activecell';
                          }
                          else
                          {
                              return null;
                          }
                      }  
                    },
                    plugins: 'gridfilters',
                    enableLocking: true,
                    columns: [
                        {text: 'Username', dataIndex: 'username', width: 150,  lockable: true,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Message', dataIndex: 'messate', width: 200, flex:1},
                        {text: 'Event Type', dataIndex: 'eventTypeDescription', width: 150,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Entity', dataIndex: 'entityName', width: 150,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        
                    ],
                    dockedItems: [
                        {
                            dock: 'top',
                            xtype: 'toolbar',
                            items: [
                                {
                                    text: 'Refresh',
                                    scale: 'medium',
                                    id: 'brandingRefreshButton',
                                    iconCls: 'fa fa-2x fa-refresh',
                                    handler: function () {
                                       refreshGrid();
                                    }
                                },
                                {
                                    text: 'Admin Message',
                                    scale: 'medium',
                                    id: 'nProcessButton',
                                    iconCls: 'fa fa-2x fa-plus',
                                    handler: function () {
                                      adminMessage();
                                    }
                                },
                                {
                                    text: 'Delete',
                                    id: 'nDeleteButton',
                                    cls: 'alert-danger',
                                    scale: 'medium',
                                    iconCls: 'fa fa-2x fa-trash',
                                    disabled: true,
                                    handler: function () {
                                        deleteEntry();  
                                    }
                                },
                                {
                                    text: 'Filter Options',
                                    scale: 'medium',
                                    id: 'nFilterButton',
                                    iconCls: 'fa fa-2x fa-filter',
                                    handler: function () {
                                       filterAll(0);
                                    }
                                },
                                
                                {
                                    xtype: 'tbfill'
                                }
                            ]
                        }
                    ],
                    listeners: {
                        itemdblclick: function (grid, record, item, index, e, opts) {
                            console.log("Double Clicked Row:"+index);
                        },
                        selectionchange: function (grid, record, index, opts) {
                          console.log("Check Buttons");
                        }
                    }
                });
                
                var messagePanel = Ext.create('Ext.panel.Panel', {
                    title: 'User Messages',
                    iconCls: 'fa fa-user',
                    layout: 'fit',
                    items: [
                             userMessageGrid
                           ]
                });
                
                var notificationsPanel = Ext.create('Ext.panel.Panel', {
                    title: 'Event Notifications',
                    iconCls: 'fa fa-bell-o',
                    layout: 'fit',
                    items: [
                             notificationsGrid
                           ]
                });
                
                
                var msgTabPanel = Ext.create('Ext.tab.Panel', {
                    title:'Manage Messages <i class="fa fa-question-circle"  data-qtip="User messages are queued messages from users. The primary usage for messages is from watches. This tool allows for viewing of queued messages as well as viewing of archived messages. Event Notifications are messages sent internally to user to notify them of event in the application." ></i>',
                    layout: 'fit',
                    items: [
                        messagePanel,
                        notificationsPanel
                    ]
                });
                
                Ext.create('Ext.container.Viewport', {
                    layout: 'fit',
                    items: [
                        msgTabPanel
                    ]
                });
                
                //
                //  Refresh and reload the grid
                //
                var refreshGrid = function(){
                    
                     Ext.getCmp('userMessageGrid').getStore().load();
                };
                
                //
                // Check which buttons should be on and which should be off
                //
                var checkNavButtons = function() {
                    
                    if (userMessageGrid.getSelectionModel().getCount() === 1) {
                        Ext.getCmp('brandingEditButton').setDisabled(false);
                        Ext.getCmp('brandingDuplicateButton').setDisabled(false);
                        Ext.getCmp('brandingDeleteButton').setDisabled(false);
                        
                        var currentStatus = Ext.getCmp('brandingGrid').getSelection()[0].get('activeStatus');
                        if(currentStatus!=='A'){
                             Ext.getCmp('brandingActivateButton').setDisabled(false);
                        }   
                    } else if (userMessageGrid.getSelectionModel().getCount() > 1) {
                        Ext.getCmp('brandingDeleteButton').setDisabled(false);
                        Ext.getCmp('brandingEditButton').setDisabled(true);
                        Ext.getCmp('brandingDuplicateButton').setDisabled(true);
                        Ext.getCmp('brandingActivateButton').setDisabled(true);
                        
                    } else {
                        Ext.getCmp('brandingEditButton').setDisabled(true);
                        Ext.getCmp('brandingDuplicateButton').setDisabled(true);
                        Ext.getCmp('brandingDeleteButton').setDisabled(true);
                        Ext.getCmp('brandingActivateButton').setDisabled(true);
                    }
                };
                
                
                //
                //  Delete a message
                //
                var deleteMessage = function() {
                    
                };
                
                //
                // View Message
                //
                var  viewMessage = function (whichone){
                    
                    
                };
        });

        </script>

    </stripes:layout-component>
</stripes:layout-render>
