import {Component, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {faTimes} from "@fortawesome/free-solid-svg-icons";
import {RecipeService} from "../recipe.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-recipe',
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.scss']
})
export class NewRecipeComponent implements OnInit {

  recipeForm: FormGroup;
  recipeId: number;
  closeIcon = faTimes;
  minRecipeTitleLength = 2;
  maxRecipeTitleLength = 100;
  minRecipeDescriptionLength = 3;
  maxRecipeDescriptionLength = 500;

  permitImageType: boolean = true;
  permitImageSize: boolean = true;
  permitImageTypes: string[] = ['image/png', 'image/jpeg'];
  imagesCounter: number = 0;
  maxImages: number = 10;
  disabled = false;

  constructor(
    private recipeService: RecipeService,
    private router: Router,
  ) {
  }

  ngOnInit(): void {
    this.recipeForm = new FormGroup({
      'title': new FormControl(null, [
        Validators.required,
        Validators.minLength(this.minRecipeTitleLength),
        Validators.maxLength(this.maxRecipeTitleLength)
      ]),
      'description': new FormControl(null, [
        Validators.required,
        Validators.minLength(this.minRecipeDescriptionLength),
        Validators.maxLength(this.maxRecipeDescriptionLength)
      ]),
      'ingredients': new FormArray([]),
      'time': new FormControl(null, [
        Validators.required
      ]),
      'steps': new FormControl(null, [
        Validators.required,
        Validators.minLength(this.minRecipeDescriptionLength),
        Validators.maxLength(this.maxRecipeDescriptionLength)
      ]),
      'images': new FormArray([]),
      'difficulty': new FormControl(1),
      'categories': new FormArray([])
    });
  }

  onClickSubmit() {
    this.disabled = true;
    let newFormData = new FormData();
    newFormData.append('title', this.recipeForm.get('title').value);
    newFormData.append('description', this.recipeForm.get('description').value);
    if (this.recipeForm.get('ingredients').value.length > 0) {
      this.recipeForm.get('ingredients').value.forEach(ingredientGroup => {
        let objClone = {...ingredientGroup};
        let obj = {a: <FormGroup>objClone.name, b: <FormGroup>objClone.quantity};
        for (let key in obj) {
          newFormData.append('ingredients', obj[key].toString());
        }
      });
    }
    newFormData.append('time', this.recipeForm.get('time').value);
    newFormData.append('steps', this.recipeForm.get('steps').value);
    if (this.recipeForm.get('images').value.length > 0) {
      this.recipeForm.get('images').value.forEach(image => {
        newFormData.append('images', image);
      });
    }
    newFormData.append('difficulty', this.recipeForm.get('difficulty').value);
    if (this.recipeForm.get('categories').value.length > 0) {
      this.recipeForm.get('categories').value.forEach(category => {
        newFormData.append('categories', category);
      });
    }
    this.recipeService.createRecipe(newFormData).subscribe((response) => {
      this.disabled = false;
      let location = response.headers.get('location').split('/');
      this.recipeId = +location[location.length - 1];
      this.router.navigate(['/recipes', this.recipeId]);
    })
  }

  addIngredient() {
    (<FormArray>this.recipeForm.get('ingredients')).push(new FormGroup({
      'name': new FormControl(),
      'quantity': new FormControl()
    }));
  }

  removeIngredient(index: number) {
    (<FormArray>this.recipeForm.get('ingredients')).removeAt(index);
  }

  getIngredients() {
    return this.recipeForm.controls['ingredients'] as FormArray;
  }

  deleteImage(index: number) {
    if (index >= 0) {
      (<FormArray>this.recipeForm.get('images')).removeAt(index);
      this.imagesCounter--;
    }
  }

  onFileChanged(event) {
    const file = event.target.files[0];
    this.permitImageType = true;
    this.permitImageSize = true;
    if (!file) {
      return;
    }
    if (!this.permitImageTypes.includes(file.type)) {
      this.permitImageType = false;
      return;
    }
    if (file.size > 3000000) {
      this.permitImageSize = false
      return;
    }
    if (this.imagesCounter < this.maxImages) {
      (<FormArray>this.recipeForm.get('images')).push(new FormControl(file));
      this.imagesCounter++;
    }
  }

  categoryArray(category: number) {
    let num;
    if ((num = (<FormArray>this.recipeForm.get('categories')).controls.findIndex(aux => aux.value == category)) < 0) {
      (<FormArray>this.recipeForm.get('categories')).push(new FormControl(category));
    } else {
      (<FormArray>this.recipeForm.get('categories')).removeAt(num);
    }
  }

}
