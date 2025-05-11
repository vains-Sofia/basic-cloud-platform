package com.basic.framework.data.jpa.autoconfigure;

import com.basic.framework.data.jpa.configuration.AuditorAwareConfiguration;
import com.basic.framework.data.jpa.configuration.ReactiveAuditorAwareConfiguration;
import com.basic.framework.data.jpa.listener.AuditingEntityNameListener;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Jpa 自动配置类
 *
 * @author vains
 */
@Slf4j
@Import({
        AuditorAwareConfiguration.class,
        AuditingEntityNameListener.class,
        ReactiveAuditorAwareConfiguration.class
})
@EnableJpaAuditing
public class JpaAutoConfiguration {

    @PostConstruct
    public void postConstruct() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing Jpa Auto Configuration.");
        }
    }

}
