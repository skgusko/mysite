desc user;

alter table user add column role enum("USER", "ADMIN") default "USER" NOT NULL;

insert into user values(null, '관리자', 'admin@mysite.com', '1234', 'female', sysdate(), 'ADMIN');

select * from user;