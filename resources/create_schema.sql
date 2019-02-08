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
    skill_id  integer REFERENCES user_skills (id) ON DELETE CASCADE,
    entry_action   VARCHAR(50),
    occurrence     VARCHAR(100)
);
CREATE TABLE users_scheduler_audit (
	id SERIAL PRIMARY KEY,
	createdAt timestamp,
	status varchar(50),
	body json
);

ALTER TABLE users ALTER COLUMN accesslevel TYPE text[] USING array[accessLevel];
ALTER TABLE users RENAME COLUMN accesslevel TO accesslevels;
ALTER TABLE entries ALTER COLUMN occurrence TYPE timestamp with time zone USING occurrence::timestamp with time zone;
ALTER TABLE tech ADD COLUMN tech_label varchar(255);
ALTER TABLE entries ADD COLUMN info varchar(255);
UPDATE entries SET info = '';
UPDATE tech SET tech_label =
(SELECT tech_name FROM tech AS t2
WHERE t2.id = tech.id);