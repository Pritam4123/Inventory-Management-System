# Inventory Management System

A console-based inventory management system built with Java, JDBC, and PostgreSQL.

## Features

- **Product Management**: Full CRUD operations for products
- **Sales Management**: Process sales with automated stock deduction
- **Low Stock Alerts**: Automatic alerts when products fall below threshold
- **Revenue Reports**: Monthly and yearly revenue summaries using SQL aggregation
- **Transaction Management**: Ensures data consistency with proper transaction handling
- **DAO Pattern**: Clean separation between database logic and business logic
- **Unit Testing**: Comprehensive tests using JUnit 5 and Mockito

## Architecture

```
src/main/java/com/inventory/
├── model/          # Data models (Product, Sale, RevenueSummary)
├── dao/            # Data Access Objects (interfaces + implementations)
├── service/        # Business logic layer
├── util/           # Utility classes (DBConnection, InputHelper, LoggerUtil)
├── exception/     # Custom exceptions
├── config/         # Application configuration
├── dto/            # Data Transfer Objects
└── Main.java      # Main application entry point
```

## Prerequisites

- Java JDK 17 or higher
- PostgreSQL 12 or higher
- Maven 3.8 or higher

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

Or set environment variables:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Build & Run

### Using Maven

```
bash
# Compile the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.inventory.Main"

# Run tests
mvn test
```

### Using IDE

1. Import the project as a Maven project
2. Run `com.inventory.Main` as the main class

## Project Structure

```
inventory-management-system/
├── pom.xml                    # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/inventory/
│   │   │   ├── model/         # Domain models
│   │   │   ├── dao/           # Data access layer
│   │   │   ├── service/       # Business logic
│   │   │   ├── util/          # Utilities
│   │   │   ├── exception/     # Custom exceptions
│   │   │   ├── config/        # Configuration
│   │   │   └── dto/           # Data transfer objects
│   │   └── resources/
│   │       ├── db.properties  # Database config
│   │       └── logging.properties
│   └── test/
│       └── java/com/inventory/
│           └── model/         # Unit tests
├── lib/                       # JDBC drivers
├── sql/
│   └── schema.sql            # Database schema
└── resources/                # Configuration files
```

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
| Column | Type | Description |
|--------|------|-------------|
| id | SERIAL | Primary key |
| name | VARCHAR(255) | Product name |
| description | TEXT | Product description |
| category | VARCHAR(100) | Product category |
| price | DECIMAL(10,2) | Product price |
| quantity | INTEGER | Stock quantity |
| low_stock_threshold | INTEGER | Low stock alert threshold |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

### Sales Table
| Column | Type | Description |
|--------|------|-------------|
| id | SERIAL | Primary key |
| product_id | INTEGER | Foreign key to products |
| quantity_sold | INTEGER | Quantity sold |
| total_amount | DECIMAL(10,2) | Total sale amount |
| sale_date | TIMESTAMP | Sale timestamp |

## Testing

The project includes unit tests for:
- Product model
- Sale model
- InventoryService
- SalesService

Run tests with:
```
bash
mvn test
```

## Error Handling

- Custom exceptions for different error scenarios
- Transaction rollback on errors
- Input validation with user-friendly messages
- Global exception handler for consistent error responses

## License

This project is for educational purposes.
