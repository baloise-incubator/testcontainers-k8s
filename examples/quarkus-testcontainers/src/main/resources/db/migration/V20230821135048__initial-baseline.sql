CREATE TABLE IF NOT EXISTS book (
    id bigserial PRIMARY KEY,
    title VARCHAR ( 50 ) NOT NULL,
    author VARCHAR ( 50 ) NOT NULL
);
CREATE TABLE IF NOT EXISTS author (
    id bigserial PRIMARY KEY,
    name varchar(50) not null,
    surname varchar(50) not null
);