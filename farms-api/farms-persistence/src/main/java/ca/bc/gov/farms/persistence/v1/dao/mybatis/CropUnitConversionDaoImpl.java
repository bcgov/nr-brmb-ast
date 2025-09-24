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
import ca.bc.gov.farms.persistence.v1.dao.CropUnitConversionDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.CropUnitConversionMapper;
import ca.bc.gov.farms.persistence.v1.dto.CropUnitConversionDto;

public class CropUnitConversionDaoImpl extends BaseDao implements CropUnitConversionDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(CropUnitConversionDaoImpl.class);

    @Autowired
    private CropUnitConversionMapper mapper;

    @Override
    public CropUnitConversionDto fetch(Long cropUnitConversionFactorId) throws DaoException {
        logger.debug("<fetch");

        CropUnitConversionDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cropUnitConversionFactorId", cropUnitConversionFactorId);
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
    public List<CropUnitConversionDto> fetchByInventoryItemCode(String inventoryItemCode) throws DaoException {
        logger.debug("<fetchByInventoryItemCode");

        List<CropUnitConversionDto> dtos = null;

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
    public void insert(CropUnitConversionDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long cropUnitConversionFactorId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            this.mapper.insertCropUnitDefault(parameters);
            int count = this.mapper.insertCropUnitConversionFactor(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            cropUnitConversionFactorId = (Long) parameters.get("cropUnitConversionFactorId");
            dto.setCropUnitConversionFactorId(cropUnitConversionFactorId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(CropUnitConversionDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                this.mapper.updateCropUnitDefault(parameters);
                int count = this.mapper.updateCropUnitConversionFactor(parameters);

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
    public void delete(Long cropUnitConversionId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cropUnitConversionId", cropUnitConversionId);
            int count = this.mapper.deleteCropUnitConversionFactor(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
