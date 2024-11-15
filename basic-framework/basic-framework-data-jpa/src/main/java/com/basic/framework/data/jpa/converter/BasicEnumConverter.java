package com.basic.framework.data.jpa.converter;

import com.basic.framework.core.enums.BasicEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 枚举转换器
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
@Converter(autoApply = true)
public abstract class BasicEnumConverter<V extends Serializable, E extends Enum<E> & BasicEnum<V, E>> implements AttributeConverter<E, V> {

    private final Class<E> enumClass;

    @Override
    public V convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public E convertToEntityAttribute(V dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return BasicEnum.fromValue(dbData, enumClass);
        } catch (Exception e) {
            log.warn("Failed to convert {} to {}, because {}", dbData, enumClass.getName(), e.getMessage());
        }
        return null;
    }

}
