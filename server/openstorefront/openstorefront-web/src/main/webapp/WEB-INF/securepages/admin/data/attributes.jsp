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
					url: '/openstorefront/api/v1/resource/attributes/attributetypes',
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
				columnLines: true,
				columns: [
					{text: 'Description', dataIndex: 'description', flex: 3},
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
					}
				],
				dockedItems: [
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
