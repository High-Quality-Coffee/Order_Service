package com.teamsparta14.order_service.store.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StoreRegionId implements Serializable {
    private UUID storeId;
    private UUID regionId;

}
