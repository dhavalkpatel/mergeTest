package com.mach.valtech.review.service;

import com.mach.valtech.review.entity.ReviewsEntity;

import java.util.List;

public interface RatingsAndReviewsService {
    public List<ReviewsEntity> getRatingsAndReviewsByProductId(String productId);

    public ReviewsEntity getRatingsAndReviewsById(String productId, int reviewId);

    public void createReviewsAndRatings(ReviewsEntity reviewsEntity);

    public boolean validateToken(String token);

    public String findCustomerIdByToken(String token);

}
