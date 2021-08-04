package com.mach.review.admin.controller;

import com.mach.review.admin.entity.ReviewsEntity;
import com.mach.review.admin.service.RatingsAndReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("services/api/vl/products")
public class RatingsAndReviewAdminController {

	@Autowired
	private RatingsAndReviewsService ratingsAndReviewsService;


	//To view collection of Pending Reviews and Ratings for a single product
	@ResponseBody
	@RequestMapping(value = "/{productCode}/reviews", method = RequestMethod.GET)
	public ResponseEntity<List<ReviewsEntity>> getReviewsByProductIdAndStatus(@PathVariable("productCode") String productId,@RequestParam ("reviewStatus") String reviewStatus) {
		try {
			List<ReviewsEntity> reviewsAndRatingsList = ratingsAndReviewsService.getReviewsByProductIdAndStatus(productId,reviewStatus);
			if(reviewsAndRatingsList.size()>0){
				return new ResponseEntity<List<ReviewsEntity>>(reviewsAndRatingsList, new HttpHeaders(), HttpStatus.OK);
			}
			else {
				return new ResponseEntity("No reviews and ratings found in pending status for the product " + productId, new HttpHeaders(), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error in fetching reviews and ratings for the product "+productId, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// To update a product review and ratings
	@ResponseBody
	@RequestMapping(value = "/{productCode}/reviews/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<String> approveOrRejectRaviewRatings(@RequestBody ReviewsEntity reviewsEntity,@RequestParam("reviewStatus") String reviewStatus, @PathVariable("productCode") String productId,@PathVariable("id") int reviewId) {
		try {
			ratingsAndReviewsService.approveOrRejectOrUpdateReviewRatings(productId,reviewId,reviewStatus,reviewsEntity);
			return new ResponseEntity("Successfully updated the status of review ratings for the product "+productId,new HttpHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Error occurred during updation of status for the review ratings of the product "+productId,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}