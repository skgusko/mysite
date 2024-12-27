show tables;
select * from user;


select * from board;
desc board;

-- BoardDao's findAll (페이징 기능 추가 필요)
select b.id, b.title, u.name, b.hit, date_format(b.reg_date, '%Y-%m-%d %h:%i:%s'), depth
from board b join user u 
on b.user_id=u.id
order by g_no desc, o_no asc;

insert into board values(null, '첫번째 글', '첫번째 내용', 0, now(), 1, 1, 0, 2);
insert into board values(null, '모먹지?', '점메추', 0, now(), 2, 1, 0, 3);
desc board;

select id, name, contents, date_format(reg_date, '%Y-%m-%d %h:%i:%s') from guestbook order by reg_date desc;

-- BoardDao's findById
select title, contents, g_no, o_no, depth, user_id from board where id=?;
select max(g_no) from board;
select * from user;

update board set hit=hit+1 where id=38;
select * from board;
select count(*) from board;
-- update board set o_no=o_no+1, depth=?(전달받은놈)+1 where g_no=?(전달받은놈의oNo) and o_no>=?+1(전달받은놈의oNo);
select g_no, o_no, depth from board where id=?;

update board set title=?, contents=? where id=?;

select b.id, b.title, u.name, b.hit, date_format(b.reg_date, '%Y-%m-%d %h:%i:%s'), b.depth, u.id 
from board b join user u 
on b.user_id=u.id
where title like '%ㅎㅇ%' or contents like '%ㅎㅇ%'
order by g_no desc, o_no asc;

select count(*) from board where title like '%ㅎㅇ%' or contents like '%ㅎㅇ%';