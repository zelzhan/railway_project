SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

drop database if exists javabase;
create database if not exists javabase;

use javabase;

drop table if exists registered_user;
create table if not exists registered_user(
	id bigint(20) primary key auto_increment,
	login varchar(255) not null,
	password varchar(512) not null,
	phone bigint(20) not null,
	unique(login)
);

drop table if exists passenger;
create table if not exists passenger(
	id bigint(20) primary key auto_increment,
	reservation_status tinyint(1) not null,
	foreign key (id) references registered_user(id)
);

drop table if exists manager;
create table if not exists manager(
	id bigint(20) primary key auto_increment,
	foreign key(id) references registered_user(id)
);

drop table if exists regular_employee;
create table if not exists regular_employee(
	id bigint(20) primary key auto_increment,
    supervisor_id bigint(20) not null,
	salary bigint(20) not null,
    schedule varchar(255) not null,
	foreign key(id) references registered_user(id),
    foreign key (supervisor_id) references manager(id)
);

drop table if exists agent;
create table if not exists agent(
	id bigint(20) primary key auto_increment,
	foreign key(id) references regular_employee(id)
);

drop table if exists train;
create table if not exists train(
	id bigint(20) primary key auto_increment,
	capacity int(20) not null,
    name1 varchar(255) not null,
	unique(name1)
);

drop table if exists station;
create table if not exists station(
	id bigint(20) primary key auto_increment,
	name varchar(255) not null,
	unique(name)
);

drop table if exists schedule;
create table if not exists schedule(
	id bigint(20) primary key auto_increment,
    route_id bigint(20) not null,
	train_id bigint(20) not null,
	station_i bigint(20) not null,
    station_f bigint(20) not null,
    departure_time date not null,
	arrival_time date not null,
	exact_timei varchar(255) not null,
	exact_timef varchar(255) not null,
	foreign key (train_id) references train(id),
	foreign key (station_i) references station(id),
    foreign key (station_f) references station(id)
);
--
-- drop table if exists ticket;
-- create table if not exists ticket(
-- 	id bigint(20) primary key auto_increment,
-- 	client_id bigint(20) not null,
--     train_id bigint(20) not null,
-- 	start_station_id bigint(20) not null,
-- 	end_station_id bigint(20) not null,
-- 	departure_time timestamp not null,
-- 	arrival_time timestamp not null,
-- 	availability tinyint(1) not null,
-- 	foreign key (client_id) references registered_user(id),
-- 	foreign key (train_id) references train(id),
-- 	foreign key (start_station_id) references station(id),
-- 	foreign key (end_station_id) references station(id)
-- );

-- add some data
insert into registered_user (id, login, password, phone) values (201521960, 'abyl', 'asdfg', 87057128700);
insert into registered_user (id, login, password, phone) values (201122333, 'keno', 'qwerty', 87776665544);
insert into manager (id) values (201122333);

insert into train (id, capacity, name1) values (560, 200, 'Tulpar01');
insert into train (id, capacity, name1) values (561, 123, 'Tulpar02');
insert into train (id, capacity, name1) values (562, 123, 'Tulpar03');

insert into station (id,name) values (1,'Almaty');
insert into station (id,name) values (2,'Astana');
insert into station (id,name) values (3,'Shymkent');
insert into station (id,name) values (4,'Karaganda');
insert into station (id,name) values (5,'Aktobe');

insert into station (id,name) values (6,'Moskva');
insert into station (id,name) values (7,'Tashkent');
insert into station (id,name) values (8,'Semey');
insert into station (id,name) values (9,'Aktau');
insert into station (id, name) values (10, 'Kyzylorda');

-- Moskva -> Aktobe -> Astana -> Karaganda -> Shymkent
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (1,561, 6, 5, '2038-01-12', '2038-01-19 ', '03:14:07', '03:14:07');
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (1, 561, 5, 2, '2038-01-12 ', '2038-01-19 ', '03:14:07', '03:14:07');
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (1, 561, 2, 4, '2038-01-12 ', '2038-01-19 ', '03:14:07', '03:14:07');
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (1, 561, 4, 3, '2038-01-12 ', '2038-01-19 ', '03:14:07', '03:14:07');

-- Semey -> Almaty -> Shymkent -> Tashkent
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (2, 560, 8, 1, '2038-01-19 ', '2038-01-19', '03:14:07', '03:14:07');
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (2, 560, 1, 3, '2038-01-19', '2038-01-19 ', '03:14:07', '03:14:07');
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (2, 560, 3, 7, '2038-01-19 ', '2038-01-19', '03:14:07', '03:14:07');

-- MOskva-- Aktau -- Kyzylorda -- Almaty -- Shymkent
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (3, 562, 6, 9, '2038-01-19 ', '2038-01-19 ', '03:14:07', '03:14:07');
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (3, 562, 9, 10, '2038-01-19 ', '2038-01-19', '03:14:07', '03:14:07');
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (3, 562, 10, 1, '2038-01-19 ', '2038-01-19', '03:14:07', '03:14:07');
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, exact_timei, exact_timef) values (2, 562, 1, 3, '2038-01-19 ', '2038-01-19 ', '03:14:07', '03:14:07');