package ca.bc.gov.webade.developer.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Input / Ouput Utilities.
 * 
 * @author Vivid Solutions Inc
 */
public final class IOUtils {

	private static final int BYTE_BUFFER_SIZE = 1024;
	
	private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

	public static String streamToString(InputStream stream) throws IOException {
		String result = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			bis = new BufferedInputStream(stream);
			bos = new BufferedOutputStream(byteArrayOutputStream);
			copyStream(bis, bos);
			result = byteArrayOutputStream.toString("UTF-8");
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return result;
	}

	public static void streamToFile(InputStream stream, File file) throws IOException {
		BufferedInputStream bis = null;

		try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
			bis = new BufferedInputStream(stream);

			copyStream(bis, bos);
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

	/**
	 * Forward the bytes from the InputStream to the OutputStream. This method
	 * does not close the streams. That is left to the calling method.
	 * 
	 * @param in
	 *            The input stream
	 * @param out
	 *            The output stream
	 * @throws IOException
	 *             Any IOException
	 */
	private static void copyStream(InputStream in, OutputStream out) throws IOException {
		synchronized (in) {
			synchronized (out) {
				byte[] buffer = new byte[BYTE_BUFFER_SIZE];
				while (true) {
					int bytesRead = in.read(buffer);
					if (bytesRead == -1) {
						break;
					}
					out.write(buffer, 0, bytesRead);
				}
				out.flush();
			}
		}
	}

	/**
	 * Create a temp file reading the contents from a resource in the classpath.
	 * 
	 * @param fileName
	 *            The file name of the resource
	 * @param fileSuffix
	 *            The file suffix of the resource
	 * @return The path to the created temp file or null if the file could not
	 *         be created.
	 */
	public static String createTempFileFromClasspath(String fileName, String fileSuffix) {
		String fileLocation = null;
		// read file from class path and write to temp file
		InputStream stream = IOUtils.class.getResourceAsStream("/" + fileName + fileSuffix);
		if (stream == null) {
			logger.error("File " + fileName + fileSuffix + " could not be found in the classpath");
		} else {
			try {
				File tempFile = File.createTempFile(fileName, fileSuffix);
				streamToFile(stream, tempFile);
				tempFile.deleteOnExit();
				if (tempFile.exists() && tempFile.canRead()) {
					fileLocation = tempFile.getAbsolutePath();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return fileLocation;
	}
}
