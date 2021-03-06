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

h3 {
    line-height: 130%;
}

.submission-question-number {
	font-weight: bold;
	font-size: 14px;
	color: black;
}

.submission-label {
	font-weight: bold;
	font-size: 14px;
	color: black;
}

.submission-section {
}

.submission-section-nav-select {
	background:	black;
}

.submission-section-title {
	font-weight: bold;
	text-align: center;
	font-size: 24px;
	padding-bottom: 10px;
	padding-top: 5px;
}

.submission-instructions {
	padding: 10px;
	font-size: 16px;
	line-height: 100%;
}

.submission-review-table {
	border:1px solid black;
	vertical-align:top;
	text-align:left;
    border-collapse: collapse;    	
}

.submission-review-header {
    border: 1px solid #ddd;
    padding: 8px;
    padding-top: 12px;
    padding-bottom: 12px;
    text-align: left;
    background-color: #3c3c3c;
    color: white;	
}

.submission-review-data {
	padding: 8px;
	border: 1px solid #ddd;
} 

.submission-review-label {
	padding: 8px;
	font-weight: bold;
	border: 1px solid #ddd;
}

.submission-review-row:nth-child(odd) {
	background-color: white;
}

.submission-review-row:nth-child(Even) {
	background-color: whitesmoke;
	
}

.submission-button {
	padding: 20px;
	height: 50px;
	border: 1px solid #ddd;	
	vertical-align: middle;
	background: ${actionBean.branding.primaryColor};
	color: ${actionBean.branding.primaryTextColor};
}

.submission-button:hover {
	opacity: .7;
}

.entry-type-selector:hover {
	background-color: whitesmoke;
	cursor: pointer;
}

.entry-type-select {
	background-color: #ffea7c;
	border-left: 10px darkgrey solid;
}
.entry-type-select:hover {
	background-color: #ffea7c;
	border-left: 10px grey solid;
}

.submission-form-preview {
	text-align: center;
	font-size: 20px;
	font-weight: bold;
	padding-top: 5px;
	padding-bottom: 5px;
}

.submission-form-reviewbutton {
	background-color: #638890;
}