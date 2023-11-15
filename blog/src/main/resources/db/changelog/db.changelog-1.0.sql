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

-- changeset vmamatsiuk:3
INSERT INTO posts (user_id, title, text, creation_date, modification_date)
VALUES
    (1, 'First Post', 'This is the content of the first post.', '2023-11-15 08:00:00', '2023-11-15 08:00:00'),
    (2, 'Second Post', 'Content of the second post goes here.', '2023-11-14 12:30:00', '2023-11-14 12:30:00'),
    (3, 'Interesting Post', 'Content of the interesting post goes here.', '2023-11-14 12:30:00', '2023-11-14 12:30:00'),
    (1, 'Cool Post', 'Content of the cool post goes here.', '2023-11-14 12:30:00', '2023-11-14 12:30:00'),
    (2, 'Boring Post', 'Content of the boring post goes here.', '2023-11-14 12:30:00', '2023-11-14 12:30:00'),
    (1, 'Another Post', 'More content for another post.', '2023-11-13 18:45:00', '2023-11-13 18:45:00');