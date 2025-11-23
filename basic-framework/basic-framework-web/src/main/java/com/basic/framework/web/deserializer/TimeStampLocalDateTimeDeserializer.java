package com.basic.framework.web.deserializer;

import com.basic.framework.core.constants.DateFormatConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 时间戳反序列化为LocalDateTime
 *
 * @author vains
 */
public class TimeStampLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

    private static final DateTimeFormatter DEFAULT_FORMATTER =
            DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT);

    /**
     * 指定日志格式
     *
     * @param formatter 格式
     */
    public TimeStampLocalDateTimeDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    public TimeStampLocalDateTimeDeserializer() {
        this(DEFAULT_FORMATTER);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            // 传入秒级时间戳
            return LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(parser.getLongValue()),
                    ZoneId.systemDefault()
            );
        }

        String value = parser.getText().trim();

        // 支持 yyyy-MM-dd HH:mm:ss
        try {
            return LocalDateTime.parse(value,
                    DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT));
        } catch (Exception ignore) {}

        // 支持 ISO 8601
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception ignore) {}

        // 回退到父类
        return super.deserialize(parser, context);
    }
}
