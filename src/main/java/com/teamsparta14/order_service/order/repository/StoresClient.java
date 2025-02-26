package com.teamsparta14.order_service.order.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsparta14.order_service.global.response.StoreClientResponse;
import com.teamsparta14.order_service.store.dto.StoreResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Repository
public class StoresClient {

    @Value("${SERVER_URL}")
    private String SERVER_URL;

    public StoreResponseDto searchStore(String storeId, String token) {

        URI uri = UriComponentsBuilder
                .fromUriString(SERVER_URL)
                .path("/api/stores/" + storeId)
                .encode()
                .build()
                .toUri();


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("access", token);

        HttpEntity<Object> request = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET,request, String.class);
            StoreClientResponse storeResponseDto =  objectMapper.readValue(
                    response.getBody(),
                    StoreClientResponse.class);

            System.out.println(response.getBody());
            return storeResponseDto.getData();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException ex) {
            throw new RuntimeException("Error calling store service: " + ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

