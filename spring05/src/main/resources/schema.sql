DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS error;

CREATE TABLE users
(
    user_id    IDENTITY PRIMARY KEY,
    first_name VARCHAR(20),
    last_name  VARCHAR(20)
);

CREATE TABLE error
(
    error_id      IDENTITY PRIMARY KEY,
    error_message VARCHAR(500)
);