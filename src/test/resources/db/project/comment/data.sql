insert into pf.employee(id, name, email, phone, position)
values ('336dfae9-0980-4867-9e7c-06a39a0212ef', 'Vova', '1_1_super@email.com', '2-2-2-2-2', 'PROJECT_LEAD'),
       ('fb341a48-b23a-4ad4-9488-d094ab8766c4', 'Igor', 'i@i.ru', '79123233434', 'DIRECTION_LEAD'),
       ('c631da58-8ae6-4a8d-bc6a-107c42a2b598', 'Oxa', 'o@i.ru', '7981231234', 'DIRECTION_LEAD');

insert into pf.auth_user(employee_id, login, password, is_locked)
values ('336dfae9-0980-4867-9e7c-06a39a0212ef', 'vova', '$2a$12$eWRkEhDqb1adfwAjz7Y81ulTBzYX4nydrx6/aYEfvqyfh3RcrbG4e',
        false);

insert into pf.direction (id, lead_id, name)
values ('211ed887-adc4-41bb-a8c4-e41393bd8e69', 'c631da58-8ae6-4a8d-bc6a-107c42a2b598', 'Nat'),
       ('caaad756-f11a-4635-898d-2861071ec38d', 'fb341a48-b23a-4ad4-9488-d094ab8766c4', 'Vasya');

insert into pf.project (id, name, project_lead_id, direction_id, description, create_date, status)
values ('ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f', 'One', '336dfae9-0980-4867-9e7c-06a39a0212ef',
        'caaad756-f11a-4635-898d-2861071ec38d', 'message 1', '2021-06-09T11:49:03.839234Z', 'UNAPPROVED'),
       ('4e7efeef-553f-4996-bc03-1c0925d56946', 'Three', '336dfae9-0980-4867-9e7c-06a39a0212ef',
        '211ed887-adc4-41bb-a8c4-e41393bd8e69', 'message', '2021-07-09T11:49:03.839234Z', 'ON_PL_PLANNING');
