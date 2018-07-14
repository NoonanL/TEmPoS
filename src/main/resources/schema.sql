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
  SKU VARCHAR(32),
  name VARCHAR(32),
  RRP int,
  cost int,
  department VARCHAR(32),
  brand VARCHAR(32),
  description VARCHAR(1024),
  branch01stock int,
  branch02stock int,
  warehousestock int
);
