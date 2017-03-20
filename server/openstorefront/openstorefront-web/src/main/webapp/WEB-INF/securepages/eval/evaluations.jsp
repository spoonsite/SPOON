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

    Document   : evaluation
    Created on : Mar 20, 2017, 2:12:17 PM
    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../layout/${actionBean.headerPage}">		
		</stripes:layout-render>			
		
        <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				
				var evaluationGrid = Ext.create('Ext.grid.Panel', {					
					title: 'Evaluation &nbsp; <i class="fa fa-lg fa-question-circle"  data-qtip="Allow editing evaluations for entries" ></i>',										
					columnLines: true,
					store: {
						
					},
					columns: [
						{ text: '', dataIndex: '', width: 200 },
					]
				});
				
				addComponentToMainViewPort(evaluationGrid);
				
				
			});

        </script>

    </stripes:layout-component>
</stripes:layout-render>		
