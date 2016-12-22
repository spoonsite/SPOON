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
/*global tinymce, Ext:true */

tinymce.PluginManager.add('osffullscreen', function(editor) {
	editor.addCommand('fullScreenMode', function() {
		var editorWin = Ext.create('OSF.component.FullScreenEditor', {				
                    editor: editor
		});		
		editorWin.show();
		editorWin.toFront();
	});

	editor.addButton('osffullscreen', {
		icon: 'fullscreen',
		tooltip: 'Full Screen',
                shortcut: 'Ctrl+Alt+F',
		cmd: 'fullScreenMode'
	});

	editor.addMenuItem('osffullscreen', {
		icon: 'fullscreen',
		text: 'Full Screen',
                shortcut: 'Ctrl+Alt+F',
		cmd: 'fullScreenMode',
		context: 'view'
	});
});
