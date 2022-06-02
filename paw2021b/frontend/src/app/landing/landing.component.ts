import { Component, OnInit } from '@angular/core';
import {Recipe} from "../model/recipe.model";
import {RecipeService} from "../recipe/recipe.service";
import {Subject, Subscription} from "rxjs";
import {faCircleArrowRight, faSearch} from "@fortawesome/free-solid-svg-icons";
import {User} from "../model/user.model";
import {UserService} from "../authentication/services/user.service";
import {Router} from "@angular/router";
import {RecipePaginationQuery, RecipePaginationResult, SearchService} from "../search/search.service";

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit {
  loaded = false;
  loadedHighlighted = false;
  faSearch = faSearch;
  arrowIcon = faCircleArrowRight;
  private latestSub: Subscription;
  private highlightedSub: Subscription;
  private userSubscription: Subscription;
  latestSubject: Subject<RecipePaginationResult> = new Subject<RecipePaginationResult>();
  highlightedSubject: Subject<RecipePaginationResult> = new Subject<RecipePaginationResult>();
  rpqLatest: RecipePaginationQuery = {
    page: 0,
    pageSize: 4,
    order: "DATE_DESC"
  }
  rprLatest: RecipePaginationResult = {
    results: [],
    pages: 0
  }
  rpqHighlighted: RecipePaginationQuery = {
    page: 0,
    pageSize: 4,
    order: "DATE_DESC",
    highlighted: true
  }
  rprHighlighted: RecipePaginationResult = {
    results: [],
    pages: 0
  }
  user: User;
  constructor(
    private router: Router,
    private recipeService: RecipeService,
    private userService: UserService,
    private searchService: SearchService,
  ) { }

  ngOnInit(): void {
    this.searchService.getRecipes(this.rpqLatest, this.latestSubject)
    this.latestSub = this.latestSubject.subscribe(
      (results) => {
        this.rprLatest = {
          ...this.rprLatest,
          ...results
        };
        this.loaded = true;
      }
    )

    this.searchService.getRecipes(this.rpqHighlighted, this.highlightedSubject)
    this.highlightedSub = this.highlightedSubject.subscribe(
      (results) => {
        this.rprHighlighted = {
          ...this.rprHighlighted,
          ...results
        };
        this.loadedHighlighted = true;
      }
    )
    this.userSubscription = this.userService.user.subscribe(user => {
      this.user = user;
    });

  }

  onSearchEnter(e: KeyboardEvent, query: string) {
    if (e.key == "Enter") {
      this.onSearch(query);
    }
  }

  onSearch(query: string) {
    this.router.navigate(['/search'],
      {
        queryParams: {
          query,
          page: '0',
          pageSize: '12',
          order: 'DATE_DESC'
        }
      });
  }

  moreHighlighted(){
    this.router.navigate(['/search'],
      {
        queryParams: {
          page: '0',
          pageSize: '12',
          difficulty: 0,
          highlighted: true,
          order: 'DATE_DESC'
        }
      });
  }

  latestSearch(){
    this.router.navigate(['/search'],
      {
        queryParams: {
          page: '0',
          pageSize: '12',
          order: 'DATE_DESC'
        }
      });
  }

  ngOnDestroy(){
    this.latestSub.unsubscribe();
    this.highlightedSub.unsubscribe();
  }

}
