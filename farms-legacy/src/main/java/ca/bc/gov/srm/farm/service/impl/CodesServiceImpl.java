/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.dao.CodesReadDAO;
import ca.bc.gov.srm.farm.dao.CodesWriteDAO;
import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.domain.codes.BPUYear;
import ca.bc.gov.srm.farm.domain.codes.Code;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCode;
import ca.bc.gov.srm.farm.domain.codes.ConfigurationParameter;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.DocumentTemplate;
import ca.bc.gov.srm.farm.domain.codes.ExpectedProduction;
import ca.bc.gov.srm.farm.domain.codes.FMV;
import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.domain.codes.FarmType3;
import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCode;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemCode;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemDetail;
import ca.bc.gov.srm.farm.domain.codes.InventoryXref;
import ca.bc.gov.srm.farm.domain.codes.LineItemCode;
import ca.bc.gov.srm.farm.domain.codes.MarketRatePremium;
import ca.bc.gov.srm.farm.domain.codes.MunicipalityCode;
import ca.bc.gov.srm.farm.domain.codes.SectorCode;
import ca.bc.gov.srm.farm.domain.codes.SectorDetailCode;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCode;
import ca.bc.gov.srm.farm.domain.codes.TipBenchmarkInfo;
import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.domain.codes.TipLineItem;
import ca.bc.gov.srm.farm.domain.codes.YearConfigurationParameter;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

/**
 * @author awilkinson
 */
public class CodesServiceImpl extends BaseService implements CodesService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final String CSV_SUFFIX = ".csv";

  private static final String EXPORT_FMV_CSV_FILE = "farm_export_fmv_";
  private static final String EXPORT_MISSING_FMV_CSV_FILE = "farm_export_missing_fmv_";
  private static final String EXPORT_BPU_CSV_FILE = "farm_export_bpu_";
  private static final String EXPORT_MISSING_BPU_CSV_FILE = "farm_export_missing_bpu_";


  /**
   * @param codeTable The name of the code table.
   * @return list of codes from the requested code table
   * @throws ServiceException On Exception
   */
  @Override
  public List<Code> getCodes(
      final String codeTable) throws ServiceException {
    return getCodes(codeTable, null);
  }

  /**
   * @param codeTable The name of the code table.
   * @param code code
   * @return Code from the requested code table
   * @throws ServiceException On Exception
   */
  @Override
  public Code getCode(
      final String codeTable,
      final String code) throws ServiceException {

    if (codeTable == null || code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Code result = null;
    List<Code> codes = getCodes(codeTable, code);
    if( ! codes.isEmpty() ) {
      result = codes.get(0);
    }
    return result;
  }

  /**
   * @param codeTable The name of the code table.
   * @param code code
   * @return list of codes from the requested code table
   * @throws ServiceException On Exception
   */
  private List<Code> getCodes(
      final String codeTable,
      final String code) throws ServiceException {

    if (codeTable == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    List<Code> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      codes = dao.readGenericCodes(transaction, codeTable, code);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return codes;
  }


  /**
   * @param codeTable codeTable
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createCode(
      final String codeTable,
      final Code code,
      final String user)
  throws ServiceException {

    if (codeTable == null || code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createGenericCode(transaction, codeTable, code, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param codeTable codeTable
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateCode(
      final String codeTable,
      final Code code,
      final String user)
  throws ServiceException {
    
    if (codeTable == null || code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateGenericCode(transaction, codeTable, code, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param codeTable codeTable
   * @param code code
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean isCodeInUse(
      final String codeTable,
      final String code)
  throws ServiceException {
    
    if (codeTable == null || code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    boolean result = false;
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      result = dao.isGenericCodeInUse(transaction, codeTable, code);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }
  
  
  /**
   * @param codeTable codeTable
   * @param code code
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteCode(
      final String codeTable,
      final String code,
      final Integer revisionCount)
  throws ServiceException {
    
    if (codeTable == null || code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.deleteGenericCode(transaction, codeTable, code, revisionCount);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param year The program year.
   * @param lineItem The line item number.
   * @return LineItemCode for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public LineItemCode getLineItem(
      final Integer year,
      final Integer lineItem) throws ServiceException {

    if (year == null || lineItem == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    LineItemCode result = null;
    List<LineItemCode> lineItems = getLineItems(year, lineItem);
    if( ! lineItems.isEmpty() ) {
      result = lineItems.get(0);
    }

    return result;
  }
  
  /**
   * @param year The program year.
   * @return list of line items for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public List<LineItemCode> getLineItems(final Integer year) throws ServiceException {
    return getLineItems(year, null);
  }
  
  /**
   * @param year The program year.
   * @param lineItem The line item number.
   * @return list of line items for the requested year.
   * @throws ServiceException On Exception
   */
  private List<LineItemCode> getLineItems(
      final Integer year,
      final Integer lineItem) throws ServiceException {

    if (year == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    List<LineItemCode> lineItems = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      lineItems = dao.readLineItems(transaction, year, lineItem);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return lineItems;
  }


  /**
   * @param lineItem lineItem
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createLineItem(
      final LineItemCode lineItem,
      final String user)
  throws ServiceException {

    if (lineItem == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createLineItem(transaction, lineItem, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param lineItem lineItem
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateLineItem(
      final LineItemCode lineItem,
      final String user)
  throws ServiceException {
   
    if (lineItem == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateLineItem(transaction, lineItem, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param year The program year.
   * @param lineItem The line item number.
   * @return true if this line item is in use in a scenario
   * @throws ServiceException On Exception
   */
  @Override
  public boolean isLineItemInUse(
      final Integer year,
      final Integer lineItem) throws ServiceException {
    
    boolean result = false;
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      
      result = dao.isLineItemInUse(transaction, year, lineItem);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }


  /**
   * @param lineItemId lineItemId
   * @param year year
   * @param lineItem lineItem
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteLineItem(
      final Integer lineItemId,
      final Integer year,
      final Integer lineItem,
      final Integer revisionCount)
  throws ServiceException {

    if (lineItemId == null || lineItem == null
        || year == null || revisionCount == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.deleteLineItem(transaction, lineItemId, year, lineItem, revisionCount);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param toYear toYear
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void copyYearLineItems(
      final Integer toYear,
      final String user)
  throws ServiceException {
    
    if (toYear == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.copyYearLineItems(transaction, toYear, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param year The program year.
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @return FMV for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public FMV getFMV(
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode) throws ServiceException {

    if (year == null
        || inventoryItemCode == null
        || municipalityCode == null
        || cropUnitCode == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    FMV result = null;
    List<FMV> fmvs =
      getFMVs(year,
          inventoryItemCode,
          municipalityCode,
          cropUnitCode);
    if( ! fmvs.isEmpty() ) {
      result = fmvs.get(0);
    }

    return result;
  }
  
  /**
   * @param year The program year.
   * @return list of line items for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public List<FMV> getFMVs(final Integer year) throws ServiceException {
    return getFMVs(year, null, null, null);
  }
  
  /**
   * @param year The program year.
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @return list of FMVs for the requested year.
   * @throws ServiceException On Exception
   */
  private List<FMV> getFMVs(
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode) throws ServiceException {

    if (year == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    List<FMV> fmvs = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      fmvs =
        dao.readFMVs(transaction,
            year,
            inventoryItemCode,
            municipalityCode,
            cropUnitCode);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return fmvs;
  }


  /**
   * @param fmv fmv
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createFMV(
      final FMV fmv,
      final String user)
  throws ServiceException {

    if (fmv == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createFMV(transaction, fmv, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param fmv fmv
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateFMV(
      final FMV fmv,
      final String user)
  throws ServiceException {
   
    if (fmv == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateFMV(transaction, fmv, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param year The program year.
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @return true if this line item is in use in a scenario
   * @throws ServiceException On Exception
   */
  @Override
  public boolean isFMVInUse(
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode) throws ServiceException {
    
    boolean result = false;
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      
      result =
        dao.isFMVInUse(transaction,
            year,
            inventoryItemCode,
            municipalityCode,
            cropUnitCode);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }


  /**
   * @param year The program year.
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteFMV(
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode)
  throws ServiceException {

    if (year == null
        || inventoryItemCode == null
        || municipalityCode == null
        || cropUnitCode == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.deleteFMV(transaction,
          year,
          inventoryItemCode,
          municipalityCode,
          cropUnitCode);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param year The program year.
   * @param inventoryItemCodeFilter inventoryItemCodeFilter
   * @param inventoryItemDescriptionFilter inventoryItemDescriptionFilter
   * @param municipalityDescriptionFilter municipalityDescriptionFilter
   * @param cropUnitDescriptionFilter cropUnitDescriptionFilter
   * @return File containing FMVs as a comma delimited lists for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public File exportFmvCsv(
      final Integer year,
      final String inventoryItemCodeFilter,
      final String inventoryItemDescriptionFilter,
      final String municipalityDescriptionFilter,
      final String cropUnitDescriptionFilter) throws ServiceException {
    
    if (year == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }

    File outFile = null;
    
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
    
    try {
      transaction = openTransaction();
      
      String fileName = EXPORT_FMV_CSV_FILE;
      String fileNameSuffix = CSV_SUFFIX;
      String headerLine = "PROGRAM_YEAR,PERIOD,MUNICIPALITY_CODE,INVENTORY_ITEM_CODE"
          + ",CROP_UNIT_CODE,AVERAGE_PRICE,PERCENT_VARIANCE";
      
      List<String> lines =
          dao.exportFMVs(transaction,
              year,
              inventoryItemCodeFilter,
              inventoryItemDescriptionFilter,
              municipalityDescriptionFilter,
              cropUnitDescriptionFilter);

      try {
        
        outFile = IOUtils.writeTempFile(fileName, fileNameSuffix, headerLine, lines);

      } catch (IOException e) {
        throw new ServiceException("", e);
      }
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return outFile;
  }
  
  
  /**
   * @param year The program year.
   * @return File containing FMVs as a comma delimited lists for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public File exportMissingFmvCsv(
      final Integer year) throws ServiceException {
    
    if (year == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    File outFile = null;
    
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
    
    try {
      transaction = openTransaction();
      
      String fileName = EXPORT_MISSING_FMV_CSV_FILE;
      String fileNameSuffix = CSV_SUFFIX;
      String headerLine = "PROGRAM_YEAR,MUNICIPALITY_CODE,INVENTORY_ITEM_CODE,CROP_UNIT_CODE";
      
      List<String> lines = dao.exportMissingFMVs(transaction, year);
      
      try {
        
        outFile = IOUtils.writeTempFile(fileName, fileNameSuffix, headerLine, lines);
        
      } catch (IOException e) {
        throw new ServiceException("", e);
      }
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return outFile;
  }


  /**
   * @return list of type MunicipalityCode
   * @throws ServiceException On Exception
   */
  @Override
  public List<MunicipalityCode> getMunicipalityCodes() throws ServiceException {
    return getMunicipalityCodes(null);
  }

  /**
   * @param code code
   * @return MunicipalityCode
   * @throws ServiceException On Exception
   */
  @Override
  public MunicipalityCode getMunicipalityCode(
      final String code) throws ServiceException {
    MunicipalityCode result = null;
    List<MunicipalityCode> codes = getMunicipalityCodes(code);
    if( ! codes.isEmpty() ) {
      result = codes.get(0);
    }
    return result;
  }

  /**
   * @param code code
   * @return list of codes from the requested code table
   * @throws ServiceException On Exception
   */
  private List<MunicipalityCode> getMunicipalityCodes(
      final String code) throws ServiceException {

    List<MunicipalityCode> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      codes = dao.readMunicipalityCodes(transaction, code);
      
      Map<String, List<String>> muniOfficeCodes = dao.readMunicipalityOfficeCodes(transaction, code);
      
      for(MunicipalityCode m : codes) {
        List<String> officeCodes = muniOfficeCodes.get(m.getCode());
        if(officeCodes == null) {
          officeCodes = new ArrayList<>();
        }
        m.setRegionalOfficeCodes(officeCodes);
      }

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return codes;
  }


  /**
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createMunicipalityCode(
      final MunicipalityCode code,
      final String user)
  throws ServiceException {

    if (code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createMunicipalityCode(transaction, code, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateMunicipalityCode(
      final MunicipalityCode code,
      final String user)
  throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateMunicipalityCode(transaction, code, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param code code
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean isMunicipalityCodeInUse(
      final String code)
  throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    boolean result = false;
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      result = dao.isMunicipalityCodeInUse(transaction, code);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }
  
  
  /**
   * @param code code
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteMunicipalityCode(
      final String code,
      final Integer revisionCount)
  throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.deleteMunicipalityCode(transaction, code, revisionCount);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  
  
  /**
   * @param year The program year.
   * @return list of BPUs for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public List<BPU> getBPUs(final Integer year) throws ServiceException {
    List<BPU> bpus = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      bpus = dao.readBPUs(transaction, year);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return bpus;
  }
  
  
  /**
   * @param bpu bpu
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createBPU(final BPU bpu, final String user) throws ServiceException {
    if (bpu == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createBPU(transaction, bpu, user);
      dao.createBPUYears(transaction, bpu, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param yearsToUpdate yearsToUpdate
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateBPUYears(final List<BPUYear> yearsToUpdate, final String user) 
  throws ServiceException {
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      for(BPUYear year : yearsToUpdate) {
      	dao.updateBPUYear(transaction, year, user);
      }
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param bpuId bpuId
   * @return true if the BPU is needed by a scenario
   * @throws ServiceException On Exception
   */
  @Override
  public boolean isBPUInUse(final Integer bpuId) throws ServiceException {
    boolean result = false;
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      result = dao.isBPUInUse(transaction, bpuId);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }


  /**
   * @param bpuId bpuId
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteBPU(final Integer bpuId) throws ServiceException {
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.deleteBPU(transaction, bpuId);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param year The program year.
   * @param inventoryItemCodeFilter inventoryItemCodeFilter
   * @param inventoryItemDescriptionFilter inventoryItemDescriptionFilter
   * @param municipalityDescriptionFilter municipalityDescriptionFilter
   * @return File containing BPUs as a comma delimited lists for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public File exportBpuCsv(
      final Integer year,
      final String inventoryItemCodeFilter,
      final String inventoryItemDescriptionFilter,
      final String municipalityDescriptionFilter) throws ServiceException {
    
    if (year == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    File outFile = null;
    
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
    
    try {
      transaction = openTransaction();
      
      String fileName = EXPORT_BPU_CSV_FILE;
      String fileNameSuffix = CSV_SUFFIX;
      String headerLine = "YEAR,MUNICIPALITY,INVENTORY_CODE,UNIT_DESCRIPTION"
          + ",YEAR_MINUS_6_MARGIN,YEAR_MINUS_5_MARGIN,YEAR_MINUS_4_MARGIN"
          + ",YEAR_MINUS_3_MARGIN,YEAR_MINUS_2_MARGIN,YEAR_MINUS_1_MARGIN"
          + ",YEAR_MINUS_6_EXPENSE,YEAR_MINUS_5_EXPENSE,YEAR_MINUS_4_EXPENSE"
          + ",YEAR_MINUS_3_EXPENSE,YEAR_MINUS_2_EXPENSE,YEAR_MINUS_1_EXPENSE";
      
      List<String> lines =
          dao.exportBPUs(transaction,
              year,
              inventoryItemCodeFilter,
              inventoryItemDescriptionFilter,
              municipalityDescriptionFilter);
      
      try {
        
        outFile = IOUtils.writeTempFile(fileName, fileNameSuffix, headerLine, lines);
        
      } catch (IOException e) {
        throw new ServiceException("", e);
      }
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return outFile;
  }
  
  
  /**
   * @param year The program year.
   * @return File containing BPUs as a comma delimited lists for the requested year.
   * @throws ServiceException On Exception
   */
  @Override
  public File exportMissingBpuCsv(
      final Integer year) throws ServiceException {
    
    if (year == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    File outFile = null;
    
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
    
    try {
      transaction = openTransaction();
      
      String fileName = EXPORT_MISSING_BPU_CSV_FILE;
      String fileNameSuffix = CSV_SUFFIX;
      String headerLine = "YEAR,INVENTORY_CODE,UNIT_DESCRIPTION,MUNICIPALITY,REFERENCE_YEAR,AFFECTED_PIN_COUNT";
      
      List<String> lines = dao.exportMissingBPUs(transaction, year);
      
      try {
        
        outFile = IOUtils.writeTempFile(fileName, fileNameSuffix, headerLine, lines);
        
      } catch (IOException e) {
        throw new ServiceException("", e);
      }
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return outFile;
  }



  /**
   * @return list of type InventoryItemCode
   * @throws ServiceException On Exception
   */
  @Override
  public List<InventoryItemCode> getInventoryItemCodes() throws ServiceException {
    return getInventoryItemCodes(null);
  }

  /**
   * @param code code
   * @return InventoryItemCode
   * @throws ServiceException On Exception
   */
  @Override
  public InventoryItemCode getInventoryItemCode(
      final String code) throws ServiceException {
    InventoryItemCode result = null;
    List<InventoryItemCode> codes = getInventoryItemCodes(code);
    if( ! codes.isEmpty() ) {
      result = codes.get(0);
    }
    return result;
  }

  /**
   * @param code code
   * @return list of type InventoryItemCode
   * @throws ServiceException On Exception
   */
  private List<InventoryItemCode> getInventoryItemCodes(
      final String code) throws ServiceException {

    List<InventoryItemCode> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      codes = dao.readInventoryItemCodes(transaction, code);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return codes;
  }


  /**
   * @param code code
   * @param detailsList detailsList
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createInventoryItemCode(
      final InventoryItemCode code,
      final List<InventoryItemDetail> detailsList,
      final String user)
  throws ServiceException {

    if (code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createInventoryItemCode(transaction, code, user);
      
      dao.createInventoryItemDetails(transaction, detailsList, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param code code
   * @param detailsAddList detailsAddList
   * @param detailsUpdateList detailsUpdateList
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateInventoryItemCode(
      final InventoryItemCode code,
      final List<InventoryItemDetail> detailsAddList,
      final List<InventoryItemDetail> detailsUpdateList,
      final String user)
  throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateInventoryItemCode(transaction, code, user);
      
      if(!detailsAddList.isEmpty()) {
        dao.createInventoryItemDetails(transaction, detailsAddList, user);
      }
      if(!detailsUpdateList.isEmpty()) {
        dao.updateInventoryItemDetails(transaction, detailsUpdateList, user);
      }
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param code code
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean isInventoryItemCodeInUse(
      final String code)
  throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    boolean result = false;
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      result = dao.isInventoryItemCodeInUse(transaction, code);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }
  
  
  
  /**
   * @param code code
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteInventoryItemCode(
      final String code,
      final Integer revisionCount)
  throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.deleteInventoryItemCode(transaction, code, revisionCount);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }



  /**
   * @param commodityXrefId commodityXrefId
   * @return List<InventoryXref>
   * @throws ServiceException On Exception
   */
  @Override
  public InventoryXref getInventoryXref(
      final Integer commodityXrefId) throws ServiceException {

    if (commodityXrefId == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    InventoryXref result = null;
    List<InventoryXref> xrefs = getInventoryXrefs(null, null, commodityXrefId);
    if( ! xrefs.isEmpty() ) {
      result = xrefs.get(0);
    }

    return result;
  }
  
  /**
   * @param inventoryClassCode inventoryClassCode
   * @param inventoryItemCode inventoryItemCode
   * @return InventoryXref
   * @throws ServiceException On Exception
   */
  @Override
  public InventoryXref getInventoryXref(
      final String inventoryClassCode,
      final String inventoryItemCode) throws ServiceException {

    if (inventoryClassCode == null || inventoryItemCode == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    InventoryXref result = null;
    List<InventoryXref> xrefs = getInventoryXrefs(inventoryClassCode, inventoryItemCode, null);
    if( ! xrefs.isEmpty() ) {
      result = xrefs.get(0);
    }

    return result;
  }
  
  /**
   * @param inventoryClassCode inventoryClassCode
   * @return List<InventoryXref>
   * @throws ServiceException On Exception
   */
  @Override
  public List<InventoryXref> getInventoryXrefs(final String inventoryClassCode) throws ServiceException {

    if (inventoryClassCode == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    return getInventoryXrefs(inventoryClassCode, null, null);
  }
  
  /**
   * @param inventoryClassCode inventoryClassCode
   * @param inventoryItemCode inventoryItemCode
   * @param commodityXrefId commodityXrefId
   * @return List<InventoryXref>
   * @throws ServiceException On Exception
   */
  private List<InventoryXref> getInventoryXrefs(
      final String inventoryClassCode,
      final String inventoryItemCode,
      final Integer commodityXrefId) throws ServiceException {

    // must have either inventoryClassCode or commodityXrefId
    if (inventoryClassCode == null && commodityXrefId == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    List<InventoryXref> xrefs = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      xrefs = dao.readInventoryXrefs(transaction,
          inventoryClassCode,
          inventoryItemCode,
          commodityXrefId);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return xrefs;
  }


  /**
   * @param xref xref
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createInventoryXref(
      final InventoryXref xref,
      final String user)
  throws ServiceException {

    if (xref == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createInventoryXref(transaction, xref, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param xref xref
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateInventoryXref(
      final InventoryXref xref,
      final String user)
  throws ServiceException {
   
    if (xref == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateInventoryXref(transaction, xref, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param commodityXrefId commodityXrefId
   * @return true if this inventory xref is in use in a scenario
   * @throws ServiceException On Exception
   */
  @Override
  public boolean isInventoryXrefInUse(
      final Integer commodityXrefId) throws ServiceException {
    
    boolean result = false;
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      
      result = dao.isInventoryXrefInUse(transaction, commodityXrefId);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }


  /**
   * @param commodityXrefId commodityXrefId
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteInventoryXref(
      final Integer commodityXrefId,
      final Integer revisionCount)
  throws ServiceException {

    if (commodityXrefId == null || revisionCount == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.deleteInventoryXref(transaction, commodityXrefId, revisionCount);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @return list of type MarketRatePremium
   * @throws ServiceException On Exception
   */
  @Override
  public List<MarketRatePremium> getMarketRatePremiums() throws ServiceException {
    return getMarketRatePremiums(null);
  }

  /**
   * @param id id
   * @return MarketRatePremium
   * @throws ServiceException On Exception
   */
  @Override
  public MarketRatePremium getMarketRatePremium(
      final Long id) throws ServiceException {
    MarketRatePremium result = null;
    List<MarketRatePremium> mrps = getMarketRatePremiums(id);
    if( ! mrps.isEmpty() ) {
      result = mrps.get(0);
    }
    return result;
  }

  /**
   * @param id id
   * @return list of type MarketRatePremium
   * @throws ServiceException On Exception
   */
  private List<MarketRatePremium> getMarketRatePremiums(
      final Long id) throws ServiceException {

    List<MarketRatePremium> mrps = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      mrps = dao.readMarketRatePremiums(transaction, id);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return mrps;
  }

  /**
   * @param mrp mrp
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createMarketRatePremium(
      final MarketRatePremium mrp,
      final String user)
  throws ServiceException {

    if (mrp == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createMarketRatePremium(transaction, mrp, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  /**
   * @param mrp mrp
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateMarketRatePremium(
      final MarketRatePremium mrp,
      final String user)
  throws ServiceException {

    if (mrp == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateMarketRatePremium(transaction, mrp, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  /**
   * @param id id
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteMarketRatePremium(
      final Long id,
      final Integer revisionCount)
  throws ServiceException {

    if (id == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.deleteMarketRatePremium(transaction, id, revisionCount);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @return list of type StructureGroupCode
   * @throws ServiceException On Exception
   */
  @Override
  public List<StructureGroupCode> getStructureGroupCodes() throws ServiceException {
    return getStructureGroupCodes(null);
  }

  /**
   * @param code
   * @return StructureGroupCode
   * @throws ServiceException
   */
  @Override
  public StructureGroupCode getStructureGroupCode(
      final String code) throws ServiceException {
    StructureGroupCode result = null;
    List<StructureGroupCode> codes = getStructureGroupCodes(code);
    if( ! codes.isEmpty() ) {
      result = codes.get(0);
    }
    return result;
  }

  /**
   * @param code code
   * @return list of type StructureGroupCode
   * @throws ServiceException On Exception
   */
  private List<StructureGroupCode> getStructureGroupCodes(
      final String code) throws ServiceException {

    List<StructureGroupCode> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      codes = dao.readStructureGroupCodes(transaction, code);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return codes;
  }

  /**
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createStructureGroupCode(
      final StructureGroupCode code,
      final String user)
  throws ServiceException {

    if (code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createStructureGroupCode(transaction, code, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateStructureGroupCode(
      final StructureGroupCode code,
      final String user)
  throws ServiceException {

    if (code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateStructureGroupCode(transaction, code, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param code code
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean isStructureGroupCodeInUse(
      final String code)
  throws ServiceException {

    if (code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    boolean result = false;

    try {
      transaction = openTransaction();
      transaction.begin();

      result = dao.isStructureGroupCodeInUse(transaction, code);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return result;
  }


  /**
   * @param code code
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteStructureGroupCode(
      final String code,
      final Integer revisionCount)
  throws ServiceException {

    if (code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.deleteStructureGroupCode(transaction, code, revisionCount);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @return List<Integer>
   * @throws ServiceException On Exception
   */
 @Override
public List<Integer> getProgramYears() throws ServiceException {
    
    List<Integer> programYears = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      programYears = dao.readProgramYears(transaction);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return programYears;
  }


 @Override
 public CropUnitConversion getCropUnitConversion(
     final String inventoryItemCode) throws ServiceException {

   if (inventoryItemCode == null) {
     logger.debug("Required object(s) are null.");

     throw new ServiceException("A required object is null");
   }
   
   CropUnitConversion result = null;
   List<CropUnitConversion> cropUnitConversions = getCropUnitConversions(inventoryItemCode);
   if( ! cropUnitConversions.isEmpty() ) {
     result = cropUnitConversions.get(0);
   }

   return result;
 }
 
 @Override
 public List<CropUnitConversion> getCropUnitConversions() throws ServiceException {
   return getCropUnitConversions(null);
 }
 
 private List<CropUnitConversion> getCropUnitConversions(
     final String inventoryItemCode) throws ServiceException {

   List<CropUnitConversion> cropUnitConversions = null;

   Transaction transaction = null;
   CodesReadDAO dao = new CodesReadDAO();

   try {
     transaction = openTransaction();

     cropUnitConversions = dao.readCropUnitConversions(transaction, inventoryItemCode);

   } catch(ServiceException se) {
     throw se;
   } catch (Exception e) {
     throw new ServiceException(e);
   } finally {
     closeTransaction(transaction);
   }
   
   return cropUnitConversions;
 }
 
 
 @Override
 public void createCropUnitConversion(
     final CropUnitConversion cropUnitConversion,
     final String user)
 throws ServiceException {

   if (cropUnitConversion == null) {
     logger.debug("Required object(s) are null.");

     throw new ServiceException("A required object is null");
   }
   
   Integer programYear = ProgramYearUtils.getCurrentProgramYear();

   Transaction transaction = null;
   CodesWriteDAO dao = new CodesWriteDAO();

   try {
     transaction = openTransaction();
     transaction.begin();

     dao.createCropUnitDefault(transaction, cropUnitConversion, user);
     dao.createCropUnitConversionItems(transaction, cropUnitConversion, user);
     dao.recalculateFmvs(transaction, programYear, cropUnitConversion.getInventoryItemCode(), user);

     transaction.commit();
   } catch(ServiceException se) {
     rollback(transaction);
     throw se;
   } catch (Exception e) {
     e.printStackTrace();
     logger.error("Unexpected error: ", e);
     rollback(transaction);
     throw new ServiceException(e);
   } finally {
     closeTransaction(transaction);
   }
 }
 
 
 @Override
 public void updateCropUnitConversion(
     final CropUnitConversion cropUnitConversion,
     final String user)
         throws ServiceException {
   
   if (cropUnitConversion == null) {
     logger.debug("Required object(s) are null.");
     
     throw new ServiceException("A required object is null");
   }
   
   Integer programYear = ProgramYearUtils.getCurrentProgramYear();

   Transaction transaction = null;
   CodesWriteDAO dao = new CodesWriteDAO();
   
   try {
     transaction = openTransaction();
     transaction.begin();
     
     dao.updateCropUnitDefault(transaction, cropUnitConversion, user);
     dao.deleteCropUnitConversionItems(transaction, cropUnitConversion.getInventoryItemCode(), null);
     dao.createCropUnitConversionItems(transaction, cropUnitConversion, user);
     dao.recalculateFmvs(transaction, programYear, cropUnitConversion.getInventoryItemCode(), user);
     
     transaction.commit();
   } catch(ServiceException se) {
     rollback(transaction);
     throw se;
   } catch (Exception e) {
     e.printStackTrace();
     logger.error("Unexpected error: ", e);
     rollback(transaction);
     throw new ServiceException(e);
   } finally {
     closeTransaction(transaction);
   }
 }


 @Override
 public void deleteCropUnitConversion(
     final String inventoryItemCode)
 throws ServiceException {

   if (inventoryItemCode == null) {
     logger.debug("Required object(s) are null.");
     
     throw new ServiceException("A required object is null");
   }

   Transaction transaction = null;
   CodesWriteDAO dao = new CodesWriteDAO();

   try {
     transaction = openTransaction();
     transaction.begin();

     dao.deleteCropUnitConversionItems(transaction, inventoryItemCode, null);
     dao.deleteCropUnitDefault(transaction, inventoryItemCode);

     transaction.commit();
   } catch(ServiceException se) {
     rollback(transaction);
     throw se;
   } catch (Exception e) {
     e.printStackTrace();
     logger.error("Unexpected error: ", e);
     rollback(transaction);
     throw new ServiceException(e);
   } finally {
     closeTransaction(transaction);
   }
 }
 
 
  @Override
  public List<FruitVegTypeCode> getFruitVegCodes() throws ServiceException {
     List<FruitVegTypeCode> codes = null;
  
     Transaction transaction = null;
     CodesReadDAO dao = new CodesReadDAO();
  
     try {
       transaction = openTransaction();
       codes = dao.readFruitVegCodes(transaction);
       
     } catch(ServiceException se) {
       throw se;
     } catch (Exception e) {
       throw new ServiceException(e);
     } finally {
       closeTransaction(transaction);
     }
     
     return codes;
  }
         
  @Override
  public FruitVegTypeCode getFruitVegCode(String fruitVegCodeName) throws ServiceException {
    FruitVegTypeCode result = null;
    List<FruitVegTypeCode> fruitVegCodes = getFruitVegCodes();
    for (FruitVegTypeCode fruitVegTypeCode : fruitVegCodes) {
     if(fruitVegTypeCode.getName().equals(fruitVegCodeName)) {
       result = fruitVegTypeCode;
       break;
     }
   }
    return result;
//  TODO use Lambda expression after upgrading to Java 8
//    return fruitVegCodes.stream().filter(fruitVegCode -> fruitVegCode.getName().equals(fruitVegCodeName)).findFirst().orElse(null);
 }
   
  @Override
  public void updateFruitVegCode(final FruitVegTypeCode fruitVegCode, final String user) throws ServiceException {
    if (fruitVegCode == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateFruitVegCode(transaction, fruitVegCode, user);
      transaction.commit();
       
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }     
  }
   
  @Override
  public void createFruitVegCode(final FruitVegTypeCode fruitVegCode, final String user) throws ServiceException {
    if (fruitVegCode == null || user == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
     
      dao.createFruitVegCode(transaction, fruitVegCode, user);
      transaction.commit();
       
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
   
  @Override
  public boolean checkFruitVegCodeInUse(final FruitVegTypeCode fruitVegCode) throws ServiceException {
    boolean inUse = false;
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      inUse = dao.checkFruitVegCodeInUse(fruitVegCode, transaction);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return inUse;
  }
   
  @Override
  public void deleteFruitVegCode(final FruitVegTypeCode fruitVegCode) throws ServiceException {
    if (fruitVegCode == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.deleteFruitVegCode(transaction, fruitVegCode);
      transaction.commit();
       
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  @Override
  public List<CodeListView> getFruitVegListItems() throws ServiceException {
    List<FruitVegTypeCode> fruitVegCodes = getFruitVegCodes();
    List<CodeListView> fruitVegCodeListItems = new ArrayList<>();
     
    for (FruitVegTypeCode fruitVegCode : fruitVegCodes) {
      CodeListView fruitVegCodeListView = new CodeListView(fruitVegCode.getName(), fruitVegCode.getDescription());
      fruitVegCodeListItems.add(fruitVegCodeListView);
    }
     
    return fruitVegCodeListItems;
  }
   
  @Override
  public List<CommodityTypeCode> getCommodityTypeCodes() throws ServiceException {
    List<CommodityTypeCode> codes = null;
  
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
  
    try {
      transaction = openTransaction();
      codes = dao.readCommodityTypeCodes(transaction);
       
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return codes;
  }
  
  @Override
  public List<CodeListView> getCommodityTypeListItems() throws ServiceException {
    List<CommodityTypeCode> commodityTypeCodes = getCommodityTypeCodes();
    List<CodeListView> commodityTypeCodeListItems = new ArrayList<>();
     
    for (CommodityTypeCode commodityTypeCode : commodityTypeCodes) {
      CodeListView commodityTypeCodeListView = new CodeListView(commodityTypeCode.getCode(), commodityTypeCode.getDescription());
      commodityTypeCodeListItems.add(commodityTypeCodeListView);
    }
     
    return commodityTypeCodeListItems;
  }

  @Override
  public List<ExpectedProduction> getExpectedProductionItems() throws ServiceException {
    return getExpectedProductionItems(null);
  }
   
  private List<ExpectedProduction> getExpectedProductionItems(Integer id) throws ServiceException {
    List<ExpectedProduction> codes = null;
     
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
     
    try {
      transaction = openTransaction();
      codes = dao.readExpectedProductionItems(transaction, id);
       
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return codes;
  }

  @Override
  public ExpectedProduction getExpectedProductionItem(Integer id) throws ServiceException {
    ExpectedProduction code = null;
  
    List<ExpectedProduction> expectedProductionItems = getExpectedProductionItems(id);
    if(!expectedProductionItems.isEmpty()) {
      code = expectedProductionItems.get(0);
    }
     
    return code;     
  }
   
  @Override
  public void updateExpectedProductionItem(final ExpectedProduction expectedProduction, final String user) throws ServiceException {
    if (expectedProduction == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateExpectedProductionItem(transaction, expectedProduction, user);
      transaction.commit();
       
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }     
  }
   
   @Override
  public void createExpectedProductionItem(final ExpectedProduction expectedProduction, final String user) throws ServiceException {
    if (expectedProduction == null || user == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
     
      dao.createExectedProductionItem(transaction, expectedProduction, user);
      transaction.commit();
       
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
   
  
  @Override
  public boolean checkExpectedProductionItemExists(final ExpectedProduction expectedProduction) throws ServiceException {
    boolean inUse = false;
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      inUse = dao.checkExpectedProductionItemExists(expectedProduction, transaction);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return inUse;
  }
   
   @Override
  public boolean checkLineItemExistsForProgramYear(final Integer programYear, final Integer lineItem) throws ServiceException {
    boolean exists = false;
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      exists = dao.checkLineItemExistsForProgramYear(programYear, lineItem, transaction);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return exists;
  }
   
  @Override
  public void deleteExpectedProductionItem(final ExpectedProduction expectedProduction) throws ServiceException {
    if (expectedProduction == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.deleteExpectedProductionItem(transaction, expectedProduction);
      transaction.commit();
       
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }     
  }

  
  @Override
  public List<ConfigurationParameter> getConfigurationParameters() throws ServiceException {
    List<ConfigurationParameter> parameters = null;
     
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
  
    try {
      transaction = openTransaction();
      parameters = dao.readConfigurationParameters(transaction);
       
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return parameters;
  }
  
  
  @Override
  public ConfigurationParameter getConfigurationParameter(String name) throws ServiceException {
    ConfigurationParameter parameter = null;
    
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
    
    try {
      transaction = openTransaction();
      parameter = dao.readConfigurationParameter(transaction, null, name);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return parameter;     
  }
  
  
  @Override
  public ConfigurationParameter getConfigurationParameter(Integer id) throws ServiceException {
    ConfigurationParameter parameter = null;
     
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
  
    try {
      transaction = openTransaction();
      parameter = dao.readConfigurationParameter(transaction, id, null);
       
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return parameter;     
  }
  
   
  @Override
  public void updateConfigParam(final ConfigurationParameter configParam, final String user) throws ServiceException {
    if (configParam == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateConfigurationParameter(transaction, configParam, user);
      transaction.commit();
       
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    ServiceFactory.getConfigurationService().loadConfigurationParameters();
  }   

 
 
  @Override
  public List<YearConfigurationParameter> getYearConfigurationParameters(Integer programYear) throws ServiceException {
    List<YearConfigurationParameter> parameters = null;
    
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
    
    try {
      transaction = openTransaction();
      parameters = dao.readYearConfigurationParams(transaction, programYear);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return parameters;
  }
  
  
  @Override
  public YearConfigurationParameter getYearConfigurationParameter(Integer id) throws ServiceException {
    YearConfigurationParameter parameter = null;
    
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
    
    try {
      transaction = openTransaction();
      parameter = dao.readYearConfigurationParam(transaction, id);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return parameter;     
  }
  
  
  @Override
  public void updateConfigParam(YearConfigurationParameter configParam, String user) throws ServiceException {
    if (configParam == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateYearConfigurationParam(transaction, configParam, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    ServiceFactory.getConfigurationService().loadYearConfigurationParameters();
  }   
  
  
  
  @Override
  public final Integer createFarmType3(
      final FarmType3 farmType, 
      final String user) throws ServiceException {    

    if (farmType == null || user == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    Integer id = null;

    try {
      transaction = openTransaction();
      transaction.begin();
    
      id = dao.createFarmType3(transaction, farmType, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return id;
  }
    
  @Override
  public void createFarmType3IncomeRange(
      final List<TipFarmTypeIncomeRange> ranges, 
      final String user,
      final Integer id) throws ServiceException {

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
    
      dao.createIncomeRange(transaction, ranges, id, null, null, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public void createFarmType2IncomeRange(
      final List<TipFarmTypeIncomeRange> ranges, 
      final String user,
      final Integer id) throws ServiceException {

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
    
      dao.createIncomeRange(transaction, ranges, null, id, null, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public void createFarmType1IncomeRange(
      final List<TipFarmTypeIncomeRange> ranges, 
      final String user,
      final Integer id) throws ServiceException {

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
    
      dao.createIncomeRange(transaction, ranges, null, null, id, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public void deleteFarmTypeDefaultIncomeRange() throws ServiceException {

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
    
      dao.deleteDefaultFarmTypeIncomeRange(transaction);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public void createFarmTypeDefaultIncomeRange(
      final List<TipFarmTypeIncomeRange> ranges, 
      final String user) throws ServiceException {

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
    
      dao.createIncomeRange(transaction, ranges, null, null, null, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
 
  @Override
  public List<FarmType3> getFarmType3s() throws ServiceException {
    List<FarmType3> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      codes = dao.readFarmType3Codes(transaction);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return codes;
 }
  
  @Override
  public void updateFarmType3(final FarmType3 farmType, String user) throws ServiceException {
    if (farmType == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateFarmType3(transaction, farmType, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public void deleteFarmType3(final FarmType3 farmType) throws ServiceException {
    if (farmType == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.deleteFarmType3(transaction, farmType);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }  
  
  @Override
  public FarmType3 getFarmTypeCode(Integer id) throws ServiceException {
    List<FarmType3> farmTypeCodes = getFarmType3s();
    for (FarmType3 farmType : farmTypeCodes) {
      if (farmType.getFarmTypeId().equals(id)) {
        return farmType;
      }
    }
    return null;
  }
  
  @Override
  public FarmType3 getFarmTypeCode(String farmTypeName) throws ServiceException {
    List<FarmType3> farmTypeCodes = getFarmType3s();
    for (FarmType3 farmType : farmTypeCodes) {
      if (farmType.getFarmTypeName().trim().equals(farmTypeName.trim())) {
        return farmType;
      }
    }
    return null;
  }
  
  @Override
  public boolean isFarmType3InUse(final FarmType3 farmType) throws ServiceException {
    boolean inUse = false;
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      inUse = dao.checkFarmType3InUse(farmType, transaction);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return inUse;
  }
  
  @Override
  public List<TipFarmTypeIncomeRange> getFarmType3IncomeRange(Integer farmType3Id) throws ServiceException {
    List<TipFarmTypeIncomeRange> ranges = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      ranges = dao.readFarmType3IncomeRange(transaction, farmType3Id);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return ranges;
 }
  
  @Override
  public List<TipFarmTypeIncomeRange> getFarmType2IncomeRange(Integer farmType2Id, Integer farmType3Id) throws ServiceException {
    List<TipFarmTypeIncomeRange> ranges = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      ranges = dao.readFarmType2IncomeRange(transaction, farmType2Id, farmType3Id);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return ranges;
 }
  
  @Override
  public List<TipFarmTypeIncomeRange> getFarmType1IncomeRange(Integer farmType1Id, Integer farmType2Id, Integer farmType3Id) 
      throws ServiceException {
    List<TipFarmTypeIncomeRange> ranges = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      ranges = dao.readFarmType1IncomeRange(transaction, farmType1Id, farmType2Id, farmType3Id);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return ranges;
 }
  
  @Override 
  public List<TipFarmTypeIncomeRange> getFarmTypeDefaultIncomeRange() throws ServiceException {
    List<TipFarmTypeIncomeRange> ranges = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      ranges = dao.readFarmTypeDefaultIncomeRange(transaction);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return ranges;
  }
  
  @Override
  public List<FarmSubtype> getFarmType2Codes() throws ServiceException {
    List<FarmSubtype> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      codes = dao.readFarmType2Codes(transaction);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return codes;    
  }
  
  @Override
  public Integer createFarmType2(final FarmSubtype farmSubtype, final String user) throws ServiceException {    
    if (farmSubtype == null || user == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    Integer id = null;

    try {
      transaction = openTransaction();
      transaction.begin();
    
      id = dao.createFarmType2(transaction, farmSubtype, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    return id;
  }
  
  @Override
  public  FarmSubtype getFarmSubtypeA(String farmSubtypeName) throws ServiceException {
    List<FarmSubtype> farmSubtypeCodes = getFarmType2Codes();
    for (FarmSubtype farmSubtype : farmSubtypeCodes) {
      if (farmSubtype.getName().equals(farmSubtypeName)) {
        return farmSubtype;
      }
    }
    return null;
  }  
  
  @Override
  public  FarmSubtype getFarmSubtypeA(Integer id) throws ServiceException {
    List<FarmSubtype> farmSubtypeCodes = getFarmType2Codes();
    for (FarmSubtype farmSubtype : farmSubtypeCodes) {
      if (farmSubtype.getId().equals(id)) {
        return farmSubtype;
      }
    }
    return null;
  }    
  
  @Override
  public void updateFarmType2(
      final FarmSubtype farmSubtype,
      final String user) 
          throws ServiceException {
    if (farmSubtype == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateFarmType2(transaction, farmSubtype, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }    
  }
  
  @Override
  public boolean isFarmSubtypeAInUse(final FarmSubtype farmSubtype) throws ServiceException {
    boolean inUse = false;
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      inUse = dao.checkFarmType2InUse(farmSubtype, transaction);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return inUse;    
  }
  
  @Override
  public void deleteFarmType2(final FarmSubtype farmSubtype) throws ServiceException {
    if (farmSubtype == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.deleteFarmType2(transaction, farmSubtype);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }    
  }
  
  @Override
  public List<FarmSubtype> getFarmSubtypesB() throws ServiceException {
    List<FarmSubtype> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      codes = dao.readFarmType1ItemCodes(transaction);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return codes;
  }  

  @Override
  public Integer createFarmType1(final FarmSubtype farmSubtype, final String user) throws ServiceException {
    if (farmSubtype == null || user == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    Integer id = null;
    try {
      transaction = openTransaction();
      transaction.begin();
    
      id = dao.createFarmType1(transaction, farmSubtype, user);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    return id;
  }
  
  @Override
  public  FarmSubtype getFarmSubtypeB(String farmSubtypeName) throws ServiceException {
    List<FarmSubtype> farmSubtypeCodes = getFarmSubtypesB();
    for (FarmSubtype farmSubtype : farmSubtypeCodes) {
      if (farmSubtype.getName().equals(farmSubtypeName)) {
        return farmSubtype;
      }
    }
    return null;
  }
  
  @Override
  public  FarmSubtype getFarmSubtypeB(Integer id) throws ServiceException {
    List<FarmSubtype> farmSubtypeCodes = getFarmSubtypesB();
    for (FarmSubtype farmSubtype : farmSubtypeCodes) {
      if (farmSubtype.getId().equals(id)) {
        return farmSubtype;
      }
    }
    return null;
  }
  
  @Override
  public void updateFarmType1(
      final FarmSubtype farmSubtype,
      final String user) 
          throws ServiceException {
    if (farmSubtype == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.updateFarmType1(transaction, farmSubtype, user, farmSubtype.getParentId());
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public void deleteFarmType1(final FarmSubtype farmSubtype) throws ServiceException {
    if (farmSubtype == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.deleteFarmType1(transaction, farmSubtype);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }    
  }
  
  @Override
  public boolean isFarmType1InUse(final FarmSubtype farmSubtype) throws ServiceException {
    boolean inUse = false;
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      inUse = dao.checkFarmType1InUse(farmSubtype, transaction);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return inUse;    
  }
  
  @Override
  public List<TipLineItem> getTipLineItems() throws ServiceException {
    List<TipLineItem> lineItems = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      lineItems = dao.readTipLineItems(transaction);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return lineItems;
  }
  
  @Override
  public TipLineItem getTipLineItem(Integer id) throws ServiceException {
    TipLineItem lineItem = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      lineItem = dao.readTipLineItem(transaction, id);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return lineItem;
  }
  
  @Override
  public void updateTipLineItem(final FarmSubtype farmSubtype, final String user, final TipLineItem lineItem) throws ServiceException {
    if (lineItem == null || user == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.updateTipLineItem(transaction, farmSubtype, user, lineItem);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public void deleteTipLineItem(final Integer id) throws ServiceException {
    if (id == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.deleteTipLineItem(transaction, id);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public boolean checkTipLineItemExists(final Integer lineItem) throws ServiceException {
    boolean inUse = false;
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      inUse = dao.checkTipLineItemExists(lineItem, transaction);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return inUse;    
  }
  
  @Override
  public void createTipLineItem(final FarmSubtype farmSubtype, final String user, final TipLineItem lineItem) throws ServiceException {
    if (lineItem == null || user == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      dao.createTipLineItem(transaction, farmSubtype, user, lineItem);
      transaction.commit();
      
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public List<TipBenchmarkInfo> getTipBenchmarkInfos() throws ServiceException {
    List<TipBenchmarkInfo> benchmarkInfos = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();
      benchmarkInfos = dao.readTipBenchmarkInfos(transaction);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return benchmarkInfos;
  }

  @Override
  public List<DocumentTemplate> getDocumentTemplates() throws ServiceException {
    List<DocumentTemplate> documentTemplates;
     
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
  
    try {
      transaction = openTransaction();
      documentTemplates = dao.readDocumentTemplates(transaction, null);
       
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return documentTemplates;
  }
  
  @Override
  public DocumentTemplate getDocumentTemplate(String templateName) throws ServiceException {
    DocumentTemplate documentTemplate = null;
     
    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();
  
    try {
      transaction = openTransaction();
      List<DocumentTemplate> documentTemplates = dao.readDocumentTemplates(transaction, templateName);
      if(documentTemplates.size() > 0) {
        documentTemplate = documentTemplates.get(0);
      }
       
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
    return documentTemplate;     
  }
   
  @Override
  public void updateDocumentTemplate(final DocumentTemplate documentTemplate, final String user) throws ServiceException {
    if (documentTemplate == null) {
      logger.debug("Required object(s) are null.");
      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateDocumentTemplate(transaction, documentTemplate, user);
      transaction.commit();
       
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
  }   


  @Override
  public List<SectorCode> getSectorCodes() throws ServiceException {
    return getSectorCodes(null);
  }


  @Override
  public SectorCode getSectorCode(String code) throws ServiceException {

    SectorCode result = null;
    List<SectorCode> codes = getSectorCodes(code);
    if( ! codes.isEmpty() ) {
      result = codes.get(0);
    }
    return result;
  }


  private List<SectorCode> getSectorCodes(String code) throws ServiceException {

    List<SectorCode> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      codes = dao.readSectorCodes(transaction, code);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return codes;
  }

  
  @Override
  public void updateSectorCode(SectorCode code, String user) throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateSectorCode(transaction, code, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  @Override
  public List<SectorDetailCode> getSectorDetailCodes() throws ServiceException {
    return getSectorDetailCodes(null);
  }


  @Override
  public SectorDetailCode getSectorDetailCode(String code) throws ServiceException {
    SectorDetailCode result = null;
    
    if(code != null) {
      List<SectorDetailCode> codes = getSectorDetailCodes(code);
      if( ! codes.isEmpty() ) {
        result = codes.get(0);
      }
    }
    
    return result;
  }


  private List<SectorDetailCode> getSectorDetailCodes(String code) throws ServiceException {

    List<SectorDetailCode> codes = null;

    Transaction transaction = null;
    CodesReadDAO dao = new CodesReadDAO();

    try {
      transaction = openTransaction();

      codes = dao.readSectorDetailCodes(transaction, code);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return codes;
  }


  @Override
  public void createSectorDetailCode(SectorDetailCode code, String user) throws ServiceException {

    if (code == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createSectorDetailCode(transaction, code, user);

      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  @Override
  public void updateSectorDetailCode(SectorDetailCode code, String user) throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.updateSectorDetailCode(transaction, code, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  @Override
  public boolean isSectorDetailCodeInUse(String code) throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    boolean result = false;
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      result = dao.isSectorDetailCodeInUse(transaction, code);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }
  
  
  
  @Override
  public void deleteSectorDetailCode(String code, Integer revisionCount) throws ServiceException {
    
    if (code == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CodesWriteDAO dao = new CodesWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.deleteSectorDetailCode(transaction, code, revisionCount);
      
      transaction.commit();
    } catch(ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
}
