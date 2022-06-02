import {TranslateService} from "@ngx-translate/core";
import {AuthenticationService} from "./authentication.service";
import {
  HttpErrorResponse,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
  HttpStatusCode
} from "@angular/common/http";
import {catchError, exhaustMap, map, take} from "rxjs/operators";
import {throwError} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(
    private translateService: TranslateService,
    private authService: AuthenticationService
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return this.authService.session.pipe(
      take(1),
      exhaustMap(token => {

        req = req.clone({headers: req.headers.set('Accept-Language', this.translateService.currentLang)});
        if(!token)
          return next.handle(req);

        const authRequest = req.clone({
          headers: req.headers.set('Authorization', "Bearer " + token.token)
        });

        return next.handle(authRequest)
          .pipe(
            // @ts-ignore
            catchError((err) => {
                if (err instanceof HttpErrorResponse) {
                  if (err.status === HttpStatusCode.Unauthorized) {
                    this.authService.logout();
                  }
                  return throwError(err);
                }
              }
            ),
            // @ts-ignore
            map(resp => {
              if (resp instanceof HttpResponse) {
                if (resp.headers.has("X-JWT")) {
                  this.authService.handleJWT(resp);
                }
                return resp;
              }
            })
          );
      })
    );
  }
}
