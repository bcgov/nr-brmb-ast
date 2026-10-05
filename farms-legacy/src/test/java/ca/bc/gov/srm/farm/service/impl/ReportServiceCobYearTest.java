package ca.bc.gov.srm.farm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ca.bc.gov.srm.farm.exception.ServiceException;

/** These rejection tests require no datasource, report server or application session. */
class ReportServiceCobYearTest {
  private static final String HISTORICAL_MESSAGE =
      "COB generation and reprinting are only available for program years 2023 onward. "
      + "Existing saved COB reports can still be viewed.";

  @ParameterizedTest
  @ValueSource(ints = {2012, 2013, 2016, 2017, 2018, 2019, 2020, 2021, 2022})
  void rejectsHistoricalPrintBeforeAccessingDatabaseOrReportServer(int year) {
    ReportServiceImpl service = new ReportServiceImpl();
    ServiceException error = assertThrows(ServiceException.class,
        () -> service.saveCob(1081568, year, "test"));
    assertEquals(HISTORICAL_MESSAGE, error.getMessage());
  }

  @ParameterizedTest
  @ValueSource(ints = {2012, 2013, 2016, 2017, 2018, 2019, 2020, 2021, 2022})
  void rejectsHistoricalReprintBeforeTouchingSavedDocument(int year) {
    ReportServiceImpl service = new ReportServiceImpl();
    ServiceException error = assertThrows(ServiceException.class,
        () -> service.updateCob(1081568, year, "test"));
    assertEquals(HISTORICAL_MESSAGE, error.getMessage());
  }

  @Test
  void rejectsMissingYear() {
    ReportServiceImpl service = new ReportServiceImpl();
    ServiceException error = assertThrows(ServiceException.class,
        () -> service.saveCob(1081568, null, "test"));
    assertEquals("A scenario ID and program year are required to generate a COB.", error.getMessage());
  }
}
