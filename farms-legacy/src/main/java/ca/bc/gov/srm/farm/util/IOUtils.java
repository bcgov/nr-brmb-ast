package ca.bc.gov.srm.farm.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * IOUtils.
 */
public final class IOUtils {
  
  private static Logger logger = LoggerFactory.getLogger(IOUtils.class);
  
  public static final String CONTENT_TYPE_CSV = "text/csv";
  public static final String CONTENT_TYPE_JSON = "application/json";
  public static final String CONTENT_TYPE_PDF = "application/pdf";
  public static final String CONTENT_TYPE_ZIP = "application/zip";

  /** constructor. */
  private IOUtils() {
  }
  
  
  public static void copy(
      final InputStream in, 
      final OutputStream out,
      final int bufferSize) throws IOException {
    byte[] buffer = new byte[bufferSize];
    int bytesRead = 0;

    while ((bytesRead = in.read(buffer)) != -1) {
      out.write(buffer, 0, bytesRead);
    }
    
  }
  /**
   * @param fileName fileName
   * @param fileNameSuffix fileNameSuffix
   * @param headerLine headerLine
   * @param lines List<String>
   * @return File
   * @throws IOException On IOException
   */
  public static File writeTempFile(String fileName, String fileNameSuffix,
      String headerLine, List<String> lines) throws IOException {
    File outFile;
    File tempDir = IOUtils.getTempDir(); 
    outFile = File.createTempFile(fileName, fileNameSuffix, tempDir);
    
    try(FileWriter fileWriter = new FileWriter(outFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {
      
      // write column headings
      if(headerLine != null) {
        bufferedWriter.write(headerLine);
        bufferedWriter.newLine();
      }
      
      for(String line : lines) {
        bufferedWriter.write(line);
        bufferedWriter.newLine();
      }
      
      bufferedWriter.flush();
    }
    return outFile;
  }
  
  public static void writeFileToResponse(
      final HttpServletResponse response, 
      final File outFile,
      String contentType) throws Exception {
    writeFileToResponse(response, outFile.toPath(), contentType);
  }
    
    
  public static void writeFileToResponse(
      final HttpServletResponse response, 
      final Path outFile,
      String contentType) throws Exception {
    writeFileToResponse(response, outFile, contentType, null);
  }
    
  public static void writeFileToResponse(
      final HttpServletResponse response, 
      final Path outFile,
      String contentType,
      String fileNameParam) throws Exception {
    
    String fileName;
    if(fileNameParam == null) {
      fileName = outFile.getFileName().toString();
    } else {
      fileName = fileNameParam;
    }
    fileName = sanitizeFileName(fileName);
    
    String contentTypeToUse = contentType != null ? contentType : URLConnection.guessContentTypeFromName(fileName);
    long fileSize = Files.size(outFile);
    
    logger.debug("Content-Length: " + fileSize);
    response.setHeader("Content-Length", String.valueOf(fileSize));
    response.setContentType(contentTypeToUse);
    
    response.setHeader("Expires", "0");
    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    response.setHeader("Pragma", "public");
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    response.setHeader("Transfer-Encoding", null);
    
    try (OutputStream out = response.getOutputStream()) {
      Files.copy(outFile, out);
      out.flush();
    }
  }

  public static void writeJsonToResponse(HttpServletResponse response, String json) throws IOException {
    writeStringToResponse(response, CONTENT_TYPE_JSON, json, null);
  }
  
  public static void writeStringToResponse(HttpServletResponse response,
      String contentType,
      String content,
      String fileName) throws IOException {
    
    response.reset();
    
    if(fileName == null) {
      response.addHeader("content-disposition", "inline");
    } else {
      String sanitizedFileName = sanitizeFileName(fileName);
      
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.setHeader("Content-disposition", "attachment; filename=" + sanitizedFileName);
      response.setHeader("Transfer-Encoding", null);
    }
    response.setContentType(contentType);
    response.setContentLength(content.length());

    try(InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)); ) {
      @SuppressWarnings("resource")
      OutputStream outputStream = response.getOutputStream();

      byte[] buffer = new byte[content.length()];
      int length = 0;

      while ((length = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, length);
      }
      outputStream.flush();
    }
  }

  public static String sanitizeFileName(String fileName) {
    return fileName
        .replace(',', '_')
        .replace('/', '_');
  }

  public static File getTempDir() {
    return new File(System.getProperty("java.io.tmpdir"));
  }

  public static Path getTempDirPath() {
    return Paths.get(System.getProperty("java.io.tmpdir"));
  }
  
  public static String loadResourceFileAsString(String fileName) throws IOException, URISyntaxException {
    return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fileName).toURI())));
  }

  public static void addFileToZip(Path targetZip, Path fileToAdd, String zipEntryFileName) throws IOException, FileNotFoundException {
    Map<String, String> env = new HashMap<>();
    env.put("create", String.valueOf(Files.notExists(targetZip)));
    
    URI uri = URI.create("jar:" + targetZip.toUri());
    try (FileSystem zipFileSystem = FileSystems.newFileSystem(uri, env)) {
      
        Path zipEntryFile = zipFileSystem.getPath(zipEntryFileName);
        
        Files.copy(fileToAdd, zipEntryFile, StandardCopyOption.REPLACE_EXISTING);
    }
  }
  
  public static String encodeFileToBase64(File file) throws IOException {
		try {
			byte[] fileContent = Files.readAllBytes(file.toPath());
			return Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			throw new IOException("could not read file " + file, e);
		}
	}
}
