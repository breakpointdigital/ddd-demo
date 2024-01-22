package pl.bpd.ddd

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.web.SecurityFilterChain
import spock.mock.DetachedMockFactory

@TestConfiguration
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration, SecurityFilterAutoConfiguration])
class DisableSecurityConfig {
    def mockFactory = new DetachedMockFactory()

    @Bean
    SecurityFilterChain httpSecurity() {
        return mockFactory.Stub(SecurityFilterChain)
    }
}