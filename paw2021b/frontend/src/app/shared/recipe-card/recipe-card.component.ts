import {Component, Input, OnInit} from '@angular/core';
import {Recipe} from "../../model/recipe.model";
import {RecipeService} from "../../recipe/recipe.service";
import {Subscription} from "rxjs";
import {faClock} from "@fortawesome/free-regular-svg-icons";
import {faAward, faGaugeHigh, faHeart, faThumbsUp} from "@fortawesome/free-solid-svg-icons";
import {UserService} from "../../authentication/services/user.service";
import {User} from "../../model/user.model";


@Component({
  selector: 'app-recipe-card',
  templateUrl: './recipe-card.component.html',
  styleUrls: ['./recipe-card.component.scss']
})
export class RecipeCardComponent implements OnInit {

  @Input("recipe") recipe!: Recipe;
  private likesSub: Subscription;
  private dislikesSub: Subscription;
  private difficultySub: Subscription;
  private categoriesSub: Subscription;
  private categoryTransSub: Subscription[] = [];

  clockIcon = faClock;
  likeIcon = faThumbsUp;
  gaugeIcon = faGaugeHigh;
  coverUrl: string = '';
  likes: number = 0;
  dislikes: number = 0;
  likePercentage: number = 0;
  difficultyString: string = "";
  categoryStrings: String[] = [];
  user: User;
  faved = false;
  isFaved: boolean;
  favIcon = faHeart;
  highlightIcon = faAward;
  isAdmin : boolean;
  isHighlighted : boolean;

  constructor(private recipeService: RecipeService, public userService: UserService) {

  }

  ngOnInit(): void {
    this.userService.user.subscribe(user => {
      this.user = user;
    });
    if (this.user) {
      this.recipeService.isFaved(this.recipe.idRecipe).subscribe(() => {
          this.isFaved = true;
        },
        () => {
          this.isFaved = false;
        }
      )
      this.isUserAdmin();
    }
    if(this.recipe.imageUrls.length > 0){
      this.coverUrl = this.recipe.imageUrls[0];
    }
    this.likesSub = this.recipeService.getLikes(this.recipe).subscribe(r => {this.likes = r; this.getLikePercentage()});
    this.dislikesSub = this.recipeService.getDislikes(this.recipe).subscribe(r => {this.dislikes = r; this.getLikePercentage()});
    this.categoriesSub = this.recipeService.getCategories(this.recipe).subscribe(r => {for(let i=0; i<r.length; i++){this.categoryTransSub[i] = this.recipeService.getRecipeCategoryString(r[i].category).subscribe(r=>{this.categoryStrings[i] = r});}
    });
    this.getLikePercentage();
    this.difficultySub = this.recipeService.getRecipeDifficultyString(this.recipe.recipeDifficulty).subscribe(r => {this.difficultyString = r.toLowerCase()});

  }

  isUserAdmin() {
    if(this.user.roles.includes('ADMIN')) {
      this.isAdmin = true;
    } else {
      this.isAdmin = false;
    }
  }

  getLikePercentage(){
    if (this.likes == 0 && this.dislikes == 0) {
            this.likePercentage = -1;
    }else{
          this.likePercentage = (this.likes * 100) / (this.likes + this.dislikes);
    }
  }

  fav() {
    this.faved = true;
    this.recipeService.fav(this.recipe.idRecipe).subscribe(() => {
      this.recipeService.getRecipe(this.recipe.idRecipe).subscribe(
        (recipe) => {
          this.recipe = recipe;
          this.faved = false;
          this.isFaved = true;
        }
      )
    })
  }

  unfav() {
    this.faved = true;
    this.recipeService.unfav(this.recipe.idRecipe).subscribe(() => {
      this.recipeService.getRecipe(this.recipe.idRecipe).subscribe(
        (recipe) => {
          this.recipe = recipe;
          this.faved = false;
          this.isFaved = false;
        }
      )
    })
  }

  getStyles(key: number) {
    let color = ''
    if (key < 25) {
      color = 'red';
    }
    else if (key >= 25 && key < 70) {
      color = 'orange';
    }
    else {
      color = '#93c021';
    }
    return {
      color: color
    };
}

  highlight() {
    this.recipeService.highlightRecipe(this.recipe.idRecipe).subscribe(() => {
      this.recipeService.getRecipe(this.recipe.idRecipe).subscribe(
        (recipe) => {
          this.recipe = recipe;
        }
      )
    })
  }


  ngOnDestroy(){
    
  }
}
