package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.StructureGroupAttributeDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.StructureGroupAttributeMapper;
import ca.bc.gov.farms.persistence.v1.dto.StructureGroupAttributeDto;

public class StructureGroupAttributeDaoImpl extends BaseDao implements StructureGroupAttributeDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(StructureGroupAttributeDaoImpl.class);

    @Autowired
    private StructureGroupAttributeMapper mapper;

    @Override
    public StructureGroupAttributeDto fetch(Long structureGroupAttributeId) throws DaoException {
        logger.debug("<fetch");

        StructureGroupAttributeDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("structureGroupAttributeId", structureGroupAttributeId);
            result = this.mapper.fetch(parameters);

            if (result != null) {
                result.resetDirty();
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetch " + result);
        return result;
    }

    @Override
    public StructureGroupAttributeDto fetchByStructureGroupCode(String structureGroupCode)
            throws DaoException {
        logger.debug("<fetchByStructureGroupCode");

        StructureGroupAttributeDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("structureGroupCode", structureGroupCode);
            result = this.mapper.fetchByStructureGroupCode(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchByStructureGroupCode " + result);
        return result;
    }

    @Override
    public void insert(StructureGroupAttributeDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long structureGroupAttributeId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertStructureGroupAttribute(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            structureGroupAttributeId = (Long) parameters.get("structureGroupAttributeId");
            dto.setStructureGroupAttributeId(structureGroupAttributeId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(StructureGroupAttributeDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.updateStructureGroupAttribute(parameters);

                if (count == 0) {
                    throw new NotFoundDaoException("Record not updated: " + count);
                }
            } catch (RuntimeException e) {
                handleException(e);
            }
        }

        logger.debug(">update");
    }

    @Override
    public void delete(Long structureGroupAttributeId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("structureGroupAttributeId", structureGroupAttributeId);
            int count = this.mapper.deleteStructureGroupAttribute(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
