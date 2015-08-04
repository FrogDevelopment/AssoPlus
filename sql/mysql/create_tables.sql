CREATE DATABASE IF NOT EXISTS asso_plus;

CREATE TABLE IF NOT EXISTS asso_plus.licence (
  id INT NOT NULL AUTO_INCREMENT,
  code VARCHAR(10),
  label VARCHAR(50),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS asso_plus.`option` (
  id INT NOT NULL AUTO_INCREMENT,
  code VARCHAR(10),
  label VARCHAR(50),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS asso_plus.member (
  id INT NOT NULL AUTO_INCREMENT,
  studentNumber INT,
  lastname VARCHAR(50),
  firstname VARCHAR(50),
  birthday VARCHAR(8),
  email VARCHAR(100),
  licence_code VARCHAR(10),
  option_code VARCHAR(10),
  phone VARCHAR(14),
  adress VARCHAR(50),
  postal_code VARCHAR(5),
  city VARCHAR(50),
  PRIMARY KEY (id)
);