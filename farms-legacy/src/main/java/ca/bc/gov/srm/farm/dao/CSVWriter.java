package ca.bc.gov.srm.farm.dao;

import java.io.File;
import java.io.FileOutputStream;

/**
 Copyright 2005 Bytecode Pty Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A very simple CSV writer released under a commercial-friendly license.
 * 
 * @author Glen Smith
 * 
 */
public class CSVWriter {

  private Logger logger = LoggerFactory.getLogger(CSVWriter.class);

  public static final int INITIAL_STRING_SIZE = 128;

  private Writer rawWriter;

  private PrintWriter pw;

  private char separator;

  private char quotechar;

  private char escapechar;

  private String lineEnd;

  public HashMap<Integer, Format> dataTypeFormats;
  
  public HashMap<String, Format> columnFormatsByName = new HashMap<>();

  /** The character used for escaping quotes. */
  public static final char DEFAULT_ESCAPE_CHARACTER = '\\';

  /** The default separator to use if none is supplied to the constructor. */
  public static final char DEFAULT_SEPARATOR = ',';

  /**
   * The default quote character to use if none is supplied to the constructor.
   */
  public static final char DEFAULT_QUOTE_CHARACTER = '"';

  /** The quote constant to use when you wish to suppress all quoting. */
  public static final char NO_QUOTE_CHARACTER = '\u0000';

  /** The escape constant to use when you wish to suppress all escaping. */
  public static final char NO_ESCAPE_CHARACTER = '\u0000';

  /** Default line terminator uses platform encoding. */
  public static final String DEFAULT_LINE_END = "\n";

  /**
   * Constructs CSVWriter using a comma for the separator.
   * 
   * @param writer
   *          the writer to an underlying CSV source.
   */
  public CSVWriter(Writer writer) {
    this(writer, DEFAULT_SEPARATOR);
  }

  /**
   * Constructs CSVWriter with supplied separator.
   * 
   * @param writer
   *          the writer to an underlying CSV source.
   * @param separator
   *          the delimiter to use for separating entries.
   */
  public CSVWriter(Writer writer, char separator) {
    this(writer, separator, DEFAULT_QUOTE_CHARACTER);
  }

  /**
   * Constructs CSVWriter with supplied separator and quote char.
   * 
   * @param writer
   *          the writer to an underlying CSV source.
   * @param separator
   *          the delimiter to use for separating entries
   * @param quotechar
   *          the character to use for quoted elements
   */
  public CSVWriter(Writer writer, char separator, char quotechar) {
    this(writer, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
  }

  /**
   * Constructs CSVWriter with supplied separator and quote char.
   * 
   * @param writer
   *          the writer to an underlying CSV source.
   * @param separator
   *          the delimiter to use for separating entries
   * @param quotechar
   *          the character to use for quoted elements
   * @param escapechar
   *          the character to use for escaping quotechars or escapechars
   */
  public CSVWriter(Writer writer, char separator, char quotechar,
      char escapechar) {
    this(writer, separator, quotechar, escapechar, DEFAULT_LINE_END);
  }

  /**
   * Constructs CSVWriter with supplied separator and quote char.
   * 
   * @param writer
   *          the writer to an underlying CSV source.
   * @param separator
   *          the delimiter to use for separating entries
   * @param quotechar
   *          the character to use for quoted elements
   * @param lineEnd
   *          the line feed terminator to use
   */
  public CSVWriter(Writer writer, char separator, char quotechar, String lineEnd) {
    this(writer, separator, quotechar, DEFAULT_ESCAPE_CHARACTER, lineEnd);
  }

  /**
   * Constructs CSVWriter with supplied separator, quote char, escape char and
   * line ending.
   * 
   * @param writer
   *          the writer to an underlying CSV source.
   * @param separator
   *          the delimiter to use for separating entries
   * @param quotechar
   *          the character to use for quoted elements
   * @param escapechar
   *          the character to use for escaping quotechars or escapechars
   * @param lineEnd
   *          the line feed terminator to use
   */
  public CSVWriter(Writer writer, char separator, char quotechar,
      char escapechar, String lineEnd) {
    this.rawWriter = writer;
    this.pw = new PrintWriter(writer);
    this.separator = separator;
    this.quotechar = quotechar;
    this.escapechar = escapechar;
    this.lineEnd = lineEnd;
    dataTypeFormats = DEFAULT_FORMATS;
  }

  public static void writeOutputFile(
      final String[] headerLines,
      final String[] columnHeadings,
      final Format[] columnFormats,
      final File outputFile,
      final ResultSet rs)
      throws SQLException, IOException {
    
    try(FileOutputStream fos = new FileOutputStream(outputFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos);) {
      
      CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
      
      writer.writeAll(headerLines);
      writer.writeNext(columnHeadings);
      
      writer.writeAll(rs, false, columnFormats);

      writer.flush();
      osw.flush();
    }
  }

  /**
   * Writes lines to the CSV file as is.
   * Useful for including header lines in a report file.
   * 
   * @param lines Each String represents a line of the file.
   * @return number of lines written
   */
  public int writeAll(String[] lines) {
    for (String line : lines) {
      pw.write(line);
      pw.write(lineEnd);
    }
    return lines.length;
  }
  
  /**
   * Writes the entire list to a CSV file.
   * 
   * @param allLines
   *          List<String[]> a List of String[], with each String[] representing
   *          a line of the file.
   * @return number of lines written
   */
  public int writeAll(List<String[]> allLines) {
    for (String[] line : allLines) {
      writeNext(line);
    }
    return allLines.size();
  }

  /**
   *  
   * @param metadata ResultSetMetaData
   * @throws SQLException SQLException
   */
  protected void writeColumnNames(ResultSetMetaData metadata)
      throws SQLException {

    int columnCount = metadata.getColumnCount();

    String[] nextLine = new String[columnCount];
    for (int i = 0; i < columnCount; i++) {
      nextLine[i] = "\""+metadata.getColumnName(i + 1)+"\"";
    }
    writeNext(nextLine);
  }

  public static final HashMap<Integer, Format> DEFAULT_FORMATS = new HashMap<>();
  {
    /*
     * MessageFormat mf = new MessageFormat("\"{0}\""); DEFAULT_FORMATS.put(new
     * Integer(Types.BIT), mf); DEFAULT_FORMATS.put(new
     * Integer(Types.JAVA_OBJECT), mf); DEFAULT_FORMATS.put(new
     * Integer(Types.LONGVARCHAR), mf); DEFAULT_FORMATS.put(new
     * Integer(Types.VARCHAR), mf); DEFAULT_FORMATS.put(new Integer(Types.CHAR),
     * mf); DEFAULT_FORMATS.put(new Integer(Types.BOOLEAN), mf);
     * DEFAULT_FORMATS.put(new Integer(Types.CLOB), mf);
     */

    DecimalFormat df = new DecimalFormat("#");
    DEFAULT_FORMATS.put(new Integer(Types.BIGINT), df);
    DEFAULT_FORMATS.put(new Integer(Types.INTEGER), df);
    DEFAULT_FORMATS.put(new Integer(Types.TINYINT), df);
    DEFAULT_FORMATS.put(new Integer(Types.SMALLINT), df);

    df = new DecimalFormat("#0.#");
    DEFAULT_FORMATS.put(new Integer(Types.DECIMAL), df);
    DEFAULT_FORMATS.put(new Integer(Types.DOUBLE), df);
    DEFAULT_FORMATS.put(new Integer(Types.FLOAT), df);
    DEFAULT_FORMATS.put(new Integer(Types.REAL), df);
    DEFAULT_FORMATS.put(new Integer(Types.NUMERIC), df);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    DEFAULT_FORMATS.put(new Integer(Types.DATE), sdf);

    sdf = new SimpleDateFormat("HH:mm:ss");
    DEFAULT_FORMATS.put(new Integer(Types.TIME), sdf);

    sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    DEFAULT_FORMATS.put(new Integer(Types.TIMESTAMP), sdf);
  }

  /**
   * Writes the entire ResultSet to a CSV file.
   * 
   * The caller is responsible for closing the ResultSet.
   * 
   * @param rs
   *          the recordset to write
   * @param includeColumnNames
   *          true if you want column names in the output, false otherwise
   * @param f
   *          either null or full set.
   * @return number of lines written
   * 
   * @throws java.io.IOException
   *           thrown by getColumnValue
   * @throws java.sql.SQLException
   *           thrown by getColumnValue
   */
  public int writeAll(java.sql.ResultSet rs, boolean includeColumnNames,
      Format[] f) throws SQLException, IOException {
    Format[] formats = f;
    ResultSetMetaData metadata = rs.getMetaData();

    if (includeColumnNames) {
      writeColumnNames(metadata);
    }

    int columnCount = metadata.getColumnCount();

    if (formats == null) {
      formats = new Format[columnCount];
      for (int i = 0; i < columnCount; i++) {
        formats[i] = getColumnFormat(metadata, i);
        if (formats[i] == null) {
          // default
          formats[i] = dataTypeFormats.get(new Integer(Types.VARCHAR));
        }
      }
    }

    if (formats.length != columnCount) {
      throw new SQLException("Invalid Column Definitions. Count of formats does not match column count.");
    }

    int recordCount = 0;
    while (rs.next()) {
      String[] nextLine = new String[columnCount];

      for (int i = 0; i < columnCount; i++) {
        nextLine[i] = getColumnValue(rs, metadata.getColumnType(i + 1), i + 1,
            formats[i]);
      }

      writeNext(nextLine);
      recordCount++;
      
      if(recordCount % 10000 == 0) {
        logger.debug("recordCount=" + recordCount);
      }
    }
    return recordCount;
  }

  /**
   * @param metadata metadata
   * @param i i
   * @return Format
   * @throws SQLException
   */
  private Format getColumnFormat(ResultSetMetaData metadata, int i) throws SQLException {
    Format result = columnFormatsByName.get(metadata.getColumnName(i + 1));
    if(result == null) {
      result = dataTypeFormats.get(new Integer(metadata.getColumnType(i + 1)));
    }
    return result;
  }

  /**
   * 
   * @param rs ResultSet
   * @param colType int
   * @param colIndex int
   * @param format Format
   * @return String
   * @throws SQLException SQLException
   * @throws IOException IOException
   */
  private static String getColumnValue(ResultSet rs, int colType, int colIndex,
      Format format) throws SQLException, IOException {

    String value = "";

    switch (colType) {
    case Types.BIT:
    case Types.JAVA_OBJECT:
      Object obj = rs.getObject(colIndex);
      if (obj != null) {
        value = obj.toString();
        if (format != null) {
          value = format.format(value);
        } else {
          value = "\"" + value + "\"";
        }
      }
      break;
    case Types.BOOLEAN:
      boolean b = rs.getBoolean(colIndex);
      if (!rs.wasNull()) {
        value = Boolean.valueOf(b).toString();
        if (format != null) {
          value = format.format(value);
        }
      }
      break;
    case Types.CLOB:
      Clob c = rs.getClob(colIndex);
      if (c != null) {
        value = read(c);
        if (format != null) {
          value = format.format(value);
        } else {
          value = "\"" + value + "\"";
        }
      }
      break;
    case Types.BIGINT:
      long lv = rs.getLong(colIndex);
      if (!rs.wasNull()) {
        value = Long.toString(lv);
        value = format.format(new Long(lv));
      }
      break;
    case Types.DECIMAL:
    case Types.DOUBLE:
    case Types.FLOAT:
    case Types.REAL:
    case Types.NUMERIC:
      BigDecimal bd = rs.getBigDecimal(colIndex);
      if (bd != null) {
        value = format.format(bd);
      }
      break;
    case Types.INTEGER:
    case Types.TINYINT:
    case Types.SMALLINT:
      int intValue = rs.getInt(colIndex);
      if (!rs.wasNull()) {
        value = format.format(new Integer(intValue));
      }
      break;
    case Types.DATE:
      java.sql.Date date = rs.getDate(colIndex);
      if (date != null) {
        value = format.format(date);
      }
      break;
    case Types.TIME:
      Time t = rs.getTime(colIndex);
      if (t != null) {
        value = format.format(t);
      }
      break;
    case Types.TIMESTAMP:
      Timestamp tstamp = rs.getTimestamp(colIndex);
      if (tstamp != null) {
        value = format.format(tstamp);
      }
      break;
    case Types.LONGVARCHAR:
    case Types.VARCHAR:
    case Types.CHAR:
      value = rs.getString(colIndex);
      if (format != null) {
        value = format.format(value);
      } else {
        if(value!=null){
          value = "\"" + value + "\"";
        }
      }
      break;
    default:
      value = "";
    }

    if (value == null) {
      value = "";
    }

    return value;

  }

  /**
   * 
   * @param c Clob
   * @return String
   * @throws SQLException SQLException
   * @throws IOException IOException
   */
  private static String read(Clob c) throws SQLException, IOException {
    StringBuffer sb = new StringBuffer((int) c.length());
    try(Reader r = c.getCharacterStream()) {
      final int twoK = 2048;
      char[] cbuf = new char[twoK];
      int n;
      while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
        if (n > 0) {
          sb.append(cbuf, 0, n);
        }
      }
    }
    return sb.toString();
  }

  /**
   * Writes the next line to the file.
   * 
   * @param nextLine
   *          a string array with each comma-separated element as a separate
   *          entry.
   */
  public void writeNext(String[] nextLine) {

    if (nextLine == null){
      return;
    }

    StringBuffer sb = new StringBuffer(INITIAL_STRING_SIZE);
    for (int i = 0; i < nextLine.length; i++) {

      if (i != 0) {
        sb.append(separator);
      }

      String nextElement = nextLine[i];
      if (nextElement == null){
        continue;
      }
      if (quotechar != NO_QUOTE_CHARACTER){
        sb.append(quotechar);
      }

      sb.append(stringContainsSpecialCharacters(nextElement) ? processLine(nextElement)
              : nextElement);

      if (quotechar != NO_QUOTE_CHARACTER){
        sb.append(quotechar);
      }
    }

    sb.append(lineEnd);
    pw.write(sb.toString());

  }

  /**
   * 
   * @param line String
   * @return boolean
   */
  private boolean stringContainsSpecialCharacters(String line) {
    return line.indexOf(quotechar) != -1 || line.indexOf(escapechar) != -1;
  }

  /**
   * 
   * @param nextElement String
   * @return String
   */
  protected String processLine(String nextElement) {
    StringBuffer sb = new StringBuffer(INITIAL_STRING_SIZE);
    for (int j = 0; j < nextElement.length(); j++) {
      char nextChar = nextElement.charAt(j);
      if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
        sb.append(escapechar).append(nextChar);
      } else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
        sb.append(escapechar).append(nextChar);
      } else {
        sb.append(nextChar);
      }
    }

    return sb.toString();
  }

  /**
   * Flush underlying stream to writer.
   */
  public void flush() {
    pw.flush();
  }

  /**
   * Close the underlying stream writer flushing any buffered content.
   * 
   * @throws IOException
   *           if bad things happen
   * 
   */
  public void close() throws IOException {
    flush();
    pw.close();
    rawWriter.close();
  }

  public HashMap<Integer, Format> getDataTypeFormats() {
    return dataTypeFormats;
  }

  public void setDataTypeFormats(HashMap<Integer, Format> dataTypeFormats) {
    this.dataTypeFormats = dataTypeFormats;
  }

  public HashMap<String, Format> getColumnFormatsByName() {
    return columnFormatsByName;
  }

  public void setColumnFormatsByName(HashMap<String, Format> columnFormatsByName) {
    this.columnFormatsByName = columnFormatsByName;
  }

}
