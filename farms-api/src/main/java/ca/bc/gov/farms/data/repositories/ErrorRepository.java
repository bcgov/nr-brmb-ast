package ca.bc.gov.farms.data.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ErrorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String codify(String msg) {

        return jdbcTemplate.queryForObject(
                "select farms_error_pkg.codify(?)",
                String.class,
                msg);
    }
}
