package com.app.controller;



import com.app.exception.ProductException;
import com.app.model.Rating;
import com.app.model.User;
import com.app.request.RatingRequest;
import com.app.response.GenericResponse;
import com.app.service.RatingService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createRating(
            @RequestBody RatingRequest ratingRequest,
            @AuthenticationPrincipal User user
    ) {
        try {
            Rating createdRating = ratingService.createRating(ratingRequest, user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new GenericResponse(true, "Rating created successfully."));
        } catch (ProductException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/product")
    public ResponseEntity<List<Rating>> getProductRatings(@RequestParam Long productId) {
        List<Rating> ratings = ratingService.getProductsRating(productId);
        return ResponseEntity.ok(ratings);
    }
}
