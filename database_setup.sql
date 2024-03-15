-- Create the database.
create database if not exists scs_sql_injection;

-- Use the created database.
use scs_sql_injection;

-- Create the user table.
create table if not exists user (
    userId int auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    primary key (userId),
);