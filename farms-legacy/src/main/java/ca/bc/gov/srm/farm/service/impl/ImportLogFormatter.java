/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import ca.bc.gov.srm.farm.csv.CSVParserException;
import ca.bc.gov.srm.farm.dao.ErrorDAO;
import ca.bc.gov.srm.farm.exception.DataAccessException;


/**
 * Utility class for pretty printing errors.
 *
 * @author  dzwiers
 */
public final class ImportLogFormatter {

  /** Creates a new ImportLogFormatter object. */
  private ImportLogFormatter() {
  }

  /**
   * @param   errors    List<CSVParserException>
   * @param   warnings  List<CSVParserException>
   * @param pConn  Connection
   *
   * @return  XML formated error list
   */
  public static String formatStagingXml(final ArrayList<CSVParserException> errors,
    final List<CSVParserException> warnings, Connection pConn) {
    StringBuffer sb = new StringBuffer();
    ErrorDAO dao = new ErrorDAO(pConn);
    sb.append("<STAGING_LOG>");

    if (errors != null) {
      sb.append("<ERRORS>");

      for (CSVParserException e: errors) {

        if (e != null) {
          sb.append("<ERROR");

          if (e.getFileNumber() != 0) {
            sb.append(" fileNumber=\"" + e.getFileNumber() + "\"");
          }

          if (e.getRowNumber() != 0) {
            sb.append(" rowNumber=\"" + e.getRowNumber() + "\"");
          }

          sb.append(">");
          try {
            sb.append(dao.codify(e.getLocalizedMessage()));
          } catch (DataAccessException e1) {
            sb.append(e.getLocalizedMessage());
            sb.append(e1.getLocalizedMessage());
          }
          sb.append("</ERROR>");
        }
      }

      sb.append("</ERRORS>");
    }

    if (warnings != null) {
      sb.append("<WARNINGS>");

      for (CSVParserException e: warnings) {

        if (e != null) {
          sb.append("<WARNING");

          if (e.getFileNumber() != 0) {
            sb.append(" fileNumber=\"" + e.getFileNumber() + "\"");
          }

          if (e.getRowNumber() != 0) {
            sb.append(" rowNumber=\"" + e.getRowNumber() + "\"");
          }

          sb.append(">");
          try {
            sb.append(dao.codify(e.getLocalizedMessage()));
          } catch (DataAccessException e1) {
            sb.append(e.getLocalizedMessage());
            sb.append(e1.getLocalizedMessage());
          }
          sb.append("</WARNING>");
        }
      }

      sb.append("</WARNINGS>");
    }

    sb.append("</STAGING_LOG>");

    return sb.toString();
  }
  
  
  /**
   * 
   * @param ex exception
   * @return xml
   */
  public static String formatUploadException(final Exception ex) {
    StringBuffer sb = new StringBuffer();
      
    sb.append("<STAGING_LOG><ERRORS><ERROR>");
    sb.append(ex.getMessage());
    sb.append("</ERROR></ERRORS></STAGING_LOG>");

    return sb.toString();
  }
  
  
  public static String formatImportException(final Throwable t) {
    
    // IMPORT_XML_PKG only allows reading 2000 characters.
    // 1500 should allow plenty of room for escaping XML.
    final int maxLength = 1500;
    
    StringWriter stringWriter = new StringWriter(maxLength);
    stringWriter.append(t.getMessage());
    
    PrintWriter printWriter = new PrintWriter(stringWriter);
    t.printStackTrace(printWriter);
    printWriter.flush();

    String shortened = stringWriter.toString();
    if(shortened != null && shortened.length() > maxLength) {
      shortened = shortened.substring(0, maxLength);
    }
    String errorMsg = StringEscapeUtils.escapeXml(shortened);
    String errorXml = formatImportException(errorMsg);

    return errorXml;
  }

  public static String formatImportException(String errorMsg) {
    StringBuilder sb = new StringBuilder();
    sb.append("<IMPORT_LOG><ERROR>");
    sb.append(errorMsg);
    sb.append("</ERROR></IMPORT_LOG>");
    return sb.toString();
  }
  
  
  /**
   * @return xml
   */
  public static String createEmptyImportXml() {
    return "<IMPORT_LOG></IMPORT_LOG>";
  }
  
  
  /**
	 * Used by the FMV and BPU imports
	 * 
	 * @param numRowsProcessed numRowsProcessed
	 * @param errors list of CSVParserException and Strings
	 * @return the XML
	 */
  public static String createStagingXml(
			final int numRowsProcessed,
			final ArrayList<?> errors) {
		StringBuffer sb = new StringBuffer();
		sb.append("<STAGING_LOG>");
		sb.append("<ERRORS>");

		for (Iterator<?> i = errors.iterator(); i.hasNext();) {
			Object o = i.next();
			
			if(o instanceof CSVParserException) {
				CSVParserException e = (CSVParserException) o;
				sb.append("<ERROR");
	
				if (e.getRowNumber() != 0) {
					sb.append(" rowNumber=\"" + e.getRowNumber() + "\"");
				}
	
				sb.append(">");
				sb.append(e.getLocalizedMessage());
				sb.append("</ERROR>");
			} else {
				// already xml
				String err = (String) o;
				sb.append(err);
			}
		}

		sb.append("</ERRORS>");
		sb.append("<NUM_TOTAL>" + numRowsProcessed + "</NUM_TOTAL>");
		sb.append("</STAGING_LOG>");

		return sb.toString();
	}
}
