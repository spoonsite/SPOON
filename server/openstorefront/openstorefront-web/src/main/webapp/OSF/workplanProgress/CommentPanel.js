/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 /* Author: cyearsley */
/* global Ext, CoreUtil, CoreService, data */

Ext.define('OSF.workplanProgress.CommentPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.osf.wp.commentpanel',

	config: {
		recordId: null,
		isPartialSubmission: null,
		editComment: {
			commentId: null,
			commentText: null
		}
	},

	title: 'Workflow Comments <i class="fa fa-question-circle"  data-qtip="Click a comment you are an author of to <b>edit/delete</b> it"></i>',
	iconCls: 'fa fa-lg fa-comment',
	layout: 'fit',
	itemId: 'workplanCommentsPanel',
	width: '100%',
	items: [
		{
			xtype: 'panel',
			itemId: 'comments',
			bodyStyle: 'padding: 10px;',
			scrollable: true,
			height: '100%',
			width: '100%',
			layout: {
				type: 'vbox',
				pack: 'center',
				align: 'middle'
			},
			items: [
				{
					xtype: 'container',
					style: 'text-align: center;',
					itemId: 'initialCommentLabel',
					hidden: false,
					width: '100%',
					html: 'There are no comments for this record<br/>Enter a comment to start the conversation'
				},
				{
					xtype: 'container',
					itemId: 'commentsContainer',
					hidden: true,
					width: '100%',
					layout: 'vbox',
					items: []
				}
			],
			dockedItems: [
				{
					xtype: 'form',
					itemId: 'form',
					dock: 'bottom',
					layout: 'anchor',
					items: [
						{
							xtype: 'htmleditor',
							name: 'comment',
							width: '100%',
							itemId: 'commentTextField',
							fieldBodyCls: 'form-comp-htmleditor-border',
							maxLength: 4000
						},
						{
							xtype: 'toolbar',
							layout: 'hbox',
							items: [
							{
								xtype: 'label',
								html: '<b>This comment will be sent to the vendor.</b>',
								itemId: 'publicCommentWarning',
								style: {
									color:'red',
									display: 'block',
									float:'left',
								}
							},
							]
						}
					],
					dockedItems: [
						{
							xtype: 'toolbar',
							dock: 'bottom',
							layout: 'hbox',
							items: [
								{
									text: 'Post Comment',									
									iconCls: 'fa fa-lg fa-comment icon-button-color-save',
									itemId: 'postCommentId',
									handler: function () {
										this.up('[itemId=comments]').up().saveUpdateComment();
									}
								},
								{
									text: 'Update Comment',
									iconCls: 'fa fa-lg fa-save icon-button-color-save',
									itemId: 'editCommentButton',
									hidden: true,
									handler: function () {
										this.up('[itemId=comments]').up().saveUpdateComment();
									}
								},
								{
									text: 'Cancel Edit',
									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
									itemId: 'cancelCommentButton',
									hidden: true,
									handler: function () {
										this.up('[itemId=comments]').up().resetFormState();
									}
								},
								{
									xtype: 'tbfill'
								},
								{
									xtype: 'fieldcontainer',
									fieldLabel: 'Private <i class="fa fa-question-circle" data-qtip="Private comments are only visible to admins"></i>',
									defaultType: 'checkboxfield',
									width: '30%',
									labelWidth: 75,
									requiredPermissions: ['WORKFLOW-ADMIN-SUBMISSION-COMMENTS'],
									style: 'float: right;',
									items: [
										{
											checked: false,
											name: 'privateCheckbox',
											itemId: 'privateCheckbox',
											listeners: {
												change: function(checkbox, newVal){
													checkbox.up('form').queryById('publicCommentWarning').setHidden(newVal);
												}
											}
										}
									]
								}
							]
						}
					]
				}
			]
		}
	],
	loadComponentComments: function (recordId, isUpdatingComment) {
	
		var commentPanel = this;
		commentPanel.setRecordId(recordId);

		commentPanel.setLoading(true);
		Ext.Ajax.request({
			url: 'api/v1/resource/' + (commentPanel.getIsPartialSubmission() ? 'usersubmissions' : 'components') + '/' + commentPanel.getRecordId() + '/comments?submissionOnly=true',
			method: 'GET',
			callback: function() {
				commentPanel.setLoading(false);
			},
			success: function (res) {
				
				res = Ext.decode(res.responseText);
				var initialLabel = commentPanel.down('[itemId=initialCommentLabel]');
				var commentsContainer = commentPanel.down('[itemId=commentsContainer]');
				commentsContainer.removeAll(true);

				if (res.length > 0) {
					CoreService.userservice.getCurrentUser().then(function (user) {
						
						var commentsToAdd = [];
						Ext.Array.forEach(res, function (commentObj, index) {
							var isOwner = user.username === commentObj.createUser;
							var commentClass = isOwner ? 'submission-comment-right' : 'submission-comment-left';
							var privateComment = commentObj.privateComment ? '<b>(Private)</b>' : '';
							var privateCommentStyle = commentObj.privateComment ? 'alert-warning' : '';
							var commentContents = [
								{
									xtype: 'container',
									width: '100%',
									cls: (isOwner ? 'owner-submission-comment ' : ' ') + (res.length === index + 1 ? 'submission-new-comment' : ''),
									html: 	'<div class="submission-comment ' + commentClass + ' ' + privateCommentStyle + '">' + 
										'<div class="author-comment">' + commentObj.createUser + ' ' + privateComment + '</div>' +
										'<div class="time-created-comment">' + (Ext.Date.format(new Date(commentObj.createDts), 'F j, Y, g:ia')) + '</div>' +
												'<div class="block-comment">' + commentObj.comment + '</div>' +
											'</div>',
									listeners: isOwner ? {
										element: 'el',
										click: function (event) {

											Ext.create('Ext.menu.Menu', {
												items: [
													{
														text: 'Comment Locked',
														iconCls: 'fa fa-2x fa-lock',
														style: 'background-color: #F2DEDE',
														hidden: commentObj.editDeleteLock ? false : true
													},
													{
														text: 'Edit Comment',
														iconCls: 'fa fa-2x fa-pencil-square-o',
														disabled: commentObj.editDeleteLock ? true : false,
														handler: function () {
															commentPanel.setEditComment({
																commentId: commentObj.commentId,
																commentText: commentObj.comment
															});

															commentPanel.down('[itemId=commentTextField]').setValue(commentObj.comment);
															commentPanel.down('[itemId=postCommentId]').hide();
															commentPanel.down('[itemId=editCommentButton]').show();
															commentPanel.down('[itemId=privateCheckbox]').setValue(commentObj.privateComment);
														}
													},
													{
														text: 'Delete Comment',
														iconCls: 'fa fa-2x fa-trash',
														disabled: commentObj.editDeleteLock ? true : false,
														handler: function () {
															Ext.Msg.show({
																title: 'Delete Comment?',
																message: 'You are about to delete the comment <b><i>"' + Ext.String.ellipsis(commentObj.comment, 100, true) + '"</i></b>.<br><br> Are you sure you want to continue?',
																buttons: Ext.Msg.YESNO,
																icon: Ext.Msg.QUESTION,
																fn: function (btn) {
																	if (btn === 'yes') {
																		commentPanel.setLoading('Deleting...');
																		Ext.Ajax.request({
																			url: 'api/v1/resource/' + (commentPanel.getIsPartialSubmission() ? 'usersubmissions' : 'components') + '/' + commentPanel.getRecordId() + '/comments/' + commentObj.commentId,
																			method: 'DELETE',
																			callback: function() {
																				commentPanel.setLoading(true);
																			},
																			success: function () {
																				commentPanel.loadComponentComments(commentPanel.getRecordId());
																				commentPanel.resetFormState();
																			}
																		});
																	}
																}
															});
														}
													}
												]
											}).showAt(event.clientX, event.clientY);
										}
									} : {}
								}
							];

							commentsToAdd.push({
								xtype: 'container',
								width: '100%',
								items: commentContents
							});
						});
						commentsContainer.add(commentsToAdd);

						initialLabel.hide();
						commentsContainer.show();
					})
				}
				else {
					initialLabel.show();
					commentsContainer.hide();
				}

				var commentsContainerParent = commentPanel.down('[itemId=comments]');

				// defer because Ext is weird with scrolling...
				Ext.defer(function () {
					if (!isUpdatingComment) {
						commentsContainerParent.setScrollY(commentsContainerParent.getHeight()*100);
					}
				}, 100);
			}
		});
	},
	saveUpdateComment: function () {

		var commentsPanel = this;
		var commentId = commentsPanel.getEditComment().commentId;

		CoreService.userservice.getCurrentUser().then(function (user) {
			var hasPermission = CoreService.userservice.userHasPermission(user, 'WORKFLOW-ADMIN-SUBMISSION-COMMENTS');

			var form = commentsPanel.down('[itemId=form]').getValues();

			var privateComment = false;
			if (form.privateCheckbox == "true"){
				privateComment = true;
			}
			var formValues = {
				comment: form.comment,
				commentType: 'SUBMISSION',
				privateComment: hasPermission ? privateComment : false,
				willSendEmail: false,
			};

			commentsPanel.setLoading('Saving...');
			Ext.Ajax.request({
				url: 'api/v1/resource/' + (commentsPanel.getIsPartialSubmission() ? 'usersubmissions' : 'components') + '/' + commentsPanel.getRecordId() + '/comments' + (commentId ? '/' + commentId : ''),
				method: commentId ? 'PUT' : 'POST',
				jsonData: formValues,
				callback: function() {
					commentsPanel.setLoading(false);
				},
				success: function (res) {

					var componentId = commentsPanel.getRecordId();
					commentsPanel.resetFormState();
					commentsPanel.loadComponentComments(componentId, commentId ? true : false);
				}
			});
		});
	},
	resetFormState: function () {
		this.setEditComment({
			commentId: null,
			commentText: null
		});

		this.down('[itemId=commentTextField]').setValue('');
		this.down('[itemId=editCommentButton]').hide();
		this.down('[itemId=cancelCommentButton]').hide();
		this.down('[itemId=postCommentId]').show();
		this.down('[itemId=privateCheckbox]').setValue(true);
	}
});
