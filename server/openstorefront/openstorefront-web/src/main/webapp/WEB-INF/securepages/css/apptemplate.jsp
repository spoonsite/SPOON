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

.home-quote-banner-section{
	background: ${actionBean.branding.quoteColor} !important;
}

.home-quote-banner-text{	
	font-size: 19px;
	font-style:  italic;
	font-family: Georgia, serif;
	color: ${actionBean.branding.primaryTextColor} !important;
}

.home-search-panel {
	background: white;
	padding: 40px 0px 40px 0px;
	border-top: 1px solid darkgray !important;
	border-bottom: 1px solid lightgray !important;
}

.home-search-field-cat {
	font-size: 1.5em;
	height: 35px !important;
	line-height: 35px;
	color: #555555;
	vertical-align: middle;
	color: #777777;
	background-color: #f1f2f0;
	background-image: none;
	border-right: 0px;		
}

.home-search-field {
	font-size: 2.6em;
	height: 48px !important;
	line-height: 1.428571429;
	color: #555555;
	vertical-align: middle;
	background-color: white;
	background-image: none;
	/* border: 1px solid #cccccc; */
	/* border-left: 0px; */
	border-right: 0px;	
	/* -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075); */
	/* box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075); */
	/* -webkit-transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s; */
	/* transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s; */
}

.home-search-field-new {
	font-size: 2.4em;
	height: 35px !important;
	line-height: 35px;
	color: #555555;
	vertical-align: middle;
	background-color: white;
	background-image: none;
	/* border: 1px solid #cccccc; */
	/* border-left: 0px; */
	border-right: 0px;	
	/* -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075); */
	/* box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075); */
	/* -webkit-transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s; */
	/* transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s; */
}

.home-search-stats {
	color: #999999;
	font-size: 21px;
	padding-bottom: 10px;
}

.home-footer {
	background-color: ${actionBean.branding.secondaryColor};	
}

.home-footer-contents {	
	color: ${actionBean.branding.primaryTextColor};
}

.home-footer-version {
	color: ${actionBean.branding.primaryTextColor};
}

.home-viewport {
	background: white !important;
}

.new-home-highlight-item {
	text-align: center;    
	color: whitesmoke;
	height: 100%;
}

.new-home-highlight-item-back {	
	/* background: rgba(0, 0, 0, 0.5); */
	/* height: 100%; */
}

.new-home-highlight-item-desc {
	
	padding: 40px 40px 40px 40px;	
	font-size: 16px;		
    color: #777;    
    text-align: justify;
    line-height: 21px;	
}

.home-highlight-header {
    text-align: center;
    //font-size: 24px;
    height: 50px;
    padding-top: 15px;
    //background: ${actionBean.branding.quoteColor};
    letter-spacing: 5px;
    text-transform: uppercase;
    font: 20px "Lato", sans-serif;
    color: #111;	
	text-overflow: ellipsis;
}

.home-highlight-header a {
	//color: white;	
	text-decoration: none;
}

.home-highlight-footer {
    position: absolute;
    bottom: 10px;
    padding-left: 40px;
    padding-right: 40px;
    width: 100%;
	color: #111;	
}

.home-readmore {
	padding: 0.5em 2em;
    border: 0.25em solid ${actionBean.branding.quoteColor};
    font-size: 1.2em;
    font-weight: 700;
    text-transform: uppercase;
    text-decoration: none;
    background-color: ${actionBean.branding.quoteColor}!important;
    color: #fff!important;
    display: inline-block;
    color: inherit;
    box-shadow: none;
}

.home-highlight-footer-indicator {
	position: absolute;
	bottom: 10px;
	width: 100%;
	text-align: center;
	color: black;
	font-size: 12px;
}

.home-highlight-footer-indicator-item:hover {
	cursor: pointer;
}

.homelink {
	color: white;	
}

.home-info-carousel {
	background-color: #f1f2f0;
}

.home-category-block {
	background-color: black;
}


.home-category-title {
	font-size: 24px;
}

.home-category-title-section {
	/* secondary light or quote */
	background-color: ${actionBean.branding.quoteColor};
}

.home-category-title-section:hover {
	cursor: pointer;
}

.home-category-section {
	float: left;
    width: 210px;
    margin: 20px;
}

.home-category-header {	
	padding: 10px; width: 100%; 
	/* accent color? */
	background-color: ${actionBean.branding.quoteColor};
	color: white;
}

.home-category-content {
	/* text color */
	background-color: white;
}

.search-tool-org {
	border-bottom: 1px solid lightgrey;
	padding: 10px;
}

.search-tool-org:nth-child(odd) {
	background: whitesmoke;
}

.search-tool-org:nth-child(even) {
	background: white;
}

.search-tool-org-logo {
	
}

.search-tool-org-logo-text {
	width: 60px;
    text-align: center;
    background-color: ${actionBean.branding.quoteColor};
    color: ${actionBean.branding.primaryTextColor};
    font-size: 24px;
    font-weight: bolder;    
    padding: 20px;
    border-radius: 50%;	
}

.search-tool-org-text-name {
	font-size: 2em;	
	text-decoration: none;
}

.search-tool-org-text-desc {
	font-size: 12px;
}

.search-tool-org:hover {
	cursor: pointer;
	background: #fff9cc !important;
}

.search-tool-relation {
	border-bottom: 1px solid lightgrey;
	padding: 10px;
}

.search-tool-relation:hover {
	cursor: pointer;
	background: #fff9cc !important;
}

.search-tool-relation:nth-child(odd) {
	background: whitesmoke;
}

.search-tool-relation:nth-child(even) {
	background: white;
}

.search-tool-relation-text {
	font-size: 2em;	
	text-decoration: none;
	margin-left: 20px;
}

.searchresults-morefilter {
	color: white;
	background-color: ${actionBean.branding.primaryColor};
}

.searchresults-item:nth-child(odd){
	background: white;
	padding: 10px;
	border: 1px solid lightgray;
	margin-top: -1px;
}

.searchresults-item:nth-child(even){
	background: whitesmoke;
	padding: 10px;
	border: 1px solid lightgray;
	margin-top: -1px;
}

.searchresults-item-update{
	font-size: 10px;
	color: darkgray;
}

.searchresults-item-org {
	font-size: 12px;
	color: grey;
}

.searchresults-item-click {
	margin: 0px;
	padding-top: 15px;
	padding-bottom: 15px;
}

.searchresults-item-click:hover{
	background: #d8e9f6;
	cursor: pointer;
}

.searchresults-tag{
	border: 1px solid lightgray;
	border-radius: 5px 5px 5px 5px;
	background: ${actionBean.branding.primaryColor};
	color: white;
	font-weight: bold;
	padding: 5px;
	line-height: 28px;
}

.details-title-name {
	font-size: 36px;
	line-height: 28px;
	color: ${actionBean.branding.primaryColor} !important;
	padding-top: 10px;
	padding-bottom: 10px;
}

.details-title-info {
	color: rgba(68,30,90,.6) !important;
}

.review-summary-rating {
	font-size: 20px;
}

.review-pro-con-header{
	font-weight: bold;
	width: 100%;	
	padding: 5px 0px 5px 5px;
	background-color: beige;
	border-bottom: 1px solid lightgrey;
}

.rating-star-color {
	color: gold;
}

.review-who-section {
	color: darkgray;
}

.review-summary-count {
	color: #999;
	font-size: 11px;
}

.question-question {
	font-size: 16px;
}

.question-question .alert-warning {
    color: #fcf8e3;
    background-color: #8a6d3b;
}

.question-response {
	font-weight: bold;
    font-size: 14px;
}

.question-response-letter {
	color: #414e68;
    font-weight: bold;
    font-size: 20px;
}

.question-response-letter-q {
    font-weight: bold;
    font-size: 20px;	
}

.question-info {
	color: #999;
}

.details-table {
	 border-collapse: collapse;
	 border: 1px solid grey;
	table-layout:fixed;
}

th.details-table {
	background-color: #BFBFBF;
	border: 1px solid grey;
	padding: 5px;
	text-align: left;	
}

a.details-table {	
    color: #555555;
	border: 0px;
	text-decoration: none;
}

a.details-table:hover  {	
    color: #2f2f2f;
	text-decoration: underline;
}

td.details-table {
	border: 1px solid grey;
	padding: 5px;
	text-align: left;
	word-wrap:break-word;
}

tr.details-table:hover {
	background-color: #f5f5f5
}

.info-table{
	 border-collapse: collapse;
	 border: 1px solid grey;	
	 width: 100%;
}

tr.info-table:nth-child(even){
	background-color: #f5f5f5;
}

td.info-table {
	border: 1px solid grey;
	padding: 5px;
	text-align: left;
}


.rolling-container {
    width: 100%;        
}

.detail-eval-item {    
	padding: 5px;
	border: 1px solid grey;
	min-width: 311px;
	max-width: 100%;
	width: 16.7%;
	border-collapse: collapse;
}

.detail-eval-item:hover  { 
	background-color: #f5f5f5;
}

.rolling-container-block {    
    float: left;		
}

.detail-eval-label{	
	min-width: 100px;
	font-weight: bold;
}

.detail-eval-score{
	float: right;
	width: 60px;
	color: #021233;
}

.detail-media-block{
	padding: 5px;
	border: 1px solid grey;
	height: 163px;
    float: left;	
	margin-right: 5px;
    margin-top: 5px;	
}

.detail-media-block-quick{
	padding: 5px;
	border: 1px solid grey;
	height: 163px;
    float: left;	
	margin-right: 5px;
    margin-top: 5px;	
}

.detail-media-block:hover{
	background-color: lightgrey;
	cursor: pointer;	
}

.detail-media-caption{
	background: black;
    color: white;
    margin-top: -35px;
    padding: 5px;
    position: relative;
    bottom: 0;
    opacity: .6;
    width: 100%;	
    text-align: center;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;	
}

.search-tool-button-outer {		
	height: 200px;
	width: 200px;
    float: left;	
	background: ${actionBean.branding.primaryColor};
}

.search-tool-button-inner {
	display: table;
	margin: 0 auto;
	/* Secondary Text Center */
	font-size: 24px;
	font-weight: bold;
	color: white;
	padding-top: 18%;
	text-align: center;
}

.search-tool-button-outer:hover {
	cursor: pointer;
	opacity: .9;
}

.action-tool-button-outer {		
	height: 200px;
	width: 200px;
    float: left;	
}

.action-tool-header {
	font-size: 32px;
    line-height: 32px;	
	color: #252525;
	text-align: center;
	background-color: rgba(255, 255, 255, .5);
}

.action-tool-button-inner {
	display: table;
	margin: 0 auto;
	/* Secondary Text Center */
	font-size: 32px;
    line-height: 32px;	
	color: #252525;
	//padding-top: 18%;
	text-align: center;
}

.action-tool-button-outer:hover {
	cursor: pointer;
	opacity: .9;
}

.mediaviewer-body{
	background-color: rgba(0, 0, 0, .6);
}

@media (min-width: 800px) {
	.footer_content {
		margin: 0px auto;
		white-space: nowrap;
		width: 800px;
		padding-left: 19%;
	}
}

@media (max-width: 800px) {
	.footer_content {
		margin: 0px auto;
		white-space: nowrap;
		width: 800px;
		padding-left: 8%;	
	}
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

.copyright a {
    color: darkgray;
	text-decoration: none;
}

.home-info-section {
	background: #efefef;
	padding: 20px;
}

.home-info-section-title {
	font-size: 36px;
	text-align: center;
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-weight: 500;
	line-height: 1.1;	
}

.home-info-section-title-rule {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid lightgray;
}

.home-highlight-item {
	border: 1px solid lightgrey;
    padding: 5px;
    background: #E4E4E4;	
}

.home-highlight-item-desc {
	padding: 5px;	
}

.home-highlight-approved {
	text-align: right;
	font-size: 10px;
}

.home-backsplash {
	background-size: 100%;
	background-position: center top;
    background-repeat: no-repeat;
	background-image: url('${actionBean.branding.homebackSplashUrl}');
}

.home-footer-item {
	color: white;
}

.home-footer-item a {
	color: whitesmoke;
	text-decoration: none;
}

.home-page-top-logo {
	z-index: 10;
	position: fixed !important;
	top: 10px !important;
	left: 30px !important;
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

/** Search tool styles **/

.list-button {
    background-color: ${actionBean.branding.primaryColor} !important;
    color: #484848;
    border-color: #747679;
    background-image: none;
    text-decoration: none!important;
}

.panel-header {
    background-color: #ababab!important;
    color: black!important;
    border-color: #787878!important;
    border-width: 1px!important;
    background-image:none;
}

.search-tools-column-orange-text {
    color: ${actionBean.branding.primaryColor} !important;
    font-size: 12px;
    font-weight: bold;
}

.search-tools-nav-body-panel-item {
    padding-top:5px;
    padding-bottom:5px;
    padding-left:2px;
    padding-right:2px;
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

.evaluation-nav-button
{
	background: steelblue;
}

.evaluation-nav-button-over
{
	background: #559edc;
}

.evaluation-nav-question-status
{
	background-color: #dadada;
    border-color: #bbbbbb;
}

.eval-form-title 
{
	background: rgba(0, 0, 0, .5) !important;
}

.eval-form-field
{
	font-size: 2em;
	border-radius: 4px 4px 4px 4px;
	color: rgba(0, 0, 0, .7);
}

.eval-form-field-label
{
	font-size: 2em;
	padding-top: 10px;
}

.checklist-question-sectionheader {
	padding: 5px;
    background-color: lightgray;
    border: 1px solid darkgrey;
}

.checklist-question{
	font-weight: normal;
    font-size: 19px;
    line-height: 1.45;   
}

.checklist-question-table{
	width: 100%;
	border: 1px solid grey;
	border-collapse: collapse;
}

.checklist-question-table-header{
    padding: 15px;
	border: 1px solid grey;
    text-align: left;	
    color: white;	
}

.checklist-question-table-header-row{
    background-color: #4CAF50;
}

.checklist-question-table-data{
    padding: 15px;
	border: 1px solid grey;
    text-align: left;
}

.checklist-question-table-datarow:nth-child(even) {
	background-color: #f2f2f2;
}

.landing-designer-tabbar {
	background: #586988;
}

.landing-designer-comp {
	background: steelblue;
	border: 1px solid #71a4c1 !important;
}

.landing-designer-layout {
	background: darkgrey;
	border: 1px solid #9b9d9e !important;
}


.review-summary
{
	width:100%;
}
.review-section
{
	width:100%;
	border-bottom-style:solid;
	border-bottom-width: thin;
	border-bottom-color:#000000;
}

.review-section .label,
.review-summary .label
{
	font-weight: bold;
}
.review-section .title, 
.review-section .rating
{
	font-weight: bold;
	font-size: 2em;
	padding: 10px 0px;
}

.review-section .comments
{
	padding-bottom: 20px;
}

.review-section .details,
.review-section .pros,
.review-section .cons,
.review-summary .details,
.review-summary .pros,
.review-summary .cons
{
	display: inline-block;
    vertical-align: top;
	padding-bottom: 20px;
}
.review-section .pros,
.review-section .cons,
.review-summary .pros,
.review-summary .cons
{
	width: 20%;
}
.review-section .details,
.review-summary .details
{
	width: 58%;
}

.review-section .alert-warning
{
	font-weight: bold;
	font-size: 1.25em;
	margin: 5px 0px;
}


@media (max-width: 520px) {
	.review-section .details,
	.review-section .pros,
	.review-section .cons,
	.review-summary .details,
	.review-summary .pros,
	.review-summary .cons
	{
		display: block;
		width: 100%;
	}
}
