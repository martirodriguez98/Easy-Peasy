import { Component, OnInit } from '@angular/core';
import {
  AdminPanelPaginationQuery,
  ReportsPaginationResult,
  UserService
} from "../../authentication/services/user.service";
import {Subscription} from "rxjs";
import {faTrashAlt} from "@fortawesome/free-regular-svg-icons";
import {faArrowDownShortWide, faClock, faExclamationCircle, faUser} from "@fortawesome/free-solid-svg-icons";
import {Router} from "@angular/router";
import {User} from "../../model/user.model";

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {

  reportsSub: Subscription;
  userSub: Subscription;
  user: User;
  deleteIcon = faTrashAlt;
  banIcon = faExclamationCircle;
  sortIcon = faArrowDownShortWide;
  userIcon = faUser;
  clockIcon = faClock;
  byReported = false;
  byReporter = false;
  byOldest = false;
  byNewest = true;

  reportsPr: ReportsPaginationResult = {
    pages: 0,
    results: []
  }

  reportsPq: AdminPanelPaginationQuery = {
    page: 0,
    pageSize: 4,
    orderBy: 'NEWEST',
  }

  constructor(
    private userService: UserService,
    private router: Router
    ) { }

  ngOnInit(): void {
    this.userSub = this.userService.user.subscribe(user => {
      this.user = user;
    });
    this.userService.getReports(this.reportsPq);
    this.reportsSub = this.userService.reports.subscribe(r => {
      this.reportsPr = {
        ...this.reportsPr,
        ...r
      }
    });
  }

  tryDeleteReport(id: number){
    this.userService.deleteReport(id).subscribe(
      res => {
        this.userService.getReports(this.reportsPq)
      }
    );
  }

  deleteUserReports(id: number){
    this.userService.deleteUserReports(id).subscribe(
      res => {
        this.userService.getReports(this.reportsPq)
      }
    )
  }

  tryBan(id: number) {
    this.deleteUserReports(id);
    this.userService.ban(id).subscribe(r => this.router.navigate(['/profile/' + id]));
  }

  changeReportOrder(order: string) {
    if (order == "BY_REPORTED") {
      this.byReported = true;
      this.byReporter = false;
      this.byOldest = false;
      this.byNewest = false;
    }else if (order == "BY_REPORTER"){
      this.byReported = false;
      this.byReporter = true;
      this.byOldest = false;
      this.byNewest = false;
    }else if (order == "OLDEST"){
      this.byReported = false;
      this.byReporter = false;
      this.byOldest = true;
      this.byNewest = false;
    }else {
      this.byReported = false;
      this.byReporter = false;
      this.byOldest = false;
      this.byNewest = true;
    }
    this.reportsPq.orderBy = order;
    this.userService.getReports(this.reportsPq);
  }

  onPageChangeReports(page: number){
    this.reportsPq.page = page;
    this.userService.getReports(this.reportsPq)
  }

  ngOnDestroy(): void{
    this.reportsSub.unsubscribe();
    this.userSub.unsubscribe();
  }

}
