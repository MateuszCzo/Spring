CREATE TABLE IF NOT EXISTS customer (
    id bigserial primary key not null,
    email varchar(255) not null,
    firstname varchar(255) not null,
    lastname varchar(255) not null
);
