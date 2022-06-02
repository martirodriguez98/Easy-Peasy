import {Injectable} from "@angular/core";
import {User} from "../../model/user.model";
import {Report} from "../../model/report.model";
import {environment} from "../../../environments/environment";
import {BehaviorSubject, concatMap, of, Subject} from "rxjs";
import {tap} from 'rxjs/operators';
import {HttpClient, HttpParams, HttpResponse, HttpStatusCode} from "@angular/common/http";
import {Recipe} from "../../model/recipe.model";
import {RecipePaginationQuery, RecipePaginationResult, SearchService} from "../../search/search.service";

export interface UserPaginationResult {
  pages: number;
  results: User[];
}

export interface AdminPanelPaginationQuery {
  page: number;
  pageSize?: number;
  orderBy?: string;
}

export interface ReportsPaginationResult {
  pages: number;
  results: Report[];
}

@Injectable({providedIn: 'root'})
export class UserService {
  user = new BehaviorSubject<User>(null);
  banned = new Subject<UserPaginationResult>();
  admins = new Subject<UserPaginationResult>();
  reports = new Subject<ReportsPaginationResult>();
  userRecipes = new Subject<RecipePaginationResult>();
  userFaves = new Subject<RecipePaginationResult>();

  constructor(
    private http: HttpClient
  ) {
  }

  fillUserData() {
    return this.http
      .get<User>(
        environment.apiBaseUrl + '/users/' + this.user.getValue().id
      ).pipe(
        tap(
          res => {
            this.user.next({...this.user.getValue(), ...res});
          }
        ),
        concatMap(res => {
          return of(this.user.getValue());
        })
      )
  }

  setId(id: number) {
    this.user.next({
      ...this.user.getValue(),
      id
    });
  }

  setRoles(roles: string[]) {
    this.user.next({
      ...this.user.getValue(),
      roles
    });
  }

  removeUser() {
    this.user.next(null);
  }

  getUser(id: number) {
    return this.http.get<User>(environment.apiBaseUrl + '/users/' + id);
  }


  getUserRecipes(id: number, recipePq: RecipePaginationQuery) {
    return this.http.get<Recipe[]>(environment.apiBaseUrl + '/users/' + id + '/recipes', {
      observe: "response",
      params: new HttpParams({fromObject: {...recipePq}})
    },).subscribe(r => {
      if (r.status === HttpStatusCode.NoContent) {
        this.userRecipes.next({
          pages: 0, results: []
        });
      } else {
        const rr: RecipePaginationResult = SearchService.parsePaginationResult(r);
        this.userRecipes.next(rr);
      }
    });
  }

  getUserFavourites(id: number, favePq: RecipePaginationQuery) {
    return this.http.get<Recipe[]>(environment.apiBaseUrl + '/users/' + id + '/favourites', {
      observe: "response",
      params: new HttpParams({fromObject: {...favePq}})
    },).subscribe(r => {
      if (r.status === HttpStatusCode.NoContent) {
        this.userFaves.next({
          pages: 0, results: []
        });
      } else {
        const rr: RecipePaginationResult = SearchService.parsePaginationResult(r);
        this.userFaves.next(rr);
      }
    });
  }

  getAdmins(adminPq: AdminPanelPaginationQuery) {
    return this.http.get<User[]>(environment.apiBaseUrl + '/users/admin', {
      observe: "response",
      params: new HttpParams({fromObject: {...adminPq}})
    },).subscribe(r => {
      if (r.status === HttpStatusCode.NoContent) {
        this.admins.next({
          pages: 0, results: []
        });
      } else {
        const ar: UserPaginationResult = UserService.parseUserPaginationResult(r);
        this.admins.next(ar);
      }
    });
  }

  getBanned(adminPq: AdminPanelPaginationQuery) {
    return this.http.get<User[]>(environment.apiBaseUrl + '/users/banned', {
      observe: "response",
      params: new HttpParams({fromObject: {...adminPq}})
    },).subscribe(r => {
      if (r.status === HttpStatusCode.NoContent) {
        this.banned.next({
          pages: 0, results: []
        });
      } else {
        const br: UserPaginationResult = UserService.parseUserPaginationResult(r);
        this.banned.next(br);
      }
    });
  }

  ban(id: number) {
    return this.http.put(environment.apiBaseUrl + '/users/banned/' + id, id);
  }

  unban(id: number) {
    return this.http.delete(environment.apiBaseUrl + '/users/banned/' + id);
  }

  makeAdmin(id: number) {
    return this.http.put(environment.apiBaseUrl + '/users/admin/' + id, id);
  }

  removeAdmin(id: number) {
    return this.http.delete(environment.apiBaseUrl + '/users/admin/' + id);
  }

  static parseUserPaginationResult(r: HttpResponse<User[]>): UserPaginationResult {

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

  updateProfileImage(imageUpload: FormData) {
    return this.http.put<FormData>(environment.apiBaseUrl + '/users/' + this.user.getValue().id + '/profileImage', imageUpload).pipe(tap(() => {
      this.fillUserData().subscribe((_) => {
      }, (_) => {
      });
    }));
  }

  reportUser(formData: FormData){
    return this.http.post<FormData>(environment.apiBaseUrl + '/users/reports',formData, {observe: 'response'});
  }

  getReports(reportsPq: AdminPanelPaginationQuery) {
    return this.http.get<Report[]>(environment.apiBaseUrl + '/users/reports', {
      observe: "response",
      params: new HttpParams({fromObject: {...reportsPq}})
    },).subscribe(r => {
      if (r.status === HttpStatusCode.NoContent) {
        this.reports.next({
          pages: 0, results: []
        });
      } else {
        const br: ReportsPaginationResult = UserService.parseReportsPaginationResult(r);
        this.reports.next(br);
      }
    });
  }

  deleteReport(id: number) {
    return this.http.delete(environment.apiBaseUrl + '/users/reports/' + id, {});
  }

  deleteUserReports(id: number) {
    return this.http.delete(environment.apiBaseUrl + '/users/' + id + '/reports', {})
  }

  static parseReportsPaginationResult(r: HttpResponse<Report[]>): ReportsPaginationResult {
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
}

