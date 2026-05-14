package com.halildurmus.hotdeals.building;

import com.halildurmus.hotdeals.building.dto.BuildingMapDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

  @Autowired
  private BuildingRepository repository;

  public List<BuildingMapDTO> getBuildingsWithDealIds() {
    return repository.findAllBuildingsWithDealIds();
  }

  public Building saveBuilding(Building building) {
    return repository.save(building);
  }
}
