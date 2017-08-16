<%--
/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

	<script src="scripts/component/templateBlocks.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/mediaViewer.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/relationshipVisualization.js?v=${appVersion}" type="text/javascript"></script>		
	<script src="scripts/component/reviewWindow.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/questionWindow.js?v=${appVersion}" type="text/javascript"></script>
	
	<div style="display:none; visibility: hidden;" id="templateHolder"></div>	
		
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */	
		
		var DetailPage = {
			showRelatedOrganizations: function(organization) {
				DetailPage.relatedWindow.show();
				DetailPage.relatedWindow.setTitle('Related Entries');
							
			    var descriptionPanel = DetailPage.relatedWindow.getComponent('grid').getComponent('description');
				descriptionPanel.update({
					description: organization,
					tip: ''
				});
				
				Ext.Ajax.request({
					url: 'api/v1/resource/organizations/name/' + organization,
					success: function(response, opts) {
						var org = Ext.decode(response.responseText);
						
						var fullDescription = '';
						if (org.organizationType) {
							fullDescription += '<b>Organization Type:</b> ' + org.organizationType + '<br>';
						}
						if (org.agency) {
							fullDescription += '<b>Agency:</b> ' + org.agency + '<br>';
						}
						if (org.department) {
							fullDescription += '<b>Department:</b> ' + org.department + '<br>';
						}	
						if (org.department) {
							fullDescription += '<b>Home Page:</b> ' + org.homeUrl + '<br>';
						}						
						if (org.description) {
							fullDescription += org.description + '<br>';
						}
						
						descriptionPanel.update({
							description: organization,
							tip: fullDescription
						});
					}
				});
				
				var searchObj = {
					"sortField": "name",
					"sortDirection": "ASC",				
					"searchElements": [{
							"searchType": 'COMPONENT',
							"field": 'organization',
							"value": organization,
							"caseInsensitive": true,
							"numberOperation": "EQUALS",
							"stringOperation": "EQUALS",
							"mergeCondition": "OR" 
					}]
				 };
				
				var store = DetailPage.relatedWindow.getComponent('grid').getStore();
				store.getProxy().buildRequest = function (operation) {
					var initialParams = Ext.apply({
						paging: true,
						sortField: operation.getSorters()[0].getProperty(),
						sortOrder: operation.getSorters()[0].getDirection(),
						offset: operation.getStart(),
						max: operation.getLimit()
					}, operation.getParams());
					params = Ext.applyIf(initialParams, store.getProxy().getExtraParams() || {});

					var request = new Ext.data.Request({
						url: 'api/v1/service/search/advance',
						params: params,
						operation: operation,
						action: operation.getAction(),
						jsonData: Ext.util.JSON.encode(searchObj)
					});
					operation.setRequest(request);

					return request;
				};
				store.loadPage(1);				
			}
		};
		
		
		
		Ext.onReady(function(){		
			
			var componentId = '${param.id}';
			var evaluationId = '${param.evalId}';
			var fullPage = '${param.fullPage}' !== '' ? true : false;
			var hideSecurityBanner =  '${param.hideSecurityBanner}' !==  '' ? true : false;
			
			var relatedStore = Ext.create('Ext.data.Store', {
				pageSize: 50,
				autoLoad: false,
				remoteSort: true,
				sorters: [
					new Ext.util.Sorter({
					property: 'name',
					direction: 'ASC'
					})
				],				
				proxy: CoreUtil.pagingProxy({
					actionMethods: {create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'},
					reader: {
						type: 'json',
						rootProperty: 'data',
						totalProperty: 'totalNumber'
					}
				}),
				listeners: {
					load: function(store, records) {
						store.filterBy(function(record){
							return record.get('componentId') !== componentId;
						});
					}
				}
			});
			
			DetailPage.relatedWindow = Ext.create('Ext.window.Window', {
				title: 'Related Entries - ',
				//x: 5,		
				//y: 220,
				width: '95%',
				height: '60%',
				modal: true,
				draggable: false,
				maximizable: true,
				layout: 'fit',
				items: [
					{
						xtype: 'grid',
						itemId: 'grid',
						columnLines: true,
						store: relatedStore,
						columns: [
							{ text: 'Name', dataIndex: 'name', flex:2, minWidth: 250, cellWrap: true, 
								renderer: function (value, meta, record) {
									return '<a class="details-table" href="view.jsp?id=' + record.get('componentId') + '&fullPage=true" target="_blank">' + value + '</a>';
								}
							},
							{ text: 'Description', dataIndex: 'description', flex: 2,
								cellWrap: true,
								renderer: function (value) {
									value = Ext.util.Format.stripTags(value);
									return Ext.String.ellipsis(value, 300);
								}
							},							
							{ text: 'Type', align: 'center', dataIndex: 'componentTypeDescription', width: 150 }							
						],
						dockedItems: [
							{
								xtype: 'pagingtoolbar',							
								dock: 'bottom',
								store: relatedStore,
								displayInfo: true
							},
							{
								xtype: 'panel',
								itemId: 'description',
								maxHeight: 200,
								bodyStyle: 'padding-left: 5px; padding-right: 5px;',
								scrollable: true,
								tpl: new Ext.XTemplate(
									'<h2 style="text-align: center;">{description}</h2><hr>',
									'{tip}'
								)
							}
						]						
					}				
				]			
			});		
		
			var headerPanel = Ext.create('Ext.panel.Panel', {
				region: 'north',
				id: 'topNavPanel',
				bodyStyle: 'background: white; padding: 15px;',
				layout: {
					type: 'hbox',
					align: 'stretch'
				},
				items: [				
					{
						xtype: 'panel',
						id: 'titlePanel',
						flex: 1,
						minHeight: 125,						
						tpl: new Ext.XTemplate(
							'<div class="details-title-name">{name} <span class="details-title-info" style="font-size: 10px">({componentTypeLabel})</span></div>',
							'<div class="details-title-info">',							
							'Organization: <b><a href="#" class="a.details-table" onclick="DetailPage.showRelatedOrganizations(\'{organization}\')">{organization}</a></b><tpl if="version"> Version: <b>{version}</b></tpl><tpl if="releaseDate"> Release Date: <b>{[Ext.util.Format.date(values.releaseDate)]}</b></tpl>',							
							'</div>',
							'  <tpl for="attributes">',
							'    <tpl if="badgeUrl"><img src="{badgeUrl}" title="{codeDescription}" width="40" /></tpl>',
							'  </tpl>'
						)
					},
					{
						xtype: 'panel',
						id: 'toolsPanel',
						layout: {
							type: 'hbox'
						},
						dockedItems: [
							{
								xtype: 'panel',
								itemId: 'updatedInfo',
								dock: 'bottom',
								tpl: '<span class="details-title-info" style="font-size: 10px">Updated: <b>{[Ext.util.Format.date(values.lastActivityDts, "m/d/y H:i:s")]}</b><tpl if="securityMarkingType"><br>Highest Classification:  <b>(<span title="{securityMarkingDescription}">{securityMarkingType}</span>)</b></span></tpl>'
							}
						],
						items: [
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-tags icon-top-padding',
								tooltip: 'Tags',
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){	
									if (Ext.getCmp('tagPanel').isHidden()){
										Ext.getCmp('tagPanel').setHidden(false);
									} else {
										Ext.getCmp('tagPanel').setHidden(true);
									}
								}
							},
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-binoculars style="position: relative; left: -10px;"',
								id: 'watchBtn',
								tooltip: 'Watch',
								scale: 'large',
								width: '58px',
								margin: '0 10 0 0',
								handler: function(){
									var watch = {
											componentId: componentId,
											lastViewDts: new Date(),
											username: '${user}',
											notifyFlg: false
									};
									Ext.Ajax.request({
										url: 'api/v1/resource/userprofiles/' +'${user}'+ '/watches',
										method: 'POST',
										jsonData: watch,
										success: function(response, opts){
											currentWatch = Ext.decode(response.responseText);
											Ext.getCmp('watchBtn').setHidden(true);
											Ext.getCmp('watchRemoveBtn').setHidden(false);
										}
									});
								}
							},
							{
								xtype: 'button',								
								text: '<span class="fa-stack fa-lg"><i class="fa fa-binoculars fa-stack-1x"></i><i class="fa fa-ban fa-stack-2x text-danger"></i></span>',
								id: 'watchRemoveBtn',
								tooltip: 'Delete Watch',
								scale: 'small',	
								height: '44px',
								margin: '0 10 0 0',
								hidden: true,
								handler: function(){
									var watchId = currentWatch.userWatchId ? currentWatch.userWatchId : currentWatch.watchId;
									Ext.Ajax.request({
										url: 'api/v1/resource/userprofiles/' +'${user}'+ '/watches/' + watchId,
										method: 'DELETE',										
										success: function(response, opts){
											Ext.getCmp('watchRemoveBtn').setHidden(true);
											Ext.getCmp('watchBtn').setHidden(false);
										}
									})
								}
							},							
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-print',
								tooltip: 'Print',
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){	
									var printWin = window.open('print.jsp?id=' + componentId, 'printwin', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=840, height=840');
									if (!printWin) {
										printWin = window.open('print.jsp?id=' + componentId, 'printwin');
									}
								}
							},
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-arrows-alt',
								tooltip: 'Full Page',
								hidden: fullPage,
								scale: 'large',
								margin: '0 10 0 0',
								href: 'view.jsp?id=' + componentId + '&fullPage=true',
								hrefTarget: '_blank'
							},
							{
								xtype: 'button',								
								id: 'ownerMenu',
								hidden: true,
								iconCls: 'fa fa-2x fa-navicon',								
								scale: 'large',								
								arrowVisible: false,
								margin: '0 10 0 0',
								menu: {
									items: [
										{
											text: 'Submit Correction',
											iconCls: 'fa fa-lg fa-comment-o icon-small-vertical-correction',
											handler: function() {
												Ext.Msg.show({
													title:'Submit Correction?',
													message: 'You are the owner of this entry.<br>Submit a change request for this entry from submissions.<br><br>Go to submissions now?',
													buttons: Ext.Msg.YESNO,
													icon: Ext.Msg.QUESTION,
													fn: function(btn) {
														if (btn === 'yes') {
															window.parent.location.href = 'UserTool.action?load=Submissions';
														}
													}
												});												
											}
										}
									]
								}
							},
							{
								xtype: 'button',								
								id: 'nonOwnerMenu',
								iconCls: 'fa fa-2x fa-navicon',								
								scale: 'large',								
								arrowVisible: false,
								margin: '0 10 0 0',
								menu: {
									items: [										
										{
											text: 'Submit Correction',
											iconCls: 'fa fa-lg fa-comment-o icon-small-vertical-correction icon-button-color-default',
											handler: function() {
												var feedbackWin = Ext.create('OSF.component.FeedbackWindow', {
													closeAction: 'destroy',
													title: 'Submit Correction',
													draggable: false,
													extraDescription: 'Entry Name: ' + entry.name,
													hideType: 'Correction Requested',
													hideSummary: Ext.String.ellipsis(entry.name, 50),
													labelForDescription: 'Correction: <br>(Please include the section needing the correction. Eg. Contacts)',
													successHandler: function() {														
														this.close();
													}
												});
												feedbackWin.show();
											}
										},
										{	
											xtype: 'menuseparator'
										},
										{
											text: 'Request Ownership',
											iconCls: 'fa fa-lg fa-envelope-square icon-small-vertical-correction icon-button-color-default',
											handler: function() {
												var feedbackWin = Ext.create('OSF.component.FeedbackWindow', {
													closeAction: 'destroy',
													title: 'Request Ownership',
													draggable: false,
													extraDescription: 'Entry Name: ' + entry.name,												
													hideType: 'Request Ownership',
													hideSummary: Ext.String.ellipsis(entry.name, 50),
													labelForDescription: 'Request Reason: <br>(Entries you own show in the User Tools->Submissions which provides tools for management.',
													successHandler: function() {	
														this.close();
													}
												});
												feedbackWin.show();												
											}
										}										
									]
								}
							}							
						]
					}
				],
				dockedItems: [
					{						
						xtype: 'panel',
						id: 'tagPanel',
						dock: 'bottom',
						bodyStyle: 'padding-left: 10px; padding-right: 10px;',
						hidden: true,
						items: [
							{
								xtype: 'panel',
								layout: 'hbox',
								items: [
									Ext.create('OSF.component.StandardComboBox', {
										name: 'text',	
										id: 'tagField',																				
										flex: 1,
										fieldLabel: 'Add Tag',
										forceSelection: false,
										valueField: 'text',
										displayField: 'text',										
										margin: '0 10 10 0',
										maxLength: 120,
										storeConfig: {
											url: 'api/v1/resource/components/' + componentId + '/tagsfree'
										},
										listeners:{
											specialkey: function(field, e) {
												var value = this.getValue();
												if (e.getKey() === e.ENTER && !Ext.isEmpty(value)) {
													actionAddTag(value);
												}	
											}
										}
									}),
									{
										xtype: 'button',
										text: 'Add',
										iconCls: 'fa fa-plus',
										margin: '30 0 0 0',
										minWidth: 75,
										handler: function(){
											var tagField = Ext.getCmp('tagField');
											if (tagField.isValid()) {
												actionAddTag(tagField.getValue());
											}
										}
									}
								]
							}
						]
					}
				]
			});
			
			if (fullPage && !hideSecurityBanner) {
				CoreService.brandingservice.getCurrentBranding().then(function(branding){					
					if (branding.securityBannerText && branding.securityBannerText !== '') {
						Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
							securityBannerText: branding.securityBannerText
						}), 0);
					}
				});			
			}
			
			var actionAddTag = function(tag) {
				
				if (!tag || tag === '') {
					Ext.getCmp('tagField').markInvalid('Tag name required');
				} else {				
					//add tag
					Ext.getCmp('tagPanel').setLoading('Tagging Entry...');
					Ext.Ajax.request({
						url: 'api/v1/resource/components/' + componentId + '/tags',
						method: 'POST',
						jsonData: {
							text: tag
						},
						callback: function(){
							Ext.getCmp('tagPanel').setLoading(false);
						},
						success: function(response, opt){
							var tag = Ext.decode(response.responseText);
							var tagField = Ext.getCmp('tagField');

							if (typeof tag.errors === 'undefined') {
								processTags(tag);
								tagField.reset();
								tagField.getStore().load();
							}
							else {
								tagField.reset();
								tagField.markInvalid(tag.errors.entry[0].value);
							}
						}
					});	
				}
			};
			
			var detailPanel = Ext.create('Ext.panel.Panel', {
				id: 'detailPanel',
				region: 'center',				
				bodyStyle: 'padding: 10px;',
				layout: 'fit'
			});
			
			
			
			var contentPanel = Ext.create('Ext.panel.Panel', {
				region: 'center',
				bodyStyle: 'background: white; padding: 5px;',
				layout: 'border',
				items: [
					detailPanel					
				]
			});			
			
			Ext.create('Ext.container.Viewport', {
				layout: 'border',
				items: [						
					headerPanel,
					contentPanel
				]
			});
			
			var entry;
			var componentTypeDetail;
			var loadDetails = function(){
				if (componentId) {
					headerPanel.setLoading(true);
					contentPanel.setLoading(true);

					var evalComponentUrl = 'api/v1/resource/components/' + componentId + '/detail';
					if (evaluationId) {
						evalComponentUrl = 'api/v1/resource/evaluations/' + evaluationId + '/componentdetails/';
					}

					Ext.Ajax.request({
						url: evalComponentUrl,
						callback: function(){
							headerPanel.setLoading(false);							
						},
						success: function(response, opts) {
							entry = Ext.decode(response.responseText);
							componentId = entry.componentId;

							Ext.getCmp('titlePanel').update(entry);
							Ext.defer(function(){
								headerPanel.updateLayout(true, true);
							}, 1000);
														
							if (entry.createUser === '${user}'){
								Ext.getCmp('nonOwnerMenu').setHidden(true);
								Ext.getCmp('ownerMenu').setHidden(false);								
							}
							
							Ext.getCmp('toolsPanel').getComponent('updatedInfo').update(entry);
							
							if (entry.approvalState !== "A") {
								var html = 'This entry has not yet been approved for the site and is still under review.';
								if (entry.approvalState == 'N') {
									html = 'This entry has not yet been submitted for review. It must be submitted to appear on the Storefront.';
								}


								headerPanel.addDocked({
									xtype: 'panel',
									dock: 'top',
									bodyCls: 'alert-warning',
									bodyStyle: 'padding: 20px; font-size: 20px; text-align: center;',
									html: html
								});
								Ext.getCmp('watchBtn').setHidden(true);
								Ext.getCmp('watchRemoveBtn').setHidden(true);
							} else {
								//check for watch
								if (currentWatch) {
									if (entry.lastActivityDts > currentWatch.lastViewDts) {
									headerPanel.addDocked({
										xtype: 'panel',
										dock: 'top',
										bodyCls: 'alert-warning',
										bodyStyle: 'padding: 20px; font-size: 20px; text-align: center;',
										html: 'This entry has recently been updated.'
									});										
									}
								}
								
							}
							
							//get component type and determine review & q&a
							Ext.Ajax.request({
								url: 'api/v1/resource/componenttypes/' + entry.componentType,								
								success: function(response, opts) {
									componentTypeDetail = Ext.decode(response.responseText);
																		
									processTags(entry.tags);
									
									var templateUrl;
									if (componentTypeDetail.componentTypeTemplate) {
										//load custom										
										templateUrl= 'Template.action?LoadTemplate&templateId=' + componentTypeDetail.componentTypeTemplate;
									} else if (entry.componentType === 'ARTICLE') {										
										templateUrl= 'Router.action?page=template/article.jsp';
									} else {
										templateUrl= 'Router.action?page=template/standard.jsp';
									}
									
									
									//populate detail via template
									Ext.Ajax.request({
										url: templateUrl,
										callback: function(){
											contentPanel.setLoading(false);
										},										
										success: function(response, opt) {
											var text = response.responseText;											
											Ext.dom.Element.get("templateHolder").setHtml(text, true, function(){
												template.refresh(Ext.getCmp('detailPanel'), entry);
											});
										}
									});
									
									
								}
							});
							
							
							
														
							
						}
					});
				}
			};
			
			var currentWatch;			
			var loadWatches = function(){
				Ext.Ajax.request({
					url: 'api/v1/resource/userprofiles/' + '${user}' + '/watches',
					success: function(response, opts) {
						var watches = Ext.decode(response.responseText);
						Ext.Array.each(watches, function(watch){
							if (watch.componentId === componentId) {
								currentWatch = watch;
							}
						});
						if (currentWatch) {
							Ext.getCmp('watchBtn').setHidden(true);
							Ext.getCmp('watchRemoveBtn').setHidden(false);							
						}

						loadDetails();
					}
				});
			};
			loadWatches();
			
			
			var processTags = function(tagList){
				
				var tags = [];
				Ext.Array.each(tagList,  function(tag){
					var tagButton;
					
					var showRelatedByTag = function() {
						
						DetailPage.relatedWindow.show();
						DetailPage.relatedWindow.setTitle('Related Entries By Tag');

						var descriptionPanel = DetailPage.relatedWindow.getComponent('grid').getComponent('description');
						descriptionPanel.update({
							description: tag.text,
							tip: ''
						});


						var searchObj = {
							"sortField": "name",
							"sortDirection": "ASC",				
							"searchElements": [{
									"searchType": 'TAG',
									"field": 'text',
									"value": tag.text,
									"caseInsensitive": true,
									"numberOperation": "EQUALS",
									"stringOperation": "EQUALS",
									"mergeCondition": "OR" 
							}]
						 };

						var store = DetailPage.relatedWindow.getComponent('grid').getStore();
						store.getProxy().buildRequest = function (operation) {
							var initialParams = Ext.apply({
								paging: true,
								sortField: operation.getSorters()[0].getProperty(),
								sortOrder: operation.getSorters()[0].getDirection(),
								offset: operation.getStart(),
								max: operation.getLimit()
							}, operation.getParams());
							params = Ext.applyIf(initialParams, store.getProxy().getExtraParams() || {});

							var request = new Ext.data.Request({
								url: 'api/v1/service/search/advance',
								params: params,
								operation: operation,
								action: operation.getAction(),
								jsonData: Ext.util.JSON.encode(searchObj)
							});
							operation.setRequest(request);

							return request;
						};
						store.loadPage(1);							
						
					};
					
					if (tag.createUser === '${user}') {
						tagButton = Ext.create('Ext.button.Split', {
							text: tag.text,							
							entryTag: tag,
							menu: {
								items: [
									{
										text: 'Show Related Entries',
										iconCls: 'fa fa-retweet',
										handler: function(){
											showRelatedByTag();
										}
									},
									{
										text: 'Delete',
										iconCls: 'fa fa-close',
										handler: function(){
											var tagButton = this.up('button');
											var tag = tagButton.entryTag;
											Ext.getCmp('tagPanel').setLoading('Deleting Tag...');
											Ext.Ajax.request({
												url: 'api/v1/resource/components/' + componentId + '/tags/' + tag.tagId,
												method: 'DELETE',
												callback: function(){
													Ext.getCmp('tagPanel').setLoading(false);
												},
												success: function(response, opt){
													Ext.getCmp('tagPanel').remove(tagButton, true);
													Ext.getCmp('tagField').store.reload();
												}
											});											
										}
									}
								]
							},
							margin: '0 10 0 0',
							handler: function(){
								showRelatedByTag();		
							}
						});
					} else {
						tagButton = Ext.create('Ext.button.Button', {
							text: tag.text,							
							margin: '0 10 0 0',
							handler: function(){
								showRelatedByTag();
							}
						});						
					}
					
					tags.push(tagButton);
				});
			
				Ext.getCmp('tagPanel').add(tags);
				
			};
		
		});
		
	</script>	
		
    </stripes:layout-component>
</stripes:layout-render>
