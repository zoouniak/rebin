delete
from member
where id > 0;
delete
from product
where id > 0;
delete
from time_slot
where id > 0;
delete
from reservation
where id > 0;


insert into member(id, login_id, name, email, nickname, phone, provider, created_at, modified_at)
values (1, 1, '오주은', 'jueun1025@naver.com', '닉네임', '010-0000-0000', 'KAKAO', '2024-12-30', '2024-12-30');

insert into product( id, additional_fee, created_at, modified_at, summary, description, name, thumbnail
                   , deleted, deposit, price)
values (1, 10000, '2024-12-23', '2024-12-23', '아이의 특별한 순간을 사진으로 간직하세요', 'description', '키즈 스냅', 'thumbnail_url', 0,
        10000, 70000);

insert into time_slot(id, date, is_available, start_time)
values (1, '2024-12-31', 1, '18:00');
insert into time_slot(id, date, is_available, start_time)
values (2, '2025-01-05', 1, '18:00');
insert into reservation
( id,is_agree_upload, people_cnt, price, created_at, member_id, modified_at
, product_id, time_slot_id, code, notes, status, shoot_date, is_agree_privacy_policy
, payer_name, payment_date, can_change)
values ( 100,1, 2, 70000, '2024-12-21', 1, '2024-12-21'
       , 1, 2, 'RE20241221KGD', '예쁘게 찍어주셔요', 'CONFIRM_REQUESTED', '2024-12-31', true, '박연기', '2024-12-31', 1);

