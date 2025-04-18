package ca.bc.gov.farms.api.rest.v1.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.farms.api.rest.v1.resource.factory.CodeResourceFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.CodeFactory;
import ca.bc.gov.nrs.wfone.common.api.rest.code.parameters.validation.CodeParameterValidator;
import ca.bc.gov.nrs.wfone.common.api.rest.code.resource.factory.CodeHierarchyResourceFactory;
import ca.bc.gov.nrs.wfone.common.api.rest.code.resource.factory.CodeTableResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.code.model.factory.CodeHierarchyFactory;
import ca.bc.gov.nrs.wfone.common.service.api.code.model.factory.CodeTableFactory;

@Configuration
@Import({
        CodeServiceApiSpringConfig.class
})
public class CodeEndpointsSpringConfig {

    // Beans provided by EndpointsSpringConfig
    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Bean
    public CodeParameterValidator codeParameterValidator() {
        CodeParameterValidator result;

        result = new CodeParameterValidator();
        result.setMessageSource(messageSource);

        return result;
    }

    @Bean
    public CodeTableFactory codeTableFactory() {
        CodeTableResourceFactory result;

        result = new CodeTableResourceFactory();

        return result;
    }

    @Bean
    public CodeFactory codeFactory() {
        CodeResourceFactory result;

        result = new CodeResourceFactory();

        return result;
    }

    @Bean
    public CodeHierarchyFactory codeHierarchyFactory() {
        CodeHierarchyResourceFactory result;

        result = new CodeHierarchyResourceFactory();

        return result;
    }
}
