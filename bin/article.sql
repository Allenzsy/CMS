drop table if exists t_article;
create table t_article(
	id integer not null auto_increment,
	title varchar(200),
	content longtext,
	source varchar(255),
	keyword varchar(255),
	intro varchar(2000),
	type varchar(50),
	author varchar(100),
	recommend boolean,
	headline boolean,
	leaveNumber integer,
	clickNumber integer,
	adminId integer,
	topicId integer,
	createTime datetime,
	updateTime datetime,
	deployTime datetime,
	primary key(id)
);