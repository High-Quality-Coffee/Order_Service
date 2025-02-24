package com.teamsparta14.order_service.order.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.global.response.ResponseCode;
import com.teamsparta14.order_service.order.dto.OrderCreateDto;
import com.teamsparta14.order_service.order.dto.OrderResponse;
import com.teamsparta14.order_service.order.dto.OrderSearchDto;
import com.teamsparta14.order_service.order.dto.OrderUpdateRequest;
import com.teamsparta14.order_service.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;



    @Operation(summary = "주문 생성", description = "주문시 사용 API")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestBody OrderCreateDto createDto,
            @RequestHeader(name = "access") String token
    ){

        return ResponseEntity.ok(ApiResponse.success(orderService.createOrder(createDto ,token)));
    }

    @Operation(summary = "주문 삭제", description = "주문 삭제시 사용 API")
    @DeleteMapping(path = "/{order_id}")
    public ResponseEntity<ApiResponse<OrderResponse>> deleteOrder(
            @PathVariable(name = "order_id") UUID orderId,
            @RequestHeader(name = "access") String token) {


        log.info("orderId : {}", orderId);
        return ResponseEntity.ok(ApiResponse.success(orderService.deleteOrder(orderId, token)));
    }

    @Operation(summary = "주문 단일 조회", description = "주문Id를 통해 주문을 조회")
    @GetMapping(path = "/{order_id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable(name = "order_id") UUID orderId,
            @RequestHeader(name = "access") String token){

        log.info("orderId : {}",orderId);

        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(orderId,token)));
    }


    @Operation(summary = "주문 전체 조회", description = "주문 전체 조회")
    @Secured({"ROLE_USER","ROLE_MASTER"})
    @GetMapping
    public ResponseEntity<ApiResponse<PagedModel<OrderResponse>>> getOrderPageByUserName(
            @RequestHeader(name = "access") String token,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "100") int limit,
            @RequestParam(name = "isAsc" , defaultValue = "true") Boolean isAsc,
            @RequestParam(name = "orderBy") String orderBy){


        return ResponseEntity.ok(ApiResponse.success(new PagedModel<>(orderService.searchOrders(token,page,limit,isAsc,orderBy))));
    }
    @Operation(summary = "주문 수정", description = "주문 수정시 사용 API")
    @Secured({"ROLE_USER"})
    @PutMapping
    public ResponseEntity<OrderResponse> updateOrder(
            @RequestBody OrderUpdateRequest orderUpdateRequest,
            @RequestHeader(name = "access") String token
    ){

        return ResponseEntity.ok(orderService.updateOrder(orderUpdateRequest,token));
    }



    //사장만 가능
    @Operation(summary = "주문 전체 조회", description = "storeId를 통해 주문조회 API")
    @Secured({"ROLE_OWNER","ROLE_MASTER"})
    @GetMapping("/{store_id}/orders")
    public ResponseEntity<ApiResponse<PagedModel<OrderResponse>>> getOrderPageByStoreId(
            @RequestHeader(name = "access") String token,
            @PathVariable(name = "store_id") @NotNull String storeId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "100") int limit,
            @RequestParam(name = "isAsc" , defaultValue = "true") Boolean isAsc,
            @RequestParam(name = "orderBy") String orderBy){


        return ResponseEntity.ok(ApiResponse.success(new PagedModel<>(orderService.searchOrdersByStoreId(storeId,token,page,limit,isAsc,orderBy))));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> searchOrder(@RequestBody OrderSearchDto requestDto) {

        return ResponseEntity.ok().body(ApiResponse.success(orderService.searchProduct(requestDto)));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponse<?>> exceptionHandlerIllegal(Exception e ){

        return ResponseEntity.badRequest().body(ApiResponse.fail(ResponseCode.BAD_REQUEST,e.getMessage()));

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse<?>> exceptionHandlerException(Exception e ){

        return ResponseEntity.internalServerError().body(ApiResponse.fail(ResponseCode.INTERNAL_SERVER_ERROR,e.getMessage()));

    }
}
