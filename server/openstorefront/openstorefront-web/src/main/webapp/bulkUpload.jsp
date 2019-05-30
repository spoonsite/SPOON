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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
	
	
		
	<!-- <form id="printForm" action="Router.action?Echo&print=true"  method="POST">		
		<input type="hidden" name="content" id="printContent" />
		<input type="hidden" name="X-Csrf-Token" id="xtoken" />
	</form> -->
	
	<script type="text/javascript">
		/* global Ext, CoreService, CoreApp */				
	
		
		Ext.onReady(function(){

			// var printTemplate = new Ext.XTemplate();
			// Ext.Ajax.request({
			// 	url: 'Router.action?page=shared/entryPrintTemplate.jsp',
			// 	success: function(response, opts){
			// 		printTemplate.set(response.responseText, true);
			// 		loadEntry();
			// 	},
			// 	failure: function(response, opts) {
			// 		alert('The Print View failed to load!')
			// 	}
			// });	
			
		
			
				// var importWin = Ext.create('Ext.window.Window', {
					// title: 'Bulk Upload',
					// modal: true,
					// closeAction: 'destroy',
					// width: 500,
					// height: 260,
					// layout: 'fit',
					// items: [
				var inputForm = Ext.create('Ext.panel.Panel', {
					title:'Bulk Uploads',
					width:	500,
					height: 310,
					items: [
						{
							xtype: 'form',
							scollable: true,
							bodyStyle: 'padding: 10px;',
							layout: 'anchor',
							defaults: {
								labelAlign: 'top',
								labelSeparator: '',
								width: '100%'
							},
							items: [
								{
									xtype: 'fileFieldMaxLabel', 
									name: 'uploadFile'
								}
							],
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											text: 'Import',
											iconCls: 'fa fa-lg fa-upload icon-button-color-default',
											formBind: true,
											requiredPermissions: ['USER-SUBMISSIONS-CREATE'],
											handler: function() {
												var uploadForm = this.up('form');
												//var data = uploadForm.getValues();
												var progressMsg = Ext.MessageBox.show({
													title: 'Archive Import',
													msg: 'Importing archive please wait...',
													width: 300,
													height: 150,
													closable: false,
													progressText: 'Importing...',
													wait: true,
													waitConfig: {interval: 300}
												});
												uploadForm.submit({
													submitEmptyText: false,
													url: 'Upload.action?BulkUpload',
													success: function(form, action) {
														Ext.toast('File has been queued for processing.', 'Upload Successfully', 'br');
														progressMsg.hide();
														importWin.close();
													}, 
													failure: function(form, action) {
														progressMsg.hide();
													}
												});	
											}
										},
										{
											xtype: 'tbfill'
										},
										{
											text: 'Close',
											iconCls: 'fa fa-lg fa-close icon-button-color-warning',											
											handler: function() {
												this.up('window').close();
											}												
										}
									]
								}
							]
						}	
					]
				});		
					// ]
				// });
				// importWin.show();		
						

			var viewport = Ext.create('Ext.container.Viewport', {
				layout: 'border',
				cls: 'printBody',
				style: 'background: white !important;',
				items: [
					inputForm
				]
			});		
			
			
		});
	</script>		
		
	</stripes:layout-component>
</stripes:layout-render>	
