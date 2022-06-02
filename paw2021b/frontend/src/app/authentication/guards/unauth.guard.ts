import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Route, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {PreviousRouteService} from "../services/previous-route.service";
import {map, Observable, take} from "rxjs";

@Injectable({providedIn: 'root'})
export class UnauthGuard implements CanActivate {

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private previousRouteService: PreviousRouteService
  ) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | boolean
    | UrlTree
    | Promise<boolean | UrlTree>
    | Observable<boolean | UrlTree> {
    return this.authService.session.pipe(
      take(1),
      map(session => {
        if (!session) {
          return true;
        }
        let previousRoute = this.previousRouteService.getPreviousUrl();
        previousRoute = !!previousRoute ? previousRoute : "/404"
        return this.router.createUrlTree([previousRoute]);
      })
    );
  }

}
