CREATE TABLE IF NOT EXISTS _notification (
    id bigserial primary key,
    to_customer_id bigint not null,
    to_customer_email varchar(255) not null,
    sender varchar(255) not null,
    message varchar(255) not null,
    send_at timestamp not null
);
