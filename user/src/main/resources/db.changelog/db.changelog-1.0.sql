-- liquibase formatted sql
-- changeset vlad-savko:1
create sequence users_seq start 1 increment 1;

-- changeset vlad-savko:2
create table if not exists _user(
    id bigint default nextval('users_seq')not null primary key,
    email varchar(255) not null,
    password varchar(255) not null,
    role integer not null,
    username varchar(255) not null
);

-- changeset vlad-savko:3
INSERT INTO _user (email, password, role, username)
VALUES
    ('user@user.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 0, 'default'),
    ('vladik@specific-group.by', 'a35caf514ffee332947000a264e6e11d5b395f96e1a51c46ea2dc8f0042d3d48', 0, 'vladik'),
    ('admin@admin.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1, 'admin');