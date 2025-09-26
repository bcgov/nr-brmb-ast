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

import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.ClientSubscription;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.domain.AuthorizedUser;

import org.apache.commons.lang.StringEscapeUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;


/**
 * DAO used by the webapp for authorizations.
 */
public class SubscriptionDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_WEBAPP_PKG";

  /** USERS_PROC. */
  private static final String USERS_PROC = "GET_AUTHORIZED_USERS";

  /** CLIENTS_PROC. */
  private static final String CLIENTS_PROC = "GET_CLIENTS";

  /** SUBS_PROC. */
  private static final String SUBS_PROC = "GET_SUBSCRIPTIONS";

  /** UPDATE_STATUS_PROC. */
  private static final String UPDATE_STATUS_PROC = "UPDATE_SUBSCRIPTION_STATUS";

  /** INSERT_SUB_PROC. */
  private static final String INSERT_SUB_PROC = "INSERT_SUBSCRIPTION";

  /** INSERT_REP_PROC. */
  private static final String GET_REP_PROC = "GET_REPRESENTATIVE";

  /** INSERT_REP_PROC. */
  private static final String INSERT_REP_PROC = "INSERT_REPRESENTATIVE";

  /** ACTIVATE_PROC. */
  private static final String ACTIVATE_PROC = "ACTIVATE_SUBSCRIPTION";

  /**
   * @param   transaction  transaction
   * @param   pin          pin
   *
   * @return  List of AuthorizedUser
   *
   * @throws  DataAccessException  on exception
   */
  public List getAuthorizedUsers(
  	final Transaction transaction,
    final Integer pin) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + USERS_PROC;
    List items = new ArrayList();
    Connection connection = getConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      int param = 1;
      proc.setInt(param++, pin);
      proc.execute();
      resultSet = proc.getResultSet();

      while (resultSet.next()) {
        AuthorizedUser user = new AuthorizedUser();

        user.setActivatedDate(resultSet.getDate("ACTIVATED_DATE"));
        user.setAgristabilityRepresntveId(
          new Integer(resultSet.getInt("AGRISTABILITY_REPRESNTVE_ID")));
        user.setClientSubscriptionId(
          new Integer(resultSet.getInt("CLIENT_SUBSCRIPTION_ID")));
        user.setUserid(resultSet.getString("USERID"));
        user.setUserGuid(resultSet.getString("USER_GUID"));
        user.setRevisionCount(new Integer(resultSet.getInt("REVISION_COUNT")));

        items.add(user);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return items;
  }


  /**
   * @param   transaction  transaction
   * @param   guid         guid
   *
   * @return  List of AgristabilityClient
   *
   * @throws  DataAccessException  on exception
   */
  public List getClientsForUser(
  	final Transaction transaction,
    final String guid) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + CLIENTS_PROC;
    List items = new ArrayList();
    Connection connection = getConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      int param = 1;
      proc.setString(param++, guid);
      proc.execute();
      resultSet = proc.getResultSet();

      while (resultSet.next()) {
        Client ac = new Client();
        Person owner = new Person();

        owner.setPersonId(new Integer(resultSet.getInt("PERSON_ID")));
        owner.setAddressLine1(resultSet.getString("address_line_1"));
        owner.setAddressLine2(resultSet.getString("address_line_2"));
        owner.setCity(resultSet.getString("CITY"));
        owner.setCorpName(resultSet.getString("CORP_NAME"));
        owner.setDaytimePhone(resultSet.getString("daytime_phone"));
        owner.setEveningPhone(resultSet.getString("evening_phone"));
        owner.setFaxNumber(resultSet.getString("fax_number"));
        owner.setFirstName(resultSet.getString("first_name"));
        owner.setLastName(resultSet.getString("last_name"));
        owner.setPostalCode(resultSet.getString("postal_code"));
        owner.setProvinceState(resultSet.getString("province_state"));

        ac.setClientId(new Integer(
            resultSet.getInt("AGRISTABILITY_CLIENT_ID")));
        ac.setParticipantPin(new Integer(resultSet.getInt("PARTICIPANT_PIN")));
        ac.setOwner(owner);

        items.add(ac);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return items;
  }


  /**
   * @param   transaction  transaction
   * @param   sub          sub
   * @param   clientId          clientId
   *
   * @throws  DataAccessException  on exception
   */
  public void insertSubscription(
    final Transaction transaction,
    final Integer clientId,
    final ClientSubscription sub) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + INSERT_SUB_PROC;
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    final int paramCount = 5;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int param = 1;
      proc.registerOutParameter(param, Types.INTEGER);

      proc.setInt(param++, (Integer) null);
      proc.setInt(param++, clientId);
      proc.setString(param++, sub.getSubscriptionNumber());
      proc.setString(param++, sub.getGeneratedByUserid());
      proc.setDate(param++, sub.getActivationExpiryDate());
      proc.execute();

      param = 1;
      sub.setClientSubscriptionId(new Integer(proc.getInt(param)));
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      getLog().error(String.valueOf(proc));
      handleException(e);
    } finally {
      close(null, proc);
    }
  }


  /**
   * @param   transaction  transaction
   * @param   pin          pin
   *
   * @return  List of ClientSubscription
   *
   * @throws  DataAccessException  on exception
   */
  public List getSubscriptions(
    final Transaction transaction, 
    final Integer pin)
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + SUBS_PROC;
    List items = new ArrayList();
    Connection connection = getConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      int param = 1;
      proc.setInt(param++, pin);
      proc.execute();
      resultSet = proc.getResultSet();

      while (resultSet.next()) {
        ClientSubscription sub = new ClientSubscription();

        sub.setActivatedDate(
        		resultSet.getDate("ACTIVATED_DATE"));
        // skip AGRISTABILITY_REPRESNTVE_ID
        sub.setClientSubscriptionId(
          new Integer(resultSet.getInt("CLIENT_SUBSCRIPTION_ID")));
        sub.setActivationExpiryDate(
          resultSet.getDate("ACTIVATION_EXPIRY_DATE"));
        sub.setActivatedByUserid(
        		resultSet.getString("ACTIVATED_BY_USERID"));
        // skip AGRISTABILITY_CLIENT_ID
        sub.setGeneratedByUserid(
        		resultSet.getString("GENERATED_BY_USERID"));
        sub.setGeneratedDate(
        		resultSet.getDate("GENERATED_DATE"));
        sub.setSubscriptionNumber(
        		resultSet.getString("SUBSCRIPTION_NUMBER"));
        sub.setSubscriptionStatusCode(
        		resultSet.getString("SUBSCRIPTION_STATUS_CODE"));
        sub.setSubscriptionStatusCodeDescription(
        		resultSet.getString("SUBSCRIPTION_STATUS_DESC"));
        sub.setRevisionCount(
        		new Integer(resultSet.getInt("REVISION_COUNT")));
        
        //
        // The backspace in the IDs needs to be turned into 2 backspaces
        // for the Javasript dialogs to display the IDs correctly.
        //
        sub.setJavascriptGeneratedByUserid(
        		StringEscapeUtils.escapeJavaScript(sub.getGeneratedByUserid()));
        sub.setJavascriptActivatedByUserid(
        		StringEscapeUtils.escapeJavaScript(sub.getActivatedByUserid()));

        items.add(sub);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return items;
  }


  /**
   * @param   transaction           transaction
   * @param   clientSubscriptionId  clientSubscriptionId
   * @param   newStatusCode         newStatusCode
   * @param   revisionCount         revisionCount
   * @param   userid                userid
   *
   * @throws  DataAccessException  on exception
   */
  public void updateSubscriptionStatus(
  	final Transaction transaction,
    final Integer clientSubscriptionId, 
    final String newStatusCode,
    final Integer revisionCount, 
    final String userid)
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + UPDATE_STATUS_PROC;

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    final int paramCount = 4;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int param = 1;
      proc.setInt(param++, clientSubscriptionId);
      proc.setString(param++, newStatusCode);
      proc.setInt(param++, revisionCount);
      proc.setString(param++, userid);
      proc.execute();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * @param   transaction  transaction
   * @param   guid         guid
   *
   * @return  Integer
   *
   * @throws  DataAccessException  on exception
   */
  public Integer getRepresentativeId(
  	final Transaction transaction,
    final String guid) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + GET_REP_PROC;
    Integer id = null;
    Connection connection = getConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      int param = 1;
      proc.setString(param++, guid);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        id = new Integer(resultSet.getInt("AGRISTABILITY_REPRESNTVE_ID"));
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return id;
  }


  /**
   * @param   transaction  transaction
   * @param   guid         guid
   * @param   userid       userid
   *
   * @return  the new ID
   *
   * @throws  DataAccessException  on exception
   */
  public Integer insertRepresentative(
  	final Transaction transaction,
    final String guid, 
    final String userid) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + INSERT_REP_PROC;
    Integer id = null;
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    final int paramCount = 3;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int param = 1;
      proc.registerOutParameter(param, Types.INTEGER);

      proc.setInt(param++, (Integer) null);
      proc.setString(param++, guid);
      proc.setString(param++, userid);
      proc.execute();

      param = 1;
      id = new Integer(proc.getInt(param));
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      getLog().error(String.valueOf(proc));
      handleException(e);
    } finally {
      close(proc);
    }

    return id;
  }


  /**
   * @param   transaction         transaction
   * @param   representativeId    representativeId
   * @param   subscriptionNumber  subscriptionNumber
   * @param   userid              userid
   *
   * @return  the number of rows updated
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public int activateSubscriptions(final Transaction transaction,
    final Integer representativeId, final String subscriptionNumber,
    final String userid) throws DataAccessException {
    String procName = PACKAGE_NAME + "." + ACTIVATE_PROC;
    int numRowsUpdated = 0;
    Connection connection = getConnection(transaction);
    final int paramCount = 4;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

      int param = 1;
      proc.registerOutParameter(param, Types.INTEGER);

      proc.setInt(param++, (Integer) null);
      proc.setInt(param++, representativeId);
      proc.setString(param++, subscriptionNumber);
      proc.setString(param++, userid);
      proc.execute();

      param = 1;
      numRowsUpdated = proc.getInt(param);
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      getLog().error(procName);
      handleException(e);
    }

    return numRowsUpdated;
  }
}
