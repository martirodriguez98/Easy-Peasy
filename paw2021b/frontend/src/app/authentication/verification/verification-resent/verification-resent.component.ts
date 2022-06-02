import { Component, OnInit } from '@angular/core';
import {faHome} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-verification-resent',
  templateUrl: './verification-resent.component.html',
  styleUrls: ['./verification-resent.component.scss']
})
export class VerificationResentComponent implements OnInit {

  homeIcon = faHome;
  constructor() { }

  ngOnInit(): void {
  }

}
