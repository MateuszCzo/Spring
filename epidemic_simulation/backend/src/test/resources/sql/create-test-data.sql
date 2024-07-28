INSERT INTO "epidemic_parameters" ("id", "name", "population", "infected",
    "r", "mortality", "recovery_time", "death_time", "simulation_time") VALUES (
    1,
    'test epidemic',
    1,
    1,
    1,
    1,
    1,
    1,
    1
);

INSERT INTO "epidemic_simulation" ("id", "epidemic_parameters_id", "day", "infected", "healthy", "dead", "immune") VALUES (
    1,
    1,
    1,
    1,
    0,
    0,
    0
);
