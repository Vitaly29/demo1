-- Важно кодировка windows-1251, не utf-8
DROP TABLE phone IF EXISTS;
CREATE TABLE phone(id SERIAL, fio VARCHAR(255), num VARCHAR(255));
INSERT INTO phone(fio, num) VALUES ('Флока1','1111-1');
INSERT INTO phone(fio, num) VALUES ('Иванов1','222-1');