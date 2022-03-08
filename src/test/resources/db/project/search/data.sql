insert into pf.employee(id, name, email, phone, position, is_fired)
values ('abaeb5af-ad0e-4d3f-b1f3-5300cada314d', 'Igor', 'i@i.ru', '79888888888', 'DIRECTOR', false),
       ('7a1d3076-6d80-4a66-b50e-2ed5c9ee056c', 'Oxa', 'o@i.ru', '79888888800', 'DIRECTOR', false);

insert into pf.direction (id, lead_id, name)
values ('4ce0d560-1b5d-4618-9a78-23254a04b79f', 'abaeb5af-ad0e-4d3f-b1f3-5300cada314d', 'Nat'),
       ('2f7c06e7-006a-4ca6-98e9-2ba13a0af6c7', 'abaeb5af-ad0e-4d3f-b1f3-5300cada314d', 'Vasya');

insert into pf.project (id, name, project_lead_id, direction_id, description, create_date, status)
values ('07691d5c-f5e7-4c18-998c-b5042f8f3e7b', 'One', 'abaeb5af-ad0e-4d3f-b1f3-5300cada314d',
        '4ce0d560-1b5d-4618-9a78-23254a04b79f', 'message 1', '2021-06-09T11:49:03.839234Z', 'UNAPPROVED'),
       ('7073f1e1-3e6c-4008-a41b-acdf42add3e5', 'Two', '7a1d3076-6d80-4a66-b50e-2ed5c9ee056c',
        '2f7c06e7-006a-4ca6-98e9-2ba13a0af6c7', 'message 2', '2021-08-09T11:49:03.839234Z', 'UNAPPROVED'),
       ('741ad655-d850-4dcc-82fe-3c7facd53b3b', 'Three', '7a1d3076-6d80-4a66-b50e-2ed5c9ee056c',
        '2f7c06e7-006a-4ca6-98e9-2ba13a0af6c7', 'message 3', '2021-07-09T11:49:03.839234Z', 'ON_PL_PLANNING'),
       ('5fedf7df-fe7d-4492-a1e8-34b450d5fbd5', 'Four', '7a1d3076-6d80-4a66-b50e-2ed5c9ee056c',
        '2f7c06e7-006a-4ca6-98e9-2ba13a0af6c7', 'message 4', '2021-07-09T11:49:03.839234Z', 'ON_PL_PLANNING'),
       ('ad80a5ce-7cbd-497d-9112-a732718d8d48', 'Five', '7a1d3076-6d80-4a66-b50e-2ed5c9ee056c',
        '2f7c06e7-006a-4ca6-98e9-2ba13a0af6c7', 'message 5', '2021-07-09T11:49:03.839234Z', 'ON_PL_PLANNING');
