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
import ca.bc.gov.farms.data.models.StructureGroupAttributeModel;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StructureGroupAttributeServiceTest {

    @Autowired
    private StructureGroupAttributeService structureGroupAttributeService;

    private static Long structureGroupAttributeId;

    @Test
    @Order(1)
    public void testCreateStructureGroupAttribute() {
        StructureGroupAttributeModel resource = new StructureGroupAttributeModel();
        resource.setStructureGroupCode("100");
        resource.setRollupStructureGroupCode("120");
        resource.setUserEmail("testUser");

        StructureGroupAttributeModel newResource = structureGroupAttributeService
                .createStructureGroupAttribute(resource);
        structureGroupAttributeId = newResource.getStructureGroupAttributeId();

        assertThat(newResource.getStructureGroupCode()).isEqualTo("100");
        assertThat(newResource.getStructureGroupDesc()).isEqualTo("Alpaca");
        assertThat(newResource.getRollupStructureGroupCode()).isEqualTo("120");
        assertThat(newResource.getRollupStructureGroupDesc()).isEqualTo("Other Livestock");
    }

    @Test
    @Order(2)
    public void testGetStructureGroupAttributesByStructureGroupCode() {
        StructureGroupAttributeModel resource = structureGroupAttributeService
                .getStructureGroupAttributesByStructureGroupCode("100");

        assertThat(resource).isNotNull();
        assertThat(resource.getStructureGroupCode()).isEqualTo("100");
        assertThat(resource.getStructureGroupDesc()).isEqualTo("Alpaca");
        assertThat(resource.getRollupStructureGroupCode()).isEqualTo("120");
        assertThat(resource.getRollupStructureGroupDesc()).isEqualTo("Other Livestock");
    }

    @Test
    @Order(3)
    public void testUpdateStructureGroupAttribute() {
        StructureGroupAttributeModel resource = null;
        try {
            resource = structureGroupAttributeService.getStructureGroupAttribute(structureGroupAttributeId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setStructureGroupCode("100");
        resource.setRollupStructureGroupCode("300");
        resource.setUserEmail("testUser");

        StructureGroupAttributeModel updatedResource = null;
        try {
            updatedResource = structureGroupAttributeService.updateStructureGroupAttribute(structureGroupAttributeId,
                    resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getStructureGroupCode()).isEqualTo("100");
        assertThat(updatedResource.getStructureGroupDesc()).isEqualTo("Alpaca");
        assertThat(updatedResource.getRollupStructureGroupCode()).isEqualTo("300");
        assertThat(updatedResource.getRollupStructureGroupDesc()).isEqualTo("Bovine");
    }

    @Test
    @Order(4)
    public void testDeleteStructureGroupAttribute() {
        assertThatNoException().isThrownBy(() -> {
            structureGroupAttributeService.deleteStructureGroupAttribute(structureGroupAttributeId);
        });
    }
}
