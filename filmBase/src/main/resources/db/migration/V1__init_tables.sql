CREATE TABLE actor (
    id          BIGSERIAL PRIMARY KEY,
    firstname   VARCHAR(255),
    lastname    VARCHAR(255)
);

CREATE TABLE film (
    id          BIGSERIAL PRIMARY KEY,
    status      VARCHAR(50),
    title       VARCHAR(255),
    description TEXT
);

CREATE TABLE _user (
    id                          BIGSERIAL PRIMARY KEY,
    username                    VARCHAR(255) NOT NULL,
    password                    VARCHAR(255) NOT NULL,
    account_non_expired         BOOLEAN NOT NULL,
    account_non_locked          BOOLEAN NOT NULL,
    credentials_non_expired     BOOLEAN NOT NULL,
    enabled                     BOOLEAN NOT NULL,
    role                        VARCHAR(50) NOT NULL,
    UNIQUE (username)
);

CREATE TABLE rating (
    id          BIGSERIAL PRIMARY KEY,
    film_id     BIGINT,
    user_id     BIGINT,
    status      VARCHAR(50),
    rating      INT,
    description TEXT,
    FOREIGN KEY (film_id) REFERENCES film(id),
    FOREIGN KEY (user_id) REFERENCES _user(id),
    UNIQUE (film_id, user_id)
);

CREATE TABLE film_actor (
    film_id     BIGINT,
    actor_id    BIGINT,
    FOREIGN KEY (film_id) REFERENCES film(id),
    FOREIGN KEY (actor_id) REFERENCES actor(id),
    PRIMARY KEY (film_id, actor_id)
);
