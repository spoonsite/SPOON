/* 
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/**
 * This is use to display a html editor for comments
 * It supports all the features. 
 * 
 */
/* global Ext */
Ext.define('OSF.common.ValidHtmlEditor', {
    extend: 'Ext.form.field.HtmlEditor',
    xtype: 'osf-common-validhtmleditor',
    maxLen: 4096,
    exceededLimit: false,
    initComponent: function(){
        this.callParent();
        var validhtmleditor = this;
        validhtmleditor.on('change', function(field, newValue, oldValue, eOpts){
            if(newValue.length > validhtmleditor.maxLen){
                field.setFieldLabel('<span style = "color: red"> ERROR!  <i class="fa fa-exclamation-triangle"></i> You have exceeded the maximum length for a comment. Please shorten your comment. <i class="fa fa-exclamation-triangle"></i></span>');
                field.exceededLimit = true;
            }
            if( this.exceededLimit && (newValue.length <= validhtmleditor.maxLen)){
                field.setFieldLabel('Component Comments');
                field.exceededLimit = false;
            }
        });
    }
});
	