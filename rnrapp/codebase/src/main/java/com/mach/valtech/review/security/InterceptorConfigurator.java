package com.mach.valtech.review.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfigurator implements WebMvcConfigurer {

    @Autowired
    ReviewsAndRatingsInterceptor reviewsAndRatingsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reviewsAndRatingsInterceptor);
    }
}