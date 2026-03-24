package ca.bc.gov.farms.service.api.v1.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.csv.CSVParserException;
import ca.bc.gov.farms.persistence.v1.dao.ErrorDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ErrorDaoImpl;

public class ImportLogFormatter {

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
        if (shortened != null && shortened.length() > maxLength) {
            shortened = shortened.substring(0, maxLength);
        }
        String errorMsg = StringEscapeUtils.escapeXml10(shortened);
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

    public static String createStagingXml(
            final int numRowsProcessed,
            final List<?> errors) {
        StringBuffer sb = new StringBuffer();
        sb.append("<STAGING_LOG>");
        sb.append("<ERRORS>");

        for (Iterator<?> i = errors.iterator(); i.hasNext();) {
            Object o = i.next();

            if (o instanceof CSVParserException) {
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

    public static String formatStagingXml(final List<CSVParserException> errors,
            final List<CSVParserException> warnings, Connection pConn) {
        StringBuffer sb = new StringBuffer();
        ErrorDao dao = new ErrorDaoImpl(pConn);
        sb.append("<STAGING_LOG>");

        if (errors != null) {
            sb.append("<ERRORS>");

            for (CSVParserException e : errors) {

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
                    } catch (DaoException e1) {
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

            for (CSVParserException e : warnings) {

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
                    } catch (DaoException e1) {
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

    public static String formatUploadException(final Exception ex) {
        StringBuffer sb = new StringBuffer();

        sb.append("<STAGING_LOG><ERRORS><ERROR>");
        sb.append(ex.getMessage());
        sb.append("</ERROR></ERRORS></STAGING_LOG>");

        return sb.toString();
    }
}
