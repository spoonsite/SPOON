/*
 * Copyright 2020 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.rest.resource;

/**
 *
 * @author afine
 */
public class Utils
{
	
	/**
	 * If the input stream and associated source file name represent a ZIP file, then save the file to disk with a unique filename.
	 * @param inStream InputStream of the file to be saved
	 * @param sourceFileName String of the original file name. Note: must end in ".zip"
	 * @return A RestErrorModel, suitable for use in a Response, containing a message and success state. 
	 */
	/**
	 * Writes an InputStream to disk in the specified location. 
	 * Ensures that the generated file is not larger that the value stored in max.post.size 
	 * 
	 * Example: writeStreamToFile(inStream, filePath).getSuccess() // True if file saved successfully
	 * @param in InputStream of the file to be saved
	 * @param fileLocation String of the path and file name for the new file
	 * @return A RestErrorModel, suitable for use in a Response, containing a message and success state. 
	 */
}
