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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.UserVerifiedIndividualView;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.InventoryItemListView;
import ca.bc.gov.srm.farm.list.LineItemListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.list.SectorDetailCodeListView;
import ca.bc.gov.srm.farm.list.UserListView;
import ca.bc.gov.srm.farm.transaction.Transaction;


/**
 * DAO for getting lists used by the webapp.
 */
public class ListDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_WEBAPP_PKG";

  /**
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getInventoryClassCodes(final Transaction transaction)
    throws DataAccessException {
    return getCodes(transaction, "GET_INVENTORY_CLASS_CODES");
  }
  
  /**
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getExportClassCodes(final Transaction transaction)
    throws DataAccessException {
    return getCodes(transaction, "GET_EXPORT_CLASS_CODES");
  }
  
  /**
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getRegionalOfficeCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_REGIONAL_OFFICE_CODES");
  }


  /**
   * Returns a ListView containing pairs of
   * farm type code and description.
   * @param   transaction  transaction
   * 
   * @return  ListView
   * 
   * @throws  DataAccessException  on exception
   */
  public ListView[] getFarmTypeCodes(final Transaction transaction)
    throws DataAccessException {
    return getCodes(transaction, "GET_FARM_TYPE_CODES");
  }


  /**
   * Returns a ListView containing pairs of
   * participant class code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getParticipantClassCodes(final Transaction transaction)
    throws DataAccessException {
    return getCodes(transaction, "GET_PARTICIPANT_CLASS_CODES");
  }


  /**
   * Returns a ListView containing pairs of
   * federal status code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getFederalStatusCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_FEDERAL_STATUS_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * federal status code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getFederalAccountingCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_FEDERAL_ACCOUNTING_CODES");
  }


  /**
   * Returns a ListView containing pairs of
   * participant profile code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getParticipantProfileCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_PARTICIPANT_PROFILE_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * municipality class code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getMunicipalityCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_MUNICIPALITY_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * scenario state code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getScenarioStateCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_SCENARIO_STATE_CODES");
  }
  
  /**
   * Returns a ListView containing pairs of
   * scenario category code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getScenarioCategoryCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_SCENARIO_CATEGORY_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * scenario class code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getScenarioClassCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_SCENARIO_CLASS_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * inventory item code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getInventoryItemCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_INVENTORY_ITEM_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * inventory item code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getInventoryGroupCodes(final Transaction transaction)
      throws DataAccessException {
    return getCodes(transaction, "GET_INVENTORY_GROUP_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * inventory item code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getPucInventoryItemCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_PUC_INVENTORY_ITEM_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * structure group code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getStructureGroupCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_STRUCTURE_GROUP_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * crop unit code and description.
   * @param   transaction  transaction
   *
   * @return  ListView
   *
   * @throws  DataAccessException  on exception
   */
  public ListView[] getCropUnitCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_CROP_UNIT_CODES");
  }
  
  
  /**
   * Returns a ListView containing pairs of
   * sector code and description.
   * @param   transaction  transaction
   * @return  ListView[]
   * @throws  DataAccessException  on exception
   */
  public ListView[] getSectorCodes(final Transaction transaction)
  throws DataAccessException {
    return getCodes(transaction, "GET_SECTOR_CODES");
  }
  
  /**
   * Returns a ListView containing pairs of
   * chef submission status code and description.
   * @param   transaction  transaction
   * @return  ListView[]
   * @throws  DataAccessException  on exception
   */
  public ListView[] getChefSubmssnStatusCodes(final Transaction transaction)
      throws DataAccessException {
    return getCodes(transaction, "GET_CHEF_SUBMSSN_STATUS_CODES");
  }

  
  /**
   * @param   transaction  transaction
   * @param   procName     procName
   *
   * @return  List
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  private List<CodeListView> getCodeList(final Transaction transaction, final String procName)
    throws DataAccessException {
    String qualifiedProcName = PACKAGE_NAME + "." + procName;
    List<CodeListView> rows = new ArrayList<>();
    Connection connection = getConnection(transaction);
    CallableStatement cstmt = null;
    ResultSet resultSet = null;
    final int paramCount = 0;

    try {
      cstmt = prepareFunction(connection, qualifiedProcName, paramCount);
      cstmt.execute();
      resultSet = (ResultSet) cstmt.getObject(1);

      while (resultSet.next()) {
        String code = resultSet.getString("CODE");
        String desc = resultSet.getString("DESCRIPTION");
        rows.add(new CodeListView(code, desc));
      }

    } catch (SQLException e) {
      handleException(e);
    } finally {
      close(resultSet, cstmt);
    }

    return rows;
  }

  /**
   * getCodes.
   *
   * @param   transaction  the transaction
   * @param   procName     the proc name
   *
   * @return  the list
   *
   * @throws  DataAccessException  on exception
   */
  private ListView[] getCodes(final Transaction transaction,
    final String procName) throws DataAccessException {
    List<CodeListView> rows = getCodeList(transaction, procName);
    CodeListView[] result = rows.toArray(
        new CodeListView[rows.size()]);

    return result;
  }

  
  /**
   * @param   transaction  transaction
   * @param   year         Integer
   * @param   verifiedDate Date
   *
   * @return  List
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public List<LineItemListView> getLineItems(final Transaction transaction, final Integer year, final Date verifiedDate)
    throws DataAccessException {
    String qualifiedProcName = PACKAGE_NAME + "." + "GET_LINE_ITEMS";
    List<LineItemListView> rows = new ArrayList<>();
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    final int paramCount = 2;

    try {
      proc = new DAOStoredProcedure(connection, qualifiedProcName, paramCount, true);

      int param = 1;
      proc.setInt(param++, year.intValue());
      proc.setDate(param++, verifiedDate);
      proc.execute();
      rs = proc.getResultSet();

      while (rs.next()) {
        LineItemListView item = new LineItemListView();
        
        item.setLineItem(getInteger(rs, "LINE_ITEM"));
        item.setDescription(getString(rs, "DESCRIPTION"));
        item.setEligible(Boolean.valueOf(getIndicator(rs, "ELIGIBLE_IND")));
        item.setIneligible(Boolean.valueOf(getIndicator(rs, "INELIGIBLE_IND")));
        
        rows.add(item);
      }

    } catch (SQLException e) {
      handleException(e);
    } finally {
      close(rs, proc);
    }

    return rows;
  }
  
  
  /**
   * @param   transaction  transaction
   * @return  ListView[]
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public ListView[] getInventoryValidItems(final Transaction transaction)
  throws DataAccessException {
    String qualifiedProcName = PACKAGE_NAME + "." + "GET_INVENTORY_VALID_ITEMS";
    List<InventoryItemListView> items = new ArrayList<>();
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    final int paramCount = 0;
    
    try {
      proc = new DAOStoredProcedure(connection, qualifiedProcName, paramCount, true);
      
      proc.execute();
      rs = proc.getResultSet();
      
      while (rs.next()) {
        InventoryItemListView item = new InventoryItemListView();
        
        item.setCommodityXrefId(getInteger(rs, "AGRISTABILTY_CMMDTY_XREF_ID"));
        item.setInventoryItemCode(getString(rs, "INVENTORY_ITEM_CODE"));
        item.setInventoryItemCodeDescription(getString(rs, "ITEM_CODE_DESC"));
        item.setInventoryClassCode(getString(rs, "INVENTORY_CLASS_CODE"));
        item.setInventoryClassCodeDescription(getString(rs, "ITEM_CLASS_DESC"));
        item.setIsMarketCommodity(Boolean.valueOf(getIndicator(rs, "MARKET_COMMODITY_IND")));
        item.setYear(getInteger(rs, "PROGRAM_YEAR"));
        item.setIsEligible(Boolean.valueOf(getIndicator(rs, "ELIGIBILITY_IND")));
        item.setCommodityTypeCode(getString(rs, "COMMODITY_TYPE_CODE"));
        item.setLineItem(getInteger(rs, "LINE_ITEM"));
        item.setMultiStageCommodityCode(getString(rs, "Multi_Stage_Commdty_Code"));
        item.setDefaultCropUnitCode(getString(rs, "DEFAULT_CROP_UNIT_CODE"));
        
        items.add(item);
      }
      
    } catch (SQLException e) {
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    
    ListView[] itemArray = new ListView[items.size()];
    items.toArray(itemArray);

    return itemArray;
  }
  
  
  /**
   * @param   transaction  transaction
   * @return  ListView[]
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public ListView[] getSectorDetailCodes(final Transaction transaction)
      throws DataAccessException {
    String qualifiedProcName = PACKAGE_NAME + "." + "GET_SECTOR_DETAIL_CODES";
    List<SectorDetailCodeListView> items = new ArrayList<>();
    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    final int paramCount = 0;
    
    try {
      proc = new DAOStoredProcedure(connection, qualifiedProcName, paramCount, true);
      
      proc.execute();
      rs = proc.getResultSet();
      
      while (rs.next()) {
        SectorDetailCodeListView item = new SectorDetailCodeListView();
        
        item.setSectorCode(getString(rs, "SECTOR_CODE"));
        item.setSectorCodeDescription(getString(rs, "SECTOR_CODE_DESCRIPTION"));
        item.setSectorDetailCode(getString(rs, "SECTOR_DETAIL_CODE"));
        item.setSectorDetailCodeDescription(getString(rs, "SECTOR_DETAIL_CODE_DESCRIPTION"));
        
        items.add(item);
      }
      
    } catch (SQLException e) {
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    
    ListView[] itemArray = new ListView[items.size()];
    items.toArray(itemArray);

    return itemArray;
  }
  
  
  public ListView[] getTriageQueueCodes(final Transaction transaction)
    throws DataAccessException {
    return getCodes(transaction, "GET_TRIAGE_QUEUE_CODES");
  }

  /**
   * Returns a UserListView containing pairs of
   * userId and accountName.
   * @param   transaction  transaction
   * 
   * @return  UserListView[]
   * 
   * @throws ServiceException 
   */
  @SuppressWarnings("resource")
  public UserListView[] getVerifiers(final Transaction transaction)
  throws ServiceException {
    
    String qualifiedProcName = PACKAGE_NAME + "." + "GET_VERIFIERS";
    List<UserListView> rows = new ArrayList<>();
    Connection connection = getConnection(transaction);
    CallableStatement cstmt = null;
    ResultSet resultSet = null;
    final int paramCount = 0;

    try {
      cstmt = prepareFunction(connection, qualifiedProcName, paramCount);
      cstmt.execute();
      resultSet = (ResultSet) cstmt.getObject(1);

      while (resultSet.next()) {
        User user = new UserVerifiedIndividualView();
        user.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        user.setId(resultSet.getLong("USER_ID"));
        UserListView ulw = new UserListView(user);
        rows.add(ulw);
      }

    } catch (SQLException e) {
      handleException(e);
    } finally {
      close(resultSet, cstmt);
    }

    return rows.toArray(new UserListView[rows.size()]);
  }

}
