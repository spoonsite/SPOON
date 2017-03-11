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
<stripes:layout-render name="../../../../../layout/toplevelLayout.jsp">
	<stripes:layout-component name="contents">

		<stripes:layout-render name="../../../../../layout/adminheader.jsp">		
		</stripes:layout-render>		
		
		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				Ext.create('Ext.data.Store', {
					storeId: 'reviewStore',
					autoLoad: true,
					fields: [
						'name',
						'title',
						'rating',
						'comment',
						'pros',
						'cons',
						'user',
						'updateDate',
						'securityMarkingType'
					],
					proxy: {
						id: 'reviewStoreProxy',
						type: 'ajax',
						url: 'api/v1/resource/components/reviewviews'
					}
				});


				var reviewGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage User Reviews <i class="fa fa-question-circle"  data-qtip="User reviews and ratings about a component."></i>',
					id: 'reviewGrid',
					store: Ext.data.StoreManager.lookup('reviewStore'),
					columnLines: true,
					selModel: 'rowmodel',
					columns: {
						defaults: {
							cellWrap: true
						},
						items: [
							{text: 'Component', dataIndex: 'name', width: 225},
							{text: 'Title', dataIndex: 'title', width: 225},
							{text: 'Rating', dataIndex: 'rating', width: 75, align: 'center'},
							{text: 'Comment', dataIndex: 'comment', flex: 1, minWidth: 300},
							{
								text: 'Pros', 
								dataIndex: 'pros', 
								sortable: false,
								width: 200,
								renderer: function (value) {
									var pros = '<ul>';
									Ext.Array.each(value, function(item) {
										pros += '<li>' + item.text + '</li>';
									});
									pros += '</ul>';
									return pros;
								}
							},
							{
								text: 'Cons',
								dataIndex: 'cons',
								sortable: false,
								width: 200,
								renderer: function (value) {
									var cons = '<ul>';
									Ext.Array.each(value, function(item) {
										cons += '<li>' + item.text + '</li>';
									});
									cons += '</ul>';
									return cons;
								}
							},
							{text: 'User', dataIndex: 'username', width: 100},
							{
								text: 'Update Time',
								dataIndex: 'updateDate',
								width: 150,
								xtype: 'datecolumn',
								format: 'm/d/y H:i:s'
							},
							{text: 'Security Type', dataIndex: 'securityMarkingDescription', width: 150, hidden: !${branding.allowSecurityMarkingsFlg}}
						]
					},
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'reviewFilter-ActiveStatus',
									emptyText: 'Active',
									fieldLabel: 'Active Status',
									name: 'activeStatus',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											if (newValue) {
												var store = Ext.data.StoreManager.lookup('reviewStore');
												if (newValue === 'A') {
													store.setProxy({
														id: 'reviewStoreProxy',
														type: 'ajax',
														url: 'api/v1/resource/components/reviewviews?status=A'
													});
													Ext.getCmp('reviewGrid-tools-toggleActivation').setText("Toggle Status");
												} else {
													store.setProxy({
														id: 'reviewStoreProxy',
														type: 'ajax',
														url: 'api/v1/resource/components/reviewviews?status=I'
													});
													Ext.getCmp('reviewGrid-tools-toggleActivation').setText("Toggle Status");
												}
												store.load();
												Ext.getCmp('reviewGrid').getSelectionModel().deselectAll();
												Ext.getCmp('reviewGrid-tools-toggleActivation').setDisabled(true);
											}
										}
									},
									storeConfig: {
										customStore: {
											fields: [
												'code',
												'description'
											],
											data: [
												{
													code: 'A',
													description: 'Active'
												},
												{
													code: 'I',
													description: 'Inactive'
												}
											]
										}
									}
								})]
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									width: '120px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
									handler: function () {
										Ext.getCmp('reviewGrid').getStore().load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Toggle Status',
									id: 'reviewGrid-tools-toggleActivation',
									iconCls: 'fa fa-2x fa-power-off icon-button-color-default icon-vertical-correction',
									disabled: true,
									scale: 'medium',
									handler: function () {
										var record = Ext.getCmp('reviewGrid').getSelection()[0];
										actionToggleActivation(record);
									}
								}
							]
						}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('reviewGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('reviewGrid-tools-toggleActivation').enable(true);
							} else {
								Ext.getCmp('reviewGrid-tools-toggleActivation').disable();
							}
						}
					}
				});


				var actionToggleActivation = function (record) {
					if (record) {
						var reviewId = record.data.reviewId;
						var componentId = record.data.componentId;
						var active = record.data.activeStatus;
						if (active === 'A') {
							var method = "DELETE";
							var url = 'api/v1/resource/components/';
							url += componentId + '/reviews/';
							url += reviewId;
							var what = "deactivate";
						} else if (active === 'I') {
							var method = "PUT";
							var url = 'api/v1/resource/components/';
							url += componentId + '/reviews/';
							url += reviewId + '/activate';
							var what = "activate";
						} else {
							Ext.MessageBox.alert("Record Not Recognized", "Error: Record is not active or inactive.");
							return false;
						}

						Ext.Ajax.request({
							url: url,
							method: method,
							success: function (response, opts) {
								var message = 'Successfully ' + what + 'd review for "' + record.data.name + '"';
								Ext.toast(message, '', 'tr');
								// The ordering below is necessary
								// to get Ext to disable the buttons.
								Ext.getCmp('reviewGrid').getStore().load();
								Ext.getCmp('reviewGrid').getSelectionModel().deselectAll();
								Ext.getCmp('reviewGrid-tools-toggleActivation').disable();
							},
							failure: function (response, opts) {
								Ext.MessageBox.alert('Failed to' + what,
										"Error: Could not " + what + ' review for "' + record.data.name + '"');
							}
						});

					} else {
						Ext.MessageBox.alert("No Review Selected", "Error: You have not selected a review.");
					}

				};

				addComponentToMainViewPort(reviewGrid);
				
			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>