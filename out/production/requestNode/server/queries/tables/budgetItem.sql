CREATE TABLE budgetItem(
category VARCHAR(32) NOT NULL,
date VARCHAR(12) NOT NULL,
amount NUMERIC NOT NULL,
id serial NOT NULL,
uid serial REFERENCES account(id),
PRIMARY KEY (id, uid)
);
