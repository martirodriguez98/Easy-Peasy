import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {UserService} from "../services/user.service";
import {faHome} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-login',
  templateUrl: './reset-password.component.html',
  styleUrls: ['../register/register.component.scss']
})
export class ResetPasswordComponent implements OnInit {

  error = false;
  disable = false;
  resetPasswordForm: FormGroup;
  minPassLength: number = 6;
  maxPassLength: number = 30;
  onSuccess = true;
  token: string;
  done = false;
  homeIcon = faHome;


  constructor(
    private route: ActivatedRoute,
    private authService: AuthenticationService,
    private userService: UserService,
    private router: Router,
    private authenticationService: AuthenticationService,
  ) {
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParams['token'];

    if (!this.token) {
      this.router.navigate(['/']);
    }

    this.resetPasswordForm = new FormGroup({
      password: new FormControl(null, [
        Validators.required,
        Validators.minLength(this.minPassLength),
        Validators.maxLength(this.maxPassLength)
      ]),
      repeatPass: new FormControl(null, [
        Validators.required,
        Validators.minLength(this.minPassLength),
        Validators.maxLength(this.maxPassLength)
      ]),
    });
    this.resetPasswordForm.setValidators(this.passwordMatching.bind(this));

  }

  onSubmit() {
    this.disable = true;
    if (!this.resetPasswordForm.valid) {
      this.resetPasswordForm.markAllAsTouched();
      this.disable = false;
      return;
    }

    this.authenticationService.resetPassword(this.token, this.resetPasswordForm.get('password').value).subscribe(() => {
      this.onSuccess = true;
      this.disable = false;
      this.done = true;
    });


  }


  passwordMatching(group: FormGroup): { [s: string]: boolean } {
    const confirmPasswordControl = group.controls['repeatPass'];
    if (group.controls['password'].value != confirmPasswordControl.value) {
      confirmPasswordControl.setErrors({passwordsDontMatch: true});
    } else {
      confirmPasswordControl.setErrors(null);
    }
    // @ts-ignore
    return;
  }
}
