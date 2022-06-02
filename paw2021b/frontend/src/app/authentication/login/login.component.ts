import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {UserService} from "../services/user.service";
import {PreviousRouteService} from "../services/previous-route.service";
import {faEye, faEyeSlash} from "@fortawesome/free-regular-svg-icons";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  disable = false;
  error = false;
  showPass = true;
  faEyeSlash = faEyeSlash;
  faEye = faEye;
  minUsernameLength: number = 2;
  maxUsernameLength: number = 30;
  minPassLength: number = 6;
  maxPassLength: number = 30;

  constructor(
    private router: Router,
    private authenticationServer: AuthenticationService,
    private userService: UserService,
    private previousRouteService: PreviousRouteService,
  ) {
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      'username': new FormControl(null, [
        Validators.required,
        Validators.minLength(this.minUsernameLength),
        Validators.maxLength(this.maxUsernameLength)]),
      'password': new FormControl(null, [
        Validators.required,
        Validators.minLength(this.minPassLength),
        Validators.maxLength(this.maxPassLength)])
    });
  }

  //login function
  onSubmitLogin() {
    this.disable = true;
    if (!this.loginForm.valid) {
      this.loginForm.markAllAsTouched();
      this.disable = false;
      return;
    }
    const username = this.loginForm.value.username;
    const password = this.loginForm.value.password;

    const authObs = this.authenticationServer.login(username, password);

    authObs.subscribe({
      next: value => this.userService
        .fillUserData()
        .subscribe(() => {
          let url = '/profile';
          if (this.previousRouteService.getAuthRedirect()) {
            let prevUrl = this.previousRouteService.getPreviousUrl();
            url = !!prevUrl ? prevUrl : url;
            this.previousRouteService.setAuthRedirect(false);
          }
          this.router.navigate([url]);
          this.disable = false;
        }),
      error: err => {
        this.disable = false;
        this.error = true;
        setTimeout(() => {
          this.error = false;
        }, 4000)
      }
    });

  }

  toggleEye() {
    this.showPass = !this.showPass;
  }

}
