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
		
	<form id="printForm" action="Router.action?Echo&print=true"  method="POST">		
		<input type="hidden" name="content" id="printContent" />
	</form>
	
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */				
	
		
		Ext.onReady(function(){	
			
			var componentId = '${param.id}';
			
			var headerPanel = Ext.create('Ext.panel.Panel', {
				region: 'north',				
				cls: 'hidePrint',
				id: 'topNavPanel',
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
									
									if (Ext.isIE) {
										var printForm = Ext.getDom('printForm');
										var printContent = Ext.getDom('printContent');
										var completeHtml = '<!DOCTYPE html> ' + html;
										
										printContent.value = completeHtml;										
										printForm.submit();
									} else {									
										frame.contentWindow.document.open();
										frame.contentWindow.document.write(html);
										frame.contentWindow.document.close();
									}
									
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

			CoreService.brandingservice.getCurrentBranding().then(function(response, opts){
				var branding = Ext.decode(response.responseText);
				if (branding.securityBannerText && branding.securityBannerText !== '') {
					Ext.getCmp('topNavPanel').addDocked(CoreUtil.securityBannerPanel({
						securityBannerText: branding.securityBannerText
					}), 0);
				}
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
		
			
			var viewport = Ext.create('Ext.container.Viewport', {
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
						
						entry = CoreUtil.processEntry(entry);
						
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
							//Ext.getCmp('contentInfo').update(entry);
							contentPanel.updateLayout(true, true);
							viewport.updateLayout(true, true);
						}, 500);
					}
				});				
			};			
			
		});
		
	</script>		
		
	</stripes:layout-component>
</stripes:layout-render>	
