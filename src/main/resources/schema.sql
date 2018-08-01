CREATE TABLE IF NOT EXISTS customers (
  id int AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(32),
  firstname VARCHAR(64),
  surname VARCHAR(64),
  street VARCHAR(64),
  town VARCHAR(64),
  postcode VARCHAR(32),
  city VARCHAR(64),
  country VARCHAR(64),
  mobile VARCHAR(32),
  email VARCHAR(64),
  marketingStatus VARCHAR(8));

CREATE TABLE IF NOT EXISTS users (
  id int AUTO_INCREMENT,
  username VARCHAR(255) PRIMARY KEY,
  hash VARCHAR(255),
  isAdmin VARCHAR(32));

CREATE TABLE IF NOT EXISTS transactions (
  id int AUTO_INCREMENT PRIMARY KEY,
  FOREIGN KEY (id) REFERENCES customers(id),
  FOREIGN KEY (id) REFERENCES users(id),
  value VARCHAR(32));

CREATE TABLE IF NOT EXISTS branchList (
  id int AUTO_INCREMENT PRIMARY KEY,
  branch VARCHAR(32));

CREATE TABLE IF NOT EXISTS products(
  id int AUTO_INCREMENT PRIMARY KEY,
  SKU VARCHAR(32) NOT NULL UNIQUE,
  name VARCHAR(32),
  RRP DECIMAL(13,4),
  cost DECIMAL(13,4),
  department VARCHAR(32),
  brand VARCHAR(32),
  description VARCHAR(1024),
);

CREATE TABLE IF NOT EXISTS departments (
  id         int AUTO_INCREMENT PRIMARY KEY,
  department VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS brands (
  id         int AUTO_INCREMENT PRIMARY KEY,
  brand VARCHAR(64) NOT NULL UNIQUE ,
  distributor VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS distributors (
  id         int AUTO_INCREMENT PRIMARY KEY,
  distributor VARCHAR(64) NOT NULL UNIQUE
);