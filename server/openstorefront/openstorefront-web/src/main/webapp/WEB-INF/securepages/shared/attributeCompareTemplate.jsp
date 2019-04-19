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

<style>
	.quickView {
		background-color: #555555;
		color: #fff;
		padding-left: 5px;
		border: 1px solid #ddd;
		font-size: 20px;
		padding-top: 5px;
		padding-bottom: 5px;
	}
	.quickView-tableheader {
		background-color: lightgrey;
	}
	.quickView-tableall {
		border: 1px solid #ddd;
		text-align: left;
		padding: 5px;
	}

	.quickView-table {
		border-collapse: collapse;
		width: 100%;
	}

	.quickView-table:nth-child(odd) {
		background: white;
	}

	.quickView-table:nth-child(even) {
		background: whitesmoke;
	}

	.quickView-table-padding {
		padding: 15px;
	}

	.review-section
	{
		width:100%;
		border-bottom-style:solid;
		border-bottom-width: thin;
		border-bottom-color:#000000;
	}

	.review-section .label
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
	.review-section .cons
	{
		display: inline-block;
		vertical-align: top;
		padding-bottom: 20px;
	}
	.review-section .pros,
	.review-section .cons
	{
		width: 20%;
		min-width: 100px;
	}
	.review-section .details
	{
		width: 58%;
	}

	.review-section .alert-warning
	{
		font-weight: bold;
		font-size: 1.25em;
		margin: 5px 0px;
	}

	.clearfix:after {
	   content: ".";
	   visibility: hidden;
	   display: block;
	   height: 0;
	   clear: both;
	}

	.version-description {
		font-size: 11px;
		color: #a3a3a3;
	}
	.evaluation-section {
		width: 97%;
		margin-left: 3%;
	}

	.eval-visible-true, .eval-visible-false {
		max-height: 0;
		opacity: 0;
        overflow-y: hidden;
        -webkit-transition: all 0.3s ease-in;
        -moz-transition:    all 0.3s ease-in;
        -o-transition:      all 0.3s ease-in;
        transition:         all 0.3s ease-in;
	}
	.eval-visible-true {
		max-height: 9999px;
		opacity: 1;
		visibility: visible;
	}
	.eval-toggle-caret {
		margin-right: 12px;
		color: #e2e2e2;
	}
	.eval-toggle-caret:hover {
		color: #fff;
		cursor: pointer;
	}
	h1 {
		line-height: 1em;
	}
	h3.quickView {
		background-color: #6c6c6c;
		cursor: pointer;
		height: 50px;
		padding-top: 0.75em;
	}
	.breadcrumbs {
		font-size: 1.2em;
	}
</style>
<tpl if="name">
	<h1>{name}</h1>
	<p>Organization: {organization}</p>
	<tpl if="version">
		<p><b>Version:</b> {version}</p>
	</tpl>
	<tpl if="releaseDate">
		<p><b>Release Date:</b> {releaseDate}</p>
	</tpl>
	<div class="breadcrumbs">
		<tpl for="parents" between="&nbsp; &gt; &nbsp;">
			<a target="_parent" onclick="CoreUtil.saveAdvancedComponentSearch('{componentType}')" href="searchResults.jsp">{label}</a>
		</tpl>
	</div>
	<tpl for="attributes">
		<tpl if="badgeUrl">
			<img src="{badgeUrl}" title="{codeDescription}" width="40" />
		</tpl>
	</tpl>
	<tpl if="releaseDate">
		<br>
		<b>Tags: </b>
		<tpl for="tags">
			<span class="searchresults-tag">
				{text}
			</span>
		</tpl>
	</tpl>
	<div>
		<h3 class="quickView toggle-collapse">Description <div data-qtip="Collapse panel" style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-top eval-toggle-caret" role="presentation"></div></h3>
		<section class="eval-visible-true">
			<p>{description}</p>
		</section>
	</div>
	<tpl if="vitals && vitals.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">Entry Vitals <div data-qtip="Expand panel" style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table class="quickView-table" border="1">
					<tr>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Label</th>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Value</th>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Unit</th>
					</tr>
					<tpl for="vitals">
						<tr class="quickView-table">
							<td class="quickView-tableall" style="width: 30%;"><b>{label}</b>
								<tpl if="privateFlag"> <span class="private-badge">private</span></tpl>
							</td>
							<td class="quickView-tableall">
								<b>{value}</b>
								<tpl if="comment"><hr>Comment: {comment}</tpl>
							</td>
							<td class="quickView-tableall">
								<tpl if="unit">
									<b>{unit}</b>
								</tpl>
							</td>
						</tr>
					</tpl>
				</table>
			</section>
		</div>
	</tpl>
	<!-- BOTTOM SPACER -->
	<div style="margin-bottom: 1em;"></div>
</tpl>
