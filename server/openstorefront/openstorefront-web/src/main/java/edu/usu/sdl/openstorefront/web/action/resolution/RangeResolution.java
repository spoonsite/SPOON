/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.action.resolution;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.util.Range;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles media streams with client aborts
 *
 * @author dshurtleff
 */
public class RangeResolution
		extends StreamingResolution
{

	private static final Logger log = Logger.getLogger(RangeResolution.class.getName());

	private static final String MULTIPART_BOUNDARY = "BOUNDARY_F7C98B76AEF711DF86D1B4FCDFD72085";
	private long totalLength = 0;
	private InputStream in;
	private HttpServletRequest request;
	private String contentType;

	public RangeResolution(String contentType, InputStream inputStream, long totalLength, HttpServletRequest request, String filename)
	{
		super(contentType, inputStream);
		this.contentType = contentType;
		in = inputStream;
		this.totalLength = totalLength;
		if (StringUtils.isNotBlank(filename)) {
			setFilename(filename);
			setAttachment(true);
		} else {
			setAttachment(false);
		}
		setRangeSupport(true);
		setLength(totalLength);
		this.request = request;
	}

	@Override
	protected void stream(HttpServletResponse response) throws Exception
	{
		int bufferLength;
		if (in != null && totalLength >= 0) {
			byte[] buffer = new byte[512];
			long count = 0;

			try {
				ServletOutputStream out = response.getOutputStream();

				List<Range<Long>> byteRanges = parseRangeHeader(request.getHeader("Range"));
				if (byteRanges == null) {
					while ((bufferLength = in.read(buffer)) != -1) {
						out.write(buffer, 0, bufferLength);
					}
				} else {
					for (Range<Long> byteRange : byteRanges) {
						// See RFC 2616 section 14.16
						if (byteRanges.size() > 1) {
							out.print("--" + MULTIPART_BOUNDARY + "\r\n");
							out.print("Content-Type: " + contentType + "\r\n");
							out.print("Content-Range: bytes " + byteRange.getStart() + "-"
									+ byteRange.getEnd() + "/" + totalLength + "\r\n");
							out.print("\r\n");
						}
						if (count < byteRange.getStart()) {
							long skip;

							skip = byteRange.getStart() - count;
							long actualbytesSkiped = in.skip(skip);
							log.log(Level.FINEST, MessageFormat.format("Actual Bytes Skipped from range: {0}", actualbytesSkiped));
							count += skip;
						}
						while ((bufferLength = in.read(buffer, 0, (int) Math.min(
								buffer.length, byteRange.getEnd() + 1 - count))) != -1) {
							try {
								out.write(buffer, 0, bufferLength);
							} catch (IOException io) {
								//Client Abort
								break;
							}
							count += bufferLength;
							if (byteRange.getEnd() + 1 == count) {
								break;
							}
						}
						if (byteRanges.size() > 1) {
							out.print("\r\n");
						}
					}
					if (byteRanges.size() > 1) {
						out.print("--" + MULTIPART_BOUNDARY + "--\r\n");
					}
				}
			} catch (IOException io) {
				//Ignore Client Abort
				log.log(Level.FINEST, "Range Resolution Error ", io);
			} finally {
				try {
					in.close();
				} catch (Exception e) {
					log.log(Level.WARNING, "Error closing input stream", e);
				}
			}
		}

	}

}
