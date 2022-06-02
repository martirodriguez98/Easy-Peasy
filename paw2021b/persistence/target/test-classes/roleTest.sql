TRUNCATE TABLE user_t RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE roles RESTART IDENTITY AND COMMIT NO CHECK;


insert into user_t(id_user, username, email, password, privileged)
values(1, 'menganito', 'menganito@mail.com', 'contrasegura', false);

insert into user_t(id_user, username, email, password, privileged)
values(2, 'fulanito', 'fulanito@mail.com', 'contrasegura', false);

insert into roles(id_user, role_t, id)
values(1, 'USER', 6);

insert into roles(id_user, role_t, id)
values(1, 'VERIFIED', 7);

insert into roles(id_user, role_t, id)
values(1, 'ADMIN', 8);
