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
		<input type="hidden" name="X-Csrf-Token" id="xtoken" />
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
								id: 'printCustomizedEntryBtn',
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
										var xtoken = Ext.getDom('xtoken');
										
										var token = Ext.util.Cookies.get('X-Csrf-Token');						
										if (!token) {
											token ='';							
										}	
										xtoken.value = token;
										
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

			CoreService.brandingservice.getCurrentBranding().then(function(branding){				
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
				},
				failure: function(response, opts) {
					alert('The Print View failed to load!')
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
					// url: 'api/v1/resource/components/' + componentId + '/detail?type=print',
					url: 'api/v1/resource/components/' + componentId + '/detail',
					callback: function(){
						contentPanel.setLoading(false);							
					},
					success: function(response, opts) {
						var entry = Ext.decode(response.responseText);						
						
						entry = CoreUtil.processEntry(entry);
						
						root = entry.componentTypeNestedModel;
						CoreUtil.traverseNestedModel(root, [], entry);

						CoreUtil.calculateEvaluationScore({
							fullEvaluations: entry.fullEvaluations,
							evaluation: entry.fullEvaluations,
							success: function (newData) {
								entry.fullEvaluations = newData.fullEvaluations;

								//build custom menu
								entry.show = {};
								entry.evaluations = [];
								var menuItems = [];
								
								var sections = [
									{ text: 'Badges', section: 'badges' },
									{ text: 'Contacts', section: 'contacts' },
									{ text: 'Dependencies', section: 'dependencies' },
									{ text: 'Description', section: 'description' },
									{ text: 'Evaluation', section: 'evaluation' },
									{ text: 'General', section: 'general' },							
									{ text: 'Media', section: 'media' },
									{ text: 'Questions', section: 'questions' },
									{ text: 'Relationships', section: 'relationships' },
									{ text: 'Resources', section: 'resources' },
									{ text: 'Reviews', section: 'reviews' },
									{ text: 'Tags', section: 'tags' },
									{ text: 'Views', section: 'views' },
									{ text: 'Vitals', section: 'vitals' }
								];

								//Disclaimer 
								entry.disclaimerMessage = CoreService.brandingservice.branding.disclaimerMessage;

								// for each evaluation of this component, add a section for it (indicating it's version)
								for (var ii = 0; ii < entry.fullEvaluations.length; ii += 1) {
									var evalVersion = entry.fullEvaluations.length > 1 ? ' - version: ' + entry.fullEvaluations[ii].evaluation.version : '';
									var rawVersion = entry.fullEvaluations[ii].evaluation.version;

									sections.push({ text: 'Evaluation Details' + evalVersion, section: 'evaluationDetails' + rawVersion, sections: [
										{ text: 'Reusability Factors', section: 'evalReusability' + rawVersion },
										{ text: 'Checklist Summary', section: 'evalChecklistSummary' + rawVersion },
										{ text: 'Recommendations', section: 'evalRecommendations' + rawVersion },
										{ text: 'Content Sections', section: 'evalContentSections' + rawVersion },
										{ text: 'Checklist Details', section: 'evalChecklistDetails' + rawVersion }
									]});
								}
								
								Ext.Array.each(sections, function(item){

									// If the item being pushed is NOT part of evaluation details
									if (item.text.indexOf('Evaluation Details') === -1) {
										entry.show[item.section] = true;
										menuItems.push({
											// xtype: 'menucheckitem',
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
									}

									// If the item being pushed IS part of evaluation details
									else {
										entry.show[item.section] = true;
										var evaluationMenu = {
											text: item.text,
											checked: true,
											xtype: 'menucheckitem',
											menu: {
												xtype: 'menu',
												items: []
											},
											listeners: {
												checkchange: function(menuItem, checked, opts) {	
													entry.show[item.section] = checked;
													Ext.getCmp('contentInfo').update(entry);										
													contentPanel.updateLayout(true, true);
												}
											}
										};

										// push each subsection of the evaluation detail
										Ext.Array.each(item.sections, function (subItem) {
											entry.show[subItem.section] = true;
											evaluationMenu.menu.items.push({
												xtype: 'menucheckitem',
												text: subItem.text,
												checked: true,
												listeners: {
													checkchange: function(menuItem, checked, opts) {	
														entry.show[subItem.section] = checked;
														Ext.getCmp('contentInfo').update(entry);										
														contentPanel.updateLayout(true, true);
													}
												}
											});
										});
										menuItems.push(evaluationMenu);	
									}
								});
								
								Ext.getCmp('customTemplateBtn').setMenu({
									items: menuItems,
									listeners: {
										beforerender: function () {
											this.setWidth(this.up('button').getWidth()*2);
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

						
					}
				});				
			};			
			
		});
		
	</script>		
		
	</stripes:layout-component>
</stripes:layout-render>	
