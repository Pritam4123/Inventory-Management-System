# Interview Preparation Guide for TCS Digital & IBM

## 🎯 Project Overview (30-second pitch)

"This is a **console-based Inventory Management System** built with **Java, JDBC, and PostgreSQL**. It demonstrates full CRUD operations, transaction management, and SQL aggregation for revenue reporting. The project follows a **3-layer architecture** (DAO/Service/UI) and includes custom exceptions, unit testing with JUnit and Mockito, and proper error handling."

---

## 📝 How to Explain Your Project (Full Explanation)

### Starting Point:
"I'd like to explain my Inventory Management System project. This was a **core Java project** where I built a console application to manage products and sales for a store."

### Key Points to Cover:

#### 1. What does the system do?
- Manages products (add, update, delete, search)
- Processes sales with automatic stock deduction
- Generates revenue reports (monthly/yearly)
- Shows low stock alerts

#### 2. Why did you build this?
"To demonstrate my Java and database skills. It shows I understand:
- JDBC connectivity
- SQL queries
- Transaction management
- Error handling
- Unit testing"

#### 3. Architecture explanation:
"I used a **3-layer architecture**:
```
┌─────────────────┐
│   Main/UI       │  ← Console menu
├─────────────────┤
│   Service       │  ← Business logic
├─────────────────┤
│   DAO           │  ← Database operations
└─────────────────┘
```

This separation makes code:
- **Maintainable** - Changes in one layer don't affect others
- **Testable** - Can test each layer separately
- **Reusable** - DAO methods can be used by different services"

---

## ❓ Expected Interview Questions & Detailed Answers

### Q1: Explain your project architecture
**💡 What interviewer wants**: Do you understand layered architecture?

**Answer**:
"I followed a 3-layer architecture:

1. **DAO Layer (Data Access Object)**:
   - Contains all database operations
   - ProductDAOImpl handles product CRUD
   - SaleDAOImpl handles sales CRUD
   
2. **Service Layer**:
   - Contains business logic
   - InventoryService: product operations
   - SalesService: sales with transaction management
   
3. **UI Layer (Main)**:
   - Console-based menu
   - User input handling
   - Display results

**Why separation?**
- Loose coupling
- Easy to maintain
- Easy to test"

---

### Q2: How do you handle database transactions?
**💡 What interviewer wants**: Do you understand ACID properties?

**Answer**:
"I use JDBC transaction management. Here's how:

```
java
public Sale processSale(int productId, int quantity) {
    Connection conn = null;
    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);  // ← Start transaction
        
        // Step 1: Create sale record
        Sale sale = saleDAO.create(sale);
        
        // Step 2: Update product quantity
        productDAO.updateQuantity(productId, newQuantity);
        
        conn.commit();  // ← Save changes
    } catch (Exception e) {
        if (conn != null) {
            conn.rollback();  // ← Undo changes
        }
        throw e;
    } finally {
        if (conn != null) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}
```

**Why this matters:**
- Ensures data consistency
- If sale fails, inventory not updated
- All-or-nothing operation"

---

### Q3: What exception handling do you use?
**💡 What interviewer wants**: Do you handle errors properly?

**Answer**:
"I created custom exceptions for different scenarios:

1. **ProductNotFoundException** - When product doesn't exist
2. **InsufficientStockException** - When quantity is less than requested
3. **DatabaseException** - For SQL errors

Example of InsufficientStockException:
```
java
public class InsufficientStockException extends RuntimeException {
    private int productId;
    private int requestedQty;
    private int availableQty;
    
    // Constructor with all fields
    // Getters for all fields
}
```

**Why custom exceptions?**
- More specific error messages
- Can store extra information
- Easier to handle different errors differently"

---

### Q4: How do you prevent SQL injection?
**💡 What interviewer wants**: Do you know about security?

**Answer**:
"I use **PreparedStatement** with parameterized queries:

**❌ WRONG (vulnerable)**:
```
java
String sql = "SELECT * FROM products WHERE name = '" + name + "'";
// Attacker can input: '; DROP TABLE products; --
```

**✅ RIGHT (safe)**:
```
java
String sql = "SELECT * FROM products WHERE name = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, name);  // ← Properly escaped
```

**Why this works:**
- Parameter values are escaped automatically
- Database treats input as data, not SQL code
- Prevents malicious input execution"

---

### Q5: How do you handle low stock alerts?
**💡 What interviewer wants**: Can you implement business logic?

**Answer**:
"Each product has a `low_stock_threshold` field. When quantity falls below this threshold, the system alerts:

```
java
public List<Product> checkLowStockAlerts() {
    // Query from DAO
    List<Product> lowStockProducts = productDAO.getLowStockProducts();
    
    // Display warnings
    for (Product product : lowStockProducts) {
        System.out.printf("WARNING: %s - Only %d left (threshold: %d)%n",
            product.getName(),
            product.getQuantity(),
            product.getLowStockThreshold());
    }
    return lowStockProducts;
}
```

**Database query:**
```
sql
SELECT * FROM products WHERE quantity <= low_stock_threshold
```

This helps store managers know when to reorder."

---

### Q6: What is the DAO pattern? Why use it?
**💡 What interviewer wants**: Do you know design patterns?

**Answer**:
"**DAO (Data Access Object)** separates database logic from business logic.

**Structure:**
```
ProductDAO (Interface)
    ↓
ProductDAOImpl (Implementation)
```

**Benefits:**
1. **Encapsulation** - All DB code in one place
2. **Abstraction** - Service doesn't know SQL
3. **Testability** - Can mock DAO in tests
4. **Maintainability** - Change DB without affecting service

**Example:**
```
java
// Service doesn't know HOW data is fetched
Product product = productDAO.getById(id);

// DAO decides the SQL
ResultSet rs = pstmt.executeQuery();
```

---

### Q7: How do you generate revenue reports?
**💡 What interviewer wants**: Do you know SQL aggregations?

**Answer**:
"I use SQL aggregate functions:

**Monthly Revenue:**
```
sql
SELECT 
    COUNT(*) as total_transactions,
    SUM(total_amount) as total_revenue,
    SUM(quantity_sold) as total_items_sold
FROM sales
WHERE EXTRACT(YEAR FROM sale_date) = 2024
  AND EXTRACT(MONTH FROM sale_date) = 1;
```

**Yearly Revenue (by month):**
```
sql
SELECT 
    EXTRACT(MONTH FROM sale_date) as month,
    COUNT(*) as transactions,
    SUM(total_amount) as revenue,
    SUM(quantity_sold) as items_sold
FROM sales
WHERE EXTRACT(YEAR FROM sale_date) = 2024
GROUP BY EXTRACT(MONTH FROM sale_date)
ORDER BY month;
```

**Key SQL concepts used:**
- COUNT, SUM aggregate functions
- GROUP BY for grouping
- WHERE with date functions
- EXTRACT for date parts"

---

### Q8: What testing have you done?
**💡 What interviewer wants**: Do you test your code?

**Answer**:
"I wrote unit tests using JUnit 5 and Mockito:

**Example - Testing getProduct:**
```
java
@Test
public void testGetProduct_Success() {
    // Arrange - setup mock
    when(productDAO.getById(1)).thenReturn(testProduct);
    
    // Act - call method
    Product result = inventoryService.getProduct(1);
    
    // Assert - verify result
    assertNotNull(result);
    assertEquals("Test Product", result.getName());
}
```

**What I test:**
- Happy path (normal operation)
- Edge cases (empty list, not found)
- Exception handling

**Test coverage:**
- InventoryServiceTest: 10 tests
- SalesServiceTest: 9 tests
- Model tests for entities"

---

### Q9: What are the challenges you faced?
**💡 What interviewer wants**: Can you solve problems?

**Answer**:
"Three main challenges:

1. **Transaction Rollback**:
   - Problem: If sale creation succeeded but inventory update failed, data would be inconsistent
   - Solution: Used try-catch with rollback in catch block

2. **Handling Null Values**:
   - Problem: Database can return null for optional fields
   - Solution: Added null checks before using values

3. **Date Handling**:
   - Problem: Storing and comparing dates in different formats
   - Solution: Used LocalDateTime and DateTimeFormatter"

---

### Q10: What is the difference between Statement and PreparedStatement?
**💡 What interviewer wants**: Do you know JDBC deeply?

**Answer**:
| Feature | Statement | PreparedStatement |
|---------|-----------|-------------------|
| SQL Injection | Vulnerable | Safe |
| Performance | Slower (compiles each time) | Faster (pre-compiled) |
| Parameters | No | Yes (?) |
| Reusability | No | Yes |

**Use PreparedStatement when:**
- Taking user input
- Executing query multiple times
- Need better performance"

---

### Q11: What is the difference between commit and rollback?
**💡 What interviewer wants**: Do you understand transactions?

**Answer**:
- **COMMIT**: Saves all changes made during transaction permanently
- **ROLLBACK**: Undoes all changes made during transaction

**Analogy**: Think of a text editor:
- **COMMIT** = Ctrl+S (save file)
- **ROLLBACK** = Ctrl+Z (undo all)"

---

### Q12: What is ACID in databases?
**💡 What interviewer wants**: Do you know database fundamentals?

**Answer**:
"ACID is a set of properties for reliable database transactions:

- **Atomicity**: All or nothing - transaction succeeds completely or fails completely
- **Consistency**: Data remains valid - transaction brings database from one valid state to another
- **Isolation**: Concurrent transactions don't interfere - each sees consistent data
- **Durability**: Once committed, data is permanent - survives system crash

My project ensures these through:
- Transactions (auto-commit off)
- Constraints (PRIMARY KEY, FOREIGN KEY)
- Proper commit/rollback handling"

---

### Q13: What is the difference between WHERE and HAVING?
**💡 What interviewer wants**: Do you know SQL well?

**Answer**:
- **WHERE**: Filters rows BEFORE grouping
- **HAVING**: Filters groups AFTER grouping

**Example:**
```
sql
-- Get categories with total sales > 10000
SELECT category, SUM(amount) as total
FROM sales
JOIN products ON sales.product_id = products.id
GROUP BY category
HAVING SUM(amount) > 10000;
```

**Rule of thumb:** Use WHERE for regular column filters, HAVING for aggregate filters"

---

### Q14: What is an index? Why use it?
**💡 What interviewer wants**: Do you know about performance?

**Answer**:
"Index is like a book index - helps find data faster.

**In my project:**
```sql
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_sales_date ON sales(sale_date);
```

**Benefits:**
- Faster searches (O(log n) vs O(n))
- Better query performance

**Trade-offs:**
- Uses extra storage space
- Slower INSERT/UPDATE/DELETE"

---

### Q15: What is normalization? What are the normal forms?
**💡 What interviewer wants**: Do you know database design?

**Answer**:
"Normalization organizes data to reduce redundancy.

**My schema uses:**
- **1NF**: Atomic values (no multi-valued fields)
- **2NF**: No partial dependencies
- **3NF**: No transitive dependencies

**Example:**
```
products table:
- id, name, description, category, price, quantity

sales table:
- id, product_id (FK), quantity_sold, total_amount, sale_date
```

Product info is in one table, sales reference it - no duplicate data."

---

## 🔧 Technical Stack Summary

| Component | Technology |
|-----------|------------|
| Language | Java 17 |
| Database | PostgreSQL |
| JDBC Driver | PostgreSQL 42.7.3 |
| Testing | JUnit 5, Mockito |
| Build Tool | Maven |

---

## 📂 Project Files Summary

| File | Purpose |
|------|---------|
| Main.java | Application entry point, menu system |
| InventoryService.java | Product CRUD, low stock alerts |
| SalesService.java | Sales processing, transactions |
| ProductDAOImpl.java | Product DB operations |
| SaleDAOImpl.java | Sales DB operations, revenue queries |
| DBConnection.java | Database connection utility |
| GlobalExceptionHandler.java | Centralized error handling |

---

## 💻 Commands to Run

```
bash
# Compile project
mvn compile

# Run tests
mvn test

# Run application
mvn exec:java -Dexec.mainClass="com.inventory.Main"
```

---

## 🎓 Tips for Interview Day

1. **Be Confident**: You built this from scratch - that's impressive!
2. **Know Your Code**: Be ready to explain any part of the code
3. **Practice Out Loud**: Say your answers out loud while practicing
4. **Connect to Real-world**: "This is how real stores track inventory"
5. **Ask Questions**: "Would you like me to explain any specific part?"
6. **Admit What You Don't Know**: "I haven't used that, but I'd learn it quickly"

---

## 🚀 Suggested Improvements (for "What would you add next?")

When interviewer asks "What would you improve?", say:

1. **REST API layer** - "Add Spring Boot to expose APIs"
2. **User authentication** - "Add login for security"
3. **Connection pooling** - "Use HikariCP for better performance"
4. **Integration tests** - "Test with actual database"
5. **Frontend** - "Add React or Angular UI"

---

## 📌 Quick Reference Card

```
Project: Inventory Management System
Language: Java 17
Database: PostgreSQL
Architecture: 3-layer (DAO/Service/UI)

Key Features:
- CRUD operations
- Transaction management
- SQL aggregations
- Custom exceptions
- Unit testing

To run:
mvn compile
mvn exec:java -Dexec.mainClass="com.inventory.Main"
```

---

Good luck with your interviews! 🎯
