import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {RecipeService} from "../recipe.service";
import {Recipe} from "../../model/recipe.model";
import {User} from "../../model/user.model";
import {UserService} from "../../authentication/services/user.service";
import {Subscription} from "rxjs";
import {Comment} from "../../model/comment.model";
import {RecipeComponent} from "../recipe.component";
import {faCircleExclamation, faShield} from "@fortawesome/free-solid-svg-icons";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit, OnDestroy  {

  @Input("comment") comment!: Comment;
  @Input("recipe") recipe!: Recipe;
  @Input("index") index!: number;
  user: User;
  private userSub: Subscription;
  isAdmin : boolean;
  isBanned: boolean;
  reportIcon = faCircleExclamation;
  adminIcon = faShield;
  reportForm: FormGroup;
  maxRepDesc: number = 244;
  disableRep = false;

  constructor(
    public recipeService: RecipeService,
    public userService: UserService,
    public recipeComponent: RecipeComponent
  ) { }

  ngOnInit(): void {
    this.userSub = this.userService.user.subscribe(user => {
      this.user = user;
      if (this.user) {
        this.isUserAdmin();
        this.isUserBanned();
      }
    });
    this.reportForm = new FormGroup({
      'reportDesc': new FormControl(null,[
        Validators.required,
        Validators.maxLength(this.maxRepDesc)])
    });
  }

  deleteComment(commentId) {
    this.recipeComponent.deleteComment(this.recipe, commentId);
  }

  isUserAdmin() {
    this.isAdmin = this.user.roles.includes('ADMIN');
  }

  isUserBanned(){
    this.isBanned = this.user.roles.includes('BANNED');
  }

  ngOnDestroy(){
    this.userSub.unsubscribe();
  }

   reportUser(){
    this.disableRep = true;
    let newFormData = new FormData();
    newFormData.append('reportDesc', this.reportForm.get('reportDesc').value);
    newFormData.append('reportedUsername',this.comment.user.username);
    newFormData.append('commentId',this.comment.id.toString())
    this.userService.reportUser(newFormData).subscribe(response => {
      this.disableRep = false;
      this.reportForm.markAsUntouched();
      this.reportForm.get('reportDesc').patchValue('');
    });
  }

}
