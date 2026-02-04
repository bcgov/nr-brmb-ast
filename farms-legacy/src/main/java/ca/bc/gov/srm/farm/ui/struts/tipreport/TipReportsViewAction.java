package ca.bc.gov.srm.farm.ui.struts.tipreport;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.domain.tips.TipFarmingOperation;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

public class TipReportsViewAction extends TipReportsAction {

  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    TipReportForm form = (TipReportForm) actionForm;
    populateForm(form);

    return forward;
  }


  protected void populateForm(TipReportForm form) throws ServiceException {
    
    syncFilterContextWithForm(form);

    setYearSelectOptions(form);
    setFarmingOperations(form); // Must be called after setYearSelectOptions(form)
    setJobInProgress(form);
    setBenchmarksMatchConfig(form);
    
    if (form.getReportStatusFilter() == null) {
      form.setReportStatusFilter("generated");
    }
    
    TipReportService service = ServiceFactory.getTipReportService();
    form.setBenchmarkDataGenerated(service.checkBenchmarkDataGenerated(form.getYear()));
  }


  private void setYearSelectOptions(TipReportForm form) {
    List<ListView> yearSelectOptions = new ArrayList<>();
    Integer currentProgramYear = ProgramYearUtils.getCurrentProgramYear();
    final Integer earliestYear = 2019;
    
    if (form.getYear() == null) {
      form.setYear(currentProgramYear);
    }
    
    for(Integer currentYear = currentProgramYear; currentYear >= earliestYear; currentYear--) {
      ListView lv = new CodeListView(currentYear.toString(), currentYear.toString());
      yearSelectOptions.add(lv);
    }
   
    form.setYearSelectOptions(yearSelectOptions);
  }
  
  private void setFarmingOperations(TipReportForm form) throws ServiceException {
    TipReportService service = ServiceFactory.getTipReportService();
    
    List<TipFarmingOperation> farmingOperations = service.getTipFarmingOperations(form.getYear());
    form.setFarmOps(farmingOperations);
    form.setNumFarmOps(farmingOperations.size());
  }
  
  private void setJobInProgress(TipReportForm form) throws ServiceException {
    form.setJobInProgress(false);
    
    ImportService service = ServiceFactory.getImportService();
    List<String> importTypes = new ArrayList<>();
    importTypes.add(ImportClassCodes.TIP_REPORT);
    List<ImportSearchResult> searchResults = service.searchImports(importTypes);
    
    for (ImportSearchResult result : searchResults) {
      if (ImportStateCodes.isInProgress(result.getStateCode())) {
        form.setJobInProgress(true);
        break;  
      }
    }
  }
  
  private void setBenchmarksMatchConfig(TipReportForm form) throws ServiceException {
    TipReportService service = ServiceFactory.getTipReportService();

    boolean benchmarksMatchConfig = service.benchmarksMatchConfig(form.getYear());
    form.setBenchmarksMatchConfig(benchmarksMatchConfig);
  }
}