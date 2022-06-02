create table if not exists user_t
(
    id_user serial primary key,
    nickname varchar(50),
    username varchar(30),
    email varchar(254) not null,
    password varchar(30),
    avatar BYTEA,
    mime_type TEXT,
    privileged boolean,
    date_created date
);

create table if not exists recipe
(
    id_recipe serial primary key,
    recipe_title varchar(100) not null,
    recipe_desc text not null,
    recipe_steps text,
    id_user integer not null,
    recipe_difficulty integer,
    is_highlighted boolean,
    recipe_time varchar(50),
    recipe_date_created date,
    likescount bigint not null,
    FOREIGN KEY (id_user) REFERENCES user_t(id_user) ON DELETE CASCADE
);

create table if not exists recipe_ingredients
(
    id_ingredient serial primary key,
    id_recipe integer not null,
    ingredient_name varchar(30) not null ,
    ingredient_quantity varchar(50) not null,
    FOREIGN KEY(id_recipe) REFERENCES recipe(id_recipe) ON DELETE CASCADE

);

create table if not exists recipe_categories
(
    id_recipe integer not null,
    category integer not null,
    FOREIGN KEY(id_recipe) REFERENCES recipe(id_recipe) ON DELETE CASCADE
);

create table if not exists verify_tokens
(
    id_user integer not null,
    token text,
    expiration_date date not null,
    FOREIGN KEY(id_user) REFERENCES user_t(id_user) ON DELETE CASCADE
);

create table if not exists password_tokens
(
    id_user integer not null,
    token text,
    expiration_date date not null,
    FOREIGN KEY(id_user) REFERENCES user_t(id_user) ON DELETE CASCADE
);

create table if not exists comment
(
    id_comment serial primary key,
    id_recipe integer not null,
    id_user integer not null,
    comment_desc text not null,
    date_created date not null,
    FOREIGN KEY(id_recipe) REFERENCES recipe(id_recipe) ON DELETE CASCADE,
    FOREIGN KEY(id_user) REFERENCES user_t(id_user) ON DELETE CASCADE
);

create table if not exists favourites
(
    id_user integer not null,
    id_recipe integer not null,
    FOREIGN KEY(id_user) REFERENCES user_t(id_user) ON DELETE CASCADE,
    FOREIGN KEY(id_recipe) REFERENCES recipe(id_recipe) ON DELETE CASCADE
);

create table if not exists images(
                                     id_image serial primary key,
                                     id_recipe integer not null,
                                     image_data bytea,
                                     mime_type text,
                                     FOREIGN KEY(id_recipe) REFERENCES recipe(id_recipe) ON DELETE CASCADE
);

create table if not exists roles(
                                    role_t TEXT NOT NULL,
                                    id_user integer not null,
                                    FOREIGN KEY (id_user) REFERENCES user_t (id_user) ON DELETE CASCADE
);

create table if not exists liked_recipes
(
    id_recipe integer not null,
    id_user integer not null,
    FOREIGN KEY(id_user) REFERENCES user_t(id_user) ON DELETE CASCADE,
    FOREIGN KEY(id_recipe) REFERENCES recipe(id_recipe) ON DELETE CASCADE
);

create table if not exists disliked_recipes
(
    id_recipe integer not null,
    id_user integer not null,
    FOREIGN KEY(id_user) REFERENCES user_t(id_user) ON DELETE CASCADE,
    FOREIGN KEY(id_recipe) REFERENCES recipe(id_recipe) ON DELETE CASCADE
);


