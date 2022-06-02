import {Injectable} from '@angular/core';
import {Recipe} from "../model/recipe.model";
import {HttpClient, HttpParams, HttpResponse, HttpStatusCode} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Subject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {Category} from "../model/category.model";
import {Ingredient} from "../model/ingredient.model";
import {Comment} from "../model/comment.model";
import {tap} from "rxjs/operators";

enum Categories {
  BREAKFAST,
  DINNER,
  LUNCH,
  SNACK,
  BITTER,
  BITTERSWEET,
  SAVOURY,
  SPICY,
  SWEET,
  BAKERY,
  FISH,
  GLUTEN_FREE,
  HEALTHY,
  KOSHER,
  MEAT,
  PASTA,
  PASTRIES,
  SALADS,
  SIDE_DISHES,
  VEGAN,
  VEGETARIAN,
}

export interface CommentPaginationResult {
  pages: number;
  results: Comment[];
}

export interface CommentPaginationQuery{
  page: number;
  pageSize?: number;
}

@Injectable({
  providedIn: 'root'
})

export class RecipeService {

  constructor(
    private http: HttpClient,
    private translateService: TranslateService,
  ) {
  }

  comments = new Subject<CommentPaginationResult>();

  getRecipe(id: number) {
    return this.http.get<Recipe>(environment.apiBaseUrl + '/recipes/' + id);
  }

  getCategories(recipe: Recipe) {
    return this.http.get<Category[]>(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe + '/categories');
  }

  getIngredients(recipe: Recipe) {
    return this.http.get<Ingredient[]>(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe + '/ingredients');
  }

  getRecipeDifficultyString(difficulty: number) {
    switch (difficulty) {
      case 1: //easy
        return this.translateService.get("newRec.difficulty.easy");
      case 2: //medium
        return this.translateService.get("newRec.difficulty.medium");
      case 3: //hard
        return this.translateService.get("newRec.difficulty.hard");
      default:
        return this.translateService.get("newRec.difficulty.easy");
    }
  }

  getRecipeCategoryString(category: number) {
    switch (category) {
      case Categories.BREAKFAST:
        return this.translateService.get("category.daytime.breakfast");
      case Categories.DINNER:
        return this.translateService.get("category.daytime.dinner");
      case Categories.LUNCH:
        return this.translateService.get("category.daytime.lunch");
      case Categories.SNACK:
        return this.translateService.get("category.daytime.snack");
      case Categories.BITTER:
        return this.translateService.get("category.flavour.bitter");
      case Categories.BITTERSWEET:
        return this.translateService.get("category.flavour.bittersweet");
      case Categories.SAVOURY:
        return this.translateService.get("category.flavour.savoury");
      case Categories.SPICY:
        return this.translateService.get("category.flavour.spicy");
      case Categories.SWEET:
        return this.translateService.get("category.flavour.sweet");
      case Categories.BAKERY:
        return this.translateService.get("category.bakery");
      case Categories.FISH:
        return this.translateService.get("category.fish");
      case Categories.GLUTEN_FREE:
        return this.translateService.get("category.glutenFree");
      case Categories.HEALTHY:
        return this.translateService.get("category.healthy");
      case Categories.KOSHER:
        return this.translateService.get("category.kosher");
      case Categories.MEAT:
        return this.translateService.get("category.meat");
      case Categories.PASTA:
        return this.translateService.get("category.pasta");
      case Categories.PASTRIES:
        return this.translateService.get("category.pastries");
      case Categories.SALADS:
        return this.translateService.get("category.salads");
      case Categories.SIDE_DISHES:
        return this.translateService.get("category.sideDishes");
      case Categories.VEGAN:
        return this.translateService.get("category.vegan");
      case Categories.VEGETARIAN:
        return this.translateService.get("search.category.vegetarian");
      default:
        return this.translateService.get("search.category.vegetarian");
    }
  }

  getLikes(recipe: Recipe) {
    return this.http.get<number>(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe + '/likes');
  }

  getDislikes(recipe: Recipe) {
    return this.http.get<number>(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe + '/dislikes');
  }

  createRecipe(formData: FormData) {
    return this.http.post<FormData>(environment.apiBaseUrl + '/recipes', formData, {observe: 'response'});
  }


  getComments(commentPq : CommentPaginationQuery, recipe: Recipe){
    return this.http.get<Comment[]>(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe + '/comments',{
          observe: "response",
          params: new HttpParams({fromObject: {...commentPq}})
        },).subscribe(r => {
          if(r.status === HttpStatusCode.NoContent){
            this.comments.next({
              pages:0, results:[]
            });
          }else{
            const cr: CommentPaginationResult = RecipeService.parseCommentPaginationResult(r);
            this.comments.next(cr);
          }
    });
  }

  static parseCommentPaginationResult(r: HttpResponse<Comment[]>): CommentPaginationResult {

    const lastLink: string = r.headers
      .getAll('Link')
      .pop()
      .split(',')
      .filter((link) => (link.includes("last")))
      .pop()
      .match(/<(.*)>/)[1];

     const pages: number = Number(new URL(lastLink).searchParams.get("page")) + 1;
     return {
       pages,
       results: r.body,
     }
  }

  postComment(formData: FormData, recipe: Recipe) {
    return this.http.post<FormData>(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe + '/comments', formData, {observe: 'response'})
  }

  getSuggestedRecipes(recipe: Recipe) {
    return this.http.get<Recipe[]>(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe + '/suggested');
  }

  deleteRecipe(recipe: Recipe) {
    return this.http.delete(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe, {});
  }

  deleteComment(recipe: Recipe, idComment: number) {
    return this.http.delete(environment.apiBaseUrl + '/recipes/' + recipe.idRecipe + '/comments/' + idComment, {});
  }

  isFaved(id: number){
    return this.http.get(environment.apiBaseUrl + '/recipes/' + id + '/faved');
  }

  fav(id: number) {
    return this.http.put(environment.apiBaseUrl + '/recipes/' + id + '/favorite', {}).pipe(tap(()=>{
      this.getRecipe(id).subscribe(()=> {
      }, () => {
      })
    }));
  }

  unfav(id: number) {
    return this.http.delete(environment.apiBaseUrl + '/recipes/' + id + '/favorite', {}).pipe(tap(()=>{
      this.getRecipe(id).subscribe(()=> {
      }, () => {
      })
    }));
  }

  share(formData: FormData,id: number) {
    return this.http.post<FormData>(environment.apiBaseUrl + '/recipes/' + id +'/shared', formData, {observe: 'response'});
  }

  highlightRecipe(id : number) {
    return this.http.put(environment.apiBaseUrl + '/recipes/' + id +'/highlighted', {});
  }

  likeRecipe(id: number) {
    return this.http.put(environment.apiBaseUrl + '/recipes/' + id + '/likes', {});
  }

  isLiked(id: number){
    return this.http.get(environment.apiBaseUrl + '/recipes/' + id + '/liked');
  }

  dislikeRecipe(id: number) {
    return this.http.put(environment.apiBaseUrl + '/recipes/' + id + '/dislikes', {});
  }

  isDisliked(id: number){
    return this.http.get(environment.apiBaseUrl + '/recipes/' + id + '/disliked');
  }

  likedPercentage(id: number){
    return this.http.get(environment.apiBaseUrl + '/recipes/' + id + '/likedPercentage');
  }

  likedReviews(id: number){
    return this.http.get(environment.apiBaseUrl + '/recipes/' + id + '/likedReviews');
  }
}
