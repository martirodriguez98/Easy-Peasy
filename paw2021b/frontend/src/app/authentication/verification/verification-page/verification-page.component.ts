import { Component, OnInit } from '@angular/core';
import {faHome} from "@fortawesome/free-solid-svg-icons";
import {AuthenticationService} from "../../services/authentication.service";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-verification-page',
  templateUrl: './verification-page.component.html',
  styleUrls: ['./verification-page.component.scss']
})
export class VerificationPageComponent implements OnInit {

  homeIcon = faHome;
  loaded = false;
  verified = "false";
  constructor(
    private authService : AuthenticationService,
    private route: ActivatedRoute,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe((e) => {
      const params = this.route.snapshot.queryParams;
      const token = params["token"];
      this.loaded = true;
      if(token != null){
        this.authService.tryVerification(token).subscribe(r => {
          this.userService.fillUserData().subscribe(() => {
            this.loaded = true;
          })
          this.verified = r.headers.get("verified");
      })
      }
    })
  }

}
