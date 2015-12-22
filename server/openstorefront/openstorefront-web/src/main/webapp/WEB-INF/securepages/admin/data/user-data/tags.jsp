
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../../client/layout/adminlayout.jsp">
	<stripes:layout-component name="contents">

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {

				Ext.create('Ext.data.Store', {
					storeId: 'tagStore',
					autoLoad: true,
					fields: [
						'componentName',
						'tagId',
						'text',
						'createUser',
						'createDts',
						'securityMarkingType'
					],
					sorters: 'componentName',
					proxy: {
						id: 'tagStoreProxy',
						type: 'ajax',
						url: '../api/v1/resource/components/tagviews'
					}
				});


				Ext.create('Ext.data.Store', {
					storeId: 'securityTypeStore',
					autoLoad: true,
					fields: [
						'code',
						'description'
					],
					proxy: {
						id: 'securityTypeStoreProxy',
						type: 'ajax',
						url: '../api/v1/resource/lookuptypes/SecurityMarkingType/view'
					}
				});



				var tagGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage Tags <i class="fa fa-question-circle"  data-qtip="Tags are user-definable labels that are associated with a component."></i>',
					id: 'tagGrid',
					store: Ext.data.StoreManager.lookup('tagStore'),
					columnLines: true,
					selModel: 'rowmodel',
					columns: {
						defaults: {
							cellWrap: true
						},
						items: [
							{text: 'Component', dataIndex: 'componentName', width: 525},
							{text: 'Tag', dataIndex: 'text', width: 325},
							{text: 'Creator', dataIndex: 'createUser', width: 175},
							{
								text: 'Creation Date',
								dataIndex: 'createDts',
								width: 275,
								xtype: 'datecolumn',
								format: 'm/d/y H:i:s'
							},
							{
								text: 'Security Type', 
								dataIndex: 'securityMarkingType',
								flex: 1,
								renderer: function (value) {
									if (value) {
										var typeStore = Ext.data.StoreManager.lookup('securityTypeStore');
										var record = typeStore.getData().find('code', value);
										return record.data.description;
									}
									else {
										return '';
									}
								}
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
										Ext.getCmp('tagGrid').getStore().load();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Add',
									scale: 'medium',
									iconCls: 'fa fa-2x fa-plus',
									handler: function () {
										actionAddTagForm();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Delete',
									id: 'tagGrid-tools-delete',
									iconCls: 'fa fa-2x fa-trash',
									disabled: true,
									scale: 'medium',
									handler: function () {
										var record = Ext.getCmp('tagGrid').getSelection()[0];
										actionDeleteTag(record);
									}
								}
							]
						}
					],
					listeners: {
						selectionchange: function (grid, record, index, opts) {
							if (Ext.getCmp('tagGrid').getSelectionModel().hasSelection()) {
								Ext.getCmp('tagGrid-tools-delete').enable(true);
							} else {
								Ext.getCmp('tagGrid-tools-delete').disable();
							}
						}
					}
				});


				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						tagGrid
					]
				});
			});

		</script>
	</stripes:layout-component>
</stripes:layout-render>