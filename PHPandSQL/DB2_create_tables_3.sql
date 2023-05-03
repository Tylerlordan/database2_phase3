DROP TABLE IF EXISTS member_of;
DROP TABLE IF EXISTS enroll;
DROP TABLE IF EXISTS assign;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS meetings;
DROP TABLE IF EXISTS time_slot;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS child_of;
DROP TABLE IF EXISTS parents;
DROP TABLE IF EXISTS submissions;
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS groups;

-- ----------------------------
-- Table structure for users
-- ----------------------------

CREATE TABLE users (
  id int(11),
  email varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  phone varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for parents
-- ----------------------------

CREATE TABLE parents (
  parent_id int(11),
  PRIMARY KEY (parent_id),
  FOREIGN KEY (parent_id) REFERENCES users (id) ON DELETE CASCADE
);

-- ----------------------------
-- Table structure for students
-- ----------------------------

CREATE TABLE students (
  student_id int(11),
  grade int(11),
  PRIMARY KEY (student_id),
  FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE
);

-- ----------------------------
-- Table structure for child_of
-- ----------------------------

CREATE TABLE child_of (
  student_id int(11),
  parent_id int(11),
  PRIMARY KEY (student_id, parent_id),
  FOREIGN KEY (student_id) REFERENCES students (student_id),
  FOREIGN KEY (parent_id) REFERENCES parents (parent_id)
);


-- ----------------------------
-- Table structure for admins
-- ----------------------------

CREATE TABLE admins (
  admin_id int(11),
  PRIMARY KEY (admin_id),
  FOREIGN KEY (admin_id) REFERENCES users (id) ON DELETE CASCADE
);


-- ----------------------------
-- Table structure for groups
-- ----------------------------

CREATE TABLE groups (
  group_id int(11),
  name varchar(255) NOT NULL,
  description varchar(255),
  grade_req int(11),
  PRIMARY KEY (group_id)
);


-- ----------------------------
-- Table structure for time_slot (this table is from normalizaion)
-- ----------------------------

CREATE TABLE time_slot (
  time_slot_id int(11),
  day_of_the_week varchar(255) NOT NULL,
  start_time time NOT NULL,
  end_time time NOT NULL,
  PRIMARY KEY (time_slot_id)
);

-- ----------------------------
-- Table structure for meetings
-- ----------------------------

CREATE TABLE meetings (
  meeting_id int(11),
  meeting_name varchar(255) NOT NULL,
  date date NOT NULL,
  time_slot_id int(11) NOT NULL,
  capacity int(11) NOT NULL,
  group_id int(11) NOT NULL,
  announcement varchar(255),
  PRIMARY KEY (meeting_id),
  FOREIGN KEY (group_id) REFERENCES groups (group_id),
  FOREIGN KEY (time_slot_id) REFERENCES time_slot (time_slot_id)
);

-- ----------------------------
-- Table structure for material
-- ----------------------------

CREATE TABLE material (
  material_id int(11),
  meeting_id int(11),
  title varchar(255) NOT NULL,
  author varchar(255),
  type varchar(255),
  url varchar(255),
  notes varchar(255),
  assigned_date date NOT NULL,
  PRIMARY KEY (material_id, meeting_id),
  FOREIGN KEY (meeting_id) REFERENCES meetings (meeting_id) ON DELETE CASCADE
);

-- ----------------------------
-- Table structure for enroll
-- ----------------------------

CREATE TABLE enroll (
  meeting_id int(11) NOT NULL,
  student_id int(11) NOT NULL,
  PRIMARY KEY (meeting_id, student_id),
  FOREIGN KEY (student_id) REFERENCES students (student_id),
  FOREIGN KEY (meeting_id) REFERENCES meetings (meeting_id)
);

-- ----------------------------
-- Table structure for member_of
-- ----------------------------

CREATE TABLE member_of (
  group_id int(11) NOT NULL,
  student_id int(11) NOT NULL,
  PRIMARY KEY (group_id, student_id),
  FOREIGN KEY (student_id) REFERENCES students (student_id),
  FOREIGN KEY (group_id) REFERENCES groups (group_id)
);


-- ----------------------------
-- Table structure for assignments
-- ----------------------------

CREATE TABLE assignments (
  group_id int(11) NOT NULL,
  assignment_id int(11) NOT NULL,
  name varchar(255),
  description varchar(255), 
  PRIMARY KEY (assignment_id),
  FOREIGN KEY (group_id) REFERENCES groups (group_id)
);


-- ----------------------------
-- Table structure for submissions
-- ----------------------------

CREATE TABLE submissions (
  assignment_id int(11) NOT NULL,
  student_id int(11) NOT NULL,
  contents varchar(255),
  score int (11), 
  PRIMARY KEY (assignment_id, student_id),
  FOREIGN KEY (student_id) REFERENCES students (student_id),
  FOREIGN KEY (assignment_id) REFERENCES assignments (assignment_id)
);


INSERT INTO users VALUES(1, "admin", "password", "admin", 1);
INSERT INTO admins VALUES(1);

INSERT INTO groups VALUES(1, "Math", "Mathematics group for fifth and sixth graders", 5);
INSERT INTO groups VALUES(2, "Literature", "Literature group for seventh and eighth graders", 7);
INSERT INTO groups VALUES(3, "Science", "Science group for second and third graders", 2);
INSERT INTO groups VALUES(4, "Computer Science", "Computer Science group for tenth and eleventh graders", 10);
INSERT INTO groups VALUES(5, "History", "History group for sixth graders", 6);

INSERT INTO time_slot (`time_slot_id`, `day_of_the_week`, `start_time`, `end_time`) VALUES
(1, 'Saturday', '17:00:00', '18:00:00'),
(2, 'Sunday', '16:00:00', '17:00:00'),
(3, 'Sunday', '14:00:00', '15:00:00'),
(4, 'Sunday', '17:00:00', '18:00:00');

INSERT INTO meetings VALUES
(1, 'Math Meeting', '2023-05-07', 4, 6, 1, 'A math meeting'),
(2, 'History 1', '2023-05-07', 4, 6, 5, 'History meeting 1'),
(3, 'Lit. Meeting', '2023-04-29', 1, 6, 2, 'First literature meeting.');

INSERT INTO users VALUES
(2, 'tom@gmail.com', 'password', 'Tom', '1111'),
(3, 'Brian@gmail.com', 'password', 'Brian', '1111'),
(4, 'parent@gmail.com', 'password', 'parent', '1213');

INSERT INTO students VALUES
(2, 6),
(3, 8);

INSERT INTO parents VALUES
(4);

INSERT INTO child_of VALUES
(2, 4);

INSERT INTO member_of VALUES
(1, 2),
(5, 2);

INSERT INTO enroll VALUES
(1, 2),
(2, 2);

INSERT INTO material VALUES
(1, 1, 'Math Book', 'Isaac Newton', 'Book', 'http://www.google.com', 'Bring to the first meeting.', '2023-04-02'),
(2, 2, 'History Book', 'Charles Xavier', 'Book', 'http://www.google.com', 'Bring to the first meeting.', '2023-04-02'),
(3, 3, 'Book', 'Helen Keller', 'Book', 'http://www.google.com', 'Bring to the first meeting.', '2023-04-29');