CREATE TABLE IF NOT EXISTS customer (
    id bigserial primary key not null,
    email varchar(255),
    firstname varchar(255),
    lastname varchar(255)
);
