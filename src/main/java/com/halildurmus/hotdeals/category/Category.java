package com.halildurmus.hotdeals.category;

import com.halildurmus.hotdeals.audit.DateAudit;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@TypeAlias("category")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends DateAudit {

  private static final long serialVersionUID = 1234567L;

  @Id private String id;

  @NotNull private Map<String, String> names;

  @NotBlank private String parent;

  @Indexed(unique = true)
  @NotBlank
  private String category;

   private String iconLigature;

   private String iconFontFamily;


    private Boolean isTag;
}
