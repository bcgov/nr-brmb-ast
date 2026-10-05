package ca.bc.gov.srm.farm.report;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.digester.Digester;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.ConfigRuleSet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.FormPropertyConfig;
import org.apache.struts.config.impl.ModuleConfigImpl;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.ui.struts.SecureActionMapping;
import ca.bc.gov.srm.farm.ui.struts.report.ReportForm;

/** Focused shared-library checks, not a replacement for a deployed Struts smoke test. */
class CobStrutsCompatibilityTest {
  @Test
  void parsesRealStrutsConfigurationAndPreservesCobPermissions() throws Exception {
    Path project = Paths.get(System.getProperty("farms.project.dir", "."));
    Path webInf = project.resolve("src/main/webapp/WEB-INF");
    ModuleConfigImpl module = new ModuleConfigImpl("");
    Digester digester = new Digester();
    digester.setValidating(false);
    digester.setUseContextClassLoader(true);
    digester.addRuleSet(new ConfigRuleSet());
    digester.register("-//Apache Software Foundation//DTD Struts Configuration 1.2//EN",
        webInf.resolve("dtd/struts-config_1_2.dtd").toUri().toURL());
    digester.push(module);
    try (InputStream source = java.nio.file.Files.newInputStream(webInf.resolve("struts-config.xml"))) {
      digester.parse(source);
    }
    assertTrue(module.findActionConfigs().length > 300);
    assertEquals("viewReport", ((SecureActionMapping) module.findActionConfig("/generateBenefitDocument")).getSecureAction());
    assertEquals("regenerateCob", ((SecureActionMapping) module.findActionConfig("/regenerateBenefitDocument")).getSecureAction());
    assertEquals("viewScenario", ((SecureActionMapping) module.findActionConfig("/viewBenefitDocument")).getSecureAction());
    assertNotNull(module.findFormBeanConfig("calculatorStatusForm"));
    assertNotNull(module.findFormBeanConfig("reportForm"));
  }

  @Test
  void populatesTheExistingReportFormUsingRequestStyleArrays() throws Exception {
    ReportForm form = new ReportForm();
    Map<String, String[]> parameters = new HashMap<>();
    parameters.put("pin", new String[] {"4375671"});
    parameters.put("year", new String[] {"2024"});
    parameters.put("reportType", new String[] {"COB"});
    parameters.put("reportUrl", new String[] {"viewBenefitDocument.do"});
    parameters.put("unrecognizedRequestParameter", new String[] {"ignored"});
    BeanUtils.populate(form, parameters);
    assertEquals("4375671", form.getPin());
    assertEquals("2024", form.getYear());
    assertEquals("COB", form.getReportType());
    assertEquals("viewBenefitDocument.do", form.getReportUrl());
  }

  @Test
  void strutsDynamicFormsStillCreateAndConvertTypedProperties() throws Exception {
    FormBeanConfig config = new FormBeanConfig();
    config.setName("cobCompatibilityProbe");
    config.setType("org.apache.struts.action.DynaActionForm");
    config.addFormPropertyConfig(new FormPropertyConfig("scenarioId", "java.lang.Integer", null));
    config.addFormPropertyConfig(new FormPropertyConfig("enabled", "java.lang.Boolean", null));
    DynaBean form = DynaActionFormClass.createDynaActionFormClass(config).newInstance();
    Map<String, String[]> values = new HashMap<>();
    values.put("scenarioId", new String[] {"1081568"});
    values.put("enabled", new String[] {"true"});
    BeanUtils.populate(form, values);
    assertEquals(Integer.valueOf(1081568), form.get("scenarioId"));
    assertEquals(Boolean.TRUE, form.get("enabled"));
  }
}
