insert into pf.employee(id, name, email, phone, position, is_fired)
values ('fb341a48-b23a-4ad4-9488-d094ab8766c4', 'Igor', 'i@i.ru', '79888888888', 'DIRECTOR', false),
       ('c631da58-8ae6-4a8d-bc6a-107c42a2b598', 'Oxa', 'o@i.ru', '79888888800', 'DIRECTOR', false);

insert into pf.direction (id, lead_id, name)
values ('211ed887-adc4-41bb-a8c4-e41393bd8e69', 1, 'Nat'),
       ('caaad756-f11a-4635-898d-2861071ec38d', 1, 'Vasya');

insert into pf.project (id, name, project_lead_id, direction_id, description, create_date, status)
values ('ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f', 'One', 1, 1, 'message 1', '2021-06-09T11:49:03.839234Z', 'UNAPPROVED'),
       ('4e7efeef-553f-4996-bc03-1c0925d56946', 'Three', 2, 2, 'message ', '2021-07-09T11:49:03.839234Z', 'ON_PL_PLANNING');