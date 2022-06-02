TRUNCATE TABLE user_t RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE recipe RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE roles RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE recipe_categories RESTART IDENTITY AND COMMIT NO CHECK;

insert into user_t(id_user, username, email, password, privileged)
values(1, 'menganito', 'menganito@mail.com', 'contrasegura', false);

insert into roles(id_user, role_t, id)
values(1, 'USER', 1);

insert into roles(id_user, role_t, id)
values(1, 'VERIFIED', 2);

insert into user_t(id_user, username, email, password, privileged)
values(2, 'fulanito', 'fulanito@mail.com', 'contrasegura', false);

insert into roles(id_user, role_t, id)
values(2, 'USER', 3);

insert into roles(id_user, role_t, id)
values(2, 'VERIFIED', 4);

insert into recipe(id_recipe, recipe_title, recipe_desc, recipe_steps, id_user, recipe_difficulty, is_highlighted, recipe_time, recipe_date_created, likescount)
values(1, 'title', 'desc', 'steps', 1, 1, false, '1 min', null, 0);
insert into recipe(id_recipe, recipe_title, recipe_desc, recipe_steps, id_user, recipe_difficulty, is_highlighted, recipe_time, recipe_date_created, likescount)
values(2, 'title', 'desc', 'steps', 1, 1, false, '1 min', null,0);
insert into recipe(id_recipe, recipe_title, recipe_desc, recipe_steps, id_user, recipe_difficulty, is_highlighted, recipe_time, recipe_date_created, likescount)
values(3, 'title', 'desc', 'steps', 1, 1, false, '1 min', null, 0);
insert into recipe(id_recipe, recipe_title, recipe_desc, recipe_steps, id_user, recipe_difficulty, is_highlighted, recipe_time, recipe_date_created, likescount)
values(4, 'title', 'desc', 'steps', 2, 1, false, '1 min', null, 0);
insert into favourites(id_user, id_recipe, id)
values(1, 1, 5);
insert into liked_recipes(id_user, id_recipe, id)
values(1, 2, 5);
insert into disliked_recipes(id_user, id_recipe, id)
values(1, 3, 6);
insert into recipe_categories(id_recipe, category, id)
values(1, 5, 5);