--liquibase formatted sql
--changeset mczosnyka:1
CREATE TABLE `AUTHORITY` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `authority` VARCHAR(100) NOT NULL
);