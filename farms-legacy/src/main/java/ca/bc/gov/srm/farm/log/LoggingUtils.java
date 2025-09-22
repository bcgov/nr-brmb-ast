/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * LoggingUtils.
 *
 * @author awilkinson
 */
public final class LoggingUtils {

  private static final Logger LOG = LoggerFactory.getLogger(LoggingUtils.class);
    
  private static final int BUFFER_SIZE = 100;

  /** empty private constructor. */
  private LoggingUtils() {
  }


  public static void convert(final StringBuffer buffer, final Object input) {

    if (input == null) {
      buffer.append("<null>");

      return;
    }

    // Primitive types, and non-object arrays
    // use toString().  Less than ideal for int[], etc., but
    // that's a lot of work for a rare case.
    if (!(input instanceof Object[])) {
      buffer.append(input.toString());

      return;
    }

    buffer.append("(");
    buffer.append(getJavaClassName(input.getClass()));
    buffer.append("){");

    Object[] array = (Object[]) input;
    int count = array.length;

    for (int i = 0; i < count; i++) {

      if (i > 0) {
        buffer.append(", ");
      }

      // We use convert() again, because it could be a multi-dimensional
      // array (god help us) where each element must be converted.
      convert(buffer, array[i]);
    }

    buffer.append("}");
  }


  public static void entry(final Logger log, final String methodName,
    final Object[] args) {

    if (log.isDebugEnabled()) {
      StringBuffer buffer = new StringBuffer(BUFFER_SIZE);
      buffer.append("> ");
      buffer.append(methodName);
      buffer.append("(");

      int count = 0;

      if (args != null) {
        count = args.length;
        
        for (int i = 0; i < count; i++) {
          Object arg = args[i];
          
          if (i > 0) {
            buffer.append(", ");
          }
          
          convert(buffer, arg);
        }
      }

      buffer.append(")");
      log.debug(buffer.toString());
    }
  }


  public static void exception(final Logger log, final String methodName,
    final Throwable t) {

    if (log.isDebugEnabled()) {
      StringBuffer buffer = new StringBuffer(BUFFER_SIZE);
      buffer.append("EXCEPTION ");
      buffer.append(methodName);
      buffer.append("() -- ");
      buffer.append(t.getClass().getName());
      log.error(buffer.toString(), t);
    }
  }
  
  /**
   * 
   * @param o Object
   */
  public static void debug(Object o){
    if( o == null || LOG.isDebugEnabled()){
      LOG.debug("", o);
    }
  }


  public static void exit(final Logger log, final String methodName,
    final Object result) {

    if (log.isDebugEnabled()) {
      StringBuffer buffer = new StringBuffer(BUFFER_SIZE);
      buffer.append("< ");
      buffer.append(methodName);
      buffer.append("() [");
      convert(buffer, result);
      buffer.append("]");
      log.debug(buffer.toString());
    }
  }


  public static void voidExit(final Logger log, final String methodName) {

    if (log.isDebugEnabled()) {
      StringBuffer buffer = new StringBuffer(BUFFER_SIZE);
      buffer.append("< ");
      buffer.append(methodName);
      buffer.append("()");
      log.debug(buffer.toString());
    }
  }


  private static String getJavaClassName(final Class<?> inputClass) {

    if (inputClass.isArray()) {
      return getJavaClassName(inputClass.getComponentType()) + "[]";
    }

    return inputClass.getName();
  }
  
  
  public static void logMethodStart(Logger logger) {
    logger.debug("<" + Thread.currentThread().getStackTrace()[2].getMethodName());
  }
  
  public static void logMethodEnd(Logger logger) {
    logger.debug(">" + Thread.currentThread().getStackTrace()[2].getMethodName());
  }
  
  public static void logMethodEnd(Logger logger, Object result) {
    logger.debug(">" + Thread.currentThread().getStackTrace()[2].getMethodName() + " - result: " + result);
  }
  
  public static void logWithMethodName(Logger logger, String message) {
    logger.debug(Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + message);
  }
  
  public static String getStackTraceString(Exception e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    pw.flush();
    String stackTraceString = sw.toString();
    return stackTraceString;
  }
}
