INSERT INTO board(name, role) VALUES ('개발일지', 'ADMIN') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO board(name, role) VALUES ('방명록', 'USER') ON DUPLICATE KEY UPDATE name=name;