INSERT INTO T_NOTEBOOK(id, age_range, duration, level, sport, class_group_id)
    VALUES (10, '1990 - 2006', '60', 'Principiante', 'Judo', 1);

INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (100, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, CURRENT_DATE);
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (101, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 1, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (103, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 2, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (104, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 3, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (105, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 4, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (106, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 5, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (107, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 6, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (108, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 7, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (109, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 8, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (110, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 9, CURRENT_DATE));
INSERT INTO T_ENTRY(id, final_exercises, specific_exercises, warm_up_exercises, notebook_id, date)
    VALUES (111, '["hola", "adios"]', '["hola", "adios"]', '["hola", "adios"]', 10, DATEADD('DAY', 10, CURRENT_DATE));