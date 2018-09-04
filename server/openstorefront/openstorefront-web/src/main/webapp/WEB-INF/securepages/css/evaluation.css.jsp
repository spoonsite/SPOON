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