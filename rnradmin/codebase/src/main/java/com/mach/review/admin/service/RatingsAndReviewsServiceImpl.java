package com.mach.review.admin.service;

import com.mach.review.admin.entity.Customer;
import com.mach.review.admin.entity.ReviewsEntity;
import com.mach.review.admin.repository.RatingsAndReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingsAndReviewsServiceImpl implements RatingsAndReviewsService {

    @Autowired
    RatingsAndReviewsRepository ratingsAndReviewsRepository;

    @Override
    public List<ReviewsEntity> getReviewsByProductIdAndStatus(String productId, String reviewStatus) {
        List<ReviewsEntity> reviewsList = ratingsAndReviewsRepository.getReviewsByProductIdAndStatus(productId,reviewStatus);
        for(ReviewsEntity reviews:reviewsList) {
            reviews.setUserName(ratingsAndReviewsRepository.getUserNameById(Long.parseLong(reviews.getUserId())));
        }
        return reviewsList;
    }

    @Override
    public boolean validateTokenAndCheckIsAdmin(String token) {
      Customer customer= ratingsAndReviewsRepository.validateTokenAndCheckIsAdmin(token);
      if(customer!=null){
          return true;
      }
      else{
          return false;

      }
    }


    public void approveOrRejectOrUpdateReviewRatings(String productId, int reviewId, String reviewStatus,ReviewsEntity reviewsEntity){
        if(reviewStatus==null|reviewStatus.isBlank()){
            ratingsAndReviewsRepository.updateRatingsAndReviews(productId,reviewsEntity.getProductReview(), reviewsEntity.getRating(), reviewId);
        }
        else{
            ratingsAndReviewsRepository.approveOrRejectReviewRatings(productId,reviewId,reviewStatus);
        }
    }

}
