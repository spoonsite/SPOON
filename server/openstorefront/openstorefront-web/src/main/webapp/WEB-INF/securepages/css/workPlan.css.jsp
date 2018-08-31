.step-view-container.last-step {
	width: 60px;
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

.static-step-view:hover {
	opacity: 1 !important;
	cursor: default !important;
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
	0%   {border-radius: 100%;}
    60%  {border-radius: 100%;}
    100% {border-radius: 10px;}
}

.wp-step-active {
	background: #82ff82;
	animation-duration: 0.6s;
	animation-name: stepactivate;
	border-radius: 10px;
}

.wp-step-new {
	background: #8aedff;
	border-radius: 100%;
}

.wp-step-existing {
	background: #d479ff;
	border-radius: 100%;
}

.wp-step-migrated {
	background: #ffb744;
	border-radius: 100%;
}

.wp-step-legend {
	float: left;
	margin-top: 3px;
	width: 1em;
	height: 1em;
}

.wp-step-label
 {
	position: absolute;
	left: 18%;
	transform: translateX(-50%);
 }
 .wp-step-label.last-step
 {
	position: absolute;
	left: 50%;
	transform: translateX(-50%);
 }

 @keyframes steplabel {
	0%   {font-weight: inherit;}
    70%  {font-weight: inherit;}
    100% {font-weight: bold;}
 }

 .wp-step-label-active {
	font-weight: bold;
	animation-duration: 0.6s;
	animation-name: steplabel;
 }

 .action-options-fieldset table.x-table-layout {
    width: 100%;
}

.wp-step-label-error {
	color: #ff0000;
}

.wp-step-error {
	border: 3px dotted #ff0000;
}

@keyframes rotatechevronactive {
	0% {
		transform: rotate(0deg);
	}
	100% {
		transform: rotate(180deg);
	}
}

@keyframes rotatechevron {
	0% {
		transform: rotate(180deg);
	}
	100% {
		transform: rotate(0deg);
	}
}

.migration-label {
	border-radius: 10px;
	border: 1px solid #888;
	padding: 1px 3px;
	background: #444;
}

.migration-label:hover {
	background: #777;
}

.migration-label::after {
	font-family: "FontAwesome";
	content: "\f078";
	display: inline-block;
	animation-name: rotatechevron;
	animation-duration: 0.3s;
}

.migration-label:hover::after {
	animation-duration: 0.3s;
	animation-name: rotatechevronactive;
	transform: rotate(180deg);
}
