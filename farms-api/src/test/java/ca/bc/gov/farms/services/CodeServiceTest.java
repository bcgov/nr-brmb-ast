package ca.bc.gov.farms.services;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

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

    private static final String TABLE_NAME = "farm_municipality_codes";
    private static final LocalDate EFFECTIVE_DATE = LocalDate.now();
    private static final LocalDate EXPIRY_DATE = EFFECTIVE_DATE.plusYears(1);

    @Test
    @Order(1)
    public void testCreateCode() {
        CodeModel resource = new CodeModel();
        resource.setCode("45");
        resource.setDescription("Test Municipality");
        resource.setEffectiveDate(EFFECTIVE_DATE);
        resource.setExpiryDate(EXPIRY_DATE);
        resource.setUserEmail("testUser");

        CodeModel newResource = codeService.createCode(TABLE_NAME, resource);

        assertThat(newResource.getCode()).isEqualTo("45");
        assertThat(newResource.getDescription()).isEqualTo("Test Municipality");
        assertThat(newResource.getEffectiveDate()).isEqualTo(EFFECTIVE_DATE);
        assertThat(newResource.getExpiryDate()).isEqualTo(EXPIRY_DATE);
    }

    @Test
    @Order(2)
    public void testGetCode() {
        CodeModel resource = null;
        try {
            resource = codeService.getCode(TABLE_NAME, "45");
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        assertThat(resource.getCode()).isEqualTo("45");
        assertThat(resource.getDescription()).isEqualTo("Test Municipality");
        assertThat(resource.getEffectiveDate()).isEqualTo(EFFECTIVE_DATE);
        assertThat(resource.getExpiryDate()).isEqualTo(EXPIRY_DATE);
    }
}
