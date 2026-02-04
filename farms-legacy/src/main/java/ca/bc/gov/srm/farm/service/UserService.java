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
package ca.bc.gov.srm.farm.service;

import java.util.List;

import ca.bc.gov.srm.farm.domain.FarmUser;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.UserListView;

public interface UserService {

  void createUser(FarmUser farmUser, String user) throws ServiceException;

  void createUsers(List<FarmUser> farmUsers, String user) throws ServiceException;

  void updateUser(FarmUser farmUser, String user) throws ServiceException;
  
  void updateUsers(List<FarmUser> farmUsers, String user) throws ServiceException;

  void deleteUserByUser(String userGuid) throws ServiceException;

  FarmUser getUserByUserGuid(String userGuid) throws ServiceException;

  FarmUser getUserByUserId(Integer userId) throws ServiceException;

  List<FarmUser> getAllUsers() throws ServiceException;

  List<FarmUser> getAllUsers(Boolean isDeleted) throws ServiceException;

  UserListView[] getVerifiersFromWebADE() throws ServiceException;

  void syncVerifiers(String user) throws ServiceException;

}