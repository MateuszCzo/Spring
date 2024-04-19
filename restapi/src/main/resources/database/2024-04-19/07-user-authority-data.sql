--liquibase formatted sql
--changeset mczosnyka:1
insert into authority(authority) values ('USER_ROLE');
insert into authority(authority) values ('ADMIN_ROLE');
insert into user_table(password, username, account_non_expired, account_non_locked, credentials_non_expired, enabled) values ('password', 'user@user.com', 1, 1, 1, 1);
insert into user_authority(user_id, authority_id) values (1, 1);