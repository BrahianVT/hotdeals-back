package com.halildurmus.hotdeals.notification.dto;

import com.halildurmus.hotdeals.notification.Notification;
import com.halildurmus.hotdeals.security.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to send notification to all users with a specific role")
public class RoleNotificationRequest {

    @NotNull
    @Schema(description = "Role to send notification to", example = "ROLE_MODERATOR")
    private Role role;


    @Schema(description = "Notification content (tokens will be auto-populated)")
    private Notification notification;
}