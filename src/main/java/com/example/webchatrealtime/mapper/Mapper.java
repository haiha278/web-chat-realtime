package com.example.webchatrealtime.mapper;

import org.modelmapper.ModelMapper;

public class Mapper {
    private static ModelMapper mapper = new ModelMapper();

    public static <T, U> T mapEntityToDTO(U entity, Class<T> dtoClass) {
        return mapper.map(entity, dtoClass);
    }

    public static <T, U> U mapDtoToEntity(T dtoClass, Class<U> entity) {
        return mapper.map(dtoClass, entity);
    }
}
