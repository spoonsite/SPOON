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

		<script type="text/javascript">
			/* global Ext, CoreService, CoreApp */

			Ext.onReady(function () {
				CoreService.brandingservice.getCurrentBranding().then(function(branding){
					var defaultMessage = 'This bulk upload tool is designed to help you submit a part or parts into our database.  '+
												'You can upload a zip file containing PDFs, excel spreadsheets, or other human readable files.  '+
												'The SPOON support team will then do all the data entry for you.<br><br>'+

												'Once SPOON support is done entering your information, you will then need to review and submit '+
												'the information for Subject Matter Expert (SME) review.  Once the SME has approved your '+
												'information your part will become searchable on the site.<br><br>'+

												'<strong style="color:red;">The information submitted to this site will be made publicly available.  Please do not submit '+
												'any sensitive information such as proprietary or ITAR restricted information.</strong><br><br>'
					var message = branding.bulkUploadMessage;
					if (message == null || message == ""){
						message = defaultMessage;
					}
					var inputForm = Ext.create('Ext.panel.Panel', {
						title: 'Bulk Uploads',
						width: 500,
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
										xtype:'label',
										html: message
									},
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
												text: 'Upload',
												iconCls: 'fa fa-lg fa-upload icon-button-color-default',
												formBind: true,
												requiredPermissions: ['USER-SUBMISSIONS-CREATE'],
												handler: function () {
													var uploadForm = this.up('form');

													var progressMsg = Ext.MessageBox.show({
														title: 'Bulk Upload',
														msg: 'Uploading file please wait...',
														width: 300,
														height: 150,
														closable: false,
														progressText: 'Uploading...',
														wait: true,
														waitConfig: { interval: 500 }
													});

													uploadForm.submit({
														submitEmptyText: false,
														url: 'Upload.action?BulkUpload',
														success: function (form, action) {
															progressMsg.hide();
															Ext.Msg.alert('Bulk Upload', 'File uploaded successfully.', function () { window.close(); });
														},
														failure: function (form, action) {
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
												handler: function () {
													window.close();
												}
											}
										]
									}
								]
							}
						]
					});

					var viewport = Ext.create('Ext.container.Viewport', {
						layout: 'border',
						cls: 'printBody',
						style: 'background: white !important;',
						items: [ inputForm ]
					});
				});
			});
		</script>
	</stripes:layout-component>
</stripes:layout-render>