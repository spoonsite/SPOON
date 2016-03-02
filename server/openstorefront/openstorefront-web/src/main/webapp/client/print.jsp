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
		function printFrame(frame) {
			if (frame.realLoad) {				
				//var printButton = frame.contentDocument.getElementById('print');
				//printButton.click();				
				frame.contentWindow.print();				
			} else {
				frame.realLoad = true;
			}
		}
	</script>
		
	
	<iframe id="contentPrintFrame" style="display: none; visibility: hidden; overflow: visible !important;" onload="printFrame(this);"></iframe>	
		
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */				
	
		
		Ext.onReady(function(){	
			
			var componentId = '${param.id}';
			
			var headerPanel = Ext.create('Ext.panel.Panel', {
				region: 'north',				
				cls: 'hidePrint',
				dockedItems: [
					{
						xtype: 'toolbar',
						dock: 'top',
						style: 'background: rgba(10, 10, 10, .7) !important;',
						items: [
							{
								xtype: 'button',
								iconCls: 'fa fa-2x fa-print icon-top-padding',
								text: 'Print',								
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){	
									var frame = Ext.getDom('contentPrintFrame');
									//frame.contentWindow.document.body.innerHTML = Ext.getCmp('contentInfo').body.getHtml();
									//var html = '<div id="print" onclick="window.focus();window.print();"></div>';
									//	html += Ext.getCmp('contentInfo').body.getHtml();									
									var html = Ext.getCmp('contentInfo').body.getHtml();	
									
									frame.contentWindow.document.open();
									frame.contentWindow.document.write(html);
									frame.contentWindow.document.close();
									
									//frame.contentWindow.print();
									//window.print();
								}
							},
							{
								xtype: 'button',
								text: 'Default Template',								
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){									
									Ext.Array.each(Ext.getCmp('customTemplateBtn').getMenu().query('menucheckitem'), function(item){
										item.setChecked(true);
									});
								}								
							},
							{
								xtype: 'button',
								text: 'Custom Template',
								id: 'customTemplateBtn',
								scale: 'large',
								margin: '0 10 0 0',
								menu: []																
							}							
						]
					}
				]
			});

			var printTemplate = new Ext.XTemplate();
			Ext.Ajax.request({
				url: 'Router.action?page=shared/entryPrintTemplate.jsp',
				success: function(response, opts){
					printTemplate.set(response.responseText, true);
					loadEntry();
				}
			});	
			
		

			var contentPanel = Ext.create('Ext.panel.Panel', {
				region: 'center',				
				bodyStyle: 'background: white !important; padding-left: 5px; padding-right: 5px;',
				scrollable: true,					
				items: [
					{
						xtype: 'panel',
						id: 'contentInfo',						
						tpl: printTemplate
					}
				]
			});
		
			
			Ext.create('Ext.container.Viewport', {
				layout: 'border',
				cls: 'printBody',
				style: 'background: white !important;',
				items: [						
					headerPanel,
					contentPanel
				]
			});		
			
			var loadEntry = function(){
				contentPanel.setLoading(true);
				Ext.Ajax.request({
					url: '../api/v1/resource/components/' + componentId + '/detail?type=print',
					callback: function(){
						contentPanel.setLoading(false);							
					},
					success: function(response, opts) {
						var entry = Ext.decode(response.responseText);						
						
						//sort and process						
						Ext.Array.sort(entry.resources, function(a, b){
							return a.resourceTypeDesc.localeCompare(b.resourceTypeDesc);	
						});	
						
						Ext.Array.sort(entry.contacts, function(a, b){
							return a.name.localeCompare(b.name);	
						});		
						
						Ext.Array.sort(entry.dependencies, function(a, b){
							return a.dependencyName.localeCompare(b.dependencyName);	
						});							
						
						var vitals = [];
						if (entry.attributes) {
							Ext.Array.each(entry.attributes, function(item){
								vitals.push({
									label: item.typeDescription,
									value: item.codeDescription,
									highlightStyle: item.highlightStyle,
									type: item.type,
									code: item.code,
									updateDts: item.updateDts,
									tip: item.codeLongDescription ? Ext.util.Format.escape(item.codeLongDescription).replace(/"/g, '') : item.codeLongDescription
								});				
							});
						}

						if (entry.metadata) {
							Ext.Array.each(entry.metadata, function(item){
								vitals.push({
									label: item.label,
									value: item.value,
									updateDts: item.updateDts
								});			
							});
						}

						Ext.Array.sort(vitals, function(a, b){
							return a.label.localeCompare(b.label);	
						});
						entry.vitals = vitals;	
						
						Ext.Array.each(entry.evaluation.evaluationSections, function(section){
							if (section.notAvailable || section.actualScore <= 0) {
								section.display = "N/A";
							} else {
								var score = Math.round(section.actualScore);
								section.display = "";
								for (var i= 0; i<score; i++){
									section.display += '<i class="fa fa-circle detail-evalscore"></i>';
								}
							}				
						});


						Ext.Array.sort(entry.evaluation.evaluationSections, function(a, b){
							return a.name.localeCompare(b.name);	
						});
						
						
						Ext.Array.each(entry.reviews, function(review){
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
						
						
						//build custom menu
						entry.show = {};
						var menuItems = [];
						
						var sections = [
							{ text: 'General', section: 'general' },							
							{ text: 'Views', section: 'views' },
							{ text: 'Badges', section: 'badges' },
							{ text: 'Description', section: 'description' },
							{ text: 'Tags', section: 'tags' },
							{ text: 'Evaluation', section: 'evaluation' },
							{ text: 'Resources', section: 'resources' },
							{ text: 'Vitals', section: 'vitals' },
							{ text: 'Contacts', section: 'contacts' },
							{ text: 'Dependencies', section: 'dependencies' },
							{ text: 'Relationships', section: 'relationships' },
							{ text: 'Reviews', section: 'reviews' },
							{ text: 'Questions', section: 'questions' },
							{ text: 'Media', section: 'media' }							
						];
						
						Ext.Array.each(sections, function(item){
							entry.show[item.section] = true;
							menuItems.push({
								xtype: 'menucheckitem',
								text: item.text,
								checked: true,
								listeners: {
									checkchange: function(menuItem, checked, opts) {	
										entry.show[item.section] = checked;
										Ext.getCmp('contentInfo').update(entry);										
										contentPanel.updateLayout(true, true);
									}
								}
							});							
						});
						
						Ext.getCmp('customTemplateBtn').setMenu({
							items: menuItems,
							listeners: {
								beforerender: function () {
									this.setWidth(this.up('button').getWidth());
								}
							}
						}, true);
						
						Ext.getCmp('contentInfo').update(entry);
						Ext.defer(function(){							
							Ext.getCmp('contentInfo').update(entry);
							contentPanel.updateLayout(true, true);
						}, 500);
					}
				});				
			};			
			
		});
		
	</script>		
		
	</stripes:layout-component>
</stripes:layout-render>	
