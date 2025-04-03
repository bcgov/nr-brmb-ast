package ca.bc.gov.farms.api.rest.v1.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class AadOAuth2ResourceServerSecurityConfig {

    /**
     * Add configuration logic as needed.
     */
    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.apply(AadResourceServerHttpSecurityConfigurer.aadResourceServer())
                .and()
                .authorizeHttpRequests()
                .antMatchers("/checkHealth").permitAll()
                .anyRequest().authenticated();
        return http.build();
    }
}
