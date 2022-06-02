import {getTestBed, TestBed} from '@angular/core/testing';

import {CommentPaginationResult, RecipeService} from './recipe.service';
import {HttpTestingController} from "@angular/common/http/testing";
import {UserService} from "../authentication/services/user.service";
import {TestingModule} from "../testing.module";
import {User} from "../model/user.model";
import {Recipe} from "../model/recipe.model";
import {HttpStatusCode} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Category} from "../model/category.model";
import {Ingredient} from "../model/ingredient.model";

describe('RecipeService', () => {
  let service: RecipeService;
  let injector: TestBed;
  let userService: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TestingModule],
      providers: [RecipeService, UserService]
    });
    injector = getTestBed();
    service = TestBed.inject(RecipeService);
    userService = injector.inject(UserService);
    httpMock = injector.inject(HttpTestingController);
  });

  it('getRecipe() should GET and return a recipe', () => {
    let id = 1;
    const user = new User(['USER'], 1, 'surname', 'email', null, '', 0, 0);
    const recipe = new Recipe(1, user, 'title', 'desc', 'steps', 1, false, '1', null, 0, null, 'url', 0)

    service.getRecipe(id).subscribe((res) => {
      expect(res).toEqual(recipe);
    });

    const req = httpMock.expectOne(environment.apiBaseUrl + '/recipes/' + id);
    expect(req.request.method).toBe('GET');
    req.flush(recipe, {
      status: HttpStatusCode.Created, statusText: HttpStatusCode.Created.toString(),
    });
  });

  it('getCategories() should GET and return recipes categories', () => {
    let id = 1;
    const user = new User(['USER'], 1, 'surname', 'email', null, '', 0, 0);
    const recipe = new Recipe(1, user, 'title', 'desc', 'steps', 1, false, '1', null, 0, null, 'url', 0)
    const category = [new Category(1)];

    service.getCategories(recipe).subscribe((res) => {
      expect(res).toEqual(category);
    });

    const req = httpMock.expectOne(environment.apiBaseUrl + '/recipes/' + id + '/categories');
    expect(req.request.method).toBe('GET');
    req.flush(category, {
      status: HttpStatusCode.Created, statusText: HttpStatusCode.Created.toString(),
    });
  });

  it('getIngredients() should GET and return recipes ingredients', () => {
    let id = 1;
    const user = new User(['USER'], 1, 'surname', 'email', null, '', 0, 0);
    const recipe = new Recipe(1, user, 'title', 'desc', 'steps', 1, false, '1', null, 0, null, 'url', 0);
    const ingredients = [new Ingredient(1, 'ing', '2')];

    service.getIngredients(recipe).subscribe((res) => {
      expect(res).toEqual(ingredients);
    });

    const req = httpMock.expectOne(environment.apiBaseUrl + '/recipes/' + id + '/ingredients');
    expect(req.request.method).toBe('GET');
    req.flush(ingredients, {
      status: HttpStatusCode.Created, statusText: HttpStatusCode.Created.toString(),
    });
  });

  it('createRecipe() should POST and return created', () => {
    let recipeData = new FormData();
    recipeData.append('title', 'title');
    recipeData.append('description', 'description');
    recipeData.append('time', 'time');
    recipeData.append('steps', 'steps');
    recipeData.append('difficulty', 'difficulty');

    service.createRecipe(recipeData).subscribe((res) => {
      expect(res.status).toEqual(HttpStatusCode.Created);
    });

    const req = httpMock.expectOne(environment.apiBaseUrl + '/recipes');
    expect(req.request.method).toBe('POST');
    req.flush({}, {status: HttpStatusCode.Created, statusText: HttpStatusCode.Created.toString()})
  });

});
