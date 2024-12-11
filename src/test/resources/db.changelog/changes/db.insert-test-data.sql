INSERT INTO users (first_name, last_name, login, password, phone, role)
VALUES ('User1FirstName', 'User1LastName', 'user1@gmail.com',
        '$2a$10$MHL3Zn3W4IjGtGkEQ3/t0ef2rJkJjRoNn2D1TroRTez1SlKZaC0Wu', '+7 (000) 000-00-01', 'USER');

INSERT INTO users (first_name, last_name, login, password, phone, role)
VALUES ('User2FirstName', 'User2LastName', 'user2@gmail.com',
        '$2a$10$MHL3Zn3W4IjGtGkEQ3/t0ef2rJkJjRoNn2D1TroRTez1SlKZaC0Wu', '+7 (000) 000-00-02', 'USER');

INSERT INTO users (first_name, last_name, login, password, phone, role)
VALUES ('AdminFirstName', 'AdminLastName', 'admin@gmail.com',
        '$2a$10$MHL3Zn3W4IjGtGkEQ3/t0ef2rJkJjRoNn2D1TroRTez1SlKZaC0Wu', '+7 (000) 000-00-03', 'ADMIN');

INSERT INTO category (category_title, user_id)
VALUES ('Category 1', 1);
INSERT INTO category (category_title, user_id)
VALUES ('Category 2', 1);
INSERT INTO category (category_title, user_id)
VALUES ('Category 3', 1);
INSERT INTO category (category_title, user_id)
VALUES ('Category 4', 1);
INSERT INTO category (category_title, user_id)
VALUES ('Category 5', 1);

INSERT INTO tasks (task_title, title, category_id, status, user_id, created_task)
VALUES ('Task1', 'HIGH', 1, 'OPEN', 1 , '2023-10-11T14:30:00.000003');
INSERT INTO tasks (task_title, title, category_id, status, user_id, created_task)
VALUES ('Task2', 'LOW', 1, 'OPEN', 1 , '2023-10-12T14:30:00.000001');
INSERT INTO tasks (task_title, title, category_id, status, user_id, created_task)
VALUES ('Task3', 'HIGH', 2, 'DONE', 1, '2023-12-12T14:30:00.000002');
INSERT INTO tasks (task_title, title, category_id, status, user_id, created_task)
VALUES ('Task4', 'LOW', 2, 'DONE', 1, '2023-12-18T14:30:00.000003');