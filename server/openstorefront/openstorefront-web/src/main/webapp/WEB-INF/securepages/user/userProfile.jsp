<%-- 
    Document   : userProfile
    Created on : Dec 18, 2015, 2:57:20 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../client/layout/usertoolslayout.jsp">
    <stripes:layout-component name="contents">

        <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				
				var profilePanel = Ext.create('OSF.component.UserProfilePanel', {					
					padding: '0 120 0 120',
					defaults: {
						labelAlign: 'right'
					}
				});
				
				var mainPanel = Ext.create('Ext.panel.Panel',{
					title: 'User Profile',
					iconCls: 'fa fa-user',
					items: [
						profilePanel
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
