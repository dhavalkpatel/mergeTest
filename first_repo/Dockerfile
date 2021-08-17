FROM nginx

ARG DEBIAN_FRONTEND=noninteractive

ENV DEBIAN_FRONTEND noninteractive

RUN rm /etc/nginx/conf.d/*
RUN apt -qq update
RUN apt -qq -y install apache2-utils curl less vim
COPY config/default.conf /etc/nginx/conf.d/
