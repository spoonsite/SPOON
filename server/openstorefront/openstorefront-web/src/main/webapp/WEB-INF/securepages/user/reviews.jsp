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

    Author     : dshurtleff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="../../../layout/toplevelLayout.jsp">
    <stripes:layout-component name="contents">
		
		<stripes:layout-render name="../../../layout/userheader.jsp">		
		</stripes:layout-render>			
		
	   <script src="scripts/component/reviewWindow.js?v=${appVersion}" type="text/javascript"></script>	
		
	   <script type="text/javascript">
			/* global Ext, CoreUtil */

			Ext.onReady(function () {
				
				var reviewGrid = Ext.create('Ext.grid.Panel', {	
					title: 'Reviews <i class="fa fa-lg fa-question-circle"  data-qtip="Inspect and manage the reviews you\'ve given here"></i>',
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
						{ text: 'Status', dataIndex: 'activeStatus', width: 200,
							renderer: function(values, meta, record) {
								return (record.get('activeStatus') === 'A') ? 'Active' : 'Pending';
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
									width: '110px',
									iconCls: 'fa fa-2x fa-refresh icon-button-color-refresh icon-vertical-correction',
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
									width: '100px',
									iconCls: 'fa fa-2x fa-edit icon-button-color-edit icon-vertical-correction-edit',
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
									iconCls: 'fa fa-2x fa-trash icon-button-color-warning icon-vertical-correction',
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
					iconCls: 'fa fa-lg fa-edit icon-small-vertical-correction',
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
						title:'Delete Review?',
						message: 'Are you sure you want to delete this review?',
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

				addComponentToMainViewPort(reviewGrid);
				
			});
			
		</script>
		
	</stripes:layout-component>
</stripes:layout-render>