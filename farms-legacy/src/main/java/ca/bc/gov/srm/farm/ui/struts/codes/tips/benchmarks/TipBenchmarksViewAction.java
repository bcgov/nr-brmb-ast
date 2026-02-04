package ca.bc.gov.srm.farm.ui.struts.codes.tips.benchmarks;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.TipBenchmarkInfo;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.codes.tips.TipsFarmType3Action;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

public class TipBenchmarksViewAction extends TipsFarmType3Action {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute
  (final ActionMapping mapping, 
      final ActionForm actionForm,
      final HttpServletRequest request, 
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Benchmarks...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    TipBenchmarksForm form = (TipBenchmarksForm) actionForm;

    populateForm(form);

    return forward;
  }

  protected void populateForm(TipBenchmarksForm form) throws Exception {
    Integer currentProgramYear = ProgramYearUtils.getCurrentProgramYear();
    final Integer earliestYear = 2019;
    Integer latestBenchmarkYear = null;
    
     CodesService service = ServiceFactory.getCodesService();
     List<TipBenchmarkInfo> existingBenchmarks = service.getTipBenchmarkInfos();
     List<TipBenchmarkInfo> allBenchmarks = new ArrayList<>();
     
     for(Integer year = currentProgramYear; year >= earliestYear; year--) {
       boolean found = false;
       for (TipBenchmarkInfo benchmark : existingBenchmarks) {
         if(year.equals(benchmark.getYear())) {
           found = true;
           allBenchmarks.add(benchmark);
           
           if(latestBenchmarkYear == null) {
             latestBenchmarkYear = year;
           }
           break;
         }
       }
       
       if(!found) {
         TipBenchmarkInfo benchmark = new TipBenchmarkInfo();
         benchmark.setYear(year);
         allBenchmarks.add(benchmark);
       }
     }

     form.setBenchmarkInfos(allBenchmarks);

     TipReportService tipReportService = ServiceFactory.getTipReportService();
     
     boolean benchmarksMatchConfig = true;
     if( latestBenchmarkYear != null ) {
       benchmarksMatchConfig = tipReportService.benchmarksMatchConfig(latestBenchmarkYear);
     }

     form.setLatestBenchmarkYear(latestBenchmarkYear);
     form.setBenchmarksMatchConfig(benchmarksMatchConfig);
  }

}
 
