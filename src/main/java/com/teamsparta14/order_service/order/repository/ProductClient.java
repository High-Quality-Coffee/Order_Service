package com.teamsparta14.order_service.order.repository;



import com.teamsparta14.order_service.order.entity.Order;
import com.teamsparta14.order_service.order.entity.ProductTM;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Service
public class ProductClient {


    private final String SERVER_URL = "http://localhost:8080";

    public ProductTM getById(String id){
        URI uri = UriComponentsBuilder
                .fromUriString(SERVER_URL)
                .path("/api/products/" + id)
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ProductTM> responseEntity = restTemplate.getForEntity(uri, ProductTM.class);

        return responseEntity.getBody();
    }
}
