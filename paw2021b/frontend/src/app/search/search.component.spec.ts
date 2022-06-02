import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchComponent } from './search.component';
import {TestingModule} from "../testing.module";

describe('SearchComponent', () => {
  let component: SearchComponent;
  let fixture: ComponentFixture<SearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchComponent ],
      imports: [
        TestingModule
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchComponent);
    component = fixture.componentInstance;
    window.history.pushState({}, 'highlighted');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
