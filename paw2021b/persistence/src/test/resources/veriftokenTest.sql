TRUNCATE TABLE verify_tokens RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE user_t RESTART IDENTITY AND COMMIT NO CHECK;

insert into user_t(id_user, username, email, password, privileged)
values(1,'menganito', 'menganito@mail.com', 'contrasegura',false);

insert into user_t(id_user, username, email, password, privileged)
values(2,'fulanito', 'fulanito@mail.com', 'contrasegura',false);

insert into verify_tokens(id_user,token,expiration_date, id)
values(2, 'token','2021-04-10', 10);

