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
--%>

<%@page  contentType="text/css" %>

<%@ include file="homePage.jsp" %>
<%@ include file="searchResultDetail.jsp" %>
<%@ include file="iconButtons.jsp" %>
<%@ include file="submissionForm.jsp" %>
<%@ include file="entryType.jsp" %>
<%@ include file="evaluation.jsp" %>

@media print {
    .pageBreak {		
		page-break-after: always;
	}
    .hidePrint{
		display: none;
	}
	.pushToTop{
		margin-top: -50px;
	}	
}

.printBody{
	background: white !important;
	overflow: visible;
}

.browser-warning{
	display: none;
    opacity: 0.8;
    z-index: 10000;
    position: absolute;
    bottom: 0px;
    left: 0px;
    padding-left: 20px;
    padding-right: 20px;
    width: 100%;
    height: 50px;
    background-color: #cc0000;
    color: white;
    text-shadow: 1px 1px 2px black;
}

.browser-warning-link {
	color: lime;
}

.browser-warning-link:visited {
	color: lime;
}

.browser-warning-link:hover, .browser-warning-link:focus {	
	text-decoration: underline;
}

.browser-warning .icon {
	float: right;
	cursor: pointer;
}

.action-icon {
	font-size: 16px;
}

.text-readable {
	font-weight: normal;
    font-size: 19px;
    line-height: 1.45;
    /* font-family: "Georgia", Serif; */
}

.link {
    color: ${actionBean.branding.linkColor};
    text-decoration: none;	
}

.link:visited {
    color: ${actionBean.branding.linkVisitedColor};
    text-decoration: none;	
}

.link:hover, .link:focus {
  color: ${actionBean.branding.linkhoverColor};
  text-decoration: underline;
}

.link:focus {
  outline: thin dotted #333;
  outline: 5px auto -webkit-focus-ring-color;
  outline-offset: -2px;
}

.linkItem:hover {
	cursor: pointer;
}

.hidden {
	display: none;
	visibility: hidden;
}

.private-badge {
    border-radius: 15%;
	padding-left: 3px;
    padding-right: 3px;
    text-align: center;
    background-color: red;
    border: 1px darkred solid;
    color: white;
}

.page-title{
	text-align: center; 
	font-size: 35px; 
	font-family: Calibri; 
	color: ${actionBean.branding.primaryTextColor};
}

.security-banner {
	text-align: center;
	font-size: 14px; 
	font-weight: bold;
	background-color: ${actionBean.branding.securityBannerBackgroundColor};
	color: ${actionBean.branding.securityBannerTextColor};
}

.nav-back-color {
    min-height: 52px;
 /*  background-color: #414e68; */
    border-color: #343f54;
    background: ${actionBean.branding.primaryColor};
}
.nav-back-color-only {
	background-color: ${actionBean.branding.primaryColor};
}

.accent-background {
	background-color: ${actionBean.branding.accentColor};
}

.border_accent {
	border-bottom: 3px solid ${actionBean.branding.accentColor};
}

.column {
    float: left;
    font-size: 11px;
    color: gray;
    /* margin: 0px 40px 0px; */
	padding-right: 40px;
    text-align: left;
    white-space: nowrap;
}

.column ul {
	list-style: none;
	padding-left: 0px;
}

.column .title {
    font-family: 'Archivo Narrow', sans-serif;
    font-size: 18px;
    font-weight: bold;
    color: #DDDDDD;
}

.copyright {
    margin-top: 10px;
    margin-left: auto;
    margin-right: auto;
    text-align: center;
    font-size: 10px;
    color: gray;
}

.footer_content a {
    color: darkgray;
	text-decoration: none;
}

.logo-small {
    width: 250px;
    padding: 2px;
    text-align: left;
    border-radius: 0px 8px 8px 0px;
    background: -moz-linear-gradient(left, rgba(255,255,255,1) 0%, rgba(255,255,255,1) 81%, rgba(255,255,255,0) 100%);
    background: -webkit-gradient(linear, left top, right top, color-stop(0%,rgba(255,255,255,1)), color-stop(81%,rgba(255,255,255,1)), color-stop(100%,rgba(255,255,255,0)));
    background: -webkit-linear-gradient(left, rgba(255,255,255,1) 0%,rgba(255,255,255,1) 81%,rgba(255,255,255,0) 100%);
    background: -o-linear-gradient(left, rgba(255,255,255,1) 0%,rgba(255,255,255,1) 81%,rgba(255,255,255,0) 100%);
    background: -ms-linear-gradient(left, rgba(255,255,255,1) 0%,rgba(255,255,255,1) 81%,rgba(255,255,255,0) 100%);
    background: linear-gradient(to right, rgba(255,255,255,1) 0%,rgba(255,255,255,1) 81%,rgba(255,255,255,0) 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffffff', endColorstr='#00ffffff',GradientType=1 );
}

.entry-template_block {
	background-color: #45587B;
}

.entry-template_block-layout {
	background-color: #c79300;;
}

.entry-template-drag-proxy {
    color: #000;
    padding: 5px;
    width: 150px;
    height: 50px;
    background-color: #ffeb3b;
    border: 5px dotted #ffeb3b;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    -ms-border-radius: 5px;
    -o-border-radius: 5px;
    border-radius: 5px;
}

.entry-template-drag-proxy-invalid {
	background-color: #bc0000;
	color: white;
}

.entry-template-drag-proxy-valid {
	background-color: #00bc19;
	color: white;
}

.generated-code {
	background-color: darkslategrey;
	color: white;
	font-weight: bold; 
}

.widget-picklist-item {
	border-bottom: 1px solid lightgrey;
	padding-bottom: 10px;
}

.widget-picklist-item-title {
	font-size: 24px;
	padding: 10px 10px 10px 10px;	
	background-color: #E0E7EF;
}

.widget-picklist-item-title:hover {
	text-decoration: underline;
	cursor: pointer;
}

.widget-picklist-item:nth-child(odd){
	background-color: white;
}

.widget-picklist-item:nth-child(even){
	background-color: whitesmoke;
}

.stat-list-group-item:first-child {
	border-top-right-radius: 4px;
	border-top-left-radius: 4px;
}
.stat-list-group-item {
	position: relative;
	display: block;
	padding: 10px 15px;
	margin-bottom: -1px;
	background-color: #8A8A8A;
	border: 1px solid #464545;
	font-size: 14px;
	color: white;
}
.stat-list-group {
	/* margin-bottom: 20px; */
	padding-left: 0;
	color: white;
}
ul.stat-list-group-item, ol.stat-list-group-item {
	margin-top: 0;
	margin-bottom: 10.5px;
}
.stat-badge {
	display: inline-block;
	min-width: 10px;
	padding: 3px 7px;
	font-size: 13px;
	font-weight: bold;
	color: ${actionBean.branding.primaryTextColor};
	line-height: 1;
	vertical-align: middle;
	white-space: nowrap;
	text-align: center;
	float: right;
	background-color: ${actionBean.branding.primaryColor};
	border-radius: 10px;
}

.default_body {
	background-color: #f5f5f5;
}

.form-comp-htmleditor-border {
	border:  1px solid lightgrey;
	padding-right: 2px; 
}

.field-required:after {
	color: red;
	font-weight: bold;
	content: "*"
}

.field-label-basic {
	font-weight: bold;
}

.popup-message {
	color:  white;
	opacity: .7;
	background-color: ${actionBean.branding.primaryColor};
}

.approval-message {
	font-size: 1.5em;
	font-weight: bold;
	margin-left: 7px;
}

.alerts-option-items {

	border: 1px solid #A5A7A7;
	border-radius: 5px;
	padding: 3px;
	margin-left: 6px;
	line-height: 1.9em;
	background-color: #949494;
	color: white;
}

.menu-items-css {

	padding-right: 1.8em;
}

.label-text-bold {

	font-weight: bold;
}

.updated-watch {
	
	font-weight: bold;
	float: right;
}

/* For displaying tables pasted in from Confluence */
.confluenceTable {
	border-collapse:collapse;
}

.confluenceTh,.confluenceTd {
	border:1px solid #ddd;
	vertical-align:top;
	text-align:left;
	min-width:8px;
	padding:7px 10px;
}

.confluenceTable ol,.confluenceTable ul {
	margin-left:0;
	padding-left:22px;
}

.confluenceTable,.table-wrap {
	overflow-x:auto;
	margin:10px 0 0;
}

table.confluenceTable th.confluenceTh,table.confluenceTable th.confluenceTh>p,table.confluenceTable td.confluenceTd.highlight,table.confluenceTable td.confluenceTd.highlight>p,table.confluenceTable th.confluenceTh.highlight-grey,table.confluenceTable th.confluenceTh.highlight-grey>p,table.confluenceTable td.confluenceTd.highlight-grey,table.confluenceTable td.confluenceTd.highlight-grey>p {
	background-color:#f0f0f0;
}

table.confluenceTable th.confluenceTh.info,table.confluenceTable th.confluenceTh.info>p,table.confluenceTable td.confluenceTd.highlight.info,table.confluenceTable td.confluenceTd.highlight.info>p,table.confluenceTable th.confluenceTh.highlight-blue,table.confluenceTable th.confluenceTh.highlight-blue>p,table.confluenceTable td.confluenceTd.highlight-blue,table.confluenceTable td.confluenceTd.highlight-blue>p {
	background-color:#e0f0ff;
}

table.confluenceTable th.confluenceTh.note,table.confluenceTable th.confluenceTh.note>p,table.confluenceTable td.confluenceTd.highlight.note,table.confluenceTable td.confluenceTd.highlight.note>p,table.confluenceTable th.confluenceTh.highlight-yellow,table.confluenceTable th.confluenceTh.highlight-yellow>p,table.confluenceTable td.confluenceTd.highlight-yellow,table.confluenceTable td.confluenceTd.highlight-yellow>p {
	background-color:#ffd;
}

table.confluenceTable th.confluenceTh.nohighlight,table.confluenceTable th.confluenceTh.nohighlight>p {
	font-weight:400;
	background-color:transparent;
}

table.confluenceTable td.numberingColumn {
	-webkit-touch-callout:none;
	-webkit-user-select:none;
	-khtml-user-select:none;
	-moz-user-select:none;
	-ms-user-select:none;
	user-select:none;
	cursor:default;
}

.app-info-box{
	background-color: ${actionBean.branding.primaryColor};
	padding: 10px;
	height: 100%;
	color: ${actionBean.branding.primaryTextColor};
	margin: 0px 10px 0px 0px;
	float: left;
}

.watch-detail-update {
	border-left: 5px solid green !important;
}

.text-danger {
	color: #a94442;
}

.alert-danger {
    color: #a94442;
    background-color: #f2dede;
    border-color: #ebccd1;
}

.text-warning {
	color: #8a6d3b;
}

.alert-warning {
    color: #8a6d3b;
    background-color: #fcf8e3;
    border-color: #faebcc;
}

.text-info {
	color: #31708f;
}

.alert-info {
    color: #31708f;
    background-color: #d9edf7;
    border-color: #bce8f1;
}

.text-success {
	color: #64B647;
}

.alert-success {
    color: #64B647;
    background-color: #dff0d8;
    border-color: #d6e9c6;
}

.highlight-danger {
    color: #a94442;
    background-color: #f2dede;
    border-color: #ebccd1;
}

.highlight-warning {
    color: #8a6d3b;
    background-color: #fcf8e3;
    border-color: #faebcc;
}

.highlight-info {
    color: #31708f;
    background-color: #d9edf7;
    border-color: #bce8f1;
}

.highlight-success {
    color: #64B647;
    background-color: #dff0d8;
    border-color: #d6e9c6;
}

.tinymce-error-field
{
  border: 1px solid red !important;
}

.tinymce-hide-border
{
  border-width: 0px;
}

.expandable-grid-cell {
	height: 100%;
    -moz-transition: height 0.25s;
    -ms-transition: height 0.25s;
    -o-transition: height 0.25s;
    -webkit-transition: height 0.25s;
    transition: height 0.25s;
}

.expandable-grid-cell-collapsed:before {
    content: "\f065";
    font-family: FontAwesome;
    font-style: normal;
    font-weight: normal;
    text-decoration: inherit;
    color: #606060;
    font-size: 14px;
    position: absolute;
    top: 1em;
    right: 1em;
}

.expandable-grid-cell-expanded:before {
    content: "\f066";
    font-family: FontAwesome;
    font-style: normal;
    font-weight: normal;
    text-decoration: inherit;
    color: #606060;
    font-size: 14px;
    position: absolute;
    top: 1em;
    right: 1em;
}

.expandable-grid-cell-collapsed {
	height: 2.8em;
}
.faq-question .x-panel-header {
	background-color:#FFFFFF;
}
.faq-question .x-panel-header-title {
	color:#000000;
	font-size:1.53m;
	font-weight:bold;
	cursor:pointer;
	text-decoration:underline;
}
.support-media-description {
	font-size: 14px;
}

.entrytype-attribute-assignment-header {
	background: steelblue;
}

.permission-row-disabled {
	pointer-events: none;
}

.permission-row-disabled .x-grid-checkcolumn {
	opacity: 0.5;
}

.permission-row-disabled .x-grid-cell {
	color: #ccc;
}

.step-view-container {
	width: 175px;
	height: 85px;
	/* border: 1px solid black; */
	overflow: visible;
	white-space: nowrap;
}

.step-view-container span {
	position: relative;
	left: -60px;
}
.step-view-container.last-step {
	display: inline-block;
}

.step-view {
	overflow: visible;
	width: 60px;
	height: 60px;
	line-height: 44px;
	display: inline-block;
	border: 2px solid #333;
	z-index: 11;
	position: absolute;
	left: 0;
	text-align: center;
	font-size: 14px;
	margin-top: 1.5em;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.4), 0 6px 20px 0 rgba(0, 0, 0, 0.39);
	outline: none;
}

.step-view:hover {
	opacity: 0.9;
	cursor: pointer;
}

.step-view::after {
	width: 115px;
	height: 3px;
	background: #333;
	content: '';
	position: absolute;
	display: block;
	top: 28px;
	left: 58px;
	pointer-events: none;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.4), 0 6px 20px 0 rgba(0, 0, 0, 0.39);
}

.step-view.last-step::after {
	visibility: hidden;
	width: 0;
	top: 0;
	left: 0;
}

.step-container > div {
	overflow: visible;
}

@keyframes stepactivate {
	from {
		border-radius: 100%
	}

	to {
		border-radius: 10px;
	}
}

.wp-step-active {
	background: #42f4e8;
	animation-duration: 0.5s;
	animation-name: stepactivate;
	border-radius: 10px;
}

/* .step-view.wp-step-active:hover {
	background: #3adacf;
} */

.wp-step-new {
	background: #86b5ff;;
	border-radius: 100%;
}

.wp-step-existing {
	background: blueviolet;
	border-radius: 100%;
}

.wp-step-migrated {
	background: goldenrod;
	border-radius: 100%;
}

.wp-step-lengend {
	float: left;
	margin-top: 3px;
	width: 1em;
	height: 1em;
}
