import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../../services/authentication.service";


@Component({
  selector: 'app-req-reset-pass',
  templateUrl: './req-reset-pass.component.html',
  styleUrls: ['../login.component.scss']
})
export class ReqResetPassComponent implements OnInit {

  resqResetPassForm: FormGroup;
  minEmailLength: number = 6;
  maxEmailLength: number = 254;
  disable = false;
  onSuccess = false;

  constructor(
    private authenticationService: AuthenticationService,
  ) {
  }

  ngOnInit(): void {
    this.resqResetPassForm = new FormGroup({
      'email': new FormControl(null, [
        Validators.required,
        Validators.maxLength(this.maxEmailLength),
        Validators.minLength(this.minEmailLength),
        Validators.pattern('^([a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+)*$')
      ])
    })
  }

  onSubmitReqResetPass() {
    this.disable = true;
    if (!this.resqResetPassForm.valid) {
      this.resqResetPassForm.markAllAsTouched();
      this.disable = false;
      return;
    }

    this.authenticationService.reqResetPassword(this.resqResetPassForm.get('email').value).subscribe(() => {
      this.onSuccess = true;
      this.disable = false;
    });
  }

  onClose() {
    this.onSuccess = false;
    this.resqResetPassForm.reset();
  }

}
