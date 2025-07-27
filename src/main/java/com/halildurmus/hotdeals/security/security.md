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