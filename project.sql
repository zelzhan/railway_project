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
                                              first_name varchar(45) not null,
                                              last_name varchar(45) not null,
                                              password varchar(512) not null,
                                              phone varchar(20) not null,
                                              role varchar(20) not null default 'passenger',
                                              unique(login)
);


drop table if exists manager;
create table if not exists manager(
                                      id bigint(20) primary key auto_increment,
                                      foreign key(id) references registered_user(id)
);

drop table if exists regular_employee;
create table if not exists regular_employee(
                                               id bigint(20) primary key auto_increment,
                                               login varchar(255) not null,
                                               supervisor_id bigint(20) not null,
                                               salary bigint(20) not null,
                                               stationN bigint(20) not null,
                                               schedule varchar(255) not null,
                                               foreign key(id) references registered_user(id),
                                               foreign key (supervisor_id) references manager(id),
                                               foreign key(stationN) references station(id)
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
                                       departure_time datetime not null,
                                       arrival_time datetime not null,
                                       availability bigint(150) not null,
                                       foreign key (train_id) references train(id),
                                       foreign key (station_i) references station(id),
                                       foreign key (station_f) references station(id)
);

drop table if exists ticket;
create table if not exists ticket(
                                     id bigint(20) primary key auto_increment,
                                     client_id bigint(20) not null,
                                     train_id bigint(20) not null,
                                     start_station_id bigint(20) not null,
                                     end_station_id bigint(20) not null,
                                     departure_time datetime not null,
                                     arrival_time datetime not null,
                                     ReservStatus varchar(20) not null,
                                     foreign key (client_id) references registered_user(id),
                                     foreign key (train_id) references train(id),
                                     foreign key (start_station_id) references station(id),
                                     foreign key (end_station_id) references station(id)
);

drop table if exists message;
create table if not exists message(
                                      id bigint(20) primary key auto_increment,
                                      manager_id bigint(20) not null,
                                      IssueDate datetime not null,
                                      msg varchar(255) not null,
                                      foreign key (manager_id) references registered_user(id)
);

-- add some data
insert into registered_user (id, login, password, first_name, last_name, phone, role) values (201521960, 'abyl', 'asdfg', 'name', 'surname', '87057128700', 'passenger');
insert into registered_user (id, login, password, first_name, last_name, phone, role) values (201122333, 'keno', 'qwerty', 'Kiano', 'Rift', '87776665544', 'passenger');
-- new mock data
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('john.example@ex.com', 'john123', 'John', 'Example', '86555689009', 'passenger');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('anne.example@ex.com', 'anne123', 'Anne', 'Example', '86555689009', 'passenger');

INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('phill.employee@ex.com', 'phillemployee123', 'Phill', 'Employee', '87675678789', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('rose.employee@ex.com', 'roseemployee123', 'Rose', 'Employee', '89877899898', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('aliya.employee@ex.com', 'aliyaemployee123', 'Aliya', 'Employee', '87655433221', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('viktor.employee@ex.com', 'viktoremployee123', 'Viktor', 'Employee', '89099877889', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('hellen.employee@ex.com', 'hellenemployee123', 'Hellen', 'Employee', '85655445445','agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('gigi.employee@ex.com', 'gigiemployee123', 'Gigi', 'Employee', '87654566776', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('robert.employee@ex.com', 'robertemployee123', 'Robert', 'Employee', '89998887766', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('michael.employee@ex.com', 'michaelemployee123', 'Michael', 'Employee', '89009998880', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('sean.employee@ex.com', 'seanemployee123', 'Sean', 'Jones', '85675675667', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('mark.employee@ex.com', 'markemployee123', 'Mark', 'Employee', '83455433445', 'agent');

INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('ellen.agent@ex.com', 'ellenagent123', 'Ellen', 'Agent', '87776667766', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('richard.agent@ex.com', 'richardagent123', 'Richard', 'Agent', '89998887766', 'agent');

INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('mona.manager@ex.com', 'monamanager123', 'Mona', 'Manager', '85675675667', 'manager');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('mao.manager@ex.com', 'maomanager123', 'Mao', 'Manager', '83455433445', 'manager');

-- deleted passenger table ()

-- manager
INSERT INTO manager (id) VALUES ((SELECT id FROM registered_user WHERE Login = 'mona.manager@ex.com'));
INSERT INTO manager (id) VALUES ((SELECT id FROM registered_user WHERE Login = 'mao.manager@ex.com'));
insert into manager (id) values (201122333);

-- employee
INSERT INTO regular_employee (login, supervisor_id, salary, stationN, schedule, id) VALUES ('sean.employee@ex.com', 15, 100000, 1, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'sean.employee@ex.com'));
INSERT INTO regular_employee (login, supervisor_id, salary, stationN, schedule, id) VALUES ('mark.employee@ex.com', 15, 100000, 2, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'mark.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('phill.employee@ex.com',15, 100000, 3, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'phill.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('rose.employee@ex.com', 15, 100000, 4, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'rose.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('aliya.employee@ex.com', 16,  100000, 5, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'aliya.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('viktor.employee@ex.com', 16, 100000, 6, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'viktor.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('hellen.employee@ex.com', 16, 100000, 7, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'hellen.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('gigi.employee@ex.com', 16, 100000, 8, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'gigi.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('robert.employee@ex.com', 16,  100000, 9, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'robert.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('michael.employee@ex.com', 15, 100000, 10, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'michael.employee@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('ellen.agent@ex.com', 15,  150000, 11, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'ellen.agent@ex.com'));
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id) VALUES ('richard.agent@ex.com', 16,  150000, 12, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'richard.agent@ex.com'));


-- train
insert into train (id, capacity, name1) values (563, 123, 'Tulpar04');
insert into train (id, capacity, name1) values (564, 123, 'Tulpar05');
insert into train (id, capacity, name1) values (565, 123, 'Tulpar06');
insert into train (id, capacity, name1) values (566, 123, 'Tulpar07');
insert into train (id, capacity, name1) values (567, 123, 'Tulpar08');
insert into train (id, capacity, name1) values (568, 123, 'Tulpar09');
insert into train (id, capacity, name1) values (569, 123, 'Tulpar10');
insert into train (id, capacity, name1) values (570, 123, 'Tulpar11');
insert into train (id, capacity, name1) values (571, 123, 'Tulpar12');
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
insert into station (id,name) values (11,'Ustkamenogorsk');
insert into station (id,name) values (12,'Kostanay');
insert into station (id,name) values (13,'Taraz');
insert into station (id,name) values (14,'Pavlodar');
insert into station (id, name) values (15, 'Uralsk');
insert into station (id,name) values (16,'Atyrau');
insert into station (id,name) values (17,'Petropavlovsk');
insert into station (id,name) values (18,'Kokshetau');


-- MESSAGE
insert into message (manager_id, IssueDate, msg) values ((SELECT id FROM registered_user WHERE Login = 'mona.manager@ex.com'), '2038-01-12 00:00:00', 'due to heavy snowfall, the route Astana-Shymkent is canceled');

-- TICKET

insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(201521971, 561, 1, 3, '2038-01-12 00:00:00', '2038-01-19 00:00:00', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(201521971, 561, 1, 3, '2019-01-01 00:00:00', '2019-01-02 00:00:00', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(2, 561, 6, 5, '2019-12-19 17:14:00', '2019-12-20 10:15:07', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(2, 561, 5, 2, '2019-01-10 00:00:00', '2019-01-11 08:14:07', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(2, 5161, 6, 2, '2019-01-10 17:14:00', '2019-01-11 08:14:07', 'Booked');

-- Moskva -> Aktobe -> Astana -> Karaganda -> Shymkent
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (1, 561, 6, 5, '2038-01-19 17:14:00', '2038-01-20 10:15:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (1, 561, 5, 2, '2038-01-20 10:25:07', '2038-01-21 08:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (1, 561, 2, 4, '2038-01-21 08:30:07', '2038-01-21 12:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (1, 561, 4, 3, '2038-01-21 12:24:07', '2038-01-22 18:10:07', 130);

-- Semey -> Almaty -> Shymkent -> Tashkent
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (2, 560, 8, 1, '2038-01-18 11:14:07', '2038-01-19 06:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (2, 560, 1, 3, '2038-01-19 06:32:07', '2038-01-19 23:45:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (2, 560, 3, 7, '2038-01-19 00:00:00', '2038-01-20 07:14:07', 130);

-- MOskva-- Aktau -- Kyzylorda -- Almaty -- Shymkent
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (3, 562, 6, 9, '2038-01-17 12:14:07', '2038-01-18 03:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (3, 562, 9, 10, '2038-01-18 03:20:07', '2038-01-18 23:57:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (3, 562, 10, 1, '2038-01-18 00:10:07', '2038-01-19 06:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (3, 562, 1, 3, '2038-01-19 06:30:07', '2038-01-19 23:14:07', 130);

-- Astana -- Karaganda -- Almaty
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (4, 563, 2, 4, '2038-01-19 14:14:07', '2038-01-19 18:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (4, 563, 4, 1, '2038-01-19 18:25:07', '2038-01-20 06:04:07', 130);

-- Astana-- Pavlodar -- Semey -- UStkamenogorsk
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (5, 564, 4, 14, '2038-01-19 15:14:07', '2038-01-19 23:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (5, 564, 14, 8, '2038-01-19 23:34:07', '2038-01-20 07:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (5, 564, 8, 11, '2038-01-20 07:30:07', '2038-01-20 12:14:07', 130);


-- Shymkent -> Karaganda -> Astana -> Aktobe-> Moskva
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (6, 565, 3, 4, '2038-01-19 06:20:07', '2038-01-20 01:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (6, 565, 4, 2, '2038-01-20 01:30:07', '2038-01-20 04:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (6, 565, 2, 5, '2038-01-20 04:30:07', '2038-01-20 23:48:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (6, 565, 5, 6, '2038-01-21 00:04:07', '2038-01-22 03:14:07', 130);


-- Shymkent-- Almaty -- Kyzylorda -- Aktau -- Moskva
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (8, 567, 3, 1, '2038-01-19 12:34:07', '2038-01-19 19:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (8, 567, 1, 10, '2038-01-19 19:32:07', '2038-01-19 23:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (8, 567, 10, 9, '2038-01-19 23:29:07', '2038-01-20 10:14:07', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (8, 567, 9, 6, '2038-01-20 10:30:07', '2038-01-21 03:14:07', 130);
