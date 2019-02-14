CREATE TABLE transactionalItem(
transaction VARCHAR(64) NOT NULL,
date VARCHAR(12) NOT NULL,
amount NUMERIC NOT NULL,
description VARCHAR(256) NOT NULL,
paymentFrequency INTEGER NOT NULL,
category VARCHAR(32),
id serial NOT NULL,
uid serial REFERENCES account(id),
PRIMARY KEY (id, uid)
);
