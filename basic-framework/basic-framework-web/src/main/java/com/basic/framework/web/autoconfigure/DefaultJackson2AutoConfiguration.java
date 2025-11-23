package com.basic.framework.web.autoconfigure;

import com.basic.framework.web.deserializer.TimeStampLocalDateTimeDeserializer;
import com.basic.framework.web.serizalizer.IdToStringModifier;
import com.basic.framework.web.serizalizer.TimeStampLocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.basic.framework.core.constants.DateFormatConstants.*;

@Slf4j
@RequiredArgsConstructor
@Import({WebMvcDateTimeConfiguration.class})
public class DefaultJackson2AutoConfiguration {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

    /**
     * 主 Jackson 配置
     */
    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // JavaTimeModule 用于所有 Java 8 时间类型
            JavaTimeModule javaTimeModule = new JavaTimeModule();

            // LocalDate
            javaTimeModule.addSerializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer(DATE_FORMATTER));
            javaTimeModule.addDeserializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer(DATE_FORMATTER));

            // LocalTime
            javaTimeModule.addSerializer(LocalTime.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer(TIME_FORMATTER));
            javaTimeModule.addDeserializer(LocalTime.class, new com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer(TIME_FORMATTER));

            // LocalDateTime（重点：你的自定义）
            javaTimeModule.addSerializer(LocalDateTime.class, new TimeStampLocalDateTimeSerializer(false));
            javaTimeModule.addDeserializer(LocalDateTime.class, new TimeStampLocalDateTimeDeserializer(DATETIME_FORMATTER));

            builder.modules(javaTimeModule);

            // simpleDateFormat 只给 java.util.Date 用的
            builder.simpleDateFormat(DEFAULT_DATE_TIME_FORMAT);

            // 注册一个 Module，用于 Long → String
            builder.postConfigurer(objectMapper -> objectMapper.setSerializerFactory(
                    objectMapper.getSerializerFactory().withSerializerModifier(new IdToStringModifier())
            ));
        };
    }

    @PostConstruct
    public void postConstruct() {
        log.info("Initialized optimized custom Jackson formatting.");
    }
}