CREATE PROCEDURE read_t_names()
    READS SQL DATA DYNAMIC RESULT SETS 1
BEGIN ATOMIC
DECLARE result CURSOR WITH RETURN FOR SELECT Id, Name, Price FROM Product WHERE LOWER(name) LIKE 't%';
OPEN result;
END;
CREATE PROCEDURE read_names_by_letter(IN prefix VARCHAR(10))
    READS SQL DATA DYNAMIC RESULT SETS 1
BEGIN ATOMIC
DECLARE result CURSOR WITH RETURN FOR
    SELECT * FROM Product WHERE LOWER(name) LIKE CONCAT(LOWER(prefix), '%');
OPEN result;
END;
CREATE PROCEDURE magic_number(OUT num INT) READS SQL DATA
BEGIN ATOMIC
SET num = 42;
END;
CREATE PROCEDURE double_number(INOUT num INT) READS SQL DATA
    DYNAMIC RESULT SETS 1
BEGIN ATOMIC
SET num = num * 2;
END;
