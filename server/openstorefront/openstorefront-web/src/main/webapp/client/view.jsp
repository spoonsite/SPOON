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
		
		
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){		
			
			var componentId = '${param.id}';
			//var showFullPage =
			
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
						minHeight: 100,						
						tpl: new Ext.XTemplate(
							'<div class="details-title-name">{name}</div>',
							'<div class="details-title-info">',
							'Organization: <b>{organization}</b><tpl if="version"> Version: <b>{version}</b></tpl><tpl if="version"> Release Date: <b>{[Ext.util.Format.date(values.releaseDate)]}</b></tpl><br>',
							'<tpl if="version">Version: {version}</tpl>',
							'<tpl if="version">Release Date: {[Ext.util.Format.date(values.releaseDate)]}</tpl>',
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
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){									
								}
							},
							{
								xtype: 'button',
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
											}
										},
										{	
											xtype: 'menuseparator'
										},
										{
											text: 'Request Ownership',
											iconCls: 'fa fa-envelope-square',
											handler: function() {
											}
										}										
									]
								}
							}							
						]
					}
				]
			});
			
			var detailPanel = Ext.create('Ext.panel.Panel', {
				title: 'Details'
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
				items: [
					{
						xtype: 'panel',
						itemId: 'summary',
						title: 'Review Summary',
						titleCollapse: true,
						collapsible: true,
						margin: '0 0 1 0',
						bodyStyle: 'padding: 10px;',
						tpl: new Ext.XTemplate(
							'<table style="width:100%"><tr>',
							'	<td valign="top">',
							'		Average Rating: <tpl for="averageRatingStars"><i class="fa fa-{star}"></i></tpl><br>',
							'		<b>{recommended} out of {totalReviews} ({[Math.round((values.recommended/values.totalReviews)*100)]}%)</b> reviewers recommended',
							'   <td>',
							'	<td valign="top">',
							'		<tpl if="pros.length &gt; 0">',
							'		<b>Pros</b><hr>',
							'		<tpl for="pros">',
							'			{text} ({count})<br>',	
							'		</tpl></tpl>',
							'   <td>',
							'	<td valign="top">',
							'		<tpl if="cons.length &gt; 0">',
							'		<b>Cons</b><hr>',
							'		<tpl for="cons">',
							'			{text} ({count})<br>',	
							'		</tpl></tpl>',
							'   <td>',
							'</tr></table>'
						),
						items: [
							{
								xtype: 'button',
								text: 'Write a Review',
								iconCls: 'fa fa-lg fa-star-half-o icon-top-padding-5',
								scale: 'medium',
								handler: function(){
								}
							}
						]						
					},
					{
						xtype: 'panel',
						itemId: 'reviews',
						title: 'Reviews',
						hidden: true,						
						titleCollapse: true,
						collapsible: true,
						bodyStyle: 'padding: 10px;',
						tpl: new Ext.XTemplate(
							'<tpl for=".">',	
							'<table style="width:100%"><tr>',
							'	<td valign="top">',
							'		<h1>{title} <tpl for="ratingStars"><i class="fa fa-{star}"></i></tpl></h1>',								
							'		<tpl if="recommend"><b>Recommend</b><br></tpl>',
							'		{username} ({userTypeCode}) - {[Ext.util.Format.date(values.updateDate, "m/d/y")]}<br>',
							'		<b>Organization:</b> {organization}<br>',
							'		<b>How long have you used it:</b> {userTimeCode}<br>',							
							'		<b>Last Used:</b> {[Ext.util.Format.date(values.lastUsed, "m/Y")]}<br>',
							'   <td>',
							'	<td valign="top">',
							'		<tpl if="pros.length &gt; 0">',
							'		<b>Pros</b><hr>',
							'		<tpl for="pros">',
							'			{text} ({count})<br>',	
							'		</tpl></tpl>',
							'   <td>',
							'	<td valign="top">',
							'		<tpl if="cons.length &gt; 0">',
							'		<b>Cons</b><hr>',
							'		<tpl for="cons">',
							'			{text} ({count})<br>',	
							'		</tpl></tpl>',
							'   <td>',
							'</tr></table>',
							'<b>Comments:</b><br>{comment}',
							' <br><br><hr>',
							'</tpl>'
						)						
					}
				]
			});
			
			var questionPanel = Ext.create('Ext.panel.Panel', {
				title: 'Q&A',
				id: 'questionPanel'
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
			var loadDetails = function(){
				if (componentId) {
					headerPanel.setLoading(true);
					contentPanel.setLoading(true);
					Ext.Ajax.request({
						url: '../api/v1/resource/components/' + componentId + '/detail',
						callback: function(){
							headerPanel.setLoading(false);
							contentPanel.setLoading(false);
						},
						success: function(response, opts) {
							entry = Ext.decode(response.responseText);
							
							Ext.getCmp('titlePanel').update(entry);
							
							//get component type and determine review & q&a
							
							//process reviews
							processReviews(entry);
							
							//populate detail via template
														
							
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
				
				reviewPanelSummary.update(summaryData);
				
				if (entryLocal.reviews.length > 0) {
					reviewPanelReviews.setHidden(false);
					reviewPanelReviews.update(entryLocal.reviews);
				}
				
				
			};
		
		});
		
	</script>	
		
    </stripes:layout-component>
</stripes:layout-render>
