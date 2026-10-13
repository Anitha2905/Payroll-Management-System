# Payroll Management System

A beginner-friendly Java console application for payroll processing using JDBC and MySQL. The project demonstrates object-oriented programming, database connectivity, DAO design, payroll calculations, and reporting.

## Tech Stack
- Java 17+
- Maven
- MySQL
- JDBC
- Console-based UI using Scanner

## Project Overview
This project simulates a small payroll system for a company. It stores employees, departments, salary data, attendance, and payroll results in a MySQL database. The console app lets the user add employees, record attendance, run payroll, and view payroll reports.

## ER Diagram Description
The database follows a simple relational model:

- `department` stores department names.
- `employee` belongs to one department and has one employee type.
- `salary` stores salary components for each employee.
- `attendance` records daily attendance entries for employees.
- `payroll_run` stores completed payroll results for a specific month and year.

ASCII view:

```text
department 1 ---- * employee 1 ---- * salary
                     |
                     *---- * attendance
                     |
                     *---- * payroll_run
```

## Database Setup
1. Install MySQL and create a database named `payroll_db`.
2. Update your database credentials in `src/main/resources/config.properties`.
3. Run the SQL script in `src/main/resources/schema.sql`.

Example:

```sql
CREATE DATABASE payroll_db;
USE payroll_db;
```

Then import the schema file.

## How to Run the Project
1. Open a terminal in the project root.
2. Run:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=com.payroll.app.Main
```

If `exec-maven-plugin` is not configured, use:

```bash
mvn clean compile
java -cp target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=cp.txt -q && cat cp.txt) com.payroll.app.Main
```

## Sample Menu
You can add your screenshots later here.

## What I Learned
- OOP design using inheritance and polymorphism
- JDBC and database connection management
- DAO layer separation for clean code
- Salary calculation logic and validation
- SQL reporting with JOIN and GROUP BY
- Writing a beginner-friendly project for resume and interview purposes

## Notes
- DB credentials are kept in `config.properties` as placeholders.
- Update the values before connecting to your local MySQL instance.
- This project intentionally uses plain JDBC, no frameworks, to make the flow easy to learn.
