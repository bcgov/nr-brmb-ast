/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.sort;

import ca.bc.gov.srm.farm.Organization;
import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.list.UserListView;


/**
 * Sorter.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 1093 $
 */
public final class Sorter {

  /** Creates a new Sorter object. */
  private Sorter() {
  }

  /**
   * sort.
   *
   * @param   organizations  The parameter value.
   *
   * @return  The return value.
   */
  public static Organization[] sort(final Organization[] organizations) {
    String[] sortOrder = new String[] {"name"};

    return (Organization[]) SortUtils.sort(organizations, sortOrder);
  }

  /**
   * sort.
   *
   * @param   users  The parameter value.
   *
   * @return  The return value.
   */
  public static User[] sort(final User[] users) {
    String[] sortOrder = new String[] {"userId"};

    return (User[]) SortUtils.sort(users, sortOrder);
  }

  /**
   * sort.
   *
   * @param   users  The parameter value.
   *
   * @return  The return value.
   */
  public static UserListView[] sort(final UserListView[] users) {
    String[] sortOrder = new String[] {"label"};

    return (UserListView[]) SortUtils.sort(users, sortOrder);
  }

  /**
   * sort.
   *
   * @param   items  The parameter value.
   *
   * @return  The return value.
   */
  public static ListView[] sort(final ListView[] items) {
    String[] sortOrder = new String[] {"label"};

    return (ListView[]) SortUtils.sort(items, sortOrder);
  }
}
