<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
	
	<script src="scripts/component/importWindow.js?v=${appVersion}" type="text/javascript"></script>	
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	




			var componentConfigGrid = Ext.create('Ext.grid.Panel', {
				title: 'Component Configuration'
			});

			var jiraConfigGrid = Ext.create('Ext.grid.Panel', {
				title: 'Jira Configuration'
			});


			var mainPanel = Ext.create('Ext.tab.Panel', {
				title: 'Manage Integration <i class="fa fa-question-circle"  data-qtip="Allows for the configuration of data integration with external systems such as JIRA"></i>',
				items: [
					componentConfigGrid,
					jiraConfigGrid
				]
			});

				
			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					mainPanel
				]
			});
			


		});		
	</script>	
		
	</stripes:layout-component>
</stripes:layout-render>	
