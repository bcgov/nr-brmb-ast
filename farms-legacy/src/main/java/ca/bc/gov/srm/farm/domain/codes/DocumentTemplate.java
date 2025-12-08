package ca.bc.gov.srm.farm.domain.codes;

public class DocumentTemplate {
  
  private String templateName;
  
  private String templateContent;
  
  public String getTemplateName() {
    return templateName;
  }
  
  public void setTemplateName(String name) {
    this.templateName = name;
  }
  
  public String getTemplateContent() {
    return templateContent;
  }
  
  public void setTemplateContent(String value) {
    this.templateContent = value;
  }
  
}
