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

.dark-purple {
	color: #441e5a;
}

.davy-grey {
	color:#555
}

.fs-12 {
	font-size: 12px;
}

.fs-13 {
	font-size: 13px;
}

.my-8 {
    margin-left: 8px;
   margin-right: 8px;
}
.mx-3 {
    margin-top: 3px;
	margin-bottom: 3px;
}

.pointer {
	cursor: pointer;
}

.underline {
	text-decoration: underline;
}

.block {
	display: block;
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

.search-tool-button-outer-large {		
	height: 200px;
	width: 200px;
    float: left;	
	background: ${actionBean.branding.primaryColor};
}

.search-tool-button-outer-medium {		
	height: 150px;
	width: 150px;
    float: left;	
	background: ${actionBean.branding.primaryColor};
}

.search-tool-button-outer-small {		
	height: 100px;
	width: 100px;
    float: left;	
	background: ${actionBean.branding.primaryColor};
}


.search-tool-button-inner-large {
	display: table;
	margin: 0 auto;
	/* Secondary Text Center */
	font-size: 24px;
	font-weight: bold;
	color: white;
	padding-top: 18%;
	text-align: center;
}

.search-tool-button-inner-medium {
	display: table;
	margin: 0 auto;
	/* Secondary Text Center */
	font-size: 20px;
	font-weight: bold;
	color: white;
	padding-top: 18%;
	text-align: center;
}

.search-tool-button-inner-small {
	display: table;
	margin: 0 auto;
	/* Secondary Text Center */
	font-size: 16px;
	font-weight: bold;
	color: white;
	padding-top: 18%;
	text-align: center;
}


.search-tool-button-outer-large:hover {
	cursor: pointer;
	opacity: .9;
}

.search-tool-button-outer-medium:hover {
	cursor: pointer;
	opacity: .9;
}

.search-tool-button-outer-small:hover {
	cursor: pointer;
	opacity: .9;
}


.action-tool-button-outer-large {		
	height: 200px;
	width: 200px;
    float: left;	
}

.action-tool-button-outer-medium {		
	height: 150px;
	width: 150px;
    float: left;	
}

.action-tool-button-outer-small {		
	height: 100px;
	width: 100px;
    float: left;	
}

.action-tool-button-outer-large:hover {
	cursor: pointer;
	opacity: .9;
}

.action-tool-button-outer-medium:hover {
	cursor: pointer;
	opacity: .9;
}

.action-tool-button-outer-small:hover {
	cursor: pointer;
	opacity: .9;
}

.action-tool-button-background {
	background: ${actionBean.branding.primaryColor};
}

.action-tool-header {
	font-size: 32px;
    line-height: 32px;	
	color: #252525;
	text-align: center;
	background-color: rgba(255, 255, 255, .5);
}

.action-tool-button-inner-large {
	display: table;
	margin: 0 auto;
	/* Secondary Text Center */
	font-size: 32px;
    line-height: 32px;	
	color: #252525;
	//padding-top: 18%;
	text-align: center;
}

.action-tool-button-inner-medium {
	display: table;
	margin: 0 auto;
	/* Secondary Text Center */
	font-size: 24px;
    line-height: 24px;	
	color: #252525;
	//padding-top: 18%;
	text-align: center;
}

.action-tool-button-inner-small {
	display: table;
	margin: 0 auto;
	/* Secondary Text Center */
	font-size: 16px;
    line-height: 16px;	
	color: #252525;
	//padding-top: 18%;
	text-align: center;
	font-weight: 700;
}

.search-tool-background {
	/* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#ffffff+0,ffffff+100&0.76+0,0+100 */
	background: -moz-linear-gradient(top, rgba(255,255,255,0.76) 0%, rgba(255,255,255,0) 100%); /* FF3.6-15 */
	background: -webkit-linear-gradient(top, rgba(255,255,255,0.76) 0%,rgba(255,255,255,0) 100%); /* Chrome10-25,Safari5.1-6 */
	background: linear-gradient(to bottom, rgba(255,255,255,0.76) 0%,rgba(255,255,255,0) 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#c2ffffff', endColorstr='#00ffffff',GradientType=0 ); /* IE6-9 */
}

.action-tool-background {
	/* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#ffffff+0,ffffff+100&0.76+0,0+100 */
	background: -moz-linear-gradient(top, rgba(255,255,255,0.76) 0%, rgba(255,255,255,0) 100%); /* FF3.6-15 */
	background: -webkit-linear-gradient(top, rgba(255,255,255,0.76) 0%,rgba(255,255,255,0) 100%); /* Chrome10-25,Safari5.1-6 */
	background: linear-gradient(to bottom, rgba(255,255,255,0.76) 0%,rgba(255,255,255,0) 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#c2ffffff', endColorstr='#00ffffff',GradientType=0 ); /* IE6-9 */
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
