package com.mach.valtech.review.service;

import com.mach.valtech.review.entity.Customer;
import com.mach.valtech.review.entity.ReviewsEntity;
import com.mach.valtech.review.repository.RatingsAndReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RatingsAndReviewsServiceImpl implements RatingsAndReviewsService {

    @Autowired
    RatingsAndReviewsRepository ratingsAndReviewsRepository;

    @Override
    public List<ReviewsEntity> getRatingsAndReviewsByProductId(String productId) {
        List<ReviewsEntity> reviewsList = ratingsAndReviewsRepository.getRatingsAndReviewsByProductId(productId);
        for(ReviewsEntity reviews:reviewsList) {
            reviews.setUserName(ratingsAndReviewsRepository.getUserNameById(Long.parseLong(reviews.getUserId())));
        }
        return reviewsList;
    }

    @Override
    public ReviewsEntity getRatingsAndReviewsById(String productId,int reviewId) {
        ReviewsEntity reviews = ratingsAndReviewsRepository.getRatingsAndReviewsById(productId,reviewId);
        reviews.setUserName(ratingsAndReviewsRepository.getUserNameById(Long.parseLong(reviews.getUserId())));
        return reviews;
    }

    @Override
    public void createReviewsAndRatings(ReviewsEntity reviewsEntity) {
        reviewsEntity.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
        ratingsAndReviewsRepository.save(reviewsEntity);
    }

    @Override
    public boolean validateToken(String token) {
      Customer customer= ratingsAndReviewsRepository.validateTokenWithUser(token);
      if(customer!=null){
          return true;
      }
      else{
          return false;

      }
    }

    @Override
    public String findCustomerIdByToken(String token) {
        Long userId=ratingsAndReviewsRepository.findCustomerIdByToken(token);
        return userId.toString();
    }

}
