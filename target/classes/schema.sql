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
  marketingStatus VARCHAR(8),
  purchaseHistory VARCHAR(32));

CREATE TABLE IF NOT EXISTS users (
  id int AUTO_INCREMENT,
  username VARCHAR(255) PRIMARY KEY,
  hash VARCHAR(255),
  isAdmin VARCHAR(32));

