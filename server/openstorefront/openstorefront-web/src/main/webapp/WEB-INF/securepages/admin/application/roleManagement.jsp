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
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../layout/adminheader.jsp">		
		</stripes:layout-render>	
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				
				
				var roleGrid = Ext.create('Ext.grid.Panel', {
					title: 'Security Role Management <i class="fa fa-question-circle"  data-qtip="Manage security roles that allow access to features in the application."></i>',
					columnLines: true,
					store: {
						autoLoad: true,
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/securityroles'
						}
					},
					columns: [
						{ text: 'Name', dataIndex: 'roleName', width: 200 },
						{ text: 'Description', dataIndex: 'description',flex: 1, minWidth: 200 }
						
					],
					listeners: {
						selectionChange: function(selModel, records, opts) {
							
						}
					},
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'top',
							items: [
								{
									text: 'Refresh',
									iconCls: 'fa fa-2x fa-refresh',
									scale: 'medium',
									handler: function() {
										
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									iconCls: 'fa fa-2x fa-plus',
									scale: 'medium',
									handler: function() {
										
									}
								},								
								{
									text: 'Edit',
									iconCls: 'fa fa-2x fa-edit',
									scale: 'medium',
									handler: function() {
										
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Manage Permissions',
									iconCls: 'fa fa-2x fa-key',
									scale: 'medium',
									handler: function() {
										
									}
								},								
								{
									text: 'Manage Data Restrictions',
									iconCls: 'fa fa-2x fa-legal',
									scale: 'medium',
									handler: function() {
										
									}
								},								
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									iconCls: 'fa fa-2x fa-trash',
									scale: 'medium',
									handler: function() {
										
									}									
								}
							]
						}
					]
					
				});
				
				addComponentToMainViewPort(roleGrid);
				
			});
			
        </script>

    </stripes:layout-component>
</stripes:layout-render>
			