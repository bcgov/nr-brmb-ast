/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.cache;

import ca.bc.gov.srm.farm.factory.BaseFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * CacheFactory.
 */
public class CacheFactory extends BaseFactory {

  private static Logger log = LoggerFactory.getLogger(CacheFactory.class);

  private static CacheFactory instance = new CacheFactory();

  private static ThreadLocal<String> userCacheKey = new ThreadLocal<>();

  private static ThreadLocal<Cache> requestCache = new ThreadLocal<>();

  private static Map<String, Cache> userCaches = new HashMap<>();

  private static Cache applicationCache = null;

  /**
   * createApplicationCache.
   *
   * @param   store  The parameter value.
   *
   * @return  The return value.
   */
  public static Cache createApplicationCache(final Object store) {

    if (applicationCache != null) {
      log.debug("clearing and re-initializing the application cache...");
      applicationCache.clearItems();
      applicationCache.initialize(store);
    } else {
      log.debug("creating a new application cache...");

      String implementingClassName = instance.findImplementingClassName(
          Cache.class, "application");
      applicationCache = (Cache) instance.createInstance(Cache.class,
          implementingClassName);
      applicationCache.initialize(store);
    }

    return applicationCache;
  }


  /**
   * createRequestCache.
   *
   * @param   store  The parameter value.
   *
   * @return  The return value.
   */
  public static Cache createRequestCache(final Object store) {
    Cache result = requestCache.get();

    if (result != null) {
      requestCache.remove();
    }

    if (log.isDebugEnabled()) {
      log.debug("creating a new request cache");
    }

    String implementingClassName = instance.findImplementingClassName(
        Cache.class, "request");
    result = (Cache) instance.createInstance(Cache.class,
        implementingClassName);
    result.initialize(store);
    requestCache.set(result);

    return result;
  }

  /**
   * createUserCache.
   *
   * @param   store  The parameter value.
   * @param   key    The parameter value.
   *
   * @return  The return value.
   */
  public static Cache createUserCache(final Object store, final String key) {

    // each http request is one thread
    // so we need to set the user cache key on each http request
    // in order to find that user's cache for the round trip of this request.
    // the cache will only be created the first time.
    setUserCacheKey(key);

    Cache result = userCaches.get(key);

    if (result == null) {

      if (log.isDebugEnabled()) {
        log.debug("creating a new user cache with key = '" + key + "'");
      }

      String implementingClassName = instance.findImplementingClassName(
          Cache.class, "user");
      result = (Cache) instance.createInstance(Cache.class,
          implementingClassName);
      result.initialize(store);
      userCaches.put(getUserCacheKey(), result);
    }

    return result;
  }

  /**
   * getApplicationCache.
   *
   * @return  The return value.
   */
  public static Cache getApplicationCache() {

    if (applicationCache == null) {

      if (log.isWarnEnabled()) {
        log.warn(
          "The application cache has not been set; using request cache implementation.");
      }

      return getRequestCache();
    }

    return applicationCache;
  }


  /**
   * getRequestCache.
   *
   * @return  The return value.
   */
  public static Cache getRequestCache() {
    Cache result = requestCache.get();

    if (result == null) {

      if (log.isWarnEnabled()) {
        log.warn(
          "The request cache has not been set; using default cache implementation.");
      }

      // do not add a store, use the default thread local store...
      result = new Cache();
      requestCache.set(result);
    }

    return result;
  }


  /**
   * getUserCache.
   *
   * @return  The return value.
   */
  public static Cache getUserCache() {
    String key = getUserCacheKey();
    Cache result = userCaches.get(key);

    if (result == null) {

      if (log.isWarnEnabled()) {
        log.warn(
          "The user cache has not been set; using request cache implementation.");
      }

      return getRequestCache();
    }

    return result;
  }


  public static void removeRequestCache() {

    if (requestCache.get() != null) {

      if (log.isDebugEnabled()) {
        log.debug("removing request cache");
      }

      requestCache.remove();
    }
  }


  /**
   * removeUnaccessed.
   *
   * @param  notAccessedTimeInMilliseconds  The parameter value.
   */
  public static void removeUnaccessed(
    final Long notAccessedTimeInMilliseconds) {
    Date now = new Date();
    removeUnaccessedCaches(now, notAccessedTimeInMilliseconds, userCaches);
  }

  /**
   * removeUserCache.
   *
   * @param  key  The parameter value.
   */
  public static void removeUserCache(final String key) {
    Cache result = userCaches.get(key);

    if (result != null) {

      if (log.isDebugEnabled()) {
        log.debug("removing user cache with key = " + key);
      }

      userCaches.remove(key);
    }
  }

  /**
   * getUserCacheKey.
   *
   * @return  The return value.
   */
  static String getUserCacheKey() {
    return userCacheKey.get();
  }

  /**
   * Sets the value for user cache key.
   *
   * @param  value  Input parameter.
   */
  static void setUserCacheKey(final String value) {
    userCacheKey.set(value);
  }

  /**
   * removeUnaccessedCaches.
   *
   * @param  now                            The parameter value.
   * @param  notAccessedTimeInMilliseconds  The parameter value.
   * @param  caches                         The parameter value.
   */
  private static void removeUnaccessedCaches(final Date now,
    final Long notAccessedTimeInMilliseconds, final Map<String, Cache> caches) {
    List<String> unaccessedCaches = new ArrayList<>();

    long nowTime = now.getTime();
    Iterator<String> iter = caches.keySet().iterator();

    while (iter.hasNext()) {
      String key = iter.next();
      Cache cache = caches.get(key);
      long lastAccessedTime = 0;

      if (cache.lastAccessed() != null) {
        lastAccessedTime = cache.lastAccessed().longValue();
      }

      long windowTime = notAccessedTimeInMilliseconds.longValue()
        + lastAccessedTime;

      if (windowTime < nowTime) {
        unaccessedCaches.add(key);
      }
    }

    // now remove any unaccessed caches...
    iter = unaccessedCaches.iterator();

    if (log.isDebugEnabled() && (unaccessedCaches.size() > 0)) {
      log.debug("removing caches unaccessed for "
        + notAccessedTimeInMilliseconds + "milliseconds..");
    }

    while (iter.hasNext()) {
      String key = iter.next();
      caches.remove(key);
    }
  }

}
