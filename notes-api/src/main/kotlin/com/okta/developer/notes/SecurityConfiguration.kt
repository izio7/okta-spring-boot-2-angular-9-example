package com.okta.developer.notes

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        //@formatter:off
        http
            .authorizeRequests().anyRequest().authenticated()
                .and()
            .oauth2Login()
                .and()
            .oauth2ResourceServer().jwt()

        http.requiresChannel()
            .requestMatchers(RequestMatcher {
                r -> r.getHeader("X-Forwarded-Proto") != null
            }).requiresSecure() // <1>

        http.csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()); // <2>

        http.headers()
            .contentSecurityPolicy("script-src 'self'; report-uri /csp-report-endpoint/"); // <3>
        //@formatter:on
    }
}
