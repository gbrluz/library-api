<br />
<div align="center">
  <a href="https://github.com/gbrluz/library-api">
    <img src="https://cdn-icons-png.flaticon.com/512/2232/2232688.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Library API</h3>

  <p align="center">
    An Library API for schools!
    <br />
    <a href="https://github.com/gbrluz/library-api"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/gbrluz/library-api">View Demo</a>
    ·
    <a href="https://github.com/gbrluz/library-api/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    ·
    <a href="https://github.com/gbrluz/library-api/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>

## About the project

This project is a library management system developed for a school. The system allows for managing books, authors, renters, and book rentals, 
following a set of specific business rules. The application is built using Java with Spring Boot and uses PostgreSQL for data persistence. 
Communication with this backend is done through a REST API with JSON format messages.



<p align="right">(<a href="#readme-top">back to top</a>)</p>


### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
* ![Spring](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
* ![Spring](https://img.shields.io/badge/spring%20data%20jpa-green?style=for-the-badge&logo=spring)
* ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
* ![Flyway](https://img.shields.io/badge/FLYWAY-red?style=for-the-badge&logo=flyway)
* ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
* ![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black)
* ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
* ![IntelliJ IDEA](https://img.shields.io/badge/INTELLIJ%20IDEA-black?style=for-the-badge&logo=intellijidea)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Process

Java, Spring Boot and Spring Data JPA were chosen for their robust ecosystem and ability to simplify the development of RESTful web 
services with minimal configuration and powerful database interaction. PostgreSQL was selected for its reliability and advanced features. Flyway 
manages database migrations, ensuring version control. Docker was used to containerize the application, ensuring consistent environments across development, testing, and production. 
IntelliJ IDEA was chosen as the IDE for its powerful features, integrated tools, and excellent support for Java and Spring Boot development.

<b>1.Project Initialization:</b>

Set up a new Spring Boot project using Spring Initializr.<br>
Include necessary dependencies such as Spring Web, Spring Data JPA, PostgreSQL Driver, and Flyway for database migrations.<br>

<b>2.Database Configuration:</b>

Configure PostgreSQL as the database in application.properties file.<br>
Set up Flyway for database version control and migrations.<br>

<b>3.Entity Design:</b>

Define the main entities: Author, Book, Renter, and Rent.<br>
Set up relationships between these entities using JPA annotations.<br>

<b>4.Repository Layer:</b>

Create repository interfaces for each entity extending JpaRepository to handle CRUD operations.<br>

<b>5.Service Layer:</b>

Implement service classes to contain the business logic for each entity.<br>
Include methods for creating, updating, deleting, and retrieving records.<br>

<b>6.Controller Layer:</b>

Develop REST controllers for each entity to handle HTTP requests.<br>
Define endpoints for CRUD operations and advanced queries.<br>
Use ResponseEntity for proper HTTP response handling.<br>

<b>7.DTOs and Mapping:</b>

Create Data Transfer Objects (DTOs) for each entity to structure API responses and requests.<br>
Implement mapping methods between entities and DTOs.<br>

<b>8.Validation and Error Handling:</b>

Add input validation in DTOs and controller methods.<br>
Implement global exception handling for consistent error responses.<br>

<b>9.Dockerization:</b>

Write a Dockerfile to containerize the application.<br>
Create a docker-compose.yml file to run the application along with the PostgreSQL database in containers.<br>

<b>10.Testing and Deployment:</b>

Write unit and integration tests to ensure the application works as expected.<br>
Use Docker to deploy the application in different environments.<br>

<b>11.Documentation:</b>

Document the API endpoints, including request and response formats.<br>
Write a README file explaining the project, setup instructions, and technologies used.<br>


<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Installation

* Clone the repository.
* Configure the PostgreSQL database and update the connection properties in the application.properties file.
* Run the database migrations with Flyway.
* Run the application using your preferred IDE or via command line with mvn spring-boot:run.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Challenges

While building this API, I faced some challenges during it. The first moment that a had some doubts, was creating the tables of the entities. I was not used to work with Join Tables, because I always made the queries getting the data of different tables instead of creating it. Working with MockMvc was very challeging at first, because I had a very basic knowledge of this framework. Another challenge that I found, was to use ObjectMapper in some Controller tests that needed to. I was not used to work with it and a had to learn to be able to test the controller effectively.

<p align="right">(<a href="#readme-top">back to top</a>)</p>
