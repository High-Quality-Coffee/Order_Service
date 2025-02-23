package com.teamsparta14.order_service.order.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsparta14.order_service.global.response.ProductClientResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsparta14.order_service.global.response.StoreClientResponse;
import com.teamsparta14.order_service.store.dto.StoreResponseDto;
import com.teamsparta14.order_service.store.entity.Store;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import java.util.UUID;

@Repository
public class StoresClient {

    private final String SERVER_URL = "http://localhost:8080";

    public Store searchStore(String storeId, String token) {

        URI uri = UriComponentsBuilder
                .fromUriString(SERVER_URL)
                .path("/api/stores/" + storeId)
                .encode()
                .build()
                .toUri();


        RestTemplate restTemplate = new RestTemplate();

        ObjectMapper objectMapper = new ObjectMapper();
        try {

            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            StoreClientResponse storeResponseDto =  objectMapper.readValue(
                    response.getBody(),
                    StoreClientResponse.class);
            return storeResponseDto.getData();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException ex) {
            throw new RuntimeException("Error calling store service: " + ex.getMessage());
        }
    }
}

