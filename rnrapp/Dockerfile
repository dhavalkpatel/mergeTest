FROM machregistrydk.azurecr.io/dhavalkpatelmach:1
MAINTAINER Dhaval Patel <dhaval.p@valtech.com>

RUN mkdir -p /rnr
COPY ./codebase/ /rnr/
RUN ls -ltr
RUN cd /rnr/ && mvn install
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/rnr/target/rnrapp.jar"]
