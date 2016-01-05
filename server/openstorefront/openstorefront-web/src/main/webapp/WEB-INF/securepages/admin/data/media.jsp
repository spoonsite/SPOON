<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function(){	
				
				var mediaGrid = Ext.create('Ext.grid.Panel', {
					id: 'mediaGrid',
					title: 'Manage Media <i class="fa fa-question-circle"  data-qtip="Media that can be used for articles and badges." ></i>',
					store: Ext.create('Ext.data.Store', {
						autoLoad: true,
						id: 'mediaGridStore',
						pageSize: 100,
						remoteSort: true,
						sorters: [
							new Ext.util.Sorter({
								property: 'name',
								direction: 'ASC'
							})
						],
						proxy: CoreUtil.pagingProxy({
							type: 'ajax',
							url: '../api/v1/resource/generalmedia',
							reader: {
								type: 'json',
								rootProperty: 'data',
								totalProperty: 'totalNumber'
							}
						})
					}),
					columnLines: true,
					columns: [						
						{ text: 'Name', dataIndex: 'name', minWidth: 200},
						{ text: 'Resource URL', dataIndex: 'mediaLink', flex: 1, minWidth: 200 },
						{ text: 'Original Filename', dataIndex: 'orignalFileName', width: 300 },
						{ text: 'Mime Type', dataIndex: 'mimeType', width: 150},
						{ text: 'Update User', dataIndex: 'updateUser', width: 150},
						{ text: 'Update Date', dataIndex: 'updateDts', width: 200, xtype: 'datecolumn', format: 'm/d/y H:i:s'}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-refresh',
									tooltip: 'Refresh Grid',
									handler: function () {
										refreshGrid();
									}
								},
								{
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
									tooltip: 'Upload media file',
									handler: function () {
										addRecord();
									}
								}, 								
								{
									text: 'Download',
									id: 'downloadButton',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-download',
									disabled: true,
									tooltip: 'Download media file',
									handler: function () {
										downloadRecord();
									}								
								},							
								{
									text: 'View',
									id: 'viewButton',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-eye',
									disabled: true,
									tooltip: 'View media file in a viewer',
									handler: function () {
										viewRecord();
									}								
								},
								{
									text: 'Copy URL',
									id: 'copyButton',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-clipboard',
									disabled: true,
									tooltip: 'Copy URL location of the media file',
									handler: function () {
										copyURL();
									}								
								},
								{	text: 'Delete',
									id: 'deleteButton',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-trash',
									disabled: true,
									tooltip: 'Delete media',
									handler: function () {
										deleteRecord();
								    }
								}
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							//console.log("double click");
							downloadRecord();
						},
						selectionchange: function(grid, record, index, opts){
							//console.log("change selection");
							checkButtonChanges();
						}
					}
				});
				
				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						mediaGrid
					]
				});
		
				var selectedObj=null;
				
				var checkButtonChanges = function() {
					var cnt = Ext.getCmp('mediaGrid').getSelectionModel().getCount();
					if ( cnt === 1) {
						Ext.getCmp('downloadButton').setDisabled(false);
						Ext.getCmp('viewButton').setDisabled(false);
						Ext.getCmp('copyButton').setDisabled(false);
						Ext.getCmp('deleteButton').setDisabled(false);
					} else {
						Ext.getCmp('downloadButton').setDisabled(true);
						Ext.getCmp('viewButton').setDisabled(true);
						Ext.getCmp('copyButton').setDisabled(true);
						Ext.getCmp('deleteButton').setDisabled(true);						
					}
				};
				
				var refreshGrid = function() {
					Ext.getCmp('mediaGrid').getStore().load();				
				};
				
				var addRecord = function() {
					addMediaWin.show();
					
					//reset form
					Ext.getCmp('addMediaForm').reset(true);
					
					
				};
				
				
				var downloadRecord = function() {
					selectedObj = Ext.getCmp('mediaGrid').getSelection()[0].data;
					//download media 
					window.location.href = '../' + selectedObj.mediaLink;
				};
				
				var viewRecord = function() {
				    selectedObj = Ext.getCmp('mediaGrid').getSelection()[0].data;
					viewMediaWin.html=null;
					viewMediaWin.setWidth(600);
					viewMediaWin.setHeight(450);
					viewMediaWin.setScrollable(true);
					//show display media based on mime type
					 if (selectedObj && selectedObj.mimeType){
						
						var type = selectedObj.mimeType;
					    if (type.match('video.*')) {
							viewMediaWin.setTitle('Video Preview');
							viewMediaWin.setWidth(600);
							viewMediaWin.setHeight(500);
					        viewMediaWin.update('<video autoplay="autoplay" controls="controls" src="../'+ selectedObj.mediaLink+'" width="100%" heigh="100%"></video>');
						}
						else if(type.match('audio.*')){
							viewMediaWin.setTitle('Audio Preview');
							viewMediaWin.setWidth(325);
							viewMediaWin.setHeight(85);
							viewMediaWin.setScrollable(false);
							viewMediaWin.update('<audio autoplay="autoplay" width="100%" controls="controls" src="../'+ selectedObj.mediaLink+'"/>');
						
							
						}
						else if(type.match('image.*')){
							
							viewMediaWin.setTitle('Image Preview');
					        viewMediaWin.update('<img src="../'+ selectedObj.mediaLink+'" width="100%"/>');
						}
						
						viewMediaWin.updateLayout();
						viewMediaWin.show();
						
					}
				};
				
				var copyURL = function(){
					selectedObj = Ext.getCmp('mediaGrid').getSelection()[0].data;
					
					var textArea = document.createElement("textArea");
					
					textArea.style.position = 'fixed';
					textArea.style.top = 0;
					textArea.style.left = 0;
					textArea.style.width = '2em';
					textArea.style.height = '2em';
					textArea.style.padding = 0;
					textArea.style.border = 'none';
					textArea.style.outline = 'none';
					textArea.style.boxShadow = 'none';
					textArea.style.background = 'transparent';


					textArea.value = location.protocol+'//'+location.host+location.pathname+'/../'+selectedObj.mediaLink;

					document.body.appendChild(textArea);

					textArea.select();
					try {
						var successful = document.execCommand('copy');
						var msg = successful ? 'successful' : 'unsuccessful';
						console.log('Copying text command was ' + msg);
					  } catch (err) {
						console.log('Oops, unable to copy');
					  }

					  document.body.removeChild(textArea);
					  Ext.toast('Link copied to clipboard.', '', 'tr');
				};
				
				var deleteRecord = function(){
					selectedObj = Ext.getCmp('mediaGrid').getSelection()[0].data;
				
					Ext.Msg.show({
						title: 'Delete Media?',
						message: 'Are you sure you want to delete the selected media?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function (btn) {
							if (btn === 'yes') {
								Ext.getCmp('mediaGrid').setLoading(true);
								Ext.Ajax.request({
									url: '../api/v1/resource/generalmedia/'+encodeURIComponent(selectedObj.name),
									method: 'DELETE',
									success: function (response, opts) {
										Ext.getCmp('mediaGrid').setLoading(false);
										refreshGrid();
									},
									failure: function (response, opts) {
										Ext.getCmp('mediaGrid').setLoading(false);
									}
								});
							}
						}
					});
				};
				
				
				
				//
				//
				//  View Media WINDOW
				//
				//
				var viewMediaWin = Ext.create('Ext.window.Window', {
					id: 'viewMediaWin',
					title: '',
					modal: true,
					style:'padding:5px;',
					maximizable: true,
					scrollable:true,
					html:'',
					listeners:{
						close: function(){
							console.log("Closed Window");
							var theWin= Ext.getCmp('viewMediaWin');
							theWin.update(null);
						}
					}
					});
				
				
				//
				//
				//  Add Media Window
				//
				//
				var addMediaWin = Ext.create('Ext.window.Window', {
					id: 'addMediaWin',
					title: 'Add Media',
					modal: true,
					width: '40%',
					y: 40,
					resizable: false,
					items: [
						{	xtype: 'form',
							id: 'addMediaForm',
							layout:'fit',
							items: [
								{
									xtype: 'panel',
									style: 'padding: 10px;',
									layout: 'vbox',
									defaults: {
										labelAlign: 'top'
									},
									items:[
										{
											xtype: 'textfield',
											id: 'name',
											name: 'name',
											fieldLabel: 'Enter a unique name for the media file<span class="field-required" />',
											style: 'padding-top: 5px;',
											allowBlank: false,
											width: '100%'
										},
										{
											xtype: 'filefield',
											name: 'file',
											id: 'file',
											fieldLabel: 'Upload Media<span class="field-required" />',
											width: '100%',
											allowBlank: false
										}
									]
								}
							],
							 dockedItems: [
							{
								xtype: 'toolbar',
								dock: 'bottom',
								items: [
									{
										text: 'Upload',
										iconCls: 'fa fa-upload',
										formBind: true,
										handler: function(){     
											Ext.getCmp('addMediaForm').setLoading(true);
                                            var data = Ext.getCmp('addMediaForm').getValues();
											Ext.getCmp('addMediaForm').submit({
												url: '../Media.action?UploadGeneralMedia&generalMedia.name='+data.name,
												method: 'POST',
												success: function(response, opts) {
													Ext.toast('Uploaded Successfully', '', 'tr');
													Ext.getCmp('addMediaForm').setLoading(false);
													Ext.getCmp('addMediaWin').hide();													
													refreshGrid();												
												},
												failure: function(response,opts){
													Ext.toast('Upload Failed', '', 'tr');
													Ext.getCmp('addMediaForm').setLoading(false);									
													refreshGrid();	
												}
											});												
										}
									},
									{
										xtype: 'tbfill'
									},
									{
										text: 'Cancel',
										iconCls: 'fa fa-close',
										handler: function(){
											Ext.getCmp('addMediaWin').close();
										}											
									}
								]
							}
						]
					}]
				   
				});
			});

		</script>	
		
	</stripes:layout-component>
</stripes:layout-render>