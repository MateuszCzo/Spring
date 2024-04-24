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

INSERT INTO rating (description, rating, status, film_id) VALUES
    ('Great movie!', 5, 'CONFIRMED', 1),
    ('One of my favorites.', 4, 'NOT_CONFIRMED', 2),
    ('Amazing performance by the cast.', 5, 'CONFIRMED', 3),
    ('Could have been better.', 3, 'NOT_CONFIRMED', 4),
    ('Classic!', 5, 'CONFIRMED', 5),
    ('Heath Ledger was outstanding.', 5, 'CONFIRMED', 6),
    ('Overrated.', 2, 'NOT_CONFIRMED', 7),
    ('A powerful portrayal of history.', 4, 'CONFIRMED', 8),
    ('Epic finale!', 5, 'CONFIRMED', 9),
    ('Mind-bending!', 4, 'NOT_CONFIRMED', 10);

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
