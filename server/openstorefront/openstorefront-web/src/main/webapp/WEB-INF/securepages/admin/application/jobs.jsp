<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../../client/layout/adminlayout.jsp">
    <stripes:layout-component name="contents">

		<script type="text/javascript">
			/* global Ext, CoreUtil */
			Ext.onReady(function () {


				var jobStore = Ext.create('Ext.data.Store', {
					storeId: 'jobStore',
					autoLoad: true,
					proxy: {
						type: 'ajax',
						url: '/openstorefront/api/v1/service/jobs'
					}
				});

				var jobGrid = Ext.create('Ext.grid.Panel', {
					title: 'Manage Jobs <i class="fa fa-question-circle"  data-qtip="Control and view scheduled jobs and background tasks."></i>',
					id: 'jobGrid',
					store: jobStore,
					columnLines: true,
					columns: [
						{text: 'Job Name', dataIndex: 'jobName', flex: 1}
					],
					dockedItems: [
					],
					listeners: {
						itemdblclick: function (grid, record, item, index, e, opts) {
							actionEditCodes(record);
						},
						selectionchange: function (grid, record, eOpts) {
							if (Ext.getCmp('jobGrid').getSelectionModel().hasSelection()) {
								
								
							} else {
							
							}
						}
					}
				});

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						jobGrid
					]
				});



			});

		</script>
    </stripes:layout-component>
</stripes:layout-render>