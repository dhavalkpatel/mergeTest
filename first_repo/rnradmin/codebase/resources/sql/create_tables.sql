create schema review_rating_app;

create table review_rating_app.reviews(
   user_id VARCHAR(100) NOT NULL,
   product_id VARCHAR(100) NOT NULL,
   application_id VARCHAR(100) NOT NULL,
   review_title VARCHAR(500),
   product_review VARCHAR(4000),
   rating INT,
   status VARCHAR(20),
   PRIMARY KEY ( user_id, product_id )
);

create table review_rating_app.user(
   user_id VARCHAR(100) NOT NULL,
   name VARCHAR(100) NOT NULL,
   role VARCHAR(100) NOT NULL,
   email VARCHAR(40) NOT NULL,
   password VARCHAR(40) NOT NULL,
   PRIMARY KEY ( user_id )
);


create table review_rating_app.application(
   application_id VARCHAR(100) NOT NULL,
   password VARCHAR(100) NOT NULL,
   name VARCHAR(100) NOT NULL,
   PRIMARY KEY ( application_id )
);

