package com.halildurmus.hotdeals.building;

import com.halildurmus.hotdeals.building.dto.BuildingMapDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  @Operation(summary = "Returns buildings and their associated deal IDs, optionally filtered by mapId and floorLevel")
  public List<BuildingMapDTO> getBuildingsMapData(
      @RequestParam(required = false) String mapId,
      @RequestParam(required = false) Integer floorLevel) {
    if (mapId != null && floorLevel != null) {
      return service.getBuildingsWithDealIds(mapId, floorLevel);
    }
    return service.getBuildingsWithDealIds();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Registers a new building or updates an existing one")
  public Building createBuilding(@Valid @RequestBody Building building) {
    return service.saveBuilding(building);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Finds a building by ID")
  public Building getBuilding(@PathVariable String id) {
    return service.getBuilding(id);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Updates an existing building")
  public Building updateBuilding(@PathVariable String id, @Valid @RequestBody Building building) {
    return service.updateBuilding(id, building);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Deletes a building")
  public void deleteBuilding(@PathVariable String id) {
    service.deleteBuilding(id);
  }
}
