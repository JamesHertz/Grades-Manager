# Grades-Manager

A mini database to keep me informed of what's happing on my school.

I've written a first version of this, using purely java and java serialization to store the data on the disk. But I eventually realized that is was a bad idea. So I am rewriting the whole code base using java again, but this time using a Sqlite. I am redesigning everything and trying to come up with a clean and simple solution.

# TODO
- [ ] Fix problems:
    - [ ] Simplify the upload process by uploading all the students again (it's cheap :>)
    - [ ] Look at the DataSctures that it's being used for some commands (deprecate some of them)
    - [ ] Improve the download speed (using threads) and user experience (progress bars)
- [ ] Add features:
    - [ ] Add a command that allows you to upload base-students
    - [ ] Add count and rank to the Student
    - [ ] Make the system create a new database when such doesn't exist
- [ ] Look at this ideas
    - [ ] Possibility of choosing and making differents databases

