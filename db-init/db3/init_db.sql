create table if not exists customers
(
    id         bigserial,
    email      varchar(64),
    first_name varchar(64),
    last_name  varchar(64),
    primary key (id)
);

insert into customers(email, first_name, last_name)
values ('aaa@aa.com', 'first_name1', 'last_name1'),
       ('bbb@bb.com', 'name2', 'surname2'),
       ('123@22.com', 'first_name3', 'last_name3');