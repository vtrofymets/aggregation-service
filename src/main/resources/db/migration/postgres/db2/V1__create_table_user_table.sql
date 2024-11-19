create table if not exists user_table
(
    id      bigserial,
    login   varchar(128),
    name    varchar(64),
    surname varchar(64),
    primary key (id)
);