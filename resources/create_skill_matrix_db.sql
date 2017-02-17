CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	firstName varchar(255),
	lastName varchar(255),
	email varchar(255)
);
CREATE TABLE user_auth (
	id SERIAL PRIMARY KEY,
	user_id integer REFERENCES users (id),
	user_key varchar(255),
	secret varchar(255)
);
-- CREATE TYPE skill_type AS ENUM ('LANGUAGE', 'LIBRARY', 'FRAMEWORK', 'CONCEPTUAL');
CREATE TABLE tech(
	id SERIAL PRIMARY KEY,
	tech_name varchar(255),
	tech_type varchar(255)
);
--CREATE TYPE skill_level AS ENUM ('CAN_TEACH', 'COMFORTABLE', 'DABBED', 'FOSSIL');
CREATE TABLE user_skills (
	id SERIAL PRIMARY KEY,
	user_id integer REFERENCES users (id),
	tech_id integer REFERENCES tech (id),
	skill_level varchar(255)
)
