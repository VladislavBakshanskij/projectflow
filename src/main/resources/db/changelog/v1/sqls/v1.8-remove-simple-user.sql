create type pf.user_position_1 as enum
(
    'PROJECT_LEAD',
    'DIRECTION_LEAD',
    'DIRECTOR'
);

alter table if exists pf.employee
    alter column position type varchar using position::varchar;

drop type pf.user_position;

alter type pf.user_position_1 rename to user_position;

alter table if exists pf.employee
    alter column position type pf.user_position using position::pf.user_position;

