package com.halildurmus.hotdeals.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halildurmus.hotdeals.security.models.SecurityProperties;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
public class SecurityConfig {

  private static final String[] PUBLIC_GET_ENDPOINTS = {
          "/actuator/health", "/categories", "/deals/**", "/error", "/stores", "/categories/tags"
  };

  // Matches /users/{id}, /users/{id}/comment-count, /users/{id}/extended
  private static final String[] PUBLIC_GET_ENDPOINTS_REGEX = {"/users/(?!me|search).+"};

  private static final String[] PUBLIC_POST_ENDPOINTS = {"/users"};

  private static final String[] SWAGGER_ENDPOINTS = {"/swagger-ui/**", "/v3/api-docs/**"};

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private FirebaseFilter firebaseFilter;

  @Bean
  public AuthenticationEntryPoint restAuthenticationEntryPoint() {
    return (httpServletRequest, httpServletResponse, e) -> {
      Map<String, Object> errorObject = new HashMap<>();
      errorObject.put("message", "Unauthorized access of protected resource, invalid credentials");
      errorObject.put("error", HttpStatus.UNAUTHORIZED);
      errorObject.put("code", HttpStatus.UNAUTHORIZED.value());
      errorObject.put("timestamp", new Timestamp(new Date().getTime()));
      httpServletResponse.setContentType("application/json;charset=UTF-8");
      httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
      httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorObject));
    };
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    var configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(securityProperties.getAllowedOrigins());
    configuration.setAllowedMethods(securityProperties.getAllowedMethods());
    // Important for credentials (like cookies, authorization headers)
    configuration.setAllowCredentials(true);
    // You might want to allow specific headers if your frontend sends custom ones
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // The new way to configure HttpSecurity is via a SecurityFilterChain bean
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // UPDATED: lambda syntax for cors
            .csrf(csrf -> csrf.disable()) // UPDATED: lambda syntax for csrf
            .formLogin(Customizer.withDefaults()) // UPDATED: lambda syntax. Customizer.withDefaults() disables it by default when you use .disable()
            .httpBasic(Customizer.withDefaults()) // UPDATED: lambda syntax. Customizer.withDefaults() disables it by default when you use .disable()
            .exceptionHandling(
                    exceptionHandling -> exceptionHandling.authenticationEntryPoint(restAuthenticationEntryPoint())) // UPDATED: lambda syntax
            .authorizeHttpRequests(
                    authorizeRequests -> // UPDATED: authorizeRequests() -> authorizeHttpRequests()
                            authorizeRequests
                                    .requestMatchers(
                                            "/actuator/**", "/comment-reports/**", "/deal-reports/**", "/user-reports/**") // UPDATED: antMatchers -> requestMatchers
                                    .hasRole("SUPER")
                                    .anyRequest()
                                    .authenticated())
            .addFilterBefore(firebaseFilter, BasicAuthenticationFilter.class)
            .sessionManagement(
                    sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // UPDATED: lambda syntax

    return httpSecurity.build(); // Don't forget to build the HttpSecurity object
  }

  // The new way to configure WebSecurity is via a WebSecurityCustomizer bean

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->
            web.ignoring()
                    .requestMatchers(HttpMethod.OPTIONS, "/**") // UPDATED: antMatchers -> requestMatchers
                    .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS) // UPDATED: antMatchers -> requestMatchers
                    .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS) // UPDATED: antMatchers -> requestMatchers
                    .requestMatchers(HttpMethod.GET, SWAGGER_ENDPOINTS) // UPDATED: antMatchers -> requestMatchers
                    .requestMatchers(new RegexRequestMatcher(PUBLIC_GET_ENDPOINTS_REGEX[0], HttpMethod.GET.name()));
  }
}
