insert into pf.employee(id, name, email, phone, position, is_fired)
values  ('1e1fc94f-e93d-4c2f-a5e2-a328c1db8b26', 'Иван Иванов', 'ivan@gmail.com', '111111111', 'DIRECTOR', false),
        ('5f31594f-0755-4222-9ba2-29e90f569b6b', 'Пётр Петров', 'petr@gmail.com', '222222222', 'DIRECTION_LEAD', false),
        ('45d35aeb-5c23-4cbc-81bf-24ac8ca554a2', 'Сидор Сидоров', 'sidor@gmail.com', '333333333', 'PROJECT_LEAD', false),
        ('9b79d3a1-63a8-48a3-8974-487b515121a8', 'Фёдор Фёдоров', 'fedor@gmail.com', '444444444', 'DIRECTION_LEAD', false),
        ('e8c44d68-2255-4cc1-b08e-1eda0eef0dec', 'Николай Николаев', 'nik@gmail.com', '555555555', 'PROJECT_LEAD', false),
        ('7618d39e-6c73-476d-a9fd-df5fddb86c60', 'Евгения Евгеньева', 'eva@gmail.com', '666666666', 'PROJECT_LEAD', false);

insert into pf.direction(id, lead_id, name)
values ('6e2d2246-04ca-45c7-aaf1-0e11df7a35bb', '1e1fc94f-e93d-4c2f-a5e2-a328c1db8b26', 'Основной Direction'),
       ('70291277-bf83-4652-8f6c-31e4740b8084', '5f31594f-0755-4222-9ba2-29e90f569b6b', 'sales'),
       ('764a4193-78f8-4c5d-b257-62e16adf1d14', '45d35aeb-5c23-4cbc-81bf-24ac8ca554a2', 'IT'),
       ('f4f51ca8-77f7-447f-8202-1ac0e01f0831', '9b79d3a1-63a8-48a3-8974-487b515121a8', 'HR'),
       ('52fc8374-474e-45de-9a09-b4fefe3be08c', 'e8c44d68-2255-4cc1-b08e-1eda0eef0dec', 'Delivery'),
       ('2a8cd165-28bb-4ff0-b069-4ca0e589fd26', '7618d39e-6c73-476d-a9fd-df5fddb86c60', 'Calculation'),
       ('e45f6dcc-84d7-4f03-8161-6ff5b92b8e6f', '7618d39e-6c73-476d-a9fd-df5fddb86c60', 'Gas'),
       ('b749d23a-8d54-41b0-b43e-40ee778c1156', '7618d39e-6c73-476d-a9fd-df5fddb86c60', 'Oil'),
       ('ef5a0c98-5444-4b97-b6cf-e8e616fb99b2', '7618d39e-6c73-476d-a9fd-df5fddb86c60', 'Water'),
       ('8baff1e9-bc05-4091-a639-3877050138fc', '7618d39e-6c73-476d-a9fd-df5fddb86c60', 'Rubbish');
