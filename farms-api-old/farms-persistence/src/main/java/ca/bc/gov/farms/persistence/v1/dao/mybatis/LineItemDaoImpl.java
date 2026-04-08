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
import ca.bc.gov.farms.persistence.v1.dao.LineItemDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.LineItemMapper;
import ca.bc.gov.farms.persistence.v1.dto.LineItemDto;

public class LineItemDaoImpl extends BaseDao implements LineItemDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(LineItemDaoImpl.class);

    @Autowired
    private LineItemMapper mapper;

    @Override
    public LineItemDto fetch(Long lineItemId) throws DaoException {
        logger.debug("<fetch");

        LineItemDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("lineItemId", lineItemId);
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
    public List<LineItemDto> fetchByProgramYear(Integer programYear)
            throws DaoException {
        logger.debug("<fetchByProgramYear");

        List<LineItemDto> dtos = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("programYear", programYear);
            dtos = this.mapper.fetchByProgramYear(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchByProgramYear " + dtos);
        return dtos;
    }

    @Override
    public void insert(LineItemDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long lineItemId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertLineItem(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            lineItemId = (Long) parameters.get("lineItemId");
            dto.setLineItemId(lineItemId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(LineItemDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.updateLineItem(parameters);

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
    public void delete(Long lineItemId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("lineItemId", lineItemId);
            int count = this.mapper.deleteLineItem(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
