You're encountering a classic Spring Security problem related to the order of operations when method-level security (like `@PreAuthorize` or your `@IsSuper` annotation) is involved within a filter chain.

Let's dissect the relevant parts of your log and code:

**The Log Error:**

```
2025-07-26 16:26:18.336 ERROR 724 --- [nio-8080-exec-2] c.h.hotdeals.security.FirebaseFilter     : Failed to add ROLE_SUPER authority to the user

org.springframework.security.authentication.AuthenticationCredentialsNotFoundException: An Authentication object was not found in the SecurityContext
	at org.springframework.security.access.intercept.AbstractSecurityInterceptor.credentialsNotFound(AbstractSecurityInterceptor.java:336) ~[spring-security-core-5.7.2.jar:5.7.2]
	at org.springframework.security.access.intercept.AbstractSecurityInterceptor.beforeInvocation(AbstractSecurityInterceptor.java:200) ~[spring-security-core-5.7.2.jar:5.7.2]
	at org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor.invoke(MethodSecurityInterceptor.java:58) ~[spring-security-core-5.7.2.jar:5.7.2]
	...
	at com.halildurmus.hotdeals.security.role.RoleServiceImpl$$EnhancerBySpringCGLIB$$45674db8.add(<generated>) ~[classes/:na]
	at com.halildurmus.hotdeals.security.FirebaseFilter.parseAuthorities(FirebaseFilter.java:71) ~[classes/:na]
	at com.halildurmus.hotdeals.security.FirebaseFilter.verifyToken(FirebaseFilter.java:98) ~[classes/:na]
	at com.halildurmus.hotdeals.security.FirebaseFilter.doFilterInternal(FirebaseFilter.java:46) ~[classes/:na]
```

**The Core Problem:**

The error `AuthenticationCredentialsNotFoundException: An Authentication object was not found in the SecurityContext` occurs when Spring Security's method-level security interceptor (which is triggered by `@IsSuper`) tries to perform an authorization check, but there's no `Authentication` object yet in the `SecurityContextHolder`.

Let's trace the execution path:

1.  **`FirebaseFilter.doFilterInternal()`** is called.
2.  It calls **`verifyToken()`**.
3.  Inside `verifyToken()`, it calls **`parseAuthorities()`**.
4.  Inside `parseAuthorities()`, if a user is a "super admin" and the `ROLE_SUPER` claim is missing, it tries to call **`roleService.add(token.getUid(), Role.ROLE_SUPER)`**.
5.  Now, here's the critical point: `RoleService` (the interface) and `RoleServiceImpl` (the implementation) are both annotated with `@IsSuper`. Even though you're calling the `add` method on `roleService` from *within* your `FirebaseFilter`, Spring's AOP (Aspect-Oriented Programming) mechanism for method security kicks in.
6.  When `roleService.add()` is called, Spring Security checks the `@IsSuper` annotation. This annotation, as defined, translates to `@PreAuthorize("hasRole('SUPER')")`.
7.  For `hasRole('SUPER')` to work, an `Authentication` object **must already be present in the `SecurityContextHolder`**.
8.  **The problem:** At this stage in `FirebaseFilter`, the `Authentication` object **has not yet been set** in the `SecurityContextHolder`. The line `SecurityContextHolder.getContext().setAuthentication(authentication);` is executed *after* the `parseAuthorities` method (and thus after the `roleService.add()` call) in `verifyToken()`.

**Solution:**

You have two main approaches to fix this, depending on your intent:

### Option 1: Remove Method-Level Security for `RoleService`'s `add` and `delete` methods (Recommended for this specific scenario)

The `RoleService` is meant to *update* Firebase claims, which is a backend operation. The `FirebaseFilter` is precisely the place where you're trying to *assign* the `ROLE_SUPER` to a user if they are configured as a super admin. You don't want the act of assigning the super admin role to *require* super admin permissions at that moment. The security for adding/deleting roles via the `/roles` endpoint is already handled by `RoleController` using `@IsSuper`.

So, the `@IsSuper` annotation on the `RoleService` interface and its implementation methods is redundant and causing this circular dependency.

**Steps:**

1.  **Remove `@IsSuper` from `RoleService` interface:**

    ```java
    // FILE: src/main/java/com/halildurmus/hotdeals/security/role/RoleService.java
    // REMOVE @IsSuper here
    public interface RoleService {
        void add(String uid, Role role);
        void delete(String uid, Role role);
    }
    ```

2.  **Remove `@IsSuper` from `RoleServiceImpl` (if it was there, though typically annotations on interfaces are inherited or apply to proxies):**

    ```java
    // FILE: src/main/java/com/halildurmus/hotdeals/security/role/RoleServiceImpl.java
    // Ensure @IsSuper is NOT present on the class or methods here.
    @Service
    @Slf4j
    public class RoleServiceImpl implements RoleService {
        // ...
    }
    ```

This way, the `FirebaseFilter` can call `roleService.add()` without requiring any existing authentication in the `SecurityContextHolder`. The `RoleController` will still enforce that only existing `ROLE_SUPER` users can access its `/roles` endpoints.

### Option 2: Manually Set Authentication for the `roleService` Call (More complex, generally not needed here)

If, for some reason, you *really* wanted to enforce a security check on `roleService.add()` even when called from `FirebaseFilter` (e.g., if `roleService` had other methods that absolutely required authentication), you could temporarily set an `Authentication` object in the `SecurityContextHolder` before calling `roleService.add()`.

However, for the specific scenario of adding the initial `ROLE_SUPER` claim based on `SecurityProperties`, this is unnecessary and overcomplicates the logic. The user is *becoming* a super admin at this point, so they can't already *be* one in the `SecurityContext`.

**Conclusion:**

The most appropriate and straightforward solution is to **remove the `@IsSuper` annotation from the `RoleService` interface and `RoleServiceImpl` class/methods**. The security enforcement for adding/removing roles should primarily reside at the API layer (in `RoleController`), not at the internal service layer when it's being called by an authentication filter during the *initial* authentication process.


## Updating to spring security 3

You're facing significant changes in Spring Security 6, which is used by Spring Boot 3. The `WebSecurityConfigurerAdapter` class was deprecated in Spring Security 5.7 and has been **removed** in Spring Security 6. Additionally, the fluent API methods like `and()`, `cors()`, `csrf()`, `formLogin()`, and `authorizeRequests()` have been updated to use a more modern, lambda-based DSL (Domain Specific Language) for configuration.

Let's break down the issues and how to update your `SecurityConfig` class:

**Issues and Solutions:**

1.  **`Cannot resolve symbol 'WebSecurityConfigurerAdapter'`**:

    * **Reason:** As mentioned, this class is removed in Spring Security 6.
    * **Solution:** You no longer extend `WebSecurityConfigurerAdapter`. Instead, you define a `SecurityFilterChain` bean.

2.  **`@EnableGlobalMethodSecurity` is deprecated**:

    * **Reason:** This annotation has been replaced by `@EnableMethodSecurity`.
    * **Solution:** Change `@EnableGlobalMethodSecurity` to `@EnableMethodSecurity`. The `prePostEnabled` is true by default, so you might be able to simplify it.

3.  **`cors()`, `and()`, `csrf()`, `formLogin()` are deprecated/removed**:

    * **Reason:** Spring Security 6 moved to a lambda-based configuration style. The `and()` method is no longer needed as the chain of calls can be nested using lambdas.
    * **Solution:** Rewrite the `HttpSecurity` configuration using the new lambda DSL.

4.  **`authorizeRequests()` and `antMatchers()`, `regexMatchers()` are deprecated/removed**:

    * **Reason:** These have been replaced by `authorizeHttpRequests()` and `requestMatchers()`.
    * **Solution:** Update your authorization rules to use `authorizeHttpRequests()` and `requestMatchers()`.

Here's your updated `SecurityConfig` class for Spring Boot 3 / Spring Security 6 / Java 21:

```java
package com.halildurmus.hotdeals.config; // Assuming this is your package

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halildurmus.hotdeals.security.FirebaseFilter;
import com.halildurmus.hotdeals.security.SecurityProperties;
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
import org.springframework.security.config.Customizer; // Import Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // UPDATED import
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer; // NEW import
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain; // NEW import
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true) // UPDATED
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
public class SecurityConfig { // No longer extends WebSecurityConfigurerAdapter

  private static final String[] PUBLIC_GET_ENDPOINTS = {
    "/actuator/health", "/categories", "/deals/**", "/error", "/stores", "/categories/tags"
  };

  // Matches /users/{id}, /users/{id}/comment-count, /users/{id}/extended
  private static final String[] PUBLIC_GET_ENDPOINTS_REGEX = {"/users/(?!me|search).+"};

  private static final String[] PUBLIC_POST_ENDPOINTS = {"/users"};

  private static final String[] SWAGGER_ENDPOINTS = {"/swagger-ui/**", "/v3/api-docs/**"};

  @Autowired private ObjectMapper objectMapper;

  @Autowired private SecurityProperties securityProperties;

  @Autowired private FirebaseFilter firebaseFilter;

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
            .requestMatchers(new RegexRequestMatcher(PUBLIC_GET_ENDPOINTS_REGEX[0], HttpMethod.GET.name()))
  }
}
```

**Summary of Changes:**

1.  **No more `extends WebSecurityConfigurerAdapter`**: The class `SecurityConfig` no longer extends this deprecated class.
2.  **`SecurityFilterChain` Bean**: The `configure(HttpSecurity httpSecurity)` method is replaced by a `@Bean` method that returns `SecurityFilterChain`. This method takes `HttpSecurity` as an argument and configures it using the new lambda DSL, finally returning `httpSecurity.build()`.
3.  **`WebSecurityCustomizer` Bean**: The `configure(WebSecurity web)` method is replaced by a `@Bean` method that returns `WebSecurityCustomizer`. This is used for ignoring specific requests from the security filter chain.
4.  **`@EnableGlobalMethodSecurity` to `@EnableMethodSecurity`**: The annotation for method-level security has been updated.
5.  **Lambda-based DSL for `HttpSecurity`**:
    * `httpSecurity.cors().configurationSource(corsConfigurationSource()).and()` becomes `httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()))`.
    * `httpSecurity.csrf().disable().and()` becomes `httpSecurity.csrf(csrf -> csrf.disable())`.
    * `httpSecurity.formLogin().disable().and()` becomes `httpSecurity.formLogin(Customizer.withDefaults())`. (Using `Customizer.withDefaults()` for `formLogin()` and `httpBasic()` effectively disables them if they were previously `disable()`d. If you want to enable default form login, just `httpSecurity.formLogin(Customizer.withDefaults())` is enough).
    * `httpSecurity.httpBasic().disable().and()` becomes `httpSecurity.httpBasic(Customizer.withDefaults())`.
    * `httpSecurity.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint()).and()` becomes `httpSecurity.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(restAuthenticationEntryPoint()))`.
    * `httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)` becomes `httpSecurity.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))`.
6.  **`authorizeRequests()` to `authorizeHttpRequests()`**: This is the new method for configuring request authorization.
7.  **`antMatchers()` to `requestMatchers()`**: The method for matching URLs is now `requestMatchers()`. This applies to both `HttpSecurity` and `WebSecurityCustomizer` configurations. `regexMatchers()` remains if you specifically need regular expressions.
8.  **Import `Customizer`**: You'll need `import org.springframework.security.config.Customizer;` for the new `withDefaults()` calls.

This updated configuration is aligned with Spring Security 6's recommended practices and should resolve all the compilation errors and deprecation warnings you're currently seeing. Remember to rebuild and test your application thoroughly after these changes.

