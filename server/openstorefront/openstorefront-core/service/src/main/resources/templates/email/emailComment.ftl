<!-- FROM: https://github.com/leemunroe/responsive-html-email-template -->

<!--
The MIT License (MIT)

Copyright (c) [2013] [Lee Munroe]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->
<!doctype html>
<html>

<head>
    <meta name="viewport" content="width=device-width" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Request for Information</title>
    <style>
        /* -------------------------------------
                GLOBAL RESETS
        ------------------------------------- */

        /*All the styling goes here*/

        img {
            border: none;
            -ms-interpolation-mode: bicubic;
            max-width: 100%;
        }

        body {
            background-color: #f6f6f6;
            font-family: sans-serif;
            -webkit-font-smoothing: antialiased;
            font-size: 14px;
            line-height: 1.4;
            margin: 0;
            padding: 0;
            -ms-text-size-adjust: 100%;
            -webkit-text-size-adjust: 100%;
        }

        table {
            border-collapse: separate;
            mso-table-lspace: 0pt;
            mso-table-rspace: 0pt;
            width: 100%;
        }

        table td {
            font-family: sans-serif;
            font-size: 14px;
            vertical-align: top;
        }

        .text-block {
            margin-left: 2em;
        }

        /* -------------------------------------
                BODY & CONTAINER
        ------------------------------------- */

        .body {
            background-color: #f6f6f6;
            width: 100%;
        }

        /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */
        .container {
            display: block;
            margin: 0 auto !important;
            /* makes it centered */
            max-width: 580px;
            padding: 10px;
            width: 580px;
        }

        /* This should also be a block element, so that it will fill 100% of the .container */
        .content {
            box-sizing: border-box;
            display: block;
            margin: 0 auto;
            max-width: 580px;
            padding: 10px;
        }

        /* -------------------------------------
                HEADER, FOOTER, MAIN
        ------------------------------------- */
        .main {
            background: #ffffff;
            border-radius: 3px;
            width: 100%;
        }

        .wrapper {
            box-sizing: border-box;
            padding: 20px;
        }

        .content-block {
            padding-bottom: 10px;
            padding-top: 10px;
        }

        .footer {
            clear: both;
            margin-top: 10px;
            text-align: center;
            width: 100%;
        }

        .footer td,
        .footer p,
        .footer span,
        .footer a {
            color: #999999;
            font-size: 12px;
            text-align: center;
        }

        /* -------------------------------------
                TYPOGRAPHY
        ------------------------------------- */
        h1,
        h2,
        h3,
        h4 {
            color: #000000;
            font-family: sans-serif;
            font-weight: 400;
            line-height: 1.4;
            margin: 0;
            margin-bottom: 30px;
        }

        h1 {
            font-size: 35px;
            font-weight: 300;
            text-align: center;
            text-transform: capitalize;
        }

        p,
        ul,
        ol {
            font-family: sans-serif;
            font-size: 14px;
            font-weight: normal;
            margin: 0;
            margin-bottom: 15px;
        }

        p li,
        ul li,
        ol li {
            list-style-position: inside;
            margin-left: 5px;
        }

        a {
            color: #3498db;
            text-decoration: underline;
        }

        /* -------------------------------------
                BUTTONS
        ------------------------------------- */
        .btn {
            box-sizing: border-box;
            width: 100%;
        }

        .btn>tbody>tr>td {
            padding-bottom: 15px;
        }

        .btn table {
            width: auto;
        }

        .btn table td {
            background-color: #ffffff;
            border-radius: 5px;
            text-align: center;
        }

        .btn a {
            background-color: #ffffff;
            border: solid 1px #3498db;
            border-radius: 5px;
            box-sizing: border-box;
            color: #3498db;
            cursor: pointer;
            display: inline-block;
            font-size: 14px;
            font-weight: bold;
            margin: 0;
            padding: 12px 25px;
            text-decoration: none;
            text-transform: capitalize;
        }

        .btn-primary table td {
            background-color: #3498db;
        }

        .btn-primary a {
            background-color: #3498db;
            border-color: #3498db;
            color: #ffffff;
        }

        /* -------------------------------------
            OTHER STYLES THAT MIGHT BE USEFUL
        ------------------------------------- */
        .last {
            margin-bottom: 0;
        }

        .first {
            margin-top: 0;
        }

        .align-center {
            text-align: center;
        }

        .align-right {
            text-align: right;
        }

        .align-left {
            text-align: left;
        }

        .clear {
            clear: both;
        }

        .mt0 {
            margin-top: 0;
        }

        .mb0 {
            margin-bottom: 0;
        }

        .preheader {
            color: transparent;
            display: none;
            height: 0;
            max-height: 0;
            max-width: 0;
            opacity: 0;
            overflow: hidden;
            mso-hide: all;
            visibility: hidden;
            width: 0;
        }

        .powered-by a {
            text-decoration: none;
        }

        hr {
            border: 0;
            border-bottom: 1px solid #f6f6f6;
            margin: 20px 0;
        }

        /* -----------------------------------------
            RESPONSIVE AND MOBILE FRIENDLY STYLES
        ------------------------------------------ */
        @media only screen and (max-width: 620px) {
            table[class=body] h1 {
                font-size: 28px !important;
                margin-bottom: 10px !important;
            }

            table[class=body] p,
            table[class=body] ul,
            table[class=body] ol,
            table[class=body] td,
            table[class=body] span,
            table[class=body] a {
                font-size: 16px !important;
            }

            table[class=body] .wrapper,
            table[class=body] .article {
                padding: 10px !important;
            }

            table[class=body] .content {
                padding: 0 !important;
            }

            table[class=body] .container {
                padding: 0 !important;
                width: 100% !important;
            }

            table[class=body] .main {
                border-left-width: 0 !important;
                border-radius: 0 !important;
                border-right-width: 0 !important;
            }

            table[class=body] .btn table {
                width: 100% !important;
            }

            table[class=body] .btn a {
                width: 100% !important;
            }

            table[class=body] .img-responsive {
                height: auto !important;
                max-width: 100% !important;
                width: auto !important;
            }
        }

        /* -------------------------------------
            PRESERVE THESE STYLES IN THE HEAD
        ------------------------------------- */
        @media all {
            .ExternalClass {
                width: 100%;
            }

            .ExternalClass,
            .ExternalClass p,
            .ExternalClass span,
            .ExternalClass font,
            .ExternalClass td,
            .ExternalClass div {
                line-height: 100%;
            }

            .apple-link a {
                color: inherit !important;
                font-family: inherit !important;
                font-size: inherit !important;
                font-weight: inherit !important;
                line-height: inherit !important;
                text-decoration: none !important;
            }

            #MessageViewBody a {
                color: inherit;
                text-decoration: none;
                font-size: inherit;
                font-family: inherit;
                font-weight: inherit;
                line-height: inherit;
            }

            .btn-primary table td:hover {
                background-color: #34495e !important;
            }

            .btn-primary a:hover {
                background-color: #34495e !important;
                border-color: #34495e !important;
            }
        }
    </style>
</head>

<body class="">
    <span class="preheader">A ${applicationName} entry you are watching has been updated.</span>
    <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="body">
        <tr>
            <td>&nbsp;</td>
            <td class="container">
                <div class="content">

                    <!-- START CENTERED WHITE CONTAINER -->
                    <table role="presentation" class="main">

                        <!-- START MAIN CONTENT AREA -->
                        <tr>
                            <td class="wrapper">
                                <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td>
                                            <!-- This is the SPOON Logo -->
                                            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAKEAAAA8CAMAAAA5dyoZAAAC61BMVEUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD///+qkWUxAAAA93RSTlMAAQIDBAUGBwgJCgsMDQ4PEBESExQWFxgZGhscHR4fICEiIyQlJicoKSorLC0uLzAxMjM0NTY3ODk6Ozw9Pj9AQUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVpbXF1eX2BhYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5ent8fX5/gIGCg4SFh4iJioyNjo+QkZKTlJWWl5iZmpudnp+goaKjpKWmqKmqq6ytrq+wsbKztLW2t7i5uru8vb6/wMHCxMXHyMnKy8zNzs/Q0dLT1NXW19jZ2tvc3t/g4eLj5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+QG98mQAAAAFiS0dE+DtjZ2YAAAbDSURBVGje7ZhpWFRlFMfPzLDMgIIMi2wDgoCyI4oyDKsjKjiCiCCKgJhDaqICCpIVpiSUohKpmFqapVLhvkRuJZgo7maFihmobCZBMudr9856hxnGwZ5HqGf+H+Ce85479/ee+y7nvQA66aSTTjr9NzVsoAOO2jTQCdf8zlAYgwYi4XWMUBjvDbxXbrgA8ZSz3LyV99I72Gwj/dcIOOcREnqx09TJkglAT8PbIzWFD/VydTR4vSmkC6oRWwosfGLnLMzi7SVou6b1EmoQxudZ9MtrbsU35bxz8XZ6Ek1NFI0nTHHsr5G4u9NMYVzLAf5OHolEDeG9nenWj3Nl+mGK8Y49gN78Ug47We5ipW9INerX2Ww0nWIMEf+1XJVXZSJxeG5Y69LvC46acedxvimF/B+yq9D2JVPNTCJph8DESSy2YoL1nHCyC46V3GfO55v0ldpzTXYkLepAlrHmsCHlLShV/UIA4RFYIDXr/MSPfv/uQSg9lyUb6TbF9Tug5NwyU+I68Fcn2c9MRAzoc2JtaYtPLDcGd5UGpc4eQIqSxohq5ITYQFBMbkQ8CNsQHwSLw2cS/dkBHxPd4RKEeHfovyGcVLXSapsN7bLKS8qiDuEXWJUrUf6dp18WYg3Y8cVaijgDJnWh6HQ8zKxF7IgkwpO6sfvYZJjxE+KfEQQh8l+dMOJMAVFDGBVG4oQeLeOoM5+DmEoxywhCWQWCuIzdiM+niq3UDnxiA5x2bAkX28nPsWkoSahv92qE9l8dEb9dZl4TNhcolzvZzXRtCQsQE6RmjAhLiLfbLUtalAg3kITG91xegdBw5Y1o6WXI9/hjbM5sB6WR564t4R08K286ho36j7FCbh/EpiCSEB959pkw8maJImt2Ih+iXBDkF+TzDSWeRzhfEcvswpqtEpUtGNKDMB8xWx6ZjjiR2p05iLMIQlY3NozsG6HFZ3VjqfZe2RN5WQUrBWxwIiejQjsoU7mBo0y4FnGmPDAcMUMxM4h9FHERac/owuaxfSCkL/kjV0/J42OquGaF5uXeQvyr3FXh+vC3pxK1IpaThL6SlO5DnI2YKQ+MR4wl0ia3YxDniomjOrCFqzWh2YFro3tu2p7UpfC0JFvt4WpuPo7VJGGsLKVXWPfxuLx1C3aYteEeub0ZO8dLchrWjs+KtSTk1ZcYqviUSi76zOtEDjdZq7t7P9aShGEXxbqw1RzWoyhM2uj6HCvhc+zyldrOz/CobD3ktZId0oJQf2NTtBrvVmV7tAjL1N8vJaQW5C34JFQy4W5hdwC4dmCDv2Tzu44irnzF9m/SitC5+gd7Ne7gLjOq6ZB6A+MVpvHJk9n+UtXg2R6EML0bX+zhw4jCJ8S8IWwhUcFvDwLXNY8Riyh7ivtDLQijmraz1PnLMJlqvkMv7TZXOidStLonISS2SfdlUbG4gMrokO7LovV06q7ndv+lhLntaerc1qWd+Dh/sNyODoS4S0ovvUEBeJSlQgh2m58ShKIq2Xh0KW8jCU+PJ2sbyurjeFczofG+ek+1hWM2UZvgHdmqRmcWEPVWkVLIoCC+VD404AlVT2EMG3Cxoth61jBcUtKY8vmKKnKYxkOQw5VLNr3V399hkdvqdHEW3QRLyEcp/9QYCw89N6aJK3iArSm4OBt4m/p6A2e8kfkwRrCXpa/HoEAGuAX5ONjBCCLlfAtPbyfOJLaLIHU8BIG5pR2X60Y3dwWNZ2DvB6fNem1MRILIemmRgAF5n2aoNKdUBdePvucvrIOHUDptxI3k0Fafy6Ww7tuj754YcrN29s3CU58YsoNh4bhFMAW4lrSFQojyp82fAsTeOTfY2X0qxAV6xkayQzQABjd/YajhEHNesujEFblebnRWaY4/Y/bzqjZ++TcmJOHw2x7LK0ft8IJ1xRW7D7HrjplXcCpXQIQBhMdwx0VDFMC8nMQJSXmBYkL/pS4hvsSuHz2LL2D2jpDQuZ6mKcN+0v9BzYidiSrNwv37Lx1MqL3KIwnB75d9dekkYQ7z5v1R1y4wKjisk1MC7CA+FJZEQBxRQQjsFwvAX0JokzmGD14BkbMiEnoHSPy7hKbdnm2pLodTz36wbVdK5pm4RW2Hyy6+caiyeuJGgnAF16FsdcrXO30qOB9ddae9FZ3gDw7LwFYYGyqAjLSYaRJCyBiZFj2PluDHEfb+3O3FWpdl2VvUOFl0I2AYGTNYllaDrVgsMKEbm5C1hx7TgBw7pnQ98iuUMTDEhzwGE/TB0IJDXBOrLwMYDKIJ9Gmg4VuQpfaFo2PkgP+aTNd9UNdJJ5100kknnXT6X+ofOWn7dciokIEAAAAASUVORK5CYII=" alt="SPOON Logo" />
                                            <br/>
                                            <h2>${entryName} has been updated.</h2>
                                            <p>Hi there,</p>
                                            This is an auto generated email to show the comments that were made to a component
											that you are subscribed to. <br>
											<strong>Entry:</strong> ${entryName} <br>
											<strong>Current-Step:</strong> ${currentStep} <br>
											<strong>author:</strong> ${author} <br><br><br>
											<strong>Comment:</strong> ${comment} <br><br><br>
											<strong>Reply-Instructions:</strong> ${replyInstructions} <br>
											<p>If you have any questions, comments, or concerns, please contact Support at ${supportEmail}</p>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<!-- END MAIN CONTENT AREA -->
						</table>
						<!-- END CENTERED WHITE CONTAINER -->

						<!-- START FOOTER -->
						<div class="footer">
							<table role="presentation" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="content-block">
										<span class="apple-link">
											<a href="${hostUrl}">
												${applicationName}
											</a>
										</span>
									</td>
								</tr>
							</table>
						</div>
						<!-- END FOOTER -->

					</div>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</body>

	</html>