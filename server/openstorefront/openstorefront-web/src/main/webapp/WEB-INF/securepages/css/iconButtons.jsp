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

.small-button-normal {
	padding: 5px;
    border: 1px solid grey;
    border-radius: 2px 2px 2px 2px;
    color: black;
}

.small-button-normal:hover {
	background-color: lightgrey;
	cursor: pointer;
}

.small-button-normal:pressed {
	background-color: darkgrey;
	cursor: pointer;
}

.small-button-danger {
	padding: 5px;
    border: 1px solid grey;
    border-radius: 2px 2px 2px 2px;
    color: red;	
}

.small-button-danger:hover {
	background-color: lightgrey;
	cursor: pointer;
}

.small-button-danger:pressed {
	background-color: darkgrey;
	cursor: pointer;
}

.button-danger {
    border-color: #cc0303;
    background-image: none;
    background-color: #ce0000;
    background-image: -webkit-gradient(linear, top, bottom, color-stop(0%, #Ae0000), color-stop(50%, #ce0000), color-stop(51%, #ce0000), color-stop(0%, #Ae0000));
    background-image: -webkit-linear-gradient(top, #Ae0000, #ce0000 50%, #ce0000 51%, #Ae0000);
    background-image: -moz-linear-gradient(top, #Ae0000, #ce0000 50%, #ce0000 51%, #Ae0000);
    background-image: -o-linear-gradient(top, #Ae0000, #ce0000 50%, #ce0000 51%, #Ae0000);
    background-image: -ms-linear-gradient(top, #Ae0000, #ce0000 50%, #ce0000 51%, #Ae0000);
    background-image: linear-gradient(top, #Ae0000, #ce0000 50%, #ce0000 51%, #Ae0000);
}

.icon-top-padding {
	padding-top: 2px !important;
}

.icon-top-padding-5{
	padding-top: 5px !important;
}

.icon-search-adjustment {
	margin-top: -11px;
	margin-left: -5px
}

/* Icon button colors */

.icon-button-color-default {
	color: #4a4e4f;
}

.icon-button-color-white-fill {
	color: white;
}

.icon-button-color-run {
	color: #FFA500;
}

.icon-button-color-refresh {
	color: #64B647;
}

.icon-button-color-save {
	color: #167DEE;
}

.icon-button-color-edit {
	color: #2E27D9;
}

.icon-button-color-warning {
	color: #a94442; 
}

.icon-button-color-view {
	color: #0d4577;
}

.icon-button-color-key {
	color: #FFA500;
}

/* For icons that don't vertically center for cryptic reasons **/

.icon-vertical-correction {
	position: relative;
	bottom: 3px;
}

.icon-correction-load-port {
	position: relative;
	bottom: 2px;
	right: 3px;
}

.icon-vertical-correction-send {
	position: relative;
	right: 8px;
	bottom: 3px;
}

.icon-vertical-correction-save {
	position: relative;
	right: 3px;
	bottom: 3px;
}

.icon-vertical-correction-add {
	position: relative;
	bottom: 2px;
}

.icon-vertical-correction-view {
	position: relative;
	right: 5px;
	bottom: 2px;
}

.icon-vertical-correction-edit {
	position: relative;
	right: 4px;
	bottom: 1px;
}

.icon-vertical-correction-check {
	position: relative;
	bottom: 4px;
	right: 2px;
}

.icon-correction-users {
	position: relative;
	right: 8px;
	bottom: 3px;
}

.icon-correction-key {
	transform: rotate(270deg) scaleX(-1);
}

.icon-correction-gavel {
	position: relative;
	bottom: 2px;
	right: 3px;
}

.icon-small-vertical-correction-load {
	position: absolute;
	top: 3px;
}

.icon-small-vertical-correction {
	margin-top: 5px;
}

.icon-small-vertical-correction-media-table{
	position: relative;
	top: 2px;
}

.icon-small-vertical-correction-book {
	margin-top: 3px;
}

.icon-lg-vertical-correction {
	position: relative;
	top: 2px;
}

.icon-horizontal-correction {
	margin-right: 3px;
}

.shift-window-text-right {
	margin-left: 8px;
}

.icon-small-horizontal-correction-left {
	margin-right: 8px;
}

.icon-small-horizontal-correction-right {
	margin-left: 5px;
}

.icon-small-horizontal-correction-trash {
	margin-left: 12px;
}

.icon-vertical-correction-eraser {
	position: relative;
	right: 5px;
	bottom: 2px;
}

.icon-vertical-correction-search-tools {
	position: relative;
	right: 4px;
	bottom: 6px;
}
