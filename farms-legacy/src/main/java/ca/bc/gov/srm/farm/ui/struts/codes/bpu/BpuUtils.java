package ca.bc.gov.srm.farm.ui.struts.codes.bpu;

import java.util.List;

import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.domain.codes.BPUYear;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * Common BPU code.
 */
public final class BpuUtils {
	/** Creates a new BpuUtils object. */
	private BpuUtils(){}
	
	
	public static BPU findById(List<BPU> bpus, Integer idToLookFor) {
  	BPU found = null;
  	
  	for(int ii = 0; ii < bpus.size(); ii++ ) {
  		BPU current = bpus.get(ii);
  		
  		if(current.getBpuId().equals(idToLookFor)) {
  			found = current;
  			break;
  		}
  	}
  	
  	return found;
  }
	
	
	public static boolean checkBpuExists(
      List<BPU> bpus,
      final String invSgCode,
      final String municipalityCode)
      throws Exception {
    BPU found = null;
    
  	for(int ii = 0; ii < bpus.size(); ii++ ) {
  		BPU current = bpus.get(ii);
  		
  		if(current.getInvSgCode().equals(invSgCode) && 
  			 current.getMunicipalityCode().equals(municipalityCode)) {
  			found = current;
  			break;
  		}
  	}
  	
    return (found != null);
  }
	
	
	/**
   * @param form form
	 * @param setEditableFields setEditableFields
   * @throws Exception On Exception
   */
	public static void populateFormForOneBPU(BPUsForm form, boolean setEditableFields) throws Exception {
    CodesService service = ServiceFactory.getCodesService();
    List<BPU> bpus = service.getBPUs(form.getYearFilter());
    BPU bpu = BpuUtils.findById(bpus, form.getBpuId());
    
    if(bpu != null) {
      form.setInvSgCodeDescription(bpu.getInvSgCodeDescription());
      form.setMunicipalityCodeDescription(bpu.getMunicipalityCodeDescription());

      if(setEditableFields) {
        form.setInvSgCode(bpu.getInvSgCode());
        form.setMunicipalityCode(bpu.getMunicipalityCode());
        
        BPUYear[] years = bpu.getYears();
        for(int ii = 0; ii < years.length; ii++) {
          if(years[ii] != null) {
            form.getAverageMargins()[ii] = StringUtils.toString(years[ii].getAverageMargin());
            form.getAverageExpenses()[ii] = StringUtils.toString(years[ii].getAverageExpense());
            form.getRevisionCounts()[ii] = StringUtils.toString(years[ii].getRevisionCount());
          }
        }
      }
    }
  }
}
