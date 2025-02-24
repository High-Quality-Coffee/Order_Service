package com.teamsparta14.order_service.review.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsparta14.order_service.global.response.OrderClientResponse;
import com.teamsparta14.order_service.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderClient {



    @Value("${project.server.url}")
    private String SERVER_URL;


    public List<OrderResponse> searchOrderList(List<UUID> requestIdList, String token) {

        URI uri = UriComponentsBuilder
                .fromUriString(SERVER_URL)
                .path("/api/orders/search")
                .encode()
                .build()
                .toUri();

        Map<String, List<UUID>> requestBody = new HashMap<>();
        requestBody.put("requestIdList", requestIdList);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("access", token);

        // 요청시 body에 requestIdList = [productId1,productId2,productId3]
        HttpEntity<Map<String, List<UUID>>> request = new HttpEntity<>(requestBody, headers);


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
            System.out.println(response.getBody());
            OrderClientResponse orderResponse = objectMapper.readValue(
                    response.getBody(),
                    OrderClientResponse.class);

            return orderResponse.getData();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException ex) {
            throw new RuntimeException("Error calling order service: " + ex.getMessage());
        }
    }
}
