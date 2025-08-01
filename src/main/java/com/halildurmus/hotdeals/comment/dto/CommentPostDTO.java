package com.halildurmus.hotdeals.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentPostDTO {

  @Schema(description = "Comment message", example = "Thanks :)")
  @NotBlank
  @Size(min = 1, max = 500)
  private String message;
}
