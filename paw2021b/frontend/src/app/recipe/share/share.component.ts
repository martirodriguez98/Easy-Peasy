import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {RecipeService} from "../recipe.service";


@Component({
  selector: 'app-share',
  templateUrl: './share.component.html',
  styleUrls: ['../recipe.component.scss']
})
export class ShareComponent implements OnInit {

  @Input() recipeId: number;
  shareForm: FormGroup;
  minEmailLength: number = 6;
  maxEmailLength: number = 254;
  minNameLength: number = 2;
  maxNameLength: number = 100;
  disable = false;
  onSuccess = false;

  constructor(
    private recipeService: RecipeService,
  ) {
  }

  ngOnInit(): void {
    this.shareForm = new FormGroup({
      'name': new FormControl(null, [
        Validators.required,
        Validators.maxLength(this.maxNameLength),
        Validators.minLength(this.minNameLength),
      ]),
      'email': new FormControl(null, [
        Validators.required,
        Validators.maxLength(this.maxEmailLength),
        Validators.minLength(this.minEmailLength),
        Validators.pattern('^([a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+)*$')
      ])
    })
  }

  onSubmitShare() {
    this.disable = true;
    if (!this.shareForm.valid) {
      this.shareForm.markAllAsTouched();
      this.disable = false;
      return;
    }
    let newFormData = new FormData();
    newFormData.append('name', this.shareForm.get('name').value);
    newFormData.append('email', this.shareForm.get('email').value);

    this.recipeService.share(newFormData, this.recipeId).subscribe(() => {
      this.onSuccess = true;
      this.disable=false;
    });
  }

  onClose(){
    this.onSuccess = false;
    this.shareForm.reset();
  }

}
