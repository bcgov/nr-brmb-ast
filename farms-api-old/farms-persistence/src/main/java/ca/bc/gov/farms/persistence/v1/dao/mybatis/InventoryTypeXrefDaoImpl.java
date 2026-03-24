package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.InventoryTypeXrefDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.InventoryTypeXrefMapper;
import ca.bc.gov.farms.persistence.v1.dto.InventoryTypeXrefDto;

public class InventoryTypeXrefDaoImpl extends BaseDao implements InventoryTypeXrefDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(InventoryTypeXrefDaoImpl.class);

    @Autowired
    private InventoryTypeXrefMapper mapper;

    @Override
    public InventoryTypeXrefDto fetch(Long agristabilityCommodityXrefId) throws DaoException {
        logger.debug("<fetch");

        InventoryTypeXrefDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("agristabilityCommodityXrefId", agristabilityCommodityXrefId);
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
    public List<InventoryTypeXrefDto> fetchByInventoryClassCode(String inventoryClassCode) throws DaoException {
        logger.debug("<fetchByInventoryClassCode");

        List<InventoryTypeXrefDto> dtos = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("inventoryClassCode", inventoryClassCode);
            dtos = this.mapper.fetchBy(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchByInventoryClassCode " + dtos);
        return dtos;
    }

    @Override
    public void insert(InventoryTypeXrefDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long agristabilityCommodityXrefId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertInventoryTypeXref(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            agristabilityCommodityXrefId = (Long) parameters.get("agristabilityCommodityXrefId");
            dto.setAgristabilityCommodityXrefId(agristabilityCommodityXrefId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(InventoryTypeXrefDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.updateInventoryTypeXref(parameters);

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
    public void delete(Long agristabilityCommodityXrefId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("agristabilityCommodityXrefId", agristabilityCommodityXrefId);
            int count = this.mapper.deleteInventoryTypeXref(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
