import {Component, OnDestroy, OnInit} from '@angular/core';
import {AdminPanelPaginationQuery, UserPaginationResult, UserService} from "../../authentication/services/user.service";
import {Subscription} from "rxjs";
import {faArrowDownShortWide, faIdCard, faUser} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-panel-admin-pagination',
  templateUrl: './panel-admin-pagination.component.html',
  styleUrls: ['./panel-admin-pagination.component.scss']
})
export class PanelAdminPaginationComponent implements OnInit, OnDestroy {

  sortIcon = faArrowDownShortWide;
  adminSub: Subscription;
  byUser=false;
  byId=true;
  idIcon=faIdCard;
  userIcon=faUser;

  adminPr: UserPaginationResult = {
    pages: 0,
    results: [],
  }

  adminPqAdm: AdminPanelPaginationQuery = {
    page: 0,
    pageSize: 6,
    orderBy: 'BY_ID',
  }

  constructor(
    private userService: UserService,
  ) { }

  onPageChangeAdm(page: number){
    this.adminPqAdm.page = page;
    this.userService.getAdmins(this.adminPqAdm);
  }

  ngOnInit(): void {
    this.userService.getAdmins(this.adminPqAdm);
    this.adminSub = this.userService.admins.subscribe(r => {
      this.adminPr = {
        ...this.adminPr,
        ...r
      };
    });
  }

  ngOnDestroy() {
    this.adminSub.unsubscribe();
  }

  changeAdminOrder(order: string){
    if(order=='BY_USERNAME'){
      this.byUser=true;
      this.byId=false;
    }
    if(order=='BY_ID'){
      this.byUser=false;
      this.byId=true;
    }
    this.adminPqAdm.orderBy = order;
    this.userService.getAdmins(this.adminPqAdm);
  }
}
