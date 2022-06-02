import {Component, OnInit} from '@angular/core';
import {faHome} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-error-404',
  templateUrl: './error-404.component.html',
  styleUrls: ['./error-404.component.scss']
})
export class Error404Component implements OnInit {

  homeIcon= faHome;

  constructor(
  ) {
  }

  ngOnInit(): void {

  }

}
