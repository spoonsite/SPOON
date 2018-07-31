





// Ext.define('OSF.common.ValidHtmlEditor', {
//     extend: 'Ext.form.field.HtmlEditor',
//     xtype: 'osf-common-validhtmleditor',
//     maxLen: 4096,
//     exceededLimit: false,
//     initComponent: function(){
//         this.callParent();
//         var validhtmleditor = this;
//         validhtmleditor.on('change', function(field, newValue, oldValue, eOpts){
//             if(newValue.length > validhtmleditor.maxLen){
//                 field.setFieldLabel('<span style = "color: red"> ERROR!  <i class="fa fa-exclamation-triangle"></i> You have exceeded the maximum length for a comment. Please shorten your comment. <i class="fa fa-exclamation-triangle"></i></span>');
//                 field.exceededLimit = true;
//             }
//             if( this.exceededLimit && (newValue.length <= validhtmleditor.maxLen)){
//                 field.setFieldLabel('Component Comments');
//                 field.exceededLimit = false;
//             }
//         });
//     }
// });

Ext.define('OSF.common.workPlanProgressComment', {
    // extend: 'Ext.panel.panel',
    xtype: 'osf-common-workplanprogresscomment',
    title: 'Comments',
    iconCls: 'fa fa-lg fa-comment',
    region: 'east',			
    collapsed: true,
    collapsible: true,
    animCollapse: false,
    floatable: false,
    titleCollapse: true,
    width: 375,
    minWidth: 250,
    split: true,			
    bodyStyle: 'background: white;',
    layout: 'fit',
    items: [
        {
            xtype: 'panel',
            itemId: 'comments',
            bodyStyle: 'padding: 10px;',
            scrollable: true,
            items: [						
            ],
            dockedItems: [
                {
                    xtype: 'form',
                    itemId: 'form',
                    dock: 'bottom',
                    layout: 'anchor',
                    items: [
                        {
                            xtype: 'hidden',
                            name: 'commentId'
                        },
                        {
                            xtype: 'hidden',
                            name: 'replyCommentId'
                        },
                        {
                            xtype: 'htmleditor',
                            name: 'comment',									
                            width: '100%',
                            fieldBodyCls: 'form-comp-htmleditor-border',
                            maxLength: 4000
                        }
                    ],
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'bottom',
                            items: [
                                {
                                    text: 'Comment',
                                    iconCls: 'fa fa-lg fa-comment icon-button-color-save',
                                    handler: function(){													
                                    }
                                },
                                {
                                    xtype: 'tbfill'
                                },
                                {
                                    text: 'Cancel',
                                    itemId: 'cancel',											
                                    iconCls: 'fa fa-lg fa-close icon-button-color-warning',
                                    handler: function(){																						
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        }				
    ],
    listeners: {
        afterrender: function () {
        }
    },


    initComponent: function(){
        this.callParent();
        var brighamComment = this;
    }
});




				// var commentPanel = Ext.create('Ext.panel.Panel', {
				// 	title: 'Comments',
				// 	iconCls: 'fa fa-lg fa-comment',
				// 	region: 'east',			
				// 	collapsed: true,
				// 	collapsible: true,
				// 	animCollapse: false,
				// 	floatable: false,
				// 	titleCollapse: true,
				// 	width: 375,
				// 	minWidth: 250,
				// 	split: true,			
				// 	bodyStyle: 'background: white;',
				// 	layout: 'fit',
				// 	items: [
				// 		{
				// 			xtype: 'panel',
				// 			itemId: 'comments',
				// 			bodyStyle: 'padding: 10px;',
				// 			scrollable: true,
				// 			items: [						
				// 			],
				// 			dockedItems: [
				// 				{
				// 					xtype: 'form',
				// 					itemId: 'form',
				// 					dock: 'bottom',
				// 					layout: 'anchor',
				// 					items: [
				// 						{
				// 							xtype: 'hidden',
				// 							name: 'commentId'
				// 						},
				// 						{
				// 							xtype: 'hidden',
				// 							name: 'replyCommentId'
				// 						},
				// 						{
				// 							xtype: 'htmleditor',
				// 							name: 'comment',									
				// 							width: '100%',
				// 							fieldBodyCls: 'form-comp-htmleditor-border',
				// 							maxLength: 4000
				// 						}
				// 					],
				// 					dockedItems: [
				// 						{
				// 							xtype: 'toolbar',
				// 							dock: 'bottom',
				// 							items: [
				// 								{
				// 									text: 'Comment',
				// 									iconCls: 'fa fa-lg fa-comment icon-button-color-save',
				// 									handler: function(){													
				// 									}
				// 								},
				// 								{
				// 									xtype: 'tbfill'
				// 								},
				// 								{
				// 									text: 'Cancel',
				// 									itemId: 'cancel',											
				// 									iconCls: 'fa fa-lg fa-close icon-button-color-warning',
				// 									handler: function(){																						
				// 									}
				// 								}
				// 							]
				// 						}
				// 					]
				// 				}
				// 			]
				// 		}				
				// 	],
				// 	listeners: {
				// 		afterrender: function () {
				// 		}
				// 	}
				// });










