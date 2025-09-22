package ca.bc.gov.srm.farm.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;


/**
 * @author   Vivid Solutions Inc.
 * @version  %I%, %G%
 */

public class BlobReaderWriter {

  private static final int BLOB_BUFFER_SIZE = 32240;
  
  /** Constructor. */
  public BlobReaderWriter() {
  }


  /**
   * read data from Database blob to an output stream.
   *
   * @param   inputBlob     blob that is thesource of data being read
   * @param   outputStream  output stream data is written to.
   *
   * @throws  IOException   Exception
   * @throws  SQLException  Exception
   */
  public void readBlob(Blob inputBlob, OutputStream outputStream)
    throws SQLException, IOException {

    @SuppressWarnings("resource")
    InputStream inputStream = inputBlob.getBinaryStream();
    int bufferSize = BLOB_BUFFER_SIZE;

    writeOutput(inputStream, outputStream, bufferSize);
  }

  /**
   * Write data from input stream to a Database blob.
   *
   * @param   outputBlob   source of data being written to blob
   * @param   inputStream  Source data to write to output stream
   *
   * @throws  IOException   Exception
   * @throws  SQLException  Exception
   */
  public void writeBlob(Blob outputBlob, InputStream inputStream)
    throws SQLException, IOException {

    @SuppressWarnings("resource")
    OutputStream outputStream = outputBlob.setBinaryStream(0L);
    int bufferSize = BLOB_BUFFER_SIZE;
    writeOutput(inputStream, outputStream, bufferSize);
  }


  /**
   * Write whatever is in the input stream to the output stream.
   *
   * @param   inputStream   Source data to write to output stream
   * @param   outputStream  Output stream to write to
   * @param   bufferSize    Size of file buffer
   *
   * @throws  IOException  Exception
   */
  private void writeOutput(InputStream inputStream, OutputStream outputStream,
    int bufferSize) throws IOException {

    try {
      byte[] buffer = new byte[bufferSize];
      int length = 0;

      while ((length = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, length);
      }
    } finally {
      outputStream.flush();
    }
  }
}
