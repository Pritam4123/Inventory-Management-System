-- Database Schema for Inventory Management System
-- PostgreSQL

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS sales CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Products table
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    low_stock_threshold INTEGER NOT NULL DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sales table
CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(id),
    quantity_sold INTEGER NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_quantity ON products(quantity);
CREATE INDEX idx_sales_product_id ON sales(product_id);
CREATE INDEX idx_sales_date ON sales(sale_date);

-- Insert sample products
INSERT INTO products (name, description, category, price, quantity, low_stock_threshold) VALUES
('Laptop', 'High-performance laptop', 'Electronics', 999.99, 50, 10),
('Mouse', 'Wireless mouse', 'Electronics', 29.99, 100, 20),
('Keyboard', 'Mechanical keyboard', 'Electronics', 79.99, 75, 15),
('Monitor', '27-inch 4K monitor', 'Electronics', 399.99, 30, 5),
('Headphones', 'Noise-cancelling headphones', 'Electronics', 199.99, 60, 10),
('USB Cable', 'USB-C cable 2m', 'Accessories', 12.99, 200, 50),
('Webcam', 'HD webcam', 'Electronics', 89.99, 40, 8),
('Desk', 'Standing desk', 'Furniture', 449.99, 15, 3),
('Chair', 'Ergonomic office chair', 'Furniture', 299.99, 20, 5),
('Notebook', 'A5 notebook', 'Stationery', 5.99, 500, 100);

-- Insert sample sales
INSERT INTO sales (product_id, quantity_sold, total_amount, sale_date) VALUES
(1, 2, 1999.98, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(2, 5, 149.95, CURRENT_TIMESTAMP - INTERVAL '4 days'),
(3, 3, 239.97, CURRENT_TIMESTAMP - INTERVAL '3 days'),
(1, 1, 999.99, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(4, 2, 799.98, CURRENT_TIMESTAMP - INTERVAL '1 day'),
(5, 4, 799.96, CURRENT_TIMESTAMP);

-- Insert sample users (password is plain text - in production use hashed passwords!)
-- Default credentials: admin / admin123
INSERT INTO users (username, password, full_name, email, role, active) VALUES
('admin', 'admin123', 'Administrator', 'admin@inventory.com', 'ADMIN', true),
('user', 'user123', 'Regular User', 'user@inventory.com', 'USER', true);
