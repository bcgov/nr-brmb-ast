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

import ca.bc.gov.srm.farm.Provider;
import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.util.DataParseUtils;

import org.apache.commons.lang.StringUtils;

import java.util.Date;


/**
 * ConfigurationProvider.
 *
 * @author   $author$
 * @version  $Revision: 167 $, $Date: 2009-10-08 09:45:16 -0700 (Thu, 08 Oct 2009) $
 */
abstract class ConfigurationProvider extends Provider {

  /**
   * getBoolean.
   *
   * @param   key  get key
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public boolean getBoolean(final String key) throws ProviderException {
    checkInitialized();

    String s = getValue(key);

    try {
      return DataParseUtils.parseBoolean(s);
    } catch (Exception e) {
      throw new ProviderException(
        "Could not parse boolean from configuration (Key ='" + key
        + "', value ='" + s + "').  " + e.getMessage(), e);
    }
  }


  /**
   * getBoolean.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public boolean getBoolean(final String key, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * getDate.
   *
   * @param   key  get key
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public Date getDate(final String key) throws ProviderException {
    checkInitialized();

    String s = getValue(key);

    try {
      return DataParseUtils.parseDate(s);
    } catch (Exception e) {
      throw new ProviderException(
        "Could not parse date from configuration (Key ='" + key + "', value ='"
        + s + "').  " + e.getMessage(), e);
    }
  }


  /**
   * getDate.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public Date getDate(final String key, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * getDouble.
   *
   * @param   key  get key
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public double getDouble(final String key) throws ProviderException {
    checkInitialized();

    String s = getValue(key);

    try {
      return DataParseUtils.parseDouble(s);
    } catch (Exception e) {
      throw new ProviderException(
        "Could not parse double from configuration (Key ='" + key
        + "', value ='" + s + "').  " + e.getMessage(), e);
    }
  }


  /**
   * getInt.
   *
   * @param   key  get key
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public int getInt(final String key) throws ProviderException {
    checkInitialized();

    String s = getValue(key);

    try {
      return DataParseUtils.parseInt(s);
    } catch (Exception e) {
      throw new ProviderException(
        "Could not parse int from configuration (Key ='" + key + "', value ='"
        + s + "').  " + e.getMessage(), e);
    }
  }


  /**
   * getInt.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public int getInt(final String key, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * getLong.
   *
   * @param   key  get key
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public long getLong(final String key) throws ProviderException {
    checkInitialized();

    String s = getValue(key);

    try {
      return DataParseUtils.parseLong(s);
    } catch (Exception e) {
      throw new ProviderException(
        "Could not parse long from configuration (Key ='" + key + "', value ='"
        + s + "').  " + e.getMessage(), e);
    }
  }


  /**
   * getLong.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public long getLong(final String key, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * getValue.
   *
   * @param   key  get key
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public String getValue(final String key) throws ProviderException {
    checkInitialized();

    String result = getConfigurationValue(key);

    return result;
  }


  /**
   * getValue.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  public String getValue(final String key, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * isReadOnly.
   *
   * @return  The return value
   */
  public boolean isReadOnly() {
    return true;
  }


  /**
   * setBoolean.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  ProviderException  On Exception
   */
  public void setBoolean(final String key, final boolean value, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * setDate.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  ProviderException  On Exception
   */
  public void setDate(final String key, final Date value, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * setDouble.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  ProviderException  On Exception
   */
  public void setDouble(final String key, final double value, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * setInt.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  ProviderException  On Exception
   */
  public void setInt(final String key, final int value, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * setLong.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  ProviderException  On Exception
   */
  public void setLong(final String key, final long value, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * setValue.
   *
   * @param   key    Set key
   * @param   value  Set value
   * @param   user   Set user
   *
   * @throws  ProviderException  On Exception
   */
  public void setValue(final String key, final String value, final User user)
    throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }


  /**
   * getConfigurationValue.
   *
   * @param   key  get key
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  protected abstract String getConfigurationValue(String key)
    throws ProviderException;


  /**
   * replaceChars.
   *
   * @param   key  key
   *
   * @return  The return value
   */
  protected String replaceChars(final String key) {
    char[] replace = {
        '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-',
        '+', '=', '{', '[', '}', ']', '|', '\\', ':', ';', '"', '\'', '<', ',',
        '>', '?', '/'
      };
    String result = key;

    // want to end up with lowercase, dotted property key
    result = StringUtils.replace(result, " ", ".");

    for (int i = 0; i < replace.length; i++) {
      result = StringUtils.replaceChars(result, replace[i], '.');
    }

    return StringUtils.lowerCase(result);
  }


  /**
   * getDouble.
   *
   * @param   key   get key
   * @param   user  get user
   *
   * @return  The return value
   *
   * @throws  ProviderException  On Exception
   */
  double getDouble(final String key, final User user) throws ProviderException {
    throw new UnsupportedOperationException(
      "User based configuration is not supported.");
  }

}
