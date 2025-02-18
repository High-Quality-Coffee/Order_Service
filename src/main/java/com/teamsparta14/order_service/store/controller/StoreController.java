package com.teamsparta14.order_service.store.controller;

import com.teamsparta14.order_service.store.dto.StoreRequestDto;
import com.teamsparta14.order_service.store.dto.StoreResponseDto;
import com.teamsparta14.order_service.store.dto.StoreUpdateRequestDto;
import com.teamsparta14.order_service.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(
            @RequestBody StoreRequestDto dto,
            @RequestHeader("X-USER-ID") String createdBy
    ) {
        return ResponseEntity.ok(storeService.createStore(dto, createdBy));
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable UUID storeId,
            @RequestBody StoreUpdateRequestDto requestDto,
            @RequestHeader("X-USER-ID") String modifiedBy
    ) {
        return ResponseEntity.ok(storeService.updateStore(storeId, requestDto, modifiedBy));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable UUID storeId,
            @RequestHeader("X-USER-ID") String deletedBy
    ) {
        storeService.deleteStore(storeId, deletedBy);
        return ResponseEntity.noContent().build();
    }
}
