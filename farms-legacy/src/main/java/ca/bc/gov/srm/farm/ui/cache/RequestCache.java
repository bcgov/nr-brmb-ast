/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.cache;

import ca.bc.gov.srm.farm.cache.Cache;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


/**
 * RequestCache.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class RequestCache extends Cache {

  /**
   * createStore.
   *
   * @param   resource  The parameter value.
   *
   * @return  The return value.
   */
  @Override
  protected Object createStore(final Object resource) {

    if (resource == null) {
      throw new IllegalArgumentException("resource cannot be null");
    }

    if (!(resource instanceof HttpServletRequest)) {
      throw new IllegalArgumentException("resource required to be of type "
        + HttpServletRequest.class.getName() + ", not "
        + resource.getClass().getName());
    }

    HttpServletRequest r = (HttpServletRequest) resource;

    return r;
  }

  /**
   * doAdd.
   *
   * @param  store  The parameter value.
   * @param  key    The parameter value.
   * @param  item   The parameter value.
   */
  @Override
  protected void doAdd(final Object store, final String key,
    final Object item) {
    HttpServletRequest cache = (HttpServletRequest) store;
    cache.setAttribute(key, item);
  }

  /**
   * doClear.
   *
   * @param  store  The parameter value.
   */
  @Override
  protected void doClear(final Object store) {
    HttpServletRequest cache = (HttpServletRequest) store;
    Enumeration enumeration = cache.getAttributeNames();

    while (enumeration.hasMoreElements()) {
      String name = (String) enumeration.nextElement();
      cache.removeAttribute(name);
    }
  }

  /**
   * doGet.
   *
   * @param   store  The parameter value.
   * @param   key    The parameter value.
   *
   * @return  The return value.
   */
  @Override
  protected Object doGet(final Object store, final String key) {
    HttpServletRequest cache = (HttpServletRequest) store;

    return cache.getAttribute(key);
  }

  /**
   * doKeySet.
   *
   * @param   store  The parameter value.
   *
   * @return  The return value.
   */
  @Override
  protected Set doKeySet(final Object store) {
    HttpServletRequest cache = (HttpServletRequest) store;
    Set result = new HashSet();
    Enumeration enumeration = cache.getAttributeNames();

    while (enumeration.hasMoreElements()) {
      String name = (String) enumeration.nextElement();
      result.add(name);
    }

    return result;
  }

  /**
   * doRemove.
   *
   * @param   store  The parameter value.
   * @param   key    The parameter value.
   *
   * @return  The return value.
   */
  @Override
  protected Object doRemove(final Object store, final String key) {
    HttpServletRequest cache = (HttpServletRequest) store;
    Object cached = cache.getAttribute(key);
    cache.removeAttribute(key);

    return cached;
  }

}
