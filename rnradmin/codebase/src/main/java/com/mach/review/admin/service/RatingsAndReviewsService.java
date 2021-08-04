package com.mach.review.admin.service;

import com.mach.review.admin.entity.ReviewsEntity;

import java.util.List;

public interface RatingsAndReviewsService {
    public List<ReviewsEntity> getReviewsByProductIdAndStatus(String productId, String reviewStatus);

    public boolean validateTokenAndCheckIsAdmin(String token);

    public void approveOrRejectOrUpdateReviewRatings(String productId, int reviewId, String reviewStatus,ReviewsEntity reviewsEntity);

}
