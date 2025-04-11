package ca.bc.gov.farms.api.rest.v1.spring;

import java.util.List;

import ca.bc.gov.farms.service.api.v1.code.impl.CodeServiceImpl;
import ca.bc.gov.farms.service.api.v1.model.factory.CodeFactory;
import ca.bc.gov.nrs.wfone.common.persistence.code.dao.CodeHierarchyConfig;
import ca.bc.gov.nrs.wfone.common.service.api.code.model.factory.CodeHierarchyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.nrs.wfone.common.persistence.code.dao.CodeTableConfig;
import ca.bc.gov.nrs.wfone.common.service.api.code.model.factory.CodeTableFactory;
import ca.bc.gov.nrs.wfone.common.service.api.code.validation.CodeValidator;

@Configuration
public class CodeServiceApiSpringConfig {

    // Beans provided by EndpointsSpringConfig
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private CodeTableFactory codeTableFactory;
    @Autowired
    private CodeFactory codeFactory;
    @Autowired
    private CodeHierarchyFactory codeHierarchyFactory;

    // Beans provided by CodeTableSpringConfig
    @Autowired
    private List<CodeTableConfig> codeTableConfigs;

    // Beans provided by CodeHierarchySpringConfig
    @Autowired
    private List<CodeHierarchyConfig> codeHierarchyConfigs;

    @Bean
    public CodeValidator codeValidator() {
        CodeValidator result;

        result = new CodeValidator();
        result.setMessageSource(messageSource);

        return result;
    }

    @Bean
    public CodeServiceImpl codeService() {
        CodeServiceImpl result;

        result = new CodeServiceImpl();
        result.setCodeValidator(codeValidator());
        result.setCodeTableFactory(codeTableFactory);
        result.setCodeFactory(codeFactory);
        result.setCodeTableConfigs(codeTableConfigs);
        result.setCodeHierarchyFactory(codeHierarchyFactory);
        result.setCodeHierarchyConfigs(codeHierarchyConfigs);

        return result;
    }

}
