-- liquibase formatted sql

-- changeset vmamatsiuk:1
create sequence posts_seq start 1 increment 1;

-- changeset vmamatsiuk:2
create table if not exists posts(
    post_id bigint default nextval('posts_seq') not null,
    user_id bigint not null,
    title varchar(100) not null,
    text varchar(2000) not null,
    creation_date timestamp not null,
    modification_date timestamp not null,

    constraint posts_pk primary key(post_id)
);