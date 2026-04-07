package ca.bc.gov.farms.data.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.bc.gov.farms.data.entities.CodeEntity;

@Repository
public class CodeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CodeEntity fetchOne(String tableName, String codeName, String codeValue) {
        String sql = "SELECT " + codeName
                + " as code, description, null, established_date as effective_date, expiry_date " +
                "FROM farms." + tableName + " " +
                "WHERE " + codeName + " = ?";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(CodeEntity.class),
                    codeValue);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<CodeEntity> fetchAll(String tableName, String codeName) {
        String sql = "SELECT " + codeName
                + " as code, description, null, established_date as effective_date, expiry_date " +
                "FROM farms." + tableName;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(CodeEntity.class));
    }
}
