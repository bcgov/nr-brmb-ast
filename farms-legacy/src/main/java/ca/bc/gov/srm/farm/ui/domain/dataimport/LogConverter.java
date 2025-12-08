package ca.bc.gov.srm.farm.ui.domain.dataimport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ca.bc.gov.srm.farm.ui.domain.resultsimport.COMMODITYCODEType;
import ca.bc.gov.srm.farm.ui.domain.resultsimport.IMPORTLOG;
import ca.bc.gov.srm.farm.ui.domain.resultsimport.PARTICIPANTType;
import ca.bc.gov.srm.farm.ui.domain.resultsimport.PRODUCTIONUNITType;
import ca.bc.gov.srm.farm.ui.domain.resultsimport.PROGRAMYEARType;
import ca.bc.gov.srm.farm.ui.domain.resultsstaging.ERRORType;
import ca.bc.gov.srm.farm.ui.domain.resultsstaging.STAGINGLOG;
import ca.bc.gov.srm.farm.ui.domain.resultsstaging.WARNINGType;

/**
 * Orginally the import XML was parsed using Oracle XML
 * queries. But the queries were way too slow with large
 * XML clobs, so now we use JAXB to parse the XML. This class
 * turns the gruesome JAXB Java objects into more usable objects.
 */
public class LogConverter {
	
	/** TRUE */
	private static final String TRUE = "TRUE";

	/**
	 * 
	 * @param log log
	 * @param results results
	 */
	public void convertImportLog(IMPORTLOG log, ImportResults results) {
		List<FileLineMessage> errors = getTopLevelErrors(log);
    results.setErrors(errors);
    List<FileLineMessage> warnings = getTopLevelWarnings(log);
    results.setWarnings(warnings);
    
    if (errors.size() == 0) {
      List commodities = getImportCommodities(log);
      results.setCommodities(commodities);

      List units = getImportProductionUnits(log);
      results.setProductionUnits(units);

      List accounts = getImportAccounts(log);
      results.setAccounts(accounts);
    }
	}
	
	
	/**
	 * 
	 * @param log log
	 * @param results results
	 */
	public void convertStagingLog(STAGINGLOG log, StagingResults results) {
		List errors = getStagingErrors(log);
    results.setErrors(errors);
    
    List warnings = getStagingWarnings(log);
    results.setWarnings(warnings);
    
    if(log.getNUMTOTAL() != null) {
    	results.setNumberOfItems(log.getNUMTOTAL().intValue());
    }
	}
	
	
	/**
	 * @param log the log
	 * @return list of FileLineMessage
	 */
	private List getStagingErrors(STAGINGLOG log) {
		ArrayList items = new ArrayList();
		
		if(log.getERRORS() != null) {
		  Iterator iter = log.getERRORS().getERROR().iterator();
		  
		  while(iter.hasNext()) {
		  	ERRORType err = (ERRORType) iter.next();
		  	FileLineMessage flm = new FileLineMessage();

		  	if(err.getFileNumber() != null) {
		  		flm.setFileNumber(err.getFileNumber().toString());
		  	}
		  	
		  	if(err.getRowNumber() != null) {
		  		flm.setLineNumber(err.getRowNumber().toString());
		  	}
		  	
        flm.setMessage(formatErrorMessage(err.getValue()));

        items.add(flm);
		  }
		}
		
		// sort by line number
		Collections.sort(items, new FileLineMessageComparator());
		
		return items;
	}
	
	
	/**
	 * @param log the log
	 * @return list of FileLineMessage
	 */
	private List getStagingWarnings(STAGINGLOG log) {
		ArrayList items = new ArrayList();
		
		if(log.getWARNINGS() != null) {
		  Iterator iter = log.getWARNINGS().getWARNING().iterator();
		  
		  while(iter.hasNext()) {
		  	WARNINGType warning = (WARNINGType) iter.next();
		  	FileLineMessage flm = new FileLineMessage();

		  	if(warning.getFileNumber() != null) {
		  		flm.setFileNumber(warning.getFileNumber().toString());
		  	}
		  	
		  	if(warning.getRowNumber() != null) {
		  		flm.setLineNumber(warning.getRowNumber().toString());
		  	}
		  	
        flm.setMessage(formatErrorMessage(warning.getValue()));

        items.add(flm);
		  }
		}
		
    // sort by line number
		Collections.sort(items, new FileLineMessageComparator());
		
		return items;
	}
	
	
	/**
	 * @param log the log
	 * @return list of FileLineMessage
	 */
	private List<FileLineMessage> getTopLevelErrors(IMPORTLOG log) {
		ArrayList<FileLineMessage> items = new ArrayList<>();
		
		if(log.getERROR() != null) {
		  Iterator<String> iter = log.getERROR().iterator();
		  
		  while(iter.hasNext()) {
		  	String err = iter.next();
		  	FileLineMessage flm = new FileLineMessage();

        flm.setMessage(formatErrorMessage(err));

        items.add(flm);
		  }
		}
		
		return items;
	}
	
	
	/**
	 * @param log the log
	 * @return list of FileLineMessage
	 */
	private List<FileLineMessage> getTopLevelWarnings(IMPORTLOG log) {
	  ArrayList<FileLineMessage> items = new ArrayList<>();
	  
	  if(log.getWARNING() != null) {
	    Iterator<String> iter = log.getWARNING().iterator();
	    
	    while(iter.hasNext()) {
	      String err = iter.next();
	      FileLineMessage flm = new FileLineMessage();
	      
	      flm.setMessage(formatErrorMessage(err));
	      
	      items.add(flm);
	    }
	  }
	  
	  return items;
	}
	
	
	
	/**
	 * @param log the log
	 * @return list of CommodityCode
	 */
	private List getImportCommodities(IMPORTLOG log) {
		ArrayList items = new ArrayList();
		
		if(log.getCOMMODITIES() != null) {
		  Iterator iter = log.getCOMMODITIES().getCOMMODITYCODE().iterator();
		  
		  while(iter.hasNext()) {
		  	COMMODITYCODEType logObj = (COMMODITYCODEType) iter.next();
		  	CommodityCode result = new CommodityCode();
		  	
		  	result.setCode(logObj.getCode());
		  	result.setErrorMessage(formatErrorMessage(logObj.getERROR()));
		  	result.setTable(logObj.getTable());
  			result.setNewDescription(logObj.getATTR().getNew());
        result.setOldDescription(logObj.getATTR().getOld());

        items.add(result);
		  }
		}
		
		return items;
	}
	
	
	/**
	 * @param log the log
	 * @return list of ProductionUnit
	 */
	private List getImportProductionUnits(IMPORTLOG log) {
		ArrayList items = new ArrayList();
		
		if(log.getPRODUCTIONUNITS() != null) {
		  Iterator iter = log.getPRODUCTIONUNITS().getPRODUCTIONUNIT().iterator();
		  
		  while(iter.hasNext()) {
		  	PRODUCTIONUNITType logObj = (PRODUCTIONUNITType) iter.next();
		  	ProductionUnit result = new ProductionUnit();
		  	
		  	result.setCode(logObj.getCropUnitCode());
		  	result.setErrorMessage(formatErrorMessage(logObj.getERROR()));
  		  result.setNewDescription(logObj.getATTR().getNew());
        result.setOldDescription(logObj.getATTR().getOld());

        items.add(result);
		  }
		}
		
		return items;
	}
	
	
	/**
	 * @param log the log
	 * @return list of ProductionUnit
	 */
	private List getImportAccounts(IMPORTLOG log) {
		ArrayList items = new ArrayList();
		
		if(log.getPARTICIPANTS() != null) {
		  Iterator iter = log.getPARTICIPANTS().getPARTICIPANT().iterator();
		  
		  while(iter.hasNext()) {
		  	PARTICIPANTType logObj = (PARTICIPANTType) iter.next();
		  	
		  	//
		  	// Users don't want to see accounts where nothing changed.
		  	//
		  	if(TRUE.equalsIgnoreCase(logObj.getAltered())) {
			  	AccountSummary result = new AccountSummary();
			  	
			  	if(StringUtils.isNotEmpty(logObj.getCorpName())) {
			  		result.setName(logObj.getCorpName());
			  	} else {
			  		String name = logObj.getFirstName() + " " + 
			  		              logObj.getLastName();
			  		result.setName(name);
			  	}
			  	
			  	result.setPin(logObj.getPin().toString());
			  	
		      String errMsg = getErrorMessage(logObj);
		      
		      //
		      // Note that we want the status field to be sortable
		      // so that errors appear first followed by warnings, 
		      // then sucesses.
		      //
		      if(StringUtils.isEmpty(errMsg)) {
			  		String warnMsg = getWarningMessage(logObj);
			  		
			  		if(StringUtils.isEmpty(warnMsg)) {
			  			result.setStatus("ZZZ_SUCCESS");
			  		} else {
			  			result.setStatus("WARNING");
				  		result.setErrorMessage(warnMsg);
			  		}
			  	} else {
			  		result.setStatus("ERROR");
			  		result.setErrorMessage(errMsg);
			  	}
	
	        items.add(result);
		  	}
		  }
		}
		
		//
		// sort the accounts so that the failed ones appear first
		//
		Collections.sort(items, new AccountSummaryComparator());
		
		return items;
	}
	
	
	/**
	 * An error can happen in a lot of places for a particpant.
	 * 
	 * @param part participant 
	 * @return error message
	 */
	private String getErrorMessage(PARTICIPANTType part) {
		if(part.getPROGRAMYEARS() != null) {
			Iterator iter = part.getPROGRAMYEARS().getPROGRAMYEAR().iterator();
			
			while(iter.hasNext()) {
				PROGRAMYEARType py = (PROGRAMYEARType) iter.next();
				
				if(py.getERROR() != null) {
					int numErrors = py.getERROR().size();
					
					if(numErrors > 0) {
						String err = (String) py.getERROR().get(0);
						return formatErrorMessage(err);
					}
				}
			}
		}
		
		if(part.getAGRISTATIBLITYCLIENT() != null) {
			String msg = part.getAGRISTATIBLITYCLIENT().getERROR();
			return formatErrorMessage(msg);
		}
		
		return null;
	}
	
	
	/**
	 * An error can happen in a lot of places for a particpant.
	 * 
	 * @param part participant 
	 * @return error message
	 */
	private String getWarningMessage(PARTICIPANTType part) {
		if(part.getPROGRAMYEARS() != null) {
			Iterator iter = part.getPROGRAMYEARS().getPROGRAMYEAR().iterator();
			
			while(iter.hasNext()) {
				PROGRAMYEARType py = (PROGRAMYEARType) iter.next();
				
				if(py.getWARNING() != null) {
					int numWarnings = py.getWARNING().size();
					
					if(numWarnings > 0) {
						String msg = (String) py.getWARNING().get(0);
						return formatErrorMessage(msg);
					}
				}
			}
		}
		
		return null;
	}
	
	
	/**
  * We are showing the errors in a YUI datatable, and newlines
  * (from stack traces) screw up the javascript.
  * 
  * @param errMsg unformatted message
  * @return error message
  */
	public static String formatErrorMessage(String errMsg) {
		String msg = null;
		
		if(errMsg != null) {
			msg = StringUtils.replace(errMsg, "\n", " ");
		}
		
		return msg;
	}
	
  /**
   * 
   */	
	class AccountSummaryComparator implements Comparator {
		/**
		 * @param obj1 obj1
		 * @param obj2 obj2
		 * @return int
		 */
		@Override
    public int compare(Object obj1, Object obj2){
			int rc = 0;
			
			AccountSummary as1 = (AccountSummary) obj1;
			AccountSummary as2 = (AccountSummary) obj2;
			
			if(as1.getStatus().equalsIgnoreCase(as2.getStatus())) {
				rc = as1.getPin().compareTo(as2.getPin());
			} else {
				rc = as1.getStatus().compareTo(as2.getStatus());
			}
			
			return rc;
		}
	}

	/**
   * 
   */
	class FileLineMessageComparator implements Comparator {
		/**
		 * @param obj1 obj1
		 * @param obj2 obj2
		 * @return int
		 */
		@Override
    public int compare(Object obj1, Object obj2){
			int rc = 0;
			
			FileLineMessage flm1 = (FileLineMessage) obj1;
			FileLineMessage flm2 = (FileLineMessage) obj2;
			
			if(flm1.getLineNumber() != null && flm2.getLineNumber() != null) {
				Integer i1 = new Integer(flm1.getLineNumber());
				Integer i2 = new Integer(flm2.getLineNumber());
				rc = i1.compareTo(i2);
			}
			
			return rc;
		}
	}

}
