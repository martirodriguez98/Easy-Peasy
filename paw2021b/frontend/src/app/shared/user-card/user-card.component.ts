import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/user.model";
import {UserService} from "../../authentication/services/user.service";
import {faBan, faCutlery, faShield, faAward} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss']
})
export class UserCardComponent implements OnInit {

  @Input("user") user!: User;
  faShield = faShield;
  faBan = faBan;
  faCutlery = faCutlery;
  faAward = faAward;
  constructor(
    public userService: UserService
  ) { }

  ngOnInit(): void {
  }

}
