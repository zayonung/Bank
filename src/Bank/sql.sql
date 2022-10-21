
create table userinfo(
user_id VARCHAR(100) NOT NULL,
user_pw VARCHAR(100) NOT NULL,
user_name VARCHAR(100) NOT NULL,
user_time timestamp not null,
PRIMARY KEY(USER_ID)
)charset = utf8; /* 한글 입력 */

select * from userinfo;
drop table userinfo;
insert into userinfo values("admin", "1", "관리자",now());
insert into userinfo values("a", "1", "1번유저",now());
insert into userinfo values("b", "1", "2번유저",now());
insert into userinfo values("c", "1", "3번유저",now());
insert into userinfo values("d", "1", "4번유저",now());



CREATE TABLE account
(
accountNo varchar(50) not null,
ac_pw int,
user_id varchar(100),
ac_name varchar(100),
balance int,
PRIMARY KEY(accountNo),
foreign key (user_id) references userinfo(user_id)
)charset = utf8;

select * from account;
drop table account;

CREATE TABLE TRANSACTION
(
accountNo varchar(50) not null,
reAccountNo varchar(50),
transType varchar(50),
amount int,
lastbalance int,
transactionTime timestamp not null default current_timestamp,
foreign key (accountNo) references account(accountNo)
)charset = utf8;

select * from TRANSACTION;
drop table transaction;