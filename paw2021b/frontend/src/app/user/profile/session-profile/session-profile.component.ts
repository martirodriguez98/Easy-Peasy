import {Component, OnInit} from '@angular/core';
import {User} from "../../../model/user.model";
import {UserService} from "../../../authentication/services/user.service";
import {Subscription} from "rxjs";
import {AuthenticationService} from "../../../authentication/services/authentication.service";
import {faBan, faCamera, faGears, faShield, faWrench} from "@fortawesome/free-solid-svg-icons";
import {RecipePaginationQuery, RecipePaginationResult} from "../../../search/search.service";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-session-profile',
  templateUrl: './session-profile.component.html',
  styleUrls: ['./session-profile.component.scss']
})
export class SessionProfileComponent implements OnInit {
  user: User;
  loaded = false;
  loadedRecipes = false;
  loadedFavourites = false;
  recipesSub: Subscription;
  favesSub: Subscription;
  managerIcon = faGears;
  adminIcon = faShield;
  bannedIcon = faBan;
  adminPanelIcon = faWrench;
  cameraIcon = faCamera;
  permitImageType: boolean = true;
  permitImageSize: boolean = true;
  permitImageTypes: string[] = ['image/png', 'image/jpeg', 'image/gif', 'image/jpg'];
  maxMBImage = 3;
  disable = false;
  userImage: FormControl;
  change="";
  private userSub: Subscription;

  recipePr: RecipePaginationResult = {
    pages: 0,
    results: [],
  }
  recipePq: RecipePaginationQuery = {
    page: 0,
    pageSize: 4,
  }

  favePr: RecipePaginationResult = {
    pages: 0,
    results: [],
  }
  favePq: RecipePaginationQuery = {
    page: 0,
    pageSize: 4,
  }


  constructor(
    private userService: UserService,
    private authService: AuthenticationService,
  ) {
  }

  ngOnInit(): void {

    this.userService.fillUserData().subscribe();

    this.userSub = this.userService.user.subscribe(user => {
      this.user = user;
      this.loaded = true;
    });

    this.userService.getUserRecipes(this.user.id, this.recipePq);
    this.recipesSub = this.userService.userRecipes.subscribe(r => {
      this.recipePr = {
        ...this.recipePr,
        ...r
      };
      this.loadedRecipes = true;
    });

    this.userService.getUserFavourites(this.user.id, this.favePq);
    this.favesSub = this.userService.userFaves.subscribe(r => {
      this.favePr = {
        ...this.favePr,
        ...r
      };
      this.loadedFavourites = true;
    });

    this.userImage=new FormControl(null);

  }

  showRecipes() {
    this.userService.getUserRecipes(this.user.id, this.recipePq);
    document.getElementById("authored-recipes")!.style.display = "block";
    document.getElementById("favorites")!.style.display = "none";
    localStorage.setItem("state", "recipes");
  }

  showFavourites() {
    this.userService.getUserFavourites(this.user.id, this.favePq);
    document.getElementById("favorites")!.style.display = "block";
    document.getElementById("authored-recipes")!.style.display = "none";
    localStorage.setItem("state", "favorites");
  }

  logout() {
    this.authService.logout();
  }

  onRecipePageChange(page: number) {
    this.recipePq.page = page;
    this.userService.getUserRecipes(this.user.id, this.recipePq);
  }

  onFavePageChange(page: number) {
    this.favePq.page = page;
    this.userService.getUserFavourites(this.user.id, this.favePq);
  }

  profileImageChanged(event) {
    const file = event.target.files[0];
    this.permitImageType = true;
    this.permitImageSize = true;

    if (!file) {
      return;
    }

    if (!this.permitImageTypes.includes(file.type)) {
      this.permitImageType = false;
      setTimeout(() => {
        this.permitImageType = true;
      }, 2000);
      return;
    }

    if (file.size > this.getTotalSize(this.maxMBImage)) {
      this.permitImageType = false;
      setTimeout(() => {
        this.permitImageType = true;
      }, 2000);
      return
    }

    this.disable = true;

    let imageUpload = new FormData();
    imageUpload.append('userImage', file);

    this.userService.updateProfileImage(imageUpload).subscribe(
      () => {
        this.change = "?" + new Date().getMilliseconds();
        this.disable = false;
      });
  }

  getTotalSize(maxMB) {
    return maxMB * Math.pow(10, 6);
  }

  ngOnDestroy(): void{
    this.favesSub.unsubscribe();
    this.recipesSub.unsubscribe();
  }
}
