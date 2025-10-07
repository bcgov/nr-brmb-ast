package ca.bc.gov.farms.api.rest.v1.endpoints;

import java.time.LocalDate;

import javax.ws.rs.ext.ContextResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ca.bc.gov.brmb.common.rest.resource.transformers.LocalDateJacksonDeserializer;
import ca.bc.gov.brmb.common.rest.resource.transformers.LocalDateJacksonSerializer;

public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDate.class, new LocalDateJacksonDeserializer());
        module.addSerializer(LocalDate.class, new LocalDateJacksonSerializer());
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
