CREATE TABLE IF NOT EXISTS fraud_check_history (
  id bigserial primary key not null,
  customer_id bigint not null,
  is_fraudster boolean not null,
  created_at timestamp not null
);
