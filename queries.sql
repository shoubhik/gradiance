create table users (u_id varchar(30), pwd varchar(30) not null, primary key(uid));

insert into users values ('kogan', 'kogan');

CREATE TABLE prof(prof_id varchar(30)  PRIMARY KEY NOT NULL, name varchar(50) default null,
primary key(prof_id), foreign key(prof_id) references users(u_id);

insert into prof values('kogan', 'kemafor ogan');

create table courses(course_id varchar(30), name varchar(50)  not null,
  startDate date not null, endDate date not null, primary key (course_id), check (endDate > startDate))
alter table courses add token varchar(30) not null

insert into courses values('CSC440', 'Database Systems', to_date('1/1/2013', 'mm/dd/yyyy'),
                           to_date('5/10/2013', 'mm/dd/yyyy'), 'CSC440SPR13' )

insert into courses values('CSC541', 'Advanced Data Structures', to_date('8/1/2011', 'mm/dd/yyyy'),
                           to_date('12/15/2011', 'mm/dd/yyyy'), 'CSC541FLL11' );

insert into courses values('CSC501', 'Operating Systems', to_date('1/1/2012', 'mm/dd/yyyy'),
                           to_date('5/10/2012', 'mm/dd/yyyy'), 'CSC501SPR12' );

create table teaches(course_id varchar(30), prof_id varchar(40),
  primary key (course_id, prof_id), foreign key(course_id) references courses(course_id),
  foreign key(prof_id) references prof(prof_id))

create table course_topics(course_id varchar(30) NOT NULL, topic_id varchar(30), name varchar(100) not null
  , primary key (course_id, topic_id), foreign key (course_id) references courses(course_id))

insert into course_topics values('CSC440', 'CSC4401', 'database Fundamental')
insert into course_topics values('CSC440', 'CSC4402', 'Security and Authorization')
insert into course_topics values('CSC440', 'CSC4403', 'ER Design and other topics')
insert into course_topics values('CSC541', 'CSC5411', 'Binary Search Trees and BTrees')
insert into course_topics values('CSC541', 'CSC5412', 'Hashing')
insert into course_topics values('CSC541', 'CSC5413', 'File and indexing and other topics')
insert into course_topics values('CSC501', 'CSC5011', 'Process and threads')
insert into course_topics values('CSC501', 'CSC5012', 'Memory Organization')
insert into course_topics values('CSC501', 'CSC5013', 'Deadlock and other topics')

create table hints(hint_id int, text varchar (200) not null, primary key (hint_id))

create table questions(q_id int, topic_id varchar(30) not null, text varchar(300) not null,
  difficulty_level int, points_incorrect int, points_correct int, hint_id int,
  primary key (q_id), foreign key (topic_id) references course_topics(topic_id),
  foreign key(hint_id) references hints(hint_id), check(difficulty_level >= 1 and difficulty_level <=5
  and points_correct > 0  and points_incorrect >= 0))

create table answers(ans_id int, text varchar(300) not null, hint_id int not null,
  primary key (ans_id), foreign key(hint_id) references hints(hint_id))

create table question_answer(q_id int, ans_id int, correct int not null, primary key (q_id, ans_id),
  foreign key(q_id) references questions(q_id), foreign key (ans_id) references answers(ans_id),
    check(correct = 1 or correct = 0))

create table score_selection_scheme(id int, name varchar(50) not null, primary key (id))
insert into score_selection_scheme values(1, 'first attempt')
insert into score_selection_scheme values(2, 'last attempt')
insert into score_selection_scheme values(3, 'max of all  attempt')
insert into score_selection_scheme values(4, 'avg of all  attempt')

create table homeworks(hw_id int, course_id varchar(30) not null,
  score_selection_scheme int not null, name varchar(100),start_date date not null,
  end_date date not null, num_attempts int not null,correct_ans_pts int not null,
  incorrect_ans_pts int not null, topic_id varchar(30) not null, primary key (hw_id),
  foreign key (course_id) references courses (course_id),
  foreign key (score_selection_scheme) references score_selection_scheme(id),
  foreign key (topic_id) references course_topics(topic_id), check(end_date > start_date))

alter table homeworks add numQuestions int

create table students (student_id varchar(30), name varchar(30), seed int not null, primary key (student_id))

create table hw_student(hw_id int not null, student_id varchar(30) not null, attempt_id int not null,
  attempt_date date not null,
  foreign key(hw_id) references homeworks(hw_id), foreign key (student_id) references students(student_id))

create table enrolled(course_id varchar(30), student_id varchar(30), primary key (course_id, student_id),
foreign key (course_id) references courses(course_id), foreign key (student_id) references students(student_id))

create table attempt_ans(attempt_ans_id int not null, ans_id int not null,
  foreign key(ans_id) references answers(ans_id) )

create table attempt(attempt_id int not null, q_id int not null, attempt_ans_id int not null,
  foreign key(q_id) references questions(q_id))

create table hw_student(hw_id int not null, student_id varchar(30) not null, attempt_id int not null,
  attempt_date date ,
  foreign key(hw_id) references homeworks(hw_id), foreign key (student_id) references students(student_id))

alter table hw_student add score int

alter table attempt add response_id int

alter table attempt add constraint fk_response foreign key (response_id) references answers(ans_id)

alter table hw_student modify (attempt_date null)

create table hw_questions(hw_id int not null, q_id int not null, foreign key (hw_id)
references homeworks(hw_id), foreign key (q_id) references questions(q_id))

alter table attempt add response_exp varchar(100) null