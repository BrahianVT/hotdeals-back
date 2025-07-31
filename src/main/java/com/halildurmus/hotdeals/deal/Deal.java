package com.halildurmus.hotdeals.deal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.halildurmus.hotdeals.audit.DateAudit;
import com.halildurmus.hotdeals.util.ObjectIdJsonSerializer;
import com.halildurmus.hotdeals.util.ObjectIdSetJsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deals")
@TypeAlias("deal")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Deal extends DateAudit {

  private static final long serialVersionUID = 1234567L;

  @Schema(description = "Deal ID", example = "5fbe790ec6f0b32014074bb2")
  @Id
  private String id;

  @Indexed
  @JsonProperty(access = Access.READ_ONLY)
  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId postedBy;


  @JsonSerialize(using = ObjectIdJsonSerializer.class)
  private ObjectId store;

  @Schema(description = "Deal score", example = "3")
  private int dealScore = 0;

  @Default
  @JsonSerialize(using = ObjectIdSetJsonSerializer.class)
  private HashSet<ObjectId> upvoters = new HashSet<>();

  @Default
  @JsonSerialize(using = ObjectIdSetJsonSerializer.class)
  private HashSet<ObjectId> downvoters = new HashSet<>();

  @Schema(description = "Category path", example = "/computers/monitors")
  @NotBlank
  private String category;

  @Schema(description = "Deal tags", example = "[\"discount\", \"electronics\", \"sale\"]")
  @Default
  private List<String> tags = new ArrayList<>();

  @Schema(
      description = "Deal title",
      example = "HP 24mh FHD Monitor with 23.8-Inch IPS Display (1080p)")
  @NotBlank
  @Size(min = 10, max = 100)
  private String title;

  @Schema(
      description = "Deal description",
      example =
          "OUTSTANDING VISUALS – This FHD display with IPS technology gives you brilliant visuals and unforgettable quality; with a maximum resolution of 1920 x 1080 at 75 Hz, you’ll experience the image accuracy and wide-viewing spectrums of premium tablets and mobile devices ")
  @NotBlank
  @Size(min = 10, max = 3000)
  private String description;

  @Schema(description = "Deal original price", example = "249.99")
  @Min(1)
  private Double originalPrice;

  @Schema(description = "Deal price", example = "226.99")
  @Min(0)
  private Double price;

  @Schema(description = "Deal cover photo URL", example = "https://www.gravatar.com/avatar")
  @URL
  @NotNull
  private String coverPhoto;

  @Schema(
      description = "Deal URL",
      example = "https://www.amazon.com/HP-24mh-FHD-Monitor-Built/dp/B08BF4CZSV/")
  @URL
  private String dealUrl;

  @Default private DealStatus status = DealStatus.ACTIVE;

  @Schema(description = "Deal photo URLs", example = "[\"https://www.gravatar.com/avatar/1\", \"https://www.gravatar.com/avatar/2\"]")
  private List<String> photos = new ArrayList<>();

  @Schema(description = "Deal views", example = "10")
  private int views = 0;

  @Schema(description = "location, is the hallway ", example = "I-3")
  private String location;
}