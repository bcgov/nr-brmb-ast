/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Cache.
 */
public class Cache {

  /** cache. */
  private Map<String, Object> cache;

  /** defaultStore. */
  private Map<String, Object> defaultStore;

  /** initialized. */
  private boolean initialized = false;

  /** lastAccessed. */
  private Long lastAccessed = new Long(0L);

  /** log. */
  private Logger log = LoggerFactory.getLogger(getClass());

  /** store. */
  private Object store = null;

  /** Creates a new Cache object. */
  public Cache() {
    defaultStore = createDefaultStore();
  }

  /**
   * addItem.
   *
   * @param  key   The parameter value.
   * @param  item  The parameter value.
   */
  public void addItem(final String key, final Object item) {

    if (initialized) {
      doAdd(accessStore(), key, item);
    } else {
      defaultAdd(key, item);
    }
  }

  /** clearItems. */
  public void clearItems() {

    if (initialized) {
      doClear(accessStore());
    } else {
      defaultClear();
    }
  }

  public Object getItem(final String key) {

    if (initialized) {
      return doGet(accessStore(), key);
    }
    return defaultGet(key);
  }
  
  public <T extends Object> T getItem(final String key, Class<T> type) {
    
    Object object;
    if (initialized) {
      object = doGet(accessStore(), key);
    } else {
      object = defaultGet(key);
    }
    
    return type.cast(object);
  }

  /**
   * initialize.
   *
   * @param  cacheStore  store The parameter value.
   */
  public void initialize(final Object cacheStore) {
    this.store = createStore(cacheStore);
    initialized = (this.store != null);
  }

  /**
   * isInitialized.
   *
   * @return  The return value.
   */
  public boolean isInitialized() {
    return initialized;
  }

  /**
   * keySet.
   *
   * @return  The return value.
   */
  public Set keySet() {

    if (initialized) {
      return doKeySet(accessStore());
    }
    return defaultKeySet();
  }

  /**
   * lastAccessed.
   *
   * @return  The return value.
   */
  public Long lastAccessed() {
    return this.lastAccessed;
  }

  /**
   * removeItem.
   *
   * @param   key  The parameter value.
   *
   * @return  The return value.
   */
  public Object removeItem(final String key) {

    if (initialized) {
      return doRemove(accessStore(), key);
    }
    return defaultRemove(key);
  }

  /**
   * accessStore.
   *
   * @return  The return value.
   */
  protected Object accessStore() {
    checkInitialized();
    setLastAccessed();

    return store;
  }

  /** checkInitialized. */
  protected void checkInitialized() {

    if (!isInitialized()) {
      log.error(getClass() + " not initialized.");
      throw new RuntimeException(getClass() + " not initialized.");
    }
  }

  /**
   * createStore.
   *
   * @param   resource  The parameter value.
   *
   * @return  The return value.
   */
  protected Object createStore(final Object resource) {

    if (resource == null) {
      throw new IllegalArgumentException("resource is cannot be null");

    }

    if (!Map.class.isInstance(resource)) {
      throw new IllegalArgumentException(
        "resource is expected to be a Map, not "
        + resource.getClass().getName());
    }

    return resource;
  }

  /**
   * defaultAdd.
   *
   * @param  key   The parameter value.
   * @param  item  The parameter value.
   */
  protected void defaultAdd(final String key, final Object item) {
    setLastAccessed();
    defaultStore.put(key, item);
  }

  /** defaultClear. */
  protected void defaultClear() {
    setLastAccessed();
    defaultStore.clear();
  }

  /**
   * defaultGet.
   *
   * @param   key  The parameter value.
   *
   * @return  The return value.
   */
  protected Object defaultGet(final String key) {
    setLastAccessed();

    return defaultStore.get(key);
  }

  /**
   * defaultKeySet.
   *
   * @return  The return value.
   */
  protected Set<String> defaultKeySet() {
    return defaultStore.keySet();
  }

  /**
   * defaultRemove.
   *
   * @param   key  The parameter value.
   *
   * @return  The return value.
   */
  protected Object defaultRemove(final String key) {
    setLastAccessed();

    return defaultStore.remove(key);
  }

  /**
   * doAdd.
   *
   * @param  cacheStore  store The parameter value.
   * @param  key         The parameter value.
   * @param  item        The parameter value.
   */
  protected void doAdd(final Object cacheStore, final String key,
    final Object item) {
    Map<String, Object> map = (Map<String, Object>) cacheStore;
    map.put(key, item);
  }

  /**
   * doClear.
   *
   * @param  cacheStore  store The parameter value.
   */
  protected void doClear(final Object cacheStore) {
    Map<String, Object> map = (Map<String, Object>) cacheStore;
    map.clear();
  }

  /**
   * doGet.
   *
   * @param   cacheStore  store The parameter value.
   * @param   key         The parameter value.
   *
   * @return  The return value.
   */
  protected Object doGet(final Object cacheStore, final String key) {
    Map<String, Object> map = (Map<String, Object>) cacheStore;

    return map.get(key);
  }

  /**
   * doKeySet.
   *
   * @param   cacheStore  store The parameter value.
   *
   * @return  The return value.
   */
  protected Set<String> doKeySet(final Object cacheStore) {
    Map<String, Object> map = (Map<String, Object>) cacheStore;

    return map.keySet();
  }

  /**
   * doRemove.
   *
   * @param   cacheStore  store The parameter value.
   * @param   key         The parameter value.
   *
   * @return  The return value.
   */
  protected Object doRemove(final Object cacheStore, final String key) {
    Map map = (Map<String, Object>) cacheStore;
    
    return map.remove(key);
  }

  /** Sets the value for last accessed. */
  protected void setLastAccessed() {
    Date now = new Date();
    this.lastAccessed = new Long(now.getTime());
  }

  /**
   * createDefaultStore.
   *
   * @return  The return value.
   */
  private Map<String, Object> createDefaultStore() {

    if (cache == null) {
      cache = new HashMap<>();
    }

    return cache;
  }


}
