package com.mach.review.admin.repository;

import com.mach.review.admin.entity.Customer;
import com.mach.review.admin.entity.ReviewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RatingsAndReviewsRepository extends JpaRepository<ReviewsEntity, Long> {

    //To update the product ratings and reviews
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ReviewsEntity r set r.productReview = :productReview , r.rating =  :rating where r.reviewId = :reviewId and r.productId= :productId")
    public void updateRatingsAndReviews(String productId, String productReview, int rating, int reviewId);

    //To display the collection of pending product ratings and reviews by productId
    @Query("SELECT r from ReviewsEntity r where r.productId= :productId and r.status= :reviewStatus")
    public List<ReviewsEntity> getReviewsByProductIdAndStatus(String productId, String reviewStatus);

    //To approve or reject the product ratings and reviews
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ReviewsEntity r set r.status = :reviewStatus where r.reviewId = :reviewId and r.productId= :productId")
    public void approveOrRejectReviewRatings(String productId, int reviewId, String reviewStatus);

    //To validate the token with user ID
    @Query("SELECT c from Customer c where c.token =:token and c.roleId = 1")
    public Customer validateTokenAndCheckIsAdmin(String token);


    //To fetch customer Name with the ID
    @Query("SELECT c.userName from Customer c where c.id =:userId")
    public String getUserNameById(Long userId);


}
