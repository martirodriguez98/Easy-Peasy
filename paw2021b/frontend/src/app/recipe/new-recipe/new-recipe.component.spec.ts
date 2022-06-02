import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewRecipeComponent } from './new-recipe.component';
import {TestingModule} from "../../testing.module";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";

describe('NewRecipeComponent', () => {
  let component: NewRecipeComponent;
  let fixture: ComponentFixture<NewRecipeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewRecipeComponent ],
      schemas:[CUSTOM_ELEMENTS_SCHEMA],
      imports: [
        TestingModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewRecipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
