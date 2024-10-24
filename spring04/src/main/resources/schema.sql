drop table IF EXISTS users;

CREATE TABLE users
(
    id IDENTITY PRIMARY KEY,
    firstname varchar(100) NOT NULL,
    lastname  varchar(100) NOT NULL,
    birth     TIMESTAMP
);