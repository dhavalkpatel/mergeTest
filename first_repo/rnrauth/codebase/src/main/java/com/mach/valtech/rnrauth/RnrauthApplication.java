package com.mach.valtech.rnrauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.logging.Logger;

@SpringBootApplication
@Configuration
@EnableSwagger2
public class RnrauthApplication {
	Logger LOG= Logger.getLogger(RnrauthApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(RnrauthApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				LOG.info("Reached CORS Configurer..!!");
				registry
						.addMapping("/**")
						.allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
						.allowedOrigins("*");
			}
		};
	}
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.mach.valtech.rnrauth"))
				.paths(PathSelectors.any())
				.build();
	}

}
