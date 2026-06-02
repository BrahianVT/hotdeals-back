package com.halildurmus.hotdeals.building;

import com.halildurmus.hotdeals.building.dto.BuildingMapDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

  @Autowired
  private BuildingRepository repository;

  public List<BuildingMapDTO> getBuildingsWithDealIds() {
    return repository.findAllBuildingsWithDealIds();
  }

  public List<BuildingMapDTO> getBuildingsWithDealIds(String mapId, Integer floorLevel) {
    return repository.findBuildingsWithDealIdsByMapIdAndFloorLevel(mapId, floorLevel);
  }

  public Building saveBuilding(Building building) {
    Optional<Building> existingBuilding = repository.findByBuildingId(building.getBuildingId());
    existingBuilding.ifPresent(existing -> building.setId(existing.getId()));
    return repository.save(building);
  }
}
