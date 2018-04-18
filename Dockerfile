FROM openjdk:8
MAINTAINER hoffmanshf@outlook.com
VOLUME /tmp
EXPOSE 8080
ADD target/waterloo-carpool-demo.jar waterloo-carpool-demo.jar
RUN sh -c 'touch /waterloo-carpool-demo.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/waterloo-carpool-demo.jar"]
