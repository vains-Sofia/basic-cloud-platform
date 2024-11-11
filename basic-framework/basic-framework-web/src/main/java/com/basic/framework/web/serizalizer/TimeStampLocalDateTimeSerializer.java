package com.basic.framework.web.serizalizer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * LocalDateTime序列化为时间戳
 *
 * @author vains
 */
@RequiredArgsConstructor
public class TimeStampLocalDateTimeSerializer extends LocalDateTimeSerializer {

    /**
     * 是否序列化为时间戳
     */
    private final boolean writeDatesAsTimestamps;

    @Override
    public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
        if (this.writeDatesAsTimestamps) {
            // 将 LocalDateTime 转为时间戳（秒）
            long timestamp = value.toEpochSecond(ZoneOffset.UTC);
            g.writeNumber(timestamp);
        } else {
            super.serialize(value, g, provider);
        }
    }
}
