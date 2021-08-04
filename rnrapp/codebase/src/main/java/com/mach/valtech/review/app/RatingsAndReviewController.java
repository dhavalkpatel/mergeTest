package com.mach.valtech.review.app;

import com.mach.valtech.review.entity.ReviewsEntity;
import com.mach.valtech.review.service.RatingsAndReviewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("services/api/vl/products")
public class RatingsAndReviewController {

    @Autowired
    private RatingsAndReviewsService ratingsAndReviewsService;

    private static String PENDING="PENDING";
    

    //To view collection of Reviews and Ratings for a single product
    @ResponseBody
    @RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.GET)
    public ResponseEntity<List<ReviewsEntity>> getRatingsAndReviewsByProductId(@PathVariable("productCode") String productId) {
        try {
            List<ReviewsEntity> reviewsAndRatingsList = ratingsAndReviewsService.getRatingsAndReviewsByProductId(productId);
            if(reviewsAndRatingsList.size()>0){
                return new ResponseEntity<List<ReviewsEntity>>(reviewsAndRatingsList, new HttpHeaders(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity("No reviews and ratings found for the product " + productId, new HttpHeaders(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error in fetching reviews and ratings for the product "+productId, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //To create reviews and Ratings
    @RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createReviewsAndRatings(HttpServletRequest request, @PathVariable("productCode") String productId, @RequestBody ReviewsEntity reviewsEntity) {
        try {

            String token = StringUtils.isNotEmpty(request.getHeader("token")) ? request.getHeader("token") : "";
            String userId = ratingsAndReviewsService.findCustomerIdByToken(token);
            reviewsEntity.setUserId(userId);
            reviewsEntity.setProductId(productId);
            reviewsEntity.setStatus(PENDING);
            if (userId != null && productId != null) {
                ratingsAndReviewsService.createReviewsAndRatings(reviewsEntity);
                return new ResponseEntity<String>("Product Review created successfully for the product " + productId, new HttpHeaders(), HttpStatus.CREATED);
            } else {
                if (userId == null) {
                    return new ResponseEntity<String>("Customer ID not found ", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<String>("Cannot create product review for the product " + productId, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Error creating product review for the product " + productId, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // To view a single product rating and review by reviewId
    @ResponseBody
    @RequestMapping(value = "/{productCode}/reviews/{id}", method = RequestMethod.GET)
    public ResponseEntity<ReviewsEntity> getRatingsAndReviewsById(@PathVariable("productCode") String productId,@PathVariable("id") int reviewId) {
        try {
            ReviewsEntity reviewsAndRatings = ratingsAndReviewsService.getRatingsAndReviewsById(productId,reviewId);
            if(reviewsAndRatings!=null){
                return new ResponseEntity<ReviewsEntity>(reviewsAndRatings, new HttpHeaders(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity("No reviews and ratings found for the review ID : " + reviewId+" and product ID : "+productId, new HttpHeaders(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error in fetching reviews and ratings for the review ID : "+reviewId+" and product ID : "+productId,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}