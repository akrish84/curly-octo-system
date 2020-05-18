create table users(
id bigint not null primary key,
email varchar(52) not null unique, 
password varchar(18) not null,
first_name varchar(46) not null ,
last_name varchar(46)
);

