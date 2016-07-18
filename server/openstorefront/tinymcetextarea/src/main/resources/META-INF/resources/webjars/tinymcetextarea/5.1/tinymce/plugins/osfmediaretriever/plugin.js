/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
/*global tinymce:true, Ext */

tinymce.PluginManager.add('osfmediaretriever', function(editor) {

	var parseEditorContents = function parseEditorContents(editor) {
		var el = document.createElement('html');
		el.innerHTML = editor.getContent();
		var images = el.getElementsByTagName('img');
		return images;
	};

	var task = new Ext.util.DelayedTask(function() {
		var images = parseEditorContents(editor);

		if (images.length) {
			for (var i=0;i<images.length;i++) {	
				var url = images[i].src.trim();
				if (url && url.indexOf('Media.action?') === -1) { 
					if (images[i].className.indexOf('storefront-media-ignore') ===  -1) {
						Ext.getStore('inlineMediaStore').add({
							url: url,
							result: 'Retrieving...',
							status: 'RETR',
							temporaryId: ''
						});
					}
				}
			}
		}
			
		Ext.getCmp('inlineMediaWindow').processMedia(editor);
	});


	editor.on('change', function(e) {
		task.delay(800);	
	});
});
