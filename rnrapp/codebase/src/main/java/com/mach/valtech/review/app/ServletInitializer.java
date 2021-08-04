package com.mach.valtech.review.app;

import com.mach.valtech.review.ReviewsAndRatingsApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ReviewsAndRatingsApplication.class);
	}

}
