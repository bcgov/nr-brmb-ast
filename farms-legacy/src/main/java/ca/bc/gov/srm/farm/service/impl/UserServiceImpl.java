/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.dao.UserDAO;
import ca.bc.gov.srm.farm.domain.FarmUser;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.UtilityException;
import ca.bc.gov.srm.farm.list.UserListView;
import ca.bc.gov.srm.farm.security.SecurityUtility;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.UserService;
import ca.bc.gov.srm.farm.transaction.Transaction;

public class UserServiceImpl extends BaseService implements UserService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  public void createUser(FarmUser farmUser, String user) throws ServiceException {
    createUsers(Collections.singletonList(farmUser), user);
  }

  @Override
  public void createUsers(List<FarmUser> farmUsers, String user) throws ServiceException {
    logMethodStart(logger);

    UserDAO userDao = new UserDAO();
    Transaction transaction = null;

    try {
      transaction = openTransaction();
      transaction.begin();

      userDao.createUsers(transaction, farmUsers, user);

      transaction.commit();
    } catch (ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    logMethodEnd(logger);
  }
  
  @Override
  public void updateUser(FarmUser farmUser, String user) throws ServiceException {
    updateUsers(Collections.singletonList(farmUser), user);
  }

  @Override
  public void updateUsers(List<FarmUser> farmUsers, String user) throws ServiceException {
    logMethodStart(logger);

    UserDAO userDao = new UserDAO();
    Transaction transaction = null;

    try {
      transaction = openTransaction();
      transaction.begin();

      userDao.updateUsers(transaction, farmUsers, user);

      transaction.commit();
    } catch (ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    logMethodEnd(logger);
  }

  @Override
  public FarmUser getUserByUserGuid(String userGuid) throws ServiceException {
    logMethodStart(logger);

    UserDAO userDao = new UserDAO();
    Transaction transaction = null;
    FarmUser user = null;

    try {
      transaction = openTransaction();

      user = userDao.getUserByUserGuid(transaction, userGuid);
    } catch (ServiceException se) {
      throw se;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return user;
  }
  
  @Override
  public FarmUser getUserByUserId(Integer userId) throws ServiceException {
    logMethodStart(logger);

    UserDAO userDao = new UserDAO();
    Transaction transaction = null;
    FarmUser user = null;

    try {
      transaction = openTransaction();

      user = userDao.getUserByUserId(transaction, userId);
    } catch (ServiceException se) {
      throw se;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return user;
  }
  
  @Override
  public List<FarmUser> getAllUsers() throws ServiceException  {
    return getAllUsers(null);
  }
  
  @Override
  public List<FarmUser> getAllUsers(Boolean isDeleted) throws ServiceException {
    logMethodStart(logger);

    UserDAO userDao = new UserDAO();
    Transaction transaction = null;
    List<FarmUser> users = new ArrayList<>();

    try {
      transaction = openTransaction();

      users = userDao.getAllUsers(transaction, isDeleted);

    } catch (ServiceException se) {
      throw se;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return users;
  }

  @Override
  public void deleteUserByUser(String userGuid) throws ServiceException {
    logMethodStart(logger);

    UserDAO userDao = new UserDAO();
    Transaction transaction = null;

    try {
      transaction = openTransaction();
      transaction.begin();

      userDao.deleteUsers(transaction, userGuid);

      transaction.commit();
    } catch (ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    logMethodEnd(logger);    
  }
  
  @Override
  public UserListView[] getVerifiersFromWebADE() throws ServiceException {
    SecurityUtility securityUtil = SecurityUtility.getInstance();
    UserListView[] verifiers;
    
    try {
      verifiers = securityUtil.getVerifiers();
    } catch (UtilityException e) {
      throw new ServiceException(e);
    }
    return verifiers;
  }
  
  @Override
  public synchronized void syncVerifiers(String user) throws ServiceException {
    // This method is synchronized to avoid multiple users running this at a time
    
    UserListView[] verifiers =  getVerifiersFromWebADE();
    List<FarmUser> users = getAllUsers(); 
    List<String> verifierGuids = new ArrayList<>();
    List<FarmUser> newVerifiers = new ArrayList<>();
    List<FarmUser> modifiedVerifiers = new ArrayList<>();
    
    for (UserListView ulv : verifiers) {
      verifierGuids.add(ulv.getGuid());
      FarmUser foundVerifier = users.stream()
          .filter(u -> u.getUserGuid().equals(ulv.getGuid())).findAny().orElse(null);
      if (foundVerifier != null) {
        logger.debug("verifier found - update verifier");
        if (!Objects.equals(foundVerifier.getSourceDirectory(), ulv.getSourceDirectory()) ||
            !Objects.equals(foundVerifier.getAccountName(), ulv.getAccountName()) ||
            !Objects.equals(foundVerifier.getEmailAddress(), ulv.getEmailAddress())) {
          logger.debug("data needs syncing");
          foundVerifier.setSourceDirectory(ulv.getSourceDirectory());
          foundVerifier.setAccountName(ulv.getAccountName());
          foundVerifier.setEmailAddress(ulv.getEmailAddress());
          modifiedVerifiers.add(foundVerifier);
        }
      } else {
        logger.debug("verifier not found - create verifier");
        FarmUser userVerifier = new FarmUser();
        userVerifier.setUserGuid(ulv.getGuid());
        userVerifier.setSourceDirectory(ulv.getSourceDirectory());
        userVerifier.setAccountName(ulv.getAccountName());
        userVerifier.setEmailAddress(ulv.getEmailAddress());
        userVerifier.setVerifierInd(true);
        userVerifier.setDeletedInd(false);
        newVerifiers.add(userVerifier);
      }
    }
      
    for (FarmUser u : users) {
      if (!verifierGuids.contains(u.getUserGuid())) {
        logger.debug("verifier not found in WebADE - update DB verifier : " + u.toString());
        u.setDeletedInd(true);
        modifiedVerifiers.add(u);
      }
    }
    
    if( ! newVerifiers.isEmpty()) {
      createUsers(newVerifiers, user);
    }
    
    if( ! modifiedVerifiers.isEmpty()) {
      updateUsers(modifiedVerifiers, user);
    }
  }
  
}
