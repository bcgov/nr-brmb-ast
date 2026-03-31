package ca.bc.gov.farms.data.repositories;

import java.math.BigDecimal;

public final class DbTypeConverters {

    private DbTypeConverters() {
        // prevent instantiation
    }

    public static BigDecimal toBigDecimal(Integer value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    public static BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    public static String toYN(Boolean value) {
        return value == null ? null : (value ? "Y" : "N");
    }

    public static Short toShort(Integer value) {
        return value == null ? null : value.shortValue();
    }

    public static Long toLong(Integer value) {
        return value == null ? null : value.longValue();
    }
}