/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.user;

import java.util.List;

import ca.bc.gov.srm.farm.SearchResults;
import ca.bc.gov.srm.farm.list.UserListView;


/**
 * UserSearchResults.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
public class UserSearchResults extends SearchResults {


  /**
   * Creates a new UserSearchResults object.
   *
   * @param  results    Input parameter to initialize class.
   * @param  totalRows  Input parameter to initialize class.
   * @param  startRow   Input parameter to initialize class.
   * @param  nextRow    Input parameter to initialize class.
   */
  public UserSearchResults(final UserListView[] results, final int totalRows,
    final int startRow, final int nextRow) {
    setTotalRows(totalRows);
    setStartRow(startRow);
    setNextRow(nextRow);
    setResultList(results);
  }

  /**
   * getSearchResults.
   *
   * @return  The return value.
   */
  public final UserListView[] getSearchResults() {
    List<UserListView> list = getResultList();

    return list.toArray(new UserListView[list.size()]);
  }

}
