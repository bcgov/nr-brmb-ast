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
package ca.bc.gov.srm.farm.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.domain.AccountSearchResult;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;


/**
 * DAO used by the webapp for searching.
 */
public class SearchDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_SEARCH_PKG";

  private static final String ACCOUNTS_PROC = "SEARCH_ACCOUNTS";
  
  private static final String IMPORTS_PROC = "SEARCH_IMPORTS";


  @SuppressWarnings("resource")
  public List<AccountSearchResult> searchAccounts(
    final Transaction transaction, 
    final String pin,
    final String name, 
    final String city, 
    final String postalCode,
    final String userId) throws DataAccessException {
    
    String procName = PACKAGE_NAME + "." + ACCOUNTS_PROC;
    List<AccountSearchResult> items = new ArrayList<>();
    Connection connection = getConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 5;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      int param = 1;
      proc.setString(param++, pin);
      proc.setString(param++, name);
      proc.setString(param++, city);
      proc.setString(param++, postalCode);
      proc.setString(param++, userId);

      proc.execute();
      resultSet = proc.getResultSet();

      while (resultSet.next()) {
        AccountSearchResult result = new AccountSearchResult();

        result.setName(resultSet.getString("NAME"));
        result.setPin(resultSet.getString("PIN"));
        result.setAddress(resultSet.getString("ADDRESS"));

        items.add(result);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return items;
  }
  
  
  @SuppressWarnings("resource")
  public final List<ImportSearchResult> searchImports(final Transaction transaction,
      final List<String> importTypes)
    throws DataAccessException {
    String procName = PACKAGE_NAME + "." + IMPORTS_PROC;
    List<ImportSearchResult> items = new ArrayList<>();
    Connection connection = getOracleConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 2;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      Array oracleArrayInventoryCodes = createStringOracleArray(transaction, importTypes);

      boolean searchByImportClass = false;
      if(importTypes != null && importTypes.size() > 0) {
        searchByImportClass = true;
      }

      int c = 1;
      proc.setIndicator(c++, searchByImportClass);
      proc.setArray(c++, oracleArrayInventoryCodes);
      proc.execute();
      resultSet = proc.getResultSet();

      while (resultSet.next()) {
        ImportSearchResult result = new ImportSearchResult();

        result.setImportVersionId(
        		resultSet.getString("IMPORT_VERSION_ID"));
        
        //
        // escape the backslashes in the user IDs.
        //
        String id = resultSet.getString("IMPORTED_BY_USER");
        id = StringEscapeUtils.escapeJavaScript(id);
        result.setImportedBy(id);
        
        //
        // Carrige returns in the description were causing the screen
        // to not show anything.
        //
        String desc = resultSet.getString("DESCRIPTION");
        desc = StringEscapeUtils.escapeJavaScript(desc);
        result.setDescription(desc);
        
        result.setStateCode(
        		resultSet.getString("IMPORT_STATE_CODE"));
        result.setStateDescription(
        		resultSet.getString("IMPORT_STATE_DESCRIPTION"));
        result.setClassCode(
        		resultSet.getString("IMPORT_CLASS_CODE"));
        result.setClassDescription(
        		resultSet.getString("IMPORT_CLASS_DESCRIPTION"));
        result.setUpdateDate(
        		resultSet.getDate("WHEN_UPDATED"));

        items.add(result);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return items;
  }
}
