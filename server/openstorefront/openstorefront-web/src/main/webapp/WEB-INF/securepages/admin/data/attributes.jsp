<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">
	
	<script type="text/javascript">
		/* global Ext, CoreUtil */
		Ext.onReady(function(){	
	

			var attributeStore = Ext.create('Ext.data.Store', {
				id: 'attributeStore',
				autoLoad: true,
				sorters: [
					new Ext.util.Sorter({
						property: 'description',
						direction: 'ASC'
					})
				],	
				proxy: {
					type: 'ajax',
					url: '/openstorefront/api/v1/resource/attributes/attributetypes?all=true',
					reader: {
						type: 'json',
						rootProperty: 'data'
					}
				}
			});


			var gridColorRenderer = function gridColorRenderer(value, metadata, record) {
				if (value) 
					metadata.tdCls = 'alert-success';
				else 
					metadata.tdCls = 'alert-danger';
				return value;
			};


			var attributeGrid = Ext.create('Ext.grid.Panel', {
				id: 'attributeGrid',
				title: 'Manage Attributes <i class="fa fa-question-circle"  data-qtip="Attributes are used to categorize components and other listings. They can be searched on and filtered. They represent the metadata for a listing. Attribute Types represent a category and a code represents a specific value. The data is linked by the type and code which allows for a simple change of the description."></i>',
				store: 'attributeStore',
				listeners: {
					selectionchange: function (grid, record, index, opts) {
						if (Ext.getCmp('attributeGrid').getSelectionModel().hasSelection()) {
							Ext.getCmp('attributeGrid-tools-edit').enable();
							Ext.getCmp('attributeGrid-tools-toggleActivation').enable();
							if (record[0].data.activeStatus === 'A') {
								Ext.getCmp('attributeGrid-tools-toggleActivation').setText('Deactivate');
							}
							else {
								Ext.getCmp('attributeGrid-tools-toggleActivation').setText('Activate');
							}
							Ext.getCmp('attributeGrid-tools-delete').enable();
						} else {
							Ext.getCmp('attributeGrid-tools-edit').disable();
							Ext.getCmp('attributeGrid-tools-toggleActivation').disable();
							Ext.getCmp('attributeGrid-tools-delete').disable();
						}
					}
				},
				columnLines: true,
				columns: [
					{text: 'Description', dataIndex: 'description', flex: 2},
					{text: 'Type Code', dataIndex: 'attributeType', flex: 2},
					{
						text: 'Visible', 
						dataIndex: 'visibleFlg', 
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Required',
						dataIndex: 'requiredFlg',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Important',
						dataIndex: 'importantFlg',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Architecture',
						dataIndex: 'architectureFlg',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Allow Multiple',
						dataIndex: 'allowMultipleFlg',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Hide On Submission',
						dataIndex: 'hideOnSubmission',
						flex: 1, 
						renderer: gridColorRenderer
					},
					{
						text: 'Default Code',
						dataIndex: 'defaultAttributeCode',
						flex: 1
					},
					{
						text: 'Status',
						dataIndex: 'activeStatus',
						align: 'center',
						flex: 0.5
					}
				],
				dockedItems: [
					{
						dock: 'top',
						xtype: 'toolbar',
						items: [
							Ext.create('OSF.component.StandardComboBox', {
								id: 'attributeFilter-activeStatus',
								emptyText: 'Show All',
								fieldLabel: 'Active Status',
								name: 'activeStatus',
								listeners: {
									change: function (filter, newValue, oldValue, opts) {
										if (newValue === 'A') {
											attributeStore.filter('activeStatus','A');
										}
										else {
											attributeStore.filter('activeStatus', 'I');
										}
									},
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
						xtype: 'toolbar',
						dock: 'top',
						items: [
							{
								text: 'Refresh',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-refresh',
								handler: function () {
									attributeStore.load();
								}
							},
							{ 
								xtype: 'tbseparator'
							},
							{
								text: 'Add New Type',
								id: 'attributeGrid-tools-add',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-plus',
								handler: function() {
								}
							},
							{
								text: 'Edit',
								id: 'attributeGrid-tools-edit',
								scale: 'medium',
								disabled: true,
								iconCls: 'fa fa-2x fa-edit',
								handler: function() {
								}
							},
							{
								text: 'Deactivate',
								id: 'attributeGrid-tools-toggleActivation',
								scale: 'medium',
								disabled: true,
								iconCls: 'fa fa-2x fa-power-off',
								handler: function() {
								}
							},
							{
								text: 'Delete',
								id: 'attributeGrid-tools-delete',
								scale: 'medium',
								disabled: true,
								iconCls: 'fa fa-2x fa-trash',
								handler: function() {
								}
							},
							{
								xtype: 'tbfill',
							},
							{
								text: 'Import',
								id: 'attributeGrid-tools-import',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-upload',
								handler: function() {
								}
							},
							{
								text: 'Export',
								id: 'attributeGrid-tools-export',
								scale: 'medium',
								iconCls: 'fa fa-2x fa-download',
								handler: function() {
								}
							}
						]
					}
				]
			});

			Ext.create('Ext.container.Viewport', {
				layout: 'fit',
				items: [
					attributeGrid
				]
			});


		});		
		</script>
		</stripes:layout-component>
		</stripes:layout-render>
