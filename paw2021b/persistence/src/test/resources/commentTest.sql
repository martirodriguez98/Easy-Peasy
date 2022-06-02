TRUNCATE TABLE user_t RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE recipe RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE comment RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE roles RESTART IDENTITY AND COMMIT NO CHECK;

insert into user_t(id_user, username, email, password, privileged)
values(1, 'menganito', 'menganito@mail.com', 'contrasegura', false);

insert into roles(id_user, role_t, id)
values(1, 'USER', 6);

insert into roles(id_user, role_t, id)
values(1, 'VERIFIED', 7);

insert into recipe(id_recipe, recipe_title, recipe_desc, recipe_steps, id_user, recipe_difficulty, is_highlighted, recipe_time, recipe_date_created, likescount)
values(1, 'title', 'desc', 'steps', 1, 1, false, '1 min', null, 0);

insert into recipe(id_recipe, recipe_title, recipe_desc, recipe_steps, id_user, recipe_difficulty, is_highlighted, recipe_time, recipe_date_created, likescount)
values(2, 'title', 'desc', 'steps', 1, 1, false, '1 min', null, 0);

insert into comment(id_comment, id_recipe, id_user, comment_desc, date_created)
values(2, 1, 1, 'genial', '2021-10-05');