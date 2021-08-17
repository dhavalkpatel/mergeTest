package com.mach.valtech.review.security;

import com.mach.valtech.review.service.RatingsAndReviewsService;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ReviewsAndRatingsInterceptor implements HandlerInterceptor {

    @Autowired
    private RatingsAndReviewsService ratingsAndReviewsService;

    Logger LOG = Logger.getLogger(ReviewsAndRatingsInterceptor.class.getName());

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = StringUtils.isNotEmpty(request.getHeader("token")) ? request.getHeader("token") : "";
        boolean isValidationSuccess = ratingsAndReviewsService.validateToken(token);
        LOG.info("Interceptor activated..!! " + "    token : " + token + "    return value: " + isValidationSuccess);
        return isValidationSuccess;
    }


}
