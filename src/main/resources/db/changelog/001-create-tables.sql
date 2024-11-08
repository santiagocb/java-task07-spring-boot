--liquibase formatted sql

--changeset yourname:1
CREATE TABLE person (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL
);