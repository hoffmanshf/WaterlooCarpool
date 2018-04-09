# Waterloo Carpool

## About
**Waterloo Carpool** is a carpool booking platform that lets you to plan your commutes around Waterloo area. Drivers and passengers can take advantage of our platform to offer or to find a daily commute as well as a long distance travel.

The application was based on **Spring Framework** and frontend template engine **Thymeleaf**. User registration and login page were secured by CSRF protection using **Spring Security**. User can manage booking reference through booking history page or notification service. The booking service was supported by **Google API**, **iCalendar integration** and **Spring Boot Mail**. The booking reference listing page implemented **PagingAndSortingRepository** and **search functionality**. 

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
 - Maven 3.5.0
 - Spring Boot 1.5.10
 - MySQL 5.7.21

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
