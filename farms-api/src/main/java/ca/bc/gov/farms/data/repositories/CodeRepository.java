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
        String sql = "SELECT " + codeName +
                " as code, description, null, established_date as effective_date, expiry_date " +
                "FROM farms." + tableName + " " +
                "WHERE " + codeName + " = ? " +
                "ORDER BY " + codeName;

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
                "FROM farms." + tableName + " " +
                "ORDER BY " + codeName;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(CodeEntity.class));
    }

    public int insert(String tableName, String codeName, CodeEntity entity, String userId) {
        String sql = "INSERT INTO farms." + tableName + " " +
                " (" + codeName
                + ", description, established_date, expiry_date, who_created, when_created, who_updated, when_updated) "
                +
                "VALUES (?, ?, ?, ?, ?, current_date, ?, current_date)";

        return jdbcTemplate.update(
                sql,
                entity.getCode(),
                entity.getDescription(),
                entity.getEffectiveDate(),
                entity.getExpiryDate(),
                userId,
                userId);
    }

    public int update(String tableName, String codeName, CodeEntity entity, String userId) {
        String sql = "UPDATE farms." + tableName + " " +
                "SET description = ?, " +
                "established_date = ?, " +
                "expiry_date = ?, " +
                "who_updated = ?, " +
                "when_updated = current_timestamp " +
                "WHERE " + codeName + " = ?";

        return jdbcTemplate.update(
                sql,
                entity.getDescription(),
                entity.getEffectiveDate(),
                entity.getExpiryDate(),
                userId,
                entity.getCode());
    }

    public int delete(String tableName, String codeName, String codeValue, String userId) {
        String sql = "UPDATE farms." + tableName + " " +
                "SET expiry_date = current_date, " +
                "who_updated = ?, " +
                "when_updated = current_timestamp " +
                "WHERE " + codeName + " = ?";

        return jdbcTemplate.update(sql, userId, codeValue);
    }
}
