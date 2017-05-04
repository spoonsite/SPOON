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

    Document   : watches
    Created on : Mar 1, 2016, 1:05:15 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../layout/userheader.jsp">		
		</stripes:layout-render>			
		
		<script src="scripts/component/userwatchPanel.js?v=${appVersion}" type="text/javascript"></script>	

		<script type="text/javascript">
			 /* global Ext, CoreUtil */

			 Ext.onReady(function() {
				
				 var watchPanel = Ext.create('OSF.component.UserWatchPanel', {
					 title: 'Manage Watches <i class="fa fa-lg fa-question-circle"  data-qtip="Watches are your way of recieving notification of changes to components in this site."></i>'					
				 });
				
				 addComponentToMainViewPort(watchPanel);
				 
			 });
			
		</script>
		
	</stripes:layout-component>
</stripes:layout-render>	
