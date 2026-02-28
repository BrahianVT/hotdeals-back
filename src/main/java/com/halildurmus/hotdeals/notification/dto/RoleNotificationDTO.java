package com.halildurmus.hotdeals.notification.dto;


import com.google.firebase.database.annotations.NotNull;
import com.halildurmus.hotdeals.notification.Notification;
import com.halildurmus.hotdeals.security.role.Role;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class RoleNotificationDTO {
    @NotNull
    private Role role;

    @NotNull
    @Valid
    private Notification notification;
}
