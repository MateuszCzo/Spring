CREATE TABLE "user" (
    "id"                        BIGSERIAL PRIMARY KEY,
    "name"                      VARCHAR(255) NOT NULL,
    "password"                  VARCHAR(255) NOT NULL,
    "account_non_expired"       BOOLEAN NOT NULL,
    "account_non_locked"        BOOLEAN NOT NULL,
    "credentials_non_expired"   BOOLEAN NOT NULL,
    "enabled"                   BOOLEAN NOT NULL,
    "role"                      VARCHAR(50) NOT NULL,
    UNIQUE ("name")
);

CREATE TABLE "address" (
    "id"          BIGSERIAL PRIMARY KEY,
    "user_id"     BIGINT NOT NULL,
    "street"      VARCHAR(255) NOT NULL,
    "number"      VARCHAR(8) NOT NULL,
    "post_code"   VARCHAR(6) NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "user"("id")
);

CREATE TABLE "contact" (
    "id"              BIGSERIAL PRIMARY KEY,
    "user_id"         BIGINT NOT NULL,
    "phone_number"    VARCHAR(16) NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "user"("id")
);

CREATE TABLE "image" (
    "id"              BIGSERIAL PRIMARY KEY,
    "name"            VARCHAR(255) NOT NULL,
    "path"            VARCHAR(255) NOT NULL,
    "extension"       VARCHAR(8) NOT NULL,
    "type"            VARCHAR(255) NOT NULL
);

CREATE TABLE "manufacturer" (
    "id"              BIGSERIAL PRIMARY KEY,
    "image_id"        BIGINT NOT NULL,
    "name"            VARCHAR(255) NOT NULL,
    "description"     VARCHAR(255) NOT NULL,
    FOREIGN KEY ("image_id") REFERENCES "image"("id")
);

CREATE TABLE "category" (
    "id"              BIGSERIAL PRIMARY KEY,
    "image_id"        BIGINT NOT NULL,
    "parent_id"       BIGINT NULL,
    "name"            VARCHAR(255) NOT NULL,
    "description"     VARCHAR(255) NOT NULL,
    FOREIGN KEY ("image_id") REFERENCES "image"("id"),
    FOREIGN KEY ("parent_id") REFERENCES "category"("id")
);

CREATE TABLE "attachment" (
    "id"              BIGSERIAL PRIMARY KEY,
    "name"            VARCHAR(255) NOT NULL,
    "description"     VARCHAR(255) NOT NULL,
    "path"            VARCHAR(255) NOT NULL,
    "extension"       VARCHAR(8) NOT NULL
);

CREATE TABLE "attribute_type" (
    "id"              BIGSERIAL PRIMARY KEY,
    "name"            VARCHAR(255) NOT NULL,
    "description"     VARCHAR(255) NOT NULL
);

CREATE TABLE "attribute" (
    "id"                  BIGSERIAL PRIMARY KEY,
    "attribute_type_id"   BIGINT NOT NULL,
    "value"               VARCHAR(255) NOT NULL,
    FOREIGN KEY ("attribute_type_id") REFERENCES "attribute_type"("id")
);

CREATE TABLE "product" (
    "id"              BIGSERIAL PRIMARY KEY,
    "category_id"     BIGINT NOT NULL,
    "manufacturer_id" BIGINT NOT NULL,
    "image_id"        BIGINT NOT NULL,
    "name"            VARCHAR(255) NOT NULL,
    "description"     VARCHAR(255) NOT NULL,
    "quantity"        VARCHAR(255) NOT NULL,
    "price"           VARCHAR(255) NOT NULL,
    "active"          BOOLEAN NOT NULL,
    "date_add"        DATE NOT NULL,
    "date_update"     DATE NOT NULL,
    FOREIGN KEY ("category_id") REFERENCES "category"("id"),
    FOREIGN KEY ("manufacturer_id") REFERENCES "manufacturer"("id"),
    FOREIGN KEY ("image_id") REFERENCES "image"("id")
);

CREATE TABLE "product_image" (
    "product_id"      BIGINT NOT NULL,
    "image_id"        BIGINT NOT NULL,
    FOREIGN KEY ("product_id") REFERENCES "product"("id"),
    FOREIGN KEY ("image_id") REFERENCES "image"("id")
);

CREATE TABLE "product_attachment" (
    "product_id"      BIGINT NOT NULL,
    "attachment_id"   BIGINT NOT NULL,
    FOREIGN KEY ("product_id") REFERENCES "product"("id"),
    FOREIGN KEY ("attachment_id") REFERENCES "attachment"("id")
);

CREATE TABLE "product_attribute" (
    "product_id"      BIGINT NOT NULL,
    "attribute_id"    BIGINT NOT NULL,
    FOREIGN KEY ("product_id") REFERENCES "product"("id"),
    FOREIGN KEY ("attribute_id") REFERENCES "attribute"("id")
);

CREATE TABLE "cart" (
    "id"              BIGSERIAL PRIMARY KEY,
    "user_id"         BIGINT NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "user"("id")
);

CREATE TABLE "cart_product" (
    "cart_id"         BIGINT NOT NULL,
    "product_id"      BIGINT NOT NULL,
    FOREIGN KEY ("cart_id") REFERENCES "cart"("id"),
    FOREIGN KEY ("product_id") REFERENCES "product"("id")
);

CREATE TABLE "payment" (
    "id"          BIGSERIAL PRIMARY KEY,
    "name"        VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL,
    "active"      BOOLEAN NOT NULL,
    "type"        VARCHAR(255) NOT NULL
);

CREATE TABLE "delivery" (
    "id"          BIGSERIAL PRIMARY KEY,
    "name"        VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL,
    "active"      BOOLEAN NOT NULL,
    "type"        VARCHAR(255) NOT NULL
);

CREATE TABLE "order" (
    "id"              BIGSERIAL PRIMARY KEY,
    "user_id"         BIGINT NOT NULL,
    "payment_id"      BIGINT NOT NULL,
    "delivery_id"     BIGINT NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "user"("id"),
    FOREIGN KEY ("payment_id") REFERENCES "payment"("id"),
    FOREIGN KEY ("delivery_id") REFERENCES "delivery"("id")
);

CREATE TABLE "order_product" (
    "order_id"        BIGINT NOT NULL,
    "product_id"      BIGINT NOT NULL,
    FOREIGN KEY ("order_id") REFERENCES "order"("id"),
    FOREIGN KEY ("product_id") REFERENCES "product"("id")
);
