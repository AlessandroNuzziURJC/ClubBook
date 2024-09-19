-- Crear el ALIAS para la función unaccent
CREATE ALIAS IF NOT EXISTS UNACCENT AS '
String unaccent(String src) {
    if (src == null) {
        return null;
    }
    return java.text.Normalizer.normalize(src, java.text.Normalizer.Form.NFD)
        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
}
';

ALTER TABLE t_user ALTER COLUMN profile_picture DROP NOT NULL;

INSERT INTO t_user (id, address, birthday, email, first_name, id_card, last_name, partner, password, phone_number, role_fk_id)
VALUES
    (1, 'C/ Street Test, 1', '2000-03-08', 'teststudent1@gmail.com', 'John', 'ID001', 'Gordon', true, 'password1', '123123123', 1),
    (2, 'C/ Street Test, 2', '1999-02-08', 'teststudent2@gmail.com', 'Jane', 'ID002', 'Jordan', false, 'password2', '123123124', 1),
    (3, 'C/ Street Test, 3', '1998-01-09', 'teststudent3@gmail.com', 'Alice', 'ID003', 'Morgan', false, 'password3', '123123125', 1),
    (4, 'C/ Street Test, 4', '2001-05-12', 'teststudent4@gmail.com', 'Bob', 'ID004', 'Smith', true, 'password4', '123123126', 1),
    (5, 'C/ Street Test, 5', '2002-07-22', 'teststudent5@gmail.com', 'Carol', 'ID005', 'Jones', true, 'password5', '123123127', 1),
    (6, 'C/ Street Test, 6', '2003-09-30', 'teststudent6@gmail.com', 'David', 'ID006', 'Williams', false, 'password6', '123123128', 1),
    (7, 'C/ Street Test, 7', '2004-11-15', 'teststudent7@gmail.com', 'Emma', 'ID007', 'Taylor', true, 'password7', '123123129', 1),
    (8, 'C/ Street Test, 8', '2005-12-25', 'teststudent8@gmail.com', 'Frank', 'ID008', 'Brown', false, 'password8', '123123130', 1),
    (9, 'C/ Street Test, 9', '2006-02-14', 'teststudent9@gmail.com', 'Grace', 'ID009', 'Davis', true, 'password9', '123123131', 1),
    (10, 'C/ Street Test, 10', '2007-04-18', 'teststudent10@gmail.com', 'Hank', 'ID010', 'Miller', false, 'password10', '123123132', 1),
    (11, 'C/ Street Test, 11', '1995-01-10', 'teststudent11@gmail.com', 'Ivy', 'ID011', 'Lopez', false, 'password11', '123123133', 1),
    (12, 'C/ Street Test, 12', '1994-02-15', 'teststudent12@gmail.com', 'Jack', 'ID012', 'Wilson', true, 'password12', '123123134', 1),
    (13, 'C/ Street Test, 13', '1993-03-20', 'teststudent13@gmail.com', 'Karen', 'ID013', 'Martinez', false, 'password13', '123123135', 1),
    (14, 'C/ Street Test, 14', '1992-04-25', 'teststudent14@gmail.com', 'Leo', 'ID014', 'Garcia', true, 'password14', '123123136', 1),
    (15, 'C/ Street Test, 15', '1991-05-30', 'teststudent15@gmail.com', 'Mia', 'ID015', 'Rodriguez', false, 'password15', '123123137', 1),
    (16, 'C/ Street Test, 16', '1990-06-10', 'teststudent16@gmail.com', 'Nina', 'ID016', 'Lewis', true, 'password16', '123123138', 1),
    (17, 'C/ Street Test, 17', '1989-07-15', 'teststudent17@gmail.com', 'Oscar', 'ID017', 'Lee', false, 'password17', '123123139', 1),
    (18, 'C/ Street Test, 18', '1988-08-20', 'teststudent18@gmail.com', 'Paula', 'ID018', 'Walker', true, 'password18', '123123140', 1),
    (19, 'C/ Street Test, 19', '1987-09-25', 'teststudent19@gmail.com', 'Quinn', 'ID019', 'Hall', false, 'password19', '123123141', 1),
    (20, 'C/ Street Test, 20', '1986-10-30', 'teststudent20@gmail.com', 'Rachel', 'ID020', 'Young', true, 'password20', '123123142', 1),
    (21, 'C/ Street Test, 21', '1985-11-15', 'teststudent21@gmail.com', 'Steve', 'ID021', 'Hernandez', false, 'password21', '123123143', 1),
    (22, 'C/ Street Test, 22', '1984-12-10', 'teststudent22@gmail.com', 'Tina', 'ID022', 'King', true, 'password22', '123123144', 1),
    (23, 'C/ Street Test, 23', '1983-01-05', 'teststudent23@gmail.com', 'Uma', 'ID023', 'Scott', false, 'password23', '123123145', 1),
    (24, 'C/ Street Test, 24', '1982-02-28', 'teststudent24@gmail.com', 'Victor', 'ID024', 'Green', true, 'password24', '123123146', 1),
    (25, 'C/ Street Test, 25', '1981-03-18', 'teststudent25@gmail.com', 'Wendy', 'ID025', 'Adams', false, 'password25', '123123147', 1),
    (26, 'C/ Street Test, 26', '1980-04-22', 'teststudent26@gmail.com', 'Xander', 'ID026', 'Baker', true, 'password26', '123123148', 1),
    (27, 'C/ Street Test, 27', '1979-05-11', 'teststudent27@gmail.com', 'Yasmin', 'ID027', 'Gonzalez', false, 'password27', '123123149', 1),
    (28, 'C/ Street Test, 28', '1978-06-09', 'teststudent28@gmail.com', 'Zane', 'ID028', 'Perez', true, 'password28', '123123150', 1),
    (29, 'C/ Street Test, 29', '1977-07-07', 'teststudent29@gmail.com', 'Aaron', 'ID029', 'Roberts', false, 'password29', '123123151', 1),
    (30, 'C/ Street Test, 30', '1976-08-25', 'teststudent30@gmail.com', 'Bella', 'ID030', 'Johnson', true, 'password30', '123123152', 1),
    (31, 'C/ Street Test, 31', '1975-09-12', 'teststudent31@gmail.com', 'Carlos', 'ID031', 'Anderson', false, 'password31', '123123153', 1),
    (32, 'C/ Street Test, 32', '1974-10-05', 'teststudent32@gmail.com', 'Diana', 'ID032', 'Thompson', true, 'password32', '123123154', 1),
    (33, 'C/ Street Test, 33', '1973-11-19', 'teststudent33@gmail.com', 'Ethan', 'ID033', 'Martinez', false, 'password33', '123123155', 1),
    (34, 'C/ Street Test, 34', '1972-12-08', 'teststudent34@gmail.com', 'Fiona', 'ID034', 'Rodriguez', true, 'password34', '123123156', 1),
    (35, 'C/ Street Test, 35', '1971-01-22', 'teststudent35@gmail.com', 'George', 'ID035', 'Lewis', false, 'password35', '123123157', 1),
    (36, 'C/ Street Test, 36', '1970-02-14', 'teststudent36@gmail.com', 'Hannah', 'ID036', 'Lee', true, 'password36', '123123158', 1),
    (37, 'C/ Street Test, 37', '1969-03-17', 'teststudent37@gmail.com', 'Isaac', 'ID037', 'Walker', false, 'password37', '123123159', 1),
    (38, 'C/ Street Test, 38', '1968-04-09', 'teststudent38@gmail.com', 'Jasmine', 'ID038', 'Hall', true, 'password38', '123123160', 1),
    (39, 'C/ Street Test, 39', '1967-05-23', 'teststudent39@gmail.com', 'Kevin', 'ID039', 'Young', false, 'password39', '123123161', 1),
    (40, 'C/ Street Test, 40', '1966-06-10', 'teststudent40@gmail.com', 'Luna', 'ID040', 'Hernandez', true, 'password40', '123123162', 1),
    (41, 'C/ Street Test, 41', '1965-07-11', 'teststudent41@gmail.com', 'Mark', 'ID041', 'King', false, 'password41', '123123163', 1),
    (42, 'C/ Street Test, 42', '1964-08-14', 'teststudent42@gmail.com', 'Nina', 'ID042', 'Scott', true, 'password42', '123123164', 1),
    (43, 'C/ Street Test, 43', '1963-09-17', 'teststudent43@gmail.com', 'Owen', 'ID043', 'Green', false, 'password43', '123123165', 1),
    (44, 'C/ Street Test, 44', '1962-10-19', 'teststudent44@gmail.com', 'Paula', 'ID044', 'Adams', true, 'password44', '123123166', 1),
    (45, 'C/ Street Test, 45', '1961-11-21', 'teststudent45@gmail.com', 'Quinn', 'ID045', 'Baker', false, 'password45', '123123167', 1),
    (46, 'C/ Street Test, 46', '1960-12-15', 'teststudent46@gmail.com', 'Rita', 'ID046', 'Gonzalez', true, 'password46', '123123168', 1),
    (47, 'C/ Street Test, 47', '1959-01-13', 'teststudent47@gmail.com', 'Sam', 'ID047', 'Perez', false, 'password47', '123123169', 1),
    (48, 'C/ Street Test, 48', '1958-02-28', 'teststudent48@gmail.com', 'Tina', 'ID048', 'Roberts', true, 'password48', '123123170', 1),
    (49, 'C/ Street Test, 49', '1957-03-25', 'teststudent49@gmail.com', 'Uma', 'ID049', 'Johnson', false, 'password49', '123123171', 1),
    (50, 'C/ Street Test, 50', '1956-04-30', 'teststudent50@gmail.com', 'Victor', 'ID050', 'Anderson', true, 'password50', '123123172', 1),
    (51, 'C/ Street Test, 51', '1955-05-10', 'teststudent51@gmail.com', 'Walter', 'ID051', 'Thompson', false, 'password51', '123123173', 1),
    (52, 'C/ Street Test, 52', '1954-06-06', 'teststudent52@gmail.com', 'Xenia', 'ID052', 'Martinez', true, 'password52', '123123174', 1),
    (53, 'C/ Street Test, 53', '1953-07-04', 'teststudent53@gmail.com', 'Yasmin', 'ID053', 'Rodriguez', false, 'password53', '123123175', 1),
    (54, 'C/ Street Test, 54', '1952-08-02', 'teststudent54@gmail.com', 'Zach', 'ID054', 'Lewis', true, 'password54', '123123176', 1),
    (55, 'C/ Street Test, 55', '1951-09-01', 'teststudent55@gmail.com', 'Andy', 'ID055', 'Lee', false, 'password55', '123123177', 1),
    (56, 'C/ Street Test, 56', '1950-10-10', 'teststudent56@gmail.com', 'Bella', 'ID056', 'Walker', true, 'password56', '123123178', 1),
    (57, 'C/ Street Test, 57', '1949-11-12', 'teststudent57@gmail.com', 'Chris', 'ID057', 'Hall', false, 'password57', '123123179', 1),
    (58, 'C/ Street Test, 58', '1948-12-30', 'teststudent58@gmail.com', 'Diana', 'ID058', 'Young', true, 'password58', '123123180', 1),
    (59, 'C/ Street Test, 59', '1947-01-23', 'teststudent59@gmail.com', 'Ethan', 'ID059', 'Hernandez', false, 'password59', '123123181', 1),
    (60, 'C/ Street Test, 60', '1946-02-14', 'teststudent60@gmail.com', 'Fiona', 'ID060', 'King', true, 'password60', '123123182', 1),
    (61, 'C/ Teacher Ave, 1', '1975-08-12', 'testteacher1@gmail.com', 'Alice', 'ID061', 'Smith', false, 'teacherpass1', '555123001', 2),
    (62, 'C/ Teacher Ave, 2', '1976-09-14', 'testteacher2@gmail.com', 'Bob', 'ID062', 'Johnson', true, 'teacherpass2', '555123002', 2),
    (63, 'C/ Teacher Ave, 3', '1977-10-16', 'testteacher3@gmail.com', 'Charlie', 'ID063', 'Williams', false, 'teacherpass3', '555123003', 2),
    (64, 'C/ Teacher Ave, 4', '1978-11-18', 'testteacher4@gmail.com', 'Diana', 'ID064', 'Brown', true, 'teacherpass4', '555123004', 2),
    (65, 'C/ Teacher Ave, 5', '1979-12-20', 'testteacher5@gmail.com', 'Edward', 'ID065', 'Jones', false, 'teacherpass5', '555123005', 2),
    (66, 'C/ Teacher Ave, 6', '1980-01-22', 'testteacher6@gmail.com', 'Fiona', 'ID066', 'Garcia', true, 'teacherpass6', '555123006', 2),
    (67, 'C/ Teacher Ave, 7', '1981-02-24', 'testteacher7@gmail.com', 'George', 'ID067', 'Martinez', false, 'teacherpass7', '555123007', 2),
    (68, 'C/ Teacher Ave, 8', '1982-03-26', 'testteacher8@gmail.com', 'Hannah', 'ID068', 'Rodriguez', true, 'teacherpass8', '555123008', 2),
    (69, 'C/ Teacher Ave, 9', '1983-04-28', 'testteacher9@gmail.com', 'Ivan', 'ID069', 'Lee', false, 'teacherpass9', '555123009', 2),
    (70, 'C/ Teacher Ave, 10', '1984-05-30', 'testteacher10@gmail.com', 'Julia', 'ID070', 'Hernandez', true, 'teacherpass10', '555123010', 2),
    (71, 'C/ Admin Blvd, 1', '1980-03-03', 'testadministrator1@gmail.com', 'Adam', 'ID071', 'Blake', true, 'adminpass1', '555321001', 3),
    (72, 'C/ Admin Blvd, 2', '1981-04-04', 'testadministrator2@gmail.com', 'Betty', 'ID072', 'Clark', false, 'adminpass2', '555321002', 3),
    (73, 'C/ Admin Blvd, 3', '1982-05-05', 'testadministrator3@gmail.com', 'Charlie', 'ID073', 'Davis', true, 'adminpass3', '555321003', 3);

INSERT INTO t_class_group (id, address, name)
VALUES
    (1, '101 Dojo Lane', 'Judo Beginners'),
    (2, '102 Dojo Lane', 'Judo Intermediate'),
    (3, '103 Dojo Lane', 'Judo Advanced'),
    (4, '104 Dojo Lane', 'Judo Kids'),
    (5, '105 Dojo Lane', 'Judo Masters');

INSERT INTO t_class_group_students(class_group_id, students_id)
VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
    (2, 11), (2, 12), (2, 13), (2, 14), (2, 15), (2, 16), (2, 17), (2, 18), (2, 19), (2, 20),
    (3, 21), (3, 22), (3, 23), (3, 24), (3, 25), (3, 26), (3, 27), (3, 28), (3, 29), (3, 30),
    (4, 31), (4, 32), (4, 33), (4, 34), (4, 35), (4, 36), (4, 37), (4, 38), (4, 39), (4, 40),
    (5, 41), (5, 42), (5, 43), (5, 44), (5, 45), (5, 46), (5, 47), (5, 48), (5, 49), (5, 50);

INSERT INTO t_class_group_teachers(class_group_id, teachers_id)
VALUES
    (1, 61), (2, 62), (3, 63), (4, 64), (5, 65), (5, 64);

INSERT INTO t_schedule(id, duration, init, weekday, class_group_fk)
VALUES
    (3, 60, '17:00', 'MONDAY', 2),
    (4, 60, '17:00', 'WEDNESDAY', 2),
    (5, 60, '18:00', 'TUESDAY', 3),
    (6, 60, '18:00', 'THURSDAY', 3),
    (7, 60, '17:00', 'MONDAY', 4),
    (8, 60, '17:00', 'WEDNESDAY', 4),
    (9, 60, '18:00', 'TUESDAY', 5),
    (10, 60, '18:00', 'THURSDAY', 5);

INSERT INTO t_attendance(id, attendance_date, user_id, attended)
VALUES
    (101, '2024-01-01', 1, true),
    (102, '2024-01-01', 2, true),
    (103, '2024-01-01', 3, true),
    (104, '2024-01-01', 4, true),
    (105, '2024-01-01', 5, true),
    (106, '2024-01-01', 6, true),
    (107, '2024-01-01', 7, false),
    (108, '2024-01-01', 8, true),
    (109, '2024-01-01', 9, true),
    (110, '2024-01-01', 10, true);

INSERT INTO T_SEASON(id, active, finish, init, admin_creator_id, admin_finisher_id)
VALUES (101, true, '2025-07-01', '2024-09-01', 71, 71);
