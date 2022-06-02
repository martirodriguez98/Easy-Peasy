import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";
import {UserPaginationResult} from "../authentication/services/user.service";

import {
  RecipePaginationQuery,
  RecipePaginationResult,
  SearchService,
  UsersPaginationQuery,
} from "./search.service";
import {Subject, Subscription} from "rxjs";

import {
  faArrowDownAZ,
  faSearch,
  faShield,
  faAward,
  faGaugeHigh,
  faXmark
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']

})
export class SearchComponent implements OnInit,OnDestroy {
  disable = false;
  fetching = true;
  private recipesSub: Subscription;
  private usersSub: Subscription;
  activeTab: string = 'recipes';
  highlighted = false;
  admins = false;
  userOrder = "NAME_ASC"
  faSearch = faSearch;
  faSort = faArrowDownAZ;
  faGauge = faGaugeHigh;
  faXmark = faXmark;
  categoriesArray: number[] = [];
  difficulty = 0;
  recipesOrder = "DATE_DESC";
  totalCategories: number[] = new Array(20);
  faShield = faShield;
  faAward = faAward;
  rpr: RecipePaginationResult = {
    results: [],
    pages: 0
  }

  rpq: RecipePaginationQuery = {
    page: 0,
    pageSize: 12,
    categories: this.categoriesArray,
    difficulty: 0,
    highlighted: false,
    order: "DATE_DESC"
  }

  upr: UserPaginationResult = {
    results: [],
    pages: 0
  }

  upq: UsersPaginationQuery = {
    page: 0,
    pageSize: 12,
    highlighted: false,
    admins: false,
    order: "NAME_ASC"
  }

  resultsSubject: Subject<RecipePaginationResult> = new Subject<RecipePaginationResult>();

  constructor(
    private router: Router,
    private translateService: TranslateService,
    private route: ActivatedRoute,
    private searchService: SearchService
  ) {
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe((e) => {
      this.parseQueryParams();
      this.searchService.getRecipes(this.rpq, this.resultsSubject);
      this.searchService.getUsers(this.upq);
    })

    this.recipesSub = this.resultsSubject.subscribe(
      (results) => {
        this.rpr = {
          ...this.rpr,
          ...results
        };
        this.fetching = false;
      }
    )

    this.usersSub = this.searchService.resultsUsers.subscribe(
      (results) => {
        this.upr = {
          ...this.upr,
          ...results
        };
        this.fetching = false;
      }
    )

  }

  changeTab(tab: string) {
    this.activeTab = tab;
  }

  private parseQueryParams() {
    const params = this.route.snapshot.queryParams;

    if (this.activeTab === 'recipes') {
      this.rpq = {
        ...this.rpq,
        ...params
      }
      if (params["page"]) {
        this.rpq.page = Number.parseInt(params["page"])
      } else {
        this.rpq.page = 0;
      }
      if(params["highlighted"]){
        this.rpq.highlighted = Boolean(JSON.parse(params["highlighted"]));
      }

    } else {
      this.upq = {
        ...this.upq,
        ...params
      }

      if (params["page"]) {
        this.upq.page = Number.parseInt(params["page"])
      } else {
        this.upq.page = 0;
      }
    }
  }

  onSearchEnter(e: KeyboardEvent, query: string) {
    if (e.key == "Enter") {
      this.onSearch(query);
    }
  }

  onSearch(query: string) {
    this.rpq.query = query;
    this.upq.query = query;
    if (this.activeTab === 'recipes') {
      this.rpq.page = 0;
      this.updateRecipesRoute(false);
    } else {
      this.upq.page = 0;
      this.updateUsersRoute(false);
    }
  }

  changeUserOrder(value: string) {
    this.userOrder = value;
    this.upq.order = value;
    this.upq.page = 0;
    this.updateUsersRoute(false);
  }

  setHighlightedFilter() {
    this.highlighted = !this.highlighted;
    this.upq.highlighted = this.highlighted;
    this.upq.page = 0;
    this.updateUsersRoute(false);
  }

  setAdminFilter() {
    this.admins = !this.admins;
    this.upq.admins = this.admins;
    this.upq.page = 0;
    this.updateUsersRoute(false);
  }

  updateCategory(category: number) {
    const index = this.categoriesArray.indexOf(category);
    if (index == -1) {
      this.categoriesArray.push(category);
    } else {
      this.categoriesArray.splice(index, 1);
    }

    this.rpq.categories = this.categoriesArray;
    this.rpq.page = 0;
    this.updateRecipesRoute(false);
  }

  changeDifficulty(value: number) {
    this.difficulty = value;
    this.rpq.difficulty = this.difficulty;
    this.rpq.page = 0;
    this.updateRecipesRoute(false);
  }

  changeRecipeOrder(value: string) {
    this.recipesOrder = value;
    this.rpq.order = this.recipesOrder;
    this.rpq.page = 0;
    this.updateRecipesRoute(false);
  }

  changeHighlightedRecipes() {
    this.rpq.highlighted = !this.rpq.highlighted;
    this.updateRecipesRoute(false);
  }

  private updateRecipesRoute(replace: boolean) {
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {...this.rpq},
        replaceUrl: replace
      });
  }

  private updateUsersRoute(replace: boolean) {
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {...this.upq},
        replaceUrl: replace
      });
  }

  onRecipesChangePage(page: number) {
    this.rpq.page = page;
    this.updateRecipesRoute(false);
  }

  onUsersChangePage(page: number) {
    this.upq.page = page;
    this.updateUsersRoute(false);
  }

  onClearRecipes() {
    this.rpq.page = 0;
    this.rpq.highlighted = false;
    this.rpq.query = "";
    this.rpq.order = "DATE_DESC";
    this.rpq.categories = [];
    this.rpq.difficulty = 0;
    this.difficulty = 0;
    this.categoriesArray = [];
    this.recipesOrder = "DATE_DESC";
    this.upq.page = 0;
    this.upq.admins = false;
    this.upq.query = "";
    this.upq.order = "NAME_ASC";
    this.upq.highlighted = false;
    this.highlighted = false;
    this.userOrder = "NAME_ASC";
    this.admins = false;
    const checkboxes = document.getElementsByClassName('checkboxes');
    for (let i=0; i<checkboxes.length; i++){
      const cat = checkboxes[i] as HTMLInputElement;
      cat.checked = false;
    }
    (document.getElementById('difficultyNone') as HTMLInputElement).checked = true;
    (document.getElementById('orderDateDesc') as HTMLInputElement).checked = true;
    this.updateRecipesRoute(false);
    this.updateUsersRoute(false);
  }


  ngOnDestroy(): void {
    this.recipesSub.unsubscribe();
    this.usersSub.unsubscribe();
  }

}

