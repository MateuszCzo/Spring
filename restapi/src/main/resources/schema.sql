CREATE TABLE `POST` (
    `id` BIGINT AUTO_INCREMENT  PRIMARY KEY,
    `title` VARCHAR(400) NOT NULL,
    `content` VARCHAR(2000) NULL,
    `created` timestamp
);

CREATE TABLE `COMMENT` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id` BIGINT NOT NULL,
    `content` VARCHAR(2000) NULL,
    `created` timestamp
);

ALTER TABLE `COMMENT`
    ADD CONSTRAINT `comment_post_id`
    FOREIGN KEY (`post_id`) REFERENCES post(`id`);

CREATE TABLE `USER_TABLE` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `password` VARCHAR(100) NOT NULL,
    `username` VARCHAR(100) NOT NULL,
    `account_non_expired` INT NOT NULL,
    `account_non_locked` INT NOT NULL,
    `credentials_non_expired` INT NOT NULL,
    `enabled` INT NOT NULL
);

CREATE TABLE `AUTHORITY` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `authority` VARCHAR(100) NOT NULL
);

CREATE TABLE `USER_AUTHORITY` (
    `user_id` BIGINT NOT NULL,
    `authority_id` BIGINT NOT NULL
);