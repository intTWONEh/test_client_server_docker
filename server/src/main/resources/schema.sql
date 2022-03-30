-- auto-generated definition
create table IF NOT EXISTS MESSAGE
(
    ID        BIGINT not null
        primary key,
    DATE_TIME TIMESTAMP,
    TEXT      VARCHAR(255)
);

