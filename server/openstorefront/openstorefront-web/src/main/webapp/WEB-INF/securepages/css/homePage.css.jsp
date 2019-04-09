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
	height: 50px !important;
	line-height: 50px;
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
	height: 50px !important;
	line-height: 50px;
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

.home-highlight-update {
	font-size: 10px;
    float: left;
    padding-top: 10px;
    padding-left: 45px;
    color: BLACK;
}

.home-highlight-update-recent {
	font-size: 10px;
    float: left;
    padding-top: 10px;  
    color: BLACK;
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
    /* font-size: 24px; */
    /* height: 50px; */
    margin-top: 50px;
    /* background: ${actionBean.branding.quoteColor}; */
    letter-spacing: 2px;
    text-transform: uppercase;
    font: 20px "Lato", sans-serif;
    color: #111;	
	text-overflow: ellipsis;
}

.home-highlight-header a {
	//color: white;	
	text-decoration: none;
}

.home-highlight-header-recent {
    text-align: left;
    /* font-size: 24px; */
    /* height: 50px; */
    margin-top: 50px;
    /* background: ${actionBean.branding.quoteColor}; */
    letter-spacing: 2px;
    text-transform: uppercase;
    font: 20px "Lato", sans-serif;
    color: #111;	
	text-overflow: ellipsis;
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

.home-readmore-recent {
	padding: 0.5em 2em;
    border: 0.25em solid ${actionBean.branding.quoteColor};
    font-size: .7em;
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

.home-footer-disclaimer-text{
	color:white;
	font-style: italic;
	display:block;
	 font-align:center;
	 padding-left:20%;
	 padding-right:20%;
}

.home-page-top-logo {
	z-index: 10;
	position: fixed !important;
	top: 10px !important;
	left: 30px !important;
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