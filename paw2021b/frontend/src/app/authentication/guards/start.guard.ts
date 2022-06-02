import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class StartGuard implements CanActivate {
  constructor(
    private authService: AuthenticationService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot,
              router: RouterStateSnapshot,
  ): |
    boolean
    | UrlTree
    | Promise<boolean | UrlTree>
    | Observable<boolean | UrlTree> {
    return this.authService.autoLogin;
  }

}
