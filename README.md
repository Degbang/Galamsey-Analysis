# Galamsey Analysis API

This is a **Spring Boot** application designed to analyze illegal small-scale mining activities (**Galamsey**) in Ghana. It reads data from a CSV file, processes and validates the records, stores the valid entries in a **PostgreSQL** database, and exposes a REST API for querying the data.

## **Features**
Reads Galamsey data from a CSV file
- Validates records (region, city, and site count)
- Removes invalid records from the dataset before saving
- Stores cleaned data into a PostgreSQL database
- Exposes REST API endpoints to interact with the data
- Supports JUnit tests for validation

---

## **Setup and Installation**

### **Prerequisites**
Ensure you have the following installed on your system:
- Java 23
- Gradle
- Intellij
- PostgreSQL (Database management system)
- pgAdmin (Optional, for GUI-based database management)

---

### **Clone the Repository**
- To get started, clone the project to your local machine:
    - git clone https://github.com/Degbang/Galamsey-Analysis.git
  - cd galamsey-analysis
---

### **Configure Environment Variables**
Update src/main/resources/application.properties:

server.port=8080
*   spring.datasource.url=jdbc:postgresql://localhost:5432/galamsey_db
*   spring.datasource.username=postgres
*   spring.datasource.password=your_password
*   spring.jpa.hibernate.ddl-auto=update
*   spring.jpa.show-sql=true
*   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

---

### **Set Up the Database**
- follow this link to install postgres https://www.postgresql.org/docs/current/tutorial-install.html

For macOS (Using Homebrew)
 -  Start PostgreSQL using Homebrew: brew services start postgresql
 - Log into PostgreSQL: psql -U postgres
 - Create the Galamsey database: CREATE DATABASE galamsey_db;

For Windows
- Start PostgreSQL (if not already running): net start postgresql/ Alternatively, start it via pgAdmin.
- Log into PostgreSQL: psql -U postgres
- Create the Galamsey database: CREATE DATABASE galamsey_db;
- https://res.cloudinary.com/dnsu7es0c/image/upload/v1738327670/Screenshot_2025-01-31_at_12.39.08_PM_nzzybp.png


### **4.API Endpoints (Swagger Documentation)**
- Once the application is running, Swagger UI will be available at: http://localhost:8080/swagger-ui/index.html. 
- This allows you to explore and test the API.
- https://res.cloudinary.com/dnsu7es0c/image/upload/v1738327669/Screenshot_2025-01-31_at_12.39.00_PM_gznyqy.png


### **Export Analyzed Data to CSV**
- Once the application has processed the Galamsey data, the analyzed results will be stored in the data directory inside the project.

### **Running the application locally**
There are several ways to run a Spring Boot application on your local machine. 
One way is to execute the main method in the com.open_foundation.GalamseyAnalysis.Application; class from your IDE.








