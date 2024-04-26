CREATE TABLE actor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255)
);

CREATE TABLE film (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(50),
    title VARCHAR(255),
    description TEXT
);

CREATE TABLE _user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_non_expired INT NOT NULL,
    account_non_locked INT NOT NULL,
    credentials_non_expired INT NOT NULL,
    enabled INT NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    film_id BIGINT,
    user_id BIGINT,
    status VARCHAR(50),
    rating INT,
    description TEXT,
    FOREIGN KEY (film_id) REFERENCES film(id),
    FOREIGN KEY (user_id) REFERENCES _user(id),
    UNIQUE (film_id, user_id)
);

CREATE TABLE film_actor (
    film_id BIGINT,
    actor_id BIGINT,
    FOREIGN KEY (film_id) REFERENCES film(id),
    FOREIGN KEY (actor_id) REFERENCES actor(id),
    PRIMARY KEY (film_id, actor_id)
);
