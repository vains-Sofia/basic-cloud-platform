package com.basic.framework.web.autoconfigure;

import com.basic.framework.web.deserializer.TimeStampLocalDateTimeDeserializer;
import com.basic.framework.web.serizalizer.TimeStampLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.basic.framework.core.constants.DateFormatConstants.*;

/**
 * 时间格式化自定义处理类，自动注入ioc
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultJackson2AutoConfiguration {

    /**
     * LocalDatetime序列化
     *
     * @return LocalDateTimeSerializer
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new TimeStampLocalDateTimeSerializer(Boolean.FALSE);
    }

    /**
     * LocalDatetime反序列化
     *
     * @return LocalDateTimeDeserializer
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new TimeStampLocalDateTimeDeserializer();
    }

    /**
     * LocalDate 序列化
     *
     * @return LocalDateSerializer
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalDateSerializer localDateSerializer() {
        return new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * LocalDate 反序列化
     *
     * @return LocalDateDeserializer
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * LocalTime 序列化
     *
     * @return LocalTimeSerializer
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalTimeSerializer localTimeSerializer() {
        return new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
    }

    /**
     * LocalTime 反序列化
     *
     * @return LocalTimeDeserializer
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalTimeDeserializer localTimeDeserializer() {
        return new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
    }

    /**
     * 针对于Json格式的请求响应做出自定义处理，自定义LocalDateTime、LocalDate和LocalTime的格式化
     *
     * @return ObjectMapper自定义
     */
    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
            builder.simpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
            builder.serializerByType(LocalDate.class, localDateSerializer());
            builder.deserializerByType(LocalDate.class, localDateDeserializer());
            builder.simpleDateFormat(DEFAULT_DATE_FORMAT);
            builder.serializerByType(LocalTime.class, localTimeSerializer());
            builder.deserializerByType(LocalTime.class, localTimeDeserializer());
            builder.simpleDateFormat(DEFAULT_TIME_FORMAT);
        };
    }

    @PostConstruct
    public void postConstruct() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing Custom Jackson formatting handler.");
        }
    }

}