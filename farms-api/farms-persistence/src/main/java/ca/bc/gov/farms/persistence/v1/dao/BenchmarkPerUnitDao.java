package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;

public interface BenchmarkPerUnitDao extends Serializable {

    BenchmarkPerUnitDto fetch(Long benchmarkPerUnitId) throws DaoException;

    List<BenchmarkPerUnitDto> fetchAll() throws DaoException;

    void insert(BenchmarkPerUnitDto dto, String userId) throws DaoException;

    void update(BenchmarkPerUnitDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long benchmarkPerUnitId) throws DaoException, NotFoundDaoException;
}
