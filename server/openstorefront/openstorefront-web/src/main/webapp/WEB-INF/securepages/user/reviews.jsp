<%-- 
Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/usertoolslayout.jsp">
    <stripes:layout-component name="contents">
		
	   <script src="scripts/component/reviewWindow.js?v=${appVersion}" type="text/javascript"></script>	
		
	   <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				
				var reviewGrid = Ext.create('Ext.grid.Panel', {	
					title: 'Reviews <i class="fa fa-question-circle"  data-qtip="Inspect and manage the reviews you\'ve given here"></i>',
					id: 'reviewGrid',
					columnLines: true,
					store: {
						sorters: [{
							property: 'name',
							direction: 'ASC'
						}],	
						autoLoad: true,
						fields: [
							{
								name: 'updateDate',
								type:	'date',
								dateFormat: 'c'
							},
							{
								name: 'ratingStars', mapping: function(data) {
									var ratingStars = [];
									for (var i=0; i<5; i++){					
										ratingStars.push({						
											star: i < data.rating ? (data.rating - i) > 0 && (data.rating - i) < 1 ? 'star-half-o' : 'star' : 'star-o'
										});
									}				
									return ratingStars;
								}
							}
						],
						proxy: {
							type: 'ajax',
							url: 'api/v1/resource/components/reviews/' + '${user}'
						}		
					},
					columns: [
						{ text: 'Entry', dataIndex: 'name', width: 200 },
						{ text: 'Title', dataIndex: 'title', width: 200 },
						{ text: 'Rating', dataIndex: 'rating', width: 125,
							renderer: function(values, meta, record) {
								var display = '';
								Ext.Array.each(record.get('ratingStars'), function(star){
									display += '<i class="fa fa-lg fa-' + star.star + ' rating-star-color"></i>';
								});								
								return display;
							}
						},						
						{ text: 'Comment', dataIndex: 'comment', flex: 1, minWidth: 200 },
						{ text: 'Update Date', dataIndex: 'updateDate', width: 200, xtype: 'datecolumn', format:'m/d/y H:i:s' }
					],
					dockedItems: [
						{
							dock: 'top',
							xtype: 'toolbar',
							itemId: 'tools',
							items: [
								{
									text: 'Refresh',
									scale: 'medium',								
									iconCls: 'fa fa-2x fa-refresh',
									handler: function () {
										actionRefresh();
									}
								},
								{
									xtype: 'tbseparator'
								},
								{
									text: 'Edit',
									itemId: 'edit',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-edit',
									handler: function () {
										actionEdit(Ext.getCmp('reviewGrid').getSelectionModel().getSelection()[0]);										
									}									
								},
								{
									xtype: 'tbfill'
								},
								{
									text: 'Delete',
									itemId: 'delete',
									scale: 'medium',
									disabled: true,
									iconCls: 'fa fa-2x fa-trash-o',
									handler: function () {
										actionDelete(Ext.getCmp('reviewGrid').getSelectionModel().getSelection()[0]);
									}									
								}								
							]
						}
					],
					listeners: {
						itemdblclick: function(grid, record, item, index, e, opts){
							actionEdit(record);
						},
						selectionchange: function(selectionModel, selected, opts){
							var tools = Ext.getCmp('reviewGrid').getComponent('tools');
							
							if (selected.length > 0) {	
								tools.getComponent('edit').setDisabled(false);
								tools.getComponent('delete').setDisabled(false);
							} else {
								tools.getComponent('edit').setDisabled(true);
								tools.getComponent('delete').setDisabled(true);
							}
						}
					}
				});
						
				var actionRefresh = function() {
					Ext.getCmp('reviewGrid').getStore().load();
				};
						
				var reviewWindow = Ext.create('OSF.component.ReviewWindow', {
					title: 'Edit Review',						
					postHandler: function(reviewWin, response) {
						actionRefresh();
					}
				});						
				var actionEdit = function(record) {
					reviewWindow.show();
					reviewWindow.editReview(record);
				};
				
				var actionDelete = function(record) {
					Ext.Msg.show({
						title:'Remove Review?',
						message: 'Are you sure you want to remove this review?',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						fn: function(btn) {
							if (btn === 'yes') {
								Ext.getCmp('reviewGrid').setLoading("Removing...");
								Ext.Ajax.request({
									url: 'api/v1/resource/components/'+record.get('componentId')+'/reviews/'+record.get('reviewId'),
									method: 'DELETE',
									callback: function(){
										Ext.getCmp('reviewGrid').setLoading(false);
									},
									success: function(){
										actionRefresh();
									}
								});
							} 
						}
					});					
				};				

				Ext.create('Ext.container.Viewport', {
					layout: 'fit',
					items: [
						reviewGrid
					]
				});
				
			});
			
		</script>
		
	</stripes:layout-component>
</stripes:layout-render>