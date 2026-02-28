package com.halildurmus.hotdeals.notification;

import com.google.firebase.auth.FirebaseAuthException;
import com.halildurmus.hotdeals.security.role.Role;

public interface NotificationService {

  int send(Notification notification);
  int sendToRole(Role role, Notification notification) throws FirebaseAuthException;
}
