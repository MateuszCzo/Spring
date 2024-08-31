ALTER TABLE image DROP COLUMN "extension";

ALTER TABLE attachment DROP COLUMN "extension";

ALTER TABLE attachment ADD COLUMN "type" VARCHAR(255);
