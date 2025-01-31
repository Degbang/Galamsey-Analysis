# Galamsey Analysis API

This is a **Spring Boot** application designed to analyze illegal small-scale mining activities (**Galamsey**) in Ghana. It reads data from a CSV file, processes and validates the records, stores the valid entries in a **PostgreSQL** database, and exposes a REST API for querying the data.

## **Features**
- Reads Galamsey data from a CSV file
- Validates records (region, city, and site count)
- Stores cleaned data into a PostgreSQL database
- Provides a REST API to retrieve and save records
- Supports JUnit tests for validation

---

## **Setup and Installation**

### **1. Prerequisites**
Ensure you have the following installed on your system:
- Java 23
- Gradle
- PostgreSQL (installed via Homebrew for macOS)
- pgAdmin (optional for database management)

---

### **2. Configure Environment Variables**
Update src/main/resources/application.properties:
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/galamsey_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

---

### **3. Set Up the Database**
For macOS (Using Homebrew)

 -  Start PostgreSQL using Homebrew: brew services start postgresql
 - Log into PostgreSQL: psql -U postgres
 - Create the Galamsey database: CREATE DATABASE galamsey_db;

For Windows
- Start PostgreSQL (if not already running): net start postgresql/ Alternatively, start it via pgAdmin.
- Log into PostgreSQL: psql -U postgres
- Create the Galamsey database: CREATE DATABASE galamsey_db;









