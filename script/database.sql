drop table if exists Enrollment;
drop table if exists Student;
drop table if exists Course;
drop view  if exists MyStudent;
drop view  if exists TopBoard;

create table Student (
    st_number int primary key,
    st_name text
);

create table Course (
    cs_id text primary key,
    cs_name text,
    credits int,
    cs_year int,
    cs_semester int,
    cs_code int unique,

    check(credits > 0)
);

create table Enrollment (
    cs_id text,
    st_number int,
    grade real,

    check(grade between 0 and 20.0),
    primary key (cs_id, st_number),
    foreign key (st_number) references Student,
    foreign key (cs_id) references Course
);

create view MyStudent as
    select st_number as number, st_name as name, total_credits, avg_grade
    from Student natural left outer join (
        select st_number, sum(credits) as total_credits, round(sum(credits * grade)/sum(credits), 2) as avg_grade
        from Enrollment natural inner join Course
        where grade >= 10
        group by st_number
    );