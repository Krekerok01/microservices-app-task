-- liquibase formatted sql

-- changeset vmamatsiuk:1
create sequence subscriptions_seq start 1 increment 1;

-- changeset vmamatsiuk:2
create table if not exists subscriptions(
    subscription_id bigint default nextval('subscriptions_seq') not null,
    user_subscriber_id bigint not null,
    user_publisher_id bigint not null,

    constraint subscriptions_pk primary key(subscription_id)
);

-- changeset vmamatsiuk:3
INSERT INTO subscriptions (user_subscriber_id, user_publisher_id)
VALUES
    (1, 2),
    (2, 3),
    (1, 3);