insert into pf.employee(id, name, email, phone, position)
values ('336dfae9-0980-4867-9e7c-06a39a0212ef', 'Vova', '1_1_super@email.com', '2-2-2-2-2', 'PROJECT_LEAD');

insert into pf.auth_user(employee_id, login, password, is_locked)
values ('336dfae9-0980-4867-9e7c-06a39a0212ef', 'user', '$2a$12$GiP/l3qaUGYCQCHdwcQC2eU3FhJkf9AnzdmFuwRedbN4UtvUVjqXi',
        false);
