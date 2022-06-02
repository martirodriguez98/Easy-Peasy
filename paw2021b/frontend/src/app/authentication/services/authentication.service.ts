import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {environment} from "../../../environments/environment";
import {BehaviorSubject, concatMap, Observable, of, tap} from "rxjs";
import {Session} from "../../model/session.model";
import {UserService} from "./user.service";
import jwtDecode from "jwt-decode";
import {catchError} from "rxjs/operators";

interface Jwt {
  exp: number,
  iat: number,
  roles: string,
  userUrl: string;
}

export interface RegisterData {
  username: string,
  email: string,
  password: string
}

@Injectable({providedIn: 'root'})
export class AuthenticationService {
  session = new BehaviorSubject<Session>(null);

  constructor(
    private http: HttpClient,
    private router: Router,
    private userService: UserService
  ) {
  }

  register(registerData: RegisterData) {
    return this.http
      .post<Session>(
        environment.apiBaseUrl + '/users',
        {
          ...registerData
        },
        {
          observe: "response"
        }
      )
  }

  login(username: string, password: string) {
    return this.http
      .post(
        environment.apiBaseUrl + '/login',
        {},
        {
          headers: new HttpHeaders().set('Authorization', "Basic " + btoa(username + ":" + password)),
          observe: "response",
        }
      )
      .pipe(
        tap(res => {
          this.handleNewSession(res);
        })
      );
  }

  autoLogin: Observable<boolean> = of(true).pipe(
    concatMap(() => {
      this.handleAutoLogin();
      if (!this.session.getValue()) {
        return of(true);
      }
      return this.http.head(
        environment.apiBaseUrl + '/recipes',
        {
          observe: "response"
        }
      ).pipe(
        catchError(() => {
          return of(true)
        }),
        concatMap((res: HttpResponse<Object>) => {
          if (res as unknown as boolean === true) {
            return of(true);
          }
          this.handleJWT(res);
          return this.userService.fillUserData()
            .pipe(
              catchError(() => {
                return of(true);
              }),
              concatMap(() => {
                  return of(true);
                }
              )
            )
        }),
      )
    })
  )

  handleNewSession(res: HttpResponse<Object>) {
    const refreshToken = res.headers.get("X-Refresh-Token");
    localStorage.setItem("refresh-token", refreshToken);
    this.handleJWT(res);
  }

  handleJWT(res: HttpResponse<Object>) {
    const authHeader = res.headers.get("X-JWT");
    const token = authHeader.split(" ")[1];
    this.handleAuthentication(token)
  }

  handleAutoLogin() {
    const refreshToken = localStorage.getItem("refresh-token");
    if (refreshToken) {
      this.session.next(new Session(null, null, refreshToken));
    }
  }

  logout() {
    this.session.next(null);
    localStorage.removeItem("refresh-token");
    this.router.navigate(['login']).then(() => {
      this.userService.removeUser();
    });
  }

  private handleAuthentication(token: string) {
    const jwt = this.decodeToken(token);

    const milliExpirationTime = (jwt.exp - jwt.iat) * 1000;
    const refreshToken = localStorage.getItem("refresh-token")
    const expirationDate = new Date(new Date().getTime() + milliExpirationTime - 5 * 1000);

    const newSession = new Session(token, expirationDate, refreshToken);
    this.userService.setRoles(jwt.roles.split(" "));
    this.userService.setId(parseInt(jwt.userUrl.match(/users\/\d+/)[0].split("/")[1]));
    this.session.next(newSession);
  }

  private decodeToken(token: string): Jwt {
    return jwtDecode(token);
  }

  reqResetPassword(email: string) {
    return this.http.post(environment.apiBaseUrl + '/users/passwordResetEmail', {email}, {observe: 'response'});
  }

  resetPassword(token: string, password: string) {
    return this.http.put(environment.apiBaseUrl + '/users/passwordReset', {token,password}, {observe: 'response'});
  }

  public resendVerification(){
    return this.http.post(environment.apiBaseUrl + '/users/verification/',{},
    {
      observe: "response"
    });
  }
  public tryVerification(sentToken: string){
    return this.http
      .put(
        environment.apiBaseUrl + '/users/verification',
        {},
        {
          params : {token: sentToken},
          observe: "response"
        },
      )
  }
}
