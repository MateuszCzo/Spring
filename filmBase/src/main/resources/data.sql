INSERT INTO actor(firstname, lastname) VALUES
    ('John', 'Doe'),
    ('Jane', 'Smith'),
    ('Michael', 'Johnson'),
    ('Emily', 'Brown'),
    ('William', 'Davis'),
    ('Emma', 'Wilson'),
    ('James', 'Taylor'),
    ('Olivia', 'Martinez'),
    ('Benjamin', 'Anderson'),
    ('Sophia', 'Harris');

INSERT INTO film(title, description, status) VALUES
   ('The Matrix', 'A computer hacker learns about the true nature of reality and his role in the war against its controllers.', 'AFTER_PREMIERE'),
   ('Inception', 'A thief who enters the dreams of others to steal secrets from their subconscious.', 'AFTER_PREMIERE'),
   ('The Shawshank Redemption', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', 'BEFORE_PREMIERE'),
   ('Forrest Gump', 'The presidencies of Kennedy and Johnson, the events of Vietnam, Watergate, and other historical events unfold from the perspective of an Alabama man with an IQ of 75, whose only desire is to be reunited with his childhood sweetheart.', 'AFTER_PREMIERE'),
   ('The Godfather', 'An organized crime dynasty''s aging patriarch transfers control of his clandestine empire to his reluctant son.', 'BEFORE_PREMIERE'),
   ('The Dark Knight', 'When the menace known as The Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham.', 'AFTER_PREMIERE'),
   ('Pulp Fiction', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.', 'BEFORE_PREMIERE'),
   ('Schindler''s List', 'In German-occupied Poland during World War II, Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazi Germans.', 'BEFORE_PREMIERE'),
   ('The Lord of the Rings: The Return of the King', 'Gandalf and Aragorn lead the World of Men against Sauron''s army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.', 'AFTER_PREMIERE'),
   ('Fight Club', 'An insomniac office worker and a devil-may-care soapmaker form an underground fight club that evolves into something much, much more.', 'BEFORE_PREMIERE');

-- users for tests
INSERT INTO _user (username, password, account_non_expired, account_non_locked, credentials_non_expired, enabled, role) VALUES
    ('user', '$2a$10$X.zK4um8oeQQJw0zmXyDXeq5X3KJuBXYanObb0EjHSfFizM0XkrmC', 1, 1, 1, 1, 'USER'),
    ('admin', '$2a$10$X.zK4um8oeQQJw0zmXyDXeq5X3KJuBXYanObb0EjHSfFizM0XkrmC', 1, 1, 1, 1, 'ADMIN');

INSERT INTO rating (description, rating, status, film_id, user_id) VALUES
    ('Great movie!', 5, 'CONFIRMED', 1, 1),
    ('One of my favorites.', 4, 'NOT_CONFIRMED', 2, 1),
    ('Amazing performance by the cast.', 5, 'CONFIRMED', 3, 1),
    ('Could have been better.', 3, 'NOT_CONFIRMED', 4, 1),
    ('Classic!', 5, 'CONFIRMED', 5, 1),
    ('Heath Ledger was outstanding.', 5, 'CONFIRMED', 6, 1),
    ('Overrated.', 2, 'NOT_CONFIRMED', 7, 1),
    ('A powerful portrayal of history.', 4, 'CONFIRMED', 8, 1),
    ('Epic finale!', 5, 'CONFIRMED', 9, 1),
    ('Mind-bending!', 4, 'NOT_CONFIRMED', 10, 1);

INSERT INTO film_actor (film_id, actor_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 6),
    (7, 7),
    (8, 8),
    (9, 9),
    (10, 10);
