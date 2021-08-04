package com.mach.valtech.review.repository;

import com.mach.valtech.review.entity.Customer;
import com.mach.valtech.review.entity.ReviewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RatingsAndReviewsRepository extends JpaRepository<ReviewsEntity, Long> {

    //To display the product ratings and reviews by reviewId and productId
    @Query("SELECT r from ReviewsEntity r where r.reviewId= :reviewId and r.status = 'APPROVED' and r.productId= :productId")
    public ReviewsEntity getRatingsAndReviewsById(String productId, int reviewId);

    //To display the collection of product ratings and reviews by productId
    @Query("SELECT r from ReviewsEntity r where r.productId= :productId and r.status = 'APPROVED'")
    public List<ReviewsEntity> getRatingsAndReviewsByProductId(String productId);

    //To validate the token with user ID
    @Query("SELECT c from Customer c where c.token =:token")
    public Customer validateTokenWithUser(String token);

    //To fetch customer ID with the token
    @Query("SELECT c.id from Customer c where c.token =:token")
    public Long findCustomerIdByToken(String token);

    //To fetch customer Name with the ID
    @Query("SELECT c.userName from Customer c where c.id =:userId")
    public String getUserNameById(Long userId);


}
