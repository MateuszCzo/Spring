CREATE TABLE "epidemic_parameters" (
    "id"                            BIGSERIAL PRIMARY KEY,
    "name"                          VARCHAR(256) NOT NULL,
    "population"                    INT NOT NULL,
    "infected"                      INT NOT NULL,
    "r"                             REAL NOT NULL,
    "mortality"                     REAL NOT NULL,
    "recovery_time"                 INT NOT NULL,
    "death_time"                    INT NOT NULL,
    "simulation_time"               INT NOT NULL,
    UNIQUE ("name")
);

CREATE TABLE "epidemic_simulation" (
    "id"                            BIGSERIAL PRIMARY KEY,
    "epidemic_parameters_id"        BIGINT NOT NULL,
    "day"                           INT NOT NULL,
    "infected"                      INT NOT NULL,
    "healthy"                       INT NOT NULL,
    "dead"                          INT NOT NULL,
    "immune"                        INT NOT NULL,
    FOREIGN KEY ("epidemic_parameters_id") REFERENCES "epidemic_parameters"("id")
);