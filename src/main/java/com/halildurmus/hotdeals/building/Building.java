package com.halildurmus.hotdeals.building;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "buildings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Building {

  @Id
  private String id;

  @Schema(description = "Building ID", example = "BLD-123")
  @Indexed(unique = true)
  @NotBlank
  @Size(max = 10)
  private String buildingId;

  @Schema(description = "Building type", example = "academic")
  private String type;

  @Schema(description = "Building address", example = "123 Main St")
  @NotBlank
  private String addressString;
}
