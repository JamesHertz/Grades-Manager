drop table Enrollment;
drop table Student;
drop table Course;

create table Student (
    st_number int primary key,
    st_name text,
    total_credits int default 0,
    avg_numerator real default 0.0,
    average_grade real generated always as ( avg_numerator / total_credits) VIRTUAL
);

create table Course (
    cs_id text primary key,
    cs_name text,
    credits int,
    cs_year int,
    cs_semester int,

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

-- PROPOSAL FOR INSTEAD OF THE TRIGGERS :)
create view MyStudent as
    select st_name as name, st_number as number, sum(credits) as total_credits, round(sum(credits * grade)/sum(credits), 2)  as avg_grade
    from Student natural inner join Enrollment natural inner join Course
    group by name, number;
-- trigger to add number to set number to
/*
DEPRECATED :)
create trigger set_enroll_number
    after insert on Enrollment
    for each row
    begin
        update Enrollment
        set enroll_number = (
            select
                case when max(enroll_number) is NULL then 0
                     else max(enroll_number) + 1
                    end
            from Enrollment
            where st_number == new.st_number
        )
        where rowid == new.ROWID;
    end;
 */
-- trigger to calculate the final grade

create trigger update_student_attrs_I
    after insert on Enrollment
    begin
        update Student
        set total_credits =
            case when new.grade >= 10
                then total_credits + (select credits from Course where cs_id = new.cs_id)
            else
                total_credits
                end,
            avg_numerator =
                case when new.grade >= 10
                    then avg_numerator + new.grade  * (select credits from Course where cs_id = new.cs_id)
                else
                    avg_numerator
        end
        where st_number = new.st_number;
    end;

create trigger update_student_attrs_II
    after delete on Enrollment
    begin
        update Student
        set total_credits =
            case when old.grade >= 10
                then total_credits - (select credits from Course where cs_id = old.cs_id)
            else
                total_credits
                end,
            avg_numerator =
                case when old.grade >= 10
                    then avg_numerator - old.grade  * (select credits from Course where cs_id = old.cs_id)
                else
                    avg_numerator
        end
        where st_number = old.st_number;
    end;

create trigger update_student_attrs_III
    after update on Enrollment
    begin
        update Student
        set total_credits =
            case when new.grade >= 10
                then total_credits + (select credits from Course where cs_id = new.cs_id)
            else
                total_credits
                end,
            avg_numerator =
                case when new.grade >= 10
                    then avg_numerator + new.grade  * (select credits from Course where cs_id = new.cs_id)
                else
                    avg_numerator
        end
        where st_number = new.st_number;

        update Student
        set total_credits =
            case when old.grade >= 10
                then total_credits - (select credits from Course where cs_id = old.cs_id)
            else
                total_credits
                end,
            avg_numerator =
                case when old.grade >= 10
                    then avg_numerator - old.grade  * (select credits from Course where cs_id = old.cs_id)
                else
                    avg_numerator
        end
        where st_number = old.st_number;
    end;