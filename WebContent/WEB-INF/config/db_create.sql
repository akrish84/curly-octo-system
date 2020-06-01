create table users(
	id INT UNSIGNED AUTO_INCREMENT not null primary key,
	email varchar(52) not null unique, 
	password varchar(18) not null,
	first_name varchar(46) not null ,
	last_name varchar(46)
);

create table aps ( 
	id int unsigned auto_increment not null primary key, 
	name varchar(52) not null
);

create table jobs ( 
	id INT UNSIGNED AUTO_INCREMENT not null primary key, 
	company_name varchar(52) not null, 
	job_title varchar(100),
	job_description text,
	aps_id int unsigned, 
	FOREIGN KEY (aps_id) REFERENCES aps(id)
);

create table default_status ( 
	status varchar(52), 
	`rank` smallint unique 
);

create table status ( 
	id int unsigned auto_increment not null primary key, 
	status varchar(52) not null, 
	user_id int unsigned not null, 
	`rank` smallint, 
	foreign key (user_id) references users(id)
);

create table company_suggestions ( 
	id int unsigned auto_increment not null primary key, 
	name varchar(52) not null
);

create table aps_suggestions(
	id int unsigned auto_increment not null primary key, 
	name varchar(52) not null
);

create table job_title_suggestions ( 
	id int unsigned not null, 
	job_title varchar(100) not null 
);

create table resumes ( 
	id int unsigned auto_increment not null primary key, 
	hash varchar(64) not null, 
	file_path varchar(100) not null, 
	file_name varchar(50) not null
);

create table applications ( 
	id int unsigned auto_increment not null primary key, 
	job_id int unsigned not null, 
	status_id int unsigned not null, 
	applied_date date not null, 
	user_id int unsigned not null, 
	resume_id int unsigned , 
	FOREIGN KEY (job_id) references jobs(id), 
	FOREIGN KEY (status_id) references status(id), 
	FOREIGN KEY (user_id) references users(id), 
	FOREIGN KEY (resume_id) references resumes(id)
);

create table application_rank ( 
	application_id int unsigned not null primary key, 
	`rank` smallint not null 
);

