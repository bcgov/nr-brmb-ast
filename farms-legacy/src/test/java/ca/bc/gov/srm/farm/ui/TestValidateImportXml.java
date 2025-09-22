package ca.bc.gov.srm.farm.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ca.bc.gov.srm.farm.util.TestUtils;
import oracle.jdbc.OracleResultSet;


public class TestValidateImportXml {
	
  @Disabled
  @Test
  public void validateImportXml() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      factory.setValidating(true);

      factory.setAttribute(
          "http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
          "http://www.w3.org/2001/XMLSchema");
      
      factory.setAttribute(
          "http://java.sun.com/xml/jaxp/properties/schemaSource", 
          "C:\\source\\SVN_REPO\\MAL_FARM\\xsd\\import_version.xsd");
      
      DocumentBuilder parser = factory.newDocumentBuilder();
      
      try(Connection connection = TestUtils.openConnection();) {
        String sqlText = "select import_audit_info from farm_import_versions where import_version_id = 1";
        
        try(Statement stmt = connection.createStatement();) {
          try(ResultSet resultSet = stmt.executeQuery(sqlText);) {
    
            if (resultSet.next()) {
              Clob clob = ((OracleResultSet) resultSet).getCLOB(1);
              
              // errors are sent to stderr
              try (InputStream asciiStream = clob.getAsciiStream();) {
                Document doc = parser.parse(asciiStream);
                assertNotNull(doc);
              }
            } else {
              System.err.println("invalid import_version_id");
              fail();
            }
            
          }
        }
      }
      
    } catch (ParserConfigurationException e) {
      System.out.println("Parser not configured: " + e.getMessage());
      fail();
    } catch (SAXException e) {
      System.out.print("Parsing XML failed due to a " + 
          e.getClass().getName() + ":");
      System.out.println(e.getMessage());
      fail();
    } catch (IOException e) {
      e.printStackTrace();
      fail();
    } catch (SQLException e) {
      e.printStackTrace();
      fail();
    }

  }
}
