package com.halildurmus.hotdeals.building.dto;

import lombok.Data;
import java.util.List;

@Data
public class BuildingMapDTO {
    private String id;
    private String buildingId;
    private String type;
    private String address_string;
    private List<String> dealIds;
}
