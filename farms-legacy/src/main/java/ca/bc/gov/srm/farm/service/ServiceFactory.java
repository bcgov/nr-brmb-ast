/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.service;

import ca.bc.gov.srm.farm.factory.BaseFactory;

public class ServiceFactory extends BaseFactory {

  /** instance. */
  private static ServiceFactory instance = new ServiceFactory();

  private static ListService listService = null;
  
  private static ConfigurationService configurationService = null;
  
  private static BenefitService benefitService = null;

  private static ImportService importService = null;

  private static SearchService searchService = null;

  private static ClientAccountService clientAccountService = null;

  private static SubscriptionService subscriptionService = null;

  private static ReportService reportService = null;
  
  private static TipReportService tipReportService = null;
  
  private static FmvService fmvService = null;
  
  private static BpuService bpuService = null;

  private static IvprService ivprService = null;

  private static NegativeMarginService negativeMarginService = null;

  private static AarmService aarmService = null;
  
  private static EnrolmentService enrolmentService = null;
  
  private static CalculatorService calculatorService = null;
  
  private static AdjustmentService adjustmentService = null;
  
  private static DiffReportService diffReportService = null;
  
  private static StructuralChangeService structuralChangeService = null;

  private static CodesService codesService = null;
  
  private static CrmTransferService crmTransferService = null;
  
  private static PreVerificationService preVerificationService = null;

  private static ChefsService chefsService = null;
  
  private static ChefsSubmissionProcessorService chefsSubmissionProcessorService = null;

  private static ChefsFormSubmissionService chefsFormSubmissionService = null;

  private static CdogsService cdogsService = null;

  private static FifoService fifoService = null;

  private static UserService userService = null;
  
  /**
   * getListService.
   *
   * @return  The return value.
   */
  public static ListService getListService() {

    if (listService == null) {
      listService = (ListService) instance.createUnsecuredService(
          ListService.class);
    }

    return listService;
  }
  
  public static ConfigurationService getConfigurationService() {
    
    if (configurationService == null) {
      configurationService = (ConfigurationService) instance.createUnsecuredService(
          ConfigurationService.class);
    }
    
    return configurationService;
  }

  /**
   * getImportService.
   *
   * @return  The return value.
   */
  public static ImportService getImportService() {

    if (importService == null) {
      importService = (ImportService) instance.createSecuredService(
          ImportService.class);
    }

    return importService;
  }

  /**
   * getSubscriptionService.
   *
   * @return  The return value.
   */
  public static SubscriptionService getSubscriptionService() {

    if (subscriptionService == null) {
      subscriptionService = (SubscriptionService) instance.createSecuredService(
          SubscriptionService.class);
    }

    return subscriptionService;
  }

  /**
   * getReportService.
   *
   * @return  The return value.
   */
  public static ReportService getReportService() {

    if (reportService == null) {
      reportService = (ReportService) instance.createSecuredService(
          ReportService.class);
    }

    return reportService;
  }
  
  /**
   * getTipReportService.
   *
   * @return  The return value.
   */
  public static TipReportService getTipReportService() {

    if (tipReportService == null) {
      tipReportService = (TipReportService) instance.createSecuredService(
          TipReportService.class);
    }

    return tipReportService;
  }

  /**
   * getSearchService.
   *
   * @return  The return value.
   */
  public static SearchService getSearchService() {

    if (searchService == null) {
      searchService = (SearchService) instance.createSecuredService(
          SearchService.class);
    }

    return searchService;
  }

  /**
   * getClientAccountService.
   *
   * @return  The return value.
   */
  public static ClientAccountService getClientAccountService() {

    if (clientAccountService == null) {
      clientAccountService = (ClientAccountService)
        instance.createSecuredService(ClientAccountService.class);
    }

    return clientAccountService;
  }
  
  
  /**
   * getFmvService.
   *
   * @return  The return value.
   */
  public static FmvService getFmvService() {

    if (fmvService == null) {
    	fmvService = (FmvService) instance.createSecuredService(
      		FmvService.class);
    }

    return fmvService;
  }
  
  /**
   * getBpuService.
   *
   * @return  The return value.
   */
  public static BpuService getBpuService() {

    if (bpuService == null) {
    	bpuService = (BpuService) instance.createSecuredService(
      		BpuService.class);
    }

    return bpuService;
  }

  /**
   * getIvprService.
   *
   * @return The return value.
   */
  public static IvprService getIvprService() {

    if (ivprService == null) {
      ivprService = (IvprService) instance.createSecuredService(
          IvprService.class);
    }

    return ivprService;
  }

  /**
   * getNegativeMarginService.
   *
   * @return  The return value.
   */
  public static NegativeMarginService getNegativeMarginService() {

    if (negativeMarginService == null) {
    	negativeMarginService = (NegativeMarginService) instance.createSecuredService(
      		NegativeMarginService.class);
    }

    return negativeMarginService;
  }

  /**
   * getAarmService.
   *
   * @return  The return value.
   */
  public static AarmService getAarmService() {

    if (aarmService == null) {
    	aarmService = (AarmService) instance.createSecuredService(
      		AarmService.class);
    }

    return aarmService;
  }

  /**
   * @return Calculator2Service
   */
  public static CalculatorService getCalculatorService() {
    
    if (calculatorService == null) {
      calculatorService = (CalculatorService) instance.createSecuredService(
          CalculatorService.class);
    }
    
    return calculatorService;
  }
  
  /**
   * @return AdjustmentService
   */
  public static AdjustmentService getAdjustmentService() {
    
    if (adjustmentService == null) {
      adjustmentService = (AdjustmentService) instance.createSecuredService(
          AdjustmentService.class);
    }
    
    return adjustmentService;
  }
  
  /**
   * @return DiffReportService
   */
  public static DiffReportService getDiffReportService() {
    
    if (diffReportService == null) {
      diffReportService = (DiffReportService) instance.createSecuredService(
          DiffReportService.class);
    }
    
    return diffReportService;
  }
  
  
  /**
   * @return  The return value.
   */
  public static EnrolmentService getEnrolmentService() {
    
    if (enrolmentService == null) {
      enrolmentService = (EnrolmentService) instance.createSecuredService(
          EnrolmentService.class);
    }
    
    return enrolmentService;
  }

  
  /**
   * 
   * @return BenefitService
   */
  public static BenefitService getBenefitService() {
    
    if (benefitService == null) {
    	benefitService = (BenefitService) instance.createSecuredService(
    			BenefitService.class);
    }
    
    return benefitService;
  }
  
  /**
   * 
   * @return StructuralChangeService
   */
  public static StructuralChangeService getStructuralChangeService() {
    
    if (structuralChangeService == null) {
    	structuralChangeService = (StructuralChangeService) 
    	  instance.createSecuredService(StructuralChangeService.class);
    }
    
    return structuralChangeService;
  }

  /**
   * @return CodesService
   */
  public static CodesService getCodesService() {
    
    if (codesService == null) {
      codesService = (CodesService) 
      instance.createSecuredService(CodesService.class);
    }
    
    return codesService;
  }
  
  /**
   * @return CrmTransferService
   */
  public static CrmTransferService getCrmTransferService() {
    
    if (crmTransferService == null) {
      crmTransferService = (CrmTransferService) 
      instance.createSecuredService(CrmTransferService.class);
    }
    
    return crmTransferService;
  }
  
  
  public static PreVerificationService getPreVerificationService() {
    
    if (preVerificationService == null) {
      preVerificationService = (PreVerificationService) 
          instance.createSecuredService(PreVerificationService.class);
    }
    
    return preVerificationService;
  }
  
  
  public static ChefsService getChefsService() {

    if (chefsService == null) {
      chefsService = (ChefsService) instance.createSecuredService(
          ChefsService.class);
    }

    return chefsService;
  }

  public static ChefsSubmissionProcessorService getChefsSubmissionProcessorService() {
  	
  	if (chefsSubmissionProcessorService == null) {
  		chefsSubmissionProcessorService = (ChefsSubmissionProcessorService) instance.createSecuredService(
  				ChefsSubmissionProcessorService.class);
    }
  	
		return chefsSubmissionProcessorService;
	}
  
  public static ChefsFormSubmissionService getChefsFormSubmissionService() {

    if (chefsFormSubmissionService == null) {
      chefsFormSubmissionService = (ChefsFormSubmissionService) instance.createSecuredService(ChefsFormSubmissionService.class);
    }

    return chefsFormSubmissionService;
  }

  public static CdogsService getCdogsService() {

    if (cdogsService == null) {
      cdogsService = (CdogsService) instance.createSecuredService(CdogsService.class);
    }

    return cdogsService;
  }

  public static FifoService getFifoService() {

    if (fifoService == null) {
      fifoService = (FifoService) instance.createSecuredService(FifoService.class);
    }

    return fifoService;
  }
  
  public static UserService getUserService() {

    if (userService == null) {
      userService = (UserService) instance.createSecuredService(UserService.class);
    }

    return userService;
  }

	/**
   * createSecuredService.
   *
   * @param   interfaceToCreate  The parameter value.
   *
   * @return  The return value.
   */
  Object createSecuredService(final Class<?> interfaceToCreate) {
    String implementingClassName = instance.findImplementingClassName(
        interfaceToCreate, null);
    SecuredServiceProxy proxy = new SecuredServiceProxy(interfaceToCreate);

    return instance.createProxiedInstance(interfaceToCreate,
        implementingClassName, proxy);
  }

  /**
   * createUnsecuredService.
   *
   * @param   interfaceToCreate  The parameter value.
   *
   * @return  The return value.
   */
  Object createUnsecuredService(final Class<?> interfaceToCreate) {
    String implementingClassName = instance.findImplementingClassName(
        interfaceToCreate, null);

    return instance.createInstance(interfaceToCreate, implementingClassName);
  }
  
  
  /**
   * JUnit work-around.
   * @return ServiceFactory
   */
  public static ServiceFactory getInstance() {
  	return instance;
  }
}
