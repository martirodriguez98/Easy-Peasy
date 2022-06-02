TRUNCATE TABLE user_t RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE roles RESTART IDENTITY AND COMMIT NO CHECK;

-- USER
insert into user_t(id_user, username, email, password, privileged)
values(1,'menganito', 'menganito@mail.com', 'contrasegura', false);

insert into roles(id_user, role_t, id)
values(1, 'ADMIN', 40);

insert into roles(id_user, role_t, id)
values(1, 'BANNED', 41);
