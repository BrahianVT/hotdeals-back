package com.halildurmus.hotdeals.security.role;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.*;
import java.util.stream.Collectors;

import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

  @Autowired private FirebaseAuth firebaseAuth;

  @Override
  public void add(String uid, Role role) {
    try {
      var user = firebaseAuth.getUser(uid);
      Map<String, Object> claims = new HashMap<>(user.getCustomClaims());
      claims.putIfAbsent(role.name(), true);
      firebaseAuth.setCustomUserClaims(uid, claims);
    } catch (FirebaseAuthException e) {
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Firebase Auth Exception", e);
    }
  }

  @Override
  public void delete(String uid, Role role) {
    try {
      var user = firebaseAuth.getUser(uid);
      Map<String, Object> claims = new HashMap<>(user.getCustomClaims());
      claims.remove(role.name());
      firebaseAuth.setCustomUserClaims(uid, claims);
    } catch (FirebaseAuthException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Firebase Auth Exception", e);
    }
  }

  @Getter
  @Schema(description = "User information with associated roles")
  public static class UserWithRoles {
      // Getters for these properties
      @Schema(description = "Unique identifier for the user", example = "abc123xyz456")
      private String uid;
    @Schema(description = "Email address of the user", example = "user@example.com")
    private String email;

    @Schema(description = "Display name of the user", example = "John Doe")
    private String displayName;
    @Schema(description = "Set of roles assigned to the user", example = "[ROLE_USER, ROLE_ADMIN]")
    private Set<Role> roles; // Using a Set to store unique roles

    public UserWithRoles(String uid, String email, String displayName, Set<Role> roles) {
      this.uid = uid;
      this.email = email;
      this.displayName = displayName;
      this.roles = roles;
    }

      @Override
    public String toString() {
      return "User(UID: " + uid + ", Email: " + email + ", DisplayName: " + displayName + ", Roles: " + roles + ")";
    }
  }

  public List<UserWithRoles> viewAllRoles() throws FirebaseAuthException {
    List<UserWithRoles> usersWithRoles = new ArrayList<>();
    ListUsersPage page = null; // Initialize page to null
    String nextPageToken = null; // Initialize next page token to null

    do {
      // Fetch users. If nextPageToken is null, it fetches the first page.
      // You can also specify a maxResults per page, e.g., firebaseAuth.listUsers(null, 1000);
      // Default maxResults is 1000.
      page = firebaseAuth.listUsers(nextPageToken);

      for (UserRecord user : page.getValues()) {
        Set<Role> roles = new HashMap<>(user.getCustomClaims())
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof Boolean && (Boolean) entry.getValue())
                .map(entry -> {
                  try {
                    return Role.valueOf(entry.getKey());
                  } catch (IllegalArgumentException e) {
                    log.debug("Found non-role custom claim for user {}: {}", user.getUid(), entry.getKey());
                    return null;
                  }
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        usersWithRoles.add(new UserWithRoles(user.getUid(), user.getEmail(), user.getDisplayName(), roles));
      }

      nextPageToken = page.getNextPageToken(); // Get the token for the next page
      // The loop condition is now based on page.hasNextPage()
    } while (page.hasNextPage()); // This is the most reliable way to loop

    return usersWithRoles;
  }


  @Override
  public UserWithRoles viewAllRolesUser(String uid) throws FirebaseAuthException {
    try {
      UserRecord user = firebaseAuth.getUser(uid);
      var roles =  new HashMap<>(user.getCustomClaims())
              .entrySet()
              .stream()
              .filter(entry -> entry.getValue() instanceof Boolean && (Boolean) entry.getValue())
              .map(entry -> Role.valueOf(entry.getKey()))
              .collect(Collectors.toSet());
      return new UserWithRoles(user.getUid(), user.getEmail(), user.getDisplayName(), roles);
    } catch (FirebaseAuthException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
    }
  }

  @Override
  public List<UserWithRoles> getUsersByRole(Role role) throws FirebaseAuthException {
    return viewAllRoles().stream()
            .filter(user -> user.getRoles().contains(role))
            .collect(Collectors.toList());
  }
}
