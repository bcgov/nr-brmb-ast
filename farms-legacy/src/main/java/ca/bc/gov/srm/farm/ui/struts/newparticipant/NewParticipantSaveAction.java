package ca.bc.gov.srm.farm.ui.struts.newparticipant;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.NewParticipant;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.participant.PersonFormData;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

public class NewParticipantSaveAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping, 
      final ActionForm actionForm,
      final HttpServletRequest request, 
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving New Participant...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    NewParticipantForm form = (NewParticipantForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      String userEmail = CurrentUser.getUser().getEmailAddress();
      
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();

      NewParticipantFormData participantFormData = jsonObjectMapper.readValue(form.getNewParticipantJson(), NewParticipantFormData.class);
      NewParticipantValidator validator = NewParticipantValidator.getInstance();
      errors = validator.validate(participantFormData);
      
      if(errors.isEmpty()) {
        NewParticipant participant = convert(participantFormData);
        calculatorService.createNewParticipant(participant, ScenarioTypeCodes.LOCAL, ScenarioCategoryCodes.LOCAL_DATA_ENTRY, userEmail, null, user);
      } else {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }

  private NewParticipant convert(NewParticipantFormData formData) throws ParseException {
    NewParticipant participant = new NewParticipant();
    Client client = new Client();
    participant.setClient(client);
    
    client.setParticipantPin(DataParseUtils.parseIntegerObject(formData.getParticipantPin()));
    client.setSin(formData.getSin());
    client.setBusinessNumber(formData.getBusinessNumber());
    client.setTrustNumber(formData.getTrustNumber());
    participant.setProgramYear(DataParseUtils.parseIntegerObject(formData.getProgramYear()));
    participant.setMunicipalityCode(formData.getMunicipalityCode());
    
    Person owner = convert(formData.getOwner());
    client.setOwner(owner);
    
    Person contact = convert(formData.getContact());
    client.setContact(contact);
    
    List<FarmingOperation> farmingOperations = new ArrayList<>();
    
    List<OperationFormData> operationFormDatas = formData.getOperations();
    for (OperationFormData operationFormData : operationFormDatas) {
      FarmingOperation farmingOperation = convert(operationFormData);
      farmingOperations.add(farmingOperation);
    }
    participant.setFarmingOperations(farmingOperations);
    
    return participant;
  }

  private FarmingOperation convert(OperationFormData formData) throws ParseException {
    FarmingOperation fo = new FarmingOperation();
    
    fo.setOperationNumber(DataParseUtils.parseIntegerObject(formData.getOperationNumber()));
    fo.setPartnershipName(formData.getPartnershipName());
    fo.setAccountingCode(formData.getAccountingCode());
    fo.setFiscalYearStart(DataParseUtils.parseDate(formData.getFiscalStartDate()));
    fo.setFiscalYearEnd(DataParseUtils.parseDate(formData.getFiscalEndDate()));
    
    if(StringUtils.isBlank(formData.getPartnershipPin())) {
      fo.setPartnershipPin(0);
    } else {
      fo.setPartnershipPin(DataParseUtils.parseIntegerObject(formData.getPartnershipPin()));
    }
    
    if(StringUtils.isBlank(formData.getPartnershipPercent())) {
      fo.setPartnershipPercent(1.00);
    } else {
      fo.setPartnershipPercent(parsePercent(formData.getPartnershipPercent()));
    }
    
    return fo;
  }

  private double parsePercent(String percentString) throws ParseException {
    double percent = DataParseUtils.parseDouble(percentString);
    percent = MathUtils.round(percent, 2);
    final int hundred = 100;
    percent = Double.valueOf(percent/hundred);
    return percent;
  }
  
  private Person convert(PersonFormData personFormData) {
    Person owner = new Person();
    owner.setFirstName(personFormData.getFirstName());
    owner.setLastName(personFormData.getLastName());
    owner.setCorpName(personFormData.getCorpName());
    owner.setAddressLine1(personFormData.getAddressLine1());
    owner.setAddressLine2(personFormData.getAddressLine2());
    owner.setCity(personFormData.getCity());
    owner.setPostalCode(personFormData.getPostalCode());
    owner.setProvinceState(personFormData.getProvinceState());
    owner.setDaytimePhone(personFormData.getDaytimePhone());
    owner.setEveningPhone(personFormData.getEveningPhone());
    owner.setFaxNumber(personFormData.getFaxNumber());
    owner.setCellNumber(personFormData.getCellNumber());
    owner.setEmailAddress(personFormData.getEmailAddress());
    return owner;
  }
}
