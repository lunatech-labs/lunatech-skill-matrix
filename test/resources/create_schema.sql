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
CREATE TABLE IF NOT EXISTS tech(
	id SERIAL PRIMARY KEY,
	tech_name varchar(255),
	tech_type varchar(255)
);
CREATE TABLE IF NOT EXISTS user_skills (
	id SERIAL PRIMARY KEY,
	user_id integer REFERENCES users (id),
	tech_id integer REFERENCES tech (id),
	skill_level varchar(255)
);