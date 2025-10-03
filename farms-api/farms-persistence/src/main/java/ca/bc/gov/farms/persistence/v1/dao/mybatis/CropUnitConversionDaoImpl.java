package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.CropUnitConversionDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.CropUnitConversionMapper;
import ca.bc.gov.farms.persistence.v1.dto.ConversionUnitDto;
import ca.bc.gov.farms.persistence.v1.dto.CropUnitConversionDto;

public class CropUnitConversionDaoImpl extends BaseDao implements CropUnitConversionDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(CropUnitConversionDaoImpl.class);

    @Autowired
    private CropUnitConversionMapper mapper;

    @Override
    public CropUnitConversionDto fetch(Long cropUnitDefaultId) throws DaoException {
        logger.debug("<fetch");

        CropUnitConversionDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cropUnitDefaultId", cropUnitDefaultId);
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
    public List<CropUnitConversionDto> fetchAll() throws DaoException {
        logger.debug("<fetchAll");

        List<CropUnitConversionDto> dtos = null;

        try {
            dtos = this.mapper.fetchAll();
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchAll " + dtos);
        return dtos;
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

        Long cropUnitDefaultId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertCropUnitDefault(parameters);
            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            cropUnitDefaultId = (Long) parameters.get("cropUnitDefaultId");
            dto.setCropUnitDefaultId(cropUnitDefaultId);

            parameters.put("inventoryItemCode", dto.getInventoryItemCode());
            for (ConversionUnitDto conversionUnit : dto.getConversionUnits()) {
                parameters.put("dto", conversionUnit);
                count = this.mapper.insertCropUnitConversionFactor(parameters);
                if (count == 0) {
                    throw new DaoException("Record not inserted: " + count);
                }
            }
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
                int count = this.mapper.updateCropUnitDefault(parameters);
                if (count == 0) {
                    throw new NotFoundDaoException("Record not updated: " + count);
                }

                parameters.put("inventoryItemCode", dto.getInventoryItemCode());
                Set<Long> cropUnitConversionFactorIds = new HashSet<>();
                for (ConversionUnitDto conversionUnit : dto.getConversionUnits()) {
                    parameters.put("dto", conversionUnit);
                    // insert
                    if (conversionUnit.getCropUnitConversionFactorId() == null) {
                        count = this.mapper.insertCropUnitConversionFactor(parameters);
                        if (count == 0) {
                            throw new NotFoundDaoException("Record not inserted: " + count);
                        }
                        cropUnitConversionFactorIds.add((Long) parameters.get("cropUnitConversionFactorId"));
                    }
                    // update
                    else {
                        if (conversionUnit.isDirty()) {
                            count = this.mapper.updateCropUnitConversionFactor(parameters);
                            if (count == 0) {
                                throw new NotFoundDaoException("Record not updated: " + count);
                            }
                        }
                        cropUnitConversionFactorIds.add(conversionUnit.getCropUnitConversionFactorId());
                    }
                }

                // delete
                parameters.put("cropUnitDefaultId", dto.getCropUnitDefaultId());
                dto = this.mapper.fetch(parameters);
                for (ConversionUnitDto conversionUnit : dto.getConversionUnits()) {
                    Long cropUnitConversionFactorId = conversionUnit.getCropUnitConversionFactorId();
                    if (!cropUnitConversionFactorIds.contains(cropUnitConversionFactorId)) {
                        parameters.put("cropUnitConversionFactorId", cropUnitConversionFactorId);
                        count = this.mapper.deleteCropUnitConversionFactor(parameters);
                        if (count == 0) {
                            throw new NotFoundDaoException("Record not deleted: " + count);
                        }
                    }
                }
            } catch (RuntimeException e) {
                handleException(e);
            }
        }

        logger.debug(">update");
    }

    @Override
    public void delete(Long cropUnitDefaultId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cropUnitDefaultId", cropUnitDefaultId);
            CropUnitConversionDto dto = this.mapper.fetch(parameters);

            for (ConversionUnitDto conversionUnit : dto.getConversionUnits()) {
                parameters.put("cropUnitConversionFactorId", conversionUnit.getCropUnitConversionFactorId());
                int count = this.mapper.deleteCropUnitConversionFactor(parameters);
                if (count == 0) {
                    throw new NotFoundDaoException("Record not deleted: " + count);
                }
            }

            int count = this.mapper.deleteCropUnitDefault(parameters);
            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
