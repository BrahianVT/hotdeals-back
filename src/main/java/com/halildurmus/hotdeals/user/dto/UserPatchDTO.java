package com.halildurmus.hotdeals.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
public class UserPatchDTO {

  @Schema(description = "User avatar URL", example = "https://www.gravatar.com/avatar")
  @URL
  @NotBlank
  private Optional<String> avatar;

  @Schema(description = "User nickname", example = "MrNobody123")
  @NotBlank
  @Size(min = 5, max = 25)
  private Optional<String> nickname;
}
