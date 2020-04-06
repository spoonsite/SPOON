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

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import org.apache.commons.io.FilenameUtils;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author afine
 */
public class Utils
{
	static final Logger LOG = Logger.getLogger(Utils.class.getName());
	
	/**
	 * If the input stream and associated source file name represent a ZIP file, then save the file to disk with a unique filename.
	 * @param inStream InputStream of the file to be saved
	 * @param sourceFileName String of the original file name. Note: must end in ".zip"
	 * @return A RestErrorModel, suitable for use in a Response, containing a message and success state. 
	 */
	public static RestErrorModel handleZipFileUpload(InputStream inStream,
			String sourceFileName)
	{
		RestErrorModel restErrorModel = new RestErrorModel();
		
		// Generate filename to use for uploaded file
		String username = SecurityUtil.getCurrentUserName();
		String timeStamp = new SimpleDateFormat("dd-MM-YYYY").format(new Date());
		String filePath = FileSystemManager
				.getInstance()
				.getDir(FileSystemManager.BULK_UPLOAD_DIR)
				.toString()
				+ File.separator
				+ username
				+ File.separator
				+ timeStamp
				+ "_"
				+ StringProcessor.uniqueId()
				+ ".zip";

		// Validate file type and save the file
		String extension = FilenameUtils.getExtension(sourceFileName);
		if (extension.equals("zip")) {
			restErrorModel = writeStreamToFile(inStream, filePath);
			if (!restErrorModel.getSuccess()) {
				return restErrorModel;
			}
		} else {
			restErrorModel.getErrors().put("message", "Uploaded file was not a zip file.");
			restErrorModel.getErrors().put("potentialResolution", "Ensure filename ends with .zip");
			restErrorModel.setSuccess(false);
			return restErrorModel;
		}

		// Send email to spoon support telling them that there is a new uploaded file.
		String recipientAddress = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_FEEDBACK_EMAIL);
		String senderAddress = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_FROM_ADDRESS);
		String senderName = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_FROM_NAME);
		if (recipientAddress == null) {
			recipientAddress = "";
		}
		if (senderAddress == null) {
			senderAddress = "";
		}
		if (senderName == null) {
			senderName = "";
		}

		if (!recipientAddress.isEmpty() && !senderAddress.isEmpty() && !senderName.isEmpty()) {
			Email email = MailManager.newEmail();
			email.setSubject("SpoonSite bulk Upload");
			email.setText("There is a new bulk upload to be reviewed at " + filePath);
			email.addRecipient("Admin", recipientAddress, Message.RecipientType.TO);
			email.setFromAddress(senderName, senderAddress);
			MailManager.send(email, true);
		} else {
			LOG.log(Level.WARNING, "Email doesn't have an email correctly defined.");
			restErrorModel.getErrors().put("message", "Could not send notification email.");
			restErrorModel.getErrors().put("potentialResolution", "Set feedback email, mail from addresss, and mail from name");
			restErrorModel.setSuccess(false);
			return restErrorModel;
		}
		restErrorModel.getErrors().put("message", "File uploaded sucessfully.");
		restErrorModel.setSuccess(true);
		return restErrorModel;
	}

	/**
	 * Writes an InputStream to disk in the specified location. 
	 * Ensures that the generated file is not larger that the value stored in max.post.size 
	 * 
	 * Example: writeStreamToFile(inStream, filePath).getSuccess() // True if file saved successfully
	 * @param in InputStream of the file to be saved
	 * @param fileLocation String of the path and file name for the new file
	 * @return A RestErrorModel, suitable for use in a Response, containing a message and success state. 
	 */
	public static RestErrorModel writeStreamToFile(InputStream in, String fileLocation)
	{
		RestErrorModel restErrorModel = new RestErrorModel();
		
		// Get the maximum allowed file size. Note: value stored in max.post.size is in MegaBytes. 
		String maxSizeString = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAX_POST_SIZE);
		int maxSize;
		if (maxSizeString != null) {
			try {
				maxSize = Integer.parseInt(maxSizeString);
			} catch (NumberFormatException ex) {
				LOG.log(Level.SEVERE, "Could not parse key KEY_MAX_POST_SIZE. Ensure that max.post.size is set to an integer.\n{0}", ex.toString());
				restErrorModel.getErrors().put("message", "Unable to determine max upload size.");
				restErrorModel.getErrors().put("potentialResolution", "Ensure that max.post.size is set to an integer.");
				restErrorModel.setSuccess(false);
				return restErrorModel;
			}
		} else {
			LOG.log(Level.SEVERE, "Could not find key KEY_MAX_POST_SIZE. Ensure that max.post.size is set to an integer.");
			restErrorModel.getErrors().put("message", "Unable to determine max upload size.");
			restErrorModel.getErrors().put("potentialResolution", "Ensure that max.post.size is set to an integer.");
			restErrorModel.setSuccess(false);
			return restErrorModel;
		}
		final int mBtoBytesConversionFactor = 1000000;
		maxSize *= mBtoBytesConversionFactor;

		// Prepare the read buffer and associated counters
		int bytesReadIntoBuffer;
		int totalReads = 0;
		// Note buffer size is arbirtary
		byte[] buffer = new byte[1024];
		
		// Prepare destination file
		File uploadedFile = new File(fileLocation);
		if (uploadedFile.getParentFile() != null) {
			uploadedFile.getParentFile().mkdirs();
		}

		try {
			uploadedFile.createNewFile();
			// Store output of stream read and check to see if read returned an error code
			try (FileOutputStream out = new FileOutputStream(fileLocation)) {
				// Store output of stream read and check to see if read returned an error code
				while ((bytesReadIntoBuffer = in.read(buffer)) != -1) {
					// No error code, so bytesReadIntoBuffer contains valid data
					out.write(buffer, 0, bytesReadIntoBuffer);
					totalReads += bytesReadIntoBuffer;
					if (totalReads >= maxSize) {
						break;
					}
					out.flush();
				}
			}
		} catch (FileNotFoundException ex) {
			String errorMessage = "Could not create FileOutputStream. File not Found.\n" + ex.getMessage();
			LOG.log(Level.SEVERE, errorMessage);
			restErrorModel.getErrors().put("message", errorMessage);
			restErrorModel.setSuccess(false);
		} catch (IOException ex) {
			String errorMessage = "I/O error on file stream.\n" + ex.getMessage();
			LOG.log(Level.SEVERE, errorMessage);
			restErrorModel.getErrors().put("message", errorMessage);
			restErrorModel.setSuccess(false);
		}

		// Error checking and cleanup
		if (restErrorModel.getSuccess()) {
			if (!uploadedFile.delete()) {
				LOG.log(Level.SEVERE, "Could not delete file {0}", fileLocation);
			}
		} else if (totalReads >= maxSize) {
			if (!uploadedFile.delete()) {
				LOG.log(Level.SEVERE, "Could not delete file {0}", fileLocation);
			}
			restErrorModel.getErrors().put("message", "File was too large to upload.");
			restErrorModel.setSuccess(false);
		} else if (totalReads == 0) {
			if (!uploadedFile.delete()) {
				LOG.log(Level.SEVERE, "Could not delete file {0}", fileLocation);
			}
			restErrorModel.getErrors().put("message", "File was empty!");
			restErrorModel.setSuccess(false);
		} else {
			restErrorModel.setSuccess(true);
		}

		return restErrorModel;
	}
}
