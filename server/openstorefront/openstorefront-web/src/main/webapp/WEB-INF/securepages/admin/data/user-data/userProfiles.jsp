
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				var userProfileStore = Ext.create('Ext.data.Store', {
					storeId: 'userProfileStore',
					autoLoad: true,
					proxy: {
						id: 'userProfileStoreProxy',
						type: 'ajax',
						reader: {
							type: 'json',
							rootProperty: 'data'
						},
						url: '/openstorefront/api/v1/resource/userprofiles'
					}
				});

				var userTypeStore = Ext.create('Ext.data.Store', {
					storeId: 'userTypeStore',
					autoLoad: true,
					fields: ['code', 'description'],
					proxy: {type: 'ajax', url: '../api/v1/resource/lookuptypes/UserTypeCode/view'}
				});

				var getUserType = function getUserType(code) {
					if (code)
						return userTypeStore.getData().find('code', code).data.description;
					return '';
				};


				var userProfileGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage User Profiles <i class="fa fa-question-circle"  data-qtip="A user profile represents a user in the system and contains the user\'s information."></i>',
					id: 'userProfileGrid',
					store: userProfileStore,
					columnLines: true,
					selModel: 'rowmodel',
					columns: {
						defaults: {
							cellWrap: true
						},
						items: [
							{
								flex: 1,
								text: 'Username',
								dataIndex: 'username'
							},
							{
								flex: 1.5,
								text: 'First Name', 
								dataIndex: 'firstName'
							},
							{
								flex: 1.5,
								text: 'Last Name', 
								dataIndex: 'lastName'
							},
							{
								flex: 1.5,
								text: 'Organization', 
								dataIndex: 'organization'
							},
							{
								flex: 1,
								text: 'User Type', 
								flex: 1.5,
								dataIndex: 'userTypeCode',
								renderer: function (value, metaData, record) {
									return getUserType(value);
								}
							},
							{
								flex: 2,
								text: 'Last Login', 
								dataIndex: 'lastLoginDts',
								xtype: 'datecolumn',
								format: 'm/d/y H:i:s'
							},
							{
								flex: 3,
								text: 'Email', 
								dataIndex: 'email'
							},
							{
								flex: 5,
								text: 'GUID', 
								dataIndex: 'guid'
							}
						]
					},
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								Ext.create('OSF.component.StandardComboBox', {
									id: 'userProfileGrid-filter-ActiveStatus',
									emptyText: 'Active',
									fieldLabel: 'Active Status',
									name: 'activeStatus',
									listeners: {
										change: function (filter, newValue, oldValue, opts) {
											if (newValue) {
												var store = userProfileStore;
												var url = '/openstorefront/api/v1/resource/userprofiles?';
												if (newValue === 'A') {
													url += 'status=A';
													Ext.getCmp('userProfileGrid-tools-toggleActivation').setText("Deactivate");
												} else {
													url += 'status=I';
													Ext.getCmp('userProfileGrid-tools-toggleActivation').setText("Activate");
												}
												store.setProxy({
													type: 'ajax',
													url: url,
													reader: {
														type: 'json',
														rootProperty: 'data'
													}
												});
												store.load();
												Ext.getCmp('userProfileGrid').getSelectionModel().deselectAll();
												Ext.getCmp('userProfileGrid-tools-edit').disable();
												Ext.getCmp('userProfileGrid-tools-toggleActivation').disable();
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
								})
							]
						},
						{
							dock: 'top',
							xtype: 'toolbar',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										userProfileStore.load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									id: 'userProfileGrid-tools-edit',
									disabled: true,
									scale: 'medium',
									iconCls: 'fa fa-2x fa-edit',
									handler: function () {
										var record = Ext.getCmp('userProfileGrid').getSelection()[0];
										actionEditUser(record);
									}
								},
								{
									text: 'Deactivate',
									id: 'userProfileGrid-tools-toggleActivation',
									iconCls: 'fa fa-2x fa-power-off',
									disabled: true,
									scale: 'medium',
									handler: function () {
										var record = Ext.getCmp('userProfileGrid').getSelection()[0];
										actionToggleUser(record);
									}
								}
							]
						}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {

						}
					}
				});



				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						userProfileGrid
					]
				});
			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>