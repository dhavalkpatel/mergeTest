package com.mach.valtech.review.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "reviews")
public class ReviewsEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "review_id")
    private int reviewId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "application_id")
    private String applicationId;
    @Column(name = "review_title")
    private String reviewTitle;
    @Column(name = "product_review")
    private String productReview;
    @Column(name = "rating")
    private int rating;
    @Column(name = "status")
    private String status;
    @Column (name="review_date")
    private Date creationDate;
    @Transient
    private String userName;


    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getProductReview() {
        return productReview;
    }

    public void setProductReview(String productReview) {
        this.productReview = productReview;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
