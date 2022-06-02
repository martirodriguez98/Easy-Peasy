import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {PreviousRouteService} from "../services/previous-route.service";
import {map, take} from "rxjs/operators";
import {UserService} from "../services/user.service";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(
    private userService: UserService,
    private router: Router,
    private previousRouteService: PreviousRouteService
  ) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.userService.user.pipe(
      take(1),
      map(user => {
        if (!!user && user.roles.includes("ADMIN")) {
          return true;
        }
        let previousRoute = this.previousRouteService.getPreviousUrl();
        previousRoute = !!previousRoute ? previousRoute : "/404"
        return this.router.createUrlTree([previousRoute]);;
      })
    );
  }

}
