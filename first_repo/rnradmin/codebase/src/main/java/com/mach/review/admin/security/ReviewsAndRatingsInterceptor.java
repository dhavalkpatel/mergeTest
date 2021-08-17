package com.mach.review.admin.security;

import com.mach.review.admin.service.RatingsAndReviewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@Component
public class ReviewsAndRatingsInterceptor implements HandlerInterceptor {

    @Autowired
    private RatingsAndReviewsService ratingsAndReviewsService;

    Logger LOG = Logger.getLogger(ReviewsAndRatingsInterceptor.class.getName());

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = StringUtils.isNotEmpty(request.getHeader("token"))?request.getHeader("token"):"";
        boolean isValidationSuccess = ratingsAndReviewsService.validateTokenAndCheckIsAdmin(token);
        LOG.info("Interceptor activated..!! " + "    token : " + token + "    return value: " + isValidationSuccess);
        return isValidationSuccess;
    }
}
