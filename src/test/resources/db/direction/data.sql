insert into pf.employee(id, name, email, phone, position)
values ('1d97755d-d424-4128-8b78-ca5cb7b015c9', 'Vova', '1_1_super@email.com', '2-2-2-2-2', 'PROJECT_LEAD'),
       ('34ffb834-e0ac-4fab-9900-1993f3b8ad5b', 'Vlad', 'super@email.com', '1-1-1-1-1', 'PROJECT_LEAD');

insert into pf.direction(id, lead_id, name)
values ('02f27e01-2ac4-4518-9c66-d0b7e2259a6f', '1d97755d-d424-4128-8b78-ca5cb7b015c9', 'super direction');
