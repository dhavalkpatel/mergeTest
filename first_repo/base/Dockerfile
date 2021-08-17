FROM openjdk:11
MAINTAINER Dhaval Patel <dhaval.p@valtech.com>

RUN set -eux; \
        apt-get update; \
        apt-get install -y --no-install-recommends \
# utilities for keeping Debian and OpenJDK CA certificates in sync
                ca-certificates p11-kit \
                maven graphviz \
        ; \
        rm -rf /var/lib/apt/lists/*
