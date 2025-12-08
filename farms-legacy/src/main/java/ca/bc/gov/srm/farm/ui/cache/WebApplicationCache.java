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

import javax.servlet.ServletContext;


/**
 * WebApplicationCache.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class WebApplicationCache extends Cache {

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

    if (!(resource instanceof ServletContext)) {
      throw new IllegalArgumentException("resource required to be of type "
        + ServletContext.class.getName() + ", not "
        + resource.getClass().getName());
    }

    ServletContext sc = (ServletContext) resource;

    return sc;
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
    ServletContext cache = (ServletContext) store;
    cache.setAttribute(key, item);
  }

  /**
   * doClear.
   *
   * @param  store  The parameter value.
   */
  @Override
  protected void doClear(final Object store) {
    ServletContext cache = (ServletContext) store;
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
    ServletContext cache = (ServletContext) store;

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
    ServletContext cache = (ServletContext) store;
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
    ServletContext cache = (ServletContext) store;
    Object cached = cache.getAttribute(key);
    cache.removeAttribute(key);

    return cached;
  }
}
