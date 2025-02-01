create database quarkus-social;

create table users (
	id bigserial not null primary key,
	name varchar(100),
	age integer not null
);

-- note que o user_id é bigint e não bigserial 
-- pois este não será gerado automaticamente.
create table posts (
	id bigserial not null primary key,
	post_text varchar(150) not null,
	dataTime timestamp not null,
	user_id bigint not null references Users(id)
);