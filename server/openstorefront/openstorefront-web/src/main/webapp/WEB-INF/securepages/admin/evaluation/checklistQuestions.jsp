<%-- 
    Document   : checklistQuestion
    Created on : Oct 11, 2016, 2:30:22 PM
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
			
			var questionGrid = Ext.create('Ext.grid.Panel', {
				title: 'Manage Checklist Questions <i class="fa fa-question-circle"  data-qtip="Manage checklist question pool; questions are tied to responses and templates."></i>',
				columnLines: true,
				columns: [
					{ text: 'QID', dataIndex: 'qid', align: 'center', width: 125},				
					{ text: 'Section', dataIndex: 'evaluationSection', align: 'center', width: 200},				
					{ text: 'Question', dataIndex: 'question', flex: 1},	
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
								text: 'Add',
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
								xtype: 'tbseparator'
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
			
			addComponentToMainViewPort(questionGrid);				
		
		});
		
	</script>

    </stripes:layout-component>
</stripes:layout-render>
