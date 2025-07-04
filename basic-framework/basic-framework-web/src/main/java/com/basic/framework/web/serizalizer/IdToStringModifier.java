package com.basic.framework.web.serizalizer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

/**
 * 修改实体类中ID字段的序列化方式，将Long类型的ID转换为String类型。
 * 主要用于解决前端接收大数字时的精度问题。
 *
 * @author vains
 */
public class IdToStringModifier extends BeanSerializerModifier {
    private final ToStringSerializer serializer = new ToStringSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config,
            BeanDescription beanDesc,
            List<BeanPropertyWriter> beanProperties) {

        for (BeanPropertyWriter writer : beanProperties) {
            if ("id".equals(writer.getName()) && writer.getType().getRawClass() == Long.class) {
                writer.assignSerializer(serializer);
            }
        }
        return beanProperties;
    }
}
