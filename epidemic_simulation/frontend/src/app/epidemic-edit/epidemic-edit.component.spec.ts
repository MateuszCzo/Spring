import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpidemicEditComponent } from './epidemic-edit.component';

describe('EpidemicEditComponent', () => {
  let component: EpidemicEditComponent;
  let fixture: ComponentFixture<EpidemicEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EpidemicEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EpidemicEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
