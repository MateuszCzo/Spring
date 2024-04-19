--liquibase formatted sql
--changeset mczosnyka:1
CREATE TABLE `USER_TABLE` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `password` VARCHAR(100) NOT NULL,
    `username` VARCHAR(100) NOT NULL,
    `account_non_expired` INT NOT NULL,
    `account_non_locked` INT NOT NULL,
    `credentials_non_expired` INT NOT NULL,
    `enabled` INT NOT NULL
);