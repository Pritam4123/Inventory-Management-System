# Inventory Management System

A console-based inventory management system built with Java, JDBC, and PostgreSQL.

## Features

- **Product Management**: Full CRUD operations for products
- **Sales Management**: Process sales with automated stock deduction
- **Low Stock Alerts**: Automatic alerts when products fall below threshold
- **Revenue Reports**: Monthly and yearly revenue summaries using SQL aggregation
- **Transaction Management**: Ensures data consistency with proper transaction handling
- **DAO Pattern**: Clean separation between database logic and business logic

## Architecture

```
src/main/java/com/inventory/
├── model/          # Data models (Product, Sale, RevenueSummary)
├── dao/            # Data Access Objects (interfaces + implementations)
├── service/        # Business logic layer
├── util/           # Utility classes (DBConnection, InputHelper)
├── exception/     # Custom exceptions
└── Main.java      # Main application entry point
```

## Prerequisites

- Java JDK 8 or higher
- PostgreSQL 12 or higher
- PostgreSQL JDBC Driver

## Database Setup

1. Create a PostgreSQL database named `inventory_db`
2. Run the SQL script to create tables:

```
bash
psql -U postgres -d inventory_db -f sql/schema.sql
```

## Configuration

Edit `resources/db.properties` to configure database connection:

```
properties
db.url=jdbc:postgresql://localhost:5432/inventory_db
db.username=postgres
db.password=your_password
```

## Build & Run

### Using Command Line

```
bash
# Compile
javac -d out -cp "lib/*" src/main/java/com/inventory/**/*.java

# Run
java -cp "out:lib/*" com.inventory.Main
```

### Using Maven

Create a `pom.xml` and use:

```
bash
mvn compile
mvn exec:java -Dexec.mainClass="com.inventory.Main"
```

### Using IDE

1. Import the project as a Java project
2. Add PostgreSQL JDBC driver to classpath
3. Run `com.inventory.Main`

## Usage

The application provides a console menu:

1. **Product Management**
   - View All Products
   - Add New Product
   - Update Product
   - Delete Product
   - Search Product by Name
   - View Products by Category

2. **Sales Management**
   - Process New Sale (automatically deducts stock)
   - View All Sales
   - View Sales by Product
   - View Sales by Date Range

3. **Reports & Revenue**
   - Monthly Revenue Summary
   - Yearly Revenue Summary

4. **Low Stock Alerts**
   - View all products below threshold

## Database Schema

### Products Table
- id (SERIAL PRIMARY KEY)
- name (VARCHAR)
- description (TEXT)
- category (VARCHAR)
- price (DECIMAL)
- quantity (INTEGER)
- low_stock_threshold (INTEGER)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

### Sales Table
- id (SERIAL PRIMARY KEY)
- product_id (INTEGER REFERENCES products)
- quantity_sold (INTEGER)
- total_amount (DECIMAL)
- sale_date (TIMESTAMP)

## Error Handling

- Custom exceptions for different error scenarios
- Transaction rollback on errors
- Input validation with user-friendly messages

## License

This project is for educational purposes.
