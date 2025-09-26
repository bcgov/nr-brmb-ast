/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.configuration;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ConfigurationNotFoundException;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.exception.UtilityException;
import ca.bc.gov.srm.farm.factory.ObjectFactory;


/**
 * ConfigurationUtility.
 */
public abstract class ConfigurationUtility {


  private static ConfigurationUtility instance = null;

  private boolean initialized = false;

  private Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, ConfigurationProvider> providers = new LinkedHashMap<>();


  /**
   * initialize.
   *
   * @param  resource  resource
   */
  public abstract void initialize(Object resource);


  /**
   * getInstance.
   *
   * @return  The return value
   */
  public static ConfigurationUtility getInstance() {

    if (instance == null) {
      instance = (ConfigurationUtility) ObjectFactory.createObject(
          ConfigurationUtility.class, null);
    }

    return instance;
  }


  /**
   * getBoolean.
   *
   * @param   key  get key
   *
   * @return  The return value
   */
  public boolean getBoolean(final String key) {
    checkInitialized();

    boolean result = false;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getBoolean(key);
          found = true;
        } catch (UnsupportedOperationException e) {
          found = false;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration not found for key='" + key + "'";
      logger.error(msg);

    }

    return result;
  }


  /**
   * getBoolean.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   */
  public boolean getBoolean(final String key, final User user) {
    checkInitialized();

    boolean result = false;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getBoolean(key, user);
          found = true;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration (for user) not found for key='" + key + "'";
      logger.warn(msg);
    }

    return result;
  }


  /**
   * getDate.
   *
   * @param   key  get key
   *
   * @return  The return value
   */
  public Date getDate(final String key) {
    checkInitialized();

    Date result = null;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getDate(key);
          found = true;
        } catch (UnsupportedOperationException e) {
          found = false;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration not found for key='" + key + "'";
      logger.error(msg);
    }

    return result;
  }


  /**
   * getDate.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   */
  public Date getDate(final String key, final User user) {
    checkInitialized();

    Date result = null;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getDate(key, user);
          found = true;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration (for user) not found for key='" + key + "'";
      logger.warn(msg);
    }

    return result;
  }


  /**
   * getDouble.
   *
   * @param   key  get key
   *
   * @return  The return value
   */
  public double getDouble(final String key) {
    checkInitialized();

    double result = 0;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getDouble(key);
          found = true;
        } catch (UnsupportedOperationException e) {
          found = false;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration not found for key='" + key + "'";
      logger.error(msg);
    }

    return result;
  }


  /**
   * getInt.
   *
   * @param   key  get key
   *
   * @return  The return value
   */
  public int getInt(final String key) {
    checkInitialized();

    int result = 0;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getInt(key);
          found = true;
        } catch (UnsupportedOperationException e) {
          found = false;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration not found for key='" + key + "'";
      logger.error(msg);

    }

    return result;
  }


  /**
   * getInt.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   */
  public int getInt(final String key, final User user) {
    checkInitialized();

    int result = 0;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getInt(key, user);
          found = true;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration (for user) not found for key='" + key + "'";
      logger.warn(msg);
    }

    return result;
  }


  /**
   * getLong.
   *
   * @param   key  get key
   *
   * @return  The return value
   */
  public long getLong(final String key) {
    checkInitialized();

    long result = 0;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getLong(key);
          found = true;
        } catch (UnsupportedOperationException e) {
          found = false;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration not found for key='" + key + "'";
      logger.error(msg);
    }

    return result;
  }


  /**
   * getLong.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   */
  public long getLong(final String key, final User user) {
    checkInitialized();

    long result = 0;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getLong(key, user);
          found = true;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration (for user) not found for key='" + key + "'";
      logger.warn(msg);
    }

    return result;
  }


  /**
   * getValue.
   *
   * @param   key  get key
   *
   * @return  The return value
   */
  public String getValue(final String key) {
    checkInitialized();

    String result = null;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getValue(key);
          found = true;
        } catch (UnsupportedOperationException e) {
          found = false;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration not found for key='" + key + "'";
      logger.warn(msg);
    }

    return result;
  }


  /**
   * getValue.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   */
  public String getValue(final String key, final User user) {
    checkInitialized();

    String result = null;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getValue(key, user);
          found = true;
        } catch (UnsupportedOperationException e) {
          found = false;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration (for user) not found for key='" + key + "'";
      logger.warn(msg);
    }

    return result;
  }


  /**
   * initializeProvider.
   *
   * @param  providerKey  providerKey
   * @param  resource     resource
   */
  public void initializeProvider(final String providerKey,
    final Object resource) {
    ConfigurationProvider provider = providers.get(providerKey);

    if (provider != null) {
      provider.initialize(resource);
    }
  }


  /**
   * isInitialized.
   *
   * @return  The return value
   */
  public boolean isInitialized() {
    return initialized;
  }


  /**
   * setBoolean.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  UtilityException  On Exception
   */
  public void setBoolean(final String key, final boolean value, final User user)
    throws UtilityException {
    checkInitialized();

    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext()) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        if (!provider.isReadOnly()) {

          try {
            provider.setBoolean(key, value, user);
          } catch (UnsupportedOperationException e) {

            if (logger.isWarnEnabled()) {
              logger.warn("Configuration provider '"
                + provider.getClass().getName()
                + "'does not support write, but readOnly is set to false.");
            }
          }
        }
      }
    }
  }


  /**
   * setDate.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  UtilityException  On Exception
   */
  public void setDate(final String key, final Date value, final User user)
    throws UtilityException {
    checkInitialized();

    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext()) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        if (!provider.isReadOnly()) {

          try {
            provider.setDate(key, value, user);
          } catch (UnsupportedOperationException e) {

            if (logger.isWarnEnabled()) {
              logger.warn("Configuration provider '"
                + provider.getClass().getName()
                + "'does not support write, but readOnly is set to false.");
            }
          }
        }
      }
    }
  }


  /**
   * setDouble.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  UtilityException  On Exception
   */
  public void setDouble(final String key, final double value, final User user)
    throws UtilityException {
    checkInitialized();

    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext()) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        if (!provider.isReadOnly()) {

          try {
            provider.setDouble(key, value, user);
          } catch (UnsupportedOperationException e) {

            if (logger.isWarnEnabled()) {
              logger.warn("Configuration provider '"
                + provider.getClass().getName()
                + "'does not support write, but readOnly is set to false.");
            }
          }
        }
      }
    }
  }


  /**
   * setInt.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  UtilityException  On Exception
   */
  public void setInt(final String key, final int value, final User user)
    throws UtilityException {
    checkInitialized();

    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext()) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        if (!provider.isReadOnly()) {

          try {
            provider.setInt(key, value, user);
          } catch (UnsupportedOperationException e) {

            if (logger.isWarnEnabled()) {
              logger.warn("Configuration provider '"
                + provider.getClass().getName()
                + "'does not support write, but readOnly is set to false.");
            }
          }
        }
      }
    }
  }


  /**
   * setLong.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  UtilityException  On Exception
   */
  public void setLong(final String key, final long value, final User user)
    throws UtilityException {
    checkInitialized();

    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext()) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        if (!provider.isReadOnly()) {

          try {
            provider.setLong(key, value, user);
          } catch (UnsupportedOperationException e) {

            if (logger.isWarnEnabled()) {
              logger.warn("Configuration provider '"
                + provider.getClass().getName()
                + "'does not support write, but readOnly is set to false.");
            }
          }
        }
      }
    }
  }


  /**
   * setValue.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  UtilityException  On Exception
   */
  public void setValue(final String key, final String value, final User user)
    throws UtilityException {
    checkInitialized();

    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext()) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        if (!provider.isReadOnly()) {

          try {
            provider.setValue(key, value, user);
          } catch (UnsupportedOperationException e) {

            if (logger.isWarnEnabled()) {
              logger.warn("Configuration provider '"
                + provider.getClass().getName()
                + "'does not support write, but readOnly is set to false.");
            }
          }
        }
      }
    }
  }


  /** checkInitialized. */
  protected void checkInitialized() {

    if (!isInitialized()) {
      logger.error(getClass() + " not initialized.");
      throw new RuntimeException(getClass() + " not initialized.");
    }
  }


  /**
   * getDouble.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   */
  double getDouble(final String key, final User user) {
    checkInitialized();

    double result = 0;
    boolean found = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = iter.next();
      ConfigurationProvider provider = providers.get(
          providerKey);

      if (!provider.isInitialized()) {
        logger.warn("configuration provider '" + providerKey
          + "' is not initialized.");
      } else {

        try {
          result = provider.getDouble(key, user);
          found = true;
        } catch (ConfigurationNotFoundException e) {
          found = false;
        } catch (ProviderException e) {
          logger.error("Unexpected error: ", e);
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Configuration (for user) not found for key='" + key + "'";
      logger.warn(msg);
    }

    return result;
  }


  /**
   * getProviders.
   *
   * @return  The return value
   */
  public Map<String, ConfigurationProvider> getProviders() {
    return providers;
  }


  /**
   * setProviders.
   *
   * @param  providers  Set providers
   */
  public void setProviders(Map<String, ConfigurationProvider> providers) {
    this.providers = providers;
  }


  /**
   * setInitialized.
   *
   * @param  initialized  Set initialized
   */
  public void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }


  public abstract String getEnvironment();

}
