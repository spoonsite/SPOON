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
									window.print();
								}
							},
							{
								xtype: 'button',
								text: 'Default Template',								
								scale: 'large',
								margin: '0 10 0 0',
								handler: function(){									
									
								}								
							},
							{
								xtype: 'button',
								text: 'Custom Template',
								id: 'customTemplateBtn',
								scale: 'large',
								margin: '0 10 0 0',
								menu: [],
								handler: function(){									
									
								}								
							}							
						]
					}
				]
			});

			var contentPanel = Ext.create('Ext.panel.Panel', {
				region: 'north',
				bodyStyle: 'background: white;',
				scrollable: true
			});
		
			
			Ext.create('Ext.container.Viewport', {
				layout: 'border',
				items: [						
					headerPanel,
					contentPanel
				]
			});			
			
		});
		
	</script>		
		
	</stripes:layout-component>
</stripes:layout-render>	
