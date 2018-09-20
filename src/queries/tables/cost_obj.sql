CREATE TABLE cost_obj(
name VARCHAR(50) NOT NULL,
month VARCHAR(3) NOT NULL,
year VARCHAR(4) NOT NULL,
cost_obj_id serial NOT NULL,
cost NUMERIC,
user_id serial REFERENCES account(user_id),
PRIMARY KEY (cost_obj_id, user_id)
);
