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
                                              role varchar(20) not null,
                                              unique(login)
);




drop table if exists regular_employee;
create table if not exists regular_employee(
                                               id bigint(20) primary key auto_increment,
                                               login varchar(255) not null,
                                               supervisor_id bigint(20),
                                               salary bigint(20) not null,
                                               stationN bigint(20) not null,
                                               schedule varchar(255) not null,
                                               firstDay varchar(30),
                                               foreign key(id) references registered_user(id),
                                               foreign key (supervisor_id) references registered_user(id),
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
drop table if exists salaryHistory;
create table if not exists salaryHistory(
                                            id bigint(20) primary key auto_increment,
                                            employee_id bigint(20) not null,
                                            PayrollDate datetime not null,
                                            Salary bigint(20) not null,
                                            foreign key (employee_id) references registered_user(id)
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
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ( 'sean.employee@ex.com', 'seanemployee123', 'Sean', 'Jones', '85675675667', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('mark.employee@ex.com', 'markemployee123', 'Mark', 'Employee', '83455433445', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('ellen.agent@ex.com', 'ellenagent123', 'Ellen', 'Agent', '87776667766', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('richard.agent@ex.com', 'richardagent123', 'Richard', 'Agent', '89998887766', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('mona.manager@ex.com', 'monamanager123', 'Mona', 'Manager', '85675675667', 'manager');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('mao.manager@ex.com', 'maomanager123', 'Mao', 'Manager', '83455433445', 'manager');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('william.riker@ex.com', '123', 'William', 'Riker', '89998887766', 'manager');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('john.smith@ex.com', '123', 'John', 'Smith', '85675675667', 'agent');
INSERT INTO registered_user (login, password, first_name,last_name, phone, role) VALUES ('beverly.crusher@ex.com', '123', 'Beverly', 'Crusher', '83455433445', 'passenger');


-- employee
INSERT INTO regular_employee (login, supervisor_id, salary, stationN, schedule, id, firstDay) VALUES ('sean.employee@ex.com', 201122333, 100000, 1, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'sean.employee@ex.com'),'January 1');
INSERT INTO regular_employee (login, supervisor_id, salary, stationN, schedule, id, firstDay) VALUES ('mark.employee@ex.com', 201122333, 100000, 2, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'mark.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('phill.employee@ex.com',201122333, 100000, 3, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'phill.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id, firstDay) VALUES ('rose.employee@ex.com', 201521975, 100000, 4, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'rose.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('aliya.employee@ex.com', 201521975,  100000, 5, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'aliya.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id, firstDay) VALUES ('viktor.employee@ex.com', 201521975, 100000, 6, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'viktor.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay ) VALUES ('hellen.employee@ex.com', 201521975, 100000, 7, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'hellen.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('gigi.employee@ex.com', 201521975, 100000, 8, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'gigi.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('robert.employee@ex.com', 201521976,  100000, 9, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'robert.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('michael.employee@ex.com', 201521976, 100000, 10, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'michael.employee@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('ellen.agent@ex.com', 201521976,  150000, 11, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'ellen.agent@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('richard.agent@ex.com', 201521976,  150000, 12, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'richard.agent@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('mona.manager@ex.com',null, 200000, 13, 'M, T, W, R, F (9.00-18.00)',  (SELECT id FROM registered_user WHERE Login = 'mona.manager@ex.com'),'January 1');
INSERT INTO regular_employee (Login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('mao.manager@ex.com',null, 200000, 14, 'M, T, W, R, F (9.00-18.00)',  (SELECT id FROM registered_user WHERE Login = 'mao.manager@ex.com'),'January 1');
INSERT INTO regular_employee (login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('william.riker@ex.com', null,  1000, 15, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'william.riker@ex.com'),'January 1') ;
INSERT INTO regular_employee (login, supervisor_id, salary, stationN, schedule, id,firstDay) VALUES ('john.smith@ex.com', null, 1000, 15, 'M, T, W, R, F (9.00-18.00)', (SELECT id FROM registered_user WHERE Login = 'john.smith@ex.com'),'January 1');

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
insert into train (id, capacity, name1) values (572, 200, 'Tulpar13');
insert into train (id, capacity, name1) values (573, 123, 'Tulpar14');
insert into train (id, capacity, name1) values (574, 123, 'Tulpar15');

insert into station (id,name) values (1,'Almaty');
insert into station (id,name) values (2,'Astana');
insert into station (id,name) values (3,'Shymkent');
insert into station (id,name) values (4,'Karaganda');
insert into station (id,name) values (5,'Aktobe');
insert into station (id,name) values (6,'Moscow');
insert into station (id,name) values (7,'Tashkent');
insert into station (id,name) values (8,'Semipalatinsk');
insert into station (id,name) values (9,'Aktau');
insert into station (id, name) values (10, 'Kyzylorda');
insert into station (id,name) values (11,'Ust-kamenogorsk');
insert into station (id,name) values (12,'Kostanay');
insert into station (id,name) values (13,'Taraz');
insert into station (id,name) values (14,'Pavlodar');
insert into station (id, name) values (15, 'Uralsk');
insert into station (id,name) values (16,'Atyrau');
insert into station (id,name) values (17,'Petropavl');
insert into station (id,name) values (18,'Kokshetau');
insert into station (id,name) values (19,'Nur-Sultan');


-- MESSAGE
insert into message (manager_id, IssueDate, msg) values ((SELECT id FROM registered_user WHERE Login = 'mona.manager@ex.com'), '2038-01-12 00:00:00', 'due to heavy snowfall, the route Astana-Shymkent is canceled');

-- TICKET

insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(201521971, 561, 1, 3, '2038-01-12 00:00:00', '2038-01-19 00:00:00', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(201521971, 561, 1, 3, '2019-01-01 00:00:00', '2019-01-02 00:00:00', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(201521971, 561, 6, 5, '2019-12-19 17:14:00', '2019-12-20 10:15:07', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(201521971, 561, 5, 2, '2019-01-10 00:00:00', '2019-01-11 08:14:07', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(201521971, 561, 6, 2, '2019-01-10 17:14:00', '2019-01-11 08:14:07', 'Booked');
insert into ticket (client_id, train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus) values(201521979, 571, 18, 12, '2019-09-01 12:00:00', '2019-09-01 15:00:00', 'Booked');



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


-- Nur-sultan -- Kokshetau -- Petropavl (dates: 22.11)
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (9, 569, 19, 18, '2019-11-22 08:00:00', '2019-11-22 11:00:00', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (9, 569, 18, 17, '2019-11-22 11:00:00', '2019-11-22 16:00:00', 130);

-- Petropavl -- Kokshetau -- NurSultan (dates: 22.11)
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (10, 570, 17, 18, '2019-11-22 17:00:00', '2019-11-22 22:00:00', 130);
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (10, 570, 18, 19, '2019-11-22 22:00:00', '2019-11-23 01:00:00', 130);


-- Kokshetau -- Kostanay (dates: 22.11, 23.11, 24.11, 25.11)
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (11, 571, 18, 12, '2019-11-22 12:00:00', '2019-11-22 15:00:00', 130);

-- Kostanay -- Kokshetau (dates: 22.11, 23.11, 24.11, 25.11)
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (12, 572, 12, 18, '2019-11-22 16:00:00', '2019-11-22 19:00:00', 130);

-- NurSultan -- Pavlodar (dates: 22.11, 23.11, 24.11, 25.11)
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (13, 573, 19, 14, '2019-11-22 12:00:00', '2019-11-22 15:00:00', 130);

-- Pavlodar -- Nursultan (dates: 22.11, 23.11, 24.11, 25.11)
insert into schedule (route_id, train_id, station_i, station_f, departure_time, arrival_time, availability) values (14, 574, 14, 19, '2019-11-22 16:00:00', '2019-11-22 19:00:00', 130);



-- SALARY TABLE MOCK DATA

insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-01-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-01-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-02-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-02-28 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-03-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-03-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-04-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-04-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-05-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-05-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-06-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-06-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-07-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-07-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-08-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-08-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-09-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-09-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-10-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-10-28 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'william.riker@ex.com'), '2019-11-15 10:00:00', '1000');

insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-01-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-01-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-02-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-02-28 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-03-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-03-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-04-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-04-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-05-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-05-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-06-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-06-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-07-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-07-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-08-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-08-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-09-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-09-30 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-10-15 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-10-28 10:00:00', '1000');
insert into salaryHistory(employee_id, payrollDate, salary) values((select id from registered_user where login = 'john.smith@ex.com'), '2019-11-15 10:00:00', '1000');
