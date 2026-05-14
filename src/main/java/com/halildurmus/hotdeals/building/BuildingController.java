package com.halildurmus.hotdeals.building;

import com.halildurmus.hotdeals.building.dto.BuildingMapDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "buildings")
@RestController
@RequestMapping("/buildings")
@Validated
public class BuildingController {

  @Autowired
  private BuildingService service;

  @GetMapping("/map-data")
  @Operation(summary = "Returns all registered buildings and their associated deal IDs")
  public List<BuildingMapDTO> getBuildingsMapData() {
    return service.getBuildingsWithDealIds();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Registers a new building")
  public Building createBuilding(@Valid @RequestBody Building building) {
    return service.saveBuilding(building);
  }
}
