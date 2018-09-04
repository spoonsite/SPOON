<%--
Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>

<%@page  contentType="text/css" %>

.entry-type-search-button span::after {
	display: none;
}
.entry-type-tree-panel-menu .x-tree-elbow-img.x-tree-elbow-end-plus.x-tree-expander {
	display: none;
}
.entry-type-tree-panel-menu .x-tree-elbow-img.x-tree-elbow {
	display: none;
}
.entry-type-tree-panel-menu .x-tree-icon.x-tree-icon-leaf  {
	display: none;
}
.entry-type-tree-panel-menu .x-tree-elbow-img.x-tree-elbow-end  {
	display: none;
}

.entry-topics-button {
	background: rgb(112, 125, 150);
    display: inline-block;
    width: 17%;
    min-width: 100px;
    height: 100px;
    margin: 15px;
    vertical-align: top;
}

.entry-topics-button:hover {
	background: rgba(162, 181, 216, 0.8);
	cursor: pointer;
}

.entry-topics-button:active {
	background: rgba(112, 125, 150, 0.8);
}

.entrytype-attribute-assignment-header {
	background: steelblue;
}
