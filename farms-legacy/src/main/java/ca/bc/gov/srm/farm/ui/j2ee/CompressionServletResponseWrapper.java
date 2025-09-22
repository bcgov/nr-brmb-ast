/*
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.bc.gov.srm.farm.ui.j2ee;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of <b>HttpServletResponseWrapper</b> that works with the
 * CompressionServletResponseStream implementation..
 * 
 * @author Amy Roh
 * @author Dmitri Valdin
 */

public class CompressionServletResponseWrapper extends HttpServletResponseWrapper {

  private Logger logger = LoggerFactory.getLogger(getClass());

  // ----------------------------------------------------- Constructor

  /**
   * Calls the parent constructor which creates a ServletResponse adaptor
   * wrapping the given response object.
   * 
   * @param response HttpServletResponse
   */
  public CompressionServletResponseWrapper(HttpServletResponse response) {
    super(response);
    origResponse = response;
    if (debug > 1) {
      logger.debug("CompressionServletResponseWrapper constructor gets called");
    }
  }

  // ----------------------------------------------------- Instance Variables

  /**
   * Original response
   */
  private HttpServletResponse origResponse = null;

  /**
   * Descriptive information about this Response implementation.
   */
  protected static final String INFO = "CompressionServletResponseWrapper";

  /**
   * The ServletOutputStream that has been returned by
   * <code>getOutputStream()</code>, if any.
   */
  private ServletOutputStream stream = null;

  /**
   * The PrintWriter that has been returned by <code>getWriter()</code>, if
   * any.
   */
  private PrintWriter writer = null;

  /**
   * The threshold number to compress
   */
  private int threshold = 0;

  /**
   * Debug level
   */
  private int debug = 0;

  /**
   * Content type
   */
  private String contentType = null;

  // --------------------------------------------------------- Public Methods

  /**
   * Set content type
   * @param contentType String
   */
  @Override
  public void setContentType(String contentType) {
    if (debug > 1) {
      logger.debug("setContentType to " + contentType);
    }
    this.contentType = contentType;
    origResponse.setContentType(contentType);
  }

  /**
   * Set threshold number
   * @param newValue int
   */
  public void setCompressionThreshold(int newValue) {
    if (debug > 1) {
      logger.debug("setCompressionThreshold to " + newValue);
    }
    this.threshold = newValue;
  }

  /**
   * Set debug level
   * @param newValue int
   */
  public void setDebugLevel(int newValue) {
    this.debug = newValue;
  }

  /**
   * Create and return a ServletOutputStream to write the content associated
   * with this Response.
   * 
   * @return ServletOutputStream
   * @exception IOException
   *              if an input/output error occurs
   */
  public ServletOutputStream createOutputStream() throws IOException {
    if (debug > 1) {
      logger.debug("createOutputStream gets called");
    }

    CompressionResponseStream crStream = new CompressionResponseStream(origResponse);
    crStream.setDebugLevel(debug);
    crStream.setBuffer(threshold);

    return crStream;

  }

  /**
   * Finish a response.
   */
  public void finishResponse() {
    try {
      if (writer != null) {
        writer.close();
      } else {
        if (stream != null) {
          stream.close();
        }
      }
    } catch (IOException e) {
      if (debug > 1) {
        logger.debug("finishResponse - IOException");
      }
    }
  }

  // ------------------------------------------------ ServletResponse Methods

  /**
   * Flush the buffer and commit this response.
   * 
   * @exception IOException
   *              if an input/output error occurs
   */
  @Override
  public void flushBuffer() throws IOException {
    if (debug > 1) {
      logger.debug("flush buffer @ CompressionServletResponseWrapper");
    }
    ((CompressionResponseStream) stream).flush();

  }

  /**
   * Return the servlet output stream associated with this Response.
   * 
   * @return ServletOutputStream
   * exception IllegalStateException
   *              if <code>getWriter</code> has already been called for this
   *              response
   * @exception IOException
   *              if an input/output error occurs
   */
  @Override
  public ServletOutputStream getOutputStream() throws IOException {

    if (writer != null) {
      throw new IllegalStateException("getWriter() has already been called for this response");
    }

    if (stream == null) {
      stream = createOutputStream();
    }
    if (debug > 1) {
      logger.debug("stream is set to " + stream + " in getOutputStream");
    }

    return (stream);

  }

  /**
   * Return the writer associated with this Response.
   * 
   * @return PrintWriter
   * exception IllegalStateException
   *              if <code>getOutputStream</code> has already been called for
   *              this response
   * @exception IOException
   *              if an input/output error occurs
   */
  @Override
  public PrintWriter getWriter() throws IOException {

    if (writer != null) {
      return (writer);
    }

    if (stream != null) {
      throw new IllegalStateException("getOutputStream() has already been called for this response");
    }

    stream = createOutputStream();
    if (debug > 1) {
      logger.debug("stream is set to " + stream + " in getWriter");
    }
    // String charset = getCharsetFromContentType(contentType);
    String charEnc = origResponse.getCharacterEncoding();
    if (debug > 1) {
      logger.debug("character encoding is " + charEnc);
    }
    // HttpServletResponse.getCharacterEncoding() shouldn't return null
    // according the spec, so feel free to remove that "if"
    if (charEnc != null) {
      writer = new PrintWriter(new OutputStreamWriter(stream, charEnc));
    } else {
      writer = new PrintWriter(stream);
    }

    return (writer);

  }

  /**
   * @param length int
   */
  @Override
  public void setContentLength(int length) {
    // no need to set the content length
  }

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

}
