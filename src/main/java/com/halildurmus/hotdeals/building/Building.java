package com.halildurmus.hotdeals.building;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @Schema(description = "Building ID", example = "018f3a2b")
  @Indexed(unique = true)
  @NotBlank
  @Size(max = 10)
  private String buildingId;

  @Schema(description = "Map ID", example = "frutas-verduras")
  @Indexed
  private String mapId;

  @Schema(description = "Floor level", example = "1")
  private Integer floorLevel;

  @Schema(description = "Building type", example = "bodega")
  private String type;

  @Schema(description = "Building address", example = "J400-A")
  @NotBlank
  private String addressString;
}
