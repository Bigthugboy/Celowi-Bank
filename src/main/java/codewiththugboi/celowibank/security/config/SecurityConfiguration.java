package codewiththugboi.celowibank.security.config;

import codewiththugboi.celowibank.exceptions.ExceptionHandlerFilter;
import codewiththugboi.celowibank.exceptions.UnAuthorizedEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;
    private final UnAuthorizedEntryPoint unAuthorizedEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final ExceptionHandlerFilter exceptionHandlerFilterBean;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/customError").permitAll()
               .requestMatchers("/access-denied").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unAuthorizedEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
               .oauth2Login()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(exceptionHandlerFilterBean, UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }
}
