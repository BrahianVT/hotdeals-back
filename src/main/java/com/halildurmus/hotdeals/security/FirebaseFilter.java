package com.halildurmus.hotdeals.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.halildurmus.hotdeals.security.models.Credentials;
import com.halildurmus.hotdeals.security.models.FirebaseAuthenticationToken;
import com.halildurmus.hotdeals.security.models.SecurityProperties;
import com.halildurmus.hotdeals.security.role.Role;
import com.halildurmus.hotdeals.security.role.RoleService;
import com.halildurmus.hotdeals.user.User;
import com.halildurmus.hotdeals.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
public class FirebaseFilter extends OncePerRequestFilter {

  private static final String HEADER_NAME = "Authorization";

  @Autowired private RoleService roleService;

  @Autowired private SecurityProperties securityProperties;

  @Autowired private UserRepository userRepository;

  @SneakyThrows
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    if (request.getHeader(HEADER_NAME) != null) {
      verifyToken(request);
    }
    filterChain.doFilter(request, response);
  }

  public String parseBearerToken(HttpServletRequest request) throws Exception {
    var authorization = request.getHeader("Authorization");
    if (!authorization.startsWith("Bearer ")) {
      throw new Exception("Invalid Authorization header! The header must contain a Bearer token.");
    }
    return authorization.substring(7);
  }

  private User firebaseTokenToUser(FirebaseToken decodedToken) {
    log.info(decodedToken.getUid());
    return userRepository.findByUid(decodedToken.getUid()).orElse(null);
  }

  public List<GrantedAuthority> parseAuthorities(FirebaseToken token, String email) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    // Handle ROLE_SUPER authority
    if (securityProperties.getSuperAdmins() != null
        && securityProperties.getSuperAdmins().contains(email)) {
      if (!token.getClaims().containsKey(Role.ROLE_SUPER.name())) {
        try {
          roleService.add(token.getUid(), Role.ROLE_SUPER);
        } catch (Exception e) {
          log.error("Failed to add ROLE_SUPER authority to the user", e);
        }
      }
      authorities.add(new SimpleGrantedAuthority(Role.ROLE_SUPER.name()));
    }
    // Handle other authorities
    token.getClaims().forEach((k, v) -> authorities.add(new SimpleGrantedAuthority(k)));

    return authorities;
  }

  private void verifyToken(HttpServletRequest request) throws Exception {
    var token = parseBearerToken(request);
    log.info("Bearer Token: " + token);
    FirebaseToken decodedToken;
    try {
      decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
      log.info("Decoded Token: " + decodedToken);
    } catch (FirebaseAuthException e) {
      log.error("Firebase Exception: " + e.getLocalizedMessage());
      throw e;
    }

    var user = firebaseTokenToUser(decodedToken);
    log.info("User: " + user);
    List<GrantedAuthority> authorities = parseAuthorities(decodedToken, user.getEmail());
    var authentication =
        new FirebaseAuthenticationToken(user, new Credentials(decodedToken, token), authorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
