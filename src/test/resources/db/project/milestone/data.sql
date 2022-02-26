insert into pf.employee(id, name, email, phone, position, is_fired)
values ('fb341a48-b23a-4ad4-9488-d094ab8766c4', 'Igor', 'i@i.ru', '79123233434', 'DIRECTOR', false),
       ('c631da58-8ae6-4a8d-bc6a-107c42a2b598', 'Oxa', 'o@i.ru', '7981231234', 'DIRECTOR', false),
       ('119882bc-e06f-455a-9c99-c067ead45fe2', 'Opopo', 'oppo@opppo.oppo', '7911111111', 'PROJECT_LEAD', false),
       ('fc9632a7-66b4-4627-846c-a0e65533637c', 'Oliblib', 'oliblib@liblib.libliblib', '79883338800', 'PROJECT_LEAD',
        false);

insert into pf.direction (id, lead_id, name)
values ('211ed887-adc4-41bb-a8c4-e41393bd8e69', 'c631da58-8ae6-4a8d-bc6a-107c42a2b598', 'Nat'),
       ('caaad756-f11a-4635-898d-2861071ec38d', 'fb341a48-b23a-4ad4-9488-d094ab8766c4', 'Vasya');

insert into pf.project (id, name, project_lead_id, direction_id, description, create_date, status)
values ('ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f', 'One', 'fc9632a7-66b4-4627-846c-a0e65533637c',
        'caaad756-f11a-4635-898d-2861071ec38d', 'message 1', '2021-06-09T11:49:03.839234Z', 'UNAPPROVED'),
       ('4e7efeef-553f-4996-bc03-1c0925d56946', 'Three', 'fc9632a7-66b4-4627-846c-a0e65533637c',
        '211ed887-adc4-41bb-a8c4-e41393bd8e69', 'message', '2021-07-09T11:49:03.839234Z', 'ON_PL_PLANNING');

insert into pf.milestone(id, project_id, name, description, planned_start_date, planned_finish_date, fact_start_date,
                         fact_finish_date)
values ('fda0c0f6-b637-4039-a2d6-640aba6b4e46', '4e7efeef-553f-4996-bc03-1c0925d56946', 'do site', 'do site on react',
        '2021-02-09T11:49:03.839234Z', '2021-06-09T11:49:03.839234Z', '2021-03-09T11:49:03.839234Z',
        '2021-06-09T11:49:03.839234Z'),
       ('35b4b5f0-31ac-4cad-83fc-4d996c586fe3', 'ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f', 'do super feature', 'do super feature on java',
        '2021-02-09T11:49:03.839234Z', '2021-06-09T11:49:03.839234Z', '2021-03-09T11:49:03.839234Z',
        '2021-06-09T11:49:03.839234Z')
