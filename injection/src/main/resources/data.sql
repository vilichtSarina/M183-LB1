--USERS
insert into users (id, username, email, password)
values (0, 'sarina', 'sarina@example.com', 'sarina'),
       (1, 'masha', 'masha@example.com', 'masha')
ON CONFLICT DO NOTHING;

--AUTHORITIES
INSERT INTO authority(id, name)
VALUES (0, 'DEFAULT'),
       (1, 'USER_MODIFY'),
       (2, 'USER_DELETE'),
       (3, 'MODIFY_FOREIGN_POST'),
       (4, 'DELETE_FOREIGN_POST')
ON CONFLICT DO NOTHING;

--ROLES
INSERT INTO role(id, name)
VALUES (0, 'DEFAULT'),
       (1, 'USER_MODIFY'),
       (2, 'USER_DELETE'),
       (3, 'ADMIN')
ON CONFLICT DO NOTHING;

--assign authorities to roles
INSERT INTO role_authority(role_id, authority_id)
VALUES (0, 0),
       (1, 1),
       (2, 2),
       (3, 1),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4)
ON CONFLICT DO NOTHING;

--assign roles to users
insert into users_role (users_id, role_id)
values (0, 3),
       (1, 3)
ON CONFLICT DO NOTHING;

--create initial notes
insert into note (id, title, content)
VALUES (0, 'sarinanote', 'This is sarinas note content'),
       (1, 'mashanote', 'This is mashas note content')
ON CONFLICT DO NOTHING;

insert into note_users (users_id, note_id)
values (0, 0),
       (1, 1)
ON CONFLICT DO NOTHING;



