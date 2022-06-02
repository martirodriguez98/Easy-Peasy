import {Injectable} from '@angular/core';
import {NavigationCancel, Router, RoutesRecognized} from "@angular/router";
import {filter, pairwise} from "rxjs/operators";

@Injectable({providedIn: 'root'})
export class PreviousRouteService {

  private previousUrl = null;
  private authRedirect = false;

  constructor(
    private router: Router
  ) {
    this.router.events.pipe(
      filter(e => e instanceof RoutesRecognized || e instanceof NavigationCancel)
      , pairwise()
    )
      .subscribe((event: any[]) => {
        this.previousUrl = event[0].url;
      });
  }

  getPreviousUrl() {
    return this.previousUrl;
  }

  setAuthRedirect(value: boolean) {
    this.authRedirect = value;
  }

  getAuthRedirect() {
    return this.authRedirect;
  }

}
