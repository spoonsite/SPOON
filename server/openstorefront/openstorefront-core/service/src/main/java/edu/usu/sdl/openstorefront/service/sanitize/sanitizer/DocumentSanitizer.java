/**
 * https://github.com/righettod/document-upload-protection
 */
package edu.usu.sdl.openstorefront.service.sanitize.sanitizer;

import java.io.File;
import java.util.Optional;

/**
 * Interface to define sanitize methods.
 *
 */
public interface DocumentSanitizer {

	/**
	 * Method to try to (sanitize) disable any code contained into the specified file by using re-writing approach.
	 * 
	 * @param f File to make safe
	 * 
	 * @return Optional<File>
	 */
	Optional<File> makeSafe(File f);

}
