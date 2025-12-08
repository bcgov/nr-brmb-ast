/**
 *
 * Copyright (c) 2010,
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.exception.DataAccessException;

/**
 * @author awilkinson
 * @created Dec 6, 2010
 */
public class CrmTransferDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_WEBAPP_PKG";

  private static final String READ_CONTACT_TRANSFER_PROC = "READ_CONTACT_TRANSFER";

  private static final String READ_CONTACT_TRANSFER_PROGRAM_YEAR_VERSION_PROC = "READ_CONTACT_TRANSFER_PROGRAM_YEAR_VERSION";


  public List<Client> getContactInformation(final Connection connection,
      final List<Integer> clientIds)
      throws DataAccessException {

    List<Client> clients = null;
    final int paramCount = 1;

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_CONTACT_TRANSFER_PROC, paramCount, true);) {

      Array oracleArray = createNumbersOracleArray(connection, clientIds);
      
      int param = 1;
      proc.setArray(param++, oracleArray);
      proc.execute();

      try(ResultSet rs = proc.getResultSet();) {

        clients = new ArrayList<>();
  
        while (rs.next()) {
          int c = 1;
          Client pinfo = new Client();
          
          pinfo.setClientId(getInteger(rs, c++));
          pinfo.setParticipantPin(getInteger(rs, c++));
          pinfo.setParticipantClassCode(getString(rs, c++));
          pinfo.setSin(getString(rs, c++));
          pinfo.setBusinessNumber(getString(rs, c++));
          pinfo.setTrustNumber(getString(rs, c++));
          pinfo.setParticipantClassCodeDescription(getString(rs, c++));
          pinfo.setFederalIdentifier(getString(rs, c++));
          pinfo.setIdentEffectiveDate(getDate(rs, c++));
          
          Person owner = new Person();
          owner.setCorpName(getString(rs, c++));
          owner.setFirstName(getString(rs, c++));
          owner.setLastName(getString(rs, c++));
          owner.setAddressLine1(getString(rs, c++));
          owner.setAddressLine2(getString(rs, c++));
          owner.setCity(getString(rs, c++));
          owner.setProvinceState(getString(rs, c++));
          owner.setCountry(getString(rs, c++));
          owner.setPostalCode(getString(rs, c++));
          owner.setDaytimePhone(getString(rs, c++));
          owner.setEveningPhone(getString(rs, c++));
          owner.setFaxNumber(getString(rs, c++));
          owner.setCellNumber(getString(rs, c++));
          owner.setEmailAddress(getString(rs, c++));
          
          Person contact = new Person();
          contact.setCorpName(getString(rs, c++));
          contact.setFirstName(getString(rs, c++));
          contact.setLastName(getString(rs, c++));
          contact.setAddressLine1(getString(rs, c++));
          contact.setAddressLine2(getString(rs, c++));
          contact.setCity(getString(rs, c++));
          contact.setProvinceState(getString(rs, c++));
          contact.setCountry(getString(rs, c++));
          contact.setPostalCode(getString(rs, c++));
          contact.setDaytimePhone(getString(rs, c++));
          contact.setEveningPhone(getString(rs, c++));
          contact.setFaxNumber(getString(rs, c++));
          contact.setCellNumber(getString(rs, c++));
          contact.setEmailAddress(getString(rs, c++));
  
          pinfo.setOwner(owner);
          pinfo.setContact(contact);
          clients.add(pinfo);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return clients;
  }


  public Map<Integer, FarmingYear> getFarmingYears(final Connection connection,
      final List<Integer> clientIds)
      throws DataAccessException {

    Map<Integer, FarmingYear> farmingYears = new HashMap<>();
    final int paramCount = 1;

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_CONTACT_TRANSFER_PROGRAM_YEAR_VERSION_PROC, paramCount, true);) {

      Array oracleArray = createNumbersOracleArray(connection, clientIds);
      
      int param = 1;
      proc.setArray(param++, oracleArray);
      proc.execute();

      try(ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          int c = 1;
          
          Integer clientId = getInteger(rs, c++);
          
          FarmingYear fy = new FarmingYear();
          
          fy.setProgramYearId(getInteger(rs, c++));
          fy.setProgramYearVersionId(getInteger(rs, c++));
          fy.setProgramYearVersionNumber(getInteger(rs, c++));
          fy.setFormVersionNumber(getInteger(rs, c++));
          fy.setCommonShareTotal(getInteger(rs, c++));
          fy.setFarmYears(getInteger(rs, c++));
          fy.setIsAccrualWorksheet(getIndicator(rs, c++));
          fy.setIsCompletedProdCycle(getIndicator(rs, c++));
          fy.setIsCwbWorksheet(getIndicator(rs, c++));
          fy.setIsPerishableCommodities(getIndicator(rs, c++));
          fy.setIsReceipts(getIndicator(rs, c++));
          fy.setIsAccrualCashConversion(getIndicator(rs, c++));
          fy.setIsCombinedFarm(getIndicator(rs, c++));
          fy.setIsCoopMember(getIndicator(rs, c++));
          fy.setIsCorporateShareholder(getIndicator(rs, c++));
          fy.setIsDisaster(getIndicator(rs, c++));
          fy.setIsPartnershipMember(getIndicator(rs, c++));
          fy.setIsSoleProprietor(getIndicator(rs, c++));
          fy.setOtherText(getString(rs, c++));
          fy.setPostMarkDate(getDate(rs, c++));
          fy.setProvinceOfResidence(getString(rs, c++));
          fy.setCraStatementAReceivedDate(getDate(rs, c++));
          fy.setIsLastYearFarming(getIndicator(rs, c++));
          fy.setDescriptionOfChange(getString(rs, c++));
          fy.setIsCanSendCobToRep(getIndicator(rs, c++));
          fy.setProvinceOfMainFarmstead(getString(rs, c++));
          fy.setIsLocallyUpdated(getIndicator(rs, c++));
          fy.setParticipantProfileCode(getString(rs, c++));
          fy.setParticipantProfileCodeDescription(getString(rs, c++));
          fy.setMunicipalityCode(getString(rs, c++));
          fy.setMunicipalityCodeDescription(getString(rs, c++));
          fy.setAgristabFedStsCode(getInteger(rs, c++));
          fy.setAgristabFedStsCodeDescription(getString(rs, c++));
          fy.setIsNonParticipant(getIndicator(rs, c++));
          fy.setIsLateParticipant(getIndicator(rs, c++));
          fy.setRevisionCount(getInteger(rs, c++));
          
          farmingYears.put(clientId, fy);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return farmingYears;
  }

}
