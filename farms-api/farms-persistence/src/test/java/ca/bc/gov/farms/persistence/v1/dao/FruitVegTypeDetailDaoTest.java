package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;

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
import ca.bc.gov.farms.persistence.v1.dto.FruitVegTypeDetailDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FruitVegTypeDetailDaoTest {

    @Autowired
    private FruitVegTypeDetailDao fruitVegTypeDetailDao;

    private static String fruitVegTypeCode;

    @Test
    @Order(1)
    public void testInsert() {
        FruitVegTypeDetailDto dto = new FruitVegTypeDetailDto();
        dto.setFruitVegTypeCode("LYCHEE");
        dto.setFruitVegTypeDesc("Tropical Fruit");
        dto.setRevenueVarianceLimit(new BigDecimal("20.000"));

        FruitVegTypeDetailDto result = null;
        try {
            fruitVegTypeDetailDao.insert(dto, "testUser");
            fruitVegTypeCode = dto.getFruitVegTypeCode();
            result = fruitVegTypeDetailDao.fetch(fruitVegTypeCode);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getFruitVegTypeCode()).isEqualTo("LYCHEE");
        assertThat(result.getFruitVegTypeDesc()).isEqualTo("Tropical Fruit");
        assertThat(result.getRevenueVarianceLimit()).isEqualTo(new BigDecimal("20.000"));
    }

    @Test
    @Order(2)
    public void testFetchAll() {
        List<FruitVegTypeDetailDto> dtos = null;
        try {
            dtos = fruitVegTypeDetailDao.fetchAll();
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dtos).isNotNull();
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(1);

        FruitVegTypeDetailDto fetchedDto = dtos.get(0);
        assertThat(fetchedDto.getFruitVegTypeCode()).isEqualTo("LYCHEE");
        assertThat(fetchedDto.getFruitVegTypeDesc()).isEqualTo("Tropical Fruit");
        assertThat(fetchedDto.getRevenueVarianceLimit()).isEqualTo(new BigDecimal("20.000"));
    }

    @Test
    @Order(3)
    public void testUpdate() {
        FruitVegTypeDetailDto dto = null;
        try {
            dto = fruitVegTypeDetailDao.fetch(fruitVegTypeCode);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setFruitVegTypeDesc("King of Fruits");
        dto.setRevenueVarianceLimit(new BigDecimal("30.000"));

        FruitVegTypeDetailDto result = null;
        try {
            fruitVegTypeDetailDao.update(dto, "testUser");
            fruitVegTypeCode = dto.getFruitVegTypeCode();
            result = fruitVegTypeDetailDao.fetch(fruitVegTypeCode);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getFruitVegTypeCode()).isEqualTo("LYCHEE");
        assertThat(result.getFruitVegTypeDesc()).isEqualTo("King of Fruits");
        assertThat(result.getRevenueVarianceLimit()).isEqualTo(new BigDecimal("30.000"));
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            fruitVegTypeDetailDao.delete(fruitVegTypeCode);
        });
    }
}
