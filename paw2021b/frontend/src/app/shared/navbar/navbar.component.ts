import { Component, OnInit } from '@angular/core';
import {UserService} from "../../authentication/services/user.service";
import {Subscription} from "rxjs";
import {User} from "../../model/user.model";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  private userSubscription: Subscription;
  user: User;

  constructor(
    private userService: UserService,
  ) { }

  ngOnInit(): void {
    this.userSubscription = this.userService.user.subscribe(user => {
      this.user = user;
    });
  }

  ngOnDestroy(){
  }

}
