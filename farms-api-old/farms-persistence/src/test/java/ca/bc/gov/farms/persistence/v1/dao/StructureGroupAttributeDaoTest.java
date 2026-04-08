package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.persistence.v1.dto.StructureGroupAttributeDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StructureGroupAttributeDaoTest {

    @Autowired
    private StructureGroupAttributeDao structureGroupAttributeDao;

    private static Long structureGroupAttributeId;

    @Test
    @Order(1)
    public void testInsert() {
        StructureGroupAttributeDto dto = new StructureGroupAttributeDto();
        dto.setStructureGroupCode("100");
        dto.setRollupStructureGroupCode("120");

        StructureGroupAttributeDto result = null;
        try {
            structureGroupAttributeDao.insert(dto, "testUser");
            structureGroupAttributeId = dto.getStructureGroupAttributeId();
            result = structureGroupAttributeDao.fetch(structureGroupAttributeId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getStructureGroupCode()).isEqualTo("100");
        assertThat(result.getStructureGroupDesc()).isEqualTo("Alpaca");
        assertThat(result.getRollupStructureGroupCode()).isEqualTo("120");
        assertThat(result.getRollupStructureGroupDesc()).isEqualTo("Other Livestock");
    }

    @Test
    @Order(2)
    public void testFetchByStructureGroupCode() {
        StructureGroupAttributeDto dto = null;
        try {
            dto = structureGroupAttributeDao.fetchByStructureGroupCode("100");
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(dto).isNotNull();
        assertThat(dto.getStructureGroupCode()).isEqualTo("100");
        assertThat(dto.getStructureGroupDesc()).isEqualTo("Alpaca");
        assertThat(dto.getRollupStructureGroupCode()).isEqualTo("120");
        assertThat(dto.getRollupStructureGroupDesc()).isEqualTo("Other Livestock");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        StructureGroupAttributeDto dto = null;
        try {
            dto = structureGroupAttributeDao.fetch(structureGroupAttributeId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setStructureGroupCode("100");
        dto.setRollupStructureGroupCode("300");

        StructureGroupAttributeDto result = null;
        try {
            structureGroupAttributeDao.update(dto, "testUser");
            result = structureGroupAttributeDao.fetch(structureGroupAttributeId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getStructureGroupCode()).isEqualTo("100");
        assertThat(result.getStructureGroupDesc()).isEqualTo("Alpaca");
        assertThat(result.getRollupStructureGroupCode()).isEqualTo("300");
        assertThat(result.getRollupStructureGroupDesc()).isEqualTo("Bovine");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            structureGroupAttributeDao.delete(structureGroupAttributeId);
        });
    }
}
