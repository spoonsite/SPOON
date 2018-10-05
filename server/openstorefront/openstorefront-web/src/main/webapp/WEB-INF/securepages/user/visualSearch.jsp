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
<%-- 
    Document   : visualSearch
    Created on : May 27, 2016, 2:55:46 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
			
		<stripes:layout-render name="../../../layout/userheader.jsp">		
		</stripes:layout-render>			
		<script src="scripts/component/visualSearch.js?v=${appVersion}" type="text/javascript"></script>	
	
		<script type="text/javascript">
			/* global Ext, CoreService, CoreApp */	
			Ext.onReady(function(){	

				var initViewOptions = Ext.Object.fromQueryString(window.location.search);

				var visualPanel = Ext.create('OSF.component.VisualContainerPanel', {
					title: 'View Relationships <i class="fa fa-lg fa-question-circle"  data-qtip="Show relationships among entries based on organization, attributes, tags and direct relationships."></i>',					
					visualPanelConfig: {
						viewType: initViewOptions ? null : 'RELATION',
						promptForType: false
					}
				});
				
				addComponentToMainViewPort(visualPanel);
				
				//init view if requested	
				if (initViewOptions && initViewOptions.viewType) {
					visualPanel.setViewType(initViewOptions.viewType, false);
					
					if (initViewOptions.viewType === 'RELATION') {
						visualPanel.visualPanel.loadRelationships(initViewOptions.entityId, initViewOptions.entityName, 0);
					} else if (initViewOptions.viewType === 'ORG') {
						visualPanel.visualPanel.loadOrganizations(initViewOptions.entityId, initViewOptions.entityName);
					} else if (initViewOptions.viewType === 'ATT') {
						visualPanel.visualPanel.loadAttributes(initViewOptions.entityId, initViewOptions.entityName);
					} else if (initViewOptions.viewType === 'TAGS') {
						visualPanel.visualPanel.loadTags(initViewOptions.entityId);
					}
				} else {
					visualPanel.visualPanel.promptForType = true;
					visualPanel.visualPanel.loadRelationships();
				}

			});

		</script>
		
    </stripes:layout-component>
</stripes:layout-render>	