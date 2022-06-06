package io.amtech.projectflow.config;

import io.amtech.projectflow.security.TokenAuthenticationFailureHandler;
import io.amtech.projectflow.security.TokenAuthenticationManager;
import io.amtech.projectflow.security.TokenFilter;
import io.amtech.projectflow.service.token.TokenService;
import io.amtech.projectflow.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final TokenService tokenService;
    private final UserService userService;
    private final TokenAuthenticationFailureHandler tokenAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .userDetailsService(userService)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**/api-docs/**",
                             "/swagger-resources/**",
                             "/configuration/ui",
                             "/swagger-resources",
                             "/configuration/security",
                             "/swagger-ui",
                             "/swagger**",
                             "/swagger-ui/**",
                             "/webjars/**",
                             "/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new TokenFilter(new TokenAuthenticationManager(tokenService), tokenAuthenticationFailureHandler),
                                 UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS)
                .antMatchers("/auth/login", "/auth/refresh",
                             "/**/api-docs/**",
                             "/swagger-resources/**",
                             "/configuration/ui",
                             "/swagger-resources",
                             "/configuration/security",
                             "/swagger-ui",
                             "/swagger**",
                             "/swagger-ui/**",
                             "/webjars/**",
                             "/actuator/**");
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> cors() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.addAllowedMethod(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        FilterRegistrationBean<CorsFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new CorsFilter(source));
        filterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterFilterRegistrationBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
