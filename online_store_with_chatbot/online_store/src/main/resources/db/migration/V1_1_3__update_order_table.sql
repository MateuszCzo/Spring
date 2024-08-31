ALTER TABLE "order"
ADD COLUMN "address_id" BIGINT NOT NULL,
ADD COLUMN "contact_id" BIGINT NOT NULL,
ADD COLUMN "final_price" NUMERIC(10,2) NOT NULL,
ADD COLUMN "date" DATE NOT NULL,
ADD FOREIGN KEY ("address_id") REFERENCES "address"("id"),
ADD FOREIGN KEY ("contact_id") REFERENCES "contact"("id");