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
            // 传入时间戳时反序列化根据时间戳来序列化
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(parser.getLongValue()), ZoneId.systemDefault());
        }
        return super.deserialize(parser, context);
    }
}
