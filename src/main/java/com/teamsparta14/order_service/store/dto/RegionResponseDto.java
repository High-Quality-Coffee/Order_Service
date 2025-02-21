package com.teamsparta14.order_service.store.dto;

import com.teamsparta14.order_service.store.entity.Region;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RegionResponseDto {
    private UUID id;
    private String regionName;

    public RegionResponseDto(Region region) {
        this.id = region.getId();
        this.regionName = region.getRegionName();
    }
}
