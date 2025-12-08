/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.user;

import java.util.List;

import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.list.UserListView;
import ca.bc.gov.srm.farm.security.BusinessRole;


/**
 * UserProvider.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
public interface UserProvider {

  /**
   * getSourceDirectoryList.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  ListView[] getSourceDirectoryList() throws ProviderException;

  /**
   * getUserList.
   *
   * @param   role  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  UserListView[] getUserList(BusinessRole role) throws ProviderException;

  /**
   * getUserList.
   *
   * @param   businessRole  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  UserListView[] getUserList(final List<BusinessRole> businessRoles)
    throws ProviderException;


  /**
   * search.
   *
   * @param   sourceDirectory  The parameter value.
   * @param   firstName        The parameter value.
   * @param   lastName         The parameter value.
   * @param   email            The parameter value.
   * @param   startRow         The parameter value.
   * @param   rowCount         The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  UserSearchResults search(String sourceDirectory, String firstName,
    String lastName, String email, int startRow, int rowCount)
    throws ProviderException;
}
