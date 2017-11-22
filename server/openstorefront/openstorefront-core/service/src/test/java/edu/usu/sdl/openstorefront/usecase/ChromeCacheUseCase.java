/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.usecase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ChromeCacheUseCase
{

	@Test
	public void decodeCacheFile() throws IOException
	{
		File input = new File("/test/hexbytes.txt");

		ByteArrayOutputStream byteout = new ByteArrayOutputStream();

		List<String> lines = Files.readAllLines(input.toPath());

		for (String line : lines) {
			String hexBytes = line.substring(0, 47);
			String data[] = hexBytes.split(" ");
			for (String hexByte : data) {
				byteout.write(Integer.valueOf(hexByte, 16));
			}
		}

		File output = new File("/test/entry.json");
		FileOutputStream out;
		try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(byteout.toByteArray()))) {
			out = new FileOutputStream(output);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gzip.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		}
		out.close();

	}

}
