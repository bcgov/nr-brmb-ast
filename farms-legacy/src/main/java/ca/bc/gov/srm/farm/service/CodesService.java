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
package ca.bc.gov.srm.farm.service;

import java.io.File;
import java.util.List;

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

/**
 * @author awilkinson
 */
public interface CodesService {

  List<Code> getCodes(final String codeTable) throws ServiceException;
  
  Code getCode(
      final String codeTable,
      final String code) throws ServiceException;

  /**
   * @param codeTable codeTable
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  void createCode(
      final String codeTable,
      final Code code,
      final String user)
  throws ServiceException;
  
  /**
   * @param codeTable codeTable
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateCode(
      final String codeTable,
      final Code code,
      final String user)
  throws ServiceException;

  /**
   * @param codeTable codeTable
   * @param code code
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean isCodeInUse(
      final String codeTable,
      final String code)
  throws ServiceException;

  /**
   * @param codeTable codeTable
   * @param code code
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  void deleteCode(
      final String codeTable,
      final String code,
      final Integer revisionCount)
  throws ServiceException;


  /**
   * @param year The program year.
   * @param lineItem The line item number.
   * @return LineItemCode for the requested year.
   * @throws ServiceException On Exception
   */
  LineItemCode getLineItem(
      final Integer year,
      final Integer lineItem) throws ServiceException;

  /**
   * @param year The program year.
   * @return list of line items for the requested year.
   * @throws ServiceException On Exception
   */
  List<LineItemCode> getLineItems(final Integer year) throws ServiceException;

  /**
   * @param year The program year.
   * @param lineItem The line item number.
   * @return true if this line item is in use in a scenario
   * @throws ServiceException On Exception
   */
  boolean isLineItemInUse(
      final Integer year,
      final Integer lineItem) throws ServiceException;

  /**
   * @param lineItem lineItem
   * @param user user
   * @throws ServiceException On Exception
   */
  void createLineItem(
      final LineItemCode lineItem,
      final String user)
  throws ServiceException;

  /**
   * @param lineItem lineItem
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateLineItem(
      final LineItemCode lineItem,
      final String user)
  throws ServiceException;

  /**
   * @param lineItemId lineItemId
   * @param year year
   * @param lineItem lineItem
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  void deleteLineItem(
      final Integer lineItemId,
      final Integer year,
      final Integer lineItem,
      final Integer revisionCount)
  throws ServiceException;

  /**
   * @param toYear toYear
   * @param user user
   * @throws ServiceException On Exception
   */
  void copyYearLineItems(
      final Integer toYear,
      final String user)
  throws ServiceException;
  
  /**
   * @param year The program year.
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @return FMV for the requested year.
   * @throws ServiceException On Exception
   */
  FMV getFMV(
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode) throws ServiceException;
  
  /**
   * @param year The program year.
   * @return list of line items for the requested year.
   * @throws ServiceException On Exception
   */
  List<FMV> getFMVs(final Integer year) throws ServiceException;

  /**
   * @param fmv fmv
   * @param user user
   * @throws ServiceException On Exception
   */
  void createFMV(
      final FMV fmv,
      final String user)
  throws ServiceException;

  /**
   * @param fmv fmv
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateFMV(
      final FMV fmv,
      final String user)
  throws ServiceException;
  
  /**
   * @param year The program year.
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @return true if this line item is in use in a scenario
   * @throws ServiceException On Exception
   */
  boolean isFMVInUse(
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode) throws ServiceException;

  /**
   * @param year The program year.
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @throws ServiceException On Exception
   */
  void deleteFMV(
      final Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode)
  throws ServiceException;

  /**
   * @param year The program year.
   * @param inventoryItemCodeFilter inventoryItemCodeFilter
   * @param inventoryItemDescriptionFilter inventoryItemDescriptionFilter
   * @param municipalityDescriptionFilter municipalityDescriptionFilter
   * @param cropUnitDescriptionFilter cropUnitDescriptionFilter
   * @return File containing FMVs as a comma delimited lists for the requested year.
   * @throws ServiceException On Exception
   */
  File exportFmvCsv(
      final Integer year,
      final String inventoryItemCodeFilter,
      final String inventoryItemDescriptionFilter,
      final String municipalityDescriptionFilter,
      final String cropUnitDescriptionFilter) throws ServiceException;
  
  /**
   * @param year The program year.
   * @return File containing FMVs as a comma delimited lists for the requested year.
   * @throws ServiceException On Exception
   */
  File exportMissingFmvCsv(
      final Integer year) throws ServiceException;

  /**
   * @return list of type MunicipalityCode
   * @throws ServiceException On Exception
   */
  List<MunicipalityCode> getMunicipalityCodes() throws ServiceException;

  /**
   * @param code code
   * @return MunicipalityCode
   * @throws ServiceException On Exception
   */
  MunicipalityCode getMunicipalityCode(
      final String code) throws ServiceException;

  /**
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  void createMunicipalityCode(
      final MunicipalityCode code,
      final String user)
  throws ServiceException;
  
  /**
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateMunicipalityCode(
      final MunicipalityCode code,
      final String user)
  throws ServiceException;
  
  /**
   * @param code code
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean isMunicipalityCodeInUse(
      final String code)
  throws ServiceException;

  
  /**
   * @param code code
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  void deleteMunicipalityCode(
      final String code,
      final Integer revisionCount)
  throws ServiceException;

  /**
   * @param year The program year.
   * @return list of BPUs for the requested year.
   * @throws ServiceException On Exception
   */
  List<BPU> getBPUs(final Integer year) throws ServiceException;
  
  /**
   * @param bpu bpu
   * @param user user
   * @throws ServiceException On Exception
   */
  void createBPU(final BPU bpu, final String user) throws ServiceException;
  
  /**
   * @param yearsToUpdate yearsToUpdate
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateBPUYears(final List<BPUYear> yearsToUpdate, final String user) throws ServiceException;
  
  /**
   * @param bpuId bpuId
   * @return true if the BPU is needed by a scenario
   * @throws ServiceException On Exception
   */
  boolean isBPUInUse(final Integer bpuId) throws ServiceException;

  /**
   * @param bpuId bpuId
   * @throws ServiceException On Exception
   */
  void deleteBPU(final Integer bpuId)throws ServiceException;
  
  /**
   * @param year The program year.
   * @param inventoryItemCodeFilter inventoryItemCodeFilter
   * @param inventoryItemDescriptionFilter inventoryItemDescriptionFilter
   * @param municipalityDescriptionFilter municipalityDescriptionFilter
   * @return File containing BPUs as a comma delimited lists for the requested year.
   * @throws ServiceException On Exception
   */
  File exportBpuCsv(
      final Integer year,
      final String inventoryItemCodeFilter,
      final String inventoryItemDescriptionFilter,
      final String municipalityDescriptionFilter) throws ServiceException;
  
  /**
   * @param year The program year.
   * @return File containing BPUs as a comma delimited lists for the requested year.
   * @throws ServiceException On Exception
   */
  File exportMissingBpuCsv(
      final Integer year) throws ServiceException;

  /**
   * @return list of type InventoryItemCode
   * @throws ServiceException On Exception
   */
  List<InventoryItemCode> getInventoryItemCodes() throws ServiceException;

  /**
   * @param code code
   * @return InventoryItemCode
   * @throws ServiceException On Exception
   */
  InventoryItemCode getInventoryItemCode(
      final String code) throws ServiceException;

  /**
   * @param code code
   * @param detailsList detailsList
   * @param user user
   * @throws ServiceException On Exception
   */
  void createInventoryItemCode(
      final InventoryItemCode code,
      final List<InventoryItemDetail> detailsList,
      final String user)
  throws ServiceException;

  /**
   * @param code code
   * @param detailsAddList detailsAddList
   * @param detailsUpdateList detailsUpdateList
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateInventoryItemCode(
      final InventoryItemCode code,
      List<InventoryItemDetail> detailsAddList, List<InventoryItemDetail> detailsUpdateList, final String user)
  throws ServiceException;

  /**
   * @param code code
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean isInventoryItemCodeInUse(
      final String code)
  throws ServiceException;

  /**
   * @param code code
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  void deleteInventoryItemCode(
      final String code,
      final Integer revisionCount)
  throws ServiceException;

  /**
   * @param commodityXrefId commodityXrefId
   * @return List<InventoryXref>
   * @throws ServiceException On Exception
   */
  InventoryXref getInventoryXref(
      final Integer commodityXrefId) throws ServiceException;

  /**
   * @param inventoryClassCode inventoryClassCode
   * @return List<InventoryXref>
   * @throws ServiceException On Exception
   */
  List<InventoryXref> getInventoryXrefs(final String inventoryClassCode) throws ServiceException;
  
  /**
   * @param inventoryClassCode inventoryClassCode
   * @param inventoryItemCode inventoryItemCode
   * @return InventoryXref
   * @throws ServiceException On Exception
   */
  InventoryXref getInventoryXref(
      final String inventoryClassCode,
      final String inventoryItemCode) throws ServiceException;

  /**
   * @param xref xref
   * @param user user
   * @throws ServiceException On Exception
   */
  void createInventoryXref(
      final InventoryXref xref,
      final String user)
  throws ServiceException;

  /**
   * @param xref xref
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateInventoryXref(
      final InventoryXref xref,
      final String user)
  throws ServiceException;

  /**
   * @param commodityXrefId commodityXrefId
   * @return true if this inventory xref is in use in a scenario
   * @throws ServiceException On Exception
   */
  boolean isInventoryXrefInUse(
      final Integer commodityXrefId) throws ServiceException;

  /**
   * @param commodityXrefId commodityXrefId
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  void deleteInventoryXref(
      final Integer commodityXrefId,
      final Integer revisionCount)
  throws ServiceException;

  /**
   * @return list of type MarketRatePremium
   * @throws ServiceException On Exception
   */
  List<MarketRatePremium> getMarketRatePremiums() throws ServiceException;

  /**
   * @param id id
   * @return MarketRatePremium
   * @throws ServiceException On Exception
   */
  MarketRatePremium getMarketRatePremium(
      final Long id) throws ServiceException;

  /**
   * @param mrp mrp
   * @param user user
   * @throws ServiceException On Exception
   */
  void createMarketRatePremium(
      final MarketRatePremium mrp,
      final String user)
  throws ServiceException;

  /**
   * @param mrp mrp
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateMarketRatePremium(
      final MarketRatePremium mrp,
      final String user)
  throws ServiceException;

  /**
   * @param id id
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  void deleteMarketRatePremium(
      final Long id,
      final Integer revisionCount)
  throws ServiceException;

  /**
   * @return list of type StructureGroupCode
   * @throws ServiceException On Exception
   */
  List<StructureGroupCode> getStructureGroupCodes() throws ServiceException;

  /**
   * @param code code
   * @return StructureGroupCode
   * @throws ServiceException On Exception
   */
  StructureGroupCode getStructureGroupCode(
      final String code) throws ServiceException;

  /**
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  void createStructureGroupCode(
      final StructureGroupCode code,
      final String user)
  throws ServiceException;

  /**
   * @param code code
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateStructureGroupCode(
      final StructureGroupCode code,
      final String user)
  throws ServiceException;

  /**
   * @param code code
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean isStructureGroupCodeInUse(
      final String code)
  throws ServiceException;

  /**
   * @param code code
   * @param revisionCount revisionCount
   * @throws ServiceException On Exception
   */
  void deleteStructureGroupCode(
      final String code,
      final Integer revisionCount)
  throws ServiceException;

  /**
   * @return List<Integer>
   * @throws ServiceException On Exception
   */
  List<Integer> getProgramYears() throws ServiceException;

  CropUnitConversion getCropUnitConversion(
      final String inventoryItemCode) throws ServiceException;

  List<CropUnitConversion> getCropUnitConversions() throws ServiceException;

  void createCropUnitConversion(
      final CropUnitConversion cropUnitConversion,
      final String user)
  throws ServiceException;

  void updateCropUnitConversion(
      final CropUnitConversion cropUnitConversion,
      final String user)
          throws ServiceException;

  void deleteCropUnitConversion(
      final String inventoryItemCode)
  throws ServiceException;
  
  List<FruitVegTypeCode> getFruitVegCodes() throws ServiceException;
  
  FruitVegTypeCode getFruitVegCode(String fruitVegCodeName) throws ServiceException;
  
  void updateFruitVegCode(
      final FruitVegTypeCode fruitVegCode,
      final String user)
          throws ServiceException;

  void createFruitVegCode(
      final FruitVegTypeCode fruitVegCode,
      final String user)
  throws ServiceException;
  
  boolean checkFruitVegCodeInUse(final FruitVegTypeCode fruitVegCode) throws ServiceException;
  
  void deleteFruitVegCode(
      final FruitVegTypeCode fruitVegCode)
  throws ServiceException;
  
  List<CodeListView> getFruitVegListItems() throws ServiceException;
  
  List<ExpectedProduction> getExpectedProductionItems() throws ServiceException;  
  
  ExpectedProduction getExpectedProductionItem(Integer id) throws ServiceException;
  
  void updateExpectedProductionItem(
      final ExpectedProduction expectedProduction,
      final String user)
  throws ServiceException;
  
  void createExpectedProductionItem(
      final ExpectedProduction expectedProduction, 
      final String user) 
  throws ServiceException;
  
  boolean checkExpectedProductionItemExists(
      final ExpectedProduction expectedProduction) throws ServiceException;
  
  void deleteExpectedProductionItem(
      final ExpectedProduction expectedProduction)
  throws ServiceException;
  
  List<ConfigurationParameter> getConfigurationParameters() throws ServiceException;
  
  ConfigurationParameter getConfigurationParameter(String name) throws ServiceException;
  
  ConfigurationParameter getConfigurationParameter(Integer id) throws ServiceException;
  
  void updateConfigParam(
      final ConfigurationParameter configParam,
      final String user)
  throws ServiceException;
  
  List<YearConfigurationParameter> getYearConfigurationParameters(Integer programYear) throws ServiceException;
  
  YearConfigurationParameter getYearConfigurationParameter(Integer id) throws ServiceException;
  
  void updateConfigParam(YearConfigurationParameter configParam, String user) throws ServiceException;
  
  boolean checkLineItemExistsForProgramYear(final Integer programYear, final Integer lineItem) throws ServiceException;

  Integer createFarmType3(
	  final FarmType3 farmType, 
	  final String user) 
		  throws ServiceException;
  
  void updateFarmType3(
      final FarmType3 farmType,
      final String user) 
          throws ServiceException;
  
  void deleteFarmType3(
      final FarmType3 farmType
      ) throws ServiceException;
  
  List<FarmType3> getFarmType3s() throws ServiceException;


  FarmType3 getFarmTypeCode(Integer id) throws ServiceException;
  
  FarmType3 getFarmTypeCode(String farmTypeName) throws ServiceException;
  
  boolean isFarmType3InUse(final FarmType3 farmType) throws ServiceException;
  
  List<FarmSubtype> getFarmType2Codes() throws ServiceException;
  
  Integer createFarmType2(
      final FarmSubtype farmType, 
      final String user) 
          throws ServiceException;
  
  FarmSubtype getFarmSubtypeA(String farmSubtypeName) throws ServiceException;
  
  FarmSubtype getFarmSubtypeA(Integer id) throws ServiceException;
  
  void updateFarmType2(
      final FarmSubtype farmType,
      final String user) 
          throws ServiceException;
  
  boolean isFarmSubtypeAInUse(final FarmSubtype farmSubtype) throws ServiceException;
  
  void deleteFarmType2(
      final FarmSubtype farmSubtype
      ) throws ServiceException;
  
  List<FarmSubtype> getFarmSubtypesB() throws ServiceException;
  
  Integer createFarmType1(
      final FarmSubtype farmSubtype, 
      final String user) 
          throws ServiceException;
  
  FarmSubtype getFarmSubtypeB(String farmSubtypeName) throws ServiceException;
  
  FarmSubtype getFarmSubtypeB(Integer id) throws ServiceException;
  
  void updateFarmType1(
      final FarmSubtype farmType,
      final String user) 
          throws ServiceException;   
  
  void deleteFarmType1(
      final FarmSubtype farmSubtype
      ) throws ServiceException;  
  
  boolean isFarmType1InUse(final FarmSubtype farmSubtype) throws ServiceException;
  
  List<TipLineItem> getTipLineItems() throws ServiceException;
  
  TipLineItem getTipLineItem(Integer id) throws ServiceException;
  
  void updateTipLineItem(
      final FarmSubtype farmSubtype,
      final String user,
      final TipLineItem lineItem)
          throws ServiceException;

  void deleteTipLineItem(Integer id) throws ServiceException;

  boolean checkTipLineItemExists(Integer lineItem) throws ServiceException;

  void createTipLineItem(FarmSubtype farmSubtype, String user, TipLineItem lineItem) throws ServiceException;

  List<TipFarmTypeIncomeRange> getFarmType3IncomeRange(Integer id) throws ServiceException;

  void createFarmType3IncomeRange(List<TipFarmTypeIncomeRange> ranges, String user, Integer id) throws ServiceException;

  List<TipFarmTypeIncomeRange> getFarmType2IncomeRange(Integer id, Integer parentId) throws ServiceException;

  void createFarmType2IncomeRange(List<TipFarmTypeIncomeRange> ranges, String user, Integer id) throws ServiceException;

  List<TipFarmTypeIncomeRange> getFarmType1IncomeRange(Integer id, Integer parentId, Integer secondaryParentId)
      throws ServiceException;

  void createFarmType1IncomeRange(List<TipFarmTypeIncomeRange> ranges, String user, Integer id) throws ServiceException;

  List<TipFarmTypeIncomeRange> getFarmTypeDefaultIncomeRange() throws ServiceException;

  List<TipBenchmarkInfo> getTipBenchmarkInfos() throws ServiceException;

  void createFarmTypeDefaultIncomeRange(List<TipFarmTypeIncomeRange> ranges, String user) throws ServiceException;

  void deleteFarmTypeDefaultIncomeRange() throws ServiceException;

  List<CommodityTypeCode> getCommodityTypeCodes() throws ServiceException;

  List<CodeListView> getCommodityTypeListItems() throws ServiceException;

  List<DocumentTemplate> getDocumentTemplates() throws ServiceException;
  
  DocumentTemplate getDocumentTemplate(String templateName) throws ServiceException;

  void updateDocumentTemplate(final DocumentTemplate documentTemplate, final String user) throws ServiceException;

  List<SectorCode> getSectorCodes() throws ServiceException;

  SectorCode getSectorCode(String code) throws ServiceException;
  
  void updateSectorCode(SectorCode code, String user) throws ServiceException;
  
  List<SectorDetailCode> getSectorDetailCodes() throws ServiceException;

  SectorDetailCode getSectorDetailCode(String code) throws ServiceException;
  
  void createSectorDetailCode(SectorDetailCode code, String user) throws ServiceException;
  
  void updateSectorDetailCode(SectorDetailCode code, String user) throws ServiceException;
  
  boolean isSectorDetailCodeInUse(String code) throws ServiceException;
  
  void deleteSectorDetailCode(String code, Integer revisionCount) throws ServiceException;

}
