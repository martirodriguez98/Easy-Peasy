import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-verification-needed',
  templateUrl: './verification-needed.component.html',
  styleUrls: ['./verification-needed.component.scss']
})
export class VerificationNeededComponent implements OnInit {
  loading = false;
  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  resend(){
    this.loading = true;
    this.authService.resendVerification().subscribe(r => {
      this.router.navigate(['/verification/resent'])
    })
  }
}
