CREATE DATABASE IF NOT EXISTS asso_plus;

CREATE TABLE IF NOT EXISTS asso_plus.licence (
  id    INT                NOT NULL AUTO_INCREMENT,
  code  VARCHAR(10) UNIQUE NOT NULL,
  label VARCHAR(50) UNIQUE NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS asso_plus.`option` (
  id           INT                NOT NULL AUTO_INCREMENT,
  code         VARCHAR(10) UNIQUE NOT NULL,
  label        VARCHAR(50) UNIQUE NOT NULL,
  licence_code VARCHAR(10),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS asso_plus.member (
  id            INT          NOT NULL AUTO_INCREMENT,
  studentNumber INT UNIQUE   NOT NULL,
  lastname      VARCHAR(50)  NOT NULL,
  firstname     VARCHAR(50)  NOT NULL,
  birthday      VARCHAR(8)   NOT NULL,
  email         VARCHAR(100) NOT NULL,
  licence_code  VARCHAR(10)  NOT NULL,
  option_code   VARCHAR(10)  NOT NULL,
  phone         VARCHAR(14),
  adress        VARCHAR(50),
  postal_code   VARCHAR(5),
  city          VARCHAR(50),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS asso_plus.event (
  id            INT          NOT NULL AUTO_INCREMENT,
  title         VARCHAR(50)  NOT NULL,
  date          VARCHAR(16)  NOT NULL,
  text          VARCHAR(255) NOT NULL,
  category_code VARCHAR(15)  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS asso_plus.category (
  id    INT                NOT NULL AUTO_INCREMENT,
  code  VARCHAR(10) UNIQUE NOT NULL,
  label VARCHAR(50) UNIQUE NOT NULL,
  PRIMARY KEY (id)
);