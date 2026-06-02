package com.halildurmus.hotdeals.building;

import com.halildurmus.hotdeals.building.dto.BuildingMapDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends MongoRepository<Building, String> {

  @Aggregation(pipeline = {
      "{ $match: { mapId: ?0, floorLevel: ?1 } }",
      "{ $lookup: { from: 'deals', localField: 'buildingId', foreignField: 'buildingId', as: 'deals' } }",
      "{ $project: { _id: 1, buildingId: 1, mapId: 1, floorLevel: 1, type: 1, address_string: 1, dealIds: { $map: { input: '$deals', as: 'deal', in: { $toString: '$$deal._id' } } } } }"
  })
  List<BuildingMapDTO> findBuildingsWithDealIdsByMapIdAndFloorLevel(String mapId, Integer floorLevel);

  @Aggregation(pipeline = {
      "{ $lookup: { from: 'deals', localField: 'buildingId', foreignField: 'buildingId', as: 'deals' } }",
      "{ $project: { _id: 1, buildingId: 1, mapId: 1, floorLevel: 1, type: 1, address_string: 1, dealIds: { $map: { input: '$deals', as: 'deal', in: { $toString: '$$deal._id' } } } } }"
  })
  List<BuildingMapDTO> findAllBuildingsWithDealIds();

  Optional<Building> findByBuildingId(String buildingId);
}
