import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {PreviousRouteService} from "../services/previous-route.service";
import {map, Observable, take} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private previousRouteService: PreviousRouteService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot,
              router: RouterStateSnapshot,
  ): |
    boolean
    | UrlTree
    | Promise<boolean | UrlTree>
    | Observable<boolean | UrlTree> {
    return this.authService.session.pipe(
      take(1),
      map(session => {
        if (!!session) {
          return true;
        }
        this.previousRouteService.setAuthRedirect(true);
        return this.router.createUrlTree(['/login']);
      })
    );
  }

}
