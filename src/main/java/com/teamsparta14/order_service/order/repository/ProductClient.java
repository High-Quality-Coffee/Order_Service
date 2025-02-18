package com.teamsparta14.order_service.order.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.order.dto.OrderProductRequest;
import com.teamsparta14.order_service.product.dto.ProductListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductClient {


    private final String SERVER_URL = "http://localhost:8080";


    public ProductListResponseDto searchProductList(List<OrderProductRequest> orderProductRequests, String token) {

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path("/api/products/search")
                .encode()
                .build()
                .toUri();

        List<UUID> requestIdList = orderProductRequests.stream().map(OrderProductRequest::getProductId).collect(Collectors.toUnmodifiableList());
        Map<String, List<UUID>> requestBody = new HashMap<>();
        requestBody.put("requestIdList", requestIdList);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        headers.add("access", token);

        HttpEntity<Map<String, List<UUID>>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ApiResponse<ProductListResponseDto> productResponse = objectMapper.readValue(response.getBody(), new TypeReference<ApiResponse<ProductListResponseDto>>() {
            });

            return productResponse.getData();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (HttpClientErrorException ex) { // Catch 4xx and 5xx errors
            String errorMessage = "Client Error: " + ex.getRawStatusCode() + " - " + ex.getResponseBodyAsString();
            throw new RuntimeException(errorMessage, ex); // Re-throw with original exception
        } catch (RestClientException ex) { // Catch other RestClientExceptions
            String errorMessage = "Error calling product service: " + ex.getMessage();
            throw new RuntimeException(errorMessage, ex); // Re-throw
        }


    }
}
