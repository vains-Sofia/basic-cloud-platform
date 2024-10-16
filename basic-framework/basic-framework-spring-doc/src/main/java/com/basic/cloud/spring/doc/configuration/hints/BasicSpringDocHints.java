package com.basic.cloud.spring.doc.configuration.hints;

import com.basic.cloud.spring.doc.customizer.ApiEnumParameterCustomizer;
import com.basic.cloud.spring.doc.customizer.ApiEnumPropertyCustomizer;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import java.util.Arrays;

/**
 * Spring doc 自定义customizer的hints
 *
 * @author vains
 */
@Slf4j
public class BasicSpringDocHints implements RuntimeHintsRegistrar {

    static Class<?>[] typesToRegister = {
            ApiEnumPropertyCustomizer.class,
            ApiEnumParameterCustomizer.class
    };

    @Override
    public void registerHints(@Nullable RuntimeHints hints, ClassLoader classLoader) {
        if (hints == null) {
            log.debug("No RuntimeHints registered. Skipping hints.");
            return;
        }
        Arrays.stream(typesToRegister).forEach(aClass ->
                hints.reflection().registerType(aClass,
                        hint -> hint.withMembers(
                                MemberCategory.DECLARED_FIELDS,
                                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                                MemberCategory.INVOKE_DECLARED_METHODS
                        )));
    }
}
