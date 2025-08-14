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
import ca.bc.gov.farms.persistence.v1.dao.InventoryItemDetailDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.InventoryItemDetailMapper;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemDetailDto;

public class InventoryItemDetailDaoImpl extends BaseDao implements InventoryItemDetailDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(InventoryItemDetailDaoImpl.class);

    @Autowired
    private InventoryItemDetailMapper mapper;

    @Override
    public InventoryItemDetailDto fetch(Long inventoryItemDetailId) throws DaoException {
        logger.debug("<fetch");

        InventoryItemDetailDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("inventoryItemDetailId", inventoryItemDetailId);
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
    public List<InventoryItemDetailDto> fetchByInventoryItemCode(String inventoryItemCode) throws DaoException {
        logger.debug("<fetchByInventoryItemCode");

        List<InventoryItemDetailDto> dtos = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("inventoryItemCode", inventoryItemCode);
            dtos = this.mapper.fetchBy(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchByInventoryItemCode " + dtos);
        return dtos;
    }

    @Override
    public void insert(InventoryItemDetailDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long inventoryItemDetailId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertInventoryItemDetail(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            inventoryItemDetailId = (Long) parameters.get("inventoryItemDetailId");
            dto.setInventoryItemDetailId(inventoryItemDetailId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(InventoryItemDetailDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.updateInventoryItemDetail(parameters);

                if (count == 0) {
                    throw new NotFoundDaoException("Record not updated: " + dto.getInventoryItemDetailId());
                }
            } catch (RuntimeException e) {
                handleException(e);
            }
        }

        logger.debug(">update");
    }

    @Override
    public void delete(Long inventoryItemDetailId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("inventoryItemDetailId", inventoryItemDetailId);
            int count = this.mapper.deleteInventoryItemDetail(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + inventoryItemDetailId);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
