-- ����� ��������� windows-1251, �� utf-8
DROP TABLE phone IF EXISTS;
CREATE TABLE phone(id SERIAL, fio VARCHAR(255), num VARCHAR(255));
INSERT INTO phone(fio, num) VALUES ('�����1','1111-1');
INSERT INTO phone(fio, num) VALUES ('������1','222-1');