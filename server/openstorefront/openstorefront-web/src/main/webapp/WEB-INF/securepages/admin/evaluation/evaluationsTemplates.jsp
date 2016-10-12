<%-- 
    Document   : evaluationTemplates
    Created on : Oct 11, 2016, 2:29:27 PM
    Author     : dshurtleff
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	<stripes:layout-render name="../../../../layout/adminheader.jsp">		
	</stripes:layout-render>		
		
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
			
			var templateGrid = Ext.create('Ext.grid.Panel', {
				title: 'Manage Evaluation Templates <i class="fa fa-question-circle"  data-qtip="Top level template used to create evaluations."></i>',
				columnLines: true,
				columns: [
					{ text: 'Name', dataIndex: 'name', flex: 1},
					{ text: 'Description', dataIndex: 'description', align: 'center', width: 225 },					
					{ text: 'Create Date', dataIndex: 'createDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Create User', dataIndex: 'createUser', width: 175 },
					{ text: 'Update Date', dataIndex: 'updateDts', xtype: 'datecolumn', format:'m/d/y H:i:s',  width: 175 },
					{ text: 'Update User', dataIndex: 'updateUser', width: 175 }
				],
				dockedItems: [
					{
						dock: 'top',
						xtype: 'toolbar',
						items: [
							{
								text: 'Refresh',
								iconCls: 'fa fa-2x fa-refresh',
								scale: 'medium',
								handler: function(){
									
								}
							},
							{
								xtype: 'tbseparator'
							},
							{
								text: 'Create',
								iconCls: 'fa fa-2x fa-plus',
								scale: 'medium',
								handler: function(){
									
								}
							},							
							{
								text: 'Edit',
								iconCls: 'fa fa-2x fa-edit',
								scale: 'medium',
								handler: function(){
									
								}
							},														
							{
								xtype: 'tbfill'
							},																				
							{
								text: 'Delete',
								iconCls: 'fa fa-2x fa-close',
								scale: 'medium',
								handler: function(){
									
								}
							}					
							
						]
					}
				]				
			});
			
			addComponentToMainViewPort(templateGrid);				
		
		});
		
	</script>

    </stripes:layout-component>
</stripes:layout-render>
