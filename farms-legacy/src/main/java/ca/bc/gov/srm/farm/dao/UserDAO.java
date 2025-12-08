/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ca.bc.gov.srm.farm.domain.FarmUser;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

public class UserDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_USER_PKG";

  private static final String CREATE_USER_PROC = "CREATE_USER";
  private static final String UPDATE_USER_PROC = "UPDATE_USER";
  private static final String DELETE_USER_PROC = "DELETE_USER";
  private static final String GET_USER_BY_USER_GUID_PROC = "GET_USER_BY_USER_GUID";
  private static final String GET_USER_BY_USER_ID_PROC = "GET_USER_BY_USER_ID";
  private static final String GET_ALL_USERS_PROC = "GET_ALL_USERS";

  public void createUser(Transaction transaction, FarmUser farmUser, String user) throws DataAccessException {
    createUsers(transaction, Collections.singletonList(farmUser), user);
  }

  public void createUsers(Transaction transaction, Collection<FarmUser> farmUsers, String user) throws DataAccessException {

    final int paramCount = 7;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "." + CREATE_USER_PROC, paramCount, false);) {

      for (FarmUser farmUser : farmUsers) {

        int param = 1;
        proc.setString(param++, farmUser.getUserGuid());
        proc.setString(param++, farmUser.getSourceDirectory());
        proc.setString(param++, farmUser.getAccountName());
        proc.setString(param++, farmUser.getEmailAddress());
        proc.setIndicator(param++, farmUser.getVerifierInd());
        proc.setIndicator(param++, farmUser.getDeletedInd());
        proc.setString(param++, user);

        if (farmUsers.size() > 1) {
          proc.addBatch();
        } else {
          proc.execute();
        }
      }

      if (farmUsers.size() > 1) {
        proc.executeBatch();
      }

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }

  public void updateUser(Transaction transaction, FarmUser farmUser, String user) throws DataAccessException {
    updateUsers(transaction, Collections.singletonList(farmUser), user);
  }

  public void updateUsers(Transaction transaction, Collection<FarmUser> farmUsers, String user) throws DataAccessException {

    final int paramCount = 8;
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "." + UPDATE_USER_PROC, paramCount, false)) {

      for (FarmUser farmUser : farmUsers) {

        int param = 1;
        proc.setString(param++, farmUser.getUserId());
        proc.setString(param++, farmUser.getUserGuid());
        proc.setString(param++, farmUser.getSourceDirectory());
        proc.setString(param++, farmUser.getAccountName());
        proc.setString(param++, farmUser.getEmailAddress());
        proc.setIndicator(param++, farmUser.getVerifierInd());
        proc.setIndicator(param++, farmUser.getDeletedInd());
        proc.setString(param++, user);

        if (farmUsers.size() > 1) {
          proc.addBatch();
        } else {
          proc.execute();
        }
      }

      if (farmUsers.size() > 1) {
        proc.executeBatch();
      }

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }

  public void deleteUsers(Transaction transaction, String... userGuids) throws DataAccessException {
    deleteUsers(transaction, Arrays.asList(userGuids));
  }

  public void deleteUsers(Transaction transaction, Collection<String> userGuids) throws DataAccessException {

    final int paramCount = 1;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "." + DELETE_USER_PROC, paramCount, false); ) {

      for (String userGuid : userGuids) {

        int param = 1;
        proc.setString(param++, userGuid);

        if (userGuids.size() > 1) {
          proc.addBatch();
        } else {
          proc.execute();
        }
      }

      if (userGuids.size() > 1) {
        proc.executeBatch();
      }

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }

  @SuppressWarnings("resource")
  public final FarmUser getUserByUserGuid(final Transaction transaction, final String userGuid) throws DataAccessException {

    String procName = PACKAGE_NAME + "." + GET_USER_BY_USER_GUID_PROC;
    final int paramCount = 1;
    FarmUser farmUser = null;
    
    Connection connection = getConnection(transaction);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, true);) {
      
      int param = 1;
      proc.setString(param++, userGuid);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet() ) {
        if (rs.next()) {
          farmUser = buildFarmUser(rs);
        }
      }

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }

    return farmUser;
  }
  
  @SuppressWarnings("resource")
  public final FarmUser getUserByUserId(final Transaction transaction, final Integer userId) throws DataAccessException {

    String procName = PACKAGE_NAME + "." + GET_USER_BY_USER_ID_PROC;
    final int paramCount = 1;
    FarmUser farmUser = null;
    
    Connection connection = getConnection(transaction);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, true);) {
      
      int param = 1;
      proc.setInt(param++, userId);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet() ) {
        if (rs.next()) {
          farmUser = buildFarmUser(rs);
        }
      }

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }

    return farmUser;
  }

  @SuppressWarnings("resource")
  public final List<FarmUser> getAllUsers(final Transaction transaction, final Boolean isDeleted) throws DataAccessException {

    String procName = PACKAGE_NAME + "." + GET_ALL_USERS_PROC;
    List<FarmUser> users = new ArrayList<>();
    Connection connection = getConnection(transaction);
    final int paramCount = 1;
    
    String deletedInd = null;
    if (isDeleted != null ) {
      deletedInd = getIndicatorYN(isDeleted);
    }

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, true)) {
      
      int param = 1;
      proc.setString(param++, deletedInd);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet()) {

        while (rs.next()) {
          FarmUser user = buildFarmUser(rs);
          users.add(user);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

    return users;
  }

  private FarmUser buildFarmUser(ResultSet rs) throws SQLException {
    FarmUser user = new FarmUser();

    user.setUserId(new Integer(rs.getInt("user_id")));
    user.setUserGuid(rs.getString("user_guid"));
    user.setSourceDirectory(rs.getString("source_directory"));
    user.setAccountName(rs.getString("account_name"));
    user.setEmailAddress(rs.getString("email_address"));
    user.setVerifierInd(Boolean.valueOf(getIndicator(rs, "verifier_ind")));
    user.setDeletedInd(Boolean.valueOf(getIndicator(rs, "deleted_ind")));
    user.setRevisionCount(new Integer(rs.getInt("revision_count")));
    user.setWhoCreated(rs.getString("who_created"));
    user.setWhenCreated(rs.getDate("when_created"));
    user.setWhoUpdated(rs.getString("who_updated"));
    user.setWhenUpdated(rs.getDate("when_updated"));

    return user;
  }

}
