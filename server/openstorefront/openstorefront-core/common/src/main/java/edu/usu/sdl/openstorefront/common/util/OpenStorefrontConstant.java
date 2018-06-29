/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.common.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Global Constants used in the Application
 *
 * @author dshurtleff
 */
public class OpenStorefrontConstant
{

	public static final String SORT_ASCENDING = "ASC";
	public static final String SORT_DESCENDING = "DESC";
	public static final String MAX_RECORDS = Integer.toString(Integer.MAX_VALUE);
	public static final String SYSTEM_USER = "SYSTEM";
	public static final String SYSTEM_ADMIN_USER = "SYSTEM-ADMIN";
	public static final String ANONYMOUS_USER = "ANONYMOUS";
	public static final String NOT_AVAILABLE = "NA";
	public static final String ERROR_LOGGER = "edu.usu.sdl.openstorefront.SYSTEM-ERROR-LOGGER";
	public static final String HEADER_USER_AGENT = "User-Agent";
	public static final String STATUS_VIEW_ALL = "ALL";
	public static final String DEFAULT_FROM_ADDRESS = "noreply@storefront.net";
	public static final String GENERAL_KEY_SEPARATOR = ":";

	//Field Max Sizes
	public static final int FIELD_SIZE_CRON = 250;
	public static final int FIELD_SIZE_USERNAME = 80;
	public static final int FIELD_SIZE_FIRSTNAME = 80;
	public static final int FIELD_SIZE_LASTNAME = 80;
	public static final int FIELD_SIZE_FULLNAME = 80;
	public static final int FIELD_SIZE_EMAIL = 250;
	public static final int FIELD_SIZE_EMAIL_LIST_SIZE = 32000;
	public static final int FIELD_SIZE_ORGANIZATION = 120;
	public static final int FIELD_SIZE_CODE = 20;
	public static final int FIELD_SIZE_PHONE = 80;
	public static final int FIELD_SIZE_DESCRIPTION = 255;
	public static final int FIELD_SIZE_COMPONENT_NAME = 255;
	public static final int FIELD_SIZE_COMPONENT_DESCRIPTION = 32000;
	public static final int FIELD_SIZE_GUID = 40;
	public static final int FIELD_SIZE_GENERAL_TEXT = 255;
	public static final int FIELD_SIZE_URL = 1024;
	public static final int FIELD_SIZE_QUESTION = 1024;
	public static final int FIELD_SIZE_RESPONSE = 4096;
	public static final int FIELD_SIZE_REVIEW_COMMENT = 4096;
	public static final int FIELD_SIZE_DETAILED_DESCRIPTION = 4096;
	public static final int FIELD_SIZE_ADMIN_MESSAGE = 32000;
	public static final int FIELD_SIZE_TAG = 60;
	public static final int FIELD_SIZE_ARTICLE_SIZE = 10485760;
	public static final int FIELD_SIZE_80 = 80;
	public static final int FIELD_SIZE_60 = 60;
	public static final int FIELD_SIZE_255 = 255;
	public static final int FIELD_SIZE_510 = 510;
	public static final int FIELD_SIZE_1K = 1024;
	public static final int FIELD_SIZE_4K = 4096;
	public static final int FIELD_SIZE_16K = 16384;
	public static final int FIELD_SIZE_32K = 32768;
	public static final int FIELD_SIZE_64K = 65536;
	public static final int FIELD_SIZE_1MB = 1048576;

	//This should only be used for warning; Not for storage validation.
	//This came from http://emailregex.com/  Previous one didn't cover all cases.
	public static final String EMAIL_PATTERN = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

	public static final String ERRORS_MAX_COUNT_DEFAULT = "5000";
	public static final String DEFAULT_RECENT_CHANGE_EMAIL_INTERVAL = "28";

	public static final String ERROR_CODE_SYSTEM = "SYS";
	public static final String ERROR_CODE_REST_API = "API";
	public static final String ERROR_CODE_INTEGRATION = "INT";
	public static final String ERROR_CODE_REPORT = "RPT";

	private static final Map<String, String> mimeXref = loadMimeXref();
	private static final Map<String, String> mimeTypeMap = loadMimeTypeMap();
	private static final Map<String, String> extMap = loadExtMap();

	/**
	 * used to select a standardized extension for mime types that have multiple
	 * extensions
	 *
	 * @return
	 */
	private static Map<String, String> loadExtMap()
	{
		Map<String, String> extMapLocal = new HashMap<>();
		extMapLocal.put(".arc", ".bin");	//Archive document (multiple files embedded)
		extMapLocal.put(".htm", ".html");	//HyperText Markup Language (HTML)
		extMapLocal.put(".jpeg", ".jpg");	//JPEG images
		extMapLocal.put(".mid", ".midi");	//Musical Instrument Digital Interface (MIDI)
		extMapLocal.put(".mpeg", ".mpg");	//OGG audio
		extMapLocal.put(".ogg", ".oga");	//OGG audio
		extMapLocal.put(".tiff", ".tif");	//Tagged Image File Format (TIFF)
		return extMapLocal;
	}

	/**
	 * used to select a standardized mimeType for extensions that have multiple
	 * mime types https://www.iana.org/assignments/media-types/media-types.xhtml
	 *
	 * @return
	 */
	private static Map<String, String> loadMimeTypeMap()
	{
		Map<String, String> mimeTypeMapLocal = new HashMap<>();
		mimeTypeMapLocal.put("application/doc", "application/msword");	//Microsoft Word
		mimeTypeMapLocal.put("application/docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");	//Microsoft Word
		mimeTypeMapLocal.put("application/xls", "application/vnd.ms-excel");	//Microsoft Excel
		mimeTypeMapLocal.put("application/xml", "text/xml");	//XML
		mimeTypeMapLocal.put("audio/mp4", "video/mp4");	//MP4 Audio
		mimeTypeMapLocal.put("audio/mpeg", "video/mpg");	//MPEG Audio
		mimeTypeMapLocal.put("audio/mpg", "video/mpg");	//MPEG Audio
		mimeTypeMapLocal.put("image/jpeg", "image/jpg");	//JPEG images
		mimeTypeMapLocal.put("text/json", "application/json");	//JSON format
		mimeTypeMapLocal.put("video/mpeg", "video/mpg");	//MPEG Video
		mimeTypeMapLocal.put("video/x-msvideo", "video/avi");	//AVI: Audio Video Interleave

		return mimeTypeMapLocal;
	}

	/**
	 * list based off
	 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types
	 * "... table lists some important MIME types for the Web:"
	 *
	 * @return
	 */
	private static Map<String, String> loadMimeXref()
	{
		Map<String, String> mimeXrefLocal = new HashMap<>();
		mimeXrefLocal.put("application/epub+zip", ".epub");	//Electronic publication (EPUB)
		mimeXrefLocal.put("application/gzip", ".gzip");	//GNU Gzip
		mimeXrefLocal.put("application/java-archive", ".jar");	//Java Archive (JAR)
		mimeXrefLocal.put("application/javascript", ".js");	//JavaScript (ECMAScript)
		mimeXrefLocal.put("application/json", ".json");	//JSON format
		mimeXrefLocal.put("application/msword", ".doc");	//Microsoft Word
		mimeXrefLocal.put("application/octet-stream", ".bin");	//Any kind of binary data
		mimeXrefLocal.put("application/ogg", ".ogx");	//OGG
		mimeXrefLocal.put("application/pdf", ".pdf");	//Adobe Portable Document Format (PDF)
		mimeXrefLocal.put("application/rtf", ".rtf");	//Rich Text Format (RTF)
		mimeXrefLocal.put("application/typescript", ".ts");	//Typescript file
		mimeXrefLocal.put("application/vnd.amazon.ebook", ".azw");	//Amazon Kindle eBook format
		mimeXrefLocal.put("application/vnd.apple.installer+xml", ".mpkg");	//Apple Installer Package
		mimeXrefLocal.put("application/vnd.mozilla.xul+xml", ".xul");	//XUL
		mimeXrefLocal.put("application/vnd.ms-excel", ".xls");	//Microsoft Excel
		mimeXrefLocal.put("application/vnd.ms-fontobject", ".eot");	//MS Embedded OpenType fonts
		mimeXrefLocal.put("application/vnd.ms-powerpoint", ".ppt");	//Microsoft PowerPoint
		mimeXrefLocal.put("application/vnd.oasis.opendocument.chart", ".odc");	//OpenDocument Chart
		mimeXrefLocal.put("application/vnd.oasis.opendocument.chart-template", ".otc");	//OpenDocument Chart Template
		mimeXrefLocal.put("application/vnd.oasis.opendocument.database", ".odb");	//OpenDocument Database
		mimeXrefLocal.put("application/vnd.oasis.opendocument.formula", ".odf");	//OpenDocument Formula
		mimeXrefLocal.put("application/vnd.oasis.opendocument.formula-template", ".odft");	//OpenDocument Formula Template
		mimeXrefLocal.put("application/vnd.oasis.opendocument.graphics", ".odg");	//OpenDocument Graphics
		mimeXrefLocal.put("application/vnd.oasis.opendocument.graphics-template", ".otg");	//OpenDocument Graphics Template
		mimeXrefLocal.put("application/vnd.oasis.opendocument.image", ".odi");	//OpenDocument Image
		mimeXrefLocal.put("application/vnd.oasis.opendocument.image-template", ".oti");	//OpenDocument Image Template
		mimeXrefLocal.put("application/vnd.oasis.opendocument.presentation", ".odp");	//OpenDocument Presentation
		mimeXrefLocal.put("application/vnd.oasis.opendocument.presentation-template", ".otp");	//OpenDocument Presentation Template
		mimeXrefLocal.put("application/vnd.oasis.opendocument.spreadsheet", ".ods");	//OpenDocument Spreadsheet
		mimeXrefLocal.put("application/vnd.oasis.opendocument.spreadsheet-template", ".ots");	//OpenDocument Spreadsheet Template
		mimeXrefLocal.put("application/vnd.oasis.opendocument.text", ".odt");	//OpenDocument Text
		mimeXrefLocal.put("application/vnd.oasis.opendocument.text-master", ".odm");	//OpenDocument Text Master
		mimeXrefLocal.put("application/vnd.oasis.opendocument.text-template", ".ott");	//OpenDocument Text Template
		mimeXrefLocal.put("application/vnd.oasis.opendocument.text-web", ".oth");	//Open Document Text Web
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");	//Microsoft Office - OOXML - Presentation
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.presentationml.slide", ".sldx");	//Microsoft Office - OOXML - Presentation (Slide)
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.presentationml.slideshow", ".ppsx");	//Microsoft Office - OOXML - Presentation (Slideshow)
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.presentationml.template", ".potx");	//Microsoft Office - OOXML - Presentation Template
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");	//Microsoft Office - OOXML - Spreadsheet
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.spreadsheetml.template", ".xltx");	//Microsoft Office - OOXML - Spreadsheet Template
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");	//Microsoft Office - OOXML - Word Document
		mimeXrefLocal.put("application/vnd.openxmlformats-officedocument.wordprocessingml.template", ".dotx");	//Microsoft Office - OOXML - Word Document Template
		mimeXrefLocal.put("application/vnd.visio", ".vsd");	//Microsoft Visio
		mimeXrefLocal.put("application/x-7z-compressed", ".7z");	//7-zip archive
		mimeXrefLocal.put("application/x-abiword", ".abw");	//AbiWord document
		mimeXrefLocal.put("application/x-bzip", ".bz");	//BZip archive
		mimeXrefLocal.put("application/x-bzip2", ".bz2");	//BZip2 archive
		mimeXrefLocal.put("application/x-csh", ".csh");	//C-Shell script
		mimeXrefLocal.put("application/xhtml+xml", ".xhtml");	//XHTML
		mimeXrefLocal.put("application/x-rar-compressed", ".rar");	//RAR archive
		mimeXrefLocal.put("application/x-sh", ".sh");	//Bourne shell script
		mimeXrefLocal.put("application/x-shockwave-flash", ".swf");	//Small web format (SWF) or Adobe Flash document
		mimeXrefLocal.put("application/x-tar", ".tar");	//Tape Archive (TAR)
		mimeXrefLocal.put("application/zip", ".zip");	//ZIP archive
		mimeXrefLocal.put("audio/aac", ".aac");	//AAC audio file
		mimeXrefLocal.put("audio/midi", ".midi");	//Musical Instrument Digital Interface (MIDI)
		mimeXrefLocal.put("audio/ogg", ".oga");	//OGG audio
		mimeXrefLocal.put("audio/webm", ".weba");	//WEBM audio
		mimeXrefLocal.put("audio/x-wav", ".wav");	//Waveform Audio Format
		mimeXrefLocal.put("font/otf", ".otf");	//OpenType font
		mimeXrefLocal.put("font/ttf", ".ttf");	//TrueType Font
		mimeXrefLocal.put("font/woff", ".woff");	//Web Open Font Format (WOFF)
		mimeXrefLocal.put("font/woff2", ".woff2");	//Web Open Font Format (WOFF)
		mimeXrefLocal.put("image/bmp", ".bmp");	//Windows Bitmap format
		mimeXrefLocal.put("image/gif", ".gif");	//Graphics Interchange Format (GIF)
		mimeXrefLocal.put("image/jpg", ".jpg");	//JPEG images
		mimeXrefLocal.put("image/png", ".png");	//Portable Network Graphics
		mimeXrefLocal.put("image/svg+xml", ".svg");	//Scalable Vector Graphics (SVG)
		mimeXrefLocal.put("image/tiff", ".tif");	//Tagged Image File Format (TIFF)
		mimeXrefLocal.put("image/webp", ".webp");	//WEBP image
		mimeXrefLocal.put("image/x-icon", ".ico");	//Icon format
		mimeXrefLocal.put("text/calendar", ".ics");	//iCalendar format
		mimeXrefLocal.put("text/css", ".css");	//Cascading Style Sheets (CSS)
		mimeXrefLocal.put("text/csv", ".csv");	//Comma-separated values (CSV)
		mimeXrefLocal.put("text/html", ".html");	//HyperText Markup Language (HTML)
		mimeXrefLocal.put("text/plain", ".txt");	//Plain Text
		mimeXrefLocal.put("text/tsv", ".tsv");	//Tab Separated Values (TSV)
		mimeXrefLocal.put("text/xml", ".xml");	//XML
		mimeXrefLocal.put("video/avi", ".avi");	//AVI: Audio Video Interleave
		mimeXrefLocal.put("video/mp4", ".mp4");	//MP4 Video
		mimeXrefLocal.put("video/mpg", ".mpg");	//MPEG Video
		mimeXrefLocal.put("video/ogg", ".ogv");	//OGG video
		mimeXrefLocal.put("video/webm", ".webm");	//WEBM video
		mimeXrefLocal.put("application/CDFV2-corrupt", ".msg");	//Outlook email

		return mimeXrefLocal;
	}

	public static String getFileExtensionForMime(String mimeType)
	{
		String ext = NOT_AVAILABLE;
		if (mimeType != null) {
			if (mimeTypeMap.containsKey(mimeType.toLowerCase())) {
				mimeType = mimeTypeMap.get(mimeType.toLowerCase());
			}
			String found = mimeXref.get(mimeType.toLowerCase());
			if (found != null) {
				ext = found;
			}
		}
		return ext;
	}

	public static String getMimeForFileExtension(String fileExtension)
	{
		String mime = "application/octet-stream";
		if (StringUtils.isNotBlank(fileExtension)) {
			String extensionToCheck = fileExtension;
			if (extensionToCheck.startsWith(".") == false) {
				extensionToCheck = "." + fileExtension;
			}
			if (extMap.containsKey(extensionToCheck.toLowerCase())) {
				extensionToCheck = extMap.get(extensionToCheck.toLowerCase());
			}
			if (mimeXref.containsValue(extensionToCheck.toLowerCase())) {
				//get first match
				for (String extCheckKey : mimeXref.keySet()) {
					String value = mimeXref.get(extCheckKey);
					if (value.equalsIgnoreCase(extensionToCheck.toLowerCase())) {
						mime = extCheckKey;
						break;
					}
				}
			}
		}
		return mime;
	}

	public static enum ListingType
	{

		ARTICLE("Article"),
		COMPONENT("Component");

		private final String description;

		private ListingType(String description)
		{
			this.description = description;
		}

		public String getDescription()
		{
			return description;
		}

	}

	public static enum TaskStatus
	{

		QUEUED,
		WORKING,
		DONE,
		CANCELLED,
		FAILED

	}

}
