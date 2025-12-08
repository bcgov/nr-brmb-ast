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
package ca.bc.gov.srm.farm.message;

import ca.bc.gov.srm.farm.MessageItem;
import ca.bc.gov.srm.farm.MessageType;
import ca.bc.gov.srm.farm.Utility;
import ca.bc.gov.srm.farm.exception.MessageNotFoundException;
import ca.bc.gov.srm.farm.factory.ObjectFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


/**
 * MessageUtility.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
public abstract class MessageUtility extends Utility {

  /** instance. */
  private static MessageUtility instance = null;

  /** log. */
  private Logger log = LoggerFactory.getLogger(getClass());

  /** providers. */
  private Map providers = new LinkedHashMap();

  /** Creates a new MessageUtility object. */
  public MessageUtility() {
    initialize(null);
  }

  /**
   * getInstance.
   *
   * @return  The return value.
   */
  public static MessageUtility getInstance() {

    if (instance == null) {
      instance = (MessageUtility) ObjectFactory.createObject(
          MessageUtility.class, null);
    }

    return instance;
  }


  /**
   * createErrorMessage.
   *
   * @param   key  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createErrorMessage(final String key) {
    return createErrorMessage(key, null, null);
  }

  /**
   * createErrorMessage.
   *
   * @param   key        The parameter value.
   * @param   arguments  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createErrorMessage(final String key,
    final Object[] arguments) {
    return createErrorMessage(key, arguments, null);
  }

  /**
   * createErrorMessage.
   *
   * @param   key        The parameter value.
   * @param   arguments  The parameter value.
   * @param   throwable  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createErrorMessage(final String key,
    final Object[] arguments, final Throwable throwable) {
    return createMessage(MessageType.error(), key, arguments, throwable);
  }


  /**
   * createExceptionMessage.
   *
   * @param   key  The parameter value.
   * @param   ex   The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createExceptionMessage(final String key,
    final Exception ex) {
    String msg = "Unknown exception reason.";

    if (ex != null) {
      msg = StringUtils.defaultIfEmpty(ex.getMessage(), msg);
    }

    return createErrorMessage(key, new Object[] {msg}, ex);
  }


  /**
   * createInfoMessage.
   *
   * @param   key  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createInfoMessage(final String key) {
    return createInfoMessage(key, null, null);
  }

  /**
   * createInfoMessage.
   *
   * @param   key        The parameter value.
   * @param   arguments  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createInfoMessage(final String key,
    final Object[] arguments) {
    return createInfoMessage(key, arguments, null);
  }

  /**
   * createInfoMessage.
   *
   * @param   key        The parameter value.
   * @param   arguments  The parameter value.
   * @param   throwable  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createInfoMessage(final String key,
    final Object[] arguments, final Throwable throwable) {
    return createMessage(MessageType.information(), key, arguments, throwable);
  }

  /**
   * createWarningMessage.
   *
   * @param   key  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createWarningMessage(final String key) {
    return createWarningMessage(key, null, null);
  }

  /**
   * createWarningMessage.
   *
   * @param   key        The parameter value.
   * @param   arguments  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createWarningMessage(final String key,
    final Object[] arguments) {
    return createWarningMessage(key, arguments, null);
  }

  /**
   * createWarningMessage.
   *
   * @param   key        The parameter value.
   * @param   arguments  The parameter value.
   * @param   throwable  The parameter value.
   *
   * @return  The return value.
   */
  public MessageItem createWarningMessage(final String key,
    final Object[] arguments, final Throwable throwable) {
    return createMessage(MessageType.warning(), key, arguments, throwable);
  }

  /**
   * getPattern.
   *
   * @param   key  Input parameter.
   *
   * @return  The return value.
   */
  public String getPattern(final String key) {
    String pattern = null;

    try {
      pattern = findPattern(MessageKeys.MESSAGE_KEY_LOCALE, key);
    } catch (MessageNotFoundException e) {
      pattern = e.getMessage();
      log.error(pattern);
    }

    return pattern;
  }

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
   */
  @Override
  public void initialize(final Object resource) {
    setInitialized(false);
    providers.clear();

    MessageProvider mp = new PropertiesProvider("config/messages.properties");
    providers.put("message", mp);

    setInitialized(true);
  }

  /**
   * initializeProvider.
   *
   * @param  providerKey  The parameter value.
   * @param  resource     The parameter value.
   */
  public void initializeProvider(final String providerKey,
    final Object resource) {
    MessageProvider provider = (MessageProvider) providers.get(providerKey);

    if (provider != null) {
      provider.initialize(resource);
    }
  }

  /**
   * createMessage.
   *
   * @param   messageType  The parameter value.
   * @param   key          The parameter value.
   * @param   arguments    The parameter value.
   * @param   throwable    The parameter value.
   *
   * @return  The return value.
   */
  protected MessageItem createMessage(final MessageType messageType,
    final String key, final Object[] arguments, final Throwable throwable) {
    checkInitialized();

    String pattern = getPattern(key);

    MessageItem result = new MessageItem();
    result.setArguments(arguments);
    result.setKey(key);
    result.setMessageType(messageType);
    result.setPattern(pattern);
    result.setThrowable(throwable);

    return result;
  }

  /**
   * findPattern.
   *
   * @param   locale  The parameter value.
   * @param   key     The parameter value.
   *
   * @return  The return value.
   *
   * @throws  MessageNotFoundException  On exception.
   */
  protected String findPattern(final Locale locale, final String key)
    throws MessageNotFoundException {
    checkInitialized();

    String result = null;
    boolean found = false;
    Iterator iter = providers.keySet().iterator();

    while (iter.hasNext() && !found) {
      String providerKey = (String) iter.next();
      MessageProvider provider = (MessageProvider) providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("message provider '" + providerKey + "' is not initialized.");
      } else {

        try {
          result = provider.getPattern(locale, key);
          found = true;
        } catch (MessageNotFoundException e) {
          found = false;
        }
      }
    }

    if (!found) {
      String msg = "Message not found for key='" + key + "'";
      log.error(msg);
      throw new MessageNotFoundException(msg);
    }

    return result;
  }

  /**
   * getProviders.
   *
   * @return  The return value
   */
  public Map getProviders() {
    return providers;
  }

  /**
   * setProviders.
   *
   * @param  providers  Set providers
   */
  public void setProviders(Map providers) {
    this.providers = providers;
  }

}
