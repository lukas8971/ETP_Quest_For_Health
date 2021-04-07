create database if not exists Quest_For_Health;

use Quest_For_Health;

drop table if exists user;
create table user (
                      Id INT AUTO_INCREMENT primary key,
                      Firstname varchar (255) not null,
                      Lastname varchar (255) not null
);