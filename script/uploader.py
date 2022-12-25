#! /usr/bin/env python3

import sqlite3
from json import loads

META_DATA_FILE = 'metadata.json'
BASE_DIR       = 'grades-base'
DB_NAME        = 'file.db'

def get_courses_info():
    with open(META_DATA_FILE) as f:
        return loads(f.read())

# insert into Course values ('id', 'name', 'credits', 'year', 'semester')
'''
    "name": "Álgebra Linear e Geometria Analítica",
        "year": 1,
        "semester": 1,
        "credits": 6,
        "file": "ALGA.out"
'''

def add_course(cursor : sqlite3.Cursor, course_id, course_info):
    name     = course_info['name']
    year     = course_info['year']
    credits  = course_info['credits']
    semester = course_info['semester']

    cursor.execute('''
            insert into Course values (?, ?, ?, ?, ?)
        ''', (course_id, name, credits, year, semester))

def get_enroll_records(file_name):
    with open(f'{BASE_DIR}/{file_name}') as file:
        return file.readlines()

def main():
    db = sqlite3.connect("file.db")

    cursor = db.cursor()
    
    for table in ["Enrollment", "Course", "Student"]:
        cursor.execute(f"delete from {table}")

    students = set()
    c_info = get_courses_info() 

    for course_id, course_info in c_info.items():
        add_course(cursor, course_id, course_info)
        enrolls : list[str] = get_enroll_records(course_info['file'])

        for record in enrolls:
            [number, name, grade] = record.split('\t')
            number = int(number)
            if not number in students:
                cursor.execute("insert into Student values(?, ?)", (number, name))
                students.add(number)
            cursor.execute("insert into Enrollment values(?, ?, ?)", \
                    (course_id, number, float(grade)))
            db.commit()


    db.close()
    


if __name__ == '__main__':
    main()