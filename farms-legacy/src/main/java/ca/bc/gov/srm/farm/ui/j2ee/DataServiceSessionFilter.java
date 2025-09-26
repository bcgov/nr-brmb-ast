/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.j2ee;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;


/**
 * DataServiceSessionFilter.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2142 $
 */
public class DataServiceSessionFilter implements Filter {

  /** destroy. */
  @Override
  public void destroy() {
    // do nothing
  }

  /**
   * doFilter.
   *
   * @param   request   The parameter value.
   * @param   response  The parameter value.
   * @param   chain     The parameter value.
   *
   * @throws  IOException       On exception.
   * @throws  ServletException  On exception.
   */
  @Override
  public void doFilter(final ServletRequest request,
    final ServletResponse response, final FilterChain chain) throws IOException,
    ServletException {

    if ((request instanceof HttpServletRequest)
        && (response instanceof HttpServletResponse)) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;

      //clear out any old session level caches...
      //these should be purged when the session is destroyed, but let's make
      //sure...
      //get the number of seconds for a session life
      final int thousand = 1000;
      int timeout = httpRequest.getSession().getMaxInactiveInterval();
      CacheFactory.removeUnaccessed(new Long(thousand * timeout));

      //add the session level cache (store data in http session).
      CacheFactory.createUserCache(httpRequest,
        CacheFactoryKeys.createUserKey(httpRequest.getSession()));

      //add a request cache that we will purge when filter returns.
      CacheFactory.createRequestCache(httpRequest);

      if (CurrentUser.getUser() == null) {
        CurrentUser.setUser(httpRequest);
      }

      //let the request flow...
      chain.doFilter(request, response);

      //now remove the request cache...
      CacheFactory.removeRequestCache();
    } else {
      chain.doFilter(request, response);
    }
  }

  /**
   * init.
   *
   * @param   filterConfig  The parameter value.
   *
   * @throws  ServletException  On exception.
   */
  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    // do nothing
  }

}
