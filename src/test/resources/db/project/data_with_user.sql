insert into pf.employee(id, name, email, phone, position, is_fired)
values ('fb341a48-b23a-4ad4-9488-d094ab8766c4', 'Igor', 'i@i.ru', '79123233434', 'DIRECTOR', false),
       ('c631da58-8ae6-4a8d-bc6a-107c42a2b598', 'Oxa', 'o@i.ru', '7981231234', 'DIRECTION_LEAD', false),
       ('119882bc-e06f-455a-9c99-c067ead45fe2', 'Opopo', 'oppo@opppo.oppo', '7911111111', 'PROJECT_LEAD', false),
       ('fc9632a7-66b4-4627-846c-a0e65533637c', 'Oliblib', 'oliblib@liblib.libliblib', '79883338800', 'PROJECT_LEAD',
        false);

insert into pf.auth_user(employee_id, login, password, is_locked)
values ('fc9632a7-66b4-4627-846c-a0e65533637c', 'project_lead',
        '$2a$12$v3zQAbYSfdEnt0mxFeOZOe6DRHD0G/nrKszW4euarO7UQmqria1mq', false),
       ('c631da58-8ae6-4a8d-bc6a-107c42a2b598', 'direction_lead',
        '$2a$12$ff4bhUA4dXq2Vd2kxk92vOZEW.ctj87i6qY.DOfgbt5KWaOTRQTTC', false),
       ('fb341a48-b23a-4ad4-9488-d094ab8766c4', 'director',
        '$2a$12$uB7QWJBNNidRkL9Cf0c4OOS8XjNkWj4TUbctRz9v0cToV.atvqake', false);

insert into pf.direction (id, lead_id, name)
values ('211ed887-adc4-41bb-a8c4-e41393bd8e69', 'c631da58-8ae6-4a8d-bc6a-107c42a2b598', 'Nat'),
       ('caaad756-f11a-4635-898d-2861071ec38d', 'fb341a48-b23a-4ad4-9488-d094ab8766c4', 'Vasya');

insert into pf.project (id, name, project_lead_id, direction_id, description, create_date, status)
values ('ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f', 'One', 'fc9632a7-66b4-4627-846c-a0e65533637c',
        'caaad756-f11a-4635-898d-2861071ec38d', 'message 1', '2021-06-09T11:49:03.839234Z', 'UNAPPROVED'),
       ('4e7efeef-553f-4996-bc03-1c0925d56946', 'Three', 'fc9632a7-66b4-4627-846c-a0e65533637c',
        '211ed887-adc4-41bb-a8c4-e41393bd8e69', 'message', '2021-07-09T11:49:03.839234Z', 'ON_PL_PLANNING');