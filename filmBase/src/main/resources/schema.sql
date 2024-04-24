CREATE TABLE actor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255)
);

CREATE TABLE film (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    status VARCHAR(50)
);

CREATE TABLE rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description TEXT,
    rating INT,
    status VARCHAR(50),
    film_id BIGINT,
    FOREIGN KEY (film_id) REFERENCES film(id)
);

CREATE TABLE film_actor (
    film_id BIGINT,
    actor_id BIGINT,
    FOREIGN KEY (film_id) REFERENCES film(id),
    FOREIGN KEY (actor_id) REFERENCES actor(id),
    PRIMARY KEY (film_id, actor_id)
);
