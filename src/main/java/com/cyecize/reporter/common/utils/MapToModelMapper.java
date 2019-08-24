package com.cyecize.reporter.common.utils;

import com.cyecize.javache.utils.PrimitiveTypeDataResolver;
import com.cyecize.summer.common.annotations.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapToModelMapper {

    private final PrimitiveTypeDataResolver dataResolver;

    public MapToModelMapper() {
        this.dataResolver = new PrimitiveTypeDataResolver();
    }

    public <T> T map(Map<String, String> source, Class<T> destination) {
        try {
            return this.map(source, (T) destination.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T map(Map<String, String> source, T destination) {

        Map<String, Field> fields = this.getAllFields(destination.getClass()).stream()
                .collect(Collectors.toMap(Field::getName, f -> f));

        for (Map.Entry<String, String> entry : source.entrySet()) {
            if (!fields.containsKey(entry.getKey())) {
                continue;
            }

            Field field = fields.get(entry.getKey());

            Object resolvedData = this.dataResolver.resolve(field.getType(), entry.getValue());
            field.setAccessible(true);

            try {
                field.set(destination, resolvedData);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return destination;
    }

    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = Arrays.stream(type.getDeclaredFields()).collect(Collectors.toList());
        if (type.getSuperclass() != null)
            fields.addAll(getAllFields(type.getSuperclass()));

        return fields;
    }
}
