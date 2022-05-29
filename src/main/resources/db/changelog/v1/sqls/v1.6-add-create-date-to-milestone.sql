alter table if exists pf.milestone
    add column if not exists create_date timestamptz;

update pf.milestone
set create_date = now();

alter table if exists pf.milestone
    alter column create_date set not null;
