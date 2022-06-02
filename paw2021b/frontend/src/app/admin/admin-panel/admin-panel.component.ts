import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from "../../authentication/services/user.service";
import {faArrowDownShortWide} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent implements OnInit, OnDestroy {

  sortIcon = faArrowDownShortWide;
  activeTab: string = 'manageUsers';

  constructor(
    private userService: UserService,
  ) { }

  changeTab(tab: string) {
    this.activeTab = tab;
  }

  ngOnInit(): void {

  }

  ngOnDestroy(): void {
  }

}
