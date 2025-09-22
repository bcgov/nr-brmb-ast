/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.jasper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author awilkinson
 */
public class Cookie {

  private final static String PATH_TAG = "$PATH";
  private final static String DELIM = ";";
  private final static String EQUALS = "=";

  private static Logger logger = LoggerFactory.getLogger(Cookie.class);

  public int version;
  public String name;
  public String value;
  public String path;

  /**
   * Constructs a Cookie with default version (0) and path (/).
   * 
   * @param name
   *          the cookie name.
   * @param value
   *          the value of the cookie
   */
  public Cookie(String name, String value) {
    this(0, name, value, "/");
  }

  /**
   * Constructor
   * 
   * @param version
   *          the cookie version, 0 or 1.
   * @param name
   *          the cookie name.
   * @param value
   *          the value of the cookie
   * @param path
   *          the cookie's path
   */
  public Cookie(int version, String name, String value, String path) {
    this.version = version;
    this.name = name;
    this.value = value;
    this.path = path;
  }

  /**
   * @return this cookie's version as int
   */
  public int getVersion() {
    return this.version;
  }

  /**
   * @param v
   *          the int version of the cookie
   */
  public void setVersion(int v) {
    this.version = v;
  }

  /**
   * @param name
   *          the String name of the cookie
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the String name of the cookie
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param value
   *          the String value of the cookie
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return the String value of the cookie
   */
  public String getValue() {
    return this.value;
  }

  /**
   * @param path
   *          the String path of the cookie
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * @return the String path of the cookie
   */
  public String getPath() {
    return this.path;
  }

  /**
   * URL encodes this cookie.
   * 
   * @return the URL encoded String representation of this cookie
   */
  public String getUrlEncoded() throws UnsupportedEncodingException {
    StringBuffer buf = new StringBuffer();
    buf.append(version);
    buf.append(DELIM);
    buf.append(name);
    buf.append(EQUALS);
    buf.append(URLEncoder.encode(value, "UTF-8"));
    buf.append(DELIM);
    buf.append(PATH_TAG);
    buf.append(EQUALS);
    buf.append(path);
    return buf.toString();
  }

  /**
   * Return the string representation of this cookie.
   * 
   * @return the string representation of this cookie.
   */
  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append(version);
    buf.append(DELIM);
    buf.append(name);
    buf.append(EQUALS);
    buf.append(value);
    buf.append(DELIM);
    buf.append(PATH_TAG);
    buf.append(EQUALS);
    buf.append(path);
    return buf.toString();
  }

  /**
   * Parses the given header field for a cookie.
   * 
   * @param headerfield
   *          the String headerfield is the header line without the header name
   *          as returned by URLConnection.getHeaderField(n)
   * @return the Cookie found in the headerfield.
   * @throws CookieFormatException
   *           if the headerfield does not conform to cookie specs
   */
  public static Cookie parseCookie(String headerfield) throws CookieFormatException {
    logger.debug("parseCookie: " + headerfield);
    int v = 0;

    // not currently validating, so have an exception that will
    // never be thrown to enforce forward compatibility
    if (v != 0 && v != 1) {
      throw new CookieFormatException("Invalid cookie version: " + v);
    }

    String name = parseName(headerfield);
    String val = parseValue(headerfield);
    String path = parsePath(headerfield);
    Cookie c = new Cookie(v, name, val, path);
    return c;
  }

  protected static String parsePath(String headerfield) {
    int start = headerfield.lastIndexOf(EQUALS) + 1;
    return headerfield.substring(start);
  }

  protected static String parseValue(String headerfield) {
    int start = headerfield.indexOf(EQUALS) + 1;
    int end = headerfield.indexOf(DELIM, start);
    return headerfield.substring(start, end);
  }

  protected static String parseName(String headerfield) {
    int start = 0;
    int end = headerfield.indexOf(EQUALS);
    return headerfield.substring(start, end);
  }
}
