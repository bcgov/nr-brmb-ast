package ca.bc.gov.srm.farm.util;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;

public final class UiUtils {

  /** private constructor */
  private UiUtils() {
  }

  public static List<ListView> getSelectOptions(int[] options) {
    List<ListView> programYearSelectOptions = new ArrayList<>();
    
    for(int curYear : options) {
      String year = Integer.toString(curYear);
      ListView lv = new CodeListView(year, year);
      programYearSelectOptions.add(lv);
    }
    return programYearSelectOptions;
  }

  public static List<ListView> getAdminYearsSelectOptions() {
    int[] adminYears = ProgramYearUtils.getAdminYears();
    List<ListView> programYearSelectOptions = getSelectOptions(adminYears);
    return programYearSelectOptions;
  }

}
