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

			var attributeGrid = Ext.create('Ext.grid.Panel', {
				id: 'attributeGrid',
				title: 'Manage Attributes <i class="fa fa-question-circle"  data-qtip="Attributes are used to categorize components and other listings. They can be searched on and filtered. They represent the metadata for a listing. Attribute Types represent a category and a code represents a specific value. The data is linked by the type and code which allows for a simple change of the description."></i>',
				store: 'attributeStore',
				columnLines: true,
				columns: [
					{text: 'Description', dataIndex: 'description', flex: 1},
					{text: 'Type Code', dataIndex: 'attributeType', flex: 1},

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
