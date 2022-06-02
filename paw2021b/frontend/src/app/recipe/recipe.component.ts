import { Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Recipe} from "../model/recipe.model";
import {CommentPaginationQuery, CommentPaginationResult, RecipeService} from "./recipe.service";
import {ActivatedRoute, Router} from "@angular/router";
import {
  faAward,
  faGaugeHigh,
  faHeart,
  faShareAlt,
  faThumbsDown,
  faThumbsUp,
  faTrashAlt
} from "@fortawesome/free-solid-svg-icons";
import { Subscription} from "rxjs";
import {Ingredient} from "../model/ingredient.model";
import {FormControl, FormGroup} from "@angular/forms";
import {User} from "../model/user.model";
import { UserService} from "../authentication/services/user.service";
import {faClock} from "@fortawesome/free-regular-svg-icons";

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss', '../shared/recipe-card/recipe-card.component.scss']
})
export class RecipeComponent implements OnInit, OnDestroy {
  @Input("recipe") recipe!: Recipe;
  clockIcon = faClock;
  gaugeIcon = faGaugeHigh;
  shareIcon = faShareAlt;
  deleteIcon = faTrashAlt;
  difficultyString: string = "";
  loaded = false;
  selectedIndex = 0;
  ingredients: Ingredient[] = [];
  suggested: Recipe[] = [];
  loadedSuggested = false;
  private recipeSub: Subscription;
  private likedSub: Subscription;
  private dislikedSub: Subscription;
  private favedSub: Subscription;
  private ingredientsSub: Subscription;
  private categoriesSub: Subscription;
  private difficultySub: Subscription;
  private categoryTransSub: Subscription[] = [];
  private userSub: Subscription;
  private commentSub: Subscription;
  private suggestedSub: Subscription;
  commentForm: FormGroup;
  loadedComments = false;
  commentsCount;
  user: User;
  comment = '';
  disabled = false;
  maxlength = 300;
  faved = false;
  isFaved: boolean;
  favIcon = faHeart;
  unfavIcon=faHeart;
  highlightIcon = faAward;
  isAdmin : boolean;
  isHighlighted : boolean;
  thumbsUpIcon = faThumbsUp;
  isLiked: boolean;
  isDisliked: boolean;
  likePercentage: Object;
  likedReviewsCount: Object;
  thumbsDownIcon = faThumbsDown;

  commentPr: CommentPaginationResult = {
    pages: 0,
    results: []
  }
  commentPq: CommentPaginationQuery = {
    page: 0,
    pageSize: 6,
  }

  categoryStrings: String[] = [];

  constructor(
    private recipeService: RecipeService,
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
  ) {
  }

  ngOnInit(): void {
    window.scrollTo(0,0)
    this.isLiked = false;
    this.isDisliked = false;
    this.isFaved = false;
    this.recipeSub = this.recipeService.getRecipe(this.route.snapshot.params['id']).subscribe(
      recipe => {
        this.recipe = recipe;
        this.commentsCount = recipe.commentsCount;
        this.likedPercentage();
        this.likedReviews();
        if (this.user) {
        this.favedSub = this.recipeService.isFaved(this.recipe.idRecipe).subscribe(() => {
            this.isFaved = true;
          },
          () => {
            this.isFaved = false;
          }
        )
        this.likedSub = this.recipeService.isLiked(this.recipe.idRecipe).subscribe((isLiked) => {
          if(isLiked) {
            this.isLiked = true;
          }
            },
            () => {
              this.isLiked = false;
            }
          )
        this.dislikedSub = this.recipeService.isDisliked(this.recipe.idRecipe).subscribe((isDisliked) => {
          if(isDisliked) {
            this.isDisliked = true;
          }
            },
            () => {
              this.isDisliked = false;
            }
          )

          this.isUserAdmin();
        }
        this.categoriesSub = this.recipeService.getCategories(this.recipe).subscribe(r => {
          for (let i = 0; i < r.length; i++) {
            this.categoryTransSub[i] = this.recipeService.getRecipeCategoryString(r[i].category).subscribe(r => {
              this.categoryStrings[i] = r
            });
          }
        });
        this.ingredientsSub = this.recipeService.getIngredients(this.recipe).subscribe(r => {
          this.ingredients = r;
        });
        this.loaded = true;
        this.difficultySub = this.recipeService.getRecipeDifficultyString(this.recipe.recipeDifficulty).subscribe(r => {
          this.difficultyString = r.toLowerCase()
        });
        this.getComments(recipe);
        this.getSuggestedRecipes(this.recipe);

      }
    )

    this.userSub = this.userService.user.subscribe(user => {
      this.user = user;
    });
    this.commentForm = new FormGroup({
      'commentDesc': new FormControl(null),
    });
  }

  onSubmit() {
    this.disabled = true;
    let formData = new FormData();
    formData.append('commentDesc', this.commentForm.get('commentDesc').value);
    this.recipeService.postComment(formData, this.recipe).subscribe(response => {
      this.disabled = false;
      this.getComments(this.recipe);
      this.cleanReviewForm();
      this.recipeService.getRecipe(this.route.snapshot.params['id']).subscribe(
      recipe => {
        this.commentsCount = recipe.commentsCount;
      });
    });
  }

  private cleanReviewForm() {
    this.commentForm.markAsUntouched();
    this.commentForm.get('commentDesc').patchValue('');
  }

  getComments(recipe) {
    this.recipeService.getComments(this.commentPq, recipe);
    this.commentSub = this.recipeService.comments.subscribe(r => {
      this.commentPr = {
        ...this.commentPr,
        ...r
      };
      this.loadedComments = true;
    });
  }

  isUserAdmin() {
    if(this.user.roles.includes('ADMIN')) {
      this.isAdmin = true;
    } else {
      this.isAdmin = false;
    }
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

  changeRecipe(){
    this.ngOnInit()
  }

  getSuggestedRecipes(recipe) {
    this.suggestedSub = this.recipeService.getSuggestedRecipes(recipe).subscribe(
      suggestedRecipes => {
        this.suggested = suggestedRecipes;
        this.loadedSuggested = true;
      }
    )
  }

  deleteComment(recipe, idComment) {
    this.recipeService.deleteComment(recipe, idComment).subscribe(
      res => {
        this.recipeService.getRecipe(this.route.snapshot.params['id']).subscribe(
      recipe => {
        this.commentsCount = recipe.commentsCount;
      });
        this.getComments(this.recipe);
      }
    );
  }

  selectPrevious() {
    if (this.selectedIndex == 0) {
      this.selectedIndex = this.recipe.imageUrls.length - 1;
    } else {
      this.selectedIndex--;
    }
  }

  selectNext() {
    if (this.selectedIndex == this.recipe.imageUrls.length - 1) {
      this.selectedIndex = 0;
    } else {
      this.selectedIndex++;
    }
  }

  removeRecipe(recipe) {
    this.recipeService.deleteRecipe(recipe).subscribe(
      aux =>
        this.router.navigate(['/'])
    );
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

  onCommentsChangePage(page: number) {
    this.commentPq.page = page;
    this.recipeService.getComments(this.commentPq, this.recipe);
  }

  like() {
    if(!this.user) {
      this.router.navigate(['/login']);
    } else {
      this.recipeService.likeRecipe(this.recipe.idRecipe).subscribe(
        res => {
          this.likedPercentage();
          this.likedReviews();
          if(this.isDisliked) {
            this.isDisliked = false;
          }
          this.isLiked = !this.isLiked;
        }
      );
    }
  }

  disLike() {
    if(!this.user) {
      this.router.navigate(['/login']);
    } else {
      this.recipeService.dislikeRecipe(this.recipe.idRecipe).subscribe(
        res => {
          this.likedPercentage();
          this.likedReviews();
          if(this.isLiked) {
            this.isLiked = false;
          }
          this.isDisliked = !this.isDisliked;
        }
      );
    }
  }

  likedPercentage() {
    this.recipeService.likedPercentage(this.recipe.idRecipe).subscribe(
      res => {
        this.likePercentage = res;
      }
    );
  }

  likedReviews() {
    this.recipeService.likedReviews(this.recipe.idRecipe).subscribe(
      res => {
        this.likedReviewsCount = res;
      }
    );
  }

  ngOnDestroy(){
    this.commentSub.unsubscribe();
  }

}
