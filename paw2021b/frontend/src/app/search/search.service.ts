import {Recipe} from "../model/recipe.model";
import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {HttpClient, HttpErrorResponse, HttpParams, HttpResponse, HttpStatusCode} from "@angular/common/http";
import {Router} from "@angular/router";
import {environment} from "../../environments/environment";
import {User} from "../model/user.model";
import {UserPaginationResult, UserService} from "../authentication/services/user.service";

export interface RecipePaginationQuery{
  query?: string;
  order?: string;
  page: number;
  pageSize: number;
  categories?: number[];
  difficulty?: number;
  highlighted?: boolean;
}

export interface RecipePaginationResult{
  pages: number;
  results: Recipe[];
}

export interface UsersPaginationQuery{
  query?: string;
  order?: string;
  page: number;
  pageSize: number;
  admins?: boolean;
  highlighted?: boolean;
}

@Injectable({providedIn: 'root'})
export class SearchService {
  resultsUsers = new Subject<UserPaginationResult>();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
  }

  getRecipes(rp: RecipePaginationQuery, results: Subject<RecipePaginationResult>){
    this.http.get<Recipe[]>(
      environment.apiBaseUrl + '/recipes',
      {
        observe: "response",
        params: new HttpParams({fromObject: {...rp}})
      },
    ).subscribe((res) => {
        if (res.status === HttpStatusCode.NoContent) {
          results.next({
            pages: 0,
            results: []
          });
        } else {
          const rr: RecipePaginationResult = SearchService.parsePaginationResult(res);
          results.next(rr);
        }
      },
      (error: HttpErrorResponse) => {
        if (error.status === HttpStatusCode.NotFound) {
          this.router.navigate(['404']);
        } else {
          this.router.navigate(['500']);
        }
      });
  }


  getUsers(up: UsersPaginationQuery){
    this.http.get<User[]>(
      environment.apiBaseUrl + '/users',
      {
        observe: "response",
        params: new HttpParams({fromObject: {...up}})
      },
    ).subscribe((res) => {
      if(res.status === HttpStatusCode.NoContent) {
        this.resultsUsers.next({
          pages: 0,
          results: []
        });
      }else {
        const ur: UserPaginationResult = UserService.parseUserPaginationResult(res);
        this.resultsUsers.next(ur);
      }
    },
      (error: HttpErrorResponse) => {
        if (error.status === HttpStatusCode.NotFound) {
          this.router.navigate(['404']);
        } else {
          this.router.navigate(['500']);
        }
      });
  }

  static parsePaginationResult(res: HttpResponse<Recipe[]>): RecipePaginationResult {
    const lastLink: string = res.headers
      .getAll('Link')
      .pop()
      .split(',')
      .filter((link) => (link.includes("last")))
      .pop()
      .match(/<(.*)>/)[1];
    const pages: number = Number(new URL(lastLink).searchParams.get("page")) + 1;
    return <RecipePaginationResult>{
      pages,
      results: res.body
    }
  }

}
