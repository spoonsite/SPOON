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
<stripes:layout-render name="../../../layout/templateLayout.jsp">
    <stripes:layout-component name="contents">

        <script type="text/javascript">

			var template = {
				blocks: [],
				refresh: function(container, entry) {
					container.removeAll();
					
					Ext.Array.each(template.blocks, function(block){
						block.updateTemplate(entry);	
					});
					
					container.add(template.blocks);	
					Ext.defer(function(){
						container.updateLayout(true, true);
					}, 500);					
				}
			};
			
			/* global Ext, CoreUtil */
			Ext.onReady(function () {
				
			
				var description = Ext.create('OSF.component.template.Description', {
					margin: '0 0 20 0',
					showDescriptionHeader: false
				});								
				
				var media = Ext.create('OSF.component.template.Media', {
					margin: '0 0 20 0'
				});								
				
				var resources = Ext.create('OSF.component.template.Resources', {					
					margin: '0 0 20 0'
				});								
				
				var contacts = Ext.create('OSF.component.template.Contacts', {					
					margin: '0 0 20 0'
				});								
				
				var vitals = Ext.create('OSF.component.template.Vitals', {					
					margin: '0 0 20 0'
				});								
		
				var relatedAttributes = Ext.create('OSF.component.template.RelatedAttributes', {					
					margin: '0 0 20 0'
				})
				
				var relationships = Ext.create('OSF.component.template.Relationships', {					
					margin: '0 0 20 0'
				});	
				
				var detailPanel = Ext.create('OSF.component.template.LayoutScroll', {
					title: 'Details',
					items: [
						description,
						media,
						resources,
						contacts,
						vitals,
						relatedAttributes,
						relationships
					]
				});
	
				var questionPanel = Ext.create('OSF.component.template.Questions', {
				});				
				
				var tabPanel = Ext.create('OSF.component.template.LayoutTab', {
					items: [
						detailPanel,
						questionPanel
					]
				});				
				
							
				template.blocks.push(tabPanel);				
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>	
