# Spring PetClinic Sample Application [![Build Status](https://github.com/spring-projects/spring-petclinic/actions/workflows/maven-build.yml/badge.svg)](https://github.com/spring-projects/spring-petclinic/actions/workflows/maven-build.yml)[![Build Status](https://github.com/spring-projects/spring-petclinic/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/spring-projects/spring-petclinic/actions/workflows/gradle-build.yml)

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/spring-projects/spring-petclinic) [![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://github.com/codespaces/new?hide_repo_select=true&ref=main&repo=7517918)

## About the Project

Spring PetClinic is a comprehensive sample application that demonstrates best practices for building modern web applications with the Spring Framework ecosystem. Originally created to showcase Spring's capabilities, PetClinic serves as a veterinary clinic management system where you can manage veterinarians, pet owners, pets, and their medical visits.

This application serves as an excellent learning resource and reference implementation for:
- **Spring Framework developers** looking to understand real-world application patterns
- **Architecture evaluation** when comparing Spring with other frameworks
- **Learning modern Java web development** with industry-standard tools and practices
- **Demonstrating enterprise application features** like data persistence, validation, and web MVC

## Key Features

- **Veterinarian Management**: Add, view, and manage veterinary staff with their specialties
- **Pet Owner Management**: Comprehensive customer database with contact information
- **Pet Registration**: Track pets with detailed information including type, birth date, and medical history
- **Visit Tracking**: Schedule and record veterinary visits with detailed notes
- **Search Functionality**: Find owners, pets, and veterinarians quickly
- **Responsive Web Interface**: Modern, mobile-friendly user interface
- **Multiple Database Support**: Works with H2 (in-memory), MySQL, and PostgreSQL
- **RESTful Architecture**: Clean API design following REST principles
- **Comprehensive Testing**: Unit, integration, and web layer tests included

## Technology Stack

This application demonstrates the integration of several key technologies:

### Backend
- **Spring Boot 3.5** - Application framework and auto-configuration
- **Spring MVC** - Web framework for REST and web controllers
- **Spring Data JPA** - Data access layer with Hibernate
- **Spring Validation** - Bean validation with custom validators
- **Spring Cache** - Caching abstraction for improved performance

### Frontend
- **Thymeleaf** - Server-side template engine
- **Bootstrap 5** - Responsive CSS framework
- **Webjars** - Web library dependency management

### Database
- **H2** - In-memory database (default for development)
- **MySQL 9.2** - Production-ready relational database
- **PostgreSQL 17.5** - Advanced open-source database

### Build & Testing
- **Maven** - Primary build tool and dependency management
- **Gradle** - Alternative build system support
- **JUnit 5** - Testing framework
- **Testcontainers** - Integration testing with real databases
- **Spring Boot Test** - Comprehensive testing support

### Development Tools
- **Spring Boot DevTools** - Hot reloading and development utilities
- **Docker Compose** - Container orchestration for databases
- **Checkstyle** - Code quality and formatting
- **Spring Java Format** - Consistent code formatting

## Understanding the Spring Petclinic application with a few diagrams

[See the presentation here](https://speakerdeck.com/michaelisvy/spring-petclinic-sample-application)

## Quick Start

Get the application running in under 5 minutes:

```bash
# Clone the repository
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic

# Run with Maven (recommended)
./mvnw spring-boot:run

# Or run with Gradle
./gradlew bootRun
```

Once started, navigate to [http://localhost:8080](http://localhost:8080) to explore the application.

## Run Petclinic locally

Spring Petclinic is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/) or [Gradle](https://spring.io/guides/gs/gradle/). You can build a jar file and run it from the command line (it should work just as well with Java 17 or newer):

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
./mvnw package
java -jar target/*.jar
```

(On Windows, or if your shell doesn't expand the glob, you might need to specify the JAR file name explicitly on the command line at the end there.)

You can then access the Petclinic at <http://localhost:8080/>.

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this, it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```bash
./mvnw spring-boot:run
```

> NOTE: If you prefer to use Gradle, you can build the app using `./gradlew build` and look for the jar file in `build/libs`.

## Building a Container

There is no `Dockerfile` in this project. You can build a container image (if you have a docker daemon) using the Spring Boot build plugin:

```bash
./mvnw spring-boot:build-image
```

## In case you find a bug/suggested improvement for Spring Petclinic

Our issue tracker is available [here](https://github.com/spring-projects/spring-petclinic/issues).

## Database configuration

In its default configuration, Petclinic uses an in-memory database (H2) which
gets populated at startup with data. The h2 console is exposed at `http://localhost:8080/h2-console`,
and it is possible to inspect the content of the database using the `jdbc:h2:mem:<uuid>` URL. The UUID is printed at startup to the console.

A similar setup is provided for MySQL and PostgreSQL if a persistent database configuration is needed. Note that whenever the database type changes, the app needs to run with a different profile: `spring.profiles.active=mysql` for MySQL or `spring.profiles.active=postgres` for PostgreSQL. See the [Spring Boot documentation](https://docs.spring.io/spring-boot/how-to/properties-and-configuration.html#howto.properties-and-configuration.set-active-spring-profiles) for more detail on how to set the active profile.

You can start MySQL or PostgreSQL locally with whatever installer works for your OS or use docker:

```bash
docker run -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:9.2
```

or

```bash
docker run -e POSTGRES_USER=petclinic -e POSTGRES_PASSWORD=petclinic -e POSTGRES_DB=petclinic -p 5432:5432 postgres:17.5
```

Further documentation is provided for [MySQL](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/mysql/petclinic_db_setup_mysql.txt)
and [PostgreSQL](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/postgres/petclinic_db_setup_postgres.txt).

Instead of vanilla `docker` you can also use the provided `docker-compose.yml` file to start the database containers. Each one has a service named after the Spring profile:

```bash
docker compose up mysql
```

or

```bash
docker compose up postgres
```

## Test Applications

At development time we recommend you use the test applications set up as `main()` methods in `PetClinicIntegrationTests` (using the default H2 database and also adding Spring Boot Devtools), `MySqlTestApplication` and `PostgresIntegrationTests`. These are set up so that you can run the apps in your IDE to get fast feedback and also run the same classes as integration tests against the respective database. The MySql integration tests use Testcontainers to start the database in a Docker container, and the Postgres tests use Docker Compose to do the same thing.

## Compiling the CSS

There is a `petclinic.css` in `src/main/resources/static/resources/css`. It was generated from the `petclinic.scss` source, combined with the [Bootstrap](https://getbootstrap.com/) library. If you make changes to the `scss`, or upgrade Bootstrap, you will need to re-compile the CSS resources using the Maven profile "css", i.e. `./mvnw package -P css`. There is no build profile for Gradle to compile the CSS.

## Working with Petclinic in your IDE

### Prerequisites

The following items should be installed in your system:

- Java 17 or newer (full JDK, not a JRE)
- [Git command line tool](https://help.github.com/articles/set-up-git)
- Your preferred IDE
  - Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, follow the install process [here](https://www.eclipse.org/m2e/)
  - [Spring Tools Suite](https://spring.io/tools) (STS)
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
  - [VS Code](https://code.visualstudio.com)

### Steps

1. On the command line run:

    ```bash
    git clone https://github.com/spring-projects/spring-petclinic.git
    ```

1. Inside Eclipse or STS:

    Open the project via `File -> Import -> Maven -> Existing Maven project`, then select the root directory of the cloned repo.

    Then either build on the command line `./mvnw generate-resources` or use the Eclipse launcher (right-click on project and `Run As -> Maven install`) to generate the CSS. Run the application's main method by right-clicking on it and choosing `Run As -> Java Application`.

1. Inside IntelliJ IDEA:

    In the main menu, choose `File -> Open` and select the Petclinic [pom.xml](pom.xml). Click on the `Open` button.

    - CSS files are generated from the Maven build. You can build them on the command line `./mvnw generate-resources` or right-click on the `spring-petclinic` project then `Maven -> Generates sources and Update Folders`.

    - A run configuration named `PetClinicApplication` should have been created for you if you're using a recent Ultimate version. Otherwise, run the application by right-clicking on the `PetClinicApplication` main class and choosing `Run 'PetClinicApplication'`.

1. Navigate to the Petclinic

    Visit [http://localhost:8080](http://localhost:8080) in your browser.

## Project Structure

The application follows standard Spring Boot conventions and clean architecture principles:

```
src/main/java/org/springframework/samples/petclinic/
├── PetClinicApplication.java          # Main Spring Boot application class
├── model/                             # Domain entities and data models
│   ├── BaseEntity.java               # Base class for all entities
│   ├── Person.java                   # Base class for persons (Owner, Vet)
│   ├── Pet.java                      # Pet entity
│   ├── PetType.java                  # Pet type enumeration
│   └── Visit.java                    # Veterinary visit entity
├── owner/                            # Owner and pet management
│   ├── Owner.java                    # Owner entity
│   ├── OwnerController.java          # Web controller for owners
│   ├── OwnerRepository.java          # Data access for owners
│   ├── Pet.java                      # Pet entity (owner relationship)
│   ├── PetController.java            # Web controller for pets
│   ├── PetRepository.java            # Data access for pets
│   ├── PetValidator.java             # Pet data validation
│   ├── Visit.java                    # Visit entity
│   ├── VisitController.java          # Web controller for visits
│   └── VisitRepository.java          # Data access for visits
├── system/                           # System configuration and utilities
│   ├── CacheConfiguration.java       # Caching setup
│   └── WelcomeController.java        # Home page controller
└── vet/                              # Veterinarian management
    ├── Vet.java                      # Veterinarian entity
    ├── VetController.java            # Web controller for vets
    ├── VetRepository.java            # Data access for vets
    └── Specialty.java                # Veterinary specialties

src/main/resources/
├── application.properties            # Main configuration
├── application-mysql.properties      # MySQL-specific configuration
├── application-postgres.properties   # PostgreSQL-specific configuration
├── db/                              # Database scripts and migrations
├── static/                          # Static web assets (CSS, JS, images)
└── templates/                       # Thymeleaf templates
```

### Key Architectural Patterns

- **MVC Pattern**: Clear separation between Model (entities), View (Thymeleaf templates), and Controller (web controllers)
- **Repository Pattern**: Data access abstraction using Spring Data JPA repositories
- **Dependency Injection**: Comprehensive use of Spring's IoC container
- **Configuration Management**: Profile-based configuration for different environments
- **Validation**: Bean validation with custom validators
- **Caching**: Strategic caching of frequently accessed data

## Looking for something in particular?

### Essential Spring Boot Configuration

|Spring Boot Configuration | Class or Java property files  |
|--------------------------|---|
|The Main Class | [PetClinicApplication](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java) |
|Properties Files | [application.properties](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources) |
|Caching | [CacheConfiguration](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/system/CacheConfiguration.java) |

### Core Domain Classes

|Domain Area | Key Classes |
|------------|-------------|
|Owner Management | [Owner](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/Owner.java), [OwnerController](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java) |
|Pet Management | [Pet](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/Pet.java), [PetController](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/PetController.java) |
|Veterinary Care | [Vet](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/vet/Vet.java), [Visit](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/Visit.java) |
|Data Access | [OwnerRepository](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/OwnerRepository.java), [VetRepository](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/vet/VetRepository.java) |

### API Endpoints

The application provides both web pages and RESTful API endpoints:

#### Web Interface
- `/` - Welcome page
- `/owners` - Owner management interface
- `/owners/{id}` - Owner details and pet management
- `/owners/{id}/pets/new` - Add new pet form
- `/owners/{id}/pets/{petId}/visits/new` - Schedule new visit
- `/vets` - Veterinarian listing

#### REST API
- `GET /api/owners` - List all owners
- `GET /api/owners/{id}` - Get owner by ID
- `GET /api/pets` - List all pets
- `GET /api/vets` - List all veterinarians
- `GET /api/petTypes` - List available pet types

*Note: The REST API is available but the primary interface is the web application. For API testing, consider using tools like Postman or curl.*

## Performance and Scalability

The PetClinic application demonstrates several performance optimization techniques:

### Caching Strategy
- **Spring Cache Abstraction**: Implements caching for frequently accessed data
- **Vet Information Caching**: Veterinarian data is cached to reduce database queries
- **Configurable Cache Providers**: Support for different cache implementations

### Database Optimization
- **Connection Pooling**: Efficient database connection management
- **JPA Query Optimization**: Optimized queries with proper fetch strategies  
- **Multiple Database Support**: Scalable from in-memory H2 to enterprise PostgreSQL/MySQL

### Monitoring and Observability
- **Spring Boot Actuator**: Built-in health checks and metrics endpoints
- **Performance Metrics**: Application performance monitoring capabilities
- **Database Health Checks**: Monitor database connectivity and performance

### Deployment Options
- **Container Ready**: Built-in support for containerization with Spring Boot
- **Native Image Support**: GraalVM native image compilation for faster startup
- **Cloud Native**: Suitable for deployment on modern cloud platforms

## Interesting Spring Petclinic branches and forks

The Spring Petclinic "main" branch in the [spring-projects](https://github.com/spring-projects/spring-petclinic)
GitHub org is the "canonical" implementation based on Spring Boot and Thymeleaf. There are
[quite a few forks](https://spring-petclinic.github.io/docs/forks.html) in the GitHub org
[spring-petclinic](https://github.com/spring-petclinic). If you are interested in using a different technology stack to implement the Pet Clinic, please join the community there.

## Interaction with other open-source projects

One of the best parts about working on the Spring Petclinic application is that we have the opportunity to work in direct contact with many Open Source projects. We found bugs/suggested improvements on various topics such as Spring, Spring Data, Bean Validation and even Eclipse! In many cases, they've been fixed/implemented in just a few days.
Here is a list of them:

| Name | Issue |
|------|-------|
| Spring JDBC: simplify usage of NamedParameterJdbcTemplate | [SPR-10256](https://github.com/spring-projects/spring-framework/issues/14889) and [SPR-10257](https://github.com/spring-projects/spring-framework/issues/14890) |
| Bean Validation / Hibernate Validator: simplify Maven dependencies and backward compatibility |[HV-790](https://hibernate.atlassian.net/browse/HV-790) and [HV-792](https://hibernate.atlassian.net/browse/HV-792) |
| Spring Data: provide more flexibility when working with JPQL queries | [DATAJPA-292](https://github.com/spring-projects/spring-data-jpa/issues/704) |

## Troubleshooting

### Common Issues and Solutions

#### Build Issues
**Problem**: Maven build fails with formatting violations
```bash
# Solution: Apply Spring Java formatting
./mvnw spring-javaformat:apply
```

**Problem**: Out of memory during build
```bash
# Solution: Increase Maven memory
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"
```

#### Runtime Issues
**Problem**: Application fails to start with database connection errors
- **H2 Database**: Ensure no other instance is running on port 8080
- **MySQL**: Verify Docker container is running: `docker-compose up mysql`
- **PostgreSQL**: Verify Docker container is running: `docker-compose up postgres`

**Problem**: Port 8080 already in use
```bash
# Solution: Use a different port
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

#### Development Issues
**Problem**: Hot reload not working in IDE
- Ensure Spring Boot DevTools is included in dependencies
- Verify automatic build is enabled in your IDE
- Check that the IDE is configured for annotation processing

### Getting Help
- Check the [GitHub Issues](https://github.com/spring-projects/spring-petclinic/issues) for known problems
- Review the [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- Visit [Spring Community Forums](https://spring.io/community) for community support

## Contributing

The [issue tracker](https://github.com/spring-projects/spring-petclinic/issues) is the preferred channel for bug reports, feature requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](.editorconfig) for easy use in common text editors. Read more and download plugins at <https://editorconfig.org>. All commits must include a __Signed-off-by__ trailer at the end of each commit message to indicate that the contributor agrees to the Developer Certificate of Origin.
For additional details, please refer to the blog post [Hello DCO, Goodbye CLA: Simplifying Contributions to Spring](https://spring.io/blog/2025/01/06/hello-dco-goodbye-cla-simplifying-contributions-to-spring).

## License

The Spring PetClinic sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
