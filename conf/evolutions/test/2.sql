# --- !Ups
INSERT INTO users VALUES (1, 'Tanya', 'Moldovan', 'tanya.moldovan@lunatech.com');
INSERT INTO users VALUES (2, 'Scooby', 'Doo', 'tanya.moldovan@gmail.com');
INSERT INTO users VALUES (3, 'Albus', 'Dumbledore', 'albus.dumbledore@hogwarts.com');
INSERT INTO users VALUES (4, 'Severus', 'Snape', 'severus.snape@hogwarts.com');

INSERT INTO tech VALUES (1, 'Scala', 'LANGUAGE');
INSERT INTO tech VALUES (2, 'Python', 'LANGUAGE');
INSERT INTO tech VALUES (3, 'OOP', 'CONCEPTUAL');
INSERT INTO tech VALUES (4, 'Defense Against The Dark Arts', 'CONCEPTUAL');
INSERT INTO tech VALUES (5, 'Dark Arts', 'CONCEPTUAL');
INSERT INTO tech VALUES (6, 'Potions', 'CONCEPTUAL');


INSERT INTO user_skills VALUES (1, 1, 1, 'COMFORTABLE');
INSERT INTO user_skills VALUES (2, 4, 5, 'CAN_TEACH');
INSERT INTO user_skills VALUES (3, 4, 6, 'CAN_TEACH');
INSERT INTO user_skills VALUES (4, 4, 4, 'CAN_TEACH');
INSERT INTO user_skills VALUES (5, 1, 3, 'COMFORTABLE');
INSERT INTO user_skills VALUES (6, 1, 2, 'FOSSIL');

# --- !Downs
DELETE from users;