package com.basic.framework.data.jpa.autoconfigure;

import com.basic.framework.data.jpa.configuration.AuditorAwareConfiguration;
import com.basic.framework.data.jpa.configuration.ReactiveAuditorAwareConfiguration;
import com.basic.framework.data.jpa.listener.AuditingEntityNameListener;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Jpa 自动配置类
 *
 * @author vains
 */
@Import({
        AuditorAwareConfiguration.class,
        AuditingEntityNameListener.class,
        ReactiveAuditorAwareConfiguration.class
})
@EnableJpaAuditing
public class JpaAutoConfiguration {
}
