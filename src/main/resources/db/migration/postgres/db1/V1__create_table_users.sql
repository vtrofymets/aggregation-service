create table if not exists users (
    user_id uuid not null,
    login varchar(128),
    first_name varchar(64),
    last_name varchar(64),
    primary key (user_id)
);


