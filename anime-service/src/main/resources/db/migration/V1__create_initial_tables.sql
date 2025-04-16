create table anime_service.anime
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
);

create table anime_service.producer
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  not null,
    name       varchar(255) null
);

