# Rent Management API 🚀

## 📖 About The Project

This is a complete and robust RESTful API developed in Java with Spring Boot to manage the entire lifecycle of property rentals. The platform is designed to be the backend for a system that connects property **Owners** and **Tenants**, orchestrating everything from property listing to the management of contracts and monthly payments.

The project is built following software development best practices, including a well-defined service architecture, robust unit tests, and REST API design principles like **HATEOAS**.

-----

## ✨ Core Features

  * **User Management**: Full CRUD for Owners and Tenants, with a role and permission system.
  * **Property Management**: Owners can list, update, and remove their properties, which have a status of `AVAILABLE` or `RENTED`.
  * **Contract Lifecycle System**:
      * Create contracts linking an Owner, a Tenant, and a Property.
      * Manage the contract lifecycle with statuses: `ACTIVE`, `EXPIRED`, `TERMINATED`.
  * **Automated Payment Engine**:
      * Automatic generation of all monthly payment installments when a contract is created.
      * Manage the payment lifecycle with statuses: `PENDING`, `PAID`, `OVERDUE`, `CANCELED`.
  * **Security**: Authorization logic to ensure that only authorized users can perform sensitive actions (e.g., only the contract's owner can confirm a payment).
  * **Hypermedia-driven API (HATEOAS)**: API responses include links to the next possible actions, making the API more discoverable and self-descriptive.
  * **Dynamic API Documentation (Swagger)**: The API is fully documented using the OpenAPI 3 standard, generating an interactive interface with Swagger UI.

-----

## 🔧 Tech Stack

| Technology          | Purpose                                               |
| :------------------ | :---------------------------------------------------- |
| **Java 17+** | Core application language.                            |
| **Spring Boot 3.x** | Core framework for building the API.                  |
| **Spring Web** | For building RESTful endpoints.                       |
| **Spring Data JPA** | For simplified data persistence.                      |
| **Spring HATEOAS** | For implementing hypermedia in API responses.         |
| **Spring Security** | For authentication and authorization (planned).         |
| **PostgreSQL** | Relational database.                                  |
| **Hibernate** | JPA implementation for Object-Relational Mapping.     |
| **Flyway** | For database schema versioning and migration.         |
| **MapStruct** | For high-performance mapping between DTOs and Entities. |
| **Springdoc OpenAPI**| For auto-generating Swagger UI documentation.       |
| **JUnit 5 & Mockito**| For unit and integration testing.                     |
| **Lombok** | To reduce boilerplate code in models and DTOs.        |

### Tools

  * **Maven** or **Gradle**: For dependency management and project build.
  * **Docker** & **Docker Compose**: To easily run the development environment (e.g., the database).
  * **Insomnia / Postman**: For testing the API endpoints.

-----

## 📄 API Documentation (Swagger)

The API is self-documented using the OpenAPI standard. After starting the application, you can access the interactive Swagger UI to view all endpoints and models, and to test the API calls directly in your browser.

Access it here: [**http://localhost:8080/swagger-ui.html**](https://www.google.com/search?q=http://localhost:8080/swagger-ui.html)

-----

## 🔑 API Endpoints

Here is a list of the main available endpoints, grouped by resource.

### Owners (`/owners`)

| Verb     | Endpoint                       | Description                     |
| :------- | :----------------------------- | :------------------------------ |
| `GET`    | `/owners`                      | Lists all owners.               |
| `GET`    | `/owners/{id}`                 | Finds an owner by ID.           |
| `POST`   | `/owners/create`               | Creates a new owner.            |
| `PUT`    | `/owners/update/{id}`          | Updates an existing owner.      |
| `DELETE` | `/owners/delete/{id}`          | Deletes an owner.               |
| `GET`    | `/owners/search/findByName`    | Searches for owners by name.    |

### Tenants (`/tenants`)

| Verb     | Endpoint                       | Description                     |
| :------- | :----------------------------- | :------------------------------ |
| `GET`    | `/tenants`                     | Lists all tenants.              |
| `GET`    | `/tenants/{id}`                | Finds a tenant by ID.           |
| `POST`   | `/tenants/create`              | Creates a new tenant.           |
| `PUT`    | `/tenants/update/{id}`         | Updates an existing tenant.     |
| `DELETE` | `/tenants/delete/{id}`         | Deletes a tenant.               |
| `GET`    | `/tenants/search/findByName`   | Searches for tenants by name.   |

### Properties (`/properties`)

| Verb     | Endpoint                     | Description                    |
| :------- | :--------------------------- | :----------------------------- |
| `GET`    | `/properties`                | Lists all properties.          |
| `GET`    | `/properties/{id}`           | Finds a property by ID.        |
| `POST`   | `/properties/create`         | Creates a new property.        |
| `PUT`    | `/properties/update/{id}`    | Updates an existing property.  |
| `DELETE` | `/properties/delete/{id}`    | Deletes a property.            |
| `GET`    | `/properties/search/query`   | Searches for properties by title.|

### Contracts (`/contracts`)

| Verb     | Endpoint                     | Description                                    |
| :------- | :--------------------------- | :--------------------------------------------- |
| `GET`    | `/contracts`                 | Lists all contracts.                           |
| `GET`    | `/contracts/{id}`            | Finds a contract by ID.                        |
| `POST`   | `/contracts/create`          | Creates a new contract and generates payments. |
| `PUT`    | `/contracts/update/{id}`     | Updates an existing contract.                  |
| `PUT`    | `/contracts/terminate/{id}`  | Terminates a contract early.                   |
| `GET`    | `/contracts/tenants/{id}`    | Finds contracts by tenant ID.                  |
| `GET`    | `/contracts/owners/{id}`     | Finds contracts by owner ID.                   |

### Payments (`/payments`)

| Verb      | Endpoint                                    | Description                              |
| :-------- | :------------------------------------------ | :--------------------------------------- |
| `GET`     | `/payments/{id}`                            | Finds a payment by ID.                   |
| `GET`     | `/payments/contracts/{id}`                  | Lists all payments for a specific contract.|
| `PATCH`   | `/payments/{paymentId}/owner/{ownerId}/confirm`| Confirms that a payment has been received.|

-----

## 🚀 Getting Started

Follow the steps below to run the application locally.

### Prerequisites

  * **JDK 17** or higher
  * **Maven** or **Gradle**
  * A running **PostgreSQL** instance
  * An API client like **Insomnia** or **Postman**

### Steps

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/your-repository.git
    cd your-repository
    ```

2.  **Configure the Database:**
    Open the file `src/main/resources/application.properties` and set up your PostgreSQL database credentials.

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
    spring.datasource.username=your_username
    spring.datasource.password=your_password

    # JPA/Hibernate configuration
    spring.jpa.hibernate.ddl-auto=validate # 'validate' is safe and relies on Flyway to manage the schema

    # Jackson configuration for field names (snake_case in JSON -> camelCase in Java)
    spring.jackson.property-naming-strategy=SNAKE_CASE
    ```

3.  **Run the Application:**
    Use Maven or Gradle to run the application:

    ```bash
    # Using Maven
    ./mvnw spring-boot:run

    # Using Gradle
    ./gradlew bootRun
    ```

    Alternatively, run the main `ApiRentApplication.java` class from your IDE.

4.  The API will be available at `http://localhost:8080`.

-----

## 🧪 Running Tests

To run the complete unit test suite, use the following command:

```bash
# Using Maven
./mvnw test

# Using Gradle
./gradlew test
```

-----
