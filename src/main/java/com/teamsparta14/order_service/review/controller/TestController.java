package com.teamsparta14.order_service.review.controller;

import com.teamsparta14.order_service.review.dto.RatingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/tests")
public class TestController {

    @PostMapping("/{storeId}")
    public ResponseEntity<String> getStars(@RequestBody RatingDto ratingDto, @PathVariable("storeId") String storeId) {

        log.info("rating = {}", storeId.toString());
        log.info("ratingDto = {}", ratingDto.getStar());

        return ResponseEntity.ok().body("hello");
    }
/*
    @GetMapping
    public ResponseEntity<String> getStars() {

        log.info("hello!");

        return ResponseEntity.ok("hello");
    }*/
}
