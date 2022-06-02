import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../../model/user.model";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../authentication/services/user.service";
import {faBan, faCircleExclamation, faGears, faShield} from "@fortawesome/free-solid-svg-icons";
import {Subscription} from "rxjs";
import {RecipePaginationQuery, RecipePaginationResult} from "../../../search/search.service";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit, OnDestroy {
  profileUser: User;
  loaded = false;
  loadedRecipes = false;
  user: User;
  userSub : Subscription;
  recipesSub : Subscription;
  managerIcon = faGears;
  adminIcon = faShield;
  bannedIcon = faBan;
  adminActionIcon = faCircleExclamation;
  recipePr: RecipePaginationResult = {
    pages: 0,
    results: [],
  }
  recipePq: RecipePaginationQuery = {
    page: 0,
    pageSize: 4,
  }

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,

  ) { }

  ngOnInit(): void {
    const userId = this.route.snapshot.params['id'];
    this.userSub = this.userService.user.subscribe(user => {
      this.user = user;
    });
    this.getUserData(userId);
    this.userService.getUserRecipes(userId, this.recipePq);
    this.recipesSub = this.userService.userRecipes.subscribe(r => {
      this.recipePr = {
        ...this.recipePr,
        ...r
      };
      this.loadedRecipes = true;
    });
  }

  ngOnDestroy() {
    this.recipesSub.unsubscribe();
  }

  onRecipePageChange(page: number){
    this.recipePq.page = page;
    this.userService.getUserRecipes(this.profileUser.id, this.recipePq);
  }

  tryBan(id: number){
    this.userService.ban(id).subscribe(r => {
    this.getUserData(id);
    });
  }
  tryUnban(id: number){
    this.userService.unban(id).subscribe(r => {
    this.getUserData(id);
    });
  }

  tryAdmin(id: number){
    this.userService.makeAdmin(id).subscribe(r => {
    this.getUserData(id);
    });
  }

  tryRemoveAdmin(id: number){
    this.userService.removeAdmin(id).subscribe(r => {
    this.getUserData(id);
    });
  }

  getUserData(userId){
    this.userService.getUser(userId).subscribe(
      user => {
        this.profileUser = user;
        this.loaded = true;
      }
    )
  }

}
