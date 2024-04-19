--liquibase formatted sql
--changeset mczosnyka:1
CREATE TABLE `USER_AUTHORITY` (
    `user_id` BIGINT NOT NULL,
    `authority_id` BIGINT NOT NULL
);