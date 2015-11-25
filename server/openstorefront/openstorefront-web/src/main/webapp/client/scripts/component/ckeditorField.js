/* 
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/**
 *  Wrap Ckeditor into an Extjs component
 */

/* global Ext, CKEDITOR */
Ext.define('OSF.component.CKEditorField', {
    extend: 'Ext.form.field.TextArea',
    alias: 'OSF.widget.CKEditorField',

    margin: '0 0 40 0',
    constructor : function() {
    	this.callParent(arguments);//Must first construct the superclass object
    	//this.addEvents("instanceReady");//Register a instanceReady event
    },
    
    initComponent: function () {
        this.callParent(arguments);
        this.on("afterrender", function(){
            
//            Ext.apply(this.editorConfig, {
//               height : this.getHeight(),
//               width : this.getWidth()
//            });            
            this.editorConfigFull = Ext.apply(this.editorConfig, this.baseEditorConfig);
            this.editor = CKEDITOR.replace(this.inputEl.id, this.editorConfigFull);
            
            
            this.editor.name = this.name;//The name property assignment allocation in the name property for a CKEditor            
            this.editor.on("instanceReady", function(){
                this.fireEvent("instanceReady", this, this.editor);//Trigger the instanceReady event
            }, this);
            this.editor.on("resize", function(evt){                
                this.setHeight(evt.data.contentsHeight + 100);
            }, this);
        }, this);
    },
    onRender: function (ct, position) {
        if (!this.el) {
            this.defaultAutoCreate = {
                tag: 'textarea',
                autocomplete: 'off'
            };
        }
        this.callParent(arguments);
    },
//    setRawValue: function(value) {
//      this.callParent(arguments);
//      if (this.editor) {
//           if (value !== "") {
//              this.editor.setData(value);
//           }
//      }      
//    },
    reset: function(){
         //this.callParent(arguments);
         if (this.editor) {           
             this.editor.setData("<p></p>");             
         }
    },
    
    setValue: function (value) {
        this.callParent(arguments);
        if (this.editor) {  
            if (value !== "") {
              Ext.defer(function(){
                this.editor.setData(value);                                 
              }, 100, this);              
            }
        }
    },
    getRawValue: function () {//To override the getRawValue method, otherwise it will not get to the value
        if (this.editor) {
            return this.editor.getData();
        } else {
            return '';
        }
    },
    getValue: function () {
        return this.getRawValue();
    },        
  
    baseEditorConfig: {
      baseFloatZIndex: 99999999,      
      entities_latin: false,
      entities_greek: false,       
      fillEmptyBlocks: function (element) {
        return true; // DON'T DO ANYTHING!!!!!
      },      
      toolbar: 'Full',
      toolbar_Full: [
        //'Source','-','Save',
        { name: 'document', items : [ 'DocProps','Preview','Print','-','Templates' ] },
        { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
        { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
        { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
        '/',
        { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv', '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
        { name: 'links', items : [ 'Link','Unlink','Anchor' ] },
        { name: 'insert', items : [ 'Placeholder','Image','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe' ] },
        '/',
        { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
        { name: 'colors', items : [ 'TextColor','BGColor' ] },       
        { name: 'tools', items : [ 'Maximize', 'ShowBlocks',  ] } 
      ]
    },
    //'About',

    editorConfig: {
    }

//    afterRender: function () {
//      this.callParent(arguments);
//      
//      var ckeditorField = this;      
//      //CKEDITOR.replace(ckeditorField.getId(), Ext.apply(ckeditorField.editorConfig, ckeditorField.baseEditorConfig));
//      CKEDITOR.inline(ckeditorField.getId(), ckeditorField.baseEditorConfig);
//    }

});
