package com.halildurmus.hotdeals.security.role;


import com.google.firebase.auth.FirebaseAuthException;

import java.util.List;

public interface RoleService {

  void add(String uid, Role role);

  void delete(String uid, Role role);
  List<RoleServiceImpl.UserWithRoles> viewAllRoles() throws FirebaseAuthException;
  RoleServiceImpl.UserWithRoles viewAllRolesUser(String uid) throws FirebaseAuthException;
}
