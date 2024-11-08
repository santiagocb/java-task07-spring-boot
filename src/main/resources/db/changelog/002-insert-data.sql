--liquibase formatted sql

--changeset yourname:1
INSERT INTO person (name, age) VALUES ('John Doe', 30);
INSERT INTO person (name, age) VALUES ('Jane Doe', 25);
INSERT INTO person (name, age) VALUES ('Alice Johnson', 28);
INSERT INTO person (name, age) VALUES ('Bob Smith', 35);
INSERT INTO person (name, age) VALUES ('Charlie Brown', 22);