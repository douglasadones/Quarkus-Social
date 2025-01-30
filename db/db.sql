create table quarkus-social;

create table users (
	id bigserial not null primary key,
	name varchar(100),
	age integer not null
);