
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
								dataIndex: 'userTypeCode'
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