insert into pf.employee(id, name, email, phone, position)
values ('1d97755d-d424-4128-8b78-ca5cb7b015c9', 'Vova', '1_1_super@email.com', '2-2-2-2-2', 'PROJECT_LEAD'),
       ('34ffb834-e0ac-4fab-9900-1993f3b8ad5b', 'Vlad', 'super@email.com', '1-1-1-1-1', 'PROJECT_LEAD');

insert into pf.auth_user(employee_id, login, password, is_locked)
values ('1d97755d-d424-4128-8b78-ca5cb7b015c9', 'vova', '$2a$12$P3wWf0FjNlapbTMFQvFJ1.2iIc9UtMHfvyEhFonaT3LOr2sZSA6iO', false);