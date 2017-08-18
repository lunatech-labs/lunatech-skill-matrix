CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	firstName varchar(255),
	lastName varchar(255),
	email varchar(255),
	accesslevel varchar(50),
	status varchar(30)
);
CREATE TABLE IF NOT EXISTS tech(
	id SERIAL PRIMARY KEY,
	tech_name varchar(255),
	tech_type varchar(255)
);
CREATE TABLE IF NOT EXISTS user_skills (
	id SERIAL PRIMARY KEY,
	user_id integer REFERENCES users (id) ON DELETE CASCADE,
	tech_id integer REFERENCES tech (id) ON DELETE CASCADE,
	skill_level varchar(255),
	status varchar(30)
);
CREATE TABLE entries(
    id SERIAL PRIMARY KEY,
    user_id   integer REFERENCES users (id) ON DELETE CASCADE,
    skill_id  integer REFERENCES tech (id) ON DELETE CASCADE,
    entry_action   VARCHAR(50),
    occurrence     VARCHAR(100)
);
