package com.halildurmus.hotdeals.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;


@Configuration
@ConditionalOnProperty(name = "security.enabled", havingValue = "false", matchIfMissing = false)
public class NoSecurityConfig {

    @Bean
    @Primary
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/**");
    }
}
