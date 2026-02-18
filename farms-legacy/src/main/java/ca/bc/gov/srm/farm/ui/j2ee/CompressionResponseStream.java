/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License"). You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * glassfish/bootstrap/legal/CDDLv1.0.txt or
 * https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * glassfish/bootstrap/legal/CDDLv1.0.txt. If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 *
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Portions Copyright Apache Software Foundation.
 */
package ca.bc.gov.srm.farm.ui.j2ee;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of <b>ServletOutputStream</b> that works with the
 * CompressionServletResponseWrapper implementation.
 * 
 * @author Amy Roh
 * @author Dmitri Valdin
 */

public class CompressionResponseStream extends ServletOutputStream {

  private Logger logger = LoggerFactory.getLogger(getClass());

  // ----------------------------------------------------------- Constructors

  /**
   * Construct a servlet output stream associated with the specified Response.
   * 
   * @param response The associated response
   * @throws IOException On IOException
   */
  public CompressionResponseStream(HttpServletResponse response) throws IOException {

    super();
    closed = false;
    this.response = response;
    this.output = response.getOutputStream();

  }

  // ----------------------------------------------------- Instance Variables

  /**
   * The threshold number which decides to compress or not. Users can configure
   * in web.xml to set it to fit their needs.
   */
  private int compressionThreshold = 0;

  /**
   * Debug level
   */
  private int debug = 0;

  /**
   * The buffer through which all of our output bytes are passed.
   */
  private byte[] buffer = null;

  /**
   * The number of data bytes currently in the buffer.
   */
  private int bufferCount = 0;

  /**
   * The underlying gzip output stream to which we should write data.
   */
  private GZIPOutputStream gzipstream = null;

  /**
   * Has this stream been closed?
   */
  private boolean closed = false;

  /**
   * The content length past which we will not write, or -1 if there is no
   * defined content length.
   */
//  protected int length = -1;

  /**
   * The response with which this servlet output stream is associated.
   */
  private HttpServletResponse response = null;

  /**
   * The underlying servket output stream to which we should write data.
   */
  private ServletOutputStream output = null;

  // --------------------------------------------------------- Public Methods

  /**
   * Set debug level
   * @param newVal debug
   */
  public void setDebugLevel(int newVal) {
    this.debug = newVal;
  }

  /**
   * Set the compressionThreshold number and create buffer for this size
   * @param threshold threshold
   */
  protected void setBuffer(int threshold) {
    compressionThreshold = threshold;
    buffer = new byte[compressionThreshold];
    if (debug > 1) {
      logger.debug("buffer is set to " + compressionThreshold);
    }
  }

  /**
   * Close this output stream, causing any buffered data to be flushed and any
   * further output data to throw an IOException.
   * @throws IOException On IOException
   */
  @Override
  public void close() throws IOException {

    if (debug > 1) {
      logger.debug("close() @ CompressionResponseStream");
    }
    if (closed) {
      throw new IOException("This output stream has already been closed");
    }

    if (gzipstream != null) {
      flushToGZip();
      gzipstream.close();
      gzipstream = null;
    } else {
      if (bufferCount > 0) {
        if (debug > 2) {
          String bytesString = new String(buffer);
          logger.debug("output.write(");
          logger.debug(bytesString);
          logger.debug(")");
        }
        output.write(buffer, 0, bufferCount);
        bufferCount = 0;
      }
    }

    output.close();
    closed = true;

  }

  /**
   * Flush any buffered data for this output stream, which also causes the
   * response to be committed.
   * @throws IOException On IOException
   */
  @Override
  public void flush() throws IOException {

    if (debug > 1) {
      logger.debug("flush() @ CompressionResponseStream");
    }
    if (closed) {
      throw new IOException("Cannot flush a closed output stream");
    }

    if (gzipstream != null) {
      gzipstream.flush();
    }

  }

  /**
   * @throws IOException On IOException
   */
  public void flushToGZip() throws IOException {

    if (debug > 1) {
      logger.debug("flushToGZip() @ CompressionResponseStream");
    }
    if (bufferCount > 0) {
      if (debug > 1) {
        logger.debug("flushing out to GZipStream, bufferCount = " + bufferCount);
      }
      writeToGZip(buffer, 0, bufferCount);
      bufferCount = 0;
    }

  }

  /**
   * Write the specified byte to our output stream.
   * 
   * @param b
   *          The byte to be written
   * 
   * @exception IOException if an input/output error occurs
   */
  @Override
  public void write(int b) throws IOException {

    if (debug > 1) {
      logger.debug("write " + b + " in CompressionResponseStream ");
    }
    if (closed) {
      throw new IOException("Cannot write to a closed output stream");
    }

    if (bufferCount >= buffer.length) {
      flushToGZip();
    }

    buffer[bufferCount++] = (byte) b;

  }

  /**
   * Write <code>b.length</code> bytes from the specified byte array to our
   * output stream.
   * 
   * @param b
   *          The byte array to be written
   * 
   * @exception IOException
   *              if an input/output error occurs
   */
  @Override
  public void write(byte[] b) throws IOException {

    write(b, 0, b.length);

  }

  /**
   * Write <code>len</code> bytes from the specified byte array, starting at
   * the specified offset, to our output stream.
   * 
   * @param b
   *          The byte array containing the bytes to be written
   * @param off
   *          Zero-relative starting offset of the bytes to be written
   * @param len
   *          The number of bytes to be written
   * 
   * @exception IOException
   *              if an input/output error occurs
   */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {

    if (debug > 1) {
      logger.debug("write, bufferCount = " + bufferCount + " len = " + len + " off = " + off);
    }
    if (debug > 2) {
      String bytesString = new String(b);
      logger.debug("write(");
      logger.debug(bytesString);
      logger.debug(")");
    }

    if (closed) {
      throw new IOException("Cannot write to a closed output stream");
    }

    if (len == 0) {
      return;
    }

    // Can we write into buffer ?
    if (len <= (buffer.length - bufferCount)) {
      System.arraycopy(b, off, buffer, bufferCount, len);
      bufferCount += len;
      return;
    }

    // There is not enough space in buffer. Flush it ...
    flushToGZip();

    // ... and try again. Note, that bufferCount = 0 here !
    if (len <= (buffer.length - bufferCount)) {
      System.arraycopy(b, off, buffer, bufferCount, len);
      bufferCount += len;
      return;
    }

    // write direct to gzip
    writeToGZip(b, off, len);
  }

  /**
   * @param b byte[]
   * @param off offset
   * @param len length
   * @throws IOException On IOException
   */
  public void writeToGZip(byte[] b, int off, int len) throws IOException {

    if (debug > 1) {
      logger.debug("writeToGZip, len = " + len);
    }
    if (debug > 2) {
      String bytesString = new String(b);
      logger.debug("writeToGZip(");
      logger.debug(bytesString);
      logger.debug(")");
    }
    if (gzipstream == null) {
      if (debug > 1) {
        logger.debug("new GZIPOutputStream");
      }
      response.addHeader("Content-Encoding", "gzip");
      gzipstream = new GZIPOutputStream(output);
    }
    gzipstream.write(b, off, len);

  }

  // -------------------------------------------------------- Package Methods

  /**
   * Has this response stream been closed?
   * @return closed
   */
  public boolean closed() {

    return (this.closed);

  }

  @Override
  public boolean isReady() {
    return true; // always ready (blocking IO)
  }

  @Override
  public void setWriteListener(WriteListener arg0) {
    // no-op: async IO not supported
  }

}
