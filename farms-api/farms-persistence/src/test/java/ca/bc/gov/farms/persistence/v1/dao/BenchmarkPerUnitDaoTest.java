package ca.bc.gov.farms.persistence.v1.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
public class BenchmarkPerUnitDaoTest {

    @Autowired
    private BenchmarkPerUnitDao benchmarkPerUnitDao;

    @Test
    public void testDemo() {
        assertThatNoException().isThrownBy(() -> {
            BenchmarkPerUnitDto dto = benchmarkPerUnitDao.fetch(1L);
            assertThat(dto).isNull();
        });
    }
}
