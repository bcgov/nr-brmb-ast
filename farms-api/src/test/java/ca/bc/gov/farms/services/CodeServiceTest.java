package ca.bc.gov.farms.services;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.models.CodeModel;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CodeServiceTest {

    @Autowired
    private CodeService codeService;

    private static String code;

    @Test
    @Order(1)
    public void testGetCode() {
        CodeModel resource = null;
        try {
            resource = codeService.getCode("farm_municipality_codes", "41");
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();
    }
}
