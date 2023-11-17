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
INSERT INTO _user (id, email, password, role, username)
VALUES
    (1, 'user@user.com', '5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8', 0, 'default'),
    (2, 'vladik@specific-group.by', 'A35CAF514FFEE332947000A264E6E11D5B395F96E1A51C46EA2DC8F0042D3D48', 0, 'vladik'),
    (3, 'admin@admin.com', '8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918', 1, 'admin');