/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.j2ee;

import ca.bc.gov.srm.farm.cache.CacheFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * DataServiceSessionListener.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
public class DataServiceSessionListener implements HttpSessionListener {

  /** log. */
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * sessionCreated.
   *
   * @param  event  The parameter value.
   */
  @Override
  public void sessionCreated(final HttpSessionEvent event) {
    // do nothing
  }

  /**
   * sessionDestroyed.
   *
   * @param  event  The parameter value.
   */
  @Override
  public void sessionDestroyed(final HttpSessionEvent event) {
    log.debug("> sessionDestroyed()");

    //since a session is being destroyed, let's purge the cache...
    log.debug("remove user cache...");

    CacheFactory.removeUserCache(CacheFactoryKeys.createUserKey(
        event.getSession()));

    log.debug("< sessionDestroyed()");
  }

}
