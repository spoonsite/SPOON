<%--
/* 
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<script src="scripts/component/advanceSearch.js?v=${appVersion}" type="text/javascript"></script>
	<script src="scripts/component/searchToolContentPanel.js?v=${appVersion}" type="text/javascript"></script>
	

	<div id="browserWarning" class="browser-warning" >
		 <p>You are using an <strong>unsupported</strong> browser. The website may not work as intended.  Please switch to <strong>
		 <a class="browser-warning-link" href="http://www.mozilla.org/en-US/firefox/new/">Firefox</a></strong> or <strong>
		 <a class="browser-warning-link" href="https://www.google.com/intl/en-US/chrome/browser/">Chrome</a></strong>, or <strong>
		 <a class="browser-warning-link" href="http://browsehappy.com/">upgrade your browser</a></strong> to improve your experience</p>		
	</div>
	
	<script type="text/javascript">
		//start fresh on index
		sessionStorage.clear();
				
		if (Ext.isIE10m) {
			Ext.get('browserWarning').setStyle({
				display: 'block'
			});
		} 
			
		${actionBean.landingTemplate}		
			
		
	</script>	
		
    </stripes:layout-component>
</stripes:layout-render>		
		