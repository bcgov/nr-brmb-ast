package ca.bc.gov.farms.persistence.v1.dao;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = { PersistenceSpringConfig.class })
public class BenchmarkPerUnitDaoTest {

    @Test
    public void testDemo() {
        assertThat(Boolean.TRUE).isFalse();
    }
}
