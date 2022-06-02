import { Component, OnInit } from '@angular/core';
import {faHome} from "@fortawesome/free-solid-svg-icons";
import {AuthenticationService} from "../services/authentication.service";

@Component({
  selector: 'app-banned',
  templateUrl: './banned.component.html',
  styleUrls: ['./banned.component.scss']
})
export class BannedComponent implements OnInit {

  homeIcon = faHome;

  constructor(private authService : AuthenticationService,) {
  }

  ngOnInit(): void {

  }

  logout() {
    this.authService.logout();
  }
}
