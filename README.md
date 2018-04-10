# Waterloo Carpool

## About
**Waterloo Carpool** is a carpool booking platform that lets you to plan your commutes around Waterloo area. Drivers and passengers can take advantage of our platform to offer or to find a daily commute as well as a long distance travel.

The application was based on **Spring Framework** and server-side Java template engine **Thymeleaf**. User registration and login page were secured by CSRF protection using **Spring Security**. User can manage booking reference through booking history page or notification service. The booking service was supported by **Google API**, **iCalendar integration** and **Spring Boot Mail**. The booking reference listing page implemented **PagingAndSortingRepository** and **Search Functionality**. 

### Technologies
 - Spring Framework: Spring Boot, Spring MVC, Spring Data, Spring Security, and Spring Test
 - Google API: Google Map, Google Direction, Google Distance Matrix and Google Autocomplete
 - Hibernate
 - MySQL Database
 - Thymeleaf Template Engine
 - Twitter Bootstrap
 - jQuery, HTML, CSS, JavaScript
 - JS Plugins: typeahead, datetimepicker, datatable, slider and etc.

## Setup

### System requirements

 - Java 7 or later
 - Maven 3.2+
 - MySQL 5.6 or better
 - You can also import the code straight into your IDE:
   - Spring Tool Suite (STS)
   - IntelliJ IDEA
   
### Create database

    $ mysql> create database WaterlooCarpool;
    $ mysql> create user 'root'@'localhost' identified by 'passw0rd!';
    $ mysql> grant all on WaterlooCarpool.* to 'root'@'localhost';

### Dependencies
```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>

  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
  </dependency>
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.0</version>
  </dependency>
  <dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5</version>
  </dependency>
  <dependency>
    <groupId>com.google.maps</groupId>
    <artifactId>google-maps-services</artifactId>
    <version>0.2.6</version>
  </dependency>
  <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-nop</artifactId>
    <version>1.7.25</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
    <version>5.0.4.RELEASE</version>
  </dependency>
  <dependency>
    <groupId>org.mnode.ical4j</groupId>
    <artifactId>ical4j</artifactId>
    <version>1.0.7</version>
  </dependency>
</dependencies>
```

### Directory Structure
```shell
└── src
    └── main
        └── java
            └── config
            └── controller
            └── domain
            └── error
            └── repository
            └── service
            └── util
            └── CarpoolApplication
        └── resources
            └── static
            └── templates
            └── application.properties
     └── test
├── pom.xml
└── .gitignore

```

## ScreenShots

### Login and Signup

![image](https://user-images.githubusercontent.com/24725550/38531330-510be0aa-3c3d-11e8-8833-8102c9eb94ff.png)

![image](https://user-images.githubusercontent.com/24725550/38531354-68db7524-3c3d-11e8-89bb-94f3e7334199.png)

### Home Page

![image](https://user-images.githubusercontent.com/24725550/38531056-1a55f18c-3c3c-11e8-8a87-36ded20b6be2.png)

### Booking Reference List

![image](https://user-images.githubusercontent.com/24725550/38531431-c38af60c-3c3d-11e8-9782-93156852f89f.png)

### Itineary Detail

![image](https://user-images.githubusercontent.com/24725550/38531232-dc0646c4-3c3c-11e8-8761-19896c44349b.png)

### Booking Submit Page

![image](https://user-images.githubusercontent.com/24725550/38531406-aad740ac-3c3d-11e8-86c0-25a9c4bb865a.png)

### Booking History

![image](https://user-images.githubusercontent.com/24725550/38531367-7b1198ae-3c3d-11e8-8f8b-70c5e2d81ce8.png)

### In-Progress Booking

![image](https://user-images.githubusercontent.com/24725550/38531108-5cd9448c-3c3c-11e8-8b20-e53b5b3c3381.png)

### Booking Complete Page

![image](https://user-images.githubusercontent.com/24725550/38531361-6fd62c2a-3c3d-11e8-9385-b11da4fa3cf5.png)

## Building the Project
    # Run the application
    $ mvn spring-boot:run
    
    # You can also run as a packaged application
    $ mvn clean install
    $ java -jar target/demo-0.0.1-SNAPSHOT.jar

    # You might also want to use the MAVEN_OPTS operating system environment variable:
    $ export MAVEN_OPTS=-Xmx1024m
