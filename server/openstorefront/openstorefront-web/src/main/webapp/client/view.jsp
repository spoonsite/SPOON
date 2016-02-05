<%--
Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
	<script src="scripts/component/templateBlocks.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/mediaViewer.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/relationshipVisualization.js?v=${appVersion}" type="text/javascript"></script>		
		
	<div style="display:none; visibility: hidden;" id="templateHolder"></div>	
		
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */	
		
		var DetailPage = {
			showRelatedWindow: function(attributeType, attributeCode, description) {
				DetailPage.relatedWindow.show();
				DetailPage.relatedWindow.setTitle('Related Entries - ' + description);
				
				var searchObj = {
					"sortField": "name",
					"sortDirection": "DESC",				
					"searchElements": [{
							"searchType": "ATTRIBUTE",
							"keyField": attributeType,
							"keyValue": attributeCode,
							"caseInsensitive": false,
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
						url: '/openstorefront/api/v1/service/search/advance',
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
			var fullPage = '${param.fullPage}' !== '' ? true : false;
			
			var relatedStore = Ext.create('Ext.data.Store', {
							pageSize: 50,
							autoLoad: false,
							remoteSort: true,
							sorters: [
								new Ext.util.Sorter({
								property: 'name',
								direction: 'DESC'
								})
							],
							proxy: CoreUtil.pagingProxy({
								actionMethods: {create: 'POST', read: 'POST', update: 'POST', destroy: 'POST'},
								reader: {
									type: 'json',
									rootProperty: 'data',
									totalProperty: 'totalNumber'
								}
							})							
			});
			
			DetailPage.relatedWindow = Ext.create('Ext.window.Window', {
				title: 'Related Entries - ',
				//x: 5,		
				//y: 220,
				width: '50%',
				height: '60%',
				modal: true,				
				layout: 'fit',
				items: [
					{
						xtype: 'grid',
						itemId: 'grid',
						columnLines: true,
						store: relatedStore,
						columns: [
							{ text: 'Name', dataIndex: 'name', width: 250, cellWrap: true, 
								renderer: function (value, meta, record) {
									return '<a class="details-table" href="view.jsp?id=' + record.get('componentId') + '&fullPage=true" target="_blank">' + value + '</a>'
								}
							},
							{ text: 'Description', dataIndex: 'description', flex: 1,
								cellWrap: true,
								renderer: function (value) {
									value = Ext.util.Format.stripTags(value);
									return Ext.String.ellipsis(value, 300);
								}
							},							
							{ text: 'Type', align: 'center', dataIndex: 'componentTypeDescription', width: 150 }							
						],
						dockedItems: [{
							xtype: 'pagingtoolbar',							
							dock: 'bottom',
							store: relatedStore,
							displayInfo: true
						}]						
					}				
				]			
			});			
			
			var headerPanel = Ext.create('Ext.panel.Panel', {
				region: 'north',
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
							'<div class="details-title-name">{name} <span class="details-title-info" style="font-size: 10px">({componentTypeLabel})</span> </div>',
							'<div class="details-title-info">',							
							'Organization: <b>{organization}</b><tpl if="version"> Version: <b>{version}</b></tpl><tpl if="version"> Release Date: <b>{[Ext.util.Format.date(values.releaseDate)]}</b></tpl><br>',						
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
						items: [
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-tags icon-top-padding',
								tooltip: 'Tags',
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){	
									
								}
							},
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-binoculars icon-top-padding',
								id: 'watchBtn',
								tooltip: 'Watch',
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){									
								}
							},
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-print icon-top-padding',
								tooltip: 'Print',
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){									
								}
							},
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-arrows-alt icon-top-padding',
								tooltip: 'Full Page',
								hidden: fullPage,
								scale: 'large',
								margin: '0 10 0 0',
								href: 'view.jsp?id=' + componentId + '&fullPage=true',
								hrefTarget: '_blank'
							},
							{
								xtype: 'button',								
								id: 'nonOwnerMenu',
								iconCls: 'fa fa-2x fa-navicon icon-top-padding',								
								scale: 'large',								
								arrowVisible: false,
								margin: '0 10 0 0',
								menu: {
									items: [										
										{
											text: 'Submit Correction',
											iconCls: 'fa fa-comment-o',
											handler: function() {
												var feedbackWin = Ext.create('OSF.component.FeedbackWindow', {
													closeAction: 'destroy',
													title: 'Submit Correction',
													extraDescription: 'Entry Name: ' + entry.name,
													hideType: 'Correction Requested',
													hideSummary: Ext.String.ellipsis(entry.name, 50),
													labelForDescription: 'Correction: <br>(Please include the section needing the correction. Eg. Contacts)',
													successHandler: function() {														
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
											iconCls: 'fa fa-envelope-square',
											handler: function() {
												var feedbackWin = Ext.create('OSF.component.FeedbackWindow', {
													closeAction: 'destroy',
													title: 'Request Ownership',
													extraDescription: 'Entry Name: ' + entry.name,												
													hideType: 'Request Ownership',
													hideSummary: Ext.String.ellipsis(entry.name, 50),
													labelForDescription: 'Request Reason: <br>(Entries you own show in the User Tool->Submission which provides tools for management.',
													successHandler: function() {														
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
						dock: 'bottom'
					}
				]
			});
			
			var detailPanel = Ext.create('Ext.panel.Panel', {
				id: 'detailPanel',
				title: 'Details',
				bodyStyle: 'padding: 10px;',
				scrollable: true
			});
			
			var reviews = Ext.create('Ext.panel.Panel', {				
				id: 'reviewPanel',		
				title: 'Reviews',
				bodyStyle: 'padding: 10px;',
				scrollable: true,
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				dockedItems: [
					{
						xtype: 'button',
						text: 'Write a Review',
						maxWidth: 200,
						scale: 'medium',
						margin: 10,
						iconCls: 'fa fa-lg fa-star-half-o icon-top-padding-5',
						handler: function(){
							
						}
					}
				],				
				items: [
					{
						xtype: 'panel',
						itemId: 'summary',
						title: 'Review Summary',
						titleCollapse: true,
						collapsible: true,
						hidden: true,
						margin: '0 0 1 0',
						bodyStyle: 'padding: 10px;',
						tpl: new Ext.XTemplate(
							'<table style="width:100%"><tr>',
							'	<td valign="top">',
							'		<tpl if="totalReviews && totalReviews &gt; 0">',
							'		    <div class="review-summary-rating">Average Rating: <tpl for="averageRatingStars"><i class="fa fa-{star} rating-star-color"></i></tpl></div>',							
							'			<b>{recommended} out of {totalReviews} ({[Math.round((values.recommended/values.totalReviews)*100)]}%)</b> reviewers recommended',
							'		</tpl>',
							'   <td>',
							'	<td valign="top" width="20%">',
							'		<tpl if="pros.length &gt; 0">',
							'			<div class="review-pro-con-header">Pros</div>',
							'			<tpl for="pros">',
							'				- {text} <span class="review-summary-count">({count})</span><br>',	
							'			</tpl>',
							'		</tpl>',
							'   <td>',
							'	<td valign="top" width="20%">',
							'		<tpl if="cons.length &gt; 0">',
							'			<div class="review-pro-con-header">Cons</div>',							
							'			<tpl for="cons">',
							'				- {text} <span class="review-summary-count">({count})</span><br>',	
							'			</tpl>',
							'		</tpl>',
							'   <td>',
							'</tr></table>'
						)						
					},
					{
						xtype: 'panel',
						itemId: 'reviews',
						title: 'User Reviews',
						hidden: true,						
						titleCollapse: true,
						collapsible: true,
						bodyStyle: 'padding: 10px;',
						tpl: new Ext.XTemplate(
							'<tpl for=".">',	
							'<table style="width:100%"><tr>',
							'	<td valign="top">',
							'		<h1>{title} <br> <tpl for="ratingStars"><i class="fa fa-{star} rating-star-color"></i></tpl></h1>',								
							'		<div class="review-who-section">{username} ({userTypeCode}) - {[Ext.util.Format.date(values.updateDate, "m/d/y")]}<tpl if="recommend"> - <b>Recommend</b></tpl></div><br>',
							'		<b>Organization:</b> {organization}<br>',
							'		<b>Experience:</b> {userTimeCode}<br>',							
							'		<b>Last Used:</b> {[Ext.util.Format.date(values.lastUsed, "m/Y")]}<br>',
							'   <td>',
							'	<td valign="top" width="20%">',
							'		<tpl if="pros.length &gt; 0">',									
							'		<div class="review-pro-con-header">Pros</div>',
							'		<tpl for="pros">',
							'			- {text}<br>',	
							'		</tpl></tpl>',
							'   <td>',
							'	<td valign="top" width="20%">',
							'		<tpl if="cons.length &gt; 0">',
							'		<div class="review-pro-con-header">Cons</div>',
							'		<tpl for="cons">',
							'			- {text}<br>',	
							'		</tpl></tpl>',
							'   <td>',
							'</tr></table>',
							'<br><b>Comments:</b><br>{comment}',
							' <br><br><hr>',
							'</tpl>'
						)						
					}
				]
			});
			
			var questionPanel = Ext.create('Ext.panel.Panel', {
				title: 'Q&A',
				id: 'questionPanel',
				bodyStyle: 'padding: 10px;',
				scrollable: true,
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				dockedItems: [
					{
						xtype: 'button',
						text: 'Ask a Question',
						maxWidth: 200,
						scale: 'medium',
						margin: 10,
						iconCls: 'fa  fa-lg fa-comment icon-top-padding-5',
						handler: function(){
							
						}
					}
				]
				
			});			
			
			var contentPanel = Ext.create('Ext.panel.Panel', {
				region: 'center',
				bodyStyle: 'background: white; padding: 5px;',
				layout: 'border',
				items: [
					{
						region: 'north',
						xtype: 'panel',
						id: 'tagPanel',
						hidden: true
					},
					{
						region: 'center',
						xtype: 'tabpanel',						
						tabBar: {
							defaults: {
								width: '33%'
							},
							dock: 'top',
							layout: {
								pack: 'left'
							}
						},						
						items: [
							
							detailPanel,
							reviews,
							questionPanel
						]
					}
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
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + componentId + '/detail',
						callback: function(){
							headerPanel.setLoading(false);							
						},
						success: function(response, opts) {
							entry = Ext.decode(response.responseText);
							
							Ext.getCmp('titlePanel').update(entry);
							
							if (entry.createUser === '${user}'){
								Ext.getCmp('nonOwnerMenu').setHidden(true);
							}
							
							if (entry.approvalState !== "A") {
								headerPanel.addDocked({
									xtype: 'panel',
									dock: 'top',
									bodyCls: 'alert-warning',
									bodyStyle: 'padding: 20px; font-size: 20px; text-align: center;',
									html: 'This entry has not yet been approved for the site and is still under review.'
								});
								Ext.getCmp('watchBtn').setHidden(true);
							}
							
							//get component type and determine review & q&a
							Ext.Ajax.request({
								url: '../api/v1/resource/componenttypes/' + entry.componentType,								
								success: function(response, opts) {
									componentTypeDetail = Ext.decode(response.responseText);
									
									if (componentTypeDetail.dataEntryReviews) {
										processReviews(entry);
									}
									if (componentTypeDetail.dataEntryQuestions) {
										processQuestions(entry);
									}
									
									var templateUrl;
									if (componentTypeDetail.componentTypeTemplate) {
										//load custom										
										templateUrl= '../api/v1/resource/componenttypetemplates/' + componentTypeDetail.componentTypeTemplate + '/template';
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
			loadDetails();
			
			var processReviews = function(entryLocal) {
				
				//gather summary
				var summaryData = {					
					totalRatings: 0,
					averageRatingStars: [],
					pros: [],
					cons: [],
					totalReviews: entryLocal.reviews.length,
					recommended: 0
				};
				
				Ext.Array.each(entryLocal.reviews, function(review){
					summaryData.totalRatings += review.rating;
					if (review.recommend) {					
						summaryData.recommended++;
					}
					
					Ext.Array.each(review.pros, function(pro) {
						var found = false;
					
						Ext.Array.each(summaryData.pros, function(sumpro){
							if (sumpro.text === pro.text) {
								sumpro.count++;
								found = true;
							}
						});
						
						if (!found) {
							summaryData.pros.push({
								text: pro.text,
								count: 1
							});
						}
					});
					
					Ext.Array.each(review.cons, function(con) {
						var found = false;
					
						Ext.Array.each(summaryData.cons, function(sumpro){
							if (sumpro.text === con.text) {
								sumpro.count++;
								found = true;
							}
						});
						
						if (!found) {
							summaryData.cons.push({
								text: con.text,
								count: 1
							});
						}
					});	
					
					Ext.Array.sort(review.pros, function(a, b){
						return a.text.localeCompare(b.text);	
					});
					Ext.Array.sort(review.cons, function(a, b){
						return a.text.localeCompare(b.text);	
					});	
					
					review.ratingStars = [];
					for (var i=0; i<5; i++){					
						review.ratingStars.push({						
							star: i <= review.rating ? (review.rating - i) > 0 && (review.rating - i) < 1 ? 'star-half-o' : 'star' : 'star-o'
						});
					}					
					
				});
				var reviewPanelReviews = Ext.getCmp('reviewPanel').getComponent('reviews');
				var reviewPanelSummary = Ext.getCmp('reviewPanel').getComponent('summary');
				
				Ext.Array.sort(summaryData.pros, function(a, b){
					return a.text.localeCompare(b.text);	
				});
				Ext.Array.sort(summaryData.cons, function(a, b){
					return a.text.localeCompare(b.text);	
				});				
				var averageRating = Math.round((summaryData.totalRatings / summaryData.totalReviews)* 10) / 10;
				summaryData.averageRating = averageRating;
				for (var i=0; i<5; i++){					
					summaryData.averageRatingStars.push({						
						star: i <= averageRating ? (averageRating - i) > 0 && (averageRating - i) < 1 ? 'star-half-o' : 'star' : 'star-o'
					});
				}
				
				if (entryLocal.reviews.length > 0) {
					reviewPanelSummary.setHidden(false);
					reviewPanelSummary.update(summaryData);
					reviewPanelReviews.setHidden(false);
					reviewPanelReviews.update(entryLocal.reviews);					
				}
				
				
			};
		
			var processQuestions = function(entryLocal) {
				
				var questionPanels = [];
				Ext.Array.each(entryLocal.questions, function(question){
					
					var text = '<div class="question-question"><span class="question-response-letter-q">Q.</span> '+ question.question + '</div>';
											
					var panel = Ext.create('Ext.panel.Panel', {
						titleCollapse: true,
						collapsible: true,
						title: text,
						bodyStyle: 'padding: 10px;',
						data: question.responses,
						tpl: new Ext.XTemplate(
							'<div class="question-info">',
							question.username + ' (' + question.userType + ') - ' + Ext.util.Format.date(question.questionUpdateDts, "m/d/Y"),
							'</div><br>',
							'<tpl for=".">',
							'	<div class="question-response"><span class="question-response-letter">A.</span> {response}</div>',
							'	<div class="question-info">{username} ({userType}) - {[Ext.util.Format.date(values.answeredDate, "m/d/Y")]}</div><br>',	
							'</tpl><hr>'
						),
						dockedItems: [
							{
								xtype: 'button',
								text: 'Answer',
								maxWidth: 150,
								scale: 'medium',
								margin: 10,
								iconCls: 'fa  fa-lg fa-comments-o icon-top-padding-5',
								handler: function(){

								}
							}
						]				
					});
					questionPanels.push(panel);				
					
				});
				Ext.getCmp('questionPanel').removeAll();
				Ext.getCmp('questionPanel').add(questionPanels);
		
			};
		
		});
		
	</script>	
		
    </stripes:layout-component>
</stripes:layout-render>
