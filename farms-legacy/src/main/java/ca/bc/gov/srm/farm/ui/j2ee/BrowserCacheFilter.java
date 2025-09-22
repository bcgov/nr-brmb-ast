/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.j2ee;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * BrowserCacheFilter.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
public class BrowserCacheFilter implements Filter {

  /** logger. */
  private static Logger logger = LoggerFactory.getLogger(BrowserCacheFilter.class);

  /** filterConfig. */
  private FilterConfig filterConfig;

  /** destroy. */
  @Override
  public void destroy() {
    this.filterConfig = null;
  }

  /**
   * doFilter.
   *
   * @param   servletRequest   The parameter value.
   * @param   servletResponse  The parameter value.
   * @param   filterChain      The parameter value.
   *
   * @throws  IOException       On exception.
   * @throws  ServletException  On exception.
   */
  @Override
  public void doFilter(final ServletRequest servletRequest,
    final ServletResponse servletResponse, final FilterChain filterChain)
    throws IOException, ServletException {

    if (logger.isDebugEnabled()) {
      logger.debug("Setting cache headers for file "
        + ((HttpServletRequest) servletRequest).getRequestURI());
    }

    String privacy = filterConfig.getInitParameter("privacy");
    String expirationTime = filterConfig.getInitParameter("expirationTime");

    /* set the provided HTTP response parameters */
    if (StringUtils.isNotBlank(privacy)
        && StringUtils.isNotBlank(expirationTime)) {
      setCacheExpireDate((HttpServletResponse) servletResponse, privacy,
        Integer.valueOf(expirationTime).intValue());
    } /* pass the request/response on */

    filterChain.doFilter(servletRequest, servletResponse);
  }

  /**
   * init.
   *
   * @param   filterConf  The parameter value.
   *
   * @throws  ServletException  On exception.
   */
  @Override
  public void init(final FilterConfig filterConf) throws ServletException {
    this.filterConfig = filterConf;
  }

  /**
   * htmlExpiresDateFormat.
   *
   * @return  The return value.
   */
  private DateFormat htmlExpiresDateFormat() {
    DateFormat httpDateFormat = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    return httpDateFormat;
  }

  /**
   * Sets the value for cache expire date.
   *
   * @param  response  Input parameter.
   * @param  privacy   Input parameter.
   * @param  seconds   Input parameter.
   */
  private void setCacheExpireDate(final HttpServletResponse response,
    final String privacy, final int seconds) {

    if (response != null) {
      Calendar cal = new GregorianCalendar();
      cal.add(Calendar.SECOND, seconds);
      response.setHeader("Cache-Control",
        privacy + ", max-age=" + seconds + ", must-revalidate");
      response.setHeader("Expires",
        htmlExpiresDateFormat().format(cal.getTime()));
    }
  }
}
