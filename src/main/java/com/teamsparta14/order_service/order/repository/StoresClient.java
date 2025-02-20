package com.teamsparta14.order_service.order.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.global.response.ProductClientResponse;
import com.teamsparta14.order_service.order.dto.OrderProductRequest;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.product.entity.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class StoresClient {

    private final String SERVER_URL = "http://localhost:8080";

    public Object searchStore(UUID storeId, String token) {

        URI uri = UriComponentsBuilder
                .fromUriString(SERVER_URL)
                .path("/api/stores/" + storeId.toString())
                .encode()
                .build()
                .toUri();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("access", token);

        HttpEntity<Map<String, List<UUID>>> request = new HttpEntity<>(headers);

//        try {
//            ResponseEntity<ApiResponse<Store>> response = restTemplate.getForEntity(uri, new ParameterizedTypeReference<ApiResponse<Store>>() {});
//            System.out.println(response.getBody());

            return null;
//            return response.getBody().getData();
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        } catch (RestClientException ex) {
//            throw new RuntimeException("Error calling product service: " + ex.getMessage());
//        }
    }
}
