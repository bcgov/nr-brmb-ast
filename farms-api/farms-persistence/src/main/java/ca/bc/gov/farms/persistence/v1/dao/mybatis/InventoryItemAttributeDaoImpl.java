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
import ca.bc.gov.farms.persistence.v1.dao.InventoryItemAttributeDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.InventoryItemAttributeMapper;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemAttributeDto;

public class InventoryItemAttributeDaoImpl extends BaseDao implements InventoryItemAttributeDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(InventoryItemAttributeDaoImpl.class);

    @Autowired
    private InventoryItemAttributeMapper mapper;

    @Override
    public InventoryItemAttributeDto fetch(Long inventoryItemAttributeId) throws DaoException {
        logger.debug("<fetch");

        InventoryItemAttributeDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("inventoryItemAttributeId", inventoryItemAttributeId);
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
    public List<InventoryItemAttributeDto> fetchByRollupInventoryItemCode(String rollupInventoryItemCode)
            throws DaoException {
        logger.debug("<fetchByRollupInventoryItemCode");

        List<InventoryItemAttributeDto> result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("rollupInventoryItemCode", rollupInventoryItemCode);
            result = this.mapper.fetchBy(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchByRollupInventoryItemCode " + result);
        return result;
    }

    @Override
    public void insert(InventoryItemAttributeDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long inventoryItemAttributeId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertInventoryItemAttribute(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            inventoryItemAttributeId = (Long) parameters.get("inventoryItemAttributeId");
            dto.setInventoryItemAttributeId(inventoryItemAttributeId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(InventoryItemAttributeDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.updateInventoryItemAttribute(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not updated: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">update");
    }

    @Override
    public void delete(Long inventoryItemAttributeId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("inventoryItemAttributeId", inventoryItemAttributeId);
            int count = this.mapper.deleteInventoryItemAttribute(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
