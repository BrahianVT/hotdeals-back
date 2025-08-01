package com.halildurmus.hotdeals.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryPostDTO {

  @Schema(
      description = "Category names",
      example = "{\"en\": \"Computers\", \"tr\": \"Bilgisayar\"}",
          type = "object",
          implementation = Map.class
  )
  @NotNull
  private final Map<String, String> names;

  @Schema(description = "Parent category path", example = "/")
  @NotBlank
  private final String parent;

  @Schema(description = "Category path", example = "/computers")
  @NotBlank
  private final String category;

  @Schema(description = "Category icon ligature", example = "computer")
  private final String iconLigature;

  @Schema(description = "Category icon font family", example = "MaterialIcons")
  private final String iconFontFamily;

  @Schema(description = "Indicates if this category is a tag", example = "false")
  private Boolean isTag;
}
