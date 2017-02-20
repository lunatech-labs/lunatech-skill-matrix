# --- !Ups
CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	firstName varchar(255),
	lastName varchar(255),
	email varchar(255)
);
CREATE TABLE IF NOT EXISTS user_auth (
	id SERIAL PRIMARY KEY,
	user_id integer REFERENCES users (id),
	user_key varchar(255),
	secret varchar(255)
);
-- CREATE TYPE skill_type AS ENUM ('LANGUAGE', 'LIBRARY', 'FRAMEWORK', 'CONCEPTUAL');
CREATE TABLE IF NOT EXISTS tech(
	id SERIAL PRIMARY KEY,
	tech_name varchar(255),
	tech_type varchar(255)
);
--CREATE TYPE skill_level AS ENUM ('CAN_TEACH', 'COMFORTABLE', 'DABBED', 'FOSSIL');
CREATE TABLE IF NOT EXISTS user_skills (
	id SERIAL PRIMARY KEY,
	user_id integer REFERENCES users (id),
	tech_id integer REFERENCES tech (id),
	skill_level varchar(255)
);

# --- !Downs
DROP TABLE IF EXISTS user_skills;
DROP TABLE IF EXISTS tech;
DROP TABLE IF EXISTS user_auth;
DROP TABLE IF EXISTS users;