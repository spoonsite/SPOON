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
<%-- 
    Document   : visualSearch
    Created on : May 27, 2016, 2:55:46 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../client/layout/usertoolslayout.jsp">
    <stripes:layout-component name="contents">
			
	<script src="scripts/component/visualSearch.js?v=${appVersion}" type="text/javascript"></script>	
	
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */	
		Ext.onReady(function(){	
			
			var visualPanel = Ext.create('OSF.component.VisualContainerPanel', {
				title: 'View Relationships <i class="fa fa-question-circle"  data-qtip="Show relationships amoung entries based on organization, attributes, tags and direct relationships."></i>'
			});
			
			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					visualPanel
				]
			});
			
		});
		
	</script>
		
    </stripes:layout-component>
</stripes:layout-render>	