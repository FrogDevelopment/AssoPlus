CREATE TABLE IF NOT EXISTS member (
  member_id      INTEGER PRIMARY KEY,
  student_number TEXT UNIQUE   NOT NULL,
  lastname       TEXT          NOT NULL,
  firstname      TEXT          NOT NULL,
  birthday       TEXT,
  email          TEXT,
  degree_code    TEXT,
  option_code    TEXT,
  phone          TEXT,
  subscription   INTEGER       NOT NULL    DEFAULT 0,
  annals         INTEGER       NOT NULL    DEFAULT 0
);

CREATE TABLE IF NOT EXISTS degree (
  degree_id INTEGER PRIMARY KEY,
  code      TEXT UNIQUE NOT NULL,
  label     TEXT        NOT NULL
);

CREATE TABLE IF NOT EXISTS option (
  option_id INTEGER PRIMARY KEY,
  code      TEXT UNIQUE NOT NULL,
  label     TEXT        NOT NULL,
  degree_id INTEGER,
  FOREIGN KEY (degree_id) REFERENCES degree (degree_id)
);