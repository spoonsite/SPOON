<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">

        <script type="text/javascript">
            /* global Ext, CoreUtil */
            
            Ext.onReady(function () {

                var brandingObj={};
                 
                var brandingConfigForm = Ext.create('Ext.form.Panel',{
                    id: 'brandingConfigForm',
                    title: 'Site Configuration  <i class="fa fa-question-circle"  data-qtip="These are the sites configuration settings." ></i>',
                    layout: 'vbox',
                    scrollable: true,
                    bodyStyle: 'padding: 10px;',
                    defaults: {
                        labelAlign: 'top'
                    },
                    handler: function(){
                        console.log("Test");
                    },
                    items: [
                            {
                                xtype: 'textfield',
                                id: 'name',
                                fieldLabel: 'Name<span class="field-required" />',
                                name: 'name',
                                width: '100%',
                                allowBlank: false,
                                maxLength: 50									
                            },
                            {
                                xtype: 'textfield',									
                                fieldLabel: 'Application Name<span class="field-required" />',
                                name: 'applicationName',
                                allowBlank: false,
                                width: '100%',
                                maxLength: 50																		
                            },
                            {
                                xtype: 'textfield',									
                                fieldLabel: 'Primary Logo Url',
                                name: 'primaryLogoUrl',
                                allowBlank: true,
                                width: '100%',
                                maxLength: 255																		
                            },
                            {
                                xtype: 'textfield',									
                                fieldLabel: 'Secondary Logo Url',
                                name: 'secondaryLogoUrl',
                                allowBlank: true,
                                width: '100%',
                                maxLength: 255																		
                            },
                            {
                                xtype: 'textfield',									
                                fieldLabel: 'Architecture Search Label',
                                name: 'architectureSearchLabel',
                                allowBlank: true,
                                width: '100%',
                                maxLength: 50																		
                            },
                            {
                                xtype: 'combobox',									
                                fieldLabel: 'Architecture Search Type',
                                name: 'architectureSearchType',
                                store: null,
                                width: '100%'																		
                            },
                            {
                                xtype: 'htmleditor',
                                name: 'loginWarning',
                                fieldLabel: 'Login Warning',
                                allowBlank: true,
                                width: '100%',
                                fieldBodyCls: 'form-comp-htmleditor-border',
                                maxLength: 255

                            },
                            {
                                xtype: 'htmleditor',
                                name: 'landingPageTitle',
                                fieldLabel: 'Landing Page Title',
                                allowBlank: true,
                                width: '100%',
                                fieldBodyCls: 'form-comp-htmleditor-border',
                                maxLength: 255,
                                style:{
                                    marginTop:'30px'
                                }
                            },
                            {
                                xtype: 'htmleditor',
                                name: 'landingPageBanner',
                                fieldLabel: 'Landing Page Banner',
                                allowBlank: true,
                                width: '100%',
                                fieldBodyCls: 'form-comp-htmleditor-border',
                                maxLength: 4000,
                                style:{
                                    marginTop:'30px'
                                }
                            },
                            {
                                xtype: 'htmleditor',
                                name: 'landingPageFooter',
                                fieldLabel: 'Landing Page Footer',
                                allowBlank: true,
                                width: '100%',
                                fieldBodyCls: 'form-comp-htmleditor-border',
                                maxLength: 60000,
                                style:{
                                    marginTop:'30px'
                                }
                            },
                            {
                                xtype: 'checkbox',
                                name: 'allowJiraFeedback',
                                boxLabel: 'Allow JIRA Feedback',
                                width: '100%',
                                style:{
                                    marginTop:'30px'
                                },
                                selected:false
                            },
                            {
                                xtype: 'checkbox',
                                name: 'showComponentTypeSearchFlg',
                                boxLabel: 'Show Component Type Search',
                                width: '100%',
                                style:{
                                    marginTop:'10px'
                                },
                                selected:false
                            }    
                    ]

                });
                
                
                
                
                
                var brandingConfigPanel = Ext.create('Ext.panel.Panel', {
                    title: 'Config',
                    iconCls: 'fa fa-list-alt',
                    layout: 'fit',
                    items: [ 
                        brandingConfigForm
                        
                    ]    
                });
                
                //
                // Branding Configuration Colors Panel
                //
                var brandingColorsForm =  Ext.create('Ext.form.Panel',{
                    title:'Site Colors <i class="fa fa-question-circle"  data-qtip="These are the site main colors." ></i>',
                    id: 'brandingColorsForm',
                    layout: 'vbox',
                    scrollable: true,
                    bodyStyle: 'padding: 10px;',
                    defaults: {
                        labelAlign: 'top'
                    },
                    items: [
                            {
                                xtype: 'colorbutton',
                                name: 'lookTextColorPicker',
                                width:25,
                                height:25,
                                bind:'{lookTextcolor}',
                                listeners:{
                                    change: function(picker, selColor){
                                        //console.log("Changed Color");

                                    }
                                }
                            },
                            {
                                xtype: 'label',
                                name: 'lookTextColor',
                                id: 'lookTextColor',
                                bind:'{lookTextColor}',
                                width:100,
                                height:25

                            }

                    ]           

                });
                
                
                var brandingColorsPanel = Ext.create('Ext.panel.Panel', {
                    title: 'Colors',
                    iconCls: 'fa fa-paint-brush',
                    layout: 'fit',
                    
                    items: [
                             brandingColorsForm
                           ]
                });
                
                //
                // Category Search Configuration Panel
                //
                var brandingFullCategoryStore = Ext.create('Ext.data.Store', {
                    autoLoad: true,
                    remoteSort: true,
                    sorters: [
                                new Ext.util.Sorter({
                                     property: 'description',
                                     direction: 'ASC'
                                })
                            ],
                    fields: [
                                {name: 'description', mapping: function (data) {
                                        return data.description;
                                    }},
                                {name: 'attributeType', mapping: function (data) {
                                        return data.attributeType;
                                    }}
                               
                            ],
                    proxy: CoreUtil.pagingProxy({
                                url: '../api/v1/resource/attributes/attributetypes',
                                method: 'GET',
                                reader: {
                                    type: 'json',
                                    rootProperty: 'data',
                                    totalProperty: 'totalNumber'
                                }
                            })
                });
           
                var brandingCategoryGrid = Ext.create('Ext.grid.Panel',{
                   title: 'Category Selections <i class="fa fa-question-circle"  data-qtip="These are the searchable categories for this site. They are used with the category search tool." ></i>',
                    id: 'brandingCategoryGrid',
                    store: brandingFullCategoryStore,
                    columnLines: true,
                    bodyCls: 'border_accent',
                    selModel: {
                        selType: 'checkboxmodel'
                    },
                    plugins: 'gridfilters',
                    enableLocking: true,
                    columns: [
                        {text: 'Name', dataIndex: 'description', width: 200, flex:1, lockable: true,
                            filter: {
                                type: 'string'
                            }
                        }
                        
                    ]
                   
                });
                
                
                
                var brandingCategoryPanel = Ext.create('Ext.panel.Panel', {
                    title: 'Categories',
                    iconCls: 'fa fa-list-ul',
                    layout: 'fit',
                    items: [
                             brandingCategoryGrid
                           ]
                });
                
                //
                //  tabPanel
                //  This is the panel to hold all the other tab panels
                //
                var brandingTabPanel = Ext.create('Ext.tab.Panel', {
                    title: '',
                    items: [
                        brandingConfigPanel,
                        brandingColorsPanel,
                        brandingCategoryPanel
                    ]

                });
                
                // Add Edit Branding Window
                var brandingAddEditWin = Ext.create('Ext.window.Window', {
                    id: 'brandingAddEditWin',
		    title: 'Branding',
		    modal: true,
                    y: 40,
		    width: '65%',
                    height: '65%',
                    layout:'fit',
                    hidden: true,
                    
                    dockedItems: [
                        {
                            dock: 'bottom',
                            xtype: 'toolbar',
                            items: [
                                {
                                    text: 'Save',
                                    formBind: true,
                                    iconCls: 'fa fa-save',
                                    handler: function() {
//                                        var method = edit ? 'PUT' : 'POST'; 
//                                        var url = edit ? '../api/v1/resource/lookuptypes/' + selectedTable.get('code') + '/' + Ext.getCmp('editCodeForm-codeField').getValue() : '../api/v1/resource/lookuptypes/' + selectedTable.get('code');       
//                                        var data = Ext.getCmp('editCodeForm').getValues();
//
//                                        CoreUtil.submitForm({
//                                            url: url,
//                                            method: method,
//                                            data: data,
//                                            removeBlankDataItems: true,
//                                            form: Ext.getCmp('editCodeForm'),
//                                            success: function(response, opts) {
//                                                    Ext.toast('Saved Successfully', '', 'tr');
//                                                    Ext.getCmp('editCodeForm').setLoading(false);
//                                                    Ext.getCmp('editCodeFormWin').hide();													
//                                                    actionLoadCodes(Ext.getCmp('editCodeFilterStatus').getValue());													
//                                            }
//                                        });
                                    }
                                },
                                {
                                    xtype: 'tbfill'
                                },
                                {
                                    text: 'Cancel',
                                    iconCls: 'fa fa-close',
                                    handler: function() {
                                            Ext.getCmp('brandingAddEditWin').hide();
                                           
                                    }
                                }							
                            ]
                        }
                    ]
                    		
                });
                
                brandingAddEditWin.add(brandingTabPanel);

                //MAIN GRID -------------->
                
                var brandingGridStore = Ext.create('Ext.data.Store', {
                    autoLoad: true,
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

                var brandingGrid = Ext.create('Ext.grid.Panel', {
                    title: 'Manage Branding <i class="fa fa-question-circle"  data-qtip="This tool allows the ability to set the graphic design and theme characteristics for the site." ></i>',
                    id: 'brandingGrid',
                    store: brandingGridStore,
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
                        {text: 'Name', dataIndex: 'name', width: 200, flex:1, lockable: true,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Application Name', align: 'center', dataIndex: 'applicationName', flex:1, width: 125,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Status', dataIndex: 'activeStatus', width: 150,
                            filter: {
                                type: 'string'
                            }
                        },
                        {text: 'Update Date', dataIndex: 'updateDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        {text: 'Update User', dataIndex: 'updateUser', width: 150},
                        {text: 'Create Date', dataIndex: 'createDts', width: 150, xtype: 'datecolumn', format: 'm/d/y H:i:s'},
                        {text: 'Create User', dataIndex: 'createUser', width: 150}
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
                                    text: 'Add',
                                    scale: 'medium',
                                    id: 'brandingAddButton',
                                    iconCls: 'fa fa-2x fa-plus',
                                    handler: function () {
                                       addOrEditBrandingEntry(0);
                                    }
                                },
                                {
                                    text: 'Edit',
                                    id: 'brandingEditButton',
                                    scale: 'medium',
                                    iconCls: 'fa fa-2x fa-edit',
                                    disabled: true,
                                    handler: function () {
                                        addOrEditBrandingEntry(1);
                                    }
                                },
                                {
                                    text: 'Duplicate',
                                    id: 'brandingDuplicateButton',
                                    scale: 'medium',
                                    iconCls: 'fa fa-2x fa-clone',
                                    disabled: true,
                                    handler: function () {
                                        duplicateBrandingEntry();
                                    }
                                },
                                {
                                    text: 'Activate',
                                    id: 'brandingActivateButton',
                                    scale: 'medium',
                                    iconCls: 'fa fa-2x fa-power-off',
                                    disabled: true,
                                    handler: function () {
                                       activateBrandingEntry();         
                                    }
                                },
                                {
                                    text: 'Delete',
                                    id: 'brandingDeleteButton',
                                    cls: 'alert-danger',
                                    scale: 'medium',
                                    iconCls: 'fa fa-2x fa-trash',
                                    disabled: true,
                                    handler: function () {
                                        deleteBrandingEntry();  
                                    }
                                },
                                
                                {
                                    xtype: 'tbfill'
                                }
                            ]
                        }
                    ],                    listeners: {
                        itemdblclick: function (grid, record, item, index, e, opts) {
                            console.log("Double Clicked Row:"+index);
                        },
                        selectionchange: function (grid, record, index, opts) {
                          checkBrandingNavButtons();
                        }
                    }

                });

                Ext.create('Ext.container.Viewport', {
                    layout: 'fit',
                    items: [
                        brandingGrid
                    ]
                });
                
                //
                //  Refresh and reload the grid
                //
                var refreshGrid = function(){
                    
                     Ext.getCmp('brandingGrid').getStore().load();
                };
                
                //
                // Check which buttons should be on and which should be off
                //
                var checkBrandingNavButtons = function() {
                    
                    if (brandingGrid.getSelectionModel().getCount() === 1) {
                        Ext.getCmp('brandingEditButton').setDisabled(false);
                        Ext.getCmp('brandingDuplicateButton').setDisabled(false);
                        Ext.getCmp('brandingDeleteButton').setDisabled(false);
                        
                        var currentStatus = Ext.getCmp('brandingGrid').getSelection()[0].get('activeStatus');
                        if(currentStatus!=='A'){
                             Ext.getCmp('brandingActivateButton').setDisabled(false);
                        }   
                    } else if (brandingGrid.getSelectionModel().getCount() > 1) {
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
                // Duplicate a branding entry obj
                //
                var duplicateBrandingEntry = function(){
                    
                    var cloneObj = {};
                    var selectedObj = Ext.getCmp('brandingGrid').getSelection()[0];
                    console.log("selectedObj",selectedObj);
                    
                    
                    Ext.getCmp('brandingGrid').setLoading(true);
                    Ext.Ajax.request({
                        
                        url: '../api/v1/resource/branding/'+ selectedObj.data.brandingId,
                        method: 'GET',
                        
                        success: function(response, opts){
                                
                                cloneObj = Ext.decode(response.responseText);
                                console.log("cloneObj",cloneObj);
                                cloneObj.branding.name+='_copy';
                                cloneObj.branding.brandingId=null;
                                
                                Ext.Ajax.request({

                                    url: '../api/v1/resource/branding/',
                                    method: 'POST',
                                    jsonData: cloneObj,

                                    success: function(response, opts){
                                            console.log("Success:",Ext.decode(response.responseText));
                                            Ext.getCmp('brandingGrid').setLoading(false);
                                            refreshGrid();
                                    },

                                    failure: function(response, opts){
                                            Ext.getCmp('brandingGrid').setLoading(false);
                                            console.log("Failed:",Ext.decode(response.responseText));
                                    }
                                });
                        },
                        
                        failure: function(response, opts){
                                Ext.getCmp('brandingGrid').setLoading(false);
                                console.log("Failed:",Ext.decode(response.responseText));
                        }
                    });
 
                };
                
                //
                //  Activate a branding entry to go live 
                //
                var activateBrandingEntry = function() {
                    
                    var selectedObj = Ext.getCmp('brandingGrid').getSelection()[0];
                    var currentStatus = selectedObj.data.activeStatus;
                    if (currentStatus === 'A') { //Already active
                        return;
                    }
                    
                    Ext.getCmp('brandingGrid').setLoading(true);
		    var brandingId = selectedObj.data.brandingId;
		    var method = 'PUT';
		    var urlEnd = '/active';
                    					
                    Ext.Ajax.request({
                        
                        url: '../api/v1/resource/branding/' + brandingId + urlEnd,
                        method: method,
                        success: function(response, opts){
                                Ext.getCmp('brandingGrid').setLoading(false);
                                refreshGrid();
                        },
                        failure: function(response, opts){
                                Ext.getCmp('brandingGrid').setLoading(false);
                        }
                    });
                };
                
                //
                //  Delete a branding entry
                //
                var deleteBrandingEntry = function() {
                    var selectedObj = Ext.getCmp('brandingGrid').getSelection()[0];
                    var brandingId = selectedObj.data.brandingId;
                    var name = selectedObj.data.name;
                    
                        Ext.Msg.show({
                                title: 'Delete Branding Entry?',
                                message: 'Are you sure you want to delete:  ' + name +' ?',
                                buttons: Ext.Msg.YESNO,
                                icon: Ext.Msg.QUESTION,
                                fn: function(btn) {
                                        if (btn === 'yes') {
                                            
                                            Ext.getCmp('brandingGrid').setLoading(true);
                                            Ext.Ajax.request({
                                                
                                                url: '..../api/v1/resource/branding/' + brandingId,
                                                method: 'DELETE',
                                                success: function(response, opts) {
                                                        Ext.getCmp('componentGrid').setLoading(false);
                                                        refeshGrid();
                                                },
                                                failure: function(response, opts) {
                                                        Ext.getCmp('componentGrid').setLoading(false);
                                                }
                                            });
                                        } 
                                }
                        });
                };
                
                //
                // Add or Edit Branding Entry
                //
                var  addOrEditBrandingEntry = function (whichone){
                     if(whichone===0)
                     {   
                         brandingObj = {};
                         brandingAddEditWin.show();
                        
                         //Add new entry
                         console.log("Add new Entry",brandingObj);
                     }
                     else
                     {
                         //Edit existing entry
                         brandingObj = Ext.getCmp('brandingGrid').getSelection()[0];
                         
                         brandingAddEditWin.show();
                        Ext.getCmp('brandingConfigForm').loadRecord(brandingObj);
                        // Ext.getCmp('brandingColorsForm').(brandingObj);
                         
                         
                         console.log("Edit entry",brandingObj);
                     }
                    
                };
        });

        </script>

    </stripes:layout-component>
</stripes:layout-render>