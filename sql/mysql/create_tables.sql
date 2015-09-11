CREATE DATABASE IF NOT EXISTS asso_plus;

CREATE TABLE IF NOT EXISTS asso_plus.event (
  id            INT          NOT NULL AUTO_INCREMENT,
  title         VARCHAR(50)  NOT NULL,
  date          VARCHAR(16)  NOT NULL,
  text          VARCHAR(500) NOT NULL,
  PRIMARY KEY (id)
);