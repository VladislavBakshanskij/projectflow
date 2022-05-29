insert into pf.milestone(id, project_id, name, description, planned_start_date, planned_finish_date, fact_start_date,
                         fact_finish_date, create_date)
values ('ce5ee15c-3afa-4649-9d68-451ad23f4cfd', '4e7efeef-553f-4996-bc03-1c0925d56946', 'do site', 'do site on react',
        '2021-02-09T11:49:03.839234Z', '2021-06-09T11:49:03.839234Z', '2021-03-09T11:49:03.839234Z',
        '2021-06-09T11:49:03.839234Z', now()),
       ('026ae239-66c5-4ac5-b37b-cf6ba3d3b10c', 'ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f', 'do super feature', null,
        '2021-02-09T11:49:03.839234Z', '2021-06-09T11:49:03.839234Z', null, null, now());
