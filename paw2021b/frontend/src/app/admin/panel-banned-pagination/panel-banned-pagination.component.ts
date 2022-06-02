import {Component, OnDestroy, OnInit} from '@angular/core';
import {AdminPanelPaginationQuery, UserPaginationResult, UserService} from "../../authentication/services/user.service";
import {faArrowDownShortWide, faExclamationCircle, faIdCard, faUser} from "@fortawesome/free-solid-svg-icons";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-panel-banned-pagination',
  templateUrl: './panel-banned-pagination.component.html',
  styleUrls: ['./panel-banned-pagination.component.scss']
})
export class PanelBannedPaginationComponent implements OnInit, OnDestroy {

  sortIcon = faArrowDownShortWide;
  unbanIcon = faExclamationCircle;
  byUser = false;
  byId = true;
  idIcon = faIdCard;
  userIcon = faUser;
  bannedSub: Subscription;

  bannedPr: UserPaginationResult = {
    pages: 0,
    results: [],
  }

  bannedPq: AdminPanelPaginationQuery = {
    page: 0,
    pageSize: 6,
    orderBy: 'BY_ID'
  }

  constructor(
    private userService: UserService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.userService.getBanned(this.bannedPq);
    this.bannedSub = this.userService.banned.subscribe(r => {
      this.bannedPr = {
        ...this.bannedPr,
        ...r
      };
    });
  }

  onPageChange(page: number) {
    this.bannedPq.page = page;
    this.userService.getBanned(this.bannedPq);
  }

  ngOnDestroy(): void {
    this.bannedSub.unsubscribe();
  }

  changeBannedOrder(order: string) {
    if (order == 'BY_USERNAME') {
      this.byUser = true;
      this.byId = false;
    }
    if (order == 'BY_ID') {
      this.byUser = false;
      this.byId = true;
    }
    this.bannedPq.orderBy = order;
    this.userService.getBanned(this.bannedPq);
  }

  tryUnban(id: number) {
    this.userService.unban(id).subscribe(r => this.router.navigate(['/profile/' + id]));
  }

}
