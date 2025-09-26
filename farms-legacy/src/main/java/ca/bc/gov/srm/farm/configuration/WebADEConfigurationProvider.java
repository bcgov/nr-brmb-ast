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

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ConfigurationNotFoundException;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.webade.WebADEProviderUtils;
import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.preferences.DefaultWebADEPreference;
import ca.bc.gov.webade.preferences.DefaultWebADEPreferenceSet;
import ca.bc.gov.webade.preferences.MultiValueWebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreferenceSet;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.user.UserCredentials;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * WebADEConfigurationProvider.
 */
class WebADEConfigurationProvider extends ConfigurationProvider {


  private Properties webADEproperties = new Properties();


  private Map<String, String> applicationKeys = new HashMap<>();


  private Map<String, String> applicationValues = null;


  private Map<String, String> globalKeys = new HashMap<>();


  private Map<String, String> globalValues = null;


  private User usr = null;

  private Logger log = LoggerFactory.getLogger(getClass());


  /** Creates a new WebADEConfigurationProvider object. */
  WebADEConfigurationProvider() {
    initialize(null);
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
  @Override
  public boolean getBoolean(final String key, final User user)
    throws ProviderException {
    this.usr = user;

    return getBoolean(key);
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
  @Override
  public Date getDate(final String key, final User user)
    throws ProviderException {
    this.usr = user;

    return getDate(key);
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
  @Override
  public double getDouble(final String key, final User user)
    throws ProviderException {
    this.usr = user;

    return getDouble(key);
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
  @Override
  public int getInt(final String key, final User user)
    throws ProviderException {
    this.usr = user;

    return getInt(key);
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
  @Override
  public long getLong(final String key, final User user)
    throws ProviderException {
    this.usr = user;

    return getLong(key);
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
  @Override
  public String getValue(final String key, final User user)
    throws ProviderException {
    this.usr = user;

    return getValue(key);
  }


  /**
   * initialize.
   *
   * @param  resource  resource
   */
  @Override
  public void initialize(final Object resource) {
    Application app = WebADERequest.getInstance().getApplication();

    if (app == null) {
      throw new IllegalArgumentException(
        "webade Application instance cannot be null.");
    }

    webADEproperties.put(ConfigurationKeys.APPLICATION_CODE,
      app.getApplicationCode());
    webADEproperties.put(ConfigurationKeys.APPLICATION_ENVIRONMENT,
      app.getApplicationEnvironment());
    setInitialized(true);
  }


  /**
   * isReadOnly.
   *
   * @return  The return value
   */
  @Override
  public boolean isReadOnly() {
    return false;
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
  @Override
  public void setBoolean(final String key, final boolean value, final User user)
    throws ProviderException {
    setValue(key, String.valueOf(value), user);
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
  @Override
  public void setDate(final String key, final Date value, final User user)
    throws ProviderException {
    String s = DataParseUtils.toDateString(value);
    setValue(key, s, user);
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
  @Override
  public void setDouble(final String key, final double value, final User user)
    throws ProviderException {
    setValue(key, String.valueOf(value), user);
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
  @Override
  public void setInt(final String key, final int value, final User user)
    throws ProviderException {
    setValue(key, String.valueOf(value), user);
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
  @Override
  public void setLong(final String key, final long value, final User user)
    throws ProviderException {
    setValue(key, String.valueOf(value), user);
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
  @Override
  public void setValue(final String key, final String value, final User user)
    throws ProviderException {

    if (user == null) {
      throw new IllegalArgumentException("usr cannot be null");
    }

    try {
      Application app = WebADERequest.getInstance().getApplication();
      String lkey = StringUtils.lowerCase(key);

      UserCredentials creds = WebADEProviderUtils.getUserCredentials(user);
      WebADEPreferences prefs = app.getWebADEUserPreferences(creds);

      // use origin keys for writing...
      Map<String, String> userKeys = new HashMap<>();
      loadFromPreferences(prefs, userKeys);

      String subType = null;
      String setName = null;
      String preferenceName = null;

      String originKey = userKeys.get(lkey);

      if (StringUtils.isBlank(originKey)) {

        // if this is null, then this is a brand new key...
        String[] pieces = splitKey(lkey);

        // iterate over the existing entries to see if we can find
        // unmangled sub type and set name to use for our new entry.
        Iterator<String> iter = userKeys.keySet().iterator();

        while (iter.hasNext() && (subType == null) && (setName == null)) {
          String origUserKey = userKeys.get(iter.next());
          String[] origPieces = splitOriginKey(origUserKey);

          if (StringUtils.equals(replaceChars(origPieces[0]), pieces[0])) {
            subType = origPieces[0];
          }

          if (StringUtils.equals(replaceChars(origPieces[1]), pieces[1])) {
            setName = origPieces[1];
          }
        }

        if (subType == null) {
          subType = pieces[0];
        }

        if (setName == null) {
          setName = pieces[1];
        }

        preferenceName = pieces[2];
      } else {

        // this is an existing entry
        // so parse out the original sub type, set name and pref name
        String[] pieces = splitOriginKey(originKey);
        subType = pieces[0];
        setName = pieces[1];
        preferenceName = pieces[2];
      }

      WebADEPreferenceSet set = prefs.getPreferenceSet(subType, setName);

      if (set == null) {
        set = new DefaultWebADEPreferenceSet(setName);
      } else {
        set.removePreference(preferenceName);
      }

      WebADEPreference pref = new DefaultWebADEPreference(preferenceName);
      pref.setPreferenceValue(String.valueOf(value));
      set.addPreference(pref);

      prefs.removePreferenceSet(subType, setName);
      prefs.addPreferenceSet(subType, set);

      app.saveWebADEUserPreferences(creds, prefs);
    } catch (WebADEException e) {
      throw new ProviderException(e);
    }
  }


  /**
   * createKey.
   *
   * @param   subType         subType
   * @param   setName         setName
   * @param   preferenceName  preferenceName
   *
   * @return  The return value
   */
  protected String createKey(final String subType, final String setName,
    final String preferenceName) {
    StringBuffer buffer = new StringBuffer();

    if (StringUtils.isNotBlank(subType)) {
      buffer.append(replaceChars(subType));
      buffer.append("/");
    }

    if (StringUtils.isNotBlank(setName)) {
      buffer.append(replaceChars(setName));
      buffer.append("/");
    }

    if (StringUtils.isNotBlank(preferenceName)) {
      buffer.append(replaceChars(preferenceName));
    }

    String result = StringUtils.lowerCase(buffer.toString());

    return result;
  }


  /**
   * createOriginKey.
   *
   * @param   subType         subType
   * @param   setName         setName
   * @param   preferenceName  preferenceName
   *
   * @return  The return value
   */
  protected String createOriginKey(final String subType, final String setName,
    final String preferenceName) {

    // we do not want to alter this in any way
    // just add delimiters...
    StringBuffer buffer = new StringBuffer();

    if (StringUtils.isNotBlank(subType)) {
      buffer.append(subType);
      buffer.append("<//>");
    }

    if (StringUtils.isNotBlank(setName)) {
      buffer.append(setName);
      buffer.append("<//>");
    }

    if (StringUtils.isNotBlank(preferenceName)) {
      buffer.append(preferenceName);
    }

    return buffer.toString();
  }


  /**
   * debug.
   *
   * @param  prefs  prefs
   */
  protected void debug(final WebADEPreferences prefs) {
    Iterator<String> subTypesIter = prefs.getPreferenceSubTypes().iterator();

    while (subTypesIter.hasNext()) {
      String subType = subTypesIter.next();
      Iterator<WebADEPreferenceSet> setIter = prefs.getPreferenceSetsBySubType(subType).iterator();

      while (setIter.hasNext()) {
        WebADEPreferenceSet set = setIter.next();
        String setName = set.getPreferenceSetName();
        Iterator<String> nameIter = set.getPreferenceNames().iterator();

        while (nameIter.hasNext()) {
          String preferenceName = nameIter.next();
          WebADEPreference pref = set.getPreference(preferenceName);
          String key = createKey(subType, setName, preferenceName);

          if ((pref instanceof MultiValueWebADEPreference)) {
            MultiValueWebADEPreference mvp = (MultiValueWebADEPreference) pref;
            List<String> values = mvp.getPreferenceValues();
            String value = StringUtils.join(mvp.getPreferenceValues()
                .toArray(new String[mvp.getPreferenceValues().size()]), ",");

            if (values != null) {
              log.debug(key + "=" + value);
            }
          } else {
            String value = pref.getPreferenceValue();

            if (!StringUtils.isBlank(value)) {
              log.debug(key + "=" + value);
            }
          }
        }
      }

      // now, get any that may not be in a set.
      WebADEPreferences prefsWithNoSet = prefs.getWebADEPreferencesBySubType(
          subType);
      Iterator<WebADEPreference> iter = prefsWithNoSet.getPreferencesBySubType(subType).iterator();

      while (iter.hasNext()) {
        WebADEPreference pref = iter.next();
        String preferenceName = pref.getPreferenceName();
        String key = createKey(subType, null, preferenceName);

        if ((pref instanceof MultiValueWebADEPreference)) {
          MultiValueWebADEPreference mvp = (MultiValueWebADEPreference) pref;
          List<String> values = mvp.getPreferenceValues();
          String value = StringUtils.join(mvp.getPreferenceValues()
              .toArray(new String[mvp.getPreferenceValues().size()]), ",");

          if (values != null) {
            log.debug(key + "=" + value);
          }
        } else {
          String value = pref.getPreferenceValue();

          if (!StringUtils.isBlank(value)) {
            log.debug(key + "=" + value);
          }
        }
      }
    }
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
  @Override
  protected String getConfigurationValue(final String key)
    throws ProviderException {

    // ok, in here we need to check...
    // usr preferences (if usr is present)
    // then check the app preferences
    String result = null;
    Application app = WebADERequest.getInstance().getApplication();
    String lkey = StringUtils.lowerCase(key);

    try {

      if (this.usr != null) {
        UserCredentials creds = WebADEProviderUtils.getUserCredentials(usr);
        WebADEPreferences prefs = app.getWebADEUserPreferences(creds);
        Map<String, String> userKeys = new HashMap<>();
        Map<String, String> userValues = loadFromPreferences(prefs, userKeys);
        result = userValues.get(lkey);

        // reset usr to null
        this.usr = null;
      }

      if (StringUtils.isBlank(result)) {

        if (applicationValues == null) {
          applicationValues = loadFromPreferences(
              app.getWebADEApplicationPreferences(),
              applicationKeys);
        }

        result = applicationValues.get(lkey);
      }

      if (StringUtils.isBlank(result)) {

        if (globalValues == null) {
          globalValues = loadFromPreferences(app.getWebADEGlobalPreferences(),
              globalKeys);
        }

        result = applicationValues.get(lkey);
      }

      if (StringUtils.isBlank(result)) {
        result = webADEproperties.getProperty(lkey);
      }

      if (StringUtils.isBlank(result)) {
        throw new ConfigurationNotFoundException();
      }
    } catch (WebADEException e) {

      // always reset the usr to null
      this.usr = null;
      log.error("Error reading configuration for key='" + lkey + "'.", e);
      throw new ProviderException("Error reading configuration for key=" + lkey,
        e);
    }

    return result;
  }


  /**
   * loadFromPreferences.
   *
   * @param   prefs  prefs
   * @param   keys   keys
   *
   * @return  The return value
   */
  protected Map<String, String> loadFromPreferences(final WebADEPreferences prefs,
    final Map<String, String> keys) {
    Map<String, String> result = new HashMap<>();
    Iterator<String> subTypesIter = prefs.getPreferenceSubTypes().iterator();

    while (subTypesIter.hasNext()) {
      String subType = subTypesIter.next();
      Iterator<WebADEPreferenceSet> setIter = prefs.getPreferenceSetsBySubType(subType).iterator();

      while (setIter.hasNext()) {
        WebADEPreferenceSet set = setIter.next();
        String setName = set.getPreferenceSetName();
        Iterator<String> nameIter = set.getPreferenceNames().iterator();

        while (nameIter.hasNext()) {
          String preferenceName = nameIter.next();
          WebADEPreference pref = set.getPreference(preferenceName);
          String key = createKey(subType, setName, preferenceName);
          String originKey = createOriginKey(subType, setName, preferenceName);

          if ((pref instanceof MultiValueWebADEPreference)) {
            MultiValueWebADEPreference mvp = (MultiValueWebADEPreference) pref;
            List<String> values = mvp.getPreferenceValues();

            if (values != null) {
              String value = StringUtils.join(mvp.getPreferenceValues().toArray(
                new String[mvp.getPreferenceValues().size()]), ",");
              result.put(key, value);
              keys.put(key, originKey);
            }
          } else {
            String value = pref.getPreferenceValue();

            if (!StringUtils.isBlank(value)) {
              result.put(key, value);
              keys.put(key, originKey);
            }
          }
        }
      }

      // now, get any that may not be in a set.
      WebADEPreferences prefsWithNoSet = prefs.getWebADEPreferencesBySubType(
          subType);
      Iterator<WebADEPreference> iter = prefsWithNoSet.getPreferencesBySubType(subType)
        .iterator();

      while (iter.hasNext()) {
        WebADEPreference pref = iter.next();
        String preferenceName = pref.getPreferenceName();
        String key = createKey(subType, null, preferenceName);
        String originKey = createOriginKey(subType, null, preferenceName);

        if ((pref instanceof MultiValueWebADEPreference)) {
          MultiValueWebADEPreference mvp = (MultiValueWebADEPreference) pref;
          List<String> values = mvp.getPreferenceValues();

          if (values != null) {
            String value = StringUtils.join(mvp.getPreferenceValues()
                .toArray(new String[mvp.getPreferenceValues().size()]), ",");
            result.put(key, value);
            keys.put(key, originKey);
          }
        } else {
          String value = pref.getPreferenceValue();

          if (!StringUtils.isBlank(value)) {
            result.put(key, value);
            keys.put(key, originKey);
          }
        }
      }
    }

    return result;
  }


  /**
   * splitKey.
   *
   * @param   key  key
   *
   * @return  The return value
   */
  protected String[] splitKey(final String key) {
    final int maxPieces = 3;
    String[] pieces = StringUtils.split(key, "/");

    if ((pieces != null) && (pieces.length == 2)) {

      // no set name...
      return new String[] {pieces[0], null, pieces[1]};
    }

    if ((pieces != null) && (pieces.length == maxPieces)) {
      return pieces;
    }

    return new String[] {null, null, null};
  }


  /**
   * splitOriginKey.
   *
   * @param   key  key
   *
   * @return  The return value
   */
  protected String[] splitOriginKey(final String key) {
    final int maxPieces = 3;
    String[] pieces = StringUtils.split(key, "<//>");

    if ((pieces != null) && (pieces.length == 2)) {

      // no set name...
      return new String[] {pieces[0], null, pieces[1]};
    }

    if ((pieces != null) && (pieces.length == maxPieces)) {
      return pieces;
    }

    return new String[] {null, null, null};
  }

}
