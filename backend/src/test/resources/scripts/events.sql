INSERT INTO T_EVENT_TYPE VALUES (1, CURRENT_TIMESTAMP, 'COMPETITION');
INSERT INTO T_EVENT_TYPE VALUES (2, CURRENT_TIMESTAMP, 'EXHIBITION');
INSERT INTO T_EVENT_TYPE VALUES (3, CURRENT_TIMESTAMP, 'TRAINING');

INSERT INTO T_EVENT(id, additional_info, address, birth_year_end, birth_year_start, date, title, type_event_type_id, deadline) VALUES(101, 'Event 1', 'Address 1', '2020-12-31', '2010-01-01', DATEADD('DAY', 10, CURRENT_DATE),'Event 1', 3, DATEADD('DAY', 5, CURRENT_DATE));
INSERT INTO T_EVENT(id, additional_info, address, birth_year_end, birth_year_start, date, title, type_event_type_id, deadline)  VALUES(102, 'Event 2', 'Address 2', '2010-12-31', '2000-01-01', DATEADD('DAY', 10, CURRENT_DATE),'Event 1', 3, DATEADD('DAY', 5, CURRENT_DATE));
INSERT INTO T_EVENT(id, additional_info, address, birth_year_end, birth_year_start, date, title, type_event_type_id, deadline)  VALUES(103, 'Event 3', 'Address 3', '2000-12-31', '1990-01-01', DATEADD('DAY', 10, CURRENT_DATE),'Event 1', 3, DATEADD('DAY', 5, CURRENT_DATE));
INSERT INTO T_EVENT(id, additional_info, address, birth_year_end, birth_year_start, date, title, type_event_type_id, deadline)  VALUES(104, 'Event 4', 'Address 4', '2020-12-31', '2015-01-01', DATEADD('DAY', 10, CURRENT_DATE),'Event 1', 3, DATEADD('DAY', 5, CURRENT_DATE));
INSERT INTO T_EVENT(id, additional_info, address, birth_year_end, birth_year_start, date, title, type_event_type_id, deadline)  VALUES(105, 'Event 5', 'Address 5', '2018-12-31', '2012-01-01', DATEADD('DAY', 10, CURRENT_DATE),'Event 1', 3, DATEADD('DAY', 5, CURRENT_DATE));
INSERT INTO T_EVENT(id, additional_info, address, birth_year_end, birth_year_start, date, title, type_event_type_id, deadline)  VALUES(106, 'Event 6', 'Address 6', '1965-12-31', '1940-01-01', DATEADD('DAY', 10, CURRENT_DATE),'Event 1', 3, DATEADD('DAY', 5, CURRENT_DATE));

INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(101, null, 102, 1);
INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(102, null, 102, 4);
INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(103, null, 102, 5);
INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(104, null, 102, 6);
INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(105, null, 102, 7);
INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(106, null, 102, 8);
INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(107, null, 102, 9);
INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(108, null, 102, 10);
INSERT INTO T_EVENT_ATTENDANCE(id, status, event_id, user_id) VALUES(109, null, 102, 61);