import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../services/authentication.service";
import {UserService} from "../services/user.service";
import {Router} from "@angular/router";
import {PreviousRouteService} from "../services/previous-route.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  error = false;
  disable = false;
  registerForm: FormGroup;
  minPassLength: number = 6;
  maxPassLength: number = 30;
  minEmailLength: number = 6;
  maxEmailLength: number = 254;
  minUsernameLength: number = 2;
  maxUsernameLength: number = 30;

  constructor(
    private authService: AuthenticationService,
    private userService: UserService,
    private router: Router,
    private previousRouteService: PreviousRouteService
  ) {
  }

  ngOnInit(): void {

    this.registerForm = new FormGroup({
        username: new FormControl(null, [
          Validators.required,
          Validators.minLength(this.minUsernameLength),
          Validators.maxLength(this.maxUsernameLength),
          Validators.pattern("[a-zA-Z0-9_]+")
        ]),
        email: new FormControl(null, [
          Validators.required,
          Validators.maxLength(this.maxEmailLength),
          Validators.minLength(this.minEmailLength),
          Validators.pattern('^([a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+)*$')
        ]),
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
    this.registerForm.setValidators(this.passwordMatching.bind(this));

  }

  onSubmit(){
    this.disable = true;
    if (!this.registerForm.valid) {
      this.registerForm.markAllAsTouched();
      this.disable = false;
      return;
    }

    const registerData = {
      username: this.registerForm.value.username,
      email: this.registerForm.value.email,
      password: this.registerForm.value.password,
    }

    const authObs = this.authService.register(registerData);

    authObs.subscribe(
      () => {
        this.authService
          .login(registerData.username, registerData.password)
          .subscribe(
            () => {
              this.userService.fillUserData().subscribe(() => {
                this.disable = false;
                let url = '/profile';
                this.router.navigate([url]);
              });
            },
            () => {
              this.disable = false;
            }
          );
      },
      () => {
        this.disable = false;
        this.error = true;
        setTimeout(() => {
          this.error = false
        }, 4000)
      }
    );


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

