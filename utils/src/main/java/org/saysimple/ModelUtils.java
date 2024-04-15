package org.saysimple;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public final class ModelUtils {
    public static <T> T mapper(Object source, Class<T> targetClass) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(source, targetClass);
    }

    public static <T> T strictMapper(Object source, Class<T> targetClass) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(source, targetClass);
    }
}
